/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.ActivityManager$RecentTaskInfo
 *  android.app.ActivityManager$RunningAppProcessInfo
 *  android.app.ActivityManager$RunningTaskInfo
 *  android.content.Intent
 *  android.os.Binder
 *  android.os.Bundle
 *  android.os.IBinder
 *  android.os.IInterface
 *  android.os.Parcel
 *  android.os.RemoteException
 */
package com.lody.virtual;

import android.app.ActivityManager;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.lody.virtual.StringFog;
import java.util.ArrayList;
import java.util.List;

public interface IExtHelperInterface
extends IInterface {
    public int syncPackages() throws RemoteException;

    public void cleanPackageData(int[] var1, String var2) throws RemoteException;

    public void forceStop(int[] var1) throws RemoteException;

    public List<ActivityManager.RunningTaskInfo> getRunningTasks(int var1) throws RemoteException;

    public List<ActivityManager.RecentTaskInfo> getRecentTasks(int var1, int var2) throws RemoteException;

    public List<ActivityManager.RunningAppProcessInfo> getRunningAppProcesses() throws RemoteException;

    public boolean isExternalStorageManager() throws RemoteException;

    public void startActivity(Intent var1, Bundle var2) throws RemoteException;

    public static abstract class Stub
    extends Binder
    implements IExtHelperInterface {
        private static final String DESCRIPTOR = "com.lody.virtual.IExtHelperInterface";
        static final int TRANSACTION_syncPackages = 1;
        static final int TRANSACTION_cleanPackageData = 2;
        static final int TRANSACTION_forceStop = 3;
        static final int TRANSACTION_getRunningTasks = 4;
        static final int TRANSACTION_getRecentTasks = 5;
        static final int TRANSACTION_getRunningAppProcesses = 6;
        static final int TRANSACTION_isExternalStorageManager = 7;
        static final int TRANSACTION_startActivity = 8;

        public Stub() {
            this.attachInterface(this, "com.lody.virtual.IExtHelperInterface");
        }

        public static IExtHelperInterface asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && iin instanceof IExtHelperInterface) {
                return (IExtHelperInterface)iin;
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
                    int _result = this.syncPackages();
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                }
                case 2: {
                    data.enforceInterface(descriptor);
                    int[] _arg0 = data.createIntArray();
                    String _arg1 = data.readString();
                    this.cleanPackageData(_arg0, _arg1);
                    reply.writeNoException();
                    return true;
                }
                case 3: {
                    data.enforceInterface(descriptor);
                    int[] _arg0 = data.createIntArray();
                    this.forceStop(_arg0);
                    reply.writeNoException();
                    return true;
                }
                case 4: {
                    data.enforceInterface(descriptor);
                    int _arg0 = data.readInt();
                    List _result = this.getRunningTasks(_arg0);
                    reply.writeNoException();
                    reply.writeTypedList(_result);
                    return true;
                }
                case 5: {
                    data.enforceInterface(descriptor);
                    int _arg0 = data.readInt();
                    int _arg1 = data.readInt();
                    List _result = this.getRecentTasks(_arg0, _arg1);
                    reply.writeNoException();
                    reply.writeTypedList(_result);
                    return true;
                }
                case 6: {
                    data.enforceInterface(descriptor);
                    List _result = this.getRunningAppProcesses();
                    reply.writeNoException();
                    reply.writeTypedList(_result);
                    return true;
                }
                case 7: {
                    data.enforceInterface(descriptor);
                    boolean _result = this.isExternalStorageManager();
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                }
                case 8: {
                    data.enforceInterface(descriptor);
                    Intent _arg0 = 0 != data.readInt() ? (Intent)Intent.CREATOR.createFromParcel(data) : null;
                    Bundle _arg1 = 0 != data.readInt() ? (Bundle)Bundle.CREATOR.createFromParcel(data) : null;
                    this.startActivity(_arg0, _arg1);
                    reply.writeNoException();
                    return true;
                }
            }
            return super.onTransact(code, data, reply, flags);
        }

        public static boolean setDefaultImpl(IExtHelperInterface impl) {
            if (Proxy.sDefaultImpl != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            }
            if (impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IExtHelperInterface getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }

        private static class Proxy
        implements IExtHelperInterface {
            private IBinder mRemote;
            public static IExtHelperInterface sDefaultImpl;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return "com.lody.virtual.IExtHelperInterface";
            }

            /*
             * WARNING - Removed try catching itself - possible behaviour change.
             */
            @Override
            public int syncPackages() throws RemoteException {
                int _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.IExtHelperInterface");
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        int n = Stub.getDefaultImpl().syncPackages();
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
            public void cleanPackageData(int[] userIds, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.IExtHelperInterface");
                    _data.writeIntArray(userIds);
                    _data.writeString(packageName);
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().cleanPackageData(userIds, packageName);
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
            public void forceStop(int[] pids) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.IExtHelperInterface");
                    _data.writeIntArray(pids);
                    boolean _status = this.mRemote.transact(3, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().forceStop(pids);
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
            public List<ActivityManager.RunningTaskInfo> getRunningTasks(int maxNum) throws RemoteException {
                ArrayList _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.IExtHelperInterface");
                    _data.writeInt(maxNum);
                    boolean _status = this.mRemote.transact(4, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        List<ActivityManager.RunningTaskInfo> list = Stub.getDefaultImpl().getRunningTasks(maxNum);
                        return list;
                    }
                    _reply.readException();
                    _result = _reply.createTypedArrayList(ActivityManager.RunningTaskInfo.CREATOR);
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
            public List<ActivityManager.RecentTaskInfo> getRecentTasks(int maxNum, int flags) throws RemoteException {
                ArrayList _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.IExtHelperInterface");
                    _data.writeInt(maxNum);
                    _data.writeInt(flags);
                    boolean _status = this.mRemote.transact(5, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        List<ActivityManager.RecentTaskInfo> list = Stub.getDefaultImpl().getRecentTasks(maxNum, flags);
                        return list;
                    }
                    _reply.readException();
                    _result = _reply.createTypedArrayList(ActivityManager.RecentTaskInfo.CREATOR);
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
            public List<ActivityManager.RunningAppProcessInfo> getRunningAppProcesses() throws RemoteException {
                ArrayList _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.IExtHelperInterface");
                    boolean _status = this.mRemote.transact(6, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        List<ActivityManager.RunningAppProcessInfo> list = Stub.getDefaultImpl().getRunningAppProcesses();
                        return list;
                    }
                    _reply.readException();
                    _result = _reply.createTypedArrayList(ActivityManager.RunningAppProcessInfo.CREATOR);
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
            public boolean isExternalStorageManager() throws RemoteException {
                boolean _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.IExtHelperInterface");
                    boolean _status = this.mRemote.transact(7, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        boolean bl = Stub.getDefaultImpl().isExternalStorageManager();
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
            public void startActivity(Intent intent, Bundle options) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.IExtHelperInterface");
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (options != null) {
                        _data.writeInt(1);
                        options.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(8, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().startActivity(intent, options);
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
    implements IExtHelperInterface {
        @Override
        public int syncPackages() throws RemoteException {
            return 0;
        }

        @Override
        public void cleanPackageData(int[] userIds, String packageName) throws RemoteException {
        }

        @Override
        public void forceStop(int[] pids) throws RemoteException {
        }

        @Override
        public List<ActivityManager.RunningTaskInfo> getRunningTasks(int maxNum) throws RemoteException {
            return null;
        }

        @Override
        public List<ActivityManager.RecentTaskInfo> getRecentTasks(int maxNum, int flags) throws RemoteException {
            return null;
        }

        @Override
        public List<ActivityManager.RunningAppProcessInfo> getRunningAppProcesses() throws RemoteException {
            return null;
        }

        @Override
        public boolean isExternalStorageManager() throws RemoteException {
            return false;
        }

        @Override
        public void startActivity(Intent intent, Bundle options) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }
}

