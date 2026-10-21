/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.TypedArray
 *  android.graphics.Canvas
 *  android.graphics.Color
 *  android.graphics.Paint
 *  android.graphics.Paint$Align
 *  android.graphics.Path
 *  android.util.AttributeSet
 *  android.view.View
 *  android.view.View$MeasureSpec
 *  com.kook.librelease.R$styleable
 */
package com.carlos.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import com.kook.librelease.R;
import com.kook.librelease.StringFog;

public class LabelView
extends View {
    private static final int DEFAULT_DEGREES = 45;
    private String mTextContent;
    private int mTextColor;
    private float mTextSize;
    private boolean mTextBold;
    private boolean mFillTriangle;
    private boolean mTextAllCaps;
    private int mBackgroundColor;
    private float mMinSize;
    private float mPadding;
    private int mGravity;
    private Paint mTextPaint = new Paint(1);
    private Paint mBackgroundPaint = new Paint(1);
    private Path mPath = new Path();

    public LabelView(Context context) {
        this(context, null);
    }

    public LabelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.obtainAttributes(context, attrs);
        this.mTextPaint.setTextAlign(Paint.Align.CENTER);
    }

    private void obtainAttributes(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.LabelView);
        this.mTextContent = ta.getString(R.styleable.LabelView_lv_text);
        this.mTextColor = ta.getColor(R.styleable.LabelView_lv_text_color, Color.parseColor((String)"#ffffff"));
        this.mTextSize = ta.getDimension(R.styleable.LabelView_lv_text_size, (float)this.sp2px(11.0f));
        this.mTextBold = ta.getBoolean(R.styleable.LabelView_lv_text_bold, true);
        this.mTextAllCaps = ta.getBoolean(R.styleable.LabelView_lv_text_all_caps, true);
        this.mFillTriangle = ta.getBoolean(R.styleable.LabelView_lv_fill_triangle, false);
        this.mBackgroundColor = ta.getColor(R.styleable.LabelView_lv_background_color, Color.parseColor((String)"#FF4081"));
        this.mMinSize = ta.getDimension(R.styleable.LabelView_lv_min_size, this.mFillTriangle ? (float)this.dp2px(35.0f) : (float)this.dp2px(50.0f));
        this.mPadding = ta.getDimension(R.styleable.LabelView_lv_padding, (float)this.dp2px(3.5f));
        this.mGravity = ta.getInt(R.styleable.LabelView_lv_gravity, 51);
        ta.recycle();
    }

    public String getText() {
        return this.mTextContent;
    }

    public void setText(String text) {
        this.mTextContent = text;
        this.invalidate();
    }

    public int getTextColor() {
        return this.mTextColor;
    }

    public void setTextColor(int textColor) {
        this.mTextColor = textColor;
        this.invalidate();
    }

    public float getTextSize() {
        return this.mTextSize;
    }

    public void setTextSize(float textSize) {
        this.mTextSize = this.sp2px(textSize);
        this.invalidate();
    }

    public boolean isTextBold() {
        return this.mTextBold;
    }

    public void setTextBold(boolean textBold) {
        this.mTextBold = textBold;
        this.invalidate();
    }

    public boolean isFillTriangle() {
        return this.mFillTriangle;
    }

    public void setFillTriangle(boolean fillTriangle) {
        this.mFillTriangle = fillTriangle;
        this.invalidate();
    }

    public boolean isTextAllCaps() {
        return this.mTextAllCaps;
    }

    public void setTextAllCaps(boolean textAllCaps) {
        this.mTextAllCaps = textAllCaps;
        this.invalidate();
    }

    public int getBgColor() {
        return this.mBackgroundColor;
    }

    public void setBgColor(int backgroundColor) {
        this.mBackgroundColor = backgroundColor;
        this.invalidate();
    }

    public float getMinSize() {
        return this.mMinSize;
    }

    public void setMinSize(float minSize) {
        this.mMinSize = this.dp2px(minSize);
        this.invalidate();
    }

    public float getPadding() {
        return this.mPadding;
    }

    public void setPadding(float padding) {
        this.mPadding = this.dp2px(padding);
        this.invalidate();
    }

    public int getGravity() {
        return this.mGravity;
    }

    public void setGravity(int gravity) {
        this.mGravity = gravity;
    }

    protected void onDraw(Canvas canvas) {
        int size = this.getHeight();
        this.mTextPaint.setColor(this.mTextColor);
        this.mTextPaint.setTextSize(this.mTextSize);
        this.mTextPaint.setFakeBoldText(this.mTextBold);
        this.mBackgroundPaint.setColor(this.mBackgroundColor);
        float textHeight = this.mTextPaint.descent() - this.mTextPaint.ascent();
        if (this.mFillTriangle) {
            if (this.mGravity == 51) {
                this.mPath.reset();
                this.mPath.moveTo(0.0f, 0.0f);
                this.mPath.lineTo(0.0f, (float)size);
                this.mPath.lineTo((float)size, 0.0f);
                this.mPath.close();
                canvas.drawPath(this.mPath, this.mBackgroundPaint);
                this.drawTextWhenFill(size, -45.0f, canvas, true);
            } else if (this.mGravity == 53) {
                this.mPath.reset();
                this.mPath.moveTo((float)size, 0.0f);
                this.mPath.lineTo(0.0f, 0.0f);
                this.mPath.lineTo((float)size, (float)size);
                this.mPath.close();
                canvas.drawPath(this.mPath, this.mBackgroundPaint);
                this.drawTextWhenFill(size, 45.0f, canvas, true);
            } else if (this.mGravity == 83) {
                this.mPath.reset();
                this.mPath.moveTo(0.0f, (float)size);
                this.mPath.lineTo(0.0f, 0.0f);
                this.mPath.lineTo((float)size, (float)size);
                this.mPath.close();
                canvas.drawPath(this.mPath, this.mBackgroundPaint);
                this.drawTextWhenFill(size, 45.0f, canvas, false);
            } else if (this.mGravity == 85) {
                this.mPath.reset();
                this.mPath.moveTo((float)size, (float)size);
                this.mPath.lineTo(0.0f, (float)size);
                this.mPath.lineTo((float)size, 0.0f);
                this.mPath.close();
                canvas.drawPath(this.mPath, this.mBackgroundPaint);
                this.drawTextWhenFill(size, -45.0f, canvas, false);
            }
        } else {
            double delta = (double)(textHeight + this.mPadding * 2.0f) * Math.sqrt(2.0);
            if (this.mGravity == 51) {
                this.mPath.reset();
                this.mPath.moveTo(0.0f, (float)((double)size - delta));
                this.mPath.lineTo(0.0f, (float)size);
                this.mPath.lineTo((float)size, 0.0f);
                this.mPath.lineTo((float)((double)size - delta), 0.0f);
                this.mPath.close();
                canvas.drawPath(this.mPath, this.mBackgroundPaint);
                this.drawText(size, -45.0f, canvas, textHeight, true);
            } else if (this.mGravity == 53) {
                this.mPath.reset();
                this.mPath.moveTo(0.0f, 0.0f);
                this.mPath.lineTo((float)delta, 0.0f);
                this.mPath.lineTo((float)size, (float)((double)size - delta));
                this.mPath.lineTo((float)size, (float)size);
                this.mPath.close();
                canvas.drawPath(this.mPath, this.mBackgroundPaint);
                this.drawText(size, 45.0f, canvas, textHeight, true);
            } else if (this.mGravity == 83) {
                this.mPath.reset();
                this.mPath.moveTo(0.0f, 0.0f);
                this.mPath.lineTo(0.0f, (float)delta);
                this.mPath.lineTo((float)((double)size - delta), (float)size);
                this.mPath.lineTo((float)size, (float)size);
                this.mPath.close();
                canvas.drawPath(this.mPath, this.mBackgroundPaint);
                this.drawText(size, 45.0f, canvas, textHeight, false);
            } else if (this.mGravity == 85) {
                this.mPath.reset();
                this.mPath.moveTo(0.0f, (float)size);
                this.mPath.lineTo((float)delta, (float)size);
                this.mPath.lineTo((float)size, (float)delta);
                this.mPath.lineTo((float)size, 0.0f);
                this.mPath.close();
                canvas.drawPath(this.mPath, this.mBackgroundPaint);
                this.drawText(size, -45.0f, canvas, textHeight, false);
            }
        }
    }

    private void drawText(int size, float degrees, Canvas canvas, float textHeight, boolean isTop) {
        canvas.save();
        canvas.rotate(degrees, (float)size / 2.0f, (float)size / 2.0f);
        float delta = isTop ? -(textHeight + this.mPadding * 2.0f) / 2.0f : (textHeight + this.mPadding * 2.0f) / 2.0f;
        float textBaseY = (float)(size / 2) - (this.mTextPaint.descent() + this.mTextPaint.ascent()) / 2.0f + delta;
        canvas.drawText(this.mTextAllCaps ? this.mTextContent.toUpperCase() : this.mTextContent, (float)(this.getPaddingLeft() + (size - this.getPaddingLeft() - this.getPaddingRight()) / 2), textBaseY, this.mTextPaint);
        canvas.restore();
    }

    private void drawTextWhenFill(int size, float degrees, Canvas canvas, boolean isTop) {
        canvas.save();
        canvas.rotate(degrees, (float)size / 2.0f, (float)size / 2.0f);
        float delta = isTop ? (float)(-size / 4) : (float)(size / 4);
        float textBaseY = (float)(size / 2) - (this.mTextPaint.descent() + this.mTextPaint.ascent()) / 2.0f + delta;
        canvas.drawText(this.mTextAllCaps ? this.mTextContent.toUpperCase() : this.mTextContent, (float)(this.getPaddingLeft() + (size - this.getPaddingLeft() - this.getPaddingRight()) / 2), textBaseY, this.mTextPaint);
        canvas.restore();
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measuredWidth = this.measureWidth(widthMeasureSpec);
        this.setMeasuredDimension(measuredWidth, measuredWidth);
    }

    private int measureWidth(int widthMeasureSpec) {
        int result;
        int specMode = View.MeasureSpec.getMode((int)widthMeasureSpec);
        int specSize = View.MeasureSpec.getSize((int)widthMeasureSpec);
        if (specMode == 0x40000000) {
            result = specSize;
        } else {
            int padding = this.getPaddingLeft() + this.getPaddingRight();
            this.mTextPaint.setColor(this.mTextColor);
            this.mTextPaint.setTextSize(this.mTextSize);
            float textWidth = this.mTextPaint.measureText(this.mTextContent + "");
            result = (int)((double)(padding + (int)textWidth) * Math.sqrt(2.0));
            if (specMode == Integer.MIN_VALUE) {
                result = Math.min(result, specSize);
            }
            result = Math.max((int)this.mMinSize, result);
        }
        return result;
    }

    protected int dp2px(float dp) {
        float scale = this.getResources().getDisplayMetrics().density;
        return (int)(dp * scale + 0.5f);
    }

    protected int sp2px(float sp) {
        float scale = this.getResources().getDisplayMetrics().scaledDensity;
        return (int)(sp * scale + 0.5f);
    }
}

