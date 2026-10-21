/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.io.instructions;

import com.android.dx.io.IndexType;
import com.android.dx.io.instructions.DecodedInstruction;
import com.android.dx.io.instructions.InstructionCodec;

public final class ZeroRegisterDecodedInstruction
extends DecodedInstruction {
    public ZeroRegisterDecodedInstruction(InstructionCodec format, int opcode, int index, IndexType indexType, int target, long literal) {
        super(format, opcode, index, indexType, target, literal);
    }

    @Override
    public int getRegisterCount() {
        return 0;
    }

    @Override
    public DecodedInstruction withIndex(int newIndex) {
        return new ZeroRegisterDecodedInstruction(this.getFormat(), this.getOpcode(), newIndex, this.getIndexType(), this.getTarget(), this.getLiteral());
    }
}

