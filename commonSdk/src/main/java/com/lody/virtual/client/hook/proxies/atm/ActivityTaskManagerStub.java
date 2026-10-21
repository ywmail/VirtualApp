//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.lody.virtual.client.hook.proxies.atm;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.IBinder;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.hook.annotations.Inject;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.BinderInvocationStub;
import com.lody.virtual.client.hook.base.StaticMethodProxy;
import com.lody.virtual.client.ipc.VActivityManager;
import com.lody.virtual.helper.compat.BuildCompat;
import com.lody.virtual.helper.utils.ComponentUtils;
import com.lody.virtual.helper.utils.Reflect;
import com.lody.virtual.os.VUserHandle;
import java.lang.reflect.Method;
import mirror.android.app.IActivityTaskManager.Stub;
import mirror.android.util.Singleton;

@Inject(MethodProxies.class)
@TargetApi(29)
public class ActivityTaskManagerStub extends BinderInvocationProxy {
    public ActivityTaskManagerStub() {
        super(Stub.asInterface, "activity_task");

        try {
            Object singleton = Reflect.on("android.app.ActivityTaskManager").field("IActivityTaskManagerSingleton").get();
            Singleton.mInstance.set(singleton, ((BinderInvocationStub)this.getInvocationStub()).getProxyInterface());
        } catch (Exception var2) {
            var2.printStackTrace();
        }

    }

    protected void onBindMethods() {
        super.onBindMethods();
        this.addMethodProxy(new StaticMethodProxy("activityDestroyed") {
            public Object afterCall(Object who, Method method, Object[] args, Object result) throws Throwable {
                IBinder token = (IBinder)args[0];
                VActivityManager.get().onActivityDestroy(token);
                return super.afterCall(who, method, args, result);
            }
        });
        this.addMethodProxy(new StaticMethodProxy("activityResumed") {
            public Object call(Object who, Method method, Object... args) throws Throwable {
                IBinder token = (IBinder)args[0];
                VActivityManager.get().onActivityResumed(token);
                return super.call(who, method, args);
            }
        });
        this.addMethodProxy(new StaticMethodProxy("finishActivity") {
            public Object call(Object who, Method method, Object... args) throws Throwable {
                IBinder token = (IBinder)args[0];
                Intent intent = (Intent)args[2];
                if (intent != null) {
                    args[2] = ComponentUtils.processOutsideIntent(VUserHandle.myUserId(), VirtualCore.get().isExtPackage(), intent);
                }

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
        if (BuildCompat.isSamsung()) {
            this.addMethodProxy(new StaticMethodProxy("startAppLockService") {
                public Object call(Object who, Method method, Object... args) {
                    return 0;
                }
            });
        }

    }
}
