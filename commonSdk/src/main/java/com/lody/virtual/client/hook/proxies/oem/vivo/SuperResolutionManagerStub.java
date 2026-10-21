/*
 * Decompiled with CFR 0.152.
 */
package com.lody.virtual.client.hook.proxies.oem.vivo;

import com.lody.virtual.StringFog;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.ReplaceFirstPkgMethodProxy;
import mirror.oem.vivo.ISuperResolutionManager;

public class SuperResolutionManagerStub
extends BinderInvocationProxy {
    private static final String SERVER_NAME = "SuperResolutionManager";

    public SuperResolutionManagerStub() {
        super(ISuperResolutionManager.Stub.TYPE, "SuperResolutionManager");
    }

    @Override
    protected void onBindMethods() {
        super.onBindMethods();
        this.addMethodProxy(new ReplaceFirstPkgMethodProxy("registerPackageSettingStateChangeListener"));
        this.addMethodProxy(new ReplaceFirstPkgMethodProxy("unRegisterPackageSettingStateChangeListener"));
        this.addMethodProxy(new ReplaceFirstPkgMethodProxy("registerSuperResolutionStateChange"));
        this.addMethodProxy(new ReplaceFirstPkgMethodProxy("unRegisterSuperResolutionStateChange"));
        this.addMethodProxy(new ReplaceFirstPkgMethodProxy("getPackageSettingState"));
        this.addMethodProxy(new ReplaceFirstPkgMethodProxy("putPackageSettingState"));
    }
}

