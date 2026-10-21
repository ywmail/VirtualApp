/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.IBinder
 *  android.util.Log
 */
package com.carlos.science.stebcore;

import android.content.Context;
import android.os.IBinder;
import android.util.Log;
import com.carlos.libcommon.StringFog;
import com.carlos.science.client.ClientActivityLifecycle;
import com.carlos.science.stebcore.IStep;
import com.carlos.science.stebcore.IStepController;
import com.carlos.science.stebcore.IStepInfo;
import com.carlos.science.stebcore.StepImpl;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public abstract class StepControllerImpl<T extends IStep>
implements IStepController {
    public static final String TAG = "StepControllerImpl";
    protected List<IStepController.StepStatusListener> mStepStatusListeners = new ArrayList<IStepController.StepStatusListener>();
    protected Queue<IStepInfo<T>> mStepInfos = new LinkedList<IStepInfo<T>>();
    protected ClientActivityLifecycle clientActivityLifecycle;
    protected IBinder callBackIBinder;
    protected StepImpl mCurrentStep;

    public StepControllerImpl(ClientActivityLifecycle clientActivityLifecycle, IBinder iBinder) {
        this.clientActivityLifecycle = clientActivityLifecycle;
        this.callBackIBinder = iBinder;
        this.initSteps();
    }

    public Context getContext() {
        return this.clientActivityLifecycle.getCurrentActivity();
    }

    public void setCllBackIBinder(IBinder ibinder) {
        if (ibinder == null) {
            return;
        }
        this.callBackIBinder = ibinder;
    }

    public final boolean addStep(IStepInfo stepInfo) {
        boolean success = this.mStepInfos.offer(stepInfo);
        return success;
    }

    protected final IStepInfo<T> getTopStepInfo() {
        IStepInfo<T> poll = this.mStepInfos.poll();
        return poll;
    }

    @Override
    public abstract void initSteps();

    @Override
    public final boolean hasNext() {
        boolean res = !this.mStepInfos.isEmpty();
        Log.i((String)TAG, (String)("hasNext: " + res + "    size:" + this.mStepInfos.size()));
        return res;
    }

    @Override
    public final void finishCurrentStep() {
        IStepInfo<T> poll = null;
        this.mCurrentStep = null;
        if (this.hasNext()) {
            poll = this.getTopStepInfo();
        } else {
            this.notifyAllStepsFinished();
        }
        if (poll != null) {
            Log.d((String)TAG, (String)("功能步骤控制器 开始执行 当前步骤:" + poll + "   '" + poll.getTitle() + "'"));
            this.doTask(poll);
        }
    }

    public final void notifyAllStepsFinished() {
        for (IStepController.StepStatusListener listener : this.mStepStatusListeners) {
            if (listener == null) continue;
            listener.onAllStepsFinished();
        }
    }

    public void addStepStatusListener(IStepController.StepStatusListener listener) {
        if (listener == null) {
            this.mStepStatusListeners.clear();
        } else if (!this.mStepStatusListeners.contains(listener)) {
            this.mStepStatusListeners.add(listener);
        }
    }

    @Override
    public final void finishAllSteps() {
        while (this.hasNext()) {
            IStepInfo<T> poll = this.mStepInfos.poll();
            StepImpl stepImpl = (StepImpl)poll.getStepImpl();
            stepImpl.finish();
        }
    }

    @Override
    public final boolean hasFinishAllSteps() {
        boolean hasNext = this.hasNext();
        StepImpl currentStep = (StepImpl)this.getCurrentStep();
        boolean finishStep = currentStep.finishStep;
        return hasNext || finishStep;
    }

    public abstract void fastForward();
}

