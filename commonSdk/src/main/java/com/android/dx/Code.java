/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx;

import com.android.dx.BinaryOp;
import com.android.dx.Comparison;
import com.android.dx.Constants;
import com.android.dx.DexMaker;
import com.android.dx.FieldId;
import com.android.dx.Label;
import com.android.dx.Local;
import com.android.dx.MethodId;
import com.android.dx.TypeId;
import com.android.dx.UnaryOp;
import com.android.dx.rop.code.BasicBlockList;
import com.android.dx.rop.code.Insn;
import com.android.dx.rop.code.PlainCstInsn;
import com.android.dx.rop.code.PlainInsn;
import com.android.dx.rop.code.RegisterSpecList;
import com.android.dx.rop.code.Rop;
import com.android.dx.rop.code.Rops;
import com.android.dx.rop.code.SourcePosition;
import com.android.dx.rop.code.ThrowingCstInsn;
import com.android.dx.rop.code.ThrowingInsn;
import com.android.dx.rop.cst.Constant;
import com.android.dx.rop.cst.CstInteger;
import com.android.dx.rop.type.StdTypeList;
import com.android.dx.rop.type.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public final class Code {
    private final MethodId<?, ?> method;
    private final List<Label> labels = new ArrayList<Label>();
    private Label currentLabel;
    private boolean localsInitialized;
    private final Local<?> thisLocal;
    private final List<Local<?>> parameters = new ArrayList();
    private final List<Local<?>> locals = new ArrayList();
    private SourcePosition sourcePosition = SourcePosition.NO_INFO;
    private final List<TypeId<?>> catchTypes = new ArrayList();
    private final List<Label> catchLabels = new ArrayList<Label>();
    private StdTypeList catches = StdTypeList.EMPTY;

    Code(DexMaker.MethodDeclaration methodDeclaration) {
        this.method = methodDeclaration.method;
        if (methodDeclaration.isStatic()) {
            this.thisLocal = null;
        } else {
            this.thisLocal = Local.get(this, this.method.declaringType);
            this.parameters.add(this.thisLocal);
        }
        for (TypeId<?> parameter : this.method.parameters.types) {
            this.parameters.add(Local.get(this, parameter));
        }
        this.currentLabel = new Label();
        this.adopt(this.currentLabel);
        this.currentLabel.marked = true;
    }

    public <T> Local<T> newLocal(TypeId<T> type) {
        if (this.localsInitialized) {
            throw new IllegalStateException("Cannot allocate locals after adding instructions");
        }
        Local<T> result = Local.get(this, type);
        this.locals.add(result);
        return result;
    }

    public <T> Local<T> getParameter(int index, TypeId<T> type) {
        if (this.thisLocal != null) {
            ++index;
        }
        return this.coerce(this.parameters.get(index), type);
    }

    public <T> Local<T> getThis(TypeId<T> type) {
        if (this.thisLocal == null) {
            throw new IllegalStateException("static methods cannot access 'this'");
        }
        return this.coerce(this.thisLocal, type);
    }

    private <T> Local<T> coerce(Local<?> local, TypeId<T> expectedType) {
        if (!local.type.equals(expectedType)) {
            throw new IllegalArgumentException("requested " + expectedType + " but was " + local.type);
        } else {
            return (Local<T>) local;
        }
    }

    void initializeLocals() {
        if (this.localsInitialized) {
            throw new AssertionError();
        }
        this.localsInitialized = true;
        int reg = 0;
        for (Local<?> local : this.locals) {
            reg += local.initialize(reg);
        }
        int firstParamReg = reg;
        ArrayList<PlainCstInsn> moveParameterInstructions = new ArrayList<PlainCstInsn>();
        for (Local<?> local : this.parameters) {
            CstInteger paramConstant = CstInteger.make(reg - firstParamReg);
            reg += local.initialize(reg);
            moveParameterInstructions.add(new PlainCstInsn(Rops.opMoveParam(local.type.ropType), this.sourcePosition, local.spec(), RegisterSpecList.EMPTY, paramConstant));
        }
        this.labels.get((int)0).instructions.addAll(0, moveParameterInstructions);
    }

    int paramSize() {
        int result = 0;
        for (Local<?> local : this.parameters) {
            result += local.size();
        }
        return result;
    }

    private void adopt(Label target) {
        if (target.code == this) {
            return;
        }
        if (target.code != null) {
            throw new IllegalArgumentException("Cannot adopt label; it belongs to another Code");
        }
        target.code = this;
        this.labels.add(target);
    }

    public void mark(Label label) {
        this.adopt(label);
        if (label.marked) {
            throw new IllegalStateException("already marked");
        }
        label.marked = true;
        if (this.currentLabel != null) {
            this.jump(label);
        }
        this.currentLabel = label;
    }

    public void jump(Label target) {
        this.adopt(target);
        this.addInstruction(new PlainInsn(Rops.GOTO, this.sourcePosition, null, RegisterSpecList.EMPTY), target);
    }

    public void addCatchClause(TypeId<? extends Throwable> toCatch, Label catchClause) {
        if (this.catchTypes.contains(toCatch)) {
            throw new IllegalArgumentException("Already caught: " + toCatch);
        }
        this.adopt(catchClause);
        this.catchTypes.add(toCatch);
        this.catches = this.toTypeList(this.catchTypes);
        this.catchLabels.add(catchClause);
    }

    public Label removeCatchClause(TypeId<? extends Throwable> toCatch) {
        int index = this.catchTypes.indexOf(toCatch);
        if (index == -1) {
            throw new IllegalArgumentException("No catch clause: " + toCatch);
        }
        this.catchTypes.remove(index);
        this.catches = this.toTypeList(this.catchTypes);
        return this.catchLabels.remove(index);
    }

    public void moveException(Local<?> result) {
        this.addInstruction(new PlainInsn(Rops.opMoveException(Type.THROWABLE), SourcePosition.NO_INFO, result.spec(), RegisterSpecList.EMPTY));
    }

    public void throwValue(Local<? extends Throwable> toThrow) {
        this.addInstruction(new ThrowingInsn(Rops.THROW, this.sourcePosition, RegisterSpecList.make(toThrow.spec()), this.catches));
    }

    private StdTypeList toTypeList(List<TypeId<?>> types) {
        StdTypeList result = new StdTypeList(types.size());
        for (int i = 0; i < types.size(); ++i) {
            result.set(i, types.get((int)i).ropType);
        }
        return result;
    }

    private void addInstruction(Insn insn) {
        this.addInstruction(insn, null);
    }

    private void addInstruction(Insn insn, Label branch) {
        if (this.currentLabel == null || !this.currentLabel.marked) {
            throw new IllegalStateException("no current label");
        }
        this.currentLabel.instructions.add(insn);
        switch (insn.getOpcode().getBranchingness()) {
            case 1: {
                if (branch != null) {
                    throw new IllegalArgumentException("unexpected branch: " + branch);
                }
                return;
            }
            case 2: {
                if (branch != null) {
                    throw new IllegalArgumentException("unexpected branch: " + branch);
                }
                this.currentLabel = null;
                break;
            }
            case 3: {
                if (branch == null) {
                    throw new IllegalArgumentException("branch == null");
                }
                this.currentLabel.primarySuccessor = branch;
                this.currentLabel = null;
                break;
            }
            case 4: {
                if (branch == null) {
                    throw new IllegalArgumentException("branch == null");
                }
                this.splitCurrentLabel(branch, Collections.emptyList());
                break;
            }
            case 6: {
                if (branch != null) {
                    throw new IllegalArgumentException("unexpected branch: " + branch);
                }
                this.splitCurrentLabel(null, new ArrayList<Label>(this.catchLabels));
                break;
            }
            default: {
                throw new IllegalArgumentException();
            }
        }
    }

    private void splitCurrentLabel(Label alternateSuccessor, List<Label> catchLabels) {
        Label newLabel = new Label();
        this.adopt(newLabel);
        this.currentLabel.primarySuccessor = newLabel;
        this.currentLabel.alternateSuccessor = alternateSuccessor;
        this.currentLabel.catchLabels = catchLabels;
        this.currentLabel = newLabel;
        this.currentLabel.marked = true;
    }

    public <T> void loadConstant(Local<T> target, T value) {
        Rop rop;
        Rop rop2 = rop = value == null ? Rops.CONST_OBJECT_NOTHROW : Rops.opConst(target.type.ropType);
        if (rop.getBranchingness() == 1) {
            this.addInstruction(new PlainCstInsn(rop, this.sourcePosition, target.spec(), RegisterSpecList.EMPTY, Constants.getConstant(value)));
        } else {
            this.addInstruction(new ThrowingCstInsn(rop, this.sourcePosition, RegisterSpecList.EMPTY, this.catches, (Constant)Constants.getConstant(value)));
            this.moveResult(target, true);
        }
    }

    public <T> void move(Local<T> target, Local<T> source) {
        this.addInstruction(new PlainInsn(Rops.opMove(source.type.ropType), this.sourcePosition, target.spec(), source.spec()));
    }

    public <T> void op(UnaryOp op, Local<T> target, Local<T> source) {
        this.addInstruction(new PlainInsn(op.rop(source.type), this.sourcePosition, target.spec(), source.spec()));
    }

    public <T1, T2> void op(BinaryOp op, Local<T1> target, Local<T1> a, Local<T2> b) {
        Rop rop = op.rop(StdTypeList.make(a.type.ropType, b.type.ropType));
        RegisterSpecList sources = RegisterSpecList.make(a.spec(), b.spec());
        if (rop.getBranchingness() == 1) {
            this.addInstruction(new PlainInsn(rop, this.sourcePosition, target.spec(), sources));
        } else {
            this.addInstruction(new ThrowingInsn(rop, this.sourcePosition, sources, this.catches));
            this.moveResult(target, true);
        }
    }

    public <T> void compare(Comparison comparison, Label trueLabel, Local<T> a, Local<T> b) {
        this.adopt(trueLabel);
        Rop rop = comparison.rop(StdTypeList.make(a.type.ropType, b.type.ropType));
        this.addInstruction(new PlainInsn(rop, this.sourcePosition, null, RegisterSpecList.make(a.spec(), b.spec())), trueLabel);
    }

    public <T> void compareZ(Comparison comparison, Label trueLabel, Local<?> a) {
        this.adopt(trueLabel);
        Rop rop = comparison.rop(StdTypeList.make(a.type.ropType));
        this.addInstruction(new PlainInsn(rop, this.sourcePosition, null, RegisterSpecList.make(a.spec())), trueLabel);
    }

    public <T extends Number> void compareFloatingPoint(Local<Integer> target, Local<T> a, Local<T> b, int nanValue) {
        Rop rop;
        if (nanValue == 1) {
            rop = Rops.opCmpg(a.type.ropType);
        } else if (nanValue == -1) {
            rop = Rops.opCmpl(a.type.ropType);
        } else {
            throw new IllegalArgumentException("expected 1 or -1 but was " + nanValue);
        }
        this.addInstruction(new PlainInsn(rop, this.sourcePosition, target.spec(), RegisterSpecList.make(a.spec(), b.spec())));
    }

    public void compareLongs(Local<Integer> target, Local<Long> a, Local<Long> b) {
        this.addInstruction(new PlainInsn(Rops.CMPL_LONG, this.sourcePosition, target.spec(), RegisterSpecList.make(a.spec(), b.spec())));
    }

    public <D, V> void iget(FieldId<D, ? extends V> fieldId, Local<V> target, Local<D> instance) {
        this.addInstruction(new ThrowingCstInsn(Rops.opGetField(target.type.ropType), this.sourcePosition, RegisterSpecList.make(instance.spec()), this.catches, (Constant)fieldId.constant));
        this.moveResult(target, true);
    }

    public <D, V> void iput(FieldId<D, V> fieldId, Local<? extends D> instance, Local<? extends V> source) {
        this.addInstruction(new ThrowingCstInsn(Rops.opPutField(source.type.ropType), this.sourcePosition, RegisterSpecList.make(source.spec(), instance.spec()), this.catches, (Constant)fieldId.constant));
    }

    public <V> void sget(FieldId<?, ? extends V> fieldId, Local<V> target) {
        this.addInstruction(new ThrowingCstInsn(Rops.opGetStatic(target.type.ropType), this.sourcePosition, RegisterSpecList.EMPTY, this.catches, (Constant)fieldId.constant));
        this.moveResult(target, true);
    }

    public <V> void sput(FieldId<?, V> fieldId, Local<? extends V> source) {
        this.addInstruction(new ThrowingCstInsn(Rops.opPutStatic(source.type.ropType), this.sourcePosition, RegisterSpecList.make(source.spec()), this.catches, (Constant)fieldId.constant));
    }

    public <T> void newInstance(Local<T> target, MethodId<T, Void> constructor, Local<?> ... args) {
        if (target == null) {
            throw new IllegalArgumentException();
        }
        this.addInstruction(new ThrowingCstInsn(Rops.NEW_INSTANCE, this.sourcePosition, RegisterSpecList.EMPTY, this.catches, (Constant)constructor.declaringType.constant));
        this.moveResult(target, true);
        this.invokeDirect(constructor, null, target, args);
    }

    public <R> void invokeStatic(MethodId<?, R> method, Local<? super R> target, Local<?> ... args) {
        this.invoke(Rops.opInvokeStatic(method.prototype(true)), method, target, null, args);
    }

    public <D, R> void invokeVirtual(MethodId<D, R> method, Local<? super R> target, Local<? extends D> instance, Local<?> ... args) {
        this.invoke(Rops.opInvokeVirtual(method.prototype(true)), method, target, instance, args);
    }

    public <D, R> void invokeDirect(MethodId<D, R> method, Local<? super R> target, Local<? extends D> instance, Local<?> ... args) {
        this.invoke(Rops.opInvokeDirect(method.prototype(true)), method, target, instance, args);
    }

    public <D, R> void invokeSuper(MethodId<D, R> method, Local<? super R> target, Local<? extends D> instance, Local<?> ... args) {
        this.invoke(Rops.opInvokeSuper(method.prototype(true)), method, target, instance, args);
    }

    public <D, R> void invokeInterface(MethodId<D, R> method, Local<? super R> target, Local<? extends D> instance, Local<?> ... args) {
        this.invoke(Rops.opInvokeInterface(method.prototype(true)), method, target, instance, args);
    }

    private <D, R> void invoke(Rop rop, MethodId<D, R> method, Local<? super R> target, Local<? extends D> object, Local<?> ... args) {
        this.addInstruction(new ThrowingCstInsn(rop, this.sourcePosition, Code.concatenate(object, args), this.catches, (Constant)method.constant));
        if (target != null) {
            this.moveResult(target, false);
        }
    }

    public void instanceOfType(Local<?> target, Local<?> source, TypeId<?> type) {
        this.addInstruction(new ThrowingCstInsn(Rops.INSTANCE_OF, this.sourcePosition, RegisterSpecList.make(source.spec()), this.catches, (Constant)type.constant));
        this.moveResult(target, true);
    }

    public void cast(Local<?> target, Local<?> source) {
        if (source.getType().ropType.isReference()) {
            this.addInstruction(new ThrowingCstInsn(Rops.CHECK_CAST, this.sourcePosition, RegisterSpecList.make(source.spec()), this.catches, (Constant)target.type.constant));
            this.moveResult(target, true);
        } else {
            this.addInstruction(new PlainInsn(this.getCastRop(source.type.ropType, target.type.ropType), this.sourcePosition, target.spec(), source.spec()));
        }
    }

    private Rop getCastRop(Type sourceType, Type targetType) {
        if (sourceType.getBasicType() == 6) {
            switch (targetType.getBasicType()) {
                case 8: {
                    return Rops.TO_SHORT;
                }
                case 3: {
                    return Rops.TO_CHAR;
                }
                case 2: {
                    return Rops.TO_BYTE;
                }
            }
        }
        return Rops.opConv(targetType, sourceType);
    }

    public <T> void arrayLength(Local<Integer> target, Local<T> array) {
        this.addInstruction(new ThrowingInsn(Rops.ARRAY_LENGTH, this.sourcePosition, RegisterSpecList.make(array.spec()), this.catches));
        this.moveResult(target, true);
    }

    public <T> void newArray(Local<T> target, Local<Integer> length) {
        this.addInstruction(new ThrowingCstInsn(Rops.opNewArray(target.type.ropType), this.sourcePosition, RegisterSpecList.make(length.spec()), this.catches, (Constant)target.type.constant));
        this.moveResult(target, true);
    }

    public void aget(Local<?> target, Local<?> array, Local<Integer> index) {
        this.addInstruction(new ThrowingInsn(Rops.opAget(target.type.ropType), this.sourcePosition, RegisterSpecList.make(array.spec(), index.spec()), this.catches));
        this.moveResult(target, true);
    }

    public void aput(Local<?> array, Local<Integer> index, Local<?> source) {
        this.addInstruction(new ThrowingInsn(Rops.opAput(source.type.ropType), this.sourcePosition, RegisterSpecList.make(source.spec(), array.spec(), index.spec()), this.catches));
    }

    public void returnVoid() {
        if (!this.method.returnType.equals(TypeId.VOID)) {
            throw new IllegalArgumentException("declared " + this.method.returnType + " but returned void");
        }
        this.addInstruction(new PlainInsn(Rops.RETURN_VOID, this.sourcePosition, null, RegisterSpecList.EMPTY));
    }

    public void returnValue(Local<?> result) {
        if (!result.type.equals(this.method.returnType)) {
            throw new IllegalArgumentException("declared " + this.method.returnType + " but returned " + result.type);
        }
        this.addInstruction(new PlainInsn(Rops.opReturn(result.type.ropType), this.sourcePosition, null, RegisterSpecList.make(result.spec())));
    }

    private void moveResult(Local<?> target, boolean afterNonInvokeThrowingInsn) {
        Rop rop = afterNonInvokeThrowingInsn ? Rops.opMoveResultPseudo(target.type.ropType) : Rops.opMoveResult(target.type.ropType);
        this.addInstruction(new PlainInsn(rop, this.sourcePosition, target.spec(), RegisterSpecList.EMPTY));
    }

    public void monitorEnter(Local<?> monitor) {
        this.addInstruction(new ThrowingInsn(Rops.MONITOR_ENTER, this.sourcePosition, RegisterSpecList.make(monitor.spec()), this.catches));
    }

    public void monitorExit(Local<?> monitor) {
        this.addInstruction(new ThrowingInsn(Rops.MONITOR_EXIT, this.sourcePosition, RegisterSpecList.make(monitor.spec()), this.catches));
    }

    BasicBlockList toBasicBlocks() {
        if (!this.localsInitialized) {
            this.initializeLocals();
        }
        this.cleanUpLabels();
        BasicBlockList result = new BasicBlockList(this.labels.size());
        for (int i = 0; i < this.labels.size(); ++i) {
            result.set(i, this.labels.get(i).toBasicBlock());
        }
        return result;
    }

    private void cleanUpLabels() {
        int id2 = 0;
        Iterator<Label> i = this.labels.iterator();
        while (i.hasNext()) {
            Label label = i.next();
            if (label.isEmpty()) {
                i.remove();
                continue;
            }
            label.compact();
            label.id = id2++;
        }
    }

    private static RegisterSpecList concatenate(Local<?> first, Local<?>[] rest) {
        int offset = first != null ? 1 : 0;
        RegisterSpecList result = new RegisterSpecList(offset + rest.length);
        if (first != null) {
            result.set(0, first.spec());
        }
        for (int i = 0; i < rest.length; ++i) {
            result.set(i + offset, rest[i].spec());
        }
        return result;
    }
}

