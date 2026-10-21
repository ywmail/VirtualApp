/*
 * Decompiled with CFR 0.152.
 */
package com.carlos.science.stebcore;

import com.carlos.science.stebcore.IStepController;
import com.carlos.science.stebcore.IStepInfo;

public interface IStep {
    public void finish();

    public void doTask();

    public IStepController getController();

    public IStepInfo getStepInfo();
}

