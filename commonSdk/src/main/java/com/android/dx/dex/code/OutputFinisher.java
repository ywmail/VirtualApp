/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.dex.code;

import com.android.dex.DexException;
import com.android.dx.dex.DexOptions;
import com.android.dx.dex.code.CodeAddress;
import com.android.dx.dex.code.CstInsn;
import com.android.dx.dex.code.DalvCode;
import com.android.dx.dex.code.DalvInsn;
import com.android.dx.dex.code.DalvInsnList;
import com.android.dx.dex.code.Dop;
import com.android.dx.dex.code.Dops;
import com.android.dx.dex.code.LocalSnapshot;
import com.android.dx.dex.code.LocalStart;
import com.android.dx.dex.code.MultiCstInsn;
import com.android.dx.dex.code.TargetInsn;
import com.android.dx.dex.code.ZeroSizeInsn;
import com.android.dx.rop.code.LocalItem;
import com.android.dx.rop.code.RegisterSpec;
import com.android.dx.rop.code.RegisterSpecList;
import com.android.dx.rop.code.RegisterSpecSet;
import com.android.dx.rop.code.SourcePosition;
import com.android.dx.rop.cst.Constant;
import com.android.dx.rop.cst.CstMemberRef;
import com.android.dx.rop.cst.CstString;
import com.android.dx.rop.cst.CstType;
import com.android.dx.rop.type.Type;
import com.android.dx.ssa.BasicRegisterMapper;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashSet;

public final class OutputFinisher {
    private final DexOptions dexOptions;
    private final int unreservedRegCount;
    private ArrayList<DalvInsn> insns;
    private boolean hasAnyPositionInfo;
    private boolean hasAnyLocalInfo;
    private int reservedCount;
    private int reservedParameterCount;
    private final int paramSize;

    public OutputFinisher(DexOptions dexOptions, int initialCapacity, int regCount, int paramSize) {
        this.dexOptions = dexOptions;
        this.unreservedRegCount = regCount;
        this.insns = new ArrayList(initialCapacity);
        this.reservedCount = -1;
        this.hasAnyPositionInfo = false;
        this.hasAnyLocalInfo = false;
        this.paramSize = paramSize;
    }

    public boolean hasAnyPositionInfo() {
        return this.hasAnyPositionInfo;
    }

    public boolean hasAnyLocalInfo() {
        return this.hasAnyLocalInfo;
    }

    private static boolean hasLocalInfo(DalvInsn insn) {
        RegisterSpec spec;
        if (insn instanceof LocalSnapshot) {
            RegisterSpecSet specs = ((LocalSnapshot)insn).getLocals();
            int size = specs.size();
            for (int i = 0; i < size; ++i) {
                if (!OutputFinisher.hasLocalInfo(specs.get(i))) continue;
                return true;
            }
        } else if (insn instanceof LocalStart && OutputFinisher.hasLocalInfo(spec = ((LocalStart)insn).getLocal())) {
            return true;
        }
        return false;
    }

    private static boolean hasLocalInfo(RegisterSpec spec) {
        return spec != null && spec.getLocalItem().getName() != null;
    }

    public HashSet<Constant> getAllConstants() {
        HashSet<Constant> result = new HashSet<Constant>(20);
        for (DalvInsn insn : this.insns) {
            OutputFinisher.addConstants(result, insn);
        }
        return result;
    }

    private static void addConstants(HashSet<Constant> result, DalvInsn insn) {
        if (insn instanceof CstInsn) {
            Constant cst = ((CstInsn)insn).getConstant();
            result.add(cst);
        } else if (insn instanceof MultiCstInsn) {
            MultiCstInsn m = (MultiCstInsn)insn;
            for (int i = 0; i < m.getNumberOfConstants(); ++i) {
                result.add(m.getConstant(i));
            }
        } else if (insn instanceof LocalSnapshot) {
            RegisterSpecSet specs = ((LocalSnapshot)insn).getLocals();
            int size = specs.size();
            for (int i = 0; i < size; ++i) {
                OutputFinisher.addConstants(result, specs.get(i));
            }
        } else if (insn instanceof LocalStart) {
            RegisterSpec spec = ((LocalStart)insn).getLocal();
            OutputFinisher.addConstants(result, spec);
        }
    }

