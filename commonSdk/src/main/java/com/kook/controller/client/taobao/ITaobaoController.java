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
package com.kook.controller.client.taobao;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.carlos.libcommon.StringFog;

public interface ITaobaoController
extends IInterface {
    public boolean checkLogin(IBinder var1) throws RemoteException;

    public void doLogin(IBinder var1) throws RemoteException;

    public void testFun(IBinder var1) throws RemoteException;

    public static abstract class Stub
    extends Binder
    implements ITaobaoController {
        private static final String DESCRIPTOR = "com.kook.controller.client.taobao.ITaobaoController";
        static final int TRANSACTION_checkLogin = 1;
        static final int TRANSACTION_doLogin = 2;
        static final int TRANSACTION_testFun = 3;

        public Stub() {
            this.attachInterface(this, "com.kook.controller.client.taobao.ITaobaoController");
        }

        public static ITaobaoController asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && iin instanceof ITaobaoController) {
                return (ITaobaoController)iin;
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
                    boolean _result = this.checkLogin(_arg0);
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                }
                case 2: {
                    data.enforceInterface(descriptor);
                    IBinder _arg0 = data.readStrongBinder();
                    this.doLogin(_arg0);
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
            }
            return super.onTransact(code, data, reply, flags);
        }

        public static boolean setDefaultImpl(ITaobaoController impl) {
            if (Proxy.sDefaultImpl != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            }
            if (impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static ITaobaoController getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }

        private static class Proxy
        implements ITaobaoController {
            private IBinder mRemote;
            public static ITaobaoController sDefaultImpl;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return "com.kook.controller.client.taobao.ITaobaoController";
            }

            /*
             * WARNING - Removed try catching itself - possible behaviour change.
             */
            @Override
            public boolean checkLogin(IBinder ibinder) throws RemoteException {
                boolean _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.kook.controller.client.taobao.ITaobaoController");
                    _data.writeStrongBinder(ibinder);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        boolean bl = Stub.getDefaultImpl().checkLogin(ibinder);
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
            public void doLogin(IBinder ibinder) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.kook.controller.client.taobao.ITaobaoController");
                    _data.writeStrongBinder(ibinder);
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().doLogin(ibinder);
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
                    _data.writeInterfaceToken("com.kook.controller.client.taobao.ITaobaoController");
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
        }
    }

    public static class Default
    implements ITaobaoController {
        @Override
        public boolean checkLogin(IBinder ibinder) throws RemoteException {
            return false;
        }

        @Override
        public void doLogin(IBinder ibinder) throws RemoteException {
        }

        @Override
        public void testFun(IBinder ibinder) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }
}

