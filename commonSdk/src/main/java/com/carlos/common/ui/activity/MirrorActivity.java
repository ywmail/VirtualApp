/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.app.Application
 *  android.app.Dialog
 *  android.content.ClipboardManager
 *  android.content.Context
 *  android.content.Intent
 *  android.content.pm.ApplicationInfo
 *  android.content.pm.PackageInfo
 *  android.content.pm.PackageManager
 *  android.content.pm.PackageManager$NameNotFoundException
 *  android.graphics.Bitmap
 *  android.graphics.drawable.Drawable
 *  android.os.Build$VERSION
 *  android.os.Bundle
 *  android.os.Parcelable
 *  android.text.TextUtils
 *  android.view.View
 *  android.view.View$OnLongClickListener
 *  android.view.ViewGroup$LayoutParams
 *  android.widget.EditText
 *  android.widget.Toast
 *  androidx.annotation.Nullable
 *  androidx.appcompat.app.AlertDialog
 *  androidx.appcompat.app.AlertDialog$Builder
 *  androidx.appcompat.widget.AppCompatImageView
 *  androidx.appcompat.widget.AppCompatTextView
 *  androidx.core.app.ActivityCompat
 *  androidx.recyclerview.widget.RecyclerView
 *  androidx.recyclerview.widget.RecyclerView$Adapter
 *  androidx.recyclerview.widget.RecyclerView$ItemDecoration
 *  androidx.recyclerview.widget.RecyclerView$LayoutManager
 *  androidx.recyclerview.widget.StaggeredGridLayoutManager
 *  androidx.recyclerview.widget.StaggeredGridLayoutManager$LayoutParams
 *  com.kook.librelease.R$dimen
 *  com.kook.librelease.R$drawable
 *  com.kook.librelease.R$id
 *  com.kook.librelease.R$layout
 *  com.kook.librelease.R$string
 *  com.kook.librelease.R$style
 */
package com.carlos.common.ui.activity;

import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import com.carlos.common.App;
import com.carlos.common.clouddisk.ClouddiskLauncher;
import com.carlos.common.clouddisk.http.HttpWorker;
import com.carlos.common.clouddisk.listview.FileItem;
import com.carlos.common.device.DeviceInfo;
import com.carlos.common.download.DownloadListner;
import com.carlos.common.download.DownloadManager;
import com.carlos.common.network.VNetworkManagerService;
import com.carlos.common.ui.activity.DeviceDetailActiivty;
import com.carlos.common.ui.activity.GDChooseLocationActivity;
import com.carlos.common.ui.activity.abs.nestedadapter.SmartRecyclerAdapter;
import com.carlos.common.ui.activity.base.VerifyActivity;
import com.carlos.common.ui.adapter.MirrorAdapter;
import com.carlos.common.ui.adapter.bean.DeviceData;
import com.carlos.common.ui.adapter.bean.MirrorData;
import com.carlos.common.ui.adapter.decorations.ItemOffsetDecoration;
import com.carlos.common.ui.parse.MirrorDataParse;
import com.carlos.common.utils.AESUtil;
import com.carlos.common.utils.FileTools;
import com.carlos.common.utils.MD5Utils;
import com.carlos.common.utils.ResponseProgram;
import com.carlos.common.utils.SPTools;
import com.carlos.common.utils.ZipTools;
import com.carlos.common.widget.LabelView;
import com.carlos.common.widget.MirrorDialog;
import com.carlos.common.widget.TextProgressBar;
import com.carlos.common.widget.toast.Toasty;
import com.kook.common.utils.HVLog;
import com.kook.librelease.R;
import com.kook.librelease.StringFog;
import com.lody.virtual.client.core.SettingConfig;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.ipc.VLocationManager;
import com.lody.virtual.os.VEnvironment;
import com.lody.virtual.remote.InstalledAppInfo;
import com.lody.virtual.remote.vloc.VLocation;
import java.io.File;
import java.util.List;

