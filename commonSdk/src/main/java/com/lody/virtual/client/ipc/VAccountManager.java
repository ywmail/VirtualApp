/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.accounts.Account
 *  android.accounts.AccountManagerCallback
 *  android.accounts.AccountManagerFuture
 *  android.accounts.AuthenticatorDescription
 *  android.app.Activity
 *  android.os.Bundle
 *  android.os.Handler
 *  android.os.RemoteException
 */
package com.lody.virtual.client.ipc;

import android.accounts.Account;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorDescription;
import android.accounts.IAccountManagerResponse;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.env.VirtualRuntime;
import com.lody.virtual.client.ipc.LocalProxyUtils;
import com.lody.virtual.client.ipc.ServiceManagerNative;
import com.lody.virtual.client.stub.AmsTask;
import com.lody.virtual.helper.utils.IInterfaceUtils;
import com.lody.virtual.os.VUserHandle;
import com.lody.virtual.server.interfaces.IAccountManager;
import java.util.Map;

public class VAccountManager {
    private static VAccountManager sMgr = new VAccountManager();
    private IAccountManager mService;

    public static VAccountManager get() {
        return sMgr;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public IAccountManager getService() {
        if (IInterfaceUtils.isAlive(this.mService)) return this.mService;
        Class<VAccountManager> clazz = VAccountManager.class;
        synchronized (VAccountManager.class) {
            Object remote = this.getStubInterface();
            this.mService = LocalProxyUtils.genProxy(IAccountManager.class, remote);
            // ** MonitorExit[var1_1] (shouldn't be in output)
            return this.mService;
        }
    }

    private Object getStubInterface() {
        return IAccountManager.Stub.asInterface(ServiceManagerNative.getService("account"));
    }

    public AuthenticatorDescription[] getAuthenticatorTypes(int userId) {
        try {
            return this.getService().getAuthenticatorTypes(userId);
        }
        catch (RemoteException e) {
            return (AuthenticatorDescription[])VirtualRuntime.crash(e);
        }
    }

    public void removeAccount(IAccountManagerResponse response, Account account, boolean expectActivityLaunch) {
        try {
            this.getService().removeAccount(VUserHandle.myUserId(), response, account, expectActivityLaunch);
        }
        catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void getAuthToken(IAccountManagerResponse response, Account account, String authTokenType, boolean notifyOnAuthFailure, boolean expectActivityLaunch, Bundle loginOptions) {
        try {
            this.getService().getAuthToken(VUserHandle.myUserId(), response, account, authTokenType, notifyOnAuthFailure, expectActivityLaunch, loginOptions);
        }
        catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public boolean addAccountExplicitly(Account account, String password, Bundle extras) {
        try {
            return this.getService().addAccountExplicitly(VUserHandle.myUserId(), account, password, extras);
        }
        catch (RemoteException e) {
            return (Boolean)VirtualRuntime.crash(e);
        }
    }

    public Account[] getAccounts(int userId, String type) {
        try {
            return this.getService().getAccounts(userId, type);
        }
        catch (RemoteException e) {
            return (Account[])VirtualRuntime.crash(e);
        }
    }

    public Account[] getAccounts(String type) {
        try {
            return this.getService().getAccounts(VUserHandle.myUserId(), type);
        }
        catch (RemoteException e) {
            return (Account[])VirtualRuntime.crash(e);
        }
    }

    public String peekAuthToken(Account account, String authTokenType) {
        try {
            return this.getService().peekAuthToken(VUserHandle.myUserId(), account, authTokenType);
        }
        catch (RemoteException e) {
            return (String)VirtualRuntime.crash(e);
        }
    }

    public String getPreviousName(Account account) {
        try {
            return this.getService().getPreviousName(VUserHandle.myUserId(), account);
        }
        catch (RemoteException e) {
            return (String)VirtualRuntime.crash(e);
        }
    }

    public void hasFeatures(IAccountManagerResponse response, Account account, String[] features) {
        try {
            this.getService().hasFeatures(VUserHandle.myUserId(), response, account, features);
        }
        catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public boolean accountAuthenticated(Account account) {
        try {
            return this.getService().accountAuthenticated(VUserHandle.myUserId(), account);
        }
        catch (RemoteException e) {
            return (Boolean)VirtualRuntime.crash(e);
        }
    }

    public void clearPassword(Account account) {
        try {
            this.getService().clearPassword(VUserHandle.myUserId(), account);
        }
        catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void renameAccount(IAccountManagerResponse response, Account accountToRename, String newName) {
        try {
            this.getService().renameAccount(VUserHandle.myUserId(), response, accountToRename, newName);
        }
        catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void setPassword(Account account, String password) {
        try {
            this.getService().setPassword(VUserHandle.myUserId(), account, password);
        }
        catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void addAccount(int userId, IAccountManagerResponse response, String accountType, String authTokenType, String[] requiredFeatures, boolean expectActivityLaunch, Bundle optionsIn) {
        try {
            this.getService().addAccount(userId, response, accountType, authTokenType, requiredFeatures, expectActivityLaunch, optionsIn);
        }
        catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void addAccount(IAccountManagerResponse response, String accountType, String authTokenType, String[] requiredFeatures, boolean expectActivityLaunch, Bundle optionsIn) {
        try {
            this.getService().addAccount(VUserHandle.myUserId(), response, accountType, authTokenType, requiredFeatures, expectActivityLaunch, optionsIn);
        }
        catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void updateCredentials(IAccountManagerResponse response, Account account, String authTokenType, boolean expectActivityLaunch, Bundle loginOptions) {
        try {
            this.getService().updateCredentials(VUserHandle.myUserId(), response, account, authTokenType, expectActivityLaunch, loginOptions);
        }
        catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public boolean removeAccountExplicitly(Account account) {
        try {
            return this.getService().removeAccountExplicitly(VUserHandle.myUserId(), account);
        }
        catch (RemoteException e) {
            return (Boolean)VirtualRuntime.crash(e);
        }
    }

    public void setUserData(Account account, String key, String value) {
        try {
            this.getService().setUserData(VUserHandle.myUserId(), account, key, value);
        }
        catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void editProperties(IAccountManagerResponse response, String accountType, boolean expectActivityLaunch) {
        try {
            this.getService().editProperties(VUserHandle.myUserId(), response, accountType, expectActivityLaunch);
        }
        catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void getAuthTokenLabel(IAccountManagerResponse response, String accountType, String authTokenType) {
        try {
            this.getService().getAuthTokenLabel(VUserHandle.myUserId(), response, accountType, authTokenType);
        }
        catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void confirmCredentials(IAccountManagerResponse response, Account account, Bundle options, boolean expectActivityLaunch) {
        try {
            this.getService().confirmCredentials(VUserHandle.myUserId(), response, account, options, expectActivityLaunch);
        }
        catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void invalidateAuthToken(String accountType, String authToken) {
        try {
            this.getService().invalidateAuthToken(VUserHandle.myUserId(), accountType, authToken);
        }
        catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void getAccountsByFeatures(IAccountManagerResponse response, String type, String[] features) {
        try {
            this.getService().getAccountsByFeatures(VUserHandle.myUserId(), response, type, features);
        }
        catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void setAuthToken(Account account, String authTokenType, String authToken) {
        try {
            this.getService().setAuthToken(VUserHandle.myUserId(), account, authTokenType, authToken);
        }
        catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public Object getPassword(Account account) {
        try {
            return this.getService().getPassword(VUserHandle.myUserId(), account);
        }
        catch (RemoteException e) {
            return VirtualRuntime.crash(e);
        }
    }

    public String getUserData(Account account, String key) {
        try {
            return this.getService().getUserData(VUserHandle.myUserId(), account, key);
        }
        catch (RemoteException e) {
            return (String)VirtualRuntime.crash(e);
        }
    }

    public AccountManagerFuture<Bundle> addAccount(final int userId, final String accountType, final String authTokenType, final String[] requiredFeatures, Bundle addAccountOptions, final Activity activity, AccountManagerCallback<Bundle> callback, Handler handler) {
        if (accountType == null) {
            throw new IllegalArgumentException("accountType is null");
        }
        final Bundle optionsIn = new Bundle();
        if (addAccountOptions != null) {
            optionsIn.putAll(addAccountOptions);
        }
        optionsIn.putString("androidPackageName", "android");
        return new AmsTask(activity, handler, callback){

            @Override
            public void doWork() throws RemoteException {
                VAccountManager.this.addAccount(userId, this.mResponse, accountType, authTokenType, requiredFeatures, activity != null, optionsIn);
            }
        }.start();
    }

    public boolean setAccountVisibility(Account a, String packageName, int newVisibility) {
        try {
            return this.getService().setAccountVisibility(VUserHandle.myUserId(), a, packageName, newVisibility);
        }
        catch (RemoteException e) {
            return (Boolean)VirtualRuntime.crash(e);
        }
    }

    public int getAccountVisibility(Account a, String packageName) {
        try {
            return this.getService().getAccountVisibility(VUserHandle.myUserId(), a, packageName);
        }
        catch (RemoteException e) {
            return (Integer)VirtualRuntime.crash(e);
        }
    }

    public void startAddAccountSession(IAccountManagerResponse response, String accountType, String authTokenType, String[] requiredFeatures, boolean expectActivityLaunch, Bundle options) {
        try {
            this.getService().startAddAccountSession(response, accountType, authTokenType, requiredFeatures, expectActivityLaunch, options);
        }
        catch (RemoteException e) {
            VirtualRuntime.crash(e);
        }
    }

    public void startUpdateCredentialsSession(IAccountManagerResponse response, Account account, String authTokenType, boolean expectActivityLaunch, Bundle options) {
        try {
            this.getService().startUpdateCredentialsSession(response, account, authTokenType, expectActivityLaunch, options);
        }
        catch (RemoteException e) {
            VirtualRuntime.crash(e);
        }
    }

    public void registerAccountListener(String[] accountTypes) {
        try {
            this.getService().registerAccountListener(accountTypes);
        }
        catch (RemoteException e) {
            VirtualRuntime.crash(e);
        }
    }

    public void unregisterAccountListener(String[] accountTypes) {
        try {
            this.getService().unregisterAccountListener(accountTypes);
        }
        catch (RemoteException e) {
            VirtualRuntime.crash(e);
        }
    }

    public Map getPackagesAndVisibilityForAccount(Account account) {
        try {
            return this.getService().getPackagesAndVisibilityForAccount(VUserHandle.myUserId(), account);
        }
        catch (RemoteException e) {
            return (Map)VirtualRuntime.crash(e);
        }
    }

    public Map getAccountsAndVisibilityForPackage(String packageName, String accountType) {
        try {
            return this.getService().getAccountsAndVisibilityForPackage(VUserHandle.myUserId(), packageName, accountType);
        }
        catch (RemoteException e) {
            return (Map)VirtualRuntime.crash(e);
        }
    }

    public void finishSessionAsUser(IAccountManagerResponse response, Bundle sessionBundle, boolean expectActivityLaunch, Bundle appInfo, int userId) {
        try {
            this.getService().finishSessionAsUser(response, sessionBundle, expectActivityLaunch, appInfo, userId);
        }
        catch (RemoteException e) {
            VirtualRuntime.crash(e);
        }
    }

    public void isCredentialsUpdateSuggested(IAccountManagerResponse response, Account account, String statusToken) {
        try {
            this.getService().isCredentialsUpdateSuggested(response, account, statusToken);
        }
        catch (RemoteException e) {
            VirtualRuntime.crash(e);
        }
    }

    public boolean addAccountExplicitlyWithVisibility(Account account, String password, Bundle extras, Map visibility) {
        try {
            return this.getService().addAccountExplicitlyWithVisibility(VUserHandle.myUserId(), account, password, extras, visibility);
        }
        catch (RemoteException e) {
            return (Boolean)VirtualRuntime.crash(e);
        }
    }
}

