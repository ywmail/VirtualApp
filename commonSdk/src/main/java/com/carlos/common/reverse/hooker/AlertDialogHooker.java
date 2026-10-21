/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.AlertDialog
 *  android.util.Log
 */
package com.carlos.common.reverse.hooker;

import android.app.AlertDialog;
import android.util.Log;
import com.kook.librelease.StringFog;
import com.lody.virtual.client.core.VirtualCore;
import com.swift.sandhook.SandHook;
import com.swift.sandhook.annotation.HookClass;
import com.swift.sandhook.annotation.HookMethod;
import com.swift.sandhook.annotation.HookMethodBackup;
import com.swift.sandhook.annotation.MethodParams;
import java.lang.reflect.Method;

@HookClass(value=AlertDialog.class)
public class AlertDialogHooker {
    @HookMethodBackup(value="setMessage")
    static Method setMessageBackup;

    @HookMethod(value="setMessage")
    @MethodParams(value={CharSequence.class})
    public static void setMessage(AlertDialog thiz, CharSequence msg) throws Throwable {
        if (VirtualCore.get().isMainProcess()) {
            SandHook.callOriginByBackup(setMessageBackup, thiz, msg);
            return;
        }
        if (msg != null) {
            Log.d((String)"AlertDialogHooker", (String)msg.toString());
            if (msg.toString().contains("維護")) {
                thiz.cancel();
            }
        }
        SandHook.callOriginByBackup(setMessageBackup, thiz, msg);
    }
}

