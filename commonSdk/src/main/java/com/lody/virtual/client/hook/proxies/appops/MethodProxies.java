/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.annotation.TargetApi
 */
package com.lody.virtual.client.hook.proxies.appops;

import android.annotation.TargetApi;
import com.lody.virtual.GmsSupport;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.fixer.ContextFixer;
import com.lody.virtual.client.hook.utils.MethodParameterUtils;
import com.lody.virtual.helper.Keep;
import com.lody.virtual.helper.compat.BuildCompat;
import java.lang.reflect.Method;
import mirror.android.content.AttributionSource;

@TargetApi(value=19)
@Keep
public class MethodProxies {
    private static void replaceUidAndPackage(Object[] args, int pkgIndex) {
        args[pkgIndex] = VirtualCore.get().getHostPkg();
        int uidIndex = pkgIndex - 1;
        if (args[pkgIndex - 1] instanceof Integer) {
            args[uidIndex] = VirtualCore.get().myUid();
        }
    }

    public static Object checkAudioOperation(Object who, Method method, Object[] args) throws Throwable {
        MethodProxies.replaceUidAndPackage(args, 3);
        return method.invoke(who, args);
    }

    public static Object checkOperation(Object who, Method method, Object[] args) throws Throwable {
        MethodProxies.replaceUidAndPackage(args, 2);
        return method.invoke(who, args);
    }

    public static Object checkPackage(Object who, Method method, Object[] args) throws Throwable {
        String pkg = (String)args[1];
        if (GmsSupport.isGoogleAppOrService(pkg)) {
            return 0;
        }
        MethodProxies.replaceUidAndPackage(args, 1);
        return method.invoke(who, args);
    }

    public static Object getOpsForPackage(Object who, Method method, Object[] args) throws Throwable {
        MethodProxies.replaceUidAndPackage(args, 1);
        return method.invoke(who, args);
    }

    public static Object getPackagesForOps(Object who, Method method, Object[] args) throws Throwable {
        return method.invoke(who, args);
    }

    public static Object noteOperation(Object who, Method method, Object[] args) throws Throwable {
        MethodProxies.replaceUidAndPackage(args, 2);
        return method.invoke(who, args);
    }

    public static Object noteProxyOperation(Object who, Method method, Object[] args) throws Throwable {
        if (BuildCompat.isS()) {
            int index = MethodParameterUtils.getIndex(args, AttributionSource.TYPE);
            if (index >= 0) {
                ContextFixer.fixAttributionSource(args[index]);
            }
            return method.invoke(who, args);
        }
        return 0;
    }

    public static Object resetAllModes(Object who, Method method, Object[] args) throws Throwable {
        args[0] = 0;
        args[1] = VirtualCore.get().getHostPkg();
        return method.invoke(who, args);
    }

    public static Object startOperation(Object who, Method method, Object[] args) throws Throwable {
        MethodProxies.replaceUidAndPackage(args, 3);
        return method.invoke(who, args);
    }

    public static Object finishOperation(Object who, Method method, Object[] args) throws Throwable {
        MethodProxies.replaceUidAndPackage(args, 3);
        return method.invoke(who, args);
    }

    public static Object checkOperationRaw(Object who, Method method, Object[] args) throws Throwable {
        MethodProxies.replaceUidAndPackage(args, 2);
        return method.invoke(who, args);
    }

    public static Object startWatchingAsyncNoted(Object who, Method method, Object[] args) throws Throwable {
        args[0] = VirtualCore.get().getHostPkg();
        return method.invoke(who, args);
    }

    public static Object stopWatchingAsyncNoted(Object who, Method method, Object[] args) throws Throwable {
        args[0] = VirtualCore.get().getHostPkg();
        return method.invoke(who, args);
    }

    public static Object extractAsyncOps(Object who, Method method, Object[] args) throws Throwable {
        args[0] = VirtualCore.get().getHostPkg();
        return method.invoke(who, args);
    }
}

