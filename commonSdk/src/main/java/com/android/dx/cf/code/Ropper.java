/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.cf.code;

import com.android.dx.cf.code.BasicBlocker;
import com.android.dx.cf.code.ByteBlock;
import com.android.dx.cf.code.ByteBlockList;
import com.android.dx.cf.code.ByteCatchList;
import com.android.dx.cf.code.ConcreteMethod;
import com.android.dx.cf.code.Frame;
import com.android.dx.cf.code.LocalVariableList;
import com.android.dx.cf.code.ReturnAddress;
import com.android.dx.cf.code.RopperMachine;
import com.android.dx.cf.code.SimException;
import com.android.dx.cf.code.Simulator;
import com.android.dx.cf.iface.MethodList;
import com.android.dx.dex.DexOptions;
import com.android.dx.rop.code.BasicBlock;
import com.android.dx.rop.code.BasicBlockList;
import com.android.dx.rop.code.Insn;
import com.android.dx.rop.code.InsnList;
import com.android.dx.rop.code.PlainCstInsn;
import com.android.dx.rop.code.PlainInsn;
import com.android.dx.rop.code.RegisterSpec;
import com.android.dx.rop.code.RegisterSpecList;
import com.android.dx.rop.code.Rop;
import com.android.dx.rop.code.RopMethod;
import com.android.dx.rop.code.Rops;
import com.android.dx.rop.code.SourcePosition;
import com.android.dx.rop.code.ThrowingCstInsn;
import com.android.dx.rop.code.ThrowingInsn;
import com.android.dx.rop.code.TranslationAdvice;
import com.android.dx.rop.cst.Constant;
import com.android.dx.rop.cst.CstInteger;
import com.android.dx.rop.cst.CstType;
import com.android.dx.rop.type.Prototype;
import com.android.dx.rop.type.StdTypeList;
import com.android.dx.rop.type.Type;
import com.android.dx.rop.type.TypeList;
import com.android.dx.util.Bits;
import com.android.dx.util.Hex;
import com.android.dx.util.IntList;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public final class Ropper {
    private static final int PARAM_ASSIGNMENT = -1;
    private static final int RETURN = -2;
    private static final int SYNCH_RETURN = -3;
    private static final int SYNCH_SETUP_1 = -4;
    private static final int SYNCH_SETUP_2 = -5;
    private static final int SYNCH_CATCH_1 = -6;
    private static final int SYNCH_CATCH_2 = -7;
    private static final int SPECIAL_LABEL_COUNT = 7;
    private final ConcreteMethod method;
    private final ByteBlockList blocks;
    private final int maxLocals;
    private final int maxLabel;
    private final RopperMachine machine;
    private final Simulator sim;
    private final Frame[] startFrames;
    private final ArrayList<BasicBlock> result;
    private final ArrayList<IntList> resultSubroutines;
    private final CatchInfo[] catchInfos;
    private boolean synchNeedsExceptionHandler;
    private final Subroutine[] subroutines;
    private boolean hasSubroutines;
    private final ExceptionSetupLabelAllocator exceptionSetupLabelAllocator;

    public static RopMethod convert(ConcreteMethod method, TranslationAdvice advice, MethodList methods, DexOptions dexOptions) {
        try {
            Ropper r = new Ropper(method, advice, methods, dexOptions);
            r.doit();
            return r.getRopMethod();
        }
        catch (SimException ex) {
            ex.addContext("...while working on method " + method.getNat().toHuman());
            throw ex;
        }
    }

    private Ropper(ConcreteMethod method, TranslationAdvice advice, MethodList methods, DexOptions dexOptions) {
        if (method == null) {
            throw new NullPointerException("method == null");
        }
        if (advice == null) {
            throw new NullPointerException("advice == null");
        }
        this.method = method;
        this.blocks = BasicBlocker.identifyBlocks(method);
        this.maxLabel = this.blocks.getMaxLabel();
        this.maxLocals = method.getMaxLocals();
        this.machine = new RopperMachine(this, method, advice, methods);
        this.sim = new Simulator(this.machine, method, dexOptions);
        this.startFrames = new Frame[this.maxLabel];
        this.subroutines = new Subroutine[this.maxLabel];
        this.result = new ArrayList(this.blocks.size() * 2 + 10);
        this.resultSubroutines = new ArrayList(this.blocks.size() * 2 + 10);
        this.catchInfos = new CatchInfo[this.maxLabel];
        this.synchNeedsExceptionHandler = false;
        this.startFrames[0] = new Frame(this.maxLocals, method.getMaxStack());
        this.exceptionSetupLabelAllocator = new ExceptionSetupLabelAllocator();
    }

    int getFirstTempStackReg() {
        int regCount = this.getNormalRegCount();
        return this.isSynchronized() ? regCount + 1 : regCount;
    }

    private int getSpecialLabel(int label) {
        return this.maxLabel + this.method.getCatches().size() + ~label;
    }

    private int getMinimumUnreservedLabel() {
        return this.maxLabel + this.method.getCatches().size() + 7;
    }

    private int getAvailableLabel() {
        int candidate = this.getMinimumUnreservedLabel();
        for (BasicBlock bb : this.result) {
            int label = bb.getLabel();
            if (label < candidate) continue;
            candidate = label + 1;
        }
        return candidate;
    }

    private boolean isSynchronized() {
        int accessFlags = this.method.getAccessFlags();
        return (accessFlags & 0x20) != 0;
    }

    private boolean isStatic() {
        int accessFlags = this.method.getAccessFlags();
        return (accessFlags & 8) != 0;
    }

    private int getNormalRegCount() {
        return this.maxLocals + this.method.getMaxStack();
    }

    private RegisterSpec getSynchReg() {
        int reg = this.getNormalRegCount();
        return RegisterSpec.make(reg < 1 ? 1 : reg, Type.OBJECT);
    }

    private int labelToResultIndex(int label) {
        int sz = this.result.size();
        for (int i = 0; i < sz; ++i) {
            BasicBlock one = this.result.get(i);
            if (one.getLabel() != label) continue;
            return i;
        }
        return -1;
    }

    private BasicBlock labelToBlock(int label) {
        int idx = this.labelToResultIndex(label);
        if (idx < 0) {
            throw new IllegalArgumentException("no such label " + Hex.u2(label));
        }
        return this.result.get(idx);
    }

    private void addBlock(BasicBlock block, IntList subroutines) {
        if (block == null) {
            throw new NullPointerException("block == null");
        }
        this.result.add(block);
        subroutines.throwIfMutable();
        this.resultSubroutines.add(subroutines);
    }

    private boolean addOrReplaceBlock(BasicBlock block, IntList subroutines) {
        boolean ret;
        if (block == null) {
            throw new NullPointerException("block == null");
        }
        int idx = this.labelToResultIndex(block.getLabel());
        if (idx < 0) {
            ret = false;
        } else {
            this.removeBlockAndSpecialSuccessors(idx);
            ret = true;
        }
        this.result.add(block);
        subroutines.throwIfMutable();
        this.resultSubroutines.add(subroutines);
        return ret;
    }

    private boolean addOrReplaceBlockNoDelete(BasicBlock block, IntList subroutines) {
        boolean ret;
        if (block == null) {
            throw new NullPointerException("block == null");
        }
        int idx = this.labelToResultIndex(block.getLabel());
        if (idx < 0) {
            ret = false;
        } else {
            this.result.remove(idx);
            this.resultSubroutines.remove(idx);
            ret = true;
        }
        this.result.add(block);
        subroutines.throwIfMutable();
        this.resultSubroutines.add(subroutines);
        return ret;
    }

    private void removeBlockAndSpecialSuccessors(int idx) {
        int minLabel = this.getMinimumUnreservedLabel();
        BasicBlock block = this.result.get(idx);
        IntList successors = block.getSuccessors();
        int sz = successors.size();
        this.result.remove(idx);
        this.resultSubroutines.remove(idx);
        for (int i = 0; i < sz; ++i) {
            int label = successors.get(i);
            if (label < minLabel) continue;
            idx = this.labelToResultIndex(label);
            if (idx < 0) {
                throw new RuntimeException("Invalid label " + Hex.u2(label));
            }
            this.removeBlockAndSpecialSuccessors(idx);
        }
    }

    private RopMethod getRopMethod() {
        int sz = this.result.size();
        BasicBlockList bbl = new BasicBlockList(sz);
        for (int i = 0; i < sz; ++i) {
            bbl.set(i, this.result.get(i));
        }
        bbl.setImmutable();
        return new RopMethod(bbl, this.getSpecialLabel(-1));
    }

    private void doit() {
        int offset;
        int[] workSet = Bits.makeBitSet(this.maxLabel);
        Bits.set(workSet, 0);
        this.addSetupBlocks();
        this.setFirstFrame();
        while ((offset = Bits.findFirst(workSet, 0)) >= 0) {
            Bits.clear(workSet, offset);
            ByteBlock block = this.blocks.labelToBlock(offset);
            Frame frame = this.startFrames[offset];
            try {
                this.processBlock(block, frame, workSet);
            }
            catch (SimException ex) {
                ex.addContext("...while working on block " + Hex.u2(offset));
                throw ex;
            }
        }
        this.addReturnBlock();
        this.addSynchExceptionHandlerBlock();
        this.addExceptionSetupBlocks();
        if (this.hasSubroutines) {
            this.inlineSubroutines();
        }
    }

    private void setFirstFrame() {
        Prototype desc = this.method.getEffectiveDescriptor();
        this.startFrames[0].initializeWithParameters(desc.getParameterTypes());
        this.startFrames[0].setImmutable();
    }

    private void processBlock(ByteBlock block, Frame frame, int[] workSet) {
        Insn lastInsn;
        boolean synch;
        int primarySucc;
        int startSuccessorIndex;
        ByteCatchList catches = block.getCatches();
        this.machine.startBlock(catches.toRopCatchList());
        frame = frame.copy();
        this.sim.simulate(block, frame);
        frame.setImmutable();
        int extraBlockCount = this.machine.getExtraBlockCount();
        ArrayList<Insn> insns = this.machine.getInsns();
        int insnSz = insns.size();
        int catchSz = catches.size();
        IntList successors = block.getSuccessors();
        Subroutine calledSubroutine = null;
        if (this.machine.hasJsr()) {
            startSuccessorIndex = 1;
            int subroutineLabel = successors.get(1);
            if (this.subroutines[subroutineLabel] == null) {
                this.subroutines[subroutineLabel] = new Subroutine(subroutineLabel);
            }
            this.subroutines[subroutineLabel].addCallerBlock(block.getLabel());
            calledSubroutine = this.subroutines[subroutineLabel];
        } else if (this.machine.hasRet()) {
            ReturnAddress ra = this.machine.getReturnAddress();
            int subroutineLabel = ra.getSubroutineAddress();
            if (this.subroutines[subroutineLabel] == null) {
                this.subroutines[subroutineLabel] = new Subroutine(subroutineLabel, block.getLabel());
            } else {
                this.subroutines[subroutineLabel].addRetBlock(block.getLabel());
            }
            successors = this.subroutines[subroutineLabel].getSuccessors();
            this.subroutines[subroutineLabel].mergeToSuccessors(frame, workSet);
            startSuccessorIndex = successors.size();
        } else {
            startSuccessorIndex = this.machine.wereCatchesUsed() ? catchSz : 0;
        }
        int succSz = successors.size();
        for (int i = startSuccessorIndex; i < succSz; ++i) {
            int succ = successors.get(i);
            try {
                this.mergeAndWorkAsNecessary(succ, block.getLabel(), calledSubroutine, frame, workSet);
                continue;
            }
            catch (SimException ex) {
                ex.addContext("...while merging to block " + Hex.u2(succ));
                throw ex;
            }
        }
        if (succSz == 0 && this.machine.returns()) {
            successors = IntList.makeImmutable(this.getSpecialLabel(-2));
            succSz = 1;
        }
        if (succSz == 0) {
            primarySucc = -1;
        } else {
            primarySucc = this.machine.getPrimarySuccessorIndex();
            if (primarySucc >= 0) {
                primarySucc = successors.get(primarySucc);
            }
        }
        boolean bl = synch = this.isSynchronized() && this.machine.canThrow();
        if (synch || catchSz != 0) {
            int i;
            boolean catchesAny = false;
            IntList newSucc = new IntList(succSz);
            for (i = 0; i < catchSz; ++i) {
                ByteCatchList.Item one = catches.get(i);
                CstType exceptionClass = one.getExceptionClass();
                int targ = one.getHandlerPc();
                catchesAny |= exceptionClass == CstType.OBJECT;
                Frame f = frame.makeExceptionHandlerStartFrame(exceptionClass);
                try {
                    this.mergeAndWorkAsNecessary(targ, block.getLabel(), null, f, workSet);
                }
                catch (SimException ex) {
                    ex.addContext("...while merging exception to block " + Hex.u2(targ));
                    throw ex;
                }
                CatchInfo handlers = this.catchInfos[targ];
                if (handlers == null) {
                    this.catchInfos[targ] = handlers = new CatchInfo();
                }
                ExceptionHandlerSetup handler = handlers.getSetup(exceptionClass.getClassType());
                newSucc.add(handler.getLabel());
            }
            if (synch && !catchesAny) {
                newSucc.add(this.getSpecialLabel(-6));
                this.synchNeedsExceptionHandler = true;
                for (i = insnSz - extraBlockCount - 1; i < insnSz; ++i) {
                    Insn insn = insns.get(i);
                    if (!insn.canThrow()) continue;
                    insn = insn.withAddedCatch(Type.OBJECT);
                    insns.set(i, insn);
                }
            }
            if (primarySucc >= 0) {
                newSucc.add(primarySucc);
            }
            newSucc.setImmutable();
            successors = newSucc;
        }
        int primarySuccListIndex = successors.indexOf(primarySucc);
        while (extraBlockCount > 0) {
            Insn extraInsn;
            boolean needsGoto = (extraInsn = insns.get(--insnSz)).getOpcode().getBranchingness() == 1;
            InsnList il = new InsnList(needsGoto ? 2 : 1);
            IntList extraBlockSuccessors = successors;
            il.set(0, extraInsn);
            if (needsGoto) {
                il.set(1, new PlainInsn(Rops.GOTO, extraInsn.getPosition(), null, RegisterSpecList.EMPTY));
                extraBlockSuccessors = IntList.makeImmutable(primarySucc);
            }
            il.setImmutable();
            int label = this.getAvailableLabel();
            BasicBlock bb = new BasicBlock(label, il, extraBlockSuccessors, primarySucc);
            this.addBlock(bb, frame.getSubroutines());
            successors = successors.mutableCopy();
            successors.set(primarySuccListIndex, label);
            successors.setImmutable();
            primarySucc = label;
            --extraBlockCount;
        }
        Insn insn = lastInsn = insnSz == 0 ? null : insns.get(insnSz - 1);
        if (lastInsn == null || lastInsn.getOpcode().getBranchingness() == 1) {
            SourcePosition pos = lastInsn == null ? SourcePosition.NO_INFO : lastInsn.getPosition();
            insns.add(new PlainInsn(Rops.GOTO, pos, null, RegisterSpecList.EMPTY));
            ++insnSz;
        }
        InsnList il = new InsnList(insnSz);
        for (int i = 0; i < insnSz; ++i) {
            il.set(i, insns.get(i));
        }
        il.setImmutable();
        BasicBlock bb = new BasicBlock(block.getLabel(), il, successors, primarySucc);
        this.addOrReplaceBlock(bb, frame.getSubroutines());
    }

    private void mergeAndWorkAsNecessary(int label, int pred, Subroutine calledSubroutine, Frame frame, int[] workSet) {
        Frame existing = this.startFrames[label];
        if (existing != null) {
            Frame merged = calledSubroutine != null ? existing.mergeWithSubroutineCaller(frame, calledSubroutine.getStartBlock(), pred) : existing.mergeWith(frame);
            if (merged != existing) {
                this.startFrames[label] = merged;
                Bits.set(workSet, label);
            }
        } else {
            this.startFrames[label] = calledSubroutine != null ? frame.makeNewSubroutineStartFrame(label, pred) : frame;
            Bits.set(workSet, label);
        }
    }

    private void addSetupBlocks() {
        Insn insn;
        LocalVariableList localVariables = this.method.getLocalVariables();
        SourcePosition pos = this.method.makeSourcePosistion(0);
        Prototype desc = this.method.getEffectiveDescriptor();
        StdTypeList params = desc.getParameterTypes();
        int sz = params.size();
        InsnList insns = new InsnList(sz + 1);
        int at = 0;
        for (int i = 0; i < sz; ++i) {
            Type one = params.get(i);
            LocalVariableList.Item local = localVariables.pcAndIndexToLocal(0, at);
            RegisterSpec result = local == null ? RegisterSpec.make(at, one) : RegisterSpec.makeLocalOptional(at, one, local.getLocalItem());
            insn = new PlainCstInsn(Rops.opMoveParam(one), pos, result, RegisterSpecList.EMPTY, CstInteger.make(at));
            insns.set(i, insn);
            at += one.getCategory();
        }
        insns.set(sz, new PlainInsn(Rops.GOTO, pos, null, RegisterSpecList.EMPTY));
        insns.setImmutable();
        boolean synch = this.isSynchronized();
        int label = synch ? this.getSpecialLabel(-4) : 0;
        BasicBlock bb = new BasicBlock(this.getSpecialLabel(-1), insns, IntList.makeImmutable(label), label);
        this.addBlock(bb, IntList.EMPTY);
        if (synch) {
            RegisterSpec synchReg = this.getSynchReg();
            if (this.isStatic()) {
                insn = new ThrowingCstInsn(Rops.CONST_OBJECT, pos, RegisterSpecList.EMPTY, StdTypeList.EMPTY, (Constant)this.method.getDefiningClass());
                insns = new InsnList(1);
                insns.set(0, insn);
            } else {
                insns = new InsnList(2);
                insn = new PlainCstInsn(Rops.MOVE_PARAM_OBJECT, pos, synchReg, RegisterSpecList.EMPTY, CstInteger.VALUE_0);
                insns.set(0, insn);
                insns.set(1, new PlainInsn(Rops.GOTO, pos, null, RegisterSpecList.EMPTY));
            }
            int label2 = this.getSpecialLabel(-5);
            insns.setImmutable();
            bb = new BasicBlock(label, insns, IntList.makeImmutable(label2), label2);
            this.addBlock(bb, IntList.EMPTY);
            insns = new InsnList(this.isStatic() ? 2 : 1);
            if (this.isStatic()) {
                insns.set(0, new PlainInsn(Rops.opMoveResultPseudo(synchReg), pos, synchReg, RegisterSpecList.EMPTY));
            }
            insn = new ThrowingInsn(Rops.MONITOR_ENTER, pos, RegisterSpecList.make(synchReg), StdTypeList.EMPTY);
            insns.set(this.isStatic() ? 1 : 0, insn);
            insns.setImmutable();
            bb = new BasicBlock(label2, insns, IntList.makeImmutable(0), 0);
            this.addBlock(bb, IntList.EMPTY);
        }
    }

    private void addReturnBlock() {
        RegisterSpecList sources;
        InsnList insns;
        Rop returnOp = this.machine.getReturnOp();
        if (returnOp == null) {
            return;
        }
        SourcePosition returnPos = this.machine.getReturnPosition();
        int label = this.getSpecialLabel(-2);
        if (this.isSynchronized()) {
            insns = new InsnList(1);
            ThrowingInsn insn = new ThrowingInsn(Rops.MONITOR_EXIT, returnPos, RegisterSpecList.make(this.getSynchReg()), StdTypeList.EMPTY);
            insns.set(0, insn);
            insns.setImmutable();
            int nextLabel = this.getSpecialLabel(-3);
            BasicBlock bb = new BasicBlock(label, insns, IntList.makeImmutable(nextLabel), nextLabel);
            this.addBlock(bb, IntList.EMPTY);
            label = nextLabel;
        }
        insns = new InsnList(1);
        TypeList sourceTypes = returnOp.getSources();
        if (sourceTypes.size() == 0) {
            sources = RegisterSpecList.EMPTY;
        } else {
            RegisterSpec source = RegisterSpec.make(0, sourceTypes.getType(0));
            sources = RegisterSpecList.make(source);
        }
        PlainInsn insn = new PlainInsn(returnOp, returnPos, null, sources);
        insns.set(0, insn);
        insns.setImmutable();
        BasicBlock bb = new BasicBlock(label, insns, IntList.EMPTY, -1);
        this.addBlock(bb, IntList.EMPTY);
    }

    private void addSynchExceptionHandlerBlock() {
        if (!this.synchNeedsExceptionHandler) {
            return;
        }
        SourcePosition pos = this.method.makeSourcePosistion(0);
        RegisterSpec exReg = RegisterSpec.make(0, Type.THROWABLE);
        InsnList insns = new InsnList(2);
        Insn insn = new PlainInsn(Rops.opMoveException(Type.THROWABLE), pos, exReg, RegisterSpecList.EMPTY);
        insns.set(0, insn);
        insn = new ThrowingInsn(Rops.MONITOR_EXIT, pos, RegisterSpecList.make(this.getSynchReg()), StdTypeList.EMPTY);
        insns.set(1, insn);
        insns.setImmutable();
        int label2 = this.getSpecialLabel(-7);
        BasicBlock bb = new BasicBlock(this.getSpecialLabel(-6), insns, IntList.makeImmutable(label2), label2);
        this.addBlock(bb, IntList.EMPTY);
        insns = new InsnList(1);
        insn = new ThrowingInsn(Rops.THROW, pos, RegisterSpecList.make(exReg), StdTypeList.EMPTY);
        insns.set(0, insn);
        insns.setImmutable();
        bb = new BasicBlock(label2, insns, IntList.EMPTY, -1);
        this.addBlock(bb, IntList.EMPTY);
    }

    private void addExceptionSetupBlocks() {
        int len = this.catchInfos.length;
        for (int i = 0; i < len; ++i) {
            CatchInfo catches = this.catchInfos[i];
            if (catches == null) continue;
            for (ExceptionHandlerSetup one : catches.getSetups()) {
                Insn proto = this.labelToBlock(i).getFirstInsn();
                SourcePosition pos = proto.getPosition();
                InsnList il = new InsnList(2);
                PlainInsn insn = new PlainInsn(Rops.opMoveException(one.getCaughtType()), pos, RegisterSpec.make(this.maxLocals, one.getCaughtType()), RegisterSpecList.EMPTY);
                il.set(0, insn);
                insn = new PlainInsn(Rops.GOTO, pos, null, RegisterSpecList.EMPTY);
                il.set(1, insn);
                il.setImmutable();
                BasicBlock bb = new BasicBlock(one.getLabel(), il, IntList.makeImmutable(i), i);
                this.addBlock(bb, this.startFrames[i].getSubroutines());
            }
        }
    }

    private boolean isSubroutineCaller(BasicBlock bb) {
        IntList successors = bb.getSuccessors();
        if (successors.size() < 2) {
            return false;
        }
        int subLabel = successors.get(1);
        return subLabel < this.subroutines.length && this.subroutines[subLabel] != null;
    }

    private void inlineSubroutines() {
        int i;
        final IntList reachableSubroutineCallerLabels = new IntList(4);
        this.forEachNonSubBlockDepthFirst(0, new BasicBlock.Visitor(){

            @Override
            public void visitBlock(BasicBlock b) {
                if (Ropper.this.isSubroutineCaller(b)) {
                    reachableSubroutineCallerLabels.add(b.getLabel());
                }
            }
        });
        int largestAllocedLabel = this.getAvailableLabel();
        ArrayList<IntList> labelToSubroutines = new ArrayList<IntList>(largestAllocedLabel);
        for (i = 0; i < largestAllocedLabel; ++i) {
            labelToSubroutines.add(null);
        }
        for (i = 0; i < this.result.size(); ++i) {
            BasicBlock b = this.result.get(i);
            if (b == null) continue;
            IntList subroutineList = this.resultSubroutines.get(i);
            labelToSubroutines.set(b.getLabel(), subroutineList);
        }
        int sz = reachableSubroutineCallerLabels.size();
        for (int i2 = 0; i2 < sz; ++i2) {
            int label = reachableSubroutineCallerLabels.get(i2);
            new SubroutineInliner(new LabelAllocator(this.getAvailableLabel()), labelToSubroutines).inlineSubroutineCalledFrom(this.labelToBlock(label));
        }
        this.deleteUnreachableBlocks();
    }

    private void deleteUnreachableBlocks() {
        final IntList reachableLabels = new IntList(this.result.size());
        this.resultSubroutines.clear();
        this.forEachNonSubBlockDepthFirst(this.getSpecialLabel(-1), new BasicBlock.Visitor(){

            @Override
            public void visitBlock(BasicBlock b) {
                reachableLabels.add(b.getLabel());
            }
        });
        reachableLabels.sort();
        for (int i = this.result.size() - 1; i >= 0; --i) {
            if (reachableLabels.indexOf(this.result.get(i).getLabel()) >= 0) continue;
            this.result.remove(i);
        }
    }

    private Subroutine subroutineFromRetBlock(int label) {
        for (int i = this.subroutines.length - 1; i >= 0; --i) {
            Subroutine subroutine;
            if (this.subroutines[i] == null || !(subroutine = this.subroutines[i]).retBlocks.get(label)) continue;
            return subroutine;
        }
        return null;
    }

    private InsnList filterMoveReturnAddressInsns(InsnList insns) {
        int newSz = 0;
        int sz = insns.size();
        for (int i = 0; i < sz; ++i) {
            if (insns.get(i).getOpcode() == Rops.MOVE_RETURN_ADDRESS) continue;
            ++newSz;
        }
        if (newSz == sz) {
            return insns;
        }
        InsnList newInsns = new InsnList(newSz);
        int newIndex = 0;
        for (int i = 0; i < sz; ++i) {
            Insn insn = insns.get(i);
            if (insn.getOpcode() == Rops.MOVE_RETURN_ADDRESS) continue;
            newInsns.set(newIndex++, insn);
        }
        newInsns.setImmutable();
        return newInsns;
    }

    private void forEachNonSubBlockDepthFirst(int firstLabel, BasicBlock.Visitor v) {
        this.forEachNonSubBlockDepthFirst0(this.labelToBlock(firstLabel), v, new BitSet(this.maxLabel));
    }

    private void forEachNonSubBlockDepthFirst0(BasicBlock next, BasicBlock.Visitor v, BitSet visited) {
        v.visitBlock(next);
        visited.set(next.getLabel());
        IntList successors = next.getSuccessors();
        int sz = successors.size();
        for (int i = 0; i < sz; ++i) {
            int idx;
            int succ = successors.get(i);
            if (visited.get(succ) || this.isSubroutineCaller(next) && i > 0 || (idx = this.labelToResultIndex(succ)) < 0) continue;
            this.forEachNonSubBlockDepthFirst0(this.result.get(idx), v, visited);
        }
    }

    private class SubroutineInliner {
        private final HashMap<Integer, Integer> origLabelToCopiedLabel = new HashMap();
        private final BitSet workList;
        private int subroutineStart;
        private int subroutineSuccessor;
        private final LabelAllocator labelAllocator;
        private final ArrayList<IntList> labelToSubroutines;

        SubroutineInliner(LabelAllocator labelAllocator, ArrayList<IntList> labelToSubroutines) {
            this.workList = new BitSet(Ropper.this.maxLabel);
            this.labelAllocator = labelAllocator;
            this.labelToSubroutines = labelToSubroutines;
        }

        void inlineSubroutineCalledFrom(BasicBlock b) {
            this.subroutineSuccessor = b.getSuccessors().get(0);
            this.subroutineStart = b.getSuccessors().get(1);
            int newSubStartLabel = this.mapOrAllocateLabel(this.subroutineStart);
            int label = this.workList.nextSetBit(0);
            while (label >= 0) {
                this.workList.clear(label);
                int newLabel = this.origLabelToCopiedLabel.get(label);
                this.copyBlock(label, newLabel);
                if (Ropper.this.isSubroutineCaller(Ropper.this.labelToBlock(label))) {
                    new SubroutineInliner(this.labelAllocator, this.labelToSubroutines).inlineSubroutineCalledFrom(Ropper.this.labelToBlock(newLabel));
                }
                label = this.workList.nextSetBit(0);
            }
            Ropper.this.addOrReplaceBlockNoDelete(new BasicBlock(b.getLabel(), b.getInsns(), IntList.makeImmutable(newSubStartLabel), newSubStartLabel), this.labelToSubroutines.get(b.getLabel()));
        }

        private void copyBlock(int origLabel, int newLabel) {
            IntList successors;
            BasicBlock origBlock = Ropper.this.labelToBlock(origLabel);
            IntList origSuccessors = origBlock.getSuccessors();
            int primarySuccessor = -1;
            if (Ropper.this.isSubroutineCaller(origBlock)) {
                successors = IntList.makeImmutable(this.mapOrAllocateLabel(origSuccessors.get(0)), origSuccessors.get(1));
            } else {
                Subroutine subroutine = Ropper.this.subroutineFromRetBlock(origLabel);
                if (null != subroutine) {
                    if (subroutine.startBlock != this.subroutineStart) {
                        throw new RuntimeException("ret instruction returns to label " + Hex.u2(subroutine.startBlock) + " expected: " + Hex.u2(this.subroutineStart));
                    }
                    successors = IntList.makeImmutable(this.subroutineSuccessor);
                    primarySuccessor = this.subroutineSuccessor;
                } else {
                    int origPrimary = origBlock.getPrimarySuccessor();
                    int sz = origSuccessors.size();
                    successors = new IntList(sz);
                    for (int i = 0; i < sz; ++i) {
                        int origSuccLabel = origSuccessors.get(i);
                        int newSuccLabel = this.mapOrAllocateLabel(origSuccLabel);
                        successors.add(newSuccLabel);
                        if (origPrimary != origSuccLabel) continue;
                        primarySuccessor = newSuccLabel;
                    }
                    successors.setImmutable();
                }
            }
            Ropper.this.addBlock(new BasicBlock(newLabel, Ropper.this.filterMoveReturnAddressInsns(origBlock.getInsns()), successors, primarySuccessor), this.labelToSubroutines.get(newLabel));
        }

        private boolean involvedInSubroutine(int label, int subroutineStart) {
            IntList subroutinesList = this.labelToSubroutines.get(label);
            return subroutinesList != null && subroutinesList.size() > 0 && subroutinesList.top() == subroutineStart;
        }

        private int mapOrAllocateLabel(int origLabel) {
            int resultLabel;
            Integer mappedLabel = this.origLabelToCopiedLabel.get(origLabel);
            if (mappedLabel != null) {
                resultLabel = mappedLabel;
            } else if (!this.involvedInSubroutine(origLabel, this.subroutineStart)) {
                resultLabel = origLabel;
            } else {
                resultLabel = this.labelAllocator.getNextLabel();
                this.workList.set(origLabel);
                this.origLabelToCopiedLabel.put(origLabel, resultLabel);
                while (this.labelToSubroutines.size() <= resultLabel) {
                    this.labelToSubroutines.add(null);
                }
                this.labelToSubroutines.set(resultLabel, this.labelToSubroutines.get(origLabel));
            }
            return resultLabel;
        }
    }

    private class ExceptionSetupLabelAllocator
    extends LabelAllocator {
        int maxSetupLabel;

        ExceptionSetupLabelAllocator() {
            super(Ropper.this.maxLabel);
            this.maxSetupLabel = Ropper.this.maxLabel + Ropper.this.method.getCatches().size();
        }

        @Override
        int getNextLabel() {
            if (this.nextAvailableLabel >= this.maxSetupLabel) {
                throw new IndexOutOfBoundsException();
            }
            return this.nextAvailableLabel++;
        }
    }

    private static class LabelAllocator {
        int nextAvailableLabel;

        LabelAllocator(int startLabel) {
            this.nextAvailableLabel = startLabel;
        }

        int getNextLabel() {
            return this.nextAvailableLabel++;
        }
    }

    private class Subroutine {
        private BitSet callerBlocks;
        private BitSet retBlocks;
        private int startBlock;

        Subroutine(int startBlock) {
            this.startBlock = startBlock;
            this.retBlocks = new BitSet(Ropper.this.maxLabel);
            this.callerBlocks = new BitSet(Ropper.this.maxLabel);
            Ropper.this.hasSubroutines = true;
        }

        Subroutine(int startBlock, int retBlock) {
            this(startBlock);
            this.addRetBlock(retBlock);
        }

        int getStartBlock() {
            return this.startBlock;
        }

        void addRetBlock(int retBlock) {
            this.retBlocks.set(retBlock);
        }

        void addCallerBlock(int label) {
            this.callerBlocks.set(label);
        }

        IntList getSuccessors() {
            IntList successors = new IntList(this.callerBlocks.size());
            int label = this.callerBlocks.nextSetBit(0);
            while (label >= 0) {
                BasicBlock subCaller = Ropper.this.labelToBlock(label);
                successors.add(subCaller.getSuccessors().get(0));
                label = this.callerBlocks.nextSetBit(label + 1);
            }
            successors.setImmutable();
            return successors;
        }

        void mergeToSuccessors(Frame frame, int[] workSet) {
            int label = this.callerBlocks.nextSetBit(0);
            while (label >= 0) {
                BasicBlock subCaller = Ropper.this.labelToBlock(label);
                int succLabel = subCaller.getSuccessors().get(0);
                Frame subFrame = frame.subFrameForLabel(this.startBlock, label);
                if (subFrame != null) {
                    Ropper.this.mergeAndWorkAsNecessary(succLabel, -1, null, subFrame, workSet);
                } else {
                    Bits.set(workSet, label);
                }
                label = this.callerBlocks.nextSetBit(label + 1);
            }
        }
    }

    private static class ExceptionHandlerSetup {
        private Type caughtType;
        private int label;

        ExceptionHandlerSetup(Type caughtType, int label) {
            this.caughtType = caughtType;
            this.label = label;
        }

        Type getCaughtType() {
            return this.caughtType;
        }

        public int getLabel() {
            return this.label;
        }
    }

    private class CatchInfo {
        private final Map<Type, ExceptionHandlerSetup> setups = new HashMap<Type, ExceptionHandlerSetup>();

        private CatchInfo() {
        }

        ExceptionHandlerSetup getSetup(Type caughtType) {
            ExceptionHandlerSetup handler = this.setups.get(caughtType);
            if (handler == null) {
                int handlerSetupLabel = Ropper.this.exceptionSetupLabelAllocator.getNextLabel();
                handler = new ExceptionHandlerSetup(caughtType, handlerSetupLabel);
                this.setups.put(caughtType, handler);
            }
            return handler;
        }

        Collection<ExceptionHandlerSetup> getSetups() {
            return this.setups.values();
        }
    }
}

