/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.Binder
 */
package com.kook.common.systemutil;

import android.content.Context;
import android.os.Binder;

public class SystemManager {
    public static boolean isSystemSign(Context context) {
        return context.getPackageManager().checkSignatures(Binder.getCallingUid(), 1000) == 0;
    }
}

