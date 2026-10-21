/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.animation.ObjectAnimator
 *  android.annotation.SuppressLint
 *  android.app.Activity
 *  android.app.AlertDialog$Builder
 *  android.content.ActivityNotFoundException
 *  android.content.Context
 *  android.content.DialogInterface
 *  android.content.DialogInterface$OnClickListener
 *  android.content.Intent
 *  android.content.res.Configuration
 *  android.net.Uri
 *  android.os.Bundle
 *  android.os.Environment
 *  android.os.Handler
 *  android.util.Log
 *  android.view.View
 *  android.view.View$OnClickListener
 *  android.widget.FrameLayout
 *  android.widget.RelativeLayout
 *  android.widget.TextView
 *  androidx.annotation.ColorInt
 *  androidx.annotation.NonNull
 *  androidx.annotation.Nullable
 *  androidx.annotation.RequiresApi
 *  androidx.appcompat.app.ActionBar
 *  androidx.appcompat.app.AppCompatActivity
 *  androidx.appcompat.widget.Toolbar
 *  androidx.core.app.ActivityCompat
 *  androidx.core.content.ContextCompat
 *  androidx.recyclerview.widget.GridLayoutManager
 *  androidx.recyclerview.widget.LinearLayoutManager
 *  androidx.recyclerview.widget.RecyclerView
 *  androidx.recyclerview.widget.RecyclerView$Adapter
 *  androidx.recyclerview.widget.RecyclerView$LayoutManager
 *  androidx.recyclerview.widget.RecyclerView$OnScrollListener
 *  androidx.recyclerview.widget.SimpleItemAnimator
 *  com.google.android.material.bottomsheet.BottomSheetDialog
 *  com.kook.librelease.R$color
 *  com.kook.librelease.R$id
 *  com.kook.librelease.R$layout
 *  com.kook.librelease.R$string
 */
