/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Resources
 *  android.graphics.Bitmap
 *  android.graphics.Bitmap$CompressFormat
 *  android.graphics.Bitmap$Config
 *  android.graphics.BitmapFactory
 *  android.graphics.BitmapFactory$Options
 *  android.graphics.Canvas
 *  android.graphics.Matrix
 *  android.graphics.drawable.Drawable
 *  android.media.ExifInterface
 *  android.os.Build$VERSION
 *  android.text.format.DateFormat
 *  android.util.Log
 */
package com.carlos.common.imagepicker.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.os.Build;
import android.text.format.DateFormat;
import android.util.Log;
import com.kook.librelease.StringFog;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Locale;

public class ImageUtil {
    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static String saveImage(Bitmap bitmap, String path) {
        String name = DateFormat.format((CharSequence)"yyyyMMdd_hhmmss", (Calendar)Calendar.getInstance(Locale.CHINA)) + ".png";
        FileOutputStream b = null;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        String fileName = path + File.separator + name;
        try {
            b = new FileOutputStream(fileName);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 75, (OutputStream)b);
            String string2 = fileName;
            return string2;
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (b != null) {
                    b.flush();
                    b.close();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public static Bitmap getBitmap(Context context, int vectorDrawableId) {
        Bitmap bitmap;
        if (Build.VERSION.SDK_INT > 21) {
            Drawable vectorDrawable = context.getDrawable(vectorDrawableId);
            assert (vectorDrawable != null);
            bitmap = Bitmap.createBitmap((int)vectorDrawable.getIntrinsicWidth(), (int)vectorDrawable.getIntrinsicHeight(), (Bitmap.Config)Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            vectorDrawable.draw(canvas);
        } else {
            bitmap = BitmapFactory.decodeResource((Resources)context.getResources(), (int)vectorDrawableId);
        }
        return bitmap;
    }

    public static Bitmap zoomBitmap(Bitmap bm, int reqWidth, int reqHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = (float)reqWidth / (float)width;
        float scaleHeight = (float)reqHeight / (float)height;
        float scale = Math.min(scaleWidth, scaleHeight);
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        Bitmap newbm = Bitmap.createBitmap((Bitmap)bm, (int)0, (int)0, (int)width, (int)height, (Matrix)matrix, (boolean)true);
        return newbm;
    }

    public static Bitmap decodeSampledBitmapFromFile(String pathName, int reqWidth, int reqHeight) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(pathName);
            int result = exifInterface.getAttributeInt("Orientation", 0);
            switch (result) {
                case 6: {
                    degree = 90;
                    break;
                }
                case 3: {
                    degree = 180;
                    break;
                }
                case 8: {
                    degree = 270;
                }
            }
        }
        catch (IOException e) {
            return null;
        }
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile((String)pathName, (BitmapFactory.Options)options);
            options.inSampleSize = ImageUtil.calculateInSampleSize(options, reqWidth, reqHeight);
            options.inJustDecodeBounds = false;
            Bitmap bitmap = BitmapFactory.decodeFile((String)pathName, (BitmapFactory.Options)options);
            if (degree != 0) {
                Bitmap newBitmap = ImageUtil.rotateImageView(bitmap, degree);
                bitmap.recycle();
                bitmap = null;
                return newBitmap;
            }
            return bitmap;
        }
        catch (OutOfMemoryError error) {
            Log.e((String)"eee", (String)"内存泄露！");
            return null;
        }
    }

    public static Bitmap rotateImageView(Bitmap bitmap, int angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate((float)angle);
        return Bitmap.createBitmap((Bitmap)bitmap, (int)0, (int)0, (int)bitmap.getWidth(), (int)bitmap.getHeight(), (Matrix)matrix, (boolean)true);
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int width = options.outWidth;
        int height = options.outHeight;
        int inSampleSize = 1;
        if (width > reqWidth && height > reqHeight) {
            int widthRatio = Math.round((float)width / (float)reqWidth);
            int heightRatio = Math.round((float)height / (float)reqHeight);
            inSampleSize = Math.max(widthRatio, heightRatio);
        }
        return inSampleSize;
    }
}

