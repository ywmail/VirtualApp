/*
 * Decompiled with CFR 0.152.
 */
package com.lody.virtual.client.hook.proxies.cross_profile;

import com.lody.virtual.StringFog;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.ReplaceCallingPkgMethodProxy;
import com.lody.virtual.client.hook.base.ResultStaticMethodProxy;
import mirror.android.content.pm.ICrossProfileApps;

public class CrossProfileAppsStub
extends BinderInvocationProxy {
    public CrossProfileAppsStub() {
        super(ICrossProfileApps.Stub.asInterface, "crossprofileapps");
    }

    @Override
    protected void onBindMethods() {
        super.onBindMethods();
        this.addMethodProxy(new ReplaceCallingPkgMethodProxy("getTargetUserProfiles"));
        this.addMethodProxy(new ResultStaticMethodProxy("startActivityAsUser", null));
    }
}

