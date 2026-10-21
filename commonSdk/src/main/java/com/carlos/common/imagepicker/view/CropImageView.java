/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.TypedArray
 *  android.graphics.Bitmap$CompressFormat
 *  android.graphics.Matrix
 *  android.graphics.RectF
 *  android.graphics.drawable.Drawable
 *  android.util.AttributeSet
 *  android.util.Log
 *  androidx.annotation.IntRange
 *  androidx.annotation.NonNull
 *  androidx.annotation.Nullable
 *  com.kook.librelease.R$styleable
 */
package com.carlos.common.imagepicker.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.carlos.common.imagepicker.callback.BitmapCropCallback;
import com.carlos.common.imagepicker.callback.CropBoundsChangeListener;
import com.carlos.common.imagepicker.entity.CropParameters;
import com.carlos.common.imagepicker.entity.ImageState;
import com.carlos.common.imagepicker.task.BitmapCropTask;
import com.carlos.common.imagepicker.util.CubicEasing;
import com.carlos.common.imagepicker.util.RectUtils;
import com.carlos.common.imagepicker.view.TransformImageView;
import com.kook.librelease.R;
import com.kook.librelease.StringFog;
import java.lang.ref.WeakReference;
import java.util.Arrays;

public class CropImageView
extends TransformImageView {
    public static final int DEFAULT_MAX_BITMAP_SIZE = 0;
    public static final int DEFAULT_IMAGE_TO_CROP_BOUNDS_ANIM_DURATION = 500;
    public static final float DEFAULT_MAX_SCALE_MULTIPLIER = 10.0f;
    public static final float SOURCE_IMAGE_ASPECT_RATIO = 0.0f;
    public static final float DEFAULT_ASPECT_RATIO = 0.0f;
    private final RectF mCropRect = new RectF();
    private final Matrix mTempMatrix = new Matrix();
    private float mTargetAspectRatio;
    private float mMaxScaleMultiplier = 10.0f;
    private CropBoundsChangeListener mCropBoundsChangeListener;
    private Runnable mWrapCropBoundsRunnable;
    private Runnable mZoomImageToPositionRunnable = null;
    private float mMaxScale;
    private float mMinScale;
    private int mMaxResultImageSizeX = 0;
    private int mMaxResultImageSizeY = 0;
    private long mImageToWrapCropBoundsAnimDuration = 500L;

    public CropImageView(Context context) {
        this(context, null);
    }

    public CropImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CropImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void cropAndSaveImage(@NonNull Bitmap.CompressFormat compressFormat, int compressQuality, @Nullable BitmapCropCallback cropCallback) {
        this.cancelAllAnimations();
        this.setImageToWrapCropBounds(false);
        ImageState imageState = new ImageState(this.mCropRect, RectUtils.trapToRect(this.mCurrentImageCorners), this.getCurrentScale(), this.getCurrentAngle());
        CropParameters cropParameters = new CropParameters(this.mMaxResultImageSizeX, this.mMaxResultImageSizeY, compressFormat, compressQuality, this.getImageInputPath(), this.getImageOutputPath(), this.getExifInfo());
        new BitmapCropTask(this.getViewBitmap(), imageState, cropParameters, cropCallback).execute(new Void[0]);
    }

    public float getMaxScale() {
        return this.mMaxScale;
    }

    public float getMinScale() {
        return this.mMinScale;
    }

    public float getTargetAspectRatio() {
        return this.mTargetAspectRatio;
    }

    public void setCropRect(RectF cropRect) {
        this.mTargetAspectRatio = cropRect.width() / cropRect.height();
        this.mCropRect.set(cropRect.left - (float)this.getPaddingLeft(), cropRect.top - (float)this.getPaddingTop(), cropRect.right - (float)this.getPaddingRight(), cropRect.bottom - (float)this.getPaddingBottom());
        this.calculateImageScaleBounds();
        this.setImageToWrapCropBounds();
    }

    public void setTargetAspectRatio(float targetAspectRatio) {
        Log.i((String)"VirtualApp", (String)("setTargetAspectRatio " + targetAspectRatio));
        Drawable drawable2 = this.getDrawable();
        if (drawable2 == null) {
            this.mTargetAspectRatio = targetAspectRatio;
            return;
        }
        this.mTargetAspectRatio = targetAspectRatio == 0.0f ? (float)drawable2.getIntrinsicWidth() / (float)drawable2.getIntrinsicHeight() : targetAspectRatio;
        if (this.mCropBoundsChangeListener != null) {
            this.mCropBoundsChangeListener.onCropAspectRatioChanged(this.mTargetAspectRatio);
        }
    }

    @Nullable
    public CropBoundsChangeListener getCropBoundsChangeListener() {
        return this.mCropBoundsChangeListener;
    }

    public void setCropBoundsChangeListener(@Nullable CropBoundsChangeListener cropBoundsChangeListener) {
        this.mCropBoundsChangeListener = cropBoundsChangeListener;
    }

    public void setMaxResultImageSizeX(@IntRange(from=10L) int maxResultImageSizeX) {
        this.mMaxResultImageSizeX = maxResultImageSizeX;
    }

    public void setMaxResultImageSizeY(@IntRange(from=10L) int maxResultImageSizeY) {
        this.mMaxResultImageSizeY = maxResultImageSizeY;
    }

    public void setImageToWrapCropBoundsAnimDuration(@IntRange(from=100L) long imageToWrapCropBoundsAnimDuration) {
        if (imageToWrapCropBoundsAnimDuration <= 0L) {
            throw new IllegalArgumentException("Animation duration cannot be negative value.");
        }
        this.mImageToWrapCropBoundsAnimDuration = imageToWrapCropBoundsAnimDuration;
    }

    public void setMaxScaleMultiplier(float maxScaleMultiplier) {
        this.mMaxScaleMultiplier = maxScaleMultiplier;
    }

    public void zoomOutImage(float deltaScale) {
        this.zoomOutImage(deltaScale, this.mCropRect.centerX(), this.mCropRect.centerY());
    }

    public void zoomOutImage(float scale, float centerX, float centerY) {
        if (scale >= this.getMinScale()) {
            this.postScale(scale / this.getCurrentScale(), centerX, centerY);
        }
    }

    public void zoomInImage(float deltaScale) {
        this.zoomInImage(deltaScale, this.mCropRect.centerX(), this.mCropRect.centerY());
    }

    public void zoomInImage(float scale, float centerX, float centerY) {
        if (scale <= this.getMaxScale()) {
            this.postScale(scale / this.getCurrentScale(), centerX, centerY);
        }
    }

    @Override
    public void postScale(float deltaScale, float px, float py) {
        if (deltaScale > 1.0f && this.getCurrentScale() * deltaScale <= this.getMaxScale()) {
            super.postScale(deltaScale, px, py);
        } else if (deltaScale < 1.0f && this.getCurrentScale() * deltaScale >= this.getMinScale()) {
            super.postScale(deltaScale, px, py);
        }
    }

    public void postRotate(float deltaAngle) {
        this.postRotate(deltaAngle, this.mCropRect.centerX(), this.mCropRect.centerY());
    }

    public void cancelAllAnimations() {
        this.removeCallbacks(this.mWrapCropBoundsRunnable);
        this.removeCallbacks(this.mZoomImageToPositionRunnable);
    }

    public void setImageToWrapCropBounds() {
        this.setImageToWrapCropBounds(true);
    }

    public void setImageToWrapCropBounds(boolean animate) {
        if (this.mBitmapLaidOut && !this.isImageWrapCropBounds()) {
            float currentX = this.mCurrentImageCenter[0];
            float currentY = this.mCurrentImageCenter[1];
            float currentScale = this.getCurrentScale();
            float deltaX = this.mCropRect.centerX() - currentX;
            float deltaY = this.mCropRect.centerY() - currentY;
            float deltaScale = 0.0f;
            this.mTempMatrix.reset();
            this.mTempMatrix.setTranslate(deltaX, deltaY);
            float[] tempCurrentImageCorners = Arrays.copyOf(this.mCurrentImageCorners, this.mCurrentImageCorners.length);
            this.mTempMatrix.mapPoints(tempCurrentImageCorners);
            boolean willImageWrapCropBoundsAfterTranslate = this.isImageWrapCropBounds(tempCurrentImageCorners);
            if (willImageWrapCropBoundsAfterTranslate) {
                float[] imageIndents = this.calculateImageIndents();
                deltaX = -(imageIndents[0] + imageIndents[2]);
                deltaY = -(imageIndents[1] + imageIndents[3]);
            } else {
                RectF tempCropRect = new RectF(this.mCropRect);
                this.mTempMatrix.reset();
                this.mTempMatrix.setRotate(this.getCurrentAngle());
                this.mTempMatrix.mapRect(tempCropRect);
                float[] currentImageSides = RectUtils.getRectSidesFromCorners(this.mCurrentImageCorners);
                deltaScale = Math.max(tempCropRect.width() / currentImageSides[0], tempCropRect.height() / currentImageSides[1]);
                deltaScale = deltaScale * currentScale - currentScale;
            }
            if (animate) {
                this.mWrapCropBoundsRunnable = new WrapCropBoundsRunnable(this, this.mImageToWrapCropBoundsAnimDuration, currentX, currentY, deltaX, deltaY, currentScale, deltaScale, willImageWrapCropBoundsAfterTranslate);
                this.post(this.mWrapCropBoundsRunnable);
            } else {
                this.postTranslate(deltaX, deltaY);
                if (!willImageWrapCropBoundsAfterTranslate) {
                    this.zoomInImage(currentScale + deltaScale, this.mCropRect.centerX(), this.mCropRect.centerY());
                }
            }
        }
    }

    private float[] calculateImageIndents() {
        this.mTempMatrix.reset();
        this.mTempMatrix.setRotate(-this.getCurrentAngle());
        float[] unrotatedImageCorners = Arrays.copyOf(this.mCurrentImageCorners, this.mCurrentImageCorners.length);
        float[] unrotatedCropBoundsCorners = RectUtils.getCornersFromRect(this.mCropRect);
        this.mTempMatrix.mapPoints(unrotatedImageCorners);
        this.mTempMatrix.mapPoints(unrotatedCropBoundsCorners);
        RectF unrotatedImageRect = RectUtils.trapToRect(unrotatedImageCorners);
        RectF unrotatedCropRect = RectUtils.trapToRect(unrotatedCropBoundsCorners);
        float deltaLeft = unrotatedImageRect.left - unrotatedCropRect.left;
        float deltaTop = unrotatedImageRect.top - unrotatedCropRect.top;
        float deltaRight = unrotatedImageRect.right - unrotatedCropRect.right;
        float deltaBottom = unrotatedImageRect.bottom - unrotatedCropRect.bottom;
        float[] indents = new float[]{deltaLeft > 0.0f ? deltaLeft : 0.0f, deltaTop > 0.0f ? deltaTop : 0.0f, deltaRight < 0.0f ? deltaRight : 0.0f, deltaBottom < 0.0f ? deltaBottom : 0.0f};
        this.mTempMatrix.reset();
        this.mTempMatrix.setRotate(this.getCurrentAngle());
        this.mTempMatrix.mapPoints(indents);
        return indents;
    }

    @Override
    protected void onImageLaidOut() {
        int height;
        super.onImageLaidOut();
        Drawable drawable2 = this.getDrawable();
        if (drawable2 == null) {
            return;
        }
        float drawableWidth = drawable2.getIntrinsicWidth();
        float drawableHeight = drawable2.getIntrinsicHeight();
        if (this.mTargetAspectRatio == 0.0f) {
            this.mTargetAspectRatio = drawableWidth / drawableHeight;
        }
        if ((height = (int)((float)this.mThisWidth / this.mTargetAspectRatio)) > this.mThisHeight) {
            int width = (int)((float)this.mThisHeight * this.mTargetAspectRatio);
            int halfDiff = (this.mThisWidth - width) / 2;
            this.mCropRect.set((float)halfDiff, 0.0f, (float)(width + halfDiff), (float)this.mThisHeight);
        } else {
            int halfDiff = (this.mThisHeight - height) / 2;
            this.mCropRect.set(0.0f, (float)halfDiff, (float)this.mThisWidth, (float)(height + halfDiff));
        }
        this.calculateImageScaleBounds(drawableWidth, drawableHeight);
        this.setupInitialImagePosition(drawableWidth, drawableHeight);
        if (this.mCropBoundsChangeListener != null) {
            this.mCropBoundsChangeListener.onCropAspectRatioChanged(this.mTargetAspectRatio);
        }
        if (this.mTransformImageListener != null) {
            this.mTransformImageListener.onScale(this.getCurrentScale());
            this.mTransformImageListener.onRotate(this.getCurrentAngle());
        }
    }

    protected boolean isImageWrapCropBounds() {
        return this.isImageWrapCropBounds(this.mCurrentImageCorners);
    }

    protected boolean isImageWrapCropBounds(float[] imageCorners) {
        this.mTempMatrix.reset();
        this.mTempMatrix.setRotate(-this.getCurrentAngle());
        float[] unrotatedImageCorners = Arrays.copyOf(imageCorners, imageCorners.length);
        this.mTempMatrix.mapPoints(unrotatedImageCorners);
        float[] unrotatedCropBoundsCorners = RectUtils.getCornersFromRect(this.mCropRect);
        this.mTempMatrix.mapPoints(unrotatedCropBoundsCorners);
        return RectUtils.trapToRect(unrotatedImageCorners).contains(RectUtils.trapToRect(unrotatedCropBoundsCorners));
    }

    protected void zoomImageToPosition(float scale, float centerX, float centerY, long durationMs) {
        if (scale > this.getMaxScale()) {
            scale = this.getMaxScale();
        }
        float oldScale = this.getCurrentScale();
        float deltaScale = scale - oldScale;
        this.mZoomImageToPositionRunnable = new ZoomImageToPosition(this, durationMs, oldScale, deltaScale, centerX, centerY);
        this.post(this.mZoomImageToPositionRunnable);
    }

    private void calculateImageScaleBounds() {
        Drawable drawable2 = this.getDrawable();
        if (drawable2 == null) {
            return;
        }
        this.calculateImageScaleBounds(drawable2.getIntrinsicWidth(), drawable2.getIntrinsicHeight());
    }

    private void calculateImageScaleBounds(float drawableWidth, float drawableHeight) {
        float widthScale = Math.min(this.mCropRect.width() / drawableWidth, this.mCropRect.width() / drawableHeight);
        float heightScale = Math.min(this.mCropRect.height() / drawableHeight, this.mCropRect.height() / drawableWidth);
        this.mMinScale = Math.min(widthScale, heightScale);
        this.mMaxScale = this.mMinScale * this.mMaxScaleMultiplier;
    }

    private void setupInitialImagePosition(float drawableWidth, float drawableHeight) {
        float cropRectWidth = this.mCropRect.width();
        float cropRectHeight = this.mCropRect.height();
        float widthScale = this.mCropRect.width() / drawableWidth;
        float heightScale = this.mCropRect.height() / drawableHeight;
        float initialMinScale = Math.max(widthScale, heightScale);
        float tw = (cropRectWidth - drawableWidth * initialMinScale) / 2.0f + this.mCropRect.left;
        float th = (cropRectHeight - drawableHeight * initialMinScale) / 2.0f + this.mCropRect.top;
        this.mCurrentImageMatrix.reset();
        this.mCurrentImageMatrix.postScale(initialMinScale, initialMinScale);
        this.mCurrentImageMatrix.postTranslate(tw, th);
        this.setImageMatrix(this.mCurrentImageMatrix);
    }

    protected void processStyledAttributes(@NonNull TypedArray a) {
        float targetAspectRatioX = Math.abs(a.getFloat(R.styleable.ucrop_UCropView_ucrop_aspect_ratio_x, 0.0f));
        float targetAspectRatioY = Math.abs(a.getFloat(R.styleable.ucrop_UCropView_ucrop_aspect_ratio_y, 0.0f));
        this.mTargetAspectRatio = targetAspectRatioX == 0.0f || targetAspectRatioY == 0.0f ? 0.0f : targetAspectRatioX / targetAspectRatioY;
    }

    private static class ZoomImageToPosition
    implements Runnable {
        private final WeakReference<CropImageView> mCropImageView;
        private final long mDurationMs;
        private final long mStartTime;
        private final float mOldScale;
        private final float mDeltaScale;
        private final float mDestX;
        private final float mDestY;

        public ZoomImageToPosition(CropImageView cropImageView, long durationMs, float oldScale, float deltaScale, float destX, float destY) {
            this.mCropImageView = new WeakReference<CropImageView>(cropImageView);
            this.mStartTime = System.currentTimeMillis();
            this.mDurationMs = durationMs;
            this.mOldScale = oldScale;
            this.mDeltaScale = deltaScale;
            this.mDestX = destX;
            this.mDestY = destY;
        }

        @Override
        public void run() {
            CropImageView cropImageView = (CropImageView)((Object)this.mCropImageView.get());
            if (cropImageView == null) {
                return;
            }
            long now = System.currentTimeMillis();
            float currentMs = Math.min(this.mDurationMs, now - this.mStartTime);
            float newScale = CubicEasing.easeInOut(currentMs, 0.0f, this.mDeltaScale, this.mDurationMs);
            if (currentMs < (float)this.mDurationMs) {
                cropImageView.zoomInImage(this.mOldScale + newScale, this.mDestX, this.mDestY);
                cropImageView.post(this);
            } else {
                cropImageView.setImageToWrapCropBounds();
            }
        }
    }

    private static class WrapCropBoundsRunnable
    implements Runnable {
        private final WeakReference<CropImageView> mCropImageView;
        private final long mDurationMs;
        private final long mStartTime;
        private final float mOldX;
        private final float mOldY;
        private final float mCenterDiffX;
        private final float mCenterDiffY;
        private final float mOldScale;
        private final float mDeltaScale;
        private final boolean mWillBeImageInBoundsAfterTranslate;

        public WrapCropBoundsRunnable(CropImageView cropImageView, long durationMs, float oldX, float oldY, float centerDiffX, float centerDiffY, float oldScale, float deltaScale, boolean willBeImageInBoundsAfterTranslate) {
            this.mCropImageView = new WeakReference<CropImageView>(cropImageView);
            this.mDurationMs = durationMs;
            this.mStartTime = System.currentTimeMillis();
            this.mOldX = oldX;
            this.mOldY = oldY;
            this.mCenterDiffX = centerDiffX;
            this.mCenterDiffY = centerDiffY;
            this.mOldScale = oldScale;
            this.mDeltaScale = deltaScale;
            this.mWillBeImageInBoundsAfterTranslate = willBeImageInBoundsAfterTranslate;
        }

        @Override
        public void run() {
            CropImageView cropImageView = (CropImageView)((Object)this.mCropImageView.get());
            if (cropImageView == null) {
                return;
            }
            long now = System.currentTimeMillis();
            float currentMs = Math.min(this.mDurationMs, now - this.mStartTime);
            float newX = CubicEasing.easeOut(currentMs, 0.0f, this.mCenterDiffX, this.mDurationMs);
            float newY = CubicEasing.easeOut(currentMs, 0.0f, this.mCenterDiffY, this.mDurationMs);
            float newScale = CubicEasing.easeInOut(currentMs, 0.0f, this.mDeltaScale, this.mDurationMs);
            if (currentMs < (float)this.mDurationMs) {
                cropImageView.postTranslate(newX - (cropImageView.mCurrentImageCenter[0] - this.mOldX), newY - (cropImageView.mCurrentImageCenter[1] - this.mOldY));
                if (!this.mWillBeImageInBoundsAfterTranslate) {
                    cropImageView.zoomInImage(this.mOldScale + newScale, cropImageView.mCropRect.centerX(), cropImageView.mCropRect.centerY());
                }
                if (!cropImageView.isImageWrapCropBounds()) {
                    cropImageView.post(this);
                }
            }
        }
    }
}

