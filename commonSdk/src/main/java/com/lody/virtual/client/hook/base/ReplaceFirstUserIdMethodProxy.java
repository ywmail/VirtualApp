/*
 * Decompiled with CFR 0.152.
 */
package com.lody.virtual.client.hook.base;

import com.lody.virtual.client.hook.base.StaticMethodProxy;
import java.lang.reflect.Method;

public class ReplaceFirstUserIdMethodProxy
extends StaticMethodProxy {
    public ReplaceFirstUserIdMethodProxy(String name) {
        super(name);
    }

    @Override
    public boolean beforeCall(Object who, Method method, Object ... args) {
        ReplaceFirstUserIdMethodProxy.replaceFirstUserId(args);
        return ReplaceFirstUserIdMethodProxy.super.beforeCall(who, method, args);
    }
}

