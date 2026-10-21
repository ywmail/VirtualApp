/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.rop.code;

import com.android.dx.rop.code.Insn;
import com.android.dx.rop.code.RegisterSpec;
import com.android.dx.rop.code.RegisterSpecList;
import com.android.dx.rop.code.Rop;
import com.android.dx.rop.code.SourcePosition;
import com.android.dx.rop.code.ThrowingInsn;
import com.android.dx.rop.cst.CstMethodRef;
import com.android.dx.rop.cst.CstNat;
import com.android.dx.rop.cst.CstProtoRef;
import com.android.dx.rop.cst.CstString;
import com.android.dx.rop.cst.CstType;
import com.android.dx.rop.type.Type;
import com.android.dx.rop.type.TypeList;

public class InvokePolymorphicInsn
extends Insn {
    private static final CstString DEFAULT_DESCRIPTOR = new CstString("([Ljava/lang/Object;)Ljava/lang/Object;");
    private static final CstString VARHANDLE_SET_DESCRIPTOR = new CstString("([Ljava/lang/Object;)V");
    private static final CstString VARHANDLE_COMPARE_AND_SET_DESCRIPTOR = new CstString("([Ljava/lang/Object;)Z");
    private final TypeList catches;
    private final CstMethodRef callSiteMethod;
    private final CstMethodRef polymorphicMethod;
    private final CstProtoRef callSiteProto;

    public InvokePolymorphicInsn(Rop opcode, SourcePosition position, RegisterSpecList sources, TypeList catches, CstMethodRef callSiteMethod) {
        super(opcode, position, null, sources);
        if (opcode.getBranchingness() != 6) {
            throw new IllegalArgumentException("opcode with invalid branchingness: " + opcode.getBranchingness());
        }
        if (catches == null) {
            throw new NullPointerException("catches == null");
        }
        this.catches = catches;
        if (callSiteMethod == null) {
            throw new NullPointerException("callSiteMethod == null");
        }
        if (!callSiteMethod.isSignaturePolymorphic()) {
            throw new IllegalArgumentException("callSiteMethod is not signature polymorphic");
        }
        this.callSiteMethod = callSiteMethod;
        this.polymorphicMethod = InvokePolymorphicInsn.makePolymorphicMethod(callSiteMethod);
        this.callSiteProto = InvokePolymorphicInsn.makeCallSiteProto(callSiteMethod);
    }

    @Override
    public TypeList getCatches() {
        return this.catches;
    }

    @Override
    public void accept(Insn.Visitor visitor) {
        visitor.visitInvokePolymorphicInsn(this);
    }

    @Override
    public Insn withAddedCatch(Type type) {
        return new InvokePolymorphicInsn(this.getOpcode(), this.getPosition(), this.getSources(), this.catches.withAddedType(type), this.getCallSiteMethod());
    }

    @Override
    public Insn withRegisterOffset(int delta) {
        return new InvokePolymorphicInsn(this.getOpcode(), this.getPosition(), this.getSources().withOffset(delta), this.catches, this.getCallSiteMethod());
    }

    @Override
    public Insn withNewRegisters(RegisterSpec result, RegisterSpecList sources) {
        return new InvokePolymorphicInsn(this.getOpcode(), this.getPosition(), sources, this.catches, this.getCallSiteMethod());
    }

    public CstMethodRef getCallSiteMethod() {
        return this.callSiteMethod;
    }

    public CstMethodRef getPolymorphicMethod() {
        return this.polymorphicMethod;
    }

    public CstProtoRef getCallSiteProto() {
        return this.callSiteProto;
    }

    @Override
    public String getInlineString() {
        return this.getPolymorphicMethod().toString() + " " + this.getCallSiteProto().toString() + " " + ThrowingInsn.toCatchString(this.catches);
    }

    private static CstMethodRef makePolymorphicMethod(CstMethodRef callSiteMethod) {
        CstType definingClass = callSiteMethod.getDefiningClass();
        CstString cstMethodName = callSiteMethod.getNat().getName();
        String methodName = callSiteMethod.getNat().getName().getString();
        if (definingClass.equals(CstType.METHOD_HANDLE) && (methodName.equals("invoke") || methodName.equals("invokeExact"))) {
            CstNat cstNat = new CstNat(cstMethodName, DEFAULT_DESCRIPTOR);
            return new CstMethodRef(definingClass, cstNat);
        }
        if (definingClass.equals(CstType.VAR_HANDLE)) {
            switch (methodName) {
                case "compareAndExchange": 
                case "compareAndExchangeAcquire": 
                case "compareAndExchangeRelease": 
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
                case "getVolatile": {
                    CstNat cstNat = new CstNat(cstMethodName, DEFAULT_DESCRIPTOR);
                    return new CstMethodRef(definingClass, cstNat);
                }
                case "set": 
                case "setOpaque": 
                case "setRelease": 
                case "setVolatile": {
                    CstNat cstNat = new CstNat(cstMethodName, VARHANDLE_SET_DESCRIPTOR);
                    return new CstMethodRef(definingClass, cstNat);
                }
                case "compareAndSet": 
                case "weakCompareAndSet": 
                case "weakCompareAndSetAcquire": 
                case "weakCompareAndSetPlain": 
                case "weakCompareAndSetRelease": {
                    CstNat cstNat = new CstNat(cstMethodName, VARHANDLE_COMPARE_AND_SET_DESCRIPTOR);
                    return new CstMethodRef(definingClass, cstNat);
                }
            }
        }
        throw new IllegalArgumentException("Unknown signature polymorphic method: " + callSiteMethod.toHuman());
    }

    private static CstProtoRef makeCallSiteProto(CstMethodRef callSiteMethod) {
        return new CstProtoRef(callSiteMethod.getPrototype(true));
    }
}

