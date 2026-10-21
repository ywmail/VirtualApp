/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.annotation.SuppressLint
 *  android.annotation.TargetApi
 *  android.app.Activity
 *  android.content.Context
 *  android.content.res.Resources
 *  android.content.res.TypedArray
 *  android.graphics.drawable.Drawable
 *  android.os.Build$VERSION
 *  android.util.DisplayMetrics
 *  android.util.TypedValue
 *  android.view.View
 *  android.view.ViewConfiguration
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.view.Window
 *  android.view.WindowManager$LayoutParams
 *  android.widget.FrameLayout$LayoutParams
 */
package com.carlos.common.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import com.kook.librelease.StringFog;
import java.lang.reflect.Method;

public class SystemBarTintManager {
    public static final int DEFAULT_TINT_COLOR = -1728053248;
    private static String sNavBarOverride;
    private final SystemBarConfig mConfig;
    private boolean mStatusBarAvailable;
    private boolean mNavBarAvailable;
    private boolean mStatusBarTintEnabled;
    private boolean mNavBarTintEnabled;
    private View mStatusBarTintView;
    private View mNavBarTintView;

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @TargetApi(value=19)
    public SystemBarTintManager(Activity activity) {
        Window win = activity.getWindow();
        ViewGroup decorViewGroup = (ViewGroup)win.getDecorView();
        if (Build.VERSION.SDK_INT >= 19) {
            int[] attrs = new int[]{16843759, 16843760};
            TypedArray a = activity.obtainStyledAttributes(attrs);
            try {
                this.mStatusBarAvailable = a.getBoolean(0, false);
                this.mNavBarAvailable = a.getBoolean(1, false);
            }
            finally {
                a.recycle();
            }
            WindowManager.LayoutParams winParams = win.getAttributes();
            int bits = 0x4000000;
            if ((winParams.flags & bits) != 0) {
                this.mStatusBarAvailable = true;
            }
            if ((winParams.flags & (bits = 0x8000000)) != 0) {
                this.mNavBarAvailable = true;
            }
        }
        this.mConfig = new SystemBarConfig(activity, this.mStatusBarAvailable, this.mNavBarAvailable);
        if (!this.mConfig.hasNavigtionBar()) {
            this.mNavBarAvailable = false;
        }
        if (this.mStatusBarAvailable) {
            this.setupStatusBarView((Context)activity, decorViewGroup);
        }
        if (this.mNavBarAvailable) {
            this.setupNavBarView((Context)activity, decorViewGroup);
        }
    }

    public void setStatusBarTintEnabled(boolean enabled) {
        this.mStatusBarTintEnabled = enabled;
        if (this.mStatusBarAvailable) {
            this.mStatusBarTintView.setVisibility(enabled ? 0 : 8);
        }
    }

    public void setNavigationBarTintEnabled(boolean enabled) {
        this.mNavBarTintEnabled = enabled;
        if (this.mNavBarAvailable) {
            this.mNavBarTintView.setVisibility(enabled ? 0 : 8);
        }
    }

    public void setTintColor(int color2) {
        this.setStatusBarTintColor(color2);
        this.setNavigationBarTintColor(color2);
    }

    public void setTintResource(int res) {
        this.setStatusBarTintResource(res);
        this.setNavigationBarTintResource(res);
    }

    public void setTintDrawable(Drawable drawable2) {
        this.setStatusBarTintDrawable(drawable2);
        this.setNavigationBarTintDrawable(drawable2);
    }

    public void setTintAlpha(float alpha) {
        this.setStatusBarAlpha(alpha);
        this.setNavigationBarAlpha(alpha);
    }

    public void setStatusBarTintColor(int color2) {
        if (this.mStatusBarAvailable) {
            this.mStatusBarTintView.setBackgroundColor(color2);
        }
    }

    public void setStatusBarTintResource(int res) {
        if (this.mStatusBarAvailable) {
            this.mStatusBarTintView.setBackgroundResource(res);
        }
    }

    public void setStatusBarTintDrawable(Drawable drawable2) {
        if (this.mStatusBarAvailable) {
            this.mStatusBarTintView.setBackgroundDrawable(drawable2);
        }
    }

    @TargetApi(value=11)
    public void setStatusBarAlpha(float alpha) {
        if (this.mStatusBarAvailable && Build.VERSION.SDK_INT >= 11) {
            this.mStatusBarTintView.setAlpha(alpha);
        }
    }

