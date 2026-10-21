/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.ssa;

import com.android.dx.rop.code.Exceptions;
import com.android.dx.rop.code.FillArrayDataInsn;
import com.android.dx.rop.code.Insn;
import com.android.dx.rop.code.PlainCstInsn;
import com.android.dx.rop.code.PlainInsn;
import com.android.dx.rop.code.RegisterSpec;
import com.android.dx.rop.code.RegisterSpecList;
import com.android.dx.rop.code.Rop;
import com.android.dx.rop.code.Rops;
import com.android.dx.rop.code.ThrowingCstInsn;
import com.android.dx.rop.code.ThrowingInsn;
import com.android.dx.rop.cst.Constant;
import com.android.dx.rop.cst.CstLiteralBits;
import com.android.dx.rop.cst.CstMethodRef;
import com.android.dx.rop.cst.CstNat;
import com.android.dx.rop.cst.CstString;
import com.android.dx.rop.cst.CstType;
import com.android.dx.rop.cst.TypedConstant;
import com.android.dx.rop.cst.Zeroes;
import com.android.dx.rop.type.StdTypeList;
import com.android.dx.rop.type.Type;
import com.android.dx.rop.type.TypeBearer;
import com.android.dx.ssa.NormalSsaInsn;
import com.android.dx.ssa.PhiInsn;
import com.android.dx.ssa.RegisterMapper;
import com.android.dx.ssa.SsaBasicBlock;
import com.android.dx.ssa.SsaConverter;
import com.android.dx.ssa.SsaInsn;
import com.android.dx.ssa.SsaMethod;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashSet;
import java.util.List;

public class EscapeAnalysis {
    private final SsaMethod ssaMeth;
    private final int regCount;
    private final ArrayList<EscapeSet> latticeValues;

    private EscapeAnalysis(SsaMethod ssaMeth) {
        this.ssaMeth = ssaMeth;
        this.regCount = ssaMeth.getRegCount();
        this.latticeValues = new ArrayList();
    }

    private int findSetIndex(RegisterSpec reg) {
        int i;
        for (i = 0; i < this.latticeValues.size(); ++i) {
            EscapeSet e = this.latticeValues.get(i);
            if (!e.regSet.get(reg.getReg())) continue;
            return i;
        }
        return i;
    }

    private SsaInsn getInsnForMove(SsaInsn moveInsn) {
        int pred = moveInsn.getBlock().getPredecessors().nextSetBit(0);
        ArrayList<SsaInsn> predInsns = this.ssaMeth.getBlocks().get(pred).getInsns();
        return predInsns.get(predInsns.size() - 1);
    }

    private SsaInsn getMoveForInsn(SsaInsn insn) {
        int succ = insn.getBlock().getSuccessors().nextSetBit(0);
        ArrayList<SsaInsn> succInsns = this.ssaMeth.getBlocks().get(succ).getInsns();
        return succInsns.get(0);
    }

    private void addEdge(EscapeSet parentSet, EscapeSet childSet) {
        if (!childSet.parentSets.contains(parentSet)) {
            childSet.parentSets.add(parentSet);
        }
        if (!parentSet.childSets.contains(childSet)) {
            parentSet.childSets.add(childSet);
        }
    }

    private void replaceNode(EscapeSet newNode, EscapeSet oldNode) {
        for (EscapeSet e : oldNode.parentSets) {
            e.childSets.remove(oldNode);
            e.childSets.add(newNode);
            newNode.parentSets.add(e);
        }
        for (EscapeSet e : oldNode.childSets) {
            e.parentSets.remove(oldNode);
            e.parentSets.add(newNode);
            newNode.childSets.add(e);
        }
    }

    public static void process(SsaMethod ssaMethod) {
        new EscapeAnalysis(ssaMethod).run();
    }

    private void processInsn(SsaInsn insn) {
        int op = insn.getOpcode().getOpcode();
        RegisterSpec result = insn.getResult();
        if (op == 56 && result.getTypeBearer().getBasicType() == 9) {
            EscapeSet escSet = this.processMoveResultPseudoInsn(insn);
            this.processRegister(result, escSet);
        } else if (op == 3 && result.getTypeBearer().getBasicType() == 9) {
            EscapeSet escSet = new EscapeSet(result.getReg(), this.regCount, EscapeState.NONE);
            this.latticeValues.add(escSet);
            this.processRegister(result, escSet);
        } else if (op == 55 && result.getTypeBearer().getBasicType() == 9) {
            EscapeSet escSet = new EscapeSet(result.getReg(), this.regCount, EscapeState.NONE);
            this.latticeValues.add(escSet);
            this.processRegister(result, escSet);
        }
    }

