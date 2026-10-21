/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.rop.code;

import com.android.dx.rop.code.LocalItem;
import com.android.dx.rop.cst.Constant;
import com.android.dx.rop.cst.CstString;
import com.android.dx.rop.type.Type;
import com.android.dx.rop.type.TypeBearer;
import com.android.dx.util.ToHuman;
import java.util.concurrent.ConcurrentHashMap;

public final class RegisterSpec implements TypeBearer,
ToHuman,
Comparable<RegisterSpec> {
    public static final String PREFIX = "v";
    private static final ConcurrentHashMap<Object, RegisterSpec> theInterns = new ConcurrentHashMap(10000, 0.75f);
    private static final ThreadLocal<ForComparison> theInterningItem = new ThreadLocal<ForComparison>(){

        @Override
        protected ForComparison initialValue() {
            return new ForComparison();
        }
    };
    private final int reg;
    private final TypeBearer type;
    private final LocalItem local;

    private static RegisterSpec intern(int reg, TypeBearer type, LocalItem local) {
        RegisterSpec existing;
        ForComparison interningItem = theInterningItem.get();
        interningItem.set(reg, type, local);
        RegisterSpec found = theInterns.get(interningItem);
        if (found == null && (existing = theInterns.putIfAbsent(found = interningItem.toRegisterSpec(), found)) != null) {
            return existing;
        }
        return found;
    }

    public static RegisterSpec make(int reg, TypeBearer type) {
        return RegisterSpec.intern(reg, type, null);
    }

    public static RegisterSpec make(int reg, TypeBearer type, LocalItem local) {
        if (local == null) {
            throw new NullPointerException("local  == null");
        }
        return RegisterSpec.intern(reg, type, local);
    }

    public static RegisterSpec makeLocalOptional(int reg, TypeBearer type, LocalItem local) {
        return RegisterSpec.intern(reg, type, local);
    }

    public static String regString(int reg) {
        return PREFIX + reg;
    }

    private RegisterSpec(int reg, TypeBearer type, LocalItem local) {
        if (reg < 0) {
            throw new IllegalArgumentException("reg < 0");
        }
        if (type == null) {
            throw new NullPointerException("type == null");
        }
        this.reg = reg;
        this.type = type;
        this.local = local;
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof RegisterSpec)) {
            if (other instanceof ForComparison) {
                ForComparison fc = (ForComparison)other;
                return this.equals(fc.reg, fc.type, fc.local);
            }
            return false;
        }
        RegisterSpec spec = (RegisterSpec)other;
        return this.equals(spec.reg, spec.type, spec.local);
    }

    public boolean equalsUsingSimpleType(RegisterSpec other) {
        if (!this.matchesVariable(other)) {
            return false;
        }
        return this.reg == other.reg;
    }

    public boolean matchesVariable(RegisterSpec other) {
        if (other == null) {
            return false;
        }
        return this.type.getType().equals(other.type.getType()) && (this.local == other.local || this.local != null && this.local.equals(other.local));
    }

    private boolean equals(int reg, TypeBearer type, LocalItem local) {
        return this.reg == reg && this.type.equals(type) && (this.local == local || this.local != null && this.local.equals(local));
    }

    @Override
    public int compareTo(RegisterSpec other) {
        if (this.reg < other.reg) {
            return -1;
        }
        if (this.reg > other.reg) {
            return 1;
        }
        if (this == other) {
            return 0;
        }
        int compare = this.type.getType().compareTo(other.type.getType());
        if (compare != 0) {
            return compare;
        }
        if (this.local == null) {
            return other.local == null ? 0 : -1;
        }
        if (other.local == null) {
            return 1;
        }
        return this.local.compareTo(other.local);
    }

    public int hashCode() {
        return RegisterSpec.hashCodeOf(this.reg, this.type, this.local);
    }

    private static int hashCodeOf(int reg, TypeBearer type, LocalItem local) {
        int hash = local != null ? local.hashCode() : 0;
        hash = (hash * 31 + type.hashCode()) * 31 + reg;
        return hash;
    }

    public String toString() {
        return this.toString0(false);
    }

    @Override
    public String toHuman() {
        return this.toString0(true);
    }

    @Override
    public Type getType() {
        return this.type.getType();
    }

    @Override
    public TypeBearer getFrameType() {
        return this.type.getFrameType();
    }

    @Override
    public final int getBasicType() {
        return this.type.getBasicType();
    }

    @Override
    public final int getBasicFrameType() {
        return this.type.getBasicFrameType();
    }

    @Override
    public final boolean isConstant() {
        return false;
    }

    public int getReg() {
        return this.reg;
    }

    public TypeBearer getTypeBearer() {
        return this.type;
    }

    public LocalItem getLocalItem() {
        return this.local;
    }

    public int getNextReg() {
        return this.reg + this.getCategory();
    }

    public int getCategory() {
        return this.type.getType().getCategory();
    }

    public boolean isCategory1() {
        return this.type.getType().isCategory1();
    }

    public boolean isCategory2() {
        return this.type.getType().isCategory2();
    }

    public String regString() {
        return RegisterSpec.regString(this.reg);
    }

    public RegisterSpec intersect(RegisterSpec other, boolean localPrimary) {
        TypeBearer resultTypeBearer;
        Type otherType;
        boolean sameName;
        if (this == other) {
            return this;
        }
        if (other == null || this.reg != other.getReg()) {
            return null;
        }
        LocalItem resultLocal = this.local == null || !this.local.equals(other.getLocalItem()) ? null : this.local;
        boolean bl = sameName = resultLocal == this.local;
        if (localPrimary && !sameName) {
            return null;
        }
        Type thisType = this.getType();
        if (thisType != (otherType = other.getType())) {
            return null;
        }
        TypeBearer typeBearer = resultTypeBearer = this.type.equals(other.getTypeBearer()) ? this.type : thisType;
        if (resultTypeBearer == this.type && sameName) {
            return this;
        }
        return resultLocal == null ? RegisterSpec.make(this.reg, resultTypeBearer) : RegisterSpec.make(this.reg, resultTypeBearer, resultLocal);
    }

    public RegisterSpec withReg(int newReg) {
        if (this.reg == newReg) {
            return this;
        }
        return RegisterSpec.makeLocalOptional(newReg, this.type, this.local);
    }

    public RegisterSpec withType(TypeBearer newType) {
        return RegisterSpec.makeLocalOptional(this.reg, newType, this.local);
    }

    public RegisterSpec withOffset(int delta) {
        if (delta == 0) {
            return this;
        }
        return this.withReg(this.reg + delta);
    }

    public RegisterSpec withSimpleType() {
        TypeBearer orig = this.type;
        Type newType = orig instanceof Type ? (Type)orig : orig.getType();
        if (newType.isUninitialized()) {
            newType = newType.getInitializedType();
        }
        if (newType == orig) {
            return this;
        }
        return RegisterSpec.makeLocalOptional(this.reg, newType, this.local);
    }

    public RegisterSpec withLocalItem(LocalItem local) {
        if (this.local == local || this.local != null && this.local.equals(local)) {
            return this;
        }
        return RegisterSpec.makeLocalOptional(this.reg, this.type, local);
    }

    public boolean isEvenRegister() {
        return (this.getReg() & 1) == 0;
    }

    private String toString0(boolean human) {
        StringBuilder sb = new StringBuilder(40);
        sb.append(this.regString());
        sb.append(":");
        if (this.local != null) {
            sb.append(this.local.toString());
        }
        Type justType = this.type.getType();
        sb.append(justType);
        if (justType != this.type) {
            sb.append("=");
            if (human && this.type instanceof CstString) {
                sb.append(((CstString)this.type).toQuoted());
            } else if (human && this.type instanceof Constant) {
                sb.append(this.type.toHuman());
            } else {
                sb.append(this.type);
            }
        }
        return sb.toString();
    }

    public static void clearInternTable() {
        theInterns.clear();
    }

    private static class ForComparison {
        private int reg;
        private TypeBearer type;
        private LocalItem local;

        private ForComparison() {
        }

        public void set(int reg, TypeBearer type, LocalItem local) {
            this.reg = reg;
            this.type = type;
            this.local = local;
        }

        public RegisterSpec toRegisterSpec() {
            return new RegisterSpec(this.reg, this.type, this.local);
        }

        public boolean equals(Object other) {
            if (!(other instanceof RegisterSpec)) {
                return false;
            }
            RegisterSpec spec = (RegisterSpec)other;
            return spec.equals(this.reg, this.type, this.local);
        }

        public int hashCode() {
            return RegisterSpec.hashCodeOf(this.reg, this.type, this.local);
        }
    }
}

