//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.lody.virtual.server.accounts;

import android.accounts.Account;
import android.accounts.AuthenticatorDescription;
import android.accounts.IAccountAuthenticator;
import android.accounts.IAccountAuthenticatorResponse;
import android.accounts.IAccountManagerResponse;
import android.accounts.IAccountAuthenticator.Stub;
import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.util.Xml;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.ipc.VActivityManager;
import com.lody.virtual.helper.utils.Singleton;
import com.lody.virtual.helper.utils.VLog;
import com.lody.virtual.os.VBinder;
import com.lody.virtual.os.VEnvironment;
import com.lody.virtual.os.VUserHandle;
import com.lody.virtual.server.am.VActivityManagerService;
import com.lody.virtual.server.interfaces.IAccountManager;
import com.lody.virtual.server.pm.VPackageManagerService;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import mirror.com.android.internal.R_Hide.styleable;

public class VAccountManagerService extends IAccountManager.Stub {
    private static final Singleton<VAccountManagerService> sInstance = new Singleton<VAccountManagerService>() {
        protected VAccountManagerService create() {
            return new VAccountManagerService();
        }
    };
    private static final long CHECK_IN_TIME = 43200000L;
    private static final String TAG = VAccountManagerService.class.getSimpleName();
    private final SparseArray<List<VAccount>> accountsByUserId = new SparseArray();
    private final SparseArray<List<VAccountVisibility>> accountsVisibilitiesByUserId = new SparseArray();
    private final LinkedList<AuthTokenRecord> authTokenRecords = new LinkedList();
    private final LinkedHashMap<String, Session> mSessions = new LinkedHashMap();
    private final AuthenticatorCache cache = new AuthenticatorCache();
    private Context mContext = VirtualCore.get().getContext();
    private long lastAccountChangeTime = 0L;

    public VAccountManagerService() {
    }

    public static VAccountManagerService get() {
        return (VAccountManagerService)sInstance.get();
    }

    public static void systemReady() {
        get().readAllAccounts();
        get().readAllAccountVisibilities();
    }

    private static AuthenticatorDescription parseAuthenticatorDescription(Resources resources, String packageName, AttributeSet attributeSet) {
        TypedArray array = resources.obtainAttributes(attributeSet, (int[])styleable.AccountAuthenticator.get());

        AuthenticatorDescription var10;
        try {
            String accountType = array.getString(styleable.AccountAuthenticator_accountType.get());
            int label = array.getResourceId(styleable.AccountAuthenticator_label.get(), 0);
            int icon = array.getResourceId(styleable.AccountAuthenticator_icon.get(), 0);
            int smallIcon = array.getResourceId(styleable.AccountAuthenticator_smallIcon.get(), 0);
            int accountPreferences = array.getResourceId(styleable.AccountAuthenticator_accountPreferences.get(), 0);
            boolean customTokens = array.getBoolean(styleable.AccountAuthenticator_customTokens.get(), false);
            if (TextUtils.isEmpty(accountType)) {
                var10 = null;
                return var10;
            }

            var10 = new AuthenticatorDescription(accountType, packageName, label, icon, smallIcon, accountPreferences, customTokens);
        } finally {
            array.recycle();
        }

        return var10;
    }

    public AuthenticatorDescription[] getAuthenticatorTypes(int userId) {
        synchronized(this.cache) {
            AuthenticatorDescription[] descArray = new AuthenticatorDescription[this.cache.authenticators.size()];
            int i = 0;

            for(Iterator var5 = this.cache.authenticators.values().iterator(); var5.hasNext(); ++i) {
                AuthenticatorInfo info = (AuthenticatorInfo)var5.next();
                descArray[i] = info.desc;
            }

            return descArray;
        }
    }

    public void getAccountsByFeatures(int userId, IAccountManagerResponse response, String type, String[] features) {
        if (response == null) {
            throw new IllegalArgumentException("response is null");
        } else if (type == null) {
            throw new IllegalArgumentException("accountType is null");
        } else {
            AuthenticatorInfo info = this.getAuthenticatorInfo(type);
            Bundle bundle;
            if (info == null) {
                bundle = new Bundle();
                bundle.putParcelableArray("accounts", new Account[0]);

                try {
                    response.onResult(bundle);
                } catch (RemoteException var8) {
                    var8.printStackTrace();
                }

            } else {
                if (features != null && features.length != 0) {
                    (new GetAccountsByTypeAndFeatureSession(response, userId, info, features)).bind();
                } else {
                    bundle = new Bundle();
                    bundle.putParcelableArray("accounts", this.getAccounts(userId, type));

                    try {
                        response.onResult(bundle);
                    } catch (RemoteException var9) {
                        var9.printStackTrace();
                    }
                }

            }
        }
    }

    public final String getPreviousName(int userId, Account account) {
        if (account == null) {
            throw new IllegalArgumentException("account is null");
        } else {
            synchronized(this.accountsByUserId) {
                String previousName = null;
                VAccount vAccount = this.getAccount(userId, account);
                if (vAccount != null) {
                    previousName = vAccount.previousName;
                }

                return previousName;
            }
        }
    }

    public Account[] getAccounts(int userId, String type) {
        List<Account> accountList = this.getAccountList(userId, type);
        return (Account[])accountList.toArray(new Account[accountList.size()]);
    }

    private List<Account> getAccountList(int userId, String type) {
        synchronized(this.accountsByUserId) {
            List<Account> accounts = new ArrayList();
            List<VAccount> vAccounts = (List)this.accountsByUserId.get(userId);
            if (vAccounts != null) {
                Iterator var6 = vAccounts.iterator();

                while(true) {
                    VAccount vAccount;
                    do {
                        if (!var6.hasNext()) {
                            return accounts;
                        }

                        vAccount = (VAccount)var6.next();
                    } while(type != null && !vAccount.type.equals(type));

                    accounts.add(new Account(vAccount.name, vAccount.type));
                }
            } else {
                return accounts;
            }
        }
    }

