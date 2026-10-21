/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.annotation.TargetApi
 *  android.content.ContentProvider
 *  android.content.ContentProviderClient
 *  android.content.ContentValues
 *  android.content.pm.ProviderInfo
 *  android.database.Cursor
 *  android.net.Uri
 *  android.os.Build$VERSION
 *  android.os.Bundle
 *  android.os.CancellationSignal
 *  android.os.IInterface
 *  android.os.ParcelFileDescriptor
 *  android.os.RemoteException
 */
package com.lody.virtual.client.stub;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentProviderClient;
import android.content.ContentValues;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.IInterface;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.ipc.VActivityManager;
import com.lody.virtual.client.ipc.VPackageManager;
import com.lody.virtual.client.stub.StubManifest;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Locale;
import mirror.android.content.ContentProviderClientICS;
import mirror.android.content.ContentProviderClientJB;

public class ContentProviderProxy
extends ContentProvider {
    public static Uri buildProxyUri(int userId, boolean isExt, String authority, Uri uri) {
        String proxyAuthority = StubManifest.getProxyAuthority(isExt);
        Uri proxyUriPrefix = Uri.parse((String)String.format(Locale.ENGLISH, "content://%1$s/%2$d/%3$s", proxyAuthority, userId, authority));
        return Uri.withAppendedPath((Uri)proxyUriPrefix, (String)uri.toString());
    }

    private TargetProviderInfo getProviderProviderInfo(Uri uri) {
        if (!VirtualCore.get().isEngineLaunched()) {
            return null;
        }
        List segments = uri.getPathSegments();
        if (segments == null || segments.size() < 3) {
            return null;
        }
        int userId = -1;
        try {
            userId = Integer.parseInt((String)segments.get(0));
        }
        catch (NumberFormatException e) {
            e.printStackTrace();
        }
        if (userId == -1) {
            return null;
        }
        String authority = (String)segments.get(1);
        ProviderInfo providerInfo = VPackageManager.get().resolveContentProvider(authority, 0, userId);
        if (providerInfo == null || !providerInfo.enabled) {
            return null;
        }
        String uriContent = uri.toString();
        String targetUriStr = uriContent.substring(authority.length() + uriContent.indexOf(authority, 1) + 1);
        if (targetUriStr.startsWith("content:/") && !targetUriStr.startsWith("content://")) {
            targetUriStr = targetUriStr.replace("content:/", "content://");
        }
        return new TargetProviderInfo(userId, providerInfo, Uri.parse((String)targetUriStr));
    }

    private ContentProviderClient acquireProviderClient(TargetProviderInfo info) {
        try {
            IInterface provider = VActivityManager.get().acquireProviderClient(info.userId, info.info);
            if (provider != null) {
                if (Build.VERSION.SDK_INT > 15) {
                    return ContentProviderClientJB.ctor.newInstance(this.getContext().getContentResolver(), provider, true);
                }
                return ContentProviderClientICS.ctor.newInstance(this.getContext().getContentResolver(), provider);
            }
        }
        catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ContentProviderClient acquireTargetProviderClient(Uri uri) {
        TargetProviderInfo info = this.getProviderProviderInfo(uri);
        if (info != null) {
            return this.acquireProviderClient(info);
        }
        return null;
    }

    public boolean onCreate() {
        return true;
    }

    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        ContentProviderClient client;
        TargetProviderInfo info = this.getProviderProviderInfo(uri);
        if (info != null && (client = this.acquireProviderClient(info)) != null) {
            try {
                return client.query(info.uri, projection, selection, selectionArgs, sortOrder);
            }
            catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public String getType(Uri uri) {
        ContentProviderClient client;
        TargetProviderInfo info = this.getProviderProviderInfo(uri);
        if (info != null && (client = this.acquireProviderClient(info)) != null) {
            try {
                return client.getType(info.uri);
            }
            catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public Uri insert(Uri uri, ContentValues values) {
        ContentProviderClient client;
        TargetProviderInfo info = this.getProviderProviderInfo(uri);
        if (info != null && (client = this.acquireProviderClient(info)) != null) {
            try {
                return client.insert(info.uri, values);
            }
            catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public int delete(Uri uri, String selection, String[] selectionArgs) {
        ContentProviderClient client;
        TargetProviderInfo info = this.getProviderProviderInfo(uri);
        if (info != null && (client = this.acquireProviderClient(info)) != null) {
            try {
                return client.delete(info.uri, selection, selectionArgs);
            }
            catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        ContentProviderClient client;
        TargetProviderInfo info = this.getProviderProviderInfo(uri);
        if (info != null && (client = this.acquireProviderClient(info)) != null) {
            try {
                return client.update(info.uri, values, selection, selectionArgs);
            }
            catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    @TargetApi(value=19)
    public Uri canonicalize(Uri uri) {
        ContentProviderClient client;
        TargetProviderInfo info = this.getProviderProviderInfo(uri);
        if (info != null && (client = this.acquireProviderClient(info)) != null) {
            try {
                return client.canonicalize(info.uri);
            }
            catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @TargetApi(value=19)
    public Uri uncanonicalize(Uri uri) {
        ContentProviderClient client;
        TargetProviderInfo info = this.getProviderProviderInfo(uri);
        if (info != null && (client = this.acquireProviderClient(info)) != null) {
            try {
                return client.uncanonicalize(info.uri);
            }
            catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return uri;
    }

    @TargetApi(value=26)
    public boolean refresh(Uri uri, Bundle args, CancellationSignal cancellationSignal) {
        ContentProviderClient client;
        TargetProviderInfo info = this.getProviderProviderInfo(uri);
        if (info != null && (client = this.acquireProviderClient(info)) != null) {
            try {
                return client.refresh(info.uri, args, cancellationSignal);
            }
            catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public ParcelFileDescriptor openFile(Uri uri, String mode) throws FileNotFoundException {
        ContentProviderClient client;
        TargetProviderInfo info = this.getProviderProviderInfo(uri);
        if (info != null && (client = this.acquireProviderClient(info)) != null) {
            try {
                return client.openFile(info.uri, mode);
            }
            catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public String[] getStreamTypes(Uri uri, String mimeTypeFilter) {
        ContentProviderClient client;
        TargetProviderInfo info = this.getProviderProviderInfo(uri);
        if (info != null && (client = this.acquireProviderClient(info)) != null) {
            try {
                return client.getStreamTypes(info.uri, mimeTypeFilter);
            }
            catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private class TargetProviderInfo {
        int userId;
        ProviderInfo info;
        Uri uri;

        TargetProviderInfo(int userId, ProviderInfo info, Uri uri) {
            this.userId = userId;
            this.info = info;
            this.uri = uri;
        }

        public String toString() {
            return "TargetProviderInfo{userId=" + this.userId + ", info=" + this.info + ", uri=" + this.uri + '}';
        }
    }
}

