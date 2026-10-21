/*
 * Decompiled with CFR 0.152.
 */
package com.lody.virtual.client.hook.proxies.content.integrity;

import com.lody.virtual.StringFog;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.ResultStaticMethodProxy;
import com.lody.virtual.helper.compat.ParceledListSliceCompat;
import java.util.Collections;
import mirror.android.content.integrity.IAppIntegrityManager;

public class AppIntegrityManagerStub
extends BinderInvocationProxy {
    private static final String SERVER_NAME = "app_integrity";

    public AppIntegrityManagerStub() {
        super(IAppIntegrityManager.Stub.asInterface, "app_integrity");
    }

    @Override
    protected void onBindMethods() {
        super.onBindMethods();
        this.addMethodProxy(new ResultStaticMethodProxy("updateRuleSet", null));
        this.addMethodProxy(new ResultStaticMethodProxy("getCurrentRuleSetVersion", ""));
        this.addMethodProxy(new ResultStaticMethodProxy("getCurrentRuleSetProvider", ""));
        this.addMethodProxy(new ResultStaticMethodProxy("getCurrentRules", ParceledListSliceCompat.create(Collections.emptyList())));
        this.addMethodProxy(new ResultStaticMethodProxy("getWhitelistedRuleProviders", Collections.emptyList()));
    }
}

