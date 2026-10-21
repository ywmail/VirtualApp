/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.ssa;

import com.android.dx.rop.code.RegisterSpec;
import com.android.dx.ssa.RegisterMapper;
import com.android.dx.util.IntList;

public class BasicRegisterMapper
extends RegisterMapper {
    private final IntList oldToNew;
    private int runningCountNewRegisters;

    public BasicRegisterMapper(int countOldRegisters) {
        this.oldToNew = new IntList(countOldRegisters);
    }

    @Override
    public int getNewRegisterCount() {
        return this.runningCountNewRegisters;
    }

    @Override
    public RegisterSpec map(RegisterSpec registerSpec) {
        int newReg;
        if (registerSpec == null) {
            return null;
        }
        try {
            newReg = this.oldToNew.get(registerSpec.getReg());
        }
        catch (IndexOutOfBoundsException ex) {
            newReg = -1;
        }
        if (newReg < 0) {
            throw new RuntimeException("no mapping specified for register");
        }
        return registerSpec.withReg(newReg);
    }

    public int oldToNew(int oldReg) {
        if (oldReg >= this.oldToNew.size()) {
            return -1;
        }
        return this.oldToNew.get(oldReg);
    }

    public String toHuman() {
        StringBuilder sb = new StringBuilder();
        sb.append("Old\tNew\n");
        int sz = this.oldToNew.size();
        for (int i = 0; i < sz; ++i) {
            sb.append(i);
            sb.append('\t');
            sb.append(this.oldToNew.get(i));
            sb.append('\n');
        }
        sb.append("new reg count:");
        sb.append(this.runningCountNewRegisters);
        sb.append('\n');
        return sb.toString();
    }

    public void addMapping(int oldReg, int newReg, int category) {
        if (oldReg >= this.oldToNew.size()) {
            for (int i = oldReg - this.oldToNew.size(); i >= 0; --i) {
                this.oldToNew.add(-1);
            }
        }
        this.oldToNew.set(oldReg, newReg);
        if (this.runningCountNewRegisters < newReg + category) {
            this.runningCountNewRegisters = newReg + category;
        }
    }
}

