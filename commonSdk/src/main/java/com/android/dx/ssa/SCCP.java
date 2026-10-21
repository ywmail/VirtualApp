/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.ssa;

import com.android.dx.rop.code.CstInsn;
import com.android.dx.rop.code.Insn;
import com.android.dx.rop.code.PlainInsn;
import com.android.dx.rop.code.RegisterSpec;
import com.android.dx.rop.code.RegisterSpecList;
import com.android.dx.rop.code.Rop;
import com.android.dx.rop.code.Rops;
import com.android.dx.rop.cst.Constant;
import com.android.dx.rop.cst.CstInteger;
import com.android.dx.rop.cst.TypedConstant;
import com.android.dx.rop.type.TypeBearer;
import com.android.dx.ssa.NormalSsaInsn;
import com.android.dx.ssa.PhiInsn;
import com.android.dx.ssa.SsaBasicBlock;
import com.android.dx.ssa.SsaInsn;
import com.android.dx.ssa.SsaMethod;
import java.util.ArrayList;
import java.util.BitSet;

public class SCCP {
    private static final int TOP = 0;
    private static final int CONSTANT = 1;
    private static final int VARYING = 2;
    private final SsaMethod ssaMeth;
    private final int regCount;
    private final int[] latticeValues;
    private final Constant[] latticeConstants;
    private final ArrayList<SsaBasicBlock> cfgWorklist;
    private final ArrayList<SsaBasicBlock> cfgPhiWorklist;
    private final BitSet executableBlocks;
    private final ArrayList<SsaInsn> ssaWorklist;
    private final ArrayList<SsaInsn> varyingWorklist;
    private final ArrayList<SsaInsn> branchWorklist;

    private SCCP(SsaMethod ssaMeth) {
        this.ssaMeth = ssaMeth;
        this.regCount = ssaMeth.getRegCount();
        this.latticeValues = new int[this.regCount];
        this.latticeConstants = new Constant[this.regCount];
        this.cfgWorklist = new ArrayList();
        this.cfgPhiWorklist = new ArrayList();
        this.executableBlocks = new BitSet(ssaMeth.getBlocks().size());
        this.ssaWorklist = new ArrayList();
        this.varyingWorklist = new ArrayList();
        this.branchWorklist = new ArrayList();
        for (int i = 0; i < this.regCount; ++i) {
            this.latticeValues[i] = 0;
            this.latticeConstants[i] = null;
        }
    }

    public static void process(SsaMethod ssaMethod) {
        new SCCP(ssaMethod).run();
    }

    private void addBlockToWorklist(SsaBasicBlock ssaBlock) {
        if (!this.executableBlocks.get(ssaBlock.getIndex())) {
            this.cfgWorklist.add(ssaBlock);
            this.executableBlocks.set(ssaBlock.getIndex());
        } else {
            this.cfgPhiWorklist.add(ssaBlock);
        }
    }

    private void addUsersToWorklist(int reg, int latticeValue) {
        if (latticeValue == 2) {
            for (SsaInsn insn : this.ssaMeth.getUseListForRegister(reg)) {
                this.varyingWorklist.add(insn);
            }
        } else {
            for (SsaInsn insn : this.ssaMeth.getUseListForRegister(reg)) {
                this.ssaWorklist.add(insn);
            }
        }
    }

    private boolean setLatticeValueTo(int reg, int value, Constant cst) {
        if (value != 1) {
            if (this.latticeValues[reg] != value) {
                this.latticeValues[reg] = value;
                return true;
            }
            return false;
        }
        if (this.latticeValues[reg] != value || !this.latticeConstants[reg].equals(cst)) {
            this.latticeValues[reg] = value;
            this.latticeConstants[reg] = cst;
            return true;
        }
        return false;
    }

