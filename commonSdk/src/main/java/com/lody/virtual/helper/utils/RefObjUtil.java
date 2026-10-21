//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.lody.virtual.helper.utils;

import mirror.RefObject;

public class RefObjUtil {
    public RefObjUtil() {
    }

    public static <T> T getRefObjectValue(RefObject ref, Object obj) {
        return ref == null ? null : (T) ref.get(obj);
    }

    public static <T> void setRefObjectValue(RefObject ref, Object obj, T value) {
        if (ref != null) {
            ref.set(obj, value);
        }
    }
}
