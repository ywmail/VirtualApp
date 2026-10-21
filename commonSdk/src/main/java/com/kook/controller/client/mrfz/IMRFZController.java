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
package com.kook.controller.client.mrfz;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.carlos.libcommon.StringFog;

public interface IMRFZController
extends IInterface {
    public void switchChange(int var1, boolean var2) throws RemoteException;

    public void setViewValue(int var1, float var2) throws RemoteException;

    public static abstract class Stub
    extends Binder
    implements IMRFZController {
        private static final String DESCRIPTOR = "com.kook.controller.client.mrfz.IMRFZController";
        static final int TRANSACTION_switchChange = 1;
        static final int TRANSACTION_setViewValue = 2;

        public Stub() {
            this.attachInterface(this, "com.kook.controller.client.mrfz.IMRFZController");
        }

        public static IMRFZController asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && iin instanceof IMRFZController) {
                return (IMRFZController)iin;
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
                    boolean _arg1 = 0 != data.readInt();
                    this.switchChange(_arg0, _arg1);
                    reply.writeNoException();
                    return true;
                }
                case 2: {
                    data.enforceInterface(descriptor);
                    int _arg0 = data.readInt();
                    float _arg1 = data.readFloat();
                    this.setViewValue(_arg0, _arg1);
                    reply.writeNoException();
                    return true;
                }
            }
            return super.onTransact(code, data, reply, flags);
        }

        public static boolean setDefaultImpl(IMRFZController impl) {
            if (Proxy.sDefaultImpl != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            }
            if (impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IMRFZController getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }

        private static class Proxy
        implements IMRFZController {
            private IBinder mRemote;
            public static IMRFZController sDefaultImpl;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return "com.kook.controller.client.mrfz.IMRFZController";
            }

            /*
             * WARNING - Removed try catching itself - possible behaviour change.
             */
            @Override
            public void switchChange(int viewIndex, boolean value) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.kook.controller.client.mrfz.IMRFZController");
                    _data.writeInt(viewIndex);
                    _data.writeInt(value ? 1 : 0);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().switchChange(viewIndex, value);
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
            public void setViewValue(int viewIndex, float value) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.kook.controller.client.mrfz.IMRFZController");
                    _data.writeInt(viewIndex);
                    _data.writeFloat(value);
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setViewValue(viewIndex, value);
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
    implements IMRFZController {
        @Override
        public void switchChange(int viewIndex, boolean value) throws RemoteException {
        }

        @Override
        public void setViewValue(int viewIndex, float value) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }
}

