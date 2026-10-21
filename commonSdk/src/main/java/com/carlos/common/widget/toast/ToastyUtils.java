/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.graphics.PorterDuff$Mode
 *  android.graphics.drawable.Drawable
 *  android.graphics.drawable.NinePatchDrawable
 *  android.os.Build$VERSION
 *  android.view.View
 *  androidx.annotation.ColorInt
 *  androidx.annotation.ColorRes
 *  androidx.annotation.DrawableRes
 *  androidx.annotation.NonNull
 *  androidx.core.content.ContextCompat
 *  com.kook.librelease.R$drawable
 */
package com.carlos.common.widget.toast;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Build;
import android.view.View;
import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import com.kook.librelease.R;

final class ToastyUtils {
    private ToastyUtils() {
    }

    static Drawable tintIcon(@NonNull Drawable drawable2, @ColorInt int tintColor) {
        drawable2.setColorFilter(tintColor, PorterDuff.Mode.SRC_IN);
        return drawable2;
    }

    static Drawable tint9PatchDrawableFrame(@NonNull Context context, @ColorInt int tintColor) {
        NinePatchDrawable toastDrawable = (NinePatchDrawable)ToastyUtils.getDrawable(context, R.drawable.toast_frame);
        return ToastyUtils.tintIcon((Drawable)toastDrawable, tintColor);
    }

    static void setBackground(@NonNull View view, Drawable drawable2) {
        if (Build.VERSION.SDK_INT >= 16) {
            view.setBackground(drawable2);
        } else {
            view.setBackgroundDrawable(drawable2);
        }
    }

    static Drawable getDrawable(@NonNull Context context, @DrawableRes int id2) {
        return ContextCompat.getDrawable((Context)context, (int)id2);
    }

    static int getColor(@NonNull Context context, @ColorRes int color2) {
        return ContextCompat.getColor((Context)context, (int)color2);
    }
}

