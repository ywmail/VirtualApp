/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.view.MotionEvent
 *  android.view.ScaleGestureDetector
 *  android.view.ScaleGestureDetector$OnScaleGestureListener
 *  android.view.VelocityTracker
 *  android.view.ViewConfiguration
 */
package com.carlos.common.imagepicker.photoview;

import android.content.Context;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import com.carlos.common.imagepicker.photoview.OnGestureListener;
import com.carlos.common.imagepicker.photoview.Util;

class CustomGestureDetector {
    private static final int INVALID_POINTER_ID = -1;
    private int mActivePointerId = -1;
    private int mActivePointerIndex = 0;
    private final ScaleGestureDetector mDetector;
    private VelocityTracker mVelocityTracker;
    private boolean mIsDragging;
    private float mLastTouchX;
    private float mLastTouchY;
    private final float mTouchSlop;
    private final float mMinimumVelocity;
    private OnGestureListener mListener;

    CustomGestureDetector(Context context, OnGestureListener listener) {
        ViewConfiguration configuration = ViewConfiguration.get((Context)context);
        this.mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
        this.mTouchSlop = configuration.getScaledTouchSlop();
        this.mListener = listener;
        ScaleGestureDetector.OnScaleGestureListener mScaleListener = new ScaleGestureDetector.OnScaleGestureListener(){
            private float lastFocusX;
            private float lastFocusY = 0.0f;

            public boolean onScale(ScaleGestureDetector detector) {
                float scaleFactor = detector.getScaleFactor();
                if (Float.isNaN(scaleFactor) || Float.isInfinite(scaleFactor)) {
                    return false;
                }
                if (scaleFactor >= 0.0f) {
                    CustomGestureDetector.this.mListener.onScale(scaleFactor, detector.getFocusX(), detector.getFocusY(), detector.getFocusX() - this.lastFocusX, detector.getFocusY() - this.lastFocusY);
                    this.lastFocusX = detector.getFocusX();
                    this.lastFocusY = detector.getFocusY();
                }
                return true;
            }

            public boolean onScaleBegin(ScaleGestureDetector detector) {
                this.lastFocusX = detector.getFocusX();
                this.lastFocusY = detector.getFocusY();
                return true;
            }

            public void onScaleEnd(ScaleGestureDetector detector) {
            }
        };
        this.mDetector = new ScaleGestureDetector(context, mScaleListener);
    }

    private float getActiveX(MotionEvent ev) {
        try {
            return ev.getX(this.mActivePointerIndex);
        }
        catch (Exception e) {
            return ev.getX();
        }
    }

    private float getActiveY(MotionEvent ev) {
        try {
            return ev.getY(this.mActivePointerIndex);
        }
        catch (Exception e) {
            return ev.getY();
        }
    }

    public boolean isScaling() {
        return this.mDetector.isInProgress();
    }

    public boolean isDragging() {
        return this.mIsDragging;
    }

    public boolean onTouchEvent(MotionEvent ev) {
        try {
            this.mDetector.onTouchEvent(ev);
            return this.processTouchEvent(ev);
        }
        catch (IllegalArgumentException e) {
            return true;
        }
    }

    private boolean processTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action & 0xFF) {
            case 0: {
                this.mActivePointerId = ev.getPointerId(0);
                this.mVelocityTracker = VelocityTracker.obtain();
                if (null != this.mVelocityTracker) {
                    this.mVelocityTracker.addMovement(ev);
                }
                this.mLastTouchX = this.getActiveX(ev);
                this.mLastTouchY = this.getActiveY(ev);
                this.mIsDragging = false;
                break;
            }
            case 2: {
                float x = this.getActiveX(ev);
                float y = this.getActiveY(ev);
                float dx = x - this.mLastTouchX;
                float dy = y - this.mLastTouchY;
                if (!this.mIsDragging) {
                    boolean bl = this.mIsDragging = Math.sqrt(dx * dx + dy * dy) >= (double)this.mTouchSlop;
                }
                if (!this.mIsDragging) break;
                this.mListener.onDrag(dx, dy);
                this.mLastTouchX = x;
                this.mLastTouchY = y;
                if (null == this.mVelocityTracker) break;
                this.mVelocityTracker.addMovement(ev);
                break;
            }
            case 3: {
                this.mActivePointerId = -1;
                if (null == this.mVelocityTracker) break;
                this.mVelocityTracker.recycle();
                this.mVelocityTracker = null;
                break;
            }
            case 1: {
                this.mActivePointerId = -1;
                if (this.mIsDragging && null != this.mVelocityTracker) {
                    this.mLastTouchX = this.getActiveX(ev);
                    this.mLastTouchY = this.getActiveY(ev);
                    this.mVelocityTracker.addMovement(ev);
                    this.mVelocityTracker.computeCurrentVelocity(1000);
                    float vX = this.mVelocityTracker.getXVelocity();
                    float vY = this.mVelocityTracker.getYVelocity();
                    if (Math.max(Math.abs(vX), Math.abs(vY)) >= this.mMinimumVelocity) {
                        this.mListener.onFling(this.mLastTouchX, this.mLastTouchY, -vX, -vY);
                    }
                }
                if (null == this.mVelocityTracker) break;
                this.mVelocityTracker.recycle();
                this.mVelocityTracker = null;
                break;
            }
            case 6: {
                int pointerIndex = Util.getPointerIndex(ev.getAction());
                int pointerId = ev.getPointerId(pointerIndex);
                if (pointerId != this.mActivePointerId) break;
                int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                this.mActivePointerId = ev.getPointerId(newPointerIndex);
                this.mLastTouchX = ev.getX(newPointerIndex);
                this.mLastTouchY = ev.getY(newPointerIndex);
            }
        }
        this.mActivePointerIndex = ev.findPointerIndex(this.mActivePointerId != -1 ? this.mActivePointerId : 0);
        return true;
    }
}

