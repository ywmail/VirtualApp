/*
 * Decompiled with CFR 0.152.
 */
package com.lody.virtual.server.device;

import com.lody.virtual.helper.collection.SparseArray;
import com.lody.virtual.remote.VDeviceConfig;
import com.lody.virtual.server.device.DeviceInfoPersistenceLayer;
import com.lody.virtual.server.interfaces.IDeviceManager;

public class VDeviceManagerService
extends IDeviceManager.Stub {
    private static final VDeviceManagerService sInstance = new VDeviceManagerService();
    final SparseArray<VDeviceConfig> mDeviceConfigs = new SparseArray();
    private DeviceInfoPersistenceLayer mPersistenceLayer = new DeviceInfoPersistenceLayer(this);

    public static VDeviceManagerService get() {
        return sInstance;
    }

    private VDeviceManagerService() {
        this.mPersistenceLayer.read();
        for (int i = 0; i < this.mDeviceConfigs.size(); ++i) {
            VDeviceConfig info = this.mDeviceConfigs.valueAt(i);
            VDeviceConfig.addToPool(info);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public VDeviceConfig getDeviceConfig(int userId) {
        VDeviceConfig info;
        SparseArray<VDeviceConfig> sparseArray = this.mDeviceConfigs;
        synchronized (sparseArray) {
            info = this.mDeviceConfigs.get(userId);
            if (info == null) {
                info = VDeviceConfig.random();
                this.mDeviceConfigs.put(userId, info);
                this.mPersistenceLayer.save();
            }
        }
        return info;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void updateDeviceConfig(int userId, VDeviceConfig config) {
        SparseArray<VDeviceConfig> sparseArray = this.mDeviceConfigs;
        synchronized (sparseArray) {
            if (config != null) {
                this.mDeviceConfigs.put(userId, config);
                this.mPersistenceLayer.save();
            }
        }
    }

    @Override
    public boolean isEnable(int userId) {
        return this.getDeviceConfig((int)userId).enable;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void setEnable(int userId, boolean enable) {
        SparseArray<VDeviceConfig> sparseArray = this.mDeviceConfigs;
        synchronized (sparseArray) {
            VDeviceConfig info = this.mDeviceConfigs.get(userId);
            if (info == null) {
                info = VDeviceConfig.random();
                this.mDeviceConfigs.put(userId, info);
            }
            info.enable = enable;
            this.mPersistenceLayer.save();
        }
    }
}

