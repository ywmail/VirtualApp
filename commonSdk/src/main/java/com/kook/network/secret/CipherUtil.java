/*
 * Decompiled with CFR 0.152.
 */
package com.kook.network.secret;

import com.kook.network.StringFog;
import java.nio.charset.Charset;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class CipherUtil {
    public static final String SECRET_KEY = "Hhj2024.08.06-07";
    private static final String ALGORITHM = "AES/ECB/PKCS5Padding";

    public static String decrypt(String secretKey, String cipherText) {
        byte[] encrypted = Base64.getDecoder().decode(cipherText);
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            SecretKeySpec key = new SecretKeySpec(secretKey.getBytes(), "AES");
            cipher.init(2, key);
            return new String(cipher.doFinal(encrypted));
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String encrypt(String secretKey, String plainText) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            SecretKeySpec key = new SecretKeySpec(secretKey.getBytes(), "AES");
            cipher.init(1, key);
            return Base64.getEncoder().encodeToString(cipher.doFinal(plainText.getBytes(Charset.forName("UTF-8"))));
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

