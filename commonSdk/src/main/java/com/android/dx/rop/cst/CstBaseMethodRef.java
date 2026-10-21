/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.rop.cst;

import com.android.dx.rop.cst.Constant;
import com.android.dx.rop.cst.CstMemberRef;
import com.android.dx.rop.cst.CstNat;
import com.android.dx.rop.cst.CstType;
import com.android.dx.rop.type.Prototype;
import com.android.dx.rop.type.Type;

public abstract class CstBaseMethodRef
extends CstMemberRef {
    private final Prototype prototype;
    private Prototype instancePrototype;

    CstBaseMethodRef(CstType definingClass, CstNat nat) {
        super(definingClass, nat);
        String descriptor = this.getNat().getDescriptor().getString();
        this.prototype = this.isSignaturePolymorphic() ? Prototype.fromDescriptor(descriptor) : Prototype.intern(descriptor);
        this.instancePrototype = null;
    }

    public final Prototype getPrototype() {
        return this.prototype;
    }

    public final Prototype getPrototype(boolean isStatic) {
        if (isStatic) {
            return this.prototype;
        }
        if (this.instancePrototype == null) {
            Type thisType = this.getDefiningClass().getClassType();
            this.instancePrototype = this.prototype.withFirstParameter(thisType);
        }
        return this.instancePrototype;
    }

    @Override
    protected final int compareTo0(Constant other) {
        int cmp = super.compareTo0(other);
        if (cmp != 0) {
            return cmp;
        }
        CstBaseMethodRef otherMethod = (CstBaseMethodRef)other;
        return this.prototype.compareTo(otherMethod.prototype);
    }

    @Override
    public final Type getType() {
        return this.prototype.getReturnType();
    }

    public final int getParameterWordCount(boolean isStatic) {
        return this.getPrototype(isStatic).getParameterTypes().getWordCount();
    }

    public final boolean isInstanceInit() {
        return this.getNat().isInstanceInit();
    }

    public final boolean isClassInit() {
        return this.getNat().isClassInit();
    }

    public final boolean isSignaturePolymorphic() {
        CstType definingClass = this.getDefiningClass();
        if (definingClass.equals(CstType.METHOD_HANDLE)) {
            switch (this.getNat().getName().getString()) {
                case "invoke": 
                case "invokeExact": {
                    return true;
                }
            }
        } else if (definingClass.equals(CstType.VAR_HANDLE)) {
            switch (this.getNat().getName().getString()) {
                case "compareAndExchange": 
                case "compareAndExchangeAcquire": 
                case "compareAndExchangeRelease": 
                case "compareAndSet": 
                case "get": 
                case "getAcquire": 
                case "getAndAdd": 
                case "getAndAddAcquire": 
                case "getAndAddRelease": 
                case "getAndBitwiseAnd": 
                case "getAndBitwiseAndAcquire": 
                case "getAndBitwiseAndRelease": 
                case "getAndBitwiseOr": 
                case "getAndBitwiseOrAcquire": 
                case "getAndBitwiseOrRelease": 
                case "getAndBitwiseXor": 
                case "getAndBitwiseXorAcquire": 
                case "getAndBitwiseXorRelease": 
                case "getAndSet": 
                case "getAndSetAcquire": 
                case "getAndSetRelease": 
                case "getOpaque": 
                case "getVolatile": 
                case "set": 
                case "setOpaque": 
                case "setRelease": 
                case "setVolatile": 
                case "weakCompareAndSet": 
                case "weakCompareAndSetAcquire": 
                case "weakCompareAndSetPlain": 
                case "weakCompareAndSetRelease": {
                    return true;
                }
            }
        }
        return false;
    }
}

