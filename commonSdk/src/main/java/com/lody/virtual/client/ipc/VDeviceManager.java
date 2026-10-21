/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.RemoteException
 */
package com.lody.virtual.client.ipc;

import android.os.RemoteException;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.env.VirtualRuntime;
import com.lody.virtual.client.ipc.LocalProxyUtils;
import com.lody.virtual.client.ipc.ServiceManagerNative;
import com.lody.virtual.helper.utils.IInterfaceUtils;
import com.lody.virtual.helper.utils.Reflect;
import com.lody.virtual.helper.utils.ReflectException;
import com.lody.virtual.remote.VDeviceConfig;
import com.lody.virtual.server.interfaces.IDeviceManager;
import java.util.Map;
import mirror.android.os.Build;

public class VDeviceManager {
    private static final VDeviceManager sInstance = new VDeviceManager();
    private IDeviceManager mService;

    public static VDeviceManager get() {
        return sInstance;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public IDeviceManager getService() {
        if (!IInterfaceUtils.isAlive(this.mService)) {
            VDeviceManager vDeviceManager = this;
            synchronized (vDeviceManager) {
                Object binder = this.getRemoteInterface();
                this.mService = LocalProxyUtils.genProxy(IDeviceManager.class, binder);
            }
        }
        return this.mService;
    }

    private Object getRemoteInterface() {
        return IDeviceManager.Stub.asInterface(ServiceManagerNative.getService("device"));
    }

    public VDeviceConfig getDeviceConfig(int userId) {
        try {
            return this.getService().getDeviceConfig(userId);
        }
        catch (RemoteException e) {
            return (VDeviceConfig)VirtualRuntime.crash(e);
        }
    }

    public void updateDeviceConfig(int userId, VDeviceConfig config) {
        try {
            this.getService().updateDeviceConfig(userId, config);
        }
        catch (RemoteException e) {
            VirtualRuntime.crash(e);
        }
    }

    public boolean isEnable(int userId) {
        try {
            return this.getService().isEnable(userId);
        }
        catch (RemoteException e) {
            return (Boolean)VirtualRuntime.crash(e);
        }
    }

    public void setEnable(int userId, boolean enable) {
        try {
            this.getService().setEnable(userId, enable);
        }
        catch (RemoteException e) {
            VirtualRuntime.crash(e);
        }
    }

    public void applyBuildProp(VDeviceConfig config) {
        for (Map.Entry<String, String> entry : config.buildProp.entrySet()) {
            try {
                Reflect.on(Build.TYPE).set(entry.getKey(), entry.getValue());
            }
            catch (ReflectException e) {
                e.printStackTrace();
            }
        }
        if (config.serial != null) {
            Reflect.on(Build.TYPE).set("SERIAL", config.serial);
        }
    }
}

