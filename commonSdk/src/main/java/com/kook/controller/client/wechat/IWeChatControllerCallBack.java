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
package com.kook.controller.client.wechat;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.carlos.libcommon.StringFog;
import java.util.HashMap;
import java.util.Map;

public interface IWeChatControllerCallBack
extends IInterface {
    public void syncScanGroupChatData(Map var1) throws RemoteException;

    public int getSendChatPosition() throws RemoteException;

    public String getSendChatName(int var1) throws RemoteException;

    public String getSendChatMessage() throws RemoteException;

    public void setSendChatMsgSuccess(int var1, boolean var2) throws RemoteException;

    public static abstract class Stub
    extends Binder
    implements IWeChatControllerCallBack {
        private static final String DESCRIPTOR = "com.kook.controller.client.wechat.IWeChatControllerCallBack";
        static final int TRANSACTION_syncScanGroupChatData = 1;
        static final int TRANSACTION_getSendChatPosition = 2;
        static final int TRANSACTION_getSendChatName = 3;
        static final int TRANSACTION_getSendChatMessage = 4;
        static final int TRANSACTION_setSendChatMsgSuccess = 5;

        public Stub() {
            this.attachInterface(this, "com.kook.controller.client.wechat.IWeChatControllerCallBack");
        }

        public static IWeChatControllerCallBack asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && iin instanceof IWeChatControllerCallBack) {
                return (IWeChatControllerCallBack)iin;
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
                    ClassLoader cl = this.getClass().getClassLoader();
                    HashMap _arg0 = data.readHashMap(cl);
                    this.syncScanGroupChatData(_arg0);
                    reply.writeNoException();
                    return true;
                }
                case 2: {
                    data.enforceInterface(descriptor);
                    int _result = this.getSendChatPosition();
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                }
                case 3: {
                    data.enforceInterface(descriptor);
                    int _arg0 = data.readInt();
                    String _result = this.getSendChatName(_arg0);
                    reply.writeNoException();
                    reply.writeString(_result);
                    return true;
                }
                case 4: {
                    data.enforceInterface(descriptor);
                    String _result = this.getSendChatMessage();
                    reply.writeNoException();
                    reply.writeString(_result);
                    return true;
                }
                case 5: {
                    data.enforceInterface(descriptor);
                    int _arg0 = data.readInt();
                    boolean _arg1 = 0 != data.readInt();
                    this.setSendChatMsgSuccess(_arg0, _arg1);
                    reply.writeNoException();
                    return true;
                }
            }
            return super.onTransact(code, data, reply, flags);
        }

        public static boolean setDefaultImpl(IWeChatControllerCallBack impl) {
            if (Proxy.sDefaultImpl != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            }
            if (impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IWeChatControllerCallBack getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }

        private static class Proxy
        implements IWeChatControllerCallBack {
            private IBinder mRemote;
            public static IWeChatControllerCallBack sDefaultImpl;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return "com.kook.controller.client.wechat.IWeChatControllerCallBack";
            }

            /*
             * WARNING - Removed try catching itself - possible behaviour change.
             */
            @Override
            public void syncScanGroupChatData(Map groupChat) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.kook.controller.client.wechat.IWeChatControllerCallBack");
                    _data.writeMap(groupChat);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().syncScanGroupChatData(groupChat);
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
            public int getSendChatPosition() throws RemoteException {
                int _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.kook.controller.client.wechat.IWeChatControllerCallBack");
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        int n = Stub.getDefaultImpl().getSendChatPosition();
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
            public String getSendChatName(int position) throws RemoteException {
                String _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.kook.controller.client.wechat.IWeChatControllerCallBack");
                    _data.writeInt(position);
                    boolean _status = this.mRemote.transact(3, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        String string2 = Stub.getDefaultImpl().getSendChatName(position);
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
            public String getSendChatMessage() throws RemoteException {
                String _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.kook.controller.client.wechat.IWeChatControllerCallBack");
                    boolean _status = this.mRemote.transact(4, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        String string2 = Stub.getDefaultImpl().getSendChatMessage();
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
            public void setSendChatMsgSuccess(int position, boolean success) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.kook.controller.client.wechat.IWeChatControllerCallBack");
                    _data.writeInt(position);
                    _data.writeInt(success ? 1 : 0);
                    boolean _status = this.mRemote.transact(5, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setSendChatMsgSuccess(position, success);
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
    implements IWeChatControllerCallBack {
        @Override
        public void syncScanGroupChatData(Map groupChat) throws RemoteException {
        }

        @Override
        public int getSendChatPosition() throws RemoteException {
            return 0;
        }

        @Override
        public String getSendChatName(int position) throws RemoteException {
            return null;
        }

        @Override
        public String getSendChatMessage() throws RemoteException {
            return null;
        }

        @Override
        public void setSendChatMsgSuccess(int position, boolean success) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }
}

