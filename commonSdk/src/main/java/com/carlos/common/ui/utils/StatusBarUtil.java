/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.annotation.TargetApi
 *  android.app.Activity
 *  android.content.Context
 *  android.graphics.Color
 *  android.os.Build$VERSION
 *  android.view.View
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.view.ViewGroup$MarginLayoutParams
 *  android.widget.LinearLayout
 *  android.widget.LinearLayout$LayoutParams
 *  androidx.drawerlayout.widget.DrawerLayout
 */
package com.carlos.common.ui.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import com.carlos.common.ui.utils.StatusBarView;
import com.kook.librelease.StringFog;

public class StatusBarUtil {
    public static final int DEFAULT_STATUS_BAR_ALPHA = 112;

    public static void setColor(Activity activity, int color2) {
        StatusBarUtil.setColor(activity, color2, 112);
    }

    public static void setColor(Activity activity, int color2, int statusBarAlpha) {
        if (Build.VERSION.SDK_INT >= 21) {
            activity.getWindow().addFlags(Integer.MIN_VALUE);
            activity.getWindow().clearFlags(0x4000000);
            activity.getWindow().setStatusBarColor(StatusBarUtil.calculateStatusColor(color2, statusBarAlpha));
        } else if (Build.VERSION.SDK_INT >= 19) {
            activity.getWindow().addFlags(0x4000000);
            ViewGroup decorView = (ViewGroup)activity.getWindow().getDecorView();
            int count = decorView.getChildCount();
            if (count > 0 && decorView.getChildAt(count - 1) instanceof StatusBarView) {
                decorView.getChildAt(count - 1).setBackgroundColor(StatusBarUtil.calculateStatusColor(color2, statusBarAlpha));
            } else {
                StatusBarView statusView = StatusBarUtil.createStatusBarView(activity, color2, statusBarAlpha);
                decorView.addView((View)statusView);
            }
            StatusBarUtil.setRootView(activity);
        }
    }

    public static void setColorForSwipeBack(Activity activity, int color2) {
        StatusBarUtil.setColorForSwipeBack(activity, color2, 112);
    }

    public static void setColorForSwipeBack(Activity activity, int color2, int statusBarAlpha) {
        if (Build.VERSION.SDK_INT >= 19) {
            ViewGroup contentView = (ViewGroup)activity.findViewById(0x1020002);
            contentView.setPadding(0, StatusBarUtil.getStatusBarHeight((Context)activity), 0, 0);
            contentView.setBackgroundColor(StatusBarUtil.calculateStatusColor(color2, statusBarAlpha));
            StatusBarUtil.setTransparentForWindow(activity);
        }
    }

    public static void setColorNoTranslucent(Activity activity, int color2) {
        StatusBarUtil.setColor(activity, color2, 0);
    }

    @Deprecated
    public static void setColorDiff(Activity activity, int color2) {
        if (Build.VERSION.SDK_INT < 19) {
            return;
        }
        StatusBarUtil.transparentStatusBar(activity);
        ViewGroup contentView = (ViewGroup)activity.findViewById(0x1020002);
        if (contentView.getChildCount() > 1) {
            contentView.getChildAt(1).setBackgroundColor(color2);
        } else {
            contentView.addView((View)StatusBarUtil.createStatusBarView(activity, color2));
        }
        StatusBarUtil.setRootView(activity);
    }

    public static void setTranslucent(Activity activity) {
        StatusBarUtil.setTranslucent(activity, 112);
    }

    public static void setTranslucent(Activity activity, int statusBarAlpha) {
        if (Build.VERSION.SDK_INT < 19) {
            return;
        }
        StatusBarUtil.setTransparent(activity);
        StatusBarUtil.addTranslucentView(activity, statusBarAlpha);
    }

    public static void setTranslucentForCoordinatorLayout(Activity activity, int statusBarAlpha) {
        if (Build.VERSION.SDK_INT < 19) {
            return;
        }
        StatusBarUtil.transparentStatusBar(activity);
        StatusBarUtil.addTranslucentView(activity, statusBarAlpha);
    }

