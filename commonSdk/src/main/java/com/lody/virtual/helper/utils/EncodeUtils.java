/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.util.Base64
 */
package com.lody.virtual.helper.utils;

import android.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class EncodeUtils {
    private static final Map<String, String> sStringPool = new HashMap<String, String>();

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static String decodeBase64(String base64) {
        Map<String, String> map = sStringPool;
        synchronized (map) {
            String decode;
            if (!sStringPool.containsKey(base64)) {
                decode = new String(Base64.decode((String)base64, (int)0));
                sStringPool.put(base64, decode);
            } else {
                decode = sStringPool.get(base64);
            }
            return decode;
        }
    }
}

