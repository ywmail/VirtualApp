/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.ComponentName
 *  android.content.Context
 *  android.content.Intent
 *  android.content.pm.PackageManager
 */
package com.lody.virtual.client.hook.base;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.VClient;
import com.lody.virtual.client.core.SettingConfig;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.env.LocalPackageCache;
import com.lody.virtual.client.hook.annotations.LogInvocation;
import com.lody.virtual.client.ipc.VirtualLocationManager;
import com.lody.virtual.os.VUserHandle;
import com.lody.virtual.remote.VDeviceConfig;
import java.lang.reflect.Method;

public abstract class MethodProxy {
    private boolean enable = true;
    private LogInvocation.Condition mInvocationLoggingCondition = LogInvocation.Condition.NEVER;

    public MethodProxy() {
        LogInvocation loggingAnnotation = this.getClass().getAnnotation(LogInvocation.class);
        if (loggingAnnotation != null) {
            this.mInvocationLoggingCondition = loggingAnnotation.value();
        }
    }

    public static String getHostPkg() {
        return VirtualCore.get().getHostPkg();
    }

    public static String getAppPkg() {
        return VClient.get().getCurrentPackage();
    }

    protected static Context getHostContext() {
        return VirtualCore.get().getContext();
    }

    protected static boolean isAppProcess() {
        return VirtualCore.get().isVAppProcess();
    }

    protected static boolean isServerProcess() {
        return VirtualCore.get().isServerProcess();
    }

    protected static boolean isMainProcess() {
        return VirtualCore.get().isMainProcess();
    }

    protected static int getVUid() {
        return VClient.get().getVUid();
    }

    public static int getAppUserId() {
        return VUserHandle.getUserId(MethodProxy.getVUid());
    }

    public static int getRealUserId() {
        return VUserHandle.realUserId();
    }

    public static void replaceLastUserId(Object[] args) {
        if (MethodProxy.getRealUserId() == 0) {
            return;
        }
        int pos = -1;
        for (int i = 0; i < args.length; ++i) {
            Object o = args[i];
            if (o != Integer.valueOf(0)) continue;
            pos = i;
        }
        if (pos >= 0) {
            args[pos] = MethodProxy.getRealUserId();
        }
    }

    public static void replaceFirstUserId(Object[] args) {
        if (MethodProxy.getRealUserId() == 0) {
            return;
        }
        for (int i = 0; i < args.length; ++i) {
            Object o = args[i];
            if (o != Integer.valueOf(0)) continue;
            args[i] = MethodProxy.getRealUserId();
            return;
        }
    }

    protected static int getBaseVUid() {
        return VClient.get().getBaseVUid();
    }

    protected static int getRealUid() {
        return VirtualCore.get().myUid();
    }

    protected static SettingConfig getConfig() {
        return VirtualCore.getConfig();
    }

    protected static VDeviceConfig getDeviceConfig() {
        return VClient.get().getDeviceConfig();
    }

    protected static boolean isFakeLocationEnable() {
        return VirtualLocationManager.get().getMode(VUserHandle.myUserId(), VClient.get().getCurrentPackage()) != 0;
    }

    public static boolean isOutsidePackage(String pkg) {
        return LocalPackageCache.isOutsideVisiblePackage(pkg);
    }

    public static boolean isInsidePackage(String pkg) {
        return VirtualCore.get().isAppInstalled(pkg);
    }

    public static boolean isHostIntent(Intent intent) {
        SettingConfig config = VirtualCore.getConfig();
        if (config.isHostIntent(intent)) {
            return true;
        }
        ComponentName component = intent.getComponent();
        if (component != null) {
            String pkg = component.getPackageName();
            return MethodProxy.isOutsidePackage(pkg);
        }
        return false;
    }

    public abstract String getMethodName();

    public boolean beforeCall(Object who, Method method, Object ... args) {
        return true;
    }

    public Object call(Object who, Method method, Object ... args) throws Throwable {
        return method.invoke(who, args);
    }

    public Object afterCall(Object who, Method method, Object[] args, Object result) throws Throwable {
        return result;
    }

    public boolean isEnable() {
        return this.enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public LogInvocation.Condition getInvocationLoggingCondition() {
        return this.mInvocationLoggingCondition;
    }

    public void setInvocationloggingCondition(LogInvocation.Condition invocationLoggingCondition) {
        this.mInvocationLoggingCondition = invocationLoggingCondition;
    }

    public boolean isAppPkg(String pkg) {
        return VirtualCore.get().isAppInstalled(pkg);
    }

    public boolean isHostPkg(String pkg) {
        SettingConfig config = VirtualCore.getConfig();
        return pkg.equals(MethodProxy.getConfig().getMainPackageName()) || pkg.equals(config.getExtPackageName());
    }

    protected PackageManager getPM() {
        return VirtualCore.getPM();
    }

    public String toString() {
        return "Method : " + this.getMethodName();
    }

    public long getIntOrLongValue(Object obj) {
        if (obj == null) {
            return 0L;
        }
        if (obj instanceof Integer) {
            return ((Integer)obj).longValue();
        }
        if (obj instanceof Long) {
            return (Long)obj;
        }
        return -1L;
    }
}

