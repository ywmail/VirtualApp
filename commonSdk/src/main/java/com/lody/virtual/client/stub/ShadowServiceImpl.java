/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Service
 *  android.content.ComponentName
 *  android.content.Intent
 *  android.os.Binder
 *  android.os.IBinder
 *  android.os.Process
 *  android.os.RemoteException
 *  android.os.SystemClock
 */
package com.lody.virtual.client.stub;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteException;
import android.os.SystemClock;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.VClient;
import com.lody.virtual.client.service.VServiceRuntime;
import com.lody.virtual.helper.utils.ComponentUtils;
import com.lody.virtual.helper.utils.VLog;
import com.lody.virtual.os.VUserHandle;
import com.lody.virtual.remote.ClientConfig;
import com.lody.virtual.remote.ServiceData;
import com.lody.virtual.server.IBinderProxyService;
import com.lody.virtual.server.secondary.FakeIdentityBinder;
import java.util.HashMap;
import java.util.Map;

public class ShadowServiceImpl
extends Service {
    private static final String TAG = ShadowServiceImpl.class.getSimpleName();
    private final VServiceRuntime mRuntime = VServiceRuntime.getInstance();
    private static final Map<String, IBindServiceProxy> sBinderServiceProxies = new HashMap<String, IBindServiceProxy>();

    public void onCreate() {
        this.mRuntime.setShadowService(this);
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null) {
            return 2;
        }
        String event = intent.getAction();
        if (event == null) {
            return 2;
        }
        if (event.equals("start_service")) {
            ServiceData.ServiceStartData data = new ServiceData.ServiceStartData(intent);
            if (data.intent == null) {
                VLog.e(TAG, "invalid start service intent: " + intent);
                return 2;
            }
            ClientConfig config = VClient.get().getClientConfig();
            if (config == null) {
                VLog.e(TAG, "restart service process: " + data.info.processName);
                return 2;
            }
            if (!data.info.processName.equals(config.processName)) {
                return 2;
            }
            if (data.userId != VUserHandle.myUserId()) {
                return 2;
            }
            VServiceRuntime.ServiceRecord record = this.mRuntime.getServiceRecord(data.component, true);
            if (record.service == null) {
                record.service = VClient.get().createService(data.info, (IBinder)record);
            }
            record.lastActivityTime = SystemClock.uptimeMillis();
            record.started = true;
            ++record.startId;
            data.intent.setExtrasClassLoader(record.service.getClassLoader());
            ComponentUtils.unpackFillIn(data.intent, record.service.getClassLoader());
            int result = record.service.onStartCommand(data.intent, flags, record.startId);
            if (result == 1) {
                result = 3;
            }
            return result;
        }
        if (event.equals("stop_service")) {
            ServiceData.ServiceStopData data = new ServiceData.ServiceStopData(intent);
            VServiceRuntime.ServiceRecord record = null;
            if (data.token instanceof VServiceRuntime.ServiceRecord) {
                record = (VServiceRuntime.ServiceRecord)data.token;
            }
            if (record == null) {
                record = this.mRuntime.getServiceRecord(data.component, false);
            }
            if (record == null) {
                return 2;
            }
            record.stopServiceIfNecessary(data.startId, true);
            return 2;
        }
        throw new RuntimeException("unknown action: " + event);
    }

    public IBinder onBind(Intent intent) {
        ServiceData.ServiceBindData data = new ServiceData.ServiceBindData(intent);
        ClientConfig config = VClient.get().getClientConfig();
        if (config == null) {
            VLog.e(TAG, "restart service process: " + data.info.processName);
            return null;
        }
        if (!data.info.processName.equals(config.processName)) {
            return null;
        }
        if (data.intent == null) {
            return null;
        }
        if (data.userId != VUserHandle.myUserId()) {
            return null;
        }
        if (data.connection == null) {
            return null;
        }
        VServiceRuntime.ServiceRecord record = this.mRuntime.getServiceRecord(data.component, true);
        if (record.service == null) {
            if ((data.flags & 1) == 0) {
                return null;
            }
            record.service = VClient.get().createService(data.info, (IBinder)record);
        }
        data.intent.setExtrasClassLoader(record.service.getClassLoader());
        IBinder binder = record.onBind(data.connection, data.intent);
        if (binder instanceof Binder) {
            try {
                String descriptor = binder.getInterfaceDescriptor();
                IBindServiceProxy proxy = sBinderServiceProxies.get(descriptor);
                if (proxy != null) {
                    binder = proxy.createProxy((Binder)binder);
                    VLog.e("ServiceRuntime", "proxy binder " + descriptor + " for service: " + data.component);
                }
            }
            catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return new BinderProxy(data.component, binder);
    }

    public boolean onUnbind(Intent intent) {
        ServiceData.ServiceBindData data = new ServiceData.ServiceBindData(intent);
        if (data.intent == null) {
            return false;
        }
        if (data.userId != VUserHandle.myUserId()) {
            return false;
        }
        if (data.connection == null) {
            return false;
        }
        VServiceRuntime.ServiceRecord record = this.mRuntime.getServiceRecord(data.component, false);
        if (record == null || record.service == null) {
            return false;
        }
        data.intent.setExtrasClassLoader(record.service.getClassLoader());
        record.onUnbind(data.connection, data.intent);
        return false;
    }

    public void onDestroy() {
        this.mRuntime.setShadowService(null);
    }

    static {
        sBinderServiceProxies.put("android.accounts.IAccountAuthenticator", new IBindServiceProxy(){

            @Override
            public Binder createProxy(Binder binder) {
                return new FakeIdentityBinder(binder, 1000, Process.myPid());
            }
        });
    }

    static class BinderProxy
    extends IBinderProxyService.Stub {
        private ComponentName component;
        private IBinder binder;

        public BinderProxy(ComponentName component, IBinder binder) {
            this.component = component;
            this.binder = binder;
        }

        @Override
        public ComponentName getComponent() {
            return this.component;
        }

        @Override
        public IBinder getService() {
            return this.binder;
        }
    }

    static interface IBindServiceProxy {
        public Binder createProxy(Binder var1);
    }
}

