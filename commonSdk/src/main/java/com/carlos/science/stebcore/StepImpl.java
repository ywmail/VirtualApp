/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.graphics.Rect
 *  android.os.Handler
 *  android.os.IBinder
 *  android.util.Log
 *  org.jdeferred.Promise
 */
package com.carlos.science.stebcore;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import com.carlos.libcommon.StringFog;
import com.carlos.science.IVirtualController;
import com.carlos.science.stebcore.IStep;
import com.carlos.science.stebcore.IStepController;
import com.carlos.science.stebcore.IStepInfo;
import com.kook.common.utils.HVLog;
import com.kook.controller.server.IServerController;
import org.jdeferred.Promise;

public abstract class StepImpl
implements IStep {
    protected static final String TAG = "StepImpl";
    private IStepInfo<StepImpl> mStepInfo;
    public boolean finishStep = false;
    IStepController mIStepController;
    protected Rect rect = new Rect();

    public Activity getActivity() {
        return this.mIStepController.getCurrentActivity();
    }

    public void setIStepControl(IStepController stepController) {
        this.mIStepController = stepController;
    }

    public abstract void onActivityResumed(Activity var1);

    public IServerController getIServerControler() {
        return this.mIStepController.getIServerController();
    }

    public IVirtualController getVirtualControllerImpl() {
        return this.mIStepController.getVirtualControllerImpl();
    }

    protected Handler getHandler() {
        return this.mIStepController.getHandler();
    }

    public IBinder getCallBackIBinder() {
        Log.d((String)TAG, (String)("getCallBackIBinder mIStepController:" + this.mIStepController));
        return this.mIStepController.getCallBackIBinder();
    }

    @Override
    public final void doTask() {
        Promise<Void, Throwable, Void> beforeTask = this.beforeTask();
        if (beforeTask != null) {
            beforeTask.done(VOID -> {
                if (this.finishStep) {
                    return;
                }
                boolean task = this.task();
                if (task) {
                    this.afterTask();
                } else {
                    this.doTask();
                }
            });
        } else {
            if (this.finishStep) {
                return;
            }
            boolean task = this.task();
            if (task) {
                this.afterTask();
            } else {
                this.doTask();
            }
        }
    }

    public abstract Promise<Void, Throwable, Void> beforeTask();

    public abstract boolean task();

    public abstract void afterTask();

    public String getTitle() {
        return this.getStepInfo().getTitle();
    }

    @Override
    public final void finish() {
        if (this.finishStep) {
            Log.i((String)TAG, (String)("步骤 " + this.getClass().getName() + "  '" + this.getTitle() + "' 已经结束过了"));
            return;
        }
        Log.i((String)TAG, (String)("结束当前步骤 " + this.getClass().getName() + "  '" + this.getTitle() + "'"));
        if (this.getClass().getName().equals("com.kook.controller.client.wechat.StepPositionOnClick")) {
            HVLog.printInfo();
            Log.i((String)TAG, (String)"******************** 追踪 调用 堆栈 **********************");
        }
        this.getController().finishCurrentStep();
        this.finishStep = true;
    }

    @Override
    public IStepController getController() {
        IStepInfo stepInfo = this.getStepInfo();
        IStepController controller = stepInfo.getController();
        return controller;
    }

    @Override
    public IStepInfo getStepInfo() {
        return this.mStepInfo;
    }

    public void sleep(long time) {
        try {
            Thread.sleep(time);
        }
        catch (InterruptedException e) {
            Log.i((String)TAG, (String)("exception e:" + e.toString()));
        }
    }

    public void setStepInfo(IStepInfo stepInfo) {
        this.mStepInfo = stepInfo;
    }
}

