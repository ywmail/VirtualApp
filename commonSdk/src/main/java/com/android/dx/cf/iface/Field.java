/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.cf.iface;

import com.android.dx.cf.iface.Member;
import com.android.dx.rop.cst.TypedConstant;

public interface Field
extends Member {
    public TypedConstant getConstantValue();
}