    private EscapeSet processMoveResultPseudoInsn(SsaInsn insn) {
        EscapeSet escSet;
        RegisterSpec result = insn.getResult();
        SsaInsn prevSsaInsn = this.getInsnForMove(insn);
        int prevOpcode = prevSsaInsn.getOpcode().getOpcode();
        switch (prevOpcode) {
            case 5: 
            case 40: {
                escSet = new EscapeSet(result.getReg(), this.regCount, EscapeState.NONE);
                break;
            }
            case 41: 
            case 42: {
                RegisterSpec prevSource = prevSsaInsn.getSources().get(0);
                if (prevSource.getTypeBearer().isConstant()) {
                    escSet = new EscapeSet(result.getReg(), this.regCount, EscapeState.NONE);
                    escSet.replaceableArray = true;
                    break;
                }
                escSet = new EscapeSet(result.getReg(), this.regCount, EscapeState.GLOBAL);
                break;
            }
            case 46: {
                escSet = new EscapeSet(result.getReg(), this.regCount, EscapeState.GLOBAL);
                break;
            }
            case 38: 
            case 43: 
            case 45: {
                RegisterSpec prevSource = prevSsaInsn.getSources().get(0);
                int setIndex = this.findSetIndex(prevSource);
                if (setIndex != this.latticeValues.size()) {
                    EscapeSet escSet2 = this.latticeValues.get(setIndex);
                    escSet2.regSet.set(result.getReg());
                    return escSet2;
                }
                if (prevSource.getType() == Type.KNOWN_NULL) {
                    escSet = new EscapeSet(result.getReg(), this.regCount, EscapeState.NONE);
                    break;
                }
                escSet = new EscapeSet(result.getReg(), this.regCount, EscapeState.GLOBAL);
                break;
            }
            default: {
                return null;
            }
        }
        this.latticeValues.add(escSet);
        return escSet;
    }

    private void processRegister(RegisterSpec result, EscapeSet escSet) {
        ArrayList<RegisterSpec> regWorklist = new ArrayList<RegisterSpec>();
        regWorklist.add(result);
        while (!regWorklist.isEmpty()) {
            int listSize = regWorklist.size() - 1;
            RegisterSpec def = (RegisterSpec)regWorklist.remove(listSize);
            List<SsaInsn> useList = this.ssaMeth.getUseListForRegister(def.getReg());
            for (SsaInsn use : useList) {
                Rop useOpcode = use.getOpcode();
                if (useOpcode == null) {
                    this.processPhiUse(use, escSet, regWorklist);
                    continue;
                }
                this.processUse(def, use, escSet, regWorklist);
            }
        }
    }

    private void processPhiUse(SsaInsn use, EscapeSet escSet, ArrayList<RegisterSpec> regWorklist) {
        int setIndex = this.findSetIndex(use.getResult());
        if (setIndex != this.latticeValues.size()) {
            EscapeSet mergeSet = this.latticeValues.get(setIndex);
            if (mergeSet != escSet) {
                escSet.replaceableArray = false;
                escSet.regSet.or(mergeSet.regSet);
                if (escSet.escape.compareTo(mergeSet.escape) < 0) {
                    escSet.escape = mergeSet.escape;
                }
                this.replaceNode(escSet, mergeSet);
                this.latticeValues.remove(setIndex);
            }
        } else {
            escSet.regSet.set(use.getResult().getReg());
            regWorklist.add(use.getResult());
        }
    }

