/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Configuration
 *  android.graphics.Point
 *  android.os.Build$VERSION
 *  android.view.LayoutInflater
 *  android.view.WindowManager
 */
package com.carlos.science;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.WindowManager;
import com.carlos.common.utils.SPTools;
import com.carlos.libcommon.StringFog;
import com.carlos.science.FloatBallUtil;
import com.carlos.science.floatball.FloatBall;
import com.carlos.science.floatball.FloatBallCfg;
import com.carlos.science.floatball.StatusBarView;
import com.carlos.science.menu.FloatMenu;
import com.carlos.science.menu.FloatMenuCfg;
import com.carlos.science.menu.MenuItem;
import com.carlos.science.server.ServerController;
import com.carlos.science.tab.FloatTab;
import com.carlos.science.tab.TabChild;
import com.kook.common.utils.HVLog;
import java.util.ArrayList;
import java.util.List;

public class FloatBallManager {
    String TAG = "FloatBallManager";
    public int mScreenWidth;
    public int mScreenHeight;
    private OnFloatBallClickListener mFloatballClickListener;
    private WindowManager mWindowManager;
    private Context mContext;
    private FloatBall floatBall;
    private FloatMenu floatMenu;
    private FloatTab floatTab;
    private StatusBarView statusBarView;
    public int floatballX;
    public int floatballY;
    private boolean isShowing = false;
    private List<MenuItem> menuItems = new ArrayList<MenuItem>();
    private List<TabChild> tabChilds = new ArrayList<TabChild>();
    LayoutInflater mLayoutInflater;

    public FloatBallManager(Context application, ServerController serverController, FloatBallCfg ballCfg) {
        this(application, serverController, ballCfg, null);
    }

    public FloatBallManager(Context application, ServerController serverController, FloatBallCfg ballCfg, FloatMenuCfg menuCfg) {
        this.mLayoutInflater = LayoutInflater.from((Context)application);
        this.mContext = application.getApplicationContext();
        FloatBallUtil.inSingleActivity = false;
        this.mWindowManager = (WindowManager)this.mContext.getSystemService("window");
        this.computeScreenSize();
        this.floatBall = new FloatBall(this.mContext, this, ballCfg);
        this.floatMenu = new FloatMenu(this.mContext, this, menuCfg);
        this.floatTab = new FloatTab(this, this.mContext, this.mLayoutInflater, serverController, false);
        this.statusBarView = new StatusBarView(this.mContext, this);
    }

    public void buildMenu() {
        this.inflateMenuItem();
    }

    public void buildTabChild() {
        this.inflateTabChild();
    }

    public Context getContext() {
        return this.mContext;
    }

    public FloatBallManager addMenuItem(MenuItem item) {
        this.menuItems.add(item);
        return this;
    }

    public WindowManager getWindowManager() {
        return this.mWindowManager;
    }

    public FloatBallManager addTabChild(TabChild tabChild) {
        this.tabChilds.add(tabChild);
        return this;
    }

    public FloatBallManager addTabChilds(List<TabChild> tabChilds) {
        this.tabChilds.clear();
        this.tabChilds.addAll(tabChilds);
        return this;
    }

    public int getMenuItemSize() {
        return this.menuItems != null ? this.menuItems.size() : 0;
    }

    public FloatBallManager setMenu(List<MenuItem> items) {
        this.menuItems = items;
        return this;
    }

    private void inflateMenuItem() {
        this.floatMenu.removeAllItemViews();
        for (MenuItem item : this.menuItems) {
            this.floatMenu.addItem(item);
        }
    }

    private void inflateTabChild() {
        this.floatTab.removeAllItemViews();
        this.floatTab.setTabData(this.tabChilds);
    }

    public int getBallSize() {
        return this.floatBall.getSize();
    }

    public FloatBall getFloatBall() {
        return this.floatBall;
    }

    public FloatTab getFloatTab() {
        return this.floatTab;
    }

    public void computeScreenSize() {
        if (Build.VERSION.SDK_INT >= 13) {
            Point point = new Point();
            this.mWindowManager.getDefaultDisplay().getSize(point);
            this.mScreenWidth = point.x;
            this.mScreenHeight = point.y;
        } else {
            this.mScreenWidth = this.mWindowManager.getDefaultDisplay().getWidth();
            this.mScreenHeight = this.mWindowManager.getDefaultDisplay().getHeight();
        }
    }

    public int getStatusBarHeight() {
        return this.statusBarView.getStatusBarHeight();
    }

    public void onStatusBarHeightChange() {
        this.floatBall.onLayoutChange();
    }

    public void show() {
        HVLog.d(this.TAG, "show isShowing : " + this.isShowing);
        SPTools.getLong(this.getContext(), "viptime");
        if (this.isShowing) {
            return;
        }
        this.isShowing = true;
        this.floatBall.attachToWindow(this.mWindowManager);
        this.floatBall.setVisibility(0);
        this.statusBarView.attachToWindow(this.mWindowManager);
        this.floatTab.detachFromWindow(this.mWindowManager);
    }

    public boolean isShowing() {
        return this.isShowing;
    }

    public void reset() {
        HVLog.d(this.TAG, "reset");
        this.floatBall.setVisibility(0);
        this.floatBall.postSleepRunnable();
        this.floatBall.attachToWindow(this.mWindowManager);
        this.floatMenu.detachFromWindow(this.mWindowManager);
        this.floatTab.setVisibility(0);
        this.floatTab.detachFromWindow(this.mWindowManager);
    }

    public void onFloatBallClick() {
        if (this.menuItems != null && this.menuItems.size() > 0 || this.tabChilds != null && this.tabChilds.size() > 0) {
            HVLog.d(this.TAG, "FloatBallManager onFloatBallClick attachToWindow");
            if (!this.floatTab.isAdded()) {
                this.floatTab.attachToWindow(this.mWindowManager);
                this.floatBall.detachFromWindow(this.mWindowManager);
            } else {
                this.floatTab.detachFromWindow(this.mWindowManager);
                this.floatBall.attachToWindow(this.mWindowManager);
            }
        } else {
            HVLog.d(this.TAG, "FloatBallManager onFloatBallClick");
            if (this.mFloatballClickListener != null) {
                this.mFloatballClickListener.onFloatBallClick();
            }
        }
    }

    public void refreshFloatTab() {
        if (!this.floatTab.isAdded()) {
            this.floatTab.attachToWindow(this.mWindowManager);
        }
        this.floatTab.refreshToWindowsw(this.mWindowManager);
    }

    public void hide() {
        HVLog.d(this.TAG, "hide  isShowing =" + this.isShowing);
        if (!this.isShowing) {
            return;
        }
        this.isShowing = false;
        this.floatBall.detachFromWindow(this.mWindowManager);
        this.floatMenu.detachFromWindow(this.mWindowManager);
        this.floatTab.detachFromWindow(this.mWindowManager);
        this.statusBarView.detachFromWindow(this.mWindowManager);
    }

    public void onConfigurationChanged(Configuration newConfig) {
        this.computeScreenSize();
        this.reset();
    }

    public void setOnFloatBallClickListener(OnFloatBallClickListener listener) {
        this.mFloatballClickListener = listener;
    }

    public static interface OnFloatBallClickListener {
        public void onFloatBallClick();
    }
}

