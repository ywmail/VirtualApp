/*
 * Decompiled with CFR 0.152.
 */
package com.lody.virtual.client.core;

import com.lody.virtual.client.hook.base.BinderInvocationStub;
import java.util.HashMap;
import java.util.Map;

public class ServiceLocalManager {
    private static final Map<String, BinderInvocationStub> sCache = new HashMap<String, BinderInvocationStub>();

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void addService(String name, BinderInvocationStub proxy) {
        Map<String, BinderInvocationStub> map = sCache;
        synchronized (map) {
            sCache.put(name, proxy);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static BinderInvocationStub getService(String name) {
        BinderInvocationStub proxy;
        Map<String, BinderInvocationStub> map = sCache;
        synchronized (map) {
            proxy = sCache.get(name);
        }
        return proxy;
    }
}