    private void processUse(RegisterSpec def, SsaInsn use, EscapeSet escSet, ArrayList<RegisterSpec> regWorklist) {
        int useOpcode = use.getOpcode().getOpcode();
        switch (useOpcode) {
            case 2: {
                escSet.regSet.set(use.getResult().getReg());
                regWorklist.add(use.getResult());
                break;
            }
            case 7: 
            case 8: 
            case 43: {
                if (escSet.escape.compareTo(EscapeState.METHOD) >= 0) break;
                escSet.escape = EscapeState.METHOD;
                break;
            }
            case 39: {
                RegisterSpec putIndex = use.getSources().get(2);
                if (!putIndex.getTypeBearer().isConstant()) {
                    escSet.replaceableArray = false;
                }
            }
            case 47: {
                RegisterSpec putValue = use.getSources().get(0);
                if (putValue.getTypeBearer().getBasicType() != 9) break;
                escSet.replaceableArray = false;
                RegisterSpecList sources = use.getSources();
                if (sources.get(0).getReg() == def.getReg()) {
                    int setIndex = this.findSetIndex(sources.get(1));
                    if (setIndex == this.latticeValues.size()) break;
                    EscapeSet parentSet = this.latticeValues.get(setIndex);
                    this.addEdge(parentSet, escSet);
                    if (escSet.escape.compareTo(parentSet.escape) >= 0) break;
                    escSet.escape = parentSet.escape;
                    break;
                }
                int setIndex = this.findSetIndex(sources.get(0));
                if (setIndex == this.latticeValues.size()) break;
                EscapeSet childSet = this.latticeValues.get(setIndex);
                this.addEdge(escSet, childSet);
                if (childSet.escape.compareTo(escSet.escape) >= 0) break;
                childSet.escape = escSet.escape;
                break;
            }
            case 38: {
                RegisterSpec getIndex = use.getSources().get(1);
                if (getIndex.getTypeBearer().isConstant()) break;
                escSet.replaceableArray = false;
                break;
            }
            case 48: {
                escSet.escape = EscapeState.GLOBAL;
                break;
            }
            case 33: 
            case 35: 
            case 49: 
            case 50: 
            case 51: 
            case 52: 
            case 53: {
                escSet.escape = EscapeState.INTER;
                break;
            }
        }
    }

    private void scalarReplacement() {
        for (EscapeSet escSet : this.latticeValues) {
            if (!escSet.replaceableArray || escSet.escape != EscapeState.NONE) continue;
            int e = escSet.regSet.nextSetBit(0);
            SsaInsn def = this.ssaMeth.getDefinitionForRegister(e);
            SsaInsn prev = this.getInsnForMove(def);
            TypeBearer lengthReg = prev.getSources().get(0).getTypeBearer();
            int length = ((CstLiteralBits)lengthReg).getIntBits();
            ArrayList<RegisterSpec> newRegs = new ArrayList<RegisterSpec>(length);
            HashSet<SsaInsn> deletedInsns = new HashSet<SsaInsn>();
            this.replaceDef(def, prev, length, newRegs);
            deletedInsns.add(prev);
            deletedInsns.add(def);
            List<SsaInsn> useList = this.ssaMeth.getUseListForRegister(e);
            for (SsaInsn use : useList) {
                this.replaceUse(use, prev, newRegs, deletedInsns);
                deletedInsns.add(use);
            }
            this.ssaMeth.deleteInsns(deletedInsns);
            this.ssaMeth.onInsnsChanged();
            SsaConverter.updateSsaMethod(this.ssaMeth, this.regCount);
            this.movePropagate();
        }
    }

    private void replaceDef(SsaInsn def, SsaInsn prev, int length, ArrayList<RegisterSpec> newRegs) {
        Type resultType = def.getResult().getType();
        for (int i = 0; i < length; ++i) {
            Constant newZero = Zeroes.zeroFor(resultType.getComponentType());
            TypedConstant typedZero = (TypedConstant)newZero;
            RegisterSpec newReg = RegisterSpec.make(this.ssaMeth.makeNewSsaReg(), typedZero);
            newRegs.add(newReg);
            this.insertPlainInsnBefore(def, RegisterSpecList.EMPTY, newReg, 5, newZero);
        }
    }

