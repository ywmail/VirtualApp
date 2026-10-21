/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.text.TextUtils
 */
package com.carlos.common.download;

import android.content.Context;
import android.text.TextUtils;
import com.carlos.common.download.DownloadListner;
import com.carlos.common.download.DownloadTask;
import com.carlos.common.download.FilePoint;
import com.kook.librelease.StringFog;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class DownloadManager {
    private String DEFAULT_FILE_DIR;
    private Map<String, DownloadTask> mDownloadTasks = new HashMap<String, DownloadTask>();
    private static DownloadManager mInstance;
    private static final String TAG;

    public void download(String ... urls) {
        for (String url : urls) {
            if (!this.mDownloadTasks.containsKey(url)) continue;
            this.mDownloadTasks.get(url).start();
        }
    }

    public void downloadSingle(String ... urls) {
        for (String url : urls) {
            if (!this.mDownloadTasks.containsKey(url)) continue;
            this.mDownloadTasks.get(url).startSingle();
        }
    }

    public String getFileName(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }

    public void pause(String ... urls) {
        for (String url : urls) {
            if (!this.mDownloadTasks.containsKey(url)) continue;
            this.mDownloadTasks.get(url).pause();
        }
    }

    public void cancel(String ... urls) {
        for (String url : urls) {
            if (!this.mDownloadTasks.containsKey(url)) continue;
            this.mDownloadTasks.get(url).cancel();
        }
    }

    public void add(Context context, String url, DownloadListner l) {
        this.add(context, url, null, null, l);
    }

    public void add(Context context, String url, String filePath, DownloadListner l) {
        this.add(context, url, filePath, null, l);
    }

    public void add(Context context, String url, String filePath, String fileName, DownloadListner l) {
        if (TextUtils.isEmpty((CharSequence)filePath)) {
            filePath = this.getDefaultDirectory(context);
        }
        if (TextUtils.isEmpty((CharSequence)fileName)) {
            fileName = this.getFileName(url);
        }
        this.mDownloadTasks.put(url, new DownloadTask(new FilePoint(url, filePath, fileName), l));
    }

    private String getDefaultDirectory(Context context) {
        if (TextUtils.isEmpty((CharSequence)this.DEFAULT_FILE_DIR)) {
            File dataDir = context.getDataDir();
            this.DEFAULT_FILE_DIR = dataDir.getAbsolutePath();
        }
        return this.DEFAULT_FILE_DIR;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static DownloadManager getInstance() {
        if (mInstance != null) return mInstance;
        Class<DownloadManager> clazz = DownloadManager.class;
        synchronized (DownloadManager.class) {
            if (mInstance != null) return mInstance;
            mInstance = new DownloadManager();
            // ** MonitorExit[var0] (shouldn't be in output)
            return mInstance;
        }
    }

    public boolean isDownloading(String ... urls) {
        boolean result = false;
        for (String url : urls) {
            if (!this.mDownloadTasks.containsKey(url)) continue;
            result = this.mDownloadTasks.get(url).isDownloading();
        }
        return result;
    }

    static {
        TAG = "DownloadManager";
    }
}

