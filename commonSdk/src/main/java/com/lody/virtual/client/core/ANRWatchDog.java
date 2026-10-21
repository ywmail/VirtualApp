/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.annotation.SuppressLint
 *  android.os.Handler
 *  android.os.Looper
 *  android.os.Message
 */
package com.lody.virtual.client.core;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.env.VirtualRuntime;

public class ANRWatchDog
extends Thread {
    private static final int MESSAGE_WATCHDOG_TIME_TICK = 0;
    private static final int ANR_TIMEOUT = 5000;
    private static int lastTimeTick = -1;
    private static int timeTick = 0;
    private boolean makeCrash;
    @SuppressLint(value={"HandlerLeak"})
    private final Handler watchDogHandler = new Handler(){

        public void handleMessage(Message msg) {
            timeTick++;
            timeTick = timeTick % Integer.MAX_VALUE;
        }
    };

    public ANRWatchDog(boolean makeCrash) {
        this.makeCrash = makeCrash;
    }

    public ANRWatchDog() {
        this(false);
    }

    @Override
    public void run() {
        while (true) {
            this.watchDogHandler.sendEmptyMessage(0);
            try {
                Thread.sleep(5000L);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (timeTick == lastTimeTick) {
                this.triggerANR();
                continue;
            }
            lastTimeTick = timeTick;
        }
    }

    private void triggerANR() {
        if (this.makeCrash) {
            throw new ANRException();
        }
        try {
            throw new ANRException();
        }
        catch (ANRException e) {
            e.printStackTrace();
            return;
        }
    }

    public static class ANRException
    extends RuntimeException {
        public ANRException() {
            super("========= ANR =========" + ANRException.getAnrDesc());
            Thread mainThread = Looper.getMainLooper().getThread();
            this.setStackTrace(mainThread.getStackTrace());
        }

        private static String getAnrDesc() {
            return VirtualCore.get().isVAppProcess() ? VirtualRuntime.getProcessName() : VirtualCore.get().getProcessName();
        }
    }
}

