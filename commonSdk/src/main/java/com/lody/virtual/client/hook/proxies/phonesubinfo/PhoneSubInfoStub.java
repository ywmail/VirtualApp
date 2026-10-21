/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 */
package com.lody.virtual.client.hook.proxies.phonesubinfo;

import android.content.Context;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.hook.annotations.Inject;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.ReplaceCallingPkgMethodProxy;
import com.lody.virtual.client.hook.base.ReplaceLastPkgMethodProxy;
import com.lody.virtual.client.hook.proxies.phonesubinfo.MethodProxies;
import com.lody.virtual.helper.compat.BuildCompat;
import java.lang.reflect.Method;
import mirror.com.android.internal.telephony.IPhoneSubInfo;

@Inject(value=MethodProxies.class)
public class PhoneSubInfoStub
extends BinderInvocationProxy {
    public PhoneSubInfoStub() {
        super(IPhoneSubInfo.Stub.asInterface, "iphonesubinfo");
    }

    @Override
    protected void onBindMethods() {
        super.onBindMethods();
        this.addMethodProxy(new ReplaceLastPkgMethodProxy("getNaiForSubscriber"));
        this.addMethodProxy(new ReplaceCallingPkgMethodProxy("getDeviceSvn"));
        this.addMethodProxy(new ReplaceLastPkgMethodProxy("getDeviceSvnUsingSubId"));
        this.addMethodProxy(new GetSubscriberId());
        this.addMethodProxy(new GetSubscriberIdForSubscriber());
        this.addMethodProxy(new ReplaceCallingPkgMethodProxy("getGroupIdLevel1"));
        this.addMethodProxy(new ReplaceLastPkgMethodProxy("getGroupIdLevel1ForSubscriber"));
        this.addMethodProxy(new ReplaceCallingPkgMethodProxy("getLine1AlphaTag"));
        this.addMethodProxy(new ReplaceLastPkgMethodProxy("getLine1AlphaTagForSubscriber"));
        this.addMethodProxy(new ReplaceCallingPkgMethodProxy("getMsisdn"));
        this.addMethodProxy(new ReplaceLastPkgMethodProxy("getMsisdnForSubscriber"));
        this.addMethodProxy(new ReplaceCallingPkgMethodProxy("getVoiceMailNumber"));
        this.addMethodProxy(new ReplaceLastPkgMethodProxy("getVoiceMailNumberForSubscriber"));
        this.addMethodProxy(new ReplaceCallingPkgMethodProxy("getVoiceMailAlphaTag"));
        this.addMethodProxy(new ReplaceLastPkgMethodProxy("getVoiceMailAlphaTagForSubscriber"));
        this.addMethodProxy(new ReplaceCallingPkgMethodProxy("getLine1Number"));
        this.addMethodProxy(new GetLine1NumberForSubscriber());
    }

    class GetLine1NumberForSubscriber
    extends ReplaceCallingPkgMethodProxy {
        public GetLine1NumberForSubscriber() {
            super("getLine1NumberForSubscriber");
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            try {
                Context context = VirtualCore.get().getContext();
                if (context != null) {
                    int p1 = context.checkCallingPermission("android.permission.READ_PHONE_NUMBERS");
                    int p2 = context.checkCallingPermission("android.permission.READ_SMS");
                    int p3 = context.checkCallingPermission("android.permission.READ_PHONE_STATE");
                    if (p1 == -1 && p2 == -1 && p3 == -1) {
                        return null;
                    }
                }
                return super.call(who, method, args);
            }
            catch (Throwable th) {
                th.printStackTrace();
                return null;
            }
        }
    }

    class GetSubscriberIdForSubscriber
    extends ReplaceCallingPkgMethodProxy {
        public GetSubscriberIdForSubscriber() {
            super("getSubscriberIdForSubscriber");
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            try {
                if (BuildCompat.isQ()) {
                    return "unknown";
                }
                return super.call(who, method, args);
            }
            catch (Throwable th) {
                return "unknown";
            }
        }
    }

    class GetSubscriberId
    extends ReplaceCallingPkgMethodProxy {
        public GetSubscriberId() {
            super("getSubscriberId");
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            try {
                if (BuildCompat.isQ()) {
                    return "unknown";
                }
                return super.call(who, method, args);
            }
            catch (Throwable th) {
                return "unknown";
            }
        }
    }
}

