/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.annotation.TargetApi
 *  android.os.IInterface
 */
package com.lody.virtual.client.hook.proxies.media.session;

import android.annotation.TargetApi;
import android.os.IInterface;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.MethodProxy;
import com.lody.virtual.client.hook.base.ReplaceCallingPkgMethodProxy;
import com.lody.virtual.client.hook.utils.MethodParameterUtils;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import mirror.android.media.session.ISessionManager;

@TargetApi(value=21)
public class SessionManagerStub
extends BinderInvocationProxy {
    public SessionManagerStub() {
        super(ISessionManager.Stub.asInterface, "media_session");
    }

    @Override
    protected void onBindMethods() {
        super.onBindMethods();
        this.addMethodProxy(new ReplaceCallingPkgMethodProxy("createSession"){

            @Override
            public Object call(Object who, Method method, Object ... args) throws Throwable {
                replaceLastUserId(args);
                final IInterface ISession = (IInterface)super.call(who, method, args);
                return SessionManagerStub.CreateProxy(ISession, new InvocationHandler(){

                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        if ("getController".equals(method.getName())) {
                            final IInterface controller = (IInterface)method.invoke(ISession, args);
                            return SessionManagerStub.CreateProxy(controller, new InvocationHandler(){

                                @Override
                                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                                    if ("setVolumeTo".equals(method.getName())) {
                                        MethodParameterUtils.replaceFirstAppPkg(args);
                                        return method.invoke(controller, args);
                                    }
                                    if ("adjustVolume".equals(method.getName())) {
                                        MethodParameterUtils.replaceFirstAppPkg(args);
                                        return method.invoke(controller, args);
                                    }
                                    if ("createSession".equals(method.getName()) || "getSessions".equals(method.getName()) || "getSession2Tokens".equals(method.getName()) || "addSessionsListener".equals(method.getName()) || "addSession2TokensListener".equals(method.getName())) {
                                        MethodProxy.replaceLastUserId(args);
                                    }
                                    return method.invoke(controller, args);
                                }
                            });
                        }
                        return method.invoke(ISession, args);
                    }
                });
            }
        });
    }

    private static Object CreateProxy(IInterface iInterface, InvocationHandler proxy) {
        return Proxy.newProxyInstance(iInterface.getClass().getClassLoader(), iInterface.getClass().getInterfaces(), proxy);
    }
}

