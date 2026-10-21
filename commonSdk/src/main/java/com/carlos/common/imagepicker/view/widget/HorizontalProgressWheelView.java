/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.annotation.TargetApi
 *  android.content.Context
 *  android.graphics.Canvas
 *  android.graphics.Paint
 *  android.graphics.Paint$Style
 *  android.graphics.Rect
 *  android.util.AttributeSet
 *  android.view.MotionEvent
 *  android.view.View
 *  androidx.annotation.ColorInt
 *  androidx.core.content.ContextCompat
 *  com.kook.librelease.R$color
 *  com.kook.librelease.R$dimen
 */
package com.carlos.common.imagepicker.view.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import androidx.annotation.ColorInt;
import androidx.core.content.ContextCompat;
import com.kook.librelease.R;

public class HorizontalProgressWheelView
extends View {
    private final Rect mCanvasClipBounds = new Rect();
    private ScrollingListener mScrollingListener;
    private float mLastTouchedPosition;
    private Paint mProgressLinePaint;
    private int mProgressLineWidth;
    private int mProgressLineHeight;
    private int mProgressLineMargin;
    private boolean mScrollStarted;
    private float mTotalScrollDistance;
    private int mMiddleLineColor;

    public HorizontalProgressWheelView(Context context) {
        this(context, null);
    }

    public HorizontalProgressWheelView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorizontalProgressWheelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init();
    }

    @TargetApi(value=21)
    public HorizontalProgressWheelView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setScrollingListener(ScrollingListener scrollingListener) {
        this.mScrollingListener = scrollingListener;
    }

    public void setMiddleLineColor(@ColorInt int middleLineColor) {
        this.mMiddleLineColor = middleLineColor;
        this.invalidate();
    }

    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case 0: {
                this.mLastTouchedPosition = event.getX();
                break;
            }
            case 1: {
                if (this.mScrollingListener == null) break;
                this.mScrollStarted = false;
                this.mScrollingListener.onScrollEnd();
                break;
            }
            case 2: {
                float distance = event.getX() - this.mLastTouchedPosition;
                if (distance == 0.0f) break;
                if (!this.mScrollStarted) {
                    this.mScrollStarted = true;
                    if (this.mScrollingListener != null) {
                        this.mScrollingListener.onScrollStart();
                    }
                }
                this.onScrollEvent(event, distance);
            }
        }
        return true;
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.getClipBounds(this.mCanvasClipBounds);
        int linesCount = this.mCanvasClipBounds.width() / (this.mProgressLineWidth + this.mProgressLineMargin);
        float deltaX = this.mTotalScrollDistance % (float)(this.mProgressLineMargin + this.mProgressLineWidth);
        this.mProgressLinePaint.setColor(this.getResources().getColor(R.color.ucrop_color_progress_wheel_line));
        for (int i = 0; i < linesCount; ++i) {
            if (i < linesCount / 4) {
                this.mProgressLinePaint.setAlpha((int)(255.0f * ((float)i / (float)(linesCount / 4))));
            } else if (i > linesCount * 3 / 4) {
                this.mProgressLinePaint.setAlpha((int)(255.0f * ((float)(linesCount - i) / (float)(linesCount / 4))));
            } else {
                this.mProgressLinePaint.setAlpha(255);
            }
            canvas.drawLine(-deltaX + (float)this.mCanvasClipBounds.left + (float)(i * (this.mProgressLineWidth + this.mProgressLineMargin)), (float)this.mCanvasClipBounds.centerY() - (float)this.mProgressLineHeight / 4.0f, -deltaX + (float)this.mCanvasClipBounds.left + (float)(i * (this.mProgressLineWidth + this.mProgressLineMargin)), (float)this.mCanvasClipBounds.centerY() + (float)this.mProgressLineHeight / 4.0f, this.mProgressLinePaint);
        }
        this.mProgressLinePaint.setColor(this.mMiddleLineColor);
        canvas.drawLine((float)this.mCanvasClipBounds.centerX(), (float)this.mCanvasClipBounds.centerY() - (float)this.mProgressLineHeight / 2.0f, (float)this.mCanvasClipBounds.centerX(), (float)this.mCanvasClipBounds.centerY() + (float)this.mProgressLineHeight / 2.0f, this.mProgressLinePaint);
    }

    private void onScrollEvent(MotionEvent event, float distance) {
        this.mTotalScrollDistance -= distance;
        this.postInvalidate();
        this.mLastTouchedPosition = event.getX();
        if (this.mScrollingListener != null) {
            this.mScrollingListener.onScroll(-distance, this.mTotalScrollDistance);
        }
    }

    private void init() {
        this.mMiddleLineColor = ContextCompat.getColor((Context)this.getContext(), (int)R.color.ucrop_color_progress_wheel_line);
        this.mProgressLineWidth = this.getContext().getResources().getDimensionPixelSize(R.dimen.ucrop_width_horizontal_wheel_progress_line);
        this.mProgressLineHeight = this.getContext().getResources().getDimensionPixelSize(R.dimen.ucrop_height_horizontal_wheel_progress_line);
        this.mProgressLineMargin = this.getContext().getResources().getDimensionPixelSize(R.dimen.ucrop_margin_horizontal_wheel_progress_line);
        this.mProgressLinePaint = new Paint(1);
        this.mProgressLinePaint.setStyle(Paint.Style.STROKE);
        this.mProgressLinePaint.setStrokeWidth((float)this.mProgressLineWidth);
    }

    public static interface ScrollingListener {
        public void onScrollStart();

        public void onScroll(float var1, float var2);

        public void onScrollEnd();
    }
}

