/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.annotation.TargetApi
 *  android.os.IInterface
 *  android.os.health.SystemHealthManager
 */
package com.lody.virtual.client.hook.proxies.battery_stats;

import android.annotation.TargetApi;
import android.os.IInterface;
import android.os.health.SystemHealthManager;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.BinderInvocationStub;
import com.lody.virtual.client.hook.base.ReplaceLastUidMethodProxy;
import java.lang.reflect.Method;
import mirror.com.android.internal.app.IBatteryStats;

@TargetApi(value=24)
public class BatteryStatsHub
extends BinderInvocationProxy {
    private static final String SERVICE_NAME = "batterystats";

    public BatteryStatsHub() {
        super(IBatteryStats.Stub.asInterface, "batterystats");
    }

    @Override
    public void inject() throws Throwable {
        super.inject();
        if (mirror.android.os.health.SystemHealthManager.mBatteryStats != null) {
            SystemHealthManager manager = (SystemHealthManager)VirtualCore.get().getContext().getSystemService("systemhealth");
            mirror.android.os.health.SystemHealthManager.mBatteryStats.set(manager, (IInterface)((BinderInvocationStub)this.getInvocationStub()).getProxyInterface());
        }
    }

    @Override
    protected void onBindMethods() {
        super.onBindMethods();
        this.addMethodProxy(new ReplaceLastUidMethodProxy("takeUidSnapshot"){

            @Override
            public Object call(Object who, Method method, Object ... args) {
                try {
                    return super.call(who, method, args);
                }
                catch (Throwable e) {
                    return null;
                }
            }
        });
    }
}

