/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.ComponentName
 *  android.content.Context
 *  android.content.ServiceConnection
 *  android.os.Handler
 *  android.os.IBinder
 *  android.os.RemoteException
 *  android.util.Log
 */
package com.lody.virtual.client.hook.secondary;

import android.app.IServiceConnection;
import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.VClient;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.hook.secondary.ProxyServiceFactory;
import com.lody.virtual.helper.collection.ArrayMap;
import com.lody.virtual.helper.compat.BuildCompat;
import com.lody.virtual.server.IBinderProxyService;
import mirror.android.app.ActivityThread;
import mirror.android.app.ContextImpl;
import mirror.android.app.IServiceConnectionO;
import mirror.android.app.LoadedApk;

public class ServiceConnectionProxy
extends IServiceConnection.Stub {
    private static final ArrayMap<IBinder, ServiceConnectionProxy> sProxyMap = new ArrayMap();
    private IServiceConnection mConn;

    private ServiceConnectionProxy(IServiceConnection mConn) {
        this.mConn = mConn;
    }

    public IServiceConnection getBase() {
        return this.mConn;
    }

    public static IServiceConnection getDispatcher(Context context, ServiceConnection connection, int flags) {
        IServiceConnection sd = null;
        if (connection == null) {
            throw new IllegalArgumentException("connection is null");
        }
        try {
            Object activityThread = ActivityThread.currentActivityThread.call(new Object[0]);
            Object loadApk = ContextImpl.mPackageInfo.get(VirtualCore.get().getContext());
            Handler handler = ActivityThread.getHandler.call(activityThread, new Object[0]);
            sd = LoadedApk.getServiceDispatcher.call(loadApk, connection, context, handler, flags);
        }
        catch (Exception e) {
            Log.e((String)"ConnectionDelegate", (String)"getServiceDispatcher", (Throwable)e);
        }
        if (sd == null) {
            throw new RuntimeException("Not supported in system context");
        }
        return sd;
    }

    public static IServiceConnection removeDispatcher(Context context, ServiceConnection conn) {
        IServiceConnection connection = null;
        try {
            Object loadApk = ContextImpl.mPackageInfo.get(VirtualCore.get().getContext());
            connection = LoadedApk.forgetServiceDispatcher.call(loadApk, context, conn);
        }
        catch (Exception e) {
            Log.e((String)"ConnectionDelegate", (String)"forgetServiceDispatcher", (Throwable)e);
        }
        return connection;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static ServiceConnectionProxy getOrCreateProxy(IServiceConnection conn) {
        if (conn instanceof ServiceConnectionProxy) {
            return (ServiceConnectionProxy)conn;
        }
        IBinder binder = conn.asBinder();
        ArrayMap<IBinder, ServiceConnectionProxy> arrayMap = sProxyMap;
        synchronized (arrayMap) {
            ServiceConnectionProxy proxy = (ServiceConnectionProxy)sProxyMap.get(binder);
            if (proxy == null) {
                proxy = new ServiceConnectionProxy(conn);
                sProxyMap.put(binder, proxy);
            }
            return proxy;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static ServiceConnectionProxy removeProxy(IServiceConnection conn) {
        if (conn == null) {
            return null;
        }
        ArrayMap<IBinder, ServiceConnectionProxy> arrayMap = sProxyMap;
        synchronized (arrayMap) {
            return (ServiceConnectionProxy)sProxyMap.remove(conn.asBinder());
        }
    }

    @Override
    public void connected(ComponentName name, IBinder service) throws RemoteException {
        this.connected(name, service, false);
    }

    public void connected(ComponentName name, IBinder service, boolean dead) throws RemoteException {
        if (service == null) {
            return;
        }
        IBinderProxyService proxy = IBinderProxyService.Stub.asInterface(service);
        if (proxy != null) {
            IBinder proxyService;
            name = proxy.getComponent();
            service = proxy.getService();
            if (VirtualCore.get().isVAppProcess() && (proxyService = ProxyServiceFactory.getProxyService((Context)VClient.get().getCurrentApplication(), name, service)) != null) {
                service = proxyService;
            }
        }
        if (BuildCompat.isOreo()) {
            IServiceConnectionO.connected.call(this.mConn, name, service, dead);
        } else {
            this.mConn.connected(name, service);
        }
    }
}

