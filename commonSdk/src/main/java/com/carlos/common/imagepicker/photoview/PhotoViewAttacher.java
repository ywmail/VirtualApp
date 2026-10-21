/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.graphics.Matrix
 *  android.graphics.Matrix$ScaleToFit
 *  android.graphics.RectF
 *  android.graphics.drawable.Drawable
 *  android.view.GestureDetector
 *  android.view.GestureDetector$OnDoubleTapListener
 *  android.view.GestureDetector$OnGestureListener
 *  android.view.GestureDetector$SimpleOnGestureListener
 *  android.view.MotionEvent
 *  android.view.View
 *  android.view.View$OnClickListener
 *  android.view.View$OnLayoutChangeListener
 *  android.view.View$OnLongClickListener
 *  android.view.View$OnTouchListener
 *  android.view.ViewParent
 *  android.view.animation.AccelerateDecelerateInterpolator
 *  android.view.animation.Interpolator
 *  android.widget.ImageView
 *  android.widget.ImageView$ScaleType
 *  android.widget.OverScroller
 */
package com.carlos.common.imagepicker.photoview;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.OverScroller;
import com.carlos.common.imagepicker.photoview.Compat;
import com.carlos.common.imagepicker.photoview.CustomGestureDetector;
import com.carlos.common.imagepicker.photoview.OnGestureListener;
import com.carlos.common.imagepicker.photoview.OnMatrixChangedListener;
import com.carlos.common.imagepicker.photoview.OnOutsidePhotoTapListener;
import com.carlos.common.imagepicker.photoview.OnPhotoTapListener;
import com.carlos.common.imagepicker.photoview.OnScaleChangedListener;
import com.carlos.common.imagepicker.photoview.OnSingleFlingListener;
import com.carlos.common.imagepicker.photoview.OnViewDragListener;
import com.carlos.common.imagepicker.photoview.OnViewTapListener;
import com.carlos.common.imagepicker.photoview.Util;
import com.kook.librelease.StringFog;

