/*
 * Decompiled with CFR 0.152.
 */
package com.lody.virtual.client.hook.proxies.device;

import com.lody.virtual.StringFog;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.ReplaceCallingPkgMethodProxy;
import mirror.android.os.IDeviceIdleController;

public class DeviceIdleControllerStub
extends BinderInvocationProxy {
    public DeviceIdleControllerStub() {
        super(IDeviceIdleController.Stub.asInterface, "deviceidle");
    }

    @Override
    protected void onBindMethods() {
        super.onBindMethods();
        this.addMethodProxy(new ReplaceCallingPkgMethodProxy("addPowerSaveWhitelistApp"));
        this.addMethodProxy(new ReplaceCallingPkgMethodProxy("removePowerSaveWhitelistApp"));
        this.addMethodProxy(new ReplaceCallingPkgMethodProxy("removeSystemPowerWhitelistApp"));
        this.addMethodProxy(new ReplaceCallingPkgMethodProxy("restoreSystemPowerWhitelistApp"));
        this.addMethodProxy(new ReplaceCallingPkgMethodProxy("isPowerSaveWhitelistExceptIdleApp"));
        this.addMethodProxy(new ReplaceCallingPkgMethodProxy("isPowerSaveWhitelistApp"));
    }
}

