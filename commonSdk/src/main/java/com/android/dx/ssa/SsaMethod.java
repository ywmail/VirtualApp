/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.ssa;

import com.android.dx.rop.code.BasicBlockList;
import com.android.dx.rop.code.Insn;
import com.android.dx.rop.code.PlainInsn;
import com.android.dx.rop.code.RegisterSpec;
import com.android.dx.rop.code.RegisterSpecList;
import com.android.dx.rop.code.RopMethod;
import com.android.dx.rop.code.Rops;
import com.android.dx.rop.code.SourcePosition;
import com.android.dx.ssa.NormalSsaInsn;
import com.android.dx.ssa.PhiInsn;
import com.android.dx.ssa.RegisterMapper;
import com.android.dx.ssa.SsaBasicBlock;
import com.android.dx.ssa.SsaInsn;
import com.android.dx.util.IntList;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.Stack;

public final class SsaMethod {
    private ArrayList<SsaBasicBlock> blocks;
    private int entryBlockIndex;
    private int exitBlockIndex;
    private int registerCount;
    private int spareRegisterBase;
    private int borrowedSpareRegisters;
    private int maxLabel;
    private final int paramWidth;
    private final boolean isStatic;
    private SsaInsn[] definitionList;
    private ArrayList<SsaInsn>[] useList;
    private List<SsaInsn>[] unmodifiableUseList;
    private boolean backMode;

    public static SsaMethod newFromRopMethod(RopMethod ropMethod, int paramWidth, boolean isStatic) {
        SsaMethod result = new SsaMethod(ropMethod, paramWidth, isStatic);
        result.convertRopToSsaBlocks(ropMethod);
        return result;
    }

    private SsaMethod(RopMethod ropMethod, int paramWidth, boolean isStatic) {
        this.paramWidth = paramWidth;
        this.isStatic = isStatic;
        this.backMode = false;
        this.maxLabel = ropMethod.getBlocks().getMaxLabel();
        this.spareRegisterBase = this.registerCount = ropMethod.getBlocks().getRegCount();
    }

    static BitSet bitSetFromLabelList(BasicBlockList blocks, IntList labelList) {
        BitSet result = new BitSet(blocks.size());
        int sz = labelList.size();
        for (int i = 0; i < sz; ++i) {
            result.set(blocks.indexOfLabel(labelList.get(i)));
        }
        return result;
    }

    public static IntList indexListFromLabelList(BasicBlockList ropBlocks, IntList labelList) {
        IntList result = new IntList(labelList.size());
        int sz = labelList.size();
        for (int i = 0; i < sz; ++i) {
            result.add(ropBlocks.indexOfLabel(labelList.get(i)));
        }
        return result;
    }

    private void convertRopToSsaBlocks(RopMethod rmeth) {
        BasicBlockList ropBlocks = rmeth.getBlocks();
        int sz = ropBlocks.size();
        this.blocks = new ArrayList(sz + 2);
        for (int i = 0; i < sz; ++i) {
            SsaBasicBlock sbb = SsaBasicBlock.newFromRop(rmeth, i, this);
            this.blocks.add(sbb);
        }
        int origEntryBlockIndex = rmeth.getBlocks().indexOfLabel(rmeth.getFirstLabel());
        SsaBasicBlock entryBlock = this.blocks.get(origEntryBlockIndex).insertNewPredecessor();
        this.entryBlockIndex = entryBlock.getIndex();
        this.exitBlockIndex = -1;
    }

    void makeExitBlock() {
        if (this.exitBlockIndex >= 0) {
            throw new RuntimeException("must be called at most once");
        }
        this.exitBlockIndex = this.blocks.size();
        SsaBasicBlock exitBlock = new SsaBasicBlock(this.exitBlockIndex, this.maxLabel++, this);
        this.blocks.add(exitBlock);
        for (SsaBasicBlock block : this.blocks) {
            block.exitBlockFixup(exitBlock);
        }
        if (exitBlock.getPredecessors().cardinality() == 0) {
            this.blocks.remove(this.exitBlockIndex);
            this.exitBlockIndex = -1;
            --this.maxLabel;
        }
    }

