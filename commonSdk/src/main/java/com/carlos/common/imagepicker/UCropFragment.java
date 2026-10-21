/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.animation.TimeInterpolator
 *  android.content.Context
 *  android.content.Intent
 *  android.graphics.Bitmap$CompressFormat
 *  android.graphics.PorterDuff$Mode
 *  android.graphics.drawable.Drawable
 *  android.net.Uri
 *  android.os.Bundle
 *  android.os.Parcelable
 *  android.text.TextUtils
 *  android.view.LayoutInflater
 *  android.view.View
 *  android.view.View$OnClickListener
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.view.animation.AccelerateInterpolator
 *  android.widget.FrameLayout
 *  android.widget.ImageView
 *  android.widget.LinearLayout
 *  android.widget.LinearLayout$LayoutParams
 *  android.widget.RelativeLayout
 *  android.widget.RelativeLayout$LayoutParams
 *  android.widget.TextView
 *  androidx.annotation.ColorInt
 *  androidx.annotation.IdRes
 *  androidx.annotation.NonNull
 *  androidx.annotation.Nullable
 *  androidx.core.content.ContextCompat
 *  androidx.fragment.app.Fragment
 *  com.kook.librelease.R$color
 *  com.kook.librelease.R$dimen
 *  com.kook.librelease.R$id
 *  com.kook.librelease.R$layout
 *  com.kook.librelease.R$string
 */
package com.carlos.common.imagepicker;

import android.animation.TimeInterpolator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.ColorInt;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.carlos.common.imagepicker.UCropFragmentCallback;
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

