/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.graphics.Bitmap
 *  android.graphics.BitmapFactory$Options
 *  android.graphics.Canvas
 *  android.graphics.Matrix
 *  android.graphics.Point
 *  android.net.Uri
 *  android.util.Log
 *  android.view.Display
 *  android.view.WindowManager
 *  androidx.annotation.NonNull
 *  androidx.annotation.Nullable
 */
package com.carlos.common.imagepicker.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.net.Uri;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.carlos.common.imagepicker.callback.BitmapLoadCallback;
import com.carlos.common.imagepicker.task.BitmapLoadTask;
import com.carlos.common.imagepicker.util.EglUtils;
import com.carlos.common.imagepicker.util.ImageHeaderParser;
import com.kook.librelease.StringFog;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

public class BitmapLoadUtils {
    private static final String TAG = "BitmapLoadUtils";

    public static void decodeBitmapInBackground(@NonNull Context context, @NonNull Uri uri, @Nullable Uri outputUri, int requiredWidth, int requiredHeight, BitmapLoadCallback loadCallback) {
        new BitmapLoadTask(context, uri, outputUri, requiredWidth, requiredHeight, loadCallback).execute(new Void[0]);
    }

    public static Bitmap transformBitmap(@NonNull Bitmap bitmap, @NonNull Matrix transformMatrix) {
        try {
            Bitmap converted = Bitmap.createBitmap((Bitmap)bitmap, (int)0, (int)0, (int)bitmap.getWidth(), (int)bitmap.getHeight(), (Matrix)transformMatrix, (boolean)true);
            if (!bitmap.sameAs(converted)) {
                bitmap = converted;
            }
        }
        catch (OutOfMemoryError error) {
            Log.e((String)TAG, (String)"transformBitmap: ", (Throwable)error);
        }
        return bitmap;
    }

    public static int calculateInSampleSize(@NonNull BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            while (height / inSampleSize > reqHeight || width / inSampleSize > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    public static int getExifOrientation(@NonNull Context context, @NonNull Uri imageUri) {
        int orientation = 0;
        try {
            InputStream stream = context.getContentResolver().openInputStream(imageUri);
            if (stream == null) {
                return orientation;
            }
            orientation = new ImageHeaderParser(stream).getOrientation();
            BitmapLoadUtils.close(stream);
        }
        catch (IOException e) {
            Log.e((String)TAG, (String)("getExifOrientation: " + imageUri.toString()), (Throwable)e);
        }
        return orientation;
    }

    public static int exifToDegrees(int exifOrientation) {
        int rotation;
        switch (exifOrientation) {
            case 5: 
            case 6: {
                rotation = 90;
                break;
            }
            case 3: 
            case 4: {
                rotation = 180;
                break;
            }
            case 7: 
            case 8: {
                rotation = 270;
                break;
            }
            default: {
                rotation = 0;
            }
        }
        return rotation;
    }

    public static int exifToTranslation(int exifOrientation) {
        int translation;
        switch (exifOrientation) {
            case 2: 
            case 4: 
            case 5: 
            case 7: {
                translation = -1;
                break;
            }
            default: {
                translation = 1;
            }
        }
        return translation;
    }

    public static int calculateMaxBitmapSize(@NonNull Context context) {
        int maxTextureSize;
        WindowManager wm = (WindowManager)context.getSystemService("window");
        Point size = new Point();
        if (wm != null) {
            Display display = wm.getDefaultDisplay();
            display.getSize(size);
        }
        int width = size.x;
        int height = size.y;
        int maxBitmapSize = (int)Math.sqrt(Math.pow(width, 2.0) + Math.pow(height, 2.0));
        Canvas canvas = new Canvas();
        int maxCanvasSize = Math.min(canvas.getMaximumBitmapWidth(), canvas.getMaximumBitmapHeight());
        if (maxCanvasSize > 0) {
            maxBitmapSize = Math.min(maxBitmapSize, maxCanvasSize);
        }
        if ((maxTextureSize = EglUtils.getMaxTextureSize()) > 0) {
            maxBitmapSize = Math.min(maxBitmapSize, maxTextureSize);
        }
        Log.d((String)TAG, (String)("maxBitmapSize: " + maxBitmapSize));
        return maxBitmapSize;
    }

    public static void close(@Nullable Closeable c) {
        if (c != null && c instanceof Closeable) {
            try {
                c.close();
            }
            catch (IOException iOException) {
                // empty catch block
            }
        }
    }
}

