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
 *  android.graphics.drawable.BitmapDrawable
 *  android.graphics.drawable.Drawable
 *  android.os.Bundle
 *  android.view.View
 *  android.view.View$OnClickListener
 *  android.widget.FrameLayout
 *  android.widget.RelativeLayout
 *  android.widget.TextView
 *  android.widget.Toast
 *  androidx.annotation.ColorInt
 *  androidx.annotation.Nullable
 *  androidx.annotation.RequiresApi
 *  androidx.appcompat.app.ActionBar
 *  androidx.appcompat.app.AppCompatActivity
 *  androidx.appcompat.widget.Toolbar
 *  androidx.core.content.ContextCompat
 *  androidx.recyclerview.widget.LinearLayoutManager
 *  androidx.recyclerview.widget.PagerSnapHelper
 *  androidx.recyclerview.widget.RecyclerView
 *  androidx.recyclerview.widget.RecyclerView$Adapter
 *  androidx.recyclerview.widget.RecyclerView$LayoutManager
 *  androidx.recyclerview.widget.RecyclerView$OnScrollListener
 *  com.google.android.material.appbar.AppBarLayout
 *  com.kook.librelease.R$color
 *  com.kook.librelease.R$drawable
 *  com.kook.librelease.R$id
 *  com.kook.librelease.R$layout
 *  com.kook.librelease.R$string
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
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import com.carlos.common.imagepicker.adapter.BottomPreviewAdapter;
import com.carlos.common.imagepicker.adapter.PreviewImageAdapter;
import com.carlos.common.imagepicker.entity.Image;
import com.carlos.common.imagepicker.utils.ImageUtil;
import com.carlos.common.imagepicker.utils.StatusBarUtils;
import com.google.android.material.appbar.AppBarLayout;
import com.kook.librelease.R;
import com.kook.librelease.StringFog;
import java.util.ArrayList;
import java.util.List;

