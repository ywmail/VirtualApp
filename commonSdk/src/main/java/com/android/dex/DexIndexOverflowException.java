/*
 * Decompiled with CFR 0.152.
 */
package com.android.dex;

import com.android.dex.DexException;

public final class DexIndexOverflowException
extends DexException {
    public DexIndexOverflowException(String message) {
        super(message);
    }

    public DexIndexOverflowException(Throwable cause) {
        super(cause);
    }
}

