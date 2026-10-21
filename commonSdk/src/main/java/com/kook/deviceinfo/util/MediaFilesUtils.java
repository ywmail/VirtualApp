/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.ContentResolver
 *  android.content.Context
 *  android.database.Cursor
 *  android.net.Uri
 *  android.os.Environment
 *  android.provider.MediaStore$Audio$Media
 *  android.provider.MediaStore$Images$Media
 *  android.provider.MediaStore$Video$Media
 *  androidx.core.app.ActivityCompat
 */
package com.kook.deviceinfo.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.core.app.ActivityCompat;
import com.kook.deviceinfo.DeviceApplication;
import java.io.File;

public class MediaFilesUtils {
    public static boolean isSDCardEnableByEnvironment() {
        return "mounted".equals(Environment.getExternalStorageState());
    }

    public static int getImagesInternal() {
        String[] projection = new String[]{"_data"};
        int count = MediaFilesUtils.getDataCount(MediaStore.Images.Media.INTERNAL_CONTENT_URI, projection);
        return count;
    }

    public static int getImagesExternal() {
        String[] projection = new String[]{"_data"};
        int count = MediaFilesUtils.getDataCount(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection);
        return count;
    }

    public static int getAudioInternal() {
        String[] projection = new String[]{"_data"};
        int count = MediaFilesUtils.getDataCount(MediaStore.Audio.Media.INTERNAL_CONTENT_URI, projection);
        return count;
    }

    public static int getAudioExternal() {
        String[] projection = new String[]{"_data"};
        int count = MediaFilesUtils.getDataCount(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection);
        return count;
    }

    public static int getVideoInternal() {
        String[] projection = new String[]{"_data"};
        int count = MediaFilesUtils.getDataCount(MediaStore.Video.Media.INTERNAL_CONTENT_URI, projection);
        return count;
    }

    public static int getVideoExternal() {
        String[] projection = new String[]{"_data"};
        int count = MediaFilesUtils.getDataCount(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection);
        return count;
    }

    public static int getDownloadFilesCount() {
        try {
            return Environment.getExternalStoragePublicDirectory((String)Environment.DIRECTORY_DOWNLOADS).listFiles().length;
        }
        catch (Exception ignored) {
            return -1;
        }
    }

    public static int getDataCount(Uri uri, String[] projection) {
        if (ActivityCompat.checkSelfPermission((Context)DeviceApplication.getApp(), (String)"android.permission.READ_EXTERNAL_STORAGE") != 0) {
            return 0;
        }
        int count = 0;
        ContentResolver contentResolver = DeviceApplication.getApp().getContentResolver();
        Cursor cursor = contentResolver.query(uri, projection, null, null, null);
        if (cursor != null) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    private static String getAbsolutePath(File file) {
        if (file == null) {
            return "";
        }
        return file.getAbsolutePath();
    }
}

