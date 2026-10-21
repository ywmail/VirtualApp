/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.rop.code;

import com.android.dx.rop.code.RegisterSpec;
import com.android.dx.rop.code.RegisterSpecList;
import com.android.dx.rop.code.Rop;
import com.android.dx.rop.code.TranslationAdvice;
import com.android.dx.rop.cst.CstInteger;
import com.android.dx.rop.type.Type;

public final class DexTranslationAdvice
implements TranslationAdvice {
    public static final DexTranslationAdvice THE_ONE = new DexTranslationAdvice();
    public static final DexTranslationAdvice NO_SOURCES_IN_ORDER = new DexTranslationAdvice(true);
    private static final int MIN_INVOKE_IN_ORDER = 6;
    private final boolean disableSourcesInOrder;

    private DexTranslationAdvice() {
        this.disableSourcesInOrder = false;
    }

    private DexTranslationAdvice(boolean disableInvokeRange) {
        this.disableSourcesInOrder = disableInvokeRange;
    }

    @Override
    public boolean hasConstantOperation(Rop opcode, RegisterSpec sourceA, RegisterSpec sourceB) {
        if (sourceA.getType() != Type.INT) {
            return false;
        }
        if (!(sourceB.getTypeBearer() instanceof CstInteger)) {
            if (sourceA.getTypeBearer() instanceof CstInteger && opcode.getOpcode() == 15) {
                CstInteger cst = (CstInteger)sourceA.getTypeBearer();
                return cst.fitsIn16Bits();
            }
            return false;
        }
        CstInteger cst = (CstInteger)sourceB.getTypeBearer();
        switch (opcode.getOpcode()) {
            case 14: 
            case 16: 
            case 17: 
            case 18: 
            case 20: 
            case 21: 
            case 22: {
                return cst.fitsIn16Bits();
            }
            case 23: 
            case 24: 
            case 25: {
                return cst.fitsIn8Bits();
            }
            case 15: {
                CstInteger cst2 = CstInteger.make(-cst.getValue());
                return cst2.fitsIn16Bits();
            }
        }
        return false;
    }

    @Override
    public boolean requiresSourcesInOrder(Rop opcode, RegisterSpecList sources) {
        return !this.disableSourcesInOrder && opcode.isCallLike() && this.totalRopWidth(sources) >= 6;
    }

    private int totalRopWidth(RegisterSpecList sources) {
        int sz = sources.size();
        int total = 0;
        for (int i = 0; i < sz; ++i) {
            total += sources.get(i).getCategory();
        }
        return total;
    }

    @Override
    public int getMaxOptimalRegisterCount() {
        return 16;
    }
}

