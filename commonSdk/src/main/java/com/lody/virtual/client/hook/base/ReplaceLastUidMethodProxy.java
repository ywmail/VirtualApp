/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Process
 */
package com.lody.virtual.client.hook.base;

import android.os.Process;
import com.lody.virtual.client.hook.base.StaticMethodProxy;
import com.lody.virtual.helper.utils.ArrayUtils;
import java.lang.reflect.Method;

public class ReplaceLastUidMethodProxy
extends StaticMethodProxy {
    public ReplaceLastUidMethodProxy(String name) {
        super(name);
    }

    @Override
    public boolean beforeCall(Object who, Method method, Object ... args) {
        int uid;
        int index = ArrayUtils.indexOfLast(args, Integer.class);
        if (index != -1 && (uid = ((Integer)args[index]).intValue()) == Process.myUid()) {
            args[index] = ReplaceLastUidMethodProxy.getRealUid();
        }
        return super.beforeCall(who, method, args);
    }
}