public class PhotoViewAttacher
implements View.OnTouchListener,
View.OnLayoutChangeListener {
    private static float DEFAULT_MAX_SCALE = 3.0f;
    private static float DEFAULT_MID_SCALE = 1.75f;
    private static float DEFAULT_MIN_SCALE = 1.0f;
    private static int DEFAULT_ZOOM_DURATION = 200;
    private static final int HORIZONTAL_EDGE_NONE = -1;
    private static final int HORIZONTAL_EDGE_LEFT = 0;
    private static final int HORIZONTAL_EDGE_RIGHT = 1;
    private static final int HORIZONTAL_EDGE_BOTH = 2;
    private static final int VERTICAL_EDGE_NONE = -1;
    private static final int VERTICAL_EDGE_TOP = 0;
    private static final int VERTICAL_EDGE_BOTTOM = 1;
    private static final int VERTICAL_EDGE_BOTH = 2;
    private static int SINGLE_TOUCH = 1;
    private Interpolator mInterpolator = new AccelerateDecelerateInterpolator();
    private int mZoomDuration = DEFAULT_ZOOM_DURATION;
    private float mMinScale = DEFAULT_MIN_SCALE;
    private float mMidScale = DEFAULT_MID_SCALE;
    private float mMaxScale = DEFAULT_MAX_SCALE;
    private boolean mAllowParentInterceptOnEdge = true;
    private boolean mBlockParentIntercept = false;
    private ImageView mImageView;
    private GestureDetector mGestureDetector;
    private CustomGestureDetector mScaleDragDetector;
    private final Matrix mBaseMatrix = new Matrix();
    private final Matrix mDrawMatrix = new Matrix();
    private final Matrix mSuppMatrix = new Matrix();
    private final RectF mDisplayRect = new RectF();
    private final float[] mMatrixValues = new float[9];
    private OnMatrixChangedListener mMatrixChangeListener;
    private OnPhotoTapListener mPhotoTapListener;
    private OnOutsidePhotoTapListener mOutsidePhotoTapListener;
    private OnViewTapListener mViewTapListener;
    private View.OnClickListener mOnClickListener;
    private View.OnLongClickListener mLongClickListener;
    private OnScaleChangedListener mScaleChangeListener;
    private OnSingleFlingListener mSingleFlingListener;
    private OnViewDragListener mOnViewDragListener;
    private FlingRunnable mCurrentFlingRunnable;
    private int mHorizontalScrollEdge = 2;
    private int mVerticalScrollEdge = 2;
    private float mBaseRotation;
    private boolean mZoomEnabled = true;
    private ImageView.ScaleType mScaleType = ImageView.ScaleType.FIT_CENTER;
    private OnGestureListener onGestureListener = new OnGestureListener(){

        @Override
        public void onDrag(float dx, float dy) {
            if (PhotoViewAttacher.this.mScaleDragDetector.isScaling()) {
                return;
            }
            if (PhotoViewAttacher.this.mOnViewDragListener != null) {
                PhotoViewAttacher.this.mOnViewDragListener.onDrag(dx, dy);
            }
            PhotoViewAttacher.this.mSuppMatrix.postTranslate(dx, dy);
            PhotoViewAttacher.this.checkAndDisplayMatrix();
            ViewParent parent = PhotoViewAttacher.this.mImageView.getParent();
            if (PhotoViewAttacher.this.mAllowParentInterceptOnEdge && !PhotoViewAttacher.this.mScaleDragDetector.isScaling() && !PhotoViewAttacher.this.mBlockParentIntercept) {
                if ((PhotoViewAttacher.this.mHorizontalScrollEdge == 2 || PhotoViewAttacher.this.mHorizontalScrollEdge == 0 && dx >= 1.0f || PhotoViewAttacher.this.mHorizontalScrollEdge == 1 && dx <= -1.0f || PhotoViewAttacher.this.mVerticalScrollEdge == 0 && dy >= 1.0f || PhotoViewAttacher.this.mVerticalScrollEdge == 1 && dy <= -1.0f) && parent != null) {
                    parent.requestDisallowInterceptTouchEvent(false);
                }
            } else if (parent != null) {
                parent.requestDisallowInterceptTouchEvent(true);
            }
        }

        @Override
        public void onFling(float startX, float startY, float velocityX, float velocityY) {
            PhotoViewAttacher.this.mCurrentFlingRunnable = new FlingRunnable(PhotoViewAttacher.this.mImageView.getContext());
            PhotoViewAttacher.this.mCurrentFlingRunnable.fling(PhotoViewAttacher.this.getImageViewWidth(PhotoViewAttacher.this.mImageView), PhotoViewAttacher.this.getImageViewHeight(PhotoViewAttacher.this.mImageView), (int)velocityX, (int)velocityY);
            PhotoViewAttacher.this.mImageView.post((Runnable)PhotoViewAttacher.this.mCurrentFlingRunnable);
        }

        @Override
        public void onScale(float scaleFactor, float focusX, float focusY) {
            this.onScale(scaleFactor, focusX, focusY, 0.0f, 0.0f);
        }

        @Override
        public void onScale(float scaleFactor, float focusX, float focusY, float dx, float dy) {
            if (PhotoViewAttacher.this.getScale() < PhotoViewAttacher.this.mMaxScale || scaleFactor < 1.0f) {
                if (PhotoViewAttacher.this.mScaleChangeListener != null) {
                    PhotoViewAttacher.this.mScaleChangeListener.onScaleChange(scaleFactor, focusX, focusY);
                }
                PhotoViewAttacher.this.mSuppMatrix.postScale(scaleFactor, scaleFactor, focusX, focusY);
                PhotoViewAttacher.this.mSuppMatrix.postTranslate(dx, dy);
                PhotoViewAttacher.this.checkAndDisplayMatrix();
            }
        }
    };

    public PhotoViewAttacher(ImageView imageView) {
        this.mImageView = imageView;
        imageView.setOnTouchListener((View.OnTouchListener)this);
        imageView.addOnLayoutChangeListener((View.OnLayoutChangeListener)this);
        if (imageView.isInEditMode()) {
            return;
        }
        this.mBaseRotation = 0.0f;
        this.mScaleDragDetector = new CustomGestureDetector(imageView.getContext(), this.onGestureListener);
        this.mGestureDetector = new GestureDetector(imageView.getContext(), (GestureDetector.OnGestureListener)new GestureDetector.SimpleOnGestureListener(){

            public void onLongPress(MotionEvent e) {
                if (PhotoViewAttacher.this.mLongClickListener != null) {
                    PhotoViewAttacher.this.mLongClickListener.onLongClick((View)PhotoViewAttacher.this.mImageView);
                }
            }

            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (PhotoViewAttacher.this.mSingleFlingListener != null) {
                    if (PhotoViewAttacher.this.getScale() > DEFAULT_MIN_SCALE) {
                        return false;
                    }
                    if (e1.getPointerCount() > SINGLE_TOUCH || e2.getPointerCount() > SINGLE_TOUCH) {
                        return false;
                    }
                    return PhotoViewAttacher.this.mSingleFlingListener.onFling(e1, e2, velocityX, velocityY);
                }
                return false;
            }
        });
        this.mGestureDetector.setOnDoubleTapListener(new GestureDetector.OnDoubleTapListener(){

            public boolean onSingleTapConfirmed(MotionEvent e) {
                if (PhotoViewAttacher.this.mOnClickListener != null) {
                    PhotoViewAttacher.this.mOnClickListener.onClick((View)PhotoViewAttacher.this.mImageView);
                }
                RectF displayRect = PhotoViewAttacher.this.getDisplayRect();
                float x = e.getX();
                float y = e.getY();
                if (PhotoViewAttacher.this.mViewTapListener != null) {
                    PhotoViewAttacher.this.mViewTapListener.onViewTap((View)PhotoViewAttacher.this.mImageView, x, y);
                }
                if (displayRect != null) {
                    if (displayRect.contains(x, y)) {
                        float xResult = (x - displayRect.left) / displayRect.width();
                        float yResult = (y - displayRect.top) / displayRect.height();
                        if (PhotoViewAttacher.this.mPhotoTapListener != null) {
                            PhotoViewAttacher.this.mPhotoTapListener.onPhotoTap(PhotoViewAttacher.this.mImageView, xResult, yResult);
                        }
                        return true;
                    }
                    if (PhotoViewAttacher.this.mOutsidePhotoTapListener != null) {
                        PhotoViewAttacher.this.mOutsidePhotoTapListener.onOutsidePhotoTap(PhotoViewAttacher.this.mImageView);
                    }
                }
                return false;
            }

            public boolean onDoubleTap(MotionEvent ev) {
                try {
                    float scale = PhotoViewAttacher.this.getScale();
                    float x = ev.getX();
                    float y = ev.getY();
                    if (scale < PhotoViewAttacher.this.getMediumScale()) {
                        PhotoViewAttacher.this.setScale(PhotoViewAttacher.this.getMediumScale(), x, y, true);
                    } else if (scale >= PhotoViewAttacher.this.getMediumScale() && scale < PhotoViewAttacher.this.getMaximumScale()) {
                        PhotoViewAttacher.this.setScale(PhotoViewAttacher.this.getMaximumScale(), x, y, true);
                    } else {
                        PhotoViewAttacher.this.setScale(PhotoViewAttacher.this.getMinimumScale(), x, y, true);
                    }
                }
                catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {
                    // empty catch block
                }
                return true;
            }

            public boolean onDoubleTapEvent(MotionEvent e) {
                return false;
            }
        });
    }

    public void setOnDoubleTapListener(GestureDetector.OnDoubleTapListener newOnDoubleTapListener) {
        this.mGestureDetector.setOnDoubleTapListener(newOnDoubleTapListener);
    }

    public void setOnScaleChangeListener(OnScaleChangedListener onScaleChangeListener) {
        this.mScaleChangeListener = onScaleChangeListener;
    }

    public void setOnSingleFlingListener(OnSingleFlingListener onSingleFlingListener) {
        this.mSingleFlingListener = onSingleFlingListener;
    }

    @Deprecated
    public boolean isZoomEnabled() {
        return this.mZoomEnabled;
    }

    public RectF getDisplayRect() {
        this.checkMatrixBounds();
        return this.getDisplayRect(this.getDrawMatrix());
    }

    public boolean setDisplayMatrix(Matrix finalMatrix) {
        if (finalMatrix == null) {
            throw new IllegalArgumentException("Matrix cannot be null");
        }
        if (this.mImageView.getDrawable() == null) {
            return false;
        }
        this.mSuppMatrix.set(finalMatrix);
        this.checkAndDisplayMatrix();
        return true;
    }

    public void setBaseRotation(float degrees) {
        this.mBaseRotation = degrees % 360.0f;
        this.update();
        this.setRotationBy(this.mBaseRotation);
        this.checkAndDisplayMatrix();
    }

    public void setRotationTo(float degrees) {
        this.mSuppMatrix.setRotate(degrees % 360.0f);
        this.checkAndDisplayMatrix();
    }

    public void setRotationBy(float degrees) {
        this.mSuppMatrix.postRotate(degrees % 360.0f);
        this.checkAndDisplayMatrix();
    }

    public float getMinimumScale() {
        return this.mMinScale;
    }

    public float getMediumScale() {
        return this.mMidScale;
    }

    public float getMaximumScale() {
        return this.mMaxScale;
    }

    public float getScale() {
        return (float)Math.sqrt((float)Math.pow(this.getValue(this.mSuppMatrix, 0), 2.0) + (float)Math.pow(this.getValue(this.mSuppMatrix, 3), 2.0));
    }

    public ImageView.ScaleType getScaleType() {
        return this.mScaleType;
    }

    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        if (left != oldLeft || top != oldTop || right != oldRight || bottom != oldBottom) {
            this.updateBaseMatrix(this.mImageView.getDrawable());
        }
    }

    public boolean onTouch(View v, MotionEvent ev) {
        boolean handled = false;
        if (this.mZoomEnabled && Util.hasDrawable((ImageView)v)) {
            switch (ev.getAction()) {
                case 0: {
                    ViewParent parent = v.getParent();
                    if (parent != null) {
                        parent.requestDisallowInterceptTouchEvent(true);
                    }
                    this.cancelFling();
                    break;
                }
                case 1: 
                case 3: {
                    RectF rect;
                    if (this.getScale() < this.mMinScale) {
                        rect = this.getDisplayRect();
                        if (rect == null) break;
                        v.post((Runnable)new AnimatedZoomRunnable(this.getScale(), this.mMinScale, rect.centerX(), rect.centerY()));
                        handled = true;
                        break;
                    }
                    if (!(this.getScale() > this.mMaxScale) || (rect = this.getDisplayRect()) == null) break;
                    v.post((Runnable)new AnimatedZoomRunnable(this.getScale(), this.mMaxScale, rect.centerX(), rect.centerY()));
                    handled = true;
                }
            }
            if (this.mScaleDragDetector != null) {
                boolean wasScaling = this.mScaleDragDetector.isScaling();
                boolean wasDragging = this.mScaleDragDetector.isDragging();
                handled = this.mScaleDragDetector.onTouchEvent(ev);
                boolean didntScale = !wasScaling && !this.mScaleDragDetector.isScaling();
                boolean didntDrag = !wasDragging && !this.mScaleDragDetector.isDragging();
                boolean bl = this.mBlockParentIntercept = didntScale && didntDrag;
            }
            if (this.mGestureDetector != null && this.mGestureDetector.onTouchEvent(ev)) {
                handled = true;
            }
        }
        return handled;
    }

    public void setAllowParentInterceptOnEdge(boolean allow) {
        this.mAllowParentInterceptOnEdge = allow;
    }

    public void setMinimumScale(float minimumScale) {
        Util.checkZoomLevels(minimumScale, this.mMidScale, this.mMaxScale);
        this.mMinScale = minimumScale;
    }

    public void setMediumScale(float mediumScale) {
        Util.checkZoomLevels(this.mMinScale, mediumScale, this.mMaxScale);
        this.mMidScale = mediumScale;
    }

    public void setMaximumScale(float maximumScale) {
        Util.checkZoomLevels(this.mMinScale, this.mMidScale, maximumScale);
        this.mMaxScale = maximumScale;
    }

    public void setScaleLevels(float minimumScale, float mediumScale, float maximumScale) {
        Util.checkZoomLevels(minimumScale, mediumScale, maximumScale);
        this.mMinScale = minimumScale;
        this.mMidScale = mediumScale;
        this.mMaxScale = maximumScale;
    }

    public void setOnLongClickListener(View.OnLongClickListener listener) {
        this.mLongClickListener = listener;
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.mOnClickListener = listener;
    }

    public void setOnMatrixChangeListener(OnMatrixChangedListener listener) {
        this.mMatrixChangeListener = listener;
    }

    public void setOnPhotoTapListener(OnPhotoTapListener listener) {
        this.mPhotoTapListener = listener;
    }

    public void setOnOutsidePhotoTapListener(OnOutsidePhotoTapListener mOutsidePhotoTapListener) {
        this.mOutsidePhotoTapListener = mOutsidePhotoTapListener;
    }

    public void setOnViewTapListener(OnViewTapListener listener) {
        this.mViewTapListener = listener;
    }

    public void setOnViewDragListener(OnViewDragListener listener) {
        this.mOnViewDragListener = listener;
    }

    public void setScale(float scale) {
        this.setScale(scale, false);
    }

    public void setScale(float scale, boolean animate) {
        this.setScale(scale, this.mImageView.getRight() / 2, this.mImageView.getBottom() / 2, animate);
    }

    public void setScale(float scale, float focalX, float focalY, boolean animate) {
        if (scale < this.mMinScale || scale > this.mMaxScale) {
            throw new IllegalArgumentException("Scale must be within the range of minScale and maxScale");
        }
        if (animate) {
            this.mImageView.post((Runnable)new AnimatedZoomRunnable(this.getScale(), scale, focalX, focalY));
        } else {
            this.mSuppMatrix.setScale(scale, scale, focalX, focalY);
            this.checkAndDisplayMatrix();
        }
    }

    public void setZoomInterpolator(Interpolator interpolator) {
        this.mInterpolator = interpolator;
    }

    public void setScaleType(ImageView.ScaleType scaleType) {
        if (Util.isSupportedScaleType(scaleType) && scaleType != this.mScaleType) {
            this.mScaleType = scaleType;
            this.update();
        }
    }

    public boolean isZoomable() {
        return this.mZoomEnabled;
    }

    public void setZoomable(boolean zoomable) {
        this.mZoomEnabled = zoomable;
        this.update();
    }

    public void update() {
        if (this.mZoomEnabled) {
            this.updateBaseMatrix(this.mImageView.getDrawable());
        } else {
            this.resetMatrix();
        }
    }

    public void getDisplayMatrix(Matrix matrix) {
        matrix.set(this.getDrawMatrix());
    }

    public void getSuppMatrix(Matrix matrix) {
        matrix.set(this.mSuppMatrix);
    }

    private Matrix getDrawMatrix() {
        this.mDrawMatrix.set(this.mBaseMatrix);
        this.mDrawMatrix.postConcat(this.mSuppMatrix);
        return this.mDrawMatrix;
    }

    public Matrix getImageMatrix() {
        return this.mDrawMatrix;
    }

    public void setZoomTransitionDuration(int milliseconds) {
        this.mZoomDuration = milliseconds;
    }

    private float getValue(Matrix matrix, int whichValue) {
        matrix.getValues(this.mMatrixValues);
        return this.mMatrixValues[whichValue];
    }

    private void resetMatrix() {
        this.mSuppMatrix.reset();
        this.setRotationBy(this.mBaseRotation);
        this.setImageViewMatrix(this.getDrawMatrix());
        this.checkMatrixBounds();
    }

    private void setImageViewMatrix(Matrix matrix) {
        RectF displayRect;
        this.mImageView.setImageMatrix(matrix);
        if (this.mMatrixChangeListener != null && (displayRect = this.getDisplayRect(matrix)) != null) {
            this.mMatrixChangeListener.onMatrixChanged(displayRect);
        }
    }

    private void checkAndDisplayMatrix() {
        if (this.checkMatrixBounds()) {
            this.setImageViewMatrix(this.getDrawMatrix());
        }
    }

    private RectF getDisplayRect(Matrix matrix) {
        Drawable d = this.mImageView.getDrawable();
        if (d != null) {
            this.mDisplayRect.set(0.0f, 0.0f, (float)d.getIntrinsicWidth(), (float)d.getIntrinsicHeight());
            matrix.mapRect(this.mDisplayRect);
            return this.mDisplayRect;
        }
        return null;
    }

    private void updateBaseMatrix(Drawable drawable2) {
        if (drawable2 == null) {
            return;
        }
        float viewWidth = this.getImageViewWidth(this.mImageView);
        float viewHeight = this.getImageViewHeight(this.mImageView);
        int drawableWidth = drawable2.getIntrinsicWidth();
        int drawableHeight = drawable2.getIntrinsicHeight();
        this.mBaseMatrix.reset();
        float widthScale = viewWidth / (float)drawableWidth;
        float heightScale = viewHeight / (float)drawableHeight;
        if (this.mScaleType == ImageView.ScaleType.CENTER) {
            this.mBaseMatrix.postTranslate((viewWidth - (float)drawableWidth) / 2.0f, (viewHeight - (float)drawableHeight) / 2.0f);
        } else if (this.mScaleType == ImageView.ScaleType.CENTER_CROP) {
            float scale = Math.max(widthScale, heightScale);
            this.mBaseMatrix.postScale(scale, scale);
            this.mBaseMatrix.postTranslate((viewWidth - (float)drawableWidth * scale) / 2.0f, (viewHeight - (float)drawableHeight * scale) / 2.0f);
        } else if (this.mScaleType == ImageView.ScaleType.CENTER_INSIDE) {
            float scale = Math.min(1.0f, Math.min(widthScale, heightScale));
            this.mBaseMatrix.postScale(scale, scale);
            this.mBaseMatrix.postTranslate((viewWidth - (float)drawableWidth * scale) / 2.0f, (viewHeight - (float)drawableHeight * scale) / 2.0f);
        } else {
            RectF mTempSrc = new RectF(0.0f, 0.0f, (float)drawableWidth, (float)drawableHeight);
            RectF mTempDst = new RectF(0.0f, 0.0f, viewWidth, viewHeight);
            if ((int)this.mBaseRotation % 180 != 0) {
                mTempSrc = new RectF(0.0f, 0.0f, (float)drawableHeight, (float)drawableWidth);
            }
            switch (this.mScaleType) {
                case FIT_CENTER: {
                    this.mBaseMatrix.setRectToRect(mTempSrc, mTempDst, Matrix.ScaleToFit.CENTER);
                    break;
                }
                case FIT_START: {
                    this.mBaseMatrix.setRectToRect(mTempSrc, mTempDst, Matrix.ScaleToFit.START);
                    break;
                }
                case FIT_END: {
                    this.mBaseMatrix.setRectToRect(mTempSrc, mTempDst, Matrix.ScaleToFit.END);
                    break;
                }
                case FIT_XY: {
                    this.mBaseMatrix.setRectToRect(mTempSrc, mTempDst, Matrix.ScaleToFit.FILL);
                    break;
                }
            }
        }
        this.resetMatrix();
    }

    private boolean checkMatrixBounds() {
        RectF rect = this.getDisplayRect(this.getDrawMatrix());
        if (rect == null) {
            return false;
        }
        float height = rect.height();
        float width = rect.width();
        float deltaX = 0.0f;
        float deltaY = 0.0f;
        int viewHeight = this.getImageViewHeight(this.mImageView);
        if (height <= (float)viewHeight) {
            switch (this.mScaleType) {
                case FIT_START: {
                    deltaY = -rect.top;
                    break;
                }
                case FIT_END: {
                    deltaY = (float)viewHeight - height - rect.top;
                    break;
                }
                default: {
                    deltaY = ((float)viewHeight - height) / 2.0f - rect.top;
                }
            }
            this.mVerticalScrollEdge = 2;
        } else if (rect.top > 0.0f) {
            this.mVerticalScrollEdge = 0;
            deltaY = -rect.top;
        } else if (rect.bottom < (float)viewHeight) {
            this.mVerticalScrollEdge = 1;
            deltaY = (float)viewHeight - rect.bottom;
        } else {
            this.mVerticalScrollEdge = -1;
        }
        int viewWidth = this.getImageViewWidth(this.mImageView);
        if (width <= (float)viewWidth) {
            switch (this.mScaleType) {
                case FIT_START: {
                    deltaX = -rect.left;
                    break;
                }
                case FIT_END: {
                    deltaX = (float)viewWidth - width - rect.left;
                    break;
                }
                default: {
                    deltaX = ((float)viewWidth - width) / 2.0f - rect.left;
                }
            }
            this.mHorizontalScrollEdge = 2;
        } else if (rect.left > 0.0f) {
            this.mHorizontalScrollEdge = 0;
            deltaX = -rect.left;
        } else if (rect.right < (float)viewWidth) {
            deltaX = (float)viewWidth - rect.right;
            this.mHorizontalScrollEdge = 1;
        } else {
            this.mHorizontalScrollEdge = -1;
        }
        this.mSuppMatrix.postTranslate(deltaX, deltaY);
        return true;
    }

    private int getImageViewWidth(ImageView imageView) {
        return imageView.getWidth() - imageView.getPaddingLeft() - imageView.getPaddingRight();
    }

    private int getImageViewHeight(ImageView imageView) {
        return imageView.getHeight() - imageView.getPaddingTop() - imageView.getPaddingBottom();
    }

    private void cancelFling() {
        if (this.mCurrentFlingRunnable != null) {
            this.mCurrentFlingRunnable.cancelFling();
            this.mCurrentFlingRunnable = null;
        }
    }

    private class FlingRunnable
    implements Runnable {
        private final OverScroller mScroller;
        private int mCurrentX;
        private int mCurrentY;

        public FlingRunnable(Context context) {
            this.mScroller = new OverScroller(context);
        }

        public void cancelFling() {
            this.mScroller.forceFinished(true);
        }

        public void fling(int viewWidth, int viewHeight, int velocityX, int velocityY) {
            int maxY;
            int minY;
            int maxX;
            int minX;
            RectF rect = PhotoViewAttacher.this.getDisplayRect();
            if (rect == null) {
                return;
            }
            int startX = Math.round(-rect.left);
            if ((float)viewWidth < rect.width()) {
                minX = 0;
                maxX = Math.round(rect.width() - (float)viewWidth);
            } else {
                minX = maxX = startX;
            }
            int startY = Math.round(-rect.top);
            if ((float)viewHeight < rect.height()) {
                minY = 0;
                maxY = Math.round(rect.height() - (float)viewHeight);
            } else {
                minY = maxY = startY;
            }
            this.mCurrentX = startX;
            this.mCurrentY = startY;
            if (startX != maxX || startY != maxY) {
                this.mScroller.fling(startX, startY, velocityX, velocityY, minX, maxX, minY, maxY, 0, 0);
            }
        }

        @Override
        public void run() {
            if (this.mScroller.isFinished()) {
                return;
            }
            if (this.mScroller.computeScrollOffset()) {
                int newX = this.mScroller.getCurrX();
                int newY = this.mScroller.getCurrY();
                PhotoViewAttacher.this.mSuppMatrix.postTranslate((float)(this.mCurrentX - newX), (float)(this.mCurrentY - newY));
                PhotoViewAttacher.this.checkAndDisplayMatrix();
                this.mCurrentX = newX;
                this.mCurrentY = newY;
                Compat.postOnAnimation((View)PhotoViewAttacher.this.mImageView, this);
            }
        }
    }

    private class AnimatedZoomRunnable
    implements Runnable {
        private final float mFocalX;
        private final float mFocalY;
        private final long mStartTime;
        private final float mZoomStart;
        private final float mZoomEnd;

        public AnimatedZoomRunnable(float currentZoom, float targetZoom, float focalX, float focalY) {
            this.mFocalX = focalX;
            this.mFocalY = focalY;
            this.mStartTime = System.currentTimeMillis();
            this.mZoomStart = currentZoom;
            this.mZoomEnd = targetZoom;
        }

        @Override
        public void run() {
            float t = this.interpolate();
            float scale = this.mZoomStart + t * (this.mZoomEnd - this.mZoomStart);
            float deltaScale = scale / PhotoViewAttacher.this.getScale();
            PhotoViewAttacher.this.onGestureListener.onScale(deltaScale, this.mFocalX, this.mFocalY);
            if (t < 1.0f) {
                Compat.postOnAnimation((View)PhotoViewAttacher.this.mImageView, this);
            }
        }

        private float interpolate() {
            float t = 1.0f * (float)(System.currentTimeMillis() - this.mStartTime) / (float)PhotoViewAttacher.this.mZoomDuration;
            t = Math.min(1.0f, t);
            t = PhotoViewAttacher.this.mInterpolator.getInterpolation(t);
            return t;
        }
    }
}

