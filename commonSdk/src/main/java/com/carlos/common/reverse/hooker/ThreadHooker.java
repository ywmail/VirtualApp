/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.util.Log
 */
package com.carlos.common.reverse.hooker;

import android.util.Log;
import com.kook.librelease.StringFog;
import com.lody.virtual.client.VClient;
import com.lody.virtual.client.core.VirtualCore;
import com.swift.sandhook.SandHook;
import com.swift.sandhook.annotation.HookClass;
import com.swift.sandhook.annotation.HookMethod;
import com.swift.sandhook.annotation.HookMethodBackup;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@HookClass(value=Thread.class)
public class ThreadHooker {
    static List<String> bypassList = new ArrayList<String>();
    @HookMethodBackup(value="start")
    static Method methodStart;

    @HookMethod(value="start")
    public static void start(Thread thiz) throws Throwable {
        if (VirtualCore.get().isMainProcess()) {
            SandHook.callOriginByBackup(methodStart, thiz, new Object[0]);
            return;
        }
        String packageName = VClient.get().getCurrentPackage();
        String clzName = thiz.getClass().getName();
        Log.e((String)"ThreadHooker", (String)("hooked Thread start packageName:" + packageName + "  thiz:" + thiz + "  clzName:" + clzName));
        thiz.dumpStack();
        if (bypassList.contains(clzName)) {
            thiz.interrupt();
            Log.e((String)"ThreadHooker", (String)(clzName + " hooked pass"));
            return;
        }
        SandHook.callOriginByBackup(methodStart, thiz, new Object[0]);
    }

    static {
        bypassList.add("com.inca.security.iiiIiiiIiI");
        bypassList.add("com.inca.security.iIiIiiiIii");
        bypassList.add("com.inca.security.iIIiiiiIiI");
        bypassList.add("com.inca.security.IIiIiiiIiI");
        bypassList.add("com.inca.security.iiiiiiiIIi");
        bypassList.add("com.inca.security.iIiIiiiiiI");
        bypassList.add("com.inca.security.IiIiiiIiiI");
        bypassList.add("com.inca.security.iiiIiiiiII");
        bypassList.add("com.inca.security.iiiIiiiIii");
        bypassList.add("com.inca.security.iIIIiiiiiI");
        bypassList.add("com.inca.security.iiIIiiiiiI");
        bypassList.add("com.inca.security.IIiiIiIiiI");
        bypassList.add("com.inca.security.IIiiiiiIiI");
        bypassList.add("com.inca.security.IiIIiiiiIi");
        bypassList.add("com.inca.security.IiIIiiiiii");
        bypassList.add("com.inca.security.iIIiiiiiII");
        bypassList.add("com.inca.security.IiiiIiiIii");
        bypassList.add("com.inca.security.IIIIIiiIiI");
        bypassList.add("com.inca.security.IIiIiiiIii");
        bypassList.add("com.inca.security.IIiIiiiIii");
        bypassList.add("com.inca.security.wa");
        bypassList.add("com.inca.security.wk");
        bypassList.add("com.inca.security.rb");
        bypassList.add("com.inca.security.fb");
        bypassList.add("com.inca.security.ll");
        bypassList.add("com.inca.security.ii");
        bypassList.add("com.inca.security.ib");
        bypassList.add("com.inca.security.jb");
        bypassList.add("com.inca.security.wb");
        bypassList.add("com.inca.security.i");
        bypassList.add("com.inca.security.Proxy.AppGuardProxyManager$1");
    }
}

