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

public class HYXDTab3
extends TabContainer {
    String TAG = "NormalTab";
    public static final int CHECKLAYOUT_SWICH_ID_21 = 21;
    public static final int CHECKLAYOUT_SWICH_ID_22 = 22;
    public static final int CHECKLAYOUT_SWICH_ID_23 = 23;
    public static final int CHECKLAYOUT_SWICH_ID_24 = 24;
    public static final int CHECKLAYOUT_SWICH_ID_25 = 25;
    public static final int CHECKLAYOUT_SWICH_ID_26 = 26;
    public static final int CHECKLAYOUT_SWICH_ID_27 = 27;
    public static final int CHECKLAYOUT_SWICH_ID_28 = 28;
    public static final int CHECKLAYOUT_SWICH_ID_29 = 29;
    public static final int CHECKLAYOUT_SWICH_ID_30 = 30;
    public static final int CHECKLAYOUT_SWICH_ID_31 = 31;
    FloatTab floatTab;
    Button featuresMenu;

    public HYXDTab3(LayoutInflater layoutInflater, FloatBallManager floatBallManager, int tabflag) {
        super(layoutInflater, floatBallManager, tabflag);
    }

    @Override
    protected int getViewId() {
        return 0;
    }

    @Override
    protected void initViews(ViewGroup view) {
        view.addView((View)this.getCheckLayout().setText("登录推荐开  功能防封").setOnCheckedChangeListener(this).setSwitchId(21));
        view.addView((View)this.getCheckLayout().setText("游戏非推荐  范围锁头").setOnCheckedChangeListener(this).setSwitchId(22));
        view.addView((View)this.getCheckLayout().setText("小岛非推荐  水下行走").setOnCheckedChangeListener(this).setSwitchId(23));
        view.addView((View)this.getCheckLayout().setText("任意可推荐  吉普加速").setOnCheckedChangeListener(this).setSwitchId(24));
        view.addView((View)this.getCheckLayout().setText("任意可推荐  面包加速").setOnCheckedChangeListener(this).setSwitchId(25));
        view.addView((View)this.getCheckLayout().setText("游戏等修复  移动射击").setOnCheckedChangeListener(this).setSwitchId(26));
        view.addView((View)this.getCheckLayout().setText("大厅停用中  汽车锁油").setOnCheckedChangeListener(this).setSwitchId(27));
        view.addView((View)this.getCheckLayout().setText("任意可推荐  汽车穿墙").setOnCheckedChangeListener(this).setSwitchId(28));
        view.addView((View)this.getCheckLayout().setText("任意可推荐  人物定格").setOnCheckedChangeListener(this).setSwitchId(29));
        view.addView((View)this.getCheckLayout().setText("任意开启  房树全除").setOnCheckedChangeListener(this).setSwitchId(30));
        view.addView((View)this.getCheckLayout().setText("任意恢复  房树全除").setOnCheckedChangeListener(this).setSwitchId(31));
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
            case 21: {
                memorySRWData = MemorySRWData.AddMemorySearch("-0.4", MemorySRWData.SearchValueType.f64);
                memorySRWData.append("3.5", 32);
                memorySRWData.append("0.5", 48);
                memorySRWData.append("0.8", 64);
                memorySRWData.append("0.1", 80);
                memorySRWData.writeValue(" 80", "999999");
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
                break;
            }
            case 22: {
                memorySRWData = MemorySRWData.AddMemorySearch("1.4012984643248e-45f", MemorySRWData.SearchValueType.f32);
                memorySRWData.append("0.0f", 4);
                memorySRWData.append("0.0f", 8);
                memorySRWData.append("0.0f", 12);
                memorySRWData.append("0.15000000596046f", 16);
                memorySRWData.writeValue("0x10", "3.5");
                this.setMemorySRWdata(memorySRWData);
                break;
            }
            case 23: {
                memorySRWData = MemorySRWData.AddMemorySearch("0.3", MemorySRWData.SearchValueType.f64);
                memorySRWData.append("0.5", 64);
                memorySRWData.append("0.1", 112);
                memorySRWData.append("0.1", 128);
                memorySRWData.append("0.1", 144);
                memorySRWData.append("0.3", 160);
                memorySRWData.append("1.0", 176);
                memorySRWData.append("0.2", 192);
                memorySRWData.writeValue("144", "-6");
                this.setMemorySRWdata(memorySRWData);
                break;
            }
            case 24: {
                memorySRWData = MemorySRWData.AddMemorySearch("0.5", MemorySRWData.SearchValueType.f64);
                memorySRWData.append("0.8", 16);
                memorySRWData.append("1.5", 32);
                memorySRWData.append("10", 48);
                memorySRWData.append("20", 64);
                memorySRWData.append("1", 80);
                memorySRWData.append("0.9", 96);
                memorySRWData.writeValue("32", "20");
                this.setMemorySRWdata(memorySRWData);
                break;
            }
            case 25: {
                memorySRWData = MemorySRWData.AddMemorySearch("2", MemorySRWData.SearchValueType.f64);
                memorySRWData.append("60", 16);
                memorySRWData.append("25", 32);
                memorySRWData.append("100", 48);
                memorySRWData.append("13.89", 64);
                memorySRWData.append("2.01", 80);
                memorySRWData.writeValue("0", "20");
                this.setMemorySRWdata(memorySRWData);
                break;
            }
            case 26: {
                memorySRWData = MemorySRWData.AddMemorySearch("1.6", MemorySRWData.SearchValueType.f64);
                memorySRWData.append("3.14", 16);
                memorySRWData.append("1.57", 48);
                memorySRWData.append("0.3925", 64);
                memorySRWData.append("0.2", 80);
                memorySRWData.writeValue("0", "5.7");
                this.setMemorySRWdata(memorySRWData);
                break;
            }
            case 27: {
                memorySRWData = MemorySRWData.AddMemorySearch("0.0078125", MemorySRWData.SearchValueType.f64);
                memorySRWData.append("1.0f", MemorySRWData.SearchValueType.f32, 4);
                memorySRWData.append("2046", MemorySRWData.SearchValueType.f64, 8);
                memorySRWData.append("5.0f", MemorySRWData.SearchValueType.f32, 12);
                this.setMemorySRWdata(memorySRWData);
                break;
            }
            case 28: {
                memorySRWData = MemorySRWData.AddMemorySearch("-0.53147888183594", MemorySRWData.SearchValueType.f32);
                memorySRWData.append("-0.50308358669281f", 8);
                memorySRWData.append("-0.50291442871094f", 12);
                memorySRWData.append("-0.50291442871094f", 16);
                memorySRWData.append("-0.50291442871094f", 20);
                memorySRWData.append("1.0f", 24);
                memorySRWData.append("1.0f", 44);
                memorySRWData.append("1.0f", 56);
                memorySRWData.append("1.0f", 60);
                memorySRWData.append("15259.28515625f", 92);
                memorySRWData.append("419920.0f", 104);
                memorySRWData.setAddressPermission(true);
                memorySRWData.writeValue("24", "10000");
                this.setMemorySRWdata(memorySRWData);
                break;
            }
            case 29: {
                memorySRWData = MemorySRWData.AddMemorySearch("1.0", MemorySRWData.SearchValueType.f32);
                memorySRWData.append("8529.51171875f", 100);
                memorySRWData.append("10769.502929688f", 108);
                memorySRWData.append("5.5284104347229f", 116);
                memorySRWData.setAddressPermission(true);
                memorySRWData.writeValue("92", "8");
                this.setMemorySRWdata(memorySRWData);
                break;
            }
            case 30: {
                memorySRWData = MemorySRWData.AddMemorySearch("2064.0634765625f", MemorySRWData.SearchValueType.f32);
                memorySRWData.append("50000.0f", 4);
                memorySRWData.append("-50000.0f", 8);
                memorySRWData.append("-50000.0f", 12);
                memorySRWData.append("50000.0f", 16);
                memorySRWData.append("-100000.0f", 24);
                memorySRWData.append("-100000.0f", 28);
                memorySRWData.append("100000.0f", 32);
                memorySRWData.setAddressPermission(true);
                memorySRWData.writeValue("28", "0");
                this.setMemorySRWdata(memorySRWData);
                break;
            }
            case 31: {
                memorySRWData = MemorySRWData.AddMemorySearch("2064.0634765625f", MemorySRWData.SearchValueType.f32);
                memorySRWData.append("50000.0f", 4);
                memorySRWData.append("-50000.0f", 8);
                memorySRWData.append("-50000.0f", 12);
                memorySRWData.append("50000.0f", 16);
                memorySRWData.append("-100000.0f", 24);
                memorySRWData.append("0.0f", 28);
                memorySRWData.append("100000.0f", 32);
                memorySRWData.setAddressPermission(true);
                memorySRWData.writeValue("28", "-100000");
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

