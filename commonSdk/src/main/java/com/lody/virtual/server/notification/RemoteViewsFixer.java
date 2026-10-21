/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.pm.PackageManager$NameNotFoundException
 *  android.graphics.Bitmap
 *  android.os.Build$VERSION
 *  android.view.LayoutInflater
 *  android.view.View
 *  android.view.View$MeasureSpec
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.widget.FrameLayout
 *  android.widget.FrameLayout$LayoutParams
 *  android.widget.RemoteViews
 *  android.widget.TextView
 *  com.kook.librelease.R$dimen
 *  com.kook.librelease.R$id
 *  com.kook.librelease.R$layout
 */
package com.lody.virtual.server.notification;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RemoteViews;
import android.widget.TextView;
import com.kook.librelease.R;
import com.lody.virtual.StringFog;
import com.lody.virtual.helper.utils.Reflect;
import com.lody.virtual.helper.utils.VLog;
import com.lody.virtual.server.notification.NotificationCompat;
import com.lody.virtual.server.notification.PendIntentCompat;
import com.lody.virtual.server.notification.WidthCompat;
import java.util.ArrayList;

class RemoteViewsFixer {
    private static final String TAG = NotificationCompat.TAG;
    private static final boolean DEBUG = false;
    private final WidthCompat mWidthCompat = new WidthCompat();
    private int notification_min_height;
    private int notification_max_height;
    private int notification_mid_height;
    private int notification_panel_width;
    private int notification_side_padding;
    private int notification_padding;
    private NotificationCompat mNotificationCompat;
    private boolean init = false;

    RemoteViewsFixer(NotificationCompat notificationCompat) {
        this.mNotificationCompat = notificationCompat;
    }

    View toView(Context context, RemoteViews remoteViews, boolean isBig) {
        View mCache = null;
        try {
            mCache = this.createView(context, remoteViews, isBig);
        }
        catch (Throwable throwable) {
            try {
                mCache = LayoutInflater.from((Context)context).inflate(remoteViews.getLayoutId(), null);
            }
            catch (Throwable throwable2) {
                // empty catch block
            }
        }
        return mCache;
    }

    Bitmap createBitmap(View mCache) {
        if (mCache == null) {
            return null;
        }
        mCache.setDrawingCacheEnabled(true);
        mCache.buildDrawingCache();
        return mCache.getDrawingCache();
    }

    private View apply(Context context, RemoteViews remoteViews) {
        ArrayList mActions;
        View view = null;
        try {
            view = LayoutInflater.from((Context)context).inflate(remoteViews.getLayoutId(), null, false);
            try {
                Reflect.on(view).call("setTagInternal", Reflect.on("com.android.internal.R$id").get("widget_frame"), remoteViews.getLayoutId());
            }
            catch (Exception e2) {
                VLog.w(TAG, "setTagInternal", e2);
            }
        }
        catch (Exception e) {
            VLog.w(TAG, "inflate", e);
        }
        if (view != null && (mActions = (ArrayList)Reflect.on(remoteViews).get("mActions")) != null) {
            VLog.d(TAG, "apply actions:" + mActions.size(), new Object[0]);
            for (Object action : mActions) {
                try {
                    Reflect.on(action).call("apply", view, null, null);
                }
                catch (Exception e) {
                    VLog.w(TAG, "apply action", e);
                }
            }
        }
        return view;
    }

    private View createView(Context context, RemoteViews remoteViews, boolean isBig) {
        if (remoteViews == null) {
            return null;
        }
        Context base = this.mNotificationCompat.getHostContext();
        this.init(base);
        int height = isBig ? this.notification_max_height : this.notification_min_height;
        int width = this.mWidthCompat.getNotificationWidth(base, this.notification_panel_width, height, this.notification_side_padding);
        FrameLayout frameLayout = new FrameLayout(context);
        View view1 = this.apply(context, remoteViews);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(-1, -1);
        params.gravity = 16;
        frameLayout.addView(view1, (ViewGroup.LayoutParams)params);
        if (view1 instanceof ViewGroup) {
            VLog.v(TAG, "createView:fixTextView");
            this.fixTextView((ViewGroup)view1);
        }
        int mode = Integer.MIN_VALUE;
        FrameLayout mCache = frameLayout;
        mCache.layout(0, 0, width, height);
        mCache.measure(View.MeasureSpec.makeMeasureSpec((int)width, (int)0x40000000), View.MeasureSpec.makeMeasureSpec((int)height, (int)mode));
        mCache.layout(0, 0, width, mCache.getMeasuredHeight());
        return mCache;
    }

