/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.content.Context
 *  android.content.Intent
 *  android.net.Uri
 *  android.os.Bundle
 *  androidx.annotation.ColorInt
 *  androidx.annotation.NonNull
 */
package com.carlos.common.imagepicker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import com.carlos.common.imagepicker.ImageSelectorActivity;
import com.carlos.common.imagepicker.UCrop;
import com.carlos.common.imagepicker.utils.PermissionsUtils;
import com.kook.librelease.StringFog;
import java.io.Serializable;
import java.util.ArrayList;

public class PhotoSelector {
    public static final int CROP_RECTANG = 1;
    public static final int CROP_CIRCLE = 2;
    public static final int DEFAULT_MAX_SELECTED_COUNT = 9;
    public static final int DEFAULT_GRID_COLUMN = 3;
    public static final int DEFAULT_REQUEST_CODE = 999;
    public static final int RESULT_CODE = 1000;
    public static final int TAKE_PHOTO_CROP_REQUESTCODE = 1001;
    public static final int TAKE_PHOTO_REQUESTCODE = 1002;
    public static final int CROP_REQUESTCODE = 1003;
    public static final String SELECT_RESULT = "select_result";
    public static final String EXTRA_MAX_SELECTED_COUNT = "max_selected_count";
    public static final String EXTRA_GRID_COLUMN = "column";
    public static final String EXTRA_SHOW_CAMERA = "show_camera";
    public static final String EXTRA_SELECTED_IMAGES = "selected_images";
    public static final String EXTRA_SINGLE = "single";
    public static final String EXTRA_CROP = "is_crop";
    public static final String EXTRA_CROP_MODE = "crop_mode";
    public static final String EXTRA_MATERIAL_DESIGN = "material_design";
    public static final String EXTRA_TOOLBARCOLOR = "toolBarColor";
    public static final String EXTRA_BOTTOMBARCOLOR = "bottomBarColor";
    public static final String EXTRA_STATUSBARCOLOR = "statusBarColor";
    public static final String EXTRA_POSITION = "position";
    public static final String EXTRA_ISPREVIEW = "isPreview";
    public static final String IS_CONFIRM = "is_confirm";

    public static Uri getCropImageUri(@NonNull Intent intent) {
        return UCrop.getOutput(intent);
    }

    public static PhotoSelectorBuilder builder() {
        return new PhotoSelectorBuilder();
    }

    public static class PhotoSelectorBuilder {
        private Bundle mPickerOptionsBundle = new Bundle();
        private Intent mPickerIntent = new Intent();

        PhotoSelectorBuilder() {
        }

        public void start(@NonNull Activity activity, int requestCode) {
            if (PermissionsUtils.checkReadStoragePermission(activity)) {
                activity.startActivityForResult(this.getIntent((Context)activity), requestCode);
            }
        }

        public Intent getIntent(@NonNull Context context) {
            this.mPickerIntent.setClass(context, ImageSelectorActivity.class);
            this.mPickerIntent.putExtras(this.mPickerOptionsBundle);
            return this.mPickerIntent;
        }

        public PhotoSelectorBuilder setIntentExtras(String extrasKey, Serializable values) {
            this.mPickerIntent.putExtra(extrasKey, values);
            return this;
        }

        public void start(@NonNull Activity activity) {
            this.start(activity, 999);
        }

        public PhotoSelectorBuilder setMaxSelectCount(int maxSelectCount) {
            this.mPickerOptionsBundle.putInt("max_selected_count", maxSelectCount);
            return this;
        }

        public PhotoSelectorBuilder setSingle(boolean isSingle) {
            this.mPickerOptionsBundle.putBoolean("single", isSingle);
            return this;
        }

        public PhotoSelectorBuilder setGridColumnCount(int columnCount) {
            this.mPickerOptionsBundle.putInt("column", columnCount);
            return this;
        }

        public PhotoSelectorBuilder setShowCamera(boolean showCamera) {
            this.mPickerOptionsBundle.putBoolean("show_camera", showCamera);
            return this;
        }

        public PhotoSelectorBuilder setSelected(ArrayList<String> selected) {
            this.mPickerOptionsBundle.putStringArrayList("selected_images", selected);
            return this;
        }

        public PhotoSelectorBuilder setToolBarColor(@ColorInt int toolBarColor) {
            this.mPickerOptionsBundle.putInt("toolBarColor", toolBarColor);
            return this;
        }

        public PhotoSelectorBuilder setBottomBarColor(@ColorInt int bottomBarColor) {
            this.mPickerOptionsBundle.putInt("bottomBarColor", bottomBarColor);
            return this;
        }

        public PhotoSelectorBuilder setStatusBarColor(@ColorInt int statusBarColor) {
            this.mPickerOptionsBundle.putInt("statusBarColor", statusBarColor);
            return this;
        }

        public PhotoSelectorBuilder setMaterialDesign(boolean materialDesign) {
            this.mPickerOptionsBundle.putBoolean("material_design", materialDesign);
            return this;
        }

        public PhotoSelectorBuilder setCrop(boolean isCrop) {
            this.mPickerOptionsBundle.putBoolean("is_crop", isCrop);
            return this;
        }

        public PhotoSelectorBuilder setCropMode(int mode) {
            this.mPickerOptionsBundle.putInt("crop_mode", mode);
            return this;
        }
    }
}

