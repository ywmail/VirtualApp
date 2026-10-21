/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.text.TextUtils
 */
package com.kook.deviceinfo.persistence;

import android.text.TextUtils;
import com.kook.common.utils.HVLog;
import com.kook.deviceinfo.persistence.VEnvironment;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class IniFile {
    public static String SYSTEM_EXPORT_CONFIG = VEnvironment.get().getPersisteceDataPath() + "export_config.ini";
    private File file = null;
    private String line_separator = "\n";
    private String charSet = "UTF-8";
    private Map<String, Section> sections = new LinkedHashMap<String, Section>();

    public static IniFile getInstance(String file) {
        File storage = new File(file);
        try {
            storage.createNewFile();
        }
        catch (IOException iOException) {
            // empty catch block
        }
        IniFile mIniFile = new IniFile(storage);
        return mIniFile;
    }

    public File getFile() {
        return this.file;
    }

    public void setLineSeparator(String line_separator) {
        this.line_separator = line_separator;
    }

    public void setCharSet(String charSet) {
        this.charSet = charSet;
    }

    public void set(String section, String key, String value) {
        Section sectionObject = this.sections.get(section);
        if (sectionObject == null) {
            sectionObject = new Section();
        }
        sectionObject.name = section;
        sectionObject.set(key, value);
        this.sections.put(section, sectionObject);
    }

    public void set(String section, String key, Object objectValue) {
        if (!(objectValue instanceof Integer || objectValue instanceof Long || objectValue instanceof Float)) {
            throw new ClassCastException(objectValue + "\u4e0d\u80fd\u8f6c\u7816\u6210String");
        }
        String value = String.valueOf(objectValue);
        Section sectionObject = this.sections.get(section);
        if (sectionObject == null) {
            sectionObject = new Section();
        }
        sectionObject.name = section;
        sectionObject.set(key, value);
        this.sections.put(section, sectionObject);
    }

    public void setList(String section, String key, List<String> listValue) {
        Section sectionObject = this.sections.get(section);
        if (sectionObject == null) {
            sectionObject = new Section();
        }
        sectionObject.name = section;
        sectionObject.set(key, String.valueOf(listValue.size()));
        int index = 0;
        for (String value : listValue) {
            sectionObject.set(key + index, value);
            ++index;
        }
        this.sections.put(section, sectionObject);
    }

    public Section get(String section) {
        return this.sections.get(section);
    }

    public String get(String section, String key) {
        return this.get(section, key, null);
    }

    public List<String> getList(String section, String key) {
        Section sectionObject = this.sections.get(section);
        ArrayList<String> listData = null;
        if (sectionObject != null) {
            listData = new ArrayList<String>();
            int listcount = (Integer)sectionObject.get(key);
            for (int i = 0; i < listcount; ++i) {
                String valueItem = (String)sectionObject.get(key + i);
                listData.add(valueItem);
            }
        }
        return listData;
    }

    public String get(String section, String key, String defaultValue) {
        Section sectionObject = this.sections.get(section);
        if (sectionObject != null) {
            String value = (String)sectionObject.get(key);
            if (value == null || value.toString().trim().equals("")) {
                return defaultValue;
            }
            return value;
        }
        return null;
    }

    public void remove(String section) {
        this.sections.remove(section);
    }

    public void remove(String section, String key) {
        Section sectionObject = this.sections.get(section);
        if (sectionObject != null) {
            sectionObject.getValues().remove(key);
        }
    }

    public IniFile(File file) {
        this.file = file;
        this.initFromFile(file);
    }

    public IniFile(InputStream inputStream) {
        this.initFromInputStream(inputStream);
    }

    public void load(File file) {
        this.file = file;
        this.initFromFile(file);
    }

    public void load(InputStream inputStream) {
        this.initFromInputStream(inputStream);
    }

    public void save(OutputStream outputStream) {
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, this.charSet));
            this.saveConfig(bufferedWriter);
        }
        catch (UnsupportedEncodingException e) {
            HVLog.printException(e);
        }
    }

    private void save(File file) {
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
            this.saveConfig(bufferedWriter);
        }
        catch (IOException e) {
            HVLog.e("\u6587\u4ef6" + file.getAbsolutePath() + "\u8bfb\u5199\u5f02\u5e38");
            HVLog.printException(e);
        }
    }

    public File save() {
        this.save(this.file);
        return this.file;
    }

    private void initFromInputStream(InputStream inputStream) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, this.charSet));
            this.toIniFile(bufferedReader);
        }
        catch (UnsupportedEncodingException e) {
            HVLog.printException(e);
        }
    }

    private void initFromFile(File file) {
        try {
            File storage;
            HVLog.e("initFromFile file:" + file.getAbsolutePath() + "    file:" + file.exists());
            if (!file.exists() && !(storage = new File(file.getAbsolutePath())).exists()) {
                boolean fileNewFile = file.createNewFile();
                HVLog.e("IniConfig storage : " + fileNewFile);
            }
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            this.toIniFile(bufferedReader);
        }
        catch (FileNotFoundException e) {
            HVLog.printException(e);
        }
        catch (IOException e) {
            HVLog.printException(e);
        }
    }

    private void toIniFile(BufferedReader bufferedReader) {
        Section section = null;
        Pattern p = Pattern.compile("^\\[.*\\]$");
        try {
            String strLine;
            while ((strLine = bufferedReader.readLine()) != null) {
                if (p.matcher(strLine).matches()) {
                    strLine = strLine.trim();
                    section = new Section();
                    section.name = strLine.substring(1, strLine.length() - 1);
                    this.sections.put(section.name, section);
                    continue;
                }
                String[] keyValue = strLine.split("=");
                if (keyValue.length != 2) continue;
                section.set(keyValue[0], keyValue[1]);
            }
            bufferedReader.close();
        }
        catch (IOException e) {
            HVLog.printException(e);
        }
    }

    private void saveConfig(BufferedWriter bufferedWriter) {
        try {
            boolean line_spe = false;
            if (this.line_separator == null || this.line_separator.trim().equals("")) {
                line_spe = true;
            }
            for (Section section : this.sections.values()) {
                bufferedWriter.write("[" + section.getName() + "]");
                if (line_spe) {
                    bufferedWriter.write(this.line_separator);
                } else {
                    bufferedWriter.newLine();
                }
                for (Map.Entry<String, String> entry : section.getValues().entrySet()) {
                    String key = entry.getKey();
                    if (TextUtils.isEmpty((CharSequence)key)) continue;
                    bufferedWriter.write(entry.getKey());
                    bufferedWriter.write("=");
                    String value = entry.getValue();
                    if (value != null) {
                        bufferedWriter.write(value.toString());
                    }
                    if (line_spe) {
                        bufferedWriter.write(this.line_separator);
                        continue;
                    }
                    bufferedWriter.newLine();
                }
            }
            bufferedWriter.flush();
            bufferedWriter.close();
        }
        catch (Exception e) {
            HVLog.printException(e);
        }
    }

    public class Section {
        private String name;
        private Map<String, String> values = new LinkedHashMap<String, String>();

        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void set(String key, String value) {
            this.values.put(key, value);
        }

        public Object get(String key) {
            return this.values.get(key);
        }

        public Map<String, String> getValues() {
            return this.values;
        }
    }
}

