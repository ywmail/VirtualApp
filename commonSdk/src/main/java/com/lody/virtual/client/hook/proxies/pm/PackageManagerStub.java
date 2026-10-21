/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.IInterface
 */
package com.lody.virtual.client.hook.proxies.pm;

import android.content.Context;
import android.os.IInterface;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.fixer.ContextFixer;
import com.lody.virtual.client.hook.annotations.Inject;
import com.lody.virtual.client.hook.base.BinderInvocationStub;
import com.lody.virtual.client.hook.base.MethodInvocationProxy;
import com.lody.virtual.client.hook.base.MethodInvocationStub;
import com.lody.virtual.client.hook.base.ReplaceCallingPkgMethodProxy;
import com.lody.virtual.client.hook.base.ResultStaticMethodProxy;
import com.lody.virtual.client.hook.proxies.pm.MethodProxies;
import com.lody.virtual.helper.compat.BuildCompat;
import com.lody.virtual.helper.utils.Reflect;
import mirror.android.app.ActivityThread;
import mirror.huawei.android.app.HwApiCacheManagerEx;

@Inject(value=MethodProxies.class)
public final class PackageManagerStub
extends MethodInvocationProxy<MethodInvocationStub<IInterface>> {
    public PackageManagerStub() {
        super(new MethodInvocationStub<IInterface>(ActivityThread.sPackageManager.get()));
    }

    @Override
    protected void onBindMethods() {
        super.onBindMethods();
        this.addMethodProxy(new ResultStaticMethodProxy("addPermissionAsync", true));
        this.addMethodProxy(new ResultStaticMethodProxy("addPermission", true));
        this.addMethodProxy(new ResultStaticMethodProxy("performDexOpt", true));
        this.addMethodProxy(new ResultStaticMethodProxy("performDexOptIfNeeded", false));
        this.addMethodProxy(new ResultStaticMethodProxy("performDexOptSecondary", true));
        this.addMethodProxy(new ResultStaticMethodProxy("addOnPermissionsChangeListener", 0));
        this.addMethodProxy(new ResultStaticMethodProxy("removeOnPermissionsChangeListener", 0));
        this.addMethodProxy(new ReplaceCallingPkgMethodProxy("shouldShowRequestPermissionRationale"));
        if (BuildCompat.isOreo()) {
            this.addMethodProxy(new ResultStaticMethodProxy("notifyDexLoad", 0));
            this.addMethodProxy(new ResultStaticMethodProxy("notifyPackageUse", 0));
            this.addMethodProxy(new ResultStaticMethodProxy("setInstantAppCookie", false));
            this.addMethodProxy(new ResultStaticMethodProxy("isInstantApp", false));
        }
        this.addMethodProxy(new ReplaceCallingPkgMethodProxy("isPackageSuspendedForUser"));
        this.addMethodProxy(new ResultStaticMethodProxy("checkPackageStartable", 0));
    }

    @Override
    public void inject() throws Throwable {
        IInterface hookedPM = (IInterface)((MethodInvocationStub)this.getInvocationStub()).getProxyInterface();
        ActivityThread.sPackageManager.set(hookedPM);
        BinderInvocationStub pmHookBinder = new BinderInvocationStub((IInterface)((MethodInvocationStub)this.getInvocationStub()).getBaseInterface());
        pmHookBinder.copyMethodProxies((MethodInvocationStub)this.getInvocationStub());
        pmHookBinder.replaceService("package");
        try {
            Context systemContext = (Context)Reflect.on(VirtualCore.mainThread()).call("getSystemContext").get();
            Object systemContextPm = Reflect.on(systemContext).field("mPackageManager").get();
            if (systemContextPm != null) {
                Reflect.on(systemContext).field("mPackageManager").set("mPM", hookedPM);
            }
        }
        catch (Throwable e) {
            e.printStackTrace();
        }
        ContextFixer.fixContext(VirtualCore.get().getContext(), null);
        if (HwApiCacheManagerEx.mPkg != null) {
            HwApiCacheManagerEx.mPkg.set(HwApiCacheManagerEx.getDefault.call(new Object[0]), VirtualCore.getPM());
        }
    }

    @Override
    public boolean isEnvBad() {
        return ((MethodInvocationStub)this.getInvocationStub()).getProxyInterface() != ActivityThread.sPackageManager.get();
    }
}

