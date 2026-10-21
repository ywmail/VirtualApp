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

public final class Form31i
extends InsnFormat {
    public static final InsnFormat THE_ONE = new Form31i();

    private Form31i() {
    }

    @Override
    public String insnArgString(DalvInsn insn) {
        RegisterSpecList regs = insn.getRegisters();
        CstLiteralBits value = (CstLiteralBits)((CstInsn)insn).getConstant();
        return regs.get(0).regString() + ", " + Form31i.literalBitsString(value);
    }

    @Override
    public String insnCommentString(DalvInsn insn, boolean noteIndices) {
        CstLiteralBits value = (CstLiteralBits)((CstInsn)insn).getConstant();
        return Form31i.literalBitsComment(value, 32);
    }

    @Override
    public int codeSize() {
        return 3;
    }

    @Override
    public boolean isCompatible(DalvInsn insn) {
        RegisterSpecList regs = insn.getRegisters();
        if (!(insn instanceof CstInsn) || regs.size() != 1 || !Form31i.unsignedFitsInByte(regs.get(0).getReg())) {
            return false;
        }
        CstInsn ci = (CstInsn)insn;
        Constant cst = ci.getConstant();
        if (!(cst instanceof CstLiteralBits)) {
            return false;
        }
        return ((CstLiteralBits)cst).fitsInInt();
    }

    @Override
    public BitSet compatibleRegs(DalvInsn insn) {
        RegisterSpecList regs = insn.getRegisters();
        BitSet bits = new BitSet(1);
        bits.set(0, Form31i.unsignedFitsInByte(regs.get(0).getReg()));
        return bits;
    }

    @Override
    public void writeTo(AnnotatedOutput out, DalvInsn insn) {
        RegisterSpecList regs = insn.getRegisters();
        int value = ((CstLiteralBits)((CstInsn)insn).getConstant()).getIntBits();
        Form31i.write(out, Form31i.opcodeUnit(insn, regs.get(0).getReg()), value);
    }
}

