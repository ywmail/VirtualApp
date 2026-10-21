/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.app.Application
 *  android.app.Instrumentation
 *  android.content.Context
 *  android.content.pm.ActivityInfo
 *  android.os.Build$VERSION
 *  android.os.Bundle
 *  android.os.PersistableBundle
 *  android.text.TextUtils
 */
package com.lody.virtual.client.hook.instruments;

import android.app.Application;
import android.app.Instrumentation;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.VClient;
import com.lody.virtual.client.core.AppCallback;
import com.lody.virtual.client.core.InvocationStubManager;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.fixer.ActivityFixer;
import com.lody.virtual.client.fixer.ContextFixer;
import com.lody.virtual.client.hook.instruments.InstrumentationProxy;
import com.lody.virtual.client.hook.proxies.am.HCallbackStub;
import com.lody.virtual.client.interfaces.IInjector;
import com.lody.virtual.helper.utils.VLog;
import java.lang.reflect.Field;
import mirror.android.app.Activity;
import mirror.android.app.ActivityThread;

public class InstrumentationVirtualApp
extends InstrumentationProxy
implements IInjector {
    private static final String TAG = InstrumentationVirtualApp.class.getSimpleName();
    private static InstrumentationVirtualApp gDefault;

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static InstrumentationVirtualApp getDefault() {
        if (gDefault != null) return gDefault;
        Class<InstrumentationVirtualApp> clazz = InstrumentationVirtualApp.class;
        synchronized (InstrumentationVirtualApp.class) {
            if (gDefault != null) return gDefault;
            gDefault = InstrumentationVirtualApp.create();
            // ** MonitorExit[var0] (shouldn't be in output)
            return gDefault;
        }
    }

    private static InstrumentationVirtualApp create() {
        Instrumentation instrumentation = ActivityThread.mInstrumentation.get(VirtualCore.mainThread());
        if (instrumentation instanceof InstrumentationVirtualApp) {
            return (InstrumentationVirtualApp)instrumentation;
        }
        return new InstrumentationVirtualApp(instrumentation);
    }

    public InstrumentationVirtualApp(Instrumentation base) {
        super(base);
    }

    private void dynamicResolveConflict() {
        try {
            Field[] fields;
            for (Field field : fields = this.base.getClass().getDeclaredFields()) {
                if (!field.getType().isAssignableFrom(Instrumentation.class)) continue;
                VLog.e(TAG, "resolve conflict instrumentation: %s->%s", this.base.getClass().getName(), field.getName());
                field.setAccessible(true);
                field.set(this.base, this.root);
            }
        }
        catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public Instrumentation getBaseInstrumentation() {
        return this.base;
    }

    @Override
    public void inject() throws Throwable {
        Instrumentation baseNew = ActivityThread.mInstrumentation.get(VirtualCore.mainThread());
        if (this.base == null) {
            this.base = baseNew;
        }
        if (baseNew != this.base) {
            this.root = this.base;
            this.base = baseNew;
            this.dynamicResolveConflict();
        }
        ActivityThread.mInstrumentation.set(VirtualCore.mainThread(), this);
    }

    @Override
    public boolean isEnvBad() {
        Instrumentation current = ActivityThread.mInstrumentation.get(VirtualCore.mainThread());
        return !(current instanceof InstrumentationVirtualApp);
    }

    private void checkActivityCallback() {
        InvocationStubManager.getInstance().checkEnv(HCallbackStub.class);
        InvocationStubManager.getInstance().checkEnv(InstrumentationVirtualApp.class);
    }

    @Override
    public void callApplicationOnCreate(Application app) {
        this.checkActivityCallback();
        super.callApplicationOnCreate(app);
    }

    private AppCallback getAppCallback() {
        return VirtualCore.get().getAppCallback();
    }

    @Override
    public void callActivityOnCreate(android.app.Activity activity, Bundle icicle) {
        this.checkActivityCallback();
        ActivityInfo info = Activity.mActivityInfo.get(activity);
        String packageName = info != null ? info.packageName : null;
        ContextFixer.fixContext((Context)activity, packageName);
        ActivityFixer.fixActivity(activity);
        AppCallback callback = this.getAppCallback();
        callback.beforeActivityOnCreate(activity);
        super.callActivityOnCreate(activity, icicle);
        callback.afterActivityOnCreate(activity);
    }

    @Override
    public void callActivityOnCreate(android.app.Activity activity, Bundle icicle, PersistableBundle persistentState) {
        this.checkActivityCallback();
        ActivityInfo info = Activity.mActivityInfo.get(activity);
        String packageName = info != null ? info.packageName : null;
        ContextFixer.fixContext((Context)activity, packageName);
        ActivityFixer.fixActivity(activity);
        AppCallback callback = this.getAppCallback();
        callback.beforeActivityOnCreate(activity);
        super.callActivityOnCreate(activity, icicle, persistentState);
        callback.afterActivityOnCreate(activity);
    }

    @Override
    public void callActivityOnStart(android.app.Activity activity) {
        ActivityInfo info;
        AppCallback callback = this.getAppCallback();
        callback.beforeActivityOnStart(activity);
        super.callActivityOnStart(activity);
        if (!VirtualCore.getConfig().disableSetScreenOrientation(activity.getPackageName()) && (info = Activity.mActivityInfo.get(activity)) != null && info.screenOrientation != -1 && activity.getRequestedOrientation() == -1) {
            activity.setRequestedOrientation(info.screenOrientation);
        }
        callback.afterActivityOnStart(activity);
    }

    @Override
    public void callActivityOnResume(android.app.Activity activity) {
        AppCallback callback = this.getAppCallback();
        callback.beforeActivityOnResume(activity);
        super.callActivityOnResume(activity);
        callback.afterActivityOnResume(activity);
    }

    @Override
    public void callActivityOnStop(android.app.Activity activity) {
        AppCallback callback = this.getAppCallback();
        callback.beforeActivityOnStop(activity);
        super.callActivityOnStop(activity);
        callback.afterActivityOnStop(activity);
    }

    @Override
    public void callActivityOnDestroy(android.app.Activity activity) {
        AppCallback callback = this.getAppCallback();
        callback.beforeActivityOnDestroy(activity);
        super.callActivityOnDestroy(activity);
        callback.afterActivityOnDestroy(activity);
    }

    private boolean checkIsEnvOk(Instrumentation instrumentation) {
        if (instrumentation instanceof InstrumentationVirtualApp) {
            return true;
        }
        Class<?> cls = instrumentation.getClass();
        if (Instrumentation.class.equals(cls)) {
            return false;
        }
        if (TextUtils.equals((CharSequence)VClient.get().getCurrentPackage(), (CharSequence)"com.zhiliaoapp.musically") && Build.VERSION.SDK_INT == 26) {
            return false;
        }
        do {
            Field[] declaredFields;
            if ((declaredFields = cls.getDeclaredFields()) == null) continue;
            for (Field field : declaredFields) {
                if (!Instrumentation.class.isAssignableFrom(field.getType())) continue;
                field.setAccessible(true);
                try {
                    if (!(field.get(instrumentation) instanceof InstrumentationVirtualApp)) continue;
                    return true;
                }
                catch (IllegalAccessException e) {
                    return false;
                }
            }
        } while (!Instrumentation.class.equals(cls = cls.getSuperclass()));
        return false;
    }
}

