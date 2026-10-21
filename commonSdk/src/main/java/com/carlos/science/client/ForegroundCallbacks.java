/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.app.Application
 *  android.app.Application$ActivityLifecycleCallbacks
 *  android.content.Context
 *  android.os.Bundle
 *  android.os.Handler
 *  android.os.Message
 */
package com.carlos.science.client;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import com.carlos.libcommon.StringFog;
import com.kook.common.utils.HVLog;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ForegroundCallbacks
implements Application.ActivityLifecycleCallbacks {
    public static final long CHECK_DELAY = 500L;
    public static final String TAG = "ForegroundCallbacks";
    private static ForegroundCallbacks instance;
    private boolean foreground = false;
    private boolean paused = true;
    private static final int HANDLER_WHAT_FOREGROUND = 1;
    private Handler handler = new Handler(){

        public void dispatchMessage(Message msg) {
            if (ForegroundCallbacks.this.foreground && ForegroundCallbacks.this.paused) {
                ForegroundCallbacks.this.foreground = false;
                HVLog.d("FloatWindowServices", "went background");
                for (Listener listener : ForegroundCallbacks.this.listeners) {
                    try {
                        listener.onBecameBackground();
                    }
                    catch (Exception exc) {
                        HVLog.d("ForegroundCallbacks", "Listener threw exception!:" + exc.toString());
                    }
                }
            } else {
                HVLog.d("ForegroundCallbacks", "still foreground");
            }
        }
    };
    private List<Listener> listeners = new CopyOnWriteArrayList<Listener>();
    private Runnable check;

    private static ForegroundCallbacks init(Application application) {
        if (instance == null) {
            instance = new ForegroundCallbacks();
            application.registerActivityLifecycleCallbacks((Application.ActivityLifecycleCallbacks)instance);
        }
        return instance;
    }

    public static ForegroundCallbacks get(Application application) {
        if (instance == null) {
            ForegroundCallbacks.init(application);
        }
        return instance;
    }

    public static ForegroundCallbacks get(Context ctx) {
        if (instance == null) {
            Context appCtx = ctx.getApplicationContext();
            if (appCtx instanceof Application) {
                ForegroundCallbacks.init((Application)appCtx);
            }
            throw new IllegalStateException("Foreground is not initialised and cannot obtain the Application object");
        }
        return instance;
    }

    public static ForegroundCallbacks get() {
        if (instance == null) {
            throw new IllegalStateException("Foreground is not initialised - invoke at least once with parameterised init/get");
        }
        return instance;
    }

    public boolean isForeground() {
        return this.foreground;
    }

    public boolean isBackground() {
        return !this.foreground;
    }

    public void addListener(Listener listener) {
        this.listeners.add(listener);
    }

    public void removeListener(Listener listener) {
        this.listeners.remove(listener);
    }

    public void onActivityResumed(Activity activity) {
        this.paused = false;
        boolean wasBackground = !this.foreground;
        this.foreground = true;
        HVLog.d(TAG, "onActivityResumed check:" + this.check);
        this.handler.removeMessages(1);
        if (wasBackground) {
            HVLog.d(TAG, "went foreground");
            for (Listener listener : this.listeners) {
                try {
                    listener.onBecameForeground(activity);
                }
                catch (Exception exc) {
                    HVLog.d(TAG, "Listener threw exception!:" + exc.toString());
                }
            }
        } else {
            HVLog.d(TAG, "still foreground");
        }
    }

    public void onActivityPaused(Activity activity) {
        this.paused = true;
        HVLog.d(TAG, "onActivityPaused check:" + this.check);
        this.handler.removeMessages(1);
        Message message = this.handler.obtainMessage(1);
        this.handler.sendMessageDelayed(message, 500L);
    }

    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
    }

    public void onActivityStarted(Activity activity) {
    }

    public void onActivityStopped(Activity activity) {
    }

    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    public void onActivityDestroyed(Activity activity) {
    }

    public static interface Listener {
        public void onBecameForeground(Activity var1);

        public void onBecameBackground();
    }
}

