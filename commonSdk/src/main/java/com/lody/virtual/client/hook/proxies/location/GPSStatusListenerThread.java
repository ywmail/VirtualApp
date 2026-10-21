/*
 * Decompiled with CFR 0.152.
 */
package com.lody.virtual.client.hook.proxies.location;

import com.lody.virtual.client.hook.proxies.location.MockLocationHelper;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

class GPSStatusListenerThread
extends TimerTask {
    private static GPSStatusListenerThread INSTANCE = new GPSStatusListenerThread();
    private boolean isRunning = false;
    private Map<Object, Long> listeners = new HashMap<Object, Long>();
    private Timer timer = new Timer();

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void addListenerTransport(Object transport) {
        if (!this.isRunning) {
            GPSStatusListenerThread gPSStatusListenerThread = this;
            synchronized (gPSStatusListenerThread) {
                if (!this.isRunning) {
                    this.isRunning = true;
                    this.timer.schedule((TimerTask)this, 100L, 800L);
                }
            }
        }
        this.listeners.put(transport, System.currentTimeMillis());
    }

    public void removeListenerTransport(Object obj) {
        if (obj != null) {
            this.listeners.remove(obj);
        }
    }

    @Override
    public void run() {
        if (!this.listeners.isEmpty()) {
            for (Map.Entry<Object, Long> entry : this.listeners.entrySet()) {
                try {
                    Object transport = entry.getKey();
                    MockLocationHelper.invokeSvStatusChanged(transport);
                    MockLocationHelper.invokeNmeaReceived(transport);
                }
                catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void stop() {
        this.timer.cancel();
    }

    public static GPSStatusListenerThread get() {
        return INSTANCE;
    }

    private GPSStatusListenerThread() {
    }
}

