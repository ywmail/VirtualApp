/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Build$VERSION
 *  android.os.IInterface
 */
package com.lody.virtual.client.hook.proxies.window;

import android.os.Build;
import android.os.IInterface;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.hook.annotations.Inject;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.BinderInvocationStub;
import com.lody.virtual.client.hook.base.ReplaceCallingPkgMethodProxy;
import com.lody.virtual.client.hook.base.StaticMethodProxy;
import com.lody.virtual.client.hook.proxies.window.MethodProxies;
import mirror.android.view.Display;
import mirror.android.view.IWindowManager;
import mirror.android.view.WindowManagerGlobal;
import mirror.com.android.internal.policy.PhoneWindow;

@Inject(value=MethodProxies.class)
public class WindowManagerStub
extends BinderInvocationProxy {
    public WindowManagerStub() {
        super(IWindowManager.Stub.asInterface, "window");
    }

    @Override
    public void inject() throws Throwable {
        super.inject();
        if (Build.VERSION.SDK_INT >= 17) {
            if (WindowManagerGlobal.sWindowManagerService != null) {
                WindowManagerGlobal.sWindowManagerService.set((IInterface)((BinderInvocationStub)this.getInvocationStub()).getProxyInterface());
            }
        } else if (Display.sWindowManager != null) {
            Display.sWindowManager.set((IInterface)((BinderInvocationStub)this.getInvocationStub()).getProxyInterface());
        }
        if (PhoneWindow.TYPE != null) {
            PhoneWindow.sWindowManager.set((IInterface)((BinderInvocationStub)this.getInvocationStub()).getProxyInterface());
        }
    }

    @Override
    protected void onBindMethods() {
        super.onBindMethods();
        this.addMethodProxy(new StaticMethodProxy("addAppToken"));
        this.addMethodProxy(new StaticMethodProxy("setScreenCaptureDisabled"));
        this.addMethodProxy(new ReplaceCallingPkgMethodProxy("isPackageWaterfallExpanded"));
    }
}