    public final void getAuthToken(final int userId, IAccountManagerResponse response, final Account account, final String authTokenType, final boolean notifyOnAuthFailure, boolean expectActivityLaunch, final Bundle loginOptions) {
        if (response == null) {
            throw new IllegalArgumentException("response is null");
        } else {
            try {
                if (account == null) {
                    VLog.w(TAG, "getAuthToken called with null account", new Object[0]);
                    response.onError(7, "account is null");
                    return;
                }

                if (authTokenType == null) {
                    VLog.w(TAG, "getAuthToken called with null authTokenType", new Object[0]);
                    response.onError(7, "authTokenType is null");
                    return;
                }
            } catch (RemoteException var16) {
                var16.printStackTrace();
                return;
            }

            AuthenticatorInfo info = this.getAuthenticatorInfo(account.type);
            if (info == null) {
                try {
                    response.onError(7, "account.type does not exist");
                } catch (RemoteException var14) {
                    var14.printStackTrace();
                }

            } else {
                final String callerPkg = loginOptions.getString("androidPackageName");
                final boolean customTokens = info.desc.customTokens;
                loginOptions.putInt("callerUid", VBinder.getCallingUid());
                loginOptions.putInt("callerPid", VBinder.getCallingPid());
                if (notifyOnAuthFailure) {
                    loginOptions.putBoolean("notifyOnAuthFailure", true);
                }

                if (!customTokens) {
                    VAccount vAccount;
                    synchronized(this.accountsByUserId) {
                        vAccount = this.getAccount(userId, account);
                    }

                    String authToken = vAccount != null ? (String)vAccount.authTokens.get(authTokenType) : null;
                    if (authToken != null) {
                        Bundle result = new Bundle();
                        result.putString("authtoken", authToken);
                        result.putString("authAccount", account.name);
                        result.putString("accountType", account.type);
                        this.onResult(response, result);
                        return;
                    }
                }

                if (customTokens) {
                    String authToken = this.getCustomAuthToken(userId, account, authTokenType, callerPkg);
                    if (authToken != null) {
                        Bundle result = new Bundle();
                        result.putString("authtoken", authToken);
                        result.putString("authAccount", account.name);
                        result.putString("accountType", account.type);
                        this.onResult(response, result);
                        return;
                    }
                }

                (new Session(response, userId, info, expectActivityLaunch, false, account.name) {
                    protected String toDebugString(long now) {
                        return super.toDebugString(now) + ", getAuthToken, " + account + ", authTokenType " + authTokenType + ", loginOptions " + loginOptions + ", notifyOnAuthFailure " + notifyOnAuthFailure;
                    }

                    public void run() throws RemoteException {
                        this.mAuthenticator.getAuthToken(this, account, authTokenType, loginOptions);
                    }

                    public void onResult(Bundle result) throws RemoteException {
                        if (result != null) {
                            String authToken = result.getString("authtoken");
                            if (authToken != null) {
                                String name = result.getString("authAccount");
                                String type = result.getString("accountType");
                                if (TextUtils.isEmpty(type) || TextUtils.isEmpty(name)) {
                                    this.onError(5, "the type and name should not be empty");
                                    return;
                                }

                                if (!customTokens) {
                                    synchronized(VAccountManagerService.this.accountsByUserId) {
                                        VAccount accountx = VAccountManagerService.this.getAccount(userId, name, type);
                                        if (accountx == null) {
                                            List<VAccount> accounts = (List)VAccountManagerService.this.accountsByUserId.get(userId);
                                            if (accounts == null) {
                                                accounts = new ArrayList();
                                                VAccountManagerService.this.accountsByUserId.put(userId, accounts);
                                            }

                                            accountx = new VAccount(userId, new Account(name, type));
                                            ((List)accounts).add(accountx);
                                            VAccountManagerService.this.saveAllAccounts();
                                        }
                                    }
                                }

                                long expiryMillis = result.getLong("android.accounts.expiry", 0L);
                                if (customTokens && expiryMillis > System.currentTimeMillis()) {
                                    AuthTokenRecord record = new AuthTokenRecord(userId, account, authTokenType, callerPkg, authToken, expiryMillis);
                                    synchronized(VAccountManagerService.this.authTokenRecords) {
                                        VAccountManagerService.this.authTokenRecords.remove(record);
                                        VAccountManagerService.this.authTokenRecords.add(record);
                                    }
                                }
                            }

                            Intent intent = (Intent)result.getParcelable("intent");
                            if (intent != null && notifyOnAuthFailure && !customTokens) {
                            }
                        }

                        super.onResult(result);
                    }
                }).bind();
            }
        }
    }

    public void setPassword(int userId, Account account, String password) {
        if (account == null) {
            throw new IllegalArgumentException("account is null");
        } else {
            this.setPasswordInternal(userId, account, password);
        }
    }

    private void setPasswordInternal(int userId, Account account, String password) {
        synchronized(this.accountsByUserId) {
            VAccount vAccount = this.getAccount(userId, account);
            if (vAccount != null) {
                vAccount.password = password;
                vAccount.authTokens.clear();
                this.saveAllAccounts();
                synchronized(this.authTokenRecords) {
                    Iterator<AuthTokenRecord> iterator = this.authTokenRecords.iterator();

                    while(iterator.hasNext()) {
                        AuthTokenRecord record = (AuthTokenRecord)iterator.next();
                        if (record.userId == userId && record.account.equals(account)) {
                            iterator.remove();
                        }
                    }
                }

                this.sendAccountsChangedBroadcast(userId);
            }

        }
    }

    public void setAuthToken(int userId, Account account, String authTokenType, String authToken) {
        if (account == null) {
            throw new IllegalArgumentException("account is null");
        } else if (authTokenType == null) {
            throw new IllegalArgumentException("authTokenType is null");
        } else {
            synchronized(this.accountsByUserId) {
                VAccount vAccount = this.getAccount(userId, account);
                if (vAccount != null) {
                    vAccount.authTokens.put(authTokenType, authToken);
                    this.saveAllAccounts();
                }

            }
        }
    }

    public void setUserData(int userId, Account account, String key, String value) {
        if (key == null) {
            throw new IllegalArgumentException("key is null");
        } else if (account == null) {
            throw new IllegalArgumentException("account is null");
        } else {
            VAccount vAccount = this.getAccount(userId, account);
            if (vAccount != null) {
                synchronized(this.accountsByUserId) {
                    vAccount.userDatas.put(key, value);
                    this.saveAllAccounts();
                }
            }

        }
    }

    public void hasFeatures(int userId, IAccountManagerResponse response, final Account account, final String[] features) {
        if (response == null) {
            throw new IllegalArgumentException("response is null");
        } else if (account == null) {
            throw new IllegalArgumentException("account is null");
        } else if (features == null) {
            throw new IllegalArgumentException("features is null");
        } else {
            AuthenticatorInfo info = this.getAuthenticatorInfo(account.type);
            if (info == null) {
                try {
                    response.onError(7, "account.type does not exist");
                } catch (RemoteException var7) {
                    var7.printStackTrace();
                }

            } else {
                (new Session(response, userId, info, false, true, account.name) {
                    public void run() throws RemoteException {
                        try {
                            this.mAuthenticator.hasFeatures(this, account, features);
                        } catch (RemoteException var2) {
                            this.onError(1, "remote exception");
                        }

                    }

                    public void onResult(Bundle result) throws RemoteException {
                        IAccountManagerResponse response = this.getResponseAndClose();
                        if (response != null) {
                            try {
                                if (result == null) {
                                    response.onError(5, "null bundle");
                                    return;
                                }

                                Log.v(VAccountManagerService.TAG, this.getClass().getSimpleName() + " calling onResult() on response " + response);
                                Bundle newResult = new Bundle();
                                newResult.putBoolean("booleanResult", result.getBoolean("booleanResult", false));
                                response.onResult(newResult);
                            } catch (RemoteException var4) {
                                Log.v(VAccountManagerService.TAG, "failure while notifying response", var4);
                            }
                        }

                    }
                }).bind();
            }
        }
    }

