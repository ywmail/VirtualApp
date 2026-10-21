/*
 * Decompiled with CFR 0.152.
 */
package com.lody.virtual.client.hook.proxies.backup;

import com.lody.virtual.StringFog;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.ResultStaticMethodProxy;
import com.lody.virtual.helper.compat.BuildCompat;
import mirror.android.app.backup.IBackupManager;

public class BackupManagerStub
extends BinderInvocationProxy {
    public BackupManagerStub() {
        super(IBackupManager.Stub.asInterface, "backup");
    }

    @Override
    protected void onBindMethods() {
        super.onBindMethods();
        this.addMethodProxy(new ResultStaticMethodProxy("dataChanged", null));
        this.addMethodProxy(new ResultStaticMethodProxy("clearBackupData", null));
        this.addMethodProxy(new ResultStaticMethodProxy("agentConnected", null));
        this.addMethodProxy(new ResultStaticMethodProxy("agentDisconnected", null));
        this.addMethodProxy(new ResultStaticMethodProxy("restoreAtInstall", null));
        this.addMethodProxy(new ResultStaticMethodProxy("setBackupEnabled", null));
        this.addMethodProxy(new ResultStaticMethodProxy("setBackupProvisioned", null));
        this.addMethodProxy(new ResultStaticMethodProxy("backupNow", null));
        this.addMethodProxy(new ResultStaticMethodProxy("fullBackup", null));
        this.addMethodProxy(new ResultStaticMethodProxy("fullTransportBackup", null));
        this.addMethodProxy(new ResultStaticMethodProxy("fullRestore", null));
        this.addMethodProxy(new ResultStaticMethodProxy("acknowledgeFullBackupOrRestore", null));
        this.addMethodProxy(new ResultStaticMethodProxy("getCurrentTransport", null));
        this.addMethodProxy(new ResultStaticMethodProxy("listAllTransports", new String[0]));
        this.addMethodProxy(new ResultStaticMethodProxy("selectBackupTransport", null));
        this.addMethodProxy(new ResultStaticMethodProxy("isBackupEnabled", false));
        this.addMethodProxy(new ResultStaticMethodProxy("setBackupPassword", true));
        this.addMethodProxy(new ResultStaticMethodProxy("hasBackupPassword", false));
        this.addMethodProxy(new ResultStaticMethodProxy("beginRestoreSession", null));
        if (BuildCompat.isOreo()) {
            this.addMethodProxy(new ResultStaticMethodProxy("selectBackupTransportAsync", null));
        }
        if (BuildCompat.isPie()) {
            this.addMethodProxy(new ResultStaticMethodProxy("updateTransportAttributes", null));
            this.addMethodProxy(new ResultStaticMethodProxy("isBackupServiceActive", false));
        }
    }
}

