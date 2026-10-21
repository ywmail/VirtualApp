/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.annotation.TargetApi
 *  android.content.Context
 *  android.content.res.ColorStateList
 *  android.content.res.TypedArray
 *  android.graphics.Canvas
 *  android.graphics.Paint
 *  android.graphics.Paint$Style
 *  android.graphics.Rect
 *  android.text.TextUtils
 *  android.util.AttributeSet
 *  android.widget.TextView
 *  androidx.annotation.ColorInt
 *  androidx.annotation.NonNull
 *  androidx.core.content.ContextCompat
 *  com.kook.librelease.R$color
 *  com.kook.librelease.R$dimen
 *  com.kook.librelease.R$styleable
 */
package com.carlos.common.imagepicker.view.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import com.carlos.common.imagepicker.entity.AspectRatio;
import com.kook.librelease.R;
import com.kook.librelease.StringFog;
import java.util.Locale;

public class AspectRatioTextView
extends TextView {
    private final Rect mCanvasClipBounds = new Rect();
    private Paint mDotPaint;
    private int mDotSize;
    private float mAspectRatio;
    private String mAspectRatioTitle;
    private float mAspectRatioX;
    private float mAspectRatioY;

    public AspectRatioTextView(Context context) {
        this(context, null);
    }

    public AspectRatioTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AspectRatioTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ucrop_AspectRatioTextView);
        this.init(a);
    }

    @TargetApi(value=21)
    public AspectRatioTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ucrop_AspectRatioTextView);
        this.init(a);
    }

    public void setActiveColor(@ColorInt int activeColor) {
        this.applyActiveColor(activeColor);
        this.invalidate();
    }

    public void setAspectRatio(@NonNull AspectRatio aspectRatio) {
        this.mAspectRatioTitle = aspectRatio.getAspectRatioTitle();
        this.mAspectRatioX = aspectRatio.getAspectRatioX();
        this.mAspectRatioY = aspectRatio.getAspectRatioY();
        this.mAspectRatio = this.mAspectRatioX == 0.0f || this.mAspectRatioY == 0.0f ? 0.0f : this.mAspectRatioX / this.mAspectRatioY;
        this.setTitle();
    }

    public float getAspectRatio(boolean toggleRatio) {
        if (toggleRatio) {
            this.toggleAspectRatio();
            this.setTitle();
        }
        return this.mAspectRatio;
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.isSelected()) {
            canvas.getClipBounds(this.mCanvasClipBounds);
            canvas.drawCircle((float)(this.mCanvasClipBounds.right - this.mCanvasClipBounds.left) / 2.0f, (float)(this.mCanvasClipBounds.bottom - this.mDotSize), (float)(this.mDotSize / 2), this.mDotPaint);
        }
    }

    private void init(@NonNull TypedArray a) {
        this.setGravity(1);
        this.mAspectRatioTitle = a.getString(R.styleable.ucrop_AspectRatioTextView_ucrop_artv_ratio_title);
        this.mAspectRatioX = a.getFloat(R.styleable.ucrop_AspectRatioTextView_ucrop_artv_ratio_x, 0.0f);
        this.mAspectRatioY = a.getFloat(R.styleable.ucrop_AspectRatioTextView_ucrop_artv_ratio_y, 0.0f);
        this.mAspectRatio = this.mAspectRatioX == 0.0f || this.mAspectRatioY == 0.0f ? 0.0f : this.mAspectRatioX / this.mAspectRatioY;
        this.mDotSize = this.getContext().getResources().getDimensionPixelSize(R.dimen.ucrop_size_dot_scale_text_view);
        this.mDotPaint = new Paint(1);
        this.mDotPaint.setStyle(Paint.Style.FILL);
        this.setTitle();
        int activeColor = this.getResources().getColor(R.color.ucrop_color_widget_active);
        this.applyActiveColor(activeColor);
        a.recycle();
    }

    private void applyActiveColor(@ColorInt int activeColor) {
        if (this.mDotPaint != null) {
            this.mDotPaint.setColor(activeColor);
        }
        ColorStateList textViewColorStateList = new ColorStateList((int[][])new int[][]{{0x10100A1}, {0}}, new int[]{activeColor, ContextCompat.getColor((Context)this.getContext(), (int)R.color.ucrop_color_widget)});
        this.setTextColor(textViewColorStateList);
    }

    private void toggleAspectRatio() {
        if (this.mAspectRatio != 0.0f) {
            float tempRatioW = this.mAspectRatioX;
            this.mAspectRatioX = this.mAspectRatioY;
            this.mAspectRatioY = tempRatioW;
            this.mAspectRatio = this.mAspectRatioX / this.mAspectRatioY;
        }
    }

    private void setTitle() {
        if (!TextUtils.isEmpty((CharSequence)this.mAspectRatioTitle)) {
            this.setText(this.mAspectRatioTitle);
        } else {
            this.setText(String.format(Locale.US, "%d:%d", (int)this.mAspectRatioX, (int)this.mAspectRatioY));
        }
    }
}

