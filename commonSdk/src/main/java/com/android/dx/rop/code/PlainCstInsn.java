/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.rop.code;

import com.android.dx.rop.code.CstInsn;
import com.android.dx.rop.code.Insn;
import com.android.dx.rop.code.RegisterSpec;
import com.android.dx.rop.code.RegisterSpecList;
import com.android.dx.rop.code.Rop;
import com.android.dx.rop.code.SourcePosition;
import com.android.dx.rop.cst.Constant;
import com.android.dx.rop.type.StdTypeList;
import com.android.dx.rop.type.Type;
import com.android.dx.rop.type.TypeList;

public final class PlainCstInsn
extends CstInsn {
    public PlainCstInsn(Rop opcode, SourcePosition position, RegisterSpec result, RegisterSpecList sources, Constant cst) {
        super(opcode, position, result, sources, cst);
        if (opcode.getBranchingness() != 1) {
            throw new IllegalArgumentException("opcode with invalid branchingness: " + opcode.getBranchingness());
        }
    }

    @Override
    public TypeList getCatches() {
        return StdTypeList.EMPTY;
    }

    @Override
    public void accept(Insn.Visitor visitor) {
        visitor.visitPlainCstInsn(this);
    }

    @Override
    public Insn withAddedCatch(Type type) {
        throw new UnsupportedOperationException("unsupported");
    }

    @Override
    public Insn withRegisterOffset(int delta) {
        return new PlainCstInsn(this.getOpcode(), this.getPosition(), this.getResult().withOffset(delta), this.getSources().withOffset(delta), this.getConstant());
    }

    @Override
    public Insn withNewRegisters(RegisterSpec result, RegisterSpecList sources) {
        return new PlainCstInsn(this.getOpcode(), this.getPosition(), result, sources, this.getConstant());
    }
}

