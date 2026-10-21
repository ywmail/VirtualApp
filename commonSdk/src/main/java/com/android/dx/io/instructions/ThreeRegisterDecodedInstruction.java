/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.io.instructions;

import com.android.dx.io.IndexType;
import com.android.dx.io.instructions.DecodedInstruction;
import com.android.dx.io.instructions.InstructionCodec;

public final class ThreeRegisterDecodedInstruction
extends DecodedInstruction {
    private final int a;
    private final int b;
    private final int c;

    public ThreeRegisterDecodedInstruction(InstructionCodec format, int opcode, int index, IndexType indexType, int target, long literal, int a, int b, int c) {
        super(format, opcode, index, indexType, target, literal);
        this.a = a;
        this.b = b;
        this.c = c;
    }

    @Override
    public int getRegisterCount() {
        return 3;
    }

    @Override
    public int getA() {
        return this.a;
    }

    @Override
    public int getB() {
        return this.b;
    }

    @Override
    public int getC() {
        return this.c;
    }

    @Override
    public DecodedInstruction withIndex(int newIndex) {
        return new ThreeRegisterDecodedInstruction(this.getFormat(), this.getOpcode(), newIndex, this.getIndexType(), this.getTarget(), this.getLiteral(), this.a, this.b, this.c);
    }
}

