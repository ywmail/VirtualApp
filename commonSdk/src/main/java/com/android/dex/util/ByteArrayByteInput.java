/*
 * Decompiled with CFR 0.152.
 */
package com.android.dex.util;

import com.android.dex.util.ByteInput;

public final class ByteArrayByteInput
implements ByteInput {
    private final byte[] bytes;
    private int position;

    public ByteArrayByteInput(byte ... bytes) {
        this.bytes = bytes;
    }

    @Override
    public byte readByte() {
        return this.bytes[this.position++];
    }
}

