//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.lody.virtual.client.hook.proxies.am;

import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.os.IInterface;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.hook.annotations.Inject;
import com.lody.virtual.client.hook.base.BinderInvocationStub;
import com.lody.virtual.client.hook.base.MethodInvocationProxy;
import com.lody.virtual.client.hook.base.MethodInvocationStub;
import com.lody.virtual.client.hook.base.ReplaceCallingPkgAndLastUserIdMethodProxy;
import com.lody.virtual.client.hook.base.ReplaceCallingPkgMethodProxy;
import com.lody.virtual.client.hook.base.ReplaceLastPkgMethodProxy;
import com.lody.virtual.client.hook.base.ResultStaticMethodProxy;
import com.lody.virtual.client.hook.base.StaticMethodProxy;
import com.lody.virtual.client.ipc.VActivityManager;
import com.lody.virtual.helper.compat.BuildCompat;
import java.lang.reflect.Method;
import java.util.Map;
import mirror.android.app.ActivityManagerNative;
import mirror.android.app.ActivityManagerOreo;
import mirror.android.app.IActivityManager;
import mirror.android.os.ServiceManager;
import mirror.android.util.Singleton;

@Inject(MethodProxies.class)
public class ActivityManagerStub extends MethodInvocationProxy<MethodInvocationStub<IInterface>> {
    public ActivityManagerStub() {
        super(new MethodInvocationStub((IInterface)ActivityManagerNative.getDefault.call(new Object[0])));
    }

    public void inject() {
        Object gDefault;
        if (BuildCompat.isOreo()) {
            gDefault = ActivityManagerOreo.IActivityManagerSingleton.get();
            Singleton.mInstance.set(gDefault, this.getInvocationStub().getProxyInterface());
        } else if (ActivityManagerNative.gDefault.type() == IActivityManager.TYPE) {
            ActivityManagerNative.gDefault.set(this.getInvocationStub().getProxyInterface());
        } else if (ActivityManagerNative.gDefault.type() == Singleton.TYPE) {
            gDefault = ActivityManagerNative.gDefault.get();
            Singleton.mInstance.set(gDefault, this.getInvocationStub().getProxyInterface());
        }

        BinderInvocationStub hookAMBinder = new BinderInvocationStub((IInterface)this.getInvocationStub().getBaseInterface());
        hookAMBinder.copyMethodProxies(this.getInvocationStub());
        ((Map)ServiceManager.sCache.get()).put("activity", hookAMBinder);
    }

    protected void onBindMethods() {
        super.onBindMethods();
        if (VirtualCore.get().isVAppProcess()) {
            this.addMethodProxy(new StaticMethodProxy("setRequestedOrientation") {
                public Object call(Object who, Method method, Object... args) throws Throwable {
                    try {
                        return super.call(who, method, args);
                    } catch (Throwable var5) {
                        var5.printStackTrace();
                        return 0;
                    }
                }
            });
            this.addMethodProxy(new ReplaceCallingPkgAndLastUserIdMethodProxy("getHistoricalProcessExitReasons"));
            this.addMethodProxy(new ResultStaticMethodProxy("registerUidObserver", 0));
            this.addMethodProxy(new ResultStaticMethodProxy("unregisterUidObserver", 0));
            this.addMethodProxy(new ReplaceLastPkgMethodProxy("getAppStartMode"));
            this.addMethodProxy(new ResultStaticMethodProxy("bindupdateConfiguration", 0));
            this.addMethodProxy(new ReplaceCallingPkgMethodProxy("setAppLockedVerifying"));
            this.addMethodProxy(new ReplaceCallingPkgMethodProxy("reportJunkFromApp"));
            this.addMethodProxy(new ReplaceCallingPkgAndLastUserIdMethodProxy("isForcedImmersiveFullScreen"));
            this.addMethodProxy(new StaticMethodProxy("activityResumed") {
                public Object call(Object who, Method method, Object... args) throws Throwable {
                    IBinder token = (IBinder)args[0];
                    VActivityManager.get().onActivityResumed(token);
                    return super.call(who, method, args);
                }
            });
            this.addMethodProxy(new StaticMethodProxy("activityDestroyed") {
                public Object afterCall(Object who, Method method, Object[] args, Object result) throws Throwable {
                    IBinder token = (IBinder)args[0];
                    VActivityManager.get().onActivityDestroy(token);
                    return super.afterCall(who, method, args, result);
                }
            });
            this.addMethodProxy(new StaticMethodProxy("checkUriPermission") {
                public Object call(Object who, Method method, Object... args) throws Throwable {
                    return args[0] instanceof Uri && args[0].toString().equals("content://telephony/carriers/preferapn") ? VirtualCore.get().checkSelfPermission("Manifest.permission.WRITE_APN_SETTINGS", VirtualCore.get().isExtPackage()) ? 0 : -1 : 0;
                }
            });
            this.addMethodProxy(new StaticMethodProxy("finishActivity") {
                public Object call(Object who, Method method, Object... args) throws Throwable {
                    IBinder token = (IBinder)args[0];
                    VActivityManager.get().onFinishActivity(token);
                    return super.call(who, method, args);
                }

                public boolean isEnable() {
                    return isAppProcess();
                }
            });
            this.addMethodProxy(new StaticMethodProxy("finishActivityAffinity") {
                public Object call(Object who, Method method, Object... args) {
                    IBinder token = (IBinder)args[0];
                    return VActivityManager.get().finishActivityAffinity(getAppUserId(), token);
                }

                public boolean isEnable() {
                    return isAppProcess();
                }
            });
        }

        this.addMethodProxy(new ReplaceCallingPkgMethodProxy("getIntentSenderWithFeature"));
    }

    public boolean isEnvBad() {
        return ActivityManagerNative.getDefault.call(new Object[0]) != this.getInvocationStub().getProxyInterface();
    }

    static final class BroadcastIntentWithFeature extends MethodProxies.BroadcastIntent {
        BroadcastIntentWithFeature() {
        }

        public final String getMethodName() {
            return "broadcastIntentWithFeature";
        }

        public final Object call(Object who, Method method, Object[] args) throws Throwable {
            Intent v1 = (Intent)args[2];
            v1.setDataAndType(v1.getData(), (String)args[3]);
            Intent v1_1 = this.handleIntent(v1);
            if (v1_1 == null) {
                return 0;
            } else {
                args[2] = v1_1;
                if (args[8] instanceof String || args[8] instanceof String[]) {
                    args[8] = null;
                }

                return method.invoke(who, args);
            }
        }

        public final boolean isEnable() {
            return isAppProcess();
        }
    }
}
