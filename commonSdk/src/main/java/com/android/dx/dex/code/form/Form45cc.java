/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.dex.code.form;

import com.android.dx.dex.code.DalvInsn;
import com.android.dx.dex.code.InsnFormat;
import com.android.dx.dex.code.MultiCstInsn;
import com.android.dx.rop.code.RegisterSpec;
import com.android.dx.rop.code.RegisterSpecList;
import com.android.dx.rop.cst.Constant;
import com.android.dx.rop.cst.CstMethodRef;
import com.android.dx.rop.cst.CstProtoRef;
import com.android.dx.rop.type.Type;
import com.android.dx.util.AnnotatedOutput;
import java.util.BitSet;

public final class Form45cc
extends InsnFormat {
    public static final InsnFormat THE_ONE = new Form45cc();
    private static final int MAX_NUM_OPS = 5;

    private Form45cc() {
    }

    @Override
    public String insnArgString(DalvInsn insn) {
        RegisterSpecList regs = Form45cc.explicitize(insn.getRegisters());
        return Form45cc.regListString(regs) + ", " + insn.cstString();
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
        if (mci.getNumberOfConstants() != 2) {
            return false;
        }
        int methodIdx = mci.getIndex(0);
        int protoIdx = mci.getIndex(1);
        if (!Form45cc.unsignedFitsInShort(methodIdx) || !Form45cc.unsignedFitsInShort(protoIdx)) {
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
        return Form45cc.wordCount(regs) >= 0;
    }

    @Override
    public BitSet compatibleRegs(DalvInsn insn) {
        RegisterSpecList regs = insn.getRegisters();
        int sz = regs.size();
        BitSet bits = new BitSet(sz);
        for (int i = 0; i < sz; ++i) {
            RegisterSpec reg = regs.get(i);
            bits.set(i, Form45cc.unsignedFitsInNibble(reg.getReg() + reg.getCategory() - 1));
        }
        return bits;
    }

    @Override
    public void writeTo(AnnotatedOutput out, DalvInsn insn) {
        MultiCstInsn mci = (MultiCstInsn)insn;
        short regB = (short)mci.getIndex(0);
        short regH = (short)mci.getIndex(1);
        RegisterSpecList regs = Form45cc.explicitize(insn.getRegisters());
        int regA = regs.size();
        int regC = regA > 0 ? regs.get(0).getReg() : 0;
        int regD = regA > 1 ? regs.get(1).getReg() : 0;
        int regE = regA > 2 ? regs.get(2).getReg() : 0;
        int regF = regA > 3 ? regs.get(3).getReg() : 0;
        int regG = regA > 4 ? regs.get(4).getReg() : 0;
        Form45cc.write(out, Form45cc.opcodeUnit(insn, Form45cc.makeByte(regG, regA)), regB, Form45cc.codeUnit(regC, regD, regE, regF), regH);
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
            if (Form45cc.unsignedFitsInNibble(one.getReg() + one.getCategory() - 1)) continue;
            return -1;
        }
        return result <= 5 ? result : -1;
    }

    private static RegisterSpecList explicitize(RegisterSpecList orig) {
        int sz;
        int wordCount = Form45cc.wordCount(orig);
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

