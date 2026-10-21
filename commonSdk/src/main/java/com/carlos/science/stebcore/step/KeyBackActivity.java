/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.util.Log
 *  android.view.View
 *  org.jdeferred.Promise
 */
package com.carlos.science.stebcore.step;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import com.carlos.common.utils.ResponseProgram;
import com.carlos.libcommon.StringFog;
import com.carlos.science.stebcore.StepImpl;
import com.kook.controller.server.IServerController;
import org.jdeferred.Promise;

public class KeyBackActivity
extends StepImpl {
    protected String className;

    public static KeyBackActivity getInstance(String className) {
        KeyBackActivity stepTrendActivity = new KeyBackActivity();
        stepTrendActivity.className = className;
        return stepTrendActivity;
    }

    @Override
    public void onActivityResumed(Activity activity) {
        Log.d((String)"StepImpl", (String)("当前是在" + activity.getLocalClassName() + "界面,准备进入 doTask "));
        this.task();
    }

    @Override
    public Promise<Void, Throwable, Void> beforeTask() {
        return null;
    }

    @Override
    public boolean task() {
        Activity activity = this.getActivity();
        String localClassName = activity.getLocalClassName();
        Log.d((String)"StepImpl", (String)("当前是在" + localClassName + "界面"));
        if (localClassName.equals(this.className)) {
            ResponseProgram.defer().when(() -> {
                boolean attachedToWindow;
                do {
                    if (this.finishStep) {
                        return;
                    }
                    View decorView = activity.getWindow().getDecorView();
                    attachedToWindow = decorView.isAttachedToWindow();
                    this.sleep(400L);
                } while (!attachedToWindow);
            }).done(VOID -> this.finish());
        } else {
            this.sendBackKey();
        }
        return true;
    }

    protected void sendBackKey() {
        try {
            IServerController iServerControler = this.getIServerControler();
            iServerControler.sendKeyEvent(4);
        }
        catch (Exception e) {
            Log.e((String)"StepImpl", (String)("StepTrend doTask " + e.toString()));
        }
    }

    @Override
    public void afterTask() {
    }
}

