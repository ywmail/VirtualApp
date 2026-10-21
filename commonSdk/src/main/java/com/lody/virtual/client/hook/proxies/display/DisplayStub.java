/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.annotation.TargetApi
 *  android.os.IInterface
 */
package com.lody.virtual.client.hook.proxies.display;

import android.annotation.TargetApi;
import android.os.IInterface;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.hook.base.MethodInvocationProxy;
import com.lody.virtual.client.hook.base.MethodInvocationStub;
import com.lody.virtual.client.hook.base.ReplaceCallingPkgMethodProxy;
import mirror.android.hardware.display.DisplayManagerGlobal;

@TargetApi(value=17)
public class DisplayStub
extends MethodInvocationProxy<MethodInvocationStub<IInterface>> {
    public DisplayStub() {
        super(new MethodInvocationStub<IInterface>(DisplayManagerGlobal.mDm.get(DisplayManagerGlobal.getInstance.call(new Object[0]))));
    }

    @Override
    protected void onBindMethods() {
        super.onBindMethods();
        this.addMethodProxy(new ReplaceCallingPkgMethodProxy("createVirtualDisplay"));
    }

    @Override
    public void inject() throws Throwable {
        Object dmg = DisplayManagerGlobal.getInstance.call(new Object[0]);
        DisplayManagerGlobal.mDm.set(dmg, (IInterface)((MethodInvocationStub)this.getInvocationStub()).getProxyInterface());
    }

    @Override
    public boolean isEnvBad() {
        Object dmg = DisplayManagerGlobal.getInstance.call(new Object[0]);
        IInterface mDm = DisplayManagerGlobal.mDm.get(dmg);
        return mDm != ((MethodInvocationStub)this.getInvocationStub()).getProxyInterface();
    }
}

