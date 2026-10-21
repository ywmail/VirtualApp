/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.dex.code.form;

import com.android.dx.dex.code.CstInsn;
import com.android.dx.dex.code.DalvInsn;
import com.android.dx.dex.code.InsnFormat;
import com.android.dx.rop.code.RegisterSpecList;
import com.android.dx.rop.cst.Constant;
import com.android.dx.rop.cst.CstCallSiteRef;
import com.android.dx.rop.cst.CstMethodRef;
import com.android.dx.rop.cst.CstType;
import com.android.dx.util.AnnotatedOutput;

public final class Form3rc
extends InsnFormat {
    public static final InsnFormat THE_ONE = new Form3rc();

    private Form3rc() {
    }

    @Override
    public String insnArgString(DalvInsn insn) {
        return Form3rc.regRangeString(insn.getRegisters()) + ", " + insn.cstString();
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
        Constant cst = ci.getConstant();
        if (!Form3rc.unsignedFitsInShort(cpi)) {
            return false;
        }
        if (!(cst instanceof CstMethodRef || cst instanceof CstType || cst instanceof CstCallSiteRef)) {
            return false;
        }
        RegisterSpecList regs = ci.getRegisters();
        int sz = regs.size();
        return regs.size() == 0 || Form3rc.isRegListSequential(regs) && Form3rc.unsignedFitsInShort(regs.get(0).getReg()) && Form3rc.unsignedFitsInByte(regs.getWordCount());
    }

    @Override
    public void writeTo(AnnotatedOutput out, DalvInsn insn) {
        RegisterSpecList regs = insn.getRegisters();
        int cpi = ((CstInsn)insn).getIndex();
        int firstReg = regs.size() == 0 ? 0 : regs.get(0).getReg();
        int count = regs.getWordCount();
        Form3rc.write(out, Form3rc.opcodeUnit(insn, count), (short)cpi, (short)firstReg);
    }
}

