/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Parcel
 */
package com.carlos.common.persistent;

import android.os.Parcel;
import com.carlos.common.network.StringFog;
import com.carlos.common.persistent.PersistenceLayer;
import com.carlos.common.persistent.StoragePersistenceServices;
import com.carlos.common.persistent.VPersistent;
import java.io.File;

public class StoragePersistenceLayer
extends PersistenceLayer {
    private StoragePersistenceServices mService;

    StoragePersistenceLayer(StoragePersistenceServices service) {
        super(new File("/data/data/com.carlos.multiapp", "app-config.ini"));
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
        VPersistent infos = this.mService.mVPersistent;
        infos.writeToParcel(p, 0);
    }

    @Override
    public void readPersistenceData(Parcel p, int version) {
        this.mService.mVPersistent.readToParcel(p);
    }

    @Override
    public void onPersistenceFileDamage() {
        this.getPersistenceFile().delete();
    }
}

