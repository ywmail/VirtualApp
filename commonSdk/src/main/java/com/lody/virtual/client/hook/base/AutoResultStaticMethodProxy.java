/*
 * Decompiled with CFR 0.152.
 */
package com.lody.virtual.client.hook.base;

import com.lody.virtual.client.hook.base.StaticMethodProxy;
import com.lody.virtual.helper.utils.Reflect;
import java.lang.reflect.Method;

public class AutoResultStaticMethodProxy
extends StaticMethodProxy {
    public AutoResultStaticMethodProxy(String name) {
        super(name);
    }

    @Override
    public Object call(Object who, Method method, Object ... args) throws Throwable {
        return this.getDefaultValue(who, method, args);
    }

    public Object getDefaultValue(Object who, Method method, Object ... args) {
        Class<?> type = Reflect.wrapper(method.getReturnType());
        if (type == null) {
            return 0;
        }
        if (type.isPrimitive()) {
            if (Boolean.class == type) {
                return false;
            }
            if (Integer.class == type) {
                return 0;
            }
            if (Long.class == type) {
                return 0L;
            }
            if (Short.class == type) {
                return (short)0;
            }
            if (Byte.class == type) {
                return (byte)0;
            }
            if (Double.class == type) {
                return 0.0;
            }
            if (Float.class == type) {
                return Float.valueOf(0.0f);
            }
            if (Character.class == type) {
                return Character.valueOf('\u0000');
            }
        }
        return null;
    }
}

