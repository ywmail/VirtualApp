/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.rop.code;

import com.android.dx.rop.code.RegisterSpec;
import com.android.dx.rop.type.Type;
import com.android.dx.rop.type.TypeList;
import com.android.dx.util.FixedSizeList;
import java.util.BitSet;

public final class RegisterSpecList extends FixedSizeList implements TypeList {
    public static final RegisterSpecList EMPTY = new RegisterSpecList(0);

    public static RegisterSpecList make(RegisterSpec spec) {
        RegisterSpecList result = new RegisterSpecList(1);
        result.set(0, spec);
        return result;
    }

    public static RegisterSpecList make(RegisterSpec spec0, RegisterSpec spec1) {
        RegisterSpecList result = new RegisterSpecList(2);
        result.set(0, spec0);
        result.set(1, spec1);
        return result;
    }

    public static RegisterSpecList make(RegisterSpec spec0, RegisterSpec spec1, RegisterSpec spec2) {
        RegisterSpecList result = new RegisterSpecList(3);
        result.set(0, spec0);
        result.set(1, spec1);
        result.set(2, spec2);
        return result;
    }

    public static RegisterSpecList make(RegisterSpec spec0, RegisterSpec spec1, RegisterSpec spec2, RegisterSpec spec3) {
        RegisterSpecList result = new RegisterSpecList(4);
        result.set(0, spec0);
        result.set(1, spec1);
        result.set(2, spec2);
        result.set(3, spec3);
        return result;
    }

    public RegisterSpecList(int size) {
        super(size);
    }

    @Override
    public Type getType(int n) {
        return this.get(n).getType().getType();
    }

    @Override
    public int getWordCount() {
        int sz = this.size();
        int result = 0;
        for (int i = 0; i < sz; ++i) {
            result += this.getType(i).getCategory();
        }
        return result;
    }

    @Override
    public TypeList withAddedType(Type type) {
        throw new UnsupportedOperationException("unsupported");
    }

    public RegisterSpec get(int n) {
        return (RegisterSpec)this.get0(n);
    }

    public RegisterSpec specForRegister(int reg) {
        int sz = this.size();
        for (int i = 0; i < sz; ++i) {
            RegisterSpec rs = this.get(i);
            if (rs.getReg() != reg) continue;
            return rs;
        }
        return null;
    }

    public int indexOfRegister(int reg) {
        int sz = this.size();
        for (int i = 0; i < sz; ++i) {
            RegisterSpec rs = this.get(i);
            if (rs.getReg() != reg) continue;
            return i;
        }
        return -1;
    }

    public void set(int n, RegisterSpec spec) {
        this.set0(n, spec);
    }

    public int getRegistersSize() {
        int sz = this.size();
        int result = 0;
        for (int i = 0; i < sz; ++i) {
            int min;
            RegisterSpec spec = (RegisterSpec)this.get0(i);
            if (spec == null || (min = spec.getNextReg()) <= result) continue;
            result = min;
        }
        return result;
    }

    public RegisterSpecList withFirst(RegisterSpec spec) {
        int sz = this.size();
        RegisterSpecList result = new RegisterSpecList(sz + 1);
        for (int i = 0; i < sz; ++i) {
            result.set0(i + 1, this.get0(i));
        }
        result.set0(0, spec);
        if (this.isImmutable()) {
            result.setImmutable();
        }
        return result;
    }

    public RegisterSpecList withoutFirst() {
        int newSize = this.size() - 1;
        if (newSize == 0) {
            return EMPTY;
        }
        RegisterSpecList result = new RegisterSpecList(newSize);
        for (int i = 0; i < newSize; ++i) {
            result.set0(i, this.get0(i + 1));
        }
        if (this.isImmutable()) {
            result.setImmutable();
        }
        return result;
    }

    public RegisterSpecList withoutLast() {
        int newSize = this.size() - 1;
        if (newSize == 0) {
            return EMPTY;
        }
        RegisterSpecList result = new RegisterSpecList(newSize);
        for (int i = 0; i < newSize; ++i) {
            result.set0(i, this.get0(i));
        }
        if (this.isImmutable()) {
            result.setImmutable();
        }
        return result;
    }

    public RegisterSpecList subset(BitSet exclusionSet) {
        int newSize = this.size() - exclusionSet.cardinality();
        if (newSize == 0) {
            return EMPTY;
        }
        RegisterSpecList result = new RegisterSpecList(newSize);
        int newIndex = 0;
        for (int oldIndex = 0; oldIndex < this.size(); ++oldIndex) {
            if (exclusionSet.get(oldIndex)) continue;
            result.set0(newIndex, this.get0(oldIndex));
            ++newIndex;
        }
        if (this.isImmutable()) {
            result.setImmutable();
        }
        return result;
    }

    public RegisterSpecList withOffset(int delta) {
        int sz = this.size();
        if (sz == 0) {
            return this;
        }
        RegisterSpecList result = new RegisterSpecList(sz);
        for (int i = 0; i < sz; ++i) {
            RegisterSpec one = (RegisterSpec)this.get0(i);
            if (one == null) continue;
            result.set0(i, one.withOffset(delta));
        }
        if (this.isImmutable()) {
            result.setImmutable();
        }
        return result;
    }

    public RegisterSpecList withExpandedRegisters(int base, boolean duplicateFirst, BitSet compatRegs) {
        int sz = this.size();
        if (sz == 0) {
            return this;
        }
        Expander expander = new Expander(this, compatRegs, base, duplicateFirst);
        for (int regIdx = 0; regIdx < sz; ++regIdx) {
            expander.expandRegister(regIdx);
        }
        return expander.getResult();
    }

    private static class Expander {
        private final BitSet compatRegs;
        private final RegisterSpecList regSpecList;
        private int base;
        private final RegisterSpecList result;
        private boolean duplicateFirst;

        private Expander(RegisterSpecList regSpecList, BitSet compatRegs, int base, boolean duplicateFirst) {
            this.regSpecList = regSpecList;
            this.compatRegs = compatRegs;
            this.base = base;
            this.result = new RegisterSpecList(regSpecList.size());
            this.duplicateFirst = duplicateFirst;
        }

        private void expandRegister(int regIdx) {
            this.expandRegister(regIdx, (RegisterSpec)this.regSpecList.get0(regIdx));
        }

        private void expandRegister(int regIdx, RegisterSpec registerToExpand) {
            boolean replace = this.compatRegs == null ? true : !this.compatRegs.get(regIdx);
            RegisterSpec expandedReg;
            if (replace) {
                expandedReg = registerToExpand.withReg(this.base);
                if (!this.duplicateFirst) {
                    this.base += expandedReg.getCategory();
                }
            } else {
                expandedReg = registerToExpand;
            }

            this.duplicateFirst = false;
            this.result.set0(regIdx, expandedReg);
        }

        private RegisterSpecList getResult() {
            if (this.regSpecList.isImmutable()) {
                this.result.setImmutable();
            }

            return this.result;
        }
    }
}

