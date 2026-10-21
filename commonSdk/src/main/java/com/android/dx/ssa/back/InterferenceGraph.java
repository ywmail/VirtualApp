/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.ssa.back;

import com.android.dx.ssa.SetFactory;
import com.android.dx.util.IntSet;
import java.util.ArrayList;

public class InterferenceGraph {
    private final ArrayList<IntSet> interference;

    public InterferenceGraph(int countRegs) {
        this.interference = new ArrayList(countRegs);
        for (int i = 0; i < countRegs; ++i) {
            this.interference.add(SetFactory.makeInterferenceSet(countRegs));
        }
    }

    public void add(int regV, int regW) {
        this.ensureCapacity(Math.max(regV, regW) + 1);
        this.interference.get(regV).add(regW);
        this.interference.get(regW).add(regV);
    }

    public void dumpToStdout() {
        int oldRegCount = this.interference.size();
        for (int i = 0; i < oldRegCount; ++i) {
            StringBuilder sb = new StringBuilder();
            sb.append("Reg " + i + ":" + this.interference.get(i).toString());
            System.out.println(sb.toString());
        }
    }

    public void mergeInterferenceSet(int reg, IntSet set) {
        if (reg < this.interference.size()) {
            set.merge(this.interference.get(reg));
        }
    }

    private void ensureCapacity(int size) {
        int countRegs = this.interference.size();
        this.interference.ensureCapacity(size);
        for (int i = countRegs; i < size; ++i) {
            this.interference.add(SetFactory.makeInterferenceSet(size));
        }
    }
}