    private static void addConstants(HashSet<Constant> result, RegisterSpec spec) {
        if (spec == null) {
            return;
        }
        LocalItem local = spec.getLocalItem();
        CstString name = local.getName();
        CstString signature = local.getSignature();
        Type type = spec.getType();
        if (type != Type.KNOWN_NULL) {
            result.add(CstType.intern(type));
        } else {
            result.add(CstType.intern(Type.OBJECT));
        }
        if (name != null) {
            result.add(name);
        }
        if (signature != null) {
            result.add(signature);
        }
    }

    public void add(DalvInsn insn) {
        this.insns.add(insn);
        this.updateInfo(insn);
    }

    public void insert(int at, DalvInsn insn) {
        this.insns.add(at, insn);
        this.updateInfo(insn);
    }

    private void updateInfo(DalvInsn insn) {
        SourcePosition pos;
        if (!this.hasAnyPositionInfo && (pos = insn.getPosition()).getLine() >= 0) {
            this.hasAnyPositionInfo = true;
        }
        if (!this.hasAnyLocalInfo && OutputFinisher.hasLocalInfo(insn)) {
            this.hasAnyLocalInfo = true;
        }
    }

    public void reverseBranch(int which, CodeAddress newTarget) {
        TargetInsn targetInsn;
        int size = this.insns.size();
        int index = size - which - 1;
        try {
            targetInsn = (TargetInsn)this.insns.get(index);
        }
        catch (IndexOutOfBoundsException ex) {
            throw new IllegalArgumentException("too few instructions");
        }
        catch (ClassCastException ex) {
            throw new IllegalArgumentException("non-reversible instruction");
        }
        this.insns.set(index, targetInsn.withNewTargetAndReversed(newTarget));
    }

    public void assignIndices(DalvCode.AssignIndicesCallback callback) {
        for (DalvInsn insn : this.insns) {
            if (insn instanceof CstInsn) {
                OutputFinisher.assignIndices((CstInsn)insn, callback);
                continue;
            }
            if (!(insn instanceof MultiCstInsn)) continue;
            OutputFinisher.assignIndices((MultiCstInsn)insn, callback);
        }
    }

    private static void assignIndices(CstInsn insn, DalvCode.AssignIndicesCallback callback) {
        CstMemberRef member;
        CstType definer;
        Constant cst = insn.getConstant();
        int index = callback.getIndex(cst);
        if (index >= 0) {
            insn.setIndex(index);
        }
        if (cst instanceof CstMemberRef && (index = callback.getIndex(definer = (member = (CstMemberRef)cst).getDefiningClass())) >= 0) {
            insn.setClassIndex(index);
        }
    }

    private static void assignIndices(MultiCstInsn insn, DalvCode.AssignIndicesCallback callback) {
        for (int i = 0; i < insn.getNumberOfConstants(); ++i) {
            Constant cst = insn.getConstant(i);
            int index = callback.getIndex(cst);
            insn.setIndex(i, index);
            if (!(cst instanceof CstMemberRef)) continue;
            CstMemberRef member = (CstMemberRef)cst;
            CstType definer = member.getDefiningClass();
            index = callback.getIndex(definer);
            insn.setClassIndex(index);
        }
    }

    public DalvInsnList finishProcessingAndGetList() {
        if (this.reservedCount >= 0) {
            throw new UnsupportedOperationException("already processed");
        }
        Dop[] opcodes = this.makeOpcodesArray();
        this.reserveRegisters(opcodes);
        if (this.dexOptions.ALIGN_64BIT_REGS_IN_OUTPUT_FINISHER) {
            this.align64bits(opcodes);
        }
        this.massageInstructions(opcodes);
        this.assignAddressesAndFixBranches();
        return DalvInsnList.makeImmutable(this.insns, this.reservedCount + this.unreservedRegCount + this.reservedParameterCount);
    }

    private Dop[] makeOpcodesArray() {
        int size = this.insns.size();
        Dop[] result = new Dop[size];
        for (int i = 0; i < size; ++i) {
            DalvInsn insn = this.insns.get(i);
            result[i] = insn.getOpcode();
        }
        return result;
    }

    private boolean reserveRegisters(Dop[] opcodes) {
        int newReservedCount;
        int oldReservedCount;
        boolean reservedCountExpanded = false;
        int n = oldReservedCount = this.reservedCount < 0 ? 0 : this.reservedCount;
        while (oldReservedCount < (newReservedCount = this.calculateReservedCount(opcodes))) {
            reservedCountExpanded = true;
            int reservedDifference = newReservedCount - oldReservedCount;
            int size = this.insns.size();
            for (int i = 0; i < size; ++i) {
                DalvInsn insn = this.insns.get(i);
                if (insn instanceof CodeAddress) continue;
                this.insns.set(i, insn.withRegisterOffset(reservedDifference));
            }
            oldReservedCount = newReservedCount;
        }
        this.reservedCount = oldReservedCount;
        return reservedCountExpanded;
    }

