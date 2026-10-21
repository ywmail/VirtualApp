/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.util.SparseArray
 */
package com.lody.virtual.server.vs;

import android.util.SparseArray;
import com.lody.virtual.StringFog;
import com.lody.virtual.server.interfaces.IVirtualStorageService;
import com.lody.virtual.server.pm.VUserManagerService;
import com.lody.virtual.server.vs.VSConfig;
import com.lody.virtual.server.vs.VSPersistenceLayer;
import java.io.File;
import java.util.HashMap;

public class VirtualStorageService
extends IVirtualStorageService.Stub {
    private static final VirtualStorageService sService = new VirtualStorageService();
    private static final String[] sPublicDirs = new String[]{"DCIM", "Music", "Pictures"};
    private final VSPersistenceLayer mLayer = new VSPersistenceLayer(this);
    private final SparseArray<HashMap<String, VSConfig>> mConfigs = new SparseArray();

    public static VirtualStorageService get() {
        return sService;
    }

    private VirtualStorageService() {
        this.mLayer.read();
    }

    SparseArray<HashMap<String, VSConfig>> getConfigs() {
        return this.mConfigs;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void setVirtualStorage(String packageName, int userId, String vsPath) {
        this.checkUserId(userId);
        SparseArray<HashMap<String, VSConfig>> sparseArray = this.mConfigs;
        synchronized (sparseArray) {
            VSConfig config = this.getOrCreateVSConfigLocked(packageName, userId);
            config.vsPath = vsPath;
            this.mLayer.save();
        }
        this.preInitPublicPath(vsPath);
    }

    private void preInitPublicPath(String vsPath) {
        new File(vsPath, "DCIM");
        for (String dir : sPublicDirs) {
            File file = new File(vsPath, dir);
            if (file.exists()) continue;
            file.mkdirs();
        }
    }

    private VSConfig getOrCreateVSConfigLocked(String packageName, int userId) {
        VSConfig config;
        HashMap<String, VSConfig> userMap = (HashMap<String, VSConfig>)this.mConfigs.get(userId);
        if (userMap == null) {
            userMap = new HashMap<String, VSConfig>();
            this.mConfigs.put(userId, userMap);
        }
        if ((config = (VSConfig)userMap.get(packageName)) == null) {
            config = new VSConfig();
            config.enable = true;
            userMap.put(packageName, config);
        }
        return config;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public String getVirtualStorage(String packageName, int userId) {
        this.checkUserId(userId);
        SparseArray<HashMap<String, VSConfig>> sparseArray = this.mConfigs;
        synchronized (sparseArray) {
            VSConfig config = this.getOrCreateVSConfigLocked(packageName, userId);
            return config.vsPath;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void setVirtualStorageState(String packageName, int userId, boolean enable) {
        this.checkUserId(userId);
        SparseArray<HashMap<String, VSConfig>> sparseArray = this.mConfigs;
        synchronized (sparseArray) {
            VSConfig config = this.getOrCreateVSConfigLocked(packageName, userId);
            config.enable = enable;
            this.mLayer.save();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public boolean isVirtualStorageEnable(String packageName, int userId) {
        this.checkUserId(userId);
        SparseArray<HashMap<String, VSConfig>> sparseArray = this.mConfigs;
        synchronized (sparseArray) {
            VSConfig config = this.getOrCreateVSConfigLocked(packageName, userId);
            return config.enable;
        }
    }

    private void checkUserId(int userId) {
        if (!VUserManagerService.get().exists(userId)) {
            throw new IllegalStateException("Invalid userId " + userId);
        }
    }
}

