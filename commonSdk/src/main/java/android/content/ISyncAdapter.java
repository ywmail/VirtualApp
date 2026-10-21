/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.accounts.Account
 *  android.os.Binder
 *  android.os.Bundle
 *  android.os.IBinder
 *  android.os.IInterface
 *  android.os.Parcel
 *  android.os.RemoteException
 */
package android.content;

import android.accounts.Account;
import android.content.ISyncContext;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.lody.virtual.StringFog;

public interface ISyncAdapter
extends IInterface {
    public void startSync(ISyncContext var1, String var2, Account var3, Bundle var4) throws RemoteException;

    public void cancelSync(ISyncContext var1) throws RemoteException;

    public void initialize(Account var1, String var2) throws RemoteException;

    public static abstract class Stub
    extends Binder
    implements ISyncAdapter {
        private static final String DESCRIPTOR = "android.content.ISyncAdapter";
        static final int TRANSACTION_startSync = 1;
        static final int TRANSACTION_cancelSync = 2;
        static final int TRANSACTION_initialize = 3;

        public Stub() {
            this.attachInterface(this, "android.content.ISyncAdapter");
        }

        public static ISyncAdapter asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && iin instanceof ISyncAdapter) {
                return (ISyncAdapter)iin;
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
                    ISyncContext _arg0 = ISyncContext.Stub.asInterface(data.readStrongBinder());
                    String _arg1 = data.readString();
                    Account _arg2 = 0 != data.readInt() ? (Account)Account.CREATOR.createFromParcel(data) : null;
                    Bundle _arg3 = 0 != data.readInt() ? (Bundle)Bundle.CREATOR.createFromParcel(data) : null;
                    this.startSync(_arg0, _arg1, _arg2, _arg3);
                    reply.writeNoException();
                    return true;
                }
                case 2: {
                    data.enforceInterface(descriptor);
                    ISyncContext _arg0 = ISyncContext.Stub.asInterface(data.readStrongBinder());
                    this.cancelSync(_arg0);
                    reply.writeNoException();
                    return true;
                }
                case 3: {
                    data.enforceInterface(descriptor);
                    Account _arg0 = 0 != data.readInt() ? (Account)Account.CREATOR.createFromParcel(data) : null;
                    String _arg1 = data.readString();
                    this.initialize(_arg0, _arg1);
                    reply.writeNoException();
                    return true;
                }
            }
            return super.onTransact(code, data, reply, flags);
        }

        public static boolean setDefaultImpl(ISyncAdapter impl) {
            if (Proxy.sDefaultImpl != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            }
            if (impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static ISyncAdapter getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }

        private static class Proxy
        implements ISyncAdapter {
            private IBinder mRemote;
            public static ISyncAdapter sDefaultImpl;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return "android.content.ISyncAdapter";
            }

            /*
             * WARNING - Removed try catching itself - possible behaviour change.
             */
            @Override
            public void startSync(ISyncContext syncContext, String authority, Account account, Bundle extras) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("android.content.ISyncAdapter");
                    _data.writeStrongBinder(syncContext != null ? syncContext.asBinder() : null);
                    _data.writeString(authority);
                    if (account != null) {
                        _data.writeInt(1);
                        account.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (extras != null) {
                        _data.writeInt(1);
                        extras.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().startSync(syncContext, authority, account, extras);
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
            public void cancelSync(ISyncContext syncContext) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("android.content.ISyncAdapter");
                    _data.writeStrongBinder(syncContext != null ? syncContext.asBinder() : null);
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().cancelSync(syncContext);
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
            public void initialize(Account account, String authority) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("android.content.ISyncAdapter");
                    if (account != null) {
                        _data.writeInt(1);
                        account.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(authority);
                    boolean _status = this.mRemote.transact(3, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().initialize(account, authority);
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
    implements ISyncAdapter {
        @Override
        public void startSync(ISyncContext syncContext, String authority, Account account, Bundle extras) throws RemoteException {
        }

        @Override
        public void cancelSync(ISyncContext syncContext) throws RemoteException {
        }

        @Override
        public void initialize(Account account, String authority) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }
}

