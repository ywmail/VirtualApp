/*
 * Decompiled with CFR 0.152.
 */
package com.lody.virtual.client.hook.base;

import com.lody.virtual.client.hook.base.MethodProxy;

public class StaticMethodProxy
extends MethodProxy {
    private String mName;

    public StaticMethodProxy(String name) {
        this.mName = name;
    }

    @Override
    public String getMethodName() {
        return this.mName;
    }
}

