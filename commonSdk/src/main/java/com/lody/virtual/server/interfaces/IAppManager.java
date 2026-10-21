/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.net.Uri
 *  android.os.Binder
 *  android.os.IBinder
 *  android.os.IInterface
 *  android.os.Parcel
 *  android.os.RemoteException
 */
package com.lody.virtual.server.interfaces;

import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.lody.virtual.StringFog;
import com.lody.virtual.remote.InstalledAppInfo;
import com.lody.virtual.remote.VAppInstallerParams;
import com.lody.virtual.remote.VAppInstallerResult;
import com.lody.virtual.server.interfaces.IPackageObserver;
import java.util.ArrayList;
import java.util.List;

public interface IAppManager
extends IInterface {
    public int[] getPackageInstalledUsers(String var1) throws RemoteException;

    public void scanApps() throws RemoteException;

    public int getUidForSharedUser(String var1) throws RemoteException;

    public InstalledAppInfo getInstalledAppInfo(String var1, int var2) throws RemoteException;

    public VAppInstallerResult installPackage(Uri var1, VAppInstallerParams var2) throws RemoteException;

    public boolean isPackageLaunched(int var1, String var2) throws RemoteException;

    public void setPackageHidden(int var1, String var2, boolean var3) throws RemoteException;

    public boolean installPackageAsUser(int var1, String var2) throws RemoteException;

    public boolean uninstallPackageAsUser(String var1, int var2) throws RemoteException;

    public boolean uninstallPackage(String var1) throws RemoteException;

    public List<InstalledAppInfo> getInstalledApps(int var1) throws RemoteException;

    public List<InstalledAppInfo> getInstalledAppsAsUser(int var1, int var2) throws RemoteException;

    public List<String> getInstalledSplitNames(String var1) throws RemoteException;

    public int getInstalledAppCount() throws RemoteException;

    public boolean isAppInstalled(String var1) throws RemoteException;

    public boolean isAppInstalledAsUser(int var1, String var2) throws RemoteException;

    public void registerObserver(IPackageObserver var1) throws RemoteException;

    public void unregisterObserver(IPackageObserver var1) throws RemoteException;

    public boolean isRunInExtProcess(String var1) throws RemoteException;

    public boolean cleanPackageData(String var1, int var2) throws RemoteException;

    public static abstract class Stub
    extends Binder
    implements IAppManager {
        private static final String DESCRIPTOR = "com.lody.virtual.server.interfaces.IAppManager";
        static final int TRANSACTION_getPackageInstalledUsers = 1;
        static final int TRANSACTION_scanApps = 2;
        static final int TRANSACTION_getUidForSharedUser = 3;
        static final int TRANSACTION_getInstalledAppInfo = 4;
        static final int TRANSACTION_installPackage = 5;
        static final int TRANSACTION_isPackageLaunched = 6;
        static final int TRANSACTION_setPackageHidden = 7;
        static final int TRANSACTION_installPackageAsUser = 8;
        static final int TRANSACTION_uninstallPackageAsUser = 9;
        static final int TRANSACTION_uninstallPackage = 10;
        static final int TRANSACTION_getInstalledApps = 11;
        static final int TRANSACTION_getInstalledAppsAsUser = 12;
        static final int TRANSACTION_getInstalledSplitNames = 13;
        static final int TRANSACTION_getInstalledAppCount = 14;
        static final int TRANSACTION_isAppInstalled = 15;
        static final int TRANSACTION_isAppInstalledAsUser = 16;
        static final int TRANSACTION_registerObserver = 17;
        static final int TRANSACTION_unregisterObserver = 18;
        static final int TRANSACTION_isRunInExtProcess = 19;
        static final int TRANSACTION_cleanPackageData = 20;

        public Stub() {
            this.attachInterface(this, "com.lody.virtual.server.interfaces.IAppManager");
        }

        public static IAppManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && iin instanceof IAppManager) {
                return (IAppManager)iin;
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
                    int[] _result = this.getPackageInstalledUsers(_arg0);
                    reply.writeNoException();
                    reply.writeIntArray(_result);
                    return true;
                }
                case 2: {
                    data.enforceInterface(descriptor);
                    this.scanApps();
                    reply.writeNoException();
                    return true;
                }
                case 3: {
                    data.enforceInterface(descriptor);
                    String _arg0 = data.readString();
                    int _result = this.getUidForSharedUser(_arg0);
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                }
                case 4: {
                    data.enforceInterface(descriptor);
                    String _arg0 = data.readString();
                    int _arg1 = data.readInt();
                    InstalledAppInfo _result = this.getInstalledAppInfo(_arg0, _arg1);
                    reply.writeNoException();
                    if (_result != null) {
                        reply.writeInt(1);
                        _result.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                }
                case 5: {
                    data.enforceInterface(descriptor);
                    Uri _arg0 = 0 != data.readInt() ? (Uri)Uri.CREATOR.createFromParcel(data) : null;
                    VAppInstallerParams _arg1 = 0 != data.readInt() ? (VAppInstallerParams)VAppInstallerParams.CREATOR.createFromParcel(data) : null;
                    VAppInstallerResult _result = this.installPackage(_arg0, _arg1);
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
                    int _arg0 = data.readInt();
                    String _arg1 = data.readString();
                    boolean _result = this.isPackageLaunched(_arg0, _arg1);
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                }
                case 7: {
                    data.enforceInterface(descriptor);
                    int _arg0 = data.readInt();
                    String _arg1 = data.readString();
                    boolean _arg2 = 0 != data.readInt();
                    this.setPackageHidden(_arg0, _arg1, _arg2);
                    reply.writeNoException();
                    return true;
                }
                case 8: {
                    data.enforceInterface(descriptor);
                    int _arg0 = data.readInt();
                    String _arg1 = data.readString();
                    boolean _result = this.installPackageAsUser(_arg0, _arg1);
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                }
                case 9: {
                    data.enforceInterface(descriptor);
                    String _arg0 = data.readString();
                    int _arg1 = data.readInt();
                    boolean _result = this.uninstallPackageAsUser(_arg0, _arg1);
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                }
                case 10: {
                    data.enforceInterface(descriptor);
                    String _arg0 = data.readString();
                    boolean _result = this.uninstallPackage(_arg0);
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                }
                case 11: {
                    data.enforceInterface(descriptor);
                    int _arg0 = data.readInt();
                    List _result = this.getInstalledApps(_arg0);
                    reply.writeNoException();
                    reply.writeTypedList(_result);
                    return true;
                }
                case 12: {
                    data.enforceInterface(descriptor);
                    int _arg0 = data.readInt();
                    int _arg1 = data.readInt();
                    List _result = this.getInstalledAppsAsUser(_arg0, _arg1);
                    reply.writeNoException();
                    reply.writeTypedList(_result);
                    return true;
                }
                case 13: {
                    data.enforceInterface(descriptor);
                    String _arg0 = data.readString();
                    List _result = this.getInstalledSplitNames(_arg0);
                    reply.writeNoException();
                    reply.writeStringList(_result);
                    return true;
                }
                case 14: {
                    data.enforceInterface(descriptor);
                    int _result = this.getInstalledAppCount();
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                }
                case 15: {
                    data.enforceInterface(descriptor);
                    String _arg0 = data.readString();
                    boolean _result = this.isAppInstalled(_arg0);
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                }
                case 16: {
                    data.enforceInterface(descriptor);
                    int _arg0 = data.readInt();
                    String _arg1 = data.readString();
                    boolean _result = this.isAppInstalledAsUser(_arg0, _arg1);
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                }
                case 17: {
                    data.enforceInterface(descriptor);
                    IPackageObserver _arg0 = IPackageObserver.Stub.asInterface(data.readStrongBinder());
                    this.registerObserver(_arg0);
                    reply.writeNoException();
                    return true;
                }
                case 18: {
                    data.enforceInterface(descriptor);
                    IPackageObserver _arg0 = IPackageObserver.Stub.asInterface(data.readStrongBinder());
                    this.unregisterObserver(_arg0);
                    reply.writeNoException();
                    return true;
                }
                case 19: {
                    data.enforceInterface(descriptor);
                    String _arg0 = data.readString();
                    boolean _result = this.isRunInExtProcess(_arg0);
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                }
                case 20: {
                    data.enforceInterface(descriptor);
                    String _arg0 = data.readString();
                    int _arg1 = data.readInt();
                    boolean _result = this.cleanPackageData(_arg0, _arg1);
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                }
            }
            return super.onTransact(code, data, reply, flags);
        }

        public static boolean setDefaultImpl(IAppManager impl) {
            if (Proxy.sDefaultImpl != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            }
            if (impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IAppManager getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }

        private static class Proxy
        implements IAppManager {
            private IBinder mRemote;
            public static IAppManager sDefaultImpl;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return "com.lody.virtual.server.interfaces.IAppManager";
            }

            /*
             * WARNING - Removed try catching itself - possible behaviour change.
             */
            @Override
            public int[] getPackageInstalledUsers(String packageName) throws RemoteException {
                int[] _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IAppManager");
                    _data.writeString(packageName);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        int[] nArray = Stub.getDefaultImpl().getPackageInstalledUsers(packageName);
                        return nArray;
                    }
                    _reply.readException();
                    _result = _reply.createIntArray();
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
            public void scanApps() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IAppManager");
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().scanApps();
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
            public int getUidForSharedUser(String sharedUserName) throws RemoteException {
                int _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IAppManager");
                    _data.writeString(sharedUserName);
                    boolean _status = this.mRemote.transact(3, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        int n = Stub.getDefaultImpl().getUidForSharedUser(sharedUserName);
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
            public InstalledAppInfo getInstalledAppInfo(String pkg, int flags) throws RemoteException {
                InstalledAppInfo _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IAppManager");
                    _data.writeString(pkg);
                    _data.writeInt(flags);
                    boolean _status = this.mRemote.transact(4, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        InstalledAppInfo installedAppInfo = Stub.getDefaultImpl().getInstalledAppInfo(pkg, flags);
                        return installedAppInfo;
                    }
                    _reply.readException();
                    _result = 0 != _reply.readInt() ? (InstalledAppInfo)InstalledAppInfo.CREATOR.createFromParcel(_reply) : null;
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
            public VAppInstallerResult installPackage(Uri uri, VAppInstallerParams params) throws RemoteException {
                VAppInstallerResult _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IAppManager");
                    if (uri != null) {
                        _data.writeInt(1);
                        uri.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (params != null) {
                        _data.writeInt(1);
                        params.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(5, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        VAppInstallerResult vAppInstallerResult = Stub.getDefaultImpl().installPackage(uri, params);
                        return vAppInstallerResult;
                    }
                    _reply.readException();
                    _result = 0 != _reply.readInt() ? (VAppInstallerResult)VAppInstallerResult.CREATOR.createFromParcel(_reply) : null;
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
            public boolean isPackageLaunched(int userId, String packageName) throws RemoteException {
                boolean _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IAppManager");
                    _data.writeInt(userId);
                    _data.writeString(packageName);
                    boolean _status = this.mRemote.transact(6, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        boolean bl = Stub.getDefaultImpl().isPackageLaunched(userId, packageName);
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
            public void setPackageHidden(int userId, String packageName, boolean hidden) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IAppManager");
                    _data.writeInt(userId);
                    _data.writeString(packageName);
                    _data.writeInt(hidden ? 1 : 0);
                    boolean _status = this.mRemote.transact(7, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setPackageHidden(userId, packageName, hidden);
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
            public boolean installPackageAsUser(int userId, String packageName) throws RemoteException {
                boolean _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IAppManager");
                    _data.writeInt(userId);
                    _data.writeString(packageName);
                    boolean _status = this.mRemote.transact(8, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        boolean bl = Stub.getDefaultImpl().installPackageAsUser(userId, packageName);
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
            public boolean uninstallPackageAsUser(String packageName, int userId) throws RemoteException {
                boolean _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IAppManager");
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(9, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        boolean bl = Stub.getDefaultImpl().uninstallPackageAsUser(packageName, userId);
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
            public boolean uninstallPackage(String packageName) throws RemoteException {
                boolean _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IAppManager");
                    _data.writeString(packageName);
                    boolean _status = this.mRemote.transact(10, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        boolean bl = Stub.getDefaultImpl().uninstallPackage(packageName);
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
            public List<InstalledAppInfo> getInstalledApps(int flags) throws RemoteException {
                ArrayList _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IAppManager");
                    _data.writeInt(flags);
                    boolean _status = this.mRemote.transact(11, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        List<InstalledAppInfo> list = Stub.getDefaultImpl().getInstalledApps(flags);
                        return list;
                    }
                    _reply.readException();
                    _result = _reply.createTypedArrayList(InstalledAppInfo.CREATOR);
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
            public List<InstalledAppInfo> getInstalledAppsAsUser(int userId, int flags) throws RemoteException {
                ArrayList _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IAppManager");
                    _data.writeInt(userId);
                    _data.writeInt(flags);
                    boolean _status = this.mRemote.transact(12, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        List<InstalledAppInfo> list = Stub.getDefaultImpl().getInstalledAppsAsUser(userId, flags);
                        return list;
                    }
                    _reply.readException();
                    _result = _reply.createTypedArrayList(InstalledAppInfo.CREATOR);
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
            public List<String> getInstalledSplitNames(String pkg) throws RemoteException {
                ArrayList _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IAppManager");
                    _data.writeString(pkg);
                    boolean _status = this.mRemote.transact(13, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        List<String> list = Stub.getDefaultImpl().getInstalledSplitNames(pkg);
                        return list;
                    }
                    _reply.readException();
                    _result = _reply.createStringArrayList();
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
            public int getInstalledAppCount() throws RemoteException {
                int _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IAppManager");
                    boolean _status = this.mRemote.transact(14, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        int n = Stub.getDefaultImpl().getInstalledAppCount();
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
            public boolean isAppInstalled(String packageName) throws RemoteException {
                boolean _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IAppManager");
                    _data.writeString(packageName);
                    boolean _status = this.mRemote.transact(15, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        boolean bl = Stub.getDefaultImpl().isAppInstalled(packageName);
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
            public boolean isAppInstalledAsUser(int userId, String packageName) throws RemoteException {
                boolean _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IAppManager");
                    _data.writeInt(userId);
                    _data.writeString(packageName);
                    boolean _status = this.mRemote.transact(16, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        boolean bl = Stub.getDefaultImpl().isAppInstalledAsUser(userId, packageName);
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
            public void registerObserver(IPackageObserver observer) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IAppManager");
                    _data.writeStrongBinder(observer != null ? observer.asBinder() : null);
                    boolean _status = this.mRemote.transact(17, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().registerObserver(observer);
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
            public void unregisterObserver(IPackageObserver observer) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IAppManager");
                    _data.writeStrongBinder(observer != null ? observer.asBinder() : null);
                    boolean _status = this.mRemote.transact(18, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().unregisterObserver(observer);
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
            public boolean isRunInExtProcess(String packageName) throws RemoteException {
                boolean _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IAppManager");
                    _data.writeString(packageName);
                    boolean _status = this.mRemote.transact(19, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        boolean bl = Stub.getDefaultImpl().isRunInExtProcess(packageName);
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
            public boolean cleanPackageData(String pkg, int userId) throws RemoteException {
                boolean _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IAppManager");
                    _data.writeString(pkg);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(20, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        boolean bl = Stub.getDefaultImpl().cleanPackageData(pkg, userId);
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
    implements IAppManager {
        @Override
        public int[] getPackageInstalledUsers(String packageName) throws RemoteException {
            return null;
        }

        @Override
        public void scanApps() throws RemoteException {
        }

        @Override
        public int getUidForSharedUser(String sharedUserName) throws RemoteException {
            return 0;
        }

        @Override
        public InstalledAppInfo getInstalledAppInfo(String pkg, int flags) throws RemoteException {
            return null;
        }

        @Override
        public VAppInstallerResult installPackage(Uri uri, VAppInstallerParams params) throws RemoteException {
            return null;
        }

        @Override
        public boolean isPackageLaunched(int userId, String packageName) throws RemoteException {
            return false;
        }

        @Override
        public void setPackageHidden(int userId, String packageName, boolean hidden) throws RemoteException {
        }

        @Override
        public boolean installPackageAsUser(int userId, String packageName) throws RemoteException {
            return false;
        }

        @Override
        public boolean uninstallPackageAsUser(String packageName, int userId) throws RemoteException {
            return false;
        }

        @Override
        public boolean uninstallPackage(String packageName) throws RemoteException {
            return false;
        }

        @Override
        public List<InstalledAppInfo> getInstalledApps(int flags) throws RemoteException {
            return null;
        }

        @Override
        public List<InstalledAppInfo> getInstalledAppsAsUser(int userId, int flags) throws RemoteException {
            return null;
        }

        @Override
        public List<String> getInstalledSplitNames(String pkg) throws RemoteException {
            return null;
        }

        @Override
        public int getInstalledAppCount() throws RemoteException {
            return 0;
        }

        @Override
        public boolean isAppInstalled(String packageName) throws RemoteException {
            return false;
        }

        @Override
        public boolean isAppInstalledAsUser(int userId, String packageName) throws RemoteException {
            return false;
        }

        @Override
        public void registerObserver(IPackageObserver observer) throws RemoteException {
        }

        @Override
        public void unregisterObserver(IPackageObserver observer) throws RemoteException {
        }

        @Override
        public boolean isRunInExtProcess(String packageName) throws RemoteException {
            return false;
        }

        @Override
        public boolean cleanPackageData(String pkg, int userId) throws RemoteException {
            return false;
        }

        public IBinder asBinder() {
            return null;
        }
    }
}

