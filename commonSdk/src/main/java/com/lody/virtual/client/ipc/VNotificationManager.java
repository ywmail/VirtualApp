/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Notification
 *  android.os.RemoteException
 */
package com.lody.virtual.client.ipc;

import android.app.Notification;
import android.os.RemoteException;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.ipc.LocalProxyUtils;
import com.lody.virtual.client.ipc.ServiceManagerNative;
import com.lody.virtual.helper.utils.IInterfaceUtils;
import com.lody.virtual.server.interfaces.INotificationManager;
import com.lody.virtual.server.notification.NotificationCompat;

public class VNotificationManager {
    private static final VNotificationManager sInstance = new VNotificationManager();
    private final NotificationCompat mNotificationCompat = NotificationCompat.create();
    private INotificationManager mService;

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public INotificationManager getService() {
        if (this.mService != null && IInterfaceUtils.isAlive(this.mService)) return this.mService;
        Class<VNotificationManager> clazz = VNotificationManager.class;
        synchronized (VNotificationManager.class) {
            Object pmBinder = this.getRemoteInterface();
            this.mService = LocalProxyUtils.genProxy(INotificationManager.class, pmBinder);
            // ** MonitorExit[var1_1] (shouldn't be in output)
            return this.mService;
        }
    }

    private Object getRemoteInterface() {
        return INotificationManager.Stub.asInterface(ServiceManagerNative.getService("notification"));
    }

    private VNotificationManager() {
    }

    public static VNotificationManager get() {
        return sInstance;
    }

    public boolean dealNotification(int id2, Notification notification, String packageName) {
        if (notification == null) {
            return false;
        }
        return VirtualCore.get().getHostPkg().equals(packageName) || this.mNotificationCompat.dealNotification(id2, notification, packageName);
    }

    public int dealNotificationId(int id2, String packageName, String tag, int userId) {
        try {
            return this.getService().dealNotificationId(id2, packageName, tag, userId);
        }
        catch (RemoteException e) {
            e.printStackTrace();
            return id2;
        }
    }

    public String dealNotificationTag(int id2, String packageName, String tag, int userId) {
        try {
            return this.getService().dealNotificationTag(id2, packageName, tag, userId);
        }
        catch (RemoteException e) {
            e.printStackTrace();
            return tag;
        }
    }

    public boolean areNotificationsEnabledForPackage(String packageName, int userId) {
        try {
            return this.getService().areNotificationsEnabledForPackage(packageName, userId);
        }
        catch (RemoteException e) {
            e.printStackTrace();
            return true;
        }
    }

    public void setNotificationsEnabledForPackage(String packageName, boolean enable, int userId) {
        try {
            this.getService().setNotificationsEnabledForPackage(packageName, enable, userId);
        }
        catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void addNotification(int id2, String tag, String packageName, int userId) {
        try {
            this.getService().addNotification(id2, tag, packageName, userId);
        }
        catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void cancelAllNotification(String packageName, int userId) {
        try {
            this.getService().cancelAllNotification(packageName, userId);
        }
        catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}

