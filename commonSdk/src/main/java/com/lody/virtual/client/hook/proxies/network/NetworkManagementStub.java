/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.annotation.TargetApi
 */
package com.lody.virtual.client.hook.proxies.network;

import android.annotation.TargetApi;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.ReplaceUidMethodProxy;
import com.lody.virtual.client.hook.base.StaticMethodProxy;
import java.lang.reflect.Method;
import mirror.android.os.INetworkManagementService;

@TargetApi(value=23)
public class NetworkManagementStub
extends BinderInvocationProxy {
    public NetworkManagementStub() {
        super(INetworkManagementService.Stub.asInterface, "network_management");
    }

    @Override
    protected void onBindMethods() {
        super.onBindMethods();
        this.addMethodProxy(new ReplaceUidMethodProxy("setUidCleartextNetworkPolicy", 0));
        this.addMethodProxy(new ReplaceUidMethodProxy("setUidMeteredNetworkBlacklist", 0));
        this.addMethodProxy(new ReplaceUidMethodProxy("setUidMeteredNetworkWhitelist", 0));
        this.addMethodProxy(new StaticMethodProxy("getNetworkStatsUidDetail"){

            @Override
            public Object call(Object who, Method method, Object ... args) throws Throwable {
                int uid = (Integer)args[0];
                if (uid == getVUid()) {
                    args[0] = getRealUid();
                }
                return super.call(who, method, args);
            }
        });
    }
}