    private void replaceUse(SsaInsn use, SsaInsn prev, ArrayList<RegisterSpec> newRegs, HashSet<SsaInsn> deletedInsns) {
        int length = newRegs.size();
        switch (use.getOpcode().getOpcode()) {
            case 38: {
                SsaInsn next = this.getMoveForInsn(use);
                RegisterSpecList sources = use.getSources();
                CstLiteralBits indexReg = (CstLiteralBits)sources.get(1).getTypeBearer();
                int index = indexReg.getIntBits();
                if (index < length) {
                    RegisterSpec source = newRegs.get(index);
                    RegisterSpec result = source.withReg(next.getResult().getReg());
                    this.insertPlainInsnBefore(next, RegisterSpecList.make(source), result, 2, null);
                } else {
                    this.insertExceptionThrow(next, sources.get(1), deletedInsns);
                    deletedInsns.add(next.getBlock().getInsns().get(2));
                }
                deletedInsns.add(next);
                break;
            }
            case 39: {
                RegisterSpecList sources = use.getSources();
                CstLiteralBits indexReg = (CstLiteralBits)sources.get(2).getTypeBearer();
                int index = indexReg.getIntBits();
                if (index < length) {
                    RegisterSpec source = sources.get(0);
                    RegisterSpec result = source.withReg(newRegs.get(index).getReg());
                    this.insertPlainInsnBefore(use, RegisterSpecList.make(source), result, 2, null);
                    newRegs.set(index, result.withSimpleType());
                    break;
                }
                this.insertExceptionThrow(use, sources.get(2), deletedInsns);
                break;
            }
            case 34: {
                TypeBearer lengthReg = prev.getSources().get(0).getTypeBearer();
                SsaInsn next = this.getMoveForInsn(use);
                this.insertPlainInsnBefore(next, RegisterSpecList.EMPTY, next.getResult(), 5, (Constant)((Object)lengthReg));
                deletedInsns.add(next);
                break;
            }
            case 54: {
                break;
            }
            case 57: {
                Insn ropUse = use.getOriginalRopInsn();
                FillArrayDataInsn fill = (FillArrayDataInsn)ropUse;
                ArrayList<Constant> constList = fill.getInitValues();
                for (int i = 0; i < length; ++i) {
                    RegisterSpec newFill = RegisterSpec.make(newRegs.get(i).getReg(), (TypeBearer)((Object)constList.get(i)));
                    this.insertPlainInsnBefore(use, RegisterSpecList.EMPTY, newFill, 5, constList.get(i));
                    newRegs.set(i, newFill);
                }
                break;
            }
        }
    }

    private void movePropagate() {
        for (int i = 0; i < this.ssaMeth.getRegCount(); ++i) {
            SsaInsn insn = this.ssaMeth.getDefinitionForRegister(i);
            if (insn == null || insn.getOpcode() == null || insn.getOpcode().getOpcode() != 2) continue;
            ArrayList<SsaInsn>[] useList = this.ssaMeth.getUseListCopy();
            final RegisterSpec source = insn.getSources().get(0);
            final RegisterSpec result = insn.getResult();
            if (source.getReg() < this.regCount && result.getReg() < this.regCount) continue;
            RegisterMapper mapper = new RegisterMapper(){

                @Override
                public int getNewRegisterCount() {
                    return EscapeAnalysis.this.ssaMeth.getRegCount();
                }

                @Override
                public RegisterSpec map(RegisterSpec registerSpec) {
                    if (registerSpec.getReg() == result.getReg()) {
                        return source;
                    }
                    return registerSpec;
                }
            };
            for (SsaInsn use : useList[result.getReg()]) {
                use.mapSourceRegisters(mapper);
            }
        }
    }

    private void run() {
        this.ssaMeth.forEachBlockDepthFirstDom(new SsaBasicBlock.Visitor(){

            @Override
            public void visitBlock(SsaBasicBlock block, SsaBasicBlock unused) {
                block.forEachInsn(new SsaInsn.Visitor(){

                    @Override
                    public void visitMoveInsn(NormalSsaInsn insn) {
                    }

                    @Override
                    public void visitPhiInsn(PhiInsn insn) {
                    }

                    @Override
                    public void visitNonMoveInsn(NormalSsaInsn insn) {
                        EscapeAnalysis.this.processInsn(insn);
                    }
                });
            }
        });
        for (EscapeSet e : this.latticeValues) {
            if (e.escape == EscapeState.NONE) continue;
            for (EscapeSet field : e.childSets) {
                if (e.escape.compareTo(field.escape) <= 0) continue;
                field.escape = e.escape;
            }
        }
        this.scalarReplacement();
    }

