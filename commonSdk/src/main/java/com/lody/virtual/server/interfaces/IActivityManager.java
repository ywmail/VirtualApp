/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.ComponentName
 *  android.content.Intent
 *  android.content.pm.ActivityInfo
 *  android.content.pm.ProviderInfo
 *  android.os.Binder
 *  android.os.Bundle
 *  android.os.IBinder
 *  android.os.IInterface
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.RemoteException
 */
package com.lody.virtual.server.interfaces;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ProviderInfo;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
import com.lody.virtual.StringFog;
import com.lody.virtual.remote.AppTaskInfo;
import com.lody.virtual.remote.BadgerInfo;
import com.lody.virtual.remote.ClientConfig;
import com.lody.virtual.remote.IntentSenderData;
import com.lody.virtual.remote.VParceledListSlice;
import java.util.ArrayList;
import java.util.List;

public interface IActivityManager
extends IInterface {
    public ClientConfig initProcess(String var1, String var2, int var3) throws RemoteException;

    public void appDoneExecuting(String var1, int var2) throws RemoteException;

    public int getFreeStubCount() throws RemoteException;

    public int checkPermission(boolean var1, String var2, int var3, int var4, String var5) throws RemoteException;

    public int getSystemPid() throws RemoteException;

    public int getSystemUid() throws RemoteException;

    public int getUidByPid(int var1) throws RemoteException;

    public int getCurrentUserId() throws RemoteException;

    public boolean isAppProcess(String var1) throws RemoteException;

    public boolean isAppRunning(String var1, int var2, boolean var3) throws RemoteException;

    public boolean isAppPid(int var1) throws RemoteException;

    public String getAppProcessName(int var1) throws RemoteException;

    public List<String> getProcessPkgList(int var1) throws RemoteException;

    public void killAllApps() throws RemoteException;

    public void killAppByPkg(String var1, int var2) throws RemoteException;

    public void killApplicationProcess(String var1, int var2) throws RemoteException;

    public void dump() throws RemoteException;

    public String getInitialPackage(int var1) throws RemoteException;

    public int startActivities(Intent[] var1, String[] var2, IBinder var3, Bundle var4, String var5, int var6) throws RemoteException;

    public int startActivity(Intent var1, ActivityInfo var2, IBinder var3, Bundle var4, String var5, int var6, String var7, int var8) throws RemoteException;

    public int startActivityFromHistory(Intent var1) throws RemoteException;

    public boolean finishActivityAffinity(int var1, IBinder var2) throws RemoteException;

    public void onActivityCreated(IBinder var1, IBinder var2, int var3) throws RemoteException;

    public void onActivityResumed(int var1, IBinder var2) throws RemoteException;

    public boolean onActivityDestroyed(int var1, IBinder var2) throws RemoteException;

    public void onActivityFinish(int var1, IBinder var2) throws RemoteException;

    public ComponentName getActivityClassForToken(int var1, IBinder var2) throws RemoteException;

    public String getCallingPackage(int var1, IBinder var2) throws RemoteException;

    public ComponentName getCallingActivity(int var1, IBinder var2) throws RemoteException;

    public AppTaskInfo getTaskInfo(int var1) throws RemoteException;

    public String getPackageForToken(int var1, IBinder var2) throws RemoteException;

    public IBinder acquireProviderClient(int var1, ProviderInfo var2) throws RemoteException;

    public boolean broadcastFinish(IBinder var1) throws RemoteException;

    public void addOrUpdateIntentSender(IntentSenderData var1, int var2) throws RemoteException;

    public void removeIntentSender(IBinder var1) throws RemoteException;

    public IntentSenderData getIntentSender(IBinder var1) throws RemoteException;

    public void processRestarted(String var1, String var2, int var3) throws RemoteException;

    public void notifyBadgerChange(BadgerInfo var1) throws RemoteException;

    public void setAppInactive(String var1, boolean var2, int var3) throws RemoteException;

    public boolean isAppInactive(String var1, int var2) throws RemoteException;

    public VParceledListSlice getServices(String var1, int var2, int var3, int var4) throws RemoteException;

    public void handleDownloadCompleteIntent(Intent var1) throws RemoteException;

    public int getAppPid(String var1, int var2, String var3) throws RemoteException;

    public void setSettingsProvider(int var1, int var2, String var3, String var4) throws RemoteException;

    public String getSettingsProvider(int var1, int var2, String var3) throws RemoteException;

    public static abstract class Stub
    extends Binder
    implements IActivityManager {
        private static final String DESCRIPTOR = "com.lody.virtual.server.interfaces.IActivityManager";
        static final int TRANSACTION_initProcess = 1;
        static final int TRANSACTION_appDoneExecuting = 2;
        static final int TRANSACTION_getFreeStubCount = 3;
        static final int TRANSACTION_checkPermission = 4;
        static final int TRANSACTION_getSystemPid = 5;
        static final int TRANSACTION_getSystemUid = 6;
        static final int TRANSACTION_getUidByPid = 7;
        static final int TRANSACTION_getCurrentUserId = 8;
        static final int TRANSACTION_isAppProcess = 9;
        static final int TRANSACTION_isAppRunning = 10;
        static final int TRANSACTION_isAppPid = 11;
        static final int TRANSACTION_getAppProcessName = 12;
        static final int TRANSACTION_getProcessPkgList = 13;
        static final int TRANSACTION_killAllApps = 14;
        static final int TRANSACTION_killAppByPkg = 15;
        static final int TRANSACTION_killApplicationProcess = 16;
        static final int TRANSACTION_dump = 17;
        static final int TRANSACTION_getInitialPackage = 18;
        static final int TRANSACTION_startActivities = 19;
        static final int TRANSACTION_startActivity = 20;
        static final int TRANSACTION_startActivityFromHistory = 21;
        static final int TRANSACTION_finishActivityAffinity = 22;
        static final int TRANSACTION_onActivityCreated = 23;
        static final int TRANSACTION_onActivityResumed = 24;
        static final int TRANSACTION_onActivityDestroyed = 25;
        static final int TRANSACTION_onActivityFinish = 26;
        static final int TRANSACTION_getActivityClassForToken = 27;
        static final int TRANSACTION_getCallingPackage = 28;
        static final int TRANSACTION_getCallingActivity = 29;
        static final int TRANSACTION_getTaskInfo = 30;
        static final int TRANSACTION_getPackageForToken = 31;
        static final int TRANSACTION_acquireProviderClient = 32;
        static final int TRANSACTION_broadcastFinish = 33;
        static final int TRANSACTION_addOrUpdateIntentSender = 34;
        static final int TRANSACTION_removeIntentSender = 35;
        static final int TRANSACTION_getIntentSender = 36;
        static final int TRANSACTION_processRestarted = 37;
        static final int TRANSACTION_notifyBadgerChange = 38;
        static final int TRANSACTION_setAppInactive = 39;
        static final int TRANSACTION_isAppInactive = 40;
        static final int TRANSACTION_getServices = 41;
        static final int TRANSACTION_handleDownloadCompleteIntent = 42;
        static final int TRANSACTION_getAppPid = 43;
        static final int TRANSACTION_setSettingsProvider = 44;
        static final int TRANSACTION_getSettingsProvider = 45;

        public Stub() {
            this.attachInterface(this, "com.lody.virtual.server.interfaces.IActivityManager");
        }

        public static IActivityManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && iin instanceof IActivityManager) {
                return (IActivityManager)iin;
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
                    int _arg2 = data.readInt();
                    ClientConfig _result = this.initProcess(_arg0, _arg1, _arg2);
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
                    String _arg0 = data.readString();
                    int _arg1 = data.readInt();
                    this.appDoneExecuting(_arg0, _arg1);
                    reply.writeNoException();
                    return true;
                }
                case 3: {
                    data.enforceInterface(descriptor);
                    int _result = this.getFreeStubCount();
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                }
                case 4: {
                    data.enforceInterface(descriptor);
                    boolean _arg0 = 0 != data.readInt();
                    String _arg1 = data.readString();
                    int _arg2 = data.readInt();
                    int _arg3 = data.readInt();
                    String _arg4 = data.readString();
                    int _result = this.checkPermission(_arg0, _arg1, _arg2, _arg3, _arg4);
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                }
                case 5: {
                    data.enforceInterface(descriptor);
                    int _result = this.getSystemPid();
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                }
                case 6: {
                    data.enforceInterface(descriptor);
                    int _result = this.getSystemUid();
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                }
                case 7: {
                    data.enforceInterface(descriptor);
                    int _arg0 = data.readInt();
                    int _result = this.getUidByPid(_arg0);
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                }
                case 8: {
                    data.enforceInterface(descriptor);
                    int _result = this.getCurrentUserId();
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                }
                case 9: {
                    data.enforceInterface(descriptor);
                    String _arg0 = data.readString();
                    boolean _result = this.isAppProcess(_arg0);
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                }
                case 10: {
                    data.enforceInterface(descriptor);
                    String _arg0 = data.readString();
                    int _arg1 = data.readInt();
                    boolean _arg2 = 0 != data.readInt();
                    boolean _result = this.isAppRunning(_arg0, _arg1, _arg2);
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                }
                case 11: {
                    data.enforceInterface(descriptor);
                    int _arg0 = data.readInt();
                    boolean _result = this.isAppPid(_arg0);
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                }
                case 12: {
                    data.enforceInterface(descriptor);
                    int _arg0 = data.readInt();
                    String _result = this.getAppProcessName(_arg0);
                    reply.writeNoException();
                    reply.writeString(_result);
                    return true;
                }
                case 13: {
                    data.enforceInterface(descriptor);
                    int _arg0 = data.readInt();
                    List _result = this.getProcessPkgList(_arg0);
                    reply.writeNoException();
                    reply.writeStringList(_result);
                    return true;
                }
                case 14: {
                    data.enforceInterface(descriptor);
                    this.killAllApps();
                    reply.writeNoException();
                    return true;
                }
                case 15: {
                    data.enforceInterface(descriptor);
                    String _arg0 = data.readString();
                    int _arg1 = data.readInt();
                    this.killAppByPkg(_arg0, _arg1);
                    reply.writeNoException();
                    return true;
                }
                case 16: {
                    data.enforceInterface(descriptor);
                    String _arg0 = data.readString();
                    int _arg1 = data.readInt();
                    this.killApplicationProcess(_arg0, _arg1);
                    reply.writeNoException();
                    return true;
                }
                case 17: {
                    data.enforceInterface(descriptor);
                    this.dump();
                    reply.writeNoException();
                    return true;
                }
                case 18: {
                    data.enforceInterface(descriptor);
                    int _arg0 = data.readInt();
                    String _result = this.getInitialPackage(_arg0);
                    reply.writeNoException();
                    reply.writeString(_result);
                    return true;
                }
                case 19: {
                    data.enforceInterface(descriptor);
                    Intent[] _arg0 = (Intent[])data.createTypedArray(Intent.CREATOR);
                    String[] _arg1 = data.createStringArray();
                    IBinder _arg2 = data.readStrongBinder();
                    Bundle _arg3 = 0 != data.readInt() ? (Bundle)Bundle.CREATOR.createFromParcel(data) : null;
                    String _arg4 = data.readString();
                    int _arg5 = data.readInt();
                    int _result = this.startActivities(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5);
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                }
                case 20: {
                    data.enforceInterface(descriptor);
                    Intent _arg0 = 0 != data.readInt() ? (Intent)Intent.CREATOR.createFromParcel(data) : null;
                    ActivityInfo _arg1 = 0 != data.readInt() ? (ActivityInfo)ActivityInfo.CREATOR.createFromParcel(data) : null;
                    IBinder _arg2 = data.readStrongBinder();
                    Bundle _arg3 = 0 != data.readInt() ? (Bundle)Bundle.CREATOR.createFromParcel(data) : null;
                    String _arg4 = data.readString();
                    int _arg5 = data.readInt();
                    String _arg6 = data.readString();
                    int _arg7 = data.readInt();
                    int _result = this.startActivity(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5, _arg6, _arg7);
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                }
                case 21: {
                    data.enforceInterface(descriptor);
                    Intent _arg0 = 0 != data.readInt() ? (Intent)Intent.CREATOR.createFromParcel(data) : null;
                    int _result = this.startActivityFromHistory(_arg0);
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                }
                case 22: {
                    data.enforceInterface(descriptor);
                    int _arg0 = data.readInt();
                    IBinder _arg1 = data.readStrongBinder();
                    boolean _result = this.finishActivityAffinity(_arg0, _arg1);
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                }
                case 23: {
                    data.enforceInterface(descriptor);
                    IBinder _arg0 = data.readStrongBinder();
                    IBinder _arg1 = data.readStrongBinder();
                    int _arg2 = data.readInt();
                    this.onActivityCreated(_arg0, _arg1, _arg2);
                    reply.writeNoException();
                    return true;
                }
                case 24: {
                    data.enforceInterface(descriptor);
                    int _arg0 = data.readInt();
                    IBinder _arg1 = data.readStrongBinder();
                    this.onActivityResumed(_arg0, _arg1);
                    reply.writeNoException();
                    return true;
                }
                case 25: {
                    data.enforceInterface(descriptor);
                    int _arg0 = data.readInt();
                    IBinder _arg1 = data.readStrongBinder();
                    boolean _result = this.onActivityDestroyed(_arg0, _arg1);
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                }
                case 26: {
                    data.enforceInterface(descriptor);
                    int _arg0 = data.readInt();
                    IBinder _arg1 = data.readStrongBinder();
                    this.onActivityFinish(_arg0, _arg1);
                    reply.writeNoException();
                    return true;
                }
                case 27: {
                    data.enforceInterface(descriptor);
                    int _arg0 = data.readInt();
                    IBinder _arg1 = data.readStrongBinder();
                    ComponentName _result = this.getActivityClassForToken(_arg0, _arg1);
                    reply.writeNoException();
                    if (_result != null) {
                        reply.writeInt(1);
                        _result.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                }
                case 28: {
                    data.enforceInterface(descriptor);
                    int _arg0 = data.readInt();
                    IBinder _arg1 = data.readStrongBinder();
                    String _result = this.getCallingPackage(_arg0, _arg1);
                    reply.writeNoException();
                    reply.writeString(_result);
                    return true;
                }
                case 29: {
                    data.enforceInterface(descriptor);
                    int _arg0 = data.readInt();
                    IBinder _arg1 = data.readStrongBinder();
                    ComponentName _result = this.getCallingActivity(_arg0, _arg1);
                    reply.writeNoException();
                    if (_result != null) {
                        reply.writeInt(1);
                        _result.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                }
                case 30: {
                    data.enforceInterface(descriptor);
                    int _arg0 = data.readInt();
                    AppTaskInfo _result = this.getTaskInfo(_arg0);
                    reply.writeNoException();
                    if (_result != null) {
                        reply.writeInt(1);
                        _result.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                }
                case 31: {
                    data.enforceInterface(descriptor);
                    int _arg0 = data.readInt();
                    IBinder _arg1 = data.readStrongBinder();
                    String _result = this.getPackageForToken(_arg0, _arg1);
                    reply.writeNoException();
                    reply.writeString(_result);
                    return true;
                }
                case 32: {
                    data.enforceInterface(descriptor);
                    int _arg0 = data.readInt();
                    ProviderInfo _arg1 = 0 != data.readInt() ? (ProviderInfo)ProviderInfo.CREATOR.createFromParcel(data) : null;
                    IBinder _result = this.acquireProviderClient(_arg0, _arg1);
                    reply.writeNoException();
                    reply.writeStrongBinder(_result);
                    return true;
                }
                case 33: {
                    data.enforceInterface(descriptor);
                    IBinder _arg0 = data.readStrongBinder();
                    boolean _result = this.broadcastFinish(_arg0);
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                }
                case 34: {
                    data.enforceInterface(descriptor);
                    IntentSenderData _arg0 = 0 != data.readInt() ? (IntentSenderData)IntentSenderData.CREATOR.createFromParcel(data) : null;
                    int _arg1 = data.readInt();
                    this.addOrUpdateIntentSender(_arg0, _arg1);
                    reply.writeNoException();
                    return true;
                }
                case 35: {
                    data.enforceInterface(descriptor);
                    IBinder _arg0 = data.readStrongBinder();
                    this.removeIntentSender(_arg0);
                    reply.writeNoException();
                    return true;
                }
                case 36: {
                    data.enforceInterface(descriptor);
                    IBinder _arg0 = data.readStrongBinder();
                    IntentSenderData _result = this.getIntentSender(_arg0);
                    reply.writeNoException();
                    if (_result != null) {
                        reply.writeInt(1);
                        _result.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                }
                case 37: {
                    data.enforceInterface(descriptor);
                    String _arg0 = data.readString();
                    String _arg1 = data.readString();
                    int _arg2 = data.readInt();
                    this.processRestarted(_arg0, _arg1, _arg2);
                    reply.writeNoException();
                    return true;
                }
                case 38: {
                    data.enforceInterface(descriptor);
                    BadgerInfo _arg0 = 0 != data.readInt() ? (BadgerInfo)BadgerInfo.CREATOR.createFromParcel(data) : null;
                    this.notifyBadgerChange(_arg0);
                    reply.writeNoException();
                    return true;
                }
                case 39: {
                    data.enforceInterface(descriptor);
                    String _arg0 = data.readString();
                    boolean _arg1 = 0 != data.readInt();
                    int _arg2 = data.readInt();
                    this.setAppInactive(_arg0, _arg1, _arg2);
                    reply.writeNoException();
                    return true;
                }
                case 40: {
                    data.enforceInterface(descriptor);
                    String _arg0 = data.readString();
                    int _arg1 = data.readInt();
                    boolean _result = this.isAppInactive(_arg0, _arg1);
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                }
                case 41: {
                    data.enforceInterface(descriptor);
                    String _arg0 = data.readString();
                    int _arg1 = data.readInt();
                    int _arg2 = data.readInt();
                    int _arg3 = data.readInt();
                    VParceledListSlice _result = this.getServices(_arg0, _arg1, _arg2, _arg3);
                    reply.writeNoException();
                    if (_result != null) {
                        reply.writeInt(1);
                        _result.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                }
                case 42: {
                    data.enforceInterface(descriptor);
                    Intent _arg0 = 0 != data.readInt() ? (Intent)Intent.CREATOR.createFromParcel(data) : null;
                    this.handleDownloadCompleteIntent(_arg0);
                    reply.writeNoException();
                    return true;
                }
                case 43: {
                    data.enforceInterface(descriptor);
                    String _arg0 = data.readString();
                    int _arg1 = data.readInt();
                    String _arg2 = data.readString();
                    int _result = this.getAppPid(_arg0, _arg1, _arg2);
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                }
                case 44: {
                    data.enforceInterface(descriptor);
                    int _arg0 = data.readInt();
                    int _arg1 = data.readInt();
                    String _arg2 = data.readString();
                    String _arg3 = data.readString();
                    this.setSettingsProvider(_arg0, _arg1, _arg2, _arg3);
                    reply.writeNoException();
                    return true;
                }
                case 45: {
                    data.enforceInterface(descriptor);
                    int _arg0 = data.readInt();
                    int _arg1 = data.readInt();
                    String _arg2 = data.readString();
                    String _result = this.getSettingsProvider(_arg0, _arg1, _arg2);
                    reply.writeNoException();
                    reply.writeString(_result);
                    return true;
                }
            }
            return super.onTransact(code, data, reply, flags);
        }

        public static boolean setDefaultImpl(IActivityManager impl) {
            if (Proxy.sDefaultImpl != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            }
            if (impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IActivityManager getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }

        private static class Proxy
        implements IActivityManager {
            private IBinder mRemote;
            public static IActivityManager sDefaultImpl;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return "com.lody.virtual.server.interfaces.IActivityManager";
            }

            /*
             * WARNING - Removed try catching itself - possible behaviour change.
             */
            @Override
            public ClientConfig initProcess(String packageName, String processName, int userId) throws RemoteException {
                ClientConfig _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IActivityManager");
                    _data.writeString(packageName);
                    _data.writeString(processName);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        ClientConfig clientConfig = Stub.getDefaultImpl().initProcess(packageName, processName, userId);
                        return clientConfig;
                    }
                    _reply.readException();
                    _result = 0 != _reply.readInt() ? (ClientConfig)ClientConfig.CREATOR.createFromParcel(_reply) : null;
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
            public void appDoneExecuting(String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IActivityManager");
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().appDoneExecuting(packageName, userId);
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
            public int getFreeStubCount() throws RemoteException {
                int _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IActivityManager");
                    boolean _status = this.mRemote.transact(3, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        int n = Stub.getDefaultImpl().getFreeStubCount();
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
            public int checkPermission(boolean isExt, String permission2, int pid, int uid, String packageName) throws RemoteException {
                int _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IActivityManager");
                    _data.writeInt(isExt ? 1 : 0);
                    _data.writeString(permission2);
                    _data.writeInt(pid);
                    _data.writeInt(uid);
                    _data.writeString(packageName);
                    boolean _status = this.mRemote.transact(4, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        int n = Stub.getDefaultImpl().checkPermission(isExt, permission2, pid, uid, packageName);
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
            public int getSystemPid() throws RemoteException {
                int _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IActivityManager");
                    boolean _status = this.mRemote.transact(5, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        int n = Stub.getDefaultImpl().getSystemPid();
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
            public int getSystemUid() throws RemoteException {
                int _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IActivityManager");
                    boolean _status = this.mRemote.transact(6, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        int n = Stub.getDefaultImpl().getSystemUid();
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
            public int getUidByPid(int pid) throws RemoteException {
                int _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IActivityManager");
                    _data.writeInt(pid);
                    boolean _status = this.mRemote.transact(7, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        int n = Stub.getDefaultImpl().getUidByPid(pid);
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
            public int getCurrentUserId() throws RemoteException {
                int _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IActivityManager");
                    boolean _status = this.mRemote.transact(8, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        int n = Stub.getDefaultImpl().getCurrentUserId();
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
            public boolean isAppProcess(String processName) throws RemoteException {
                boolean _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IActivityManager");
                    _data.writeString(processName);
                    boolean _status = this.mRemote.transact(9, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        boolean bl = Stub.getDefaultImpl().isAppProcess(processName);
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
            public boolean isAppRunning(String packageName, int userId, boolean foreground) throws RemoteException {
                boolean _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IActivityManager");
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    _data.writeInt(foreground ? 1 : 0);
                    boolean _status = this.mRemote.transact(10, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        boolean bl = Stub.getDefaultImpl().isAppRunning(packageName, userId, foreground);
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
            public boolean isAppPid(int pid) throws RemoteException {
                boolean _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IActivityManager");
                    _data.writeInt(pid);
                    boolean _status = this.mRemote.transact(11, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        boolean bl = Stub.getDefaultImpl().isAppPid(pid);
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
            public String getAppProcessName(int pid) throws RemoteException {
                String _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IActivityManager");
                    _data.writeInt(pid);
                    boolean _status = this.mRemote.transact(12, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        String string2 = Stub.getDefaultImpl().getAppProcessName(pid);
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
            public List<String> getProcessPkgList(int pid) throws RemoteException {
                ArrayList _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IActivityManager");
                    _data.writeInt(pid);
                    boolean _status = this.mRemote.transact(13, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        List<String> list = Stub.getDefaultImpl().getProcessPkgList(pid);
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
            public void killAllApps() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IActivityManager");
                    boolean _status = this.mRemote.transact(14, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().killAllApps();
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
            public void killAppByPkg(String pkg, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IActivityManager");
                    _data.writeString(pkg);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(15, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().killAppByPkg(pkg, userId);
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
            public void killApplicationProcess(String processName, int vuid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IActivityManager");
                    _data.writeString(processName);
                    _data.writeInt(vuid);
                    boolean _status = this.mRemote.transact(16, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().killApplicationProcess(processName, vuid);
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
            public void dump() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IActivityManager");
                    boolean _status = this.mRemote.transact(17, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().dump();
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
            public String getInitialPackage(int pid) throws RemoteException {
                String _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IActivityManager");
                    _data.writeInt(pid);
                    boolean _status = this.mRemote.transact(18, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        String string2 = Stub.getDefaultImpl().getInitialPackage(pid);
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
            public int startActivities(Intent[] intents, String[] resolvedTypes, IBinder token, Bundle options, String callingPkg, int userId) throws RemoteException {
                int _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IActivityManager");
                    _data.writeTypedArray((Parcelable[])intents, 0);
                    _data.writeStringArray(resolvedTypes);
                    _data.writeStrongBinder(token);
                    if (options != null) {
                        _data.writeInt(1);
                        options.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callingPkg);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(19, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        int n = Stub.getDefaultImpl().startActivities(intents, resolvedTypes, token, options, callingPkg, userId);
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
            public int startActivity(Intent intent, ActivityInfo info, IBinder resultTo, Bundle options, String resultWho, int requestCode, String callingPkg, int userId) throws RemoteException {
                int _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IActivityManager");
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (info != null) {
                        _data.writeInt(1);
                        info.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(resultTo);
                    if (options != null) {
                        _data.writeInt(1);
                        options.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(resultWho);
                    _data.writeInt(requestCode);
                    _data.writeString(callingPkg);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(20, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        int n = Stub.getDefaultImpl().startActivity(intent, info, resultTo, options, resultWho, requestCode, callingPkg, userId);
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
            public int startActivityFromHistory(Intent intent) throws RemoteException {
                int _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IActivityManager");
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(21, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        int n = Stub.getDefaultImpl().startActivityFromHistory(intent);
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
            public boolean finishActivityAffinity(int userId, IBinder token) throws RemoteException {
                boolean _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IActivityManager");
                    _data.writeInt(userId);
                    _data.writeStrongBinder(token);
                    boolean _status = this.mRemote.transact(22, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        boolean bl = Stub.getDefaultImpl().finishActivityAffinity(userId, token);
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
            public void onActivityCreated(IBinder record, IBinder token, int taskId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IActivityManager");
                    _data.writeStrongBinder(record);
                    _data.writeStrongBinder(token);
                    _data.writeInt(taskId);
                    boolean _status = this.mRemote.transact(23, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onActivityCreated(record, token, taskId);
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
            public void onActivityResumed(int userId, IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IActivityManager");
                    _data.writeInt(userId);
                    _data.writeStrongBinder(token);
                    boolean _status = this.mRemote.transact(24, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onActivityResumed(userId, token);
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
            public boolean onActivityDestroyed(int userId, IBinder token) throws RemoteException {
                boolean _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IActivityManager");
                    _data.writeInt(userId);
                    _data.writeStrongBinder(token);
                    boolean _status = this.mRemote.transact(25, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        boolean bl = Stub.getDefaultImpl().onActivityDestroyed(userId, token);
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
            public void onActivityFinish(int userId, IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IActivityManager");
                    _data.writeInt(userId);
                    _data.writeStrongBinder(token);
                    boolean _status = this.mRemote.transact(26, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onActivityFinish(userId, token);
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
            public ComponentName getActivityClassForToken(int userId, IBinder token) throws RemoteException {
                ComponentName _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IActivityManager");
                    _data.writeInt(userId);
                    _data.writeStrongBinder(token);
                    boolean _status = this.mRemote.transact(27, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        ComponentName componentName = Stub.getDefaultImpl().getActivityClassForToken(userId, token);
                        return componentName;
                    }
                    _reply.readException();
                    _result = 0 != _reply.readInt() ? (ComponentName)ComponentName.CREATOR.createFromParcel(_reply) : null;
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
            public String getCallingPackage(int userId, IBinder token) throws RemoteException {
                String _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IActivityManager");
                    _data.writeInt(userId);
                    _data.writeStrongBinder(token);
                    boolean _status = this.mRemote.transact(28, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        String string2 = Stub.getDefaultImpl().getCallingPackage(userId, token);
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
            public ComponentName getCallingActivity(int userId, IBinder token) throws RemoteException {
                ComponentName _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IActivityManager");
                    _data.writeInt(userId);
                    _data.writeStrongBinder(token);
                    boolean _status = this.mRemote.transact(29, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        ComponentName componentName = Stub.getDefaultImpl().getCallingActivity(userId, token);
                        return componentName;
                    }
                    _reply.readException();
                    _result = 0 != _reply.readInt() ? (ComponentName)ComponentName.CREATOR.createFromParcel(_reply) : null;
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
            public AppTaskInfo getTaskInfo(int taskId) throws RemoteException {
                AppTaskInfo _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IActivityManager");
                    _data.writeInt(taskId);
                    boolean _status = this.mRemote.transact(30, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        AppTaskInfo appTaskInfo = Stub.getDefaultImpl().getTaskInfo(taskId);
                        return appTaskInfo;
                    }
                    _reply.readException();
                    _result = 0 != _reply.readInt() ? (AppTaskInfo)AppTaskInfo.CREATOR.createFromParcel(_reply) : null;
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
            public String getPackageForToken(int userId, IBinder token) throws RemoteException {
                String _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IActivityManager");
                    _data.writeInt(userId);
                    _data.writeStrongBinder(token);
                    boolean _status = this.mRemote.transact(31, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        String string2 = Stub.getDefaultImpl().getPackageForToken(userId, token);
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
            public IBinder acquireProviderClient(int userId, ProviderInfo info) throws RemoteException {
                IBinder _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IActivityManager");
                    _data.writeInt(userId);
                    if (info != null) {
                        _data.writeInt(1);
                        info.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(32, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        IBinder iBinder = Stub.getDefaultImpl().acquireProviderClient(userId, info);
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
            public boolean broadcastFinish(IBinder token) throws RemoteException {
                boolean _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IActivityManager");
                    _data.writeStrongBinder(token);
                    boolean _status = this.mRemote.transact(33, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        boolean bl = Stub.getDefaultImpl().broadcastFinish(token);
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
            public void addOrUpdateIntentSender(IntentSenderData sender, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IActivityManager");
                    if (sender != null) {
                        _data.writeInt(1);
                        sender.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(34, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().addOrUpdateIntentSender(sender, userId);
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
            public void removeIntentSender(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IActivityManager");
                    _data.writeStrongBinder(token);
                    boolean _status = this.mRemote.transact(35, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().removeIntentSender(token);
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
            public IntentSenderData getIntentSender(IBinder token) throws RemoteException {
                IntentSenderData _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IActivityManager");
                    _data.writeStrongBinder(token);
                    boolean _status = this.mRemote.transact(36, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        IntentSenderData intentSenderData = Stub.getDefaultImpl().getIntentSender(token);
                        return intentSenderData;
                    }
                    _reply.readException();
                    _result = 0 != _reply.readInt() ? (IntentSenderData)IntentSenderData.CREATOR.createFromParcel(_reply) : null;
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
            public void processRestarted(String packageName, String processName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IActivityManager");
                    _data.writeString(packageName);
                    _data.writeString(processName);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(37, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().processRestarted(packageName, processName, userId);
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
            public void notifyBadgerChange(BadgerInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IActivityManager");
                    if (info != null) {
                        _data.writeInt(1);
                        info.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(38, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().notifyBadgerChange(info);
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
            public void setAppInactive(String packageName, boolean idle, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IActivityManager");
                    _data.writeString(packageName);
                    _data.writeInt(idle ? 1 : 0);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(39, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setAppInactive(packageName, idle, userId);
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
            public boolean isAppInactive(String packageName, int userId) throws RemoteException {
                boolean _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IActivityManager");
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(40, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        boolean bl = Stub.getDefaultImpl().isAppInactive(packageName, userId);
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
            public VParceledListSlice getServices(String pkg, int maxNum, int flags, int userId) throws RemoteException {
                VParceledListSlice _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IActivityManager");
                    _data.writeString(pkg);
                    _data.writeInt(maxNum);
                    _data.writeInt(flags);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(41, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        VParceledListSlice vParceledListSlice = Stub.getDefaultImpl().getServices(pkg, maxNum, flags, userId);
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
            public void handleDownloadCompleteIntent(Intent intent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IActivityManager");
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(42, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().handleDownloadCompleteIntent(intent);
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
            public int getAppPid(String packageName, int userId, String proccessName) throws RemoteException {
                int _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IActivityManager");
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    _data.writeString(proccessName);
                    boolean _status = this.mRemote.transact(43, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        int n = Stub.getDefaultImpl().getAppPid(packageName, userId, proccessName);
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
            public void setSettingsProvider(int userId, int tableIndex, String arg, String value) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IActivityManager");
                    _data.writeInt(userId);
                    _data.writeInt(tableIndex);
                    _data.writeString(arg);
                    _data.writeString(value);
                    boolean _status = this.mRemote.transact(44, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setSettingsProvider(userId, tableIndex, arg, value);
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
            public String getSettingsProvider(int userId, int tableIndex, String arg) throws RemoteException {
                String _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IActivityManager");
                    _data.writeInt(userId);
                    _data.writeInt(tableIndex);
                    _data.writeString(arg);
                    boolean _status = this.mRemote.transact(45, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        String string2 = Stub.getDefaultImpl().getSettingsProvider(userId, tableIndex, arg);
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
        }
    }

    public static class Default
    implements IActivityManager {
        @Override
        public ClientConfig initProcess(String packageName, String processName, int userId) throws RemoteException {
            return null;
        }

        @Override
        public void appDoneExecuting(String packageName, int userId) throws RemoteException {
        }

        @Override
        public int getFreeStubCount() throws RemoteException {
            return 0;
        }

        @Override
        public int checkPermission(boolean isExt, String permission2, int pid, int uid, String packageName) throws RemoteException {
            return 0;
        }

        @Override
        public int getSystemPid() throws RemoteException {
            return 0;
        }

        @Override
        public int getSystemUid() throws RemoteException {
            return 0;
        }

        @Override
        public int getUidByPid(int pid) throws RemoteException {
            return 0;
        }

        @Override
        public int getCurrentUserId() throws RemoteException {
            return 0;
        }

        @Override
        public boolean isAppProcess(String processName) throws RemoteException {
            return false;
        }

        @Override
        public boolean isAppRunning(String packageName, int userId, boolean foreground) throws RemoteException {
            return false;
        }

        @Override
        public boolean isAppPid(int pid) throws RemoteException {
            return false;
        }

        @Override
        public String getAppProcessName(int pid) throws RemoteException {
            return null;
        }

        @Override
        public List<String> getProcessPkgList(int pid) throws RemoteException {
            return null;
        }

        @Override
        public void killAllApps() throws RemoteException {
        }

        @Override
        public void killAppByPkg(String pkg, int userId) throws RemoteException {
        }

        @Override
        public void killApplicationProcess(String processName, int vuid) throws RemoteException {
        }

        @Override
        public void dump() throws RemoteException {
        }

        @Override
        public String getInitialPackage(int pid) throws RemoteException {
            return null;
        }

        @Override
        public int startActivities(Intent[] intents, String[] resolvedTypes, IBinder token, Bundle options, String callingPkg, int userId) throws RemoteException {
            return 0;
        }

        @Override
        public int startActivity(Intent intent, ActivityInfo info, IBinder resultTo, Bundle options, String resultWho, int requestCode, String callingPkg, int userId) throws RemoteException {
            return 0;
        }

        @Override
        public int startActivityFromHistory(Intent intent) throws RemoteException {
            return 0;
        }

        @Override
        public boolean finishActivityAffinity(int userId, IBinder token) throws RemoteException {
            return false;
        }

        @Override
        public void onActivityCreated(IBinder record, IBinder token, int taskId) throws RemoteException {
        }

        @Override
        public void onActivityResumed(int userId, IBinder token) throws RemoteException {
        }

        @Override
        public boolean onActivityDestroyed(int userId, IBinder token) throws RemoteException {
            return false;
        }

        @Override
        public void onActivityFinish(int userId, IBinder token) throws RemoteException {
        }

        @Override
        public ComponentName getActivityClassForToken(int userId, IBinder token) throws RemoteException {
            return null;
        }

        @Override
        public String getCallingPackage(int userId, IBinder token) throws RemoteException {
            return null;
        }

        @Override
        public ComponentName getCallingActivity(int userId, IBinder token) throws RemoteException {
            return null;
        }

        @Override
        public AppTaskInfo getTaskInfo(int taskId) throws RemoteException {
            return null;
        }

        @Override
        public String getPackageForToken(int userId, IBinder token) throws RemoteException {
            return null;
        }

        @Override
        public IBinder acquireProviderClient(int userId, ProviderInfo info) throws RemoteException {
            return null;
        }

        @Override
        public boolean broadcastFinish(IBinder token) throws RemoteException {
            return false;
        }

        @Override
        public void addOrUpdateIntentSender(IntentSenderData sender, int userId) throws RemoteException {
        }

        @Override
        public void removeIntentSender(IBinder token) throws RemoteException {
        }

        @Override
        public IntentSenderData getIntentSender(IBinder token) throws RemoteException {
            return null;
        }

        @Override
        public void processRestarted(String packageName, String processName, int userId) throws RemoteException {
        }

        @Override
        public void notifyBadgerChange(BadgerInfo info) throws RemoteException {
        }

        @Override
        public void setAppInactive(String packageName, boolean idle, int userId) throws RemoteException {
        }

        @Override
        public boolean isAppInactive(String packageName, int userId) throws RemoteException {
            return false;
        }

        @Override
        public VParceledListSlice getServices(String pkg, int maxNum, int flags, int userId) throws RemoteException {
            return null;
        }

        @Override
        public void handleDownloadCompleteIntent(Intent intent) throws RemoteException {
        }

        @Override
        public int getAppPid(String packageName, int userId, String proccessName) throws RemoteException {
            return 0;
        }

        @Override
        public void setSettingsProvider(int userId, int tableIndex, String arg, String value) throws RemoteException {
        }

        @Override
        public String getSettingsProvider(int userId, int tableIndex, String arg) throws RemoteException {
            return null;
        }

        public IBinder asBinder() {
            return null;
        }
    }
}

