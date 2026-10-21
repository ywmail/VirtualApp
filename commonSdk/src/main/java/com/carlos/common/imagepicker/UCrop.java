/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.annotation.TargetApi
 *  android.app.Activity
 *  android.app.Fragment
 *  android.content.Context
 *  android.content.Intent
 *  android.graphics.Bitmap$CompressFormat
 *  android.net.Uri
 *  android.os.Bundle
 *  android.os.Parcelable
 *  android.util.Log
 *  androidx.annotation.ColorInt
 *  androidx.annotation.DrawableRes
 *  androidx.annotation.FloatRange
 *  androidx.annotation.IntRange
 *  androidx.annotation.NonNull
 *  androidx.annotation.Nullable
 */
package com.carlos.common.imagepicker;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.FloatRange;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.carlos.common.imagepicker.UCropActivity;
import com.carlos.common.imagepicker.UCropFragment;
import com.carlos.common.imagepicker.entity.AspectRatio;
import com.kook.librelease.StringFog;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class UCrop {
    public static final int REQUEST_CROP = 69;
    public static final int RESULT_ERROR = 96;
    public static final int MIN_SIZE = 10;
    private static final String EXTRA_PREFIX = "com.carlos.multiapp";
    public static final String EXTRA_INPUT_URI = "com.carlos.multiapp.InputUri";
    public static final String EXTRA_OUTPUT_URI = "com.carlos.multiapp.OutputUri";
    public static final String EXTRA_OUTPUT_CROP_ASPECT_RATIO = "com.carlos.multiapp.CropAspectRatio";
    public static final String EXTRA_OUTPUT_IMAGE_WIDTH = "com.carlos.multiapp.ImageWidth";
    public static final String EXTRA_OUTPUT_IMAGE_HEIGHT = "com.carlos.multiapp.ImageHeight";
    public static final String EXTRA_OUTPUT_OFFSET_X = "com.carlos.multiapp.OffsetX";
    public static final String EXTRA_OUTPUT_OFFSET_Y = "com.carlos.multiapp.OffsetY";
    public static final String EXTRA_ERROR = "com.carlos.multiapp.Error";
    public static final String EXTRA_ASPECT_RATIO_X = "com.carlos.multiapp.AspectRatioX";
    public static final String EXTRA_ASPECT_RATIO_Y = "com.carlos.multiapp.AspectRatioY";
    public static final String EXTRA_MAX_SIZE_X = "com.carlos.multiapp.MaxSizeX";
    public static final String EXTRA_MAX_SIZE_Y = "com.carlos.multiapp.MaxSizeY";
    private Intent mCropIntent;
    private Bundle mCropOptionsBundle;

    public static UCrop of(Intent intent, @NonNull Uri source, @NonNull Uri destination) {
        return new UCrop(intent, source, destination);
    }

    private UCrop(Intent intent, @NonNull Uri source, @NonNull Uri destination) {
        if (intent == null) {
            this.mCropIntent = new Intent();
        } else {
            this.mCropIntent = intent;
            String packageName = intent.getStringExtra("packageName");
            int userId = intent.getIntExtra("appuserid", -1);
            Log.d((String)"HV-", (String)("UCropActivity  packageName:" + packageName + "    userId:" + userId));
        }
        this.mCropOptionsBundle = new Bundle();
        this.mCropOptionsBundle.putParcelable("com.carlos.multiapp.InputUri", (Parcelable)source);
        this.mCropOptionsBundle.putParcelable("com.carlos.multiapp.OutputUri", (Parcelable)destination);
        this.mCropOptionsBundle.putParcelable("com.carlos.multiapp.InputUri", (Parcelable)source);
    }

    public UCrop withAspectRatio(float x, float y) {
        this.mCropOptionsBundle.putFloat(EXTRA_ASPECT_RATIO_X, x);
        this.mCropOptionsBundle.putFloat(EXTRA_ASPECT_RATIO_Y, y);
        return this;
    }

    public UCrop useSourceImageAspectRatio() {
        this.mCropOptionsBundle.putFloat(EXTRA_ASPECT_RATIO_X, 0.0f);
        this.mCropOptionsBundle.putFloat(EXTRA_ASPECT_RATIO_Y, 0.0f);
        return this;
    }

    public UCrop withMaxResultSize(@IntRange(from=10L) int width, @IntRange(from=10L) int height) {
        if (width < 10) {
            width = 10;
        }
        if (height < 10) {
            height = 10;
        }
        this.mCropOptionsBundle.putInt(EXTRA_MAX_SIZE_X, width);
        this.mCropOptionsBundle.putInt(EXTRA_MAX_SIZE_Y, height);
        return this;
    }

    public UCrop withOptions(@NonNull Options options) {
        this.mCropOptionsBundle.putAll(options.getOptionBundle());
        return this;
    }

    public void start(@NonNull Activity activity) {
        this.start(activity, 69);
    }

    public void start(@NonNull Activity activity, int requestCode) {
        activity.startActivityForResult(this.getIntent((Context)activity), requestCode);
    }

    public void start(@NonNull Context context, @NonNull Fragment fragment) {
        this.start(context, fragment, 69);
    }

    @TargetApi(value=11)
    public void start(@NonNull Context context, @NonNull Fragment fragment, int requestCode) {
        fragment.startActivityForResult(this.getIntent(context), requestCode);
    }

    public Intent getIntent(@NonNull Context context) {
        this.mCropIntent.setClass(context, UCropActivity.class);
        this.mCropIntent.putExtras(this.mCropOptionsBundle);
        return this.mCropIntent;
    }

    public UCropFragment getFragment() {
        return UCropFragment.newInstance(this.mCropOptionsBundle);
    }

    public UCropFragment getFragment(Bundle bundle) {
        this.mCropOptionsBundle = bundle;
        return this.getFragment();
    }

    @Nullable
    public static Uri getOutput(@NonNull Intent intent) {
        return (Uri)intent.getParcelableExtra(EXTRA_OUTPUT_URI);
    }

    public static int getOutputImageWidth(@NonNull Intent intent) {
        return intent.getIntExtra(EXTRA_OUTPUT_IMAGE_WIDTH, -1);
    }

    public static int getOutputImageHeight(@NonNull Intent intent) {
        return intent.getIntExtra(EXTRA_OUTPUT_IMAGE_HEIGHT, -1);
    }

    public static float getOutputCropAspectRatio(@NonNull Intent intent) {
        return (Float) intent.getFloatExtra(EXTRA_OUTPUT_CROP_ASPECT_RATIO, 0);
    }

    @Nullable
    public static Throwable getError(@NonNull Intent result) {
        return (Throwable)result.getSerializableExtra(EXTRA_ERROR);
    }

    public static class Options {
        public static final String EXTRA_COMPRESSION_FORMAT_NAME = "com.carlos.multiapp.CompressionFormatName";
        public static final String EXTRA_COMPRESSION_QUALITY = "com.carlos.multiapp.CompressionQuality";
        public static final String EXTRA_ALLOWED_GESTURES = "com.carlos.multiapp.AllowedGestures";
        public static final String EXTRA_MAX_BITMAP_SIZE = "com.carlos.multiapp.MaxBitmapSize";
        public static final String EXTRA_MAX_SCALE_MULTIPLIER = "com.carlos.multiapp.MaxScaleMultiplier";
        public static final String EXTRA_IMAGE_TO_CROP_BOUNDS_ANIM_DURATION = "com.carlos.multiapp.ImageToCropBoundsAnimDuration";
        public static final String EXTRA_DIMMED_LAYER_COLOR = "com.carlos.multiapp.DimmedLayerColor";
        public static final String EXTRA_CIRCLE_DIMMED_LAYER = "com.carlos.multiapp.CircleDimmedLayer";
        public static final String EXTRA_SHOW_CROP_FRAME = "com.carlos.multiapp.ShowCropFrame";
        public static final String EXTRA_CROP_FRAME_COLOR = "com.carlos.multiapp.CropFrameColor";
        public static final String EXTRA_CROP_FRAME_STROKE_WIDTH = "com.carlos.multiapp.CropFrameStrokeWidth";
        public static final String EXTRA_SHOW_CROP_GRID = "com.carlos.multiapp.ShowCropGrid";
        public static final String EXTRA_CROP_GRID_ROW_COUNT = "com.carlos.multiapp.CropGridRowCount";
        public static final String EXTRA_CROP_GRID_COLUMN_COUNT = "com.carlos.multiapp.CropGridColumnCount";
        public static final String EXTRA_CROP_GRID_COLOR = "com.carlos.multiapp.CropGridColor";
        public static final String EXTRA_CROP_GRID_STROKE_WIDTH = "com.carlos.multiapp.CropGridStrokeWidth";
        public static final String EXTRA_TOOL_BAR_COLOR = "com.carlos.multiapp.ToolbarColor";
        public static final String EXTRA_STATUS_BAR_COLOR = "com.carlos.multiapp.StatusBarColor";
        public static final String EXTRA_UCROP_COLOR_WIDGET_ACTIVE = "com.carlos.multiapp.UcropColorWidgetActive";
        public static final String EXTRA_UCROP_WIDGET_COLOR_TOOLBAR = "com.carlos.multiapp.UcropToolbarWidgetColor";
        public static final String EXTRA_UCROP_TITLE_TEXT_TOOLBAR = "com.carlos.multiapp.UcropToolbarTitleText";
        public static final String EXTRA_UCROP_WIDGET_CANCEL_DRAWABLE = "com.carlos.multiapp.UcropToolbarCancelDrawable";
        public static final String EXTRA_UCROP_WIDGET_CROP_DRAWABLE = "com.carlos.multiapp.UcropToolbarCropDrawable";
        public static final String EXTRA_UCROP_LOGO_COLOR = "com.carlos.multiapp.UcropLogoColor";
        public static final String EXTRA_HIDE_BOTTOM_CONTROLS = "com.carlos.multiapp.HideBottomControls";
        public static final String EXTRA_FREE_STYLE_CROP = "com.carlos.multiapp.FreeStyleCrop";
        public static final String EXTRA_ASPECT_RATIO_SELECTED_BY_DEFAULT = "com.carlos.multiapp.AspectRatioSelectedByDefault";
        public static final String EXTRA_ASPECT_RATIO_OPTIONS = "com.carlos.multiapp.AspectRatioOptions";
        public static final String EXTRA_UCROP_ROOT_VIEW_BACKGROUND_COLOR = "com.carlos.multiapp.UcropRootViewBackgroundColor";
        private final Bundle mOptionBundle = new Bundle();

        @NonNull
        public Bundle getOptionBundle() {
            return this.mOptionBundle;
        }

        public void setCompressionFormat(@NonNull Bitmap.CompressFormat format) {
            this.mOptionBundle.putString(EXTRA_COMPRESSION_FORMAT_NAME, format.name());
        }

        public void setCompressionQuality(@IntRange(from=0L) int compressQuality) {
            this.mOptionBundle.putInt(EXTRA_COMPRESSION_QUALITY, compressQuality);
        }

        public void setAllowedGestures(int tabScale, int tabRotate, int tabAspectRatio) {
            this.mOptionBundle.putIntArray(EXTRA_ALLOWED_GESTURES, new int[]{tabScale, tabRotate, tabAspectRatio});
        }

        public void setMaxScaleMultiplier(@FloatRange(from=1.0, fromInclusive=false) float maxScaleMultiplier) {
            this.mOptionBundle.putFloat(EXTRA_MAX_SCALE_MULTIPLIER, maxScaleMultiplier);
        }

        public void setImageToCropBoundsAnimDuration(@IntRange(from=10L) int durationMillis) {
            this.mOptionBundle.putInt(EXTRA_IMAGE_TO_CROP_BOUNDS_ANIM_DURATION, durationMillis);
        }

        public void setMaxBitmapSize(@IntRange(from=10L) int maxBitmapSize) {
            this.mOptionBundle.putInt(EXTRA_MAX_BITMAP_SIZE, maxBitmapSize);
        }

        public void setDimmedLayerColor(@ColorInt int color2) {
            this.mOptionBundle.putInt(EXTRA_DIMMED_LAYER_COLOR, color2);
        }

        public void setCircleDimmedLayer(boolean isCircle) {
            this.mOptionBundle.putBoolean(EXTRA_CIRCLE_DIMMED_LAYER, isCircle);
        }

        public void setShowCropFrame(boolean show) {
            this.mOptionBundle.putBoolean(EXTRA_SHOW_CROP_FRAME, show);
        }

        public void setCropFrameColor(@ColorInt int color2) {
            this.mOptionBundle.putInt(EXTRA_CROP_FRAME_COLOR, color2);
        }

        public void setCropFrameStrokeWidth(@IntRange(from=0L) int width) {
            this.mOptionBundle.putInt(EXTRA_CROP_FRAME_STROKE_WIDTH, width);
        }

        public void setShowCropGrid(boolean show) {
            this.mOptionBundle.putBoolean(EXTRA_SHOW_CROP_GRID, show);
        }

        public void setCropGridRowCount(@IntRange(from=0L) int count) {
            this.mOptionBundle.putInt(EXTRA_CROP_GRID_ROW_COUNT, count);
        }

        public void setCropGridColumnCount(@IntRange(from=0L) int count) {
            this.mOptionBundle.putInt(EXTRA_CROP_GRID_COLUMN_COUNT, count);
        }

        public void setCropGridColor(@ColorInt int color2) {
            this.mOptionBundle.putInt(EXTRA_CROP_GRID_COLOR, color2);
        }

        public void setCropGridStrokeWidth(@IntRange(from=0L) int width) {
            this.mOptionBundle.putInt(EXTRA_CROP_GRID_STROKE_WIDTH, width);
        }

        public void setToolbarColor(@ColorInt int color2) {
            this.mOptionBundle.putInt(EXTRA_TOOL_BAR_COLOR, color2);
        }

        public void setStatusBarColor(@ColorInt int color2) {
            this.mOptionBundle.putInt(EXTRA_STATUS_BAR_COLOR, color2);
        }

        public void setActiveWidgetColor(@ColorInt int color2) {
            this.mOptionBundle.putInt(EXTRA_UCROP_COLOR_WIDGET_ACTIVE, color2);
        }

        public void setToolbarWidgetColor(@ColorInt int color2) {
            this.mOptionBundle.putInt(EXTRA_UCROP_WIDGET_COLOR_TOOLBAR, color2);
        }

        public void setToolbarTitle(@Nullable String text) {
            this.mOptionBundle.putString(EXTRA_UCROP_TITLE_TEXT_TOOLBAR, text);
        }

        public void setToolbarCancelDrawable(@DrawableRes int drawable2) {
            this.mOptionBundle.putInt(EXTRA_UCROP_WIDGET_CANCEL_DRAWABLE, drawable2);
        }

        public void setToolbarCropDrawable(@DrawableRes int drawable2) {
            this.mOptionBundle.putInt(EXTRA_UCROP_WIDGET_CROP_DRAWABLE, drawable2);
        }

        public void setLogoColor(@ColorInt int color2) {
            this.mOptionBundle.putInt(EXTRA_UCROP_LOGO_COLOR, color2);
        }

        public void setHideBottomControls(boolean hide) {
            this.mOptionBundle.putBoolean(EXTRA_HIDE_BOTTOM_CONTROLS, hide);
        }

        public void setFreeStyleCropEnabled(boolean enabled) {
            this.mOptionBundle.putBoolean(EXTRA_FREE_STYLE_CROP, enabled);
        }

        public void setAspectRatioOptions(int selectedByDefault, AspectRatio ... aspectRatio) {
            if (selectedByDefault > aspectRatio.length) {
                throw new IllegalArgumentException(String.format(Locale.US, "Index [selectedByDefault = %d] cannot be higher than aspect ratio options count [count = %d].", selectedByDefault, aspectRatio.length));
            }
            this.mOptionBundle.putInt(EXTRA_ASPECT_RATIO_SELECTED_BY_DEFAULT, selectedByDefault);
            this.mOptionBundle.putParcelableArrayList(EXTRA_ASPECT_RATIO_OPTIONS, new ArrayList<AspectRatio>(Arrays.asList(aspectRatio)));
        }

        public void setRootViewBackgroundColor(@ColorInt int color2) {
            this.mOptionBundle.putInt(EXTRA_UCROP_ROOT_VIEW_BACKGROUND_COLOR, color2);
        }

        public void withAspectRatio(float x, float y) {
            this.mOptionBundle.putFloat("com.carlos.multiapp.AspectRatioX", x);
            this.mOptionBundle.putFloat("com.carlos.multiapp.AspectRatioY", y);
        }

        public void useSourceImageAspectRatio() {
            this.mOptionBundle.putFloat("com.carlos.multiapp.AspectRatioX", 0.0f);
            this.mOptionBundle.putFloat("com.carlos.multiapp.AspectRatioY", 0.0f);
        }

        public void withMaxResultSize(@IntRange(from=10L) int width, @IntRange(from=10L) int height) {
            this.mOptionBundle.putInt("com.carlos.multiapp.MaxSizeX", width);
            this.mOptionBundle.putInt("com.carlos.multiapp.MaxSizeY", height);
        }
    }
}

