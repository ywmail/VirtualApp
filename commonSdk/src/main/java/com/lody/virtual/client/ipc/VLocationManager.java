/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.PendingIntent
 *  android.location.Location
 *  android.location.LocationManager
 *  android.os.Build$VERSION
 *  android.os.Handler
 *  android.os.HandlerThread
 *  android.util.Log
 */
package com.lody.virtual.client.ipc;

import android.app.PendingIntent;
import android.location.Location;

import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.VClient;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.hook.proxies.location.MockLocationHelper;
import com.lody.virtual.client.hook.utils.MethodParameterUtils;
import com.lody.virtual.client.ipc.VirtualLocationManager;
import com.lody.virtual.helper.utils.Reflect;
import com.lody.virtual.os.VUserHandle;
import com.lody.virtual.remote.vloc.VLocation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mirror.android.location.LocationManager;

public class VLocationManager {
    private Handler mWorkHandler;
    private HandlerThread mHandlerThread;
    private final List<Object> mGpsListeners = new ArrayList<Object>();
    private static VLocationManager sVLocationManager = new VLocationManager();
    private Runnable mUpdateGpsStatusTask = new Runnable(){

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public void run() {
            List list = VLocationManager.this.mGpsListeners;
            synchronized (list) {
                for (Object listener : VLocationManager.this.mGpsListeners) {
                    VLocationManager.this.notifyGpsStatus(listener);
                }
            }
            VLocationManager.this.mWorkHandler.postDelayed(VLocationManager.this.mUpdateGpsStatusTask, 8000L);
        }
    };
    private final Map<Object, UpdateLocationTask> mLocationTaskMap = new HashMap<Object, UpdateLocationTask>();

    private VLocationManager() {
        android.location.LocationManager locationManager = (android.location.LocationManager)VirtualCore.get().getContext().getSystemService("location");
        MockLocationHelper.fakeGpsStatus(locationManager);
    }

