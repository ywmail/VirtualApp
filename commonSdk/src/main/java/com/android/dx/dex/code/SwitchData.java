/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.dex.code;

import com.android.dx.dex.code.CodeAddress;
import com.android.dx.dex.code.DalvInsn;
import com.android.dx.dex.code.Dops;
import com.android.dx.dex.code.VariableSizeInsn;
import com.android.dx.rop.code.RegisterSpecList;
import com.android.dx.rop.code.SourcePosition;
import com.android.dx.util.AnnotatedOutput;
import com.android.dx.util.Hex;
import com.android.dx.util.IntList;

public final class SwitchData
extends VariableSizeInsn {
    private final CodeAddress user;
    private final IntList cases;
    private final CodeAddress[] targets;
    private final boolean packed;

    public SwitchData(SourcePosition position, CodeAddress user, IntList cases, CodeAddress[] targets) {
        super(position, RegisterSpecList.EMPTY);
        if (user == null) {
            throw new NullPointerException("user == null");
        }
        if (cases == null) {
            throw new NullPointerException("cases == null");
        }
        if (targets == null) {
            throw new NullPointerException("targets == null");
        }
        int sz = cases.size();
        if (sz != targets.length) {
            throw new IllegalArgumentException("cases / targets mismatch");
        }
        if (sz > 65535) {
            throw new IllegalArgumentException("too many cases");
        }
        this.user = user;
        this.cases = cases;
        this.targets = targets;
        this.packed = SwitchData.shouldPack(cases);
    }

    @Override
    public int codeSize() {
        return this.packed ? (int)SwitchData.packedCodeSize(this.cases) : (int)SwitchData.sparseCodeSize(this.cases);
    }

    @Override
    public void writeTo(AnnotatedOutput out) {
        int baseAddress = this.user.getAddress();
        int defaultTarget = Dops.PACKED_SWITCH.getFormat().codeSize();
        int sz = this.targets.length;
        if (this.packed) {
            int firstCase = sz == 0 ? 0 : this.cases.get(0);
            int lastCase = sz == 0 ? 0 : this.cases.get(sz - 1);
            int outSz = lastCase - firstCase + 1;
            out.writeShort(256);
            out.writeShort(outSz);
            out.writeInt(firstCase);
            int caseAt = 0;
            for (int i = 0; i < outSz; ++i) {
                int relTarget;
                int outCase = firstCase + i;
                int oneCase = this.cases.get(caseAt);
                if (oneCase > outCase) {
                    relTarget = defaultTarget;
                } else {
                    relTarget = this.targets[caseAt].getAddress() - baseAddress;
                    ++caseAt;
                }
                out.writeInt(relTarget);
            }
        } else {
            int i;
            out.writeShort(512);
            out.writeShort(sz);
            for (i = 0; i < sz; ++i) {
                out.writeInt(this.cases.get(i));
            }
            for (i = 0; i < sz; ++i) {
                int relTarget = this.targets[i].getAddress() - baseAddress;
                out.writeInt(relTarget);
            }
        }
    }

    @Override
    public DalvInsn withRegisters(RegisterSpecList registers) {
        return new SwitchData(this.getPosition(), this.user, this.cases, this.targets);
    }

    public boolean isPacked() {
        return this.packed;
    }

    @Override
    protected String argString() {
        StringBuilder sb = new StringBuilder(100);
        int sz = this.targets.length;
        for (int i = 0; i < sz; ++i) {
            sb.append("\n    ");
            sb.append(this.cases.get(i));
            sb.append(": ");
            sb.append(this.targets[i]);
        }
        return sb.toString();
    }

    @Override
    protected String listingString0(boolean noteIndices) {
        int baseAddress = this.user.getAddress();
        StringBuilder sb = new StringBuilder(100);
        int sz = this.targets.length;
        sb.append(this.packed ? "packed" : "sparse");
        sb.append("-switch-payload // for switch @ ");
        sb.append(Hex.u2(baseAddress));
        for (int i = 0; i < sz; ++i) {
            int absTarget = this.targets[i].getAddress();
            int relTarget = absTarget - baseAddress;
            sb.append("\n  ");
            sb.append(this.cases.get(i));
            sb.append(": ");
            sb.append(Hex.u4(absTarget));
            sb.append(" // ");
            sb.append(Hex.s4(relTarget));
        }
        return sb.toString();
    }

    private static long packedCodeSize(IntList cases) {
        int sz = cases.size();
        long low = cases.get(0);
        long high = cases.get(sz - 1);
        long result = (high - low + 1L) * 2L + 4L;
        return result <= Integer.MAX_VALUE ? result : -1L;
    }

    private static long sparseCodeSize(IntList cases) {
        int sz = cases.size();
        return (long)sz * 4L + 2L;
    }

    private static boolean shouldPack(IntList cases) {
        int sz = cases.size();
        if (sz < 2) {
            return true;
        }
        long packedSize = SwitchData.packedCodeSize(cases);
        long sparseSize = SwitchData.sparseCodeSize(cases);
        return packedSize >= 0L && packedSize <= sparseSize * 5L / 4L;
    }
}

