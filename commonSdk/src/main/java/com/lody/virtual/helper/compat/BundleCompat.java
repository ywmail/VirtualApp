/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Intent
 *  android.os.Bundle
 *  android.os.IBinder
 *  android.os.Parcel
 */
package com.lody.virtual.helper.compat;

import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import com.lody.virtual.StringFog;
import mirror.android.os.BaseBundle;
import mirror.android.os.BundleICS;

public class BundleCompat {
    public static IBinder getBinder(Bundle bundle, String key) {
        return bundle.getBinder(key);
    }

    public static void putBinder(Bundle bundle, String key, IBinder value) {
        bundle.putBinder(key, value);
    }

    public static void putBinder(Intent intent, String key, IBinder value) {
        Bundle bundle = new Bundle();
        BundleCompat.putBinder(bundle, "binder", value);
        intent.putExtra(key, bundle);
    }

    public static IBinder getBinder(Intent intent, String key) {
        Bundle bundle = intent.getBundleExtra(key);
        if (bundle != null) {
            return BundleCompat.getBinder(bundle, "binder");
        }
        return null;
    }

    public static void clearParcelledData(Bundle bundle) {
        Parcel obtain = Parcel.obtain();
        obtain.writeInt(0);
        obtain.setDataPosition(0);
        if (BaseBundle.TYPE != null) {
            Parcel parcel = BaseBundle.mParcelledData.get(bundle);
            if (parcel != null) {
                parcel.recycle();
            }
            BaseBundle.mParcelledData.set(bundle, obtain);
        } else if (BundleICS.TYPE != null) {
            Parcel parcel = BundleICS.mParcelledData.get(bundle);
            if (parcel != null) {
                parcel.recycle();
            }
            BundleICS.mParcelledData.set(bundle, obtain);
        }
    }
}

