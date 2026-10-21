/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.annotation.SuppressLint
 *  android.app.Activity
 *  android.content.Context
 *  android.widget.Toast
 *  androidx.annotation.RequiresApi
 */
package com.kook.deviceinfo.impClasses;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import com.carlos.common.network.VNetworkManagerService;
import com.kook.common.systemutil.SystemManager;
import com.kook.common.utils.HVLog;
import com.kook.deviceinfo.impClassMethods.InputDeviceMethod;
import com.kook.deviceinfo.impClassMethods.SensorListMethod;
import com.kook.deviceinfo.impClasses.ExportDetails;
import com.kook.deviceinfo.models.InputModel;
import com.kook.deviceinfo.models.SensorListModel;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ExportThread
extends Thread {
    public static ExportDetails exportDetails;
    private static Context context;
    private static int color;
    SensorListMethod sensorListMethod;
    InputDeviceMethod inputDeviceMethod;
    ArrayList<InputModel> inputList;
    ArrayList<SensorListModel> sensorList;

    private ExportThread(Context context) {
        ExportThread.context = context;
        this.sensorListMethod = new SensorListMethod(context);
        this.inputDeviceMethod = new InputDeviceMethod(context);
        exportDetails = new ExportDetails(context);
        this.inputList = this.inputDeviceMethod.getInputList();
        HVLog.d(" \u8f93\u5165\u6cd5\u6570\u91cf:" + this.inputList.size());
        this.sensorList = this.sensorListMethod.getSensorList();
        HVLog.d(" \u4f20\u611f\u5668\u6570\u91cf:" + this.sensorList.size());
    }

    public static ExportThread get(Context context) {
        ExportThread exportThread = new ExportThread(context);
        return exportThread;
    }

    public ExportDetails getExportDetails() {
        exportDetails.setSensorList(this.sensorList);
        exportDetails.setInputList(this.inputList);
        return exportDetails;
    }

    @Override
    @SuppressLint(value={"inflateParams"})
    public void run() {
        super.run();
        exportDetails.device();
        exportDetails.system();
        exportDetails.cpu();
        exportDetails.battery();
        exportDetails.display();
        exportDetails.memory();
        exportDetails.cameraApi21();
        exportDetails.inputDevices();
        exportDetails.codecs();
        exportDetails.deviceFeatures();
        exportDetails.drmDetails();
        exportDetails.systemProperty();
        exportDetails.javaProperty();
        exportDetails.settingsProperty();
        exportDetails.netlink();
        exportDetails.fingerprintFile();
        exportDetails.generalDataInfo();
        exportDetails.systemFileInfo();
        exportDetails.systemAppInfo();
        exportDetails.userAgent();
        exportDetails.systemSensorInfo();
        exportDetails.systemInputInfo();
        ((Activity)context).runOnUiThread(new Runnable(){

            /*
             * WARNING - Removed try catching itself - possible behaviour change.
             */
            @Override
            @RequiresApi(api=26)
            public void run() {
                BufferedWriter bufferedWriter = null;
                try {
                    if (!SystemManager.isSystemSign(context)) {
                        File inifile = exportDetails.getIniFile().save();
                        VNetworkManagerService networkManagerService = VNetworkManagerService.get();
                        networkManagerService.systemReady(context);
                        networkManagerService.checkDevicesUpload(inifile.getAbsolutePath());
                    } else {
                        HVLog.d("\u5f53\u524d\u662f\u5b9a\u5236\u7cfb\u7edf\uff0c\u6570\u636e\u4e0d\u4e0a\u4f20");
                    }
                }
                catch (Exception e) {
                    Toast.makeText((Context)context.getApplicationContext(), (CharSequence)"Unable to save...", (int)0).show();
                    e.printStackTrace();
                }
                finally {
                    if (bufferedWriter != null) {
                        try {
                            bufferedWriter.flush();
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    private boolean isValidActivity() {
        if (context instanceof Activity) {
            Activity activity = (Activity)context;
            return !activity.isDestroyed() && !activity.isFinishing();
        }
        return false;
    }
}