    public static void setTransparent(Activity activity) {
        if (Build.VERSION.SDK_INT < 19) {
            return;
        }
        StatusBarUtil.transparentStatusBar(activity);
        StatusBarUtil.setRootView(activity);
    }

    @Deprecated
    public static void setTranslucentDiff(Activity activity) {
        if (Build.VERSION.SDK_INT >= 19) {
            activity.getWindow().addFlags(0x4000000);
            StatusBarUtil.setRootView(activity);
        }
    }

    public static void setColorForDrawerLayout(Activity activity, DrawerLayout drawerLayout, int color2) {
        StatusBarUtil.setColorForDrawerLayout(activity, drawerLayout, color2, 112);
    }

    public static void setColorNoTranslucentForDrawerLayout(Activity activity, DrawerLayout drawerLayout, int color2) {
        StatusBarUtil.setColorForDrawerLayout(activity, drawerLayout, color2, 0);
    }

    public static void setColorForDrawerLayout(Activity activity, DrawerLayout drawerLayout, int color2, int statusBarAlpha) {
        if (Build.VERSION.SDK_INT < 19) {
            return;
        }
        if (Build.VERSION.SDK_INT >= 21) {
            activity.getWindow().addFlags(Integer.MIN_VALUE);
            activity.getWindow().clearFlags(0x4000000);
            activity.getWindow().setStatusBarColor(0);
        } else {
            activity.getWindow().addFlags(0x4000000);
        }
        ViewGroup contentLayout = (ViewGroup)drawerLayout.getChildAt(0);
        if (contentLayout.getChildCount() > 0 && contentLayout.getChildAt(0) instanceof StatusBarView) {
            contentLayout.getChildAt(0).setBackgroundColor(StatusBarUtil.calculateStatusColor(color2, statusBarAlpha));
        } else {
            StatusBarView statusBarView = StatusBarUtil.createStatusBarView(activity, color2);
            contentLayout.addView((View)statusBarView, 0);
        }
        if (!(contentLayout instanceof LinearLayout) && contentLayout.getChildAt(1) != null) {
            contentLayout.getChildAt(1).setPadding(contentLayout.getPaddingLeft(), StatusBarUtil.getStatusBarHeight((Context)activity) + contentLayout.getPaddingTop(), contentLayout.getPaddingRight(), contentLayout.getPaddingBottom());
        }
        ViewGroup drawer = (ViewGroup)drawerLayout.getChildAt(1);
        drawerLayout.setFitsSystemWindows(false);
        contentLayout.setFitsSystemWindows(false);
        contentLayout.setClipToPadding(true);
        drawer.setFitsSystemWindows(false);
        StatusBarUtil.addTranslucentView(activity, statusBarAlpha);
    }

    @Deprecated
    public static void setColorForDrawerLayoutDiff(Activity activity, DrawerLayout drawerLayout, int color2) {
        if (Build.VERSION.SDK_INT >= 19) {
            activity.getWindow().addFlags(0x4000000);
            ViewGroup contentLayout = (ViewGroup)drawerLayout.getChildAt(0);
            if (contentLayout.getChildCount() > 0 && contentLayout.getChildAt(0) instanceof StatusBarView) {
                contentLayout.getChildAt(0).setBackgroundColor(StatusBarUtil.calculateStatusColor(color2, 112));
            } else {
                StatusBarView statusBarView = StatusBarUtil.createStatusBarView(activity, color2);
                contentLayout.addView((View)statusBarView, 0);
            }
            if (!(contentLayout instanceof LinearLayout) && contentLayout.getChildAt(1) != null) {
                contentLayout.getChildAt(1).setPadding(0, StatusBarUtil.getStatusBarHeight((Context)activity), 0, 0);
            }
            ViewGroup drawer = (ViewGroup)drawerLayout.getChildAt(1);
            drawerLayout.setFitsSystemWindows(false);
            contentLayout.setFitsSystemWindows(false);
            contentLayout.setClipToPadding(true);
            drawer.setFitsSystemWindows(false);
        }
    }

