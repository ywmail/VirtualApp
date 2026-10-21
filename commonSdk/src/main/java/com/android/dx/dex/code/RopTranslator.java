/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.dex.code;

import com.android.dx.dex.DexOptions;
import com.android.dx.dex.code.ArrayData;
import com.android.dx.dex.code.BlockAddresses;
import com.android.dx.dex.code.CodeAddress;
import com.android.dx.dex.code.CstInsn;
import com.android.dx.dex.code.DalvCode;
import com.android.dx.dex.code.DalvInsn;
import com.android.dx.dex.code.Dop;
import com.android.dx.dex.code.Dops;
import com.android.dx.dex.code.FixedSizeInsn;
import com.android.dx.dex.code.LocalSnapshot;
import com.android.dx.dex.code.LocalStart;
import com.android.dx.dex.code.MultiCstInsn;
import com.android.dx.dex.code.OddSpacer;
import com.android.dx.dex.code.OutputCollector;
import com.android.dx.dex.code.RopToDop;
import com.android.dx.dex.code.SimpleInsn;
import com.android.dx.dex.code.StdCatchBuilder;
import com.android.dx.dex.code.SwitchData;
import com.android.dx.dex.code.TargetInsn;
import com.android.dx.rop.code.BasicBlock;
import com.android.dx.rop.code.BasicBlockList;
import com.android.dx.rop.code.FillArrayDataInsn;
import com.android.dx.rop.code.Insn;
import com.android.dx.rop.code.InvokePolymorphicInsn;
import com.android.dx.rop.code.LocalVariableInfo;
import com.android.dx.rop.code.PlainCstInsn;
import com.android.dx.rop.code.PlainInsn;
import com.android.dx.rop.code.RegisterSpec;
import com.android.dx.rop.code.RegisterSpecList;
import com.android.dx.rop.code.RegisterSpecSet;
import com.android.dx.rop.code.Rop;
import com.android.dx.rop.code.RopMethod;
import com.android.dx.rop.code.SourcePosition;
import com.android.dx.rop.code.SwitchInsn;
import com.android.dx.rop.code.ThrowingCstInsn;
import com.android.dx.rop.code.ThrowingInsn;
import com.android.dx.rop.cst.Constant;
import com.android.dx.rop.cst.CstInteger;
import com.android.dx.util.Bits;
import com.android.dx.util.IntList;
import java.util.ArrayList;

public final class RopTranslator {
    private final DexOptions dexOptions;
    private final RopMethod method;
    private final int positionInfo;
    private final LocalVariableInfo locals;
    private final BlockAddresses addresses;
    private final OutputCollector output;
    private final TranslationVisitor translationVisitor;
    private final int regCount;
    private int[] order;
    private final int paramSize;
    private final boolean paramsAreInOrder;

    public static DalvCode translate(RopMethod method, int positionInfo, LocalVariableInfo locals, int paramSize, DexOptions dexOptions) {
        RopTranslator translator = new RopTranslator(method, positionInfo, locals, paramSize, dexOptions);
        return translator.translateAndGetResult();
    }

    private RopTranslator(RopMethod method, int positionInfo, LocalVariableInfo locals, int paramSize, DexOptions dexOptions) {
        this.dexOptions = dexOptions;
        this.method = method;
        this.positionInfo = positionInfo;
        this.locals = locals;
        this.addresses = new BlockAddresses(method);
        this.paramSize = paramSize;
        this.order = null;
        this.paramsAreInOrder = RopTranslator.calculateParamsAreInOrder(method, paramSize);
        BasicBlockList blocks = method.getBlocks();
        int bsz = blocks.size();
        int maxInsns = bsz * 3 + blocks.getInstructionCount();
        if (locals != null) {
            maxInsns += bsz + locals.getAssignmentCount();
        }
        this.regCount = blocks.getRegCount() + (this.paramsAreInOrder ? 0 : this.paramSize);
        this.output = new OutputCollector(dexOptions, maxInsns, bsz * 3, this.regCount, paramSize);
        this.translationVisitor = locals != null ? new LocalVariableAwareTranslationVisitor(this.output, locals) : new TranslationVisitor(this.output);
    }

    private static boolean calculateParamsAreInOrder(RopMethod method, final int paramSize) {
        final boolean[] paramsAreInOrder = new boolean[]{true};
        final int initialRegCount = method.getBlocks().getRegCount();
        method.getBlocks().forEachInsn(new Insn.BaseVisitor(){

            @Override
            public void visitPlainCstInsn(PlainCstInsn insn) {
                if (insn.getOpcode().getOpcode() == 3) {
                    int param = ((CstInteger)insn.getConstant()).getValue();
                    paramsAreInOrder[0] = paramsAreInOrder[0] && initialRegCount - paramSize + param == insn.getResult().getReg();
                }
            }
        });
        return paramsAreInOrder[0];
    }

