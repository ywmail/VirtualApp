/*
 * Decompiled with CFR 0.152.
 */
package com.lody.virtual.client.hook.proxies.system;

import com.android.internal.widget.ILockSettings;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.ResultStaticMethodProxy;
import java.util.Collections;
import mirror.android.os.ServiceManager;

public class LockSettingsStub
extends BinderInvocationProxy {
    private static final String SERVICE_NAME = "lock_settings";

    public LockSettingsStub() {
        super(new EmptyLockSettings(), "lock_settings");
    }

    @Override
    public void inject() throws Throwable {
        if (ServiceManager.checkService.call(SERVICE_NAME) == null) {
            super.inject();
        }
    }

    @Override
    protected void onBindMethods() {
        super.onBindMethods();
        this.addMethodProxy(new ResultStaticMethodProxy("getRecoveryStatus", Collections.emptyMap()));
    }

    static class EmptyLockSettings
    extends ILockSettings.Stub {
        EmptyLockSettings() {
        }

        @Override
        public void setRecoverySecretTypes(int[] secretTypes) {
        }

        @Override
        public int[] getRecoverySecretTypes() {
            return new int[0];
        }
    }
}

