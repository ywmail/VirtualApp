/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.rop.cst;

import com.android.dx.rop.cst.Constant;
import com.android.dx.rop.type.TypeBearer;

public abstract class TypedConstant
extends Constant
implements TypeBearer {
    @Override
    public final TypeBearer getFrameType() {
        return this;
    }

    @Override
    public final int getBasicType() {
        return this.getType().getBasicType();
    }

    @Override
    public final int getBasicFrameType() {
        return this.getType().getBasicFrameType();
    }

    @Override
    public final boolean isConstant() {
        return true;
    }
}

