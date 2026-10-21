/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.rop.code;

import com.android.dx.rop.code.Insn;
import com.android.dx.rop.code.PlainCstInsn;
import com.android.dx.rop.code.RegisterSpec;
import com.android.dx.rop.code.RegisterSpecList;
import com.android.dx.rop.code.Rop;
import com.android.dx.rop.code.Rops;
import com.android.dx.rop.code.SourcePosition;
import com.android.dx.rop.cst.Constant;
import com.android.dx.rop.cst.CstInteger;
import com.android.dx.rop.type.StdTypeList;
import com.android.dx.rop.type.Type;
import com.android.dx.rop.type.TypeBearer;
import com.android.dx.rop.type.TypeList;

public final class PlainInsn
extends Insn {
    public PlainInsn(Rop opcode, SourcePosition position, RegisterSpec result, RegisterSpecList sources) {
        super(opcode, position, result, sources);
        switch (opcode.getBranchingness()) {
            case 5: 
            case 6: {
                throw new IllegalArgumentException("opcode with invalid branchingness: " + opcode.getBranchingness());
            }
        }
        if (result != null && opcode.getBranchingness() != 1) {
            throw new IllegalArgumentException("can't mix branchingness with result");
        }
    }

    public PlainInsn(Rop opcode, SourcePosition position, RegisterSpec result, RegisterSpec source) {
        this(opcode, position, result, RegisterSpecList.make(source));
    }

    @Override
    public TypeList getCatches() {
        return StdTypeList.EMPTY;
    }

    @Override
    public void accept(Insn.Visitor visitor) {
        visitor.visitPlainInsn(this);
    }

    @Override
    public Insn withAddedCatch(Type type) {
        throw new UnsupportedOperationException("unsupported");
    }

    @Override
    public Insn withRegisterOffset(int delta) {
        return new PlainInsn(this.getOpcode(), this.getPosition(), this.getResult().withOffset(delta), this.getSources().withOffset(delta));
    }

    @Override
    public Insn withSourceLiteral() {
        Rop newRop;
        RegisterSpecList sources = this.getSources();
        int szSources = sources.size();
        if (szSources == 0) {
            return this;
        }
        TypeBearer lastType = sources.get(szSources - 1).getTypeBearer();
        if (!lastType.isConstant()) {
            TypeBearer firstType = sources.get(0).getTypeBearer();
            if (szSources == 2 && firstType.isConstant()) {
                Constant cst = (Constant)((Object)firstType);
                RegisterSpecList newSources = sources.withoutFirst();
                Rop newRop2 = Rops.ropFor(this.getOpcode().getOpcode(), this.getResult(), newSources, cst);
                return new PlainCstInsn(newRop2, this.getPosition(), this.getResult(), newSources, cst);
            }
            return this;
        }
        Constant cst = (Constant)((Object)lastType);
        RegisterSpecList newSources = sources.withoutLast();
        try {
            int opcode = this.getOpcode().getOpcode();
            if (opcode == 15 && cst instanceof CstInteger) {
                opcode = 14;
                cst = CstInteger.make(-((CstInteger)cst).getValue());
            }
            newRop = Rops.ropFor(opcode, this.getResult(), newSources, cst);
        }
        catch (IllegalArgumentException ex) {
            return this;
        }
        return new PlainCstInsn(newRop, this.getPosition(), this.getResult(), newSources, cst);
    }

    @Override
    public Insn withNewRegisters(RegisterSpec result, RegisterSpecList sources) {
        return new PlainInsn(this.getOpcode(), this.getPosition(), result, sources);
    }
}

