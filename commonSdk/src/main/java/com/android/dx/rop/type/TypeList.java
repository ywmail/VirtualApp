/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.rop.type;

import com.android.dx.rop.type.Type;

public interface TypeList {
    public boolean isMutable();

    public int size();

    public Type getType(int var1);

    public int getWordCount();

    public TypeList withAddedType(Type var1);
}

