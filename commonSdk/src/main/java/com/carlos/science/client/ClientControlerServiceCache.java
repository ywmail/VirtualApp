/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.annotation.SuppressLint
 *  android.os.IBinder
 */
package com.carlos.science.client;

import android.annotation.SuppressLint;
import android.os.IBinder;
import com.carlos.libcommon.StringFog;
import com.carlos.science.client.hyxd.HYXDControllerImpl;
import com.carlos.science.client.normal.LearnControllerImpl;
import java.util.HashMap;
import java.util.Map;

public class ClientControlerServiceCache {
    @SuppressLint(value={"NewApi"})
    private static final Map<String, IBinder> sCache = new HashMap<String, IBinder>(5);

    public static void addService(String name, IBinder service) {
        sCache.put(name, service);
    }

    public static IBinder removeService(String name) {
        return sCache.remove(name);
    }

    public static IBinder getService(String name) {
        if (sCache.containsKey(name)) {
            return sCache.get(name);
        }
        return sCache.get("def");
    }

    static {
        ClientControlerServiceCache.addService("def", (IBinder)LearnControllerImpl.get());
        ClientControlerServiceCache.addService("com.netease.hyxd.ewan", (IBinder)HYXDControllerImpl.get());
    }
}

