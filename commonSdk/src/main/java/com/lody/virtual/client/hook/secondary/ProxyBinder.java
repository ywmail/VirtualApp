/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.IBinder
 *  android.os.IBinder$DeathRecipient
 *  android.os.IInterface
 *  android.os.Parcel
 *  android.os.RemoteException
 */
package com.lody.virtual.client.hook.secondary;

import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.io.FileDescriptor;

public class ProxyBinder
implements IBinder {
    private IBinder mBase;
    IInterface mProxyInterface;

    public ProxyBinder(IBinder base, IInterface proxyInterface) {
        this.mBase = base;
        this.mProxyInterface = proxyInterface;
    }

    public String getInterfaceDescriptor() throws RemoteException {
        return this.mBase.getInterfaceDescriptor();
    }

    public boolean pingBinder() {
        return this.mBase.pingBinder();
    }

    public boolean isBinderAlive() {
        return this.mBase.isBinderAlive();
    }

    public IInterface queryLocalInterface(String descriptor) {
        return this.mProxyInterface;
    }

    public void dump(FileDescriptor fd, String[] args) throws RemoteException {
        this.mBase.dump(fd, args);
    }

    public void dumpAsync(FileDescriptor fd, String[] args) throws RemoteException {
        this.mBase.dumpAsync(fd, args);
    }

    public boolean transact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
        return this.mBase.transact(code, data, reply, flags);
    }

    public void linkToDeath(IBinder.DeathRecipient recipient, int flags) throws RemoteException {
        this.mBase.linkToDeath(recipient, flags);
    }

    public boolean unlinkToDeath(IBinder.DeathRecipient recipient, int flags) {
        return this.mBase.unlinkToDeath(recipient, flags);
    }
}

