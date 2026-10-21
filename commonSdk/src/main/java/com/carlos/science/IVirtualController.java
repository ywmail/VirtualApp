/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.app.Application
 */
package com.carlos.science;

import android.app.Activity;
import android.app.Application;

public interface IVirtualController {
    public static final IVirtualController EMPTY = new IVirtualController(){

        @Override
        public IController getController() {
            return null;
        }

        @Override
        public void onCreateController(Application application, String hostPkg) {
        }

        @Override
        public void controllerActivityCreate(Activity activity) {
        }

        @Override
        public void controllerActivityResume(Activity activity) {
        }

        @Override
        public void controllerActivityDestroy(Activity activity) {
        }

        @Override
        public void controllerActivityPause(Activity activity) {
        }
    };

    public IController getController();

    public void onCreateController(Application var1, String var2);

    public void controllerActivityCreate(Activity var1);

    public void controllerActivityResume(Activity var1);

    public void controllerActivityDestroy(Activity var1);

    public void controllerActivityPause(Activity var1);

    public static interface IController {
        public boolean needShow();
    }
}

