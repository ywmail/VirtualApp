/*
 * Decompiled with CFR 0.152.
 */
package com.swift.sandhook.xposedcompat.methodgen;

import de.robv.android.xposed.XposedBridge;
import java.lang.reflect.Member;
import java.lang.reflect.Method;

public interface HookMaker {
    public void start(Member var1, XposedBridge.AdditionalHookInfo var2, ClassLoader var3, String var4) throws Exception;

    public Method getHookMethod();

    public Method getBackupMethod();

    public Method getCallBackupMethod();
}

