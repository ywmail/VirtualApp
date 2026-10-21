/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.ComponentName
 *  android.content.Intent
 *  android.content.pm.ActivityInfo
 *  android.content.pm.ApplicationInfo
 *  android.content.pm.PackageInfo
 *  android.content.pm.PermissionGroupInfo
 *  android.content.pm.PermissionInfo
 *  android.content.pm.ProviderInfo
 *  android.content.pm.ResolveInfo
 *  android.content.pm.ServiceInfo
 *  android.os.Binder
 *  android.os.IBinder
 *  android.os.IInterface
 *  android.os.Parcel
 *  android.os.RemoteException
 */
package com.lody.virtual.server.interfaces;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PermissionGroupInfo;
import android.content.pm.PermissionInfo;
import android.content.pm.ProviderInfo;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.lody.virtual.StringFog;
import com.lody.virtual.remote.ReceiverInfo;
import com.lody.virtual.remote.VParceledListSlice;
import java.util.ArrayList;
import java.util.List;

public interface IPackageManager
extends IInterface {
    public int getPackageUid(String var1, int var2) throws RemoteException;

    public String[] getPackagesForUid(int var1) throws RemoteException;

    public List<String> getSharedLibraries(String var1) throws RemoteException;

    public int checkPermission(boolean var1, String var2, String var3, int var4) throws RemoteException;

    public PackageInfo getPackageInfo(String var1, int var2, int var3) throws RemoteException;

    public ActivityInfo getActivityInfo(ComponentName var1, int var2, int var3) throws RemoteException;

    public boolean activitySupportsIntent(ComponentName var1, Intent var2, String var3) throws RemoteException;

    public ActivityInfo getReceiverInfo(ComponentName var1, int var2, int var3) throws RemoteException;

    public ServiceInfo getServiceInfo(ComponentName var1, int var2, int var3) throws RemoteException;

    public ProviderInfo getProviderInfo(ComponentName var1, int var2, int var3) throws RemoteException;

    public ResolveInfo resolveIntent(Intent var1, String var2, int var3, int var4) throws RemoteException;

    public List<ResolveInfo> queryIntentActivities(Intent var1, String var2, int var3, int var4) throws RemoteException;

    public List<ResolveInfo> queryIntentReceivers(Intent var1, String var2, int var3, int var4) throws RemoteException;

    public ResolveInfo resolveService(Intent var1, String var2, int var3, int var4) throws RemoteException;

    public List<ResolveInfo> queryIntentServices(Intent var1, String var2, int var3, int var4) throws RemoteException;

    public List<ResolveInfo> queryIntentContentProviders(Intent var1, String var2, int var3, int var4) throws RemoteException;

    public VParceledListSlice getInstalledPackages(int var1, int var2) throws RemoteException;

    public VParceledListSlice getInstalledApplications(int var1, int var2) throws RemoteException;

    public List<ReceiverInfo> getReceiverInfos(String var1, String var2, int var3) throws RemoteException;

    public PermissionInfo getPermissionInfo(String var1, int var2) throws RemoteException;

    public List<PermissionInfo> queryPermissionsByGroup(String var1, int var2) throws RemoteException;

    public PermissionGroupInfo getPermissionGroupInfo(String var1, int var2) throws RemoteException;

    public List<PermissionGroupInfo> getAllPermissionGroups(int var1) throws RemoteException;

    public ProviderInfo resolveContentProvider(String var1, int var2, int var3) throws RemoteException;

    public ApplicationInfo getApplicationInfo(String var1, int var2, int var3) throws RemoteException;

    public VParceledListSlice queryContentProviders(String var1, int var2, int var3) throws RemoteException;

    public List<String> querySharedPackages(String var1) throws RemoteException;

    public String getNameForUid(int var1) throws RemoteException;

    public IBinder getPackageInstaller() throws RemoteException;

    public int checkSignatures(String var1, String var2) throws RemoteException;

    public String[] getDangerousPermissions(String var1) throws RemoteException;

    public void setComponentEnabledSetting(ComponentName var1, int var2, int var3, int var4) throws RemoteException;

    public int getComponentEnabledSetting(ComponentName var1, int var2) throws RemoteException;

    public static abstract class Stub
    extends Binder
    implements IPackageManager {
        private static final String DESCRIPTOR = "com.lody.virtual.server.interfaces.IPackageManager";
        static final int TRANSACTION_getPackageUid = 1;
        static final int TRANSACTION_getPackagesForUid = 2;
        static final int TRANSACTION_getSharedLibraries = 3;
        static final int TRANSACTION_checkPermission = 4;
        static final int TRANSACTION_getPackageInfo = 5;
        static final int TRANSACTION_getActivityInfo = 6;
        static final int TRANSACTION_activitySupportsIntent = 7;
        static final int TRANSACTION_getReceiverInfo = 8;
        static final int TRANSACTION_getServiceInfo = 9;
        static final int TRANSACTION_getProviderInfo = 10;
        static final int TRANSACTION_resolveIntent = 11;
        static final int TRANSACTION_queryIntentActivities = 12;
        static final int TRANSACTION_queryIntentReceivers = 13;
        static final int TRANSACTION_resolveService = 14;
        static final int TRANSACTION_queryIntentServices = 15;
        static final int TRANSACTION_queryIntentContentProviders = 16;
        static final int TRANSACTION_getInstalledPackages = 17;
        static final int TRANSACTION_getInstalledApplications = 18;
        static final int TRANSACTION_getReceiverInfos = 19;
        static final int TRANSACTION_getPermissionInfo = 20;
        static final int TRANSACTION_queryPermissionsByGroup = 21;
        static final int TRANSACTION_getPermissionGroupInfo = 22;
        static final int TRANSACTION_getAllPermissionGroups = 23;
        static final int TRANSACTION_resolveContentProvider = 24;
        static final int TRANSACTION_getApplicationInfo = 25;
        static final int TRANSACTION_queryContentProviders = 26;
        static final int TRANSACTION_querySharedPackages = 27;
        static final int TRANSACTION_getNameForUid = 28;
        static final int TRANSACTION_getPackageInstaller = 29;
        static final int TRANSACTION_checkSignatures = 30;
        static final int TRANSACTION_getDangerousPermissions = 31;
        static final int TRANSACTION_setComponentEnabledSetting = 32;
        static final int TRANSACTION_getComponentEnabledSetting = 33;

        public Stub() {
            this.attachInterface(this, "com.lody.virtual.server.interfaces.IPackageManager");
        }

        public static IPackageManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && iin instanceof IPackageManager) {
                return (IPackageManager)iin;
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
                    int _result = this.getPackageUid(_arg0, _arg1);
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                }
                case 2: {
                    data.enforceInterface(descriptor);
                    int _arg0 = data.readInt();
                    String[] _result = this.getPackagesForUid(_arg0);
                    reply.writeNoException();
                    reply.writeStringArray(_result);
                    return true;
                }
                case 3: {
                    data.enforceInterface(descriptor);
                    String _arg0 = data.readString();
                    List _result = this.getSharedLibraries(_arg0);
                    reply.writeNoException();
                    reply.writeStringList(_result);
                    return true;
                }
                case 4: {
                    data.enforceInterface(descriptor);
                    boolean _arg0 = 0 != data.readInt();
                    String _arg1 = data.readString();
                    String _arg2 = data.readString();
                    int _arg3 = data.readInt();
                    int _result = this.checkPermission(_arg0, _arg1, _arg2, _arg3);
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                }
                case 5: {
                    data.enforceInterface(descriptor);
                    String _arg0 = data.readString();
                    int _arg1 = data.readInt();
                    int _arg2 = data.readInt();
                    PackageInfo _result = this.getPackageInfo(_arg0, _arg1, _arg2);
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
                    ComponentName _arg0 = 0 != data.readInt() ? (ComponentName)ComponentName.CREATOR.createFromParcel(data) : null;
                    int _arg1 = data.readInt();
                    int _arg2 = data.readInt();
                    ActivityInfo _result = this.getActivityInfo(_arg0, _arg1, _arg2);
                    reply.writeNoException();
                    if (_result != null) {
                        reply.writeInt(1);
                        _result.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                }
                case 7: {
                    data.enforceInterface(descriptor);
                    ComponentName _arg0 = 0 != data.readInt() ? (ComponentName)ComponentName.CREATOR.createFromParcel(data) : null;
                    Intent _arg1 = 0 != data.readInt() ? (Intent)Intent.CREATOR.createFromParcel(data) : null;
                    String _arg2 = data.readString();
                    boolean _result = this.activitySupportsIntent(_arg0, _arg1, _arg2);
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                }
                case 8: {
                    data.enforceInterface(descriptor);
                    ComponentName _arg0 = 0 != data.readInt() ? (ComponentName)ComponentName.CREATOR.createFromParcel(data) : null;
                    int _arg1 = data.readInt();
                    int _arg2 = data.readInt();
                    ActivityInfo _result = this.getReceiverInfo(_arg0, _arg1, _arg2);
                    reply.writeNoException();
                    if (_result != null) {
                        reply.writeInt(1);
                        _result.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                }
                case 9: {
                    data.enforceInterface(descriptor);
                    ComponentName _arg0 = 0 != data.readInt() ? (ComponentName)ComponentName.CREATOR.createFromParcel(data) : null;
                    int _arg1 = data.readInt();
                    int _arg2 = data.readInt();
                    ServiceInfo _result = this.getServiceInfo(_arg0, _arg1, _arg2);
                    reply.writeNoException();
                    if (_result != null) {
                        reply.writeInt(1);
                        _result.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                }
                case 10: {
                    data.enforceInterface(descriptor);
                    ComponentName _arg0 = 0 != data.readInt() ? (ComponentName)ComponentName.CREATOR.createFromParcel(data) : null;
                    int _arg1 = data.readInt();
                    int _arg2 = data.readInt();
                    ProviderInfo _result = this.getProviderInfo(_arg0, _arg1, _arg2);
                    reply.writeNoException();
                    if (_result != null) {
                        reply.writeInt(1);
                        _result.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                }
                case 11: {
                    data.enforceInterface(descriptor);
                    Intent _arg0 = 0 != data.readInt() ? (Intent)Intent.CREATOR.createFromParcel(data) : null;
                    String _arg1 = data.readString();
                    int _arg2 = data.readInt();
                    int _arg3 = data.readInt();
                    ResolveInfo _result = this.resolveIntent(_arg0, _arg1, _arg2, _arg3);
                    reply.writeNoException();
                    if (_result != null) {
                        reply.writeInt(1);
                        _result.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                }
                case 12: {
                    data.enforceInterface(descriptor);
                    Intent _arg0 = 0 != data.readInt() ? (Intent)Intent.CREATOR.createFromParcel(data) : null;
                    String _arg1 = data.readString();
                    int _arg2 = data.readInt();
                    int _arg3 = data.readInt();
                    List _result = this.queryIntentActivities(_arg0, _arg1, _arg2, _arg3);
                    reply.writeNoException();
                    reply.writeTypedList(_result);
                    return true;
                }
                case 13: {
                    data.enforceInterface(descriptor);
                    Intent _arg0 = 0 != data.readInt() ? (Intent)Intent.CREATOR.createFromParcel(data) : null;
                    String _arg1 = data.readString();
                    int _arg2 = data.readInt();
                    int _arg3 = data.readInt();
                    List _result = this.queryIntentReceivers(_arg0, _arg1, _arg2, _arg3);
                    reply.writeNoException();
                    reply.writeTypedList(_result);
                    return true;
                }
                case 14: {
                    data.enforceInterface(descriptor);
                    Intent _arg0 = 0 != data.readInt() ? (Intent)Intent.CREATOR.createFromParcel(data) : null;
                    String _arg1 = data.readString();
                    int _arg2 = data.readInt();
                    int _arg3 = data.readInt();
                    ResolveInfo _result = this.resolveService(_arg0, _arg1, _arg2, _arg3);
                    reply.writeNoException();
                    if (_result != null) {
                        reply.writeInt(1);
                        _result.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                }
                case 15: {
                    data.enforceInterface(descriptor);
                    Intent _arg0 = 0 != data.readInt() ? (Intent)Intent.CREATOR.createFromParcel(data) : null;
                    String _arg1 = data.readString();
                    int _arg2 = data.readInt();
                    int _arg3 = data.readInt();
                    List _result = this.queryIntentServices(_arg0, _arg1, _arg2, _arg3);
                    reply.writeNoException();
                    reply.writeTypedList(_result);
                    return true;
                }
                case 16: {
                    data.enforceInterface(descriptor);
                    Intent _arg0 = 0 != data.readInt() ? (Intent)Intent.CREATOR.createFromParcel(data) : null;
                    String _arg1 = data.readString();
                    int _arg2 = data.readInt();
                    int _arg3 = data.readInt();
                    List _result = this.queryIntentContentProviders(_arg0, _arg1, _arg2, _arg3);
                    reply.writeNoException();
                    reply.writeTypedList(_result);
                    return true;
                }
                case 17: {
                    data.enforceInterface(descriptor);
                    int _arg0 = data.readInt();
                    int _arg1 = data.readInt();
                    VParceledListSlice _result = this.getInstalledPackages(_arg0, _arg1);
                    reply.writeNoException();
                    if (_result != null) {
                        reply.writeInt(1);
                        _result.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                }
                case 18: {
                    data.enforceInterface(descriptor);
                    int _arg0 = data.readInt();
                    int _arg1 = data.readInt();
                    VParceledListSlice _result = this.getInstalledApplications(_arg0, _arg1);
                    reply.writeNoException();
                    if (_result != null) {
                        reply.writeInt(1);
                        _result.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                }
                case 19: {
                    data.enforceInterface(descriptor);
                    String _arg0 = data.readString();
                    String _arg1 = data.readString();
                    int _arg2 = data.readInt();
                    List _result = this.getReceiverInfos(_arg0, _arg1, _arg2);
                    reply.writeNoException();
                    reply.writeTypedList(_result);
                    return true;
                }
                case 20: {
                    data.enforceInterface(descriptor);
                    String _arg0 = data.readString();
                    int _arg1 = data.readInt();
                    PermissionInfo _result = this.getPermissionInfo(_arg0, _arg1);
                    reply.writeNoException();
                    if (_result != null) {
                        reply.writeInt(1);
                        _result.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                }
                case 21: {
                    data.enforceInterface(descriptor);
                    String _arg0 = data.readString();
                    int _arg1 = data.readInt();
                    List _result = this.queryPermissionsByGroup(_arg0, _arg1);
                    reply.writeNoException();
                    reply.writeTypedList(_result);
                    return true;
                }
                case 22: {
                    data.enforceInterface(descriptor);
                    String _arg0 = data.readString();
                    int _arg1 = data.readInt();
                    PermissionGroupInfo _result = this.getPermissionGroupInfo(_arg0, _arg1);
                    reply.writeNoException();
                    if (_result != null) {
                        reply.writeInt(1);
                        _result.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                }
                case 23: {
                    data.enforceInterface(descriptor);
                    int _arg0 = data.readInt();
                    List _result = this.getAllPermissionGroups(_arg0);
                    reply.writeNoException();
                    reply.writeTypedList(_result);
                    return true;
                }
                case 24: {
                    data.enforceInterface(descriptor);
                    String _arg0 = data.readString();
                    int _arg1 = data.readInt();
                    int _arg2 = data.readInt();
                    ProviderInfo _result = this.resolveContentProvider(_arg0, _arg1, _arg2);
                    reply.writeNoException();
                    if (_result != null) {
                        reply.writeInt(1);
                        _result.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                }
                case 25: {
                    data.enforceInterface(descriptor);
                    String _arg0 = data.readString();
                    int _arg1 = data.readInt();
                    int _arg2 = data.readInt();
                    ApplicationInfo _result = this.getApplicationInfo(_arg0, _arg1, _arg2);
                    reply.writeNoException();
                    if (_result != null) {
                        reply.writeInt(1);
                        _result.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                }
                case 26: {
                    data.enforceInterface(descriptor);
                    String _arg0 = data.readString();
                    int _arg1 = data.readInt();
                    int _arg2 = data.readInt();
                    VParceledListSlice _result = this.queryContentProviders(_arg0, _arg1, _arg2);
                    reply.writeNoException();
                    if (_result != null) {
                        reply.writeInt(1);
                        _result.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                }
                case 27: {
                    data.enforceInterface(descriptor);
                    String _arg0 = data.readString();
                    List _result = this.querySharedPackages(_arg0);
                    reply.writeNoException();
                    reply.writeStringList(_result);
                    return true;
                }
                case 28: {
                    data.enforceInterface(descriptor);
                    int _arg0 = data.readInt();
                    String _result = this.getNameForUid(_arg0);
                    reply.writeNoException();
                    reply.writeString(_result);
                    return true;
                }
                case 29: {
                    data.enforceInterface(descriptor);
                    IBinder _result = this.getPackageInstaller();
                    reply.writeNoException();
                    reply.writeStrongBinder(_result);
                    return true;
                }
                case 30: {
                    data.enforceInterface(descriptor);
                    String _arg0 = data.readString();
                    String _arg1 = data.readString();
                    int _result = this.checkSignatures(_arg0, _arg1);
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                }
                case 31: {
                    data.enforceInterface(descriptor);
                    String _arg0 = data.readString();
                    String[] _result = this.getDangerousPermissions(_arg0);
                    reply.writeNoException();
                    reply.writeStringArray(_result);
                    return true;
                }
                case 32: {
                    data.enforceInterface(descriptor);
                    ComponentName _arg0 = 0 != data.readInt() ? (ComponentName)ComponentName.CREATOR.createFromParcel(data) : null;
                    int _arg1 = data.readInt();
                    int _arg2 = data.readInt();
                    int _arg3 = data.readInt();
                    this.setComponentEnabledSetting(_arg0, _arg1, _arg2, _arg3);
                    reply.writeNoException();
                    return true;
                }
                case 33: {
                    data.enforceInterface(descriptor);
                    ComponentName _arg0 = 0 != data.readInt() ? (ComponentName)ComponentName.CREATOR.createFromParcel(data) : null;
                    int _arg1 = data.readInt();
                    int _result = this.getComponentEnabledSetting(_arg0, _arg1);
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                }
            }
            return super.onTransact(code, data, reply, flags);
        }

        public static boolean setDefaultImpl(IPackageManager impl) {
            if (Proxy.sDefaultImpl != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            }
            if (impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IPackageManager getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }

        private static class Proxy
        implements IPackageManager {
            private IBinder mRemote;
            public static IPackageManager sDefaultImpl;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return "com.lody.virtual.server.interfaces.IPackageManager";
            }

            /*
             * WARNING - Removed try catching itself - possible behaviour change.
             */
            @Override
            public int getPackageUid(String packageName, int userId) throws RemoteException {
                int _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IPackageManager");
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        int n = Stub.getDefaultImpl().getPackageUid(packageName, userId);
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
            public String[] getPackagesForUid(int vuid) throws RemoteException {
                String[] _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IPackageManager");
                    _data.writeInt(vuid);
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        String[] stringArray = Stub.getDefaultImpl().getPackagesForUid(vuid);
                        return stringArray;
                    }
                    _reply.readException();
                    _result = _reply.createStringArray();
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
            public List<String> getSharedLibraries(String pkgName) throws RemoteException {
                ArrayList _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IPackageManager");
                    _data.writeString(pkgName);
                    boolean _status = this.mRemote.transact(3, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        List<String> list = Stub.getDefaultImpl().getSharedLibraries(pkgName);
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
            public int checkPermission(boolean isExt, String permName, String pkgName, int userId) throws RemoteException {
                int _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IPackageManager");
                    _data.writeInt(isExt ? 1 : 0);
                    _data.writeString(permName);
                    _data.writeString(pkgName);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(4, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        int n = Stub.getDefaultImpl().checkPermission(isExt, permName, pkgName, userId);
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
            public PackageInfo getPackageInfo(String packageName, int flags, int userId) throws RemoteException {
                PackageInfo _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IPackageManager");
                    _data.writeString(packageName);
                    _data.writeInt(flags);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(5, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        PackageInfo packageInfo = Stub.getDefaultImpl().getPackageInfo(packageName, flags, userId);
                        return packageInfo;
                    }
                    _reply.readException();
                    _result = 0 != _reply.readInt() ? (PackageInfo)PackageInfo.CREATOR.createFromParcel(_reply) : null;
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
            public ActivityInfo getActivityInfo(ComponentName componentName, int flags, int userId) throws RemoteException {
                ActivityInfo _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IPackageManager");
                    if (componentName != null) {
                        _data.writeInt(1);
                        componentName.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(flags);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(6, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        ActivityInfo activityInfo = Stub.getDefaultImpl().getActivityInfo(componentName, flags, userId);
                        return activityInfo;
                    }
                    _reply.readException();
                    _result = 0 != _reply.readInt() ? (ActivityInfo)ActivityInfo.CREATOR.createFromParcel(_reply) : null;
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
            public boolean activitySupportsIntent(ComponentName component, Intent intent, String resolvedType) throws RemoteException {
                boolean _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IPackageManager");
                    if (component != null) {
                        _data.writeInt(1);
                        component.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(resolvedType);
                    boolean _status = this.mRemote.transact(7, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        boolean bl = Stub.getDefaultImpl().activitySupportsIntent(component, intent, resolvedType);
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
            public ActivityInfo getReceiverInfo(ComponentName componentName, int flags, int userId) throws RemoteException {
                ActivityInfo _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IPackageManager");
                    if (componentName != null) {
                        _data.writeInt(1);
                        componentName.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(flags);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(8, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        ActivityInfo activityInfo = Stub.getDefaultImpl().getReceiverInfo(componentName, flags, userId);
                        return activityInfo;
                    }
                    _reply.readException();
                    _result = 0 != _reply.readInt() ? (ActivityInfo)ActivityInfo.CREATOR.createFromParcel(_reply) : null;
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
            public ServiceInfo getServiceInfo(ComponentName componentName, int flags, int userId) throws RemoteException {
                ServiceInfo _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IPackageManager");
                    if (componentName != null) {
                        _data.writeInt(1);
                        componentName.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(flags);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(9, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        ServiceInfo serviceInfo = Stub.getDefaultImpl().getServiceInfo(componentName, flags, userId);
                        return serviceInfo;
                    }
                    _reply.readException();
                    _result = 0 != _reply.readInt() ? (ServiceInfo)ServiceInfo.CREATOR.createFromParcel(_reply) : null;
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
            public ProviderInfo getProviderInfo(ComponentName componentName, int flags, int userId) throws RemoteException {
                ProviderInfo _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IPackageManager");
                    if (componentName != null) {
                        _data.writeInt(1);
                        componentName.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(flags);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(10, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        ProviderInfo providerInfo = Stub.getDefaultImpl().getProviderInfo(componentName, flags, userId);
                        return providerInfo;
                    }
                    _reply.readException();
                    _result = 0 != _reply.readInt() ? (ProviderInfo)ProviderInfo.CREATOR.createFromParcel(_reply) : null;
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
            public ResolveInfo resolveIntent(Intent intent, String resolvedType, int flags, int userId) throws RemoteException {
                ResolveInfo _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IPackageManager");
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(resolvedType);
                    _data.writeInt(flags);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(11, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        ResolveInfo resolveInfo = Stub.getDefaultImpl().resolveIntent(intent, resolvedType, flags, userId);
                        return resolveInfo;
                    }
                    _reply.readException();
                    _result = 0 != _reply.readInt() ? (ResolveInfo)ResolveInfo.CREATOR.createFromParcel(_reply) : null;
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
            public List<ResolveInfo> queryIntentActivities(Intent intent, String resolvedType, int flags, int userId) throws RemoteException {
                ArrayList _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IPackageManager");
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(resolvedType);
                    _data.writeInt(flags);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(12, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        List<ResolveInfo> list = Stub.getDefaultImpl().queryIntentActivities(intent, resolvedType, flags, userId);
                        return list;
                    }
                    _reply.readException();
                    _result = _reply.createTypedArrayList(ResolveInfo.CREATOR);
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
            public List<ResolveInfo> queryIntentReceivers(Intent intent, String resolvedType, int flags, int userId) throws RemoteException {
                ArrayList _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IPackageManager");
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(resolvedType);
                    _data.writeInt(flags);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(13, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        List<ResolveInfo> list = Stub.getDefaultImpl().queryIntentReceivers(intent, resolvedType, flags, userId);
                        return list;
                    }
                    _reply.readException();
                    _result = _reply.createTypedArrayList(ResolveInfo.CREATOR);
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
            public ResolveInfo resolveService(Intent intent, String resolvedType, int flags, int userId) throws RemoteException {
                ResolveInfo _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IPackageManager");
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(resolvedType);
                    _data.writeInt(flags);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(14, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        ResolveInfo resolveInfo = Stub.getDefaultImpl().resolveService(intent, resolvedType, flags, userId);
                        return resolveInfo;
                    }
                    _reply.readException();
                    _result = 0 != _reply.readInt() ? (ResolveInfo)ResolveInfo.CREATOR.createFromParcel(_reply) : null;
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
            public List<ResolveInfo> queryIntentServices(Intent intent, String resolvedType, int flags, int userId) throws RemoteException {
                ArrayList _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IPackageManager");
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(resolvedType);
                    _data.writeInt(flags);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(15, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        List<ResolveInfo> list = Stub.getDefaultImpl().queryIntentServices(intent, resolvedType, flags, userId);
                        return list;
                    }
                    _reply.readException();
                    _result = _reply.createTypedArrayList(ResolveInfo.CREATOR);
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
            public List<ResolveInfo> queryIntentContentProviders(Intent intent, String resolvedType, int flags, int userId) throws RemoteException {
                ArrayList _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IPackageManager");
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(resolvedType);
                    _data.writeInt(flags);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(16, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        List<ResolveInfo> list = Stub.getDefaultImpl().queryIntentContentProviders(intent, resolvedType, flags, userId);
                        return list;
                    }
                    _reply.readException();
                    _result = _reply.createTypedArrayList(ResolveInfo.CREATOR);
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
            public VParceledListSlice getInstalledPackages(int flags, int userId) throws RemoteException {
                VParceledListSlice _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IPackageManager");
                    _data.writeInt(flags);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(17, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        VParceledListSlice vParceledListSlice = Stub.getDefaultImpl().getInstalledPackages(flags, userId);
                        return vParceledListSlice;
                    }
                    _reply.readException();
                    _result = 0 != _reply.readInt() ? (VParceledListSlice)VParceledListSlice.CREATOR.createFromParcel(_reply) : null;
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
            public VParceledListSlice getInstalledApplications(int flags, int userId) throws RemoteException {
                VParceledListSlice _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IPackageManager");
                    _data.writeInt(flags);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(18, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        VParceledListSlice vParceledListSlice = Stub.getDefaultImpl().getInstalledApplications(flags, userId);
                        return vParceledListSlice;
                    }
                    _reply.readException();
                    _result = 0 != _reply.readInt() ? (VParceledListSlice)VParceledListSlice.CREATOR.createFromParcel(_reply) : null;
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
            public List<ReceiverInfo> getReceiverInfos(String packageName, String processName, int userId) throws RemoteException {
                ArrayList _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IPackageManager");
                    _data.writeString(packageName);
                    _data.writeString(processName);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(19, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        List<ReceiverInfo> list = Stub.getDefaultImpl().getReceiverInfos(packageName, processName, userId);
                        return list;
                    }
                    _reply.readException();
                    _result = _reply.createTypedArrayList(ReceiverInfo.CREATOR);
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
            public PermissionInfo getPermissionInfo(String name, int flags) throws RemoteException {
                PermissionInfo _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IPackageManager");
                    _data.writeString(name);
                    _data.writeInt(flags);
                    boolean _status = this.mRemote.transact(20, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        PermissionInfo permissionInfo = Stub.getDefaultImpl().getPermissionInfo(name, flags);
                        return permissionInfo;
                    }
                    _reply.readException();
                    _result = 0 != _reply.readInt() ? (PermissionInfo)PermissionInfo.CREATOR.createFromParcel(_reply) : null;
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
            public List<PermissionInfo> queryPermissionsByGroup(String group, int flags) throws RemoteException {
                ArrayList _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IPackageManager");
                    _data.writeString(group);
                    _data.writeInt(flags);
                    boolean _status = this.mRemote.transact(21, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        List<PermissionInfo> list = Stub.getDefaultImpl().queryPermissionsByGroup(group, flags);
                        return list;
                    }
                    _reply.readException();
                    _result = _reply.createTypedArrayList(PermissionInfo.CREATOR);
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
            public PermissionGroupInfo getPermissionGroupInfo(String name, int flags) throws RemoteException {
                PermissionGroupInfo _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IPackageManager");
                    _data.writeString(name);
                    _data.writeInt(flags);
                    boolean _status = this.mRemote.transact(22, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        PermissionGroupInfo permissionGroupInfo = Stub.getDefaultImpl().getPermissionGroupInfo(name, flags);
                        return permissionGroupInfo;
                    }
                    _reply.readException();
                    _result = 0 != _reply.readInt() ? (PermissionGroupInfo)PermissionGroupInfo.CREATOR.createFromParcel(_reply) : null;
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
            public List<PermissionGroupInfo> getAllPermissionGroups(int flags) throws RemoteException {
                ArrayList _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IPackageManager");
                    _data.writeInt(flags);
                    boolean _status = this.mRemote.transact(23, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        List<PermissionGroupInfo> list = Stub.getDefaultImpl().getAllPermissionGroups(flags);
                        return list;
                    }
                    _reply.readException();
                    _result = _reply.createTypedArrayList(PermissionGroupInfo.CREATOR);
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
            public ProviderInfo resolveContentProvider(String name, int flags, int userId) throws RemoteException {
                ProviderInfo _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IPackageManager");
                    _data.writeString(name);
                    _data.writeInt(flags);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(24, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        ProviderInfo providerInfo = Stub.getDefaultImpl().resolveContentProvider(name, flags, userId);
                        return providerInfo;
                    }
                    _reply.readException();
                    _result = 0 != _reply.readInt() ? (ProviderInfo)ProviderInfo.CREATOR.createFromParcel(_reply) : null;
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
            public ApplicationInfo getApplicationInfo(String packageName, int flags, int userId) throws RemoteException {
                ApplicationInfo _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IPackageManager");
                    _data.writeString(packageName);
                    _data.writeInt(flags);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(25, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        ApplicationInfo applicationInfo = Stub.getDefaultImpl().getApplicationInfo(packageName, flags, userId);
                        return applicationInfo;
                    }
                    _reply.readException();
                    _result = 0 != _reply.readInt() ? (ApplicationInfo)ApplicationInfo.CREATOR.createFromParcel(_reply) : null;
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
            public VParceledListSlice queryContentProviders(String processName, int vuid, int flags) throws RemoteException {
                VParceledListSlice _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IPackageManager");
                    _data.writeString(processName);
                    _data.writeInt(vuid);
                    _data.writeInt(flags);
                    boolean _status = this.mRemote.transact(26, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        VParceledListSlice vParceledListSlice = Stub.getDefaultImpl().queryContentProviders(processName, vuid, flags);
                        return vParceledListSlice;
                    }
                    _reply.readException();
                    _result = 0 != _reply.readInt() ? (VParceledListSlice)VParceledListSlice.CREATOR.createFromParcel(_reply) : null;
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
            public List<String> querySharedPackages(String packageName) throws RemoteException {
                ArrayList _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IPackageManager");
                    _data.writeString(packageName);
                    boolean _status = this.mRemote.transact(27, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        List<String> list = Stub.getDefaultImpl().querySharedPackages(packageName);
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
            public String getNameForUid(int uid) throws RemoteException {
                String _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IPackageManager");
                    _data.writeInt(uid);
                    boolean _status = this.mRemote.transact(28, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        String string2 = Stub.getDefaultImpl().getNameForUid(uid);
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
            public IBinder getPackageInstaller() throws RemoteException {
                IBinder _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IPackageManager");
                    boolean _status = this.mRemote.transact(29, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        IBinder iBinder = Stub.getDefaultImpl().getPackageInstaller();
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
            public int checkSignatures(String pkg1, String pkg2) throws RemoteException {
                int _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IPackageManager");
                    _data.writeString(pkg1);
                    _data.writeString(pkg2);
                    boolean _status = this.mRemote.transact(30, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        int n = Stub.getDefaultImpl().checkSignatures(pkg1, pkg2);
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
            public String[] getDangerousPermissions(String packageName) throws RemoteException {
                String[] _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IPackageManager");
                    _data.writeString(packageName);
                    boolean _status = this.mRemote.transact(31, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        String[] stringArray = Stub.getDefaultImpl().getDangerousPermissions(packageName);
                        return stringArray;
                    }
                    _reply.readException();
                    _result = _reply.createStringArray();
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
            public void setComponentEnabledSetting(ComponentName componentName, int newState, int flags, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IPackageManager");
                    if (componentName != null) {
                        _data.writeInt(1);
                        componentName.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(newState);
                    _data.writeInt(flags);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(32, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setComponentEnabledSetting(componentName, newState, flags, userId);
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
            public int getComponentEnabledSetting(ComponentName component, int userId) throws RemoteException {
                int _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IPackageManager");
                    if (component != null) {
                        _data.writeInt(1);
                        component.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(33, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        int n = Stub.getDefaultImpl().getComponentEnabledSetting(component, userId);
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
    implements IPackageManager {
        @Override
        public int getPackageUid(String packageName, int userId) throws RemoteException {
            return 0;
        }

        @Override
        public String[] getPackagesForUid(int vuid) throws RemoteException {
            return null;
        }

        @Override
        public List<String> getSharedLibraries(String pkgName) throws RemoteException {
            return null;
        }

        @Override
        public int checkPermission(boolean isExt, String permName, String pkgName, int userId) throws RemoteException {
            return 0;
        }

        @Override
        public PackageInfo getPackageInfo(String packageName, int flags, int userId) throws RemoteException {
            return null;
        }

        @Override
        public ActivityInfo getActivityInfo(ComponentName componentName, int flags, int userId) throws RemoteException {
            return null;
        }

        @Override
        public boolean activitySupportsIntent(ComponentName component, Intent intent, String resolvedType) throws RemoteException {
            return false;
        }

        @Override
        public ActivityInfo getReceiverInfo(ComponentName componentName, int flags, int userId) throws RemoteException {
            return null;
        }

        @Override
        public ServiceInfo getServiceInfo(ComponentName componentName, int flags, int userId) throws RemoteException {
            return null;
        }

        @Override
        public ProviderInfo getProviderInfo(ComponentName componentName, int flags, int userId) throws RemoteException {
            return null;
        }

        @Override
        public ResolveInfo resolveIntent(Intent intent, String resolvedType, int flags, int userId) throws RemoteException {
            return null;
        }

        @Override
        public List<ResolveInfo> queryIntentActivities(Intent intent, String resolvedType, int flags, int userId) throws RemoteException {
            return null;
        }

        @Override
        public List<ResolveInfo> queryIntentReceivers(Intent intent, String resolvedType, int flags, int userId) throws RemoteException {
            return null;
        }

        @Override
        public ResolveInfo resolveService(Intent intent, String resolvedType, int flags, int userId) throws RemoteException {
            return null;
        }

        @Override
        public List<ResolveInfo> queryIntentServices(Intent intent, String resolvedType, int flags, int userId) throws RemoteException {
            return null;
        }

        @Override
        public List<ResolveInfo> queryIntentContentProviders(Intent intent, String resolvedType, int flags, int userId) throws RemoteException {
            return null;
        }

        @Override
        public VParceledListSlice getInstalledPackages(int flags, int userId) throws RemoteException {
            return null;
        }

        @Override
        public VParceledListSlice getInstalledApplications(int flags, int userId) throws RemoteException {
            return null;
        }

        @Override
        public List<ReceiverInfo> getReceiverInfos(String packageName, String processName, int userId) throws RemoteException {
            return null;
        }

        @Override
        public PermissionInfo getPermissionInfo(String name, int flags) throws RemoteException {
            return null;
        }

        @Override
        public List<PermissionInfo> queryPermissionsByGroup(String group, int flags) throws RemoteException {
            return null;
        }

        @Override
        public PermissionGroupInfo getPermissionGroupInfo(String name, int flags) throws RemoteException {
            return null;
        }

        @Override
        public List<PermissionGroupInfo> getAllPermissionGroups(int flags) throws RemoteException {
            return null;
        }

        @Override
        public ProviderInfo resolveContentProvider(String name, int flags, int userId) throws RemoteException {
            return null;
        }

        @Override
        public ApplicationInfo getApplicationInfo(String packageName, int flags, int userId) throws RemoteException {
            return null;
        }

        @Override
        public VParceledListSlice queryContentProviders(String processName, int vuid, int flags) throws RemoteException {
            return null;
        }

        @Override
        public List<String> querySharedPackages(String packageName) throws RemoteException {
            return null;
        }

        @Override
        public String getNameForUid(int uid) throws RemoteException {
            return null;
        }

        @Override
        public IBinder getPackageInstaller() throws RemoteException {
            return null;
        }

        @Override
        public int checkSignatures(String pkg1, String pkg2) throws RemoteException {
            return 0;
        }

        @Override
        public String[] getDangerousPermissions(String packageName) throws RemoteException {
            return null;
        }

        @Override
        public void setComponentEnabledSetting(ComponentName componentName, int newState, int flags, int userId) throws RemoteException {
        }

        @Override
        public int getComponentEnabledSetting(ComponentName component, int userId) throws RemoteException {
            return 0;
        }

        public IBinder asBinder() {
            return null;
        }
    }
}

