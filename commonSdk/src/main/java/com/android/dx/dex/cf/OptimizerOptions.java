/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.dex.cf;

import com.android.dx.dex.cf.CfOptions;
import com.android.dx.rop.code.RopMethod;
import com.android.dx.rop.code.TranslationAdvice;
import com.android.dx.ssa.Optimizer;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.EnumSet;
import java.util.HashSet;

public class OptimizerOptions {
    private HashSet<String> optimizeList;
    private HashSet<String> dontOptimizeList;
    private boolean optimizeListsLoaded;

    public void loadOptimizeLists(String optimizeListFile, String dontOptimizeListFile) {
        if (this.optimizeListsLoaded) {
            return;
        }
        if (optimizeListFile != null && dontOptimizeListFile != null) {
            throw new RuntimeException("optimize and don't optimize lists  are mutually exclusive.");
        }
        if (optimizeListFile != null) {
            this.optimizeList = OptimizerOptions.loadStringsFromFile(optimizeListFile);
        }
        if (dontOptimizeListFile != null) {
            this.dontOptimizeList = OptimizerOptions.loadStringsFromFile(dontOptimizeListFile);
        }
        this.optimizeListsLoaded = true;
    }

    private static HashSet<String> loadStringsFromFile(String filename) {
        HashSet<String> result = new HashSet<String>();
        try {
            String line;
            FileReader fr = new FileReader(filename);
            BufferedReader bfr = new BufferedReader(fr);
            while (null != (line = bfr.readLine())) {
                result.add(line);
            }
            fr.close();
        }
        catch (IOException ex) {
            throw new RuntimeException("Error with optimize list: " + filename, ex);
        }
        return result;
    }

    public void compareOptimizerStep(RopMethod nonOptRmeth, int paramSize, boolean isStatic, CfOptions args, TranslationAdvice advice, RopMethod rmeth) {
        EnumSet<Optimizer.OptionalStep> steps = EnumSet.allOf(Optimizer.OptionalStep.class);
        steps.remove((Object)Optimizer.OptionalStep.CONST_COLLECTOR);
        RopMethod skipRopMethod = Optimizer.optimize(nonOptRmeth, paramSize, isStatic, args.localInfo, advice, steps);
        int normalInsns = rmeth.getBlocks().getEffectiveInstructionCount();
        int skipInsns = skipRopMethod.getBlocks().getEffectiveInstructionCount();
        System.err.printf("optimize step regs:(%d/%d/%.2f%%) insns:(%d/%d/%.2f%%)\n", rmeth.getBlocks().getRegCount(), skipRopMethod.getBlocks().getRegCount(), 100.0 * (double)((float)(skipRopMethod.getBlocks().getRegCount() - rmeth.getBlocks().getRegCount()) / (float)skipRopMethod.getBlocks().getRegCount()), normalInsns, skipInsns, 100.0 * (double)((float)(skipInsns - normalInsns) / (float)skipInsns));
    }

    public boolean shouldOptimize(String canonicalMethodName) {
        if (this.optimizeList != null) {
            return this.optimizeList.contains(canonicalMethodName);
        }
        if (this.dontOptimizeList != null) {
            return !this.dontOptimizeList.contains(canonicalMethodName);
        }
        return true;
    }
}

