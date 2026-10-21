//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.lody.virtual.client.hook.proxies.location;

import android.location.Location;
import android.os.Build;
import android.os.Handler;
import android.os.Build.VERSION;
import android.util.ArrayMap;
import com.lody.virtual.client.ipc.VirtualLocationManager;
import com.lody.virtual.remote.vloc.VLocation;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import mirror.android.location.LocationManager;
import mirror.android.location.LocationManager.ListenerTransport;

class GPSListenerThread extends TimerTask {
    private static GPSListenerThread INSTANCE = new GPSListenerThread();
    private Handler handler = new Handler();
    private boolean isRunning = false;
    private HashMap<Object, Long> listeners = new HashMap();
    private Timer timer = new Timer();

    private void notifyGPSStatus(Map listeners) {
        if (listeners != null && !listeners.isEmpty()) {
            Set<Map.Entry> entries = listeners.entrySet();
            Iterator var3 = entries.iterator();

            while(var3.hasNext()) {
                Map.Entry entry = (Map.Entry)var3.next();

                try {
                    Object value = entry.getValue();
                    if (value != null) {
                        MockLocationHelper.invokeSvStatusChanged(value);
                    }
                } catch (Throwable var6) {
                    var6.printStackTrace();
                }
            }
        }

    }

    private void notifyLocation(Map listeners) {
        if (listeners != null) {
            try {
                if (!listeners.isEmpty()) {
                    VLocation vLocation = VirtualLocationManager.get().getLocation();
                    if (vLocation != null) {
                        Location location = vLocation.toSysLocation();
                        Set<Map.Entry> entries = listeners.entrySet();
                        Iterator var5 = entries.iterator();

                        while(var5.hasNext()) {
                            Map.Entry entry = (Map.Entry)var5.next();
                            Object value = entry.getValue();
                            if (value != null) {
                                try {
                                    ListenerTransport.onLocationChanged.call(value, new Object[]{location});
                                } catch (Throwable var9) {
                                    var9.printStackTrace();
                                }
                            }
                        }
                    }
                }
            } catch (Throwable var10) {
                var10.printStackTrace();
            }
        }

    }

    private void notifyMNmeaListener(Map listeners) {
        if (listeners != null && !listeners.isEmpty()) {
            Set<Map.Entry> entries = listeners.entrySet();
            Iterator var3 = entries.iterator();

            while(var3.hasNext()) {
                Map.Entry entry = (Map.Entry)var3.next();

                try {
                    Object value = entry.getValue();
                    if (value != null) {
                        MockLocationHelper.invokeNmeaReceived(value);
                    }
                } catch (Exception var6) {
                    var6.printStackTrace();
                }
            }
        }

    }

    public void addListenerTransport(Object transport) {
        this.listeners.put(transport, System.currentTimeMillis());
        if (!this.isRunning) {
            synchronized(this) {
                if (!this.isRunning) {
                    this.isRunning = true;
                    this.timer.schedule(this, 1000L, 1000L);
                }
            }
        }

    }

    public void removeListenerTransport(Object transport) {
        if (transport != null) {
            this.listeners.remove(transport);
        }

    }

    @Override
    public void run() {
        if (!this.listeners.isEmpty()) {
            if (VirtualLocationManager.get().getMode() == 0) {
                this.listeners.clear();
                return;
            }
            for (Map.Entry<Object, Long> entry : this.listeners.entrySet()) {
                try {
                    Map gpsStatusListeners;
                    Object transport = entry.getKey();
                    if (Build.VERSION.SDK_INT >= 24) {
                        Map nmeaListeners = LocationManager.mGnssNmeaListeners.get(transport);
                        this.notifyGPSStatus(LocationManager.mGnssStatusListeners.get(transport));
                        this.notifyMNmeaListener(nmeaListeners);
                        gpsStatusListeners = LocationManager.mGpsStatusListeners.get(transport);
                        if (gpsStatusListeners instanceof HashMap) {
                            this.notifyGPSStatus((HashMap)gpsStatusListeners);
                        } else if (gpsStatusListeners instanceof ArrayMap) {
                            this.notifyGPSStatus((Map)((ArrayMap)gpsStatusListeners));
                        }
                        this.notifyMNmeaListener(LocationManager.mGpsNmeaListeners.get(transport));
                    } else {
                        gpsStatusListeners = LocationManager.mGpsStatusListeners.get(transport);
                        if (gpsStatusListeners instanceof HashMap) {
                            this.notifyGPSStatus((HashMap)gpsStatusListeners);
                        } else if (gpsStatusListeners instanceof ArrayMap) {
                            this.notifyGPSStatus((Map)((ArrayMap)gpsStatusListeners));
                        }
                        this.notifyMNmeaListener(LocationManager.mNmeaListeners.get(transport));
                    }
                    final Map listeners = LocationManager.mListeners.get(transport);
                    if (gpsStatusListeners == null || gpsStatusListeners.isEmpty()) continue;
                    if (listeners == null || listeners.isEmpty()) {
                        this.handler.postDelayed(new Runnable(){

                            @Override
                            public void run() {
                                GPSListenerThread.this.notifyLocation(listeners);
                            }
                        }, 100L);
                        continue;
                    }
                    this.notifyLocation(listeners);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void stop() {
        this.timer.cancel();
    }

    public static GPSListenerThread get() {
        return INSTANCE;
    }

    private GPSListenerThread() {
    }
}
