/*
 * Decompiled with CFR 0.152.
 */
package com.lody.virtual.client.hook.proxies.accessibility;

import com.lody.virtual.StringFog;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.StaticMethodProxy;
import java.lang.reflect.Method;
import mirror.android.view.accessibility.IAccessibilityManager;

public class AccessibilityManagerStub
extends BinderInvocationProxy {
    public AccessibilityManagerStub() {
        super(IAccessibilityManager.Stub.TYPE, "accessibility");
    }

    @Override
    protected void onBindMethods() {
        super.onBindMethods();
        this.addMethodProxy(new ReplaceLastUserIdProxy("addClient"));
        this.addMethodProxy(new ReplaceLastUserIdProxy("sendAccessibilityEvent"));
        this.addMethodProxy(new ReplaceLastUserIdProxy("getInstalledAccessibilityServiceList"));
        this.addMethodProxy(new ReplaceLastUserIdProxy("getEnabledAccessibilityServiceList"));
        this.addMethodProxy(new ReplaceLastUserIdProxy("getWindowToken"));
        this.addMethodProxy(new ReplaceLastUserIdProxy("interrupt"));
        this.addMethodProxy(new ReplaceLastUserIdProxy("addAccessibilityInteractionConnection"));
    }

    private static class ReplaceLastUserIdProxy
    extends StaticMethodProxy {
        public ReplaceLastUserIdProxy(String name) {
            super(name);
        }

        @Override
        public boolean beforeCall(Object who, Method method, Object ... args) {
            int index = args.length - 1;
            if (index >= 0 && args[index] instanceof Integer) {
                args[index] = ReplaceLastUserIdProxy.getRealUserId();
            }
            return super.beforeCall(who, method, args);
        }
    }
}

