/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.pm.ApplicationInfo
 *  android.os.Binder
 *  android.os.IInterface
 *  android.os.Process
 */
package com.lody.virtual.server.am;

import android.content.pm.ApplicationInfo;
import android.os.Binder;
import android.os.IInterface;
import android.os.Process;
import com.lody.virtual.client.IVClient;
import com.lody.virtual.client.stub.StubManifest;
import com.lody.virtual.helper.compat.ObjectsCompat;
import com.lody.virtual.os.VUserHandle;
import com.lody.virtual.remote.ClientConfig;
import com.lody.virtual.server.extension.VExtPackageAccessor;
import com.lody.virtual.server.pm.PrivilegeAppOptimizer;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

final class ProcessRecord
extends Binder {
    public final ApplicationInfo info;
    public final String processName;
    final Set<String> pkgList = Collections.synchronizedSet(new HashSet());
    public IVClient client;
    public IInterface appThread;
    public int pid;
    public int vuid;
    public int vpid;
    public boolean isExt;
    public int userId;
    public boolean privilege;

    public ProcessRecord(ApplicationInfo info, String processName, int vuid, int vpid, boolean isExt) {
        this.info = info;
        this.vuid = vuid;
        this.vpid = vpid;
        this.userId = VUserHandle.getUserId(vuid);
        this.processName = processName;
        this.isExt = isExt;
        this.privilege = PrivilegeAppOptimizer.get().isPrivilegeProcess(processName);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || ((Object)((Object)this)).getClass() != o.getClass()) {
            return false;
        }
        ProcessRecord that = (ProcessRecord)((Object)o);
        return this.vuid == that.vuid && this.vpid == that.vpid && this.isExt == that.isExt && this.userId == that.userId && ObjectsCompat.equals(this.processName, that.processName);
    }

    public int hashCode() {
        return ObjectsCompat.hash(this.processName, this.vuid, this.vpid, this.isExt, this.userId);
    }

    public String getProviderAuthority() {
        return StubManifest.getStubAuthority(this.vpid, this.isExt);
    }

    public ClientConfig getClientConfig() {
        ClientConfig config = new ClientConfig();
        config.isExt = this.isExt;
        config.vuid = this.vuid;
        config.vpid = this.vpid;
        config.packageName = this.info.packageName;
        config.processName = this.processName;
        config.token = this;
        return config;
    }

    public boolean isPrivilegeProcess() {
        return this.privilege;
    }

    public void kill() {
        if (this.isExt) {
            VExtPackageAccessor.forceStop(new int[]{this.pid});
        } else {
            try {
                Process.killProcess((int)this.pid);
            }
            catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }
}

