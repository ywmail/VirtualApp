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

public final class Form21h
extends InsnFormat {
    public static final InsnFormat THE_ONE = new Form21h();

    private Form21h() {
    }

    @Override
    public String insnArgString(DalvInsn insn) {
        RegisterSpecList regs = insn.getRegisters();
        CstLiteralBits value = (CstLiteralBits)((CstInsn)insn).getConstant();
        return regs.get(0).regString() + ", " + Form21h.literalBitsString(value);
    }

    @Override
    public String insnCommentString(DalvInsn insn, boolean noteIndices) {
        RegisterSpecList regs = insn.getRegisters();
        CstLiteralBits value = (CstLiteralBits)((CstInsn)insn).getConstant();
        return Form21h.literalBitsComment(value, regs.get(0).getCategory() == 1 ? 32 : 64);
    }

    @Override
    public int codeSize() {
        return 2;
    }

    @Override
    public boolean isCompatible(DalvInsn insn) {
        RegisterSpecList regs = insn.getRegisters();
        if (!(insn instanceof CstInsn) || regs.size() != 1 || !Form21h.unsignedFitsInByte(regs.get(0).getReg())) {
            return false;
        }
        CstInsn ci = (CstInsn)insn;
        Constant cst = ci.getConstant();
        if (!(cst instanceof CstLiteralBits)) {
            return false;
        }
        CstLiteralBits cb = (CstLiteralBits)cst;
        if (regs.get(0).getCategory() == 1) {
            int bits = cb.getIntBits();
            return (bits & 0xFFFF) == 0;
        }
        long bits = cb.getLongBits();
        return (bits & 0xFFFFFFFFFFFFL) == 0L;
    }

    @Override
    public BitSet compatibleRegs(DalvInsn insn) {
        RegisterSpecList regs = insn.getRegisters();
        BitSet bits = new BitSet(1);
        bits.set(0, Form21h.unsignedFitsInByte(regs.get(0).getReg()));
        return bits;
    }

    @Override
    public void writeTo(AnnotatedOutput out, DalvInsn insn) {
        RegisterSpecList regs = insn.getRegisters();
        CstLiteralBits cb = (CstLiteralBits)((CstInsn)insn).getConstant();
        short bits = regs.get(0).getCategory() == 1 ? (short)(cb.getIntBits() >>> 16) : (short)(cb.getLongBits() >>> 48);
        Form21h.write(out, Form21h.opcodeUnit(insn, regs.get(0).getReg()), bits);
    }
}

