/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.annotation.TargetApi
 *  android.content.Context
 */
package com.lody.virtual.client.hook.proxies.telecom;

import android.annotation.TargetApi;
import android.content.Context;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.ReplaceCallingPkgMethodProxy;
import com.lody.virtual.client.hook.base.StaticMethodProxy;
import java.lang.reflect.Method;
import mirror.com.android.internal.telecom.ITelecomService;

@TargetApi(value=21)
public class TelecomManagerStub
extends BinderInvocationProxy {
    public TelecomManagerStub() {
        super(ITelecomService.Stub.TYPE, "telecom");
    }

    @Override
    protected void onBindMethods() {
        super.onBindMethods();
        this.addMethodProxy(new StaticMethodProxy("registerPhoneAccount"){

            @Override
            public Object call(Object who, Method method, Object ... args) throws Throwable {
                return 0;
            }
        });
        this.addMethodProxy(new ReplaceCallingPkgMethodProxy("showInCallScreen"));
        this.addMethodProxy(new ReplaceCallingPkgMethodProxy("getDefaultOutgoingPhoneAccount"));
        this.addMethodProxy(new ReplaceCallingPkgMethodProxy("getUserSelectedOutgoingPhoneAccount"));
        this.addMethodProxy(new ReplaceCallingPkgMethodProxy("getCallCapablePhoneAccounts"));
        this.addMethodProxy(new ReplaceCallingPkgMethodProxy("getSelfManagedPhoneAccounts"));
        this.addMethodProxy(new ReplaceCallingPkgMethodProxy("getOwnSelfManagedPhoneAccounts"));
        this.addMethodProxy(new ReplaceCallingPkgMethodProxy("getPhoneAccountsSupportingScheme"));
        this.addMethodProxy(new ReplaceCallingPkgMethodProxy("getPhoneAccountsForPackage"));
        this.addMethodProxy(new ReplaceCallingPkgMethodProxy("getPhoneAccount"));
        this.addMethodProxy(new ReplaceCallingPkgMethodProxy("clearAccounts"));
        this.addMethodProxy(new ReplaceCallingPkgMethodProxy("isVoiceMailNumber"));
        this.addMethodProxy(new ReplaceCallingPkgMethodProxy("getVoiceMailNumber"));
        this.addMethodProxy(new ReplaceCallingPkgMethodProxy("getLine1Number"));
        this.addMethodProxy(new ReplaceCallingPkgMethodProxy("silenceRinger"));
        this.addMethodProxy(new ReplaceCallingPkgMethodProxy("isInCall"));
        this.addMethodProxy(new ReplaceCallingPkgMethodProxy("isInManagedCall"));
        this.addMethodProxy(new ReplaceCallingPkgMethodProxy("isRinging"));
        this.addMethodProxy(new ReplaceCallingPkgMethodProxy("acceptRingingCall"));
        this.addMethodProxy(new ReplaceCallingPkgMethodProxy("acceptRingingCallWithVideoState("));
        this.addMethodProxy(new ReplaceCallingPkgMethodProxy("cancelMissedCallsNotification"));
        this.addMethodProxy(new ReplaceCallingPkgMethodProxy("handlePinMmi"));
        this.addMethodProxy(new ReplaceCallingPkgMethodProxy("handlePinMmiForPhoneAccount"));
        this.addMethodProxy(new ReplaceCallingPkgMethodProxy("getAdnUriForPhoneAccount"));
        this.addMethodProxy(new ReplaceCallingPkgMethodProxy("isTtySupported"));
        this.addMethodProxy(new ReplaceCallingPkgMethodProxy("getCurrentTtyMode"));
        this.addMethodProxy(new ReplaceCallingPkgMethodProxy("placeCall"));
        this.addMethodProxy(new GetCallStateUsingPackage());
        this.addMethodProxy(new ReplaceCallingPkgMethodProxy("endCall"));
        this.addMethodProxy(new ReplaceCallingPkgMethodProxy("startConference"));
        this.addMethodProxy(new ReplaceCallingPkgMethodProxy("setDefaultDialer"));
        this.addMethodProxy(new ReplaceCallingPkgMethodProxy("isIncomingCallPermitted"));
        this.addMethodProxy(new ReplaceCallingPkgMethodProxy("isOutgoingCallPermitted"));
        this.addMethodProxy(new ReplaceCallingPkgMethodProxy("isInSelfManagedCall"));
    }

    class GetCallStateUsingPackage
    extends ReplaceCallingPkgMethodProxy {
        public GetCallStateUsingPackage() {
            super("getCallStateUsingPackage");
        }

        @Override
        public Object call(Object obj, Method method, Object ... args) throws Throwable {
            Context context = VirtualCore.get().getContext();
            if (context != null && context.checkCallingPermission("android.permission.READ_PHONE_STATE") == 0) {
                return 0;
            }
            return super.call(obj, method, args);
        }
    }
}

