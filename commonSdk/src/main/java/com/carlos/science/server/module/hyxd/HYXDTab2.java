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

public class HYXDTab2
extends TabContainer {
    public static final int CHECKLAYOUT_SWICH_ID_11 = 11;
    public static final int CHECKLAYOUT_SWICH_ID_12 = 12;
    public static final int CHECKLAYOUT_SWICH_ID_13 = 13;
    public static final int CHECKLAYOUT_SWICH_ID_14 = 14;
    public static final int CHECKLAYOUT_SWICH_ID_15 = 15;
    public static final int CHECKLAYOUT_SWICH_ID_16 = 16;
    public static final int CHECKLAYOUT_SWICH_ID_17 = 17;
    public static final int CHECKLAYOUT_SWICH_ID_18 = 18;
    public static final int CHECKLAYOUT_SWICH_ID_19 = 19;
    String TAG = "NormalTab";
    FloatTab floatTab;
    Button featuresMenu;

    public HYXDTab2(LayoutInflater layoutInflater, FloatBallManager floatBallManager, int tabflag) {
        super(layoutInflater, floatBallManager, tabflag);
    }

    @Override
    protected int getViewId() {
        return 0;
    }

    @Override
    protected void initViews(ViewGroup view) {
        view.addView((View)this.getCheckLayout().setText("靶场/出生岛  深红835版").setOnCheckedChangeListener(this).setSwitchId(11));
        view.addView((View)this.getCheckLayout().setText("靶场/出生岛  透视835版").setOnCheckedChangeListener(this).setSwitchId(12));
        view.addView((View)this.getCheckLayout().setText("靶场/出生岛  红色845版").setOnCheckedChangeListener(this).setSwitchId(13));
        view.addView((View)this.getCheckLayout().setText("靶场/出生岛  透视845版").setOnCheckedChangeListener(this).setSwitchId(14));
        view.addView((View)this.getCheckLayout().setText("靶场/出生岛  透视855版").setOnCheckedChangeListener(this).setSwitchId(15));
        view.addView((View)this.getCheckLayout().setText("落地前断网  激战原野全机型-红色").setOnCheckedChangeListener(this).setSwitchId(16));
        view.addView((View)this.getCheckLayout().setText("落地前断网  飓风半岛全机型-红色").setOnCheckedChangeListener(this).setSwitchId(17));
        view.addView((View)this.getCheckLayout().setText("雷达非推荐   脚步显示危险").setOnCheckedChangeListener(this).setSwitchId(18));
        view.addView((View)this.getCheckLayout().setText("雷达非推荐  对手语音危险").setOnCheckedChangeListener(this).setSwitchId(19));
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
            case 11: {
                memorySRWData = MemorySRWData.AddMemorySearch("5.5625042915344f", MemorySRWData.SearchValueType.f32);
                memorySRWData.append("2.2509768009186f", 184);
                memorySRWData.append("3.75f", 216);
                memorySRWData.append("3.75f", 264);
                memorySRWData.append("0.9990000128746f", 456);
                memorySRWData.setAddressPermission(true);
                memorySRWData.writeValue("456", "80");
                this.setMemorySRWdata(memorySRWData);
                memorySRWData = MemorySRWData.AddMemorySearch("5.5625042915344f", MemorySRWData.SearchValueType.f32);
                memorySRWData.append("3.7500011920929f", 16);
                memorySRWData.append("3.75f", 216);
                memorySRWData.append("3.75f", 264);
                memorySRWData.append("0.9990000128746f", 456);
                memorySRWData.setAddressPermission(true);
                memorySRWData.writeValue("456", "80");
                this.setMemorySRWdata(memorySRWData);
                break;
            }
            case 12: {
                memorySRWData = MemorySRWData.AddMemorySearch("3.7504887580872f", MemorySRWData.SearchValueType.f32);
                memorySRWData.append("2.25f", 224);
                memorySRWData.append("2.0f", 444);
                memorySRWData.setAddressPermission(true);
                memorySRWData.writeValue("444", "80");
                this.setMemorySRWdata(memorySRWData);
                memorySRWData = MemorySRWData.AddMemorySearch("5.5625042915344f", MemorySRWData.SearchValueType.f32);
                memorySRWData.append("3.75f", 216);
                memorySRWData.append("2.0f", 444);
                memorySRWData.setAddressPermission(true);
                memorySRWData.writeValue("444", "80");
                this.setMemorySRWdata(memorySRWData);
                memorySRWData = MemorySRWData.AddMemorySearch("3.7504887580872f", MemorySRWData.SearchValueType.f32);
                memorySRWData.append("3.7504894733429f", 200);
                memorySRWData.append("2.25f", 224);
                memorySRWData.append("2.0f", 444);
                memorySRWData.setAddressPermission(true);
                memorySRWData.writeValue("444", "80");
                this.setMemorySRWdata(memorySRWData);
                memorySRWData = MemorySRWData.AddMemorySearch("5.5625042915344f", MemorySRWData.SearchValueType.f32);
                memorySRWData.append("3.75f", 216);
                memorySRWData.append("3.75f", 264);
                memorySRWData.append("3.875f", 196);
                memorySRWData.append("2.0f", 444);
                memorySRWData.writeValue("444", "80");
                this.setMemorySRWdata(memorySRWData);
                break;
            }
            case 13: {
                memorySRWData = MemorySRWData.AddMemorySearch("5.5625042915344f", MemorySRWData.SearchValueType.f32);
                memorySRWData.append("3.7500016689301f", 16);
                memorySRWData.append("2.2509768009186f", 184);
                memorySRWData.append("773094637568.0f", 188);
                memorySRWData.append("3.8755548000336f", 200);
                memorySRWData.append("3.7500059604645f", 208);
                memorySRWData.append("3.75f", 216);
                memorySRWData.append("2.015625f", 252);
                memorySRWData.append("3.75f", 264);
                memorySRWData.append("3.875f", 296);
                memorySRWData.append("0.9990000128746f", 464);
                memorySRWData.writeValue("464", "80");
                this.setMemorySRWdata(memorySRWData);
                memorySRWData = MemorySRWData.AddMemorySearch("5.5625057220459f", MemorySRWData.SearchValueType.f32);
                memorySRWData.append("3.75f", 224);
                memorySRWData.append("3.2500002384186f", 232);
                memorySRWData.append("2.015625f", 260);
                memorySRWData.append("3.75f", 272);
                memorySRWData.append("3.875f", 304);
                memorySRWData.append("80.0f", 452);
                memorySRWData.append("-1.0f", 456);
                memorySRWData.append("0.9990000128746f", 464);
                memorySRWData.writeValue("464", "80");
                this.setMemorySRWdata(memorySRWData);
                break;
            }
            case 14: {
                memorySRWData = MemorySRWData.AddMemorySearch("3.7500019073486f", MemorySRWData.SearchValueType.f32);
                memorySRWData.append("5.5625042915344f", 8);
                memorySRWData.append("4.9068384272064e+21f", 16);
                memorySRWData.append("3.75f", 224);
                memorySRWData.append("2.015625f", 260);
                memorySRWData.append("3.75f", 272);
                memorySRWData.append("3.875f", 304);
                memorySRWData.append("2.0f", 460);
                memorySRWData.append("-1.0f", 464);
                memorySRWData.append("0.9990000128746f", 472);
                memorySRWData.writeValue("460", "80");
                this.setMemorySRWdata(memorySRWData);
                memorySRWData = MemorySRWData.AddMemorySearch("11266.75f", MemorySRWData.SearchValueType.f32);
                memorySRWData.append("24581.5f", 16);
                memorySRWData.append("2.0f", 36);
                memorySRWData.append("3.8750669956207f", 152);
                memorySRWData.append("3.8750674724579f", 456);
                memorySRWData.append("3.7500042915344f", 632);
                memorySRWData.writeValue("36", "80");
                this.setMemorySRWdata(memorySRWData);
                break;
            }
            case 15: {
                memorySRWData = MemorySRWData.AddMemorySearch("781684768768.0f", MemorySRWData.SearchValueType.f32);
                memorySRWData.append("3.75f", 236);
                memorySRWData.append("2.0f", 432);
                memorySRWData.append("-1.0f ", 436);
                memorySRWData.append("0.9990000128746f ", 444);
                memorySRWData.writeValue("0x1b0", "80");
                this.setMemorySRWdata(memorySRWData);
                memorySRWData = MemorySRWData.AddMemorySearch("11266.75f", MemorySRWData.SearchValueType.f32);
                memorySRWData.append("24581.5f", 16);
                memorySRWData.append("2.0f ", 36);
                memorySRWData.append("3.8750674724579f ", 456);
                memorySRWData.append("3.7500042915344f ", 632);
                memorySRWData.append("3.7500042915344f ", 760);
                memorySRWData.append("2.2500007152557f ", 968);
                memorySRWData.writeValue("0x24", "80");
                this.setMemorySRWdata(memorySRWData);
                memorySRWData = MemorySRWData.AddMemorySearch("4.0f", MemorySRWData.SearchValueType.f32);
                memorySRWData.append("11266.75f", 12);
                memorySRWData.append("24581.5f", 28);
                memorySRWData.append("2.0f", 48);
                memorySRWData.writeValue("0x30", "80");
                this.setMemorySRWdata(memorySRWData);
                break;
            }
            case 16: {
                memorySRWData = MemorySRWData.AddMemorySearch("0.5f", MemorySRWData.SearchValueType.f32);
                memorySRWData.append("0.75f", 4);
                memorySRWData.append("1.0f", 8);
                memorySRWData.append("1.0f", 12);
                memorySRWData.append("1.0f", 16);
                memorySRWData.append("1.0f", 20);
                memorySRWData.append("0.5f", 24);
                memorySRWData.append("1.2000000476837f", 52);
                memorySRWData.writeValue("12", "520");
                this.setMemorySRWdata(memorySRWData);
                break;
            }
            case 17: {
                memorySRWData = MemorySRWData.AddMemorySearch("1.0f", MemorySRWData.SearchValueType.f32);
                memorySRWData.append("1.0f", 4);
                memorySRWData.append("1.0f", 8);
                memorySRWData.append("1.0f", 12);
                memorySRWData.append("1.0f", 16);
                memorySRWData.append("0.8524374961853f", 20);
                memorySRWData.append("1.3999999761581f", 24);
                memorySRWData.append("1.1799999475479f", 52);
                memorySRWData.append("20.0f", 80);
                memorySRWData.writeValue("16", "88");
                memorySRWData.writeValue("20", "88");
                memorySRWData.writeValue("24", "88");
                this.setMemorySRWdata(memorySRWData);
                break;
            }
            case 18: {
                memorySRWData = MemorySRWData.AddMemorySearch("33", MemorySRWData.SearchValueType.f64);
                memorySRWData.append("18", 48);
                memorySRWData.append("15", 64);
                memorySRWData.append("220", 80);
                memorySRWData.append("30", 96);
                memorySRWData.writeValue("96 ", "1200");
                this.setMemorySRWdata(memorySRWData);
                break;
            }
            case 19: {
                memorySRWData = MemorySRWData.AddMemorySearch("2", MemorySRWData.SearchValueType.f64);
                memorySRWData.append("5", 16);
                memorySRWData.append("20", 32);
                memorySRWData.append("100", 48);
                memorySRWData.append("10", 64);
                memorySRWData.writeValue("44", "1200");
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

