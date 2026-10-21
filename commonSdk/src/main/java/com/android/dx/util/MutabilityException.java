/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.util;

import com.android.dex.util.ExceptionWithContext;

public class MutabilityException
extends ExceptionWithContext {
    public MutabilityException(String message) {
        super(message);
    }

    public MutabilityException(Throwable cause) {
        super(cause);
    }

    public MutabilityException(String message, Throwable cause) {
        super(message, cause);
    }
}

