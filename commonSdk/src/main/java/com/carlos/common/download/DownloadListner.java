/*
 * Decompiled with CFR 0.152.
 */
package com.carlos.common.download;

public interface DownloadListner {
    public void onFinished();

    public void onProgress(float var1);

    public void onPause();

    public void onCancel();
}

