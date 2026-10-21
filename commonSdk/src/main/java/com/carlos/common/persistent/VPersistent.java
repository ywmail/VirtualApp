/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 */
package com.carlos.common.persistent;

import android.os.Parcel;
import android.os.Parcelable;
import com.carlos.common.network.StringFog;
import java.util.HashMap;
import java.util.Map;

public class VPersistent
implements Parcelable {
    public static final int VERSION = 3;
    public static final String PRODUCT_ENV_SIT = "sit";
    public static final String PRODUCT_ENV_PROD = "prod";
    public static String PRODUCT_ENV_KEY;
    public int requestCount = 0;
    public long currentTimeMillis = 0L;
    public static final String upgradeEnforce;
    public static final String upgradeVersion;
    public static String fileName;
    public static final String fileMd5;
    public static final String urlHost;
    public static final String uploadAppUrl;
    public static final String downloadAppUrl;
    public static final String uploadDevicesUrl;
    public static final String downloadDevicesUrl;
    public static final String requestTime;
    public static final String heartbeatCount;
    public final Map<String, String> buildAllConfig = new HashMap<String, String>();
    public static final String adbHook;
    public static final String backupRecovery;
    public static final String dingtalk;
    public static final String dingtalkPic;
    public static final String hookXposed;
    public static final String injectSo;
    public static final String mockDevice;
    public static final String mockphone;
    public static final String mockwifi;
    public static final String staticIp;
    public static final String virtualbox;
    public static final String virtuallocation;
    public static final String channelLimit;
    public static final String channelStatus;
    public static final Parcelable.Creator<VPersistent> CREATOR;

    public int describeContents() {
        long currentTimeMillis = System.currentTimeMillis();
        return 0;
    }

    public VPersistent() {
    }

    public String getBuildConfig(String key) {
        return this.buildAllConfig.get(key);
    }

    public void setBuildConfig(String key, String value) {
        this.buildAllConfig.put(key, value);
    }

    public void readToParcel(Parcel in) {
        this.requestCount = in.readInt();
        this.currentTimeMillis = in.readLong();
        int buildAppConfigSize = in.readInt();
        for (int i = 0; i < buildAppConfigSize; ++i) {
            String key = in.readString();
            String value = in.readString();
            this.buildAllConfig.put(key, value);
        }
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.requestCount);
        dest.writeLong(this.currentTimeMillis);
        dest.writeInt(this.buildAllConfig.size());
        for (Map.Entry<String, String> entry : this.buildAllConfig.entrySet()) {
            dest.writeString(entry.getKey());
            dest.writeString(entry.getValue());
        }
    }

    public VPersistent(Parcel in) {
        this.readToParcel(in);
    }

    static {
        upgradeEnforce = "upgradeEnforce";
        upgradeVersion = "upgradeVersion";
        fileMd5 = "fileMd5";
        urlHost = "url_host";
        uploadAppUrl = "upload_app_url";
        downloadAppUrl = "download_app_url";
        uploadDevicesUrl = "upload_devices_url";
        downloadDevicesUrl = "download_devices_url";
        requestTime = "requestTime";
        heartbeatCount = "heartbeatCount";
        adbHook = "adbHook";
        backupRecovery = "backupRecovery";
        dingtalk = "dingtalk";
        dingtalkPic = "dingtalkPic";
        hookXposed = "hookXposed";
        injectSo = "injectSo";
        mockDevice = "mockDevice";
        mockphone = "mockphone";
        mockwifi = "mockwifi";
        staticIp = "staticIp";
        virtualbox = "virtualbox";
        virtuallocation = "virtuallocation";
        channelLimit = "channelLimit";
        channelStatus = "channelStatus";
        PRODUCT_ENV_KEY = "product_ent_key";
        fileName = "fileName";
        CREATOR = new Parcelable.Creator<VPersistent>(){

            public VPersistent createFromParcel(Parcel source) {
                return new VPersistent(source);
            }

            public VPersistent[] newArray(int size) {
                return new VPersistent[size];
            }
        };
    }
}

