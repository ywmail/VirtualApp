/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 */
package com.lody.virtual.server.location;

import android.os.Parcel;
import android.os.Parcelable;
import com.lody.virtual.helper.PersistenceLayer;
import com.lody.virtual.helper.collection.SparseArray;
import com.lody.virtual.os.VEnvironment;
import com.lody.virtual.remote.vloc.VCell;
import com.lody.virtual.remote.vloc.VLocation;
import com.lody.virtual.server.interfaces.IVirtualLocationManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VirtualLocationService
extends IVirtualLocationManager.Stub {
    private static final VirtualLocationService sInstance = new VirtualLocationService();
    private final SparseArray<Map<String, VLocConfig>> mLocConfigs = new SparseArray();
    private final VLocConfig mGlobalConfig = new VLocConfig();
    private final PersistenceLayer mPersistenceLayer = new PersistenceLayer(VEnvironment.getVirtualLocationFile()){

        @Override
        public int getCurrentVersion() {
            return 1;
        }

        @Override
        public void writePersistenceData(Parcel p) {
            VirtualLocationService.this.mGlobalConfig.writeToParcel(p, 0);
            p.writeInt(VirtualLocationService.this.mLocConfigs.size());
            for (int i = 0; i < VirtualLocationService.this.mLocConfigs.size(); ++i) {
                int userId = VirtualLocationService.this.mLocConfigs.keyAt(i);
                Map pkgs = (Map)VirtualLocationService.this.mLocConfigs.valueAt(i);
                p.writeInt(userId);
                p.writeMap(pkgs);
            }
        }

        @Override
        public void readPersistenceData(Parcel p, int version) {
            VirtualLocationService.this.mGlobalConfig.set(new VLocConfig(p));
            VirtualLocationService.this.mLocConfigs.clear();
            int size = p.readInt();
            while (size-- > 0) {
                int userId = p.readInt();
                HashMap pkgs = p.readHashMap(this.getClass().getClassLoader());
                VirtualLocationService.this.mLocConfigs.put(userId, pkgs);
            }
        }
    };

    public static VirtualLocationService get() {
        return sInstance;
    }

    private VirtualLocationService() {
        this.mPersistenceLayer.read();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public int getMode(int userId, String pkg) {
        SparseArray<Map<String, VLocConfig>> sparseArray = this.mLocConfigs;
        synchronized (sparseArray) {
            VLocConfig config = this.getOrCreateConfig(userId, pkg);
            this.mPersistenceLayer.save();
            return config.mode;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void setMode(int userId, String pkg, int mode) {
        SparseArray<Map<String, VLocConfig>> sparseArray = this.mLocConfigs;
        synchronized (sparseArray) {
            this.getOrCreateConfig((int)userId, (String)pkg).mode = mode;
            this.mPersistenceLayer.save();
        }
    }

    private VLocConfig getOrCreateConfig(int userId, String pkg) {
        VLocConfig config;
        Map<String, VLocConfig> pkgs = this.mLocConfigs.get(userId);
        if (pkgs == null) {
            pkgs = new HashMap<String, VLocConfig>();
            this.mLocConfigs.put(userId, pkgs);
        }
        if ((config = pkgs.get(pkg)) == null) {
            config = new VLocConfig();
            config.mode = 0;
            pkgs.put(pkg, config);
        }
        return config;
    }

    @Override
    public void setCell(int userId, String pkg, VCell cell) {
        this.getOrCreateConfig((int)userId, (String)pkg).cell = cell;
        this.mPersistenceLayer.save();
    }

    @Override
    public void setAllCell(int userId, String pkg, List<VCell> cell) {
        this.getOrCreateConfig((int)userId, (String)pkg).allCell = cell;
        this.mPersistenceLayer.save();
    }

    @Override
    public void setNeighboringCell(int userId, String pkg, List<VCell> cell) {
        this.getOrCreateConfig((int)userId, (String)pkg).neighboringCell = cell;
        this.mPersistenceLayer.save();
    }

    @Override
    public void setGlobalCell(VCell cell) {
        this.mGlobalConfig.cell = cell;
        this.mPersistenceLayer.save();
    }

    @Override
    public void setGlobalAllCell(List<VCell> cell) {
        this.mGlobalConfig.allCell = cell;
        this.mPersistenceLayer.save();
    }

    @Override
    public void setGlobalNeighboringCell(List<VCell> cell) {
        this.mGlobalConfig.neighboringCell = cell;
        this.mPersistenceLayer.save();
    }

    @Override
    public VCell getCell(int userId, String pkg) {
        VLocConfig config = this.getOrCreateConfig(userId, pkg);
        this.mPersistenceLayer.save();
        switch (config.mode) {
            case 2: {
                return config.cell;
            }
            case 1: {
                return this.mGlobalConfig.cell;
            }
        }
        return null;
    }

    @Override
    public List<VCell> getAllCell(int userId, String pkg) {
        VLocConfig config = this.getOrCreateConfig(userId, pkg);
        this.mPersistenceLayer.save();
        switch (config.mode) {
            case 2: {
                return config.allCell;
            }
            case 1: {
                return this.mGlobalConfig.allCell;
            }
        }
        return null;
    }

    @Override
    public List<VCell> getNeighboringCell(int userId, String pkg) {
        VLocConfig config = this.getOrCreateConfig(userId, pkg);
        this.mPersistenceLayer.save();
        switch (config.mode) {
            case 2: {
                return config.neighboringCell;
            }
            case 1: {
                return this.mGlobalConfig.neighboringCell;
            }
        }
        return null;
    }

    @Override
    public void setLocation(int userId, String pkg, VLocation loc) {
        this.getOrCreateConfig((int)userId, (String)pkg).location = loc;
        this.mPersistenceLayer.save();
    }

    @Override
    public VLocation getLocation(int userId, String pkg) {
        VLocConfig config = this.getOrCreateConfig(userId, pkg);
        this.mPersistenceLayer.save();
        switch (config.mode) {
            case 2: {
                return config.location;
            }
            case 1: {
                return this.mGlobalConfig.location;
            }
        }
        return null;
    }

    @Override
    public void setGlobalLocation(VLocation loc) {
        this.mGlobalConfig.location = loc;
    }

    @Override
    public VLocation getGlobalLocation() {
        return this.mGlobalConfig.location;
    }

    private static class VLocConfig
    implements Parcelable {
        int mode;
        VCell cell;
        List<VCell> allCell;
        List<VCell> neighboringCell;
        VLocation location;
        public static final Parcelable.Creator<VLocConfig> CREATOR = new Parcelable.Creator<VLocConfig>(){

            public VLocConfig createFromParcel(Parcel source) {
                return new VLocConfig(source);
            }

            public VLocConfig[] newArray(int size) {
                return new VLocConfig[size];
            }
        };

        public void set(VLocConfig other) {
            this.mode = other.mode;
            this.cell = other.cell;
            this.allCell = other.allCell;
            this.neighboringCell = other.neighboringCell;
            this.location = other.location;
        }

        VLocConfig() {
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.mode);
            dest.writeParcelable((Parcelable)this.cell, flags);
            dest.writeTypedList(this.allCell);
            dest.writeTypedList(this.neighboringCell);
            dest.writeParcelable((Parcelable)this.location, flags);
        }

        VLocConfig(Parcel in) {
            this.mode = in.readInt();
            this.cell = (VCell)in.readParcelable(VCell.class.getClassLoader());
            this.allCell = in.createTypedArrayList(VCell.CREATOR);
            this.neighboringCell = in.createTypedArrayList(VCell.CREATOR);
            this.location = (VLocation)in.readParcelable(VLocation.class.getClassLoader());
        }
    }
}

