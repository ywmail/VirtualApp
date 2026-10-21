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
package com.kook.controller.client.wechat;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.carlos.libcommon.StringFog;

public interface IWeChatController
extends IInterface {
    public void scangroup(IBinder var1) throws RemoteException;

    public void sendgroup(IBinder var1) throws RemoteException;

    public void testFun(IBinder var1) throws RemoteException;

    public void onStopOrPause(boolean var1) throws RemoteException;

    public boolean hasFinishAllSteps() throws RemoteException;

    public static abstract class Stub
    extends Binder
    implements IWeChatController {
        private static final String DESCRIPTOR = "com.kook.controller.client.wechat.IWeChatController";
        static final int TRANSACTION_scangroup = 1;
        static final int TRANSACTION_sendgroup = 2;
        static final int TRANSACTION_testFun = 3;
        static final int TRANSACTION_onStopOrPause = 4;
        static final int TRANSACTION_hasFinishAllSteps = 5;

        public Stub() {
            this.attachInterface(this, "com.kook.controller.client.wechat.IWeChatController");
        }

        public static IWeChatController asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && iin instanceof IWeChatController) {
                return (IWeChatController)iin;
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
                    IBinder _arg0 = data.readStrongBinder();
                    this.scangroup(_arg0);
                    reply.writeNoException();
                    return true;
                }
                case 2: {
                    data.enforceInterface(descriptor);
                    IBinder _arg0 = data.readStrongBinder();
                    this.sendgroup(_arg0);
                    reply.writeNoException();
                    return true;
                }
                case 3: {
                    data.enforceInterface(descriptor);
                    IBinder _arg0 = data.readStrongBinder();
                    this.testFun(_arg0);
                    reply.writeNoException();
                    return true;
                }
                case 4: {
                    data.enforceInterface(descriptor);
                    boolean _arg0 = 0 != data.readInt();
                    this.onStopOrPause(_arg0);
                    reply.writeNoException();
                    return true;
                }
                case 5: {
                    data.enforceInterface(descriptor);
                    boolean _result = this.hasFinishAllSteps();
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                }
            }
            return super.onTransact(code, data, reply, flags);
        }

        public static boolean setDefaultImpl(IWeChatController impl) {
            if (Proxy.sDefaultImpl != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            }
            if (impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IWeChatController getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }

        private static class Proxy
        implements IWeChatController {
            private IBinder mRemote;
            public static IWeChatController sDefaultImpl;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return "com.kook.controller.client.wechat.IWeChatController";
            }

            /*
             * WARNING - Removed try catching itself - possible behaviour change.
             */
            @Override
            public void scangroup(IBinder ibinder) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.kook.controller.client.wechat.IWeChatController");
                    _data.writeStrongBinder(ibinder);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().scangroup(ibinder);
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
            public void sendgroup(IBinder ibinder) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.kook.controller.client.wechat.IWeChatController");
                    _data.writeStrongBinder(ibinder);
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().sendgroup(ibinder);
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
            public void testFun(IBinder ibinder) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.kook.controller.client.wechat.IWeChatController");
                    _data.writeStrongBinder(ibinder);
                    boolean _status = this.mRemote.transact(3, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().testFun(ibinder);
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
            public void onStopOrPause(boolean stopOrPause) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.kook.controller.client.wechat.IWeChatController");
                    _data.writeInt(stopOrPause ? 1 : 0);
                    boolean _status = this.mRemote.transact(4, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onStopOrPause(stopOrPause);
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
            public boolean hasFinishAllSteps() throws RemoteException {
                boolean _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.kook.controller.client.wechat.IWeChatController");
                    boolean _status = this.mRemote.transact(5, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        boolean bl = Stub.getDefaultImpl().hasFinishAllSteps();
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
    implements IWeChatController {
        @Override
        public void scangroup(IBinder ibinder) throws RemoteException {
        }

        @Override
        public void sendgroup(IBinder ibinder) throws RemoteException {
        }

        @Override
        public void testFun(IBinder ibinder) throws RemoteException {
        }

        @Override
        public void onStopOrPause(boolean stopOrPause) throws RemoteException {
        }

        @Override
        public boolean hasFinishAllSteps() throws RemoteException {
            return false;
        }

        public IBinder asBinder() {
            return null;
        }
    }
}

