/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.view.View
 *  android.view.animation.AccelerateInterpolator
 *  android.view.animation.Interpolator
 *  androidx.core.view.ViewCompat
 *  androidx.core.view.ViewPropertyAnimatorListener
 *  androidx.interpolator.view.animation.LinearOutSlowInInterpolator
 */
package com.carlos.common.imagepicker.widget;

import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorListener;
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;

public class AnimatorUtil {
    private static LinearOutSlowInInterpolator FAST_OUT_SLOW_IN_INTERPOLATOR = new LinearOutSlowInInterpolator();
    private static AccelerateInterpolator LINER_INTERPOLATOR = new AccelerateInterpolator();

    public static void scaleShow(View view, ViewPropertyAnimatorListener viewPropertyAnimatorListener) {
        view.setVisibility(0);
        ViewCompat.animate((View)view).scaleX(1.0f).scaleY(1.0f).alpha(1.0f).setDuration(800L).setListener(viewPropertyAnimatorListener).setInterpolator((Interpolator)FAST_OUT_SLOW_IN_INTERPOLATOR).start();
    }

    public static void scaleHide(View view, ViewPropertyAnimatorListener viewPropertyAnimatorListener) {
        ViewCompat.animate((View)view).scaleX(0.0f).scaleY(0.0f).alpha(0.0f).setDuration(800L).setInterpolator((Interpolator)FAST_OUT_SLOW_IN_INTERPOLATOR).setListener(viewPropertyAnimatorListener).start();
    }

    public static void translateShow(View view, ViewPropertyAnimatorListener viewPropertyAnimatorListener) {
        view.setVisibility(0);
        ViewCompat.animate((View)view).translationY(0.0f).setDuration(400L).setListener(viewPropertyAnimatorListener).setInterpolator((Interpolator)FAST_OUT_SLOW_IN_INTERPOLATOR).start();
    }

    public static void translateHide(View view, ViewPropertyAnimatorListener viewPropertyAnimatorListener) {
        view.setVisibility(0);
        ViewCompat.animate((View)view).translationY(260.0f).setDuration(400L).setInterpolator((Interpolator)FAST_OUT_SLOW_IN_INTERPOLATOR).setListener(viewPropertyAnimatorListener).start();
    }
}

