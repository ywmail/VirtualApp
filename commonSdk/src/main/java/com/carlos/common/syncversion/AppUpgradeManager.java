/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 */
package com.carlos.common.syncversion;

import android.content.Context;
import com.carlos.common.download.DownloadListner;
import com.carlos.common.download.DownloadManager;
import com.kook.common.utils.HVLog;
import com.kook.librelease.StringFog;

public class AppUpgradeManager {
    public static AppUpgradeManager mAppUpgradeManager = new AppUpgradeManager();
    private boolean SYNC_STATUS = false;
    private String APPLICATION_SERVER_URL = "https://github.com/ServenScorpion/virtualapp_version_release_config/blob/master/va_config.xml";

    public static AppUpgradeManager getInstance() {
        return mAppUpgradeManager;
    }

    public boolean syncVersion(Context context, final SyncCallback syncCallback) {
        this.SYNC_STATUS = false;
        DownloadManager downloadManager = DownloadManager.getInstance();
        downloadManager.add(context, this.APPLICATION_SERVER_URL, new DownloadListner(){

            @Override
            public void onFinished() {
                AppUpgradeManager.this.SYNC_STATUS = true;
                syncCallback.finishedListener();
                HVLog.d("va_config 下载完成");
            }

            @Override
            public void onProgress(float progress) {
                HVLog.d("va_config 下载 progress ：" + progress);
            }

            @Override
            public void onPause() {
                HVLog.d("va_config 暂停下载");
            }

            @Override
            public void onCancel() {
                HVLog.d("va_config 取消下载");
            }
        });
        downloadManager.downloadSingle(this.APPLICATION_SERVER_URL);
        return true;
    }

    public static interface SyncCallback {
        public void finishedListener();
    }
}

