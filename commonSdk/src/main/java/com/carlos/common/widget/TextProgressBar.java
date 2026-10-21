/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.graphics.Canvas
 *  android.graphics.Color
 *  android.graphics.Paint
 *  android.graphics.Path
 *  android.graphics.Rect
 *  android.graphics.RectF
 *  android.util.AttributeSet
 *  android.view.View
 *  android.view.ViewGroup
 *  android.widget.LinearLayout
 *  android.widget.ProgressBar
 *  com.kook.librelease.R$id
 *  com.kook.librelease.R$layout
 */
package com.carlos.common.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import com.kook.librelease.R;
import com.kook.librelease.StringFog;

public class TextProgressBar
extends LinearLayout {
    private static final String TAG = "TextProgressBar";
    String text = "0";
    Paint mPaint;
    private Rect textRect;
    private RectF textBackRectF;
    private ProgressBar progressBar;
    int progress;
    int proWidth;
    int proHeight;
    private Path path;
    private int mTextMargin;
    private int mTextMarginTop;

    public TextProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.initText(context);
    }

    public TextProgressBar(Context context) {
        super(context);
        this.initText(context);
    }

    public TextProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initText(context);
    }

    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.progress = this.progressBar.getProgress();
        if (this.progress != 0) {
            this.text = (int)((double)this.progress / (double)this.progressBar.getMax() * 100.0) + "%";
        }
        this.mPaint.getTextBounds(this.text, 0, this.text.length(), this.textRect);
        this.proWidth = this.progressBar.getWidth() - this.progressBar.getPaddingLeft() - this.progressBar.getPaddingRight();
        this.proHeight = this.progressBar.getHeight();
        int tvx = (int)((float)(this.progressBar.getLeft() + this.progressBar.getPaddingLeft()) + (float)this.proWidth * ((float)this.progress * 1.0f / (float)this.progressBar.getMax())) - this.textRect.centerX();
        int tvy = this.progressBar.getBottom() + this.textRect.height() + this.mTextMarginTop;
        if (tvx < 0) {
            tvx = this.progressBar.getLeft() + this.progressBar.getPaddingLeft();
        }
        if (tvx >= this.progressBar.getRight() - this.textRect.width()) {
            tvx = this.progressBar.getRight() - this.textRect.width() - this.progressBar.getPaddingRight();
        }
        if (this.textBackRectF == null) {
            this.textBackRectF = new RectF();
        }
        this.mPaint.setColor(Color.parseColor((String)"#FFB3CB"));
        this.textBackRectF.set((float)(tvx - this.mTextMargin), (float)(tvy + this.mTextMargin), (float)(tvx + this.textRect.width() + this.mTextMargin), (float)(this.textRect.height() + this.mTextMarginTop));
        canvas.drawRoundRect(this.textBackRectF, 10.0f, 10.0f, this.mPaint);
        if (this.path == null) {
            this.path = new Path();
        }
        this.path.reset();
        this.path.moveTo(this.textBackRectF.centerX(), this.textBackRectF.bottom - 5.0f);
        this.path.lineTo(this.textBackRectF.centerX() - 8.0f, this.textBackRectF.bottom);
        this.path.lineTo(this.textBackRectF.centerX() + 8.0f, this.textBackRectF.bottom);
        this.path.close();
        canvas.drawPath(this.path, this.mPaint);
        this.mPaint.setColor(-1);
        canvas.drawText(this.text, (float)tvx, (float)tvy, this.mPaint);
    }

    private void initText(Context context) {
        View inflate = View.inflate((Context)context, (int)R.layout.textprogressbar, (ViewGroup)this);
        this.progressBar = (ProgressBar)inflate.findViewById(R.id.progressbar1);
        this.mTextMargin = this.dp2Px(5.0f);
        this.mTextMarginTop = this.dp2Px(8.0f);
        this.setWillNotDraw(false);
        this.mPaint = new Paint();
        this.mPaint.setAntiAlias(true);
        this.mPaint.setColor(Color.parseColor((String)"#FFB3CB"));
        this.mPaint.setTextSize(25.0f);
        this.textRect = new Rect();
    }

    public void setProgress(int progress) {
        this.progressBar.setProgress(progress);
        this.invalidate();
    }

    public void setText(String text) {
        this.text = text;
        this.invalidate();
    }

    public int getProgress() {
        return this.progress;
    }

    public void setMax(int totalTime) {
        this.progressBar.setMax(totalTime);
    }

    public int dp2Px(float dp) {
        float scale = this.getResources().getDisplayMetrics().density;
        return (int)(dp * scale + 0.5f);
    }
}

