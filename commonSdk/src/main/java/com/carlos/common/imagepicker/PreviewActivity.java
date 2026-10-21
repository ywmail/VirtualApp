/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.animation.Animator
 *  android.animation.Animator$AnimatorListener
 *  android.animation.AnimatorListenerAdapter
 *  android.animation.ObjectAnimator
 *  android.annotation.SuppressLint
 *  android.app.Activity
 *  android.content.Context
 *  android.content.Intent
 *  android.content.res.Resources
 *  android.graphics.Bitmap
 *  android.graphics.BitmapFactory
 *  android.graphics.drawable.BitmapDrawable
 *  android.graphics.drawable.Drawable
 *  android.os.Build$VERSION
 *  android.os.Bundle
 *  android.view.View
 *  android.view.View$OnClickListener
 *  android.view.ViewGroup$LayoutParams
 *  android.view.Window
 *  android.widget.FrameLayout
 *  android.widget.RelativeLayout
 *  android.widget.RelativeLayout$LayoutParams
 *  android.widget.TextView
 *  androidx.annotation.Nullable
 *  androidx.annotation.RequiresApi
 *  androidx.appcompat.app.AppCompatActivity
 *  androidx.core.content.ContextCompat
 *  androidx.viewpager.widget.ViewPager$OnPageChangeListener
 *  com.kook.librelease.R$color
 *  com.kook.librelease.R$drawable
 *  com.kook.librelease.R$id
 *  com.kook.librelease.R$layout
 */
package com.carlos.common.imagepicker;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import com.carlos.common.imagepicker.adapter.ImagePagerAdapter;
import com.carlos.common.imagepicker.entity.Image;
import com.carlos.common.imagepicker.widget.MyViewPager;
import com.kook.librelease.R;
import com.kook.librelease.StringFog;
import java.util.ArrayList;

