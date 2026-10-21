/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 */
package com.lody.virtual.client.hook.base;

import android.content.Context;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.hook.annotations.Inject;
import com.lody.virtual.client.hook.annotations.LogInvocation;
import com.lody.virtual.client.hook.annotations.SkipInject;
import com.lody.virtual.client.hook.base.MethodInvocationStub;
import com.lody.virtual.client.hook.base.MethodProxy;
import com.lody.virtual.client.hook.base.StaticMethodProxy;
import com.lody.virtual.client.interfaces.IInjector;
import java.lang.reflect.Constructor;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public abstract class MethodInvocationProxy<T extends MethodInvocationStub>
implements IInjector {
    protected T mInvocationStub;

    public MethodInvocationProxy(T invocationStub) {
        this.mInvocationStub = invocationStub;
        this.onBindMethods();
        this.afterHookApply(invocationStub);
        LogInvocation loggingAnnotation = this.getClass().getAnnotation(LogInvocation.class);
        if (loggingAnnotation != null) {
            ((MethodInvocationStub)invocationStub).setInvocationLoggingCondition(loggingAnnotation.value());
        }
    }

    protected void onBindMethods() {
        if (this.mInvocationStub == null) {
            return;
        }
        Class<?> clazz = this.getClass();
        Inject inject = clazz.getAnnotation(Inject.class);
        if (inject != null) {
            Class<?> proxiesClass = inject.value();
            Class<?>[] innerClasses = proxiesClass.getDeclaredClasses();
            for (Class<?> clazz2 : innerClasses) {
                if (Modifier.isAbstract(clazz2.getModifiers()) || !MethodProxy.class.isAssignableFrom(clazz2) || clazz2.getAnnotation(SkipInject.class) != null) continue;
                this.addMethodProxy(clazz2);
            }
            for (GenericDeclaration genericDeclaration : proxiesClass.getMethods()) {
                if (!Modifier.isStatic(((Method)genericDeclaration).getModifiers()) || ((Method)genericDeclaration).getAnnotation(SkipInject.class) != null) continue;
                this.addMethodProxy(new DirectCallMethodProxy((Method)genericDeclaration));
            }
        }
    }

    private void addMethodProxy(Class<?> hookType) {
        try {
            Constructor<?> constructor = hookType.getDeclaredConstructors()[0];
            if (!constructor.isAccessible()) {
                constructor.setAccessible(true);
            }
            MethodProxy methodProxy = constructor.getParameterTypes().length == 0 ? (MethodProxy)constructor.newInstance(new Object[0]) : (MethodProxy)constructor.newInstance(this);
            ((MethodInvocationStub)this.mInvocationStub).addMethodProxy(methodProxy);
        }
        catch (Throwable e) {
            throw new RuntimeException("Unable to instance Hook : " + hookType + " : " + e.getMessage());
        }
    }

    public MethodProxy addMethodProxy(MethodProxy methodProxy) {
        return ((MethodInvocationStub)this.mInvocationStub).addMethodProxy(methodProxy);
    }

    public void setDefaultMethodProxy(MethodProxy methodProxy) {
        ((MethodInvocationStub)this.mInvocationStub).setDefaultMethodProxy(methodProxy);
    }

    protected void afterHookApply(T delegate) {
    }

    @Override
    public abstract void inject() throws Throwable;

    public Context getContext() {
        return VirtualCore.get().getContext();
    }

    public T getInvocationStub() {
        return this.mInvocationStub;
    }

    private static final class DirectCallMethodProxy
    extends StaticMethodProxy {
        private Method directCallMethod;

        public DirectCallMethodProxy(Method method) {
            super(method.getName());
            this.directCallMethod = method;
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            return this.directCallMethod.invoke(null, who, method, args);
        }
    }
}

