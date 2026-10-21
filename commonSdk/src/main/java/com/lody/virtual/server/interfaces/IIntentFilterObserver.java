/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Intent
 *  android.os.Binder
 *  android.os.IBinder
 *  android.os.IInterface
 *  android.os.Parcel
 *  android.os.RemoteException
 */
package com.lody.virtual.server.interfaces;

import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.lody.virtual.StringFog;

public interface IIntentFilterObserver
extends IInterface {
    public Intent filter(Intent var1) throws RemoteException;

    public void setCallBack(IBinder var1) throws RemoteException;

    public IBinder getCallBack() throws RemoteException;

    public static abstract class Stub
    extends Binder
    implements IIntentFilterObserver {
        private static final String DESCRIPTOR = "com.lody.virtual.server.interfaces.IIntentFilterObserver";
        static final int TRANSACTION_filter = 1;
        static final int TRANSACTION_setCallBack = 2;
        static final int TRANSACTION_getCallBack = 3;

        public Stub() {
            this.attachInterface(this, "com.lody.virtual.server.interfaces.IIntentFilterObserver");
        }

        public static IIntentFilterObserver asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && iin instanceof IIntentFilterObserver) {
                return (IIntentFilterObserver)iin;
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
                    Intent _arg0 = 0 != data.readInt() ? (Intent)Intent.CREATOR.createFromParcel(data) : null;
                    Intent _result = this.filter(_arg0);
                    reply.writeNoException();
                    if (_result != null) {
                        reply.writeInt(1);
                        _result.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                }
                case 2: {
                    data.enforceInterface(descriptor);
                    IBinder _arg0 = data.readStrongBinder();
                    this.setCallBack(_arg0);
                    reply.writeNoException();
                    return true;
                }
                case 3: {
                    data.enforceInterface(descriptor);
                    IBinder _result = this.getCallBack();
                    reply.writeNoException();
                    reply.writeStrongBinder(_result);
                    return true;
                }
            }
            return super.onTransact(code, data, reply, flags);
        }

        public static boolean setDefaultImpl(IIntentFilterObserver impl) {
            if (Proxy.sDefaultImpl != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            }
            if (impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IIntentFilterObserver getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }

        private static class Proxy
        implements IIntentFilterObserver {
            private IBinder mRemote;
            public static IIntentFilterObserver sDefaultImpl;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return "com.lody.virtual.server.interfaces.IIntentFilterObserver";
            }

            /*
             * WARNING - Removed try catching itself - possible behaviour change.
             */
            @Override
            public Intent filter(Intent intent) throws RemoteException {
                Intent _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IIntentFilterObserver");
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Intent intent2 = Stub.getDefaultImpl().filter(intent);
                        return intent2;
                    }
                    _reply.readException();
                    _result = 0 != _reply.readInt() ? (Intent)Intent.CREATOR.createFromParcel(_reply) : null;
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
            public void setCallBack(IBinder callBack) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IIntentFilterObserver");
                    _data.writeStrongBinder(callBack);
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setCallBack(callBack);
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
            public IBinder getCallBack() throws RemoteException {
                IBinder _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IIntentFilterObserver");
                    boolean _status = this.mRemote.transact(3, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        IBinder iBinder = Stub.getDefaultImpl().getCallBack();
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
        }
    }

    public static class Default
    implements IIntentFilterObserver {
        @Override
        public Intent filter(Intent intent) throws RemoteException {
            return null;
        }

        @Override
        public void setCallBack(IBinder callBack) throws RemoteException {
        }

        @Override
        public IBinder getCallBack() throws RemoteException {
            return null;
        }

        public IBinder asBinder() {
            return null;
        }
    }
}

