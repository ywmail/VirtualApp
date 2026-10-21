/*
 * Decompiled with CFR 0.152.
 */
package com.kook.deviceinfo.data;

import java.util.ArrayList;
import java.util.List;

public class SensorData {
    public List<SensorInfo> sensor_lists = new ArrayList<SensorInfo>();

    public static class SensorInfo {
        public int type;
        public String name;
        public int version;
        public String vendor;
        public float max_range;
        public int min_delay;
        public float power;
        public float resolution;
    }
}