    public void setNavigationBarTintColor(int color2) {
        if (this.mNavBarAvailable) {
            this.mNavBarTintView.setBackgroundColor(color2);
        }
    }

    public void setNavigationBarTintResource(int res) {
        if (this.mNavBarAvailable) {
            this.mNavBarTintView.setBackgroundResource(res);
        }
    }

    public void setNavigationBarTintDrawable(Drawable drawable2) {
        if (this.mNavBarAvailable) {
            this.mNavBarTintView.setBackgroundDrawable(drawable2);
        }
    }

    @TargetApi(value=11)
    public void setNavigationBarAlpha(float alpha) {
        if (this.mNavBarAvailable && Build.VERSION.SDK_INT >= 11) {
            this.mNavBarTintView.setAlpha(alpha);
        }
    }

    public SystemBarConfig getConfig() {
        return this.mConfig;
    }

    public boolean isStatusBarTintEnabled() {
        return this.mStatusBarTintEnabled;
    }

    public boolean isNavBarTintEnabled() {
        return this.mNavBarTintEnabled;
    }

    private void setupStatusBarView(Context context, ViewGroup decorViewGroup) {
        this.mStatusBarTintView = new View(context);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(-1, this.mConfig.getStatusBarHeight());
        params.gravity = 48;
        if (this.mNavBarAvailable && !this.mConfig.isNavigationAtBottom()) {
            params.rightMargin = this.mConfig.getNavigationBarWidth();
        }
        this.mStatusBarTintView.setLayoutParams((ViewGroup.LayoutParams)params);
        this.mStatusBarTintView.setBackgroundColor(-1728053248);
        this.mStatusBarTintView.setVisibility(8);
        decorViewGroup.addView(this.mStatusBarTintView);
    }

    private void setupNavBarView(Context context, ViewGroup decorViewGroup) {
        FrameLayout.LayoutParams params;
        this.mNavBarTintView = new View(context);
        if (this.mConfig.isNavigationAtBottom()) {
            params = new FrameLayout.LayoutParams(-1, this.mConfig.getNavigationBarHeight());
            params.gravity = 80;
        } else {
            params = new FrameLayout.LayoutParams(this.mConfig.getNavigationBarWidth(), -1);
            params.gravity = 5;
        }
        this.mNavBarTintView.setLayoutParams((ViewGroup.LayoutParams)params);
        this.mNavBarTintView.setBackgroundColor(-1728053248);
        this.mNavBarTintView.setVisibility(8);
        decorViewGroup.addView(this.mNavBarTintView);
    }

    static {
        if (Build.VERSION.SDK_INT >= 19) {
            try {
                Class<?> c = Class.forName("android.os.SystemProperties");
                Method m = c.getDeclaredMethod("get", String.class);
                m.setAccessible(true);
                sNavBarOverride = (String)m.invoke(null, "qemu.hw.mainkeys");
            }
            catch (Throwable e) {
                sNavBarOverride = null;
            }
        }
    }

    public static class SystemBarConfig {
        private static final String STATUS_BAR_HEIGHT_RES_NAME = "status_bar_height";
        private static final String NAV_BAR_HEIGHT_RES_NAME = "navigation_bar_height";
        private static final String NAV_BAR_HEIGHT_LANDSCAPE_RES_NAME = "navigation_bar_height_landscape";
        private static final String NAV_BAR_WIDTH_RES_NAME = "navigation_bar_width";
        private static final String SHOW_NAV_BAR_RES_NAME = "config_showNavigationBar";
        private final boolean mTranslucentStatusBar;
        private final boolean mTranslucentNavBar;
        private final int mStatusBarHeight;
        private final int mActionBarHeight;
        private final boolean mHasNavigationBar;
        private final int mNavigationBarHeight;
        private final int mNavigationBarWidth;
        private final boolean mInPortrait;
        private final float mSmallestWidthDp;

