/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.animation.TimeInterpolator
 *  android.annotation.TargetApi
 *  android.content.Context
 *  android.content.Intent
 *  android.graphics.Bitmap$CompressFormat
 *  android.graphics.PorterDuff$Mode
 *  android.graphics.drawable.Animatable
 *  android.graphics.drawable.Drawable
 *  android.net.Uri
 *  android.os.Build$VERSION
 *  android.os.Bundle
 *  android.os.Parcelable
 *  android.text.TextUtils
 *  android.util.Log
 *  android.view.Menu
 *  android.view.MenuItem
 *  android.view.View
 *  android.view.View$OnClickListener
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.view.Window
 *  android.view.animation.AccelerateInterpolator
 *  android.widget.FrameLayout
 *  android.widget.ImageView
 *  android.widget.LinearLayout
 *  android.widget.LinearLayout$LayoutParams
 *  android.widget.RelativeLayout
 *  android.widget.RelativeLayout$LayoutParams
 *  android.widget.TextView
 *  androidx.annotation.ColorInt
 *  androidx.annotation.DrawableRes
 *  androidx.annotation.IdRes
 *  androidx.annotation.NonNull
 *  androidx.appcompat.app.ActionBar
 *  androidx.appcompat.app.AppCompatActivity
 *  androidx.appcompat.widget.Toolbar
 *  androidx.core.content.ContextCompat
 *  com.kook.librelease.R$color
 *  com.kook.librelease.R$dimen
 *  com.kook.librelease.R$drawable
 *  com.kook.librelease.R$id
 *  com.kook.librelease.R$layout
 *  com.kook.librelease.R$menu
 *  com.kook.librelease.R$string
 */
package com.carlos.common.imagepicker;

