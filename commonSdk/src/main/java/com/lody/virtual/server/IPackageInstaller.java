/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.IntentSender
 *  android.graphics.Bitmap
 *  android.os.Binder
 *  android.os.IBinder
 *  android.os.IInterface
 *  android.os.Parcel
 *  android.os.RemoteException
 */
package com.lody.virtual.server;

import android.content.IntentSender;
import android.content.pm.IPackageInstallerCallback;
import android.content.pm.IPackageInstallerSession;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.lody.virtual.StringFog;
import com.lody.virtual.remote.VParceledListSlice;
import com.lody.virtual.server.pm.installer.SessionInfo;
import com.lody.virtual.server.pm.installer.SessionParams;

public interface IPackageInstaller
extends IInterface {
    public int createSession(SessionParams var1, String var2, int var3) throws RemoteException;

    public void updateSessionAppIcon(int var1, Bitmap var2) throws RemoteException;

    public void updateSessionAppLabel(int var1, String var2) throws RemoteException;

    public void abandonSession(int var1) throws RemoteException;

    public IPackageInstallerSession openSession(int var1) throws RemoteException;

    public SessionInfo getSessionInfo(int var1) throws RemoteException;

    public VParceledListSlice getAllSessions(int var1) throws RemoteException;

    public VParceledListSlice getMySessions(String var1, int var2) throws RemoteException;

    public void registerCallback(IPackageInstallerCallback var1, int var2) throws RemoteException;

    public void unregisterCallback(IPackageInstallerCallback var1) throws RemoteException;

    public void uninstall(String var1, String var2, int var3, IntentSender var4, int var5) throws RemoteException;

    public void setPermissionsResult(int var1, boolean var2) throws RemoteException;

    public static abstract class Stub
    extends Binder
    implements IPackageInstaller {
        private static final String DESCRIPTOR = "com.lody.virtual.server.IPackageInstaller";
        static final int TRANSACTION_createSession = 1;
        static final int TRANSACTION_updateSessionAppIcon = 2;
        static final int TRANSACTION_updateSessionAppLabel = 3;
        static final int TRANSACTION_abandonSession = 4;
        static final int TRANSACTION_openSession = 5;
        static final int TRANSACTION_getSessionInfo = 6;
        static final int TRANSACTION_getAllSessions = 7;
        static final int TRANSACTION_getMySessions = 8;
        static final int TRANSACTION_registerCallback = 9;
        static final int TRANSACTION_unregisterCallback = 10;
        static final int TRANSACTION_uninstall = 11;
        static final int TRANSACTION_setPermissionsResult = 12;

        public Stub() {
            this.attachInterface(this, "com.lody.virtual.server.IPackageInstaller");
        }

        public static IPackageInstaller asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && iin instanceof IPackageInstaller) {
                return (IPackageInstaller)iin;
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
                    SessionParams _arg0 = 0 != data.readInt() ? (SessionParams)SessionParams.CREATOR.createFromParcel(data) : null;
                    String _arg1 = data.readString();
                    int _arg2 = data.readInt();
                    int _result = this.createSession(_arg0, _arg1, _arg2);
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                }
                case 2: {
                    data.enforceInterface(descriptor);
                    int _arg0 = data.readInt();
                    Bitmap _arg1 = 0 != data.readInt() ? (Bitmap)Bitmap.CREATOR.createFromParcel(data) : null;
                    this.updateSessionAppIcon(_arg0, _arg1);
                    reply.writeNoException();
                    return true;
                }
                case 3: {
                    data.enforceInterface(descriptor);
                    int _arg0 = data.readInt();
                    String _arg1 = data.readString();
                    this.updateSessionAppLabel(_arg0, _arg1);
                    reply.writeNoException();
                    return true;
                }
                case 4: {
                    data.enforceInterface(descriptor);
                    int _arg0 = data.readInt();
                    this.abandonSession(_arg0);
                    reply.writeNoException();
                    return true;
                }
                case 5: {
                    data.enforceInterface(descriptor);
                    int _arg0 = data.readInt();
                    IPackageInstallerSession _result = this.openSession(_arg0);
                    reply.writeNoException();
                    reply.writeStrongBinder(_result != null ? _result.asBinder() : null);
                    return true;
                }
                case 6: {
                    data.enforceInterface(descriptor);
                    int _arg0 = data.readInt();
                    SessionInfo _result = this.getSessionInfo(_arg0);
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
                    int _arg0 = data.readInt();
                    VParceledListSlice _result = this.getAllSessions(_arg0);
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
                    String _arg0 = data.readString();
                    int _arg1 = data.readInt();
                    VParceledListSlice _result = this.getMySessions(_arg0, _arg1);
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
                    IPackageInstallerCallback _arg0 = IPackageInstallerCallback.Stub.asInterface(data.readStrongBinder());
                    int _arg1 = data.readInt();
                    this.registerCallback(_arg0, _arg1);
                    reply.writeNoException();
                    return true;
                }
                case 10: {
                    data.enforceInterface(descriptor);
                    IPackageInstallerCallback _arg0 = IPackageInstallerCallback.Stub.asInterface(data.readStrongBinder());
                    this.unregisterCallback(_arg0);
                    reply.writeNoException();
                    return true;
                }
                case 11: {
                    data.enforceInterface(descriptor);
                    String _arg0 = data.readString();
                    String _arg1 = data.readString();
                    int _arg2 = data.readInt();
                    IntentSender _arg3 = 0 != data.readInt() ? (IntentSender)IntentSender.CREATOR.createFromParcel(data) : null;
                    int _arg4 = data.readInt();
                    this.uninstall(_arg0, _arg1, _arg2, _arg3, _arg4);
                    reply.writeNoException();
                    return true;
                }
                case 12: {
                    data.enforceInterface(descriptor);
                    int _arg0 = data.readInt();
                    boolean _arg1 = 0 != data.readInt();
                    this.setPermissionsResult(_arg0, _arg1);
                    reply.writeNoException();
                    return true;
                }
            }
            return super.onTransact(code, data, reply, flags);
        }

        public static boolean setDefaultImpl(IPackageInstaller impl) {
            if (Proxy.sDefaultImpl != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            }
            if (impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IPackageInstaller getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }

        private static class Proxy
        implements IPackageInstaller {
            private IBinder mRemote;
            public static IPackageInstaller sDefaultImpl;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return "com.lody.virtual.server.IPackageInstaller";
            }

            /*
             * WARNING - Removed try catching itself - possible behaviour change.
             */
            @Override
            public int createSession(SessionParams params, String installerPackageName, int userId) throws RemoteException {
                int _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.IPackageInstaller");
                    if (params != null) {
                        _data.writeInt(1);
                        params.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(installerPackageName);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        int n = Stub.getDefaultImpl().createSession(params, installerPackageName, userId);
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
            public void updateSessionAppIcon(int sessionId, Bitmap appIcon) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.IPackageInstaller");
                    _data.writeInt(sessionId);
                    if (appIcon != null) {
                        _data.writeInt(1);
                        appIcon.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().updateSessionAppIcon(sessionId, appIcon);
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
            public void updateSessionAppLabel(int sessionId, String appLabel) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.IPackageInstaller");
                    _data.writeInt(sessionId);
                    _data.writeString(appLabel);
                    boolean _status = this.mRemote.transact(3, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().updateSessionAppLabel(sessionId, appLabel);
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
            public void abandonSession(int sessionId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.IPackageInstaller");
                    _data.writeInt(sessionId);
                    boolean _status = this.mRemote.transact(4, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().abandonSession(sessionId);
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
            public IPackageInstallerSession openSession(int sessionId) throws RemoteException {
                IPackageInstallerSession _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.IPackageInstaller");
                    _data.writeInt(sessionId);
                    boolean _status = this.mRemote.transact(5, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        IPackageInstallerSession iPackageInstallerSession = Stub.getDefaultImpl().openSession(sessionId);
                        return iPackageInstallerSession;
                    }
                    _reply.readException();
                    _result = IPackageInstallerSession.Stub.asInterface(_reply.readStrongBinder());
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
            public SessionInfo getSessionInfo(int sessionId) throws RemoteException {
                SessionInfo _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.IPackageInstaller");
                    _data.writeInt(sessionId);
                    boolean _status = this.mRemote.transact(6, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        SessionInfo sessionInfo = Stub.getDefaultImpl().getSessionInfo(sessionId);
                        return sessionInfo;
                    }
                    _reply.readException();
                    _result = 0 != _reply.readInt() ? (SessionInfo)SessionInfo.CREATOR.createFromParcel(_reply) : null;
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
            public VParceledListSlice getAllSessions(int userId) throws RemoteException {
                VParceledListSlice _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.IPackageInstaller");
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(7, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        VParceledListSlice vParceledListSlice = Stub.getDefaultImpl().getAllSessions(userId);
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
            public VParceledListSlice getMySessions(String installerPackageName, int userId) throws RemoteException {
                VParceledListSlice _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.IPackageInstaller");
                    _data.writeString(installerPackageName);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(8, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        VParceledListSlice vParceledListSlice = Stub.getDefaultImpl().getMySessions(installerPackageName, userId);
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
            public void registerCallback(IPackageInstallerCallback callback, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.IPackageInstaller");
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(9, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().registerCallback(callback, userId);
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
            public void unregisterCallback(IPackageInstallerCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.IPackageInstaller");
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(10, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().unregisterCallback(callback);
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
            public void uninstall(String packageName, String callerPackageName, int flags, IntentSender statusReceiver, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.IPackageInstaller");
                    _data.writeString(packageName);
                    _data.writeString(callerPackageName);
                    _data.writeInt(flags);
                    if (statusReceiver != null) {
                        _data.writeInt(1);
                        statusReceiver.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(11, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().uninstall(packageName, callerPackageName, flags, statusReceiver, userId);
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
            public void setPermissionsResult(int sessionId, boolean accepted) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.IPackageInstaller");
                    _data.writeInt(sessionId);
                    _data.writeInt(accepted ? 1 : 0);
                    boolean _status = this.mRemote.transact(12, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setPermissionsResult(sessionId, accepted);
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
    implements IPackageInstaller {
        @Override
        public int createSession(SessionParams params, String installerPackageName, int userId) throws RemoteException {
            return 0;
        }

        @Override
        public void updateSessionAppIcon(int sessionId, Bitmap appIcon) throws RemoteException {
        }

        @Override
        public void updateSessionAppLabel(int sessionId, String appLabel) throws RemoteException {
        }

        @Override
        public void abandonSession(int sessionId) throws RemoteException {
        }

        @Override
        public IPackageInstallerSession openSession(int sessionId) throws RemoteException {
            return null;
        }

        @Override
        public SessionInfo getSessionInfo(int sessionId) throws RemoteException {
            return null;
        }

        @Override
        public VParceledListSlice getAllSessions(int userId) throws RemoteException {
            return null;
        }

        @Override
        public VParceledListSlice getMySessions(String installerPackageName, int userId) throws RemoteException {
            return null;
        }

        @Override
        public void registerCallback(IPackageInstallerCallback callback, int userId) throws RemoteException {
        }

        @Override
        public void unregisterCallback(IPackageInstallerCallback callback) throws RemoteException {
        }

        @Override
        public void uninstall(String packageName, String callerPackageName, int flags, IntentSender statusReceiver, int userId) throws RemoteException {
        }

        @Override
        public void setPermissionsResult(int sessionId, boolean accepted) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }
}

