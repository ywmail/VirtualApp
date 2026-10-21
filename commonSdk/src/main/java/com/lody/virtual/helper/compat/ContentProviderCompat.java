/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.ContentProviderClient
 *  android.content.Context
 *  android.net.Uri
 *  android.os.Build$VERSION
 *  android.os.Bundle
 *  android.os.RemoteException
 *  android.os.SystemClock
 */
package com.lody.virtual.helper.compat;

import android.content.ContentProviderClient;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.SystemClock;

public class ContentProviderCompat {
    public static Bundle call(Context context, Uri uri, String method, String arg, Bundle extras, int retryCount) throws IllegalAccessException {
        if (Build.VERSION.SDK_INT < 17) {
            return context.getContentResolver().call(uri, method, arg, extras);
        }
        ContentProviderClient client = ContentProviderCompat.acquireContentProviderClientRetry(context, uri, retryCount);
        try {
            if (client == null) {
                throw new IllegalAccessException();
            }
            Bundle bundle = client.call(method, arg, extras);
            return bundle;
        }
        catch (RemoteException e) {
            throw new IllegalAccessException(e.getMessage());
        }
        finally {
            ContentProviderCompat.releaseQuietly(client);
        }
    }

    private static ContentProviderClient acquireContentProviderClient(Context context, Uri uri) {
        try {
            if (Build.VERSION.SDK_INT >= 16) {
                return context.getContentResolver().acquireUnstableContentProviderClient(uri);
            }
            return context.getContentResolver().acquireContentProviderClient(uri);
        }
        catch (SecurityException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ContentProviderClient acquireContentProviderClientRetry(Context context, Uri uri, int retryCount) {
        ContentProviderClient client = ContentProviderCompat.acquireContentProviderClient(context, uri);
        if (client == null) {
            for (int retry = 0; retry < retryCount && client == null; ++retry) {
                SystemClock.sleep((long)100L);
                client = ContentProviderCompat.acquireContentProviderClient(context, uri);
            }
        }
        return client;
    }

    public static ContentProviderClient acquireContentProviderClientRetry(Context context, String name, int retryCount) {
        ContentProviderClient client = ContentProviderCompat.acquireContentProviderClient(context, name);
        if (client == null) {
            for (int retry = 0; retry < retryCount && client == null; ++retry) {
                SystemClock.sleep((long)100L);
                client = ContentProviderCompat.acquireContentProviderClient(context, name);
            }
        }
        return client;
    }

    private static ContentProviderClient acquireContentProviderClient(Context context, String name) {
        if (Build.VERSION.SDK_INT >= 16) {
            return context.getContentResolver().acquireUnstableContentProviderClient(name);
        }
        return context.getContentResolver().acquireContentProviderClient(name);
    }

    private static void releaseQuietly(ContentProviderClient client) {
        if (client != null) {
            try {
                if (Build.VERSION.SDK_INT >= 24) {
                    client.close();
                } else {
                    client.release();
                }
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
    }
}

