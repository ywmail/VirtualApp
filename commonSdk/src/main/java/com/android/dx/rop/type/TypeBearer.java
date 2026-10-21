/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.rop.type;

import com.android.dx.rop.type.Type;
import com.android.dx.util.ToHuman;

public interface TypeBearer
extends ToHuman {
    public Type getType();

    public TypeBearer getFrameType();

    public int getBasicType();

    public int getBasicFrameType();

    public boolean isConstant();
}