public class UCropFragment
extends Fragment {
    public static final int DEFAULT_COMPRESS_QUALITY = 90;
    public static final Bitmap.CompressFormat DEFAULT_COMPRESS_FORMAT;
    public static final int NONE = 0;
    public static final int SCALE = 1;
    public static final int ROTATE = 2;
    public static final int ALL = 3;
    public static final String TAG;
    private static final int TABS_COUNT = 3;
    private static final int SCALE_WIDGET_SENSITIVITY_COEFFICIENT = 15000;
    private static final int ROTATE_WIDGET_SENSITIVITY_COEFFICIENT = 42;
    private UCropFragmentCallback callback;
    private int mActiveWidgetColor;
    @ColorInt
    private int mRootViewBackgroundColor;
    private int mLogoColor;
    private boolean mShowBottomControls;
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
            UCropFragment.this.setAngleText(currentAngle);
        }

        @Override
        public void onScale(float currentScale) {
            UCropFragment.this.setScaleText(currentScale);
        }

        @Override
        public void onLoadComplete() {
            UCropFragment.this.mUCropView.animate().alpha(1.0f).setDuration(300L).setInterpolator((TimeInterpolator)new AccelerateInterpolator());
            UCropFragment.this.mBlockingView.setClickable(false);
            UCropFragment.this.callback.loadingProgress(false);
        }

        @Override
        public void onLoadFailure(@NonNull Exception e) {
            UCropFragment.this.callback.onCropFinish(UCropFragment.this.getError(e));
        }
    };
    private final View.OnClickListener mStateClickListener = new View.OnClickListener(){

        public void onClick(View v) {
            if (!v.isSelected()) {
                UCropFragment.this.setWidgetState(v.getId());
            }
        }
    };

    public static UCropFragment newInstance(Bundle uCrop) {
        UCropFragment fragment = new UCropFragment();
        fragment.setArguments(uCrop);
        return fragment;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            this.callback = (UCropFragmentCallback)context;
        }
        catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement UCropFragmentCallback");
        }
    }

    public void setCallback(UCropFragmentCallback callback) {
        this.callback = callback;
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.ucrop_fragment_photobox, container, false);
        Bundle args = this.getArguments();
        this.setupViews(rootView, args);
        this.setImageData(args);
        this.setInitialState();
        this.addBlockingView(rootView);
        return rootView;
    }

    public void setupViews(View view, Bundle args) {
        this.mActiveWidgetColor = args.getInt("com.carlos.multiapp.UcropColorWidgetActive", ContextCompat.getColor((Context)this.getContext(), (int)R.color.ucrop_color_widget_active));
        this.mLogoColor = args.getInt("com.carlos.multiapp.UcropLogoColor", ContextCompat.getColor((Context)this.getContext(), (int)R.color.ucrop_color_default_logo));
        this.mShowBottomControls = !args.getBoolean("com.carlos.multiapp.HideBottomControls", false);
        this.mRootViewBackgroundColor = args.getInt("com.carlos.multiapp.UcropRootViewBackgroundColor", ContextCompat.getColor((Context)this.getContext(), (int)R.color.ucrop_color_crop_background));
        this.initiateRootViews(view);
        this.callback.loadingProgress(true);
        if (this.mShowBottomControls) {
            ViewGroup photoBox = (ViewGroup)view.findViewById(R.id.ucrop_photobox);
            View.inflate((Context)this.getContext(), (int)R.layout.ucrop_controls, (ViewGroup)photoBox);
            this.mWrapperStateAspectRatio = (ViewGroup)view.findViewById(R.id.state_aspect_ratio);
            this.mWrapperStateAspectRatio.setOnClickListener(this.mStateClickListener);
            this.mWrapperStateRotate = (ViewGroup)view.findViewById(R.id.state_rotate);
            this.mWrapperStateRotate.setOnClickListener(this.mStateClickListener);
            this.mWrapperStateScale = (ViewGroup)view.findViewById(R.id.state_scale);
            this.mWrapperStateScale.setOnClickListener(this.mStateClickListener);
            this.mLayoutAspectRatio = (ViewGroup)view.findViewById(R.id.layout_aspect_ratio);
            this.mLayoutRotate = (ViewGroup)view.findViewById(R.id.layout_rotate_wheel);
            this.mLayoutScale = (ViewGroup)view.findViewById(R.id.layout_scale_wheel);
            this.setupAspectRatioWidget(args, view);
            this.setupRotateWidget(view);
            this.setupScaleWidget(view);
            this.setupStatesWrapper(view);
        }
    }

    private void setImageData(@NonNull Bundle bundle) {
        Uri inputUri = (Uri)bundle.getParcelable("com.carlos.multiapp.InputUri");
        Uri outputUri = (Uri)bundle.getParcelable("com.carlos.multiapp.OutputUri");
        this.processOptions(bundle);
        if (inputUri != null && outputUri != null) {
            try {
                this.mGestureCropImageView.setImageUri(inputUri, outputUri);
            }
            catch (Exception e) {
                this.callback.onCropFinish(this.getError(e));
            }
        } else {
            this.callback.onCropFinish(this.getError(new NullPointerException(this.getString(R.string.ucrop_error_input_data_is_absent))));
        }
    }

    private void processOptions(@NonNull Bundle bundle) {
        String compressionFormatName = bundle.getString("com.carlos.multiapp.CompressionFormatName");
        Bitmap.CompressFormat compressFormat = null;
        if (!TextUtils.isEmpty((CharSequence)compressionFormatName)) {
            compressFormat = Bitmap.CompressFormat.valueOf((String)compressionFormatName);
        }
        this.mCompressFormat = compressFormat == null ? DEFAULT_COMPRESS_FORMAT : compressFormat;
        this.mCompressQuality = bundle.getInt("com.carlos.multiapp.CompressionQuality", 90);
        int[] allowedGestures = bundle.getIntArray("com.carlos.multiapp.AllowedGestures");
        if (allowedGestures != null && allowedGestures.length == 3) {
            this.mAllowedGestures = allowedGestures;
        }
        this.mGestureCropImageView.setMaxBitmapSize(bundle.getInt("com.carlos.multiapp.MaxBitmapSize", 0));
        this.mGestureCropImageView.setMaxScaleMultiplier(bundle.getFloat("com.carlos.multiapp.MaxScaleMultiplier", 10.0f));
        this.mGestureCropImageView.setImageToWrapCropBoundsAnimDuration(bundle.getInt("com.carlos.multiapp.ImageToCropBoundsAnimDuration", 500));
        this.mOverlayView.setFreestyleCropEnabled(bundle.getBoolean("com.carlos.multiapp.FreeStyleCrop", false));
        this.mOverlayView.setDimmedColor(bundle.getInt("com.carlos.multiapp.DimmedLayerColor", this.getResources().getColor(R.color.ucrop_color_default_dimmed)));
        this.mOverlayView.setCircleDimmedLayer(bundle.getBoolean("com.carlos.multiapp.CircleDimmedLayer", false));
        this.mOverlayView.setShowCropFrame(bundle.getBoolean("com.carlos.multiapp.ShowCropFrame", true));
        this.mOverlayView.setCropFrameColor(bundle.getInt("com.carlos.multiapp.CropFrameColor", this.getResources().getColor(R.color.ucrop_color_default_crop_frame)));
        this.mOverlayView.setCropFrameStrokeWidth(bundle.getInt("com.carlos.multiapp.CropFrameStrokeWidth", this.getResources().getDimensionPixelSize(R.dimen.ucrop_default_crop_frame_stoke_width)));
        this.mOverlayView.setShowCropGrid(bundle.getBoolean("com.carlos.multiapp.ShowCropGrid", true));
        this.mOverlayView.setCropGridRowCount(bundle.getInt("com.carlos.multiapp.CropGridRowCount", 2));
        this.mOverlayView.setCropGridColumnCount(bundle.getInt("com.carlos.multiapp.CropGridColumnCount", 2));
        this.mOverlayView.setCropGridColor(bundle.getInt("com.carlos.multiapp.CropGridColor", this.getResources().getColor(R.color.ucrop_color_default_crop_grid)));
        this.mOverlayView.setCropGridStrokeWidth(bundle.getInt("com.carlos.multiapp.CropGridStrokeWidth", this.getResources().getDimensionPixelSize(R.dimen.ucrop_default_crop_grid_stoke_width)));
        float aspectRatioX = bundle.getFloat("com.carlos.multiapp.AspectRatioX", 0.0f);
        float aspectRatioY = bundle.getFloat("com.carlos.multiapp.AspectRatioY", 0.0f);
        int aspectRationSelectedByDefault = bundle.getInt("com.carlos.multiapp.AspectRatioSelectedByDefault", 0);
        ArrayList aspectRatioList = bundle.getParcelableArrayList("com.carlos.multiapp.AspectRatioOptions");
        if (aspectRatioX > 0.0f && aspectRatioY > 0.0f) {
            if (this.mWrapperStateAspectRatio != null) {
                this.mWrapperStateAspectRatio.setVisibility(View.GONE);
            }
            this.mGestureCropImageView.setTargetAspectRatio(aspectRatioX / aspectRatioY);
        } else if (aspectRatioList != null && aspectRationSelectedByDefault < aspectRatioList.size()) {
            this.mGestureCropImageView.setTargetAspectRatio(((AspectRatio)aspectRatioList.get(aspectRationSelectedByDefault)).getAspectRatioX() / ((AspectRatio)aspectRatioList.get(aspectRationSelectedByDefault)).getAspectRatioY());
        } else {
            this.mGestureCropImageView.setTargetAspectRatio(0.0f);
        }
        int maxSizeX = bundle.getInt("com.carlos.multiapp.MaxSizeX", 0);
        int maxSizeY = bundle.getInt("com.carlos.multiapp.MaxSizeY", 0);
        if (maxSizeX > 0 && maxSizeY > 0) {
            this.mGestureCropImageView.setMaxResultImageSizeX(maxSizeX);
            this.mGestureCropImageView.setMaxResultImageSizeY(maxSizeY);
        }
    }

    private void initiateRootViews(View view) {
        this.mUCropView = (UCropView)view.findViewById(R.id.ucrop);
        this.mGestureCropImageView = this.mUCropView.getCropImageView();
        this.mOverlayView = this.mUCropView.getOverlayView();
        this.mGestureCropImageView.setTransformImageListener(this.mImageListener);
        ((ImageView)view.findViewById(R.id.image_view_logo)).setColorFilter(this.mLogoColor, PorterDuff.Mode.SRC_ATOP);
        view.findViewById(R.id.ucrop_frame).setBackgroundColor(this.mRootViewBackgroundColor);
    }

    private void setupStatesWrapper(View view) {
        ImageView stateScaleImageView = (ImageView)view.findViewById(R.id.image_view_state_scale);
        ImageView stateRotateImageView = (ImageView)view.findViewById(R.id.image_view_state_rotate);
        ImageView stateAspectRatioImageView = (ImageView)view.findViewById(R.id.image_view_state_aspect_ratio);
        stateScaleImageView.setImageDrawable((Drawable)new SelectedStateListDrawable(stateScaleImageView.getDrawable(), this.mActiveWidgetColor));
        stateRotateImageView.setImageDrawable((Drawable)new SelectedStateListDrawable(stateRotateImageView.getDrawable(), this.mActiveWidgetColor));
        stateAspectRatioImageView.setImageDrawable((Drawable)new SelectedStateListDrawable(stateAspectRatioImageView.getDrawable(), this.mActiveWidgetColor));
    }

    private void setupAspectRatioWidget(@NonNull Bundle bundle, View view) {
        int aspectRationSelectedByDefault = bundle.getInt("com.carlos.multiapp.AspectRatioSelectedByDefault", 0);
        ArrayList<AspectRatio> aspectRatioList = bundle.getParcelableArrayList("com.carlos.multiapp.AspectRatioOptions");
        if (aspectRatioList == null || aspectRatioList.isEmpty()) {
            aspectRationSelectedByDefault = 2;
            aspectRatioList = new ArrayList<AspectRatio>();
            aspectRatioList.add(new AspectRatio(null, 1.0f, 1.0f));
            aspectRatioList.add(new AspectRatio(null, 3.0f, 4.0f));
            aspectRatioList.add(new AspectRatio(this.getString(R.string.ucrop_label_original).toUpperCase(), 0.0f, 0.0f));
            aspectRatioList.add(new AspectRatio(null, 3.0f, 2.0f));
            aspectRatioList.add(new AspectRatio(null, 16.0f, 9.0f));
        }
        LinearLayout wrapperAspectRatioList = (LinearLayout)view.findViewById(R.id.layout_aspect_ratio);
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
                    UCropFragment.this.mGestureCropImageView.setTargetAspectRatio(((AspectRatioTextView)((ViewGroup)v).getChildAt(0)).getAspectRatio(v.isSelected()));
                    UCropFragment.this.mGestureCropImageView.setImageToWrapCropBounds();
                    if (!v.isSelected()) {
                        Iterator var2 = UCropFragment.this.mCropAspectRatioViews.iterator();
                        while(var2.hasNext()) {
                            ViewGroup cropAspectRatioView = (ViewGroup)var2.next();
                            cropAspectRatioView.setSelected(cropAspectRatioView == v);
                        }
                    }
                }
            });
        }
    }

    private void setupRotateWidget(View view) {
        this.mTextViewRotateAngle = (TextView)view.findViewById(R.id.text_view_rotate);
        ((HorizontalProgressWheelView)view.findViewById(R.id.rotate_scroll_wheel)).setScrollingListener(new HorizontalProgressWheelView.ScrollingListener(){

            @Override
            public void onScroll(float delta, float totalDistance) {
                UCropFragment.this.mGestureCropImageView.postRotate(delta / 42.0f);
            }

            @Override
            public void onScrollEnd() {
                UCropFragment.this.mGestureCropImageView.setImageToWrapCropBounds();
            }

            @Override
            public void onScrollStart() {
                UCropFragment.this.mGestureCropImageView.cancelAllAnimations();
            }
        });
        ((HorizontalProgressWheelView)view.findViewById(R.id.rotate_scroll_wheel)).setMiddleLineColor(this.mActiveWidgetColor);
        view.findViewById(R.id.wrapper_reset_rotate).setOnClickListener(new View.OnClickListener(){

            public void onClick(View v) {
                UCropFragment.this.resetRotation();
            }
        });
        view.findViewById(R.id.wrapper_rotate_by_angle).setOnClickListener(new View.OnClickListener(){

            public void onClick(View v) {
                UCropFragment.this.rotateByAngle(90);
            }
        });
    }

    private void setupScaleWidget(View view) {
        this.mTextViewScalePercent = (TextView)view.findViewById(R.id.text_view_scale);
        ((HorizontalProgressWheelView)view.findViewById(R.id.scale_scroll_wheel)).setScrollingListener(new HorizontalProgressWheelView.ScrollingListener(){

            @Override
            public void onScroll(float delta, float totalDistance) {
                if (delta > 0.0f) {
                    UCropFragment.this.mGestureCropImageView.zoomInImage(UCropFragment.this.mGestureCropImageView.getCurrentScale() + delta * ((UCropFragment.this.mGestureCropImageView.getMaxScale() - UCropFragment.this.mGestureCropImageView.getMinScale()) / 15000.0f));
                } else {
                    UCropFragment.this.mGestureCropImageView.zoomOutImage(UCropFragment.this.mGestureCropImageView.getCurrentScale() + delta * ((UCropFragment.this.mGestureCropImageView.getMaxScale() - UCropFragment.this.mGestureCropImageView.getMinScale()) / 15000.0f));
                }
            }

            @Override
            public void onScrollEnd() {
                UCropFragment.this.mGestureCropImageView.setImageToWrapCropBounds();
            }

            @Override
            public void onScrollStart() {
                UCropFragment.this.mGestureCropImageView.cancelAllAnimations();
            }
        });
        ((HorizontalProgressWheelView)view.findViewById(R.id.scale_scroll_wheel)).setMiddleLineColor(this.mActiveWidgetColor);
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
            if (this.mWrapperStateAspectRatio.getVisibility() == View.VISIBLE) {
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
        this.mLayoutAspectRatio.setVisibility(stateViewId == R.id.state_aspect_ratio ? View.VISIBLE : View.GONE);
        this.mLayoutRotate.setVisibility(stateViewId == R.id.state_rotate ? View.VISIBLE : View.GONE);
        this.mLayoutScale.setVisibility(stateViewId == R.id.state_scale ? View.VISIBLE : View.GONE);
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

    private void addBlockingView(View view) {
        if (this.mBlockingView == null) {
            this.mBlockingView = new View(this.getContext());
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(-1, -1);
            this.mBlockingView.setLayoutParams((ViewGroup.LayoutParams)lp);
            this.mBlockingView.setClickable(true);
        }
        ((RelativeLayout)view.findViewById(R.id.ucrop_photobox)).addView(this.mBlockingView);
    }

    public void cropAndSaveImage() {
        this.mBlockingView.setClickable(true);
        this.callback.loadingProgress(true);
        this.mGestureCropImageView.cropAndSaveImage(this.mCompressFormat, this.mCompressQuality, new BitmapCropCallback(){

            @Override
            public void onBitmapCropped(@NonNull Uri resultUri, int offsetX, int offsetY, int imageWidth, int imageHeight) {
                UCropFragment.this.callback.onCropFinish(UCropFragment.this.getResult(resultUri, UCropFragment.this.mGestureCropImageView.getTargetAspectRatio(), offsetX, offsetY, imageWidth, imageHeight));
                UCropFragment.this.callback.loadingProgress(false);
            }

            @Override
            public void onCropFailure(@NonNull Throwable t) {
                UCropFragment.this.callback.onCropFinish(UCropFragment.this.getError(t));
            }
        });
    }

    protected UCropResult getResult(Uri uri, float resultAspectRatio, int offsetX, int offsetY, int imageWidth, int imageHeight) {
        return new UCropResult(-1, new Intent().putExtra("com.carlos.multiapp.OutputUri", (Parcelable)uri).putExtra("com.carlos.multiapp.CropAspectRatio", resultAspectRatio).putExtra("com.carlos.multiapp.ImageWidth", imageWidth).putExtra("com.carlos.multiapp.ImageHeight", imageHeight).putExtra("com.carlos.multiapp.OffsetX", offsetX).putExtra("com.carlos.multiapp.OffsetY", offsetY));
    }

    protected UCropResult getError(Throwable throwable) {
        return new UCropResult(96, new Intent().putExtra("com.carlos.multiapp.Error", (Serializable)throwable));
    }

    static {
        TAG = "UCropFragment";
        DEFAULT_COMPRESS_FORMAT = Bitmap.CompressFormat.JPEG;
    }

    public class UCropResult {
        public int mResultCode;
        public Intent mResultData;

        public UCropResult(int resultCode, Intent data) {
            this.mResultCode = resultCode;
            this.mResultData = data;
        }
    }

    @Retention(value=RetentionPolicy.SOURCE)
    public static @interface GestureTypes {
    }
}

