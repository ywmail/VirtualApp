/*
 * Decompiled with CFR 0.152.
 */
package com.lody.virtual.client.hook.proxies.camera;

import com.lody.virtual.StringFog;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.ReplaceCallingPkgMethodProxy;
import com.lody.virtual.client.hook.base.ReplaceLastPkgMethodProxy;
import com.lody.virtual.client.hook.base.StaticMethodProxy;
import java.lang.reflect.Method;
import mirror.android.camera.ICameraService;

public class CameraServiceStub
extends BinderInvocationProxy {
    private static final String SERVER_NAME = "media.camera";

    public CameraServiceStub() {
        super(ICameraService.Stub.asInterface, "media.camera");
    }

    @Override
    protected void onBindMethods() {
        super.onBindMethods();
        this.addMethodProxy(new ReplaceCallingPkgMethodProxy("connect"));
        this.addMethodProxy(new StaticMethodProxy("connectDevice"){

            @Override
            public Object call(Object who, Method method, Object ... args) throws Throwable {
                if (args[2] instanceof String) {
                    args[2] = VirtualCore.get().getHostPkg();
                }
                return super.call(who, method, args);
            }
        });
        this.addMethodProxy(new ReplaceLastPkgMethodProxy("connectLegacy"));
    }
}

