/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.SharedPreferences
 */
package com.lody.virtual.sandxposed;

import android.content.SharedPreferences;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.core.VirtualCore;

public class XposedModuleProfile {
    private static SharedPreferences config = VirtualCore.get().getContext().getSharedPreferences("xposed_config", 0);

    public static void enbaleXposed(boolean enbale) {
        config.edit().putBoolean("xposed_enable", enbale).commit();
    }

    public static boolean isXposedEnable() {
        return config.getBoolean("xposed_enable", true);
    }

    public static void enableModule(String pkg, boolean enable) {
        config.edit().putBoolean(pkg, enable).apply();
    }

    public static boolean isModuleEnable(String pkg) {
        return config.getBoolean(pkg, true);
    }
}

