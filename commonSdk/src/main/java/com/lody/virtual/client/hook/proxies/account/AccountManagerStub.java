/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.accounts.Account
 *  android.accounts.AccountManager
 *  android.annotation.TargetApi
 *  android.os.Bundle
 */
package com.lody.virtual.client.hook.proxies.account;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.IAccountManagerResponse;
import android.annotation.TargetApi;
import android.os.Bundle;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.BinderInvocationStub;
import com.lody.virtual.client.hook.base.MethodProxy;
import com.lody.virtual.client.ipc.VAccountManager;
import com.lody.virtual.helper.compat.BuildCompat;
import com.lody.virtual.helper.utils.Reflect;
import com.lody.virtual.os.VUserHandle;
import java.lang.reflect.Method;
import java.util.Map;
import mirror.android.accounts.IAccountManager;

public class AccountManagerStub
extends BinderInvocationProxy {
    private static VAccountManager Mgr = VAccountManager.get();

    public AccountManagerStub() {
        super(IAccountManager.Stub.asInterface, "account");
    }

    @Override
    public void inject() throws Throwable {
        super.inject();
        try {
            AccountManager accountManager = (AccountManager)this.getContext().getSystemService("account");
            Reflect.on(accountManager).set("mService", ((BinderInvocationStub)this.getInvocationStub()).getProxyInterface());
        }
        catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onBindMethods() {
        super.onBindMethods();
        this.addMethodProxy(new getPassword());
        this.addMethodProxy(new getUserData());
        this.addMethodProxy(new getAuthenticatorTypes());
        this.addMethodProxy(new getAccounts());
        this.addMethodProxy(new getAccountsForPackage());
        this.addMethodProxy(new getAccountsByTypeForPackage());
        this.addMethodProxy(new getAccountsAsUser());
        this.addMethodProxy(new hasFeatures());
        this.addMethodProxy(new getAccountsByFeatures());
        this.addMethodProxy(new addAccountExplicitly());
        this.addMethodProxy(new removeAccount());
        this.addMethodProxy(new removeAccountAsUser());
        this.addMethodProxy(new removeAccountExplicitly());
        this.addMethodProxy(new copyAccountToUser());
        this.addMethodProxy(new invalidateAuthToken());
        this.addMethodProxy(new peekAuthToken());
        this.addMethodProxy(new setAuthToken());
        this.addMethodProxy(new setPassword());
        this.addMethodProxy(new clearPassword());
        this.addMethodProxy(new setUserData());
        this.addMethodProxy(new updateAppPermission());
        this.addMethodProxy(new getAuthToken());
        this.addMethodProxy(new addAccount());
        this.addMethodProxy(new addAccountAsUser());
        this.addMethodProxy(new updateCredentials());
        this.addMethodProxy(new editProperties());
        this.addMethodProxy(new confirmCredentialsAsUser());
        this.addMethodProxy(new accountAuthenticated());
        this.addMethodProxy(new getAuthTokenLabel());
        this.addMethodProxy(new addSharedAccountAsUser());
        this.addMethodProxy(new getSharedAccountsAsUser());
        this.addMethodProxy(new removeSharedAccountAsUser());
        this.addMethodProxy(new renameAccount());
        this.addMethodProxy(new getPreviousName());
        this.addMethodProxy(new renameSharedAccountAsUser());
        if (BuildCompat.isOreo()) {
            this.addMethodProxy(new finishSessionAsUser());
            this.addMethodProxy(new getAccountVisibility());
            this.addMethodProxy(new addAccountExplicitlyWithVisibility());
            this.addMethodProxy(new getAccountsAndVisibilityForPackage());
            this.addMethodProxy(new getPackagesAndVisibilityForAccount());
            this.addMethodProxy(new setAccountVisibility());
            this.addMethodProxy(new startAddAccountSession());
            this.addMethodProxy(new startUpdateCredentialsSession());
            this.addMethodProxy(new registerAccountListener());
            this.addMethodProxy(new unregisterAccountListener());
        }
    }

    @TargetApi(value=26)
    private static class unregisterAccountListener
    extends MethodProxy {
        private unregisterAccountListener() {
        }

        @Override
        public String getMethodName() {
            return "unregisterAccountListener";
        }

        @Override
        public Object call(Object obj, Method method, Object ... objArr) throws Throwable {
            Mgr.unregisterAccountListener((String[])objArr[0]);
            return 0;
        }
    }

    @TargetApi(value=26)
    private static class registerAccountListener
    extends MethodProxy {
        private registerAccountListener() {
        }

        @Override
        public String getMethodName() {
            return "registerAccountListener";
        }

        @Override
        public Object call(Object obj, Method method, Object ... objArr) throws Throwable {
            Mgr.registerAccountListener((String[])objArr[0]);
            return 0;
        }
    }

    @TargetApi(value=26)
    private static class startUpdateCredentialsSession
    extends MethodProxy {
        private startUpdateCredentialsSession() {
        }

        @Override
        public String getMethodName() {
            return "startUpdateCredentialsSession";
        }

        @Override
        public Object call(Object obj, Method method, Object ... objArr) throws Throwable {
            Mgr.startUpdateCredentialsSession((IAccountManagerResponse)objArr[0], (Account)objArr[1], (String)objArr[2], (Boolean)objArr[3], (Bundle)objArr[4]);
            return 0;
        }
    }

    @TargetApi(value=26)
    private static class startAddAccountSession
    extends MethodProxy {
        private startAddAccountSession() {
        }

        @Override
        public String getMethodName() {
            return "startAddAccountSession";
        }

        @Override
        public Object call(Object obj, Method method, Object ... objArr) throws Throwable {
            Mgr.startAddAccountSession((IAccountManagerResponse)objArr[0], (String)objArr[1], (String)objArr[2], (String[])objArr[3], (Boolean)objArr[4], (Bundle)objArr[5]);
            return 0;
        }
    }

    @TargetApi(value=26)
    private static class setAccountVisibility
    extends MethodProxy {
        private setAccountVisibility() {
        }

        @Override
        public String getMethodName() {
            return "setAccountVisibility";
        }

        @Override
        public Object call(Object obj, Method method, Object ... objArr) throws Throwable {
            return Mgr.setAccountVisibility((Account)objArr[0], (String)objArr[1], (Integer)objArr[2]);
        }
    }

    @TargetApi(value=26)
    private static class getPackagesAndVisibilityForAccount
    extends MethodProxy {
        private getPackagesAndVisibilityForAccount() {
        }

        @Override
        public String getMethodName() {
            return "getPackagesAndVisibilityForAccount";
        }

        @Override
        public Object call(Object obj, Method method, Object ... objArr) throws Throwable {
            return Mgr.getPackagesAndVisibilityForAccount((Account)objArr[0]);
        }
    }

    @TargetApi(value=26)
    private static class getAccountsAndVisibilityForPackage
    extends MethodProxy {
        private getAccountsAndVisibilityForPackage() {
        }

        @Override
        public String getMethodName() {
            return "getAccountsAndVisibilityForPackage";
        }

        @Override
        public Object call(Object obj, Method method, Object ... objArr) throws Throwable {
            return Mgr.getAccountsAndVisibilityForPackage((String)objArr[0], (String)objArr[1]);
        }
    }

    @TargetApi(value=26)
    private static class addAccountExplicitlyWithVisibility
    extends MethodProxy {
        private addAccountExplicitlyWithVisibility() {
        }

        @Override
        public String getMethodName() {
            return "addAccountExplicitlyWithVisibility";
        }

        @Override
        public Object call(Object obj, Method method, Object ... objArr) throws Throwable {
            return Mgr.addAccountExplicitlyWithVisibility((Account)objArr[0], (String)objArr[1], (Bundle)objArr[2], (Map)objArr[3]);
        }
    }

    @TargetApi(value=26)
    private static class getAccountVisibility
    extends MethodProxy {
        private getAccountVisibility() {
        }

        @Override
        public String getMethodName() {
            return "getAccountVisibility";
        }

        @Override
        public Object call(Object obj, Method method, Object ... objArr) throws Throwable {
            return Mgr.getAccountVisibility((Account)objArr[0], (String)objArr[1]);
        }
    }

    @TargetApi(value=26)
    private static class finishSessionAsUser
    extends MethodProxy {
        private finishSessionAsUser() {
        }

        @Override
        public String getMethodName() {
            return "finishSessionAsUser";
        }

        @Override
        public Object call(Object obj, Method method, Object ... objArr) throws Throwable {
            Mgr.finishSessionAsUser((IAccountManagerResponse)objArr[0], (Bundle)objArr[1], (Boolean)objArr[2], (Bundle)objArr[3], (Integer)objArr[4]);
            return 0;
        }
    }

    @TargetApi(value=26)
    private static class isCredentialsUpdateSuggested
    extends MethodProxy {
        private isCredentialsUpdateSuggested() {
        }

        @Override
        public String getMethodName() {
            return "isCredentialsUpdateSuggested";
        }

        @Override
        public Object call(Object obj, Method method, Object ... objArr) throws Throwable {
            Mgr.isCredentialsUpdateSuggested((IAccountManagerResponse)objArr[0], (Account)objArr[1], (String)objArr[2]);
            return 0;
        }
    }

    private static class renameSharedAccountAsUser
    extends MethodProxy {
        private renameSharedAccountAsUser() {
        }

        @Override
        public String getMethodName() {
            return "renameSharedAccountAsUser";
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            Account accountToRename = (Account)args[0];
            String newName = (String)args[1];
            int userId = (Integer)args[2];
            return method.invoke(who, args);
        }
    }

    private static class getPreviousName
    extends MethodProxy {
        private getPreviousName() {
        }

        @Override
        public String getMethodName() {
            return "getPreviousName";
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            Account account = (Account)args[0];
            return Mgr.getPreviousName(account);
        }
    }

    private static class renameAccount
    extends MethodProxy {
        private renameAccount() {
        }

        @Override
        public String getMethodName() {
            return "renameAccount";
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            IAccountManagerResponse response = (IAccountManagerResponse)args[0];
            Account accountToRename = (Account)args[1];
            String newName = (String)args[2];
            Mgr.renameAccount(response, accountToRename, newName);
            return 0;
        }
    }

    private static class removeSharedAccountAsUser
    extends MethodProxy {
        private removeSharedAccountAsUser() {
        }

        @Override
        public String getMethodName() {
            return "removeSharedAccountAsUser";
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            Account account = (Account)args[0];
            int userId = (Integer)args[1];
            return method.invoke(who, args);
        }
    }

    private static class getSharedAccountsAsUser
    extends MethodProxy {
        private getSharedAccountsAsUser() {
        }

        @Override
        public String getMethodName() {
            return "getSharedAccountsAsUser";
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            int userId = (Integer)args[0];
            return method.invoke(who, args);
        }
    }

    private static class addSharedAccountAsUser
    extends MethodProxy {
        private addSharedAccountAsUser() {
        }

        @Override
        public String getMethodName() {
            return "addSharedAccountAsUser";
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            Account account = (Account)args[0];
            int userId = (Integer)args[1];
            return method.invoke(who, args);
        }
    }

    private static class getAuthTokenLabel
    extends MethodProxy {
        private getAuthTokenLabel() {
        }

        @Override
        public String getMethodName() {
            return "getAuthTokenLabel";
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            IAccountManagerResponse response = (IAccountManagerResponse)args[0];
            String accountType = (String)args[1];
            String authTokenType = (String)args[2];
            Mgr.getAuthTokenLabel(response, accountType, authTokenType);
            return 0;
        }
    }

    private static class accountAuthenticated
    extends MethodProxy {
        private accountAuthenticated() {
        }

        @Override
        public String getMethodName() {
            return "accountAuthenticated";
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            Account account = (Account)args[0];
            return Mgr.accountAuthenticated(account);
        }
    }

    private static class confirmCredentialsAsUser
    extends MethodProxy {
        private confirmCredentialsAsUser() {
        }

        @Override
        public String getMethodName() {
            return "confirmCredentialsAsUser";
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            IAccountManagerResponse response = (IAccountManagerResponse)args[0];
            Account account = (Account)args[1];
            Bundle options = (Bundle)args[2];
            boolean expectActivityLaunch = (Boolean)args[3];
            Mgr.confirmCredentials(response, account, options, expectActivityLaunch);
            return 0;
        }
    }

    private static class editProperties
    extends MethodProxy {
        private editProperties() {
        }

        @Override
        public String getMethodName() {
            return "editProperties";
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            IAccountManagerResponse response = (IAccountManagerResponse)args[0];
            String authTokenType = (String)args[1];
            boolean expectActivityLaunch = (Boolean)args[2];
            Mgr.editProperties(response, authTokenType, expectActivityLaunch);
            return 0;
        }
    }

    private static class updateCredentials
    extends MethodProxy {
        private updateCredentials() {
        }

        @Override
        public String getMethodName() {
            return "updateCredentials";
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            IAccountManagerResponse response = (IAccountManagerResponse)args[0];
            Account account = (Account)args[1];
            String authTokenType = (String)args[2];
            boolean expectActivityLaunch = (Boolean)args[3];
            Bundle options = (Bundle)args[4];
            Mgr.updateCredentials(response, account, authTokenType, expectActivityLaunch, options);
            return 0;
        }
    }

    private static class addAccountAsUser
    extends MethodProxy {
        private addAccountAsUser() {
        }

        @Override
        public String getMethodName() {
            return "addAccountAsUser";
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            IAccountManagerResponse response = (IAccountManagerResponse)args[0];
            String accountType = (String)args[1];
            String authTokenType = (String)args[2];
            String[] requiredFeatures = (String[])args[3];
            boolean expectActivityLaunch = (Boolean)args[4];
            Bundle options = (Bundle)args[5];
            Mgr.addAccount(response, accountType, authTokenType, requiredFeatures, expectActivityLaunch, options);
            return 0;
        }
    }

    private static class addAccount
    extends MethodProxy {
        private addAccount() {
        }

        @Override
        public String getMethodName() {
            return "addAccount";
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            IAccountManagerResponse response = (IAccountManagerResponse)args[0];
            String accountType = (String)args[1];
            String authTokenType = (String)args[2];
            String[] requiredFeatures = (String[])args[3];
            boolean expectActivityLaunch = (Boolean)args[4];
            Bundle options = (Bundle)args[5];
            Mgr.addAccount(response, accountType, authTokenType, requiredFeatures, expectActivityLaunch, options);
            return 0;
        }
    }

    private static class getAuthToken
    extends MethodProxy {
        private getAuthToken() {
        }

        @Override
        public String getMethodName() {
            return "getAuthToken";
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            IAccountManagerResponse response = (IAccountManagerResponse)args[0];
            Account account = (Account)args[1];
            String authTokenType = (String)args[2];
            boolean notifyOnAuthFailure = (Boolean)args[3];
            boolean expectActivityLaunch = (Boolean)args[4];
            Bundle options = (Bundle)args[5];
            Mgr.getAuthToken(response, account, authTokenType, notifyOnAuthFailure, expectActivityLaunch, options);
            return 0;
        }
    }

    private static class updateAppPermission
    extends MethodProxy {
        private updateAppPermission() {
        }

        @Override
        public String getMethodName() {
            return "updateAppPermission";
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            Account account = (Account)args[0];
            String authTokenType = (String)args[1];
            int uid = (Integer)args[2];
            boolean val = (Boolean)args[3];
            method.invoke(who, args);
            return 0;
        }
    }

    private static class setUserData
    extends MethodProxy {
        private setUserData() {
        }

        @Override
        public String getMethodName() {
            return "setUserData";
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            Account account = (Account)args[0];
            String key = (String)args[1];
            String value = (String)args[2];
            Mgr.setUserData(account, key, value);
            return 0;
        }
    }

    private static class clearPassword
    extends MethodProxy {
        private clearPassword() {
        }

        @Override
        public String getMethodName() {
            return "clearPassword";
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            Account account = (Account)args[0];
            Mgr.clearPassword(account);
            return 0;
        }
    }

    private static class setPassword
    extends MethodProxy {
        private setPassword() {
        }

        @Override
        public String getMethodName() {
            return "setPassword";
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            Account account = (Account)args[0];
            String password = (String)args[1];
            Mgr.setPassword(account, password);
            return 0;
        }
    }

    private static class setAuthToken
    extends MethodProxy {
        private setAuthToken() {
        }

        @Override
        public String getMethodName() {
            return "setAuthToken";
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            Account account = (Account)args[0];
            String authTokenType = (String)args[1];
            String authToken = (String)args[2];
            Mgr.setAuthToken(account, authTokenType, authToken);
            return 0;
        }
    }

    private static class peekAuthToken
    extends MethodProxy {
        private peekAuthToken() {
        }

        @Override
        public String getMethodName() {
            return "peekAuthToken";
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            Account account = (Account)args[0];
            String authTokenType = (String)args[1];
            return Mgr.peekAuthToken(account, authTokenType);
        }
    }

    private static class invalidateAuthToken
    extends MethodProxy {
        private invalidateAuthToken() {
        }

        @Override
        public String getMethodName() {
            return "invalidateAuthToken";
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            String accountType = (String)args[0];
            String authToken = (String)args[1];
            Mgr.invalidateAuthToken(accountType, authToken);
            return 0;
        }
    }

    private static class copyAccountToUser
    extends MethodProxy {
        private copyAccountToUser() {
        }

        @Override
        public String getMethodName() {
            return "copyAccountToUser";
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            IAccountManagerResponse response = (IAccountManagerResponse)args[0];
            Account account = (Account)args[1];
            int userFrom = (Integer)args[2];
            int userTo = (Integer)args[3];
            method.invoke(who, args);
            return 0;
        }
    }

    private static class removeAccountExplicitly
    extends MethodProxy {
        private removeAccountExplicitly() {
        }

        @Override
        public String getMethodName() {
            return "removeAccountExplicitly";
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            Account account = (Account)args[0];
            return Mgr.removeAccountExplicitly(account);
        }
    }

    private static class removeAccountAsUser
    extends MethodProxy {
        private removeAccountAsUser() {
        }

        @Override
        public String getMethodName() {
            return "removeAccountAsUser";
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            IAccountManagerResponse response = (IAccountManagerResponse)args[0];
            Account account = (Account)args[1];
            boolean expectActivityLaunch = (Boolean)args[2];
            Mgr.removeAccount(response, account, expectActivityLaunch);
            return 0;
        }
    }

    private static class removeAccount
    extends MethodProxy {
        private removeAccount() {
        }

        @Override
        public String getMethodName() {
            return "removeAccount";
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            IAccountManagerResponse response = (IAccountManagerResponse)args[0];
            Account account = (Account)args[1];
            boolean expectActivityLaunch = (Boolean)args[2];
            Mgr.removeAccount(response, account, expectActivityLaunch);
            return 0;
        }
    }

    private static class addAccountExplicitly
    extends MethodProxy {
        private addAccountExplicitly() {
        }

        @Override
        public String getMethodName() {
            return "addAccountExplicitly";
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            Account account = (Account)args[0];
            String password = (String)args[1];
            Bundle extras = (Bundle)args[2];
            return Mgr.addAccountExplicitly(account, password, extras);
        }
    }

    private static class getAccountsByFeatures
    extends MethodProxy {
        private getAccountsByFeatures() {
        }

        @Override
        public String getMethodName() {
            return "getAccountsByFeatures";
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            IAccountManagerResponse response = (IAccountManagerResponse)args[0];
            String accountType = (String)args[1];
            String[] features = (String[])args[2];
            Mgr.getAccountsByFeatures(response, accountType, features);
            return 0;
        }
    }

    private static class hasFeatures
    extends MethodProxy {
        private hasFeatures() {
        }

        @Override
        public String getMethodName() {
            return "hasFeatures";
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            IAccountManagerResponse response = (IAccountManagerResponse)args[0];
            Account account = (Account)args[1];
            String[] features = (String[])args[2];
            Mgr.hasFeatures(response, account, features);
            return 0;
        }
    }

    private static class getAccountsAsUser
    extends MethodProxy {
        private getAccountsAsUser() {
        }

        @Override
        public String getMethodName() {
            return "getAccountsAsUser";
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            String accountType = (String)args[0];
            return Mgr.getAccounts(accountType);
        }
    }

    private static class getAccountByTypeAndFeatures
    extends MethodProxy {
        private getAccountByTypeAndFeatures() {
        }

        @Override
        public String getMethodName() {
            return "getAccountByTypeAndFeatures";
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            String type = (String)args[0];
            String packageName = (String)args[1];
            return Mgr.getAccounts(type);
        }
    }

    private static class getAccountsByTypeForPackage
    extends MethodProxy {
        private getAccountsByTypeForPackage() {
        }

        @Override
        public String getMethodName() {
            return "getAccountsByTypeForPackage";
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            String type = (String)args[0];
            String packageName = (String)args[1];
            return Mgr.getAccounts(type);
        }
    }

    private static class getAccountsForPackage
    extends MethodProxy {
        private getAccountsForPackage() {
        }

        @Override
        public String getMethodName() {
            return "getAccountsForPackage";
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            String packageName = (String)args[0];
            return Mgr.getAccounts(null);
        }
    }

    private static class getAccounts
    extends MethodProxy {
        private getAccounts() {
        }

        @Override
        public String getMethodName() {
            return "getAccounts";
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            String accountType = (String)args[0];
            return Mgr.getAccounts(accountType);
        }
    }

    private static class getAuthenticatorTypes
    extends MethodProxy {
        private getAuthenticatorTypes() {
        }

        @Override
        public String getMethodName() {
            return "getAuthenticatorTypes";
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            return Mgr.getAuthenticatorTypes(VUserHandle.myUserId());
        }
    }

    private static class getUserData
    extends MethodProxy {
        private getUserData() {
        }

        @Override
        public String getMethodName() {
            return "getUserData";
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            Account account = (Account)args[0];
            String key = (String)args[1];
            return Mgr.getUserData(account, key);
        }
    }

    private static class getPassword
    extends MethodProxy {
        private getPassword() {
        }

        @Override
        public String getMethodName() {
            return "getPassword";
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            Account account = (Account)args[0];
            return Mgr.getPassword(account);
        }
    }
}

