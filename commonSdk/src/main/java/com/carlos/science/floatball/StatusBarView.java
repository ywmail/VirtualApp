/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.content.Context
 *  android.view.View
 *  android.view.View$OnLayoutChangeListener
 *  android.view.ViewGroup$LayoutParams
 *  android.view.WindowManager
 *  android.view.WindowManager$LayoutParams
 */
package com.carlos.science.floatball;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import com.carlos.science.FloatBallManager;
import com.carlos.science.FloatBallUtil;

public class StatusBarView
extends View {
    private Context mContext;
    private FloatBallManager mFloatBallManager;
    private WindowManager.LayoutParams mLayoutParams;
    private boolean isAdded;
    private View.OnLayoutChangeListener layoutChangeListener = new View.OnLayoutChangeListener(){

        public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
            StatusBarView.this.mFloatBallManager.onStatusBarHeightChange();
        }
    };

    public StatusBarView(Context context, FloatBallManager floatBallManager) {
        super(context);
        this.mContext = context;
        this.mFloatBallManager = floatBallManager;
        this.mLayoutParams = FloatBallUtil.getStatusBarLayoutParams(context);
    }

    public void attachToWindow(WindowManager wm) {
        if (!this.isAdded) {
            this.addOnLayoutChangeListener(this.layoutChangeListener);
            wm.addView((View)this, (ViewGroup.LayoutParams)this.mLayoutParams);
            this.isAdded = true;
        }
    }

    public void detachFromWindow(WindowManager windowManager) {
        if (!this.isAdded) {
            return;
        }
        this.isAdded = false;
        this.removeOnLayoutChangeListener(this.layoutChangeListener);
        if (this.getContext() instanceof Activity) {
            windowManager.removeViewImmediate((View)this);
        } else {
            windowManager.removeView((View)this);
        }
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public int getStatusBarHeight() {
        int[] windowParams = new int[2];
        int[] screenParams = new int[2];
        this.getLocationInWindow(windowParams);
        this.getLocationOnScreen(screenParams);
        return screenParams[1] - windowParams[1];
    }
}

