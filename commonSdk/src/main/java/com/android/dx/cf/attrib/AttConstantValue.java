/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.cf.attrib;

import com.android.dx.cf.attrib.BaseAttribute;
import com.android.dx.rop.cst.CstDouble;
import com.android.dx.rop.cst.CstFloat;
import com.android.dx.rop.cst.CstInteger;
import com.android.dx.rop.cst.CstLong;
import com.android.dx.rop.cst.CstString;
import com.android.dx.rop.cst.TypedConstant;

public final class AttConstantValue
extends BaseAttribute {
    public static final String ATTRIBUTE_NAME = "ConstantValue";
    private final TypedConstant constantValue;

    public AttConstantValue(TypedConstant constantValue) {
        super(ATTRIBUTE_NAME);
        if (!(constantValue instanceof CstString || constantValue instanceof CstInteger || constantValue instanceof CstLong || constantValue instanceof CstFloat || constantValue instanceof CstDouble)) {
            if (constantValue == null) {
                throw new NullPointerException("constantValue == null");
            }
            throw new IllegalArgumentException("bad type for constantValue");
        }
        this.constantValue = constantValue;
    }

    @Override
    public int byteLength() {
        return 8;
    }

    public TypedConstant getConstantValue() {
        return this.constantValue;
    }
}

