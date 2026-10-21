/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.cf.code;

import com.android.dex.util.ExceptionWithContext;
import com.android.dx.cf.code.LocalsArraySet;
import com.android.dx.cf.code.OneLocalsArray;
import com.android.dx.rop.code.RegisterSpec;
import com.android.dx.rop.type.Type;
import com.android.dx.rop.type.TypeBearer;
import com.android.dx.util.MutabilityControl;
import com.android.dx.util.ToHuman;

public abstract class LocalsArray
extends MutabilityControl
implements ToHuman {
    protected LocalsArray(boolean mutable) {
        super(mutable);
    }

    public abstract LocalsArray copy();

    public abstract void annotate(ExceptionWithContext var1);

    public abstract void makeInitialized(Type var1);

    public abstract int getMaxLocals();

    public abstract void set(int var1, TypeBearer var2);

    public abstract void set(RegisterSpec var1);

    public abstract void invalidate(int var1);

    public abstract TypeBearer getOrNull(int var1);

    public abstract TypeBearer get(int var1);

    public abstract TypeBearer getCategory1(int var1);

    public abstract TypeBearer getCategory2(int var1);

    public abstract LocalsArray merge(LocalsArray var1);

    public abstract LocalsArraySet mergeWithSubroutineCaller(LocalsArray var1, int var2);

    protected abstract OneLocalsArray getPrimary();
}