    public void updateCredentials(int userId, IAccountManagerResponse response, final Account account, final String authTokenType, boolean expectActivityLaunch, final Bundle loginOptions) {
        if (response == null) {
            throw new IllegalArgumentException("response is null");
        } else if (account == null) {
            throw new IllegalArgumentException("account is null");
        } else if (authTokenType == null) {
            throw new IllegalArgumentException("authTokenType is null");
        } else {
            AuthenticatorInfo info = this.getAuthenticatorInfo(account.type);
            if (info == null) {
                try {
                    response.onError(7, "account.type does not exist");
                } catch (RemoteException var9) {
                    var9.printStackTrace();
                }

            } else {
                (new Session(response, userId, info, expectActivityLaunch, false, account.name) {
                    public void run() throws RemoteException {
                        this.mAuthenticator.updateCredentials(this, account, authTokenType, loginOptions);
                    }

                    protected String toDebugString(long now) {
                        if (loginOptions != null) {
                            loginOptions.keySet();
                        }

                        return super.toDebugString(now) + ", updateCredentials, " + account + ", authTokenType " + authTokenType + ", loginOptions " + loginOptions;
                    }
                }).bind();
            }
        }
    }

    public String getPassword(int userId, Account account) {
        if (account == null) {
            throw new IllegalArgumentException("account is null");
        } else {
            synchronized(this.accountsByUserId) {
                VAccount vAccount = this.getAccount(userId, account);
                return vAccount != null ? vAccount.password : null;
            }
        }
    }

    public String getUserData(int userId, Account account, String key) {
        if (account == null) {
            throw new IllegalArgumentException("account is null");
        } else if (key == null) {
            throw new IllegalArgumentException("key is null");
        } else {
            synchronized(this.accountsByUserId) {
                VAccount vAccount = this.getAccount(userId, account);
                return vAccount != null ? (String)vAccount.userDatas.get(key) : null;
            }
        }
    }

    public void editProperties(int userId, IAccountManagerResponse response, final String accountType, boolean expectActivityLaunch) {
        if (response == null) {
            throw new IllegalArgumentException("response is null");
        } else if (accountType == null) {
            throw new IllegalArgumentException("accountType is null");
        } else {
            AuthenticatorInfo info = this.getAuthenticatorInfo(accountType);
            if (info == null) {
                try {
                    response.onError(7, "account.type does not exist");
                } catch (RemoteException var7) {
                    var7.printStackTrace();
                }

            } else {
                (new Session(response, userId, info, expectActivityLaunch, true, (String)null) {
                    public void run() throws RemoteException {
                        this.mAuthenticator.editProperties(this, this.mAuthenticatorInfo.desc.type);
                    }

                    protected String toDebugString(long now) {
                        return super.toDebugString(now) + ", editProperties, accountType " + accountType;
                    }
                }).bind();
            }
        }
    }

    public void getAuthTokenLabel(int userId, IAccountManagerResponse response, String accountType, final String authTokenType) {
        if (accountType == null) {
            throw new IllegalArgumentException("accountType is null");
        } else if (authTokenType == null) {
            throw new IllegalArgumentException("authTokenType is null");
        } else {
            AuthenticatorInfo info = this.getAuthenticatorInfo(accountType);
            if (info == null) {
                try {
                    response.onError(7, "account.type does not exist");
                } catch (RemoteException var7) {
                    var7.printStackTrace();
                }

            } else {
                (new Session(response, userId, info, false, false, (String)null) {
                    public void run() throws RemoteException {
                        this.mAuthenticator.getAuthTokenLabel(this, authTokenType);
                    }

                    public void onResult(Bundle result) throws RemoteException {
                        if (result != null) {
                            String label = result.getString("authTokenLabelKey");
                            Bundle bundle = new Bundle();
                            bundle.putString("authTokenLabelKey", label);
                            super.onResult(bundle);
                        } else {
                            super.onResult((Bundle)null);
                        }

                    }
                }).bind();
            }
        }
    }

    public void confirmCredentials(int userId, IAccountManagerResponse response, final Account account, final Bundle options, boolean expectActivityLaunch) {
        if (response == null) {
            throw new IllegalArgumentException("response is null");
        } else if (account == null) {
            throw new IllegalArgumentException("account is null");
        } else {
            AuthenticatorInfo info = this.getAuthenticatorInfo(account.type);
            if (info == null) {
                try {
                    response.onError(7, "account.type does not exist");
                } catch (RemoteException var8) {
                    var8.printStackTrace();
                }

            } else {
                (new Session(response, userId, info, expectActivityLaunch, true, account.name, true, true) {
                    public void run() throws RemoteException {
                        this.mAuthenticator.confirmCredentials(this, account, options);
                    }
                }).bind();
            }
        }
    }

    public void addAccount(int userId, IAccountManagerResponse response, final String accountType, final String authTokenType, final String[] requiredFeatures, boolean expectActivityLaunch, final Bundle optionsIn) {
        if (response == null) {
            throw new IllegalArgumentException("response is null");
        } else if (accountType == null) {
            throw new IllegalArgumentException("accountType is null");
        } else {
            AuthenticatorInfo info = this.getAuthenticatorInfo(accountType);
            if (info == null) {
                try {
                    Bundle result = new Bundle();
                    result.putString("authtoken", authTokenType);
                    result.putString("accountType", accountType);
                    result.putBoolean("booleanResult", false);
                    response.onResult(result);
                } catch (RemoteException var10) {
                    var10.printStackTrace();
                }

            } else {
                (new Session(response, userId, info, expectActivityLaunch, true, (String)null, false, true) {
                    public void run() throws RemoteException {
                        this.mAuthenticator.addAccount(this, this.mAuthenticatorInfo.desc.type, authTokenType, requiredFeatures, optionsIn);
                    }

                    protected String toDebugString(long now) {
                        return super.toDebugString(now) + ", addAccount, accountType " + accountType + ", requiredFeatures " + (requiredFeatures != null ? TextUtils.join(",", requiredFeatures) : null);
                    }
                }).bind();
            }
        }
    }

    public boolean addAccountExplicitly(int userId, Account account, String password, Bundle extras) {
        if (account == null) {
            throw new IllegalArgumentException("account is null");
        } else {
            return this.insertAccountIntoDatabase(userId, account, password, extras);
        }
    }

    public boolean removeAccountExplicitly(int userId, Account account) {
        return account != null && this.removeAccountInternal(userId, account);
    }

    public void renameAccount(int userId, IAccountManagerResponse response, Account accountToRename, String newName) {
        if (accountToRename == null) {
            throw new IllegalArgumentException("account is null");
        } else {
            Account resultingAccount = this.renameAccountInternal(userId, accountToRename, newName);
            Bundle result = new Bundle();
            result.putString("authAccount", resultingAccount.name);
            result.putString("accountType", resultingAccount.type);

            try {
                response.onResult(result);
            } catch (RemoteException var8) {
                Log.w(TAG, var8.getMessage());
            }

        }
    }

