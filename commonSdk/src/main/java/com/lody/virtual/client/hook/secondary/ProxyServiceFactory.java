/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.ComponentName
 *  android.content.Context
 *  android.os.IBinder
 */
package com.lody.virtual.client.hook.secondary;

import android.content.ComponentName;
import android.content.Context;
import android.os.IBinder;
import java.util.HashMap;
import java.util.Map;

public class ProxyServiceFactory {
    private static final String TAG = ProxyServiceFactory.class.getSimpleName();
    private static Map<String, ServiceFetcher> sHookSecondaryServiceMap = new HashMap<String, ServiceFetcher>();

    public static IBinder getProxyService(Context context, ComponentName component, IBinder binder) {
        if (context == null || binder == null) {
            return null;
        }
        try {
            IBinder res;
            String description = binder.getInterfaceDescriptor();
            ServiceFetcher fetcher = sHookSecondaryServiceMap.get(description);
            if (fetcher != null && (res = fetcher.getService(context, context.getClassLoader(), binder)) != null) {
                return res;
            }
        }
        catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    private static interface ServiceFetcher {
        public IBinder getService(Context var1, ClassLoader var2, IBinder var3);
    }
}

