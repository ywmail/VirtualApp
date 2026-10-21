/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.annotation.TargetApi
 *  android.app.Notification
 *  android.app.Notification$Builder
 *  android.content.Context
 *  android.content.res.Resources
 *  android.graphics.Bitmap
 *  android.graphics.drawable.Drawable
 *  android.graphics.drawable.Icon
 *  android.net.Uri
 *  android.os.Build$VERSION
 *  android.widget.RemoteViews
 */
package com.lody.virtual.server.notification;

import android.annotation.TargetApi;
import android.app.Notification;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.widget.RemoteViews;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.helper.compat.BuildCompat;
import com.lody.virtual.helper.utils.BitmapUtils;
import com.lody.virtual.helper.utils.Reflect;
import com.lody.virtual.server.notification.NotificationCompat;
import com.lody.virtual.server.notification.ReflectionActionCompat;
import java.util.ArrayList;
import mirror.android.graphics.drawable.Icon;
import mirror.com.android.internal.R_Hide;

class NotificationFixer {
    private static final String TAG = NotificationCompat.TAG;
    private NotificationCompat mNotificationCompat;

    NotificationFixer(NotificationCompat notificationCompat) {
        this.mNotificationCompat = notificationCompat;
    }

    private static void fixNotificationIcon(Context context, Notification notification, Notification.Builder builder) {
        if (Build.VERSION.SDK_INT < 23) {
            builder.setSmallIcon(notification.icon);
            builder.setLargeIcon(notification.largeIcon);
        } else {
            Bitmap bitmap;
            android.graphics.drawable.Icon largeIcon;
            Bitmap bitmap2;
            android.graphics.drawable.Icon icon = notification.getSmallIcon();
            if (icon != null && (bitmap2 = BitmapUtils.drawableToBitmap(icon.loadDrawable(context))) != null) {
                android.graphics.drawable.Icon newIcon = android.graphics.drawable.Icon.createWithBitmap((Bitmap)bitmap2);
                builder.setSmallIcon(newIcon);
            }
            if ((largeIcon = notification.getLargeIcon()) != null && (bitmap = BitmapUtils.drawableToBitmap(largeIcon.loadDrawable(context))) != null) {
                android.graphics.drawable.Icon newIcon = android.graphics.drawable.Icon.createWithBitmap((Bitmap)bitmap);
                builder.setLargeIcon(newIcon);
            }
        }
    }

    @TargetApi(value=23)
    void fixIcon(android.graphics.drawable.Icon icon, Context appContext, boolean installed) {
        if (icon == null) {
            return;
        }
        int type = Icon.mType.get(icon);
        if (type == 2) {
            if (installed) {
                Icon.mObj1.set(icon, appContext.getResources());
                Icon.mString1.set(icon, appContext.getPackageName());
            } else {
                Drawable drawable2 = icon.loadDrawable(appContext);
                Bitmap bitmap = BitmapUtils.drawableToBitmap(drawable2);
                Icon.mObj1.set(icon, bitmap);
                Icon.mString1.set(icon, null);
                Icon.mType.set(icon, 1);
            }
        }
    }

    @TargetApi(value=21)
    void fixNotificationRemoteViews(Context pluginContext, Notification notification) {
        Notification.Builder rebuild = null;
        try {
            rebuild = (Notification.Builder)Reflect.on(Notification.Builder.class).create(pluginContext, notification).get();
        }
        catch (Exception exception) {
            // empty catch block
        }
        if (rebuild != null) {
            Notification renotification = rebuild.build();
            if (notification.tickerView == null) {
                notification.tickerView = renotification.tickerView;
            }
            if (notification.contentView == null) {
                notification.contentView = renotification.contentView;
            }
            if (notification.bigContentView == null) {
                notification.bigContentView = renotification.bigContentView;
            }
            if (notification.headsUpContentView == null) {
                notification.headsUpContentView = renotification.headsUpContentView;
            }
        }
    }

