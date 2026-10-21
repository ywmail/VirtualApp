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
package com.kook.controller.server.service;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.carlos.libcommon.StringFog;
import com.kook.controller.server.service.IServiceCallBack;

public interface IControllerService
extends IInterface {
    public boolean picCompare(String var1, String var2) throws RemoteException;

    public void startCheck(String var1, int var2) throws RemoteException;

    public void registerCallBack(IServiceCallBack var1) throws RemoteException;

    public void unregisterCallBack(IServiceCallBack var1) throws RemoteException;

    public void startFullWakeLock() throws RemoteException;

    public void eventDialogTips() throws RemoteException;

    public void setOptionAction(String var1) throws RemoteException;

    public String getOptionAction() throws RemoteException;

    public boolean killApp(String var1, String var2) throws RemoteException;

    public boolean isAppOnForeground() throws RemoteException;

    public boolean virtualClick(int var1, int var2) throws RemoteException;

    public boolean virtualTouch(int var1, int var2, int var3, int var4, boolean var5) throws RemoteException;

    public boolean sendKeyEvent(int var1) throws RemoteException;

    public static abstract class Stub
    extends Binder
    implements IControllerService {
        private static final String DESCRIPTOR = "com.kook.controller.server.service.IControllerService";
        static final int TRANSACTION_picCompare = 1;
        static final int TRANSACTION_startCheck = 2;
        static final int TRANSACTION_registerCallBack = 3;
        static final int TRANSACTION_unregisterCallBack = 4;
        static final int TRANSACTION_startFullWakeLock = 5;
        static final int TRANSACTION_eventDialogTips = 6;
        static final int TRANSACTION_setOptionAction = 7;
        static final int TRANSACTION_getOptionAction = 8;
        static final int TRANSACTION_killApp = 9;
        static final int TRANSACTION_isAppOnForeground = 10;
        static final int TRANSACTION_virtualClick = 11;
        static final int TRANSACTION_virtualTouch = 12;
        static final int TRANSACTION_sendKeyEvent = 13;

        public Stub() {
            this.attachInterface(this, "com.kook.controller.server.service.IControllerService");
        }

        public static IControllerService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && iin instanceof IControllerService) {
                return (IControllerService)iin;
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
                    boolean _result = this.picCompare(_arg0, _arg1);
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                }
                case 2: {
                    data.enforceInterface(descriptor);
                    String _arg0 = data.readString();
                    int _arg1 = data.readInt();
                    this.startCheck(_arg0, _arg1);
                    reply.writeNoException();
                    return true;
                }
                case 3: {
                    data.enforceInterface(descriptor);
                    IServiceCallBack _arg0 = IServiceCallBack.Stub.asInterface(data.readStrongBinder());
                    this.registerCallBack(_arg0);
                    reply.writeNoException();
                    return true;
                }
                case 4: {
                    data.enforceInterface(descriptor);
                    IServiceCallBack _arg0 = IServiceCallBack.Stub.asInterface(data.readStrongBinder());
                    this.unregisterCallBack(_arg0);
                    reply.writeNoException();
                    return true;
                }
                case 5: {
                    data.enforceInterface(descriptor);
                    this.startFullWakeLock();
                    reply.writeNoException();
                    return true;
                }
                case 6: {
                    data.enforceInterface(descriptor);
                    this.eventDialogTips();
                    reply.writeNoException();
                    return true;
                }
                case 7: {
                    data.enforceInterface(descriptor);
                    String _arg0 = data.readString();
                    this.setOptionAction(_arg0);
                    reply.writeNoException();
                    return true;
                }
                case 8: {
                    data.enforceInterface(descriptor);
                    String _result = this.getOptionAction();
                    reply.writeNoException();
                    reply.writeString(_result);
                    return true;
                }
                case 9: {
                    data.enforceInterface(descriptor);
                    String _arg0 = data.readString();
                    String _arg1 = data.readString();
                    boolean _result = this.killApp(_arg0, _arg1);
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                }
                case 10: {
                    data.enforceInterface(descriptor);
                    boolean _result = this.isAppOnForeground();
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                }
                case 11: {
                    data.enforceInterface(descriptor);
                    int _arg0 = data.readInt();
                    int _arg1 = data.readInt();
                    boolean _result = this.virtualClick(_arg0, _arg1);
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                }
                case 12: {
                    data.enforceInterface(descriptor);
                    int _arg0 = data.readInt();
                    int _arg1 = data.readInt();
                    int _arg2 = data.readInt();
                    int _arg3 = data.readInt();
                    boolean _arg4 = 0 != data.readInt();
                    boolean _result = this.virtualTouch(_arg0, _arg1, _arg2, _arg3, _arg4);
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                }
                case 13: {
                    data.enforceInterface(descriptor);
                    int _arg0 = data.readInt();
                    boolean _result = this.sendKeyEvent(_arg0);
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                }
            }
            return super.onTransact(code, data, reply, flags);
        }

        public static boolean setDefaultImpl(IControllerService impl) {
            if (Proxy.sDefaultImpl != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            }
            if (impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IControllerService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }

        private static class Proxy
        implements IControllerService {
            private IBinder mRemote;
            public static IControllerService sDefaultImpl;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return "com.kook.controller.server.service.IControllerService";
            }

            /*
             * WARNING - Removed try catching itself - possible behaviour change.
             */
            @Override
            public boolean picCompare(String picPathSource, String picPathTarget) throws RemoteException {
                boolean _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.kook.controller.server.service.IControllerService");
                    _data.writeString(picPathSource);
                    _data.writeString(picPathTarget);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        boolean bl = Stub.getDefaultImpl().picCompare(picPathSource, picPathTarget);
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
            public void startCheck(String productPicPathSource, int targetProductIndex) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.kook.controller.server.service.IControllerService");
                    _data.writeString(productPicPathSource);
                    _data.writeInt(targetProductIndex);
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().startCheck(productPicPathSource, targetProductIndex);
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
            public void registerCallBack(IServiceCallBack serviceCallBack) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.kook.controller.server.service.IControllerService");
                    _data.writeStrongBinder(serviceCallBack != null ? serviceCallBack.asBinder() : null);
                    boolean _status = this.mRemote.transact(3, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().registerCallBack(serviceCallBack);
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
            public void unregisterCallBack(IServiceCallBack serviceCallBack) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.kook.controller.server.service.IControllerService");
                    _data.writeStrongBinder(serviceCallBack != null ? serviceCallBack.asBinder() : null);
                    boolean _status = this.mRemote.transact(4, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().unregisterCallBack(serviceCallBack);
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
            public void startFullWakeLock() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.kook.controller.server.service.IControllerService");
                    boolean _status = this.mRemote.transact(5, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().startFullWakeLock();
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
            public void eventDialogTips() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.kook.controller.server.service.IControllerService");
                    boolean _status = this.mRemote.transact(6, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().eventDialogTips();
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
            public void setOptionAction(String action) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.kook.controller.server.service.IControllerService");
                    _data.writeString(action);
                    boolean _status = this.mRemote.transact(7, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setOptionAction(action);
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
            public String getOptionAction() throws RemoteException {
                String _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.kook.controller.server.service.IControllerService");
                    boolean _status = this.mRemote.transact(8, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        String string2 = Stub.getDefaultImpl().getOptionAction();
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
            public boolean killApp(String pkg, String processName) throws RemoteException {
                boolean _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.kook.controller.server.service.IControllerService");
                    _data.writeString(pkg);
                    _data.writeString(processName);
                    boolean _status = this.mRemote.transact(9, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        boolean bl = Stub.getDefaultImpl().killApp(pkg, processName);
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
            public boolean isAppOnForeground() throws RemoteException {
                boolean _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.kook.controller.server.service.IControllerService");
                    boolean _status = this.mRemote.transact(10, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        boolean bl = Stub.getDefaultImpl().isAppOnForeground();
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
            public boolean virtualClick(int centerX, int centerY) throws RemoteException {
                boolean _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.kook.controller.server.service.IControllerService");
                    _data.writeInt(centerX);
                    _data.writeInt(centerY);
                    boolean _status = this.mRemote.transact(11, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        boolean bl = Stub.getDefaultImpl().virtualClick(centerX, centerY);
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
            public boolean virtualTouch(int fromX, int fromY, int toX, int toY, boolean direction) throws RemoteException {
                boolean _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.kook.controller.server.service.IControllerService");
                    _data.writeInt(fromX);
                    _data.writeInt(fromY);
                    _data.writeInt(toX);
                    _data.writeInt(toY);
                    _data.writeInt(direction ? 1 : 0);
                    boolean _status = this.mRemote.transact(12, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        boolean bl = Stub.getDefaultImpl().virtualTouch(fromX, fromY, toX, toY, direction);
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
            public boolean sendKeyEvent(int keycode) throws RemoteException {
                boolean _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.kook.controller.server.service.IControllerService");
                    _data.writeInt(keycode);
                    boolean _status = this.mRemote.transact(13, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        boolean bl = Stub.getDefaultImpl().sendKeyEvent(keycode);
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
    implements IControllerService {
        @Override
        public boolean picCompare(String picPathSource, String picPathTarget) throws RemoteException {
            return false;
        }

        @Override
        public void startCheck(String productPicPathSource, int targetProductIndex) throws RemoteException {
        }

        @Override
        public void registerCallBack(IServiceCallBack serviceCallBack) throws RemoteException {
        }

        @Override
        public void unregisterCallBack(IServiceCallBack serviceCallBack) throws RemoteException {
        }

        @Override
        public void startFullWakeLock() throws RemoteException {
        }

        @Override
        public void eventDialogTips() throws RemoteException {
        }

        @Override
        public void setOptionAction(String action) throws RemoteException {
        }

        @Override
        public String getOptionAction() throws RemoteException {
            return null;
        }

        @Override
        public boolean killApp(String pkg, String processName) throws RemoteException {
            return false;
        }

        @Override
        public boolean isAppOnForeground() throws RemoteException {
            return false;
        }

        @Override
        public boolean virtualClick(int centerX, int centerY) throws RemoteException {
            return false;
        }

        @Override
        public boolean virtualTouch(int fromX, int fromY, int toX, int toY, boolean direction) throws RemoteException {
            return false;
        }

        @Override
        public boolean sendKeyEvent(int keycode) throws RemoteException {
            return false;
        }

        public IBinder asBinder() {
            return null;
        }
    }
}