    private int calculateReservedCount(Dop[] opcodes) {
        int size = this.insns.size();
        int newReservedCount = this.reservedCount;
        for (int i = 0; i < size; ++i) {
            Dop originalOpcode;
            DalvInsn insn = this.insns.get(i);
            Dop newOpcode = this.findOpcodeForInsn(insn, originalOpcode = opcodes[i]);
            if (newOpcode == null) {
                Dop expandedOp = this.findExpandedOpcodeForInsn(insn);
                BitSet compatRegs = expandedOp.getFormat().compatibleRegs(insn);
                int reserve = insn.getMinimumRegisterRequirement(compatRegs);
                if (reserve > newReservedCount) {
                    newReservedCount = reserve;
                }
            } else if (originalOpcode == newOpcode) continue;
            opcodes[i] = newOpcode;
        }
        return newReservedCount;
    }

    private Dop findOpcodeForInsn(DalvInsn insn, Dop guess) {
        while (guess != null && (!guess.getFormat().isCompatible(insn) || this.dexOptions.forceJumbo && guess.getOpcode() == 26)) {
            guess = Dops.getNextOrNull(guess, this.dexOptions);
        }
        return guess;
    }

    private Dop findExpandedOpcodeForInsn(DalvInsn insn) {
        Dop result = this.findOpcodeForInsn(insn.getLowRegVersion(), insn.getOpcode());
        if (result == null) {
            throw new DexException("No expanded opcode for " + insn);
        }
        return result;
    }

    private void massageInstructions(Dop[] opcodes) {
        if (this.reservedCount == 0) {
            int size = this.insns.size();
            for (int i = 0; i < size; ++i) {
                Dop currentOpcode;
                DalvInsn insn = this.insns.get(i);
                Dop originalOpcode = insn.getOpcode();
                if (originalOpcode == (currentOpcode = opcodes[i])) continue;
                this.insns.set(i, insn.withOpcode(currentOpcode));
            }
        } else {
            this.insns = this.performExpansion(opcodes);
        }
    }

    private ArrayList<DalvInsn> performExpansion(Dop[] opcodes) {
        int size = this.insns.size();
        ArrayList<DalvInsn> result = new ArrayList<DalvInsn>(size * 2);
        ArrayList<CodeAddress> closelyBoundAddresses = new ArrayList<CodeAddress>();
        for (int i = 0; i < size; ++i) {
            DalvInsn suffix;
            DalvInsn prefix;
            DalvInsn insn = this.insns.get(i);
            Dop originalOpcode = insn.getOpcode();
            Dop currentOpcode = opcodes[i];
            if (currentOpcode != null) {
                prefix = null;
                suffix = null;
            } else {
                currentOpcode = this.findExpandedOpcodeForInsn(insn);
                BitSet compatRegs = currentOpcode.getFormat().compatibleRegs(insn);
                prefix = insn.expandedPrefix(compatRegs);
                suffix = insn.expandedSuffix(compatRegs);
                insn = insn.expandedVersion(compatRegs);
            }
            if (insn instanceof CodeAddress && ((CodeAddress)insn).getBindsClosely()) {
                closelyBoundAddresses.add((CodeAddress)insn);
                continue;
            }
            if (prefix != null) {
                result.add(prefix);
            }
            if (!(insn instanceof ZeroSizeInsn) && closelyBoundAddresses.size() > 0) {
                for (CodeAddress codeAddress : closelyBoundAddresses) {
                    result.add(codeAddress);
                }
                closelyBoundAddresses.clear();
            }
            if (currentOpcode != originalOpcode) {
                insn = insn.withOpcode(currentOpcode);
            }
            result.add(insn);
            if (suffix == null) continue;
            result.add(suffix);
        }
        return result;
    }

    private void assignAddressesAndFixBranches() {
        do {
            this.assignAddresses();
        } while (this.fixBranches());
    }

    private void assignAddresses() {
        int address = 0;
        int size = this.insns.size();
        for (int i = 0; i < size; ++i) {
            DalvInsn insn = this.insns.get(i);
            insn.setAddress(address);
            address += insn.codeSize();
        }
    }

