/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 */
package com.carlos.science.stebcore;

import android.app.Activity;
import com.carlos.science.stebcore.IStepController;
import com.carlos.science.stebcore.IStepInfo;
import com.carlos.science.stebcore.StepImpl;
import com.kook.controller.server.IServerController;
import java.util.UUID;

public class StepInfoImpl
implements IStepInfo<StepImpl> {
    private final long mId = UUID.randomUUID().getLeastSignificantBits();
    private StepImpl mStepImpl;
    private IStepController mController;
    private final String mTitle;

    public StepInfoImpl(StepImpl stepClass, IStepController stepControlller, String title) {
        this.mStepImpl = stepClass;
        this.mController = stepControlller;
        this.mStepImpl.setIStepControl(stepControlller);
        this.mTitle = title;
    }

    @Override
    public long getStepId() {
        return this.mId;
    }

    @Override
    public String getTitle() {
        return this.mTitle;
    }

    @Override
    public StepImpl getStepImpl() {
        Activity currentActivity = this.mController.getCurrentActivity();
        IServerController serverController = this.mController.getIServerController();
        this.mStepImpl.setStepInfo(this);
        return this.mStepImpl;
    }

    @Override
    public IStepController getController() {
        return this.mController;
    }
}

