/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.rop.code;

import com.android.dx.rop.code.RegisterSpec;
import com.android.dx.rop.code.RegisterSpecList;
import com.android.dx.rop.code.Rop;
import com.android.dx.rop.code.TranslationAdvice;

public final class ConservativeTranslationAdvice
implements TranslationAdvice {
    public static final ConservativeTranslationAdvice THE_ONE = new ConservativeTranslationAdvice();

    private ConservativeTranslationAdvice() {
    }

    @Override
    public boolean hasConstantOperation(Rop opcode, RegisterSpec sourceA, RegisterSpec sourceB) {
        return false;
    }

    @Override
    public boolean requiresSourcesInOrder(Rop opcode, RegisterSpecList sources) {
        return false;
    }

    @Override
    public int getMaxOptimalRegisterCount() {
        return Integer.MAX_VALUE;
    }
}

