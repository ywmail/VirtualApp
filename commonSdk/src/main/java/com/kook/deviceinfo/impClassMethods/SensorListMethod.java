/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.hardware.Sensor
 *  android.hardware.SensorManager
 */
package com.kook.deviceinfo.impClassMethods;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import com.kook.deviceinfo.impClasses.BuildInfo;
import com.kook.deviceinfo.models.SensorListModel;
import java.util.ArrayList;
import java.util.List;

public class SensorListMethod {
    private final Context context;
    List<Sensor> sensors = new ArrayList<Sensor>();
    ArrayList<SensorListModel> sensorList = new ArrayList();
    SensorManager sm;
    BuildInfo buildInfo;

    public SensorListMethod(Context context) {
        this.context = context;
        this.sm = (SensorManager)context.getSystemService("sensor");
        this.sensorInfo();
    }

    private void sensorInfo() {
        this.buildInfo = new BuildInfo(this.context);
        this.sensors = this.sm.getSensorList(-1);
        for (Sensor s : this.sensors) {
            int id2 = s.getId();
            String name = s.getName();
            String vendor = s.getVendor();
            String stringType = s.getStringType();
            float power = s.getPower();
            int version = s.getVersion();
            float resolution = s.getResolution();
            float maximumRange = s.getMaximumRange();
            int fifoMaxEventCount = s.getFifoMaxEventCount();
            int fifoReservedEventCount = s.getFifoReservedEventCount();
            int maxDelay = s.getMaxDelay();
            int minDelay = s.getMinDelay();
            int reportingMode = s.getReportingMode();
            SensorListModel sensorListModel = new SensorListModel(id2, name, vendor, stringType, power, version, resolution, maximumRange, fifoMaxEventCount, fifoReservedEventCount, maxDelay, minDelay, reportingMode);
            this.sensorList.add(sensorListModel);
        }
    }

    public ArrayList<SensorListModel> getSensorList() {
        return this.sensorList;
    }
}

