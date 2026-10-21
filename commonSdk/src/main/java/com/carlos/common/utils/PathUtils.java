/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.annotation.SuppressLint
 *  android.content.Context
 *  android.database.Cursor
 *  android.net.Uri
 */
package com.carlos.common.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import com.kook.librelease.StringFog;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class PathUtils {
    public static String getRealPathFromURI(Context context, Uri contentUri) {
        String result = null;
        String[] proj = new String[]{"_data"};
        Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int column_index = cursor.getColumnIndexOrThrow("_data");
                result = cursor.getString(column_index);
            }
            cursor.close();
        }
        return result;
    }

    private File createFileFromInputStream(Context context, InputStream inputStream) throws IOException {
        int length;
        File tempFile = File.createTempFile("tempFile", ".tmp", context.getCacheDir());
        FileOutputStream outputStream = new FileOutputStream(tempFile);
        byte[] buffer = new byte[1024];
        while ((length = inputStream.read(buffer)) != -1) {
            ((OutputStream)outputStream).write(buffer, 0, length);
        }
        ((OutputStream)outputStream).close();
        inputStream.close();
        return tempFile;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @SuppressLint(value={"Range"})
    private String getFileName(Context context, Uri uri) {
        int cut;
        String result = null;
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);){
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex("_display_name"));
                }
            }
        }
        if (result == null && (cut = (result = uri.getPath()).lastIndexOf(47)) != -1) {
            result = result.substring(cut + 1);
        }
        return result;
    }
}

