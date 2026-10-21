/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.dex.code.form;

import com.android.dx.dex.code.DalvInsn;
import com.android.dx.dex.code.InsnFormat;
import com.android.dx.dex.code.MultiCstInsn;
import com.android.dx.rop.code.RegisterSpecList;
import com.android.dx.rop.cst.Constant;
import com.android.dx.rop.cst.CstMethodRef;
import com.android.dx.rop.cst.CstProtoRef;
import com.android.dx.util.AnnotatedOutput;

public final class Form4rcc
extends InsnFormat {
    public static final InsnFormat THE_ONE = new Form4rcc();

    private Form4rcc() {
    }

    @Override
    public String insnArgString(DalvInsn insn) {
        return Form4rcc.regRangeString(insn.getRegisters()) + ", " + insn.cstString();
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
        return 4;
    }

    @Override
    public boolean isCompatible(DalvInsn insn) {
        if (!(insn instanceof MultiCstInsn)) {
            return false;
        }
        MultiCstInsn mci = (MultiCstInsn)insn;
        int methodIdx = mci.getIndex(0);
        int protoIdx = mci.getIndex(1);
        if (!Form4rcc.unsignedFitsInShort(methodIdx) || !Form4rcc.unsignedFitsInShort(protoIdx)) {
            return false;
        }
        Constant methodRef = mci.getConstant(0);
        if (!(methodRef instanceof CstMethodRef)) {
            return false;
        }
        Constant protoRef = mci.getConstant(1);
        if (!(protoRef instanceof CstProtoRef)) {
            return false;
        }
        RegisterSpecList regs = mci.getRegisters();
        int sz = regs.size();
        if (sz == 0) {
            return true;
        }
        return Form4rcc.unsignedFitsInByte(regs.getWordCount()) && Form4rcc.unsignedFitsInShort(sz) && Form4rcc.unsignedFitsInShort(regs.get(0).getReg()) && Form4rcc.isRegListSequential(regs);
    }

    @Override
    public void writeTo(AnnotatedOutput out, DalvInsn insn) {
        MultiCstInsn mci = (MultiCstInsn)insn;
        short regB = (short)mci.getIndex(0);
        short regH = (short)mci.getIndex(1);
        RegisterSpecList regs = insn.getRegisters();
        short regC = 0;
        if (regs.size() > 0) {
            regC = (short)regs.get(0).getReg();
        }
        int regA = regs.getWordCount();
        Form4rcc.write(out, Form4rcc.opcodeUnit(insn, regA), regB, regC, regH);
    }
}

