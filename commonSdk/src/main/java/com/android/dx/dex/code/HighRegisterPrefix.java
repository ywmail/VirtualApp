/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.dex.code;

import com.android.dx.dex.code.DalvInsn;
import com.android.dx.dex.code.SimpleInsn;
import com.android.dx.dex.code.VariableSizeInsn;
import com.android.dx.rop.code.RegisterSpec;
import com.android.dx.rop.code.RegisterSpecList;
import com.android.dx.rop.code.SourcePosition;
import com.android.dx.util.AnnotatedOutput;

public final class HighRegisterPrefix
extends VariableSizeInsn {
    private SimpleInsn[] insns;

    public HighRegisterPrefix(SourcePosition position, RegisterSpecList registers) {
        super(position, registers);
        if (registers.size() == 0) {
            throw new IllegalArgumentException("registers.size() == 0");
        }
        this.insns = null;
    }

    @Override
    public int codeSize() {
        int result = 0;
        this.calculateInsnsIfNecessary();
        for (SimpleInsn insn : this.insns) {
            result += insn.codeSize();
        }
        return result;
    }

    @Override
    public void writeTo(AnnotatedOutput out) {
        this.calculateInsnsIfNecessary();
        for (SimpleInsn insn : this.insns) {
            insn.writeTo(out);
        }
    }

    private void calculateInsnsIfNecessary() {
        if (this.insns != null) {
            return;
        }
        RegisterSpecList registers = this.getRegisters();
        int sz = registers.size();
        this.insns = new SimpleInsn[sz];
        int outAt = 0;
        for (int i = 0; i < sz; ++i) {
            RegisterSpec src = registers.get(i);
            this.insns[i] = HighRegisterPrefix.moveInsnFor(src, outAt);
            outAt += src.getCategory();
        }
    }

    @Override
    public DalvInsn withRegisters(RegisterSpecList registers) {
        return new HighRegisterPrefix(this.getPosition(), registers);
    }

    @Override
    protected String argString() {
        return null;
    }

    @Override
    protected String listingString0(boolean noteIndices) {
        RegisterSpecList registers = this.getRegisters();
        int sz = registers.size();
        StringBuilder sb = new StringBuilder(100);
        int outAt = 0;
        for (int i = 0; i < sz; ++i) {
            RegisterSpec src = registers.get(i);
            SimpleInsn insn = HighRegisterPrefix.moveInsnFor(src, outAt);
            if (i != 0) {
                sb.append('\n');
            }
            sb.append(insn.listingString0(noteIndices));
            outAt += src.getCategory();
        }
        return sb.toString();
    }

    private static SimpleInsn moveInsnFor(RegisterSpec src, int destIndex) {
        return DalvInsn.makeMove(SourcePosition.NO_INFO, RegisterSpec.make(destIndex, src.getType()), src);
    }
}

