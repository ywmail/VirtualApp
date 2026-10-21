/*
 * Decompiled with CFR 0.152.
 */
package com.lody.virtual.server.am;

import com.lody.virtual.StringFog;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.helper.utils.FileUtils;
import com.lody.virtual.helper.utils.VLog;
import com.lody.virtual.os.VEnvironment;
import com.lody.virtual.server.pm.parser.VPackage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

public class UidSystem {
    private static final String TAG = UidSystem.class.getSimpleName();
    private final HashMap<String, Integer> mSharedUserIdMap = new HashMap();
    private int mFreeUid = 10000;

    public void initUidList() {
        this.mSharedUserIdMap.clear();
        File uidFile = VEnvironment.getUidListFile();
        if (!this.loadUidList(uidFile)) {
            File bakUidFile = VEnvironment.getBakUidListFile();
            this.loadUidList(bakUidFile);
        }
    }

    private boolean loadUidList(File uidFile) {
        if (!uidFile.exists()) {
            return false;
        }
        try {
            ObjectInputStream is = new ObjectInputStream(new FileInputStream(uidFile));
            this.mFreeUid = is.readInt();
            HashMap map = (HashMap)is.readObject();
            this.mSharedUserIdMap.putAll(map);
            is.close();
        }
        catch (Throwable e) {
            return false;
        }
        return true;
    }

    private void save() {
        File uidFile = VEnvironment.getUidListFile();
        File bakUidFile = VEnvironment.getBakUidListFile();
        if (uidFile.exists()) {
            if (bakUidFile.exists() && !bakUidFile.delete()) {
                VLog.w(TAG, "Warning: Unable to delete the expired file --\n " + bakUidFile.getPath(), new Object[0]);
            }
            try {
                FileUtils.copyFile(uidFile, bakUidFile);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(uidFile));
            os.writeInt(this.mFreeUid);
            os.writeObject(this.mSharedUserIdMap);
            os.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public int getOrCreateUid(VPackage pkg) {
        HashMap<String, Integer> hashMap = this.mSharedUserIdMap;
        synchronized (hashMap) {
            int newUid;
            Integer uid;
            String sharedUserId = pkg.mSharedUserId;
            if (sharedUserId == null) {
                sharedUserId = pkg.packageName;
            }
            if ((uid = this.mSharedUserIdMap.get(sharedUserId)) != null) {
                return uid;
            }
            ++this.mFreeUid;
            if ((newUid = this.mFreeUid++) == VirtualCore.get().myUid()) {
                newUid = this.mFreeUid;
            }
            this.mSharedUserIdMap.put(sharedUserId, newUid);
            this.save();
            return newUid;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public int getUid(String sharedUserName) {
        HashMap<String, Integer> hashMap = this.mSharedUserIdMap;
        synchronized (hashMap) {
            Integer uid = this.mSharedUserIdMap.get(sharedUserName);
            if (uid != null) {
                return uid;
            }
        }
        return -1;
    }
}

