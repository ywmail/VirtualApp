/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.Handler
 *  android.view.LayoutInflater
 *  android.view.View
 *  android.view.View$OnClickListener
 *  android.view.ViewGroup
 *  android.widget.CompoundButton
 *  android.widget.CompoundButton$OnCheckedChangeListener
 *  android.widget.LinearLayout
 *  android.widget.ScrollView
 *  androidx.annotation.IdRes
 *  com.kook.librelease.R$layout
 */
package com.carlos.science.tab;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import androidx.annotation.IdRes;
import com.carlos.libcommon.StringFog;
import com.carlos.science.FloatBallManager;
import com.carlos.science.server.ServerController;
import com.carlos.science.tab.CheckLayout;
import com.carlos.science.tab.FloatTab;
import com.carlos.science.tab.TabChild;
import com.kook.librelease.R;

public abstract class TabContainer
implements View.OnClickListener,
CompoundButton.OnCheckedChangeListener {
    private View view = null;
    protected LayoutInflater layoutInflater;
    Handler mHandler;
    protected ServerController serverController;
    public FloatBallManager floatBallManager;
    TabChild tabChild;
    int tabflag;
    boolean isContainer = false;

    public TabContainer(LayoutInflater layoutInflater, FloatBallManager floatBallManager, int tabflag) {
        this.layoutInflater = layoutInflater;
        this.floatBallManager = floatBallManager;
        this.tabflag = tabflag;
        this.isContainer = false;
    }

    public void initContainer() {
        if (!this.isContainer) {
            int viewId = this.getViewId();
            try {
                if (viewId > 1) {
                    this.view = this.layoutInflater.inflate(viewId, null);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
                this.view = null;
            }
            if (this.view == null) {
                ScrollView scrollView = new ScrollView(this.floatBallManager.getContext());
                LinearLayout linearLayout = new LinearLayout(this.floatBallManager.getContext());
                linearLayout.setOrientation(1);
                scrollView.addView((View)linearLayout);
                this.view = scrollView;
                this.initViews((ViewGroup)linearLayout);
            } else {
                this.findViews(this.view);
            }
            this.isContainer = true;
        }
    }

    public View findViewById(@IdRes int id2) {
        return this.view.findViewById(id2);
    }

    public View getRootView() {
        return this.view;
    }

    protected CheckLayout getCheckLayout() {
        CheckLayout checkLayout = (CheckLayout)this.layoutInflater.inflate(R.layout.float_item_check_layout, null);
        return checkLayout;
    }

    protected abstract int getViewId();

    public Context getContext() {
        return this.layoutInflater.getContext();
    }

    protected void findViews(View root) {
    }

    protected void initViews(ViewGroup view) {
    }

    public int getViewHeight() {
        return this.view.getHeight();
    }

    public int getViewWidth() {
        return this.view.getWidth();
    }

    public abstract Object getTabContainerData();

    public void onClick(View view) {
    }

    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
    }

    public abstract void setFloatTab(FloatTab var1);

    public void setHandler(Handler handler) {
        this.mHandler = handler;
    }

    protected void runOnUiThread(Runnable runnable) {
        if (this.mHandler == null) {
            throw new NullPointerException("Handler is null");
        }
        this.mHandler.post(runnable);
    }

    public void onCurrentPageSelected(FloatTab floatTab) {
    }

    public void onAttachedToWindow(FloatTab floatTab) {
    }

    public void setTabChild(TabChild tabChild) {
        this.tabChild = tabChild;
    }

    public String getClientPackageName() {
        return this.tabChild.getPackageName();
    }
}