    public void removeAccount(final int userId, IAccountManagerResponse response, final Account account, boolean expectActivityLaunch) {
        if (response == null) {
            throw new IllegalArgumentException("response is null");
        } else if (account == null) {
            throw new IllegalArgumentException("account is null");
        } else {
            AuthenticatorInfo info = this.getAuthenticatorInfo(account.type);
            if (info == null) {
                try {
                    response.onError(7, "account.type does not exist");
                } catch (RemoteException var7) {
                    var7.printStackTrace();
                }

            } else {
                (new Session(response, userId, info, expectActivityLaunch, true, account.name) {
                    protected String toDebugString(long now) {
                        return super.toDebugString(now) + ", removeAccount, account " + account;
                    }

                    public void run() throws RemoteException {
                        this.mAuthenticator.getAccountRemovalAllowed(this, account);
                    }

                    public void onResult(Bundle result) throws RemoteException {
                        if (result != null && result.containsKey("booleanResult") && !result.containsKey("intent")) {
                            boolean removalAllowed = result.getBoolean("booleanResult");
                            if (removalAllowed) {
                                VAccountManagerService.this.removeAccountInternal(userId, account);
                            }

                            IAccountManagerResponse response = this.getResponseAndClose();
                            if (response != null) {
                                Log.v(VAccountManagerService.TAG, this.getClass().getSimpleName() + " calling onResult() on response " + response);
                                Bundle result2 = new Bundle();
                                result2.putBoolean("booleanResult", removalAllowed);

                                try {
                                    response.onResult(result2);
                                } catch (RemoteException var6) {
                                    var6.printStackTrace();
                                }
                            }
                        }

                        super.onResult(result);
                    }
                }).bind();
            }
        }
    }

    public void clearPassword(int userId, Account account) {
        if (account == null) {
            throw new IllegalArgumentException("account is null");
        } else {
            this.setPasswordInternal(userId, account, (String)null);
        }
    }

    private boolean removeAccountInternal(int userId, Account account) {
        List<VAccount> accounts = (List)this.accountsByUserId.get(userId);
        if (accounts != null) {
            Iterator<VAccount> iterator = accounts.iterator();

            while(iterator.hasNext()) {
                VAccount vAccount = (VAccount)iterator.next();
                if (userId == vAccount.userId && TextUtils.equals(vAccount.name, account.name) && TextUtils.equals(account.type, vAccount.type)) {
                    iterator.remove();
                    this.saveAllAccounts();
                    this.sendAccountsChangedBroadcast(userId);
                    return true;
                }
            }
        }

        return false;
    }

    public boolean accountAuthenticated(int userId, Account account) {
        if (account == null) {
            throw new IllegalArgumentException("account is null");
        } else {
            synchronized(this.accountsByUserId) {
                VAccount vAccount = this.getAccount(userId, account);
                if (vAccount != null) {
                    vAccount.lastAuthenticatedTime = System.currentTimeMillis();
                    this.saveAllAccounts();
                    return true;
                } else {
                    return false;
                }
            }
        }
    }

    public void invalidateAuthToken(int userId, String accountType, String authToken) {
        if (accountType == null) {
            throw new IllegalArgumentException("accountType is null");
        } else if (authToken == null) {
            throw new IllegalArgumentException("authToken is null");
        } else {
            synchronized(this.accountsByUserId) {
                List<VAccount> accounts = (List)this.accountsByUserId.get(userId);
                Iterator iterator;
                if (accounts != null) {
                    boolean changed = false;
                    iterator = accounts.iterator();

                    while(iterator.hasNext()) {
                        VAccount account = (VAccount)iterator.next();
                        if (account.type.equals(accountType)) {
                            account.authTokens.values().remove(authToken);
                            changed = true;
                        }
                    }

                    if (changed) {
                        this.saveAllAccounts();
                    }
                }

                synchronized(this.authTokenRecords) {
                    iterator = this.authTokenRecords.iterator();

                    while(iterator.hasNext()) {
                        AuthTokenRecord record = (AuthTokenRecord)iterator.next();
                        if (record.userId == userId && record.authTokenType.equals(accountType) && record.authToken.equals(authToken)) {
                            iterator.remove();
                        }
                    }
                }

            }
        }
    }

    private Account renameAccountInternal(int userId, Account accountToRename, String newName) {
        synchronized(this.accountsByUserId) {
            VAccount vAccount = this.getAccount(userId, accountToRename);
            if (vAccount != null) {
                vAccount.previousName = vAccount.name;
                vAccount.name = newName;
                this.saveAllAccounts();
                Account newAccount = new Account(vAccount.name, vAccount.type);
                synchronized(this.authTokenRecords) {
                    Iterator var8 = this.authTokenRecords.iterator();

                    while(var8.hasNext()) {
                        AuthTokenRecord record = (AuthTokenRecord)var8.next();
                        if (record.userId == userId && record.account.equals(accountToRename)) {
                            record.account = newAccount;
                        }
                    }
                }

                this.sendAccountsChangedBroadcast(userId);
                return newAccount;
            } else {
                return accountToRename;
            }
        }
    }

    public String peekAuthToken(int userId, Account account, String authTokenType) {
        if (account == null) {
            throw new IllegalArgumentException("account is null");
        } else if (authTokenType == null) {
            throw new IllegalArgumentException("authTokenType is null");
        } else {
            synchronized(this.accountsByUserId) {
                VAccount vAccount = this.getAccount(userId, account);
                return vAccount != null ? (String)vAccount.authTokens.get(authTokenType) : null;
            }
        }
    }

    private String getCustomAuthToken(int userId, Account account, String authTokenType, String packageName) {
        AuthTokenRecord record = new AuthTokenRecord(userId, account, authTokenType, packageName);
        String authToken = null;
        long now = System.currentTimeMillis();
        synchronized(this.authTokenRecords) {
            Iterator<AuthTokenRecord> iterator = this.authTokenRecords.iterator();

            while(true) {
                while(iterator.hasNext()) {
                    AuthTokenRecord one = (AuthTokenRecord)iterator.next();
                    if (one.expiryEpochMillis > 0L && one.expiryEpochMillis < now) {
                        iterator.remove();
                    } else if (record.equals(one)) {
                        authToken = record.authToken;
                    }
                }

                return authToken;
            }
        }
    }

    private void onResult(IAccountManagerResponse response, Bundle result) {
        try {
            response.onResult(result);
        } catch (RemoteException var4) {
            var4.printStackTrace();
        }

    }

    private AuthenticatorInfo getAuthenticatorInfo(String type) {
        synchronized(this.cache) {
            return type == null ? null : (AuthenticatorInfo)this.cache.authenticators.get(type);
        }
    }

    private VAccount getAccount(int userId, Account account) {
        return this.getAccount(userId, account.name, account.type);
    }