    public static void setTranslucentForDrawerLayout(Activity activity, DrawerLayout drawerLayout) {
        StatusBarUtil.setTranslucentForDrawerLayout(activity, drawerLayout, 112);
    }

    public static void setTranslucentForDrawerLayout(Activity activity, DrawerLayout drawerLayout, int statusBarAlpha) {
        if (Build.VERSION.SDK_INT < 19) {
            return;
        }
        StatusBarUtil.setTransparentForDrawerLayout(activity, drawerLayout);
        StatusBarUtil.addTranslucentView(activity, statusBarAlpha);
    }

    public static void setTransparentForDrawerLayout(Activity activity, DrawerLayout drawerLayout) {
        if (Build.VERSION.SDK_INT < 19) {
            return;
        }
        if (Build.VERSION.SDK_INT >= 21) {
            activity.getWindow().addFlags(Integer.MIN_VALUE);
            activity.getWindow().clearFlags(0x4000000);
            activity.getWindow().setStatusBarColor(0);
        } else {
            activity.getWindow().addFlags(0x4000000);
        }
        ViewGroup contentLayout = (ViewGroup)drawerLayout.getChildAt(0);
        if (!(contentLayout instanceof LinearLayout) && contentLayout.getChildAt(1) != null) {
            contentLayout.getChildAt(1).setPadding(0, StatusBarUtil.getStatusBarHeight((Context)activity), 0, 0);
        }
        ViewGroup drawer = (ViewGroup)drawerLayout.getChildAt(1);
        drawerLayout.setFitsSystemWindows(false);
        contentLayout.setFitsSystemWindows(false);
        contentLayout.setClipToPadding(true);
        drawer.setFitsSystemWindows(false);
    }

    @Deprecated
    public static void setTranslucentForDrawerLayoutDiff(Activity activity, DrawerLayout drawerLayout) {
        if (Build.VERSION.SDK_INT >= 19) {
            activity.getWindow().addFlags(0x4000000);
            ViewGroup contentLayout = (ViewGroup)drawerLayout.getChildAt(0);
            contentLayout.setFitsSystemWindows(true);
            contentLayout.setClipToPadding(true);
            ViewGroup vg = (ViewGroup)drawerLayout.getChildAt(1);
            vg.setFitsSystemWindows(false);
            drawerLayout.setFitsSystemWindows(false);
        }
    }

    public static void setTransparentForImageView(Activity activity, View needOffsetView) {
        StatusBarUtil.setTranslucentForImageView(activity, 0, needOffsetView);
    }

    public static void setTranslucentForImageView(Activity activity, View needOffsetView) {
        StatusBarUtil.setTranslucentForImageView(activity, 112, needOffsetView);
    }