    private void simulatePhi(PhiInsn insn) {
        int phiResultReg = insn.getResult().getReg();
        if (this.latticeValues[phiResultReg] == 2) {
            return;
        }
        RegisterSpecList sources = insn.getSources();
        int phiResultValue = 0;
        Constant phiConstant = null;
        int sourceSize = sources.size();
        for (int i = 0; i < sourceSize; ++i) {
            int predBlockIndex = insn.predBlockIndexForSourcesIndex(i);
            int sourceReg = sources.get(i).getReg();
            int sourceRegValue = this.latticeValues[sourceReg];
            if (!this.executableBlocks.get(predBlockIndex)) continue;
            if (sourceRegValue == 1) {
                if (phiConstant == null) {
                    phiConstant = this.latticeConstants[sourceReg];
                    phiResultValue = 1;
                    continue;
                }
                if (this.latticeConstants[sourceReg].equals(phiConstant)) continue;
                phiResultValue = 2;
                break;
            }
            phiResultValue = sourceRegValue;
            break;
        }
        if (this.setLatticeValueTo(phiResultReg, phiResultValue, phiConstant)) {
            this.addUsersToWorklist(phiResultReg, phiResultValue);
        }
    }

    private void simulateBlock(SsaBasicBlock block) {
        for (SsaInsn insn : block.getInsns()) {
            if (insn instanceof PhiInsn) {
                this.simulatePhi((PhiInsn)insn);
                continue;
            }
            this.simulateStmt(insn);
        }
    }

    private void simulatePhiBlock(SsaBasicBlock block) {
        for (SsaInsn insn : block.getInsns()) {
            if (insn instanceof PhiInsn) {
                this.simulatePhi((PhiInsn)insn);
                continue;
            }
            return;
        }
    }

    private static String latticeValName(int latticeVal) {
        switch (latticeVal) {
            case 0: {
                return "TOP";
            }
            case 1: {
                return "CONSTANT";
            }
            case 2: {
                return "VARYING";
            }
        }
        return "UNKNOWN";
    }

    private void simulateBranch(SsaInsn insn) {
        Rop opcode = insn.getOpcode();
        RegisterSpecList sources = insn.getSources();
        boolean constantBranch = false;
        boolean constantSuccessor = false;
        if (opcode.getBranchingness() == 4) {
            Constant cA = null;
            Constant cB = null;
            RegisterSpec specA = sources.get(0);
            int regA = specA.getReg();
            if (!this.ssaMeth.isRegALocal(specA) && this.latticeValues[regA] == 1) {
                cA = this.latticeConstants[regA];
            }
            if (sources.size() == 2) {
                RegisterSpec specB = sources.get(1);
                int regB = specB.getReg();
                if (!this.ssaMeth.isRegALocal(specB) && this.latticeValues[regB] == 1) {
                    cB = this.latticeConstants[regB];
                }
            }
            if (cA != null && sources.size() == 1) {
                block0 : switch (((TypedConstant)cA).getBasicType()) {
                    case 6: {
                        constantBranch = true;
                        int vA = ((CstInteger)cA).getValue();
                        switch (opcode.getOpcode()) {
                            case 7: {
                                constantSuccessor = vA == 0;
                                break block0;
                            }
                            case 8: {
                                constantSuccessor = vA != 0;
                                break block0;
                            }
                            case 9: {
                                constantSuccessor = vA < 0;
                                break block0;
                            }
                            case 10: {
                                constantSuccessor = vA >= 0;
                                break block0;
                            }
                            case 11: {
                                constantSuccessor = vA <= 0;
                                break block0;
                            }
                            case 12: {
                                constantSuccessor = vA > 0;
                                break block0;
                            }
                        }
                        throw new RuntimeException("Unexpected op");
                    }
                }
            } else if (cA != null && cB != null) {
                block11 : switch (((TypedConstant)cA).getBasicType()) {
                    case 6: {
                        constantBranch = true;
                        int vA = ((CstInteger)cA).getValue();
                        int vB = ((CstInteger)cB).getValue();
                        switch (opcode.getOpcode()) {
                            case 7: {
                                constantSuccessor = vA == vB;
                                break block11;
                            }
                            case 8: {
                                constantSuccessor = vA != vB;
                                break block11;
                            }
                            case 9: {
                                constantSuccessor = vA < vB;
                                break block11;
                            }
                            case 10: {
                                constantSuccessor = vA >= vB;
                                break block11;
                            }
                            case 11: {
                                constantSuccessor = vA <= vB;
                                break block11;
                            }
                            case 12: {
                                constantSuccessor = vA > vB;
                                break block11;
                            }
                        }
                        throw new RuntimeException("Unexpected op");
                    }
                }
            }
        }
        SsaBasicBlock block = insn.getBlock();
        if (constantBranch) {
            int successorBlock = constantSuccessor ? block.getSuccessorList().get(1) : block.getSuccessorList().get(0);
            this.addBlockToWorklist(this.ssaMeth.getBlocks().get(successorBlock));
            this.branchWorklist.add(insn);
        } else {
            for (int i = 0; i < block.getSuccessorList().size(); ++i) {
                int successorBlock = block.getSuccessorList().get(i);
                this.addBlockToWorklist(this.ssaMeth.getBlocks().get(successorBlock));
            }
        }
    }

