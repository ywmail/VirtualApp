/*
 * Decompiled with CFR 0.152.
 */
package com.lody.virtual.helper.utils;

public abstract class Singleton<T> {
    private T mInstance;

    protected abstract T create();

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public final T get() {
        if (this.mInstance == null) {
            Singleton singleton = this;
            synchronized (singleton) {
                if (this.mInstance == null) {
                    this.mInstance = this.create();
                }
            }
        }
        return this.mInstance;
    }
}

