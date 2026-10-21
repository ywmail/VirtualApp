/*
 * Decompiled with CFR 0.152.
 */
package com.lody.virtual.client.hook.proxies.textservices;

import com.lody.virtual.StringFog;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.ReplaceFirstUserIdMethodProxy;
import mirror.com.android.internal.textservice.ITextServicesManager;

public class TextServicesManagerServiceStub
extends BinderInvocationProxy {
    public TextServicesManagerServiceStub() {
        super(ITextServicesManager.Stub.asInterface, "textservices");
    }

    @Override
    protected void onBindMethods() {
        TextServicesManagerServiceStub.super.onBindMethods();
        this.addMethodProxy(new ReplaceFirstUserIdMethodProxy("getCurrentSpellChecker"));
        this.addMethodProxy(new ReplaceFirstUserIdMethodProxy("getCurrentSpellCheckerSubtype"));
        this.addMethodProxy(new ReplaceFirstUserIdMethodProxy("getSpellCheckerService"));
        this.addMethodProxy(new ReplaceFirstUserIdMethodProxy("finishSpellCheckerService"));
        this.addMethodProxy(new ReplaceFirstUserIdMethodProxy("isSpellCheckerEnabled"));
        this.addMethodProxy(new ReplaceFirstUserIdMethodProxy("getEnabledSpellCheckers"));
    }
}

