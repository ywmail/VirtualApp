/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.os.Handler
 *  android.os.IBinder
 */
package com.carlos.science.stebcore;

import android.app.Activity;
import android.os.Handler;
import android.os.IBinder;
import com.carlos.science.IVirtualController;
import com.carlos.science.stebcore.IStep;
import com.carlos.science.stebcore.IStepInfo;
import com.kook.controller.server.IServerController;

public interface IStepController<T extends IStep> {
    public void initSteps();

    public Activity getCurrentActivity();

    public Handler getHandler();

    public IBinder getCallBackIBinder();

    public IServerController getIServerController();

    public IVirtualController getVirtualControllerImpl();

    public void doTask(IStepInfo<T> var1);

    public boolean hasNext();

    public void finishCurrentStep();

    public void finishAllSteps();

    public boolean hasFinishAllSteps();

    public IStep getCurrentStep();

    public static interface StepStatusListener {
        public void onStepFinished(IStep var1);

        public void onAllStepsFinished();
    }
}

