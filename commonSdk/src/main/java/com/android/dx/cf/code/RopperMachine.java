//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.android.dx.cf.code;

import com.android.dx.cf.iface.Method;
import com.android.dx.cf.iface.MethodList;
import com.android.dx.rop.code.AccessFlags;
import com.android.dx.rop.code.FillArrayDataInsn;
import com.android.dx.rop.code.Insn;
import com.android.dx.rop.code.InvokePolymorphicInsn;
import com.android.dx.rop.code.PlainCstInsn;
import com.android.dx.rop.code.PlainInsn;
import com.android.dx.rop.code.RegisterSpec;
import com.android.dx.rop.code.RegisterSpecList;
import com.android.dx.rop.code.Rop;
import com.android.dx.rop.code.Rops;
import com.android.dx.rop.code.SourcePosition;
import com.android.dx.rop.code.SwitchInsn;
import com.android.dx.rop.code.ThrowingCstInsn;
import com.android.dx.rop.code.ThrowingInsn;
import com.android.dx.rop.code.TranslationAdvice;
import com.android.dx.rop.cst.Constant;
import com.android.dx.rop.cst.CstCallSiteRef;
import com.android.dx.rop.cst.CstFieldRef;
import com.android.dx.rop.cst.CstInteger;
import com.android.dx.rop.cst.CstMethodRef;
import com.android.dx.rop.cst.CstNat;
import com.android.dx.rop.cst.CstString;
import com.android.dx.rop.cst.CstType;
import com.android.dx.rop.type.Type;
import com.android.dx.rop.type.TypeBearer;
import com.android.dx.rop.type.TypeList;
import com.android.dx.util.IntList;
import java.util.ArrayList;

final class RopperMachine extends ValueAwareMachine {
    private static final CstType ARRAY_REFLECT_TYPE = new CstType(Type.internClassName("java/lang/reflect/Array"));
    private static final CstMethodRef MULTIANEWARRAY_METHOD;
    private final Ropper ropper;
    private final ConcreteMethod method;
    private final MethodList methods;
    private final TranslationAdvice advice;
    private final int maxLocals;
    private final ArrayList<Insn> insns;
    private TypeList catches;
    private boolean catchesUsed;
    private boolean returns;
    private int primarySuccessorIndex;
    private int extraBlockCount;
    private boolean hasJsr;
    private boolean blockCanThrow;
    private ReturnAddress returnAddress;
    private Rop returnOp;
    private SourcePosition returnPosition;

    public RopperMachine(Ropper ropper, ConcreteMethod method, TranslationAdvice advice, MethodList methods) {
        super(method.getEffectiveDescriptor());
        if (methods == null) {
            throw new NullPointerException("methods == null");
        } else if (ropper == null) {
            throw new NullPointerException("ropper == null");
        } else if (advice == null) {
            throw new NullPointerException("advice == null");
        } else {
            this.ropper = ropper;
            this.method = method;
            this.methods = methods;
            this.advice = advice;
            this.maxLocals = method.getMaxLocals();
            this.insns = new ArrayList(25);
            this.catches = null;
            this.catchesUsed = false;
            this.returns = false;
            this.primarySuccessorIndex = -1;
            this.extraBlockCount = 0;
            this.blockCanThrow = false;
            this.returnOp = null;
            this.returnPosition = null;
        }
    }

    public ArrayList<Insn> getInsns() {
        return this.insns;
    }

    public Rop getReturnOp() {
        return this.returnOp;
    }

    public SourcePosition getReturnPosition() {
        return this.returnPosition;
    }

    public void startBlock(TypeList catches) {
        this.catches = catches;
        this.insns.clear();
        this.catchesUsed = false;
        this.returns = false;
        this.primarySuccessorIndex = 0;
        this.extraBlockCount = 0;
        this.blockCanThrow = false;
        this.hasJsr = false;
        this.returnAddress = null;
    }

    public boolean wereCatchesUsed() {
        return this.catchesUsed;
    }

    public boolean returns() {
        return this.returns;
    }

