/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 */
package com.carlos.common.persistent;

import android.content.Context;
import com.carlos.common.network.StringFog;
import com.carlos.common.persistent.StoragePersistenceLayer;
import com.carlos.common.persistent.VPersistent;

public class StoragePersistenceServices {
    public static String SERVICE_NAME = "storage_persistence";
    static StoragePersistenceServices mStoragePersistenceServices = new StoragePersistenceServices();
    Context mContext;
    private StoragePersistenceLayer mPersistenceLayer = new StoragePersistenceLayer(this);
    public VPersistent mVPersistent = new VPersistent();

    public static StoragePersistenceServices get() {
        return mStoragePersistenceServices;
    }

    public StoragePersistenceServices() {
        this.mPersistenceLayer.read();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void updatePersistent(VPersistent config) {
        VPersistent vPersistent = this.mVPersistent;
        synchronized (vPersistent) {
            if (config != null) {
                this.mVPersistent = config;
                this.mPersistenceLayer.save();
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public VPersistent getVPersistent() {
        StoragePersistenceLayer storagePersistenceLayer = this.mPersistenceLayer;
        synchronized (storagePersistenceLayer) {
            this.mPersistenceLayer.read();
        }
        return this.mVPersistent;
    }
}

