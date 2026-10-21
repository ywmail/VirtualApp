/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.io.instructions;

import com.android.dx.io.instructions.DecodedInstruction;
import com.android.dx.io.instructions.InstructionCodec;

public final class SparseSwitchPayloadDecodedInstruction
extends DecodedInstruction {
    private final int[] keys;
    private final int[] targets;

    public SparseSwitchPayloadDecodedInstruction(InstructionCodec format, int opcode, int[] keys, int[] targets) {
        super(format, opcode, 0, null, 0, 0L);
        if (keys.length != targets.length) {
            throw new IllegalArgumentException("keys/targets length mismatch");
        }
        this.keys = keys;
        this.targets = targets;
    }

    @Override
    public int getRegisterCount() {
        return 0;
    }

    public int[] getKeys() {
        return this.keys;
    }

    public int[] getTargets() {
        return this.targets;
    }

    @Override
    public DecodedInstruction withIndex(int newIndex) {
        throw new UnsupportedOperationException("no index in instruction");
    }
}

