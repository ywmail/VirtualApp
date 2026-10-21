/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.ContentValues
 *  android.database.Cursor
 *  android.net.Uri
 *  android.os.Bundle
 *  android.os.IInterface
 */
package com.lody.virtual.client.hook.providers;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.IInterface;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.hook.base.MethodBox;
import com.lody.virtual.client.hook.providers.ExternalProviderHook;
import com.lody.virtual.helper.utils.VLog;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

class DownloadProviderHook
extends ExternalProviderHook {
    private static final String TAG;
    private static final String COLUMN_NOTIFICATION_PACKAGE;
    private static final String COLUMN_NOTIFICATION_CLASS;
    public static final String COLUMN_IS_VISIBLE_IN_DOWNLOADS_UI;
    public static final String COLUMN_VISIBILITY;
    public static final String COLUMN_DESCRIPTION;
    public static final String COLUMN_FILE_NAME_HINT;

    DownloadProviderHook(IInterface base) {
        super(base);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object ... args) throws Throwable {
        VLog.e("DownloadManager", "call " + method.getName() + " -> " + Arrays.toString(args));
        return super.invoke(proxy, method, args);
    }

    @Override
    public Uri insert(MethodBox methodBox, Uri url, ContentValues initialValues) throws InvocationTargetException {
        VLog.e("DownloadManager", "insert: " + initialValues);
        String notificationPkg = initialValues.getAsString(COLUMN_NOTIFICATION_PACKAGE);
        VLog.e("DownloadManager", "notificationPkg: " + notificationPkg);
        if (notificationPkg == null) {
            return (Uri)methodBox.call();
        }
        initialValues.put(COLUMN_NOTIFICATION_PACKAGE, VirtualCore.get().getHostPkg());
        initialValues.put(COLUMN_VISIBILITY, Integer.valueOf(1));
        String hint = initialValues.getAsString(COLUMN_FILE_NAME_HINT);
        String replaceHint = hint.replace(notificationPkg, VirtualCore.get().getHostPkg());
        initialValues.put(COLUMN_FILE_NAME_HINT, replaceHint);
        return super.insert(methodBox, url, initialValues);
    }

    @Override
    public Cursor query(MethodBox methodBox, Uri url, String[] projection, String selection, String[] selectionArgs, String sortOrder, Bundle originQueryArgs) throws InvocationTargetException {
        VLog.e(TAG, "query : selection: " + selection + ", args: " + Arrays.toString(selectionArgs));
        return super.query(methodBox, url, projection, selection, selectionArgs, sortOrder, originQueryArgs);
    }

    static {
        COLUMN_NOTIFICATION_PACKAGE = "notificationpackage";
        COLUMN_NOTIFICATION_CLASS = "notificationclass";
        COLUMN_IS_VISIBLE_IN_DOWNLOADS_UI = "is_visible_in_downloads_ui";
        COLUMN_VISIBILITY = "visibility";
        COLUMN_DESCRIPTION = "description";
        COLUMN_FILE_NAME_HINT = "hint";
        TAG = DownloadProviderHook.class.getSimpleName();
    }
}