    public int getPrimarySuccessorIndex() {
        return this.primarySuccessorIndex;
    }

    public int getExtraBlockCount() {
        return this.extraBlockCount;
    }

    public boolean canThrow() {
        return this.blockCanThrow;
    }

    public boolean hasJsr() {
        return this.hasJsr;
    }

    public boolean hasRet() {
        return this.returnAddress != null;
    }

    public ReturnAddress getReturnAddress() {
        return this.returnAddress;
    }

    @Override
    public void run(Frame frame, int offset, int opcode) {
        Insn insn;
        Rop rop;
        RegisterSpec dest;
        int stackPointer = this.maxLocals + frame.getStack().size();
        RegisterSpecList sources = this.getSources(opcode, stackPointer);
        int sourceCount = sources.size();
        super.run(frame, offset, opcode);
        SourcePosition pos = this.method.makeSourcePosistion(offset);
        RegisterSpec localTarget = this.getLocalTarget(opcode == 54);
        int destCount = this.resultCount();
        if (destCount == 0) {
            dest = null;
            switch (opcode) {
                case 87:
                case 88: {
                    return;
                }
            }
        } else if (localTarget != null) {
            dest = localTarget;
        } else if (destCount == 1) {
            dest = RegisterSpec.make(stackPointer, this.result(0));
        } else {
            int scratchAt = this.ropper.getFirstTempStackReg();
            RegisterSpec[] scratchRegs = new RegisterSpec[sourceCount];
            for (int i = 0; i < sourceCount; ++i) {
                RegisterSpec src = sources.get(i);
                TypeBearer type = src.getTypeBearer();
                RegisterSpec scratch = src.withReg(scratchAt);
                this.insns.add(new PlainInsn(Rops.opMove(type), pos, scratch, src));
                scratchRegs[i] = scratch;
                scratchAt += src.getCategory();
            }
            for (int pattern = this.getAuxInt(); pattern != 0; pattern >>= 4) {
                int which = (pattern & 0xF) - 1;
                RegisterSpec scratch = scratchRegs[which];
                TypeBearer type = scratch.getTypeBearer();
                this.insns.add(new PlainInsn(Rops.opMove(type), pos, scratch.withReg(stackPointer), scratch));
                stackPointer += type.getType().getCategory();
            }
            return;
        }
        TypeBearer destType = dest != null ? dest : Type.VOID;
        Constant cst = this.getAuxCst();
        if (opcode == 197) {
            this.blockCanThrow = true;
            this.extraBlockCount = 6;
            RegisterSpec dimsReg = RegisterSpec.make(dest.getNextReg(), Type.INT_ARRAY);
            rop = Rops.opFilledNewArray(Type.INT_ARRAY, sourceCount);
            insn = new ThrowingCstInsn(rop, pos, sources, this.catches, (Constant)CstType.INT_ARRAY);
            this.insns.add(insn);
            rop = Rops.opMoveResult(Type.INT_ARRAY);
            insn = new PlainInsn(rop, pos, dimsReg, RegisterSpecList.EMPTY);
            this.insns.add(insn);
            Type componentType = ((CstType)cst).getClassType();
            for (int i = 0; i < sourceCount; ++i) {
                componentType = componentType.getComponentType();
            }
            RegisterSpec classReg = RegisterSpec.make(dest.getReg(), Type.CLASS);
            if (componentType.isPrimitive()) {
                CstFieldRef typeField = CstFieldRef.forPrimitiveType(componentType);
                insn = new ThrowingCstInsn(Rops.GET_STATIC_OBJECT, pos, RegisterSpecList.EMPTY, this.catches, (Constant)typeField);
            } else {
                insn = new ThrowingCstInsn(Rops.CONST_OBJECT, pos, RegisterSpecList.EMPTY, this.catches, (Constant)new CstType(componentType));
            }
            this.insns.add(insn);
            rop = Rops.opMoveResultPseudo(classReg.getType());
            insn = new PlainInsn(rop, pos, classReg, RegisterSpecList.EMPTY);
            this.insns.add(insn);
            RegisterSpec objectReg = RegisterSpec.make(dest.getReg(), Type.OBJECT);
            insn = new ThrowingCstInsn(Rops.opInvokeStatic(MULTIANEWARRAY_METHOD.getPrototype()), pos, RegisterSpecList.make(classReg, dimsReg), this.catches, (Constant)MULTIANEWARRAY_METHOD);
            this.insns.add(insn);
            rop = Rops.opMoveResult(MULTIANEWARRAY_METHOD.getPrototype().getReturnType());
            insn = new PlainInsn(rop, pos, objectReg, RegisterSpecList.EMPTY);
            this.insns.add(insn);
            opcode = 192;
            sources = RegisterSpecList.make(objectReg);
        } else {
            if (opcode == 168) {
                this.hasJsr = true;
                return;
            }
            if (opcode == 169) {
                try {
                    this.returnAddress = (ReturnAddress)this.arg(0);
                }
                catch (ClassCastException ex) {
                    throw new RuntimeException("Argument to RET was not a ReturnAddress", ex);
                }
                return;
            }
        }
        int ropOpcode = this.jopToRopOpcode(opcode, cst);
        rop = Rops.ropFor(ropOpcode, destType, sources, cst);
        PlainInsn moveResult = null;
        if (dest != null && rop.isCallLike()) {
            ++this.extraBlockCount;
            Type returnType = rop.getOpcode() == 59 ? ((CstCallSiteRef)cst).getReturnType() : ((CstMethodRef)cst).getPrototype().getReturnType();
            moveResult = new PlainInsn(Rops.opMoveResult(returnType), pos, dest, RegisterSpecList.EMPTY);
            dest = null;
        } else if (dest != null && rop.canThrow()) {
            ++this.extraBlockCount;
            moveResult = new PlainInsn(Rops.opMoveResultPseudo(dest.getTypeBearer()), pos, dest, RegisterSpecList.EMPTY);
            dest = null;
        }
        if (ropOpcode == 41) {
            cst = CstType.intern(rop.getResult());
        } else if (cst == null && sourceCount == 2) {
            TypeBearer firstType = sources.get(0).getTypeBearer();
            TypeBearer lastType = sources.get(1).getTypeBearer();
            if ((lastType.isConstant() || firstType.isConstant()) && this.advice.hasConstantOperation(rop, sources.get(0), sources.get(1))) {
                if (lastType.isConstant()) {
                    cst = (Constant)((Object)lastType);
                    sources = sources.withoutLast();
                    if (rop.getOpcode() == 15) {
                        ropOpcode = 14;
                        CstInteger cstInt = (CstInteger)lastType;
                        cst = CstInteger.make(-cstInt.getValue());
                    }
                } else {
                    cst = (Constant)((Object)firstType);
                    sources = sources.withoutFirst();
                }
                rop = Rops.ropFor(ropOpcode, destType, sources, cst);
            }
        }
        SwitchList cases = this.getAuxCases();
        ArrayList<Constant> initValues = this.getInitValues();
        boolean canThrow = rop.canThrow();
        this.blockCanThrow |= canThrow;
        if (cases != null) {
            if (cases.size() == 0) {
                insn = new PlainInsn(Rops.GOTO, pos, null, RegisterSpecList.EMPTY);
                this.primarySuccessorIndex = 0;
            } else {
                IntList values = cases.getValues();
                insn = new SwitchInsn(rop, pos, dest, sources, values);
                this.primarySuccessorIndex = values.size();
            }
        } else if (ropOpcode == 33) {
            if (sources.size() != 0) {
                RegisterSpec source = sources.get(0);
                TypeBearer type = source.getTypeBearer();
                if (source.getReg() != 0) {
                    this.insns.add(new PlainInsn(Rops.opMove(type), pos, RegisterSpec.make(0, type), source));
                }
            }
            insn = new PlainInsn(Rops.GOTO, pos, null, RegisterSpecList.EMPTY);
            this.primarySuccessorIndex = 0;
            this.updateReturnOp(rop, pos);
            this.returns = true;
        } else if (cst != null) {
            if (canThrow) {
                insn = rop.getOpcode() == 58 ? this.makeInvokePolymorphicInsn(rop, pos, sources, this.catches, cst) : new ThrowingCstInsn(rop, pos, sources, this.catches, cst);
                this.catchesUsed = true;
                this.primarySuccessorIndex = this.catches.size();
            } else {
                insn = new PlainCstInsn(rop, pos, dest, sources, cst);
            }
        } else if (canThrow) {
            insn = new ThrowingInsn(rop, pos, sources, this.catches);
            this.catchesUsed = true;
            this.primarySuccessorIndex = opcode == 191 ? -1 : this.catches.size();
        } else {
            insn = new PlainInsn(rop, pos, dest, sources);
        }
        this.insns.add(insn);
        if (moveResult != null) {
            this.insns.add(moveResult);
        }
        if (initValues != null) {
            ++this.extraBlockCount;
            insn = new FillArrayDataInsn(Rops.FILL_ARRAY_DATA, pos, RegisterSpecList.make(moveResult.getResult()), initValues, cst);
            this.insns.add(insn);
        }
    }