public class PreviewActivity
extends AppCompatActivity {
    private MyViewPager vpImage;
    private TextView tvIndicator;
    private TextView tvConfirm;
    private FrameLayout btnConfirm;
    private TextView tvSelect;
    private RelativeLayout rlTopBar;
    private RelativeLayout rlBottomBar;
    private static ArrayList<Image> tempImages;
    private static ArrayList<Image> tempSelectImages;
    private ArrayList<Image> mImages;
    private ArrayList<Image> mSelectImages;
    private boolean isShowBar = true;
    private boolean isConfirm = false;
    private boolean isSingle;
    private int mMaxCount;
    private BitmapDrawable mSelectDrawable;
    private BitmapDrawable mUnSelectDrawable;

    public static void openActivity(Activity activity, ArrayList<Image> images, ArrayList<Image> selectImages, boolean isSingle, int maxSelectCount, int position, int topBarColor, int bottomBarColor, int statusBarColor) {
        tempImages = images;
        tempSelectImages = selectImages;
        Intent intent = new Intent((Context)activity, PreviewActivity.class);
        intent.putExtra("max_selected_count", maxSelectCount);
        intent.putExtra("single", isSingle);
        intent.putExtra("position", position);
        intent.putExtra("toolBarColor", topBarColor);
        intent.putExtra("bottomBarColor", bottomBarColor);
        intent.putExtra("statusBarColor", statusBarColor);
        activity.startActivityForResult(intent, 1000);
    }

    @RequiresApi(api=16)
    @SuppressLint(value={"SetTextI18n"})
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_preview);
        this.setStatusBarVisible(true);
        this.mImages = tempImages;
        tempImages = null;
        this.mSelectImages = tempSelectImages;
        tempSelectImages = null;
        Intent intent = this.getIntent();
        this.mMaxCount = intent.getIntExtra("max_selected_count", 0);
        this.isSingle = intent.getBooleanExtra("single", false);
        Resources resources = this.getResources();
        Bitmap selectBitmap = BitmapFactory.decodeResource((Resources)resources, (int)R.drawable.ic_image_select);
        this.mSelectDrawable = new BitmapDrawable(resources, selectBitmap);
        this.mSelectDrawable.setBounds(0, 0, selectBitmap.getWidth(), selectBitmap.getHeight());
        Bitmap unSelectBitmap = BitmapFactory.decodeResource((Resources)resources, (int)R.drawable.ic_image_un_select);
        this.mUnSelectDrawable = new BitmapDrawable(resources, unSelectBitmap);
        this.mUnSelectDrawable.setBounds(0, 0, unSelectBitmap.getWidth(), unSelectBitmap.getHeight());
        this.setStatusBarColor(intent.getIntExtra("statusBarColor", R.color.blue));
        this.initView();
        this.setToolBarColor(intent.getIntExtra("toolBarColor", R.color.blue));
        this.setBottomBarColor(intent.getIntExtra("bottomBarColor", R.color.blue));
        this.initListener();
        this.initViewPager();
        this.tvIndicator.setText((CharSequence)("1/" + this.mImages.size()));
        this.changeSelect(this.mImages.get(0));
        this.vpImage.setCurrentItem(intent.getIntExtra("position", 0));
    }

    private void initView() {
        this.vpImage = (MyViewPager)this.findViewById(R.id.vp_image);
        this.tvIndicator = (TextView)this.findViewById(R.id.tv_indicator);
        this.tvConfirm = (TextView)this.findViewById(R.id.tv_confirm);
        this.btnConfirm = (FrameLayout)this.findViewById(R.id.btn_confirm);
        this.tvSelect = (TextView)this.findViewById(R.id.tv_select);
        this.rlTopBar = (RelativeLayout)this.findViewById(R.id.rl_top_bar);
        this.rlBottomBar = (RelativeLayout)this.findViewById(R.id.rl_bottom_bar);
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams)this.rlTopBar.getLayoutParams();
        lp.topMargin = PreviewActivity.getStatusBarHeight((Context)this);
        this.rlTopBar.setLayoutParams((ViewGroup.LayoutParams)lp);
    }

    private void initListener() {
        this.findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener(){

            public void onClick(View v) {
                PreviewActivity.this.finish();
            }
        });
        this.btnConfirm.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v) {
                PreviewActivity.this.isConfirm = true;
                PreviewActivity.this.finish();
            }
        });
        this.tvSelect.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v) {
                PreviewActivity.this.clickSelect();
            }
        });
    }

    private void initViewPager() {
        ImagePagerAdapter adapter = new ImagePagerAdapter((Context)this, this.mImages);
        this.vpImage.setAdapter(adapter);
        adapter.setOnItemClickListener(new ImagePagerAdapter.OnItemClickListener(){

            @Override
            @RequiresApi(api=16)
            public void onItemClick(int position, Image image) {
                if (PreviewActivity.this.isShowBar) {
                    PreviewActivity.this.hideBar();
                } else {
                    PreviewActivity.this.showBar();
                }
            }
        });
        this.vpImage.addOnPageChangeListener(new ViewPager.OnPageChangeListener(){

            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @SuppressLint(value={"SetTextI18n"})
            public void onPageSelected(int position) {
                PreviewActivity.this.tvIndicator.setText((CharSequence)(position + 1 + "/" + PreviewActivity.this.mImages.size()));
                PreviewActivity.this.changeSelect((Image)PreviewActivity.this.mImages.get(position));
            }

            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void setStatusBarColor(int statusBarColor) {
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(Integer.MIN_VALUE);
            window.setStatusBarColor(ContextCompat.getColor((Context)this, (int)statusBarColor));
        }
    }

    private void setToolBarColor(int color2) {
        this.rlTopBar.setBackgroundColor(ContextCompat.getColor((Context)this, (int)color2));
    }

    private void setBottomBarColor(int color2) {
        this.rlBottomBar.setBackgroundColor(ContextCompat.getColor((Context)this, (int)color2));
    }

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @RequiresApi(api=16)
    private void setStatusBarVisible(boolean show) {
        if (show) {
            this.getWindow().getDecorView().setSystemUiVisibility(1024);
        } else {
            this.getWindow().getDecorView().setSystemUiVisibility(1028);
        }
    }

    @RequiresApi(api=16)
    private void showBar() {
        this.isShowBar = true;
        this.setStatusBarVisible(true);
        this.rlTopBar.postDelayed(new Runnable(){

            @Override
            public void run() {
                if (PreviewActivity.this.rlTopBar != null) {
                    ObjectAnimator animator = ObjectAnimator.ofFloat((Object)PreviewActivity.this.rlTopBar, (String)"translationY", (float[])new float[]{PreviewActivity.this.rlTopBar.getTranslationY(), 0.0f}).setDuration(300L);
                    animator.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter(){

                        public void onAnimationStart(Animator animation) {
                            super.onAnimationStart(animation);
                            if (PreviewActivity.this.rlTopBar != null) {
                                PreviewActivity.this.rlTopBar.setVisibility(0);
                            }
                        }
                    });
                    animator.start();
                    ObjectAnimator.ofFloat((Object)PreviewActivity.this.rlBottomBar, (String)"translationY", (float[])new float[]{PreviewActivity.this.rlBottomBar.getTranslationY(), 0.0f}).setDuration(300L).start();
                }
            }
        }, 100L);
    }

    private void hideBar() {
        this.isShowBar = false;
        ObjectAnimator animator = ObjectAnimator.ofFloat((Object)this.rlTopBar, (String)"translationY", (float[])new float[]{0.0f, -this.rlTopBar.getHeight()}).setDuration(300L);
        animator.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter(){

            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (PreviewActivity.this.rlTopBar != null) {
                    PreviewActivity.this.rlTopBar.setVisibility(8);
                    PreviewActivity.this.rlTopBar.postDelayed(new Runnable(){

                        @Override
                        @RequiresApi(api=16)
                        public void run() {
                            PreviewActivity.this.setStatusBarVisible(false);
                        }
                    }, 5L);
                }
            }
        });
        animator.start();
        ObjectAnimator.ofFloat((Object)this.rlBottomBar, (String)"translationY", (float[])new float[]{0.0f, this.rlBottomBar.getHeight()}).setDuration(300L).start();
    }

    private void clickSelect() {
        int position = this.vpImage.getCurrentItem();
        if (this.mImages != null && this.mImages.size() > position) {
            Image image = this.mImages.get(position);
            if (this.mSelectImages.contains(image)) {
                this.mSelectImages.remove(image);
            } else if (this.isSingle) {
                this.mSelectImages.clear();
                this.mSelectImages.add(image);
            } else if (this.mMaxCount <= 0 || this.mSelectImages.size() < this.mMaxCount) {
                this.mSelectImages.add(image);
            }
            this.changeSelect(image);
        }
    }

    private void changeSelect(Image image) {
        this.tvSelect.setCompoundDrawables((Drawable)(this.mSelectImages.contains(image) ? this.mSelectDrawable : this.mUnSelectDrawable), null, null, null);
        this.setSelectImageCount(this.mSelectImages.size());
    }

    @SuppressLint(value={"SetTextI18n"})
    private void setSelectImageCount(int count) {
        if (count == 0) {
            this.btnConfirm.setEnabled(false);
            this.tvConfirm.setText((CharSequence)"确定");
        } else {
            this.btnConfirm.setEnabled(true);
            if (this.isSingle) {
                this.tvConfirm.setText((CharSequence)"确定");
            } else if (this.mMaxCount > 0) {
                this.tvConfirm.setText((CharSequence)("确定(" + count + "/" + this.mMaxCount + ")"));
            } else {
                this.tvConfirm.setText((CharSequence)("确定(" + count + ")"));
            }
        }
    }

    public void finish() {
        Intent intent = new Intent();
        intent.putExtra("is_confirm", this.isConfirm);
        this.setResult(1000, intent);
        super.finish();
    }
}

