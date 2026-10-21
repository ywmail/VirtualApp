/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.net.LinkProperties
 *  android.net.NetworkInfo
 *  android.os.Binder
 *  android.os.IBinder
 *  android.os.IInterface
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.RemoteException
 */
package android.net;

import android.net.LinkProperties;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
import com.lody.virtual.StringFog;

public interface IConnectivityManager
extends IInterface {
    public NetworkInfo getActiveNetworkInfo() throws RemoteException;

    public NetworkInfo getActiveNetworkInfoForUid(int var1, boolean var2) throws RemoteException;

    public NetworkInfo getNetworkInfo(int var1) throws RemoteException;

    public NetworkInfo[] getAllNetworkInfo() throws RemoteException;

    public boolean isActiveNetworkMetered() throws RemoteException;

    public boolean requestRouteToHostAddress(int var1, int var2) throws RemoteException;

    public LinkProperties getActiveLinkProperties() throws RemoteException;

    public LinkProperties getLinkProperties(int var1) throws RemoteException;

    public static abstract class Stub
    extends Binder
    implements IConnectivityManager {
        private static final String DESCRIPTOR = "android.net.IConnectivityManager";
        static final int TRANSACTION_getActiveNetworkInfo = 1;
        static final int TRANSACTION_getActiveNetworkInfoForUid = 2;
        static final int TRANSACTION_getNetworkInfo = 3;
        static final int TRANSACTION_getAllNetworkInfo = 4;
        static final int TRANSACTION_isActiveNetworkMetered = 5;
        static final int TRANSACTION_requestRouteToHostAddress = 6;
        static final int TRANSACTION_getActiveLinkProperties = 7;
        static final int TRANSACTION_getLinkProperties = 8;

        public Stub() {
            this.attachInterface(this, "android.net.IConnectivityManager");
        }

        public static IConnectivityManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && iin instanceof IConnectivityManager) {
                return (IConnectivityManager)iin;
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
                    NetworkInfo _result = this.getActiveNetworkInfo();
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
                    boolean _arg1 = 0 != data.readInt();
                    NetworkInfo _result = this.getActiveNetworkInfoForUid(_arg0, _arg1);
                    reply.writeNoException();
                    if (_result != null) {
                        reply.writeInt(1);
                        _result.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                }
                case 3: {
                    data.enforceInterface(descriptor);
                    int _arg0 = data.readInt();
                    NetworkInfo _result = this.getNetworkInfo(_arg0);
                    reply.writeNoException();
                    if (_result != null) {
                        reply.writeInt(1);
                        _result.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                }
                case 4: {
                    data.enforceInterface(descriptor);
                    NetworkInfo[] _result = this.getAllNetworkInfo();
                    reply.writeNoException();
                    reply.writeTypedArray((Parcelable[])_result, 1);
                    return true;
                }
                case 5: {
                    data.enforceInterface(descriptor);
                    boolean _result = this.isActiveNetworkMetered();
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                }
                case 6: {
                    data.enforceInterface(descriptor);
                    int _arg0 = data.readInt();
                    int _arg1 = data.readInt();
                    boolean _result = this.requestRouteToHostAddress(_arg0, _arg1);
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                }
                case 7: {
                    data.enforceInterface(descriptor);
                    LinkProperties _result = this.getActiveLinkProperties();
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
                    int _arg0 = data.readInt();
                    LinkProperties _result = this.getLinkProperties(_arg0);
                    reply.writeNoException();
                    if (_result != null) {
                        reply.writeInt(1);
                        _result.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                }
            }
            return super.onTransact(code, data, reply, flags);
        }

        public static boolean setDefaultImpl(IConnectivityManager impl) {
            if (Proxy.sDefaultImpl != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            }
            if (impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IConnectivityManager getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }

        private static class Proxy
        implements IConnectivityManager {
            private IBinder mRemote;
            public static IConnectivityManager sDefaultImpl;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return "android.net.IConnectivityManager";
            }

            /*
             * WARNING - Removed try catching itself - possible behaviour change.
             */
            @Override
            public NetworkInfo getActiveNetworkInfo() throws RemoteException {
                NetworkInfo _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("android.net.IConnectivityManager");
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        NetworkInfo networkInfo = Stub.getDefaultImpl().getActiveNetworkInfo();
                        return networkInfo;
                    }
                    _reply.readException();
                    _result = 0 != _reply.readInt() ? (NetworkInfo)NetworkInfo.CREATOR.createFromParcel(_reply) : null;
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
            public NetworkInfo getActiveNetworkInfoForUid(int uid, boolean ignoreBlocked) throws RemoteException {
                NetworkInfo _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("android.net.IConnectivityManager");
                    _data.writeInt(uid);
                    _data.writeInt(ignoreBlocked ? 1 : 0);
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        NetworkInfo networkInfo = Stub.getDefaultImpl().getActiveNetworkInfoForUid(uid, ignoreBlocked);
                        return networkInfo;
                    }
                    _reply.readException();
                    _result = 0 != _reply.readInt() ? (NetworkInfo)NetworkInfo.CREATOR.createFromParcel(_reply) : null;
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
            public NetworkInfo getNetworkInfo(int networkType) throws RemoteException {
                NetworkInfo _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("android.net.IConnectivityManager");
                    _data.writeInt(networkType);
                    boolean _status = this.mRemote.transact(3, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        NetworkInfo networkInfo = Stub.getDefaultImpl().getNetworkInfo(networkType);
                        return networkInfo;
                    }
                    _reply.readException();
                    _result = 0 != _reply.readInt() ? (NetworkInfo)NetworkInfo.CREATOR.createFromParcel(_reply) : null;
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
            public NetworkInfo[] getAllNetworkInfo() throws RemoteException {
                NetworkInfo[] _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("android.net.IConnectivityManager");
                    boolean _status = this.mRemote.transact(4, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        NetworkInfo[] networkInfoArray = Stub.getDefaultImpl().getAllNetworkInfo();
                        return networkInfoArray;
                    }
                    _reply.readException();
                    _result = (NetworkInfo[])_reply.createTypedArray(NetworkInfo.CREATOR);
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
            public boolean isActiveNetworkMetered() throws RemoteException {
                boolean _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("android.net.IConnectivityManager");
                    boolean _status = this.mRemote.transact(5, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        boolean bl = Stub.getDefaultImpl().isActiveNetworkMetered();
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
            public boolean requestRouteToHostAddress(int networkType, int address) throws RemoteException {
                boolean _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("android.net.IConnectivityManager");
                    _data.writeInt(networkType);
                    _data.writeInt(address);
                    boolean _status = this.mRemote.transact(6, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        boolean bl = Stub.getDefaultImpl().requestRouteToHostAddress(networkType, address);
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
            public LinkProperties getActiveLinkProperties() throws RemoteException {
                LinkProperties _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("android.net.IConnectivityManager");
                    boolean _status = this.mRemote.transact(7, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        LinkProperties linkProperties = Stub.getDefaultImpl().getActiveLinkProperties();
                        return linkProperties;
                    }
                    _reply.readException();
                    _result = 0 != _reply.readInt() ? (LinkProperties)LinkProperties.CREATOR.createFromParcel(_reply) : null;
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
            public LinkProperties getLinkProperties(int networkType) throws RemoteException {
                LinkProperties _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("android.net.IConnectivityManager");
                    _data.writeInt(networkType);
                    boolean _status = this.mRemote.transact(8, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        LinkProperties linkProperties = Stub.getDefaultImpl().getLinkProperties(networkType);
                        return linkProperties;
                    }
                    _reply.readException();
                    _result = 0 != _reply.readInt() ? (LinkProperties)LinkProperties.CREATOR.createFromParcel(_reply) : null;
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
    implements IConnectivityManager {
        @Override
        public NetworkInfo getActiveNetworkInfo() throws RemoteException {
            return null;
        }

        @Override
        public NetworkInfo getActiveNetworkInfoForUid(int uid, boolean ignoreBlocked) throws RemoteException {
            return null;
        }

        @Override
        public NetworkInfo getNetworkInfo(int networkType) throws RemoteException {
            return null;
        }

        @Override
        public NetworkInfo[] getAllNetworkInfo() throws RemoteException {
            return null;
        }

        @Override
        public boolean isActiveNetworkMetered() throws RemoteException {
            return false;
        }

        @Override
        public boolean requestRouteToHostAddress(int networkType, int address) throws RemoteException {
            return false;
        }

        @Override
        public LinkProperties getActiveLinkProperties() throws RemoteException {
            return null;
        }

        @Override
        public LinkProperties getLinkProperties(int networkType) throws RemoteException {
            return null;
        }

        public IBinder asBinder() {
            return null;
        }
    }
}

