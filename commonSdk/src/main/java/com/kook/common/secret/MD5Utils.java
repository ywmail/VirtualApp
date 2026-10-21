/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.text.TextUtils
 */
package com.kook.common.secret;

import android.text.TextUtils;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.GZIPInputStream;

public class MD5Utils {
    public static InputStream ungzipInputStream(InputStream input, boolean gzip) throws IOException {
        FilterInputStream is = null;
        if (input != null) {
            if (gzip) {
                is = new GZIPInputStream(input);
                BufferedInputStream bis = new BufferedInputStream(is);
                bis.mark(2);
                byte[] header = new byte[2];
                int result = bis.read(header);
                bis.reset();
                int ss = header[0] & 0xFF | (header[1] & 0xFF) << 8;
                is = result != -1 && ss == 35615 ? new GZIPInputStream(bis) : bis;
            } else {
                is = new BufferedInputStream(input);
            }
        }
        return is;
    }

    public static String md5Encode(String inStr) {
        MessageDigest md5 = null;
        StringBuffer hexValue = new StringBuffer();
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] byteArray = inStr.getBytes("UTF-8");
            byte[] md5Bytes = md5.digest(byteArray);
            String content = MD5Utils.binToHex(md5Bytes);
            return content;
        }
        catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
            return "";
        }
    }

    public static String binToHex(byte[] md) {
        StringBuffer sb = new StringBuffer("");
        int read = 0;
        for (int i = 0; i < md.length; ++i) {
            read = md[i];
            if (read < 0) {
                read += 256;
            }
            if (read < 16) {
                sb.append("0");
            }
            sb.append(Integer.toHexString(read));
        }
        return sb.toString();
    }

    public static String fileMD5Sync(String filePath) {
        if (TextUtils.isEmpty((CharSequence)filePath)) {
            return "";
        }
        return MD5Utils.fileMD5Sync(new File(filePath));
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static String fileMD5Sync(File file) {
        if (file != null && file.exists()) {
            FileInputStream fis = null;
            try {
                MessageDigest messageDigest = MessageDigest.getInstance("MD5");
                fis = new FileInputStream(file);
                MappedByteBuffer byteBuffer = fis.getChannel().map(FileChannel.MapMode.READ_ONLY, 0L, file.length());
                messageDigest.update(byteBuffer);
                BigInteger bigInt = new BigInteger(1, messageDigest.digest());
                String md5 = bigInt.toString(16);
                while (md5.length() < 32) {
                    md5 = "0" + md5;
                }
                String string2 = md5;
                return string2;
            }
            catch (IOException | NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            finally {
                if (fis != null) {
                    try {
                        fis.close();
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return "";
    }
}

