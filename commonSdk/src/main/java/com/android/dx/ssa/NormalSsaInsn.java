/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.ssa;

import com.android.dx.rop.code.Insn;
import com.android.dx.rop.code.LocalItem;
import com.android.dx.rop.code.RegisterSpec;
import com.android.dx.rop.code.RegisterSpecList;
import com.android.dx.rop.code.Rop;
import com.android.dx.ssa.Optimizer;
import com.android.dx.ssa.RegisterMapper;
import com.android.dx.ssa.SsaBasicBlock;
import com.android.dx.ssa.SsaInsn;

public final class NormalSsaInsn
extends SsaInsn
implements Cloneable {
    private Insn insn;

    NormalSsaInsn(Insn insn, SsaBasicBlock block) {
        super(insn.getResult(), block);
        this.insn = insn;
    }

    @Override
    public final void mapSourceRegisters(RegisterMapper mapper) {
        RegisterSpecList oldSources = this.insn.getSources();
        RegisterSpecList newSources = mapper.map(oldSources);
        if (newSources != oldSources) {
            this.insn = this.insn.withNewRegisters(this.getResult(), newSources);
            this.getBlock().getParent().onSourcesChanged(this, oldSources);
        }
    }

    public final void changeOneSource(int index, RegisterSpec newSpec) {
        RegisterSpecList origSources = this.insn.getSources();
        int sz = origSources.size();
        RegisterSpecList newSources = new RegisterSpecList(sz);
        for (int i = 0; i < sz; ++i) {
            newSources.set(i, i == index ? newSpec : origSources.get(i));
        }
        newSources.setImmutable();
        RegisterSpec origSpec = origSources.get(index);
        if (origSpec.getReg() != newSpec.getReg()) {
            this.getBlock().getParent().onSourceChanged(this, origSpec, newSpec);
        }
        this.insn = this.insn.withNewRegisters(this.getResult(), newSources);
    }

    public final void setNewSources(RegisterSpecList newSources) {
        RegisterSpecList origSources = this.insn.getSources();
        if (origSources.size() != newSources.size()) {
            throw new RuntimeException("Sources counts don't match");
        }
        this.insn = this.insn.withNewRegisters(this.getResult(), newSources);
    }

    @Override
    public NormalSsaInsn clone() {
        return (NormalSsaInsn)super.clone();
    }

    @Override
    public RegisterSpecList getSources() {
        return this.insn.getSources();
    }

    @Override
    public String toHuman() {
        return this.toRopInsn().toHuman();
    }

    @Override
    public Insn toRopInsn() {
        return this.insn.withNewRegisters(this.getResult(), this.insn.getSources());
    }

    @Override
    public Rop getOpcode() {
        return this.insn.getOpcode();
    }

    @Override
    public Insn getOriginalRopInsn() {
        return this.insn;
    }

    @Override
    public RegisterSpec getLocalAssignment() {
        RegisterSpec assignment = this.insn.getOpcode().getOpcode() == 54 ? this.insn.getSources().get(0) : this.getResult();
        if (assignment == null) {
            return null;
        }
        LocalItem local = assignment.getLocalItem();
        if (local == null) {
            return null;
        }
        return assignment;
    }

    public void upgradeToLiteral() {
        RegisterSpecList oldSources = this.insn.getSources();
        this.insn = this.insn.withSourceLiteral();
        this.getBlock().getParent().onSourcesChanged(this, oldSources);
    }

    @Override
    public boolean isNormalMoveInsn() {
        return this.insn.getOpcode().getOpcode() == 2;
    }

    @Override
    public boolean isMoveException() {
        return this.insn.getOpcode().getOpcode() == 4;
    }

    @Override
    public boolean canThrow() {
        return this.insn.canThrow();
    }

    @Override
    public void accept(SsaInsn.Visitor v) {
        if (this.isNormalMoveInsn()) {
            v.visitMoveInsn(this);
        } else {
            v.visitNonMoveInsn(this);
        }
    }

    @Override
    public boolean isPhiOrMove() {
        return this.isNormalMoveInsn();
    }

    @Override
    public boolean hasSideEffect() {
        Rop opcode = this.getOpcode();
        if (opcode.getBranchingness() != 1) {
            return true;
        }
        boolean hasLocalSideEffect = Optimizer.getPreserveLocals() && this.getLocalAssignment() != null;
        switch (opcode.getOpcode()) {
            case 2: 
            case 5: 
            case 55: {
                return hasLocalSideEffect;
            }
        }
        return true;
    }
}