    private RegisterSpecList getSources(int opcode, int stackPointer) {
        RegisterSpecList sources;
        int count = this.argCount();
        if (count == 0) {
            return RegisterSpecList.EMPTY;
        }
        int localIndex = this.getLocalIndex();
        if (localIndex >= 0) {
            sources = new RegisterSpecList(1);
            sources.set(0, RegisterSpec.make(localIndex, this.arg(0)));
        } else {
            sources = new RegisterSpecList(count);
            int regAt = stackPointer;
            for (int i = 0; i < count; ++i) {
                RegisterSpec spec = RegisterSpec.make(regAt, this.arg(i));
                sources.set(i, spec);
                regAt += spec.getCategory();
            }
            switch (opcode) {
                case 79: {
                    if (count != 3) {
                        throw new RuntimeException("shouldn't happen");
                    }
                    RegisterSpec array = sources.get(0);
                    RegisterSpec index = sources.get(1);
                    RegisterSpec value = sources.get(2);
                    sources.set(0, value);
                    sources.set(1, array);
                    sources.set(2, index);
                    break;
                }
                case 181: {
                    if (count != 2) {
                        throw new RuntimeException("shouldn't happen");
                    }
                    RegisterSpec obj = sources.get(0);
                    RegisterSpec value = sources.get(1);
                    sources.set(0, value);
                    sources.set(1, obj);
                    break;
                }
            }
        }
        sources.setImmutable();
        return sources;
    }

