/*
 * Decompiled with CFR 0.152.
 */
package com.kook.network.file;

public interface IDownloadListener {
    public void onDownloadSuccess();

    public void onDownloadFail(Exception var1);

    public void onProgress(int var1);
}