    private boolean fixBranches() {
        int size = this.insns.size();
        boolean anyFixed = false;
        for (int i = 0; i < size; ++i) {
            DalvInsn insn = this.insns.get(i);
            if (!(insn instanceof TargetInsn)) continue;
            Dop opcode = insn.getOpcode();
            TargetInsn target = (TargetInsn)insn;
            if (opcode.getFormat().branchFits(target)) continue;
            if (opcode.getFamily() == 40) {
                if ((opcode = this.findOpcodeForInsn(insn, opcode)) == null) {
                    throw new UnsupportedOperationException("method too long");
                }
                this.insns.set(i, insn.withOpcode(opcode));
            } else {
                CodeAddress newTarget;
                try {
                    newTarget = (CodeAddress)this.insns.get(i + 1);
                }
                catch (IndexOutOfBoundsException ex) {
                    throw new IllegalStateException("unpaired TargetInsn (dangling)");
                }
                catch (ClassCastException ex) {
                    throw new IllegalStateException("unpaired TargetInsn");
                }
                TargetInsn gotoInsn = new TargetInsn(Dops.GOTO, target.getPosition(), RegisterSpecList.EMPTY, target.getTarget());
                this.insns.set(i, gotoInsn);
                this.insns.add(i, target.withNewTargetAndReversed(newTarget));
                ++size;
                ++i;
            }
            anyFixed = true;
        }
        return anyFixed;
    }

    private void align64bits(Dop[] opcodes) {
        do {
            int notAligned64bitRegAccess = 0;
            int aligned64bitRegAccess = 0;
            int notAligned64bitParamAccess = 0;
            int aligned64bitParamAccess = 0;
            int lastParameter = this.unreservedRegCount + this.reservedCount + this.reservedParameterCount;
            int firstParameter = lastParameter - this.paramSize;
            for (DalvInsn insn : this.insns) {
                RegisterSpecList regs = insn.getRegisters();
                for (int usedRegIdx = 0; usedRegIdx < regs.size(); ++usedRegIdx) {
                    boolean isParameter;
                    RegisterSpec reg = regs.get(usedRegIdx);
                    if (!reg.isCategory2()) continue;
                    boolean bl = isParameter = reg.getReg() >= firstParameter;
                    if (reg.isEvenRegister()) {
                        if (isParameter) {
                            ++aligned64bitParamAccess;
                            continue;
                        }
                        ++aligned64bitRegAccess;
                        continue;
                    }
                    if (isParameter) {
                        ++notAligned64bitParamAccess;
                        continue;
                    }
                    ++notAligned64bitRegAccess;
                }
            }
            if (notAligned64bitParamAccess > aligned64bitParamAccess && notAligned64bitRegAccess > aligned64bitRegAccess) {
                this.addReservedRegisters(1);
                continue;
            }
            if (notAligned64bitParamAccess > aligned64bitParamAccess) {
                this.addReservedParameters(1);
                continue;
            }
            if (notAligned64bitRegAccess <= aligned64bitRegAccess) break;
            this.addReservedRegisters(1);
            if (this.paramSize == 0 || aligned64bitParamAccess <= notAligned64bitParamAccess) continue;
            this.addReservedParameters(1);
        } while (this.reserveRegisters(opcodes));
    }

    private void addReservedParameters(int delta) {
        this.shiftParameters(delta);
        this.reservedParameterCount += delta;
    }

    private void addReservedRegisters(int delta) {
        this.shiftAllRegisters(delta);
        this.reservedCount += delta;
    }

    private void shiftAllRegisters(int delta) {
        int insnSize = this.insns.size();
        for (int i = 0; i < insnSize; ++i) {
            DalvInsn insn = this.insns.get(i);
            if (insn instanceof CodeAddress) continue;
            this.insns.set(i, insn.withRegisterOffset(delta));
        }
    }

    private void shiftParameters(int delta) {
        int i;
        int insnSize = this.insns.size();
        int lastParameter = this.unreservedRegCount + this.reservedCount + this.reservedParameterCount;
        int firstParameter = lastParameter - this.paramSize;
        BasicRegisterMapper mapper = new BasicRegisterMapper(lastParameter);
        for (i = 0; i < lastParameter; ++i) {
            if (i >= firstParameter) {
                mapper.addMapping(i, i + delta, 1);
                continue;
            }
            mapper.addMapping(i, i, 1);
        }
        for (i = 0; i < insnSize; ++i) {
            DalvInsn insn = this.insns.get(i);
            if (insn instanceof CodeAddress) continue;
            this.insns.set(i, insn.withMapper(mapper));
        }
    }
}

