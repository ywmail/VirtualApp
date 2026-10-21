/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.IBinder
 *  android.os.RemoteException
 *  android.view.LayoutInflater
 *  android.view.View
 *  android.widget.Button
 *  android.widget.Switch
 *  com.kook.librelease.R$id
 *  com.kook.librelease.R$layout
 */
package com.carlos.science.server.module.normal;

import android.os.IBinder;
import android.os.RemoteException;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import com.carlos.libcommon.StringFog;
import com.carlos.science.FloatBallManager;
import com.carlos.science.tab.FloatTab;
import com.carlos.science.tab.TabContainer;
import com.kook.common.utils.HVLog;
import com.kook.controller.client.normal.ILearnController;
import com.kook.librelease.R;

public class NormalTab
extends TabContainer {
    String TAG = "NormalTab";
    FloatTab floatTab;
    Button featuresMenu;
    ILearnController iLearnController;

    public NormalTab(LayoutInflater layoutInflater, FloatBallManager floatBallManager, int tabflag) {
        super(layoutInflater, floatBallManager, tabflag);
    }

    @Override
    protected int getViewId() {
        return R.layout.normal_tab;
    }

    @Override
    protected void findViews(View root) {
        Switch viewById = (Switch)this.findViewById(R.id.switch_menu);
        viewById.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                this.floatBallManager.hide();
            }
        });
    }

    @Override
    public Object getTabContainerData() {
        return null;
    }

    @Override
    public void setFloatTab(FloatTab tabLayout) {
        this.floatTab = tabLayout;
        this.featuresMenu = this.floatTab.getFeaturesMenu();
    }

    @Override
    public void onAttachedToWindow(FloatTab floatTab) {
        if (this.featuresMenu != null) {
            this.featuresMenu.setVisibility(4);
        }
    }

    @Override
    public void onCurrentPageSelected(FloatTab floatTab) {
    }

    @Override
    public void onClick(View view) {
        HVLog.d(this.TAG, " normalTab onClick " + (view == this.floatTab.getFeaturesMenu()));
        IBinder clientBinder = this.floatTab.getClientBinder();
        if (clientBinder == null) {
            return;
        }
        if (this.iLearnController == null) {
            this.iLearnController = ILearnController.Stub.asInterface(clientBinder);
        }
        try {
            this.iLearnController.debugLearn();
        }
        catch (RemoteException e) {
            HVLog.printException((Exception)((Object)e));
        }
        if (view == this.floatTab.getFeaturesMenu()) {
            // empty if block
        }
    }
}

