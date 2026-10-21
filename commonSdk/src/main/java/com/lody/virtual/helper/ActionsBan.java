/*
 * Decompiled with CFR 0.152.
 */
package com.lody.virtual.helper;

import com.lody.virtual.StringFog;
import java.util.HashSet;

public class ActionsBan {
    public static HashSet sActionsBan = new HashSet();

    public static boolean isBanAction(String action) {
        return sActionsBan.contains(action);
    }

    static {
        sActionsBan.add("com.vivo.hotfixcollect");
    }
}

