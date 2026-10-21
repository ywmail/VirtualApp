/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.animation.TimeInterpolator
 *  android.animation.TypeEvaluator
 *  android.animation.ValueAnimator
 *  android.animation.ValueAnimator$AnimatorUpdateListener
 *  android.content.Context
 *  android.content.res.TypedArray
 *  android.graphics.Canvas
 *  android.graphics.Color
 *  android.graphics.Paint
 *  android.graphics.Rect
 *  android.graphics.drawable.GradientDrawable
 *  android.os.Bundle
 *  android.os.Parcelable
 *  android.util.AttributeSet
 *  android.util.SparseArray
 *  android.view.View
 *  android.view.View$OnClickListener
 *  android.view.ViewGroup$LayoutParams
 *  android.view.ViewGroup$MarginLayoutParams
 *  android.view.animation.OvershootInterpolator
 *  android.widget.FrameLayout
 *  android.widget.LinearLayout
 *  android.widget.LinearLayout$LayoutParams
 *  android.widget.TextView
 *  androidx.fragment.app.Fragment
 *  androidx.fragment.app.FragmentActivity
 *  com.kook.librelease.R$id
 *  com.kook.librelease.R$layout
 *  com.kook.librelease.R$styleable
 */
package com.carlos.common.widget.tablayout;

import android.animation.TimeInterpolator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.carlos.common.widget.tablayout.listener.OnTabSelectListener;
import com.carlos.common.widget.tablayout.utils.FragmentChangeManager;
import com.carlos.common.widget.tablayout.utils.UnreadMsgUtils;
import com.carlos.common.widget.tablayout.widget.MsgView;
import com.kook.librelease.R;
import com.kook.librelease.StringFog;
import java.util.ArrayList;

