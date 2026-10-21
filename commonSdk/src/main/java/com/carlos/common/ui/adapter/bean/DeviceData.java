/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 */
package com.carlos.common.ui.adapter.bean;

import android.content.Context;
import com.carlos.common.ui.adapter.bean.SettingsData;
import com.lody.virtual.client.ipc.VDeviceManager;
import com.lody.virtual.remote.InstalledAppInfo;

public class DeviceData
extends SettingsData {
    public DeviceData(Context context, InstalledAppInfo installedAppInfo, int userId) {
        super(context, installedAppInfo, userId);
    }

    public boolean isMocking() {
        return VDeviceManager.get().isEnable(this.userId);
    }
}

