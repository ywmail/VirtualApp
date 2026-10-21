/*
 * Decompiled with CFR 0.152.
 */
package com.lody.virtual.client.hook.base;

import com.lody.virtual.client.hook.base.StaticMethodProxy;
import java.lang.reflect.Method;

public class ReplaceUidMethodProxy
extends StaticMethodProxy {
    private final int index;

    public ReplaceUidMethodProxy(String name, int index) {
        super(name);
        this.index = index;
    }

    @Override
    public boolean beforeCall(Object who, Method method, Object ... args) {
        int uid = (Integer)args[this.index];
        if (uid == ReplaceUidMethodProxy.getVUid() || uid == ReplaceUidMethodProxy.getBaseVUid()) {
            args[this.index] = ReplaceUidMethodProxy.getRealUid();
        }
        return super.beforeCall(who, method, args);
    }
}

