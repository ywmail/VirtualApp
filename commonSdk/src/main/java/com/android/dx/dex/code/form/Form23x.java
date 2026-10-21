/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.dex.code.form;

import com.android.dx.dex.code.DalvInsn;
import com.android.dx.dex.code.InsnFormat;
import com.android.dx.dex.code.SimpleInsn;
import com.android.dx.rop.code.RegisterSpecList;
import com.android.dx.util.AnnotatedOutput;
import java.util.BitSet;

public final class Form23x
extends InsnFormat {
    public static final InsnFormat THE_ONE = new Form23x();

    private Form23x() {
    }

    @Override
    public String insnArgString(DalvInsn insn) {
        RegisterSpecList regs = insn.getRegisters();
        return regs.get(0).regString() + ", " + regs.get(1).regString() + ", " + regs.get(2).regString();
    }

    @Override
    public String insnCommentString(DalvInsn insn, boolean noteIndices) {
        return "";
    }

    @Override
    public int codeSize() {
        return 2;
    }

    @Override
    public boolean isCompatible(DalvInsn insn) {
        RegisterSpecList regs = insn.getRegisters();
        return insn instanceof SimpleInsn && regs.size() == 3 && Form23x.unsignedFitsInByte(regs.get(0).getReg()) && Form23x.unsignedFitsInByte(regs.get(1).getReg()) && Form23x.unsignedFitsInByte(regs.get(2).getReg());
    }

    @Override
    public BitSet compatibleRegs(DalvInsn insn) {
        RegisterSpecList regs = insn.getRegisters();
        BitSet bits = new BitSet(3);
        bits.set(0, Form23x.unsignedFitsInByte(regs.get(0).getReg()));
        bits.set(1, Form23x.unsignedFitsInByte(regs.get(1).getReg()));
        bits.set(2, Form23x.unsignedFitsInByte(regs.get(2).getReg()));
        return bits;
    }

    @Override
    public void writeTo(AnnotatedOutput out, DalvInsn insn) {
        RegisterSpecList regs = insn.getRegisters();
        Form23x.write(out, Form23x.opcodeUnit(insn, regs.get(0).getReg()), Form23x.codeUnit(regs.get(1).getReg(), regs.get(2).getReg()));
    }
}

