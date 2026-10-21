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

public final class Form22x
extends InsnFormat {
    public static final InsnFormat THE_ONE = new Form22x();

    private Form22x() {
    }

    @Override
    public String insnArgString(DalvInsn insn) {
        RegisterSpecList regs = insn.getRegisters();
        return regs.get(0).regString() + ", " + regs.get(1).regString();
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
        return insn instanceof SimpleInsn && regs.size() == 2 && Form22x.unsignedFitsInByte(regs.get(0).getReg()) && Form22x.unsignedFitsInShort(regs.get(1).getReg());
    }

    @Override
    public BitSet compatibleRegs(DalvInsn insn) {
        RegisterSpecList regs = insn.getRegisters();
        BitSet bits = new BitSet(2);
        bits.set(0, Form22x.unsignedFitsInByte(regs.get(0).getReg()));
        bits.set(1, Form22x.unsignedFitsInShort(regs.get(1).getReg()));
        return bits;
    }

    @Override
    public void writeTo(AnnotatedOutput out, DalvInsn insn) {
        RegisterSpecList regs = insn.getRegisters();
        Form22x.write(out, Form22x.opcodeUnit(insn, regs.get(0).getReg()), (short)regs.get(1).getReg());
    }
}

