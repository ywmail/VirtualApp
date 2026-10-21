/*
 * Decompiled with CFR 0.152.
 */
package com.lody.virtual.client.hook.proxies.connectivity;

import com.lody.virtual.StringFog;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.ReplaceFirstPkgMethodProxy;
import com.lody.virtual.client.hook.base.ResultStaticMethodProxy;
import mirror.android.net.IConnectivityManager;

public class ConnectivityStub
extends BinderInvocationProxy {
    public ConnectivityStub() {
        super(IConnectivityManager.Stub.asInterface, "connectivity");
    }

    @Override
    protected void onBindMethods() {
        super.onBindMethods();
        this.addMethodProxy(new ResultStaticMethodProxy("isTetheringSupported", true));
        this.addMethodProxy(new ReplaceFirstPkgMethodProxy("requestNetwork"));
        this.addMethodProxy(new ReplaceFirstPkgMethodProxy("getNetworkCapabilities"));
        this.addMethodProxy(new ReplaceFirstPkgMethodProxy("listenForNetwork"));
    }
}