        private SystemBarConfig(Activity activity, boolean translucentStatusBar, boolean traslucentNavBar) {
            Resources res = activity.getResources();
            this.mInPortrait = res.getConfiguration().orientation == 1;
            this.mSmallestWidthDp = this.getSmallestWidthDp(activity);
            this.mStatusBarHeight = this.getInternalDimensionSize(res, "status_bar_height");
            this.mActionBarHeight = this.getActionBarHeight((Context)activity);
            this.mNavigationBarHeight = this.getNavigationBarHeight((Context)activity);
            this.mNavigationBarWidth = this.getNavigationBarWidth((Context)activity);
            this.mHasNavigationBar = this.mNavigationBarHeight > 0;
            this.mTranslucentStatusBar = translucentStatusBar;
            this.mTranslucentNavBar = traslucentNavBar;
        }

        @TargetApi(value=14)
        private int getActionBarHeight(Context context) {
            int result = 0;
            if (Build.VERSION.SDK_INT >= 14) {
                TypedValue tv = new TypedValue();
                context.getTheme().resolveAttribute(16843499, tv, true);
                result = TypedValue.complexToDimensionPixelSize((int)tv.data, (DisplayMetrics)context.getResources().getDisplayMetrics());
            }
            return result;
        }

        @TargetApi(value=14)
        private int getNavigationBarHeight(Context context) {
            Resources res = context.getResources();
            int result = 0;
            if (Build.VERSION.SDK_INT >= 14 && this.hasNavBar(context)) {
                String key = this.mInPortrait ? NAV_BAR_HEIGHT_RES_NAME : NAV_BAR_HEIGHT_LANDSCAPE_RES_NAME;
                return this.getInternalDimensionSize(res, key);
            }
            return result;
        }

        @TargetApi(value=14)
        private int getNavigationBarWidth(Context context) {
            Resources res = context.getResources();
            int result = 0;
            if (Build.VERSION.SDK_INT >= 14 && this.hasNavBar(context)) {
                return this.getInternalDimensionSize(res, NAV_BAR_WIDTH_RES_NAME);
            }
            return result;
        }

        @TargetApi(value=14)
        private boolean hasNavBar(Context context) {
            Resources res = context.getResources();
            int resourceId = res.getIdentifier(SHOW_NAV_BAR_RES_NAME, "bool", "android");
            if (resourceId != 0) {
                boolean hasNav = res.getBoolean(resourceId);
                if ("1".equals(sNavBarOverride)) {
                    hasNav = false;
                } else if ("0".equals(sNavBarOverride)) {
                    hasNav = true;
                }
                return hasNav;
            }
            return !ViewConfiguration.get((Context)context).hasPermanentMenuKey();
        }

        private int getInternalDimensionSize(Resources res, String key) {
            int result = 0;
            int resourceId = res.getIdentifier(key, "dimen", "android");
            if (resourceId > 0) {
                result = res.getDimensionPixelSize(resourceId);
            }
            return result;
        }

        @SuppressLint(value={"NewApi"})
        private float getSmallestWidthDp(Activity activity) {
            DisplayMetrics metrics = new DisplayMetrics();
            if (Build.VERSION.SDK_INT >= 16) {
                activity.getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
            } else {
                activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
            }
            float widthDp = (float)metrics.widthPixels / metrics.density;
            float heightDp = (float)metrics.heightPixels / metrics.density;
            return Math.min(widthDp, heightDp);
        }

        public boolean isNavigationAtBottom() {
            return this.mSmallestWidthDp >= 600.0f || this.mInPortrait;
        }

        public int getStatusBarHeight() {
            return this.mStatusBarHeight;
        }

        public int getActionBarHeight() {
            return this.mActionBarHeight;
        }

        public boolean hasNavigtionBar() {
            return this.mHasNavigationBar;
        }

        public int getNavigationBarHeight() {
            return this.mNavigationBarHeight;
        }

        public int getNavigationBarWidth() {
            return this.mNavigationBarWidth;
        }

        public int getPixelInsetTop(boolean withActionBar) {
            return (this.mTranslucentStatusBar ? this.mStatusBarHeight : 0) + (withActionBar ? this.mActionBarHeight : 0);
        }

        public int getPixelInsetBottom() {
            if (this.mTranslucentNavBar && this.isNavigationAtBottom()) {
                return this.mNavigationBarHeight;
            }
            return 0;
        }

        public int getPixelInsetRight() {
            if (this.mTranslucentNavBar && !this.isNavigationAtBottom()) {
                return this.mNavigationBarWidth;
            }
            return 0;
        }
    }
}

