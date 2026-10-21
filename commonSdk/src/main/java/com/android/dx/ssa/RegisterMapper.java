/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.ssa;

import com.android.dx.rop.code.RegisterSpec;
import com.android.dx.rop.code.RegisterSpecList;
import com.android.dx.rop.code.RegisterSpecSet;

public abstract class RegisterMapper {
    public abstract int getNewRegisterCount();

    public abstract RegisterSpec map(RegisterSpec var1);

    public final RegisterSpecList map(RegisterSpecList sources) {
        int sz = sources.size();
        RegisterSpecList newSources = new RegisterSpecList(sz);
        for (int i = 0; i < sz; ++i) {
            newSources.set(i, this.map(sources.get(i)));
        }
        newSources.setImmutable();
        return newSources.equals(sources) ? sources : newSources;
    }

    public final RegisterSpecSet map(RegisterSpecSet sources) {
        int sz = sources.getMaxSize();
        RegisterSpecSet newSources = new RegisterSpecSet(this.getNewRegisterCount());
        for (int i = 0; i < sz; ++i) {
            RegisterSpec registerSpec = sources.get(i);
            if (registerSpec == null) continue;
            newSources.put(this.map(registerSpec));
        }
        newSources.setImmutable();
        return newSources.equals(sources) ? sources : newSources;
    }
}

