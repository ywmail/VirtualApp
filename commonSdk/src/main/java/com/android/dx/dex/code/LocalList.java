/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.dex.code;

import com.android.dx.dex.code.DalvInsn;
import com.android.dx.dex.code.DalvInsnList;
import com.android.dx.dex.code.LocalSnapshot;
import com.android.dx.dex.code.LocalStart;
import com.android.dx.rop.code.RegisterSpec;
import com.android.dx.rop.code.RegisterSpecSet;
import com.android.dx.rop.cst.CstString;
import com.android.dx.rop.cst.CstType;
import com.android.dx.rop.type.Type;
import com.android.dx.util.FixedSizeList;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;

public final class LocalList
extends FixedSizeList {
    public static final LocalList EMPTY = new LocalList(0);
    private static final boolean DEBUG = false;

    public LocalList(int size) {
        super(size);
    }

    public Entry get(int n) {
        return (Entry)this.get0(n);
    }

    public void set(int n, Entry entry) {
        this.set0(n, entry);
    }

    public void debugPrint(PrintStream out, String prefix) {
        int sz = this.size();
        for (int i = 0; i < sz; ++i) {
            out.print(prefix);
            out.println(this.get(i));
        }
    }

    public static LocalList make(DalvInsnList insns) {
        int sz = insns.size();
        MakeState state = new MakeState(sz);
        for (int i = 0; i < sz; ++i) {
            DalvInsn insn = insns.get(i);
            if (insn instanceof LocalSnapshot) {
                RegisterSpecSet snapshot = ((LocalSnapshot)insn).getLocals();
                state.snapshot(insn.getAddress(), snapshot);
                continue;
            }
            if (!(insn instanceof LocalStart)) continue;
            RegisterSpec local = ((LocalStart)insn).getLocal();
            state.startLocal(insn.getAddress(), local);
        }
        LocalList result = state.finish();
        return result;
    }

    private static void debugVerify(LocalList locals) {
        try {
            LocalList.debugVerify0(locals);
        }
        catch (RuntimeException ex) {
            int sz = locals.size();
            for (int i = 0; i < sz; ++i) {
                System.err.println(locals.get(i));
            }
            throw ex;
        }
    }

    private static void debugVerify0(LocalList locals) {
        int sz = locals.size();
        Entry[] active = new Entry[65536];
        for (int i = 0; i < sz; ++i) {
            Entry test;
            Entry e = locals.get(i);
            int reg = e.getRegister();
            if (e.isStart()) {
                Entry already = active[reg];
                if (already != null && e.matches(already)) {
                    throw new RuntimeException("redundant start at " + Integer.toHexString(e.getAddress()) + ": got " + e + "; had " + already);
                }
                active[reg] = e;
                continue;
            }
            if (active[reg] == null) {
                throw new RuntimeException("redundant end at " + Integer.toHexString(e.getAddress()));
            }
            int addr = e.getAddress();
            boolean foundStart = false;
            for (int j = i + 1; j < sz && (test = locals.get(j)).getAddress() == addr; ++j) {
                if (test.getRegisterSpec().getReg() != reg) continue;
                if (test.isStart()) {
                    if (e.getDisposition() != Disposition.END_REPLACED) {
                        throw new RuntimeException("improperly marked end at " + Integer.toHexString(addr));
                    }
                    foundStart = true;
                    continue;
                }
                throw new RuntimeException("redundant end at " + Integer.toHexString(addr));
            }
            if (!foundStart && e.getDisposition() == Disposition.END_REPLACED) {
                throw new RuntimeException("improper end replacement claim at " + Integer.toHexString(addr));
            }
            active[reg] = null;
        }
    }

    public static class MakeState {
        private final ArrayList<Entry> result;
        private int nullResultCount;
        private RegisterSpecSet regs;
        private int[] endIndices;
        private final int lastAddress;

        public MakeState(int initialSize) {
            this.result = new ArrayList(initialSize);
            this.nullResultCount = 0;
            this.regs = null;
            this.endIndices = null;
            this.lastAddress = 0;
        }

        private void aboutToProcess(int address, int reg) {
            boolean first;
            boolean bl = first = this.endIndices == null;
            if (address == this.lastAddress && !first) {
                return;
            }
            if (address < this.lastAddress) {
                throw new RuntimeException("shouldn't happen");
            }
            if (first || reg >= this.endIndices.length) {
                int newSz = reg + 1;
                RegisterSpecSet newRegs = new RegisterSpecSet(newSz);
                int[] newEnds = new int[newSz];
                Arrays.fill(newEnds, -1);
                if (!first) {
                    newRegs.putAll(this.regs);
                    System.arraycopy(this.endIndices, 0, newEnds, 0, this.endIndices.length);
                }
                this.regs = newRegs;
                this.endIndices = newEnds;
            }
        }

        public void snapshot(int address, RegisterSpecSet specs) {
            int sz = specs.getMaxSize();
            this.aboutToProcess(address, sz - 1);
            for (int i = 0; i < sz; ++i) {
                RegisterSpec oldSpec = this.regs.get(i);
                RegisterSpec newSpec = MakeState.filterSpec(specs.get(i));
                if (oldSpec == null) {
                    if (newSpec == null) continue;
                    this.startLocal(address, newSpec);
                    continue;
                }
                if (newSpec == null) {
                    this.endLocal(address, oldSpec);
                    continue;
                }
                if (newSpec.equalsUsingSimpleType(oldSpec)) continue;
                this.endLocal(address, oldSpec);
                this.startLocal(address, newSpec);
            }
        }

        public void startLocal(int address, RegisterSpec startedLocal) {
            RegisterSpec justAbove;
            RegisterSpec justBelow;
            Entry endEntry;
            int regNum = startedLocal.getReg();
            startedLocal = MakeState.filterSpec(startedLocal);
            this.aboutToProcess(address, regNum);
            RegisterSpec existingLocal = this.regs.get(regNum);
            if (startedLocal.equalsUsingSimpleType(existingLocal)) {
                return;
            }
            RegisterSpec movedLocal = this.regs.findMatchingLocal(startedLocal);
            if (movedLocal != null) {
                this.addOrUpdateEnd(address, Disposition.END_MOVED, movedLocal);
            }
            int endAt = this.endIndices[regNum];
            if (existingLocal != null) {
                this.add(address, Disposition.END_REPLACED, existingLocal);
            } else if (endAt >= 0 && (endEntry = this.result.get(endAt)).getAddress() == address) {
                if (endEntry.matches(startedLocal)) {
                    this.result.set(endAt, null);
                    ++this.nullResultCount;
                    this.regs.put(startedLocal);
                    this.endIndices[regNum] = -1;
                    return;
                }
                endEntry = endEntry.withDisposition(Disposition.END_REPLACED);
                this.result.set(endAt, endEntry);
            }
            if (regNum > 0 && (justBelow = this.regs.get(regNum - 1)) != null && justBelow.isCategory2()) {
                this.addOrUpdateEnd(address, Disposition.END_CLOBBERED_BY_NEXT, justBelow);
            }
            if (startedLocal.isCategory2() && (justAbove = this.regs.get(regNum + 1)) != null) {
                this.addOrUpdateEnd(address, Disposition.END_CLOBBERED_BY_PREV, justAbove);
            }
            this.add(address, Disposition.START, startedLocal);
        }

        public void endLocal(int address, RegisterSpec endedLocal) {
            this.endLocal(address, endedLocal, Disposition.END_SIMPLY);
        }

        public void endLocal(int address, RegisterSpec endedLocal, Disposition disposition) {
            int regNum = endedLocal.getReg();
            endedLocal = MakeState.filterSpec(endedLocal);
            this.aboutToProcess(address, regNum);
            int endAt = this.endIndices[regNum];
            if (endAt >= 0) {
                return;
            }
            if (this.checkForEmptyRange(address, endedLocal)) {
                return;
            }
            this.add(address, disposition, endedLocal);
        }

        private boolean checkForEmptyRange(int address, RegisterSpec endedLocal) {
            Entry entry;
            int at;
            for (at = this.result.size() - 1; at >= 0; --at) {
                entry = this.result.get(at);
                if (entry == null) continue;
                if (entry.getAddress() != address) {
                    return false;
                }
                if (entry.matches(endedLocal)) break;
            }
            this.regs.remove(endedLocal);
            this.result.set(at, null);
            ++this.nullResultCount;
            int regNum = endedLocal.getReg();
            boolean found = false;
            entry = null;
            --at;
            while (at >= 0) {
                entry = this.result.get(at);
                if (entry != null && entry.getRegisterSpec().getReg() == regNum) {
                    found = true;
                    break;
                }
                --at;
            }
            if (found) {
                this.endIndices[regNum] = at;
                if (entry.getAddress() == address) {
                    this.result.set(at, entry.withDisposition(Disposition.END_SIMPLY));
                }
            }
            return true;
        }

        private static RegisterSpec filterSpec(RegisterSpec orig) {
            if (orig != null && orig.getType() == Type.KNOWN_NULL) {
                return orig.withType(Type.OBJECT);
            }
            return orig;
        }

        private void add(int address, Disposition disposition, RegisterSpec spec) {
            int regNum = spec.getReg();
            this.result.add(new Entry(address, disposition, spec));
            if (disposition == Disposition.START) {
                this.regs.put(spec);
                this.endIndices[regNum] = -1;
            } else {
                this.regs.remove(spec);
                this.endIndices[regNum] = this.result.size() - 1;
            }
        }

        private void addOrUpdateEnd(int address, Disposition disposition, RegisterSpec spec) {
            Entry endEntry;
            if (disposition == Disposition.START) {
                throw new RuntimeException("shouldn't happen");
            }
            int regNum = spec.getReg();
            int endAt = this.endIndices[regNum];
            if (endAt >= 0 && (endEntry = this.result.get(endAt)).getAddress() == address && endEntry.getRegisterSpec().equals(spec)) {
                this.result.set(endAt, endEntry.withDisposition(disposition));
                this.regs.remove(spec);
                return;
            }
            this.endLocal(address, spec, disposition);
        }

        public LocalList finish() {
            this.aboutToProcess(Integer.MAX_VALUE, 0);
            int resultSz = this.result.size();
            int finalSz = resultSz - this.nullResultCount;
            if (finalSz == 0) {
                return EMPTY;
            }
            Object[] resultArr = new Entry[finalSz];
            if (resultSz == finalSz) {
                this.result.toArray(resultArr);
            } else {
                int at = 0;
                for (Entry e : this.result) {
                    if (e == null) continue;
                    resultArr[at++] = e;
                }
            }
            Arrays.sort(resultArr);
            LocalList resultList = new LocalList(finalSz);
            for (int i = 0; i < finalSz; ++i) {
                resultList.set(i, (Entry)resultArr[i]);
            }
            resultList.setImmutable();
            return resultList;
        }
    }

    public static class Entry
    implements Comparable<Entry> {
        private final int address;
        private final Disposition disposition;
        private final RegisterSpec spec;
        private final CstType type;

        public Entry(int address, Disposition disposition, RegisterSpec spec) {
            if (address < 0) {
                throw new IllegalArgumentException("address < 0");
            }
            if (disposition == null) {
                throw new NullPointerException("disposition == null");
            }
            try {
                if (spec.getLocalItem() == null) {
                    throw new NullPointerException("spec.getLocalItem() == null");
                }
            }
            catch (NullPointerException ex) {
                throw new NullPointerException("spec == null");
            }
            this.address = address;
            this.disposition = disposition;
            this.spec = spec;
            this.type = CstType.intern(spec.getType());
        }

        public String toString() {
            return Integer.toHexString(this.address) + " " + (Object)((Object)this.disposition) + " " + this.spec;
        }

        public boolean equals(Object other) {
            if (!(other instanceof Entry)) {
                return false;
            }
            return this.compareTo((Entry)other) == 0;
        }

        @Override
        public int compareTo(Entry other) {
            boolean otherIsStart;
            if (this.address < other.address) {
                return -1;
            }
            if (this.address > other.address) {
                return 1;
            }
            boolean thisIsStart = this.isStart();
            if (thisIsStart != (otherIsStart = other.isStart())) {
                return thisIsStart ? 1 : -1;
            }
            return this.spec.compareTo(other.spec);
        }

        public int getAddress() {
            return this.address;
        }

        public Disposition getDisposition() {
            return this.disposition;
        }

        public boolean isStart() {
            return this.disposition == Disposition.START;
        }

        public CstString getName() {
            return this.spec.getLocalItem().getName();
        }

        public CstString getSignature() {
            return this.spec.getLocalItem().getSignature();
        }

        public CstType getType() {
            return this.type;
        }

        public int getRegister() {
            return this.spec.getReg();
        }

        public RegisterSpec getRegisterSpec() {
            return this.spec;
        }

        public boolean matches(RegisterSpec otherSpec) {
            return this.spec.equalsUsingSimpleType(otherSpec);
        }

        public boolean matches(Entry other) {
            return this.matches(other.spec);
        }

        public Entry withDisposition(Disposition disposition) {
            if (disposition == this.disposition) {
                return this;
            }
            return new Entry(this.address, disposition, this.spec);
        }
    }

    public static enum Disposition {
        START,
        END_SIMPLY,
        END_REPLACED,
        END_MOVED,
        END_CLOBBERED_BY_PREV,
        END_CLOBBERED_BY_NEXT;

    }
}

