/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.cf.iface;

import com.android.dex.util.ExceptionWithContext;

public class ParseException
extends ExceptionWithContext {
    public ParseException(String message) {
        super(message);
    }

    public ParseException(Throwable cause) {
        super(cause);
    }

    public ParseException(String message, Throwable cause) {
        super(message, cause);
    }
}

