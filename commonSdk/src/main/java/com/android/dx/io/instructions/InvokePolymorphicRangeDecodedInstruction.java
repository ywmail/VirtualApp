/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.io.instructions;

import com.android.dx.io.IndexType;
import com.android.dx.io.instructions.DecodedInstruction;
import com.android.dx.io.instructions.InstructionCodec;

public class InvokePolymorphicRangeDecodedInstruction
extends DecodedInstruction {
    private final int c;
    private final int registerCount;
    private final int protoIndex;

    public InvokePolymorphicRangeDecodedInstruction(InstructionCodec format, int opcode, int methodIndex, IndexType indexType, int c, int registerCount, int protoIndex) {
        super(format, opcode, methodIndex, indexType, 0, 0L);
        if (protoIndex != (short)protoIndex) {
            throw new IllegalArgumentException("protoIndex doesn't fit in a short: " + protoIndex);
        }
        this.c = c;
        this.registerCount = registerCount;
        this.protoIndex = protoIndex;
    }

    @Override
    public int getRegisterCount() {
        return this.registerCount;
    }

    @Override
    public int getC() {
        return this.c;
    }

    @Override
    public DecodedInstruction withProtoIndex(int newIndex, int newProtoIndex) {
        return new InvokePolymorphicRangeDecodedInstruction(this.getFormat(), this.getOpcode(), newIndex, this.getIndexType(), this.c, this.registerCount, newProtoIndex);
    }

    @Override
    public DecodedInstruction withIndex(int newIndex) {
        throw new UnsupportedOperationException("use withProtoIndex to update both the method and proto indices for invoke-polymorphic/range");
    }

    @Override
    public short getProtoIndex() {
        return (short)this.protoIndex;
    }
}

