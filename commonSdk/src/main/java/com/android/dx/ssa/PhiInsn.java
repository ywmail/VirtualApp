/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.ssa;

import com.android.dx.rop.code.Insn;
import com.android.dx.rop.code.LocalItem;
import com.android.dx.rop.code.RegisterSpec;
import com.android.dx.rop.code.RegisterSpecList;
import com.android.dx.rop.code.Rop;
import com.android.dx.rop.code.SourcePosition;
import com.android.dx.rop.type.Type;
import com.android.dx.rop.type.TypeBearer;
import com.android.dx.ssa.Optimizer;
import com.android.dx.ssa.RegisterMapper;
import com.android.dx.ssa.SsaBasicBlock;
import com.android.dx.ssa.SsaInsn;
import com.android.dx.ssa.SsaMethod;
import com.android.dx.util.Hex;
import java.util.ArrayList;
import java.util.List;

public final class PhiInsn
extends SsaInsn {
    private final int ropResultReg;
    private final ArrayList<Operand> operands = new ArrayList();
    private RegisterSpecList sources;

    public PhiInsn(RegisterSpec resultReg, SsaBasicBlock block) {
        super(resultReg, block);
        this.ropResultReg = resultReg.getReg();
    }

    public PhiInsn(int resultReg, SsaBasicBlock block) {
        super(RegisterSpec.make(resultReg, Type.VOID), block);
        this.ropResultReg = resultReg;
    }

    @Override
    public PhiInsn clone() {
        throw new UnsupportedOperationException("can't clone phi");
    }

    public void updateSourcesToDefinitions(SsaMethod ssaMeth) {
        for (Operand o : this.operands) {
            RegisterSpec def = ssaMeth.getDefinitionForRegister(o.regSpec.getReg()).getResult();
            o.regSpec = o.regSpec.withType(def.getType());
        }
        this.sources = null;
    }

    public void changeResultType(TypeBearer type, LocalItem local) {
        this.setResult(RegisterSpec.makeLocalOptional(this.getResult().getReg(), type, local));
    }

    public int getRopResultReg() {
        return this.ropResultReg;
    }

    public void addPhiOperand(RegisterSpec registerSpec, SsaBasicBlock predBlock) {
        this.operands.add(new Operand(registerSpec, predBlock.getIndex(), predBlock.getRopLabel()));
        this.sources = null;
    }

    public void removePhiRegister(RegisterSpec registerSpec) {
        ArrayList<Operand> operandsToRemove = new ArrayList<Operand>();
        for (Operand o : this.operands) {
            if (o.regSpec.getReg() != registerSpec.getReg()) continue;
            operandsToRemove.add(o);
        }
        this.operands.removeAll(operandsToRemove);
        this.sources = null;
    }

    public int predBlockIndexForSourcesIndex(int sourcesIndex) {
        return this.operands.get((int)sourcesIndex).blockIndex;
    }

    @Override
    public Rop getOpcode() {
        return null;
    }

    @Override
    public Insn getOriginalRopInsn() {
        return null;
    }

    @Override
    public boolean canThrow() {
        return false;
    }

    @Override
    public RegisterSpecList getSources() {
        if (this.sources != null) {
            return this.sources;
        }
        if (this.operands.size() == 0) {
            return RegisterSpecList.EMPTY;
        }
        int szSources = this.operands.size();
        this.sources = new RegisterSpecList(szSources);
        for (int i = 0; i < szSources; ++i) {
            Operand o = this.operands.get(i);
            this.sources.set(i, o.regSpec);
        }
        this.sources.setImmutable();
        return this.sources;
    }

    @Override
    public boolean isRegASource(int reg) {
        for (Operand o : this.operands) {
            if (o.regSpec.getReg() != reg) continue;
            return true;
        }
        return false;
    }

    public boolean areAllOperandsEqual() {
        if (this.operands.size() == 0) {
            return true;
        }
        int firstReg = this.operands.get((int)0).regSpec.getReg();
        for (Operand o : this.operands) {
            if (firstReg == o.regSpec.getReg()) continue;
            return false;
        }
        return true;
    }

    @Override
    public final void mapSourceRegisters(RegisterMapper mapper) {
        for (Operand o : this.operands) {
            RegisterSpec old = o.regSpec;
            o.regSpec = mapper.map(old);
            if (old == o.regSpec) continue;
            this.getBlock().getParent().onSourceChanged(this, old, o.regSpec);
        }
        this.sources = null;
    }

    @Override
    public Insn toRopInsn() {
        throw new IllegalArgumentException("Cannot convert phi insns to rop form");
    }

    public List<SsaBasicBlock> predBlocksForReg(int reg, SsaMethod ssaMeth) {
        ArrayList<SsaBasicBlock> ret = new ArrayList<SsaBasicBlock>();
        for (Operand o : this.operands) {
            if (o.regSpec.getReg() != reg) continue;
            ret.add(ssaMeth.getBlocks().get(o.blockIndex));
        }
        return ret;
    }

    @Override
    public boolean isPhiOrMove() {
        return true;
    }

    @Override
    public boolean hasSideEffect() {
        return Optimizer.getPreserveLocals() && this.getLocalAssignment() != null;
    }

    @Override
    public void accept(SsaInsn.Visitor v) {
        v.visitPhiInsn(this);
    }

    @Override
    public String toHuman() {
        return this.toHumanWithInline(null);
    }

    protected final String toHumanWithInline(String extra) {
        RegisterSpec result;
        StringBuilder sb = new StringBuilder(80);
        sb.append(SourcePosition.NO_INFO);
        sb.append(": phi");
        if (extra != null) {
            sb.append("(");
            sb.append(extra);
            sb.append(")");
        }
        if ((result = this.getResult()) == null) {
            sb.append(" .");
        } else {
            sb.append(" ");
            sb.append(result.toHuman());
        }
        sb.append(" <-");
        int sz = this.getSources().size();
        if (sz == 0) {
            sb.append(" .");
        } else {
            for (int i = 0; i < sz; ++i) {
                sb.append(" ");
                sb.append(this.sources.get(i).toHuman() + "[b=" + Hex.u2(this.operands.get((int)i).ropLabel) + "]");
            }
        }
        return sb.toString();
    }

    public static interface Visitor {
        public void visitPhiInsn(PhiInsn var1);
    }

    private static class Operand {
        public RegisterSpec regSpec;
        public final int blockIndex;
        public final int ropLabel;

        public Operand(RegisterSpec regSpec, int blockIndex, int ropLabel) {
            this.regSpec = regSpec;
            this.blockIndex = blockIndex;
            this.ropLabel = ropLabel;
        }
    }
}

