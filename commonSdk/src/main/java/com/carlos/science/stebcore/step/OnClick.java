/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.os.RemoteException
 *  android.util.Log
 *  android.view.View
 */
package com.carlos.science.stebcore.step;

import android.app.Activity;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import com.carlos.libcommon.StringFog;
import com.carlos.science.IVirtualController;
import com.carlos.science.client.core.FindView;
import com.carlos.science.stebcore.StepImpl;
import com.kook.controller.server.IServerController;

public abstract class OnClick
extends StepImpl {
    protected String TAG = "OnClick";
    protected int tryMax = 5;
    protected boolean doubleClick = false;

    @Override
    public void onActivityResumed(Activity activity) {
    }

    @Override
    public boolean task() {
        int[] center = this.getLocCenterByView();
        if (center == null) {
            for (int tryCount = 0; tryCount < this.tryMax; ++tryCount) {
                if (this.finishStep) {
                    return true;
                }
                this.sleep(400L);
                center = this.getLocCenterByView();
                Log.d((String)this.TAG, (String)"再次尝试view 的位置");
                if (center != null) break;
            }
            if (center == null) {
                Log.d((String)this.TAG, (String)"这里查找到的view 应该是为null,所以这里center 才是null");
                return false;
            }
        }
        int centerX = center[0];
        int centerY = center[1];
        IVirtualController virtualControllerImpl = this.getVirtualControllerImpl();
        try {
            IServerController iServerControler = this.getIServerControler();
            if (this.doubleClick) {
                iServerControler.virtualClick(centerX, centerY);
                this.sleep(50L);
                iServerControler.virtualClick(centerX, centerY);
            } else {
                iServerControler.virtualClick(centerX, centerY);
            }
        }
        catch (RemoteException e) {
            e.printStackTrace();
            Log.e((String)this.TAG, (String)("StemClick doTask exception:" + e.toString()));
        }
        return true;
    }

    protected abstract View findView();

    public int[] getLocCenterByView() {
        View viewByActivity = this.findView();
        if (viewByActivity == null) {
            return null;
        }
        int[] loc = FindView.getLocationOnScreen(viewByActivity);
        int width = viewByActivity.getWidth();
        int height = viewByActivity.getHeight();
        int centerX = loc[0] + width / 2;
        int centerY = loc[1] + height / 2;
        int[] center = new int[]{centerX, centerY};
        return center;
    }
}

