/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.IInterface
 */
package com.lody.virtual.client.hook.proxies.content;

import android.os.IInterface;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.hook.annotations.Inject;
import com.lody.virtual.client.hook.annotations.LogInvocation;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.BinderInvocationStub;
import com.lody.virtual.client.hook.proxies.content.MethodProxies;
import mirror.android.content.ContentResolver;
import mirror.android.content.IContentService;

@LogInvocation
@Inject(value=MethodProxies.class)
public class ContentServiceStub
extends BinderInvocationProxy {
    private static final String TAG = ContentServiceStub.class.getSimpleName();

    public ContentServiceStub() {
        super(IContentService.Stub.asInterface, "content");
    }

    @Override
    public void inject() throws Throwable {
        super.inject();
        ContentResolver.sContentService.set((IInterface)((BinderInvocationStub)this.getInvocationStub()).getProxyInterface());
    }
}

