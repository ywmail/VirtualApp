/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.content.Context
 *  android.os.Build$VERSION
 *  android.util.Log
 *  android.view.KeyEvent
 *  android.view.MotionEvent
 *  android.view.View
 *  android.view.View$OnClickListener
 *  android.view.View$OnKeyListener
 *  android.view.ViewGroup$LayoutParams
 *  android.view.ViewTreeObserver$OnGlobalLayoutListener
 *  android.view.WindowManager
 *  android.view.WindowManager$LayoutParams
 *  android.widget.FrameLayout
 *  android.widget.FrameLayout$LayoutParams
 *  android.widget.ImageView
 */
package com.carlos.science.menu;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.carlos.libcommon.StringFog;
import com.carlos.science.FloatBallManager;
import com.carlos.science.FloatBallUtil;
import com.carlos.science.menu.FloatMenuCfg;
import com.carlos.science.menu.MenuItem;
import com.carlos.science.menu.MenuLayout;

public class FloatMenu
extends FrameLayout {
    String TAG = "FloatMenu";
    private MenuLayout mMenuLayout;
    private ImageView mIconView;
    private int mPosition;
    private int mItemSize;
    private int mSize;
    private int mDuration = 250;
    private FloatBallManager floatBallManager;
    private WindowManager.LayoutParams mLayoutParams;
    private boolean isAdded = false;
    private int mBallSize;
    private FloatMenuCfg mConfig;
    private boolean mListenBackEvent = true;
    public static final int LEFT_TOP = 1;
    public static final int CENTER_TOP = 2;
    public static final int RIGHT_TOP = 3;
    public static final int LEFT_CENTER = 4;
    public static final int CENTER = 5;
    public static final int RIGHT_CENTER = 6;
    public static final int LEFT_BOTTOM = 7;
    public static final int CENTER_BOTTOM = 8;
    public static final int RIGHT_BOTTOM = 9;

    public FloatMenu(Context context, FloatBallManager floatBallManager, FloatMenuCfg config) {
        super(context);
        this.floatBallManager = floatBallManager;
        if (config == null) {
            return;
        }
        this.mConfig = config;
        this.mItemSize = this.mConfig.mItemSize;
        this.mSize = this.mConfig.mSize;
        this.init(context);
        this.mMenuLayout.setChildSize(this.mItemSize);
    }

    private void initLayoutParams(Context context) {
        this.mLayoutParams = FloatBallUtil.getLayoutParams(context, this.mListenBackEvent);
    }

    public void removeViewTreeObserver(ViewTreeObserver.OnGlobalLayoutListener listener) {
        if (Build.VERSION.SDK_INT < 16) {
            this.getViewTreeObserver().removeGlobalOnLayoutListener(listener);
        } else {
            this.getViewTreeObserver().removeOnGlobalLayoutListener(listener);
        }
    }

    public int getSize() {
        return this.mSize;
    }

    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        int x = (int)event.getRawX();
        int y = (int)event.getRawY();
        switch (action) {
            case 0: {
                break;
            }
            case 2: {
                break;
            }
            case 1: 
            case 3: 
            case 4: {
                if (!this.mMenuLayout.isExpanded()) break;
                this.toggle(this.mDuration);
            }
        }
        return super.onTouchEvent(event);
    }

    public void attachToWindow(WindowManager windowManager) {
        if (!this.isAdded) {
            this.mBallSize = this.floatBallManager.getBallSize();
            this.mLayoutParams.x = this.floatBallManager.floatballX;
            this.mLayoutParams.y = this.floatBallManager.floatballY - this.mSize / 2;
            this.mPosition = this.computeMenuLayout(this.mLayoutParams);
            this.refreshPathMenu(this.mPosition);
            this.toggle(this.mDuration);
            windowManager.addView((View)this, (ViewGroup.LayoutParams)this.mLayoutParams);
            this.isAdded = true;
        }
    }

    public void detachFromWindow(WindowManager windowManager) {
        Log.d((String)this.TAG, (String)"detachFromWindow");
        if (this.isAdded) {
            this.toggle(0);
            this.mMenuLayout.setVisibility(8);
            if (this.getContext() instanceof Activity) {
                windowManager.removeViewImmediate((View)this);
            } else {
                windowManager.removeView((View)this);
            }
            this.isAdded = false;
        }
    }

    private void addMenuLayout(Context context) {
        this.mMenuLayout = new MenuLayout(context);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(this.mSize, this.mSize);
        this.addView((View)this.mMenuLayout, layoutParams);
        this.mMenuLayout.setVisibility(4);
    }

    private void addControllLayout(Context context) {
        this.mIconView = new ImageView(context);
        FrameLayout.LayoutParams sublayoutParams = new FrameLayout.LayoutParams(this.mBallSize, this.mBallSize);
        this.addView((View)this.mIconView, (ViewGroup.LayoutParams)sublayoutParams);
    }

    private void init(Context context) {
        this.initLayoutParams(context);
        this.mLayoutParams.height = this.mSize;
        this.mLayoutParams.width = this.mSize;
        this.addMenuLayout(context);
        this.addControllLayout(context);
        this.mIconView.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v) {
                FloatMenu.this.closeMenu();
            }
        });
        if (this.mListenBackEvent) {
            this.setOnKeyListener(new View.OnKeyListener(){

                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    int action = event.getAction();
                    return action == 0 && keyCode == 4;
                }
            });
            this.setFocusableInTouchMode(true);
        }
    }

    public void closeMenu() {
        if (this.mMenuLayout.isExpanded()) {
            this.toggle(this.mDuration);
        }
    }

    public void remove() {
        this.floatBallManager.reset();
        this.mMenuLayout.setExpand(false);
    }

    private void toggle(final int duration) {
        if (!this.mMenuLayout.isExpanded() && duration <= 0) {
            return;
        }
        this.mMenuLayout.setVisibility(0);
        if (this.getWidth() == 0) {
            this.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener(){

                public void onGlobalLayout() {
                    FloatMenu.this.mMenuLayout.switchState(FloatMenu.this.mPosition, duration);
                    FloatMenu.this.removeViewTreeObserver(this);
                }
            });
        } else {
            this.mMenuLayout.switchState(this.mPosition, duration);
        }
    }

    public boolean isMoving() {
        return this.mMenuLayout.isMoving();
    }

    public void addItem(final MenuItem menuItem) {
        if (this.mConfig == null) {
            return;
        }
        ImageView imageview = new ImageView(this.getContext());
        imageview.setBackgroundDrawable(menuItem.mDrawable);
        this.mMenuLayout.addView((View)imageview);
        imageview.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v) {
                if (!FloatMenu.this.mMenuLayout.isMoving()) {
                    menuItem.action();
                }
            }
        });
    }

    public void removeAllItemViews() {
        this.mMenuLayout.removeAllViews();
    }

    public void refreshPathMenu(int position) {
        FrameLayout.LayoutParams menuLp = (FrameLayout.LayoutParams)this.mMenuLayout.getLayoutParams();
        FrameLayout.LayoutParams iconLp = (FrameLayout.LayoutParams)this.mIconView.getLayoutParams();
        switch (position) {
            case 1: {
                iconLp.gravity = 51;
                menuLp.gravity = 51;
                this.mMenuLayout.setArc(0.0f, 90.0f, position);
                break;
            }
            case 4: {
                iconLp.gravity = 19;
                menuLp.gravity = 19;
                this.mMenuLayout.setArc(270.0f, 450.0f, position);
                break;
            }
            case 7: {
                iconLp.gravity = 83;
                menuLp.gravity = 83;
                this.mMenuLayout.setArc(270.0f, 360.0f, position);
                break;
            }
            case 3: {
                iconLp.gravity = 53;
                menuLp.gravity = 53;
                this.mMenuLayout.setArc(90.0f, 180.0f, position);
                break;
            }
            case 6: {
                iconLp.gravity = 21;
                menuLp.gravity = 21;
                this.mMenuLayout.setArc(90.0f, 270.0f, position);
                break;
            }
            case 9: {
                iconLp.gravity = 85;
                menuLp.gravity = 85;
                this.mMenuLayout.setArc(180.0f, 270.0f, position);
                break;
            }
            case 2: {
                iconLp.gravity = 49;
                menuLp.gravity = 49;
                this.mMenuLayout.setArc(0.0f, 180.0f, position);
                break;
            }
            case 8: {
                iconLp.gravity = 81;
                menuLp.gravity = 81;
                this.mMenuLayout.setArc(180.0f, 360.0f, position);
                break;
            }
            case 5: {
                iconLp.gravity = 17;
                menuLp.gravity = 17;
                this.mMenuLayout.setArc(0.0f, 360.0f, position);
            }
        }
        this.mIconView.setLayoutParams((ViewGroup.LayoutParams)iconLp);
        this.mMenuLayout.setLayoutParams((ViewGroup.LayoutParams)menuLp);
    }

    public int computeMenuLayout(WindowManager.LayoutParams layoutParams) {
        int position = 6;
        int halfBallSize = this.mBallSize / 2;
        int screenWidth = this.floatBallManager.mScreenWidth;
        int screenHeight = this.floatBallManager.mScreenHeight;
        int floatballCenterY = this.floatBallManager.floatballY + halfBallSize;
        int wmX = this.floatBallManager.floatballX;
        int wmY = floatballCenterY;
        if (wmX <= screenWidth / 3) {
            wmX = 0;
            if (wmY <= this.mSize / 2) {
                position = 1;
                wmY = floatballCenterY - halfBallSize;
            } else if (wmY > screenHeight - this.mSize / 2) {
                position = 7;
                wmY = floatballCenterY - this.mSize + halfBallSize;
            } else {
                position = 4;
                wmY = floatballCenterY - this.mSize / 2;
            }
        } else if (wmX >= screenWidth * 2 / 3) {
            wmX = screenWidth - this.mSize;
            if (wmY <= this.mSize / 2) {
                position = 3;
                wmY = floatballCenterY - halfBallSize;
            } else if (wmY > screenHeight - this.mSize / 2) {
                position = 9;
                wmY = floatballCenterY - this.mSize + halfBallSize;
            } else {
                position = 6;
                wmY = floatballCenterY - this.mSize / 2;
            }
        }
        layoutParams.x = wmX;
        layoutParams.y = wmY;
        return position;
    }
}

