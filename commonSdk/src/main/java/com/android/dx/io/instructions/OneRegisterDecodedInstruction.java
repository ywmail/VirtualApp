/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.io.instructions;

import com.android.dx.io.IndexType;
import com.android.dx.io.instructions.DecodedInstruction;
import com.android.dx.io.instructions.InstructionCodec;

public final class OneRegisterDecodedInstruction
extends DecodedInstruction {
    private final int a;

    public OneRegisterDecodedInstruction(InstructionCodec format, int opcode, int index, IndexType indexType, int target, long literal, int a) {
        super(format, opcode, index, indexType, target, literal);
        this.a = a;
    }

    @Override
    public int getRegisterCount() {
        return 1;
    }

    @Override
    public int getA() {
        return this.a;
    }

    @Override
    public DecodedInstruction withIndex(int newIndex) {
        return new OneRegisterDecodedInstruction(this.getFormat(), this.getOpcode(), newIndex, this.getIndexType(), this.getTarget(), this.getLiteral(), this.a);
    }
}

