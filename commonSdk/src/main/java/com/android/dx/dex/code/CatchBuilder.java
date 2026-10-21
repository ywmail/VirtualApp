/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.dex.code;

import com.android.dx.dex.code.CatchTable;
import com.android.dx.rop.type.Type;
import java.util.HashSet;

public interface CatchBuilder {
    public CatchTable build();

    public boolean hasAnyCatches();

    public HashSet<Type> getCatchTypes();
}

