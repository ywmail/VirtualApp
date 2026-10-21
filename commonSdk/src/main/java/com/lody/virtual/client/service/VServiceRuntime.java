/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.ActivityManager$RunningServiceInfo
 *  android.app.Service
 *  android.content.ComponentName
 *  android.content.Intent
 *  android.os.Binder
 *  android.os.Handler
 *  android.os.IBinder
 *  android.os.IInterface
 *  android.os.Looper
 *  android.os.Process
 *  android.os.RemoteCallbackList
 *  android.os.SystemClock
 */
package com.lody.virtual.client.service;

import android.app.ActivityManager;
import android.app.IServiceConnection;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import android.os.Process;
import android.os.RemoteCallbackList;
import android.os.SystemClock;
import com.lody.virtual.client.VClient;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class VServiceRuntime {
    private static final VServiceRuntime sInstance = new VServiceRuntime();
    private final Map<ComponentName, ServiceRecord> mComponentToServiceRecords = new HashMap<ComponentName, ServiceRecord>();
    private RemoteCallbackList<IServiceConnection> mConnectionCallbackList = new RemoteCallbackList<IServiceConnection>(){

        public void onCallbackDied(final IServiceConnection callback) {
            VServiceRuntime.this.mHandler.post(new Runnable(){

                @Override
                public void run() {
                    VServiceRuntime.this.handleConnectionDied(callback);
                }
            });
        }
    };
    private Service mShadowService;
    private Handler mHandler = new Handler(Looper.getMainLooper());

    private VServiceRuntime() {
    }

    public void setShadowService(Service service) {
        this.mShadowService = service;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public ServiceRecord getServiceRecord(ComponentName component, boolean create) {
        Map<ComponentName, ServiceRecord> map = this.mComponentToServiceRecords;
        synchronized (map) {
            ServiceRecord record = this.mComponentToServiceRecords.get(component);
            if (record == null && create) {
                record = new ServiceRecord();
                record.component = component;
                record.lastActivityTime = SystemClock.uptimeMillis();
                record.activeSince = SystemClock.elapsedRealtime();
                this.mComponentToServiceRecords.put(component, record);
            }
            return record;
        }
    }

    public static VServiceRuntime getInstance() {
        return sInstance;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void handleConnectionDied(IServiceConnection conn) {
        Map<ComponentName, ServiceRecord> map = this.mComponentToServiceRecords;
        synchronized (map) {
            for (ServiceRecord serviceRecord : this.mComponentToServiceRecords.values()) {
                for (ServiceBindRecord bindRecord : serviceRecord.bindings) {
                    bindRecord.connections.remove(conn.asBinder());
                }
            }
            this.trimService();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void trimService() {
        Map<ComponentName, ServiceRecord> map = this.mComponentToServiceRecords;
        synchronized (map) {
            for (ServiceRecord serviceRecord : this.mComponentToServiceRecords.values()) {
                if (serviceRecord.service == null || serviceRecord.started || serviceRecord.getClientCount() > 0 || serviceRecord.getConnectionCount() > 0) continue;
                serviceRecord.service.onDestroy();
                serviceRecord.service = null;
                this.mComponentToServiceRecords.remove(serviceRecord.component);
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public List<ActivityManager.RunningServiceInfo> getServices() {
        ArrayList<ActivityManager.RunningServiceInfo> infos = new ArrayList<ActivityManager.RunningServiceInfo>(this.mComponentToServiceRecords.size());
        Map<ComponentName, ServiceRecord> map = this.mComponentToServiceRecords;
        synchronized (map) {
            for (ServiceRecord serviceRecord : this.mComponentToServiceRecords.values()) {
                ActivityManager.RunningServiceInfo info = new ActivityManager.RunningServiceInfo();
                info.pid = Process.myPid();
                info.uid = VClient.get().getVUid();
                info.activeSince = serviceRecord.activeSince;
                info.lastActivityTime = serviceRecord.lastActivityTime;
                info.clientCount = serviceRecord.getClientCount();
                info.service = serviceRecord.component;
                info.started = serviceRecord.started;
                info.process = VClient.get().getClientConfig().processName;
                infos.add(info);
            }
        }
        return infos;
    }

    public class ServiceRecord
    extends Binder {
        public ComponentName component;
        public long activeSince;
        public boolean foreground;
        public long lastActivityTime;
        public boolean started;
        public Service service;
        public int startId;
        public final List<ServiceBindRecord> bindings = new ArrayList<ServiceBindRecord>();

        public int getClientCount() {
            return this.bindings.size();
        }

        int getConnectionCount() {
            int count = 0;
            for (ServiceBindRecord record : this.bindings) {
                count += record.getConnectionCount();
            }
            return count;
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        public void stopServiceIfNecessary(int requestStartId, boolean stopService) {
            if (stopService) {
                if (requestStartId != -1 && requestStartId != this.startId) {
                    return;
                }
                this.started = false;
            }
            if (this.service != null && !this.started && this.getConnectionCount() <= 0) {
                this.service.onDestroy();
                this.service = null;
                Map map = VServiceRuntime.this.mComponentToServiceRecords;
                synchronized (map) {
                    VServiceRuntime.this.mComponentToServiceRecords.remove(this.component);
                }
                if (VServiceRuntime.this.mComponentToServiceRecords.isEmpty()) {
                    VServiceRuntime.this.mShadowService.stopSelf();
                }
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        public IBinder onBind(IServiceConnection conn, Intent intent) {
            this.lastActivityTime = SystemClock.uptimeMillis();
            VServiceRuntime.this.mConnectionCallbackList.register(conn);
            Map map = VServiceRuntime.this.mComponentToServiceRecords;
            synchronized (map) {
                for (ServiceBindRecord binding : this.bindings) {
                    if (!binding.intent.filterEquals(intent)) continue;
                    if (binding.connections.isEmpty() && binding.rebindStatus == RebindStatus.Rebind) {
                        this.service.onRebind(intent);
                    }
                    binding.connections.add(conn.asBinder());
                    return binding.binder;
                }
                ServiceBindRecord bindRecord = new ServiceBindRecord();
                bindRecord.intent = intent;
                bindRecord.connections.add(conn.asBinder());
                bindRecord.binder = this.service.onBind(intent);
                this.bindings.add(bindRecord);
                return bindRecord.binder;
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        public void onUnbind(IServiceConnection conn, Intent intent) {
            Map map = VServiceRuntime.this.mComponentToServiceRecords;
            synchronized (map) {
                for (ServiceBindRecord binding : this.bindings) {
                    if (!binding.intent.filterEquals(intent)) continue;
                    if (!binding.connections.remove(conn.asBinder())) break;
                    if (binding.connections.isEmpty() && binding.rebindStatus != RebindStatus.NotRebind) {
                        binding.rebindStatus = this.service.onUnbind(intent) ? RebindStatus.Rebind : RebindStatus.NotRebind;
                    }
                    this.stopServiceIfNecessary(-1, false);
                    break;
                }
            }
        }
    }

    public static class ServiceBindRecord {
        public Intent intent;
        public final Set<IBinder> connections = new HashSet<IBinder>();
        public RebindStatus rebindStatus;
        public IBinder binder;

        public int getConnectionCount() {
            return this.connections.size();
        }
    }

    public static enum RebindStatus {
        NotYetBound,
        Rebind,
        NotRebind;

    }
}

