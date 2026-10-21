/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx;

import com.android.dx.Code;
import com.android.dx.TypeId;
import com.android.dx.rop.code.RegisterSpec;

public final class Local<T> {
    private final Code code;
    final TypeId<T> type;
    private int reg = -1;
    private RegisterSpec spec;

    private Local(Code code, TypeId<T> type) {
        this.code = code;
        this.type = type;
    }

    static <T> Local<T> get(Code code, TypeId<T> type) {
        return new Local<T>(code, type);
    }

    int initialize(int nextAvailableRegister) {
        this.reg = nextAvailableRegister;
        this.spec = RegisterSpec.make(nextAvailableRegister, this.type.ropType);
        return this.size();
    }

    int size() {
        return this.type.ropType.getCategory();
    }

    RegisterSpec spec() {
        if (this.spec == null) {
            this.code.initializeLocals();
            if (this.spec == null) {
                throw new AssertionError();
            }
        }
        return this.spec;
    }

    public TypeId getType() {
        return this.type;
    }

    public String toString() {
        return "v" + this.reg + "(" + this.type + ")";
    }
}