    private DalvCode translateAndGetResult() {
        this.pickOrder();
        this.outputInstructions();
        StdCatchBuilder catches = new StdCatchBuilder(this.method, this.order, this.addresses);
        return new DalvCode(this.positionInfo, this.output.getFinisher(), catches);
    }

    private void outputInstructions() {
        BasicBlockList blocks = this.method.getBlocks();
        int[] order = this.order;
        int len = order.length;
        for (int i = 0; i < len; ++i) {
            int nextI = i + 1;
            int nextLabel = nextI == order.length ? -1 : order[nextI];
            this.outputBlock(blocks.labelToBlock(order[i]), nextLabel);
        }
    }

    private void outputBlock(BasicBlock block, int nextLabel) {
        CodeAddress startAddress = this.addresses.getStart(block);
        this.output.add(startAddress);
        if (this.locals != null) {
            RegisterSpecSet starts = this.locals.getStarts(block);
            this.output.add(new LocalSnapshot(startAddress.getPosition(), starts));
        }
        this.translationVisitor.setBlock(block, this.addresses.getLast(block));
        block.getInsns().forEach(this.translationVisitor);
        this.output.add(this.addresses.getEnd(block));
        int succ = block.getPrimarySuccessor();
        Insn lastInsn = block.getLastInsn();
        if (succ >= 0 && succ != nextLabel) {
            Rop lastRop = lastInsn.getOpcode();
            if (lastRop.getBranchingness() == 4 && block.getSecondarySuccessor() == nextLabel) {
                this.output.reverseBranch(1, this.addresses.getStart(succ));
            } else {
                TargetInsn insn = new TargetInsn(Dops.GOTO, lastInsn.getPosition(), RegisterSpecList.EMPTY, this.addresses.getStart(succ));
                this.output.add(insn);
            }
        }
    }

    private void pickOrder() {
        BasicBlockList blocks = this.method.getBlocks();
        int sz = blocks.size();
        int maxLabel = blocks.getMaxLabel();
        int[] workSet = Bits.makeBitSet(maxLabel);
        int[] tracebackSet = Bits.makeBitSet(maxLabel);
        for (int i = 0; i < sz; ++i) {
            BasicBlock one = blocks.get(i);
            Bits.set(workSet, one.getLabel());
        }
        int[] order = new int[sz];
        int at = 0;
        int label = this.method.getFirstLabel();
        while (label != -1) {
            block2: while (true) {
                int predLabel;
                IntList preds = this.method.labelToPredecessors(label);
                int psz = preds.size();
                for (int i = 0; i < psz && !Bits.get(tracebackSet, predLabel = preds.get(i)); ++i) {
                    BasicBlock pred;
                    if (!Bits.get(workSet, predLabel) || (pred = blocks.labelToBlock(predLabel)).getPrimarySuccessor() != label) continue;
                    label = predLabel;
                    Bits.set(tracebackSet, label);
                    continue block2;
                }
                break;
            }
            block4: while (label != -1) {
                Bits.clear(workSet, label);
                Bits.clear(tracebackSet, label);
                order[at] = label;
                ++at;
                BasicBlock one = blocks.labelToBlock(label);
                BasicBlock preferredBlock = blocks.preferredSuccessorOf(one);
                if (preferredBlock == null) break;
                int preferred = preferredBlock.getLabel();
                int primary = one.getPrimarySuccessor();
                if (Bits.get(workSet, preferred)) {
                    label = preferred;
                    continue;
                }
                if (primary != preferred && primary >= 0 && Bits.get(workSet, primary)) {
                    label = primary;
                    continue;
                }
                IntList successors = one.getSuccessors();
                int ssz = successors.size();
                label = -1;
                for (int i = 0; i < ssz; ++i) {
                    int candidate = successors.get(i);
                    if (!Bits.get(workSet, candidate)) continue;
                    label = candidate;
                    continue block4;
                }
            }
            label = Bits.findFirst(workSet, 0);
        }
        if (at != sz) {
            throw new RuntimeException("shouldn't happen");
        }
        this.order = order;
    }

    private static RegisterSpecList getRegs(Insn insn) {
        return RopTranslator.getRegs(insn, insn.getResult());
    }

    private static RegisterSpecList getRegs(Insn insn, RegisterSpec resultReg) {
        RegisterSpecList regs = insn.getSources();
        if (insn.getOpcode().isCommutative() && regs.size() == 2 && resultReg.getReg() == regs.get(1).getReg()) {
            regs = RegisterSpecList.make(regs.get(1), regs.get(0));
        }
        if (resultReg == null) {
            return regs;
        }
        return regs.withFirst(resultReg);
    }

