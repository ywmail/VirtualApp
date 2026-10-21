/*
 * Decompiled with CFR 0.152.
 */
package com.lody.virtual.client.hook.proxies.oem.vivo;

import com.lody.virtual.StringFog;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.ReplaceFirstPkgMethodProxy;
import com.lody.virtual.client.hook.base.ReplaceLastPkgMethodProxy;
import mirror.oem.vivo.ISystemDefenceManager;

public class SystemDefenceManagerStub
extends BinderInvocationProxy {
    private static final String SERVER_NAME = "system_defence_service";

    public SystemDefenceManagerStub() {
        super(ISystemDefenceManager.Stub.TYPE, "system_defence_service");
    }

    @Override
    protected void onBindMethods() {
        super.onBindMethods();
        this.addMethodProxy(new ReplaceFirstPkgMethodProxy("checkTransitionTimoutErrorDefence"));
        this.addMethodProxy(new ReplaceLastPkgMethodProxy("checkSkipKilledByRemoveTask"));
        this.addMethodProxy(new ReplaceFirstPkgMethodProxy("checkSmallIconNULLPackage"));
        this.addMethodProxy(new ReplaceFirstPkgMethodProxy("checkDelayUpdate"));
        this.addMethodProxy(new ReplaceFirstPkgMethodProxy("onSetActivityResumed"));
        this.addMethodProxy(new ReplaceFirstPkgMethodProxy("checkReinstallPacakge"));
        this.addMethodProxy(new ReplaceFirstPkgMethodProxy("reportFgCrashData"));
    }
}

