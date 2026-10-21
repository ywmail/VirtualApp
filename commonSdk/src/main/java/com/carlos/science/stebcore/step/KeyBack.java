/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.util.Log
 *  org.jdeferred.Promise
 */
package com.carlos.science.stebcore.step;

import android.app.Activity;
import android.util.Log;
import com.carlos.common.utils.ResponseProgram;
import com.carlos.libcommon.StringFog;
import com.carlos.science.stebcore.StepImpl;
import com.kook.controller.server.IServerController;
import org.jdeferred.Promise;

public class KeyBack
extends StepImpl {
    public static KeyBack getInstance() {
        KeyBack stepTrendActivity = new KeyBack();
        return stepTrendActivity;
    }

    @Override
    public void onActivityResumed(Activity activity) {
    }

    @Override
    public Promise<Void, Throwable, Void> beforeTask() {
        return ResponseProgram.defer().when(() -> {
            this.sendBackKey();
            this.sleep(500L);
        }).done(VOID -> this.finish());
    }

    @Override
    public boolean task() {
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

