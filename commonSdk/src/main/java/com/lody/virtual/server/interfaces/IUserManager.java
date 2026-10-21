/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.Bitmap
 *  android.os.Binder
 *  android.os.IBinder
 *  android.os.IInterface
 *  android.os.Parcel
 *  android.os.RemoteException
 */
package com.lody.virtual.server.interfaces;

import android.graphics.Bitmap;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.lody.virtual.StringFog;
import com.lody.virtual.os.VUserInfo;
import java.util.ArrayList;
import java.util.List;

public interface IUserManager
extends IInterface {
    public VUserInfo createUser(String var1, int var2) throws RemoteException;

    public boolean removeUser(int var1) throws RemoteException;

    public void setUserName(int var1, String var2) throws RemoteException;

    public void setUserIcon(int var1, Bitmap var2) throws RemoteException;

    public Bitmap getUserIcon(int var1) throws RemoteException;

    public List<VUserInfo> getUsers(boolean var1) throws RemoteException;

    public VUserInfo getUserInfo(int var1) throws RemoteException;

    public void setGuestEnabled(boolean var1) throws RemoteException;

    public boolean isGuestEnabled() throws RemoteException;

    public void wipeUser(int var1) throws RemoteException;

    public int getUserSerialNumber(int var1) throws RemoteException;

    public int getUserHandle(int var1) throws RemoteException;

    public static abstract class Stub
    extends Binder
    implements IUserManager {
        private static final String DESCRIPTOR = "com.lody.virtual.server.interfaces.IUserManager";
        static final int TRANSACTION_createUser = 1;
        static final int TRANSACTION_removeUser = 2;
        static final int TRANSACTION_setUserName = 3;
        static final int TRANSACTION_setUserIcon = 4;
        static final int TRANSACTION_getUserIcon = 5;
        static final int TRANSACTION_getUsers = 6;
        static final int TRANSACTION_getUserInfo = 7;
        static final int TRANSACTION_setGuestEnabled = 8;
        static final int TRANSACTION_isGuestEnabled = 9;
        static final int TRANSACTION_wipeUser = 10;
        static final int TRANSACTION_getUserSerialNumber = 11;
        static final int TRANSACTION_getUserHandle = 12;

        public Stub() {
            this.attachInterface(this, "com.lody.virtual.server.interfaces.IUserManager");
        }

        public static IUserManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && iin instanceof IUserManager) {
                return (IUserManager)iin;
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
                    int _arg1 = data.readInt();
                    VUserInfo _result = this.createUser(_arg0, _arg1);
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
                    int _arg0 = data.readInt();
                    boolean _result = this.removeUser(_arg0);
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                }
                case 3: {
                    data.enforceInterface(descriptor);
                    int _arg0 = data.readInt();
                    String _arg1 = data.readString();
                    this.setUserName(_arg0, _arg1);
                    reply.writeNoException();
                    return true;
                }
                case 4: {
                    data.enforceInterface(descriptor);
                    int _arg0 = data.readInt();
                    Bitmap _arg1 = 0 != data.readInt() ? (Bitmap)Bitmap.CREATOR.createFromParcel(data) : null;
                    this.setUserIcon(_arg0, _arg1);
                    reply.writeNoException();
                    return true;
                }
                case 5: {
                    data.enforceInterface(descriptor);
                    int _arg0 = data.readInt();
                    Bitmap _result = this.getUserIcon(_arg0);
                    reply.writeNoException();
                    if (_result != null) {
                        reply.writeInt(1);
                        _result.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                }
                case 6: {
                    data.enforceInterface(descriptor);
                    boolean _arg0 = 0 != data.readInt();
                    List _result = this.getUsers(_arg0);
                    reply.writeNoException();
                    reply.writeTypedList(_result);
                    return true;
                }
                case 7: {
                    data.enforceInterface(descriptor);
                    int _arg0 = data.readInt();
                    VUserInfo _result = this.getUserInfo(_arg0);
                    reply.writeNoException();
                    if (_result != null) {
                        reply.writeInt(1);
                        _result.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                }
                case 8: {
                    data.enforceInterface(descriptor);
                    boolean _arg0 = 0 != data.readInt();
                    this.setGuestEnabled(_arg0);
                    reply.writeNoException();
                    return true;
                }
                case 9: {
                    data.enforceInterface(descriptor);
                    boolean _result = this.isGuestEnabled();
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                }
                case 10: {
                    data.enforceInterface(descriptor);
                    int _arg0 = data.readInt();
                    this.wipeUser(_arg0);
                    reply.writeNoException();
                    return true;
                }
                case 11: {
                    data.enforceInterface(descriptor);
                    int _arg0 = data.readInt();
                    int _result = this.getUserSerialNumber(_arg0);
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                }
                case 12: {
                    data.enforceInterface(descriptor);
                    int _arg0 = data.readInt();
                    int _result = this.getUserHandle(_arg0);
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                }
            }
            return super.onTransact(code, data, reply, flags);
        }

        public static boolean setDefaultImpl(IUserManager impl) {
            if (Proxy.sDefaultImpl != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            }
            if (impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IUserManager getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }

        private static class Proxy
        implements IUserManager {
            private IBinder mRemote;
            public static IUserManager sDefaultImpl;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return "com.lody.virtual.server.interfaces.IUserManager";
            }

            /*
             * WARNING - Removed try catching itself - possible behaviour change.
             */
            @Override
            public VUserInfo createUser(String name, int flags) throws RemoteException {
                VUserInfo _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IUserManager");
                    _data.writeString(name);
                    _data.writeInt(flags);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        VUserInfo vUserInfo = Stub.getDefaultImpl().createUser(name, flags);
                        return vUserInfo;
                    }
                    _reply.readException();
                    _result = 0 != _reply.readInt() ? (VUserInfo)VUserInfo.CREATOR.createFromParcel(_reply) : null;
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
            public boolean removeUser(int userHandle) throws RemoteException {
                boolean _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IUserManager");
                    _data.writeInt(userHandle);
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        boolean bl = Stub.getDefaultImpl().removeUser(userHandle);
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
            public void setUserName(int userHandle, String name) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IUserManager");
                    _data.writeInt(userHandle);
                    _data.writeString(name);
                    boolean _status = this.mRemote.transact(3, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setUserName(userHandle, name);
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
            public void setUserIcon(int userHandle, Bitmap icon) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IUserManager");
                    _data.writeInt(userHandle);
                    if (icon != null) {
                        _data.writeInt(1);
                        icon.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(4, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setUserIcon(userHandle, icon);
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
            public Bitmap getUserIcon(int userHandle) throws RemoteException {
                Bitmap _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IUserManager");
                    _data.writeInt(userHandle);
                    boolean _status = this.mRemote.transact(5, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Bitmap bitmap = Stub.getDefaultImpl().getUserIcon(userHandle);
                        return bitmap;
                    }
                    _reply.readException();
                    _result = 0 != _reply.readInt() ? (Bitmap)Bitmap.CREATOR.createFromParcel(_reply) : null;
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
            public List<VUserInfo> getUsers(boolean excludeDying) throws RemoteException {
                ArrayList _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IUserManager");
                    _data.writeInt(excludeDying ? 1 : 0);
                    boolean _status = this.mRemote.transact(6, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        List<VUserInfo> list = Stub.getDefaultImpl().getUsers(excludeDying);
                        return list;
                    }
                    _reply.readException();
                    _result = _reply.createTypedArrayList(VUserInfo.CREATOR);
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
            public VUserInfo getUserInfo(int userHandle) throws RemoteException {
                VUserInfo _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IUserManager");
                    _data.writeInt(userHandle);
                    boolean _status = this.mRemote.transact(7, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        VUserInfo vUserInfo = Stub.getDefaultImpl().getUserInfo(userHandle);
                        return vUserInfo;
                    }
                    _reply.readException();
                    _result = 0 != _reply.readInt() ? (VUserInfo)VUserInfo.CREATOR.createFromParcel(_reply) : null;
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
            public void setGuestEnabled(boolean enable) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IUserManager");
                    _data.writeInt(enable ? 1 : 0);
                    boolean _status = this.mRemote.transact(8, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setGuestEnabled(enable);
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
            public boolean isGuestEnabled() throws RemoteException {
                boolean _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IUserManager");
                    boolean _status = this.mRemote.transact(9, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        boolean bl = Stub.getDefaultImpl().isGuestEnabled();
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
            public void wipeUser(int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IUserManager");
                    _data.writeInt(userHandle);
                    boolean _status = this.mRemote.transact(10, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().wipeUser(userHandle);
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
            public int getUserSerialNumber(int userHandle) throws RemoteException {
                int _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IUserManager");
                    _data.writeInt(userHandle);
                    boolean _status = this.mRemote.transact(11, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        int n = Stub.getDefaultImpl().getUserSerialNumber(userHandle);
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
            public int getUserHandle(int userSerialNumber) throws RemoteException {
                int _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IUserManager");
                    _data.writeInt(userSerialNumber);
                    boolean _status = this.mRemote.transact(12, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        int n = Stub.getDefaultImpl().getUserHandle(userSerialNumber);
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
        }
    }

    public static class Default
    implements IUserManager {
        @Override
        public VUserInfo createUser(String name, int flags) throws RemoteException {
            return null;
        }

        @Override
        public boolean removeUser(int userHandle) throws RemoteException {
            return false;
        }

        @Override
        public void setUserName(int userHandle, String name) throws RemoteException {
        }

        @Override
        public void setUserIcon(int userHandle, Bitmap icon) throws RemoteException {
        }

        @Override
        public Bitmap getUserIcon(int userHandle) throws RemoteException {
            return null;
        }

        @Override
        public List<VUserInfo> getUsers(boolean excludeDying) throws RemoteException {
            return null;
        }

        @Override
        public VUserInfo getUserInfo(int userHandle) throws RemoteException {
            return null;
        }

        @Override
        public void setGuestEnabled(boolean enable) throws RemoteException {
        }

        @Override
        public boolean isGuestEnabled() throws RemoteException {
            return false;
        }

        @Override
        public void wipeUser(int userHandle) throws RemoteException {
        }

        @Override
        public int getUserSerialNumber(int userHandle) throws RemoteException {
            return 0;
        }

        @Override
        public int getUserHandle(int userSerialNumber) throws RemoteException {
            return 0;
        }

        public IBinder asBinder() {
            return null;
        }
    }
}

