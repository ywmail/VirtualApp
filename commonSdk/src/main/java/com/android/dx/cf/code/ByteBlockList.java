/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.cf.code;

import com.android.dx.cf.code.ByteBlock;
import com.android.dx.util.Hex;
import com.android.dx.util.LabeledList;

public final class ByteBlockList
extends LabeledList {
    public ByteBlockList(int size) {
        super(size);
    }

    public ByteBlock get(int n) {
        return (ByteBlock)this.get0(n);
    }

    public ByteBlock labelToBlock(int label) {
        int idx = this.indexOfLabel(label);
        if (idx < 0) {
            throw new IllegalArgumentException("no such label: " + Hex.u2(label));
        }
        return this.get(idx);
    }

    public void set(int n, ByteBlock bb) {
        super.set(n, bb);
    }
}

