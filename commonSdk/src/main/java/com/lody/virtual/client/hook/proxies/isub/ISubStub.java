/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Build$VERSION
 */
package com.lody.virtual.client.hook.proxies.isub;

import android.os.Build;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.ReplaceCallingPkgMethodProxy;
import com.lody.virtual.client.hook.base.ReplaceLastPkgMethodProxy;
import com.lody.virtual.client.hook.base.ResultStaticMethodProxy;
import com.lody.virtual.client.hook.base.StaticMethodProxy;
import mirror.com.android.internal.telephony.ISub;

public class ISubStub
extends BinderInvocationProxy {
    public ISubStub() {
        super(ISub.Stub.asInterface, "isub");
    }

    @Override
    protected void onBindMethods() {
        super.onBindMethods();
        this.addMethodProxy(new ReplaceLastPkgMethodProxy("getActiveSubInfoCount"));
        this.addMethodProxy(new ReplaceLastPkgMethodProxy("getSubscriptionProperty"));
        this.addMethodProxy(new StaticMethodProxy(Build.VERSION.SDK_INT >= 24 ? "getSimStateForSlotIdx" : "getSimStateForSubscriber"));
        this.addMethodProxy(new ReplaceLastPkgMethodProxy("getActiveSubscriptionInfo"));
        this.addMethodProxy(new ReplaceLastPkgMethodProxy("getActiveSubscriptionInfoForIccId"));
        this.addMethodProxy(new ReplaceLastPkgMethodProxy("getActiveSubscriptionInfoForSimSlotIndex"));
        this.addMethodProxy(new ReplaceCallingPkgMethodProxy("getAllSubInfoList"));
        this.addMethodProxy(new ReplaceCallingPkgMethodProxy("getAllSubInfoCount"));
        this.addMethodProxy(new ReplaceCallingPkgMethodProxy("getActiveSubscriptionInfoList"));
        this.addMethodProxy(new ReplaceCallingPkgMethodProxy("getAvailableSubscriptionInfoList"));
        this.addMethodProxy(new ReplaceCallingPkgMethodProxy("getAccessibleSubscriptionInfoList"));
        this.addMethodProxy(new ReplaceLastPkgMethodProxy("isActiveSubId"));
        this.addMethodProxy(new ReplaceLastPkgMethodProxy("getOpportunisticSubscriptions"));
        this.addMethodProxy(new ReplaceLastPkgMethodProxy("createSubscriptionGroup"));
        this.addMethodProxy(new ReplaceLastPkgMethodProxy("removeSubscriptionsFromGroup"));
        this.addMethodProxy(new ResultStaticMethodProxy("getActiveSubIdList", new int[0]));
    }
}

