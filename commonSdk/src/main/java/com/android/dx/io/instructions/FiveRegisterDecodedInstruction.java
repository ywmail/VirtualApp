/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.io.instructions;

import com.android.dx.io.IndexType;
import com.android.dx.io.instructions.DecodedInstruction;
import com.android.dx.io.instructions.InstructionCodec;

public final class FiveRegisterDecodedInstruction
extends DecodedInstruction {
    private final int a;
    private final int b;
    private final int c;
    private final int d;
    private final int e;

    public FiveRegisterDecodedInstruction(InstructionCodec format, int opcode, int index, IndexType indexType, int target, long literal, int a, int b, int c, int d, int e) {
        super(format, opcode, index, indexType, target, literal);
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.e = e;
    }

    @Override
    public int getRegisterCount() {
        return 5;
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
    public int getD() {
        return this.d;
    }

    @Override
    public int getE() {
        return this.e;
    }

    @Override
    public DecodedInstruction withIndex(int newIndex) {
        return new FiveRegisterDecodedInstruction(this.getFormat(), this.getOpcode(), newIndex, this.getIndexType(), this.getTarget(), this.getLiteral(), this.a, this.b, this.c, this.d, this.e);
    }
}