    private static SsaInsn getGoto(SsaBasicBlock block) {
        return new NormalSsaInsn(new PlainInsn(Rops.GOTO, SourcePosition.NO_INFO, null, RegisterSpecList.EMPTY), block);
    }

    public SsaBasicBlock makeNewGotoBlock() {
        int newIndex = this.blocks.size();
        SsaBasicBlock newBlock = new SsaBasicBlock(newIndex, this.maxLabel++, this);
        newBlock.getInsns().add(SsaMethod.getGoto(newBlock));
        this.blocks.add(newBlock);
        return newBlock;
    }

    public int getEntryBlockIndex() {
        return this.entryBlockIndex;
    }

    public SsaBasicBlock getEntryBlock() {
        return this.blocks.get(this.entryBlockIndex);
    }

    public int getExitBlockIndex() {
        return this.exitBlockIndex;
    }

    public SsaBasicBlock getExitBlock() {
        return this.exitBlockIndex < 0 ? null : this.blocks.get(this.exitBlockIndex);
    }

    public int blockIndexToRopLabel(int bi) {
        if (bi < 0) {
            return -1;
        }
        return this.blocks.get(bi).getRopLabel();
    }

    public int getRegCount() {
        return this.registerCount;
    }

    public int getParamWidth() {
        return this.paramWidth;
    }

    public boolean isStatic() {
        return this.isStatic;
    }

    public int borrowSpareRegister(int category) {
        int result = this.spareRegisterBase + this.borrowedSpareRegisters;
        this.borrowedSpareRegisters += category;
        this.registerCount = Math.max(this.registerCount, result + category);
        return result;
    }

    public void returnSpareRegisters() {
        this.borrowedSpareRegisters = 0;
    }

    public ArrayList<SsaBasicBlock> getBlocks() {
        return this.blocks;
    }

    public BitSet computeReachability() {
        int index;
        int size = this.blocks.size();
        BitSet reachableUnvisited = new BitSet(size);
        BitSet reachableVisited = new BitSet(size);
        reachableUnvisited.set(this.getEntryBlock().getIndex());
        while ((index = reachableUnvisited.nextSetBit(0)) != -1) {
            reachableVisited.set(index);
            reachableUnvisited.or(this.blocks.get(index).getSuccessors());
            reachableUnvisited.andNot(reachableVisited);
        }
        return reachableVisited;
    }

    public void mapRegisters(RegisterMapper mapper) {
        for (SsaBasicBlock block : this.getBlocks()) {
            for (SsaInsn insn : block.getInsns()) {
                insn.mapRegisters(mapper);
            }
        }
        this.spareRegisterBase = this.registerCount = mapper.getNewRegisterCount();
    }

    public SsaInsn getDefinitionForRegister(int reg) {
        if (this.backMode) {
            throw new RuntimeException("No def list in back mode");
        }
        if (this.definitionList != null) {
            return this.definitionList[reg];
        }
        this.definitionList = new SsaInsn[this.getRegCount()];
        this.forEachInsn(new SsaInsn.Visitor(){

            @Override
            public void visitMoveInsn(NormalSsaInsn insn) {
                ((SsaMethod)SsaMethod.this).definitionList[insn.getResult().getReg()] = insn;
            }

            @Override
            public void visitPhiInsn(PhiInsn phi) {
                ((SsaMethod)SsaMethod.this).definitionList[phi.getResult().getReg()] = phi;
            }

            @Override
            public void visitNonMoveInsn(NormalSsaInsn insn) {
                RegisterSpec result = insn.getResult();
                if (result != null) {
                    ((SsaMethod)SsaMethod.this).definitionList[insn.getResult().getReg()] = insn;
                }
            }
        });
        return this.definitionList[reg];
    }

