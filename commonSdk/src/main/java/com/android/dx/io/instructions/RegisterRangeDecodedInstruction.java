/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.io.instructions;

import com.android.dx.io.IndexType;
import com.android.dx.io.instructions.DecodedInstruction;
import com.android.dx.io.instructions.InstructionCodec;

public final class RegisterRangeDecodedInstruction
extends DecodedInstruction {
    private final int a;
    private final int registerCount;

    public RegisterRangeDecodedInstruction(InstructionCodec format, int opcode, int index, IndexType indexType, int target, long literal, int a, int registerCount) {
        super(format, opcode, index, indexType, target, literal);
        this.a = a;
        this.registerCount = registerCount;
    }

    @Override
    public int getRegisterCount() {
        return this.registerCount;
    }

    @Override
    public int getA() {
        return this.a;
    }

    @Override
    public DecodedInstruction withIndex(int newIndex) {
        return new RegisterRangeDecodedInstruction(this.getFormat(), this.getOpcode(), newIndex, this.getIndexType(), this.getTarget(), this.getLiteral(), this.a, this.registerCount);
    }
}

