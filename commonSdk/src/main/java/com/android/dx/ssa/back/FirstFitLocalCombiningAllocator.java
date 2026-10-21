/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.ssa.back;

import com.android.dx.rop.code.CstInsn;
import com.android.dx.rop.code.LocalItem;
import com.android.dx.rop.code.RegisterSpec;
import com.android.dx.rop.code.RegisterSpecList;
import com.android.dx.rop.code.Rop;
import com.android.dx.rop.cst.CstInteger;
import com.android.dx.ssa.InterferenceRegisterMapper;
import com.android.dx.ssa.NormalSsaInsn;
import com.android.dx.ssa.Optimizer;
import com.android.dx.ssa.PhiInsn;
import com.android.dx.ssa.RegisterMapper;
import com.android.dx.ssa.SsaBasicBlock;
import com.android.dx.ssa.SsaInsn;
import com.android.dx.ssa.SsaMethod;
import com.android.dx.ssa.back.InterferenceGraph;
import com.android.dx.ssa.back.RegisterAllocator;
import com.android.dx.util.IntIterator;
import com.android.dx.util.IntSet;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Map;
import java.util.TreeMap;

public class FirstFitLocalCombiningAllocator
extends RegisterAllocator {
    private static final boolean DEBUG = false;
    private final Map<LocalItem, ArrayList<RegisterSpec>> localVariables;
    private final ArrayList<NormalSsaInsn> moveResultPseudoInsns;
    private final ArrayList<NormalSsaInsn> invokeRangeInsns;
    private final ArrayList<PhiInsn> phiInsns;
    private final BitSet ssaRegsMapped;
    private final InterferenceRegisterMapper mapper;
    private final int paramRangeEnd;
    private final BitSet reservedRopRegs;
    private final BitSet usedRopRegs;
    private final boolean minimizeRegisters;

    public FirstFitLocalCombiningAllocator(SsaMethod ssaMeth, InterferenceGraph interference, boolean minimizeRegisters) {
        super(ssaMeth, interference);
        this.ssaRegsMapped = new BitSet(ssaMeth.getRegCount());
        this.mapper = new InterferenceRegisterMapper(interference, ssaMeth.getRegCount());
        this.minimizeRegisters = minimizeRegisters;
        this.paramRangeEnd = ssaMeth.getParamWidth();
        this.reservedRopRegs = new BitSet(this.paramRangeEnd * 2);
        this.reservedRopRegs.set(0, this.paramRangeEnd);
        this.usedRopRegs = new BitSet(this.paramRangeEnd * 2);
        this.localVariables = new TreeMap<LocalItem, ArrayList<RegisterSpec>>();
        this.moveResultPseudoInsns = new ArrayList();
        this.invokeRangeInsns = new ArrayList();
        this.phiInsns = new ArrayList();
    }

    @Override
    public boolean wantsParamsMovedHigh() {
        return true;
    }

    @Override
    public RegisterMapper allocateRegisters() {
        this.analyzeInstructions();
        this.handleLocalAssociatedParams();
        this.handleUnassociatedParameters();
        this.handleInvokeRangeInsns();
        this.handleLocalAssociatedOther();
        this.handleCheckCastResults();
        this.handlePhiInsns();
        this.handleNormalUnassociated();
        return this.mapper;
    }

    private void printLocalVars() {
        System.out.println("Printing local vars");
        for (Map.Entry<LocalItem, ArrayList<RegisterSpec>> e : this.localVariables.entrySet()) {
            StringBuilder regs = new StringBuilder();
            regs.append('{');
            regs.append(' ');
            for (RegisterSpec reg : e.getValue()) {
                regs.append('v');
                regs.append(reg.getReg());
                regs.append(' ');
            }
            regs.append('}');
            System.out.printf("Local: %s Registers: %s\n", e.getKey(), regs);
        }
    }

    private void handleLocalAssociatedParams() {
        for (ArrayList<RegisterSpec> ssaRegs : this.localVariables.values()) {
            int sz = ssaRegs.size();
            int paramIndex = -1;
            int paramCategory = 0;
            for (int i = 0; i < sz; ++i) {
                RegisterSpec ssaSpec = ssaRegs.get(i);
                int ssaReg = ssaSpec.getReg();
                paramIndex = this.getParameterIndexForReg(ssaReg);
                if (paramIndex < 0) continue;
                paramCategory = ssaSpec.getCategory();
                this.addMapping(ssaSpec, paramIndex);
                break;
            }
            if (paramIndex < 0) continue;
            this.tryMapRegs(ssaRegs, paramIndex, paramCategory, true);
        }
    }

    private int getParameterIndexForReg(int ssaReg) {
        SsaInsn defInsn = this.ssaMeth.getDefinitionForRegister(ssaReg);
        if (defInsn == null) {
            return -1;
        }
        Rop opcode = defInsn.getOpcode();
        if (opcode != null && opcode.getOpcode() == 3) {
            CstInsn origInsn = (CstInsn)defInsn.getOriginalRopInsn();
            return ((CstInteger)origInsn.getConstant()).getValue();
        }
        return -1;
    }

    private void handleLocalAssociatedOther() {
        for (ArrayList<RegisterSpec> specs : this.localVariables.values()) {
            int ropReg = this.paramRangeEnd;
            boolean done = false;
            do {
                int maxCategory = 1;
                int sz = specs.size();
                for (int i = 0; i < sz; ++i) {
                    RegisterSpec ssaSpec = specs.get(i);
                    int category = ssaSpec.getCategory();
                    if (this.ssaRegsMapped.get(ssaSpec.getReg()) || category <= maxCategory) continue;
                    maxCategory = category;
                }
                if (this.canMapRegs(specs, ropReg = this.findRopRegForLocal(ropReg, maxCategory))) {
                    done = this.tryMapRegs(specs, ropReg, maxCategory, true);
                }
                ++ropReg;
            } while (!done);
        }
    }

    private boolean tryMapRegs(ArrayList<RegisterSpec> specs, int ropReg, int maxAllowedCategory, boolean markReserved) {
        boolean remaining = false;
        for (RegisterSpec spec : specs) {
            if (this.ssaRegsMapped.get(spec.getReg())) continue;
            boolean succeeded = this.tryMapReg(spec, ropReg, maxAllowedCategory);
            boolean bl = remaining = !succeeded || remaining;
            if (!succeeded || !markReserved) continue;
            this.markReserved(ropReg, spec.getCategory());
        }
        return !remaining;
    }

    private boolean tryMapReg(RegisterSpec ssaSpec, int ropReg, int maxAllowedCategory) {
        if (ssaSpec.getCategory() <= maxAllowedCategory && !this.ssaRegsMapped.get(ssaSpec.getReg()) && this.canMapReg(ssaSpec, ropReg)) {
            this.addMapping(ssaSpec, ropReg);
            return true;
        }
        return false;
    }

    private void markReserved(int ropReg, int category) {
        this.reservedRopRegs.set(ropReg, ropReg + category, true);
    }

    private boolean rangeContainsReserved(int ropRangeStart, int width) {
        for (int i = ropRangeStart; i < ropRangeStart + width; ++i) {
            if (!this.reservedRopRegs.get(i)) continue;
            return true;
        }
        return false;
    }

    private boolean isThisPointerReg(int startReg) {
        return startReg == 0 && !this.ssaMeth.isStatic();
    }

    private Alignment getAlignment(int regCategory) {
        Alignment alignment = Alignment.UNSPECIFIED;
        if (regCategory == 2) {
            alignment = FirstFitLocalCombiningAllocator.isEven(this.paramRangeEnd) ? Alignment.EVEN : Alignment.ODD;
        }
        return alignment;
    }

    private int findNextUnreservedRopReg(int startReg, int regCategory) {
        return this.findNextUnreservedRopReg(startReg, regCategory, this.getAlignment(regCategory));
    }

    private int findNextUnreservedRopReg(int startReg, int width, Alignment alignment) {
        int reg = alignment.nextClearBit(this.reservedRopRegs, startReg);
        while (true) {
            int i;
            for (i = 1; i < width && !this.reservedRopRegs.get(reg + i); ++i) {
            }
            if (i == width) {
                return reg;
            }
            reg = alignment.nextClearBit(this.reservedRopRegs, reg + i);
        }
    }

    private int findRopRegForLocal(int startReg, int category) {
        Alignment alignment = this.getAlignment(category);
        int reg = alignment.nextClearBit(this.usedRopRegs, startReg);
        while (true) {
            int i;
            for (i = 1; i < category && !this.usedRopRegs.get(reg + i); ++i) {
            }
            if (i == category) {
                return reg;
            }
            reg = alignment.nextClearBit(this.usedRopRegs, reg + i);
        }
    }

    private void handleUnassociatedParameters() {
        int szSsaRegs = this.ssaMeth.getRegCount();
        for (int ssaReg = 0; ssaReg < szSsaRegs; ++ssaReg) {
            if (this.ssaRegsMapped.get(ssaReg)) continue;
            int paramIndex = this.getParameterIndexForReg(ssaReg);
            RegisterSpec ssaSpec = this.getDefinitionSpecForSsaReg(ssaReg);
            if (paramIndex < 0) continue;
            this.addMapping(ssaSpec, paramIndex);
        }
    }

    private void handleInvokeRangeInsns() {
        for (NormalSsaInsn insn : this.invokeRangeInsns) {
            this.adjustAndMapSourceRangeRange(insn);
        }
    }

    private void handleCheckCastResults() {
        for (NormalSsaInsn insn : this.moveResultPseudoInsns) {
            int checkRopReg;
            boolean hasExceptionHandlers;
            boolean checkMapped;
            SsaBasicBlock predBlock;
            ArrayList<SsaInsn> insnList;
            SsaInsn checkCastInsn;
            RegisterSpec moveRegSpec = insn.getResult();
            int moveReg = moveRegSpec.getReg();
            BitSet predBlocks = insn.getBlock().getPredecessors();
            if (predBlocks.cardinality() != 1 || (checkCastInsn = (insnList = (predBlock = this.ssaMeth.getBlocks().get(predBlocks.nextSetBit(0))).getInsns()).get(insnList.size() - 1)).getOpcode().getOpcode() != 43) continue;
            RegisterSpec checkRegSpec = checkCastInsn.getSources().get(0);
            int checkReg = checkRegSpec.getReg();
            int category = checkRegSpec.getCategory();
            boolean moveMapped = this.ssaRegsMapped.get(moveReg);
            if (moveMapped & !(checkMapped = this.ssaRegsMapped.get(checkReg))) {
                int moveRopReg = this.mapper.oldToNew(moveReg);
                checkMapped = this.tryMapReg(checkRegSpec, moveRopReg, category);
            }
            if (checkMapped & !moveMapped) {
                int checkRopReg2 = this.mapper.oldToNew(checkReg);
                moveMapped = this.tryMapReg(moveRegSpec, checkRopReg2, category);
            }
            if (!moveMapped || !checkMapped) {
                int ropReg = this.findNextUnreservedRopReg(this.paramRangeEnd, category);
                ArrayList<RegisterSpec> ssaRegs = new ArrayList<RegisterSpec>(2);
                ssaRegs.add(moveRegSpec);
                ssaRegs.add(checkRegSpec);
                while (!this.tryMapRegs(ssaRegs, ropReg, category, false)) {
                    ropReg = this.findNextUnreservedRopReg(ropReg + 1, category);
                }
            }
            boolean bl = hasExceptionHandlers = checkCastInsn.getOriginalRopInsn().getCatches().size() != 0;
            int moveRopReg = this.mapper.oldToNew(moveReg);
            if (moveRopReg == (checkRopReg = this.mapper.oldToNew(checkReg)) || hasExceptionHandlers) continue;
            ((NormalSsaInsn)checkCastInsn).changeOneSource(0, this.insertMoveBefore(checkCastInsn, checkRegSpec));
            this.addMapping(checkCastInsn.getSources().get(0), moveRopReg);
        }
    }

    private void handlePhiInsns() {
        for (PhiInsn insn : this.phiInsns) {
            this.processPhiInsn(insn);
        }
    }

    private void handleNormalUnassociated() {
        int szSsaRegs = this.ssaMeth.getRegCount();
        for (int ssaReg = 0; ssaReg < szSsaRegs; ++ssaReg) {
            RegisterSpec ssaSpec;
            if (this.ssaRegsMapped.get(ssaReg) || (ssaSpec = this.getDefinitionSpecForSsaReg(ssaReg)) == null) continue;
            int category = ssaSpec.getCategory();
            int ropReg = this.findNextUnreservedRopReg(this.paramRangeEnd, category);
            while (!this.canMapReg(ssaSpec, ropReg)) {
                ropReg = this.findNextUnreservedRopReg(ropReg + 1, category);
            }
            this.addMapping(ssaSpec, ropReg);
        }
    }

    private boolean canMapRegs(ArrayList<RegisterSpec> specs, int ropReg) {
        for (RegisterSpec spec : specs) {
            if (this.ssaRegsMapped.get(spec.getReg()) || this.canMapReg(spec, ropReg)) continue;
            return false;
        }
        return true;
    }

    private boolean canMapReg(RegisterSpec ssaSpec, int ropReg) {
        int category = ssaSpec.getCategory();
        return !this.spansParamRange(ropReg, category) && !this.mapper.interferes(ssaSpec, ropReg);
    }

    private boolean spansParamRange(int ssaReg, int category) {
        return ssaReg < this.paramRangeEnd && ssaReg + category > this.paramRangeEnd;
    }

    private void analyzeInstructions() {
        this.ssaMeth.forEachInsn(new SsaInsn.Visitor(){

            @Override
            public void visitMoveInsn(NormalSsaInsn insn) {
                this.processInsn(insn);
            }

            @Override
            public void visitPhiInsn(PhiInsn insn) {
                this.processInsn(insn);
            }

            @Override
            public void visitNonMoveInsn(NormalSsaInsn insn) {
                this.processInsn(insn);
            }

            private void processInsn(SsaInsn insn) {
                RegisterSpec assignment = insn.getLocalAssignment();
                if (assignment != null) {
                    LocalItem local = assignment.getLocalItem();
                    ArrayList<RegisterSpec> regList = (ArrayList<RegisterSpec>)FirstFitLocalCombiningAllocator.this.localVariables.get(local);
                    if (regList == null) {
                        regList = new ArrayList<RegisterSpec>();
                        FirstFitLocalCombiningAllocator.this.localVariables.put(local, regList);
                    }
                    regList.add(assignment);
                }
                if (insn instanceof NormalSsaInsn) {
                    if (insn.getOpcode().getOpcode() == 56) {
                        FirstFitLocalCombiningAllocator.this.moveResultPseudoInsns.add((NormalSsaInsn)insn);
                    } else if (Optimizer.getAdvice().requiresSourcesInOrder(insn.getOriginalRopInsn().getOpcode(), insn.getSources())) {
                        FirstFitLocalCombiningAllocator.this.invokeRangeInsns.add((NormalSsaInsn)insn);
                    }
                } else if (insn instanceof PhiInsn) {
                    FirstFitLocalCombiningAllocator.this.phiInsns.add((PhiInsn)insn);
                }
            }
        });
    }

    private void addMapping(RegisterSpec ssaSpec, int ropReg) {
        int ssaReg = ssaSpec.getReg();
        if (this.ssaRegsMapped.get(ssaReg) || !this.canMapReg(ssaSpec, ropReg)) {
            throw new RuntimeException("attempt to add invalid register mapping");
        }
        int category = ssaSpec.getCategory();
        this.mapper.addMapping(ssaSpec.getReg(), ropReg, category);
        this.ssaRegsMapped.set(ssaReg);
        this.usedRopRegs.set(ropReg, ropReg + category);
    }

    private void adjustAndMapSourceRangeRange(NormalSsaInsn insn) {
        int newRegStart = this.findRangeAndAdjust(insn);
        RegisterSpecList sources = insn.getSources();
        int szSources = sources.size();
        int nextRopReg = newRegStart;
        for (int i = 0; i < szSources; ++i) {
            RegisterSpec source = sources.get(i);
            int sourceReg = source.getReg();
            int category = source.getCategory();
            int curRopReg = nextRopReg;
            nextRopReg += category;
            if (this.ssaRegsMapped.get(sourceReg)) continue;
            LocalItem localItem = this.getLocalItemForReg(sourceReg);
            this.addMapping(source, curRopReg);
            if (localItem == null) continue;
            this.markReserved(curRopReg, category);
            ArrayList<RegisterSpec> similarRegisters = this.localVariables.get(localItem);
            int szSimilar = similarRegisters.size();
            for (int j = 0; j < szSimilar; ++j) {
                RegisterSpec similarSpec = similarRegisters.get(j);
                int similarReg = similarSpec.getReg();
                if (-1 != sources.indexOfRegister(similarReg)) continue;
                this.tryMapReg(similarSpec, curRopReg, category);
            }
        }
    }

    private int findRangeAndAdjust(NormalSsaInsn insn) {
        int i;
        RegisterSpecList sources = insn.getSources();
        int szSources = sources.size();
        int[] categoriesForIndex = new int[szSources];
        int rangeLength = 0;
        for (int i2 = 0; i2 < szSources; ++i2) {
            int category;
            categoriesForIndex[i2] = category = sources.get(i2).getCategory();
            rangeLength += categoriesForIndex[i2];
        }
        int maxScore = Integer.MIN_VALUE;
        int resultRangeStart = -1;
        BitSet resultMovesRequired = null;
        int rangeStartOffset = 0;
        for (i = 0; i < szSources; ++i) {
            BitSet curMovesRequired;
            int fitWidth;
            int rangeStart;
            int ssaCenterReg = sources.get(i).getReg();
            if (i != 0) {
                rangeStartOffset -= categoriesForIndex[i - 1];
            }
            if (!this.ssaRegsMapped.get(ssaCenterReg) || (rangeStart = this.mapper.oldToNew(ssaCenterReg) + rangeStartOffset) < 0 || this.spansParamRange(rangeStart, rangeLength) || (fitWidth = this.fitPlanForRange(rangeStart, insn, categoriesForIndex, curMovesRequired = new BitSet(szSources))) < 0) continue;
            int score = fitWidth - curMovesRequired.cardinality();
            if (score > maxScore) {
                maxScore = score;
                resultRangeStart = rangeStart;
                resultMovesRequired = curMovesRequired;
            }
            if (fitWidth == rangeLength) break;
        }
        if (resultRangeStart == -1) {
            resultMovesRequired = new BitSet(szSources);
            resultRangeStart = this.findAnyFittingRange(insn, rangeLength, categoriesForIndex, resultMovesRequired);
        }
        i = resultMovesRequired.nextSetBit(0);
        while (i >= 0) {
            insn.changeOneSource(i, this.insertMoveBefore(insn, sources.get(i)));
            i = resultMovesRequired.nextSetBit(i + 1);
        }
        return resultRangeStart;
    }

    private int findAnyFittingRange(NormalSsaInsn insn, int rangeLength, int[] categoriesForIndex, BitSet outMovesRequired) {
        int fitWidth;
        Alignment alignment = Alignment.UNSPECIFIED;
        int regNumber = 0;
        int p64bitsAligned = 0;
        int p64bitsNotAligned = 0;
        for (int category : categoriesForIndex) {
            if (category == 2) {
                if (FirstFitLocalCombiningAllocator.isEven(regNumber)) {
                    ++p64bitsAligned;
                } else {
                    ++p64bitsNotAligned;
                }
                regNumber += 2;
                continue;
            }
            ++regNumber;
        }
        if (p64bitsNotAligned > p64bitsAligned) {
            alignment = FirstFitLocalCombiningAllocator.isEven(this.paramRangeEnd) ? Alignment.ODD : Alignment.EVEN;
        } else if (p64bitsAligned > 0) {
            alignment = FirstFitLocalCombiningAllocator.isEven(this.paramRangeEnd) ? Alignment.EVEN : Alignment.ODD;
        }
        int rangeStart = this.paramRangeEnd;
        while ((fitWidth = this.fitPlanForRange(rangeStart = this.findNextUnreservedRopReg(rangeStart, rangeLength, alignment), insn, categoriesForIndex, outMovesRequired)) < 0) {
            ++rangeStart;
            outMovesRequired.clear();
        }
        return rangeStart;
    }

    private int fitPlanForRange(int ropReg, NormalSsaInsn insn, int[] categoriesForIndex, BitSet outMovesRequired) {
        RegisterSpecList sources = insn.getSources();
        int szSources = sources.size();
        int fitWidth = 0;
        IntSet liveOut = insn.getBlock().getLiveOutRegs();
        RegisterSpecList liveOutSpecs = this.ssaSetToSpecs(liveOut);
        BitSet seen = new BitSet(this.ssaMeth.getRegCount());
        for (int i = 0; i < szSources; ++i) {
            RegisterSpec ssaSpec = sources.get(i);
            int ssaReg = ssaSpec.getReg();
            int category = categoriesForIndex[i];
            if (i != 0) {
                ropReg += categoriesForIndex[i - 1];
            }
            if (this.ssaRegsMapped.get(ssaReg) && this.mapper.oldToNew(ssaReg) == ropReg) {
                fitWidth += category;
            } else {
                if (this.rangeContainsReserved(ropReg, category)) {
                    fitWidth = -1;
                    break;
                }
                if (!this.ssaRegsMapped.get(ssaReg) && this.canMapReg(ssaSpec, ropReg) && !seen.get(ssaReg)) {
                    fitWidth += category;
                } else if (!this.mapper.areAnyPinned(liveOutSpecs, ropReg, category) && !this.mapper.areAnyPinned(sources, ropReg, category)) {
                    outMovesRequired.set(i);
                } else {
                    fitWidth = -1;
                    break;
                }
            }
            seen.set(ssaReg);
        }
        return fitWidth;
    }

    RegisterSpecList ssaSetToSpecs(IntSet ssaSet) {
        RegisterSpecList result = new RegisterSpecList(ssaSet.elements());
        IntIterator iter = ssaSet.iterator();
        int i = 0;
        while (iter.hasNext()) {
            result.set(i++, this.getDefinitionSpecForSsaReg(iter.next()));
        }
        return result;
    }

    private LocalItem getLocalItemForReg(int ssaReg) {
        for (Map.Entry<LocalItem, ArrayList<RegisterSpec>> entry : this.localVariables.entrySet()) {
            for (RegisterSpec spec : entry.getValue()) {
                if (spec.getReg() != ssaReg) continue;
                return entry.getKey();
            }
        }
        return null;
    }

    private void processPhiInsn(PhiInsn insn) {
        int i;
        RegisterSpec result = insn.getResult();
        int resultReg = result.getReg();
        int category = result.getCategory();
        RegisterSpecList sources = insn.getSources();
        int sourcesSize = sources.size();
        ArrayList<RegisterSpec> ssaRegs = new ArrayList<RegisterSpec>();
        Multiset mapSet = new Multiset(sourcesSize + 1);
        if (this.ssaRegsMapped.get(resultReg)) {
            mapSet.add(this.mapper.oldToNew(resultReg));
        } else {
            ssaRegs.add(result);
        }
        for (i = 0; i < sourcesSize; ++i) {
            RegisterSpec source = sources.get(i);
            SsaInsn def = this.ssaMeth.getDefinitionForRegister(source.getReg());
            RegisterSpec sourceDef = def.getResult();
            int sourceReg = sourceDef.getReg();
            if (this.ssaRegsMapped.get(sourceReg)) {
                mapSet.add(this.mapper.oldToNew(sourceReg));
                continue;
            }
            ssaRegs.add(sourceDef);
        }
        for (i = 0; i < mapSet.getSize(); ++i) {
            int maxReg = mapSet.getAndRemoveHighestCount();
            this.tryMapRegs(ssaRegs, maxReg, category, false);
        }
        int mapReg = this.findNextUnreservedRopReg(this.paramRangeEnd, category);
        while (!this.tryMapRegs(ssaRegs, mapReg, category, false)) {
            mapReg = this.findNextUnreservedRopReg(mapReg + 1, category);
        }
    }

    private static boolean isEven(int regNumger) {
        return (regNumger & 1) == 0;
    }

    private static class Multiset {
        private final int[] reg;
        private final int[] count;
        private int size;

        public Multiset(int maxSize) {
            this.reg = new int[maxSize];
            this.count = new int[maxSize];
            this.size = 0;
        }

        public void add(int element) {
            for (int i = 0; i < this.size; ++i) {
                if (this.reg[i] != element) continue;
                int n = i;
                this.count[n] = this.count[n] + 1;
                return;
            }
            this.reg[this.size] = element;
            this.count[this.size] = 1;
            ++this.size;
        }

        public int getAndRemoveHighestCount() {
            int maxIndex = -1;
            int maxReg = -1;
            int maxCount = 0;
            for (int i = 0; i < this.size; ++i) {
                if (maxCount >= this.count[i]) continue;
                maxIndex = i;
                maxReg = this.reg[i];
                maxCount = this.count[i];
            }
            this.count[maxIndex] = 0;
            return maxReg;
        }

        public int getSize() {
            return this.size;
        }
    }

    private static enum Alignment {
        EVEN{

            @Override
            int nextClearBit(BitSet bitSet, int startIdx) {
                int bitNumber = bitSet.nextClearBit(startIdx);
                while (!FirstFitLocalCombiningAllocator.isEven(bitNumber)) {
                    bitNumber = bitSet.nextClearBit(bitNumber + 1);
                }
                return bitNumber;
            }
        }
        ,
        ODD{

            @Override
            int nextClearBit(BitSet bitSet, int startIdx) {
                int bitNumber = bitSet.nextClearBit(startIdx);
                while (FirstFitLocalCombiningAllocator.isEven(bitNumber)) {
                    bitNumber = bitSet.nextClearBit(bitNumber + 1);
                }
                return bitNumber;
            }
        }
        ,
        UNSPECIFIED{

            @Override
            int nextClearBit(BitSet bitSet, int startIdx) {
                return bitSet.nextClearBit(startIdx);
            }
        };


        abstract int nextClearBit(BitSet var1, int var2);
    }
}

