/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.view.View
 */
package com.carlos.science.runner;

import android.view.View;

public abstract class OnceRunnable
implements Runnable {
    private boolean mScheduled;

    @Override
    public final void run() {
        this.onRun();
        this.mScheduled = false;
    }

    public abstract void onRun();

    public void postSelf(View carrier) {
        this.postDelaySelf(carrier, 0);
    }

    public void postDelaySelf(View carrier, int delay) {
        if (!this.mScheduled) {
            carrier.postDelayed((Runnable)this, (long)delay);
            this.mScheduled = true;
        }
    }

    public boolean isRunning() {
        return this.mScheduled;
    }

    public void removeSelf(View carrier) {
        this.mScheduled = false;
        carrier.removeCallbacks((Runnable)this);
    }
}

