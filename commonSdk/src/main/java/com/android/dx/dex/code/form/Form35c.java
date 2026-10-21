/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.dex.code.form;

import com.android.dx.dex.code.CstInsn;
import com.android.dx.dex.code.DalvInsn;
import com.android.dx.dex.code.InsnFormat;
import com.android.dx.rop.code.RegisterSpec;
import com.android.dx.rop.code.RegisterSpecList;
import com.android.dx.rop.cst.Constant;
import com.android.dx.rop.cst.CstCallSiteRef;
import com.android.dx.rop.cst.CstMethodRef;
import com.android.dx.rop.cst.CstType;
import com.android.dx.rop.type.Type;
import com.android.dx.util.AnnotatedOutput;
import java.util.BitSet;

public final class Form35c
extends InsnFormat {
    public static final InsnFormat THE_ONE = new Form35c();
    private static final int MAX_NUM_OPS = 5;

    private Form35c() {
    }

    @Override
    public String insnArgString(DalvInsn insn) {
        RegisterSpecList regs = Form35c.explicitize(insn.getRegisters());
        return Form35c.regListString(regs) + ", " + insn.cstString();
    }

    @Override
    public String insnCommentString(DalvInsn insn, boolean noteIndices) {
        if (noteIndices) {
            return insn.cstComment();
        }
        return "";
    }

    @Override
    public int codeSize() {
        return 3;
    }

    @Override
    public boolean isCompatible(DalvInsn insn) {
        if (!(insn instanceof CstInsn)) {
            return false;
        }
        CstInsn ci = (CstInsn)insn;
        int cpi = ci.getIndex();
        if (!Form35c.unsignedFitsInShort(cpi)) {
            return false;
        }
        Constant cst = ci.getConstant();
        if (!(cst instanceof CstMethodRef || cst instanceof CstType || cst instanceof CstCallSiteRef)) {
            return false;
        }
        RegisterSpecList regs = ci.getRegisters();
        return Form35c.wordCount(regs) >= 0;
    }

    @Override
    public BitSet compatibleRegs(DalvInsn insn) {
        RegisterSpecList regs = insn.getRegisters();
        int sz = regs.size();
        BitSet bits = new BitSet(sz);
        for (int i = 0; i < sz; ++i) {
            RegisterSpec reg = regs.get(i);
            bits.set(i, Form35c.unsignedFitsInNibble(reg.getReg() + reg.getCategory() - 1));
        }
        return bits;
    }

    @Override
    public void writeTo(AnnotatedOutput out, DalvInsn insn) {
        int cpi = ((CstInsn)insn).getIndex();
        RegisterSpecList regs = Form35c.explicitize(insn.getRegisters());
        int sz = regs.size();
        int r0 = sz > 0 ? regs.get(0).getReg() : 0;
        int r1 = sz > 1 ? regs.get(1).getReg() : 0;
        int r2 = sz > 2 ? regs.get(2).getReg() : 0;
        int r3 = sz > 3 ? regs.get(3).getReg() : 0;
        int r4 = sz > 4 ? regs.get(4).getReg() : 0;
        Form35c.write(out, Form35c.opcodeUnit(insn, Form35c.makeByte(r4, sz)), (short)cpi, Form35c.codeUnit(r0, r1, r2, r3));
    }

    private static int wordCount(RegisterSpecList regs) {
        int sz = regs.size();
        if (sz > 5) {
            return -1;
        }
        int result = 0;
        for (int i = 0; i < sz; ++i) {
            RegisterSpec one = regs.get(i);
            result += one.getCategory();
            if (Form35c.unsignedFitsInNibble(one.getReg() + one.getCategory() - 1)) continue;
            return -1;
        }
        return result <= 5 ? result : -1;
    }

    private static RegisterSpecList explicitize(RegisterSpecList orig) {
        int sz;
        int wordCount = Form35c.wordCount(orig);
        if (wordCount == (sz = orig.size())) {
            return orig;
        }
        RegisterSpecList result = new RegisterSpecList(wordCount);
        int wordAt = 0;
        for (int i = 0; i < sz; ++i) {
            RegisterSpec one = orig.get(i);
            result.set(wordAt, one);
            if (one.getCategory() == 2) {
                result.set(wordAt + 1, RegisterSpec.make(one.getReg() + 1, Type.VOID));
                wordAt += 2;
                continue;
            }
            ++wordAt;
        }
        result.setImmutable();
        return result;
    }
}

