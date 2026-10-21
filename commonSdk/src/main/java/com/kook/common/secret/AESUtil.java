/*
 * Decompiled with CFR 0.152.
 */
package com.kook.common.secret;

import com.kook.common.utils.HVLog;
import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class AESUtil {
    private static final String SECRET_KEY = "sesr1107";

    public static void main(String[] args) {
        String content = "";
        String key = "20171117";
        System.currentTimeMillis();
        System.out.println("\u52a0\u5bc6\u524d\uff1a" + content);
        byte[] encrypted = AESUtil.desEncrypt(content.getBytes(), key.getBytes());
        System.out.println("\u52a0\u5bc6\u540e\uff1a" + AESUtil.byteToHexString(encrypted));
        byte[] decrypted = AESUtil.desDecrypt(encrypted, key.getBytes());
        System.out.println("\u89e3\u5bc6\u540e\uff1a" + new String(decrypted));
    }

    public static String desEncrypt(String content) {
        byte[] desEncrypt = AESUtil.desEncrypt(content.getBytes(), SECRET_KEY.getBytes());
        return AESUtil.byteToHexString(desEncrypt);
    }

    private static byte[] desEncrypt(byte[] content, byte[] keyBytes) {
        try {
            DESKeySpec keySpec = new DESKeySpec(keyBytes);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey key = keyFactory.generateSecret(keySpec);
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            cipher.init(1, (Key)key, new IvParameterSpec(keySpec.getKey()));
            byte[] result = cipher.doFinal(content);
            return result;
        }
        catch (Exception e) {
            System.out.println("exception:" + e.toString());
            return null;
        }
    }

    public static String desDecrypt(String content) {
        HVLog.e("VA-DSD", " content  111  :" + content);
        byte[] bytes = AESUtil.hexStringToByteArray(content);
        byte[] desDecrypt = AESUtil.desDecrypt(bytes, SECRET_KEY.getBytes());
        if (desDecrypt == null) {
            return null;
        }
        String time = new String(desDecrypt);
        return time;
    }

    public static String desDecrypt(byte[] content) {
        byte[] desDecrypt = AESUtil.desDecrypt(content, SECRET_KEY.getBytes());
        return new String(desDecrypt);
    }

    private static byte[] desDecrypt(byte[] content, byte[] keyBytes) {
        try {
            DESKeySpec keySpec = new DESKeySpec(keyBytes);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey key = keyFactory.generateSecret(keySpec);
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            cipher.init(2, (Key)key, new IvParameterSpec(keyBytes));
            byte[] result = cipher.doFinal(content);
            return result;
        }
        catch (Exception e) {
            HVLog.e("VA-DSD", " desDecrypt exception  :" + e.toString());
            return null;
        }
    }

    private static String byteToHexString(byte[] bytes) {
        StringBuffer sb = new StringBuffer(bytes.length);
        for (int i = 0; i < bytes.length; ++i) {
            String sTemp = Integer.toHexString(0xFF & bytes[i]);
            if (sTemp.length() < 2) {
                sb.append(0);
            }
            sb.append(sTemp);
        }
        return sb.toString();
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] b = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            b[i / 2] = (byte)((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
        }
        return b;
    }
}

