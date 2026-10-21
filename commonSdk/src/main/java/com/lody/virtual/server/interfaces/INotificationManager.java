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

public interface INotificationManager
extends IInterface {
    public int dealNotificationId(int var1, String var2, String var3, int var4) throws RemoteException;

    public String dealNotificationTag(int var1, String var2, String var3, int var4) throws RemoteException;

    public boolean areNotificationsEnabledForPackage(String var1, int var2) throws RemoteException;

    public void setNotificationsEnabledForPackage(String var1, boolean var2, int var3) throws RemoteException;

    public void addNotification(int var1, String var2, String var3, int var4) throws RemoteException;

    public void cancelAllNotification(String var1, int var2) throws RemoteException;

    public static abstract class Stub
    extends Binder
    implements INotificationManager {
        private static final String DESCRIPTOR = "com.lody.virtual.server.interfaces.INotificationManager";
        static final int TRANSACTION_dealNotificationId = 1;
        static final int TRANSACTION_dealNotificationTag = 2;
        static final int TRANSACTION_areNotificationsEnabledForPackage = 3;
        static final int TRANSACTION_setNotificationsEnabledForPackage = 4;
        static final int TRANSACTION_addNotification = 5;
        static final int TRANSACTION_cancelAllNotification = 6;

        public Stub() {
            this.attachInterface(this, "com.lody.virtual.server.interfaces.INotificationManager");
        }

        public static INotificationManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && iin instanceof INotificationManager) {
                return (INotificationManager)iin;
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
                    String _arg1 = data.readString();
                    String _arg2 = data.readString();
                    int _arg3 = data.readInt();
                    int _result = this.dealNotificationId(_arg0, _arg1, _arg2, _arg3);
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                }
                case 2: {
                    data.enforceInterface(descriptor);
                    int _arg0 = data.readInt();
                    String _arg1 = data.readString();
                    String _arg2 = data.readString();
                    int _arg3 = data.readInt();
                    String _result = this.dealNotificationTag(_arg0, _arg1, _arg2, _arg3);
                    reply.writeNoException();
                    reply.writeString(_result);
                    return true;
                }
                case 3: {
                    data.enforceInterface(descriptor);
                    String _arg0 = data.readString();
                    int _arg1 = data.readInt();
                    boolean _result = this.areNotificationsEnabledForPackage(_arg0, _arg1);
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                }
                case 4: {
                    data.enforceInterface(descriptor);
                    String _arg0 = data.readString();
                    boolean _arg1 = 0 != data.readInt();
                    int _arg2 = data.readInt();
                    this.setNotificationsEnabledForPackage(_arg0, _arg1, _arg2);
                    reply.writeNoException();
                    return true;
                }
                case 5: {
                    data.enforceInterface(descriptor);
                    int _arg0 = data.readInt();
                    String _arg1 = data.readString();
                    String _arg2 = data.readString();
                    int _arg3 = data.readInt();
                    this.addNotification(_arg0, _arg1, _arg2, _arg3);
                    reply.writeNoException();
                    return true;
                }
                case 6: {
                    data.enforceInterface(descriptor);
                    String _arg0 = data.readString();
                    int _arg1 = data.readInt();
                    this.cancelAllNotification(_arg0, _arg1);
                    reply.writeNoException();
                    return true;
                }
            }
            return super.onTransact(code, data, reply, flags);
        }

        public static boolean setDefaultImpl(INotificationManager impl) {
            if (Proxy.sDefaultImpl != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            }
            if (impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static INotificationManager getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }

        private static class Proxy
        implements INotificationManager {
            private IBinder mRemote;
            public static INotificationManager sDefaultImpl;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return "com.lody.virtual.server.interfaces.INotificationManager";
            }

            /*
             * WARNING - Removed try catching itself - possible behaviour change.
             */
            @Override
            public int dealNotificationId(int id2, String packageName, String tag, int userId) throws RemoteException {
                int _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.INotificationManager");
                    _data.writeInt(id2);
                    _data.writeString(packageName);
                    _data.writeString(tag);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        int n = Stub.getDefaultImpl().dealNotificationId(id2, packageName, tag, userId);
                        return n;
                    }
                    _reply.readException();
                    _result = _reply.readInt();
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
            public String dealNotificationTag(int id2, String packageName, String tag, int userId) throws RemoteException {
                String _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.INotificationManager");
                    _data.writeInt(id2);
                    _data.writeString(packageName);
                    _data.writeString(tag);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        String string2 = Stub.getDefaultImpl().dealNotificationTag(id2, packageName, tag, userId);
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
            public boolean areNotificationsEnabledForPackage(String packageName, int userId) throws RemoteException {
                boolean _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.INotificationManager");
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(3, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        boolean bl = Stub.getDefaultImpl().areNotificationsEnabledForPackage(packageName, userId);
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
            public void setNotificationsEnabledForPackage(String packageName, boolean enable, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.INotificationManager");
                    _data.writeString(packageName);
                    _data.writeInt(enable ? 1 : 0);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(4, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setNotificationsEnabledForPackage(packageName, enable, userId);
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
            public void addNotification(int id2, String tag, String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.INotificationManager");
                    _data.writeInt(id2);
                    _data.writeString(tag);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(5, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().addNotification(id2, tag, packageName, userId);
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
            public void cancelAllNotification(String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.INotificationManager");
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(6, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().cancelAllNotification(packageName, userId);
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
    implements INotificationManager {
        @Override
        public int dealNotificationId(int id2, String packageName, String tag, int userId) throws RemoteException {
            return 0;
        }

        @Override
        public String dealNotificationTag(int id2, String packageName, String tag, int userId) throws RemoteException {
            return null;
        }

        @Override
        public boolean areNotificationsEnabledForPackage(String packageName, int userId) throws RemoteException {
            return false;
        }

        @Override
        public void setNotificationsEnabledForPackage(String packageName, boolean enable, int userId) throws RemoteException {
        }

        @Override
        public void addNotification(int id2, String tag, String packageName, int userId) throws RemoteException {
        }

        @Override
        public void cancelAllNotification(String packageName, int userId) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }
}

