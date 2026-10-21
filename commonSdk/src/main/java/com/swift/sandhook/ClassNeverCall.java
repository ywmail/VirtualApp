/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.util.Log
 */
package com.swift.sandhook;

import android.util.Log;

public class ClassNeverCall {
    private void neverCall() {
    }

    private void neverCall2() {
        Log.e((String)"ClassNeverCall", (String)"ClassNeverCall2");
    }

    private static void neverCallStatic() {
    }

    private native void neverCallNative();

    private native void neverCallNative2();
}