    private class LocalVariableAwareTranslationVisitor
    extends TranslationVisitor {
        private final LocalVariableInfo locals;

        public LocalVariableAwareTranslationVisitor(OutputCollector output, LocalVariableInfo locals) {
            super(output);
            this.locals = locals;
        }

        @Override
        public void visitPlainInsn(PlainInsn insn) {
            super.visitPlainInsn(insn);
            this.addIntroductionIfNecessary(insn);
        }

        @Override
        public void visitPlainCstInsn(PlainCstInsn insn) {
            super.visitPlainCstInsn(insn);
            this.addIntroductionIfNecessary(insn);
        }

        @Override
        public void visitSwitchInsn(SwitchInsn insn) {
            super.visitSwitchInsn(insn);
            this.addIntroductionIfNecessary(insn);
        }

        @Override
        public void visitThrowingCstInsn(ThrowingCstInsn insn) {
            super.visitThrowingCstInsn(insn);
            this.addIntroductionIfNecessary(insn);
        }

        @Override
        public void visitThrowingInsn(ThrowingInsn insn) {
            super.visitThrowingInsn(insn);
            this.addIntroductionIfNecessary(insn);
        }

        public void addIntroductionIfNecessary(Insn insn) {
            RegisterSpec spec = this.locals.getAssignment(insn);
            if (spec != null) {
                this.addOutput(new LocalStart(insn.getPosition(), spec));
            }
        }
    }

