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
import com.android.dx.rop.cst.CstFieldRef;
import com.android.dx.rop.cst.CstMethodHandle;
import com.android.dx.rop.cst.CstProtoRef;
import com.android.dx.rop.cst.CstString;
import com.android.dx.rop.cst.CstType;
import com.android.dx.util.AnnotatedOutput;
import java.util.BitSet;

public final class Form21c
extends InsnFormat {
    public static final InsnFormat THE_ONE = new Form21c();

    private Form21c() {
    }

    @Override
    public String insnArgString(DalvInsn insn) {
        RegisterSpecList regs = insn.getRegisters();
        return regs.get(0).regString() + ", " + insn.cstString();
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
        return 2;
    }

    @Override
    public boolean isCompatible(DalvInsn insn) {
        RegisterSpec reg;
        if (!(insn instanceof CstInsn)) {
            return false;
        }
        RegisterSpecList regs = insn.getRegisters();
        switch (regs.size()) {
            case 1: {
                reg = regs.get(0);
                break;
            }
            case 2: {
                reg = regs.get(0);
                if (reg.getReg() == regs.get(1).getReg()) break;
                return false;
            }
            default: {
                return false;
            }
        }
        if (!Form21c.unsignedFitsInByte(reg.getReg())) {
            return false;
        }
        CstInsn ci = (CstInsn)insn;
        int cpi = ci.getIndex();
        Constant cst = ci.getConstant();
        if (!Form21c.unsignedFitsInShort(cpi)) {
            return false;
        }
        return cst instanceof CstType || cst instanceof CstFieldRef || cst instanceof CstString || cst instanceof CstMethodHandle || cst instanceof CstProtoRef;
    }

    @Override
    public BitSet compatibleRegs(DalvInsn insn) {
        RegisterSpecList regs = insn.getRegisters();
        int sz = regs.size();
        BitSet bits = new BitSet(sz);
        boolean compat = Form21c.unsignedFitsInByte(regs.get(0).getReg());
        if (sz == 1) {
            bits.set(0, compat);
        } else if (regs.get(0).getReg() == regs.get(1).getReg()) {
            bits.set(0, compat);
            bits.set(1, compat);
        }
        return bits;
    }

    @Override
    public void writeTo(AnnotatedOutput out, DalvInsn insn) {
        RegisterSpecList regs = insn.getRegisters();
        int cpi = ((CstInsn)insn).getIndex();
        Form21c.write(out, Form21c.opcodeUnit(insn, regs.get(0).getReg()), (short)cpi);
    }
}