    public static VLocationManager get() {
        return sVLocationManager;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void checkWork() {
        VLocationManager vLocationManager;
        if (this.mHandlerThread == null) {
            vLocationManager = this;
            synchronized (vLocationManager) {
                if (this.mHandlerThread == null) {
                    this.mHandlerThread = new HandlerThread("loc_thread");
                    this.mHandlerThread.start();
                }
            }
        }
        if (this.mWorkHandler == null) {
            vLocationManager = this;
            synchronized (vLocationManager) {
                if (this.mWorkHandler == null) {
                    this.mWorkHandler = new Handler(this.mHandlerThread.getLooper());
                }
            }
        }
    }

    private void stopGpsTask() {
        if (this.mWorkHandler != null) {
            this.mWorkHandler.removeCallbacks(this.mUpdateGpsStatusTask);
        }
    }

    private void startGpsTask() {
        this.checkWork();
        this.stopGpsTask();
        this.mWorkHandler.postDelayed(this.mUpdateGpsStatusTask, 5000L);
    }

    public boolean hasVirtualLocation(String packageName, int userId) {
        try {
            return VirtualLocationManager.get().getMode(userId, packageName) != 0;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isProviderEnabled(String provider) {
        return "gps".equals(provider);
    }

    public VLocation getLocation(String packageName, int userId) {
        return this.getVirtualLocation(packageName, null, userId);
    }

    public VLocation getCurAppLocation() {
        return this.getVirtualLocation(VClient.get().getCurrentPackage(), null, VUserHandle.myUserId());
    }

    public VLocation getVirtualLocation(String packageName, Location loc, int userId) {
        try {
            if (VirtualLocationManager.get().getMode(userId, packageName) == 1) {
                return VirtualLocationManager.get().getGlobalLocation();
            }
            return VirtualLocationManager.get().getLocation(userId, packageName);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getPackageName() {
        return VClient.get().getCurrentPackage();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void removeGpsStatusListener(Object[] args) {
        boolean needStop;
        if (args[0] instanceof PendingIntent) {
            return;
        }
        List<Object> list = this.mGpsListeners;
        synchronized (list) {
            this.mGpsListeners.remove(args[0]);
            needStop = this.mGpsListeners.size() == 0;
        }
        if (needStop) {
            this.stopGpsTask();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void addGpsStatusListener(Object[] args) {
        Object GpsStatusListenerTransport2 = args[0];
        MockLocationHelper.invokeSvStatusChanged(GpsStatusListenerTransport2);
        if (GpsStatusListenerTransport2 != null) {
            List<Object> list = this.mGpsListeners;
            synchronized (list) {
                this.mGpsListeners.add(GpsStatusListenerTransport2);
            }
        }
        this.checkWork();
        this.notifyGpsStatus(GpsStatusListenerTransport2);
        this.startGpsTask();
    }

    private void notifyGpsStatus(final Object transport) {
        if (transport == null) {
            return;
        }
        this.mWorkHandler.post(new Runnable(){

            @Override
            public void run() {
                MockLocationHelper.invokeSvStatusChanged(transport);
                MockLocationHelper.invokeNmeaReceived(transport);
            }
        });
    }

    public void removeUpdates(Object[] args) {
        UpdateLocationTask task;
        if (args[0] != null && (task = this.getTask(args[0])) != null) {
            task.stop();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void requestLocationUpdates(Object[] args) {
        int index = Build.VERSION.SDK_INT >= 17 ? 1 : args.length - 1;
        Object listenerTransport = args[index];
        if (listenerTransport == null) {
            Log.e((String)"VLoc", (String)"ListenerTransport:null");
        } else {
            long mInterval;
            if (Build.VERSION.SDK_INT >= 17) {
                try {
                    mInterval = (Long)Reflect.on(args[0]).get("mInterval");
                }
                catch (Throwable e) {
                    mInterval = 60000L;
                }
            } else {
                mInterval = MethodParameterUtils.getFirstParam(args, Long.class);
            }
            VLocation location = this.getCurAppLocation();
            this.checkWork();
            this.notifyLocation(listenerTransport, location.toSysLocation(), true);
            UpdateLocationTask task = this.getTask(listenerTransport);
            if (task == null) {
                Map<Object, UpdateLocationTask> map = this.mLocationTaskMap;
                synchronized (map) {
                    task = new UpdateLocationTask(listenerTransport, mInterval);
                    this.mLocationTaskMap.put(listenerTransport, task);
                }
            }
            task.start();
        }
    }

    private boolean notifyLocation(final Object ListenerTransport2, final Location location, boolean post) {
        if (ListenerTransport2 == null) {
            return false;
        }
        if (!post) {
            try {
                LocationManager.ListenerTransport.onLocationChanged.call(ListenerTransport2, location);
                return true;
            }
            catch (Throwable e) {
                e.printStackTrace();
                return false;
            }
        }
        this.mWorkHandler.post(new Runnable(){

            @Override
            public void run() {
                try {
                    LocationManager.ListenerTransport.onLocationChanged.call(ListenerTransport2, location);
                }
                catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });
        return true;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private UpdateLocationTask getTask(Object locationListener) {
        UpdateLocationTask task;
        Map<Object, UpdateLocationTask> map = this.mLocationTaskMap;
        synchronized (map) {
            task = this.mLocationTaskMap.get(locationListener);
        }
        return task;
    }

    private class UpdateLocationTask
    implements Runnable {
        private Object mListenerTransport;
        private long mTime;
        private volatile boolean mRunning;

        private UpdateLocationTask(Object ListenerTransport2, long time) {
            this.mListenerTransport = ListenerTransport2;
            this.mTime = time;
        }

        @Override
        public void run() {
            VLocation location;
            if (this.mRunning && (location = VLocationManager.this.getCurAppLocation()) != null && VLocationManager.this.notifyLocation(this.mListenerTransport, location.toSysLocation(), false)) {
                this.start();
            }
        }

        public void start() {
            this.mRunning = true;
            VLocationManager.this.mWorkHandler.removeCallbacks((Runnable)this);
            if (this.mTime > 0L) {
                VLocationManager.this.mWorkHandler.postDelayed((Runnable)this, this.mTime);
            } else {
                VLocationManager.this.mWorkHandler.post((Runnable)this);
            }
        }

        public void stop() {
            this.mRunning = false;
            VLocationManager.this.mWorkHandler.removeCallbacks((Runnable)this);
        }
    }
}