    private void buildUseList() {
        int i;
        if (this.backMode) {
            throw new RuntimeException("No use list in back mode");
        }
        this.useList = new ArrayList[this.registerCount];
        for (i = 0; i < this.registerCount; ++i) {
            this.useList[i] = new ArrayList();
        }
        this.forEachInsn(new SsaInsn.Visitor(){

            @Override
            public void visitMoveInsn(NormalSsaInsn insn) {
                this.addToUses(insn);
            }

            @Override
            public void visitPhiInsn(PhiInsn phi) {
                this.addToUses(phi);
            }

            @Override
            public void visitNonMoveInsn(NormalSsaInsn insn) {
                this.addToUses(insn);
            }

            private void addToUses(SsaInsn insn) {
                RegisterSpecList rl = insn.getSources();
                int sz = rl.size();
                for (int i = 0; i < sz; ++i) {
                    SsaMethod.this.useList[rl.get(i).getReg()].add(insn);
                }
            }
        });
        this.unmodifiableUseList = new List[this.registerCount];
        for (i = 0; i < this.registerCount; ++i) {
            this.unmodifiableUseList[i] = Collections.unmodifiableList(this.useList[i]);
        }
    }

    void onSourceChanged(SsaInsn insn, RegisterSpec oldSource, RegisterSpec newSource) {
        int reg;
        if (this.useList == null) {
            return;
        }
        if (oldSource != null) {
            reg = oldSource.getReg();
            this.useList[reg].remove(insn);
        }
        if (this.useList.length <= (reg = newSource.getReg())) {
            this.useList = null;
            return;
        }
        this.useList[reg].add(insn);
    }

    void onSourcesChanged(SsaInsn insn, RegisterSpecList oldSources) {
        if (this.useList == null) {
            return;
        }
        if (oldSources != null) {
            this.removeFromUseList(insn, oldSources);
        }
        RegisterSpecList sources = insn.getSources();
        int szNew = sources.size();
        for (int i = 0; i < szNew; ++i) {
            int reg = sources.get(i).getReg();
            this.useList[reg].add(insn);
        }
    }

    private void removeFromUseList(SsaInsn insn, RegisterSpecList oldSources) {
        if (oldSources == null) {
            return;
        }
        int szNew = oldSources.size();
        for (int i = 0; i < szNew; ++i) {
            if (this.useList[oldSources.get(i).getReg()].remove(insn)) continue;
            throw new RuntimeException("use not found");
        }
    }

    void onInsnAdded(SsaInsn insn) {
        this.onSourcesChanged(insn, null);
        this.updateOneDefinition(insn, null);
    }

    void onInsnRemoved(SsaInsn insn) {
        if (this.useList != null) {
            this.removeFromUseList(insn, insn.getSources());
        }
        RegisterSpec resultReg = insn.getResult();
        if (this.definitionList != null && resultReg != null) {
            this.definitionList[resultReg.getReg()] = null;
        }
    }

    public void onInsnsChanged() {
        this.definitionList = null;
        this.useList = null;
        this.unmodifiableUseList = null;
    }

    void updateOneDefinition(SsaInsn insn, RegisterSpec oldResult) {
        RegisterSpec resultReg;
        if (this.definitionList == null) {
            return;
        }
        if (oldResult != null) {
            int reg = oldResult.getReg();
            this.definitionList[reg] = null;
        }
        if ((resultReg = insn.getResult()) != null) {
            int reg = resultReg.getReg();
            if (this.definitionList[reg] != null) {
                throw new RuntimeException("Duplicate add of insn");
            }
            this.definitionList[resultReg.getReg()] = insn;
        }
    }

    public List<SsaInsn> getUseListForRegister(int reg) {
        if (this.unmodifiableUseList == null) {
            this.buildUseList();
        }
        return this.unmodifiableUseList[reg];
    }

    public ArrayList<SsaInsn>[] getUseListCopy() {
        if (this.useList == null) {
            this.buildUseList();
        }
        ArrayList[] useListCopy = new ArrayList[this.registerCount];
        for (int i = 0; i < this.registerCount; ++i) {
            useListCopy[i] = new ArrayList<SsaInsn>(this.useList[i]);
        }
        return useListCopy;
    }

    public boolean isRegALocal(RegisterSpec spec) {
        SsaInsn defn = this.getDefinitionForRegister(spec.getReg());
        if (defn == null) {
            return false;
        }
        if (defn.getLocalAssignment() != null) {
            return true;
        }
        for (SsaInsn use : this.getUseListForRegister(spec.getReg())) {
            Insn insn = use.getOriginalRopInsn();
            if (insn == null || insn.getOpcode().getOpcode() != 54) continue;
            return true;
        }
        return false;
    }

