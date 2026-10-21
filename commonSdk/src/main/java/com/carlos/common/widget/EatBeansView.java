/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.animation.Animator
 *  android.animation.ValueAnimator
 *  android.content.Context
 *  android.graphics.Canvas
 *  android.graphics.Paint
 *  android.graphics.Paint$Style
 *  android.graphics.RectF
 *  android.util.AttributeSet
 */
package com.carlos.common.widget;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import com.carlos.common.widget.BaseView;

public class EatBeansView
extends BaseView {
    int eatSpeed = 5;
    private Paint mPaint;
    private Paint mPaintEye;
    private float mWidth = 0.0f;
    private float mHigh = 0.0f;
    private float mPadding = 5.0f;
    private float eatErWidth = 60.0f;
    private float eatErPositionX = 0.0f;
    private float beansWidth = 10.0f;
    private float mAngle;
    private float eatErStartAngle = this.mAngle = 34.0f;
    private float eatErEndAngle = 360.0f - 2.0f * this.eatErStartAngle;

    public EatBeansView(Context context) {
        super(context);
    }

    public EatBeansView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EatBeansView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        this.mWidth = this.getMeasuredWidth();
        this.mHigh = this.getMeasuredHeight();
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float eatRightX = this.mPadding + this.eatErWidth + this.eatErPositionX;
        RectF rectF = new RectF(this.mPadding + this.eatErPositionX, this.mHigh / 2.0f - this.eatErWidth / 2.0f, eatRightX, this.mHigh / 2.0f + this.eatErWidth / 2.0f);
        canvas.drawArc(rectF, this.eatErStartAngle, this.eatErEndAngle, true, this.mPaint);
        canvas.drawCircle(this.mPadding + this.eatErPositionX + this.eatErWidth / 2.0f, this.mHigh / 2.0f - this.eatErWidth / 4.0f, this.beansWidth / 2.0f, this.mPaintEye);
        int beansCount = (int)((this.mWidth - this.mPadding * 2.0f - this.eatErWidth) / this.beansWidth / 2.0f);
        for (int i = 0; i < beansCount; ++i) {
            float x = (float)(beansCount * i) + this.beansWidth / 2.0f + this.mPadding + this.eatErWidth;
            if (!(x > eatRightX)) continue;
            canvas.drawCircle(x, this.mHigh / 2.0f, this.beansWidth / 2.0f, this.mPaint);
        }
    }

    private void initPaint() {
        this.mPaint = new Paint();
        this.mPaint.setAntiAlias(true);
        this.mPaint.setStyle(Paint.Style.FILL);
        this.mPaint.setColor(-1);
        this.mPaintEye = new Paint();
        this.mPaintEye.setAntiAlias(true);
        this.mPaintEye.setStyle(Paint.Style.FILL);
        this.mPaintEye.setColor(-16777216);
    }

    public void setViewColor(int color2) {
        this.mPaint.setColor(color2);
        this.postInvalidate();
    }

    public void setEyeColor(int color2) {
        this.mPaintEye.setColor(color2);
        this.postInvalidate();
    }

    @Override
    protected void InitPaint() {
        this.initPaint();
    }

    @Override
    protected void OnAnimationUpdate(ValueAnimator valueAnimator) {
        float mAnimatedValue = ((Float)valueAnimator.getAnimatedValue()).floatValue();
        this.eatErPositionX = (this.mWidth - 2.0f * this.mPadding - this.eatErWidth) * mAnimatedValue;
        this.eatErStartAngle = this.mAngle * (1.0f - (mAnimatedValue * (float)this.eatSpeed - (float)((int)(mAnimatedValue * (float)this.eatSpeed))));
        this.eatErEndAngle = 360.0f - this.eatErStartAngle * 2.0f;
        this.invalidate();
    }

    @Override
    protected void OnAnimationRepeat(Animator animation) {
    }

    @Override
    protected int OnStopAnim() {
        this.eatErPositionX = 0.0f;
        this.postInvalidate();
        return 1;
    }

    @Override
    protected int SetAnimRepeatMode() {
        return 1;
    }

    @Override
    protected void AnimIsRunning() {
    }

    @Override
    protected int SetAnimRepeatCount() {
        return -1;
    }
}

