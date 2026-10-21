/*
 * Decompiled with CFR 0.152.
 */
package com.lody.virtual.helper.utils;

import com.lody.virtual.StringFog;
import com.lody.virtual.helper.utils.FileUtils;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Map;
import java.util.Properties;

public class PropertiesUtils {
    private static final char[] hexDigit = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static boolean load(Properties properties, File file) {
        if (properties == null || file == null || !file.exists()) {
            return false;
        }
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            properties.load(inputStream);
        }
        catch (Exception exception) {
            FileUtils.closeQuietly(inputStream);
        }
        catch (Throwable throwable) {
            FileUtils.closeQuietly(inputStream);
            throw throwable;
        }
        FileUtils.closeQuietly(inputStream);
        return true;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static boolean save(Map properties, File file, String comments) {
        if (properties == null || file == null) {
            return false;
        }
        FileOutputStream outputStream = null;
        try {
            if (file.exists()) {
                file.delete();
            } else {
                File dir = file.getParentFile();
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                file.createNewFile();
            }
            outputStream = new FileOutputStream(file);
            PropertiesUtils.store(properties, outputStream, comments);
            FileUtils.closeQuietly(outputStream);
        }
        catch (Exception e) {
            boolean bl = false;
            return bl;
        }
        finally {
            FileUtils.closeQuietly(outputStream);
        }
        return true;
    }

    private static void store(Map properties, OutputStream out, String comments) throws IOException {
        PropertiesUtils.store0(properties, new BufferedWriter(new OutputStreamWriter(out, "8859_1")), comments, true);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static void store0(Map properties, BufferedWriter bw, String comments, boolean escUnicode) throws IOException {
        bw.newLine();
        if (comments != null) {
            PropertiesUtils.writeComments(bw, comments);
        }
        Map map = properties;
        synchronized (map) {
            for (Object k : properties.keySet()) {
                String key = String.valueOf(k);
                String val = String.valueOf(properties.get(k));
                key = PropertiesUtils.saveConvert(key, true, escUnicode);
                val = PropertiesUtils.saveConvert(val, false, escUnicode);
                bw.write(key + "=" + val);
                bw.newLine();
            }
        }
        bw.flush();
    }

    private static char toHex(int nibble) {
        return hexDigit[nibble & 0xF];
    }

    private static String saveConvert(String theString, boolean escapeSpace, boolean escapeUnicode) {
        int len = theString.length();
        int bufLen = len * 2;
        if (bufLen < 0) {
            bufLen = Integer.MAX_VALUE;
        }
        StringBuffer outBuffer = new StringBuffer(bufLen);
        block8: for (int x = 0; x < len; ++x) {
            char aChar = theString.charAt(x);
            if (aChar > '=' && aChar < '\u007f') {
                if (aChar == '\\') {
                    outBuffer.append('\\');
                    outBuffer.append('\\');
                    continue;
                }
                outBuffer.append(aChar);
                continue;
            }
            switch (aChar) {
                case ' ': {
                    if (x == 0 || escapeSpace) {
                        outBuffer.append('\\');
                    }
                    outBuffer.append(' ');
                    continue block8;
                }
                case '\t': {
                    outBuffer.append('\\');
                    outBuffer.append('t');
                    continue block8;
                }
                case '\n': {
                    outBuffer.append('\\');
                    outBuffer.append('n');
                    continue block8;
                }
                case '\r': {
                    outBuffer.append('\\');
                    outBuffer.append('r');
                    continue block8;
                }
                case '\f': {
                    outBuffer.append('\\');
                    outBuffer.append('f');
                    continue block8;
                }
                case '!': 
                case '#': 
                case ':': 
                case '=': {
                    outBuffer.append('\\');
                    outBuffer.append(aChar);
                    continue block8;
                }
                default: {
                    if ((aChar < ' ' || aChar > '~') & escapeUnicode) {
                        outBuffer.append('\\');
                        outBuffer.append('u');
                        outBuffer.append(PropertiesUtils.toHex(aChar >> 12 & 0xF));
                        outBuffer.append(PropertiesUtils.toHex(aChar >> 8 & 0xF));
                        outBuffer.append(PropertiesUtils.toHex(aChar >> 4 & 0xF));
                        outBuffer.append(PropertiesUtils.toHex(aChar & 0xF));
                        continue block8;
                    }
                    outBuffer.append(aChar);
                }
            }
        }
        return outBuffer.toString();
    }

    private static void writeComments(BufferedWriter bw, String comments) throws IOException {
        int current;
        bw.write("#");
        int len = comments.length();
        int last = 0;
        char[] uu = new char[6];
        uu[0] = 92;
        uu[1] = 117;
        for (current = 0; current < len; ++current) {
            char c = comments.charAt(current);
            if (c <= '\u00ff' && c != '\n' && c != '\r') continue;
            if (last != current) {
                bw.write(comments.substring(last, current));
            }
            if (c > '\u00ff') {
                uu[2] = PropertiesUtils.toHex(c >> 12 & 0xF);
                uu[3] = PropertiesUtils.toHex(c >> 8 & 0xF);
                uu[4] = PropertiesUtils.toHex(c >> 4 & 0xF);
                uu[5] = PropertiesUtils.toHex(c & 0xF);
                bw.write(new String(uu));
            } else {
                bw.newLine();
                if (c == '\r' && current != len - 1 && comments.charAt(current + 1) == '\n') {
                    ++current;
                }
                if (current == len - 1 || comments.charAt(current + 1) != '#' && comments.charAt(current + 1) != '!') {
                    bw.write("#");
                }
            }
            last = current + 1;
        }
        if (last != current) {
            bw.write(comments.substring(last, current));
        }
        bw.newLine();
    }
}

