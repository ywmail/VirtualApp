/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.app.Application
 *  android.content.Context
 */
package com.lody.virtual.client.core;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

public interface AppCallback {
    public static final AppCallback EMPTY = new AppCallback(){

        @Override
        public void beforeStartApplication(String packageName, String processName, Context context) {
        }

        @Override
        public void beforeApplicationCreate(String packageName, String processName, Application application) {
        }

        @Override
        public void afterApplicationCreate(String packageName, String processName, Application application) {
        }

        @Override
        public void beforeActivityOnCreate(Activity activity) {
        }

        @Override
        public void afterActivityOnCreate(Activity activity) {
        }

        @Override
        public void beforeActivityOnStart(Activity activity) {
        }

        @Override
        public void afterActivityOnStart(Activity activity) {
        }

        @Override
        public void beforeActivityOnResume(Activity activity) {
        }

        @Override
        public void afterActivityOnResume(Activity activity) {
        }

        @Override
        public void beforeActivityOnStop(Activity activity) {
        }

        @Override
        public void afterActivityOnStop(Activity activity) {
        }

        @Override
        public void beforeActivityOnDestroy(Activity activity) {
        }

        @Override
        public void afterActivityOnDestroy(Activity activity) {
        }
    };

    public void beforeStartApplication(String var1, String var2, Context var3);

    public void beforeApplicationCreate(String var1, String var2, Application var3);

    public void afterApplicationCreate(String var1, String var2, Application var3);

    public void beforeActivityOnCreate(Activity var1);

    public void afterActivityOnCreate(Activity var1);

    public void beforeActivityOnStart(Activity var1);

    public void afterActivityOnStart(Activity var1);

    public void beforeActivityOnResume(Activity var1);

    public void afterActivityOnResume(Activity var1);

    public void beforeActivityOnStop(Activity var1);

    public void afterActivityOnStop(Activity var1);

    public void beforeActivityOnDestroy(Activity var1);

    public void afterActivityOnDestroy(Activity var1);
}

