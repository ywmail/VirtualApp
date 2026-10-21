/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.annotation.TargetApi
 *  android.os.IInterface
 */
package com.lody.virtual.client.hook.proxies.input;

import android.annotation.TargetApi;
import android.os.IInterface;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.hook.annotations.Inject;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.BinderInvocationStub;
import com.lody.virtual.client.hook.base.ReplaceLastUserIdMethodProxy;
import com.lody.virtual.client.hook.proxies.input.MethodProxies;
import mirror.com.android.internal.view.inputmethod.InputMethodManager;

@Inject(value=MethodProxies.class)
@TargetApi(value=16)
public class InputMethodManagerStub
extends BinderInvocationProxy {
    public InputMethodManagerStub() {
        super(InputMethodManager.mService.get(VirtualCore.get().getContext().getSystemService("input_method")), "input_method");
    }

    @Override
    public void inject() throws Throwable {
        Object inputMethodManager = this.getContext().getSystemService("input_method");
        InputMethodManager.mService.set(inputMethodManager, (IInterface)((BinderInvocationStub)this.getInvocationStub()).getProxyInterface());
        ((BinderInvocationStub)this.getInvocationStub()).replaceService("input_method");
    }

    @Override
    protected void onBindMethods() {
        super.onBindMethods();
        this.addMethodProxy(new ReplaceLastUserIdMethodProxy("getInputMethodList"));
        this.addMethodProxy(new ReplaceLastUserIdMethodProxy("getEnabledInputMethodList"));
    }

    @Override
    public boolean isEnvBad() {
        Object inputMethodManager = this.getContext().getSystemService("input_method");
        return InputMethodManager.mService.get(inputMethodManager) != ((BinderInvocationStub)this.getInvocationStub()).getBaseInterface();
    }
}

