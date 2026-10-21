/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.rop.code;

import com.android.dx.rop.code.Insn;
import com.android.dx.rop.code.RegisterSpec;
import com.android.dx.rop.code.RegisterSpecList;
import com.android.dx.rop.code.Rop;
import com.android.dx.rop.code.SourcePosition;
import com.android.dx.rop.cst.Constant;
import com.android.dx.rop.type.StdTypeList;
import com.android.dx.rop.type.Type;
import com.android.dx.rop.type.TypeList;
import java.util.ArrayList;

public final class FillArrayDataInsn
extends Insn {
    private final ArrayList<Constant> initValues;
    private final Constant arrayType;

    public FillArrayDataInsn(Rop opcode, SourcePosition position, RegisterSpecList sources, ArrayList<Constant> initValues, Constant cst) {
        super(opcode, position, null, sources);
        if (opcode.getBranchingness() != 1) {
            throw new IllegalArgumentException("opcode with invalid branchingness: " + opcode.getBranchingness());
        }
        this.initValues = initValues;
        this.arrayType = cst;
    }

    @Override
    public TypeList getCatches() {
        return StdTypeList.EMPTY;
    }

    public ArrayList<Constant> getInitValues() {
        return this.initValues;
    }

    public Constant getConstant() {
        return this.arrayType;
    }

    @Override
    public void accept(Insn.Visitor visitor) {
        visitor.visitFillArrayDataInsn(this);
    }

    @Override
    public Insn withAddedCatch(Type type) {
        throw new UnsupportedOperationException("unsupported");
    }

    @Override
    public Insn withRegisterOffset(int delta) {
        return new FillArrayDataInsn(this.getOpcode(), this.getPosition(), this.getSources().withOffset(delta), this.initValues, this.arrayType);
    }

    @Override
    public Insn withNewRegisters(RegisterSpec result, RegisterSpecList sources) {
        return new FillArrayDataInsn(this.getOpcode(), this.getPosition(), sources, this.initValues, this.arrayType);
    }
}

