/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.net.Uri
 *  android.os.Bundle
 *  android.os.Parcelable
 */
package com.lody.virtual.client.ipc;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.helper.compat.ContentProviderCompat;
import java.io.Serializable;

public class ProviderCall {
    public static Bundle callSafely(String authority, String methodName, String arg, Bundle bundle) {
        return ProviderCall.callSafely(authority, methodName, arg, bundle, 3);
    }

    public static Bundle callSafely(String authority, String methodName, String arg, Bundle bundle, int retryCount) {
        try {
            return ProviderCall.call(authority, VirtualCore.get().getContext(), methodName, arg, bundle, retryCount);
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Bundle call(String authority, Context context, String method, String arg, Bundle bundle, int retryCount) throws IllegalAccessException {
        Uri uri = Uri.parse((String)("content://" + authority));
        return ContentProviderCompat.call(context, uri, method, arg, bundle, retryCount);
    }

    public static final class Builder {
        private Context context;
        private Bundle bundle = new Bundle();
        private String method;
        private String auth;
        private String arg;
        private int retryCount = 5;

        public Builder(Context context, String auth) {
            this.context = context;
            this.auth = auth;
        }

        public Builder methodName(String name) {
            this.method = name;
            return this;
        }

        public Builder arg(String arg) {
            this.arg = arg;
            return this;
        }

        public Builder addArg(String key, Object value) {
            if (value != null) {
                if (value instanceof Boolean) {
                    this.bundle.putBoolean(key, ((Boolean)value).booleanValue());
                } else if (value instanceof Integer) {
                    this.bundle.putInt(key, ((Integer)value).intValue());
                } else if (value instanceof String) {
                    this.bundle.putString(key, (String)value);
                } else if (value instanceof Serializable) {
                    this.bundle.putSerializable(key, (Serializable)value);
                } else if (value instanceof Bundle) {
                    this.bundle.putBundle(key, (Bundle)value);
                } else if (value instanceof Parcelable) {
                    this.bundle.putParcelable(key, (Parcelable)value);
                } else if (value instanceof int[]) {
                    this.bundle.putIntArray(key, (int[])value);
                } else {
                    throw new IllegalArgumentException("Unknown type " + value.getClass() + " in Bundle.");
                }
            }
            return this;
        }

        public Builder retry(int retryCount) {
            this.retryCount = retryCount;
            return this;
        }

        public Bundle call() throws IllegalAccessException {
            return ProviderCall.call(this.auth, this.context, this.method, this.arg, this.bundle, this.retryCount);
        }

        public Bundle callSafely() {
            try {
                return this.call();
            }
            catch (IllegalAccessException e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}