    void setNewRegCount(int newRegCount) {
        this.spareRegisterBase = this.registerCount = newRegCount;
        this.onInsnsChanged();
    }

    public int makeNewSsaReg() {
        int reg = this.registerCount++;
        this.spareRegisterBase = this.registerCount;
        this.onInsnsChanged();
        return reg;
    }

    public void forEachInsn(SsaInsn.Visitor visitor) {
        for (SsaBasicBlock block : this.blocks) {
            block.forEachInsn(visitor);
        }
    }

    public void forEachPhiInsn(PhiInsn.Visitor v) {
        for (SsaBasicBlock block : this.blocks) {
            block.forEachPhiInsn(v);
        }
    }

    public void forEachBlockDepthFirst(boolean reverse, SsaBasicBlock.Visitor v) {
        SsaBasicBlock rootBlock;
        BitSet visited = new BitSet(this.blocks.size());
        Stack<SsaBasicBlock> stack = new Stack<SsaBasicBlock>();
        SsaBasicBlock ssaBasicBlock = rootBlock = reverse ? this.getExitBlock() : this.getEntryBlock();
        if (rootBlock == null) {
            return;
        }
        stack.add(null);
        stack.add(rootBlock);
        while (stack.size() > 0) {
            SsaBasicBlock cur = (SsaBasicBlock)stack.pop();
            SsaBasicBlock parent = (SsaBasicBlock)stack.pop();
            if (visited.get(cur.getIndex())) continue;
            BitSet children = reverse ? cur.getPredecessors() : cur.getSuccessors();
            int i = children.nextSetBit(0);
            while (i >= 0) {
                stack.add(cur);
                stack.add(this.blocks.get(i));
                i = children.nextSetBit(i + 1);
            }
            visited.set(cur.getIndex());
            v.visitBlock(cur, parent);
        }
    }

    public void forEachBlockDepthFirstDom(SsaBasicBlock.Visitor v) {
        BitSet visited = new BitSet(this.getBlocks().size());
        Stack<SsaBasicBlock> stack = new Stack<SsaBasicBlock>();
        stack.add(this.getEntryBlock());
        while (stack.size() > 0) {
            SsaBasicBlock cur = (SsaBasicBlock)stack.pop();
            ArrayList<SsaBasicBlock> curDomChildren = cur.getDomChildren();
            if (visited.get(cur.getIndex())) continue;
            for (int i = curDomChildren.size() - 1; i >= 0; --i) {
                SsaBasicBlock child = curDomChildren.get(i);
                stack.add(child);
            }
            visited.set(cur.getIndex());
            v.visitBlock(cur, null);
        }
    }

    public void deleteInsns(Set<SsaInsn> deletedInsns) {
        for (SsaInsn deletedInsn : deletedInsns) {
            int insnsSz;
            SsaInsn lastInsn;
            SsaBasicBlock block = deletedInsn.getBlock();
            ArrayList<SsaInsn> insns = block.getInsns();
            for (int i = insns.size() - 1; i >= 0; --i) {
                SsaInsn insn = insns.get(i);
                if (deletedInsn != insn) continue;
                this.onInsnRemoved(insn);
                insns.remove(i);
                break;
            }
            SsaInsn ssaInsn = lastInsn = (insnsSz = insns.size()) == 0 ? null : insns.get(insnsSz - 1);
            if (block == this.getExitBlock() || insnsSz != 0 && lastInsn.getOriginalRopInsn() != null && lastInsn.getOriginalRopInsn().getOpcode().getBranchingness() != 1) continue;
            PlainInsn gotoInsn = new PlainInsn(Rops.GOTO, SourcePosition.NO_INFO, null, RegisterSpecList.EMPTY);
            insns.add(SsaInsn.makeFromRop(gotoInsn, block));
            BitSet succs = block.getSuccessors();
            int i = succs.nextSetBit(0);
            while (i >= 0) {
                if (i != block.getPrimarySuccessorIndex()) {
                    block.removeSuccessor(i);
                }
                i = succs.nextSetBit(i + 1);
            }
        }
    }

    public void setBackMode() {
        this.backMode = true;
        this.useList = null;
        this.definitionList = null;
    }
}

