/*
 * Decompiled with CFR 0.152.
 */
package de.robv.android.xposed;

import de.robv.android.xposed.callbacks.IXUnhook;
import de.robv.android.xposed.callbacks.XCallback;
import java.lang.reflect.Member;

public abstract class XC_MethodHook extends XCallback {
    public XC_MethodHook() {
    }

    public XC_MethodHook(int priority) {
        super(priority);
    }

    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
    }

    public void callBeforeHookedMethod(MethodHookParam param) throws Throwable {
        this.beforeHookedMethod(param);
    }

    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
    }

    public void callAfterHookedMethod(MethodHookParam param) throws Throwable {
        this.afterHookedMethod(param);
    }

    public static class Unhook implements IXUnhook<XC_MethodHook> {
        private final Member hookMethod;
        private final XC_MethodHook mCallback;

        Unhook(XC_MethodHook callback, Member hookMethod) {
            this.hookMethod = hookMethod;
            this.mCallback = callback;
        }

        public Member getHookedMethod() {
            return this.hookMethod;
        }

        @Override
        public XC_MethodHook getCallback() {
            return mCallback;
        }

        @Override
        public void unhook() {
            XposedBridge.unhookMethod(this.hookMethod, mCallback);
        }
    }

    public static final class MethodHookParam extends XCallback.Param {
        public Member method;
        public Object thisObject;
        public Object[] args;
        private Object result = null;
        private Throwable throwable = null;
        public boolean returnEarly = false;

        public Object getResult() {
            return this.result;
        }

        public void setResult(Object result) {
            this.result = result;
            this.throwable = null;
            this.returnEarly = true;
        }

        public Throwable getThrowable() {
            return this.throwable;
        }

        public boolean hasThrowable() {
            return this.throwable != null;
        }

        public void setThrowable(Throwable throwable) {
            this.throwable = throwable;
            this.result = null;
            this.returnEarly = true;
        }

        public Object getResultOrThrowable() throws Throwable {
            if (this.throwable != null) {
                throw this.throwable;
            }
            return this.result;
        }
    }
}