    private void updateReturnOp(Rop op, SourcePosition pos) {
        if (op == null) {
            throw new NullPointerException("op == null");
        } else if (pos == null) {
            throw new NullPointerException("pos == null");
        } else {
            if (this.returnOp == null) {
                this.returnOp = op;
                this.returnPosition = pos;
            } else {
                if (this.returnOp != op) {
                    throw new SimException("return op mismatch: " + op + ", " + this.returnOp);
                }

                if (pos.getLine() > this.returnPosition.getLine()) {
                    this.returnPosition = pos;
                }
            }

        }
    }

    private int jopToRopOpcode(int jop, Constant cst) {
        CstMethodRef ref;
        switch (jop) {
            case 0:
                return 1;
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 19:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            case 27:
            case 28:
            case 29:
            case 30:
            case 31:
            case 32:
            case 33:
            case 34:
            case 35:
            case 36:
            case 37:
            case 38:
            case 39:
            case 40:
            case 41:
            case 42:
            case 43:
            case 44:
            case 45:
            case 47:
            case 48:
            case 49:
            case 50:
            case 51:
            case 52:
            case 53:
            case 55:
            case 56:
            case 57:
            case 58:
            case 59:
            case 60:
            case 61:
            case 62:
            case 63:
            case 64:
            case 65:
            case 66:
            case 67:
            case 68:
            case 69:
            case 70:
            case 71:
            case 72:
            case 73:
            case 74:
            case 75:
            case 76:
            case 77:
            case 78:
            case 80:
            case 81:
            case 82:
            case 83:
            case 84:
            case 85:
            case 86:
            case 87:
            case 88:
            case 89:
            case 90:
            case 91:
            case 92:
            case 93:
            case 94:
            case 95:
            case 97:
            case 98:
            case 99:
            case 101:
            case 102:
            case 103:
            case 105:
            case 106:
            case 107:
            case 109:
            case 110:
            case 111:
            case 113:
            case 114:
            case 115:
            case 117:
            case 118:
            case 119:
            case 121:
            case 123:
            case 125:
            case 127:
            case 129:
            case 131:
            case 168:
            case 169:
            case 170:
            case 173:
            case 174:
            case 175:
            case 176:
            case 196:
            case 197:
            default:
                throw new RuntimeException("shouldn't happen");
            case 18:
            case 20:
                return 5;
            case 21:
            case 54:
                return 2;
            case 46:
                return 38;
            case 79:
                return 39;
            case 96:
            case 132:
                return 14;
            case 100:
                return 15;
            case 104:
                return 16;
            case 108:
                return 17;
            case 112:
                return 18;
            case 116:
                return 19;
            case 120:
                return 23;
            case 122:
                return 24;
            case 124:
                return 25;
            case 126:
                return 20;
            case 128:
                return 21;
            case 130:
                return 22;
            case 133:
            case 134:
            case 135:
            case 136:
            case 137:
            case 138:
            case 139:
            case 140:
            case 141:
            case 142:
            case 143:
            case 144:
                return 29;
            case 145:
                return 30;
            case 146:
                return 31;
            case 147:
                return 32;
            case 148:
            case 149:
            case 151:
                return 27;
            case 150:
            case 152:
                return 28;
            case 153:
            case 159:
            case 165:
            case 198:
                return 7;
            case 154:
            case 160:
            case 166:
            case 199:
                return 8;
            case 155:
            case 161:
                return 9;
            case 156:
            case 162:
                return 10;
            case 157:
            case 163:
                return 12;
            case 158:
            case 164:
                return 11;
            case 167:
                return 6;
            case 171:
                return 13;
            case 172:
            case 177:
                return 33;
            case 178:
                return 46;
            case 179:
                return 48;
            case 180:
                return 45;
            case 181:
                return 47;
            case 182:
                ref = (CstMethodRef)cst;
                if (ref.getDefiningClass().equals(this.method.getDefiningClass())) {
                    for(int i = 0; i < this.methods.size(); ++i) {
                        Method m = this.methods.get(i);
                        if (AccessFlags.isPrivate(m.getAccessFlags()) && ref.getNat().equals(m.getNat())) {
                            return 52;
                        }
                    }
                }

                return ref.isSignaturePolymorphic() ? 58 : 50;
            case 183:
                ref = (CstMethodRef)cst;
                if (!ref.isInstanceInit() && !ref.getDefiningClass().equals(this.method.getDefiningClass())) {
                    return 51;
                }

                return 52;
            case 184:
                return 49;
            case 185:
                return 53;
            case 186:
                return 59;
            case 187:
                return 40;
            case 188:
            case 189:
                return 41;
            case 190:
                return 34;
            case 191:
                return 35;
            case 192:
                return 43;
            case 193:
                return 44;
            case 194:
                return 36;
            case 195:
                return 37;
        }
    }

    private Insn makeInvokePolymorphicInsn(Rop rop, SourcePosition pos, RegisterSpecList sources, TypeList catches, Constant cst) {
        CstMethodRef cstMethodRef = (CstMethodRef)cst;
        return new InvokePolymorphicInsn(rop, pos, sources, catches, cstMethodRef);
    }

    static {
        MULTIANEWARRAY_METHOD = new CstMethodRef(ARRAY_REFLECT_TYPE, new CstNat(new CstString("newInstance"), new CstString("(Ljava/lang/Class;[I)Ljava/lang/Object;")));
    }
}
