/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.annotation.TargetApi
 */
package com.lody.virtual.client.hook.proxies.media.router;

import android.annotation.TargetApi;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.ReplaceCallingPkgMethodProxy;
import mirror.android.media.IMediaRouterService;

@TargetApi(value=16)
public class MediaRouterServiceStub
extends BinderInvocationProxy {
    public MediaRouterServiceStub() {
        super(IMediaRouterService.Stub.asInterface, "media_router");
    }

    @Override
    protected void onBindMethods() {
        super.onBindMethods();
        this.addMethodProxy(new ReplaceCallingPkgMethodProxy("registerClientAsUser"));
    }
}

