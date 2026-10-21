/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.IBinder
 *  android.os.IInterface
 */
package com.lody.virtual.client.hook.base;

import android.os.IBinder;
import android.os.IInterface;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.hook.base.BinderInvocationStub;
import com.lody.virtual.client.hook.base.MethodInvocationProxy;
import com.lody.virtual.helper.utils.VLog;
import mirror.RefStaticMethod;
import mirror.android.os.ServiceManager;

public abstract class BinderInvocationProxy
extends MethodInvocationProxy<BinderInvocationStub> {
    protected String mServiceName;

    public BinderInvocationProxy(IInterface stub, String serviceName) {
        this(new BinderInvocationStub(stub), serviceName);
    }

    public BinderInvocationProxy(RefStaticMethod<IInterface> asInterfaceMethod, String serviceName) {
        this(new BinderInvocationStub(asInterfaceMethod, BinderInvocationProxy.getService(serviceName)), serviceName);
    }

    public BinderInvocationProxy(Class<?> stubClass, String serviceName) {
        this(new BinderInvocationStub(stubClass, BinderInvocationProxy.getService(serviceName)), serviceName);
    }

    private static IBinder getService(String serviceName) {
        return ServiceManager.getService.call(serviceName);
    }

    public BinderInvocationProxy(BinderInvocationStub hookDelegate, String serviceName) {
        super(hookDelegate);
        if (hookDelegate.getBaseInterface() == null) {
            VLog.d("BinderInvocationProxy", "Unable to build HookDelegate: %s.", serviceName);
        }
        this.mServiceName = serviceName;
    }

    @Override
    public void inject() throws Throwable {
        ((BinderInvocationStub)this.getInvocationStub()).replaceService(this.mServiceName);
    }

    @Override
    public boolean isEnvBad() {
        IBinder binder = ServiceManager.getService.call(this.mServiceName);
        return binder != null && this.getInvocationStub() != binder;
    }
}