    private Constant simulateMath(SsaInsn insn, int resultType) {
        Constant cB;
        Insn ropInsn = insn.getOriginalRopInsn();
        int opcode = insn.getOpcode().getOpcode();
        RegisterSpecList sources = insn.getSources();
        int regA = sources.get(0).getReg();
        Constant cA = this.latticeValues[regA] != 1 ? null : this.latticeConstants[regA];
        if (sources.size() == 1) {
            CstInsn cstInsn = (CstInsn)ropInsn;
            cB = cstInsn.getConstant();
        } else {
            int regB = sources.get(1).getReg();
            cB = this.latticeValues[regB] != 1 ? null : this.latticeConstants[regB];
        }
        if (cA == null || cB == null) {
            return null;
        }
        switch (resultType) {
            case 6: {
                int vR;
                boolean skip = false;
                int vA = ((CstInteger)cA).getValue();
                int vB = ((CstInteger)cB).getValue();
                switch (opcode) {
                    case 14: {
                        vR = vA + vB;
                        break;
                    }
                    case 15: {
                        if (sources.size() == 1) {
                            vR = vB - vA;
                            break;
                        }
                        vR = vA - vB;
                        break;
                    }
                    case 16: {
                        vR = vA * vB;
                        break;
                    }
                    case 17: {
                        if (vB == 0) {
                            skip = true;
                            vR = 0;
                            break;
                        }
                        vR = vA / vB;
                        break;
                    }
                    case 20: {
                        vR = vA & vB;
                        break;
                    }
                    case 21: {
                        vR = vA | vB;
                        break;
                    }
                    case 22: {
                        vR = vA ^ vB;
                        break;
                    }
                    case 23: {
                        vR = vA << vB;
                        break;
                    }
                    case 24: {
                        vR = vA >> vB;
                        break;
                    }
                    case 25: {
                        vR = vA >>> vB;
                        break;
                    }
                    case 18: {
                        if (vB == 0) {
                            skip = true;
                            vR = 0;
                            break;
                        }
                        vR = vA % vB;
                        break;
                    }
                    default: {
                        throw new RuntimeException("Unexpected op");
                    }
                }
                return skip ? null : CstInteger.make(vR);
            }
        }
        return null;
    }

    private void simulateStmt(SsaInsn insn) {
        Insn ropInsn = insn.getOriginalRopInsn();
        if (ropInsn.getOpcode().getBranchingness() != 1 || ropInsn.getOpcode().isCallLike()) {
            this.simulateBranch(insn);
        }
        int opcode = insn.getOpcode().getOpcode();
        RegisterSpec result = insn.getResult();
        if (result == null) {
            if (opcode == 17 || opcode == 18) {
                SsaBasicBlock succ = insn.getBlock().getPrimarySuccessor();
                result = succ.getInsns().get(0).getResult();
            } else {
                return;
            }
        }
        int resultReg = result.getReg();
        int resultValue = 2;
        Constant resultConstant = null;
        switch (opcode) {
            case 5: {
                CstInsn cstInsn = (CstInsn)ropInsn;
                resultValue = 1;
                resultConstant = cstInsn.getConstant();
                break;
            }
            case 2: {
                if (insn.getSources().size() != 1) break;
                int sourceReg = insn.getSources().get(0).getReg();
                resultValue = this.latticeValues[sourceReg];
                resultConstant = this.latticeConstants[sourceReg];
                break;
            }
            case 14: 
            case 15: 
            case 16: 
            case 17: 
            case 18: 
            case 20: 
            case 21: 
            case 22: 
            case 23: 
            case 24: 
            case 25: {
                resultConstant = this.simulateMath(insn, result.getBasicType());
                if (resultConstant == null) break;
                resultValue = 1;
                break;
            }
            case 56: {
                if (this.latticeValues[resultReg] != 1) break;
                resultValue = this.latticeValues[resultReg];
                resultConstant = this.latticeConstants[resultReg];
                break;
            }
        }
        if (this.setLatticeValueTo(resultReg, resultValue, resultConstant)) {
            this.addUsersToWorklist(resultReg, resultValue);
        }
    }