    private boolean insertAccountIntoDatabase(int userId, Account account, String password, Bundle extras) {
        if (account == null) {
            return false;
        } else {
            synchronized(this.accountsByUserId) {
                if (this.getAccount(userId, account.name, account.type) != null) {
                    return false;
                } else {
                    VAccount vAccount = new VAccount(userId, account);
                    vAccount.password = password;
                    if (extras != null) {
                        Iterator var7 = extras.keySet().iterator();

                        while(var7.hasNext()) {
                            String key = (String)var7.next();
                            Object value = extras.get(key);
                            if (value instanceof String) {
                                vAccount.userDatas.put(key, (String)value);
                            }
                        }
                    }

                    List<VAccount> accounts = (List)this.accountsByUserId.get(userId);
                    if (accounts == null) {
                        accounts = new ArrayList();
                        this.accountsByUserId.put(userId, accounts);
                    }

                    ((List)accounts).add(vAccount);
                    this.saveAllAccounts();
                    this.sendAccountsChangedBroadcast(vAccount.userId);
                    return true;
                }
            }
        }
    }

    private void sendAccountsChangedBroadcast(int userId) {
        Intent loginChangeIntent = new Intent("android.accounts.LOGIN_ACCOUNTS_CHANGED");
        VActivityManagerService.get().sendBroadcastAsUser(loginChangeIntent, new VUserHandle(userId));
        Intent accountsChangeIntent = new Intent("android.accounts.action.VISIBLE_ACCOUNTS_CHANGED");
        VActivityManagerService.get().sendBroadcastAsUser(accountsChangeIntent, new VUserHandle(userId));
        this.broadcastCheckInNowIfNeed(userId);
    }

    private void broadcastCheckInNowIfNeed(int userId) {
        long time = System.currentTimeMillis();
        if (Math.abs(time - this.lastAccountChangeTime) > 43200000L) {
            this.lastAccountChangeTime = time;
            this.saveAllAccounts();
            Intent intent = new Intent("android.server.checkin.CHECKIN_NOW");
            VActivityManagerService.get().sendBroadcastAsUser(intent, new VUserHandle(userId));
        }

    }

    private void saveAllAccounts() {
        File accountFile = VEnvironment.getAccountConfigFile();
        Parcel dest = Parcel.obtain();

        try {
            dest.writeInt(1);
            List<VAccount> accounts = new ArrayList();

            for(int i = 0; i < this.accountsByUserId.size(); ++i) {
                List<VAccount> list = (List)this.accountsByUserId.valueAt(i);
                if (list != null) {
                    accounts.addAll(list);
                }
            }

            dest.writeInt(accounts.size());
            Iterator var7 = accounts.iterator();

            while(var7.hasNext()) {
                VAccount account = (VAccount)var7.next();
                account.writeToParcel(dest, 0);
            }

            dest.writeLong(this.lastAccountChangeTime);
            FileOutputStream fileOutputStream = new FileOutputStream(accountFile);
            fileOutputStream.write(dest.marshall());
            fileOutputStream.close();
        } catch (Exception var6) {
            var6.printStackTrace();
        }

        dest.recycle();
    }

    private void readAllAccounts() {
        File accountFile = VEnvironment.getAccountConfigFile();
        this.refreshAuthenticatorCache((String)null);
        if (accountFile.exists()) {
            this.accountsByUserId.clear();
            Parcel dest = Parcel.obtain();

            try {
                FileInputStream is = new FileInputStream(accountFile);
                byte[] bytes = new byte[(int)accountFile.length()];
                int readLength = is.read(bytes);
                is.close();
                if (readLength != bytes.length) {
                    throw new IOException(String.format(Locale.ENGLISH, "Expect length %d, but got %d.", bytes.length, readLength));
                }

                dest.unmarshall(bytes, 0, bytes.length);
                dest.setDataPosition(0);
                dest.readInt();

                VAccount account;
                Object accounts;
                for(int size = dest.readInt(); size-- > 0; ((List)accounts).add(account)) {
                    account = new VAccount(dest);
                    VLog.d(TAG, "Reading account : " + account.type, new Object[0]);
                    accounts = (List)this.accountsByUserId.get(account.userId);
                    if (accounts == null) {
                        accounts = new ArrayList();
                        this.accountsByUserId.put(account.userId, (List<VAccount>) accounts);
                    }
                }

                this.lastAccountChangeTime = dest.readLong();
            } catch (Exception var12) {
                var12.printStackTrace();
            } finally {
                dest.recycle();
            }
        }

    }

    private VAccount getAccount(int userId, String accountName, String accountType) {
        List<VAccount> accounts = (List)this.accountsByUserId.get(userId);
        if (accounts != null) {
            Iterator var5 = accounts.iterator();

            while(var5.hasNext()) {
                VAccount account = (VAccount)var5.next();
                if (TextUtils.equals(account.name, accountName) && TextUtils.equals(account.type, accountType)) {
                    return account;
                }
            }
        }

        return null;
    }

    public void refreshAuthenticatorCache(String packageName) {
        this.cache.authenticators.clear();
        Intent intent = new Intent("android.accounts.AccountAuthenticator");
        if (packageName != null) {
            intent.setPackage(packageName);
        }

        this.generateServicesMap(VPackageManagerService.get().queryIntentServices(intent, (String)null, 128, 0), this.cache.authenticators, new RegisteredServicesParser());
    }

    private void generateServicesMap(List<ResolveInfo> services, Map<String, AuthenticatorInfo> map, RegisteredServicesParser accountParser) {
        Iterator var4 = services.iterator();

        while(true) {
            ResolveInfo info;
            XmlResourceParser parser;
            do {
                if (!var4.hasNext()) {
                    return;
                }

                info = (ResolveInfo)var4.next();
                parser = accountParser.getParser(this.mContext, info.serviceInfo, "android.accounts.AccountAuthenticator");
            } while(parser == null);

            try {
                AttributeSet attributeSet = Xml.asAttributeSet(parser);

                int type;
                while((type = parser.next()) != 1 && type != 2) {
                }

                if ("account-authenticator".equals(parser.getName())) {
                    AuthenticatorDescription desc = parseAuthenticatorDescription(accountParser.getResources(this.mContext, info.serviceInfo.applicationInfo), info.serviceInfo.packageName, attributeSet);
                    if (desc != null) {
                        map.put(desc.type, new AuthenticatorInfo(desc, info.serviceInfo));
                    }
                }
            } catch (Exception var10) {
                var10.printStackTrace();
            }
        }
    }

    @TargetApi(26)
    private boolean removeAccountVisibility(int userId, Account account) {
        List<VAccountVisibility> list = (List)this.accountsVisibilitiesByUserId.get(userId);
        if (list != null) {
            Iterator<VAccountVisibility> it = list.iterator();

            while(it.hasNext()) {
                VAccountVisibility vAccountVisibility = (VAccountVisibility)it.next();
                if (userId == vAccountVisibility.userId && TextUtils.equals(vAccountVisibility.name, account.name) && TextUtils.equals(account.type, vAccountVisibility.type)) {
                    it.remove();
                    this.saveAllAccountVisibilities();
                    this.sendAccountsChangedBroadcast(userId);
                    return true;
                }
            }
        }

        return false;
    }

