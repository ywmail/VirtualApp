/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Binder
 *  android.os.IBinder
 *  android.os.IInterface
 *  android.os.Parcel
 *  android.os.RemoteException
 */
package com.lody.virtual.server.interfaces;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.lody.virtual.StringFog;

public interface IServiceFetcher
extends IInterface {
    public IBinder getService(String var1) throws RemoteException;

    public void addService(String var1, IBinder var2) throws RemoteException;

    public void removeService(String var1) throws RemoteException;

    public static abstract class Stub
    extends Binder
    implements IServiceFetcher {
        private static final String DESCRIPTOR = "com.lody.virtual.server.interfaces.IServiceFetcher";
        static final int TRANSACTION_getService = 1;
        static final int TRANSACTION_addService = 2;
        static final int TRANSACTION_removeService = 3;

        public Stub() {
            this.attachInterface(this, "com.lody.virtual.server.interfaces.IServiceFetcher");
        }

        public static IServiceFetcher asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && iin instanceof IServiceFetcher) {
                return (IServiceFetcher)iin;
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
                    String _arg0 = data.readString();
                    IBinder _result = this.getService(_arg0);
                    reply.writeNoException();
                    reply.writeStrongBinder(_result);
                    return true;
                }
                case 2: {
                    data.enforceInterface(descriptor);
                    String _arg0 = data.readString();
                    IBinder _arg1 = data.readStrongBinder();
                    this.addService(_arg0, _arg1);
                    reply.writeNoException();
                    return true;
                }
                case 3: {
                    data.enforceInterface(descriptor);
                    String _arg0 = data.readString();
                    this.removeService(_arg0);
                    reply.writeNoException();
                    return true;
                }
            }
            return super.onTransact(code, data, reply, flags);
        }

        public static boolean setDefaultImpl(IServiceFetcher impl) {
            if (Proxy.sDefaultImpl != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            }
            if (impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IServiceFetcher getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }

        private static class Proxy
        implements IServiceFetcher {
            private IBinder mRemote;
            public static IServiceFetcher sDefaultImpl;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return "com.lody.virtual.server.interfaces.IServiceFetcher";
            }

            /*
             * WARNING - Removed try catching itself - possible behaviour change.
             */
            @Override
            public IBinder getService(String name) throws RemoteException {
                IBinder _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IServiceFetcher");
                    _data.writeString(name);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        IBinder iBinder = Stub.getDefaultImpl().getService(name);
                        return iBinder;
                    }
                    _reply.readException();
                    _result = _reply.readStrongBinder();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return _result;
            }

            /*
             * WARNING - Removed try catching itself - possible behaviour change.
             */
            @Override
            public void addService(String name, IBinder service) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IServiceFetcher");
                    _data.writeString(name);
                    _data.writeStrongBinder(service);
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().addService(name, service);
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
            public void removeService(String name) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IServiceFetcher");
                    _data.writeString(name);
                    boolean _status = this.mRemote.transact(3, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().removeService(name);
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
    implements IServiceFetcher {
        @Override
        public IBinder getService(String name) throws RemoteException {
            return null;
        }

        @Override
        public void addService(String name, IBinder service) throws RemoteException {
        }

        @Override
        public void removeService(String name) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }
}