    private void fixTextView(ViewGroup viewGroup) {
        int count = viewGroup.getChildCount();
        for (int i = 0; i < count; ++i) {
            View v = viewGroup.getChildAt(i);
            if (v instanceof TextView) {
                TextView tv = (TextView)v;
                if (!this.isSingleLine(tv)) continue;
                tv.setSingleLine(false);
                tv.setMaxLines(1);
                continue;
            }
            if (!(v instanceof ViewGroup)) continue;
            this.fixTextView((ViewGroup)v);
        }
    }

    private boolean isSingleLine(TextView textView) {
        boolean singleLine;
        try {
            singleLine = (Boolean)Reflect.on(textView).get("mSingleLine");
        }
        catch (Exception e) {
            singleLine = (textView.getInputType() & 0x20000) != 0;
        }
        return singleLine;
    }

    public RemoteViews makeRemoteViews(String key, Context pluginContext, RemoteViews contentView, boolean isBig, boolean click) {
        if (contentView == null) {
            return null;
        }
        PendIntentCompat pendIntentCompat = new PendIntentCompat(contentView);
        int layoutId = !click || pendIntentCompat.findPendIntents() <= 0 ? R.layout.custom_notification_lite : R.layout.custom_notification;
        RemoteViews remoteViews = Build.VERSION.SDK_INT > 19 && mirror.android.widget.RemoteViews.ctor != null ? mirror.android.widget.RemoteViews.ctor.newInstance(this.mNotificationCompat.getHostContext().getApplicationInfo(), layoutId) : new RemoteViews(this.mNotificationCompat.getHostContext().getPackageName(), layoutId);
        View cache = this.toView(pluginContext, contentView, isBig);
        Bitmap bmp = this.createBitmap(cache);
        remoteViews.setImageViewBitmap(R.id.im_main, bmp);
        if (click && layoutId == R.layout.custom_notification) {
            try {
                pendIntentCompat.setPendIntent(remoteViews, this.toView(this.mNotificationCompat.getHostContext(), remoteViews, isBig), cache);
            }
            catch (Exception e) {
                VLog.e(TAG, "setPendIntent error", e);
            }
        }
        return remoteViews;
    }

    private void init(Context context) {
        if (this.init) {
            return;
        }
        this.init = true;
        if (this.notification_panel_width == 0) {
            Context systemUi = null;
            try {
                systemUi = context.createPackageContext("com.android.systemui", 2);
            }
            catch (PackageManager.NameNotFoundException nameNotFoundException) {
                // empty catch block
            }
            this.notification_side_padding = Build.VERSION.SDK_INT <= 19 ? 0 : this.getDimem(context, systemUi, "notification_side_padding", R.dimen.notification_side_padding);
            this.notification_panel_width = this.getDimem(context, systemUi, "notification_panel_width", R.dimen.notification_panel_width);
            if (this.notification_panel_width <= 0) {
                this.notification_panel_width = context.getResources().getDisplayMetrics().widthPixels;
            }
            this.notification_min_height = this.getDimem(context, systemUi, "notification_min_height", R.dimen.notification_min_height);
            this.notification_max_height = this.getDimem(context, systemUi, "notification_max_height", R.dimen.notification_max_height);
            this.notification_mid_height = this.getDimem(context, systemUi, "notification_mid_height", R.dimen.notification_mid_height);
            this.notification_padding = this.getDimem(context, systemUi, "notification_padding", R.dimen.notification_padding);
        }
    }

    private int getDimem(Context context, Context sysContext, String name, int defId) {
        int id2;
        if (sysContext != null && (id2 = sysContext.getResources().getIdentifier(name, "dimen", "com.android.systemui")) != 0) {
            try {
                return Math.round(sysContext.getResources().getDimension(id2));
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        return defId == 0 ? 0 : Math.round(context.getResources().getDimension(defId));
    }
}

