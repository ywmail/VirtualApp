/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Trace
 */
package com.swift.sandhook.xposedcompat.methodgen;

import android.os.Trace;
import com.swift.sandhook.SandHook;
import com.swift.sandhook.blacklist.HookBlackList;
import com.swift.sandhook.wrapper.HookWrapper;
import com.swift.sandhook.xposedcompat.XposedCompat;
import com.swift.sandhook.xposedcompat.classloaders.ProxyClassLoader;
import com.swift.sandhook.xposedcompat.hookstub.HookMethodEntity;
import com.swift.sandhook.xposedcompat.hookstub.HookStubManager;
import com.swift.sandhook.xposedcompat.methodgen.HookMaker;
import com.swift.sandhook.xposedcompat.methodgen.HookerDexMaker;
import com.swift.sandhook.xposedcompat.methodgen.HookerDexMakerNew;
import com.swift.sandhook.xposedcompat.utils.DexLog;
import com.swift.sandhook.xposedcompat.utils.FileUtils;
import de.robv.android.xposed.XposedBridge;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public final class DynamicBridge {
    private static HookMaker defaultHookMaker = XposedCompat.useNewCallBackup ? new HookerDexMakerNew() : new HookerDexMaker();
    private static final AtomicBoolean dexPathInited = new AtomicBoolean(false);
    private static File dexDir;
    private static final Map<Member, HookMethodEntity> entityMap;
    private static final HashMap<Member, Method> hookedInfo;

    public static synchronized void hookMethod(Member hookMethod, XposedBridge.AdditionalHookInfo additionalHookInfo) {
        if (!DynamicBridge.checkMember(hookMethod)) {
            return;
        }
        if (hookedInfo.containsKey(hookMethod) || entityMap.containsKey(hookMethod)) {
            DexLog.w("already hook method:" + hookMethod.toString());
            return;
        }
        try {
            if (dexPathInited.compareAndSet(false, true)) {
                try {
                    String fixedAppDataDir = XposedCompat.getCacheDir().getAbsolutePath();
                    dexDir = new File(fixedAppDataDir, "/sandxposed/");
                    if (!dexDir.exists()) {
                        dexDir.mkdirs();
                    }
                }
                catch (Throwable throwable) {
                    DexLog.e("error when init dex path", throwable);
                }
            }
            Trace.beginSection((String)"SandHook-Xposed");
            long timeStart = System.currentTimeMillis();
            HookMethodEntity stub = null;
            if (XposedCompat.useInternalStub && !HookBlackList.canNotHookByStub(hookMethod) && !HookBlackList.canNotHookByBridge(hookMethod)) {
                stub = HookStubManager.getHookMethodEntity(hookMethod, additionalHookInfo);
            }
            if (stub != null) {
                SandHook.hook(new HookWrapper.HookEntity(hookMethod, stub.hook, stub.backup, false));
                entityMap.put(hookMethod, stub);
            } else {
                HookMaker hookMaker = HookBlackList.canNotHookByBridge(hookMethod) ? new HookerDexMaker() : defaultHookMaker;
                hookMaker.start(hookMethod, additionalHookInfo, new ProxyClassLoader(DynamicBridge.class.getClassLoader(), hookMethod.getDeclaringClass().getClassLoader()), dexDir == null ? null : dexDir.getAbsolutePath());
                hookedInfo.put(hookMethod, hookMaker.getCallBackupMethod());
            }
            DexLog.d("hook method <" + hookMethod.toString() + "> cost " + (System.currentTimeMillis() - timeStart) + " ms, by " + (stub != null ? "internal stub" : "dex maker"));
            Trace.endSection();
        }
        catch (Throwable e) {
            DexLog.e("error occur when hook method <" + hookMethod.toString() + ">", e);
        }
    }

    public static void clearOatFile() {
        String fixedAppDataDir = XposedCompat.getCacheDir().getAbsolutePath();
        File dexOatDir = new File(fixedAppDataDir, "/sandxposed/oat/");
        if (!dexOatDir.exists()) {
            return;
        }
        try {
            FileUtils.delete(dexOatDir);
            dexOatDir.mkdirs();
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    private static boolean checkMember(Member member) {
        if (member instanceof Method) {
            return true;
        }
        if (member instanceof Constructor) {
            return true;
        }
        if (member.getDeclaringClass().isInterface()) {
            DexLog.e("Cannot hook interfaces: " + member.toString());
            return false;
        }
        if (Modifier.isAbstract(member.getModifiers())) {
            DexLog.e("Cannot hook abstract methods: " + member.toString());
            return false;
        }
        DexLog.e("Only methods and constructors can be hooked: " + member.toString());
        return false;
    }

    static {
        entityMap = new HashMap<Member, HookMethodEntity>();
        hookedInfo = new HashMap();
    }
}

