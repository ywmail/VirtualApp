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

public interface IProcessObserver
extends IInterface {
    public void onProcessCreated(String var1, String var2) throws RemoteException;

    public void onProcessDied(String var1, String var2) throws RemoteException;

    public static abstract class Stub
    extends Binder
    implements IProcessObserver {
        private static final String DESCRIPTOR = "com.lody.virtual.server.interfaces.IProcessObserver";
        static final int TRANSACTION_onProcessCreated = 1;
        static final int TRANSACTION_onProcessDied = 2;

        public Stub() {
            this.attachInterface(this, "com.lody.virtual.server.interfaces.IProcessObserver");
        }

        public static IProcessObserver asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && iin instanceof IProcessObserver) {
                return (IProcessObserver)iin;
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
                    this.onProcessCreated(_arg0, _arg1);
                    reply.writeNoException();
                    return true;
                }
                case 2: {
                    data.enforceInterface(descriptor);
                    String _arg0 = data.readString();
                    String _arg1 = data.readString();
                    this.onProcessDied(_arg0, _arg1);
                    reply.writeNoException();
                    return true;
                }
            }
            return super.onTransact(code, data, reply, flags);
        }

        public static boolean setDefaultImpl(IProcessObserver impl) {
            if (Proxy.sDefaultImpl != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            }
            if (impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IProcessObserver getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }

        private static class Proxy
        implements IProcessObserver {
            private IBinder mRemote;
            public static IProcessObserver sDefaultImpl;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return "com.lody.virtual.server.interfaces.IProcessObserver";
            }

            /*
             * WARNING - Removed try catching itself - possible behaviour change.
             */
            @Override
            public void onProcessCreated(String pkg, String processName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IProcessObserver");
                    _data.writeString(pkg);
                    _data.writeString(processName);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onProcessCreated(pkg, processName);
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
            public void onProcessDied(String pkg, String processName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IProcessObserver");
                    _data.writeString(pkg);
                    _data.writeString(processName);
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onProcessDied(pkg, processName);
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
    implements IProcessObserver {
        @Override
        public void onProcessCreated(String pkg, String processName) throws RemoteException {
        }

        @Override
        public void onProcessDied(String pkg, String processName) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }
}

