/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.dex.cf;

import com.android.dx.dex.code.DalvCode;
import com.android.dx.rop.code.RopMethod;
import java.io.PrintStream;

public final class CodeStatistics {
    private static final boolean DEBUG = false;
    public int runningDeltaRegisters = 0;
    public int runningDeltaInsns = 0;
    public int runningTotalInsns = 0;
    public int dexRunningDeltaRegisters = 0;
    public int dexRunningDeltaInsns = 0;
    public int dexRunningTotalInsns = 0;
    public int runningOriginalBytes = 0;

    public void updateOriginalByteCount(int count) {
        this.runningOriginalBytes += count;
    }

    public void updateDexStatistics(DalvCode nonOptCode, DalvCode code) {
        this.dexRunningDeltaInsns += code.getInsns().codeSize() - nonOptCode.getInsns().codeSize();
        this.dexRunningDeltaRegisters += code.getInsns().getRegistersSize() - nonOptCode.getInsns().getRegistersSize();
        this.dexRunningTotalInsns += code.getInsns().codeSize();
    }

    public void updateRopStatistics(RopMethod nonOptRmeth, RopMethod rmeth) {
        int oldCountInsns = nonOptRmeth.getBlocks().getEffectiveInstructionCount();
        int oldCountRegs = nonOptRmeth.getBlocks().getRegCount();
        int newCountInsns = rmeth.getBlocks().getEffectiveInstructionCount();
        this.runningDeltaInsns += newCountInsns - oldCountInsns;
        this.runningDeltaRegisters += rmeth.getBlocks().getRegCount() - oldCountRegs;
        this.runningTotalInsns += newCountInsns;
    }

    public void dumpStatistics(PrintStream out) {
        out.printf("Optimizer Delta Rop Insns: %d total: %d (%.2f%%) Delta Registers: %d\n", this.runningDeltaInsns, this.runningTotalInsns, 100.0 * (double)((float)this.runningDeltaInsns / (float)(this.runningTotalInsns + Math.abs(this.runningDeltaInsns))), this.runningDeltaRegisters);
        out.printf("Optimizer Delta Dex Insns: Insns: %d total: %d (%.2f%%) Delta Registers: %d\n", this.dexRunningDeltaInsns, this.dexRunningTotalInsns, 100.0 * (double)((float)this.dexRunningDeltaInsns / (float)(this.dexRunningTotalInsns + Math.abs(this.dexRunningDeltaInsns))), this.dexRunningDeltaRegisters);
        out.printf("Original bytecode byte count: %d\n", this.runningOriginalBytes);
    }
}

