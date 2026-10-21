/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.accounts.Account
 *  android.accounts.AuthenticatorDescription
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
import android.accounts.AuthenticatorDescription;
import android.accounts.IAccountManagerResponse;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
import com.lody.virtual.StringFog;
import java.util.HashMap;
import java.util.Map;

public interface IAccountManager
extends IInterface {
    public AuthenticatorDescription[] getAuthenticatorTypes(int var1) throws RemoteException;

    public void getAccountsByFeatures(int var1, IAccountManagerResponse var2, String var3, String[] var4) throws RemoteException;

    public String getPreviousName(int var1, Account var2) throws RemoteException;

    public Account[] getAccounts(int var1, String var2) throws RemoteException;

    public void getAuthToken(int var1, IAccountManagerResponse var2, Account var3, String var4, boolean var5, boolean var6, Bundle var7) throws RemoteException;

    public void setPassword(int var1, Account var2, String var3) throws RemoteException;

    public void setAuthToken(int var1, Account var2, String var3, String var4) throws RemoteException;

    public void setUserData(int var1, Account var2, String var3, String var4) throws RemoteException;

    public void hasFeatures(int var1, IAccountManagerResponse var2, Account var3, String[] var4) throws RemoteException;

    public void updateCredentials(int var1, IAccountManagerResponse var2, Account var3, String var4, boolean var5, Bundle var6) throws RemoteException;

    public void editProperties(int var1, IAccountManagerResponse var2, String var3, boolean var4) throws RemoteException;

    public void getAuthTokenLabel(int var1, IAccountManagerResponse var2, String var3, String var4) throws RemoteException;

    public String getUserData(int var1, Account var2, String var3) throws RemoteException;

    public String getPassword(int var1, Account var2) throws RemoteException;

    public void confirmCredentials(int var1, IAccountManagerResponse var2, Account var3, Bundle var4, boolean var5) throws RemoteException;

    public void addAccount(int var1, IAccountManagerResponse var2, String var3, String var4, String[] var5, boolean var6, Bundle var7) throws RemoteException;

    public boolean addAccountExplicitly(int var1, Account var2, String var3, Bundle var4) throws RemoteException;

    public boolean removeAccountExplicitly(int var1, Account var2) throws RemoteException;

    public void renameAccount(int var1, IAccountManagerResponse var2, Account var3, String var4) throws RemoteException;

    public void removeAccount(int var1, IAccountManagerResponse var2, Account var3, boolean var4) throws RemoteException;

    public void clearPassword(int var1, Account var2) throws RemoteException;

    public boolean accountAuthenticated(int var1, Account var2) throws RemoteException;

    public void invalidateAuthToken(int var1, String var2, String var3) throws RemoteException;

    public String peekAuthToken(int var1, Account var2, String var3) throws RemoteException;

    public boolean setAccountVisibility(int var1, Account var2, String var3, int var4) throws RemoteException;

    public int getAccountVisibility(int var1, Account var2, String var3) throws RemoteException;

    public void startAddAccountSession(IAccountManagerResponse var1, String var2, String var3, String[] var4, boolean var5, Bundle var6) throws RemoteException;

    public void startUpdateCredentialsSession(IAccountManagerResponse var1, Account var2, String var3, boolean var4, Bundle var5) throws RemoteException;

    public void registerAccountListener(String[] var1) throws RemoteException;

    public void unregisterAccountListener(String[] var1) throws RemoteException;

    public Map getPackagesAndVisibilityForAccount(int var1, Account var2) throws RemoteException;

    public Map getAccountsAndVisibilityForPackage(int var1, String var2, String var3) throws RemoteException;

    public void finishSessionAsUser(IAccountManagerResponse var1, Bundle var2, boolean var3, Bundle var4, int var5) throws RemoteException;

    public void isCredentialsUpdateSuggested(IAccountManagerResponse var1, Account var2, String var3) throws RemoteException;

    public boolean addAccountExplicitlyWithVisibility(int var1, Account var2, String var3, Bundle var4, Map var5) throws RemoteException;

    public static abstract class Stub
    extends Binder
    implements IAccountManager {
        private static final String DESCRIPTOR = "com.lody.virtual.server.interfaces.IAccountManager";
        static final int TRANSACTION_getAuthenticatorTypes = 1;
        static final int TRANSACTION_getAccountsByFeatures = 2;
        static final int TRANSACTION_getPreviousName = 3;
        static final int TRANSACTION_getAccounts = 4;
        static final int TRANSACTION_getAuthToken = 5;
        static final int TRANSACTION_setPassword = 6;
        static final int TRANSACTION_setAuthToken = 7;
        static final int TRANSACTION_setUserData = 8;
        static final int TRANSACTION_hasFeatures = 9;
        static final int TRANSACTION_updateCredentials = 10;
        static final int TRANSACTION_editProperties = 11;
        static final int TRANSACTION_getAuthTokenLabel = 12;
        static final int TRANSACTION_getUserData = 13;
        static final int TRANSACTION_getPassword = 14;
        static final int TRANSACTION_confirmCredentials = 15;
        static final int TRANSACTION_addAccount = 16;
        static final int TRANSACTION_addAccountExplicitly = 17;
        static final int TRANSACTION_removeAccountExplicitly = 18;
        static final int TRANSACTION_renameAccount = 19;
        static final int TRANSACTION_removeAccount = 20;
        static final int TRANSACTION_clearPassword = 21;
        static final int TRANSACTION_accountAuthenticated = 22;
        static final int TRANSACTION_invalidateAuthToken = 23;
        static final int TRANSACTION_peekAuthToken = 24;
        static final int TRANSACTION_setAccountVisibility = 25;
        static final int TRANSACTION_getAccountVisibility = 26;
        static final int TRANSACTION_startAddAccountSession = 27;
        static final int TRANSACTION_startUpdateCredentialsSession = 28;
        static final int TRANSACTION_registerAccountListener = 29;
        static final int TRANSACTION_unregisterAccountListener = 30;
        static final int TRANSACTION_getPackagesAndVisibilityForAccount = 31;
        static final int TRANSACTION_getAccountsAndVisibilityForPackage = 32;
        static final int TRANSACTION_finishSessionAsUser = 33;
        static final int TRANSACTION_isCredentialsUpdateSuggested = 34;
        static final int TRANSACTION_addAccountExplicitlyWithVisibility = 35;

        public Stub() {
            this.attachInterface(this, "com.lody.virtual.server.interfaces.IAccountManager");
        }

        public static IAccountManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && iin instanceof IAccountManager) {
                return (IAccountManager)iin;
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
                    int _arg0 = data.readInt();
                    AuthenticatorDescription[] _result = this.getAuthenticatorTypes(_arg0);
                    reply.writeNoException();
                    reply.writeTypedArray((Parcelable[])_result, 1);
                    return true;
                }
                case 2: {
                    data.enforceInterface(descriptor);
                    int _arg0 = data.readInt();
                    IAccountManagerResponse _arg1 = IAccountManagerResponse.Stub.asInterface(data.readStrongBinder());
                    String _arg2 = data.readString();
                    String[] _arg3 = data.createStringArray();
                    this.getAccountsByFeatures(_arg0, _arg1, _arg2, _arg3);
                    reply.writeNoException();
                    return true;
                }
                case 3: {
                    data.enforceInterface(descriptor);
                    int _arg0 = data.readInt();
                    Account _arg1 = 0 != data.readInt() ? (Account)Account.CREATOR.createFromParcel(data) : null;
                    String _result = this.getPreviousName(_arg0, _arg1);
                    reply.writeNoException();
                    reply.writeString(_result);
                    return true;
                }
                case 4: {
                    data.enforceInterface(descriptor);
                    int _arg0 = data.readInt();
                    String _arg1 = data.readString();
                    Account[] _result = this.getAccounts(_arg0, _arg1);
                    reply.writeNoException();
                    reply.writeTypedArray((Parcelable[])_result, 1);
                    return true;
                }
                case 5: {
                    data.enforceInterface(descriptor);
                    int _arg0 = data.readInt();
                    IAccountManagerResponse _arg1 = IAccountManagerResponse.Stub.asInterface(data.readStrongBinder());
                    Account _arg2 = 0 != data.readInt() ? (Account)Account.CREATOR.createFromParcel(data) : null;
                    String _arg3 = data.readString();
                    boolean _arg4 = 0 != data.readInt();
                    boolean _arg5 = 0 != data.readInt();
                    Bundle _arg6 = 0 != data.readInt() ? (Bundle)Bundle.CREATOR.createFromParcel(data) : null;
                    this.getAuthToken(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5, _arg6);
                    reply.writeNoException();
                    return true;
                }
                case 6: {
                    data.enforceInterface(descriptor);
                    int _arg0 = data.readInt();
                    Account _arg1 = 0 != data.readInt() ? (Account)Account.CREATOR.createFromParcel(data) : null;
                    String _arg2 = data.readString();
                    this.setPassword(_arg0, _arg1, _arg2);
                    reply.writeNoException();
                    return true;
                }
                case 7: {
                    data.enforceInterface(descriptor);
                    int _arg0 = data.readInt();
                    Account _arg1 = 0 != data.readInt() ? (Account)Account.CREATOR.createFromParcel(data) : null;
                    String _arg2 = data.readString();
                    String _arg3 = data.readString();
                    this.setAuthToken(_arg0, _arg1, _arg2, _arg3);
                    reply.writeNoException();
                    return true;
                }
                case 8: {
                    data.enforceInterface(descriptor);
                    int _arg0 = data.readInt();
                    Account _arg1 = 0 != data.readInt() ? (Account)Account.CREATOR.createFromParcel(data) : null;
                    String _arg2 = data.readString();
                    String _arg3 = data.readString();
                    this.setUserData(_arg0, _arg1, _arg2, _arg3);
                    reply.writeNoException();
                    return true;
                }
                case 9: {
                    data.enforceInterface(descriptor);
                    int _arg0 = data.readInt();
                    IAccountManagerResponse _arg1 = IAccountManagerResponse.Stub.asInterface(data.readStrongBinder());
                    Account _arg2 = 0 != data.readInt() ? (Account)Account.CREATOR.createFromParcel(data) : null;
                    String[] _arg3 = data.createStringArray();
                    this.hasFeatures(_arg0, _arg1, _arg2, _arg3);
                    reply.writeNoException();
                    return true;
                }
                case 10: {
                    data.enforceInterface(descriptor);
                    int _arg0 = data.readInt();
                    IAccountManagerResponse _arg1 = IAccountManagerResponse.Stub.asInterface(data.readStrongBinder());
                    Account _arg2 = 0 != data.readInt() ? (Account)Account.CREATOR.createFromParcel(data) : null;
                    String _arg3 = data.readString();
                    boolean _arg4 = 0 != data.readInt();
                    Bundle _arg5 = 0 != data.readInt() ? (Bundle)Bundle.CREATOR.createFromParcel(data) : null;
                    this.updateCredentials(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5);
                    reply.writeNoException();
                    return true;
                }
                case 11: {
                    data.enforceInterface(descriptor);
                    int _arg0 = data.readInt();
                    IAccountManagerResponse _arg1 = IAccountManagerResponse.Stub.asInterface(data.readStrongBinder());
                    String _arg2 = data.readString();
                    boolean _arg3 = 0 != data.readInt();
                    this.editProperties(_arg0, _arg1, _arg2, _arg3);
                    reply.writeNoException();
                    return true;
                }
                case 12: {
                    data.enforceInterface(descriptor);
                    int _arg0 = data.readInt();
                    IAccountManagerResponse _arg1 = IAccountManagerResponse.Stub.asInterface(data.readStrongBinder());
                    String _arg2 = data.readString();
                    String _arg3 = data.readString();
                    this.getAuthTokenLabel(_arg0, _arg1, _arg2, _arg3);
                    reply.writeNoException();
                    return true;
                }
                case 13: {
                    data.enforceInterface(descriptor);
                    int _arg0 = data.readInt();
                    Account _arg1 = 0 != data.readInt() ? (Account)Account.CREATOR.createFromParcel(data) : null;
                    String _arg2 = data.readString();
                    String _result = this.getUserData(_arg0, _arg1, _arg2);
                    reply.writeNoException();
                    reply.writeString(_result);
                    return true;
                }
                case 14: {
                    data.enforceInterface(descriptor);
                    int _arg0 = data.readInt();
                    Account _arg1 = 0 != data.readInt() ? (Account)Account.CREATOR.createFromParcel(data) : null;
                    String _result = this.getPassword(_arg0, _arg1);
                    reply.writeNoException();
                    reply.writeString(_result);
                    return true;
                }
                case 15: {
                    data.enforceInterface(descriptor);
                    int _arg0 = data.readInt();
                    IAccountManagerResponse _arg1 = IAccountManagerResponse.Stub.asInterface(data.readStrongBinder());
                    Account _arg2 = 0 != data.readInt() ? (Account)Account.CREATOR.createFromParcel(data) : null;
                    Bundle _arg3 = 0 != data.readInt() ? (Bundle)Bundle.CREATOR.createFromParcel(data) : null;
                    boolean _arg4 = 0 != data.readInt();
                    this.confirmCredentials(_arg0, _arg1, _arg2, _arg3, _arg4);
                    reply.writeNoException();
                    return true;
                }
                case 16: {
                    data.enforceInterface(descriptor);
                    int _arg0 = data.readInt();
                    IAccountManagerResponse _arg1 = IAccountManagerResponse.Stub.asInterface(data.readStrongBinder());
                    String _arg2 = data.readString();
                    String _arg3 = data.readString();
                    String[] _arg4 = data.createStringArray();
                    boolean _arg5 = 0 != data.readInt();
                    Bundle _arg6 = 0 != data.readInt() ? (Bundle)Bundle.CREATOR.createFromParcel(data) : null;
                    this.addAccount(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5, _arg6);
                    reply.writeNoException();
                    return true;
                }
                case 17: {
                    data.enforceInterface(descriptor);
                    int _arg0 = data.readInt();
                    Account _arg1 = 0 != data.readInt() ? (Account)Account.CREATOR.createFromParcel(data) : null;
                    String _arg2 = data.readString();
                    Bundle _arg3 = 0 != data.readInt() ? (Bundle)Bundle.CREATOR.createFromParcel(data) : null;
                    boolean _result = this.addAccountExplicitly(_arg0, _arg1, _arg2, _arg3);
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                }
                case 18: {
                    data.enforceInterface(descriptor);
                    int _arg0 = data.readInt();
                    Account _arg1 = 0 != data.readInt() ? (Account)Account.CREATOR.createFromParcel(data) : null;
                    boolean _result = this.removeAccountExplicitly(_arg0, _arg1);
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                }
                case 19: {
                    data.enforceInterface(descriptor);
                    int _arg0 = data.readInt();
                    IAccountManagerResponse _arg1 = IAccountManagerResponse.Stub.asInterface(data.readStrongBinder());
                    Account _arg2 = 0 != data.readInt() ? (Account)Account.CREATOR.createFromParcel(data) : null;
                    String _arg3 = data.readString();
                    this.renameAccount(_arg0, _arg1, _arg2, _arg3);
                    reply.writeNoException();
                    return true;
                }
                case 20: {
                    data.enforceInterface(descriptor);
                    int _arg0 = data.readInt();
                    IAccountManagerResponse _arg1 = IAccountManagerResponse.Stub.asInterface(data.readStrongBinder());
                    Account _arg2 = 0 != data.readInt() ? (Account)Account.CREATOR.createFromParcel(data) : null;
                    boolean _arg3 = 0 != data.readInt();
                    this.removeAccount(_arg0, _arg1, _arg2, _arg3);
                    reply.writeNoException();
                    return true;
                }
                case 21: {
                    data.enforceInterface(descriptor);
                    int _arg0 = data.readInt();
                    Account _arg1 = 0 != data.readInt() ? (Account)Account.CREATOR.createFromParcel(data) : null;
                    this.clearPassword(_arg0, _arg1);
                    reply.writeNoException();
                    return true;
                }
                case 22: {
                    data.enforceInterface(descriptor);
                    int _arg0 = data.readInt();
                    Account _arg1 = 0 != data.readInt() ? (Account)Account.CREATOR.createFromParcel(data) : null;
                    boolean _result = this.accountAuthenticated(_arg0, _arg1);
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                }
                case 23: {
                    data.enforceInterface(descriptor);
                    int _arg0 = data.readInt();
                    String _arg1 = data.readString();
                    String _arg2 = data.readString();
                    this.invalidateAuthToken(_arg0, _arg1, _arg2);
                    reply.writeNoException();
                    return true;
                }
                case 24: {
                    data.enforceInterface(descriptor);
                    int _arg0 = data.readInt();
                    Account _arg1 = 0 != data.readInt() ? (Account)Account.CREATOR.createFromParcel(data) : null;
                    String _arg2 = data.readString();
                    String _result = this.peekAuthToken(_arg0, _arg1, _arg2);
                    reply.writeNoException();
                    reply.writeString(_result);
                    return true;
                }
                case 25: {
                    data.enforceInterface(descriptor);
                    int _arg0 = data.readInt();
                    Account _arg1 = 0 != data.readInt() ? (Account)Account.CREATOR.createFromParcel(data) : null;
                    String _arg2 = data.readString();
                    int _arg3 = data.readInt();
                    boolean _result = this.setAccountVisibility(_arg0, _arg1, _arg2, _arg3);
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                }
                case 26: {
                    data.enforceInterface(descriptor);
                    int _arg0 = data.readInt();
                    Account _arg1 = 0 != data.readInt() ? (Account)Account.CREATOR.createFromParcel(data) : null;
                    String _arg2 = data.readString();
                    int _result = this.getAccountVisibility(_arg0, _arg1, _arg2);
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                }
                case 27: {
                    data.enforceInterface(descriptor);
                    IAccountManagerResponse _arg0 = IAccountManagerResponse.Stub.asInterface(data.readStrongBinder());
                    String _arg1 = data.readString();
                    String _arg2 = data.readString();
                    String[] _arg3 = data.createStringArray();
                    boolean _arg4 = 0 != data.readInt();
                    Bundle _arg5 = 0 != data.readInt() ? (Bundle)Bundle.CREATOR.createFromParcel(data) : null;
                    this.startAddAccountSession(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5);
                    reply.writeNoException();
                    return true;
                }
                case 28: {
                    data.enforceInterface(descriptor);
                    IAccountManagerResponse _arg0 = IAccountManagerResponse.Stub.asInterface(data.readStrongBinder());
                    Account _arg1 = 0 != data.readInt() ? (Account)Account.CREATOR.createFromParcel(data) : null;
                    String _arg2 = data.readString();
                    boolean _arg3 = 0 != data.readInt();
                    Bundle _arg4 = 0 != data.readInt() ? (Bundle)Bundle.CREATOR.createFromParcel(data) : null;
                    this.startUpdateCredentialsSession(_arg0, _arg1, _arg2, _arg3, _arg4);
                    reply.writeNoException();
                    return true;
                }
                case 29: {
                    data.enforceInterface(descriptor);
                    String[] _arg0 = data.createStringArray();
                    this.registerAccountListener(_arg0);
                    reply.writeNoException();
                    return true;
                }
                case 30: {
                    data.enforceInterface(descriptor);
                    String[] _arg0 = data.createStringArray();
                    this.unregisterAccountListener(_arg0);
                    reply.writeNoException();
                    return true;
                }
                case 31: {
                    data.enforceInterface(descriptor);
                    int _arg0 = data.readInt();
                    Account _arg1 = 0 != data.readInt() ? (Account)Account.CREATOR.createFromParcel(data) : null;
                    Map _result = this.getPackagesAndVisibilityForAccount(_arg0, _arg1);
                    reply.writeNoException();
                    reply.writeMap(_result);
                    return true;
                }
                case 32: {
                    data.enforceInterface(descriptor);
                    int _arg0 = data.readInt();
                    String _arg1 = data.readString();
                    String _arg2 = data.readString();
                    Map _result = this.getAccountsAndVisibilityForPackage(_arg0, _arg1, _arg2);
                    reply.writeNoException();
                    reply.writeMap(_result);
                    return true;
                }
                case 33: {
                    data.enforceInterface(descriptor);
                    IAccountManagerResponse _arg0 = IAccountManagerResponse.Stub.asInterface(data.readStrongBinder());
                    Bundle _arg1 = 0 != data.readInt() ? (Bundle)Bundle.CREATOR.createFromParcel(data) : null;
                    boolean _arg2 = 0 != data.readInt();
                    Bundle _arg3 = 0 != data.readInt() ? (Bundle)Bundle.CREATOR.createFromParcel(data) : null;
                    int _arg4 = data.readInt();
                    this.finishSessionAsUser(_arg0, _arg1, _arg2, _arg3, _arg4);
                    reply.writeNoException();
                    return true;
                }
                case 34: {
                    data.enforceInterface(descriptor);
                    IAccountManagerResponse _arg0 = IAccountManagerResponse.Stub.asInterface(data.readStrongBinder());
                    Account _arg1 = 0 != data.readInt() ? (Account)Account.CREATOR.createFromParcel(data) : null;
                    String _arg2 = data.readString();
                    this.isCredentialsUpdateSuggested(_arg0, _arg1, _arg2);
                    reply.writeNoException();
                    return true;
                }
                case 35: {
                    data.enforceInterface(descriptor);
                    int _arg0 = data.readInt();
                    Account _arg1 = 0 != data.readInt() ? (Account)Account.CREATOR.createFromParcel(data) : null;
                    String _arg2 = data.readString();
                    Bundle _arg3 = 0 != data.readInt() ? (Bundle)Bundle.CREATOR.createFromParcel(data) : null;
                    ClassLoader cl = this.getClass().getClassLoader();
                    HashMap _arg4 = data.readHashMap(cl);
                    boolean _result = this.addAccountExplicitlyWithVisibility(_arg0, _arg1, _arg2, _arg3, _arg4);
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                }
            }
            return super.onTransact(code, data, reply, flags);
        }

        public static boolean setDefaultImpl(IAccountManager impl) {
            if (Proxy.sDefaultImpl != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            }
            if (impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IAccountManager getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }

        private static class Proxy
        implements IAccountManager {
            private IBinder mRemote;
            public static IAccountManager sDefaultImpl;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return "com.lody.virtual.server.interfaces.IAccountManager";
            }

            /*
             * WARNING - Removed try catching itself - possible behaviour change.
             */
            @Override
            public AuthenticatorDescription[] getAuthenticatorTypes(int userId) throws RemoteException {
                AuthenticatorDescription[] _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IAccountManager");
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        AuthenticatorDescription[] authenticatorDescriptionArray = Stub.getDefaultImpl().getAuthenticatorTypes(userId);
                        return authenticatorDescriptionArray;
                    }
                    _reply.readException();
                    _result = (AuthenticatorDescription[])_reply.createTypedArray(AuthenticatorDescription.CREATOR);
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
            public void getAccountsByFeatures(int userId, IAccountManagerResponse response, String type, String[] features) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IAccountManager");
                    _data.writeInt(userId);
                    _data.writeStrongBinder(response != null ? response.asBinder() : null);
                    _data.writeString(type);
                    _data.writeStringArray(features);
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().getAccountsByFeatures(userId, response, type, features);
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
            public String getPreviousName(int userId, Account account) throws RemoteException {
                String _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IAccountManager");
                    _data.writeInt(userId);
                    if (account != null) {
                        _data.writeInt(1);
                        account.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(3, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        String string2 = Stub.getDefaultImpl().getPreviousName(userId, account);
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
            public Account[] getAccounts(int userId, String type) throws RemoteException {
                Account[] _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IAccountManager");
                    _data.writeInt(userId);
                    _data.writeString(type);
                    boolean _status = this.mRemote.transact(4, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Account[] accountArray = Stub.getDefaultImpl().getAccounts(userId, type);
                        return accountArray;
                    }
                    _reply.readException();
                    _result = (Account[])_reply.createTypedArray(Account.CREATOR);
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
            public void getAuthToken(int userId, IAccountManagerResponse response, Account account, String authTokenType, boolean notifyOnAuthFailure, boolean expectActivityLaunch, Bundle loginOptions) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IAccountManager");
                    _data.writeInt(userId);
                    _data.writeStrongBinder(response != null ? response.asBinder() : null);
                    if (account != null) {
                        _data.writeInt(1);
                        account.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(authTokenType);
                    _data.writeInt(notifyOnAuthFailure ? 1 : 0);
                    _data.writeInt(expectActivityLaunch ? 1 : 0);
                    if (loginOptions != null) {
                        _data.writeInt(1);
                        loginOptions.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(5, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().getAuthToken(userId, response, account, authTokenType, notifyOnAuthFailure, expectActivityLaunch, loginOptions);
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
            public void setPassword(int userId, Account account, String password) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IAccountManager");
                    _data.writeInt(userId);
                    if (account != null) {
                        _data.writeInt(1);
                        account.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(password);
                    boolean _status = this.mRemote.transact(6, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setPassword(userId, account, password);
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
            public void setAuthToken(int userId, Account account, String authTokenType, String authToken) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IAccountManager");
                    _data.writeInt(userId);
                    if (account != null) {
                        _data.writeInt(1);
                        account.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(authTokenType);
                    _data.writeString(authToken);
                    boolean _status = this.mRemote.transact(7, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setAuthToken(userId, account, authTokenType, authToken);
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
            public void setUserData(int userId, Account account, String key, String value) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IAccountManager");
                    _data.writeInt(userId);
                    if (account != null) {
                        _data.writeInt(1);
                        account.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(key);
                    _data.writeString(value);
                    boolean _status = this.mRemote.transact(8, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setUserData(userId, account, key, value);
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
            public void hasFeatures(int userId, IAccountManagerResponse response, Account account, String[] features) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IAccountManager");
                    _data.writeInt(userId);
                    _data.writeStrongBinder(response != null ? response.asBinder() : null);
                    if (account != null) {
                        _data.writeInt(1);
                        account.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStringArray(features);
                    boolean _status = this.mRemote.transact(9, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().hasFeatures(userId, response, account, features);
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
            public void updateCredentials(int userId, IAccountManagerResponse response, Account account, String authTokenType, boolean expectActivityLaunch, Bundle loginOptions) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IAccountManager");
                    _data.writeInt(userId);
                    _data.writeStrongBinder(response != null ? response.asBinder() : null);
                    if (account != null) {
                        _data.writeInt(1);
                        account.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(authTokenType);
                    _data.writeInt(expectActivityLaunch ? 1 : 0);
                    if (loginOptions != null) {
                        _data.writeInt(1);
                        loginOptions.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(10, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().updateCredentials(userId, response, account, authTokenType, expectActivityLaunch, loginOptions);
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
            public void editProperties(int userId, IAccountManagerResponse response, String accountType, boolean expectActivityLaunch) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IAccountManager");
                    _data.writeInt(userId);
                    _data.writeStrongBinder(response != null ? response.asBinder() : null);
                    _data.writeString(accountType);
                    _data.writeInt(expectActivityLaunch ? 1 : 0);
                    boolean _status = this.mRemote.transact(11, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().editProperties(userId, response, accountType, expectActivityLaunch);
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
            public void getAuthTokenLabel(int userId, IAccountManagerResponse response, String accountType, String authTokenType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IAccountManager");
                    _data.writeInt(userId);
                    _data.writeStrongBinder(response != null ? response.asBinder() : null);
                    _data.writeString(accountType);
                    _data.writeString(authTokenType);
                    boolean _status = this.mRemote.transact(12, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().getAuthTokenLabel(userId, response, accountType, authTokenType);
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
            public String getUserData(int userId, Account account, String key) throws RemoteException {
                String _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IAccountManager");
                    _data.writeInt(userId);
                    if (account != null) {
                        _data.writeInt(1);
                        account.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(key);
                    boolean _status = this.mRemote.transact(13, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        String string2 = Stub.getDefaultImpl().getUserData(userId, account, key);
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
            public String getPassword(int userId, Account account) throws RemoteException {
                String _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IAccountManager");
                    _data.writeInt(userId);
                    if (account != null) {
                        _data.writeInt(1);
                        account.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(14, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        String string2 = Stub.getDefaultImpl().getPassword(userId, account);
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
            public void confirmCredentials(int userId, IAccountManagerResponse response, Account account, Bundle options, boolean expectActivityLaunch) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IAccountManager");
                    _data.writeInt(userId);
                    _data.writeStrongBinder(response != null ? response.asBinder() : null);
                    if (account != null) {
                        _data.writeInt(1);
                        account.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (options != null) {
                        _data.writeInt(1);
                        options.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(expectActivityLaunch ? 1 : 0);
                    boolean _status = this.mRemote.transact(15, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().confirmCredentials(userId, response, account, options, expectActivityLaunch);
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
            public void addAccount(int userId, IAccountManagerResponse response, String accountType, String authTokenType, String[] requiredFeatures, boolean expectActivityLaunch, Bundle optionsIn) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IAccountManager");
                    _data.writeInt(userId);
                    _data.writeStrongBinder(response != null ? response.asBinder() : null);
                    _data.writeString(accountType);
                    _data.writeString(authTokenType);
                    _data.writeStringArray(requiredFeatures);
                    _data.writeInt(expectActivityLaunch ? 1 : 0);
                    if (optionsIn != null) {
                        _data.writeInt(1);
                        optionsIn.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(16, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().addAccount(userId, response, accountType, authTokenType, requiredFeatures, expectActivityLaunch, optionsIn);
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
            public boolean addAccountExplicitly(int userId, Account account, String password, Bundle extras) throws RemoteException {
                boolean _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IAccountManager");
                    _data.writeInt(userId);
                    if (account != null) {
                        _data.writeInt(1);
                        account.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(password);
                    if (extras != null) {
                        _data.writeInt(1);
                        extras.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(17, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        boolean bl = Stub.getDefaultImpl().addAccountExplicitly(userId, account, password, extras);
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
            public boolean removeAccountExplicitly(int userId, Account account) throws RemoteException {
                boolean _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IAccountManager");
                    _data.writeInt(userId);
                    if (account != null) {
                        _data.writeInt(1);
                        account.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(18, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        boolean bl = Stub.getDefaultImpl().removeAccountExplicitly(userId, account);
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
            public void renameAccount(int userId, IAccountManagerResponse response, Account accountToRename, String newName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IAccountManager");
                    _data.writeInt(userId);
                    _data.writeStrongBinder(response != null ? response.asBinder() : null);
                    if (accountToRename != null) {
                        _data.writeInt(1);
                        accountToRename.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(newName);
                    boolean _status = this.mRemote.transact(19, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().renameAccount(userId, response, accountToRename, newName);
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
            public void removeAccount(int userId, IAccountManagerResponse response, Account account, boolean expectActivityLaunch) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IAccountManager");
                    _data.writeInt(userId);
                    _data.writeStrongBinder(response != null ? response.asBinder() : null);
                    if (account != null) {
                        _data.writeInt(1);
                        account.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(expectActivityLaunch ? 1 : 0);
                    boolean _status = this.mRemote.transact(20, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().removeAccount(userId, response, account, expectActivityLaunch);
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
            public void clearPassword(int userId, Account account) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IAccountManager");
                    _data.writeInt(userId);
                    if (account != null) {
                        _data.writeInt(1);
                        account.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(21, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().clearPassword(userId, account);
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
            public boolean accountAuthenticated(int userId, Account account) throws RemoteException {
                boolean _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IAccountManager");
                    _data.writeInt(userId);
                    if (account != null) {
                        _data.writeInt(1);
                        account.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(22, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        boolean bl = Stub.getDefaultImpl().accountAuthenticated(userId, account);
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
            public void invalidateAuthToken(int userId, String accountType, String authToken) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IAccountManager");
                    _data.writeInt(userId);
                    _data.writeString(accountType);
                    _data.writeString(authToken);
                    boolean _status = this.mRemote.transact(23, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().invalidateAuthToken(userId, accountType, authToken);
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
            public String peekAuthToken(int userId, Account account, String authTokenType) throws RemoteException {
                String _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IAccountManager");
                    _data.writeInt(userId);
                    if (account != null) {
                        _data.writeInt(1);
                        account.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(authTokenType);
                    boolean _status = this.mRemote.transact(24, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        String string2 = Stub.getDefaultImpl().peekAuthToken(userId, account, authTokenType);
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
            public boolean setAccountVisibility(int userId, Account a, String packageName, int newVisibility) throws RemoteException {
                boolean _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IAccountManager");
                    _data.writeInt(userId);
                    if (a != null) {
                        _data.writeInt(1);
                        a.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(packageName);
                    _data.writeInt(newVisibility);
                    boolean _status = this.mRemote.transact(25, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        boolean bl = Stub.getDefaultImpl().setAccountVisibility(userId, a, packageName, newVisibility);
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
            public int getAccountVisibility(int userId, Account a, String packageName) throws RemoteException {
                int _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IAccountManager");
                    _data.writeInt(userId);
                    if (a != null) {
                        _data.writeInt(1);
                        a.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(packageName);
                    boolean _status = this.mRemote.transact(26, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        int n = Stub.getDefaultImpl().getAccountVisibility(userId, a, packageName);
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
            public void startAddAccountSession(IAccountManagerResponse response, String accountType, String authTokenType, String[] requiredFeatures, boolean expectActivityLaunch, Bundle options) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IAccountManager");
                    _data.writeStrongBinder(response != null ? response.asBinder() : null);
                    _data.writeString(accountType);
                    _data.writeString(authTokenType);
                    _data.writeStringArray(requiredFeatures);
                    _data.writeInt(expectActivityLaunch ? 1 : 0);
                    if (options != null) {
                        _data.writeInt(1);
                        options.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(27, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().startAddAccountSession(response, accountType, authTokenType, requiredFeatures, expectActivityLaunch, options);
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
            public void startUpdateCredentialsSession(IAccountManagerResponse response, Account account, String authTokenType, boolean expectActivityLaunch, Bundle options) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IAccountManager");
                    _data.writeStrongBinder(response != null ? response.asBinder() : null);
                    if (account != null) {
                        _data.writeInt(1);
                        account.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(authTokenType);
                    _data.writeInt(expectActivityLaunch ? 1 : 0);
                    if (options != null) {
                        _data.writeInt(1);
                        options.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(28, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().startUpdateCredentialsSession(response, account, authTokenType, expectActivityLaunch, options);
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
            public void registerAccountListener(String[] accountTypes) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IAccountManager");
                    _data.writeStringArray(accountTypes);
                    boolean _status = this.mRemote.transact(29, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().registerAccountListener(accountTypes);
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
            public void unregisterAccountListener(String[] accountTypes) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IAccountManager");
                    _data.writeStringArray(accountTypes);
                    boolean _status = this.mRemote.transact(30, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().unregisterAccountListener(accountTypes);
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
            public Map getPackagesAndVisibilityForAccount(int userId, Account account) throws RemoteException {
                HashMap _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IAccountManager");
                    _data.writeInt(userId);
                    if (account != null) {
                        _data.writeInt(1);
                        account.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(31, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Map map = Stub.getDefaultImpl().getPackagesAndVisibilityForAccount(userId, account);
                        return map;
                    }
                    _reply.readException();
                    ClassLoader cl = this.getClass().getClassLoader();
                    _result = _reply.readHashMap(cl);
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
            public Map getAccountsAndVisibilityForPackage(int userId, String packageName, String accountType) throws RemoteException {
                HashMap _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IAccountManager");
                    _data.writeInt(userId);
                    _data.writeString(packageName);
                    _data.writeString(accountType);
                    boolean _status = this.mRemote.transact(32, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Map map = Stub.getDefaultImpl().getAccountsAndVisibilityForPackage(userId, packageName, accountType);
                        return map;
                    }
                    _reply.readException();
                    ClassLoader cl = this.getClass().getClassLoader();
                    _result = _reply.readHashMap(cl);
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
            public void finishSessionAsUser(IAccountManagerResponse response, Bundle sessionBundle, boolean expectActivityLaunch, Bundle appInfo, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IAccountManager");
                    _data.writeStrongBinder(response != null ? response.asBinder() : null);
                    if (sessionBundle != null) {
                        _data.writeInt(1);
                        sessionBundle.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(expectActivityLaunch ? 1 : 0);
                    if (appInfo != null) {
                        _data.writeInt(1);
                        appInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(33, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().finishSessionAsUser(response, sessionBundle, expectActivityLaunch, appInfo, userId);
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
            public void isCredentialsUpdateSuggested(IAccountManagerResponse response, Account account, String statusToken) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IAccountManager");
                    _data.writeStrongBinder(response != null ? response.asBinder() : null);
                    if (account != null) {
                        _data.writeInt(1);
                        account.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(statusToken);
                    boolean _status = this.mRemote.transact(34, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().isCredentialsUpdateSuggested(response, account, statusToken);
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
            public boolean addAccountExplicitlyWithVisibility(int userId, Account account, String password, Bundle extras, Map visibility) throws RemoteException {
                boolean _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IAccountManager");
                    _data.writeInt(userId);
                    if (account != null) {
                        _data.writeInt(1);
                        account.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(password);
                    if (extras != null) {
                        _data.writeInt(1);
                        extras.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeMap(visibility);
                    boolean _status = this.mRemote.transact(35, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        boolean bl = Stub.getDefaultImpl().addAccountExplicitlyWithVisibility(userId, account, password, extras, visibility);
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
    implements IAccountManager {
        @Override
        public AuthenticatorDescription[] getAuthenticatorTypes(int userId) throws RemoteException {
            return null;
        }

        @Override
        public void getAccountsByFeatures(int userId, IAccountManagerResponse response, String type, String[] features) throws RemoteException {
        }

        @Override
        public String getPreviousName(int userId, Account account) throws RemoteException {
            return null;
        }

        @Override
        public Account[] getAccounts(int userId, String type) throws RemoteException {
            return null;
        }

        @Override
        public void getAuthToken(int userId, IAccountManagerResponse response, Account account, String authTokenType, boolean notifyOnAuthFailure, boolean expectActivityLaunch, Bundle loginOptions) throws RemoteException {
        }

        @Override
        public void setPassword(int userId, Account account, String password) throws RemoteException {
        }

        @Override
        public void setAuthToken(int userId, Account account, String authTokenType, String authToken) throws RemoteException {
        }

        @Override
        public void setUserData(int userId, Account account, String key, String value) throws RemoteException {
        }

        @Override
        public void hasFeatures(int userId, IAccountManagerResponse response, Account account, String[] features) throws RemoteException {
        }

        @Override
        public void updateCredentials(int userId, IAccountManagerResponse response, Account account, String authTokenType, boolean expectActivityLaunch, Bundle loginOptions) throws RemoteException {
        }

        @Override
        public void editProperties(int userId, IAccountManagerResponse response, String accountType, boolean expectActivityLaunch) throws RemoteException {
        }

        @Override
        public void getAuthTokenLabel(int userId, IAccountManagerResponse response, String accountType, String authTokenType) throws RemoteException {
        }

        @Override
        public String getUserData(int userId, Account account, String key) throws RemoteException {
            return null;
        }

        @Override
        public String getPassword(int userId, Account account) throws RemoteException {
            return null;
        }

        @Override
        public void confirmCredentials(int userId, IAccountManagerResponse response, Account account, Bundle options, boolean expectActivityLaunch) throws RemoteException {
        }

        @Override
        public void addAccount(int userId, IAccountManagerResponse response, String accountType, String authTokenType, String[] requiredFeatures, boolean expectActivityLaunch, Bundle optionsIn) throws RemoteException {
        }

        @Override
        public boolean addAccountExplicitly(int userId, Account account, String password, Bundle extras) throws RemoteException {
            return false;
        }

        @Override
        public boolean removeAccountExplicitly(int userId, Account account) throws RemoteException {
            return false;
        }

        @Override
        public void renameAccount(int userId, IAccountManagerResponse response, Account accountToRename, String newName) throws RemoteException {
        }

        @Override
        public void removeAccount(int userId, IAccountManagerResponse response, Account account, boolean expectActivityLaunch) throws RemoteException {
        }

        @Override
        public void clearPassword(int userId, Account account) throws RemoteException {
        }

        @Override
        public boolean accountAuthenticated(int userId, Account account) throws RemoteException {
            return false;
        }

        @Override
        public void invalidateAuthToken(int userId, String accountType, String authToken) throws RemoteException {
        }

        @Override
        public String peekAuthToken(int userId, Account account, String authTokenType) throws RemoteException {
            return null;
        }

        @Override
        public boolean setAccountVisibility(int userId, Account a, String packageName, int newVisibility) throws RemoteException {
            return false;
        }

        @Override
        public int getAccountVisibility(int userId, Account a, String packageName) throws RemoteException {
            return 0;
        }

        @Override
        public void startAddAccountSession(IAccountManagerResponse response, String accountType, String authTokenType, String[] requiredFeatures, boolean expectActivityLaunch, Bundle options) throws RemoteException {
        }

        @Override
        public void startUpdateCredentialsSession(IAccountManagerResponse response, Account account, String authTokenType, boolean expectActivityLaunch, Bundle options) throws RemoteException {
        }

        @Override
        public void registerAccountListener(String[] accountTypes) throws RemoteException {
        }

        @Override
        public void unregisterAccountListener(String[] accountTypes) throws RemoteException {
        }

        @Override
        public Map getPackagesAndVisibilityForAccount(int userId, Account account) throws RemoteException {
            return null;
        }

        @Override
        public Map getAccountsAndVisibilityForPackage(int userId, String packageName, String accountType) throws RemoteException {
            return null;
        }

        @Override
        public void finishSessionAsUser(IAccountManagerResponse response, Bundle sessionBundle, boolean expectActivityLaunch, Bundle appInfo, int userId) throws RemoteException {
        }

        @Override
        public void isCredentialsUpdateSuggested(IAccountManagerResponse response, Account account, String statusToken) throws RemoteException {
        }

        @Override
        public boolean addAccountExplicitlyWithVisibility(int userId, Account account, String password, Bundle extras, Map visibility) throws RemoteException {
            return false;
        }

        public IBinder asBinder() {
            return null;
        }
    }
}