    boolean fixRemoteViewActions(Context appContext, boolean installed, RemoteViews remoteViews) {
        boolean hasIcon = false;
        if (remoteViews != null) {
            int systemIconViewId = R_Hide.id.icon.get();
            ArrayList<BitmapReflectionAction> mNew = new ArrayList<BitmapReflectionAction>();
            ArrayList mActions = (ArrayList)Reflect.on(remoteViews).get("mActions");
            if (mActions != null) {
                int count = mActions.size();
                for (int i = count - 1; i >= 0; --i) {
                    Object e = mActions.get(i);
                    if (e == null) continue;
                    if (e.getClass().getSimpleName().endsWith("TextViewDrawableAction")) {
                        mActions.remove(e);
                        continue;
                    }
                    if (!ReflectionActionCompat.isInstance(e)) continue;
                    int viewId = (Integer)Reflect.on(e).get("viewId");
                    String methodName = (String)Reflect.on(e).get("methodName");
                    int type = (Integer)Reflect.on(e).get("type");
                    Object value = Reflect.on(e).get("value");
                    if (!hasIcon) {
                        boolean bl = hasIcon = viewId == systemIconViewId;
                        if (hasIcon && type == 4 && (Integer)value == 0) {
                            hasIcon = false;
                        }
                    }
                    if (methodName.equals("setImageResource")) {
                        mNew.add(new BitmapReflectionAction(viewId, "setImageBitmap", BitmapUtils.drawableToBitmap(appContext.getResources().getDrawable(((Integer)value).intValue()))));
                        mActions.remove(e);
                        continue;
                    }
                    if (methodName.equals("setText") && type == 4) {
                        Reflect.on(e).set("type", 9);
                        Reflect.on(e).set("value", appContext.getResources().getString(((Integer)value).intValue()));
                        continue;
                    }
                    if (methodName.equals("setLabelFor")) {
                        mActions.remove(e);
                        continue;
                    }
                    if (methodName.equals("setBackgroundResource")) {
                        mActions.remove(e);
                        continue;
                    }
                    if (methodName.equals("setImageURI")) {
                        Uri uri = (Uri)value;
                        if (uri.getScheme().startsWith("http")) continue;
                        mActions.remove(e);
                        continue;
                    }
                    if (Build.VERSION.SDK_INT < 23 || !(value instanceof android.graphics.drawable.Icon)) continue;
                    android.graphics.drawable.Icon icon = (android.graphics.drawable.Icon)value;
                    this.fixIcon(icon, appContext, installed);
                }
                for (BitmapReflectionAction bitmapReflectionAction : mNew) {
                    remoteViews.setBitmap(bitmapReflectionAction.viewId, bitmapReflectionAction.methodName, bitmapReflectionAction.bitmap);
                }
            }
            if (Build.VERSION.SDK_INT < 21) {
                mirror.android.widget.RemoteViews.mPackage.set(remoteViews, VirtualCore.get().getHostPkg());
            }
        }
        return hasIcon;
    }

    void fixIconImage(Resources resources, RemoteViews remoteViews, boolean hasIconBitmap, Notification notification) {
        block7: {
            if (remoteViews == null || notification.icon == 0) {
                return;
            }
            if (!this.mNotificationCompat.isSystemLayout(remoteViews)) {
                return;
            }
            try {
                int id2 = R_Hide.id.icon.get();
                if (hasIconBitmap || notification.largeIcon != null) break block7;
                Drawable drawable2 = null;
                Bitmap bitmap = null;
                try {
                    drawable2 = resources.getDrawable(notification.icon);
                    drawable2.setLevel(notification.iconLevel);
                    bitmap = BitmapUtils.drawableToBitmap(drawable2);
                }
                catch (Throwable throwable) {
                    // empty catch block
                }
                remoteViews.setImageViewBitmap(id2, bitmap);
                if (BuildCompat.isEMUI() && notification.largeIcon == null) {
                    notification.largeIcon = bitmap;
                }
            }
            catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    private static class BitmapReflectionAction {
        int viewId;
        String methodName;
        Bitmap bitmap;

        BitmapReflectionAction(int viewId, String methodName, Bitmap bitmap) {
            this.viewId = viewId;
            this.methodName = methodName;
            this.bitmap = bitmap;
        }
    }
}

