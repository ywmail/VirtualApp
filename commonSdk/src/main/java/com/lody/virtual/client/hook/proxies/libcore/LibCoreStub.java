/*
 * Decompiled with CFR 0.152.
 */
package com.lody.virtual.client.hook.proxies.libcore;

import com.lody.virtual.StringFog;
import com.lody.virtual.client.hook.annotations.Inject;
import com.lody.virtual.client.hook.base.MethodInvocationProxy;
import com.lody.virtual.client.hook.base.MethodInvocationStub;
import com.lody.virtual.client.hook.base.StaticMethodProxy;
import com.lody.virtual.client.hook.proxies.libcore.MethodProxies;
import com.lody.virtual.helper.utils.VLog;
import java.lang.reflect.Method;
import mirror.libcore.io.ForwardingOs;
import mirror.libcore.io.Libcore;

@Inject(value=MethodProxies.class)
public class LibCoreStub
extends MethodInvocationProxy<MethodInvocationStub<Object>> {
    public LibCoreStub() {
        super(new MethodInvocationStub<Object>(LibCoreStub.getOs()));
    }

    private static Object getOs() {
        Object posix;
        Object os = Libcore.os.get();
        if (ForwardingOs.os != null && (posix = ForwardingOs.os.get(os)) != null) {
            os = posix;
        }
        return os;
    }

    @Override
    protected void onBindMethods() {
        super.onBindMethods();
        this.addMethodProxy(new ReplaceUidMethodProxy("chown", 1));
        this.addMethodProxy(new ReplaceUidMethodProxy("fchown", 1));
        this.addMethodProxy(new ReplaceUidMethodProxy("getpwuid", 0));
        this.addMethodProxy(new ReplaceUidMethodProxy("lchown", 1));
        this.addMethodProxy(new ReplaceUidMethodProxy("setuid", 0));
    }

    @Override
    public void inject() {
        Libcore.os.set(((MethodInvocationStub)this.getInvocationStub()).getProxyInterface());
    }

    @Override
    public boolean isEnvBad() {
        return LibCoreStub.getOs() != ((MethodInvocationStub)this.getInvocationStub()).getProxyInterface();
    }

    public class ReplaceUidMethodProxy
    extends StaticMethodProxy {
        private final int index;

        public ReplaceUidMethodProxy(String name, int index) {
            super(name);
            this.index = index;
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            return super.call(who, method, args);
        }

        @Override
        public boolean beforeCall(Object who, Method method, Object ... args) {
            VLog.d("HV-", " ReplaceUidMethodProxy method:  " + method.getName(), new Object[0]);
            int uid = (Integer)args[this.index];
            if (uid == ReplaceUidMethodProxy.getVUid() || uid == ReplaceUidMethodProxy.getBaseVUid()) {
                args[this.index] = ReplaceUidMethodProxy.getRealUid();
            }
            return super.beforeCall(who, method, args);
        }
    }
}

