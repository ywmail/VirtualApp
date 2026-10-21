/*
 * Decompiled with CFR 0.152.
 */
package de.robv.android.xposed.callbacks;

public interface IXUnhook<T> {
    public T getCallback();

    public void unhook();
}

