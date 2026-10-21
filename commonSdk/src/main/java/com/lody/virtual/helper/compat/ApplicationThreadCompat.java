/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.IBinder
 *  android.os.IInterface
 */
package com.lody.virtual.helper.compat;

import android.os.IBinder;
import android.os.IInterface;
import com.lody.virtual.helper.compat.BuildCompat;
import mirror.android.app.ApplicationThreadNative;
import mirror.android.app.IApplicationThreadOreo;

public class ApplicationThreadCompat {
    public static IInterface asInterface(IBinder binder) {
        if (BuildCompat.isOreo()) {
            return IApplicationThreadOreo.Stub.asInterface.call(binder);
        }
        return ApplicationThreadNative.asInterface.call(binder);
    }
}

