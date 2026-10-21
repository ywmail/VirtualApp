/*
 * Decompiled with CFR 0.152.
 */
package com.lody.virtual.client.hook.proxies.oem.vivo;

import com.lody.virtual.StringFog;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.ReplaceCallingPkgAndLastUserIdMethodProxy;
import com.lody.virtual.client.hook.base.ReplaceFirstPkgMethodProxy;
import com.lody.virtual.client.hook.base.ReplaceLastUidMethodProxy;
import com.lody.virtual.client.hook.base.StaticMethodProxy;
import java.lang.reflect.Method;
import mirror.oem.vivo.IVivoPermissonService;

public class VivoPermissionServiceStub
extends BinderInvocationProxy {
    private static final String SERVER_NAME = "vivo_permission_service";

    public VivoPermissionServiceStub() {
        super(IVivoPermissonService.Stub.TYPE, "vivo_permission_service");
    }

    @Override
    protected void onBindMethods() {
        super.onBindMethods();
        this.addMethodProxy(new ReplaceLastUidMethodProxy("checkPermission"));
        this.addMethodProxy(new ReplaceFirstPkgMethodProxy("getAppPermission"));
        this.addMethodProxy(new ReplaceFirstPkgMethodProxy("setAppPermission"));
        this.addMethodProxy(new ReplaceFirstPkgMethodProxy("setWhiteListApp"));
        this.addMethodProxy(new ReplaceFirstPkgMethodProxy("setBlackListApp"));
        this.addMethodProxy(new ReplaceFirstPkgMethodProxy("noteStartActivityProcess"));
        this.addMethodProxy(new ReplaceFirstPkgMethodProxy("isBuildInThirdPartApp"));
        this.addMethodProxy(new ReplaceCallingPkgAndLastUserIdMethodProxy("setOnePermission"));
        this.addMethodProxy(new ReplaceCallingPkgAndLastUserIdMethodProxy("setOnePermissionExt"));
        this.addMethodProxy(new StaticMethodProxy("checkDelete"){

            @Override
            public Object call(Object who, Method method, Object ... args) throws Throwable {
                if (args[1] instanceof String) {
                    args[1] = getHostPkg();
                }
                replaceLastUserId(args);
                return super.call(who, method, args);
            }
        });
        this.addMethodProxy(new ReplaceFirstPkgMethodProxy("isVivoImeiPkg"));
    }
}

