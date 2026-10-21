/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.IntentSender
 *  android.net.Uri
 *  android.os.Binder
 *  android.os.IBinder
 *  android.os.IInterface
 *  android.os.Parcel
 *  android.os.RemoteException
 */
package com.lody.virtual.server.app;

import android.content.IntentSender;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.lody.virtual.StringFog;

public interface IAppInstallerSession
extends IInterface {
    public void addPackage(Uri var1) throws RemoteException;

    public void addSplit(Uri var1) throws RemoteException;

    public void commit(IntentSender var1) throws RemoteException;

    public void cancel() throws RemoteException;

    public static abstract class Stub
    extends Binder
    implements IAppInstallerSession {
        private static final String DESCRIPTOR = "com.lody.virtual.server.app.IAppInstallerSession";
        static final int TRANSACTION_addPackage = 1;
        static final int TRANSACTION_addSplit = 2;
        static final int TRANSACTION_commit = 3;
        static final int TRANSACTION_cancel = 4;

        public Stub() {
            this.attachInterface(this, "com.lody.virtual.server.app.IAppInstallerSession");
        }

        public static IAppInstallerSession asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && iin instanceof IAppInstallerSession) {
                return (IAppInstallerSession)iin;
            }
            return new Proxy(obj);
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            switch (code) {
                case 1598968902: {
                    reply.writeString(descriptor);
                    return true;
                }
                case 1: {
                    data.enforceInterface(descriptor);
                    Uri _arg0 = 0 != data.readInt() ? (Uri)Uri.CREATOR.createFromParcel(data) : null;
                    this.addPackage(_arg0);
                    reply.writeNoException();
                    return true;
                }
                case 2: {
                    data.enforceInterface(descriptor);
                    Uri _arg0 = 0 != data.readInt() ? (Uri)Uri.CREATOR.createFromParcel(data) : null;
                    this.addSplit(_arg0);
                    reply.writeNoException();
                    return true;
                }
                case 3: {
                    data.enforceInterface(descriptor);
                    IntentSender _arg0 = 0 != data.readInt() ? (IntentSender)IntentSender.CREATOR.createFromParcel(data) : null;
                    this.commit(_arg0);
                    reply.writeNoException();
                    return true;
                }
                case 4: {
                    data.enforceInterface(descriptor);
                    this.cancel();
                    reply.writeNoException();
                    return true;
                }
            }
            return super.onTransact(code, data, reply, flags);
        }

        public static boolean setDefaultImpl(IAppInstallerSession impl) {
            if (Proxy.sDefaultImpl != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            }
            if (impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IAppInstallerSession getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }

        private static class Proxy
        implements IAppInstallerSession {
            private IBinder mRemote;
            public static IAppInstallerSession sDefaultImpl;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return "com.lody.virtual.server.app.IAppInstallerSession";
            }

            /*
             * WARNING - Removed try catching itself - possible behaviour change.
             */
            @Override
            public void addPackage(Uri uri) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.app.IAppInstallerSession");
                    if (uri != null) {
                        _data.writeInt(1);
                        uri.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().addPackage(uri);
                        return;
                    }
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            /*
             * WARNING - Removed try catching itself - possible behaviour change.
             */
            @Override
            public void addSplit(Uri uri) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.app.IAppInstallerSession");
                    if (uri != null) {
                        _data.writeInt(1);
                        uri.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().addSplit(uri);
                        return;
                    }
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            /*
             * WARNING - Removed try catching itself - possible behaviour change.
             */
            @Override
            public void commit(IntentSender statusReceiver) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.app.IAppInstallerSession");
                    if (statusReceiver != null) {
                        _data.writeInt(1);
                        statusReceiver.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(3, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().commit(statusReceiver);
                        return;
                    }
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            /*
             * WARNING - Removed try catching itself - possible behaviour change.
             */
            @Override
            public void cancel() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.app.IAppInstallerSession");
                    boolean _status = this.mRemote.transact(4, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().cancel();
                        return;
                    }
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }

    public static class Default
    implements IAppInstallerSession {
        @Override
        public void addPackage(Uri uri) throws RemoteException {
        }

        @Override
        public void addSplit(Uri uri) throws RemoteException {
        }

        @Override
        public void commit(IntentSender statusReceiver) throws RemoteException {
        }

        @Override
        public void cancel() throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }
}

