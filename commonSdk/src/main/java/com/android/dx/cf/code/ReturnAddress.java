/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.cf.code;

import com.android.dx.rop.type.Type;
import com.android.dx.rop.type.TypeBearer;
import com.android.dx.util.Hex;

public final class ReturnAddress
implements TypeBearer {
    private final int subroutineAddress;

    public ReturnAddress(int subroutineAddress) {
        if (subroutineAddress < 0) {
            throw new IllegalArgumentException("subroutineAddress < 0");
        }
        this.subroutineAddress = subroutineAddress;
    }

    public String toString() {
        return "<addr:" + Hex.u2(this.subroutineAddress) + ">";
    }

    @Override
    public String toHuman() {
        return this.toString();
    }

    @Override
    public Type getType() {
        return Type.RETURN_ADDRESS;
    }

    @Override
    public TypeBearer getFrameType() {
        return this;
    }

    @Override
    public int getBasicType() {
        return Type.RETURN_ADDRESS.getBasicType();
    }

    @Override
    public int getBasicFrameType() {
        return Type.RETURN_ADDRESS.getBasicFrameType();
    }

    @Override
    public boolean isConstant() {
        return false;
    }

    public boolean equals(Object other) {
        if (!(other instanceof ReturnAddress)) {
            return false;
        }
        return this.subroutineAddress == ((ReturnAddress)other).subroutineAddress;
    }

    public int hashCode() {
        return this.subroutineAddress;
    }

    public int getSubroutineAddress() {
        return this.subroutineAddress;
    }
}

