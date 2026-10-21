/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.reactivex.observers.DefaultObserver
 */
package com.kook.network.file;

import io.reactivex.observers.DefaultObserver;

public abstract class FileUploadObserver<T>
extends DefaultObserver<T> {
    public void onNext(T t) {
        this.onUpLoadNext(t);
    }

    public void onError(Throwable e) {
        this.onUpLoadFail(e);
    }

    public void onComplete() {
        this.onUpLoadSuccess();
    }

    public void onProgressChange(long bytesWritten, long contentLength) {
        this.onProgress((int)(bytesWritten * 100L / contentLength));
    }

    public abstract void onUpLoadSuccess();

    public abstract void onUpLoadNext(T var1);

    public abstract void onUpLoadFail(Throwable var1);

    public abstract void onProgress(int var1);
}