    public static void setTranslucentForImageView(Activity activity, int statusBarAlpha, View needOffsetView) {
        if (Build.VERSION.SDK_INT < 19) {
            return;
        }
        StatusBarUtil.setTransparentForWindow(activity);
        StatusBarUtil.addTranslucentView(activity, statusBarAlpha);
        if (needOffsetView != null) {
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams)needOffsetView.getLayoutParams();
            layoutParams.setMargins(0, StatusBarUtil.getStatusBarHeight((Context)activity), 0, 0);
        }
    }

    public static void setTranslucentForImageViewInFragment(Activity activity, View needOffsetView) {
        StatusBarUtil.setTranslucentForImageViewInFragment(activity, 112, needOffsetView);
    }

    public static void setTransparentForImageViewInFragment(Activity activity, View needOffsetView) {
        StatusBarUtil.setTranslucentForImageViewInFragment(activity, 0, needOffsetView);
    }

    public static void setTranslucentForImageViewInFragment(Activity activity, int statusBarAlpha, View needOffsetView) {
        StatusBarUtil.setTranslucentForImageView(activity, statusBarAlpha, needOffsetView);
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            StatusBarUtil.clearPreviousSetting(activity);
        }
    }

    @TargetApi(value=19)
    private static void clearPreviousSetting(Activity activity) {
        ViewGroup decorView = (ViewGroup)activity.getWindow().getDecorView();
        int count = decorView.getChildCount();
        if (count > 0 && decorView.getChildAt(count - 1) instanceof StatusBarView) {
            decorView.removeViewAt(count - 1);
            ViewGroup rootView = (ViewGroup)((ViewGroup)activity.findViewById(0x1020002)).getChildAt(0);
            rootView.setPadding(0, 0, 0, 0);
        }
    }

    private static void addTranslucentView(Activity activity, int statusBarAlpha) {
        ViewGroup contentView = (ViewGroup)activity.findViewById(0x1020002);
        if (contentView.getChildCount() > 1) {
            contentView.getChildAt(1).setBackgroundColor(Color.argb((int)statusBarAlpha, (int)0, (int)0, (int)0));
        } else {
            contentView.addView((View)StatusBarUtil.createTranslucentStatusBarView(activity, statusBarAlpha));
        }
    }

    private static StatusBarView createStatusBarView(Activity activity, int color2) {
        StatusBarView statusBarView = new StatusBarView((Context)activity);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, StatusBarUtil.getStatusBarHeight((Context)activity));
        statusBarView.setLayoutParams((ViewGroup.LayoutParams)params);
        statusBarView.setBackgroundColor(color2);
        return statusBarView;
    }

    private static StatusBarView createStatusBarView(Activity activity, int color2, int alpha) {
        StatusBarView statusBarView = new StatusBarView((Context)activity);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, StatusBarUtil.getStatusBarHeight((Context)activity));
        statusBarView.setLayoutParams((ViewGroup.LayoutParams)params);
        statusBarView.setBackgroundColor(StatusBarUtil.calculateStatusColor(color2, alpha));
        return statusBarView;
    }

    private static void setRootView(Activity activity) {
        ViewGroup rootView = (ViewGroup)((ViewGroup)activity.findViewById(0x1020002)).getChildAt(0);
        rootView.setFitsSystemWindows(true);
        rootView.setClipToPadding(true);
    }

    private static void setTransparentForWindow(Activity activity) {
        if (Build.VERSION.SDK_INT >= 21) {
            activity.getWindow().setStatusBarColor(0);
            activity.getWindow().getDecorView().setSystemUiVisibility(1280);
        } else if (Build.VERSION.SDK_INT >= 19) {
            activity.getWindow().setFlags(0x4000000, 0x4000000);
        }
    }

    @TargetApi(value=19)
    private static void transparentStatusBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= 21) {
            activity.getWindow().addFlags(Integer.MIN_VALUE);
            activity.getWindow().clearFlags(0x4000000);
            activity.getWindow().addFlags(0x8000000);
            activity.getWindow().setStatusBarColor(0);
        } else {
            activity.getWindow().addFlags(0x4000000);
        }
    }

    private static StatusBarView createTranslucentStatusBarView(Activity activity, int alpha) {
        StatusBarView statusBarView = new StatusBarView((Context)activity);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, StatusBarUtil.getStatusBarHeight((Context)activity));
        statusBarView.setLayoutParams((ViewGroup.LayoutParams)params);
        statusBarView.setBackgroundColor(Color.argb((int)alpha, (int)0, (int)0, (int)0));
        return statusBarView;
    }

    private static int getStatusBarHeight(Context context) {
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return context.getResources().getDimensionPixelSize(resourceId);
    }

    private static int calculateStatusColor(int color2, int alpha) {
        float a = 1.0f - (float)alpha / 255.0f;
        int red = color2 >> 16 & 0xFF;
        int green = color2 >> 8 & 0xFF;
        int blue = color2 & 0xFF;
        red = (int)((double)((float)red * a) + 0.5);
        green = (int)((double)((float)green * a) + 0.5);
        blue = (int)((double)((float)blue * a) + 0.5);
        return 0xFF000000 | red << 16 | green << 8 | blue;
    }
}

