/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Parcel
 */
package com.lody.virtual.server.device;

import android.os.Parcel;
import com.lody.virtual.helper.PersistenceLayer;
import com.lody.virtual.helper.collection.SparseArray;
import com.lody.virtual.os.VEnvironment;
import com.lody.virtual.remote.VDeviceConfig;
import com.lody.virtual.server.device.VDeviceManagerService;

public class DeviceInfoPersistenceLayer
extends PersistenceLayer {
    private VDeviceManagerService mService;

    DeviceInfoPersistenceLayer(VDeviceManagerService service) {
        super(VEnvironment.getDeviceInfoFile());
        this.mService = service;
    }

    @Override
    public int getCurrentVersion() {
        return 3;
    }

    @Override
    public void writeMagic(Parcel p) {
    }

    @Override
    public boolean verifyMagic(Parcel p) {
        return true;
    }

    @Override
    public void writePersistenceData(Parcel p) {
        SparseArray<VDeviceConfig> infos = this.mService.mDeviceConfigs;
        int size = infos.size();
        p.writeInt(size);
        for (int i = 0; i < size; ++i) {
            int userId = infos.keyAt(i);
            VDeviceConfig info = infos.valueAt(i);
            p.writeInt(userId);
            info.writeToParcel(p, 0);
        }
    }

    @Override
    public void readPersistenceData(Parcel p, int version) {
        SparseArray<VDeviceConfig> infos = this.mService.mDeviceConfigs;
        infos.clear();
        int size = p.readInt();
        while (size-- > 0) {
            int userId = p.readInt();
            VDeviceConfig info = new VDeviceConfig(p);
            infos.put(userId, info);
        }
    }

    @Override
    public void onPersistenceFileDamage() {
        this.getPersistenceFile().delete();
    }
}

