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
package com.kook.controller.client.hyxd;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.carlos.libcommon.StringFog;

public interface IHYXDController
extends IInterface {
    public boolean memorySRWData(String var1, String var2, boolean var3) throws RemoteException;

    public boolean memoryTest() throws RemoteException;

    public static abstract class Stub
    extends Binder
    implements IHYXDController {
        private static final String DESCRIPTOR = "com.kook.controller.client.hyxd.IHYXDController";
        static final int TRANSACTION_memorySRWData = 1;
        static final int TRANSACTION_memoryTest = 2;

        public Stub() {
            this.attachInterface(this, "com.kook.controller.client.hyxd.IHYXDController");
        }

        public static IHYXDController asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && iin instanceof IHYXDController) {
                return (IHYXDController)iin;
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
                    String _arg1 = data.readString();
                    boolean _arg2 = 0 != data.readInt();
                    boolean _result = this.memorySRWData(_arg0, _arg1, _arg2);
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                }
                case 2: {
                    data.enforceInterface(descriptor);
                    boolean _result = this.memoryTest();
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                }
            }
            return super.onTransact(code, data, reply, flags);
        }

        public static boolean setDefaultImpl(IHYXDController impl) {
            if (Proxy.sDefaultImpl != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            }
            if (impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IHYXDController getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }

        private static class Proxy
        implements IHYXDController {
            private IBinder mRemote;
            public static IHYXDController sDefaultImpl;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return "com.kook.controller.client.hyxd.IHYXDController";
            }

            /*
             * WARNING - Removed try catching itself - possible behaviour change.
             */
            @Override
            public boolean memorySRWData(String searchValue, String writeValue, boolean permission2) throws RemoteException {
                boolean _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.kook.controller.client.hyxd.IHYXDController");
                    _data.writeString(searchValue);
                    _data.writeString(writeValue);
                    _data.writeInt(permission2 ? 1 : 0);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        boolean bl = Stub.getDefaultImpl().memorySRWData(searchValue, writeValue, permission2);
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

            /*
             * WARNING - Removed try catching itself - possible behaviour change.
             */
            @Override
            public boolean memoryTest() throws RemoteException {
                boolean _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.kook.controller.client.hyxd.IHYXDController");
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        boolean bl = Stub.getDefaultImpl().memoryTest();
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
    implements IHYXDController {
        @Override
        public boolean memorySRWData(String searchValue, String writeValue, boolean permission2) throws RemoteException {
            return false;
        }

        @Override
        public boolean memoryTest() throws RemoteException {
            return false;
        }

        public IBinder asBinder() {
            return null;
        }
    }
}

