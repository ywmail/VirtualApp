/*
 * Decompiled with CFR 0.152.
 */
package com.swift.sandhook.xposedcompat.hookstub;

import com.swift.sandhook.SandHook;
import com.swift.sandhook.utils.ParamWrapper;
import java.lang.reflect.Constructor;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class HookMethodEntity {
    public Member origin;
    public Method hook;
    public Method backup;
    public Class[] parType;
    public Class retType;
    public boolean isStatic;

    public HookMethodEntity(Member origin, Method hook, Method backup) {
        this.origin = origin;
        this.hook = hook;
        this.backup = backup;
        this.isStatic = Modifier.isStatic(origin.getModifiers());
    }

    public Object[] getArgs(long ... addresses) {
        if (addresses == null || addresses.length == 0) {
            return new Object[0];
        }
        if (this.parType == null || this.parType.length == 0) {
            return new Object[0];
        }
        int argStart = 0;
        if (!this.isStatic) {
            argStart = 1;
        }
        Object[] args = new Object[this.parType.length];
        for (int i = argStart; i < this.parType.length + argStart; ++i) {
            args[i - argStart] = this.getArg(i - argStart, addresses[i]);
        }
        return args;
    }

    public long[] getArgsAddress(long[] oldAddress, Object ... args) {
        long[] addresses;
        if (oldAddress == null || oldAddress.length == 0) {
            return new long[0];
        }
        int argStart = 0;
        if (!this.isStatic) {
            argStart = 1;
            addresses = new long[oldAddress.length + 1];
            addresses[0] = oldAddress[0];
        } else {
            addresses = new long[oldAddress.length];
        }
        for (int i = 0; i < this.parType.length; ++i) {
            addresses[i + argStart] = ParamWrapper.objectToAddress(this.parType[i], args[i]);
        }
        return addresses;
    }

    public Object getThis(long address) {
        if (this.isStatic) {
            return null;
        }
        return SandHook.getObject(address);
    }

    public Object getArg(int index, long address) {
        return ParamWrapper.addressToObject(this.parType[index], address);
    }

    public Object getResult(long address) {
        if (this.isVoid()) {
            return null;
        }
        return ParamWrapper.addressToObject(this.retType, address);
    }

    public long getResultAddress(Object result) {
        if (this.isVoid()) {
            return 0L;
        }
        return ParamWrapper.objectToAddress(this.retType, result);
    }

    public boolean isVoid() {
        return this.retType == null || Void.TYPE.equals(this.retType);
    }

    public boolean isConstructor() {
        return this.origin instanceof Constructor;
    }
}

