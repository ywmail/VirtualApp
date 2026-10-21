/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.rop.code;

import com.android.dx.rop.code.RegisterSpec;
import com.android.dx.rop.code.RegisterSpecList;
import com.android.dx.rop.code.Rop;

public interface TranslationAdvice {
    public boolean hasConstantOperation(Rop var1, RegisterSpec var2, RegisterSpec var3);

    public boolean requiresSourcesInOrder(Rop var1, RegisterSpecList var2);

    public int getMaxOptimalRegisterCount();
}

