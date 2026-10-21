/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.util.AttributeSet
 *  android.view.GestureDetector
 *  android.view.GestureDetector$OnGestureListener
 *  android.view.GestureDetector$SimpleOnGestureListener
 *  android.view.MotionEvent
 *  android.view.ScaleGestureDetector
 *  android.view.ScaleGestureDetector$OnScaleGestureListener
 *  android.view.ScaleGestureDetector$SimpleOnScaleGestureListener
 */
package com.carlos.common.imagepicker.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import com.carlos.common.imagepicker.util.RotationGestureDetector;
import com.carlos.common.imagepicker.view.CropImageView;

public class GestureCropImageView
extends CropImageView {
    private static final int DOUBLE_TAP_ZOOM_DURATION = 200;
    private ScaleGestureDetector mScaleDetector;
    private RotationGestureDetector mRotateDetector;
    private GestureDetector mGestureDetector;
    private float mMidPntX;
    private float mMidPntY;
    private boolean mIsRotateEnabled = true;
    private boolean mIsScaleEnabled = true;
    private int mDoubleTapScaleSteps = 5;

    public GestureCropImageView(Context context) {
        super(context);
    }

    public GestureCropImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GestureCropImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setScaleEnabled(boolean scaleEnabled) {
        this.mIsScaleEnabled = scaleEnabled;
    }

    public boolean isScaleEnabled() {
        return this.mIsScaleEnabled;
    }

    public void setRotateEnabled(boolean rotateEnabled) {
        this.mIsRotateEnabled = rotateEnabled;
    }

    public boolean isRotateEnabled() {
        return this.mIsRotateEnabled;
    }

    public void setDoubleTapScaleSteps(int doubleTapScaleSteps) {
        this.mDoubleTapScaleSteps = doubleTapScaleSteps;
    }

    public int getDoubleTapScaleSteps() {
        return this.mDoubleTapScaleSteps;
    }

    public boolean onTouchEvent(MotionEvent event) {
        if ((event.getAction() & 0xFF) == 0) {
            this.cancelAllAnimations();
        }
        if (event.getPointerCount() > 1) {
            this.mMidPntX = (event.getX(0) + event.getX(1)) / 2.0f;
            this.mMidPntY = (event.getY(0) + event.getY(1)) / 2.0f;
        }
        this.mGestureDetector.onTouchEvent(event);
        if (this.mIsScaleEnabled) {
            this.mScaleDetector.onTouchEvent(event);
        }
        if (this.mIsRotateEnabled) {
            this.mRotateDetector.onTouchEvent(event);
        }
        if ((event.getAction() & 0xFF) == 1) {
            this.setImageToWrapCropBounds();
        }
        return true;
    }

    @Override
    protected void init() {
        super.init();
        this.setupGestureListeners();
    }

    protected float getDoubleTapTargetScale() {
        return this.getCurrentScale() * (float)Math.pow(this.getMaxScale() / this.getMinScale(), 1.0f / (float)this.mDoubleTapScaleSteps);
    }

    private void setupGestureListeners() {
        this.mGestureDetector = new GestureDetector(this.getContext(), (GestureDetector.OnGestureListener)new GestureListener(), null, true);
        this.mScaleDetector = new ScaleGestureDetector(this.getContext(), (ScaleGestureDetector.OnScaleGestureListener)new ScaleListener());
        this.mRotateDetector = new RotationGestureDetector(new RotateListener());
    }

    private class RotateListener
    extends RotationGestureDetector.SimpleOnRotationGestureListener {
        private RotateListener() {
        }

        @Override
        public boolean onRotation(RotationGestureDetector rotationDetector) {
            GestureCropImageView.this.postRotate(rotationDetector.getAngle(), GestureCropImageView.this.mMidPntX, GestureCropImageView.this.mMidPntY);
            return true;
        }
    }

    private class GestureListener
    extends GestureDetector.SimpleOnGestureListener {
        private GestureListener() {
        }

        public boolean onDoubleTap(MotionEvent e) {
            GestureCropImageView.this.zoomImageToPosition(GestureCropImageView.this.getDoubleTapTargetScale(), e.getX(), e.getY(), 200L);
            return super.onDoubleTap(e);
        }

        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            GestureCropImageView.this.postTranslate(-distanceX, -distanceY);
            return true;
        }
    }

    private class ScaleListener
    extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        private ScaleListener() {
        }

        public boolean onScale(ScaleGestureDetector detector) {
            GestureCropImageView.this.postScale(detector.getScaleFactor(), GestureCropImageView.this.mMidPntX, GestureCropImageView.this.mMidPntY);
            return true;
        }
    }
}