package com.carlos.common.imagepicker;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import com.carlos.common.imagepicker.RvPreviewActivity;
import com.carlos.common.imagepicker.UCrop;
import com.carlos.common.imagepicker.adapter.FolderAdapter;
import com.carlos.common.imagepicker.adapter.ImageAdapter;
import com.carlos.common.imagepicker.entity.AspectRatio;
import com.carlos.common.imagepicker.entity.Folder;
import com.carlos.common.imagepicker.entity.Image;
import com.carlos.common.imagepicker.model.ImageModel;
import com.carlos.common.imagepicker.utils.DateUtils;
import com.carlos.common.imagepicker.utils.ImageCaptureManager;
import com.carlos.common.imagepicker.utils.PermissionsUtils;
import com.carlos.common.imagepicker.utils.StatusBarUtils;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.kook.librelease.R;
import com.kook.librelease.StringFog;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ImageSelectorActivity
extends AppCompatActivity {
    private TextView tvTime;
    private TextView tvFolderName;
    private TextView tvConfirm;
    private TextView tvPreview;
    private FrameLayout btnConfirm;
    private FrameLayout btnPreview;
    private RecyclerView rvImage;
    private RecyclerView rvFolder;
    private View masking;
    private ImageAdapter mAdapter;
    private GridLayoutManager mLayoutManager;
    private ImageCaptureManager captureManager;
    private ArrayList<Folder> mFolders;
    private Folder mFolder;
    private boolean isToSettings = false;
    private static final int PERMISSION_REQUEST_CODE = 17;
    private boolean isShowTime;
    private boolean isInitFolder;
    private RelativeLayout rlBottomBar;
    private int toolBarColor;
    private int bottomBarColor;
    private int statusBarColor;
    private int column;
    private boolean isSingle;
    private boolean showCamera;
    private int mMaxCount;
    private ArrayList<String> mSelectedImages;
    private boolean isCrop;
    private int cropMode;
    private Toolbar toolbar;
    private Handler mHideHandler = new Handler();
    private Runnable mHide = new Runnable(){

        @Override
        public void run() {
            ImageSelectorActivity.this.hideTime();
        }
    };
    private BottomSheetDialog bottomSheetDialog;
    private String filePath;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        assert (bundle != null);
        this.mMaxCount = bundle.getInt("max_selected_count", 9);
        this.column = bundle.getInt("column", 3);
        this.isSingle = bundle.getBoolean("single", false);
        this.cropMode = bundle.getInt("crop_mode", 1);
        this.showCamera = bundle.getBoolean("show_camera", true);
        this.isCrop = bundle.getBoolean("is_crop", false);
        this.mSelectedImages = bundle.getStringArrayList("selected_images");
        this.captureManager = new ImageCaptureManager((Context)this);
        this.toolBarColor = bundle.getInt("toolBarColor", ContextCompat.getColor((Context)this, (int)R.color.blue));
        this.bottomBarColor = bundle.getInt("bottomBarColor", ContextCompat.getColor((Context)this, (int)R.color.blue));
        this.statusBarColor = bundle.getInt("statusBarColor", ContextCompat.getColor((Context)this, (int)R.color.blue));
        boolean materialDesign = bundle.getBoolean("material_design", false);
        if (materialDesign) {
            this.setContentView(R.layout.activity_image_select);
        } else {
            this.setContentView(R.layout.activity_image_select2);
        }
        this.initView();
        StatusBarUtils.setColor((Activity)this, this.statusBarColor);
        this.setToolBarColor(this.toolBarColor);
        this.setBottomBarColor(this.bottomBarColor);
        this.initListener();
        this.initImageList();
        this.checkPermissionAndLoadImages();
        this.hideFolderList();
        if (this.mSelectedImages != null) {
            this.setSelectImageCount(this.mSelectedImages.size());
        } else {
            this.setSelectImageCount(0);
        }
    }

    private void initView() {
        this.toolbar = (Toolbar)this.findViewById(R.id.toolbar);
        this.setSupportActionBar(this.toolbar);
        ActionBar actionBar = this.getSupportActionBar();
        assert (actionBar != null);
        actionBar.setDisplayHomeAsUpEnabled(true);
        this.rlBottomBar = (RelativeLayout)this.findViewById(R.id.rl_bottom_bar);
        this.rvImage = (RecyclerView)this.findViewById(R.id.rv_image);
        this.bottomSheetDialog = new BottomSheetDialog((Context)this);
        View bsdFolderDialogView = this.getLayoutInflater().inflate(R.layout.bsd_folder_dialog, null);
        this.bottomSheetDialog.setContentView(bsdFolderDialogView);
        this.rvFolder = (RecyclerView)bsdFolderDialogView.findViewById(R.id.rv_folder);
        this.tvConfirm = (TextView)this.findViewById(R.id.tv_confirm);
        this.tvPreview = (TextView)this.findViewById(R.id.tv_preview);
        this.btnConfirm = (FrameLayout)this.findViewById(R.id.btn_confirm);
        this.btnPreview = (FrameLayout)this.findViewById(R.id.btn_preview);
        this.tvFolderName = (TextView)this.findViewById(R.id.tv_folder_name);
        this.tvTime = (TextView)this.findViewById(R.id.tv_time);
        this.masking = this.findViewById(R.id.masking);
    }

    private void initListener() {
        this.toolbar.setNavigationOnClickListener(new View.OnClickListener(){

            public void onClick(View v) {
                ImageSelectorActivity.this.finish();
            }
        });
        this.btnPreview.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v) {
                ArrayList<Image> images = new ArrayList<Image>(ImageSelectorActivity.this.mAdapter.getSelectImages());
                ImageSelectorActivity.this.toPreviewActivity(true, images, 0);
            }
        });
        this.btnConfirm.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v) {
                if (ImageSelectorActivity.this.isCrop && ImageSelectorActivity.this.isSingle) {
                    ImageSelectorActivity.this.crop(ImageSelectorActivity.this.mAdapter.getSelectImages().get(0).getPath(), 69);
                } else {
                    ImageSelectorActivity.this.confirm();
                }
            }
        });
        this.findViewById(R.id.btn_folder).setOnClickListener(new View.OnClickListener(){

            public void onClick(View v) {
                if (ImageSelectorActivity.this.isInitFolder) {
                    ImageSelectorActivity.this.openFolder();
                }
            }
        });
        this.masking.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v) {
                ImageSelectorActivity.this.closeFolder();
            }
        });
        this.rvImage.addOnScrollListener(new RecyclerView.OnScrollListener(){

            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                ImageSelectorActivity.this.changeTime();
            }

            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                ImageSelectorActivity.this.changeTime();
            }
        });
    }

    private void crop(@NonNull String imagePath, int requestCode) {
        Uri selectUri = Uri.fromFile((File)new File(imagePath));
        SimpleDateFormat timeFormatter = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA);
        long time = System.currentTimeMillis();
        String imageName = timeFormatter.format(new Date(time));
        UCrop uCrop = UCrop.of(this.getIntent(), selectUri, Uri.fromFile((File)new File(this.getCacheDir(), imageName + ".jpg")));
        UCrop.Options options = new UCrop.Options();
        if (this.cropMode == 2) {
            options.setCircleDimmedLayer(true);
            options.setShowCropGrid(false);
            options.setShowCropFrame(false);
        }
        options.setToolbarColor(this.toolBarColor);
        options.setStatusBarColor(this.statusBarColor);
        options.setActiveWidgetColor(this.bottomBarColor);
        options.setAspectRatioOptions(0, new AspectRatio[0]);
        options.setCompressionQuality(100);
        uCrop.withOptions(options);
        uCrop.start((Activity)this, requestCode);
    }

    private void setToolBarColor(@ColorInt int color2) {
        this.toolbar.setBackgroundColor(color2);
    }

    private void setBottomBarColor(@ColorInt int color2) {
        this.rlBottomBar.setBackgroundColor(color2);
    }

    private void initImageList() {
        Configuration configuration = this.getResources().getConfiguration();
        this.mLayoutManager = configuration.orientation == 1 ? new GridLayoutManager((Context)this, this.column) : new GridLayoutManager((Context)this, 5);
        this.rvImage.setLayoutManager((RecyclerView.LayoutManager)this.mLayoutManager);
        this.mAdapter = new ImageAdapter((Context)this, this.mMaxCount, this.isSingle);
        this.rvImage.setAdapter((RecyclerView.Adapter)this.mAdapter);
        ((SimpleItemAnimator)this.rvImage.getItemAnimator()).setSupportsChangeAnimations(false);
        if (this.mFolders != null && !this.mFolders.isEmpty()) {
            this.setFolder(this.mFolders.get(0));
        }
        this.mAdapter.setOnImageSelectListener(new ImageAdapter.OnImageSelectListener(){

            @Override
            public void OnImageSelect(Image image, boolean isSelect, int selectCount) {
                ImageSelectorActivity.this.setSelectImageCount(selectCount);
            }
        });
        this.mAdapter.setOnItemClickListener(new ImageAdapter.OnItemClickListener(){

            @Override
            public void OnItemClick(Image image, View itemView, int position) {
                ImageSelectorActivity.this.toPreviewActivity(false, ImageSelectorActivity.this.mAdapter.getData(), position);
            }
        });
        this.mAdapter.setOnCameraClickListener(new View.OnClickListener(){

            @RequiresApi(api=23)
            public void onClick(View v) {
                if (!PermissionsUtils.checkCameraPermission((Activity)ImageSelectorActivity.this)) {
                    return;
                }
                if (!PermissionsUtils.checkWriteStoragePermission((Activity)ImageSelectorActivity.this)) {
                    return;
                }
                ImageSelectorActivity.this.openCamera();
            }
        });
    }

    private void openCamera() {
        try {
            Intent intent = this.captureManager.dispatchTakePictureIntent();
            if (this.isCrop && this.isSingle) {
                this.filePath = intent.getStringExtra("photo_path");
                this.startActivityForResult(intent, 1001);
            } else {
                this.startActivityForResult(intent, 1002);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (ActivityNotFoundException e) {
            Log.e((String)"PhotoPickerFragment", (String)"No Activity Found to handle Intent", (Throwable)e);
        }
    }

    private void initFolderList() {
        if (this.mFolders != null && !this.mFolders.isEmpty()) {
            this.isInitFolder = true;
            this.rvFolder.setLayoutManager((RecyclerView.LayoutManager)new LinearLayoutManager((Context)this));
            FolderAdapter adapter = new FolderAdapter((Context)this, this.mFolders);
            adapter.setOnFolderSelectListener(new FolderAdapter.OnFolderSelectListener(){

                @Override
                public void OnFolderSelect(Folder folder) {
                    ImageSelectorActivity.this.setFolder(folder);
                    ImageSelectorActivity.this.closeFolder();
                }
            });
            this.rvFolder.setAdapter((RecyclerView.Adapter)adapter);
        }
    }

    private void hideFolderList() {
    }

    private void setFolder(Folder folder) {
        if (folder != null && this.mAdapter != null && !folder.equals(this.mFolder)) {
            this.mFolder = folder;
            this.tvFolderName.setText((CharSequence)folder.getName());
            this.rvImage.scrollToPosition(0);
            this.mAdapter.refresh(folder.getImages(), folder.isUseCamera());
        }
    }

    @SuppressLint(value={"SetTextI18n"})
    private void setSelectImageCount(int count) {
        if (count == 0) {
            this.btnConfirm.setEnabled(false);
            this.btnPreview.setEnabled(false);
            this.tvConfirm.setText((CharSequence)this.getString(R.string.confirm));
            this.tvPreview.setText((CharSequence)this.getString(R.string.preview));
        } else {
            this.btnConfirm.setEnabled(true);
            this.btnPreview.setEnabled(true);
            this.tvPreview.setText((CharSequence)this.getString(R.string.preview_count, new Object[]{count}));
            if (this.isSingle) {
                this.tvConfirm.setText((CharSequence)this.getString(R.string.confirm));
            } else if (this.mMaxCount > 0) {
                this.tvConfirm.setText((CharSequence)this.getString(R.string.confirm_maxcount, new Object[]{count, this.mMaxCount}));
            } else {
                this.tvConfirm.setText((CharSequence)this.getString(R.string.confirm_count, new Object[]{count}));
            }
        }
    }

    private void openFolder() {
        this.bottomSheetDialog.show();
    }

    private void closeFolder() {
        this.bottomSheetDialog.dismiss();
    }

    private void hideTime() {
        if (this.isShowTime) {
            ObjectAnimator.ofFloat((Object)this.tvTime, (String)"alpha", (float[])new float[]{1.0f, 0.0f}).setDuration(300L).start();
            this.isShowTime = false;
        }
    }

    private void showTime() {
        if (!this.isShowTime) {
            ObjectAnimator.ofFloat((Object)this.tvTime, (String)"alpha", (float[])new float[]{0.0f, 1.0f}).setDuration(300L).start();
            this.isShowTime = true;
        }
    }

    private void changeTime() {
        int firstVisibleItem = this.getFirstVisibleItem();
        if (firstVisibleItem >= 0 && firstVisibleItem < this.mAdapter.getData().size()) {
            Image image = this.mAdapter.getData().get(firstVisibleItem);
            String time = DateUtils.getImageTime(image.getTime() * 1000L);
            this.tvTime.setText((CharSequence)time);
            this.showTime();
            this.mHideHandler.removeCallbacks(this.mHide);
            this.mHideHandler.postDelayed(this.mHide, 1500L);
        }
    }

    private int getFirstVisibleItem() {
        return this.mLayoutManager.findFirstVisibleItemPosition();
    }

    private void confirm() {
        if (this.mAdapter == null) {
            return;
        }
        ArrayList<Image> selectImages = this.mAdapter.getSelectImages();
        ArrayList<String> images = new ArrayList<String>();
        for (Image image : selectImages) {
            images.add(image.getPath());
        }
        Intent intent = this.getIntent();
        intent.putStringArrayListExtra("select_result", images);
        this.setResult(-1, intent);
        this.finish();
    }

    private void toPreviewActivity(boolean isPreview, ArrayList<Image> images, int position) {
        if (images != null && !images.isEmpty()) {
            RvPreviewActivity.openActivity(isPreview, (Activity)this, images, this.mAdapter.getSelectImages(), this.isSingle, this.mMaxCount, position, this.toolBarColor, this.bottomBarColor, this.statusBarColor);
        }
    }

    protected void onStart() {
        super.onStart();
        if (this.isToSettings) {
            this.isToSettings = false;
            this.checkPermissionAndLoadImages();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d((String)"HV-", (String)"ImageSelectorActivity  onActivityResult:");
        switch (requestCode) {
            case 1000: {
                if (data != null && data.getBooleanExtra("is_confirm", false)) {
                    if (this.isSingle && this.isCrop) {
                        this.crop(this.mAdapter.getSelectImages().get(0).getPath(), 69);
                        break;
                    }
                    this.confirm();
                    break;
                }
                this.mAdapter.notifyDataSetChanged();
                this.setSelectImageCount(this.mAdapter.getSelectImages().size());
                break;
            }
            case 1002: {
                this.loadImageForSDCard();
                this.setSelectImageCount(this.mAdapter.getSelectImages().size());
                this.mSelectedImages = new ArrayList();
                for (Image image : this.mAdapter.getSelectImages()) {
                    this.mSelectedImages.add(image.getPath());
                }
                this.mAdapter.setSelectedImages(this.mSelectedImages);
                this.mAdapter.notifyDataSetChanged();
                break;
            }
            case 1001: {
                this.crop(this.filePath, 1003);
                break;
            }
            case 69: {
                if (data != null) {
                    this.setResult(-1, data);
                    this.finish();
                    break;
                }
                this.mAdapter.notifyDataSetChanged();
                this.setSelectImageCount(this.mAdapter.getSelectImages().size());
                break;
            }
            case 1003: {
                if (data != null) {
                    this.setResult(-1, data);
                    this.finish();
                    break;
                }
                this.loadImageForSDCard();
                this.setSelectImageCount(this.mAdapter.getSelectImages().size());
                this.mSelectedImages = new ArrayList();
                for (Image image : this.mAdapter.getSelectImages()) {
                    this.mSelectedImages.add(image.getPath());
                }
                this.mAdapter.setSelectedImages(this.mSelectedImages);
                this.mAdapter.notifyDataSetChanged();
            }
        }
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (this.mLayoutManager != null && this.mAdapter != null) {
            if (newConfig.orientation == 1) {
                this.mLayoutManager.setSpanCount(3);
            } else if (newConfig.orientation == 2) {
                this.mLayoutManager.setSpanCount(5);
            }
            this.mAdapter.notifyDataSetChanged();
        }
    }

    private void checkPermissionAndLoadImages() {
        if (!Environment.getExternalStorageState().equals("mounted")) {
            return;
        }
        int hasWriteContactsPermission = ContextCompat.checkSelfPermission((Context)this.getApplication(), (String)"android.permission.WRITE_EXTERNAL_STORAGE");
        if (hasWriteContactsPermission == 0) {
            this.loadImageForSDCard();
        } else {
            ActivityCompat.requestPermissions((Activity)this, (String[])new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, (int)17);
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 17) {
            if (grantResults.length > 0 && grantResults[0] == 0) {
                this.loadImageForSDCard();
            } else {
                this.showExceptionDialog();
            }
        }
    }

    private void showExceptionDialog() {
        new AlertDialog.Builder((Context)this).setCancelable(false).setTitle((CharSequence)"提示").setMessage((CharSequence)"该相册需要赋予访问存储的权限，请到“设置”>“应用”>“权限”中配置权限。").setNegativeButton((CharSequence)"取消", new DialogInterface.OnClickListener(){

            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                ImageSelectorActivity.this.finish();
            }
        }).setPositiveButton((CharSequence)"确定", new DialogInterface.OnClickListener(){

            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                ImageSelectorActivity.this.startAppSettings();
                ImageSelectorActivity.this.isToSettings = true;
            }
        }).show();
    }

    private void loadImageForSDCard() {
        ImageModel.loadImageForSDCard((Context)this, new ImageModel.DataCallback(){

            @Override
            public void onSuccess(ArrayList<Folder> folders) {
                ImageSelectorActivity.this.mFolders = folders;
                ImageSelectorActivity.this.runOnUiThread(new Runnable(){

                    @Override
                    public void run() {
                        if (ImageSelectorActivity.this.mFolders != null && !ImageSelectorActivity.this.mFolders.isEmpty()) {
                            ImageSelectorActivity.this.initFolderList();
                            ((Folder)ImageSelectorActivity.this.mFolders.get(0)).setUseCamera(ImageSelectorActivity.this.showCamera);
                            ImageSelectorActivity.this.setFolder((Folder)ImageSelectorActivity.this.mFolders.get(0));
                            if (ImageSelectorActivity.this.mSelectedImages != null && ImageSelectorActivity.this.mAdapter != null) {
                                ImageSelectorActivity.this.mAdapter.setSelectedImages(ImageSelectorActivity.this.mSelectedImages);
                                ImageSelectorActivity.this.mSelectedImages = null;
                            }
                        }
                    }
                });
            }
        });
    }

    private void startAppSettings() {
        Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(Uri.parse((String)("package:" + this.getPackageName())));
        this.startActivity(intent);
    }
}

