/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.graphics.Bitmap
 *  android.graphics.Matrix
 *  android.graphics.RectF
 *  android.graphics.drawable.Drawable
 *  android.net.Uri
 *  android.util.AttributeSet
 *  android.util.Log
 *  android.widget.ImageView
 *  android.widget.ImageView$ScaleType
 *  androidx.annotation.IntRange
 *  androidx.annotation.NonNull
 *  androidx.annotation.Nullable
 */
package com.carlos.common.imagepicker.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.carlos.common.imagepicker.callback.BitmapLoadCallback;
import com.carlos.common.imagepicker.entity.ExifInfo;
import com.carlos.common.imagepicker.util.BitmapLoadUtils;
import com.carlos.common.imagepicker.util.FastBitmapDrawable;
import com.carlos.common.imagepicker.util.RectUtils;
import com.kook.librelease.StringFog;

public class TransformImageView
extends ImageView {
    private static final String TAG = "TransformImageView";
    private static final int RECT_CORNER_POINTS_COORDS = 8;
    private static final int RECT_CENTER_POINT_COORDS = 2;
    private static final int MATRIX_VALUES_COUNT = 9;
    protected final float[] mCurrentImageCorners = new float[8];
    protected final float[] mCurrentImageCenter = new float[2];
    private final float[] mMatrixValues = new float[9];
    protected Matrix mCurrentImageMatrix = new Matrix();
    protected int mThisWidth;
    protected int mThisHeight;
    protected TransformImageListener mTransformImageListener;
    private float[] mInitialImageCorners;
    private float[] mInitialImageCenter;
    protected boolean mBitmapDecoded = false;
    protected boolean mBitmapLaidOut = false;
    private int mMaxBitmapSize = 0;
    private String mImageInputPath;
    private String mImageOutputPath;
    private ExifInfo mExifInfo;

    public TransformImageView(Context context) {
        this(context, null);
    }

    public TransformImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TransformImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.init();
    }

    public void setTransformImageListener(TransformImageListener transformImageListener) {
        this.mTransformImageListener = transformImageListener;
    }

    public void setScaleType(ImageView.ScaleType scaleType) {
        if (scaleType == ImageView.ScaleType.MATRIX) {
            super.setScaleType(scaleType);
        } else {
            Log.w((String)TAG, (String)"Invalid ScaleType. Only ScaleType.MATRIX can be used");
        }
    }

    public void setMaxBitmapSize(int maxBitmapSize) {
        this.mMaxBitmapSize = maxBitmapSize;
    }

    public int getMaxBitmapSize() {
        if (this.mMaxBitmapSize <= 0) {
            this.mMaxBitmapSize = BitmapLoadUtils.calculateMaxBitmapSize(this.getContext());
        }
        return this.mMaxBitmapSize;
    }

    public void setImageBitmap(Bitmap bitmap) {
        this.setImageDrawable(new FastBitmapDrawable(bitmap));
    }

    public String getImageInputPath() {
        return this.mImageInputPath;
    }

    public String getImageOutputPath() {
        return this.mImageOutputPath;
    }

    public ExifInfo getExifInfo() {
        return this.mExifInfo;
    }

    public void setImageUri(@NonNull Uri imageUri, @Nullable Uri outputUri) throws Exception {
        int maxBitmapSize = this.getMaxBitmapSize();
        BitmapLoadUtils.decodeBitmapInBackground(this.getContext(), imageUri, outputUri, maxBitmapSize, maxBitmapSize, new BitmapLoadCallback(){

            @Override
            public void onBitmapLoaded(@NonNull Bitmap bitmap, @NonNull ExifInfo exifInfo, @NonNull String imageInputPath, @Nullable String imageOutputPath) {
                TransformImageView.this.mImageInputPath = imageInputPath;
                TransformImageView.this.mImageOutputPath = imageOutputPath;
                TransformImageView.this.mExifInfo = exifInfo;
                TransformImageView.this.mBitmapDecoded = true;
                TransformImageView.this.setImageBitmap(bitmap);
            }

            @Override
            public void onFailure(@NonNull Exception bitmapWorkerException) {
                Log.e((String)"TransformImageView", (String)"onFailure: setImageUri", (Throwable)bitmapWorkerException);
                if (TransformImageView.this.mTransformImageListener != null) {
                    TransformImageView.this.mTransformImageListener.onLoadFailure(bitmapWorkerException);
                }
            }
        });
    }

    public float getCurrentScale() {
        return this.getMatrixScale(this.mCurrentImageMatrix);
    }

    public float getMatrixScale(@NonNull Matrix matrix) {
        return (float)Math.sqrt(Math.pow(this.getMatrixValue(matrix, 0), 2.0) + Math.pow(this.getMatrixValue(matrix, 3), 2.0));
    }

    public float getCurrentAngle() {
        return this.getMatrixAngle(this.mCurrentImageMatrix);
    }

    public float getMatrixAngle(@NonNull Matrix matrix) {
        return (float)(-(Math.atan2(this.getMatrixValue(matrix, 1), this.getMatrixValue(matrix, 0)) * 57.29577951308232));
    }

    public void setImageMatrix(Matrix matrix) {
        super.setImageMatrix(matrix);
        this.mCurrentImageMatrix.set(matrix);
        this.updateCurrentImagePoints();
    }

    @Nullable
    public Bitmap getViewBitmap() {
        if (this.getDrawable() == null || !(this.getDrawable() instanceof FastBitmapDrawable)) {
            return null;
        }
        return ((FastBitmapDrawable)this.getDrawable()).getBitmap();
    }

    public void postTranslate(float deltaX, float deltaY) {
        if (deltaX != 0.0f || deltaY != 0.0f) {
            this.mCurrentImageMatrix.postTranslate(deltaX, deltaY);
            this.setImageMatrix(this.mCurrentImageMatrix);
        }
    }

    public void postScale(float deltaScale, float px, float py) {
        if (deltaScale != 0.0f) {
            this.mCurrentImageMatrix.postScale(deltaScale, deltaScale, px, py);
            this.setImageMatrix(this.mCurrentImageMatrix);
            if (this.mTransformImageListener != null) {
                this.mTransformImageListener.onScale(this.getMatrixScale(this.mCurrentImageMatrix));
            }
        }
    }

    public void postRotate(float deltaAngle, float px, float py) {
        if (deltaAngle != 0.0f) {
            this.mCurrentImageMatrix.postRotate(deltaAngle, px, py);
            this.setImageMatrix(this.mCurrentImageMatrix);
            if (this.mTransformImageListener != null) {
                this.mTransformImageListener.onRotate(this.getMatrixAngle(this.mCurrentImageMatrix));
            }
        }
    }

    protected void init() {
        this.setScaleType(ImageView.ScaleType.MATRIX);
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed || this.mBitmapDecoded && !this.mBitmapLaidOut) {
            left = this.getPaddingLeft();
            top = this.getPaddingTop();
            right = this.getWidth() - this.getPaddingRight();
            bottom = this.getHeight() - this.getPaddingBottom();
            this.mThisWidth = right - left;
            this.mThisHeight = bottom - top;
            this.onImageLaidOut();
        }
    }

    protected void onImageLaidOut() {
        Drawable drawable2 = this.getDrawable();
        if (drawable2 == null) {
            return;
        }
        float w = drawable2.getIntrinsicWidth();
        float h = drawable2.getIntrinsicHeight();
        Log.d((String)TAG, (String)String.format("Image size: [%d:%d]", (int)w, (int)h));
        RectF initialImageRect = new RectF(0.0f, 0.0f, w, h);
        this.mInitialImageCorners = RectUtils.getCornersFromRect(initialImageRect);
        this.mInitialImageCenter = RectUtils.getCenterFromRect(initialImageRect);
        this.mBitmapLaidOut = true;
        if (this.mTransformImageListener != null) {
            this.mTransformImageListener.onLoadComplete();
        }
    }

    protected float getMatrixValue(@NonNull Matrix matrix, @IntRange(from=0L, to=9L) int valueIndex) {
        matrix.getValues(this.mMatrixValues);
        return this.mMatrixValues[valueIndex];
    }

    protected void printMatrix(@NonNull String logPrefix, @NonNull Matrix matrix) {
        float x = this.getMatrixValue(matrix, 2);
        float y = this.getMatrixValue(matrix, 5);
        float rScale = this.getMatrixScale(matrix);
        float rAngle = this.getMatrixAngle(matrix);
        Log.d((String)TAG, (String)(logPrefix + ": matrix: { x: " + x + ", y: " + y + ", scale: " + rScale + ", angle: " + rAngle + " }"));
    }

    private void updateCurrentImagePoints() {
        this.mCurrentImageMatrix.mapPoints(this.mCurrentImageCorners, this.mInitialImageCorners);
        this.mCurrentImageMatrix.mapPoints(this.mCurrentImageCenter, this.mInitialImageCenter);
    }

    public static interface TransformImageListener {
        public void onLoadComplete();

        public void onLoadFailure(@NonNull Exception var1);

        public void onRotate(float var1);

        public void onScale(float var1);
    }
}