public class RvPreviewActivity
extends AppCompatActivity {
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private TextView tvConfirm;
    private FrameLayout btnConfirm;
    private TextView tvSelect;
    private RelativeLayout rlBottomBar;
    private AppBarLayout appBarLayout;
    private Toolbar toolbar;
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
    private RecyclerView bottomRecycleview;
    private BottomPreviewAdapter bottomPreviewAdapter;
    private View line;
    private boolean isPreview;
    private PreviewImageAdapter previewImageAdapter;

    public static void openActivity(boolean isPreview, Activity activity, ArrayList<Image> images, ArrayList<Image> selectImages, boolean isSingle, int maxSelectCount, int position, @ColorInt int toolBarColor, @ColorInt int bottomBarColor, @ColorInt int statusBarColor) {
        tempImages = images;
        tempSelectImages = selectImages;
        Intent intent = new Intent((Context)activity, RvPreviewActivity.class);
        intent.putExtra("max_selected_count", maxSelectCount);
        intent.putExtra("single", isSingle);
        intent.putExtra("position", position);
        intent.putExtra("isPreview", isPreview);
        intent.putExtra("toolBarColor", toolBarColor);
        intent.putExtra("bottomBarColor", bottomBarColor);
        intent.putExtra("statusBarColor", statusBarColor);
        activity.startActivityForResult(intent, 1000);
    }

    @RequiresApi(api=16)
    @SuppressLint(value={"SetTextI18n"})
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_rv_preview);
        this.appBarLayout = (AppBarLayout)this.findViewById(R.id.appbar);
        this.toolbar = (Toolbar)this.findViewById(R.id.toolbar);
        this.setSupportActionBar(this.toolbar);
        ActionBar actionBar = this.getSupportActionBar();
        assert (actionBar != null);
        actionBar.setDisplayHomeAsUpEnabled(true);
        this.toolbar.setNavigationOnClickListener(new View.OnClickListener(){

            public void onClick(View v) {
                RvPreviewActivity.this.finish();
            }
        });
        this.setStatusBarVisible(true);
        this.mImages = tempImages;
        tempImages = null;
        this.mSelectImages = tempSelectImages;
        tempSelectImages = null;
        Intent intent = this.getIntent();
        this.mMaxCount = intent.getIntExtra("max_selected_count", 0);
        this.isSingle = intent.getBooleanExtra("single", false);
        this.isPreview = intent.getBooleanExtra("isPreview", false);
        Resources resources = this.getResources();
        Bitmap selectBitmap = ImageUtil.getBitmap((Context)this, R.drawable.ic_image_select);
        this.mSelectDrawable = new BitmapDrawable(resources, selectBitmap);
        this.mSelectDrawable.setBounds(0, 0, selectBitmap.getWidth(), selectBitmap.getHeight());
        Bitmap unSelectBitmap = ImageUtil.getBitmap((Context)this, R.drawable.ic_image_un_select);
        this.mUnSelectDrawable = new BitmapDrawable(resources, unSelectBitmap);
        this.mUnSelectDrawable.setBounds(0, 0, unSelectBitmap.getWidth(), unSelectBitmap.getHeight());
        int toolBarColor = intent.getIntExtra("toolBarColor", ContextCompat.getColor((Context)this, (int)R.color.blue));
        int bottomBarColor = intent.getIntExtra("bottomBarColor", ContextCompat.getColor((Context)this, (int)R.color.blue));
        int statusBarColor = intent.getIntExtra("statusBarColor", ContextCompat.getColor((Context)this, (int)R.color.blue));
        this.initView();
        StatusBarUtils.setBarColor((Activity)this, statusBarColor);
        this.setToolBarColor(toolBarColor);
        this.setBottomBarColor(bottomBarColor);
        this.initListener();
        this.initViewPager();
        this.changeSelect(this.mImages.get(intent.getIntExtra("position", 0)));
        this.recyclerView.scrollToPosition(intent.getIntExtra("position", 0));
        this.toolbar.setTitle((CharSequence)(intent.getIntExtra("position", 0) + 1 + "/" + this.mImages.size()));
        if (this.isPreview) {
            this.bottomRecycleview.smoothScrollToPosition(0);
        }
    }

    private void initView() {
        this.recyclerView = (RecyclerView)this.findViewById(R.id.rv_preview);
        this.tvConfirm = (TextView)this.findViewById(R.id.tv_confirm);
        this.btnConfirm = (FrameLayout)this.findViewById(R.id.btn_confirm);
        this.tvSelect = (TextView)this.findViewById(R.id.tv_select);
        this.rlBottomBar = (RelativeLayout)this.findViewById(R.id.rl_bottom_bar);
        this.bottomRecycleview = (RecyclerView)this.findViewById(R.id.bottom_recycleview);
        this.line = this.findViewById(R.id.line);
        this.bottomRecycleview.setLayoutManager((RecyclerView.LayoutManager)new LinearLayoutManager((Context)this, 0, false));
        if (this.mSelectImages.size() == 0) {
            this.bottomRecycleview.setVisibility(8);
            this.line.setVisibility(8);
        }
        this.bottomPreviewAdapter = new BottomPreviewAdapter((Context)this, this.mSelectImages);
        this.bottomRecycleview.setAdapter((RecyclerView.Adapter)this.bottomPreviewAdapter);
    }

    private void initListener() {
        this.btnConfirm.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v) {
                RvPreviewActivity.this.isConfirm = true;
                RvPreviewActivity.this.finish();
            }
        });
        this.tvSelect.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v) {
                RvPreviewActivity.this.clickSelect();
            }
        });
        this.bottomPreviewAdapter.setOnItemClcikLitener(new BottomPreviewAdapter.OnItemClcikLitener(){

            @Override
            public void OnItemClcik(int position, Image image) {
                if (RvPreviewActivity.this.isPreview) {
                    List<Image> imageList = RvPreviewActivity.this.previewImageAdapter.getData();
                    for (int i = 0; i < imageList.size(); ++i) {
                        if (!imageList.get(i).equals(image)) continue;
                        RvPreviewActivity.this.recyclerView.smoothScrollToPosition(i);
                    }
                } else {
                    RvPreviewActivity.this.recyclerView.smoothScrollToPosition(((Image)RvPreviewActivity.this.mSelectImages.get(position)).getPosition());
                }
                RvPreviewActivity.this.bottomPreviewAdapter.notifyDataSetChanged();
            }
        });
    }

    private void initViewPager() {
        this.recyclerView.setLayoutManager((RecyclerView.LayoutManager)new LinearLayoutManager((Context)this, 0, false));
        this.linearLayoutManager = (LinearLayoutManager)this.recyclerView.getLayoutManager();
        this.previewImageAdapter = new PreviewImageAdapter((Context)this, this.mImages);
        this.recyclerView.setAdapter((RecyclerView.Adapter)this.previewImageAdapter);
        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(this.recyclerView);
        this.previewImageAdapter.setOnItemClcikLitener(new PreviewImageAdapter.OnItemClcikLitener(){

            @Override
            @RequiresApi(api=16)
            public void OnItemClcik(PreviewImageAdapter previewImageAdapter, View iteView, int position) {
                if (RvPreviewActivity.this.isShowBar) {
                    RvPreviewActivity.this.hideBar();
                } else {
                    RvPreviewActivity.this.showBar();
                }
            }
        });
        this.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){

            @SuppressLint(value={"SetTextI18n"})
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == 0) {
                    int position = RvPreviewActivity.this.linearLayoutManager.findLastVisibleItemPosition();
                    ((Image)RvPreviewActivity.this.mImages.get(position)).setPosition(position);
                    RvPreviewActivity.this.toolbar.setTitle((CharSequence)(position + 1 + "/" + RvPreviewActivity.this.mImages.size()));
                    RvPreviewActivity.this.changeSelect((Image)RvPreviewActivity.this.mImages.get(position));
                }
            }
        });
    }

    private void setToolBarColor(@ColorInt int color2) {
        this.toolbar.setBackgroundColor(color2);
    }

    private void setBottomBarColor(@ColorInt int color2) {
        this.rlBottomBar.setBackgroundColor(color2);
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
        this.appBarLayout.postDelayed(new Runnable(){

            @Override
            public void run() {
                if (RvPreviewActivity.this.appBarLayout != null) {
                    ObjectAnimator animator = ObjectAnimator.ofFloat((Object)RvPreviewActivity.this.appBarLayout, (String)"translationY", (float[])new float[]{RvPreviewActivity.this.appBarLayout.getTranslationY(), 0.0f}).setDuration(300L);
                    animator.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter(){

                        public void onAnimationStart(Animator animation) {
                            super.onAnimationStart(animation);
                            if (RvPreviewActivity.this.appBarLayout != null) {
                                RvPreviewActivity.this.appBarLayout.setVisibility(0);
                            }
                        }
                    });
                    animator.start();
                    ObjectAnimator.ofFloat((Object)RvPreviewActivity.this.rlBottomBar, (String)"translationY", (float[])new float[]{RvPreviewActivity.this.rlBottomBar.getTranslationY(), 0.0f}).setDuration(300L).start();
                }
            }
        }, 100L);
    }

    private void hideBar() {
        this.isShowBar = false;
        ObjectAnimator animator = ObjectAnimator.ofFloat((Object)this.appBarLayout, (String)"translationY", (float[])new float[]{0.0f, -this.appBarLayout.getHeight()}).setDuration(300L);
        animator.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter(){

            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (RvPreviewActivity.this.appBarLayout != null) {
                    RvPreviewActivity.this.appBarLayout.setVisibility(8);
                    RvPreviewActivity.this.appBarLayout.postDelayed(new Runnable(){

                        @Override
                        @RequiresApi(api=16)
                        public void run() {
                            RvPreviewActivity.this.setStatusBarVisible(false);
                        }
                    }, 5L);
                }
            }
        });
        animator.start();
        ObjectAnimator.ofFloat((Object)this.rlBottomBar, (String)"translationY", (float[])new float[]{0.0f, this.rlBottomBar.getHeight()}).setDuration(300L).start();
    }

    private void clickSelect() {
        int position = this.linearLayoutManager.findFirstVisibleItemPosition();
        if (this.mImages != null && this.mImages.size() > position) {
            Image image = this.mImages.get(position);
            if (this.mSelectImages.contains(image)) {
                this.mSelectImages.remove(image);
            } else if (this.isSingle) {
                this.mSelectImages.clear();
                this.mSelectImages.add(image);
            } else if (this.mMaxCount <= 0 || this.mSelectImages.size() < this.mMaxCount) {
                this.mSelectImages.add(image);
            } else {
                Toast.makeText((Context)this, (CharSequence)("最多只能选" + this.mMaxCount + "张"), (int)0).show();
            }
            this.bottomPreviewAdapter.referesh(this.mSelectImages);
            this.bottomPreviewAdapter.notifyDataSetChanged();
            this.changeSelect(image);
        }
        if (this.mSelectImages.size() > 0) {
            this.bottomRecycleview.setVisibility(0);
            this.line.setVisibility(0);
        } else {
            this.bottomRecycleview.setVisibility(8);
            this.line.setVisibility(8);
        }
    }

    private void changeSelect(Image image) {
        this.tvSelect.setCompoundDrawables((Drawable)(this.mSelectImages.contains(image) ? this.mSelectDrawable : this.mUnSelectDrawable), null, null, null);
        this.setSelectImageCount(this.mSelectImages.size());
        for (Image image1 : this.mSelectImages) {
            image1.setChecked(false);
        }
        image.setChecked(true);
        this.bottomPreviewAdapter.referesh(this.mSelectImages);
        this.bottomPreviewAdapter.notifyDataSetChanged();
        if (this.mSelectImages.contains(image)) {
            this.bottomRecycleview.smoothScrollToPosition(image.getSelectPosition());
        }
    }

    private void setSelectImageCount(int count) {
        if (count == 0) {
            this.btnConfirm.setEnabled(false);
            this.tvConfirm.setText((CharSequence)this.getString(R.string.confirm));
        } else {
            this.btnConfirm.setEnabled(true);
            if (this.isSingle) {
                this.tvConfirm.setText((CharSequence)this.getString(R.string.confirm));
            } else if (this.mMaxCount > 0) {
                this.tvConfirm.setText((CharSequence)this.getString(R.string.confirm_maxcount, new Object[]{count, this.mMaxCount}));
            } else {
                this.tvConfirm.setText((CharSequence)this.getString(R.string.confirm_count, new Object[]{count}));
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

