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
package com.lody.virtual.server;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.lody.virtual.StringFog;

public interface IRequestPermissionsResult
extends IInterface {
    public boolean onResult(int var1, String[] var2, int[] var3) throws RemoteException;

    public static abstract class Stub
    extends Binder
    implements IRequestPermissionsResult {
        private static final String DESCRIPTOR = "com.lody.virtual.server.IRequestPermissionsResult";
        static final int TRANSACTION_onResult = 1;

        public Stub() {
            this.attachInterface(this, "com.lody.virtual.server.IRequestPermissionsResult");
        }

        public static IRequestPermissionsResult asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && iin instanceof IRequestPermissionsResult) {
                return (IRequestPermissionsResult)iin;
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
                    int _arg0 = data.readInt();
                    String[] _arg1 = data.createStringArray();
                    int[] _arg2 = data.createIntArray();
                    boolean _result = this.onResult(_arg0, _arg1, _arg2);
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                }
            }
            return super.onTransact(code, data, reply, flags);
        }

        public static boolean setDefaultImpl(IRequestPermissionsResult impl) {
            if (Proxy.sDefaultImpl != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            }
            if (impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IRequestPermissionsResult getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }

        private static class Proxy
        implements IRequestPermissionsResult {
            private IBinder mRemote;
            public static IRequestPermissionsResult sDefaultImpl;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return "com.lody.virtual.server.IRequestPermissionsResult";
            }

            /*
             * WARNING - Removed try catching itself - possible behaviour change.
             */
            @Override
            public boolean onResult(int requestCode, String[] permissions, int[] grantResults) throws RemoteException {
                boolean _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.IRequestPermissionsResult");
                    _data.writeInt(requestCode);
                    _data.writeStringArray(permissions);
                    _data.writeIntArray(grantResults);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        boolean bl = Stub.getDefaultImpl().onResult(requestCode, permissions, grantResults);
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
    implements IRequestPermissionsResult {
        @Override
        public boolean onResult(int requestCode, String[] permissions, int[] grantResults) throws RemoteException {
            return false;
        }

        public IBinder asBinder() {
            return null;
        }
    }
}

