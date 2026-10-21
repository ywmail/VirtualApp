/*
 * Decompiled with CFR 0.152.
 */
package com.lody.virtual.client.hook.proxies.app;

import com.lody.virtual.StringFog;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.ReplaceCallingPkgMethodProxy;
import mirror.android.app.ILocaleManager;

public class LocaleManagerStub
extends BinderInvocationProxy {
    public LocaleManagerStub() {
        super(ILocaleManager.Stub.asInterface, "locale");
    }

    @Override
    public void onBindMethods() {
        super.onBindMethods();
        this.addMethodProxy(new ReplaceCallingPkgMethodProxy("setApplicationLocales"));
        this.addMethodProxy(new ReplaceCallingPkgMethodProxy("getApplicationLocales"));
    }
}

