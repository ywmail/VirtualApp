/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.util.AttributeSet
 *  android.view.View
 *  androidx.annotation.NonNull
 *  androidx.coordinatorlayout.widget.CoordinatorLayout
 *  androidx.coordinatorlayout.widget.CoordinatorLayout$Behavior
 *  androidx.core.view.ViewPropertyAnimatorListener
 */
package com.carlos.common.imagepicker.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewPropertyAnimatorListener;
import com.carlos.common.imagepicker.widget.AnimatorUtil;

public class ScaleDownShowBehavior
extends CoordinatorLayout.Behavior<View> {
    private boolean isAnimateIng = false;
    private boolean isShow = true;

    public ScaleDownShowBehavior(Context context, AttributeSet attrs) {
    }

    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        return axes == 2 || super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, axes, type);
    }

    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        if ((dyConsumed > 0 || dyUnconsumed > 0) && !this.isAnimateIng && this.isShow) {
            AnimatorUtil.translateHide(child, new StateListener(){

                @Override
                public void onAnimationStart(View view) {
                    super.onAnimationStart(view);
                    ScaleDownShowBehavior.this.isShow = false;
                }
            });
        } else if (dyConsumed < 0 || dyUnconsumed < 0 && !this.isAnimateIng && !this.isShow) {
            AnimatorUtil.translateShow(child, new StateListener(){

                @Override
                public void onAnimationStart(View view) {
                    super.onAnimationStart(view);
                    ScaleDownShowBehavior.this.isShow = true;
                }
            });
        }
    }

    class StateListener
    implements ViewPropertyAnimatorListener {
        StateListener() {
        }

        public void onAnimationStart(View view) {
            ScaleDownShowBehavior.this.isAnimateIng = true;
        }

        public void onAnimationEnd(View view) {
            ScaleDownShowBehavior.this.isAnimateIng = false;
        }

        public void onAnimationCancel(View view) {
            ScaleDownShowBehavior.this.isAnimateIng = false;
        }
    }
}

