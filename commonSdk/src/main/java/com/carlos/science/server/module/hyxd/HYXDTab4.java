/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.IBinder
 *  android.os.RemoteException
 *  android.view.LayoutInflater
 *  android.view.View
 *  android.view.ViewGroup
 *  android.widget.Button
 *  android.widget.CompoundButton
 */
package com.carlos.science.server.module.hyxd;

import android.os.IBinder;
import android.os.RemoteException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import com.carlos.libcommon.StringFog;
import com.carlos.science.FloatBallManager;
import com.carlos.science.client.core.MemorySRWData;
import com.carlos.science.tab.FloatTab;
import com.carlos.science.tab.TabContainer;
import com.kook.common.utils.HVLog;
import com.kook.controller.client.hyxd.IHYXDController;

public class HYXDTab4
extends TabContainer {
    String TAG = "NormalTab";
    public static final int CHECKLAYOUT_SWICH_ID_41 = 41;
    public static final int CHECKLAYOUT_SWICH_ID_42 = 42;
    public static final int CHECKLAYOUT_SWICH_ID_43 = 43;
    public static final int CHECKLAYOUT_SWICH_ID_44 = 44;
    public static final int CHECKLAYOUT_SWICH_ID_45 = 45;
    public static final int CHECKLAYOUT_SWICH_ID_46 = 46;
    FloatTab floatTab;
    Button featuresMenu;

    public HYXDTab4(LayoutInflater layoutInflater, FloatBallManager floatBallManager, int tabflag) {
        super(layoutInflater, floatBallManager, tabflag);
    }

    @Override
    protected int getViewId() {
        return 0;
    }

    @Override
    protected void initViews(ViewGroup view) {
        view.addView((View)this.getCheckLayout().setText("大厅不推荐  飞行加速").setOnCheckedChangeListener(this).setSwitchId(41));
        view.addView((View)this.getCheckLayout().setText("大厅不推荐  秒药治疗").setOnCheckedChangeListener(this).setSwitchId(42));
        view.addView((View)this.getCheckLayout().setText("任意不推荐  范围锁甲").setOnCheckedChangeListener(this).setSwitchId(43));
        view.addView((View)this.getCheckLayout().setText("落地   人物遁地 同附近人非同水平线角色异常").setOnCheckedChangeListener(this).setSwitchId(44));
        view.addView((View)this.getCheckLayout().setText("落地推荐   去除土地").setOnCheckedChangeListener(this).setSwitchId(45));
        view.addView((View)this.getCheckLayout().setText("落地推荐   恢复土地").setOnCheckedChangeListener(this).setSwitchId(46));
    }

    IHYXDController getHYXDController() {
        IBinder binder = this.floatTab.getClientBinder();
        HVLog.i(this.TAG, " getHYXDController binder:" + binder + "    isBinderAlive::" + binder.isBinderAlive());
        return IHYXDController.Stub.asInterface(binder);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (!isChecked) {
            return;
        }
        MemorySRWData memorySRWData = null;
        switch (buttonView.getId()) {
            case 41: {
                memorySRWData = MemorySRWData.AddMemorySearch("0.1", MemorySRWData.SearchValueType.f64);
                memorySRWData.append("-0.1", 16);
                memorySRWData.append("0.6", 32);
                memorySRWData.append("0.4", 48);
                memorySRWData.append("10", 64);
                memorySRWData.append("0.2", 80);
                memorySRWData.append("0.436", 96);
                memorySRWData.append("0.717", 112);
                memorySRWData.append("1.25", 128);
                memorySRWData.writeValue("0", "200");
                memorySRWData.writeValue("16", "200");
                this.setMemorySRWdata(memorySRWData);
                break;
            }
            case 42: {
                memorySRWData = MemorySRWData.AddMemorySearch("1.78749990463f", MemorySRWData.SearchValueType.f32);
                memorySRWData.append("2.6875f", 64);
                memorySRWData.append("2.625f", 80);
                memorySRWData.append("2.5f", 112);
                memorySRWData.append("2.375f", 144);
                memorySRWData.append("2.125f", 208);
                memorySRWData.writeValue("0x50 ", "0.1");
                memorySRWData.writeValue("0x70 ", "0.1");
                memorySRWData.writeValue("0x90", "0.1");
                memorySRWData.writeValue("0xD0 ", "0.1");
                this.setMemorySRWdata(memorySRWData);
                break;
            }
            case 43: {
                memorySRWData = MemorySRWData.AddMemorySearch("1.0f", MemorySRWData.SearchValueType.f32);
                memorySRWData.append("1.4012984643248e-45f ", 12);
                memorySRWData.append("0.10000000149012f", 40);
                memorySRWData.append(" 0", 44);
                memorySRWData.append("0 ", 48);
                memorySRWData.append("0 ", 52);
                memorySRWData.append("1.0f", 56);
                memorySRWData.writeValue("0x28 ", "5");
                this.setMemorySRWdata(memorySRWData);
                break;
            }
            case 44: {
                memorySRWData = MemorySRWData.AddMemorySearch("0.6216099858283997f", MemorySRWData.SearchValueType.f32);
                memorySRWData.append("0.7071068286895752f ", -16);
                memorySRWData.append("1.0f", -8);
                memorySRWData.append("0.009999999776482582f", 4);
                memorySRWData.writeValue("-8", "3");
                this.setMemorySRWdata(memorySRWData);
                break;
            }
            case 45: {
                memorySRWData = MemorySRWData.AddMemorySearch("1.0f", MemorySRWData.SearchValueType.f32);
                memorySRWData.append("-3200.0f ", 4);
                memorySRWData.append("-204.77432250977f", 8);
                memorySRWData.append("-3200.0f", 12);
                memorySRWData.append("1.0f", 20);
                memorySRWData.writeValue("4", "0");
                this.setMemorySRWdata(memorySRWData);
                break;
            }
            case 46: {
                memorySRWData = MemorySRWData.AddMemorySearch("1.0f", MemorySRWData.SearchValueType.f32);
                memorySRWData.setAddressPermission(true);
                memorySRWData.writeValue("0", "999888");
                this.setMemorySRWdata(memorySRWData);
                memorySRWData = MemorySRWData.AddMemorySearch("1.0f ", MemorySRWData.SearchValueType.f32);
                memorySRWData.setAddressPermission(true);
                memorySRWData.writeValue("0", "999888");
                this.setMemorySRWdata(memorySRWData);
            }
        }
    }

    public void setMemorySRWdata(MemorySRWData memorySRWData) {
        IHYXDController hyxdController = this.getHYXDController();
        try {
            hyxdController.memorySRWData(memorySRWData.getSearchValue(), memorySRWData.getWriteValue(), memorySRWData.getAddressPermission());
        }
        catch (RemoteException e) {
            e.printStackTrace();
            HVLog.printException((Exception)((Object)e));
        }
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
}

