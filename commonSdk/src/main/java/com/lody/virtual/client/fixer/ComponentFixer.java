/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.pm.ApplicationInfo
 *  android.content.pm.ComponentInfo
 *  android.text.TextUtils
 */
package com.lody.virtual.client.fixer;

import android.content.pm.ApplicationInfo;
import android.content.pm.ComponentInfo;
import android.text.TextUtils;

public class ComponentFixer {
    public static String fixComponentClassName(String pkgName, String className) {
        if (className != null) {
            if (className.charAt(0) == '.') {
                return pkgName + className;
            }
            return className;
        }
        return null;
    }

    public static void fixComponentInfo(ComponentInfo info) {
        if (info != null) {
            if (TextUtils.isEmpty((CharSequence)info.processName)) {
                info.processName = info.packageName;
            }
            info.name = ComponentFixer.fixComponentClassName(info.packageName, info.name);
            if (info.processName == null) {
                info.processName = info.applicationInfo.processName;
            }
        }
    }

    public static void fixOutsideComponentInfo(ComponentInfo info) {
        if (info != null) {
            ComponentFixer.fixOutsideApplicationInfo(info.applicationInfo);
        }
    }

    public static void fixOutsideApplicationInfo(ApplicationInfo info) {
        if (info != null) {
            info.uid = 9000;
        }
    }
}

