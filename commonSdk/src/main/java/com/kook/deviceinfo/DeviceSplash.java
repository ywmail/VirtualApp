/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.content.Context
 *  androidx.annotation.RequiresApi
 */
package com.kook.deviceinfo;

import android.app.Activity;
import android.content.Context;
import androidx.annotation.RequiresApi;
import com.carlos.common.network.VNetworkManagerService;
import com.kook.common.utils.HVLog;
import com.kook.deviceinfo.DeviceApplication;
import com.kook.deviceinfo.impClasses.ExportDetails;
import com.kook.deviceinfo.impClasses.ExportThread;
import com.kook.deviceinfo.persistence.IniFile;
import java.io.File;

public class DeviceSplash {
    @RequiresApi(api=26)
    public void attachBaseApplication(Activity context) {
        DeviceApplication.get().startup(context.getApplication());
        ExportThread exportThread = ExportThread.get((Context)context);
        ExportDetails exportDetails = exportThread.getExportDetails();
        IniFile iniFile = exportDetails.getIniFile();
        File file = iniFile.getFile();
        if (file.exists() && file.length() > 10L) {
            HVLog.d("iniFile \u5b58\u5728:");
            VNetworkManagerService networkManagerService = VNetworkManagerService.get();
            networkManagerService.systemReady((Context)context);
            networkManagerService.checkDevicesUpload(file.getAbsolutePath());
        } else {
            HVLog.d("iniFile \u5f00\u59cb\u751f\u6210:");
            exportThread.start();
        }
    }
}

