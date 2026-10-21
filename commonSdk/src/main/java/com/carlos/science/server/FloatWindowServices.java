/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.NotificationChannel
 *  android.app.NotificationManager
 *  android.content.BroadcastReceiver
 *  android.content.Context
 *  android.content.Intent
 *  android.content.IntentFilter
 *  android.content.ServiceConnection
 *  android.graphics.drawable.Drawable
 *  android.os.Build$VERSION
 *  android.os.IBinder
 *  android.view.LayoutInflater
 *  androidx.annotation.RequiresApi
 *  org.jdeferred.Promise
 */
package com.carlos.science.server;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.IBinder;
import android.view.LayoutInflater;
import androidx.annotation.RequiresApi;
import com.carlos.common.utils.ResponseProgram;
import com.carlos.libcommon.StringFog;
import com.carlos.science.FloatBallManager;
import com.carlos.science.floatball.FloatBallCfg;
import com.carlos.science.menu.FloatMenuCfg;
import com.carlos.science.server.EventService;
import com.carlos.science.server.ServerController;
import com.carlos.science.tab.FloatTab;
import com.carlos.science.tab.TabChild;
import com.carlos.science.tab.TabContainerFactory;
import com.carlos.science.utils.BackGroudSeletor;
import com.carlos.science.utils.DensityUtil;
import com.kook.common.utils.HVLog;
import java.util.List;
import org.jdeferred.Promise;

public class FloatWindowServices
extends EventService {
    public static final String TAG = "FloatWindowServices";
    ServerController serverController;
    private FloatBallManager mFloatballManager;
    LayoutInflater layoutInflater;
    private final BroadcastReceiver homeListenerReceiver = new BroadcastReceiver(){
        final String SYSTEM_DIALOG_REASON_KEY = "reason";
        final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            HVLog.e("FloatWindowServices", "action:" + action);
            FloatWindowServices.this.hide();
        }
    };

    @Override
    @RequiresApi(api=16)
    public void onCreate() {
        super.onCreate();
        HVLog.i(TAG, "悬浮窗 初始化");
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationManager notificationManager = (NotificationManager)this.getSystemService("notification");
            NotificationChannel channel = new NotificationChannel(TAG, (CharSequence)TAG, 3);
            notificationManager.createNotificationChannel(channel);
        }
        this.layoutInflater = LayoutInflater.from((Context)this);
        this.serverController = new ServerController(this);
        this.init();
        TabContainerFactory.getInstance().initTag(this.layoutInflater, this.mFloatballManager);
        IntentFilter homeFilter = new IntentFilter("android.intent.action.CLOSE_SYSTEM_DIALOGS");
        homeFilter.addAction("com.kook.action");
        this.registerReceiver(this.homeListenerReceiver, homeFilter);
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        boolean closeFloat = false;
        boolean showFloat = false;
        if (intent != null) {
            closeFloat = intent.getBooleanExtra("closeFloat", false);
            showFloat = intent.getBooleanExtra("showFloat", false);
        }
        HVLog.d(TAG, "onStartCommand " + this.getPackageName() + "  closeFloat : " + closeFloat + "    showFloat:" + showFloat);
        if (closeFloat) {
            this.hide();
        }
        if (showFloat) {
            // empty if block
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void init() {
        int ballSize = DensityUtil.dip2px((Context)this, 45.0f);
        Drawable ballIcon = BackGroudSeletor.getdrawble("ic_floatball", (Context)this);
        HVLog.d(TAG, "当前圆形菜单大小 ballSize：" + ballSize + "    " + ballIcon);
        FloatBallCfg ballCfg = new FloatBallCfg(ballSize, ballIcon, FloatBallCfg.Gravity.RIGHT_CENTER);
        ballCfg.setHideHalfLater(false);
        int menuSize = DensityUtil.dip2px((Context)this, 180.0f);
        int menuItemSize = DensityUtil.dip2px((Context)this, 40.0f);
        FloatMenuCfg menuCfg = new FloatMenuCfg(menuSize, menuItemSize);
        this.mFloatballManager = new FloatBallManager(this.getApplicationContext(), this.serverController, ballCfg, menuCfg);
    }

    public void show(String packageName, IBinder binder) {
        try {
            List<TabChild> tabChildren = TabContainerFactory.getInstance().getTabChildListByPackageName(packageName);
            HVLog.i(TAG, "FloatWindowServices  show; binder:" + binder + "    packageName:" + packageName);
            if (tabChildren == null) {
                HVLog.i(TAG, "应用:" + packageName + "未找到需要启动的菜单");
                return;
            }
            this.mFloatballManager.addTabChilds(tabChildren).buildTabChild();
            if (!this.mFloatballManager.isShowing()) {
                FloatTab floatTab = this.mFloatballManager.getFloatTab();
                floatTab.setClientBinder(binder);
                floatTab.setPackageName(packageName);
                floatTab.setFocusableInTouchMode(true);
                this.mFloatballManager.show();
            }
        }
        catch (Exception e) {
            HVLog.printException(TAG, e);
        }
    }

    public void hide() {
        try {
            HVLog.i(TAG, "FloatWindowServices   hide");
            if (this.mFloatballManager.isShowing()) {
                this.mFloatballManager.hide();
            }
        }
        catch (Exception e) {
            HVLog.e(TAG, "FloatWindowServices show exception:" + e.toString());
        }
    }

    public Promise<Void, Throwable, Void> hideControllerContainer() {
        HVLog.i(TAG, "FloatWindowServices   hideControllerContainer");
        this.mFloatballManager.reset();
        return ResponseProgram.defer().when(() -> {
            while (this.mFloatballManager.getFloatTab().isAdded()) {
                this.sleep(100L);
            }
            HVLog.i(TAG, "FloatWindowServices   hideControllerContainer end");
        });
    }

    public Promise<Void, Throwable, Void> showControllerContainer() {
        HVLog.i(TAG, "FloatWindowServices   hideControllerContainer");
        return ResponseProgram.defer().when(() -> {
            while (!this.mFloatballManager.getFloatTab().isAdded()) {
                this.sleep(100L);
            }
            HVLog.i(TAG, "FloatWindowServices   hideControllerContainer end");
        });
    }

    public void setPosition(int position) {
        this.getFloatTab().setPosition(position);
    }

    public FloatTab getFloatTab() {
        return null;
    }

    public IBinder onBind(Intent intent) {
        HVLog.e(TAG, "onBind intent:" + intent.toString());
        return this.serverController.asBinder();
    }

    public void unbindService(ServiceConnection conn) {
        super.unbindService(conn);
        this.mFloatballManager.hide();
        HVLog.e(TAG, "===================unbindService  conn:" + conn);
        this.unregisterReceiver(this.homeListenerReceiver);
    }

    public void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(this.homeListenerReceiver);
    }
}

