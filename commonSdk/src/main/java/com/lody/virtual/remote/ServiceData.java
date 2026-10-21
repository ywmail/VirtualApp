/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.ComponentName
 *  android.content.Intent
 *  android.content.pm.ServiceInfo
 *  android.os.IBinder
 *  android.os.Parcelable
 */
package com.lody.virtual.remote;

import android.app.IServiceConnection;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ServiceInfo;
import android.os.IBinder;
import android.os.Parcelable;
import com.lody.virtual.StringFog;
import com.lody.virtual.helper.compat.BundleCompat;

public class ServiceData {

    public static class ServiceBindData {
        public ComponentName component;
        public ServiceInfo info;
        public Intent intent;
        public int flags;
        public int userId;
        public IServiceConnection connection;

        public ServiceBindData(ComponentName component, ServiceInfo info, Intent intent, int flags, int userId, IServiceConnection connection) {
            this.component = component;
            this.info = info;
            this.intent = intent;
            this.flags = flags;
            this.userId = userId;
            this.connection = connection;
        }

        public ServiceBindData(Intent proxyIntent) {
            this.info = (ServiceInfo)proxyIntent.getParcelableExtra("info");
            if (this.info != null) {
                this.component = new ComponentName(this.info.packageName, this.info.name);
            }
            this.intent = (Intent)proxyIntent.getParcelableExtra("intent");
            this.flags = proxyIntent.getIntExtra("flags", 0);
            this.userId = proxyIntent.getIntExtra("user_id", 0);
            IBinder connBinder = BundleCompat.getBinder(proxyIntent, "conn");
            if (connBinder != null) {
                this.connection = IServiceConnection.Stub.asInterface(connBinder);
            }
        }

        public void saveToIntent(Intent proxyIntent) {
            proxyIntent.putExtra("info", (Parcelable)this.info);
            proxyIntent.putExtra("intent", (Parcelable)this.intent);
            proxyIntent.putExtra("flags", this.flags);
            proxyIntent.putExtra("user_id", this.userId);
            if (this.connection != null) {
                BundleCompat.putBinder(proxyIntent, "conn", this.connection.asBinder());
            }
        }
    }

    public static class ServiceStartData {
        public ComponentName component;
        public ServiceInfo info;
        public Intent intent;
        public int userId;

        public ServiceStartData(ComponentName component, ServiceInfo info, Intent intent, int userId) {
            this.component = component;
            this.info = info;
            this.intent = intent;
            this.userId = userId;
        }

        public ServiceStartData(Intent proxyIntent) {
            String type = proxyIntent.getType();
            if (type != null) {
                this.component = ComponentName.unflattenFromString((String)type);
            }
            this.info = (ServiceInfo)proxyIntent.getParcelableExtra("info");
            this.intent = (Intent)proxyIntent.getParcelableExtra("intent");
            this.userId = proxyIntent.getIntExtra("user_id", 0);
            if (this.info != null && this.intent != null && this.component != null && this.intent.getComponent() == null) {
                this.intent.setComponent(this.component);
            }
        }

        public void saveToIntent(Intent proxyIntent) {
            proxyIntent.setAction("start_service");
            proxyIntent.setType(this.component.flattenToString());
            proxyIntent.putExtra("info", (Parcelable)this.info);
            proxyIntent.putExtra("intent", (Parcelable)this.intent);
            proxyIntent.putExtra("user_id", this.userId);
        }
    }

    public static class ServiceStopData {
        public int userId;
        public ComponentName component;
        public int startId;
        public IBinder token;

        public ServiceStopData(int userId, ComponentName component, int startId, IBinder token) {
            this.userId = userId;
            this.component = component;
            this.startId = startId;
            this.token = token;
        }

        public ServiceStopData(Intent proxyIntent) {
            String type = proxyIntent.getType();
            if (type != null) {
                this.component = ComponentName.unflattenFromString((String)type);
            }
            this.userId = proxyIntent.getIntExtra("user_id", 0);
            this.startId = proxyIntent.getIntExtra("start_id", 0);
            this.token = BundleCompat.getBinder(proxyIntent, "token");
        }

        public void saveToIntent(Intent proxyIntent) {
            proxyIntent.setAction("stop_service");
            proxyIntent.setType(this.component.flattenToString());
            proxyIntent.putExtra("user_id", this.userId);
            proxyIntent.putExtra("start_id", this.startId);
            BundleCompat.putBinder(proxyIntent, "token", this.token);
        }
    }
}