    @TargetApi(26)
    private boolean renameAccountVisibility(int userId, Account account, String name) {
        synchronized(this.accountsVisibilitiesByUserId) {
            VAccountVisibility accountVisibility = this.getAccountVisibility(userId, account);
            if (accountVisibility != null) {
                accountVisibility.name = name;
                this.saveAllAccountVisibilities();
                this.sendAccountsChangedBroadcast(userId);
                return true;
            } else {
                return false;
            }
        }
    }

    @TargetApi(26)
    public Map<String, Integer> getPackagesAndVisibilityForAccount(int userId, Account account) {
        VAccountVisibility accountVisibility = this.getAccountVisibility(userId, account);
        return accountVisibility != null ? accountVisibility.visibility : null;
    }

    @TargetApi(26)
    public boolean addAccountExplicitlyWithVisibility(int userId, Account account, String password, Bundle extras, Map visibility) {
        if (account == null) {
            throw new IllegalArgumentException("account is null");
        } else {
            boolean insertAccountIntoDatabase = this.insertAccountIntoDatabase(userId, account, password, extras);
            this.insertAccountVisibilityIntoDatabase(userId, account, visibility);
            return insertAccountIntoDatabase;
        }
    }

    @TargetApi(26)
    public boolean setAccountVisibility(int userId, Account account, String packageName, int newVisibility) {
        VAccountVisibility accountVisibility = this.getAccountVisibility(userId, account);
        if (accountVisibility == null) {
            return false;
        } else {
            accountVisibility.visibility.put(packageName, newVisibility);
            this.saveAllAccountVisibilities();
            this.sendAccountsChangedBroadcast(userId);
            return true;
        }
    }

    @TargetApi(26)
    public int getAccountVisibility(int userId, Account account, String packageName) {
        VAccountVisibility accountVisibility = this.getAccountVisibility(userId, account);
        return accountVisibility != null && accountVisibility.visibility.containsKey(packageName) ? (Integer)accountVisibility.visibility.get(packageName) : 0;
    }

    @TargetApi(26)
    public Map<Account, Integer> getAccountsAndVisibilityForPackage(int userId, String packageName, String accountType) {
        Map<Account, Integer> hashMap = new HashMap();
        Iterator var5 = this.getAccountList(userId, accountType).iterator();

        while(var5.hasNext()) {
            Account account = (Account)var5.next();
            VAccountVisibility accountVisibility = this.getAccountVisibility(userId, account);
            if (accountVisibility != null && accountVisibility.visibility.containsKey(packageName)) {
                hashMap.put(account, (Integer)accountVisibility.visibility.get(packageName));
            }
        }

        return hashMap;
    }

    @TargetApi(26)
    private boolean insertAccountVisibilityIntoDatabase(int userId, Account account, Map<String, Integer> map) {
        if (account == null) {
            return false;
        } else {
            synchronized(this.accountsVisibilitiesByUserId) {
                VAccountVisibility vAccountVisibility = new VAccountVisibility(userId, account, map);
                List<VAccountVisibility> list = (List)this.accountsVisibilitiesByUserId.get(userId);
                if (list == null) {
                    list = new ArrayList();
                    this.accountsVisibilitiesByUserId.put(userId, list);
                }

                ((List)list).add(vAccountVisibility);
                this.saveAllAccountVisibilities();
                this.sendAccountsChangedBroadcast(vAccountVisibility.userId);
                return true;
            }
        }
    }

    @TargetApi(26)
    private void saveAllAccountVisibilities() {
        File accountVisibilityConfigFile = VEnvironment.getAccountVisibilityConfigFile();
        Parcel obtain = Parcel.obtain();

        try {
            obtain.writeInt(1);
            obtain.writeInt(this.accountsVisibilitiesByUserId.size());

            for(int i = 0; i < this.accountsVisibilitiesByUserId.size(); ++i) {
                obtain.writeInt(i);
                List<VAccountVisibility> list = (List)this.accountsVisibilitiesByUserId.valueAt(i);
                if (list == null) {
                    obtain.writeInt(0);
                } else {
                    obtain.writeInt(list.size());
                    Iterator var5 = list.iterator();

                    while(var5.hasNext()) {
                        VAccountVisibility writeToParcel = (VAccountVisibility)var5.next();
                        writeToParcel.writeToParcel(obtain, 0);
                    }
                }
            }

            obtain.writeLong(this.lastAccountChangeTime);
            FileOutputStream fileOutputStream = new FileOutputStream(accountVisibilityConfigFile);
            fileOutputStream.write(obtain.marshall());
            fileOutputStream.close();
        } catch (Exception var7) {
            var7.printStackTrace();
        }

        obtain.recycle();
    }

    @TargetApi(26)
    private void readAllAccountVisibilities() {
        File accountVisibilityConfigFile = VEnvironment.getAccountVisibilityConfigFile();
        Parcel dest = Parcel.obtain();
        if (accountVisibilityConfigFile.exists()) {
            try {
                FileInputStream is = new FileInputStream(accountVisibilityConfigFile);
                byte[] bytes = new byte[(int)accountVisibilityConfigFile.length()];
                int readLength = is.read(bytes);
                is.close();
                if (readLength != bytes.length) {
                    throw new IOException(String.format("Expect length %d, but got %d.", bytes.length, readLength));
                }

                dest.unmarshall(bytes, 0, bytes.length);
                dest.setDataPosition(0);
                int version = dest.readInt();
                int userCount = dest.readInt();

                for(int i = 0; i < userCount; ++i) {
                    int userId = dest.readInt();
                    int count = dest.readInt();
                    List<VAccountVisibility> list = new ArrayList();
                    this.accountsVisibilitiesByUserId.put(userId, list);

                    for(int j = 0; j < count; ++j) {
                        list.add(new VAccountVisibility(dest));
                    }
                }

                this.lastAccountChangeTime = dest.readLong();
            } catch (Throwable var13) {
                var13.printStackTrace();
            }
        }

        dest.recycle();
    }

    @TargetApi(26)
    private VAccountVisibility getAccountVisibility(int i, String name, String type) {
        List<VAccountVisibility> list = (List)this.accountsVisibilitiesByUserId.get(i);
        if (list != null) {
            Iterator var5 = list.iterator();

            while(var5.hasNext()) {
                VAccountVisibility vAccountVisibility = (VAccountVisibility)var5.next();
                if (TextUtils.equals(vAccountVisibility.name, name) && TextUtils.equals(vAccountVisibility.type, type)) {
                    return vAccountVisibility;
                }
            }
        }

        return null;
    }

    @TargetApi(26)
    private VAccountVisibility getAccountVisibility(int userId, Account account) {
        return this.getAccountVisibility(userId, account.name, account.type);
    }

    public void startAddAccountSession(IAccountManagerResponse response, String accountType, String authTokenType, String[] requiredFeatures, boolean expectActivityLaunch, Bundle options) throws RemoteException {
        throw new RuntimeException("Stub!");
    }

