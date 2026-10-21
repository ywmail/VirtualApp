/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Notification
 *  android.os.Build$VERSION
 */
package com.lody.virtual.client.hook.proxies.notification;

import android.app.Notification;
import android.os.Build;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.hook.base.MethodProxy;
import com.lody.virtual.client.hook.utils.MethodParameterUtils;
import com.lody.virtual.client.ipc.VNotificationManager;
import com.lody.virtual.helper.compat.BuildCompat;
import com.lody.virtual.helper.utils.ArrayUtils;
import java.lang.reflect.Method;

class MethodProxies {
    MethodProxies() {
    }

    static class GetAppActiveNotifications
    extends MethodProxy {
        GetAppActiveNotifications() {
        }

        @Override
        public String getMethodName() {
            return "getAppActiveNotifications";
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            args[0] = GetAppActiveNotifications.getHostPkg();
            GetAppActiveNotifications.replaceLastUserId(args);
            return method.invoke(who, args);
        }
    }

    static class SetNotificationsEnabledForPackage
    extends MethodProxy {
        SetNotificationsEnabledForPackage() {
        }

        @Override
        public String getMethodName() {
            return "setNotificationsEnabledForPackage";
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            String pkg = (String)args[0];
            if (SetNotificationsEnabledForPackage.getHostPkg().equals(pkg)) {
                return method.invoke(who, args);
            }
            int enableIndex = ArrayUtils.indexOfFirst(args, Boolean.class);
            boolean enable = (Boolean)args[enableIndex];
            VNotificationManager.get().setNotificationsEnabledForPackage(pkg, enable, SetNotificationsEnabledForPackage.getAppUserId());
            return 0;
        }
    }

    static class AreNotificationsEnabledForPackage
    extends MethodProxy {
        AreNotificationsEnabledForPackage() {
        }

        @Override
        public String getMethodName() {
            return "areNotificationsEnabledForPackage";
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            String pkg = (String)args[0];
            if (AreNotificationsEnabledForPackage.getHostPkg().equals(pkg)) {
                return method.invoke(who, args);
            }
            return VNotificationManager.get().areNotificationsEnabledForPackage(pkg, AreNotificationsEnabledForPackage.getAppUserId());
        }
    }

    static class CancelAllNotifications
    extends MethodProxy {
        CancelAllNotifications() {
        }

        @Override
        public String getMethodName() {
            return "cancelAllNotifications";
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            String pkg = MethodParameterUtils.replaceFirstAppPkg(args);
            if (VirtualCore.get().isAppInstalled(pkg)) {
                VNotificationManager.get().cancelAllNotification(pkg, CancelAllNotifications.getAppUserId());
                return 0;
            }
            CancelAllNotifications.replaceLastUserId(args);
            return method.invoke(who, args);
        }
    }

    static class CancelNotificationWithTag
    extends MethodProxy {
        CancelNotificationWithTag() {
        }

        @Override
        public String getMethodName() {
            return "cancelNotificationWithTag";
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            int id_index = BuildCompat.isR() ? 3 : 2;
            int tag_index = BuildCompat.isR() ? 2 : 1;
            String pkg = MethodParameterUtils.replaceFirstAppPkg(args);
            CancelNotificationWithTag.replaceLastUserId(args);
            if (CancelNotificationWithTag.getHostPkg().equals(pkg)) {
                return method.invoke(who, args);
            }
            String tag = (String)args[tag_index];
            int id2 = (Integer)args[id_index];
            id2 = VNotificationManager.get().dealNotificationId(id2, pkg, tag, CancelNotificationWithTag.getAppUserId());
            tag = VNotificationManager.get().dealNotificationTag(id2, pkg, tag, CancelNotificationWithTag.getAppUserId());
            args[tag_index] = tag;
            args[id_index] = id2;
            return method.invoke(who, args);
        }
    }

    static class EnqueueNotificationWithTagPriority
    extends EnqueueNotificationWithTag {
        EnqueueNotificationWithTagPriority() {
        }

        @Override
        public String getMethodName() {
            return "enqueueNotificationWithTagPriority";
        }
    }

    static class EnqueueNotificationWithTag
    extends MethodProxy {
        EnqueueNotificationWithTag() {
        }

        @Override
        public String getMethodName() {
            return "enqueueNotificationWithTag";
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            String pkg = (String)args[0];
            EnqueueNotificationWithTag.replaceLastUserId(args);
            if (EnqueueNotificationWithTag.getHostPkg().equals(pkg)) {
                return method.invoke(who, args);
            }
            int notificationIndex = ArrayUtils.indexOfFirst(args, Notification.class);
            int idIndex = ArrayUtils.indexOfFirst(args, Integer.class);
            int tagIndex = Build.VERSION.SDK_INT >= 18 ? 2 : 1;
            int id2 = (Integer)args[idIndex];
            String tag = (String)args[tagIndex];
            id2 = VNotificationManager.get().dealNotificationId(id2, pkg, tag, EnqueueNotificationWithTag.getAppUserId());
            tag = VNotificationManager.get().dealNotificationTag(id2, pkg, tag, EnqueueNotificationWithTag.getAppUserId());
            args[idIndex] = id2;
            args[tagIndex] = tag;
            Notification notification = (Notification)args[notificationIndex];
            if (!VNotificationManager.get().dealNotification(id2, notification, pkg)) {
                return 0;
            }
            VNotificationManager.get().addNotification(id2, tag, pkg, EnqueueNotificationWithTag.getAppUserId());
            args[0] = EnqueueNotificationWithTag.getHostPkg();
            if (Build.VERSION.SDK_INT >= 18 && args[1] instanceof String) {
                args[1] = EnqueueNotificationWithTag.getHostPkg();
            }
            return method.invoke(who, args);
        }
    }

    static class EnqueueNotification
    extends MethodProxy {
        EnqueueNotification() {
        }

        @Override
        public String getMethodName() {
            return "enqueueNotification";
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            String pkg = (String)args[0];
            EnqueueNotification.replaceLastUserId(args);
            if (EnqueueNotification.getHostPkg().equals(pkg)) {
                return method.invoke(who, args);
            }
            int notificationIndex = ArrayUtils.indexOfFirst(args, Notification.class);
            int idIndex = ArrayUtils.indexOfFirst(args, Integer.class);
            int id2 = (Integer)args[idIndex];
            id2 = VNotificationManager.get().dealNotificationId(id2, pkg, null, EnqueueNotification.getAppUserId());
            args[idIndex] = id2;
            Notification notification = (Notification)args[notificationIndex];
            if (!VNotificationManager.get().dealNotification(id2, notification, pkg)) {
                return 0;
            }
            VNotificationManager.get().addNotification(id2, null, pkg, EnqueueNotification.getAppUserId());
            args[0] = EnqueueNotification.getHostPkg();
            return method.invoke(who, args);
        }
    }
}

