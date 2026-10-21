/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.io.instructions;

import com.android.dx.io.instructions.CodeCursor;
import java.io.EOFException;

public interface CodeInput
extends CodeCursor {
    public boolean hasMore();

    public int read() throws EOFException;

    public int readInt() throws EOFException;

    public long readLong() throws EOFException;
}

