/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.accounts.Account
 *  android.content.PeriodicSync
 *  android.content.SyncAdapterType
 *  android.content.SyncRequest
 *  android.net.Uri
 *  android.os.Binder
 *  android.os.Bundle
 *  android.os.IBinder
 *  android.os.IInterface
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.RemoteException
 */
package com.lody.virtual.server.interfaces;

import android.accounts.Account;
import android.content.ISyncStatusObserver;
import android.content.PeriodicSync;
import android.content.SyncAdapterType;
import android.content.SyncInfo;
import android.content.SyncRequest;
import android.content.SyncStatusInfo;
import android.database.IContentObserver;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
import com.lody.virtual.StringFog;
import java.util.ArrayList;
import java.util.List;

public interface IContentService
extends IInterface {
    public void unregisterContentObserver(IContentObserver var1) throws RemoteException;

    public void registerContentObserver(Uri var1, boolean var2, IContentObserver var3, int var4) throws RemoteException;

    public void notifyChange(Uri var1, IContentObserver var2, boolean var3, boolean var4, int var5) throws RemoteException;

    public void requestSync(Account var1, String var2, Bundle var3) throws RemoteException;

    public void sync(SyncRequest var1) throws RemoteException;

    public void cancelSync(Account var1, String var2) throws RemoteException;

    public boolean getSyncAutomatically(Account var1, String var2) throws RemoteException;

    public void setSyncAutomatically(Account var1, String var2, boolean var3) throws RemoteException;

    public List<PeriodicSync> getPeriodicSyncs(Account var1, String var2) throws RemoteException;

    public void addPeriodicSync(Account var1, String var2, Bundle var3, long var4) throws RemoteException;

    public void removePeriodicSync(Account var1, String var2, Bundle var3) throws RemoteException;

    public int getIsSyncable(Account var1, String var2) throws RemoteException;

    public void setIsSyncable(Account var1, String var2, int var3) throws RemoteException;

    public void setMasterSyncAutomatically(boolean var1) throws RemoteException;

    public boolean getMasterSyncAutomatically() throws RemoteException;

    public boolean isSyncActive(Account var1, String var2) throws RemoteException;

    public List<SyncInfo> getCurrentSyncs() throws RemoteException;

    public SyncAdapterType[] getSyncAdapterTypes() throws RemoteException;

    public SyncStatusInfo getSyncStatus(Account var1, String var2) throws RemoteException;

    public boolean isSyncPending(Account var1, String var2) throws RemoteException;

    public void addStatusChangeListener(int var1, ISyncStatusObserver var2) throws RemoteException;

    public void removeStatusChangeListener(ISyncStatusObserver var1) throws RemoteException;

    public static abstract class Stub
    extends Binder
    implements IContentService {
        private static final String DESCRIPTOR = "com.lody.virtual.server.interfaces.IContentService";
        static final int TRANSACTION_unregisterContentObserver = 1;
        static final int TRANSACTION_registerContentObserver = 2;
        static final int TRANSACTION_notifyChange = 3;
        static final int TRANSACTION_requestSync = 4;
        static final int TRANSACTION_sync = 5;
        static final int TRANSACTION_cancelSync = 6;
        static final int TRANSACTION_getSyncAutomatically = 7;
        static final int TRANSACTION_setSyncAutomatically = 8;
        static final int TRANSACTION_getPeriodicSyncs = 9;
        static final int TRANSACTION_addPeriodicSync = 10;
        static final int TRANSACTION_removePeriodicSync = 11;
        static final int TRANSACTION_getIsSyncable = 12;
        static final int TRANSACTION_setIsSyncable = 13;
        static final int TRANSACTION_setMasterSyncAutomatically = 14;
        static final int TRANSACTION_getMasterSyncAutomatically = 15;
        static final int TRANSACTION_isSyncActive = 16;
        static final int TRANSACTION_getCurrentSyncs = 17;
        static final int TRANSACTION_getSyncAdapterTypes = 18;
        static final int TRANSACTION_getSyncStatus = 19;
        static final int TRANSACTION_isSyncPending = 20;
        static final int TRANSACTION_addStatusChangeListener = 21;
        static final int TRANSACTION_removeStatusChangeListener = 22;

        public Stub() {
            this.attachInterface(this, "com.lody.virtual.server.interfaces.IContentService");
        }

        public static IContentService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && iin instanceof IContentService) {
                return (IContentService)iin;
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
                    IContentObserver _arg0 = IContentObserver.Stub.asInterface(data.readStrongBinder());
                    this.unregisterContentObserver(_arg0);
                    reply.writeNoException();
                    return true;
                }
                case 2: {
                    data.enforceInterface(descriptor);
                    Uri _arg0 = 0 != data.readInt() ? (Uri)Uri.CREATOR.createFromParcel(data) : null;
                    boolean _arg1 = 0 != data.readInt();
                    IContentObserver _arg2 = IContentObserver.Stub.asInterface(data.readStrongBinder());
                    int _arg3 = data.readInt();
                    this.registerContentObserver(_arg0, _arg1, _arg2, _arg3);
                    reply.writeNoException();
                    return true;
                }
                case 3: {
                    data.enforceInterface(descriptor);
                    Uri _arg0 = 0 != data.readInt() ? (Uri)Uri.CREATOR.createFromParcel(data) : null;
                    IContentObserver _arg1 = IContentObserver.Stub.asInterface(data.readStrongBinder());
                    boolean _arg2 = 0 != data.readInt();
                    boolean _arg3 = 0 != data.readInt();
                    int _arg4 = data.readInt();
                    this.notifyChange(_arg0, _arg1, _arg2, _arg3, _arg4);
                    reply.writeNoException();
                    return true;
                }
                case 4: {
                    data.enforceInterface(descriptor);
                    Account _arg0 = 0 != data.readInt() ? (Account)Account.CREATOR.createFromParcel(data) : null;
                    String _arg1 = data.readString();
                    Bundle _arg2 = 0 != data.readInt() ? (Bundle)Bundle.CREATOR.createFromParcel(data) : null;
                    this.requestSync(_arg0, _arg1, _arg2);
                    reply.writeNoException();
                    return true;
                }
                case 5: {
                    data.enforceInterface(descriptor);
                    SyncRequest _arg0 = 0 != data.readInt() ? (SyncRequest)SyncRequest.CREATOR.createFromParcel(data) : null;
                    this.sync(_arg0);
                    reply.writeNoException();
                    return true;
                }
                case 6: {
                    data.enforceInterface(descriptor);
                    Account _arg0 = 0 != data.readInt() ? (Account)Account.CREATOR.createFromParcel(data) : null;
                    String _arg1 = data.readString();
                    this.cancelSync(_arg0, _arg1);
                    reply.writeNoException();
                    return true;
                }
                case 7: {
                    data.enforceInterface(descriptor);
                    Account _arg0 = 0 != data.readInt() ? (Account)Account.CREATOR.createFromParcel(data) : null;
                    String _arg1 = data.readString();
                    boolean _result = this.getSyncAutomatically(_arg0, _arg1);
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                }
                case 8: {
                    data.enforceInterface(descriptor);
                    Account _arg0 = 0 != data.readInt() ? (Account)Account.CREATOR.createFromParcel(data) : null;
                    String _arg1 = data.readString();
                    boolean _arg2 = 0 != data.readInt();
                    this.setSyncAutomatically(_arg0, _arg1, _arg2);
                    reply.writeNoException();
                    return true;
                }
                case 9: {
                    data.enforceInterface(descriptor);
                    Account _arg0 = 0 != data.readInt() ? (Account)Account.CREATOR.createFromParcel(data) : null;
                    String _arg1 = data.readString();
                    List _result = this.getPeriodicSyncs(_arg0, _arg1);
                    reply.writeNoException();
                    reply.writeTypedList(_result);
                    return true;
                }
                case 10: {
                    data.enforceInterface(descriptor);
                    Account _arg0 = 0 != data.readInt() ? (Account)Account.CREATOR.createFromParcel(data) : null;
                    String _arg1 = data.readString();
                    Bundle _arg2 = 0 != data.readInt() ? (Bundle)Bundle.CREATOR.createFromParcel(data) : null;
                    long _arg3 = data.readLong();
                    this.addPeriodicSync(_arg0, _arg1, _arg2, _arg3);
                    reply.writeNoException();
                    return true;
                }
                case 11: {
                    data.enforceInterface(descriptor);
                    Account _arg0 = 0 != data.readInt() ? (Account)Account.CREATOR.createFromParcel(data) : null;
                    String _arg1 = data.readString();
                    Bundle _arg2 = 0 != data.readInt() ? (Bundle)Bundle.CREATOR.createFromParcel(data) : null;
                    this.removePeriodicSync(_arg0, _arg1, _arg2);
                    reply.writeNoException();
                    return true;
                }
                case 12: {
                    data.enforceInterface(descriptor);
                    Account _arg0 = 0 != data.readInt() ? (Account)Account.CREATOR.createFromParcel(data) : null;
                    String _arg1 = data.readString();
                    int _result = this.getIsSyncable(_arg0, _arg1);
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                }
                case 13: {
                    data.enforceInterface(descriptor);
                    Account _arg0 = 0 != data.readInt() ? (Account)Account.CREATOR.createFromParcel(data) : null;
                    String _arg1 = data.readString();
                    int _arg2 = data.readInt();
                    this.setIsSyncable(_arg0, _arg1, _arg2);
                    reply.writeNoException();
                    return true;
                }
                case 14: {
                    data.enforceInterface(descriptor);
                    boolean _arg0 = 0 != data.readInt();
                    this.setMasterSyncAutomatically(_arg0);
                    reply.writeNoException();
                    return true;
                }
                case 15: {
                    data.enforceInterface(descriptor);
                    boolean _result = this.getMasterSyncAutomatically();
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                }
                case 16: {
                    data.enforceInterface(descriptor);
                    Account _arg0 = 0 != data.readInt() ? (Account)Account.CREATOR.createFromParcel(data) : null;
                    String _arg1 = data.readString();
                    boolean _result = this.isSyncActive(_arg0, _arg1);
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                }
                case 17: {
                    data.enforceInterface(descriptor);
                    List _result = this.getCurrentSyncs();
                    reply.writeNoException();
                    reply.writeTypedList(_result);
                    return true;
                }
                case 18: {
                    data.enforceInterface(descriptor);
                    SyncAdapterType[] _result = this.getSyncAdapterTypes();
                    reply.writeNoException();
                    reply.writeTypedArray((Parcelable[])_result, 1);
                    return true;
                }
                case 19: {
                    data.enforceInterface(descriptor);
                    Account _arg0 = 0 != data.readInt() ? (Account)Account.CREATOR.createFromParcel(data) : null;
                    String _arg1 = data.readString();
                    SyncStatusInfo _result = this.getSyncStatus(_arg0, _arg1);
                    reply.writeNoException();
                    if (_result != null) {
                        reply.writeInt(1);
                        _result.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                }
                case 20: {
                    data.enforceInterface(descriptor);
                    Account _arg0 = 0 != data.readInt() ? (Account)Account.CREATOR.createFromParcel(data) : null;
                    String _arg1 = data.readString();
                    boolean _result = this.isSyncPending(_arg0, _arg1);
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                }
                case 21: {
                    data.enforceInterface(descriptor);
                    int _arg0 = data.readInt();
                    ISyncStatusObserver _arg1 = ISyncStatusObserver.Stub.asInterface(data.readStrongBinder());
                    this.addStatusChangeListener(_arg0, _arg1);
                    reply.writeNoException();
                    return true;
                }
                case 22: {
                    data.enforceInterface(descriptor);
                    ISyncStatusObserver _arg0 = ISyncStatusObserver.Stub.asInterface(data.readStrongBinder());
                    this.removeStatusChangeListener(_arg0);
                    reply.writeNoException();
                    return true;
                }
            }
            return super.onTransact(code, data, reply, flags);
        }

        public static boolean setDefaultImpl(IContentService impl) {
            if (Proxy.sDefaultImpl != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            }
            if (impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IContentService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }

        private static class Proxy
        implements IContentService {
            private IBinder mRemote;
            public static IContentService sDefaultImpl;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return "com.lody.virtual.server.interfaces.IContentService";
            }

            /*
             * WARNING - Removed try catching itself - possible behaviour change.
             */
            @Override
            public void unregisterContentObserver(IContentObserver observer) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IContentService");
                    _data.writeStrongBinder(observer != null ? observer.asBinder() : null);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().unregisterContentObserver(observer);
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
            public void registerContentObserver(Uri uri, boolean notifyForDescendants, IContentObserver observer, int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IContentService");
                    if (uri != null) {
                        _data.writeInt(1);
                        uri.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(notifyForDescendants ? 1 : 0);
                    _data.writeStrongBinder(observer != null ? observer.asBinder() : null);
                    _data.writeInt(userHandle);
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().registerContentObserver(uri, notifyForDescendants, observer, userHandle);
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
            public void notifyChange(Uri uri, IContentObserver observer, boolean observerWantsSelfNotifications, boolean syncToNetwork, int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IContentService");
                    if (uri != null) {
                        _data.writeInt(1);
                        uri.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(observer != null ? observer.asBinder() : null);
                    _data.writeInt(observerWantsSelfNotifications ? 1 : 0);
                    _data.writeInt(syncToNetwork ? 1 : 0);
                    _data.writeInt(userHandle);
                    boolean _status = this.mRemote.transact(3, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().notifyChange(uri, observer, observerWantsSelfNotifications, syncToNetwork, userHandle);
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
            public void requestSync(Account account, String authority, Bundle extras) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IContentService");
                    if (account != null) {
                        _data.writeInt(1);
                        account.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(authority);
                    if (extras != null) {
                        _data.writeInt(1);
                        extras.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(4, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().requestSync(account, authority, extras);
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
            public void sync(SyncRequest request) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IContentService");
                    if (request != null) {
                        _data.writeInt(1);
                        request.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(5, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().sync(request);
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
            public void cancelSync(Account account, String authority) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IContentService");
                    if (account != null) {
                        _data.writeInt(1);
                        account.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(authority);
                    boolean _status = this.mRemote.transact(6, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().cancelSync(account, authority);
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
            public boolean getSyncAutomatically(Account account, String providerName) throws RemoteException {
                boolean _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IContentService");
                    if (account != null) {
                        _data.writeInt(1);
                        account.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(providerName);
                    boolean _status = this.mRemote.transact(7, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        boolean bl = Stub.getDefaultImpl().getSyncAutomatically(account, providerName);
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
            public void setSyncAutomatically(Account account, String providerName, boolean sync) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IContentService");
                    if (account != null) {
                        _data.writeInt(1);
                        account.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(providerName);
                    _data.writeInt(sync ? 1 : 0);
                    boolean _status = this.mRemote.transact(8, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setSyncAutomatically(account, providerName, sync);
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
            public List<PeriodicSync> getPeriodicSyncs(Account account, String providerName) throws RemoteException {
                ArrayList _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IContentService");
                    if (account != null) {
                        _data.writeInt(1);
                        account.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(providerName);
                    boolean _status = this.mRemote.transact(9, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        List<PeriodicSync> list = Stub.getDefaultImpl().getPeriodicSyncs(account, providerName);
                        return list;
                    }
                    _reply.readException();
                    _result = _reply.createTypedArrayList(PeriodicSync.CREATOR);
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
            public void addPeriodicSync(Account account, String providerName, Bundle extras, long pollFrequency) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IContentService");
                    if (account != null) {
                        _data.writeInt(1);
                        account.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(providerName);
                    if (extras != null) {
                        _data.writeInt(1);
                        extras.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeLong(pollFrequency);
                    boolean _status = this.mRemote.transact(10, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().addPeriodicSync(account, providerName, extras, pollFrequency);
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
            public void removePeriodicSync(Account account, String providerName, Bundle extras) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IContentService");
                    if (account != null) {
                        _data.writeInt(1);
                        account.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(providerName);
                    if (extras != null) {
                        _data.writeInt(1);
                        extras.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(11, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().removePeriodicSync(account, providerName, extras);
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
            public int getIsSyncable(Account account, String providerName) throws RemoteException {
                int _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IContentService");
                    if (account != null) {
                        _data.writeInt(1);
                        account.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(providerName);
                    boolean _status = this.mRemote.transact(12, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        int n = Stub.getDefaultImpl().getIsSyncable(account, providerName);
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
            public void setIsSyncable(Account account, String providerName, int syncable) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IContentService");
                    if (account != null) {
                        _data.writeInt(1);
                        account.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(providerName);
                    _data.writeInt(syncable);
                    boolean _status = this.mRemote.transact(13, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setIsSyncable(account, providerName, syncable);
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
            public void setMasterSyncAutomatically(boolean flag) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IContentService");
                    _data.writeInt(flag ? 1 : 0);
                    boolean _status = this.mRemote.transact(14, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setMasterSyncAutomatically(flag);
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
            public boolean getMasterSyncAutomatically() throws RemoteException {
                boolean _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IContentService");
                    boolean _status = this.mRemote.transact(15, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        boolean bl = Stub.getDefaultImpl().getMasterSyncAutomatically();
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
            public boolean isSyncActive(Account account, String authority) throws RemoteException {
                boolean _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IContentService");
                    if (account != null) {
                        _data.writeInt(1);
                        account.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(authority);
                    boolean _status = this.mRemote.transact(16, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        boolean bl = Stub.getDefaultImpl().isSyncActive(account, authority);
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
            public List<SyncInfo> getCurrentSyncs() throws RemoteException {
                ArrayList _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IContentService");
                    boolean _status = this.mRemote.transact(17, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        List<SyncInfo> list = Stub.getDefaultImpl().getCurrentSyncs();
                        return list;
                    }
                    _reply.readException();
                    _result = _reply.createTypedArrayList(SyncInfo.CREATOR);
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
            public SyncAdapterType[] getSyncAdapterTypes() throws RemoteException {
                SyncAdapterType[] _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IContentService");
                    boolean _status = this.mRemote.transact(18, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        SyncAdapterType[] syncAdapterTypeArray = Stub.getDefaultImpl().getSyncAdapterTypes();
                        return syncAdapterTypeArray;
                    }
                    _reply.readException();
                    _result = (SyncAdapterType[])_reply.createTypedArray(SyncAdapterType.CREATOR);
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
            public SyncStatusInfo getSyncStatus(Account account, String authority) throws RemoteException {
                SyncStatusInfo _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IContentService");
                    if (account != null) {
                        _data.writeInt(1);
                        account.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(authority);
                    boolean _status = this.mRemote.transact(19, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        SyncStatusInfo syncStatusInfo = Stub.getDefaultImpl().getSyncStatus(account, authority);
                        return syncStatusInfo;
                    }
                    _reply.readException();
                    _result = 0 != _reply.readInt() ? (SyncStatusInfo)SyncStatusInfo.CREATOR.createFromParcel(_reply) : null;
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
            public boolean isSyncPending(Account account, String authority) throws RemoteException {
                boolean _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IContentService");
                    if (account != null) {
                        _data.writeInt(1);
                        account.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(authority);
                    boolean _status = this.mRemote.transact(20, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        boolean bl = Stub.getDefaultImpl().isSyncPending(account, authority);
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
            public void addStatusChangeListener(int mask, ISyncStatusObserver callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IContentService");
                    _data.writeInt(mask);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(21, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().addStatusChangeListener(mask, callback);
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
            public void removeStatusChangeListener(ISyncStatusObserver callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IContentService");
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(22, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().removeStatusChangeListener(callback);
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
    implements IContentService {
        @Override
        public void unregisterContentObserver(IContentObserver observer) throws RemoteException {
        }

        @Override
        public void registerContentObserver(Uri uri, boolean notifyForDescendants, IContentObserver observer, int userHandle) throws RemoteException {
        }

        @Override
        public void notifyChange(Uri uri, IContentObserver observer, boolean observerWantsSelfNotifications, boolean syncToNetwork, int userHandle) throws RemoteException {
        }

        @Override
        public void requestSync(Account account, String authority, Bundle extras) throws RemoteException {
        }

        @Override
        public void sync(SyncRequest request) throws RemoteException {
        }

        @Override
        public void cancelSync(Account account, String authority) throws RemoteException {
        }

        @Override
        public boolean getSyncAutomatically(Account account, String providerName) throws RemoteException {
            return false;
        }

        @Override
        public void setSyncAutomatically(Account account, String providerName, boolean sync) throws RemoteException {
        }

        @Override
        public List<PeriodicSync> getPeriodicSyncs(Account account, String providerName) throws RemoteException {
            return null;
        }

        @Override
        public void addPeriodicSync(Account account, String providerName, Bundle extras, long pollFrequency) throws RemoteException {
        }

        @Override
        public void removePeriodicSync(Account account, String providerName, Bundle extras) throws RemoteException {
        }

        @Override
        public int getIsSyncable(Account account, String providerName) throws RemoteException {
            return 0;
        }

        @Override
        public void setIsSyncable(Account account, String providerName, int syncable) throws RemoteException {
        }

        @Override
        public void setMasterSyncAutomatically(boolean flag) throws RemoteException {
        }

        @Override
        public boolean getMasterSyncAutomatically() throws RemoteException {
            return false;
        }

        @Override
        public boolean isSyncActive(Account account, String authority) throws RemoteException {
            return false;
        }

        @Override
        public List<SyncInfo> getCurrentSyncs() throws RemoteException {
            return null;
        }

        @Override
        public SyncAdapterType[] getSyncAdapterTypes() throws RemoteException {
            return null;
        }

        @Override
        public SyncStatusInfo getSyncStatus(Account account, String authority) throws RemoteException {
            return null;
        }

        @Override
        public boolean isSyncPending(Account account, String authority) throws RemoteException {
            return false;
        }

        @Override
        public void addStatusChangeListener(int mask, ISyncStatusObserver callback) throws RemoteException {
        }

        @Override
        public void removeStatusChangeListener(ISyncStatusObserver callback) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }
}

