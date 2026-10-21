/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.rop.code;

import com.android.dx.rop.code.Insn;
import com.android.dx.rop.code.RegisterSpec;
import com.android.dx.rop.code.RegisterSpecList;
import com.android.dx.rop.code.Rop;
import com.android.dx.rop.code.SourcePosition;
import com.android.dx.rop.type.StdTypeList;
import com.android.dx.rop.type.Type;
import com.android.dx.rop.type.TypeList;
import com.android.dx.util.IntList;

public final class SwitchInsn
extends Insn {
    private final IntList cases;

    public SwitchInsn(Rop opcode, SourcePosition position, RegisterSpec result, RegisterSpecList sources, IntList cases) {
        super(opcode, position, result, sources);
        if (opcode.getBranchingness() != 5) {
            throw new IllegalArgumentException("bogus branchingness");
        }
        if (cases == null) {
            throw new NullPointerException("cases == null");
        }
        this.cases = cases;
    }

    @Override
    public String getInlineString() {
        return this.cases.toString();
    }

    @Override
    public TypeList getCatches() {
        return StdTypeList.EMPTY;
    }

    @Override
    public void accept(Insn.Visitor visitor) {
        visitor.visitSwitchInsn(this);
    }

    @Override
    public Insn withAddedCatch(Type type) {
        throw new UnsupportedOperationException("unsupported");
    }

    @Override
    public Insn withRegisterOffset(int delta) {
        return new SwitchInsn(this.getOpcode(), this.getPosition(), this.getResult().withOffset(delta), this.getSources().withOffset(delta), this.cases);
    }

    @Override
    public boolean contentEquals(Insn b) {
        return false;
    }

    @Override
    public Insn withNewRegisters(RegisterSpec result, RegisterSpecList sources) {
        return new SwitchInsn(this.getOpcode(), this.getPosition(), result, sources, this.cases);
    }

    public IntList getCases() {
        return this.cases;
    }
}

