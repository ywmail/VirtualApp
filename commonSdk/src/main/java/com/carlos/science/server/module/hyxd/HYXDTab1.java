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

public class HYXDTab1
extends TabContainer {
    public static final int CHECKLAYOUT_SWICH_ID_0 = 0;
    public static final int CHECKLAYOUT_SWICH_ID_1 = 1;
    public static final int CHECKLAYOUT_SWICH_ID_2 = 2;
    public static final int CHECKLAYOUT_SWICH_ID_3 = 3;
    public static final int CHECKLAYOUT_SWICH_ID_4 = 4;
    public static final int CHECKLAYOUT_SWICH_ID_5 = 5;
    public static final int CHECKLAYOUT_SWICH_ID_6 = 6;
    public static final int CHECKLAYOUT_SWICH_ID_7 = 7;
    FloatTab floatTab;
    Button featuresMenu;
    String TAG = "HYXDTab1";
    public boolean isInit = false;

    public HYXDTab1(LayoutInflater layoutInflater, FloatBallManager floatBallManager, int tabflag) {
        super(layoutInflater, floatBallManager, tabflag);
    }

    @Override
    protected int getViewId() {
        return 0;
    }

    @Override
    protected void initViews(ViewGroup view) {
        view.addView((View)this.getCheckLayout().setText("先开必开   大厅防封").setOnCheckedChangeListener(this).setSwitchId(1));
        view.addView((View)this.getCheckLayout().setText("大厅推荐   子弹全穿").setOnCheckedChangeListener(this).setSwitchId(2));
        view.addView((View)this.getCheckLayout().setText("大厅推荐   人物穿墙").setOnCheckedChangeListener(this).setSwitchId(3));
        view.addView((View)this.getCheckLayout().setText("大厅推荐   聚点无后").setOnCheckedChangeListener(this).setSwitchId(4));
        view.addView((View)this.getCheckLayout().setText("落地推荐   人物天线").setOnCheckedChangeListener(this).setSwitchId(5));
        view.addView((View)this.getCheckLayout().setText("小岛推荐   全图除草").setOnCheckedChangeListener(this).setSwitchId(6));
        view.addView((View)this.getCheckLayout().setText("必配防封   完美无后").setOnCheckedChangeListener(this).setSwitchId(7));
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
            case 1: {
                memorySRWData = MemorySRWData.AddMemorySearch("1.1", MemorySRWData.SearchValueType.f64);
                memorySRWData.append("0.8", -32);
                memorySRWData.append("0.1", -16);
                memorySRWData.writeValue("-16", "999999");
                this.setMemorySRWdata(memorySRWData);
                memorySRWData = MemorySRWData.AddMemorySearch("12", MemorySRWData.SearchValueType.f64);
                memorySRWData.append("0.2", -112);
                memorySRWData.append("1", -96);
                memorySRWData.append("0.1", -64);
                memorySRWData.append("0.01", -16);
                memorySRWData.append("4", 16);
                memorySRWData.writeValue("-64", "2");
                this.setMemorySRWdata(memorySRWData);
                memorySRWData = MemorySRWData.AddMemorySearch("-0.4", MemorySRWData.SearchValueType.f64);
                memorySRWData.append("3.5", 32);
                memorySRWData.append("0.5", 48);
                memorySRWData.append("0.8", 64);
                memorySRWData.append("0.1", 80);
                memorySRWData.writeValue("80", "999999");
                this.setMemorySRWdata(memorySRWData);
                memorySRWData = MemorySRWData.AddMemorySearch("0.1", MemorySRWData.SearchValueType.f64);
                memorySRWData.append("0.1", 48);
                memorySRWData.append("0.1", 64);
                memorySRWData.append("0.1", 80);
                memorySRWData.append("0.1", 112);
                memorySRWData.append("0.1", 128);
                memorySRWData.append("1126.4", 160);
                memorySRWData.append("0.1", 192);
                memorySRWData.append("0.1", 288);
                memorySRWData.append("0.1", 352);
                memorySRWData.append("0.1", 384);
                memorySRWData.append("0.1", 416);
                memorySRWData.append("0.1", 464);
                memorySRWData.append("0.1", 480);
                memorySRWData.writeValue("0", "999999");
                memorySRWData.writeValue("48", "999999");
                memorySRWData.writeValue("64", "999999");
                memorySRWData.writeValue("80", "999999");
                memorySRWData.writeValue("112", "999999");
                memorySRWData.writeValue("128", "999999");
                memorySRWData.writeValue("160", "999999");
                memorySRWData.writeValue("192", "999999");
                memorySRWData.writeValue("288", "999999");
                memorySRWData.writeValue("352", "999999");
                memorySRWData.writeValue("384", "999999");
                memorySRWData.writeValue("416", "999999");
                memorySRWData.writeValue("464", "999999");
                memorySRWData.writeValue("480", "999999");
                this.setMemorySRWdata(memorySRWData);
                memorySRWData = MemorySRWData.AddMemorySearch("0.23", MemorySRWData.SearchValueType.f64);
                memorySRWData.append("-1.54", 16);
                memorySRWData.writeValue("0", "-0.23");
                this.setMemorySRWdata(memorySRWData);
                break;
            }
            case 2: {
                memorySRWData = MemorySRWData.AddMemorySearch("16842752", MemorySRWData.SearchValueType.i32);
                memorySRWData.append("16842753", 28);
                memorySRWData.append("16777217", MemorySRWData.SearchValueType.i32, 48);
                memorySRWData.append("16777473", 72);
                memorySRWData.writeValue("48", "0");
                this.setMemorySRWdata(memorySRWData);
                break;
            }
            case 3: {
                memorySRWData = MemorySRWData.AddMemorySearch("0.003921584226191f", MemorySRWData.SearchValueType.f32);
                memorySRWData.append("10000.0f", 16);
                memorySRWData.append("10000.0f", 20);
                memorySRWData.append("10000.0f", 24);
                memorySRWData.append("0.5f", 32);
                memorySRWData.writeValue("0", "80");
                memorySRWData.writeValue("32", "80");
                this.setMemorySRWdata(memorySRWData);
                break;
            }
            case 4: {
                memorySRWData = MemorySRWData.AddMemorySearch("0.7", MemorySRWData.SearchValueType.f64);
                memorySRWData.append("0.05", 24);
                memorySRWData.append("100", 48);
                memorySRWData.append("1", 96);
                memorySRWData.append("-1", 120);
                memorySRWData.writeValue("0x40", "0");
                this.setMemorySRWdata(memorySRWData);
                memorySRWData = MemorySRWData.AddMemorySearch("0.1", MemorySRWData.SearchValueType.f64);
                memorySRWData.append("4.9e-324", 12);
                memorySRWData.append("0.01", 48);
                memorySRWData.append("12", 64);
                memorySRWData.writeValue("0x0", "1");
                this.setMemorySRWdata(memorySRWData);
                break;
            }
            case 5: {
                memorySRWData = MemorySRWData.AddMemorySearch("0.99886602163f", MemorySRWData.SearchValueType.f32);
                memorySRWData.append("-0.04760599881f", 8);
                memorySRWData.append("0.04760599881f", 32);
                memorySRWData.append("0.99886602163f", 40);
                memorySRWData.append("1.0f", 20);
                memorySRWData.append("-0.02063599974f", 48);
                memorySRWData.append("0.00770800002f", 56);
                memorySRWData.append("1.0f", 60);
                memorySRWData.writeValue("16", "999888");
                memorySRWData.writeValue("60", "999888");
                this.setMemorySRWdata(memorySRWData);
                break;
            }
            case 6: {
                memorySRWData = MemorySRWData.AddMemorySearch("5126", MemorySRWData.SearchValueType.i32);
                memorySRWData.append("3", 4);
                memorySRWData.append("10", MemorySRWData.SearchValueType.i32, 8);
                memorySRWData.append("16", 12);
                memorySRWData.append("4", 16);
                memorySRWData.append("80", 20);
                memorySRWData.append("1", MemorySRWData.SearchValueType.i32, 24);
                memorySRWData.append("10", 28);
                memorySRWData.append("11", 40);
                memorySRWData.append("32", 44);
                memorySRWData.writeValue("28", "0");
                this.setMemorySRWdata(memorySRWData);
                break;
            }
            case 7: {
                memorySRWData = MemorySRWData.AddMemorySearch("10", MemorySRWData.SearchValueType.f64);
                memorySRWData.append("0.8", -24);
                memorySRWData.append("4.18359375", MemorySRWData.SearchValueType.f32, 28);
                memorySRWData.writeValue("28", "999");
                this.setMemorySRWdata(memorySRWData);
                break;
            }
            case 0: {
                memorySRWData = MemorySRWData.AddMemorySearch("0.99886602163f", MemorySRWData.SearchValueType.f32);
                memorySRWData.append("-0.04760599881f", 8);
                memorySRWData.append("0.04760599881f", 32);
                memorySRWData.append("0.99886602163f", 40);
                memorySRWData.append("1.0f", 20);
                memorySRWData.append("-0.02063599974f", 48);
                memorySRWData.append("0.00770800002f", 56);
                memorySRWData.append("1.0f", 60);
                memorySRWData.writeValue("16", "999888");
                memorySRWData.writeValue("60", "999888");
                this.setMemorySRWdata(memorySRWData);
            }
        }
    }

    public void setMemorySRWdata(MemorySRWData memorySRWData) {
        IHYXDController hyxdController = this.getHYXDController();
        try {
            if (memorySRWData != null) {
                hyxdController.memorySRWData(memorySRWData.getSearchValue(), memorySRWData.getWriteValue(), memorySRWData.getAddressPermission());
            } else {
                hyxdController.memoryTest();
            }
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

