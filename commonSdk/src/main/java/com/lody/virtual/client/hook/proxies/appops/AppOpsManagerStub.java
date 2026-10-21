/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.AppOpsManager
 *  android.os.IInterface
 */
package com.lody.virtual.client.hook.proxies.appops;

import android.os.IInterface;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.hook.annotations.Inject;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.BinderInvocationStub;
import com.lody.virtual.client.hook.proxies.appops.MethodProxies;
import mirror.android.app.AppOpsManager;
import mirror.com.android.internal.app.IAppOpsService;

@Inject(value=MethodProxies.class)
public class AppOpsManagerStub
extends BinderInvocationProxy {
    public AppOpsManagerStub() {
        super(IAppOpsService.Stub.asInterface, "appops");
    }

    @Override
    public void inject() throws Throwable {
        super.inject();
        if (AppOpsManager.mService != null) {
            android.app.AppOpsManager appOpsManager = (android.app.AppOpsManager)VirtualCore.get().getContext().getSystemService("appops");
            try {
                AppOpsManager.mService.set(appOpsManager, (IInterface)((BinderInvocationStub)this.getInvocationStub()).getProxyInterface());
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onBindMethods() {
        super.onBindMethods();
    }
}

