/*
 * Decompiled with CFR 0.152.
 */
package com.lody.virtual.client.hook.base;

import com.lody.virtual.client.hook.base.StaticMethodProxy;
import java.lang.reflect.Method;

public class ResultStaticMethodProxy
extends StaticMethodProxy {
    Object mResult;

    public ResultStaticMethodProxy(String name, Object result) {
        super(name);
        this.mResult = result;
    }

    public Object getResult() {
        return this.mResult;
    }

    @Override
    public Object call(Object who, Method method, Object ... args) throws Throwable {
        return this.mResult;
    }
}

