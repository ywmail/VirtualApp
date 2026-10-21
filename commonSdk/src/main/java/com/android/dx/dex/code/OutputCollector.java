/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.dex.code;

import com.android.dx.dex.DexOptions;
import com.android.dx.dex.code.CodeAddress;
import com.android.dx.dex.code.DalvInsn;
import com.android.dx.dex.code.OutputFinisher;
import java.util.ArrayList;

public final class OutputCollector {
    private final OutputFinisher finisher;
    private ArrayList<DalvInsn> suffix;

    public OutputCollector(DexOptions dexOptions, int initialCapacity, int suffixInitialCapacity, int regCount, int paramSize) {
        this.finisher = new OutputFinisher(dexOptions, initialCapacity, regCount, paramSize);
        this.suffix = new ArrayList(suffixInitialCapacity);
    }

    public void add(DalvInsn insn) {
        this.finisher.add(insn);
    }

    public void reverseBranch(int which, CodeAddress newTarget) {
        this.finisher.reverseBranch(which, newTarget);
    }

    public void addSuffix(DalvInsn insn) {
        this.suffix.add(insn);
    }

    public OutputFinisher getFinisher() {
        if (this.suffix == null) {
            throw new UnsupportedOperationException("already processed");
        }
        this.appendSuffixToOutput();
        return this.finisher;
    }

    private void appendSuffixToOutput() {
        int size = this.suffix.size();
        for (int i = 0; i < size; ++i) {
            this.finisher.add(this.suffix.get(i));
        }
        this.suffix = null;
    }
}

