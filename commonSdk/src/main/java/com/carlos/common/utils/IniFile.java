/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Environment
 */
package com.carlos.common.utils;

import android.os.Environment;
import com.kook.common.utils.HVLog;
import com.kook.librelease.StringFog;
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
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class IniFile {
    static String STORAGE = Environment.getExternalStorageDirectory() + File.separator + "scorpion/";
    private String line_separator = "\n";
    private String charSet = "UTF-8";
    private Map<String, Section> sections = new LinkedHashMap<String, Section>();
    private File file = null;
    private static IniFile mIniFile = new IniFile(new File(STORAGE + "scorpion.ini"));

    public void setLineSeparator(String line_separator) {
        this.line_separator = line_separator;
    }

    public void setCharSet(String charSet) {
        this.charSet = charSet;
    }

    public void set(String section, String key, Object value) {
        Section sectionObject = this.sections.get(section);
        if (sectionObject == null) {
            sectionObject = new Section();
        }
        sectionObject.name = section;
        sectionObject.set(key, value);
        this.sections.put(section, sectionObject);
    }

    public Section get(String section) {
        return this.sections.get(section);
    }

    public Object get(String section, String key) {
        return this.get(section, key, null);
    }

    public Object get(String section, String key, String defaultValue) {
        Section sectionObject = this.sections.get(section);
        if (sectionObject != null) {
            Object value = sectionObject.get(key);
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

    public static IniFile getInstance() {
        return mIniFile;
    }

    private IniFile(File file) {
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
            HVLog.printException(e);
        }
    }

    public void save() {
        this.save(this.file);
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
            if (!file.exists()) {
                File storage = new File(STORAGE);
                if (!storage.exists()) {
                    boolean mkdirs = storage.mkdirs();
                    HVLog.e("IniFile storage : " + mkdirs);
                }
                boolean fileNewFile = file.createNewFile();
                HVLog.e("IniFile  fileNewFile:" + fileNewFile);
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
                for (Map.Entry<String, Object> entry : section.getValues().entrySet()) {
                    bufferedWriter.write(entry.getKey());
                    bufferedWriter.write("=");
                    bufferedWriter.write(entry.getValue().toString());
                    if (line_spe) {
                        bufferedWriter.write(this.line_separator);
                        continue;
                    }
                    bufferedWriter.newLine();
                }
            }
            bufferedWriter.close();
        }
        catch (Exception e) {
            HVLog.printException(e);
        }
    }

    public class Section {
        private String name;
        private Map<String, Object> values = new LinkedHashMap<String, Object>();

        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void set(String key, Object value) {
            this.values.put(key, value);
        }

        public Object get(String key) {
            return this.values.get(key);
        }

        public Map<String, Object> getValues() {
            return this.values;
        }
    }
}

