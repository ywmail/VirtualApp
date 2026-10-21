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

public abstract class CstInsn
extends Insn {
    private final Constant cst;

    public CstInsn(Rop opcode, SourcePosition position, RegisterSpec result, RegisterSpecList sources, Constant cst) {
        super(opcode, position, result, sources);
        if (cst == null) {
            throw new NullPointerException("cst == null");
        }
        this.cst = cst;
    }

    @Override
    public String getInlineString() {
        return this.cst.toHuman();
    }

    public Constant getConstant() {
        return this.cst;
    }

    @Override
    public boolean contentEquals(Insn b) {
        return super.contentEquals(b) && this.cst.equals(((CstInsn)b).getConstant());
    }
}

