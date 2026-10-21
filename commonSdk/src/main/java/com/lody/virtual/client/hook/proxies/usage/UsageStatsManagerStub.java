/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.annotation.TargetApi
 */
package com.lody.virtual.client.hook.proxies.usage;

import android.annotation.TargetApi;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.ReplaceCallingPkgMethodProxy;
import com.lody.virtual.client.hook.base.ReplaceLastPkgMethodProxy;
import com.lody.virtual.client.hook.base.ResultStaticMethodProxy;
import com.lody.virtual.client.hook.base.StaticMethodProxy;
import com.lody.virtual.client.ipc.VActivityManager;
import java.lang.reflect.Method;
import mirror.android.app.IUsageStatsManager;

@TargetApi(value=22)
public class UsageStatsManagerStub
extends BinderInvocationProxy {
    public UsageStatsManagerStub() {
        super(IUsageStatsManager.Stub.asInterface, "usagestats");
    }

    @Override
    protected void onBindMethods() {
        super.onBindMethods();
        this.addMethodProxy(new ReplaceCallingPkgMethodProxy("getAppStandbyBucket"));
        this.addMethodProxy(new ReplaceLastPkgMethodProxy("queryUsageStats"));
        this.addMethodProxy(new ReplaceLastPkgMethodProxy("queryConfigurations"));
        this.addMethodProxy(new ReplaceLastPkgMethodProxy("queryEvents"));
        this.addMethodProxy(new StaticMethodProxy("setAppInactive"){

            @Override
            public Object call(Object who, Method method, Object ... args) {
                int userId = args.length > 2 ? (Integer)args[2] : 0;
                VActivityManager.get().setAppInactive((String)args[0], (Boolean)args[1], userId);
                return 0;
            }
        });
        this.addMethodProxy(new StaticMethodProxy("isAppInactive"){

            @Override
            public Object call(Object who, Method method, Object ... args) {
                int userId = args.length > 1 ? (Integer)args[1] : 0;
                return VActivityManager.get().isAppInactive((String)args[0], userId);
            }
        });
        this.addMethodProxy(new ReplacePkgAndUserIdMethodProxy("whitelistAppTemporarily"));
        this.addMethodProxy(new ResultStaticMethodProxy("setAppStandbyBucket", null));
        this.addMethodProxy(new ResultStaticMethodProxy("setAppStandbyBuckets", null));
        this.addMethodProxy(new ResultStaticMethodProxy("registerAppUsageObserver", null));
        this.addMethodProxy(new ResultStaticMethodProxy("unregisterAppUsageObserver", null));
    }

    private class ReplacePkgAndUserIdMethodProxy
    extends ReplaceLastPkgMethodProxy {
        public ReplacePkgAndUserIdMethodProxy(String name) {
            super(name);
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            if (args[args.length - 1] instanceof Integer) {
                args[args.length - 1] = 0;
            }
            return super.call(who, method, args);
        }
    }
}

