/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.app.Application
 *  android.os.Handler
 */
package com.carlos.science.client;

import android.app.Activity;
import android.app.Application;
import android.os.Handler;
import com.carlos.science.IVirtualController;
import com.kook.controller.server.IServerController;

public interface ClientActivityLifecycle {
    public void setIServerController(Application var1, IServerController var2, IVirtualController var3);

    public IVirtualController getVirtualControllerImpl();

    public IServerController getIServerController();

    public Activity getCurrentActivity();

    public Handler getHandler();

    public void onActivityResumed(Activity var1);

    public void onActivityPaused(Activity var1);

    public void runOnUiThread(Runnable var1);
}