    private void run() {
        SsaBasicBlock firstBlock = this.ssaMeth.getEntryBlock();
        this.addBlockToWorklist(firstBlock);
        while (!(this.cfgWorklist.isEmpty() && this.cfgPhiWorklist.isEmpty() && this.ssaWorklist.isEmpty() && this.varyingWorklist.isEmpty())) {
            SsaInsn insn;
            SsaBasicBlock block;
            int listSize;
            while (!this.cfgWorklist.isEmpty()) {
                listSize = this.cfgWorklist.size() - 1;
                block = this.cfgWorklist.remove(listSize);
                this.simulateBlock(block);
            }
            while (!this.cfgPhiWorklist.isEmpty()) {
                listSize = this.cfgPhiWorklist.size() - 1;
                block = this.cfgPhiWorklist.remove(listSize);
                this.simulatePhiBlock(block);
            }
            while (!this.varyingWorklist.isEmpty()) {
                listSize = this.varyingWorklist.size() - 1;
                insn = this.varyingWorklist.remove(listSize);
                if (!this.executableBlocks.get(insn.getBlock().getIndex())) continue;
                if (insn instanceof PhiInsn) {
                    this.simulatePhi((PhiInsn)insn);
                    continue;
                }
                this.simulateStmt(insn);
            }
            while (!this.ssaWorklist.isEmpty()) {
                listSize = this.ssaWorklist.size() - 1;
                insn = this.ssaWorklist.remove(listSize);
                if (!this.executableBlocks.get(insn.getBlock().getIndex())) continue;
                if (insn instanceof PhiInsn) {
                    this.simulatePhi((PhiInsn)insn);
                    continue;
                }
                this.simulateStmt(insn);
            }
        }
        this.replaceConstants();
        this.replaceBranches();
    }

    private void replaceConstants() {
        for (int reg = 0; reg < this.regCount; ++reg) {
            SsaInsn defn;
            TypeBearer typeBearer;
            if (this.latticeValues[reg] != 1 || !(this.latticeConstants[reg] instanceof TypedConstant) || (typeBearer = (defn = this.ssaMeth.getDefinitionForRegister(reg)).getResult().getTypeBearer()).isConstant()) continue;
            RegisterSpec dest = defn.getResult();
            RegisterSpec newDest = dest.withType((TypedConstant)this.latticeConstants[reg]);
            defn.setResult(newDest);
            for (SsaInsn insn : this.ssaMeth.getUseListForRegister(reg)) {
                if (insn.isPhiOrMove()) continue;
                NormalSsaInsn nInsn = (NormalSsaInsn)insn;
                RegisterSpecList sources = insn.getSources();
                int index = sources.indexOfRegister(reg);
                RegisterSpec spec = sources.get(index);
                RegisterSpec newSpec = spec.withType((TypedConstant)this.latticeConstants[reg]);
                nInsn.changeOneSource(index, newSpec);
            }
        }
    }

    private void replaceBranches() {
        for (SsaInsn insn : this.branchWorklist) {
            int oldSuccessor = -1;
            SsaBasicBlock block = insn.getBlock();
            int successorSize = block.getSuccessorList().size();
            for (int i = 0; i < successorSize; ++i) {
                int successorBlock = block.getSuccessorList().get(i);
                if (this.executableBlocks.get(successorBlock)) continue;
                oldSuccessor = successorBlock;
            }
            if (successorSize != 2 || oldSuccessor == -1) continue;
            Insn originalRopInsn = insn.getOriginalRopInsn();
            block.replaceLastInsn(new PlainInsn(Rops.GOTO, originalRopInsn.getPosition(), null, RegisterSpecList.EMPTY));
            block.removeSuccessor(oldSuccessor);
        }
    }
}

