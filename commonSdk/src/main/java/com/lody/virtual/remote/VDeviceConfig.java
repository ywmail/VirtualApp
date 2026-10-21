/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.annotation.SuppressLint
 *  android.os.Build
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 *  android.text.TextUtils
 */
package com.lody.virtual.remote;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import com.lody.virtual.StringFog;
import com.lody.virtual.helper.utils.VLog;
import com.lody.virtual.os.VEnvironment;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class VDeviceConfig
implements Parcelable {
    private static final UsedDeviceInfoPool mPool = new UsedDeviceInfoPool();
    public static final int VERSION = 3;
    public boolean enable;
    public String deviceId;
    public String androidId;
    public String wifiMac;
    public String wifiName;
    public String bluetoothMac;
    public String iccId;
    public String serial;
    public String gmsAdId;
    public String bluetoothName;
    public final Map<String, String> buildProp = new HashMap<String, String>();
    public static final Parcelable.Creator<VDeviceConfig> CREATOR = new Parcelable.Creator<VDeviceConfig>(){

        public VDeviceConfig createFromParcel(Parcel source) {
            return new VDeviceConfig(source);
        }

        public VDeviceConfig[] newArray(int size) {
            return new VDeviceConfig[size];
        }
    };

    public VDeviceConfig() {
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.enable ? (byte)1 : 0);
        dest.writeString(this.deviceId);
        dest.writeString(this.androidId);
        dest.writeString(this.wifiMac);
        dest.writeString(this.wifiName);
        dest.writeString(this.bluetoothMac);
        dest.writeString(this.iccId);
        dest.writeString(this.serial);
        dest.writeString(this.gmsAdId);
        dest.writeInt(this.buildProp.size());
        for (Map.Entry<String, String> entry : this.buildProp.entrySet()) {
            dest.writeString(entry.getKey());
            dest.writeString(entry.getValue());
        }
        dest.writeString(this.bluetoothName);
    }

    public VDeviceConfig(Parcel in) {
        this.enable = in.readByte() != 0;
        this.deviceId = in.readString();
        this.androidId = in.readString();
        this.wifiMac = in.readString();
        this.wifiName = in.readString();
        this.bluetoothMac = in.readString();
        this.iccId = in.readString();
        this.serial = in.readString();
        this.gmsAdId = in.readString();
        int buildPropSize = in.readInt();
        for (int i = 0; i < buildPropSize; ++i) {
            String key = in.readString();
            String value = in.readString();
            this.buildProp.put(key, value);
        }
        this.bluetoothName = in.readString();
    }

    public String getProp(String key) {
        return this.buildProp.get(key);
    }

    public void setProp(String key, String value) {
        this.buildProp.put(key, value);
    }

    public void clear() {
        this.deviceId = null;
        this.androidId = null;
        this.wifiMac = null;
        this.wifiName = null;
        this.bluetoothMac = null;
        this.iccId = null;
        this.serial = null;
        this.gmsAdId = null;
    }

    public static VDeviceConfig random() {
        String value;
        VDeviceConfig info = new VDeviceConfig();
        do {
            info.deviceId = value = VDeviceConfig.generateDeviceId();
        } while (VDeviceConfig.mPool.deviceIds.contains(value));
        do {
            info.androidId = value = VDeviceConfig.generateHex(System.currentTimeMillis(), 16);
        } while (VDeviceConfig.mPool.androidIds.contains(value));
        do {
            info.wifiMac = value = VDeviceConfig.generateMac();
        } while (VDeviceConfig.mPool.wifiMacs.contains(value));
        do {
            info.wifiName = value = VDeviceConfig.generate10(System.currentTimeMillis(), 10);
        } while (VDeviceConfig.mPool.wifiName.contains(value));
        do {
            info.bluetoothMac = value = VDeviceConfig.generateMac();
        } while (VDeviceConfig.mPool.bluetoothMacs.contains(value));
        do {
            info.iccId = value = VDeviceConfig.generate10(System.currentTimeMillis(), 20);
        } while (VDeviceConfig.mPool.iccIds.contains(value));
        info.serial = VDeviceConfig.generateSerial();
        VDeviceConfig.addToPool(info);
        return info;
    }

    public static String generateDeviceId() {
        return VDeviceConfig.generate10(System.currentTimeMillis(), 15);
    }

    public static String generate10(long seed, int length) {
        Random random = new Random(seed);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; ++i) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    public static String generateHex(long seed, int length) {
        Random random = new Random(seed);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; ++i) {
            int nextInt = random.nextInt(16);
            if (nextInt < 10) {
                sb.append(nextInt);
                continue;
            }
            sb.append((char)(nextInt - 10 + 97));
        }
        return sb.toString();
    }

    private static String generateMac() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        int next = 1;
        for (int cur = 0; cur < 12; ++cur) {
            int val = random.nextInt(16);
            if (val < 10) {
                sb.append(val);
            } else {
                sb.append((char)(val + 87));
            }
            if (cur != next || cur == 11) continue;
            sb.append(":");
            next += 2;
        }
        return sb.toString();
    }

    @SuppressLint(value={"HardwareIds"})
    private static String generateSerial() {
        String serial = Build.SERIAL == null || Build.SERIAL.length() <= 0 ? "0123456789ABCDEF" : Build.SERIAL;
        ArrayList<Character> list = new ArrayList<Character>();
        VLog.e("VA-", "serial:" + serial);
        for (char c : serial.toCharArray()) {
            list.add(Character.valueOf(c));
        }
        Collections.shuffle(list);
        StringBuilder sb = new StringBuilder();
        for (Character c : list) {
            sb.append(c.charValue());
        }
        VLog.e("VA-", "serial StringBuilder:" + sb.toString());
        return sb.toString();
    }

    public File getWifiFile(int userId, boolean isExt) {
        if (TextUtils.isEmpty((CharSequence)this.wifiMac)) {
            return null;
        }
        File wifiMacFie = VEnvironment.getWifiMacFile(userId, isExt);
        if (!wifiMacFie.exists()) {
            try {
                RandomAccessFile file = new RandomAccessFile(wifiMacFie, "rws");
                file.write((this.wifiMac + "\n").getBytes());
                file.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        return wifiMacFie;
    }

    public static void addToPool(VDeviceConfig info) {
        VDeviceConfig.mPool.deviceIds.add(info.deviceId);
        VDeviceConfig.mPool.androidIds.add(info.androidId);
        VDeviceConfig.mPool.wifiMacs.add(info.wifiMac);
        VDeviceConfig.mPool.wifiName.add(info.wifiName);
        VDeviceConfig.mPool.bluetoothMacs.add(info.bluetoothMac);
        VDeviceConfig.mPool.iccIds.add(info.iccId);
    }

    private static final class UsedDeviceInfoPool {
        final List<String> deviceIds = new ArrayList<String>();
        final List<String> androidIds = new ArrayList<String>();
        final List<String> wifiMacs = new ArrayList<String>();
        final List<String> wifiName = new ArrayList<String>();
        final List<String> bluetoothMacs = new ArrayList<String>();
        final List<String> iccIds = new ArrayList<String>();
        final List<String> serials = new ArrayList<String>();

        private UsedDeviceInfoPool() {
        }
    }
}

