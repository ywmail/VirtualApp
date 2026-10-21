/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Bundle
 *  android.os.PersistableBundle
 */
package com.lody.virtual.client.hook.proxies.system;

import android.os.Bundle;
import android.os.ISystemUpdateManager;
import android.os.PersistableBundle;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import mirror.android.os.ServiceManager;

public class SystemUpdateStub
extends BinderInvocationProxy {
    private static final String SERVICE_NAME = "system_update";

    public SystemUpdateStub() {
        super(new EmptySystemUpdateManagerImpl(), "system_update");
    }

    @Override
    public void inject() throws Throwable {
        if (ServiceManager.checkService.call(SERVICE_NAME) == null) {
            super.inject();
        }
    }

    static class EmptySystemUpdateManagerImpl
    extends ISystemUpdateManager.Stub {
        EmptySystemUpdateManagerImpl() {
        }

        @Override
        public Bundle retrieveSystemUpdateInfo() {
            Bundle info = new Bundle();
            info.putInt("status", 0);
            return info;
        }

        @Override
        public void updateSystemUpdateInfo(PersistableBundle data) {
        }
    }
}

