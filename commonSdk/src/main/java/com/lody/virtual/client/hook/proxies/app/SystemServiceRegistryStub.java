/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 */
package com.lody.virtual.client.hook.proxies.app;

import android.content.Context;
import com.lody.virtual.client.hook.base.MethodInvocationProxy;
import com.lody.virtual.client.hook.base.MethodInvocationStub;
import mirror.android.app.SystemServiceRegistry;

public class SystemServiceRegistryStub
extends MethodInvocationProxy<MethodInvocationStub<Object>> {
    public SystemServiceRegistryStub(Context context, String name) {
        super(new MethodInvocationStub<Object>(SystemServiceRegistry.getSystemService.call(context, name)));
    }

    @Override
    public void inject() throws Throwable {
    }

    @Override
    protected void onBindMethods() {
        super.onBindMethods();
    }

    @Override
    public boolean isEnvBad() {
        return false;
    }
}

