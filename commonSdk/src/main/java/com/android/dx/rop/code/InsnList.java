/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.rop.code;

import com.android.dx.rop.code.Insn;
import com.android.dx.util.FixedSizeList;

public final class InsnList
extends FixedSizeList {
    public InsnList(int size) {
        super(size);
    }

    public Insn get(int n) {
        return (Insn)this.get0(n);
    }

    public void set(int n, Insn insn) {
        this.set0(n, insn);
    }

    public Insn getLast() {
        return this.get(this.size() - 1);
    }

    public void forEach(Insn.Visitor visitor) {
        int sz = this.size();
        for (int i = 0; i < sz; ++i) {
            this.get(i).accept(visitor);
        }
    }

    public boolean contentEquals(InsnList b) {
        if (b == null) {
            return false;
        }
        int sz = this.size();
        if (sz != b.size()) {
            return false;
        }
        for (int i = 0; i < sz; ++i) {
            if (this.get(i).contentEquals(b.get(i))) continue;
            return false;
        }
        return true;
    }

    public InsnList withRegisterOffset(int delta) {
        int sz = this.size();
        InsnList result = new InsnList(sz);
        for (int i = 0; i < sz; ++i) {
            Insn one = (Insn)this.get0(i);
            if (one == null) continue;
            result.set0(i, one.withRegisterOffset(delta));
        }
        if (this.isImmutable()) {
            result.setImmutable();
        }
        return result;
    }
}

