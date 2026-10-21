/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.pm.ProviderInfo
 *  android.net.Uri
 *  android.os.Build$VERSION
 */
package com.lody.virtual.client.hook.proxies.content;

import android.content.pm.ProviderInfo;
import android.database.IContentObserver;
import android.net.Uri;
import android.os.Build;
import com.lody.virtual.client.hook.base.MethodProxy;
import com.lody.virtual.client.ipc.VContentManager;
import com.lody.virtual.client.ipc.VPackageManager;
import com.lody.virtual.helper.Keep;
import com.lody.virtual.helper.compat.BuildCompat;
import com.lody.virtual.os.VUserHandle;
import java.lang.reflect.Method;

@Keep
public class MethodProxies {
    private static boolean isAppUri(Uri uri) {
        ProviderInfo info = VPackageManager.get().resolveContentProvider(uri.getAuthority(), 0, VUserHandle.myUserId());
        return info != null && info.enabled;
    }

    public static Object registerContentObserver(Object who, Method method, Object[] args) throws Throwable {
        if (Build.VERSION.SDK_INT >= 24 && args.length >= 5) {
            args[4] = 22;
        }
        Uri uri = (Uri)args[0];
        boolean notifyForDescendents = (Boolean)args[1];
        IContentObserver observer = (IContentObserver)args[2];
        if (MethodProxies.isAppUri(uri)) {
            VContentManager.get().registerContentObserver(uri, notifyForDescendents, observer, VUserHandle.myUserId());
            return 0;
        }
        MethodProxy.replaceFirstUserId(args);
        return method.invoke(who, args);
    }

    public static Object unregisterContentObserver(Object who, Method method, Object[] args) throws Throwable {
        IContentObserver observer = (IContentObserver)args[0];
        VContentManager.get().unregisterContentObserver(observer);
        return method.invoke(who, args);
    }

    public static Object notifyChange(Object who, Method method, Object[] args) throws Throwable {
        int flags;
        if (Build.VERSION.SDK_INT >= 24 && args.length >= 6) {
            args[5] = 22;
        }
        IContentObserver observer = (IContentObserver)args[1];
        boolean observerWantsSelfNotifications = (Boolean)args[2];
        boolean syncToNetwork = args[3] instanceof Integer ? ((flags = ((Integer)args[3]).intValue()) & 1) != 0 : (Boolean)args[3];
        if (BuildCompat.isR()) {
            Uri[] uris;
            MethodProxy.replaceLastUserId(args);
            for (Uri uri : uris = (Uri[])args[0]) {
                if (MethodProxies.isAppUri(uri)) {
                    VContentManager.get().notifyChange(uri, observer, observerWantsSelfNotifications, syncToNetwork, VUserHandle.myUserId());
                    continue;
                }
                method.invoke(who, args);
            }
            return 0;
        }
        Uri uri = (Uri)args[0];
        if (MethodProxies.isAppUri(uri)) {
            VContentManager.get().notifyChange(uri, observer, observerWantsSelfNotifications, syncToNetwork, VUserHandle.myUserId());
            return 0;
        }
        MethodProxy.replaceLastUserId(args);
        return method.invoke(who, args);
    }
}