    public void startUpdateCredentialsSession(IAccountManagerResponse response, Account account, String authTokenType, boolean expectActivityLaunch, Bundle options) throws RemoteException {
        throw new RuntimeException("Stub!");
    }

    public void registerAccountListener(String[] accountTypes) throws RemoteException {
    }

    public void unregisterAccountListener(String[] accountTypes) throws RemoteException {
    }

    public void finishSessionAsUser(IAccountManagerResponse response, Bundle sessionBundle, boolean expectActivityLaunch, Bundle appInfo, int userId) throws RemoteException {
        throw new RuntimeException("Stub!");
    }

    public void isCredentialsUpdateSuggested(IAccountManagerResponse response, Account account, String statusToken) throws RemoteException {
        throw new RuntimeException("Stub!");
    }

    public AccountAndUser[] getAllAccounts() {
        List<AccountAndUser> list = new ArrayList();

        for(int i = 0; i < this.accountsByUserId.size(); ++i) {
            List<VAccount> accounts = (List)this.accountsByUserId.valueAt(i);
            Iterator var4 = accounts.iterator();

            while(var4.hasNext()) {
                VAccount account = (VAccount)var4.next();
                list.add(new AccountAndUser(new Account(account.name, account.type), account.userId));
            }
        }

        return (AccountAndUser[])list.toArray(new AccountAndUser[0]);
    }

    private class GetAccountsByTypeAndFeatureSession extends Session {
        private final String[] mFeatures;
        private volatile Account[] mAccountsOfType = null;
        private volatile ArrayList<Account> mAccountsWithFeatures = null;
        private volatile int mCurrentAccount = 0;

        public GetAccountsByTypeAndFeatureSession(IAccountManagerResponse response, int userId, AuthenticatorInfo info, String[] features) {
            super(response, userId, info, false, true, (String)null);
            this.mFeatures = features;
        }

        public void run() throws RemoteException {
            this.mAccountsOfType = VAccountManagerService.this.getAccounts(this.mUserId, this.mAuthenticatorInfo.desc.type);
            this.mAccountsWithFeatures = new ArrayList(this.mAccountsOfType.length);
            this.mCurrentAccount = 0;
            this.checkAccount();
        }

        public void checkAccount() {
            if (this.mCurrentAccount >= this.mAccountsOfType.length) {
                this.sendResult();
            } else {
                IAccountAuthenticator accountAuthenticator = this.mAuthenticator;
                if (accountAuthenticator == null) {
                    Log.v(VAccountManagerService.TAG, "checkAccount: aborting session since we are no longer connected to the authenticator, " + this.toDebugString());
                } else {
                    try {
                        accountAuthenticator.hasFeatures(this, this.mAccountsOfType[this.mCurrentAccount], this.mFeatures);
                    } catch (RemoteException var3) {
                        this.onError(1, "remote exception");
                    }

                }
            }
        }

        public void onResult(Bundle result) {
            ++this.mNumResults;
            if (result == null) {
                this.onError(5, "null bundle");
            } else {
                if (result.getBoolean("booleanResult", false)) {
                    this.mAccountsWithFeatures.add(this.mAccountsOfType[this.mCurrentAccount]);
                }

                ++this.mCurrentAccount;
                this.checkAccount();
            }
        }

        public void sendResult() {
            IAccountManagerResponse response = this.getResponseAndClose();
            if (response != null) {
                try {
                    Account[] accounts = new Account[this.mAccountsWithFeatures.size()];

                    for(int i = 0; i < accounts.length; ++i) {
                        accounts[i] = (Account)this.mAccountsWithFeatures.get(i);
                    }

                    if (Log.isLoggable(VAccountManagerService.TAG, Log.VERBOSE)) {
                        Log.v(VAccountManagerService.TAG, this.getClass().getSimpleName() + " calling onResult() on response " + response);
                    }

                    Bundle result = new Bundle();
                    result.putParcelableArray("accounts", accounts);
                    response.onResult(result);
                } catch (RemoteException var4) {
                    Log.v(VAccountManagerService.TAG, "failure while notifying response", var4);
                }
            }

        }

        protected String toDebugString(long now) {
            return super.toDebugString(now) + ", getAccountsByTypeAndFeatures, " + (this.mFeatures != null ? TextUtils.join(",", this.mFeatures) : null);
        }
    }

    private abstract class Session extends IAccountAuthenticatorResponse.Stub implements IBinder.DeathRecipient, ServiceConnection {
        final int mUserId;
        final AuthenticatorInfo mAuthenticatorInfo;
        private final boolean mStripAuthTokenFromResult;
        public int mNumResults;
        IAccountAuthenticator mAuthenticator;
        private IAccountManagerResponse mResponse;
        private boolean mExpectActivityLaunch;
        private long mCreationTime;
        private String mAccountName;
        private boolean mAuthDetailsRequired;
        private boolean mUpdateLastAuthenticatedTime;
        private int mNumRequestContinued;
        private int mNumErrors;

        Session(IAccountManagerResponse response, int userId, AuthenticatorInfo info, boolean expectActivityLaunch, boolean stripAuthTokenFromResult, String accountName, boolean authDetailsRequired, boolean updateLastAuthenticatedTime) {
            if (info == null) {
                throw new IllegalArgumentException("accountType is null");
            } else {
                this.mStripAuthTokenFromResult = stripAuthTokenFromResult;
                this.mResponse = response;
                this.mUserId = userId;
                this.mAuthenticatorInfo = info;
                this.mExpectActivityLaunch = expectActivityLaunch;
                this.mCreationTime = SystemClock.elapsedRealtime();
                this.mAccountName = accountName;
                this.mAuthDetailsRequired = authDetailsRequired;
                this.mUpdateLastAuthenticatedTime = updateLastAuthenticatedTime;
                synchronized(VAccountManagerService.this.mSessions) {
                    VAccountManagerService.this.mSessions.put(this.toString(), this);
                }

                if (response != null) {
                    try {
                        response.asBinder().linkToDeath(this, 0);
                    } catch (RemoteException var12) {
                        this.mResponse = null;
                        this.binderDied();
                    }
                }

            }
        }

        Session(IAccountManagerResponse response, int userId, AuthenticatorInfo info, boolean expectActivityLaunch, boolean stripAuthTokenFromResult, String accountName) {
            this(response, userId, info, expectActivityLaunch, stripAuthTokenFromResult, accountName, false, false);
        }

        IAccountManagerResponse getResponseAndClose() {
            if (this.mResponse == null) {
                return null;
            } else {
                IAccountManagerResponse response = this.mResponse;
                this.close();
                return response;
            }
        }

        private void close() {
            synchronized(VAccountManagerService.this.mSessions) {
                if (VAccountManagerService.this.mSessions.remove(this.toString()) == null) {
                    return;
                }
            }

            if (this.mResponse != null) {
                this.mResponse.asBinder().unlinkToDeath(this, 0);
                this.mResponse = null;
            }

            this.unbind();
        }