public class SegmentTabLayout
extends FrameLayout
implements ValueAnimator.AnimatorUpdateListener {
    private Context mContext;
    private String[] mTitles;
    private LinearLayout mTabsContainer;
    private int mCurrentTab;
    private int mLastTab;
    private int mTabCount;
    private Rect mIndicatorRect = new Rect();
    private GradientDrawable mIndicatorDrawable = new GradientDrawable();
    private float mTabPadding;
    private boolean mTabSpaceEqual;
    private float mTabWidth;
    private int mIndicatorColor;
    private float mIndicatorHeight;
    private float mIndicatorCornerRadius;
    private float mIndicatorMarginLeft;
    private float mIndicatorMarginTop;
    private float mIndicatorMarginRight;
    private float mIndicatorMarginBottom;
    private long mIndicatorAnimDuration;
    private boolean mIndicatorAnimEnable;
    private boolean mIndicatorBounceEnable;
    private static final int TEXT_BOLD_NONE = 0;
    private static final int TEXT_BOLD_WHEN_SELECT = 1;
    private static final int TEXT_BOLD_BOTH = 2;
    private float mTextsize;
    private int mTextSelectColor;
    private int mTextUnselectColor;
    private int mTextBold;
    private boolean mTextAllCaps;
    private int mHeight;
    private ValueAnimator mValueAnimator;
    private OvershootInterpolator mInterpolator = new OvershootInterpolator(0.8f);
    private FragmentChangeManager mFragmentChangeManager;
    private float[] mRadiusArr = new float[8];
    private boolean mIsFirstDraw = true;
    private Paint mTextPaint = new Paint(1);
    private SparseArray<Boolean> mInitSetMap = new SparseArray();
    private OnTabSelectListener mListener;
    private IndicatorPoint mCurrentP = new IndicatorPoint();
    private IndicatorPoint mLastP = new IndicatorPoint();

    public SegmentTabLayout(Context context) {
        this(context, null, 0);
    }

    public SegmentTabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SegmentTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setWillNotDraw(false);
        this.setClipChildren(false);
        this.setClipToPadding(false);
        this.mContext = context;
        this.mTabsContainer = new LinearLayout(context);
        this.addView((View)this.mTabsContainer);
        this.obtainAttributes(context, attrs);
        String height = attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "layout_height");
        if (!height.equals("-1") && !height.equals("-2")) {
            int[] systemAttrs = new int[]{16842997};
            TypedArray a = context.obtainStyledAttributes(attrs, systemAttrs);
            this.mHeight = a.getDimensionPixelSize(0, -2);
            a.recycle();
        }
        this.mValueAnimator = ValueAnimator.ofObject((TypeEvaluator)new PointEvaluator(), (Object[])new Object[]{this.mLastP, this.mCurrentP});
        this.mValueAnimator.addUpdateListener((ValueAnimator.AnimatorUpdateListener)this);
    }

    private void obtainAttributes(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SegmentTabLayout);
        this.mIndicatorColor = ta.getColor(R.styleable.SegmentTabLayout_tl_indicator_color, Color.parseColor((String)"#222831"));
        this.mIndicatorHeight = ta.getDimension(R.styleable.SegmentTabLayout_tl_indicator_height, -1.0f);
        this.mIndicatorCornerRadius = ta.getDimension(R.styleable.SegmentTabLayout_tl_indicator_corner_radius, -1.0f);
        this.mIndicatorMarginLeft = ta.getDimension(R.styleable.SegmentTabLayout_tl_indicator_margin_left, (float)this.dp2px(0.0f));
        this.mIndicatorMarginTop = ta.getDimension(R.styleable.SegmentTabLayout_tl_indicator_margin_top, 0.0f);
        this.mIndicatorMarginRight = ta.getDimension(R.styleable.SegmentTabLayout_tl_indicator_margin_right, (float)this.dp2px(0.0f));
        this.mIndicatorMarginBottom = ta.getDimension(R.styleable.SegmentTabLayout_tl_indicator_margin_bottom, 0.0f);
        this.mIndicatorAnimEnable = ta.getBoolean(R.styleable.SegmentTabLayout_tl_indicator_anim_enable, false);
        this.mIndicatorBounceEnable = ta.getBoolean(R.styleable.SegmentTabLayout_tl_indicator_bounce_enable, false);
        this.mIndicatorAnimDuration = ta.getInt(R.styleable.SegmentTabLayout_tl_indicator_anim_duration, -1);
        this.mTextsize = ta.getDimension(R.styleable.SegmentTabLayout_tl_textsize, (float)this.sp2px(13.0f));
        this.mTextSelectColor = ta.getColor(R.styleable.SegmentTabLayout_tl_textSelectColor, Color.parseColor((String)"#ffffff"));
        this.mTextUnselectColor = ta.getColor(R.styleable.SegmentTabLayout_tl_textUnselectColor, this.mIndicatorColor);
        this.mTextBold = ta.getInt(R.styleable.SegmentTabLayout_tl_textBold, 0);
        this.mTextAllCaps = ta.getBoolean(R.styleable.SegmentTabLayout_tl_textAllCaps, false);
        this.mTabSpaceEqual = ta.getBoolean(R.styleable.SegmentTabLayout_tl_tab_space_equal, true);
        this.mTabWidth = ta.getDimension(R.styleable.SegmentTabLayout_tl_tab_width, (float)this.dp2px(-1.0f));
        this.mTabPadding = ta.getDimension(R.styleable.SegmentTabLayout_tl_tab_padding, this.mTabSpaceEqual || this.mTabWidth > 0.0f ? (float)this.dp2px(0.0f) : (float)this.dp2px(10.0f));
        ta.recycle();
    }

    public void setTabData(String[] titles) {
        if (titles == null || titles.length == 0) {
            throw new IllegalStateException("Titles can not be NULL or EMPTY !");
        }
        this.mTitles = titles;
        this.notifyDataSetChanged();
    }

    public void setTabData(String[] titles, FragmentActivity fa, int containerViewId, ArrayList<Fragment> fragments) {
        this.mFragmentChangeManager = new FragmentChangeManager(fa.getSupportFragmentManager(), containerViewId, fragments);
        this.setTabData(titles);
    }

    public void notifyDataSetChanged() {
        this.mTabsContainer.removeAllViews();
        this.mTabCount = this.mTitles.length;
        for (int i = 0; i < this.mTabCount; ++i) {
            View tabView = View.inflate((Context)this.mContext, (int)R.layout.layout_tab_segment, null);
            tabView.setTag((Object)i);
            this.addTab(i, tabView);
        }
        this.updateTabStyles();
    }

    private void addTab(int position, View tabView) {
        LinearLayout.LayoutParams lp_tab;
        TextView tv_tab_title = (TextView)tabView.findViewById(R.id.tv_tab_title);
        tv_tab_title.setText((CharSequence)this.mTitles[position]);
        tabView.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v) {
                int position = (Integer)v.getTag();
                if (SegmentTabLayout.this.mCurrentTab != position) {
                    SegmentTabLayout.this.setCurrentTab(position);
                    if (SegmentTabLayout.this.mListener != null) {
                        SegmentTabLayout.this.mListener.onTabSelect(position);
                    }
                } else if (SegmentTabLayout.this.mListener != null) {
                    SegmentTabLayout.this.mListener.onTabReselect(position);
                }
            }
        });
        LinearLayout.LayoutParams layoutParams = lp_tab = this.mTabSpaceEqual ? new LinearLayout.LayoutParams(0, -1, 1.0f) : new LinearLayout.LayoutParams(-2, -1);
        if (this.mTabWidth > 0.0f) {
            lp_tab = new LinearLayout.LayoutParams((int)this.mTabWidth, -1);
        }
        this.mTabsContainer.addView(tabView, position, (ViewGroup.LayoutParams)lp_tab);
    }

    private void updateTabStyles() {
        for (int i = 0; i < this.mTabCount; ++i) {
            View tabView = this.mTabsContainer.getChildAt(i);
            tabView.setPadding((int)this.mTabPadding, 0, (int)this.mTabPadding, 0);
            TextView tv_tab_title = (TextView)tabView.findViewById(R.id.tv_tab_title);
            tv_tab_title.setTextColor(i == this.mCurrentTab ? this.mTextSelectColor : this.mTextUnselectColor);
            tv_tab_title.setTextSize(0, this.mTextsize);
            if (this.mTextAllCaps) {
                tv_tab_title.setText((CharSequence)tv_tab_title.getText().toString().toUpperCase());
            }
            if (this.mTextBold == 2) {
                tv_tab_title.getPaint().setFakeBoldText(true);
                continue;
            }
            if (this.mTextBold != 0) continue;
            tv_tab_title.getPaint().setFakeBoldText(false);
        }
    }

    private void updateTabSelection(int position) {
        for (int i = 0; i < this.mTabCount; ++i) {
            View tabView = this.mTabsContainer.getChildAt(i);
            boolean isSelect = i == position;
            TextView tab_title = (TextView)tabView.findViewById(R.id.tv_tab_title);
            tab_title.setTextColor(isSelect ? this.mTextSelectColor : this.mTextUnselectColor);
            if (this.mTextBold != 1) continue;
            tab_title.getPaint().setFakeBoldText(isSelect);
        }
    }

    private void calcOffset() {
        View currentTabView = this.mTabsContainer.getChildAt(this.mCurrentTab);
        this.mCurrentP.left = currentTabView.getLeft();
        this.mCurrentP.right = currentTabView.getRight();
        View lastTabView = this.mTabsContainer.getChildAt(this.mLastTab);
        this.mLastP.left = lastTabView.getLeft();
        this.mLastP.right = lastTabView.getRight();
        if (this.mLastP.left == this.mCurrentP.left && this.mLastP.right == this.mCurrentP.right) {
            this.invalidate();
        } else {
            this.mValueAnimator.setObjectValues(new Object[]{this.mLastP, this.mCurrentP});
            if (this.mIndicatorBounceEnable) {
                this.mValueAnimator.setInterpolator((TimeInterpolator)this.mInterpolator);
            }
            if (this.mIndicatorAnimDuration < 0L) {
                this.mIndicatorAnimDuration = this.mIndicatorBounceEnable ? 500L : 250L;
            }
            this.mValueAnimator.setDuration(this.mIndicatorAnimDuration);
            this.mValueAnimator.start();
        }
    }

    private void calcIndicatorRect() {
        View currentTabView = this.mTabsContainer.getChildAt(this.mCurrentTab);
        float left = currentTabView.getLeft();
        float right = currentTabView.getRight();
        this.mIndicatorRect.left = (int)left;
        this.mIndicatorRect.right = (int)right;
        if (!this.mIndicatorAnimEnable) {
            if (this.mCurrentTab == 0) {
                this.mRadiusArr[0] = this.mIndicatorCornerRadius;
                this.mRadiusArr[1] = this.mIndicatorCornerRadius;
                this.mRadiusArr[2] = 0.0f;
                this.mRadiusArr[3] = 0.0f;
                this.mRadiusArr[4] = 0.0f;
                this.mRadiusArr[5] = 0.0f;
                this.mRadiusArr[6] = this.mIndicatorCornerRadius;
                this.mRadiusArr[7] = this.mIndicatorCornerRadius;
            } else if (this.mCurrentTab == this.mTabCount - 1) {
                this.mRadiusArr[0] = 0.0f;
                this.mRadiusArr[1] = 0.0f;
                this.mRadiusArr[2] = this.mIndicatorCornerRadius;
                this.mRadiusArr[3] = this.mIndicatorCornerRadius;
                this.mRadiusArr[4] = this.mIndicatorCornerRadius;
                this.mRadiusArr[5] = this.mIndicatorCornerRadius;
                this.mRadiusArr[6] = 0.0f;
                this.mRadiusArr[7] = 0.0f;
            } else {
                this.mRadiusArr[0] = 0.0f;
                this.mRadiusArr[1] = 0.0f;
                this.mRadiusArr[2] = 0.0f;
                this.mRadiusArr[3] = 0.0f;
                this.mRadiusArr[4] = 0.0f;
                this.mRadiusArr[5] = 0.0f;
                this.mRadiusArr[6] = 0.0f;
                this.mRadiusArr[7] = 0.0f;
            }
        } else {
            this.mRadiusArr[0] = this.mIndicatorCornerRadius;
            this.mRadiusArr[1] = this.mIndicatorCornerRadius;
            this.mRadiusArr[2] = this.mIndicatorCornerRadius;
            this.mRadiusArr[3] = this.mIndicatorCornerRadius;
            this.mRadiusArr[4] = this.mIndicatorCornerRadius;
            this.mRadiusArr[5] = this.mIndicatorCornerRadius;
            this.mRadiusArr[6] = this.mIndicatorCornerRadius;
            this.mRadiusArr[7] = this.mIndicatorCornerRadius;
        }
    }

    public void onAnimationUpdate(ValueAnimator animation) {
        IndicatorPoint p = (IndicatorPoint)animation.getAnimatedValue();
        this.mIndicatorRect.left = (int)p.left;
        this.mIndicatorRect.right = (int)p.right;
        this.invalidate();
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.isInEditMode() || this.mTabCount <= 0) {
            return;
        }
        int height = this.getHeight();
        int paddingLeft = this.getPaddingLeft();
        if (this.mIndicatorHeight < 0.0f) {
            this.mIndicatorHeight = (float)height - this.mIndicatorMarginTop - this.mIndicatorMarginBottom;
        }
        if (this.mIndicatorCornerRadius < 0.0f || this.mIndicatorCornerRadius > this.mIndicatorHeight / 2.0f) {
            this.mIndicatorCornerRadius = this.mIndicatorHeight / 2.0f;
        }
        if (this.mIndicatorAnimEnable) {
            if (this.mIsFirstDraw) {
                this.mIsFirstDraw = false;
                this.calcIndicatorRect();
            }
        } else {
            this.calcIndicatorRect();
        }
        this.mIndicatorDrawable.setColor(this.mIndicatorColor);
        this.mIndicatorDrawable.setBounds(paddingLeft + (int)this.mIndicatorMarginLeft + this.mIndicatorRect.left, (int)this.mIndicatorMarginTop, (int)((float)(paddingLeft + this.mIndicatorRect.right) - this.mIndicatorMarginRight), (int)(this.mIndicatorMarginTop + this.mIndicatorHeight));
        this.mIndicatorDrawable.setCornerRadii(this.mRadiusArr);
        this.mIndicatorDrawable.draw(canvas);
    }

    public void setCurrentTab(int currentTab) {
        this.mLastTab = this.mCurrentTab;
        this.mCurrentTab = currentTab;
        this.updateTabSelection(currentTab);
        if (this.mFragmentChangeManager != null) {
            this.mFragmentChangeManager.setFragments(currentTab);
        }
        if (this.mIndicatorAnimEnable) {
            this.calcOffset();
        } else {
            this.invalidate();
        }
    }

    public void setTabPadding(float tabPadding) {
        this.mTabPadding = this.dp2px(tabPadding);
        this.updateTabStyles();
    }

    public void setTabSpaceEqual(boolean tabSpaceEqual) {
        this.mTabSpaceEqual = tabSpaceEqual;
        this.updateTabStyles();
    }

    public void setTabWidth(float tabWidth) {
        this.mTabWidth = this.dp2px(tabWidth);
        this.updateTabStyles();
    }

    public void setIndicatorColor(int indicatorColor) {
        this.mIndicatorColor = indicatorColor;
        this.invalidate();
    }

    public void setIndicatorHeight(float indicatorHeight) {
        this.mIndicatorHeight = this.dp2px(indicatorHeight);
        this.invalidate();
    }

    public void setIndicatorCornerRadius(float indicatorCornerRadius) {
        this.mIndicatorCornerRadius = this.dp2px(indicatorCornerRadius);
        this.invalidate();
    }

    public void setIndicatorMargin(float indicatorMarginLeft, float indicatorMarginTop, float indicatorMarginRight, float indicatorMarginBottom) {
        this.mIndicatorMarginLeft = this.dp2px(indicatorMarginLeft);
        this.mIndicatorMarginTop = this.dp2px(indicatorMarginTop);
        this.mIndicatorMarginRight = this.dp2px(indicatorMarginRight);
        this.mIndicatorMarginBottom = this.dp2px(indicatorMarginBottom);
        this.invalidate();
    }

    public void setIndicatorAnimDuration(long indicatorAnimDuration) {
        this.mIndicatorAnimDuration = indicatorAnimDuration;
    }

    public void setIndicatorAnimEnable(boolean indicatorAnimEnable) {
        this.mIndicatorAnimEnable = indicatorAnimEnable;
    }

    public void setIndicatorBounceEnable(boolean indicatorBounceEnable) {
        this.mIndicatorBounceEnable = indicatorBounceEnable;
    }

    public void setTextsize(float textsize) {
        this.mTextsize = this.sp2px(textsize);
        this.updateTabStyles();
    }

    public void setTextSelectColor(int textSelectColor) {
        this.mTextSelectColor = textSelectColor;
        this.updateTabStyles();
    }

    public void setTextUnselectColor(int textUnselectColor) {
        this.mTextUnselectColor = textUnselectColor;
        this.updateTabStyles();
    }

    public void setTextBold(int textBold) {
        this.mTextBold = textBold;
        this.updateTabStyles();
    }

    public void setTextAllCaps(boolean textAllCaps) {
        this.mTextAllCaps = textAllCaps;
        this.updateTabStyles();
    }

    public int getTabCount() {
        return this.mTabCount;
    }

    public int getCurrentTab() {
        return this.mCurrentTab;
    }

    public float getTabPadding() {
        return this.mTabPadding;
    }

    public boolean isTabSpaceEqual() {
        return this.mTabSpaceEqual;
    }

    public float getTabWidth() {
        return this.mTabWidth;
    }

    public int getIndicatorColor() {
        return this.mIndicatorColor;
    }

    public float getIndicatorHeight() {
        return this.mIndicatorHeight;
    }

    public float getIndicatorCornerRadius() {
        return this.mIndicatorCornerRadius;
    }

    public float getIndicatorMarginLeft() {
        return this.mIndicatorMarginLeft;
    }

    public float getIndicatorMarginTop() {
        return this.mIndicatorMarginTop;
    }

    public float getIndicatorMarginRight() {
        return this.mIndicatorMarginRight;
    }

    public float getIndicatorMarginBottom() {
        return this.mIndicatorMarginBottom;
    }

    public long getIndicatorAnimDuration() {
        return this.mIndicatorAnimDuration;
    }

    public boolean isIndicatorAnimEnable() {
        return this.mIndicatorAnimEnable;
    }

    public boolean isIndicatorBounceEnable() {
        return this.mIndicatorBounceEnable;
    }

    public float getTextsize() {
        return this.mTextsize;
    }

    public int getTextSelectColor() {
        return this.mTextSelectColor;
    }

    public int getTextUnselectColor() {
        return this.mTextUnselectColor;
    }

    public int getTextBold() {
        return this.mTextBold;
    }

    public boolean isTextAllCaps() {
        return this.mTextAllCaps;
    }

    public TextView getTitleView(int tab) {
        View tabView = this.mTabsContainer.getChildAt(tab);
        TextView tv_tab_title = (TextView)tabView.findViewById(R.id.tv_tab_title);
        return tv_tab_title;
    }

    public void showMsg(int position, int num) {
        View tabView;
        MsgView tipView;
        if (position >= this.mTabCount) {
            position = this.mTabCount - 1;
        }
        if ((tipView = (MsgView)(tabView = this.mTabsContainer.getChildAt(position)).findViewById(R.id.rtv_msg_tip)) != null) {
            UnreadMsgUtils.show(tipView, num);
            if (this.mInitSetMap.get(position) != null && ((Boolean)this.mInitSetMap.get(position)).booleanValue()) {
                return;
            }
            this.setMsgMargin(position, 2.0f, 2.0f);
            this.mInitSetMap.put(position, true);
        }
    }

    public void showDot(int position) {
        if (position >= this.mTabCount) {
            position = this.mTabCount - 1;
        }
        this.showMsg(position, 0);
    }

    public void hideMsg(int position) {
        View tabView;
        MsgView tipView;
        if (position >= this.mTabCount) {
            position = this.mTabCount - 1;
        }
        if ((tipView = (MsgView)(tabView = this.mTabsContainer.getChildAt(position)).findViewById(R.id.rtv_msg_tip)) != null) {
            tipView.setVisibility(View.GONE);
        }
    }

    public void setMsgMargin(int position, float leftPadding, float bottomPadding) {
        View tabView;
        MsgView tipView;
        if (position >= this.mTabCount) {
            position = this.mTabCount - 1;
        }
        if ((tipView = (MsgView)(tabView = this.mTabsContainer.getChildAt(position)).findViewById(R.id.rtv_msg_tip)) != null) {
            TextView tv_tab_title = (TextView)tabView.findViewById(R.id.tv_tab_title);
            this.mTextPaint.setTextSize(this.mTextsize);
            float textWidth = this.mTextPaint.measureText(tv_tab_title.getText().toString());
            float textHeight = this.mTextPaint.descent() - this.mTextPaint.ascent();
            ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams)tipView.getLayoutParams();
            lp.leftMargin = this.dp2px(leftPadding);
            lp.topMargin = this.mHeight > 0 ? (int)((float)this.mHeight - textHeight) / 2 - this.dp2px(bottomPadding) : this.dp2px(bottomPadding);
            tipView.setLayoutParams((ViewGroup.LayoutParams)lp);
        }
    }

    public MsgView getMsgView(int position) {
        if (position >= this.mTabCount) {
            position = this.mTabCount - 1;
        }
        View tabView = this.mTabsContainer.getChildAt(position);
        MsgView tipView = (MsgView)tabView.findViewById(R.id.rtv_msg_tip);
        return tipView;
    }

    public void setOnTabSelectListener(OnTabSelectListener listener) {
        this.mListener = listener;
    }

    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("instanceState", super.onSaveInstanceState());
        bundle.putInt("mCurrentTab", this.mCurrentTab);
        return bundle;
    }

    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle)state;
            this.mCurrentTab = bundle.getInt("mCurrentTab");
            state = bundle.getParcelable("instanceState");
            if (this.mCurrentTab != 0 && this.mTabsContainer.getChildCount() > 0) {
                this.updateTabSelection(this.mCurrentTab);
            }
        }
        super.onRestoreInstanceState(state);
    }

    protected int dp2px(float dp) {
        float scale = this.mContext.getResources().getDisplayMetrics().density;
        return (int)(dp * scale + 0.5f);
    }

    protected int sp2px(float sp) {
        float scale = this.mContext.getResources().getDisplayMetrics().scaledDensity;
        return (int)(sp * scale + 0.5f);
    }

    class PointEvaluator
    implements TypeEvaluator<IndicatorPoint> {
        PointEvaluator() {
        }

        public IndicatorPoint evaluate(float fraction, IndicatorPoint startValue, IndicatorPoint endValue) {
            float left = startValue.left + fraction * (endValue.left - startValue.left);
            float right = startValue.right + fraction * (endValue.right - startValue.right);
            IndicatorPoint point = new IndicatorPoint();
            point.left = left;
            point.right = right;
            return point;
        }
    }

    class IndicatorPoint {
        public float left;
        public float right;

        IndicatorPoint() {
        }
    }
}

