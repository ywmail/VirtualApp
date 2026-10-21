/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.ssa;

import com.android.dx.rop.code.RopMethod;
import com.android.dx.rop.code.TranslationAdvice;
import com.android.dx.ssa.ConstCollector;
import com.android.dx.ssa.DeadCodeRemover;
import com.android.dx.ssa.EscapeAnalysis;
import com.android.dx.ssa.LiteralOpUpgrader;
import com.android.dx.ssa.MoveParamCombiner;
import com.android.dx.ssa.PhiTypeResolver;
import com.android.dx.ssa.SCCP;
import com.android.dx.ssa.SsaConverter;
import com.android.dx.ssa.SsaMethod;
import com.android.dx.ssa.back.LivenessAnalyzer;
import com.android.dx.ssa.back.SsaToRop;
import java.util.AbstractCollection;
import java.util.EnumSet;

public class Optimizer {
    private static boolean preserveLocals = true;
    private static TranslationAdvice advice;

    public static boolean getPreserveLocals() {
        return preserveLocals;
    }

    public static TranslationAdvice getAdvice() {
        return advice;
    }

    public static RopMethod optimize(RopMethod rmeth, int paramWidth, boolean isStatic, boolean inPreserveLocals, TranslationAdvice inAdvice) {
        return Optimizer.optimize(rmeth, paramWidth, isStatic, inPreserveLocals, inAdvice, EnumSet.allOf(OptionalStep.class));
    }

    public static RopMethod optimize(RopMethod rmeth, int paramWidth, boolean isStatic, boolean inPreserveLocals, TranslationAdvice inAdvice, EnumSet<OptionalStep> steps) {
        SsaMethod ssaMeth = null;
        preserveLocals = inPreserveLocals;
        advice = inAdvice;
        ssaMeth = SsaConverter.convertToSsaMethod(rmeth, paramWidth, isStatic);
        Optimizer.runSsaFormSteps(ssaMeth, steps);
        RopMethod resultMeth = SsaToRop.convertToRopMethod(ssaMeth, false);
        if (resultMeth.getBlocks().getRegCount() > advice.getMaxOptimalRegisterCount()) {
            resultMeth = Optimizer.optimizeMinimizeRegisters(rmeth, paramWidth, isStatic, steps);
        }
        return resultMeth;
    }

    private static RopMethod optimizeMinimizeRegisters(RopMethod rmeth, int paramWidth, boolean isStatic, EnumSet<OptionalStep> steps) {
        SsaMethod ssaMeth = SsaConverter.convertToSsaMethod(rmeth, paramWidth, isStatic);
        Object newSteps = steps.clone();
        ((AbstractCollection)newSteps).remove((Object)OptionalStep.CONST_COLLECTOR);
        Optimizer.runSsaFormSteps(ssaMeth, (EnumSet<OptionalStep>)newSteps);
        RopMethod resultMeth = SsaToRop.convertToRopMethod(ssaMeth, true);
        return resultMeth;
    }

    private static void runSsaFormSteps(SsaMethod ssaMeth, EnumSet<OptionalStep> steps) {
        boolean needsDeadCodeRemover = true;
        if (steps.contains((Object)OptionalStep.MOVE_PARAM_COMBINER)) {
            MoveParamCombiner.process(ssaMeth);
        }
        if (steps.contains((Object)OptionalStep.SCCP)) {
            SCCP.process(ssaMeth);
            DeadCodeRemover.process(ssaMeth);
            needsDeadCodeRemover = false;
        }
        if (steps.contains((Object)OptionalStep.LITERAL_UPGRADE)) {
            LiteralOpUpgrader.process(ssaMeth);
            DeadCodeRemover.process(ssaMeth);
            needsDeadCodeRemover = false;
        }
        steps.remove((Object)OptionalStep.ESCAPE_ANALYSIS);
        if (steps.contains((Object)OptionalStep.ESCAPE_ANALYSIS)) {
            EscapeAnalysis.process(ssaMeth);
            DeadCodeRemover.process(ssaMeth);
            needsDeadCodeRemover = false;
        }
        if (steps.contains((Object)OptionalStep.CONST_COLLECTOR)) {
            ConstCollector.process(ssaMeth);
            DeadCodeRemover.process(ssaMeth);
            needsDeadCodeRemover = false;
        }
        if (needsDeadCodeRemover) {
            DeadCodeRemover.process(ssaMeth);
        }
        PhiTypeResolver.process(ssaMeth);
    }

    public static SsaMethod debugEdgeSplit(RopMethod rmeth, int paramWidth, boolean isStatic, boolean inPreserveLocals, TranslationAdvice inAdvice) {
        preserveLocals = inPreserveLocals;
        advice = inAdvice;
        return SsaConverter.testEdgeSplit(rmeth, paramWidth, isStatic);
    }

    public static SsaMethod debugPhiPlacement(RopMethod rmeth, int paramWidth, boolean isStatic, boolean inPreserveLocals, TranslationAdvice inAdvice) {
        preserveLocals = inPreserveLocals;
        advice = inAdvice;
        return SsaConverter.testPhiPlacement(rmeth, paramWidth, isStatic);
    }

    public static SsaMethod debugRenaming(RopMethod rmeth, int paramWidth, boolean isStatic, boolean inPreserveLocals, TranslationAdvice inAdvice) {
        preserveLocals = inPreserveLocals;
        advice = inAdvice;
        return SsaConverter.convertToSsaMethod(rmeth, paramWidth, isStatic);
    }

    public static SsaMethod debugDeadCodeRemover(RopMethod rmeth, int paramWidth, boolean isStatic, boolean inPreserveLocals, TranslationAdvice inAdvice) {
        preserveLocals = inPreserveLocals;
        advice = inAdvice;
        SsaMethod ssaMeth = SsaConverter.convertToSsaMethod(rmeth, paramWidth, isStatic);
        DeadCodeRemover.process(ssaMeth);
        return ssaMeth;
    }

    public static SsaMethod debugNoRegisterAllocation(RopMethod rmeth, int paramWidth, boolean isStatic, boolean inPreserveLocals, TranslationAdvice inAdvice, EnumSet<OptionalStep> steps) {
        preserveLocals = inPreserveLocals;
        advice = inAdvice;
        SsaMethod ssaMeth = SsaConverter.convertToSsaMethod(rmeth, paramWidth, isStatic);
        Optimizer.runSsaFormSteps(ssaMeth, steps);
        LivenessAnalyzer.constructInterferenceGraph(ssaMeth);
        return ssaMeth;
    }

    public static enum OptionalStep {
        MOVE_PARAM_COMBINER,
        SCCP,
        LITERAL_UPGRADE,
        CONST_COLLECTOR,
        ESCAPE_ANALYSIS;

    }
}

