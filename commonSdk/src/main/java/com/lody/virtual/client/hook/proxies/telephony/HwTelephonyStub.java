/*
 * Decompiled with CFR 0.152.
 */
package com.lody.virtual.client.hook.proxies.telephony;

import com.lody.virtual.StringFog;
import com.lody.virtual.client.hook.annotations.Inject;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.proxies.telephony.MethodProxies;
import mirror.com.android.internal.telephony.IHwTelephony;

@Inject(value=MethodProxies.class)
public class HwTelephonyStub
extends BinderInvocationProxy {
    public HwTelephonyStub() {
        super(IHwTelephony.Stub.TYPE, "phone_huawei");
    }

    @Override
    protected void onBindMethods() {
        this.addMethodProxy(new GetUniqueDeviceId());
    }

    private static class GetUniqueDeviceId
    extends MethodProxies.GetDeviceId {
        private GetUniqueDeviceId() {
        }

        @Override
        public String getMethodName() {
            return "getUniqueDeviceId";
        }
    }
}

