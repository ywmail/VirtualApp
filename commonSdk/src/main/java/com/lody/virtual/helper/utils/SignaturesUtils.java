/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.pm.Signature
 */
package com.lody.virtual.helper.utils;

import android.content.pm.Signature;
import java.util.ArrayList;

public class SignaturesUtils {
    public static int compareSignatures(Signature[] s1, Signature[] s2) {
        if (s1 == null) {
            return s2 == null ? 1 : -1;
        }
        if (s2 == null) {
            return -2;
        }
        if (s1.length != s2.length) {
            return -3;
        }
        if (s1.length == 1) {
            return s1[0].equals((Object)s2[0]) ? 0 : -3;
        }
        ArrayList<Signature> set1 = new ArrayList<Signature>();
        for (Signature sig : s1) {
            set1.add(sig);
        }
        ArrayList<Signature> set2 = new ArrayList<Signature>();
        for (Signature sig : s2) {
            set2.add(sig);
        }
        if (set1.equals(set2)) {
            return 0;
        }
        return -3;
    }
}

