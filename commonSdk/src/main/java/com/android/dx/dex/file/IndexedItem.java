/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.dex.file;

import com.android.dx.dex.file.Item;

public abstract class IndexedItem
extends Item {
    private int index = -1;

    public final boolean hasIndex() {
        return this.index >= 0;
    }

    public final int getIndex() {
        if (this.index < 0) {
            throw new RuntimeException("index not yet set");
        }
        return this.index;
    }

    public final void setIndex(int index) {
        if (this.index != -1) {
            throw new RuntimeException("index already set");
        }
        this.index = index;
    }

    public final String indexString() {
        return '[' + Integer.toHexString(this.index) + ']';
    }
}

