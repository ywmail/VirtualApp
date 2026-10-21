/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.annotation.TargetApi
 */
package com.lody.virtual.client.hook.proxies.user;

import android.annotation.TargetApi;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.ReplaceCallingPkgMethodProxy;
import com.lody.virtual.client.hook.base.ReplaceLastUserIdMethodProxy;
import com.lody.virtual.client.hook.base.ResultStaticMethodProxy;
import java.util.Collections;
import mirror.android.content.pm.UserInfo;
import mirror.android.os.IUserManager;

@TargetApi(value=17)
public class UserManagerStub
extends BinderInvocationProxy {
    public UserManagerStub() {
        super(IUserManager.Stub.asInterface, "user");
    }

    @Override
    protected void onBindMethods() {
        super.onBindMethods();
        this.addMethodProxy(new ReplaceCallingPkgMethodProxy("setApplicationRestrictions"));
        this.addMethodProxy(new ReplaceCallingPkgMethodProxy("getApplicationRestrictions"));
        this.addMethodProxy(new ReplaceCallingPkgMethodProxy("getApplicationRestrictionsForUser"));
        this.addMethodProxy(new ReplaceLastUserIdMethodProxy("isUserUnlocked"));
        this.addMethodProxy(new ReplaceLastUserIdMethodProxy("isProfile"));
        this.addMethodProxy(new ReplaceLastUserIdMethodProxy("isUserUnlockingOrUnlocked"));
        this.addMethodProxy(new ReplaceLastUserIdMethodProxy("isManagedProfile"));
        this.addMethodProxy(new ResultStaticMethodProxy("getProfileParent", null));
        this.addMethodProxy(new ResultStaticMethodProxy("getUserIcon", null));
        this.addMethodProxy(new ResultStaticMethodProxy("getUserInfo", UserInfo.ctor.newInstance(0, "Admin", UserInfo.FLAG_PRIMARY.get())));
        this.addMethodProxy(new ResultStaticMethodProxy("getDefaultGuestRestrictions", null));
        this.addMethodProxy(new ResultStaticMethodProxy("setDefaultGuestRestrictions", null));
        this.addMethodProxy(new ResultStaticMethodProxy("removeRestrictions", null));
        this.addMethodProxy(new ResultStaticMethodProxy("getUsers", Collections.singletonList(UserInfo.ctor.newInstance(0, "Admin", UserInfo.FLAG_PRIMARY.get()))));
        this.addMethodProxy(new ResultStaticMethodProxy("createUser", null));
        this.addMethodProxy(new ResultStaticMethodProxy("createProfileForUser", null));
        this.addMethodProxy(new ResultStaticMethodProxy("getProfiles", Collections.EMPTY_LIST));
        this.addMethodProxy(new ResultStaticMethodProxy("setUserEnabled", null));
        this.addMethodProxy(new ResultStaticMethodProxy("removeUser", false));
        this.addMethodProxy(new ResultStaticMethodProxy("setUserName", null));
        this.addMethodProxy(new ResultStaticMethodProxy("setUserIcon", null));
        this.addMethodProxy(new ResultStaticMethodProxy("canAddMoreManagedProfiles", false));
        this.addMethodProxy(new ResultStaticMethodProxy("setUserRestrictions", null));
        this.addMethodProxy(new ResultStaticMethodProxy("setUserRestriction", null));
        this.addMethodProxy(new ResultStaticMethodProxy("markGuestForDeletion", true));
        this.addMethodProxy(new ResultStaticMethodProxy("createRestrictedProfile", null));
        this.addMethodProxy(new ResultStaticMethodProxy("getPrimaryUser", null));
        this.addMethodProxy(new ResultStaticMethodProxy("hasBaseUserRestriction", false));
        this.addMethodProxy(new ResultStaticMethodProxy("getUserName", ""));
        this.addMethodProxy(new ResultStaticMethodProxy("getSeedAccountOptions", null));
    }
}

