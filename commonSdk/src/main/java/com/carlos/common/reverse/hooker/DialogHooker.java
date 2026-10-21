/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Dialog
 *  android.util.Log
 */
package com.carlos.common.reverse.hooker;

import android.app.Dialog;
import android.util.Log;
import com.kook.librelease.StringFog;
import com.lody.virtual.client.VClient;
import com.lody.virtual.client.core.VirtualCore;
import com.swift.sandhook.SandHook;
import com.swift.sandhook.annotation.HookClass;
import com.swift.sandhook.annotation.HookMethod;
import com.swift.sandhook.annotation.HookMethodBackup;
import java.lang.reflect.Method;

@HookClass(value=Dialog.class)
public class DialogHooker {
    @HookMethodBackup(value="show")
    static Method showBackup;

    @HookMethod(value="show")
    public static void show(Dialog thiz) throws Throwable {
        if (VirtualCore.get().isMainProcess()) {
            SandHook.callOriginByBackup(showBackup, thiz, new Object[0]);
            return;
        }
        String packageName = VClient.get().getCurrentPackage();
        String clzName = thiz.getClass().getName();
        Log.e((String)"DialogHooker", (String)(packageName + " Dialog show " + thiz));
        thiz.cancel();
    }
}

