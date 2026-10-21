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
package com.android.internal.widget;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.lody.virtual.StringFog;

public interface ILockSettings
extends IInterface {
    public void setRecoverySecretTypes(int[] var1) throws RemoteException;

    public int[] getRecoverySecretTypes() throws RemoteException;

    public static abstract class Stub
    extends Binder
    implements ILockSettings {
        private static final String DESCRIPTOR = "com.android.internal.widget.ILockSettings";
        static final int TRANSACTION_setRecoverySecretTypes = 1;
        static final int TRANSACTION_getRecoverySecretTypes = 2;

        public Stub() {
            this.attachInterface(this, "com.android.internal.widget.ILockSettings");
        }

        public static ILockSettings asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && iin instanceof ILockSettings) {
                return (ILockSettings)iin;
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
                    int[] _arg0 = data.createIntArray();
                    this.setRecoverySecretTypes(_arg0);
                    reply.writeNoException();
                    return true;
                }
                case 2: {
                    data.enforceInterface(descriptor);
                    int[] _result = this.getRecoverySecretTypes();
                    reply.writeNoException();
                    reply.writeIntArray(_result);
                    return true;
                }
            }
            return super.onTransact(code, data, reply, flags);
        }

        public static boolean setDefaultImpl(ILockSettings impl) {
            if (Proxy.sDefaultImpl != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            }
            if (impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static ILockSettings getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }

        private static class Proxy
        implements ILockSettings {
            private IBinder mRemote;
            public static ILockSettings sDefaultImpl;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return "com.android.internal.widget.ILockSettings";
            }

            /*
             * WARNING - Removed try catching itself - possible behaviour change.
             */
            @Override
            public void setRecoverySecretTypes(int[] secretTypes) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.android.internal.widget.ILockSettings");
                    _data.writeIntArray(secretTypes);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setRecoverySecretTypes(secretTypes);
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
            public int[] getRecoverySecretTypes() throws RemoteException {
                int[] _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.android.internal.widget.ILockSettings");
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        int[] nArray = Stub.getDefaultImpl().getRecoverySecretTypes();
                        return nArray;
                    }
                    _reply.readException();
                    _result = _reply.createIntArray();
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
    implements ILockSettings {
        @Override
        public void setRecoverySecretTypes(int[] secretTypes) throws RemoteException {
        }

        @Override
        public int[] getRecoverySecretTypes() throws RemoteException {
            return null;
        }

        public IBinder asBinder() {
            return null;
        }
    }
}