public class MirrorActivity
extends VerifyActivity
implements MirrorAdapter.OnAppClickListener {
    private static final String PKG_NAME_ARGUMENT = "MODEL_ARGUMENT";
    private static final String KEY_PKGNAME = "KEY_PKGNAME";
    private static final String APP_NAME = "APP_NAME";
    private static final String KEY_USER = "KEY_USER";
    RecyclerView mMirrorMenuPanel;
    MirrorAdapter mMirrorAdapter;
    AppCompatTextView startApplication;
    AppCompatImageView appIcon;
    AppCompatTextView appNameView;
    AppCompatTextView appVersion;
    LabelView appUserId;
    LabelView appIsExt;
    List<MirrorData> mirrorDataList;
    String packageName;
    String appName;
    int userId;
    public String mMirrorParseFileName = "mirrorParse.text";
    public static final int MENU_TYPE_WIFI = 0;
    public static final int MENU_TYPE_LOC = 1;
    public static final int MENU_TYPE_PHONE = 2;
    public static final int MENU_TYPE_SHORTCUT = 3;
    public static final int MENU_TYPE_DELETE_CLEAR = 4;
    public static final int MENU_TYPE_BACKUP_RECOVERY = 5;
    public static final int LOCATION_CODE = 12;
    public static final int MOCK_PHONE = 1001;
    public static int MENU_TYPE_BACKUP = 7;
    public static int MENU_TYPE_RECOVERY = 8;
    boolean isNetWork = true;

    public static void launch(Context context, String packageName, int userId) {
        Intent intent = VirtualCore.get().getLaunchIntent(packageName, userId);
        if (intent != null) {
            Intent loadingPageIntent = new Intent(context, MirrorActivity.class);
            loadingPageIntent.putExtra(PKG_NAME_ARGUMENT, packageName);
            loadingPageIntent.addFlags(0x10000000);
            loadingPageIntent.putExtra(KEY_PKGNAME, (Parcelable)intent);
            loadingPageIntent.putExtra(KEY_USER, userId);
            HVLog.d("packageName:" + packageName + "   userId:" + userId);
            context.startActivity(loadingPageIntent);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_mirror);
        this.userId = this.getIntent().getIntExtra(KEY_USER, -1);
        this.packageName = this.getIntent().getStringExtra(PKG_NAME_ARGUMENT);
        this.initView();
        this.initData(this.packageName, this.userId);
        HVLog.d("onCreate");
        Application application = this.getApplication();
    }

    @Override
    protected boolean isCheckLog() {
        return true;
    }

    private void initData(String pkg, int userId) {
        this.mirrorDataList = MirrorData.mirrorDataList;
        this.mMirrorAdapter.setList(this.mirrorDataList);
        boolean runInExtProcess = VirtualCore.get().isRunInExtProcess(pkg);
        InstalledAppInfo installedAppInfo = VirtualCore.get().getInstalledAppInfo(pkg, 0);
        ApplicationInfo applicationInfo = installedAppInfo.getApplicationInfo(installedAppInfo.getInstalledUsers()[0]);
        PackageManager pm = this.getPackageManager();
        CharSequence sequence = applicationInfo.loadLabel(pm);
        this.appName = sequence.toString();
        Drawable icon = applicationInfo.loadIcon(pm);
        PackageInfo packageInfo = null;
        try {
            packageInfo = pm.getPackageInfo(pkg, 0);
        }
        catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        this.appIcon.setImageDrawable(icon);
        this.appIcon.setOnClickListener(view -> {
            String apkPath = installedAppInfo.getApkPath();
            File externalFilesDir = this.getExternalFilesDir(this.getPackageName() + "/");
            HVLog.d(externalFilesDir + applicationInfo.packageName + ".apk");
            FileTools.copyFile(apkPath, externalFilesDir + applicationInfo.packageName + ".apk");
        });
        this.appIcon.setOnLongClickListener(new View.OnLongClickListener(){

            public boolean onLongClick(View view) {
                VNetworkManagerService networkManagerService = VNetworkManagerService.get();
                networkManagerService.systemReady(MirrorActivity.this.getContext());
                networkManagerService.randomDevices();
                return true;
            }
        });
        this.appNameView.setText((CharSequence)this.appName);
        if (packageInfo != null) {
            this.appVersion.setText((CharSequence)packageInfo.versionName);
        }
        this.getTitleLeftMenuIcon().setOnClickListener(view -> this.finish());
        if (userId > 0) {
            this.appUserId.setVisibility(0);
            this.appUserId.setText(userId + 1 + "");
        } else {
            this.appUserId.setVisibility(4);
        }
        if (runInExtProcess) {
            this.appIsExt.setVisibility(0);
            this.appIsExt.setText("Ext");
        } else {
            this.appIsExt.setVisibility(4);
        }
    }

    private void initView() {
        this.mMirrorMenuPanel = (RecyclerView)this.findViewById(R.id.menu_panel);
        this.setTitleLeftMenuIcon(R.drawable.icon_back);
        this.setTitleName(R.string.title_mirror_manager);
        this.appIcon = (AppCompatImageView)this.findViewById(R.id.app_icon);
        this.appNameView = (AppCompatTextView)this.findViewById(R.id.app_name);
        this.appVersion = (AppCompatTextView)this.findViewById(R.id.app_version);
        this.appUserId = (LabelView)this.findViewById(R.id.app_user_id);
        this.appIsExt = (LabelView)this.findViewById(R.id.app_is_ext);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, 1);
        this.mMirrorMenuPanel.setLayoutManager((RecyclerView.LayoutManager)layoutManager);
        this.mMirrorAdapter = new MirrorAdapter(this, this.packageName, this.userId);
        SmartRecyclerAdapter wrap = new SmartRecyclerAdapter(this.mMirrorAdapter);
        View footer = new View((Context)this);
        footer.setLayoutParams((ViewGroup.LayoutParams)new StaggeredGridLayoutManager.LayoutParams(-1, ResponseProgram.dpToPx((Context)this, 60)));
        wrap.setFooterView(footer);
        this.mMirrorMenuPanel.setAdapter((RecyclerView.Adapter)wrap);
        this.mMirrorMenuPanel.addItemDecoration((RecyclerView.ItemDecoration)new ItemOffsetDecoration((Context)this, R.dimen.desktop_divider));
        this.mMirrorAdapter.setAppClickListener(this);
        this.startApplication = (AppCompatTextView)this.findViewById(R.id.start_application);
        this.startApplication.setOnClickListener(view -> {
            if (this.isNovatioNecessaria()) {
                Toasty.warning(this.getContext(), this.getString(R.string.update_latest_version)).show();
                return;
            }
            this.checkExtProcessAndlunch(this.userId, this.packageName, this.appName);
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        HVLog.d("onActivityResult  requestCode：" + requestCode + "  resultCode:" + resultCode + "    data:" + data);
        if (requestCode == 12 && resultCode == -1) {
            int position = data.getIntExtra("position", -1);
            HVLog.d("position：" + position);
            this.mMirrorAdapter.notifyItemChanged(position);
        }
        if (requestCode == 1001 && resultCode == -1) {
            this.mMirrorAdapter.notifyDataSetChanged();
        }
        if (resultCode == 1107 && requestCode == 1) {
            String pkg = data.getStringExtra(KEY_PKGNAME);
            String appName = data.getStringExtra(APP_NAME);
            int userId = data.getIntExtra(KEY_USER, -1);
            HVLog.d("startActivity  ： " + pkg + "   userId : " + userId);
            this.launchMirrorApp(userId, this.packageName, appName);
            return;
        }
    }

    @Override
    public void onAppClick(int position, MirrorData model, String tag) {
        if (!this.isNetWork) {
            Toasty.warning((Context)this, "当前网络不可用,请连接网络退出再进").show();
            return;
        }
        switch (model.getMenuType()) {
            case 0: {
                HVLog.d(" model.getMenuType():" + model.getMenuType() + "    position:" + position + "    tsp_mockwifi:" + this.tsp_mockwifi);
                if (this.tsp_mockwifi != 2) {
                    Toasty.warning(this.getContext(), "功能受限、请在设置中联系软件作者qixie0306@gmail.com").show();
                    return;
                }
                this.modifyWifiAddr(position, model);
                break;
            }
            case 1: {
                HVLog.d(" model.getMenuType():" + model.getMenuType() + "    position:" + position + "    tsp_virtuallocation:" + this.tsp_virtuallocation);
                if (this.tsp_virtuallocation != 2) {
                    Toasty.warning(this.getContext(), "功能受限、请在设置中联系软件作者qixie0306@gmail.com").show();
                    return;
                }
                this.startMockLocation(position, model);
                break;
            }
            case 2: {
                if (Build.VERSION.SDK_INT >= 23 && VirtualCore.get().getTargetSdkVersion() >= 23) {
                    ActivityCompat.requestPermissions((Activity)this, (String[])new String[]{"android.permission.READ_PHONE_STATE"}, (int)0);
                }
                HVLog.d(" model.getMenuType():" + model.getMenuType() + "    position:" + position + "    tsp_mockphone:" + this.tsp_mockphone);
                if (this.tsp_mockphone != 2) {
                    Toasty.warning(this.getContext(), "功能受限、请在设置中联系软件作者qixie0306@gmail.com").show();
                    return;
                }
                this.startMockDevice(position, model);
                break;
            }
            case 3: {
                this.createShortcut();
                break;
            }
            case 4: {
                HVLog.d("tag:" + tag);
                if ("BTN1".equals(tag)) {
                    boolean res = VirtualCore.get().cleanPackageData(this.packageName, this.userId);
                    Toast.makeText((Context)this, (CharSequence)("clean app data " + (res ? "success." : "failed.")), (int)0).show();
                    break;
                }
                if (!"BTN2".equals(tag)) break;
                new AlertDialog.Builder(this.getContext()).setTitle(R.string.tip_delete).setMessage((CharSequence)this.getContext().getString(R.string.text_delete_app, new Object[]{this.appName})).setPositiveButton(17039379, (dialog, which) -> {
                    VirtualCore.get().uninstallPackageAsUser(this.packageName, this.userId);
                    this.finish();
                }).setNegativeButton(17039369, null).show();
                break;
            }
            case 5: {
                HVLog.d(" model.getMenuType():" + model.getMenuType() + "    position:" + position + "    MENU_TYPE_BACKUP_RECOVERY:" + 5);
                if (this.tsp_backupRecovery != 2) {
                    Toasty.warning(this.getContext(), "功能受限、请在设置中联系软件作者qixie0306@gmail.com").show();
                    return;
                }
                HVLog.d("tag:" + tag);
                ClouddiskLauncher.getInstance().launcherCloud((Context)this);
                String content = "";
                if ("BTN1".equals(tag)) {
                    content = "备份不超过100M应用数据到云端服务器,数据信息及应用信息都是加密的,如有疑惑请停止使用该功能,备份数据有:\n1:虚拟定位\n2：Wifi信息\n3：机型模拟\n4:应用缓存数据";
                    MirrorDialog.getInstance().showBakupAndRecovery((Activity)this, content, (view, dialog, textProgressBar) -> {
                        textProgressBar.setVisibility(0);
                        HVLog.d("备份好,定位,wifi,机型数据");
                        this.dataBakupAndRecovery(MENU_TYPE_BACKUP, dialog, textProgressBar);
                    });
                    break;
                }
                if (!"BTN2".equals(tag)) break;
                content = "还原数据会将当前分身的数据清除,请输入一个备份数据码";
                MirrorDialog.getInstance().singleRecoveryInputDialog((Activity)this, content, (view, editText, dialog, textProgressBar) -> {
                    String code = editText.getText().toString();
                    if (TextUtils.isEmpty((CharSequence)code)) {
                        this.getHandler().post(() -> Toasty.error((Context)this, "还原数据的码不能为空").show());
                        return;
                    }
                    try {
                        String desDecrypt = AESUtil.desDecrypt(code);
                        HVLog.d("desDecrypt:" + desDecrypt + " ");
                        if (!TextUtils.isEmpty((CharSequence)desDecrypt)) {
                            String[] strings;
                            textProgressBar.setVisibility(0);
                            textProgressBar.setText("开始解析还原码");
                            String fileNameByPath = FileTools.getFileNameByPath(desDecrypt, false);
                            HVLog.d("desDecrypt:" + desDecrypt + "     fileNameByPath:" + fileNameByPath);
                            if (!TextUtils.isEmpty((CharSequence)fileNameByPath) && (strings = fileNameByPath.split("_")).length == 4) {
                                String deviceNo = strings[0];
                                String pkgName = strings[1];
                                String userId = strings[2];
                                String time = strings[3];
                                String currentDate = ClouddiskLauncher.getCurrentDate(time);
                                HVLog.d("deviceNo:" + deviceNo + "  pkgName:" + pkgName + "    userId:" + userId + "    time:" + time + "   currentDate:" + currentDate);
                                if (this.packageName.equals(pkgName)) {
                                    textProgressBar.setText("下载数据包");
                                    boolean res = VirtualCore.get().cleanPackageData(this.packageName, this.userId);
                                    Toast.makeText((Context)this, (CharSequence)("clean app data " + (res ? "success." : "failed.")), (int)0).show();
                                    ClouddiskLauncher.getInstance().launcherCloud((Context)this, currentDate, fileItemList -> {
                                        final String fileName = FileTools.getFileNameByPath(desDecrypt, true);
                                        for (FileItem fileItem : fileItemList) {
                                            HVLog.d("fileName: " + fileName + "  " + fileItem.toString());
                                            if (!fileName.equals(fileItem.getFilename())) continue;
                                            HVLog.d("找到了要下载的文件列表 Z");
                                            ClouddiskLauncher.getInstance().downFileByCloud(fileItem.getId()).done(downloadLink -> {
                                                if (downloadLink == null || TextUtils.isEmpty((CharSequence)downloadLink.getDlLink())) {
                                                    this.getHandler().post(() -> {
                                                        dialog.dismiss();
                                                        Toasty.warning((Context)this, "还原功能异常").show();
                                                    });
                                                } else {
                                                    HVLog.d("  开始下载 ");
                                                    DownloadManager mDownloadManager = DownloadManager.getInstance();
                                                    final File dataUserPackageDirectory = VEnvironment.getDataUserPackageDirectory(this.userId, this.packageName);
                                                    if (!dataUserPackageDirectory.exists()) {
                                                        dataUserPackageDirectory.mkdirs();
                                                    }
                                                    mDownloadManager.add(this.getContext(), downloadLink.getDlLink(), dataUserPackageDirectory.getPath(), fileName, new DownloadListner(){

                                                        @Override
                                                        public void onFinished() {
                                                            MirrorActivity.this.getHandler().post(() -> {
                                                                Toasty.success(MirrorActivity.this.getContext(), "下载完成!", 0).show();
                                                                File file = new File(dataUserPackageDirectory.getPath(), fileName);
                                                                HVLog.d("file 下载完成 " + file.getAbsolutePath() + "  file is exists :" + file.exists());
                                                                String fileMD5Sync = MD5Utils.fileMD5Sync(file);
                                                                HVLog.d("fileMD5Sync:" + fileMD5Sync);
                                                                int i = ZipTools.uncompressZip(file.getAbsolutePath(), dataUserPackageDirectory.getPath(), zipName -> MirrorActivity.this.getHandler().post(() -> textProgressBar.setText("解压" + zipName)));
                                                                if (i == 0) {
                                                                    HVLog.d("解压完成");
                                                                    MirrorDataParse mirrorDataParse = new MirrorDataParse();
                                                                    mirrorDataParse.parseBackupData(dataUserPackageDirectory.getPath() + "/" + MirrorActivity.this.mMirrorParseFileName);
                                                                }
                                                                dialog.dismiss();
                                                                MirrorActivity.this.mMirrorAdapter.notifyDataSetChanged();
                                                            });
                                                        }

                                                        @Override
                                                        public void onProgress(float progress) {
                                                            MirrorActivity.this.getHandler().post(() -> textProgressBar.setProgress((int)progress));
                                                        }

                                                        @Override
                                                        public void onPause() {
                                                            MirrorActivity.this.getHandler().post(() -> Toast.makeText((Context)MirrorActivity.this.getContext(), (CharSequence)"暂停了!", (int)0).show());
                                                        }

                                                        @Override
                                                        public void onCancel() {
                                                            MirrorActivity.this.getHandler().post(() -> {
                                                                dialog.dismiss();
                                                                textProgressBar.setText("0%");
                                                                textProgressBar.setProgress(0);
                                                                textProgressBar.setText("下载失败");
                                                                Toast.makeText((Context)MirrorActivity.this.getContext(), (CharSequence)"下载异常被取消!", (int)0).show();
                                                            });
                                                        }
                                                    });
                                                    mDownloadManager.downloadSingle(downloadLink.getDlLink());
                                                }
                                            });
                                            break;
                                        }
                                    });
                                } else {
                                    this.getHandler().post(() -> Toasty.warning((Context)this, "备份数据不能适用当前<" + this.appName + ">应用").show());
                                }
                            }
                        }
                    }
                    catch (Exception e) {
                        this.getHandler().post(() -> Toasty.error((Context)this, "请输入正确的备份数据码").show());
                    }
                });
            }
        }
    }

    public void dataBakupAndRecovery(int ITEM_TYPE, final Dialog dialog, final TextProgressBar textProgressBar) {
        DeviceInfo instance = DeviceInfo.getInstance((Context)this);
        String devicesNo = instance.getDevicesNo();
        textProgressBar.setText("开始处理应用数据");
        File dataUserPackageDirectory = VEnvironment.getDataUserPackageDirectory(this.userId, this.packageName);
        HVLog.d(" 应用数据目录 ：" + dataUserPackageDirectory.getAbsolutePath() + "   是否存在：" + dataUserPackageDirectory.exists());
        if (!dataUserPackageDirectory.exists()) {
            this.getHandler().post(() -> {
                Toasty.error((Context)this, "当前应用数据为空").show();
                dialog.dismiss();
            });
            return;
        }
        String currentDateFolderId = ClouddiskLauncher.getInstance().getCurrentDateFolderId();
        MirrorDataParse mirrorDataParse = new MirrorDataParse();
        String backupData = mirrorDataParse.getBackupData(this.packageName, this.userId);
        FileTools.saveAsFileWriter(dataUserPackageDirectory.getAbsolutePath() + "/" + this.mMirrorParseFileName, backupData);
        HVLog.d("dataUserPackageDirectory:" + dataUserPackageDirectory.getAbsolutePath());
        long currentTimeMillis = System.currentTimeMillis();
        final String zipFile = this.getSavePath() + devicesNo + "_" + this.packageName + "_" + this.userId + "_" + currentTimeMillis + ".zip";
        final String desEncrypt = AESUtil.desEncrypt(zipFile);
        HVLog.d("zipFile:" + zipFile + "    加密:" + desEncrypt);
        String desDecrypt = AESUtil.desDecrypt(desEncrypt);
        HVLog.d("解密 desDecrypt：" + desDecrypt);
        ResponseProgram.defer().when(() -> {
            try {
                if (ITEM_TYPE == MENU_TYPE_BACKUP) {
                    HVLog.d("开始压缩:");
                    textProgressBar.setText("开始压缩");
                    ZipTools.compressZip(dataUserPackageDirectory.getAbsolutePath(), zipFile, name -> this.getHandler().post(() -> textProgressBar.setText("开始压缩" + name)));
                    textProgressBar.setProgress(10);
                    HVLog.d("压缩完成:" + zipFile);
                    textProgressBar.setText("压缩完成");
                    int maxlength = 0x6300000;
                    File file = new File(zipFile);
                    long sizeKb = file.length() / 1024L;
                    long sizeMb = sizeKb / 1024L;
                    HVLog.d("文件大小：" + sizeKb + "kb   sizeMb:" + sizeMb + " MB");
                    if (file.length() >= (long)maxlength) {
                        this.getHandler().post(() -> MirrorDialog.getInstance().tipsSingleDialog((Activity)this, "应用数据超过100MB,当前" + sizeMb + "MB,不能上传", new MirrorDialog.SingleDialogClickListener(){

                            @Override
                            public void onClick(View view, Dialog exitDialog) {
                                exitDialog.dismiss();
                            }
                        }));
                        dialog.dismiss();
                        return;
                    }
                    ClouddiskLauncher.getInstance().updaterCloud(zipFile, currentDateFolderId, new HttpWorker.UpLoadCallbackListener(){
                        long time = 0L;

                        @Override
                        public void onError(int count) {
                            MirrorActivity.this.getHandler().post(() -> {
                                textProgressBar.setText("上传至" + count + "错误");
                                HVLog.d("onError 删除备份的压缩文件:" + zipFile);
                                FileTools.delete(zipFile);
                                dialog.dismiss();
                                MirrorDialog.getInstance().tipsSingleDialog((Activity)MirrorActivity.this, "上传至" + count + "错误,请退出", new MirrorDialog.SingleDialogClickListener(){

                                    @Override
                                    public void onClick(View view, Dialog exitDialog) {
                                        exitDialog.dismiss();
                                    }
                                });
                            });
                        }

                        @Override
                        public void Progress(double progress) {
                            long timeMillis = System.currentTimeMillis();
                            if (timeMillis - this.time > 2000L) {
                                HVLog.d("Progress 上传了:" + progress);
                                this.time = timeMillis;
                            }
                            if (textProgressBar != null) {
                                MirrorActivity.this.getHandler().post(() -> textProgressBar.setProgress((int)progress));
                            }
                            if (progress == 100.0) {
                                HVLog.d("onFinish 删除备份的压缩文件:" + zipFile);
                                FileTools.delete(zipFile);
                                MirrorActivity.this.getHandler().post(() -> {
                                    dialog.dismiss();
                                    MirrorDialog.getInstance().tipsSingleDialog((Activity)MirrorActivity.this, "请记住'" + desEncrypt + "'码,该码用于还原数据使用,确认复制", new MirrorDialog.SingleDialogClickListener(){

                                        @Override
                                        public void onClick(View view, Dialog exitDialog) {
                                            exitDialog.dismiss();
                                            ClipboardManager cmb = (ClipboardManager)MirrorActivity.this.getSystemService("clipboard");
                                            cmb.setText((CharSequence)desEncrypt);
                                            Toasty.success((Context)MirrorActivity.this, "编码已复制", 1).show();
                                        }
                                    });
                                });
                            }
                        }

                        @Override
                        public void onFinish(int count) {
                            MirrorActivity.this.getHandler().post(() -> {
                                if (textProgressBar != null) {
                                    textProgressBar.setText("上传" + count + "完成");
                                }
                                HVLog.d("onFinish 删除备份的压缩文件:" + zipFile);
                                FileTools.delete(zipFile);
                                dialog.dismiss();
                            });
                        }
                    });
                }
                if (ITEM_TYPE == MENU_TYPE_RECOVERY) {
                    HVLog.d("开始还原");
                    HVLog.d("还原完成:" + zipFile);
                }
            }
            catch (Exception e) {
                HVLog.printException(e);
            }
        });
    }

    public void startMockDevice(int position, MirrorData model) {
        DeviceData deviceData = new DeviceData(this.getContext(), null, this.userId);
        int count = this.userId + 1;
        deviceData.name = this.getString(R.string.menu_mock_phone) + "(" + this.appName + (this.userId == 0 ? "" : "-" + count) + ")";
        deviceData.packageName = this.packageName;
        DeviceDetailActiivty.open((Activity)this, deviceData, position);
    }

    public void createShortcut() {
        HVLog.d("创建快捷方式");
        VirtualCore.OnEmitShortcutListener listener = new VirtualCore.OnEmitShortcutListener(){

            @Override
            public Bitmap getIcon(Bitmap originIcon) {
                return originIcon;
            }

            @Override
            public String getName(String originName) {
                return originName;
            }
        };
        boolean shortcut = VirtualCore.get().createShortcut(this.userId, this.packageName, listener);
        Toast.makeText((Context)this.getContext(), (CharSequence)("创建" + (shortcut ? "成功" : "失败")), (int)0).show();
    }

    public void startMockLocation(int position, MirrorData model) {
        VLocation location = VLocationManager.get().getLocation(this.packageName, this.userId);
        Intent intent = new Intent(this.getContext(), GDChooseLocationActivity.class);
        if (location != null) {
            intent.putExtra("virtual_location", (Parcelable)location);
        }
        intent.putExtra("virtual.extras.package", this.packageName);
        intent.putExtra("virtual.extras.userid", this.userId);
        intent.putExtra("position", position);
        this.startActivityForResult(intent, 12);
    }

    public void modifyWifiAddr(int position, MirrorData model) {
        String SSID_KEY = "ssid_key" + this.packageName + "_" + this.userId;
        String MAC_KEY = "mac_key" + this.packageName + "_" + this.userId;
        SettingConfig.FakeWifiStatus fakeWifiStatus = App.getApp().mConfig.getFakeWifiStatus(this.packageName, this.userId);
        AlertDialog.Builder builder = new AlertDialog.Builder((Context)this, R.style.VACustomTheme);
        View view1 = this.getLayoutInflater().inflate(R.layout.dialog_change_wifi, null);
        builder.setView(view1);
        AlertDialog dialog = builder.show();
        dialog.setCanceledOnTouchOutside(false);
        EditText editText1 = (EditText)view1.findViewById(R.id.edt_ssid);
        if (fakeWifiStatus != null) {
            editText1.setText((CharSequence)fakeWifiStatus.getSSID());
        } else {
            editText1.setText((CharSequence)SettingConfig.FakeWifiStatus.DEFAULT_SSID);
        }
        EditText editText2 = (EditText)view1.findViewById(R.id.edt_mac);
        if (fakeWifiStatus != null) {
            editText2.setText((CharSequence)fakeWifiStatus.getMAC());
        } else {
            editText2.setText((CharSequence)SettingConfig.FakeWifiStatus.DEFAULT_MAC);
        }
        dialog.setCancelable(false);
        view1.findViewById(R.id.btn_cancel).setOnClickListener(arg_0 -> MirrorActivity.lambda$modifyWifiAddr$17((Dialog)dialog, arg_0));
        view1.findViewById(R.id.btn_ok).setOnClickListener(arg_0 -> this.lambda$modifyWifiAddr$18((Dialog)dialog, editText1, editText2, SSID_KEY, MAC_KEY, position, arg_0));
    }

    private /* synthetic */ void lambda$modifyWifiAddr$18(Dialog dialog, EditText editText1, EditText editText2, String SSID_KEY, String MAC_KEY, int position, View v2) {
        dialog.dismiss();
        try {
            HVLog.d("模拟wifi ok");
            String ssid = editText1.getText().toString();
            String mac = editText2.getText().toString();
            SPTools.putString(this.getContext(), SSID_KEY, ssid);
            SPTools.putString(this.getContext(), MAC_KEY, mac);
            this.mMirrorAdapter.notifyItemChanged(position);
        }
        catch (Exception e) {
            Toast.makeText((Context)this.getContext(), (int)R.string.input_loc_error, (int)0).show();
        }
    }

    private static /* synthetic */ void lambda$modifyWifiAddr$17(Dialog dialog, View v2) {
        HVLog.d("模拟wifi cancel");
        dialog.dismiss();
    }
}

