/*
 * Decompiled with CFR 0.152.
 */
package com.lody.virtual.sandxposed;

import com.lody.virtual.StringFog;
import java.util.HashSet;
import java.util.Set;

public class BlackList {
    private static Set<String> packageList = new HashSet<String>();
    private static Set<String> processList = new HashSet<String>();

    public static boolean canNotInject(String packageName, String processName) {
        return packageList.contains(packageName) || processList.contains(processName);
    }

    static {
        processList.add("com.tencent.mm:push");
    }
}