    private void insertExceptionThrow(SsaInsn insn, RegisterSpec index, HashSet<SsaInsn> deletedInsns) {
        CstType exception = new CstType(Exceptions.TYPE_ArrayIndexOutOfBoundsException);
        this.insertThrowingInsnBefore(insn, RegisterSpecList.EMPTY, null, 40, exception);
        SsaBasicBlock currBlock = insn.getBlock();
        SsaBasicBlock newBlock = currBlock.insertNewSuccessor(currBlock.getPrimarySuccessor());
        SsaInsn newInsn = newBlock.getInsns().get(0);
        RegisterSpec newReg = RegisterSpec.make(this.ssaMeth.makeNewSsaReg(), exception);
        this.insertPlainInsnBefore(newInsn, RegisterSpecList.EMPTY, newReg, 56, null);
        SsaBasicBlock newBlock2 = newBlock.insertNewSuccessor(newBlock.getPrimarySuccessor());
        SsaInsn newInsn2 = newBlock2.getInsns().get(0);
        CstNat newNat = new CstNat(new CstString("<init>"), new CstString("(I)V"));
        CstMethodRef newRef = new CstMethodRef(exception, newNat);
        this.insertThrowingInsnBefore(newInsn2, RegisterSpecList.make(newReg, index), null, 52, newRef);
        deletedInsns.add(newInsn2);
        SsaBasicBlock newBlock3 = newBlock2.insertNewSuccessor(newBlock2.getPrimarySuccessor());
        SsaInsn newInsn3 = newBlock3.getInsns().get(0);
        this.insertThrowingInsnBefore(newInsn3, RegisterSpecList.make(newReg), null, 35, null);
        newBlock3.replaceSuccessor(newBlock3.getPrimarySuccessorIndex(), this.ssaMeth.getExitBlock().getIndex());
        deletedInsns.add(newInsn3);
    }

    private void insertPlainInsnBefore(SsaInsn insn, RegisterSpecList newSources, RegisterSpec newResult, int newOpcode, Constant cst) {
        Insn originalRopInsn = insn.getOriginalRopInsn();
        Rop newRop = newOpcode == 56 ? Rops.opMoveResultPseudo(newResult.getType()) : Rops.ropFor(newOpcode, newResult, newSources, cst);
        Insn newRopInsn = cst == null ? new PlainInsn(newRop, originalRopInsn.getPosition(), newResult, newSources) : new PlainCstInsn(newRop, originalRopInsn.getPosition(), newResult, newSources, cst);
        NormalSsaInsn newInsn = new NormalSsaInsn(newRopInsn, insn.getBlock());
        ArrayList<SsaInsn> insns = insn.getBlock().getInsns();
        insns.add(insns.lastIndexOf(insn), newInsn);
        this.ssaMeth.onInsnAdded(newInsn);
    }

    private void insertThrowingInsnBefore(SsaInsn insn, RegisterSpecList newSources, RegisterSpec newResult, int newOpcode, Constant cst) {
        Insn origRopInsn = insn.getOriginalRopInsn();
        Rop newRop = Rops.ropFor(newOpcode, newResult, newSources, cst);
        Insn newRopInsn = cst == null ? new ThrowingInsn(newRop, origRopInsn.getPosition(), newSources, StdTypeList.EMPTY) : new ThrowingCstInsn(newRop, origRopInsn.getPosition(), newSources, StdTypeList.EMPTY, cst);
        NormalSsaInsn newInsn = new NormalSsaInsn(newRopInsn, insn.getBlock());
        ArrayList<SsaInsn> insns = insn.getBlock().getInsns();
        insns.add(insns.lastIndexOf(insn), newInsn);
        this.ssaMeth.onInsnAdded(newInsn);
    }

    public static enum EscapeState {
        TOP,
        NONE,
        METHOD,
        INTER,
        GLOBAL;

    }

    static class EscapeSet {
        BitSet regSet;
        EscapeState escape;
        ArrayList<EscapeSet> childSets;
        ArrayList<EscapeSet> parentSets;
        boolean replaceableArray;

        EscapeSet(int reg, int size, EscapeState escState) {
            this.regSet = new BitSet(size);
            this.regSet.set(reg);
            this.escape = escState;
            this.childSets = new ArrayList();
            this.parentSets = new ArrayList();
            this.replaceableArray = false;
        }
    }
}

