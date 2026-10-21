/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.ComponentName
 *  android.content.Context
 *  android.content.Intent
 *  android.content.ServiceConnection
 *  android.content.pm.PackageManager
 *  android.os.IBinder
 *  android.os.IInterface
 *  android.os.Looper
 *  android.os.Parcel
 *  android.os.RemoteException
 */
package com.kook.deviceinfo.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.concurrent.LinkedBlockingQueue;

public class AdvertisingIdClient {
    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static String getGoogleAdId(Context context) throws Exception {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            return "Cannot call in the main thread, You must call in the other thread";
        }
        PackageManager pm = context.getPackageManager();
        pm.getPackageInfo("com.android.vending", 0);
        AdvertisingConnection connection = new AdvertisingConnection();
        Intent intent = new Intent("com.google.android.gms.ads.identifier.service.START");
        intent.setPackage("com.google.android.gms");
        if (context.bindService(intent, (ServiceConnection)connection, 1)) {
            try {
                AdvertisingInterface adInterface = new AdvertisingInterface(connection.getBinder());
                String string2 = adInterface.getId();
                return string2;
            }
            finally {
                context.unbindService((ServiceConnection)connection);
            }
        }
        return "";
    }

    private static final class AdvertisingInterface
    implements IInterface {
        private IBinder binder;

        public AdvertisingInterface(IBinder pBinder) {
            this.binder = pBinder;
        }

        public IBinder asBinder() {
            return this.binder;
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        public String getId() throws RemoteException {
            String id2;
            Parcel data = Parcel.obtain();
            Parcel reply = Parcel.obtain();
            try {
                data.writeInterfaceToken("com.google.android.gms.ads.identifier.internal.IAdvertisingIdService");
                this.binder.transact(1, data, reply, 0);
                reply.readException();
                id2 = reply.readString();
            }
            finally {
                reply.recycle();
                data.recycle();
            }
            return id2;
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        public boolean isLimitAdTrackingEnabled(boolean paramBoolean) throws RemoteException {
            boolean limitAdTracking;
            Parcel data = Parcel.obtain();
            Parcel reply = Parcel.obtain();
            try {
                data.writeInterfaceToken("com.google.android.gms.ads.identifier.internal.IAdvertisingIdService");
                data.writeInt(paramBoolean ? 1 : 0);
                this.binder.transact(2, data, reply, 0);
                reply.readException();
                limitAdTracking = 0 != reply.readInt();
            }
            finally {
                reply.recycle();
                data.recycle();
            }
            return limitAdTracking;
        }
    }

    private static final class AdvertisingConnection
    implements ServiceConnection {
        boolean retrieved = false;
        private final LinkedBlockingQueue<IBinder> queue = new LinkedBlockingQueue(1);

        private AdvertisingConnection() {
        }

        public void onServiceConnected(ComponentName name, IBinder service) {
            try {
                this.queue.put(service);
            }
            catch (InterruptedException interruptedException) {
                // empty catch block
            }
        }

        public void onServiceDisconnected(ComponentName name) {
        }

        public IBinder getBinder() throws InterruptedException {
            if (this.retrieved) {
                throw new IllegalStateException();
            }
            this.retrieved = true;
            return this.queue.take();
        }
    }
}

