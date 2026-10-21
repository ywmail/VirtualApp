/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.dex.code.form;

import com.android.dx.dex.code.CstInsn;
import com.android.dx.dex.code.DalvInsn;
import com.android.dx.dex.code.InsnFormat;
import com.android.dx.rop.code.RegisterSpecList;
import com.android.dx.rop.cst.Constant;
import com.android.dx.rop.cst.CstLiteralBits;
import com.android.dx.util.AnnotatedOutput;
import java.util.BitSet;

public final class Form21s
extends InsnFormat {
    public static final InsnFormat THE_ONE = new Form21s();

    private Form21s() {
    }

    @Override
    public String insnArgString(DalvInsn insn) {
        RegisterSpecList regs = insn.getRegisters();
        CstLiteralBits value = (CstLiteralBits)((CstInsn)insn).getConstant();
        return regs.get(0).regString() + ", " + Form21s.literalBitsString(value);
    }

    @Override
    public String insnCommentString(DalvInsn insn, boolean noteIndices) {
        CstLiteralBits value = (CstLiteralBits)((CstInsn)insn).getConstant();
        return Form21s.literalBitsComment(value, 16);
    }

    @Override
    public int codeSize() {
        return 2;
    }

    @Override
    public boolean isCompatible(DalvInsn insn) {
        RegisterSpecList regs = insn.getRegisters();
        if (!(insn instanceof CstInsn) || regs.size() != 1 || !Form21s.unsignedFitsInByte(regs.get(0).getReg())) {
            return false;
        }
        CstInsn ci = (CstInsn)insn;
        Constant cst = ci.getConstant();
        if (!(cst instanceof CstLiteralBits)) {
            return false;
        }
        CstLiteralBits cb = (CstLiteralBits)cst;
        return cb.fitsInInt() && Form21s.signedFitsInShort(cb.getIntBits());
    }

    @Override
    public BitSet compatibleRegs(DalvInsn insn) {
        RegisterSpecList regs = insn.getRegisters();
        BitSet bits = new BitSet(1);
        bits.set(0, Form21s.unsignedFitsInByte(regs.get(0).getReg()));
        return bits;
    }

    @Override
    public void writeTo(AnnotatedOutput out, DalvInsn insn) {
        RegisterSpecList regs = insn.getRegisters();
        int value = ((CstLiteralBits)((CstInsn)insn).getConstant()).getIntBits();
        Form21s.write(out, Form21s.opcodeUnit(insn, regs.get(0).getReg()), (short)value);
    }
}

