/*
 * Decompiled with CFR 0.152.
 */
package com.lody.virtual.client.hook.base;

import com.lody.virtual.client.hook.base.StaticMethodProxy;
import java.lang.reflect.Method;

public class ReplaceLastUserIdMethodProxy
extends StaticMethodProxy {
    public ReplaceLastUserIdMethodProxy(String name) {
        super(name);
    }

    @Override
    public boolean beforeCall(Object who, Method method, Object ... args) {
        ReplaceLastUserIdMethodProxy.replaceLastUserId(args);
        return super.beforeCall(who, method, args);
    }
}

