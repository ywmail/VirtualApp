/*
 * Decompiled with CFR 0.152.
 */
package com.swift.sandhook;

import com.swift.sandhook.HookLog;
import com.swift.sandhook.SandHook;
import com.swift.sandhook.SandHookConfig;
import com.swift.sandhook.wrapper.HookErrorException;
import com.swift.sandhook.wrapper.HookWrapper;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

public class PendingHookHandler {
    private static Map<Class, Vector<HookWrapper.HookEntity>> pendingHooks = new ConcurrentHashMap<Class, Vector<HookWrapper.HookEntity>>();
    private static boolean canUsePendingHook;

    public static boolean canWork() {
        return canUsePendingHook && SandHook.canGetObject();
    }

    public static synchronized void addPendingHook(HookWrapper.HookEntity hookEntity) {
        Vector<HookWrapper.HookEntity> entities = pendingHooks.get(hookEntity.target.getDeclaringClass());
        if (entities == null) {
            entities = new Vector();
            pendingHooks.put(hookEntity.target.getDeclaringClass(), entities);
        }
        entities.add(hookEntity);
        SandHook.addPendingHookNative(hookEntity.target);
    }

    public static void onClassInit(long clazz_ptr) {
        if (clazz_ptr == 0L) {
            return;
        }
        Class clazz = (Class)SandHook.getObject(clazz_ptr);
        if (clazz == null) {
            return;
        }
        Vector<HookWrapper.HookEntity> entities = pendingHooks.get(clazz);
        if (entities == null) {
            return;
        }
        for (HookWrapper.HookEntity entity : entities) {
            HookLog.w("do pending hook for method: " + entity.target.toString());
            try {
                entity.initClass = false;
                SandHook.hook(entity);
            }
            catch (HookErrorException e) {
                HookLog.e("Pending Hook Error!", e);
            }
        }
        pendingHooks.remove(clazz);
    }

    static {
        if (SandHookConfig.delayHook) {
            canUsePendingHook = SandHook.initForPendingHook();
        }
    }
}