    private class TranslationVisitor
    implements Insn.Visitor {
        private final OutputCollector output;
        private BasicBlock block;
        private CodeAddress lastAddress;

        public TranslationVisitor(OutputCollector output) {
            this.output = output;
        }

        public void setBlock(BasicBlock block, CodeAddress lastAddress) {
            this.block = block;
            this.lastAddress = lastAddress;
        }

        @Override
        public void visitPlainInsn(PlainInsn insn) {
            FixedSizeInsn di;
            Rop rop = insn.getOpcode();
            if (rop.getOpcode() == 54) {
                return;
            }
            if (rop.getOpcode() == 56) {
                return;
            }
            SourcePosition pos = insn.getPosition();
            Dop opcode = RopToDop.dopFor(insn);
            switch (rop.getBranchingness()) {
                case 1: 
                case 2: 
                case 6: {
                    di = new SimpleInsn(opcode, pos, RopTranslator.getRegs(insn));
                    break;
                }
                case 3: {
                    return;
                }
                case 4: {
                    int target = this.block.getSuccessors().get(1);
                    di = new TargetInsn(opcode, pos, RopTranslator.getRegs(insn), RopTranslator.this.addresses.getStart(target));
                    break;
                }
                default: {
                    throw new RuntimeException("shouldn't happen");
                }
            }
            this.addOutput(di);
        }

        @Override
        public void visitPlainCstInsn(PlainCstInsn insn) {
            SourcePosition pos = insn.getPosition();
            Dop opcode = RopToDop.dopFor(insn);
            Rop rop = insn.getOpcode();
            int ropOpcode = rop.getOpcode();
            if (rop.getBranchingness() != 1) {
                throw new RuntimeException("shouldn't happen");
            }
            if (ropOpcode == 3) {
                if (!RopTranslator.this.paramsAreInOrder) {
                    RegisterSpec dest = insn.getResult();
                    int param = ((CstInteger)insn.getConstant()).getValue();
                    RegisterSpec source = RegisterSpec.make(RopTranslator.this.regCount - RopTranslator.this.paramSize + param, dest.getType());
                    SimpleInsn di = new SimpleInsn(opcode, pos, RegisterSpecList.make(dest, source));
                    this.addOutput(di);
                }
            } else {
                RegisterSpecList regs = RopTranslator.getRegs(insn);
                CstInsn di = new CstInsn(opcode, pos, regs, insn.getConstant());
                this.addOutput(di);
            }
        }

        @Override
        public void visitSwitchInsn(SwitchInsn insn) {
            SourcePosition pos = insn.getPosition();
            IntList cases = insn.getCases();
            IntList successors = this.block.getSuccessors();
            int casesSz = cases.size();
            int succSz = successors.size();
            int primarySuccessor = this.block.getPrimarySuccessor();
            if (casesSz != succSz - 1 || primarySuccessor != successors.get(casesSz)) {
                throw new RuntimeException("shouldn't happen");
            }
            CodeAddress[] switchTargets = new CodeAddress[casesSz];
            for (int i = 0; i < casesSz; ++i) {
                int label = successors.get(i);
                switchTargets[i] = RopTranslator.this.addresses.getStart(label);
            }
            CodeAddress dataAddress = new CodeAddress(pos);
            CodeAddress switchAddress = new CodeAddress(this.lastAddress.getPosition(), true);
            SwitchData dataInsn = new SwitchData(pos, switchAddress, cases, switchTargets);
            Dop opcode = dataInsn.isPacked() ? Dops.PACKED_SWITCH : Dops.SPARSE_SWITCH;
            TargetInsn switchInsn = new TargetInsn(opcode, pos, RopTranslator.getRegs(insn), dataAddress);
            this.addOutput(switchAddress);
            this.addOutput(switchInsn);
            this.addOutputSuffix(new OddSpacer(pos));
            this.addOutputSuffix(dataAddress);
            this.addOutputSuffix(dataInsn);
        }

        private RegisterSpec getNextMoveResultPseudo() {
            int label = this.block.getPrimarySuccessor();
            if (label < 0) {
                return null;
            }
            Insn insn = RopTranslator.this.method.getBlocks().labelToBlock(label).getInsns().get(0);
            if (insn.getOpcode().getOpcode() != 56) {
                return null;
            }
            return insn.getResult();
        }

        @Override
        public void visitInvokePolymorphicInsn(InvokePolymorphicInsn insn) {
            SourcePosition pos = insn.getPosition();
            Dop opcode = RopToDop.dopFor(insn);
            Rop rop = insn.getOpcode();
            if (rop.getBranchingness() != 6) {
                throw new RuntimeException("Expected BRANCH_THROW got " + rop.getBranchingness());
            }
            if (!rop.isCallLike()) {
                throw new RuntimeException("Expected call-like operation");
            }
            this.addOutput(this.lastAddress);
            RegisterSpecList regs = insn.getSources();
            Constant[] constants = new Constant[]{insn.getPolymorphicMethod(), insn.getCallSiteProto()};
            MultiCstInsn di = new MultiCstInsn(opcode, pos, regs, constants);
            this.addOutput(di);
        }

        @Override
        public void visitThrowingCstInsn(ThrowingCstInsn insn) {
            SourcePosition pos = insn.getPosition();
            Dop opcode = RopToDop.dopFor(insn);
            Rop rop = insn.getOpcode();
            Constant cst = insn.getConstant();
            if (rop.getBranchingness() != 6) {
                throw new RuntimeException("Expected BRANCH_THROW got " + rop.getBranchingness());
            }
            this.addOutput(this.lastAddress);
            if (rop.isCallLike()) {
                RegisterSpecList regs = insn.getSources();
                CstInsn di = new CstInsn(opcode, pos, regs, cst);
                this.addOutput(di);
            } else {
                RegisterSpec realResult = this.getNextMoveResultPseudo();
                RegisterSpecList regs = RopTranslator.getRegs(insn, realResult);
                boolean hasResult = opcode.hasResult() || rop.getOpcode() == 43;
                if (hasResult != (realResult != null)) {
                    throw new RuntimeException("Insn with result/move-result-pseudo mismatch " + insn);
                }
                FixedSizeInsn di = rop.getOpcode() == 41 && opcode.getOpcode() != 35 ? new SimpleInsn(opcode, pos, regs) : new CstInsn(opcode, pos, regs, cst);
                this.addOutput(di);
            }
        }

        @Override
        public void visitThrowingInsn(ThrowingInsn insn) {
            SourcePosition pos = insn.getPosition();
            Dop opcode = RopToDop.dopFor(insn);
            Rop rop = insn.getOpcode();
            if (rop.getBranchingness() != 6) {
                throw new RuntimeException("shouldn't happen");
            }
            RegisterSpec realResult = this.getNextMoveResultPseudo();
            if (opcode.hasResult() != (realResult != null)) {
                throw new RuntimeException("Insn with result/move-result-pseudo mismatch" + insn);
            }
            this.addOutput(this.lastAddress);
            SimpleInsn di = new SimpleInsn(opcode, pos, RopTranslator.getRegs(insn, realResult));
            this.addOutput(di);
        }

        @Override
        public void visitFillArrayDataInsn(FillArrayDataInsn insn) {
            SourcePosition pos = insn.getPosition();
            Constant cst = insn.getConstant();
            ArrayList<Constant> values = insn.getInitValues();
            Rop rop = insn.getOpcode();
            if (rop.getBranchingness() != 1) {
                throw new RuntimeException("shouldn't happen");
            }
            CodeAddress dataAddress = new CodeAddress(pos);
            ArrayData dataInsn = new ArrayData(pos, this.lastAddress, values, cst);
            TargetInsn fillArrayDataInsn = new TargetInsn(Dops.FILL_ARRAY_DATA, pos, RopTranslator.getRegs(insn), dataAddress);
            this.addOutput(this.lastAddress);
            this.addOutput(fillArrayDataInsn);
            this.addOutputSuffix(new OddSpacer(pos));
            this.addOutputSuffix(dataAddress);
            this.addOutputSuffix(dataInsn);
        }

        protected void addOutput(DalvInsn insn) {
            this.output.add(insn);
        }

        protected void addOutputSuffix(DalvInsn insn) {
            this.output.addSuffix(insn);
        }
    }
}

