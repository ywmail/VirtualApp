/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  org.json.JSONException
 *  org.json.JSONObject
 */
package com.kook.deviceinfo.impClasses;

import android.content.Context;
import com.kook.common.utils.HVLog;
import com.kook.deviceinfo.constant.SystemFileConStant;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonData {
    private String cpuFamily = "";
    private String process = "";
    private String memory = "";
    private String bandwidth = "";
    private String channels = "";
    private String json = null;
    private String machine;
    private final Context context;

    public JsonData(Context context) {
        this.context = context;
    }

    private void getDataFromJson() {
        try {
            InputStream inputStream = this.context.getAssets().open("socList.json");
            int size = inputStream.available();
            byte[] bytes = new byte[size];
            inputStream.read(bytes);
            inputStream.close();
            this.json = new String(bytes);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        try {
            String str;
            BufferedReader bufferedReader = new BufferedReader(new FileReader(SystemFileConStant.SOC_MACHINE));
            while ((str = bufferedReader.readLine()) != null) {
                this.machine = str;
            }
        }
        catch (IOException e) {
            this.machine = "error";
            e.printStackTrace();
        }
        if (this.json != null) {
            try {
                JSONObject jsonObject = new JSONObject(this.json);
                HVLog.e("machine:" + this.machine + "   " + jsonObject.isNull(this.machine));
                if (!jsonObject.isNull(this.machine)) {
                    JSONObject object = jsonObject.getJSONObject(this.machine);
                    if (!object.getString("CPU").equals("")) {
                        this.cpuFamily = object.getString("CPU");
                    }
                    if (!object.getString("FAB").equals("")) {
                        this.process = object.getString("FAB");
                    }
                    this.memory = object.getString("MEMORY");
                    this.bandwidth = object.getString("BANDWIDTH");
                    this.channels = object.getString("CHANNELS");
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public String getCpuFamily() {
        return this.cpuFamily;
    }

    public String getProcess() {
        return this.process;
    }

    public String getMemory() {
        return this.memory;
    }

    public String getBandwidth() {
        return this.bandwidth;
    }

    public String getChannels() {
        return this.channels;
    }
}

