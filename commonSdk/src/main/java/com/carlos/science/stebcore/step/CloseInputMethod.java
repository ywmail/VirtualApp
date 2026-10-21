/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  org.jdeferred.Promise
 */
package com.carlos.science.stebcore.step;

import android.app.Activity;
import com.carlos.common.utils.ResponseProgram;
import com.carlos.science.stebcore.StepImpl;
import org.jdeferred.Promise;

public class CloseInputMethod
extends StepImpl {
    public static CloseInputMethod getInstance() {
        CloseInputMethod closeInputMethod = new CloseInputMethod();
        return closeInputMethod;
    }

    @Override
    public void onActivityResumed(Activity activity) {
    }

    @Override
    public Promise<Void, Throwable, Void> beforeTask() {
        return ResponseProgram.defer().when(() -> this.sleep(400L));
    }

    @Override
    public boolean task() {
        this.finish();
        return true;
    }

    @Override
    public void afterTask() {
    }
}

