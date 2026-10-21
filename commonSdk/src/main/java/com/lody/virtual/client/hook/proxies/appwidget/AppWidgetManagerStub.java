/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.annotation.TargetApi
 */
package com.lody.virtual.client.hook.proxies.appwidget;

import android.annotation.TargetApi;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.ResultStaticMethodProxy;
import mirror.com.android.internal.appwidget.IAppWidgetService;

@TargetApi(value=21)
public class AppWidgetManagerStub
extends BinderInvocationProxy {
    public AppWidgetManagerStub() {
        super(IAppWidgetService.Stub.asInterface, "appwidget");
    }

    @Override
    protected void onBindMethods() {
        super.onBindMethods();
        this.addMethodProxy(new ResultStaticMethodProxy("startListening", new int[0]));
        this.addMethodProxy(new ResultStaticMethodProxy("stopListening", 0));
        this.addMethodProxy(new ResultStaticMethodProxy("allocateAppWidgetId", 0));
        this.addMethodProxy(new ResultStaticMethodProxy("deleteAppWidgetId", 0));
        this.addMethodProxy(new ResultStaticMethodProxy("deleteHost", 0));
        this.addMethodProxy(new ResultStaticMethodProxy("deleteAllHosts", 0));
        this.addMethodProxy(new ResultStaticMethodProxy("getAppWidgetViews", null));
        this.addMethodProxy(new ResultStaticMethodProxy("getAppWidgetIdsForHost", null));
        this.addMethodProxy(new ResultStaticMethodProxy("createAppWidgetConfigIntentSender", null));
        this.addMethodProxy(new ResultStaticMethodProxy("updateAppWidgetIds", 0));
        this.addMethodProxy(new ResultStaticMethodProxy("updateAppWidgetOptions", 0));
        this.addMethodProxy(new ResultStaticMethodProxy("getAppWidgetOptions", null));
        this.addMethodProxy(new ResultStaticMethodProxy("partiallyUpdateAppWidgetIds", 0));
        this.addMethodProxy(new ResultStaticMethodProxy("updateAppWidgetProvider", 0));
        this.addMethodProxy(new ResultStaticMethodProxy("notifyAppWidgetViewDataChanged", 0));
        this.addMethodProxy(new ResultStaticMethodProxy("getInstalledProvidersForProfile", null));
        this.addMethodProxy(new ResultStaticMethodProxy("getAppWidgetInfo", null));
        this.addMethodProxy(new ResultStaticMethodProxy("hasBindAppWidgetPermission", false));
        this.addMethodProxy(new ResultStaticMethodProxy("setBindAppWidgetPermission", 0));
        this.addMethodProxy(new ResultStaticMethodProxy("bindAppWidgetId", false));
        this.addMethodProxy(new ResultStaticMethodProxy("bindRemoteViewsService", 0));
        this.addMethodProxy(new ResultStaticMethodProxy("unbindRemoteViewsService", 0));
        this.addMethodProxy(new ResultStaticMethodProxy("getAppWidgetIds", new int[0]));
        this.addMethodProxy(new ResultStaticMethodProxy("isBoundWidgetPackage", false));
    }
}

