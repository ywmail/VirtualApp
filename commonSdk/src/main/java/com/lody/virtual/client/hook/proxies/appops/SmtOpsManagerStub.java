/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.annotation.TargetApi
 */
package com.lody.virtual.client.hook.proxies.appops;

import android.annotation.TargetApi;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.hook.annotations.Inject;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.proxies.appops.MethodProxies;
import mirror.com.android.internal.app.ISmtOpsService;

@Inject(value=MethodProxies.class)
@TargetApi(value=19)
public class SmtOpsManagerStub
extends BinderInvocationProxy {
    public SmtOpsManagerStub() {
        super(ISmtOpsService.Stub.asInterface, "smtops");
    }
}

