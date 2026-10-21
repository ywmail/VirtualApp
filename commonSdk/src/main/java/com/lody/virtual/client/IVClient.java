/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.ActivityManager$RunningServiceInfo
 *  android.content.ComponentName
 *  android.content.Intent
 *  android.content.pm.ProviderInfo
 *  android.os.Binder
 *  android.os.IBinder
 *  android.os.IInterface
 *  android.os.Parcel
 *  android.os.RemoteException
 */
package com.lody.virtual.client;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ProviderInfo;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.lody.virtual.StringFog;
import java.util.ArrayList;
import java.util.List;

public interface IVClient
extends IInterface {
    public void scheduleNewIntent(String var1, IBinder var2, Intent var3) throws RemoteException;

    public void finishActivity(IBinder var1) throws RemoteException;

    public IBinder createProxyService(ComponentName var1, IBinder var2) throws RemoteException;

    public IBinder acquireProviderClient(ProviderInfo var1) throws RemoteException;

    public IBinder getAppThread() throws RemoteException;

    public IBinder getToken() throws RemoteException;

    public boolean isAppRunning() throws RemoteException;

    public String getDebugInfo() throws RemoteException;

    public boolean finishReceiver(IBinder var1) throws RemoteException;

    public List<ActivityManager.RunningServiceInfo> getServices() throws RemoteException;

    public static abstract class Stub
    extends Binder
    implements IVClient {
        private static final String DESCRIPTOR = "com.lody.virtual.client.IVClient";
        static final int TRANSACTION_scheduleNewIntent = 1;
        static final int TRANSACTION_finishActivity = 2;
        static final int TRANSACTION_createProxyService = 3;
        static final int TRANSACTION_acquireProviderClient = 4;
        static final int TRANSACTION_getAppThread = 5;
        static final int TRANSACTION_getToken = 6;
        static final int TRANSACTION_isAppRunning = 7;
        static final int TRANSACTION_getDebugInfo = 8;
        static final int TRANSACTION_finishReceiver = 9;
        static final int TRANSACTION_getServices = 10;

        public Stub() {
            this.attachInterface(this, "com.lody.virtual.client.IVClient");
        }

        public static IVClient asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && iin instanceof IVClient) {
                return (IVClient)iin;
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
                    IBinder _arg1 = data.readStrongBinder();
                    Intent _arg2 = 0 != data.readInt() ? (Intent)Intent.CREATOR.createFromParcel(data) : null;
                    this.scheduleNewIntent(_arg0, _arg1, _arg2);
                    reply.writeNoException();
                    return true;
                }
                case 2: {
                    data.enforceInterface(descriptor);
                    IBinder _arg0 = data.readStrongBinder();
                    this.finishActivity(_arg0);
                    reply.writeNoException();
                    return true;
                }
                case 3: {
                    data.enforceInterface(descriptor);
                    ComponentName _arg0 = 0 != data.readInt() ? (ComponentName)ComponentName.CREATOR.createFromParcel(data) : null;
                    IBinder _arg1 = data.readStrongBinder();
                    IBinder _result = this.createProxyService(_arg0, _arg1);
                    reply.writeNoException();
                    reply.writeStrongBinder(_result);
                    return true;
                }
                case 4: {
                    data.enforceInterface(descriptor);
                    ProviderInfo _arg0 = 0 != data.readInt() ? (ProviderInfo)ProviderInfo.CREATOR.createFromParcel(data) : null;
                    IBinder _result = this.acquireProviderClient(_arg0);
                    reply.writeNoException();
                    reply.writeStrongBinder(_result);
                    return true;
                }
                case 5: {
                    data.enforceInterface(descriptor);
                    IBinder _result = this.getAppThread();
                    reply.writeNoException();
                    reply.writeStrongBinder(_result);
                    return true;
                }
                case 6: {
                    data.enforceInterface(descriptor);
                    IBinder _result = this.getToken();
                    reply.writeNoException();
                    reply.writeStrongBinder(_result);
                    return true;
                }
                case 7: {
                    data.enforceInterface(descriptor);
                    boolean _result = this.isAppRunning();
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                }
                case 8: {
                    data.enforceInterface(descriptor);
                    String _result = this.getDebugInfo();
                    reply.writeNoException();
                    reply.writeString(_result);
                    return true;
                }
                case 9: {
                    data.enforceInterface(descriptor);
                    IBinder _arg0 = data.readStrongBinder();
                    boolean _result = this.finishReceiver(_arg0);
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                }
                case 10: {
                    data.enforceInterface(descriptor);
                    List _result = this.getServices();
                    reply.writeNoException();
                    reply.writeTypedList(_result);
                    return true;
                }
            }
            return super.onTransact(code, data, reply, flags);
        }

        public static boolean setDefaultImpl(IVClient impl) {
            if (Proxy.sDefaultImpl != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            }
            if (impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IVClient getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }

        private static class Proxy
        implements IVClient {
            private IBinder mRemote;
            public static IVClient sDefaultImpl;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return "com.lody.virtual.client.IVClient";
            }

            /*
             * WARNING - Removed try catching itself - possible behaviour change.
             */
            @Override
            public void scheduleNewIntent(String creator, IBinder token, Intent intent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.client.IVClient");
                    _data.writeString(creator);
                    _data.writeStrongBinder(token);
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().scheduleNewIntent(creator, token, intent);
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
            public void finishActivity(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.client.IVClient");
                    _data.writeStrongBinder(token);
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().finishActivity(token);
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
            public IBinder createProxyService(ComponentName component, IBinder binder) throws RemoteException {
                IBinder _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.client.IVClient");
                    if (component != null) {
                        _data.writeInt(1);
                        component.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(binder);
                    boolean _status = this.mRemote.transact(3, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        IBinder iBinder = Stub.getDefaultImpl().createProxyService(component, binder);
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

            /*
             * WARNING - Removed try catching itself - possible behaviour change.
             */
            @Override
            public IBinder acquireProviderClient(ProviderInfo info) throws RemoteException {
                IBinder _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.client.IVClient");
                    if (info != null) {
                        _data.writeInt(1);
                        info.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(4, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        IBinder iBinder = Stub.getDefaultImpl().acquireProviderClient(info);
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

            /*
             * WARNING - Removed try catching itself - possible behaviour change.
             */
            @Override
            public IBinder getAppThread() throws RemoteException {
                IBinder _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.client.IVClient");
                    boolean _status = this.mRemote.transact(5, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        IBinder iBinder = Stub.getDefaultImpl().getAppThread();
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

            /*
             * WARNING - Removed try catching itself - possible behaviour change.
             */
            @Override
            public IBinder getToken() throws RemoteException {
                IBinder _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.client.IVClient");
                    boolean _status = this.mRemote.transact(6, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        IBinder iBinder = Stub.getDefaultImpl().getToken();
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

            /*
             * WARNING - Removed try catching itself - possible behaviour change.
             */
            @Override
            public boolean isAppRunning() throws RemoteException {
                boolean _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.client.IVClient");
                    boolean _status = this.mRemote.transact(7, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        boolean bl = Stub.getDefaultImpl().isAppRunning();
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
            public String getDebugInfo() throws RemoteException {
                String _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.client.IVClient");
                    boolean _status = this.mRemote.transact(8, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        String string2 = Stub.getDefaultImpl().getDebugInfo();
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
            public boolean finishReceiver(IBinder token) throws RemoteException {
                boolean _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.client.IVClient");
                    _data.writeStrongBinder(token);
                    boolean _status = this.mRemote.transact(9, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        boolean bl = Stub.getDefaultImpl().finishReceiver(token);
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
            public List<ActivityManager.RunningServiceInfo> getServices() throws RemoteException {
                ArrayList _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.client.IVClient");
                    boolean _status = this.mRemote.transact(10, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        List<ActivityManager.RunningServiceInfo> list = Stub.getDefaultImpl().getServices();
                        return list;
                    }
                    _reply.readException();
                    _result = _reply.createTypedArrayList(ActivityManager.RunningServiceInfo.CREATOR);
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
    implements IVClient {
        @Override
        public void scheduleNewIntent(String creator, IBinder token, Intent intent) throws RemoteException {
        }

        @Override
        public void finishActivity(IBinder token) throws RemoteException {
        }

        @Override
        public IBinder createProxyService(ComponentName component, IBinder binder) throws RemoteException {
            return null;
        }

        @Override
        public IBinder acquireProviderClient(ProviderInfo info) throws RemoteException {
            return null;
        }

        @Override
        public IBinder getAppThread() throws RemoteException {
            return null;
        }

        @Override
        public IBinder getToken() throws RemoteException {
            return null;
        }

        @Override
        public boolean isAppRunning() throws RemoteException {
            return false;
        }

        @Override
        public String getDebugInfo() throws RemoteException {
            return null;
        }

        @Override
        public boolean finishReceiver(IBinder token) throws RemoteException {
            return false;
        }

        @Override
        public List<ActivityManager.RunningServiceInfo> getServices() throws RemoteException {
            return null;
        }

        public IBinder asBinder() {
            return null;
        }
    }
}

