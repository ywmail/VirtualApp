/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.NotificationManager
 *  android.content.Context
 *  android.text.TextUtils
 */
package com.lody.virtual.server.notification;

import android.app.NotificationManager;
import android.content.Context;
import android.text.TextUtils;
import com.lody.virtual.StringFog;
import com.lody.virtual.helper.utils.Singleton;
import com.lody.virtual.helper.utils.VLog;
import com.lody.virtual.server.interfaces.INotificationManager;
import com.lody.virtual.server.notification.NotificationCompat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VNotificationManagerService
extends INotificationManager.Stub {
    private static final Singleton<VNotificationManagerService> gService = new Singleton<VNotificationManagerService>(){

        @Override
        protected VNotificationManagerService create() {
            return new VNotificationManagerService();
        }
    };
    private NotificationManager mNotificationManager;
    static final String TAG = NotificationCompat.class.getSimpleName();
    private final List<String> mDisables = new ArrayList<String>();
    private final HashMap<String, List<NotificationInfo>> mNotifications = new HashMap();
    private Context mContext;

    private void init(Context context) {
        this.mContext = context;
        this.mNotificationManager = (NotificationManager)context.getSystemService("notification");
    }

    public static void systemReady(Context context) {
        VNotificationManagerService.get().init(context);
    }

    public static VNotificationManagerService get() {
        return gService.get();
    }

    @Override
    public int dealNotificationId(int id2, String packageName, String tag, int userId) {
        return id2;
    }

    @Override
    public String dealNotificationTag(int id2, String packageName, String tag, int userId) {
        if (TextUtils.equals((CharSequence)this.mContext.getPackageName(), (CharSequence)packageName)) {
            return tag;
        }
        if (tag == null) {
            return packageName + "@" + userId;
        }
        return packageName + ":" + tag + "@" + userId;
    }

    @Override
    public boolean areNotificationsEnabledForPackage(String packageName, int userId) {
        return !this.mDisables.contains(packageName + ":" + userId);
    }

    @Override
    public void setNotificationsEnabledForPackage(String packageName, boolean enable, int userId) {
        String key = packageName + ":" + userId;
        if (enable) {
            if (this.mDisables.contains(key)) {
                this.mDisables.remove(key);
            }
        } else if (!this.mDisables.contains(key)) {
            this.mDisables.add(key);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void addNotification(int id2, String tag, String packageName, int userId) {
        NotificationInfo notificationInfo = new NotificationInfo(id2, tag, packageName, userId);
        HashMap<String, List<NotificationInfo>> hashMap = this.mNotifications;
        synchronized (hashMap) {
            List<NotificationInfo> list = this.mNotifications.get(packageName);
            if (list == null) {
                list = new ArrayList<NotificationInfo>();
                this.mNotifications.put(packageName, list);
            }
            if (!list.contains(notificationInfo)) {
                list.add(notificationInfo);
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void cancelAllNotification(String packageName, int userId) {
        ArrayList<NotificationInfo> infos = new ArrayList<NotificationInfo>();
        HashMap<String, List<NotificationInfo>> hashMap = this.mNotifications;
        synchronized (hashMap) {
            List<NotificationInfo> list = this.mNotifications.get(packageName);
            if (list != null) {
                int count = list.size();
                for (int i = count - 1; i >= 0; --i) {
                    NotificationInfo info = list.get(i);
                    if (info.userId != userId) continue;
                    infos.add(info);
                    list.remove(i);
                }
            }
        }
        for (NotificationInfo info : infos) {
            VLog.d(TAG, "cancel " + info.tag + " " + info.id, new Object[0]);
            this.mNotificationManager.cancel(info.tag, info.id);
        }
    }

    private static class NotificationInfo {
        int id;
        String tag;
        String packageName;
        int userId;

        NotificationInfo(int id2, String tag, String packageName, int userId) {
            this.id = id2;
            this.tag = tag;
            this.packageName = packageName;
            this.userId = userId;
        }

        public boolean equals(Object obj) {
            if (obj instanceof NotificationInfo) {
                NotificationInfo that = (NotificationInfo)obj;
                return that.id == this.id && TextUtils.equals((CharSequence)that.tag, (CharSequence)this.tag) && TextUtils.equals((CharSequence)this.packageName, (CharSequence)that.packageName) && that.userId == this.userId;
            }
            return super.equals(obj);
        }
    }
}

