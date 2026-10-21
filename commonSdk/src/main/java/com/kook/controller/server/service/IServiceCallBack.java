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
package com.kook.controller.server.service;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.carlos.libcommon.StringFog;

public interface IServiceCallBack
extends IInterface {
    public void checkBack(boolean var1, int var2) throws RemoteException;

    public void listenerOptionEvent(int var1) throws RemoteException;

    public void adbDebugFunction() throws RemoteException;

    public static abstract class Stub
    extends Binder
    implements IServiceCallBack {
        private static final String DESCRIPTOR = "com.kook.controller.server.service.IServiceCallBack";
        static final int TRANSACTION_checkBack = 1;
        static final int TRANSACTION_listenerOptionEvent = 2;
        static final int TRANSACTION_adbDebugFunction = 3;

        public Stub() {
            this.attachInterface(this, "com.kook.controller.server.service.IServiceCallBack");
        }

        public static IServiceCallBack asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && iin instanceof IServiceCallBack) {
                return (IServiceCallBack)iin;
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
                    boolean _arg0 = 0 != data.readInt();
                    int _arg1 = data.readInt();
                    this.checkBack(_arg0, _arg1);
                    reply.writeNoException();
                    return true;
                }
                case 2: {
                    data.enforceInterface(descriptor);
                    int _arg0 = data.readInt();
                    this.listenerOptionEvent(_arg0);
                    reply.writeNoException();
                    return true;
                }
                case 3: {
                    data.enforceInterface(descriptor);
                    this.adbDebugFunction();
                    reply.writeNoException();
                    return true;
                }
            }
            return super.onTransact(code, data, reply, flags);
        }

        public static boolean setDefaultImpl(IServiceCallBack impl) {
            if (Proxy.sDefaultImpl != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            }
            if (impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IServiceCallBack getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }

        private static class Proxy
        implements IServiceCallBack {
            private IBinder mRemote;
            public static IServiceCallBack sDefaultImpl;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return "com.kook.controller.server.service.IServiceCallBack";
            }

            /*
             * WARNING - Removed try catching itself - possible behaviour change.
             */
            @Override
            public void checkBack(boolean check, int targetProductIndex) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.kook.controller.server.service.IServiceCallBack");
                    _data.writeInt(check ? 1 : 0);
                    _data.writeInt(targetProductIndex);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().checkBack(check, targetProductIndex);
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
            public void listenerOptionEvent(int optionEvt) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.kook.controller.server.service.IServiceCallBack");
                    _data.writeInt(optionEvt);
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().listenerOptionEvent(optionEvt);
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
            public void adbDebugFunction() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.kook.controller.server.service.IServiceCallBack");
                    boolean _status = this.mRemote.transact(3, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().adbDebugFunction();
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
    implements IServiceCallBack {
        @Override
        public void checkBack(boolean check, int targetProductIndex) throws RemoteException {
        }

        @Override
        public void listenerOptionEvent(int optionEvt) throws RemoteException {
        }

        @Override
        public void adbDebugFunction() throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }
}