        public void onServiceConnected(ComponentName name, IBinder service) {
            this.mAuthenticator = (IAccountAuthenticator) Stub.asInterface(service);

            try {
                this.run();
            } catch (RemoteException var4) {
                this.onError(1, "remote exception");
            }

        }

        public void onRequestContinued() {
            ++this.mNumRequestContinued;
        }

        public void onError(int errorCode, String errorMessage) {
            ++this.mNumErrors;
            IAccountManagerResponse response = this.getResponseAndClose();
            if (response != null) {
                Log.v(VAccountManagerService.TAG, this.getClass().getSimpleName() + " calling onError() on response " + response);

                try {
                    response.onError(errorCode, errorMessage);
                } catch (RemoteException var5) {
                    Log.v(VAccountManagerService.TAG, "Session.onError: caught RemoteException while responding", var5);
                }
            } else {
                Log.v(VAccountManagerService.TAG, "Session.onError: already closed");
            }

        }

        public void onServiceDisconnected(ComponentName name) {
            this.mAuthenticator = null;
            IAccountManagerResponse response = this.getResponseAndClose();
            if (response != null) {
                try {
                    response.onError(1, "disconnected");
                } catch (RemoteException var4) {
                    Log.v(VAccountManagerService.TAG, "Session.onServiceDisconnected: caught RemoteException while responding", var4);
                }
            }

        }

        public void onResult(Bundle result) throws RemoteException {
            ++this.mNumResults;
            if (result != null) {
                boolean isSuccessfulConfirmCreds = result.getBoolean("booleanResult", false);
                boolean isSuccessfulUpdateCredsOrAddAccount = result.containsKey("authAccount") && result.containsKey("accountType");
                boolean needUpdate = this.mUpdateLastAuthenticatedTime && (isSuccessfulConfirmCreds || isSuccessfulUpdateCredsOrAddAccount);
                if (needUpdate || this.mAuthDetailsRequired) {
                    synchronized(VAccountManagerService.this.accountsByUserId) {
                        VAccount account = VAccountManagerService.this.getAccount(this.mUserId, this.mAccountName, this.mAuthenticatorInfo.desc.type);
                        if (needUpdate && account != null) {
                            account.lastAuthenticatedTime = System.currentTimeMillis();
                            VAccountManagerService.this.saveAllAccounts();
                        }

                        if (this.mAuthDetailsRequired) {
                            long lastAuthenticatedTime = -1L;
                            if (account != null) {
                                lastAuthenticatedTime = account.lastAuthenticatedTime;
                            }

                            result.putLong("lastAuthenticatedTime", lastAuthenticatedTime);
                        }
                    }
                }
            }

            if (result != null && !TextUtils.isEmpty(result.getString("authtoken"))) {
            }

            Intent intent = null;
            if (result != null) {
                intent = (Intent)result.getParcelable("intent");
            }

            IAccountManagerResponse response;
            if (this.mExpectActivityLaunch && result != null && result.containsKey("intent")) {
                response = this.mResponse;
            } else {
                response = this.getResponseAndClose();
            }

            if (response != null) {
                try {
                    if (result == null) {
                        Log.v(VAccountManagerService.TAG, this.getClass().getSimpleName() + " calling onError() on response " + response);
                        response.onError(5, "null bundle returned");
                    } else {
                        if (this.mStripAuthTokenFromResult) {
                            result.remove("authtoken");
                        }

                        Log.v(VAccountManagerService.TAG, this.getClass().getSimpleName() + " calling onResult() on response " + response);
                        if (result.getInt("errorCode", -1) > 0 && intent == null) {
                            response.onError(result.getInt("errorCode"), result.getString("errorMessage"));
                        } else {
                            response.onResult(result);
                        }
                    }
                } catch (RemoteException var11) {
                    Log.v(VAccountManagerService.TAG, "failure while notifying response", var11);
                }
            }

        }

        public abstract void run() throws RemoteException;

        void bind() {
            Log.v(VAccountManagerService.TAG, "initiating bind to authenticator type " + this.mAuthenticatorInfo.desc.type);
            Intent intent = new Intent();
            intent.setAction("android.accounts.AccountAuthenticator");
            intent.setClassName(this.mAuthenticatorInfo.serviceInfo.packageName, this.mAuthenticatorInfo.serviceInfo.name);
            if (!VActivityManager.get().bindService(VAccountManagerService.this.mContext, intent, this, 1, this.mUserId)) {
                Log.d(VAccountManagerService.TAG, "bind attempt failed for " + this.toDebugString());
                this.onError(1, "bind failure");
            }

        }

        protected String toDebugString() {
            return this.toDebugString(SystemClock.elapsedRealtime());
        }

        protected String toDebugString(long now) {
            return "Session: expectLaunch " + this.mExpectActivityLaunch + ", connected " + (this.mAuthenticator != null) + ", stats (" + this.mNumResults + "/" + this.mNumRequestContinued + "/" + this.mNumErrors + "), lifetime " + (double)(now - this.mCreationTime) / 1000.0;
        }

        private void unbind() {
            if (this.mAuthenticator != null) {
                this.mAuthenticator = null;
                VActivityManager.get().unbindService(VAccountManagerService.this.mContext, this);
            }

        }

        public void binderDied() {
            this.mResponse = null;
            this.close();
        }
    }

    private final class AuthenticatorCache {
        final Map<String, AuthenticatorInfo> authenticators;

        private AuthenticatorCache() {
            this.authenticators = new HashMap();
        }
    }

    private final class AuthenticatorInfo {
        final AuthenticatorDescription desc;
        final ServiceInfo serviceInfo;

        AuthenticatorInfo(AuthenticatorDescription desc, ServiceInfo info) {
            this.desc = desc;
            this.serviceInfo = info;
        }
    }

    static final class AuthTokenRecord {
        public int userId;
        public Account account;
        public long expiryEpochMillis;
        public String authToken;
        private String authTokenType;
        private String packageName;

        AuthTokenRecord(int userId, Account account, String authTokenType, String packageName, String authToken, long expiryEpochMillis) {
            this.userId = userId;
            this.account = account;
            this.authTokenType = authTokenType;
            this.packageName = packageName;
            this.authToken = authToken;
            this.expiryEpochMillis = expiryEpochMillis;
        }

        AuthTokenRecord(int userId, Account account, String authTokenType, String packageName) {
            this.userId = userId;
            this.account = account;
            this.authTokenType = authTokenType;
            this.packageName = packageName;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            } else if (o != null && this.getClass() == o.getClass()) {
                AuthTokenRecord that = (AuthTokenRecord)o;
                return this.userId == that.userId && this.account.equals(that.account) && this.authTokenType.equals(that.authTokenType) && this.packageName.equals(that.packageName);
            } else {
                return false;
            }
        }

        public int hashCode() {
            return ((this.userId * 31 + this.account.hashCode()) * 31 + this.authTokenType.hashCode()) * 31 + this.packageName.hashCode();
        }
    }
}
