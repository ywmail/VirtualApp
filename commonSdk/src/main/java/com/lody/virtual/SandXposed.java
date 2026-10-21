/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.Build$VERSION
 */
package com.lody.virtual;

import android.content.Context;
import android.os.Build;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.helper.compat.BuildCompat;
import com.swift.sandhook.HookLog;
import com.swift.sandhook.SandHook;
import com.swift.sandhook.SandHookConfig;
import com.swift.sandhook.xposedcompat.XposedCompat;
import com.swift.sandhook.xposedcompat.utils.DexMakerUtils;
import java.io.File;
import java.lang.reflect.Member;
import mirror.dalvik.system.VMRuntime;

public class SandXposed {
    public static void init() {
        try {
            SandHookConfig.DEBUG = VMRuntime.isJavaDebuggable == null ? false : VMRuntime.isJavaDebuggable.call(VMRuntime.getRuntime.call(new Object[0]), new Object[0]);
        }
        catch (Throwable e) {
            e.printStackTrace();
        }
        HookLog.DEBUG = false;
        SandHookConfig.SDK_INT = BuildCompat.isS() ? 31 : (BuildCompat.isR() ? 30 : Build.VERSION.SDK_INT);
        SandHookConfig.compiler = SandHookConfig.SDK_INT < 26;
        SandHookConfig.delayHook = false;
        SandHook.setHookModeCallBack(new SandHook.HookModeCallBack(){

            @Override
            public int hookMode(Member member) {
                if (Build.VERSION.SDK_INT >= 30) {
                    return 2;
                }
                return 0;
            }
        });
        SandHook.disableVMInline();
        XposedCompat.cacheDir = new File(VirtualCore.get().getContext().getCacheDir(), "sandhook_cache_general");
    }

    public static void initForXposed(Context context, String processName) {
        XposedCompat.cacheDir = new File(context.getCacheDir(), DexMakerUtils.MD5(processName));
    }
}

