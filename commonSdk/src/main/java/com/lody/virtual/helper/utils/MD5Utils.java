/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.text.TextUtils
 *  android.util.Base64
 */
package com.lody.virtual.helper.utils;

import android.text.TextUtils;
import android.util.Base64;
import com.lody.virtual.StringFog;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {
    protected static char[] HEX_DIGITS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    protected static MessageDigest MESSAGE_DIGEST_5 = null;

    public static String getFileMD5String(File file) throws IOException {
        int numRead;
        FileInputStream fis = new FileInputStream(file);
        byte[] buffer = new byte[1024];
        while ((numRead = ((InputStream)fis).read(buffer)) > 0) {
            MESSAGE_DIGEST_5.update(buffer, 0, numRead);
        }
        ((InputStream)fis).close();
        return MD5Utils.bufferToHex(MESSAGE_DIGEST_5.digest());
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static String hashBase64(byte[] bs) {
        ByteArrayInputStream in = new ByteArrayInputStream(bs);
        MessageDigest SHA = null;
        try {
            int numRead;
            SHA = MessageDigest.getInstance("SHA-1");
            byte[] buffer = new byte[1024];
            while ((numRead = in.read(buffer)) > 0) {
                SHA.update(buffer, 0, numRead);
            }
        }
        catch (Exception e) {
            String string2 = null;
            return string2;
        }
        finally {
            try {
                in.close();
            }
            catch (IOException iOException) {}
        }
        return Base64.encodeToString((byte[])SHA.digest(), (int)0);
    }

    public static String getFileMD5String(InputStream in) throws IOException {
        int numRead;
        byte[] buffer = new byte[1024];
        while ((numRead = in.read(buffer)) > 0) {
            MESSAGE_DIGEST_5.update(buffer, 0, numRead);
        }
        in.close();
        return MD5Utils.bufferToHex(MESSAGE_DIGEST_5.digest());
    }

    private static String bufferToHex(byte[] bytes) {
        return MD5Utils.bufferToHex(bytes, 0, bytes.length);
    }

    private static String bufferToHex(byte[] bytes, int m, int n) {
        StringBuffer stringbuffer = new StringBuffer(2 * n);
        int k = m + n;
        for (int l = m; l < k; ++l) {
            MD5Utils.appendHexPair(bytes[l], stringbuffer);
        }
        return stringbuffer.toString();
    }

    private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
        char c0 = HEX_DIGITS[(bt & 0xF0) >> 4];
        char c1 = HEX_DIGITS[bt & 0xF];
        stringbuffer.append(c0);
        stringbuffer.append(c1);
    }

    public static boolean compareFiles(File one, File two) throws IOException {
        if (one.getAbsolutePath().equals(two.getAbsolutePath())) {
            return true;
        }
        String md5_1 = MD5Utils.getFileMD5String(one);
        String md5_2 = MD5Utils.getFileMD5String(two);
        return TextUtils.equals((CharSequence)md5_1, (CharSequence)md5_2);
    }

    static {
        try {
            MESSAGE_DIGEST_5 = MessageDigest.getInstance("MD5");
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}

