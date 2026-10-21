/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.ContentResolver
 *  android.content.Context
 *  android.database.Cursor
 *  android.net.Uri
 *  android.provider.MediaStore$Images$Media
 */
package com.carlos.common.imagepicker.model;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import com.carlos.common.imagepicker.entity.Folder;
import com.carlos.common.imagepicker.entity.Image;
import com.carlos.common.imagepicker.utils.StringUtils;
import com.kook.librelease.StringFog;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ImageModel {
    public static void loadImageForSDCard(final Context context, final DataCallback callback) {
        new Thread(new Runnable(){

            @Override
            public void run() {
                Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver mContentResolver = context.getContentResolver();
                Cursor mCursor = mContentResolver.query(mImageUri, new String[]{"_data", "_display_name", "date_added", "_id"}, null, null, "date_added");
                ArrayList<Image> images = new ArrayList<Image>();
                if (mCursor != null) {
                    while (mCursor.moveToNext()) {
                        String path = mCursor.getString(mCursor.getColumnIndex("_data"));
                        String name = mCursor.getString(mCursor.getColumnIndex("_display_name"));
                        long time = mCursor.getLong(mCursor.getColumnIndex("date_added"));
                        if (".downloading".equals(ImageModel.getExtensionName(path))) continue;
                        images.add(new Image(path, time, name));
                    }
                    mCursor.close();
                }
                Collections.reverse(images);
                callback.onSuccess(ImageModel.splitFolder(images));
            }
        }).start();
    }

    private static ArrayList<Folder> splitFolder(ArrayList<Image> images) {
        ArrayList<Folder> folders = new ArrayList<Folder>();
        folders.add(new Folder("全部图片", images));
        if (images != null && !images.isEmpty()) {
            int size = images.size();
            for (int i = 0; i < size; ++i) {
                String path = images.get(i).getPath();
                String name = ImageModel.getFolderName(path);
                if (!StringUtils.isNotEmptyString(name)) continue;
                Folder folder = ImageModel.getFolder(name, folders);
                folder.addImage(images.get(i));
            }
        }
        return folders;
    }

    private static String getExtensionName(String filename) {
        int dot;
        if (filename != null && filename.length() > 0 && (dot = filename.lastIndexOf(46)) > -1 && dot < filename.length() - 1) {
            return filename.substring(dot + 1);
        }
        return "";
    }

    private static String getFolderName(String path) {
        String[] strings;
        if (StringUtils.isNotEmptyString(path) && (strings = path.split(File.separator)).length >= 2) {
            return strings[strings.length - 2];
        }
        return "";
    }

    private static Folder getFolder(String name, List<Folder> folders) {
        if (!folders.isEmpty()) {
            int size = folders.size();
            for (int i = 0; i < size; ++i) {
                Folder folder = folders.get(i);
                if (!name.equals(folder.getName())) continue;
                return folder;
            }
        }
        Folder newFolder = new Folder(name);
        folders.add(newFolder);
        return newFolder;
    }

    public static interface DataCallback {
        public void onSuccess(ArrayList<Folder> var1);
    }
}