import android.animation.TimeInterpolator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import com.carlos.common.imagepicker.callback.BitmapCropCallback;
import com.carlos.common.imagepicker.entity.AspectRatio;
import com.carlos.common.imagepicker.util.SelectedStateListDrawable;
import com.carlos.common.imagepicker.view.GestureCropImageView;
import com.carlos.common.imagepicker.view.OverlayView;
import com.carlos.common.imagepicker.view.TransformImageView;
import com.carlos.common.imagepicker.view.UCropView;
import com.carlos.common.imagepicker.view.widget.AspectRatioTextView;
import com.carlos.common.imagepicker.view.widget.HorizontalProgressWheelView;
import com.kook.librelease.R;
import com.kook.librelease.StringFog;
import java.io.Serializable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class UCropActivity
extends AppCompatActivity {
    public static final int DEFAULT_COMPRESS_QUALITY = 90;
    public static final Bitmap.CompressFormat DEFAULT_COMPRESS_FORMAT;
    public static final int NONE = 0;
    public static final int SCALE = 1;
    public static final int ROTATE = 2;
    public static final int ALL = 3;
    private static final String TAG;
    private static final int TABS_COUNT = 3;
    private static final int SCALE_WIDGET_SENSITIVITY_COEFFICIENT = 15000;
    private static final int ROTATE_WIDGET_SENSITIVITY_COEFFICIENT = 42;
    private String mToolbarTitle;
    private int mToolbarColor;
    private int mStatusBarColor;
    private int mActiveWidgetColor;
    private int mToolbarWidgetColor;
    @ColorInt
    private int mRootViewBackgroundColor;
    @DrawableRes
    private int mToolbarCancelDrawable;
    @DrawableRes
    private int mToolbarCropDrawable;
    private int mLogoColor;
    private boolean mShowBottomControls;
    private boolean mShowLoader = true;
    private UCropView mUCropView;
    private GestureCropImageView mGestureCropImageView;
    private OverlayView mOverlayView;
    private ViewGroup mWrapperStateAspectRatio;
    private ViewGroup mWrapperStateRotate;
    private ViewGroup mWrapperStateScale;
    private ViewGroup mLayoutAspectRatio;
    private ViewGroup mLayoutRotate;
    private ViewGroup mLayoutScale;
    private List<ViewGroup> mCropAspectRatioViews = new ArrayList<ViewGroup>();
    private TextView mTextViewRotateAngle;
    private TextView mTextViewScalePercent;
    private View mBlockingView;
    private Bitmap.CompressFormat mCompressFormat = DEFAULT_COMPRESS_FORMAT;
    private int mCompressQuality = 90;
    private int[] mAllowedGestures = new int[]{1, 2, 3};
    private TransformImageView.TransformImageListener mImageListener = new TransformImageView.TransformImageListener(){

        @Override
        public void onRotate(float currentAngle) {
            UCropActivity.this.setAngleText(currentAngle);
        }

        @Override
        public void onScale(float currentScale) {
            UCropActivity.this.setScaleText(currentScale);
        }

        @Override
        public void onLoadComplete() {
            UCropActivity.this.mUCropView.animate().alpha(1.0f).setDuration(300L).setInterpolator((TimeInterpolator)new AccelerateInterpolator());
            UCropActivity.this.mBlockingView.setClickable(false);
            UCropActivity.this.mShowLoader = false;
            UCropActivity.this.supportInvalidateOptionsMenu();
        }

        @Override
        public void onLoadFailure(@NonNull Exception e) {
            UCropActivity.this.setResultError(e);
            UCropActivity.this.finish();
        }
    };
    private final View.OnClickListener mStateClickListener = new View.OnClickListener(){

        public void onClick(View v) {
            if (!v.isSelected()) {
                UCropActivity.this.setWidgetState(v.getId());
            }
        }
    };

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.ucrop_activity_photobox);
        Intent intent = this.getIntent();
        this.setupViews(intent);
        this.setImageData(intent);
        this.setInitialState();
        this.addBlockingView();
    }

    public boolean onCreateOptionsMenu(Menu menu2) {
        this.getMenuInflater().inflate(R.menu.ucrop_menu_activity, menu2);
        MenuItem menuItemLoader = menu2.findItem(R.id.menu_loader);
        Drawable menuItemLoaderIcon = menuItemLoader.getIcon();
        if (menuItemLoaderIcon != null) {
            try {
                menuItemLoaderIcon.mutate();
                menuItemLoaderIcon.setColorFilter(this.mToolbarWidgetColor, PorterDuff.Mode.SRC_ATOP);
                menuItemLoader.setIcon(menuItemLoaderIcon);
            }
            catch (IllegalStateException e) {
                Log.i((String)TAG, (String)String.format("%s - %s", e.getMessage(), this.getString(R.string.ucrop_mutate_exception_hint)));
            }
            ((Animatable)menuItemLoader.getIcon()).start();
        }
        MenuItem menuItemCrop = menu2.findItem(R.id.menu_crop);
        Drawable menuItemCropIcon = ContextCompat.getDrawable((Context)this, (int)this.mToolbarCropDrawable);
        if (menuItemCropIcon != null) {
            menuItemCropIcon.mutate();
            menuItemCropIcon.setColorFilter(this.mToolbarWidgetColor, PorterDuff.Mode.SRC_ATOP);
            menuItemCrop.setIcon(menuItemCropIcon);
        }
        return true;
    }

    public boolean onPrepareOptionsMenu(Menu menu2) {
        menu2.findItem(R.id.menu_crop).setVisible(!this.mShowLoader);
        menu2.findItem(R.id.menu_loader).setVisible(this.mShowLoader);
        return super.onPrepareOptionsMenu(menu2);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_crop) {
            this.cropAndSaveImage();
        } else if (item.getItemId() == 16908332) {
            this.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onStop() {
        super.onStop();
        if (this.mGestureCropImageView != null) {
            this.mGestureCropImageView.cancelAllAnimations();
        }
    }

    private void setImageData(@NonNull Intent intent) {
        Uri inputUri = (Uri)intent.getParcelableExtra("com.carlos.multiapp.InputUri");
        Uri outputUri = (Uri)intent.getParcelableExtra("com.carlos.multiapp.OutputUri");
        this.processOptions(intent);
        if (inputUri != null && outputUri != null) {
            try {
                this.mGestureCropImageView.setImageUri(inputUri, outputUri);
            }
            catch (Exception e) {
                this.setResultError(e);
                this.finish();
            }
        } else {
            this.setResultError(new NullPointerException(this.getString(R.string.ucrop_error_input_data_is_absent)));
            this.finish();
        }
    }

    private void processOptions(@NonNull Intent intent) {
        String compressionFormatName = intent.getStringExtra("com.carlos.multiapp.CompressionFormatName");
        Bitmap.CompressFormat compressFormat = null;
        if (!TextUtils.isEmpty((CharSequence)compressionFormatName)) {
            compressFormat = Bitmap.CompressFormat.valueOf((String)compressionFormatName);
        }
        this.mCompressFormat = compressFormat == null ? DEFAULT_COMPRESS_FORMAT : compressFormat;
        this.mCompressQuality = intent.getIntExtra("com.carlos.multiapp.CompressionQuality", 90);
        int[] allowedGestures = intent.getIntArrayExtra("com.carlos.multiapp.AllowedGestures");
        if (allowedGestures != null && allowedGestures.length == 3) {
            this.mAllowedGestures = allowedGestures;
        }
        this.mGestureCropImageView.setMaxBitmapSize(intent.getIntExtra("com.carlos.multiapp.MaxBitmapSize", 0));
        this.mGestureCropImageView.setMaxScaleMultiplier(intent.getFloatExtra("com.carlos.multiapp.MaxScaleMultiplier", 10.0f));
        this.mGestureCropImageView.setImageToWrapCropBoundsAnimDuration(intent.getIntExtra("com.carlos.multiapp.ImageToCropBoundsAnimDuration", 500));
        this.mOverlayView.setFreestyleCropEnabled(intent.getBooleanExtra("com.carlos.multiapp.FreeStyleCrop", false));
        this.mOverlayView.setDimmedColor(intent.getIntExtra("com.carlos.multiapp.DimmedLayerColor", this.getResources().getColor(R.color.ucrop_color_default_dimmed)));
        this.mOverlayView.setCircleDimmedLayer(intent.getBooleanExtra("com.carlos.multiapp.CircleDimmedLayer", false));
        this.mOverlayView.setShowCropFrame(intent.getBooleanExtra("com.carlos.multiapp.ShowCropFrame", true));
        this.mOverlayView.setCropFrameColor(intent.getIntExtra("com.carlos.multiapp.CropFrameColor", this.getResources().getColor(R.color.ucrop_color_default_crop_frame)));
        this.mOverlayView.setCropFrameStrokeWidth(intent.getIntExtra("com.carlos.multiapp.CropFrameStrokeWidth", this.getResources().getDimensionPixelSize(R.dimen.ucrop_default_crop_frame_stoke_width)));
        this.mOverlayView.setShowCropGrid(intent.getBooleanExtra("com.carlos.multiapp.ShowCropGrid", true));
        this.mOverlayView.setCropGridRowCount(intent.getIntExtra("com.carlos.multiapp.CropGridRowCount", 2));
        this.mOverlayView.setCropGridColumnCount(intent.getIntExtra("com.carlos.multiapp.CropGridColumnCount", 2));
        this.mOverlayView.setCropGridColor(intent.getIntExtra("com.carlos.multiapp.CropGridColor", this.getResources().getColor(R.color.ucrop_color_default_crop_grid)));
        this.mOverlayView.setCropGridStrokeWidth(intent.getIntExtra("com.carlos.multiapp.CropGridStrokeWidth", this.getResources().getDimensionPixelSize(R.dimen.ucrop_default_crop_grid_stoke_width)));
        float aspectRatioX = intent.getFloatExtra("com.carlos.multiapp.AspectRatioX", 0.0f);
        float aspectRatioY = intent.getFloatExtra("com.carlos.multiapp.AspectRatioY", 0.0f);
        int aspectRationSelectedByDefault = intent.getIntExtra("com.carlos.multiapp.AspectRatioSelectedByDefault", 0);
        ArrayList aspectRatioList = intent.getParcelableArrayListExtra("com.carlos.multiapp.AspectRatioOptions");
        Log.i((String)"VirtualApp", (String)"setTargetAspectRatio  init kook");
        this.mGestureCropImageView.setTargetAspectRatio(1.0f);
        int maxSizeX = intent.getIntExtra("com.carlos.multiapp.MaxSizeX", 0);
        int maxSizeY = intent.getIntExtra("com.carlos.multiapp.MaxSizeY", 0);
        if (maxSizeX > 0 && maxSizeY > 0) {
            this.mGestureCropImageView.setMaxResultImageSizeX(maxSizeX);
            this.mGestureCropImageView.setMaxResultImageSizeY(maxSizeY);
        }
    }

    private void setupViews(@NonNull Intent intent) {
        this.mStatusBarColor = intent.getIntExtra("com.carlos.multiapp.StatusBarColor", ContextCompat.getColor((Context)this, (int)R.color.ucrop_color_statusbar));
        this.mToolbarColor = intent.getIntExtra("com.carlos.multiapp.ToolbarColor", ContextCompat.getColor((Context)this, (int)R.color.ucrop_color_toolbar));
        this.mActiveWidgetColor = intent.getIntExtra("com.carlos.multiapp.UcropColorWidgetActive", ContextCompat.getColor((Context)this, (int)R.color.ucrop_color_widget_active));
        this.mToolbarWidgetColor = intent.getIntExtra("com.carlos.multiapp.UcropToolbarWidgetColor", ContextCompat.getColor((Context)this, (int)R.color.ucrop_color_toolbar_widget));
        this.mToolbarCancelDrawable = intent.getIntExtra("com.carlos.multiapp.UcropToolbarCancelDrawable", R.drawable.ucrop_ic_cross);
        this.mToolbarCropDrawable = intent.getIntExtra("com.carlos.multiapp.UcropToolbarCropDrawable", R.drawable.ucrop_ic_done);
        this.mToolbarTitle = intent.getStringExtra("com.carlos.multiapp.UcropToolbarTitleText");
        this.mToolbarTitle = this.mToolbarTitle != null ? this.mToolbarTitle : this.getResources().getString(R.string.ucrop_label_edit_photo);
        this.mLogoColor = intent.getIntExtra("com.carlos.multiapp.UcropLogoColor", ContextCompat.getColor((Context)this, (int)R.color.ucrop_color_default_logo));
        this.mShowBottomControls = !intent.getBooleanExtra("com.carlos.multiapp.HideBottomControls", false);
        this.mRootViewBackgroundColor = intent.getIntExtra("com.carlos.multiapp.UcropRootViewBackgroundColor", ContextCompat.getColor((Context)this, (int)R.color.ucrop_color_crop_background));
        this.setupAppBar();
        this.initiateRootViews();
        if (this.mShowBottomControls) {
            ViewGroup photoBox = (ViewGroup)this.findViewById(R.id.ucrop_photobox);
            View.inflate((Context)this, (int)R.layout.ucrop_controls, (ViewGroup)photoBox);
            this.mWrapperStateAspectRatio = (ViewGroup)this.findViewById(R.id.state_aspect_ratio);
            this.mWrapperStateAspectRatio.setOnClickListener(this.mStateClickListener);
            this.mWrapperStateRotate = (ViewGroup)this.findViewById(R.id.state_rotate);
            this.mWrapperStateRotate.setOnClickListener(this.mStateClickListener);
            this.mWrapperStateScale = (ViewGroup)this.findViewById(R.id.state_scale);
            this.mWrapperStateScale.setOnClickListener(this.mStateClickListener);
            this.mLayoutAspectRatio = (ViewGroup)this.findViewById(R.id.layout_aspect_ratio);
            this.mLayoutRotate = (ViewGroup)this.findViewById(R.id.layout_rotate_wheel);
            this.mLayoutScale = (ViewGroup)this.findViewById(R.id.layout_scale_wheel);
            this.setupAspectRatioWidget(intent);
            this.setupRotateWidget();
            this.setupScaleWidget();
            this.setupStatesWrapper();
        }
    }

    private void setupAppBar() {
        this.setStatusBarColor(this.mStatusBarColor);
        Toolbar toolbar = (Toolbar)this.findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(this.mToolbarColor);
        toolbar.setTitleTextColor(this.mToolbarWidgetColor);
        TextView toolbarTitle = (TextView)toolbar.findViewById(R.id.toolbar_title);
        toolbarTitle.setTextColor(this.mToolbarWidgetColor);
        toolbarTitle.setText((CharSequence)this.mToolbarTitle);
        Drawable stateButtonDrawable = ContextCompat.getDrawable((Context)this, (int)this.mToolbarCancelDrawable).mutate();
        stateButtonDrawable.setColorFilter(this.mToolbarWidgetColor, PorterDuff.Mode.SRC_ATOP);
        toolbar.setNavigationIcon(stateButtonDrawable);
        this.setSupportActionBar(toolbar);
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    private void initiateRootViews() {
        this.mUCropView = (UCropView)this.findViewById(R.id.ucrop);
        this.mGestureCropImageView = this.mUCropView.getCropImageView();
        this.mOverlayView = this.mUCropView.getOverlayView();
        this.mGestureCropImageView.setTransformImageListener(this.mImageListener);
        ((ImageView)this.findViewById(R.id.image_view_logo)).setColorFilter(this.mLogoColor, PorterDuff.Mode.SRC_ATOP);
        this.findViewById(R.id.ucrop_frame).setBackgroundColor(this.mRootViewBackgroundColor);
    }

    private void setupStatesWrapper() {
        ImageView stateScaleImageView = (ImageView)this.findViewById(R.id.image_view_state_scale);
        ImageView stateRotateImageView = (ImageView)this.findViewById(R.id.image_view_state_rotate);
        ImageView stateAspectRatioImageView = (ImageView)this.findViewById(R.id.image_view_state_aspect_ratio);
        stateScaleImageView.setImageDrawable((Drawable)new SelectedStateListDrawable(stateScaleImageView.getDrawable(), this.mActiveWidgetColor));
        stateRotateImageView.setImageDrawable((Drawable)new SelectedStateListDrawable(stateRotateImageView.getDrawable(), this.mActiveWidgetColor));
        stateAspectRatioImageView.setImageDrawable((Drawable)new SelectedStateListDrawable(stateAspectRatioImageView.getDrawable(), this.mActiveWidgetColor));
    }

    @TargetApi(value=21)
    private void setStatusBarColor(@ColorInt int color2) {
        Window window;
        if (Build.VERSION.SDK_INT >= 21 && (window = this.getWindow()) != null) {
            window.addFlags(Integer.MIN_VALUE);
            window.setStatusBarColor(color2);
        }
    }

    private void setupAspectRatioWidget(@NonNull Intent intent) {
        int aspectRationSelectedByDefault = intent.getIntExtra("com.carlos.multiapp.AspectRatioSelectedByDefault", 0);
        ArrayList<AspectRatio> aspectRatioList = intent.getParcelableArrayListExtra("com.carlos.multiapp.AspectRatioOptions");
        if (aspectRatioList == null || aspectRatioList.isEmpty()) {
            aspectRationSelectedByDefault = 0;
            aspectRatioList = new ArrayList<AspectRatio>();
            aspectRatioList.add(new AspectRatio(null, 1.0f, 1.0f));
            aspectRatioList.add(new AspectRatio(null, 3.0f, 4.0f));
            aspectRatioList.add(new AspectRatio(this.getString(R.string.ucrop_label_original).toUpperCase(), 0.0f, 0.0f));
            aspectRatioList.add(new AspectRatio(null, 3.0f, 2.0f));
            aspectRatioList.add(new AspectRatio(null, 16.0f, 9.0f));
        }
        LinearLayout wrapperAspectRatioList = (LinearLayout)this.findViewById(R.id.layout_aspect_ratio);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, -1);
        lp.weight = 1.0f;
        for (AspectRatio aspectRatio : aspectRatioList) {
            FrameLayout wrapperAspectRatio = (FrameLayout)this.getLayoutInflater().inflate(R.layout.ucrop_aspect_ratio, null);
            wrapperAspectRatio.setLayoutParams((ViewGroup.LayoutParams)lp);
            AspectRatioTextView aspectRatioTextView = (AspectRatioTextView)wrapperAspectRatio.getChildAt(0);
            aspectRatioTextView.setActiveColor(this.mActiveWidgetColor);
            aspectRatioTextView.setAspectRatio(aspectRatio);
            wrapperAspectRatioList.addView((View)wrapperAspectRatio);
            this.mCropAspectRatioViews.add((ViewGroup)wrapperAspectRatio);
        }
        this.mCropAspectRatioViews.get(aspectRationSelectedByDefault).setSelected(true);
        for (ViewGroup cropAspectRatioView : this.mCropAspectRatioViews) {
            cropAspectRatioView.setOnClickListener(new View.OnClickListener(){

                public void onClick(View v) {
                    UCropActivity.this.mGestureCropImageView.setTargetAspectRatio(((AspectRatioTextView)((ViewGroup)v).getChildAt(0)).getAspectRatio(v.isSelected()));
                    UCropActivity.this.mGestureCropImageView.setImageToWrapCropBounds();
                    if (!v.isSelected()) {
                        Iterator var2 = UCropActivity.this.mCropAspectRatioViews.iterator();

                        while(var2.hasNext()) {
                            ViewGroup cropAspectRatioView = (ViewGroup)var2.next();
                            cropAspectRatioView.setSelected(cropAspectRatioView == v);
                        }
                    }
                }
            });
        }
    }

    private void setupRotateWidget() {
        this.mTextViewRotateAngle = (TextView)this.findViewById(R.id.text_view_rotate);
        ((HorizontalProgressWheelView)this.findViewById(R.id.rotate_scroll_wheel)).setScrollingListener(new HorizontalProgressWheelView.ScrollingListener(){

            @Override
            public void onScroll(float delta, float totalDistance) {
                UCropActivity.this.mGestureCropImageView.postRotate(delta / 42.0f);
            }

            @Override
            public void onScrollEnd() {
                UCropActivity.this.mGestureCropImageView.setImageToWrapCropBounds();
            }

            @Override
            public void onScrollStart() {
                UCropActivity.this.mGestureCropImageView.cancelAllAnimations();
            }
        });
        ((HorizontalProgressWheelView)this.findViewById(R.id.rotate_scroll_wheel)).setMiddleLineColor(this.mActiveWidgetColor);
        this.findViewById(R.id.wrapper_reset_rotate).setOnClickListener(new View.OnClickListener(){

            public void onClick(View v) {
                UCropActivity.this.resetRotation();
            }
        });
        this.findViewById(R.id.wrapper_rotate_by_angle).setOnClickListener(new View.OnClickListener(){

            public void onClick(View v) {
                UCropActivity.this.rotateByAngle(90);
            }
        });
    }

    private void setupScaleWidget() {
        this.mTextViewScalePercent = (TextView)this.findViewById(R.id.text_view_scale);
        ((HorizontalProgressWheelView)this.findViewById(R.id.scale_scroll_wheel)).setScrollingListener(new HorizontalProgressWheelView.ScrollingListener(){

            @Override
            public void onScroll(float delta, float totalDistance) {
                if (delta > 0.0f) {
                    UCropActivity.this.mGestureCropImageView.zoomInImage(UCropActivity.this.mGestureCropImageView.getCurrentScale() + delta * ((UCropActivity.this.mGestureCropImageView.getMaxScale() - UCropActivity.this.mGestureCropImageView.getMinScale()) / 15000.0f));
                } else {
                    UCropActivity.this.mGestureCropImageView.zoomOutImage(UCropActivity.this.mGestureCropImageView.getCurrentScale() + delta * ((UCropActivity.this.mGestureCropImageView.getMaxScale() - UCropActivity.this.mGestureCropImageView.getMinScale()) / 15000.0f));
                }
            }

            @Override
            public void onScrollEnd() {
                UCropActivity.this.mGestureCropImageView.setImageToWrapCropBounds();
            }

            @Override
            public void onScrollStart() {
                UCropActivity.this.mGestureCropImageView.cancelAllAnimations();
            }
        });
        ((HorizontalProgressWheelView)this.findViewById(R.id.scale_scroll_wheel)).setMiddleLineColor(this.mActiveWidgetColor);
    }

    private void setAngleText(float angle) {
        if (this.mTextViewRotateAngle != null) {
            this.mTextViewRotateAngle.setText((CharSequence)String.format(Locale.getDefault(), "%.1f°", Float.valueOf(angle)));
        }
    }

    private void setScaleText(float scale) {
        if (this.mTextViewScalePercent != null) {
            this.mTextViewScalePercent.setText((CharSequence)String.format(Locale.getDefault(), "%d%%", (int)(scale * 100.0f)));
        }
    }

    private void resetRotation() {
        this.mGestureCropImageView.postRotate(-this.mGestureCropImageView.getCurrentAngle());
        this.mGestureCropImageView.setImageToWrapCropBounds();
    }

    private void rotateByAngle(int angle) {
        this.mGestureCropImageView.postRotate(angle);
        this.mGestureCropImageView.setImageToWrapCropBounds();
    }

    private void setInitialState() {
        if (this.mShowBottomControls) {
            if (this.mWrapperStateAspectRatio.getVisibility() == 0) {
                this.setWidgetState(R.id.state_aspect_ratio);
            } else {
                this.setWidgetState(R.id.state_scale);
            }
        } else {
            this.setAllowedGestures(0);
        }
    }

    private void setWidgetState(@IdRes int stateViewId) {
        if (!this.mShowBottomControls) {
            return;
        }
        this.mWrapperStateAspectRatio.setSelected(stateViewId == R.id.state_aspect_ratio);
        this.mWrapperStateRotate.setSelected(stateViewId == R.id.state_rotate);
        this.mWrapperStateScale.setSelected(stateViewId == R.id.state_scale);
        this.mLayoutAspectRatio.setVisibility(stateViewId == R.id.state_aspect_ratio ? 0 : 8);
        this.mLayoutRotate.setVisibility(stateViewId == R.id.state_rotate ? 0 : 8);
        this.mLayoutScale.setVisibility(stateViewId == R.id.state_scale ? 0 : 8);
        if (stateViewId == R.id.state_scale) {
            this.setAllowedGestures(0);
        } else if (stateViewId == R.id.state_rotate) {
            this.setAllowedGestures(1);
        } else {
            this.setAllowedGestures(2);
        }
    }

    private void setAllowedGestures(int tab) {
        this.mGestureCropImageView.setScaleEnabled(this.mAllowedGestures[tab] == 3 || this.mAllowedGestures[tab] == 1);
        this.mGestureCropImageView.setRotateEnabled(this.mAllowedGestures[tab] == 3 || this.mAllowedGestures[tab] == 2);
    }

    private void addBlockingView() {
        if (this.mBlockingView == null) {
            this.mBlockingView = new View((Context)this);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(-1, -1);
            lp.addRule(3, R.id.toolbar);
            this.mBlockingView.setLayoutParams((ViewGroup.LayoutParams)lp);
            this.mBlockingView.setClickable(true);
        }
        ((RelativeLayout)this.findViewById(R.id.ucrop_photobox)).addView(this.mBlockingView);
    }

    protected void cropAndSaveImage() {
        this.mBlockingView.setClickable(true);
        this.mShowLoader = true;
        this.supportInvalidateOptionsMenu();
        this.mGestureCropImageView.cropAndSaveImage(this.mCompressFormat, this.mCompressQuality, new BitmapCropCallback(){

            @Override
            public void onBitmapCropped(@NonNull Uri resultUri, int offsetX, int offsetY, int imageWidth, int imageHeight) {
                Log.i((String)"VirtualApp", (String)("resultUri:" + resultUri));
                Log.i((String)"VirtualApp", (String)("offsetX:" + offsetX + "  offsetY:" + offsetY + "    imageWidth:" + imageWidth + "    imageHeight:" + imageHeight));
                UCropActivity.this.setResultUri(resultUri, UCropActivity.this.mGestureCropImageView.getTargetAspectRatio(), offsetX, offsetY, imageWidth, imageHeight);
                UCropActivity.this.finish();
            }

            @Override
            public void onCropFailure(@NonNull Throwable t) {
                UCropActivity.this.setResultError(t);
                UCropActivity.this.finish();
            }
        });
    }

    protected void setResultUri(Uri uri, float resultAspectRatio, int offsetX, int offsetY, int imageWidth, int imageHeight) {
        this.setResult(-1, this.getIntent().putExtra("com.carlos.multiapp.OutputUri", (Parcelable)uri).putExtra("com.carlos.multiapp.CropAspectRatio", resultAspectRatio).putExtra("com.carlos.multiapp.ImageWidth", imageWidth).putExtra("com.carlos.multiapp.ImageHeight", imageHeight).putExtra("com.carlos.multiapp.OffsetX", offsetX).putExtra("com.carlos.multiapp.OffsetY", offsetY));
    }

    protected void setResultError(Throwable throwable) {
        this.setResult(96, new Intent().putExtra("com.carlos.multiapp.Error", (Serializable)throwable));
    }

    static {
        TAG = "UCropActivity";
        DEFAULT_COMPRESS_FORMAT = Bitmap.CompressFormat.JPEG;
    }

    @Retention(value=RetentionPolicy.SOURCE)
    public static @interface GestureTypes {
    }
}

