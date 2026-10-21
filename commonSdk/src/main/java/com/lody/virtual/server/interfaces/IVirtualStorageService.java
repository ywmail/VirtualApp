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

public interface IVirtualStorageService
extends IInterface {
    public void setVirtualStorage(String var1, int var2, String var3) throws RemoteException;

    public String getVirtualStorage(String var1, int var2) throws RemoteException;

    public void setVirtualStorageState(String var1, int var2, boolean var3) throws RemoteException;

    public boolean isVirtualStorageEnable(String var1, int var2) throws RemoteException;

    public static abstract class Stub
    extends Binder
    implements IVirtualStorageService {
        private static final String DESCRIPTOR = "com.lody.virtual.server.interfaces.IVirtualStorageService";
        static final int TRANSACTION_setVirtualStorage = 1;
        static final int TRANSACTION_getVirtualStorage = 2;
        static final int TRANSACTION_setVirtualStorageState = 3;
        static final int TRANSACTION_isVirtualStorageEnable = 4;

        public Stub() {
            this.attachInterface(this, "com.lody.virtual.server.interfaces.IVirtualStorageService");
        }

        public static IVirtualStorageService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && iin instanceof IVirtualStorageService) {
                return (IVirtualStorageService)iin;
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
                    int _arg1 = data.readInt();
                    String _arg2 = data.readString();
                    this.setVirtualStorage(_arg0, _arg1, _arg2);
                    reply.writeNoException();
                    return true;
                }
                case 2: {
                    data.enforceInterface(descriptor);
                    String _arg0 = data.readString();
                    int _arg1 = data.readInt();
                    String _result = this.getVirtualStorage(_arg0, _arg1);
                    reply.writeNoException();
                    reply.writeString(_result);
                    return true;
                }
                case 3: {
                    data.enforceInterface(descriptor);
                    String _arg0 = data.readString();
                    int _arg1 = data.readInt();
                    boolean _arg2 = 0 != data.readInt();
                    this.setVirtualStorageState(_arg0, _arg1, _arg2);
                    reply.writeNoException();
                    return true;
                }
                case 4: {
                    data.enforceInterface(descriptor);
                    String _arg0 = data.readString();
                    int _arg1 = data.readInt();
                    boolean _result = this.isVirtualStorageEnable(_arg0, _arg1);
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                }
            }
            return super.onTransact(code, data, reply, flags);
        }

        public static boolean setDefaultImpl(IVirtualStorageService impl) {
            if (Proxy.sDefaultImpl != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            }
            if (impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IVirtualStorageService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }

        private static class Proxy
        implements IVirtualStorageService {
            private IBinder mRemote;
            public static IVirtualStorageService sDefaultImpl;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return "com.lody.virtual.server.interfaces.IVirtualStorageService";
            }

            /*
             * WARNING - Removed try catching itself - possible behaviour change.
             */
            @Override
            public void setVirtualStorage(String packageName, int userId, String vsPath) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IVirtualStorageService");
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    _data.writeString(vsPath);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setVirtualStorage(packageName, userId, vsPath);
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
            public String getVirtualStorage(String packageName, int userId) throws RemoteException {
                String _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IVirtualStorageService");
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        String string2 = Stub.getDefaultImpl().getVirtualStorage(packageName, userId);
                        return string2;
                    }
                    _reply.readException();
                    _result = _reply.readString();
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
            public void setVirtualStorageState(String packageName, int userId, boolean enable) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IVirtualStorageService");
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    _data.writeInt(enable ? 1 : 0);
                    boolean _status = this.mRemote.transact(3, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setVirtualStorageState(packageName, userId, enable);
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
            public boolean isVirtualStorageEnable(String packageName, int userId) throws RemoteException {
                boolean _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IVirtualStorageService");
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(4, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        boolean bl = Stub.getDefaultImpl().isVirtualStorageEnable(packageName, userId);
                        return bl;
                    }
                    _reply.readException();
                    _result = 0 != _reply.readInt();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return _result;
            }
        }
    }

    public static class Default
    implements IVirtualStorageService {
        @Override
        public void setVirtualStorage(String packageName, int userId, String vsPath) throws RemoteException {
        }

        @Override
        public String getVirtualStorage(String packageName, int userId) throws RemoteException {
            return null;
        }

        @Override
        public void setVirtualStorageState(String packageName, int userId, boolean enable) throws RemoteException {
        }

        @Override
        public boolean isVirtualStorageEnable(String packageName, int userId) throws RemoteException {
            return false;
        }

        public IBinder asBinder() {
            return null;
        }
    }
}

