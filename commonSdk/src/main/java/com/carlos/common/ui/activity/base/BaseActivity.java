/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.app.AlertDialog
 *  android.app.AlertDialog$Builder
 *  android.app.Dialog
 *  android.content.Context
 *  android.content.Intent
 *  android.content.pm.PackageInfo
 *  android.content.pm.PackageManager
 *  android.net.Uri
 *  android.os.Build$VERSION
 *  android.os.Bundle
 *  android.os.Environment
 *  android.os.Handler
 *  android.text.TextUtils
 *  android.view.View
 *  android.view.View$OnClickListener
 *  android.view.inputmethod.InputMethodManager
 *  android.widget.ImageView
 *  android.widget.TextView
 *  androidx.annotation.DrawableRes
 *  androidx.annotation.StringRes
 *  androidx.appcompat.app.AppCompatActivity
 *  androidx.core.content.FileProvider
 *  com.kook.librelease.R$color
 *  com.kook.librelease.R$id
 *  com.kook.librelease.R$layout
 *  com.kook.librelease.R$style
 */
package com.carlos.common.ui.activity.base;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import com.carlos.common.device.DeviceInfo;
import com.carlos.common.download.DownloadListner;
import com.carlos.common.download.DownloadManager;
import com.carlos.common.network.VNetworkManagerService;
import com.carlos.common.persistent.StoragePersistenceServices;
import com.carlos.common.persistent.VPersistent;
import com.carlos.common.ui.adapter.bean.SoftVersions;
import com.carlos.common.ui.utils.StatusBarUtil;
import com.carlos.common.utils.MD5Utils;
import com.kook.common.utils.HVLog;
import com.kook.deviceinfo.DeviceSplash;
import com.kook.librelease.R;
import com.kook.librelease.StringFog;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BaseActivity
extends AppCompatActivity
implements View.OnClickListener {
    String TAG = BaseActivity.class.getSimpleName();
    public static final int DOWNLOAD_FAIL = 0;
    public static final int DOWNLOAD_PROGRESS = 1;
    public static final int DOWNLOAD_SUCCESS = 2;
    Handler mHandler = new Handler();
    protected SoftVersions mSoftVersions;
    DeviceSplash mDeviceSplash;
    VNetworkManagerService networkManagerService;
    protected int tsp_virtualbox = 0;
    protected int tsp_dingtalk = 0;
    protected int tsp_dingtalkPic = 0;
    protected int tsp_mockphone = 0;
    protected int tsp_mockwifi = 0;
    protected int tsp_virtuallocation = 0;
    protected int tsp_hookXposed = 0;
    protected int tsp_backupRecovery = 0;
    protected int channelLimit = 0;
    protected int channelStatus = 0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = this.getIntent();
        this.setStatusBar();
        if (this.isCheckLog()) {
            this.networkManagerService = VNetworkManagerService.get();
            this.networkManagerService.systemReady((Context)this);
            this.networkManagerService.devicesLog();
            if (this.mDeviceSplash == null) {
                this.mDeviceSplash = new DeviceSplash();
            }
            this.mDeviceSplash.attachBaseApplication((Activity)this);
        }
        if (this.isCheckLog()) {
            this.tsp_virtualbox = this.getPersistentValueToInt("virtualbox");
            this.tsp_dingtalk = this.getPersistentValueToInt("dingtalk");
            this.tsp_dingtalkPic = this.getPersistentValueToInt("dingtalkPic");
            this.tsp_mockphone = this.getPersistentValueToInt("mockphone");
            this.tsp_mockwifi = this.getPersistentValueToInt("mockwifi");
            this.tsp_virtuallocation = this.getPersistentValueToInt("virtuallocation");
            this.tsp_hookXposed = this.getPersistentValueToInt("hookXposed");
            this.tsp_backupRecovery = this.getPersistentValueToInt("backupRecovery");
            this.channelLimit = this.getPersistentValueToInt("channelLimit");
            this.channelStatus = this.getPersistentValueToInt("channelStatus");
        }
        this.checkUpgrade();
    }

    protected boolean checkUpgrade() {
        DeviceInfo deviceInfo = DeviceInfo.getInstance((Context)this);
        int versionCode = deviceInfo.getVersionCode();
        String versionName = deviceInfo.getVersionName((Context)this);
        int upgradeEnforce = this.getPersistentValueToInt("upgradeEnforce");
        int upgradeVersion = this.getPersistentValueToInt("upgradeVersion");
        String fileName = this.getVPersistent().getBuildConfig(VPersistent.fileName);
        VPersistent persistent = this.getVPersistent();
        HVLog.d("versionCode:" + versionCode + "    upgradeVersion:" + upgradeVersion);
        if (versionCode < upgradeVersion) {
            String appConfigMd5 = persistent.getBuildConfig("fileMd5");
            String localApk = this.getFilesDir().getAbsolutePath() + "/Download/" + fileName;
            File apkFile = new File(localApk);
            String fileMD5Sync = MD5Utils.fileMD5Sync(apkFile);
            HVLog.d("fileMD5Sync:" + fileMD5Sync + "    apkFile:" + apkFile.exists() + "   localApk:" + localApk + "    appConfigMd5:" + appConfigMd5);
            if (fileMD5Sync.equals(appConfigMd5)) {
                HVLog.d("文件下载成功");
                this.installApkWindow(localApk);
                return true;
            }
        }
        return false;
    }

    private void installApkWindow(String filePath) {
        AlertDialog.Builder builder = new AlertDialog.Builder((Context)this, R.style.VACustomTheme);
        View view1 = this.getLayoutInflater().inflate(R.layout.dialog_tips, null);
        builder.setView(view1);
        AlertDialog dialog = builder.show();
        dialog.setCanceledOnTouchOutside(false);
        TextView textView = (TextView)view1.findViewById(R.id.tips_content);
        textView.setText((CharSequence)"有新版本更新，请更新安装");
        dialog.setCancelable(false);
        view1.findViewById(R.id.double_btn_layout).setVisibility(0);
        view1.findViewById(R.id.btn_cancel).setOnClickListener(arg_0 -> this.lambda$installApkWindow$0((Dialog)dialog, arg_0));
        view1.findViewById(R.id.btn_ok).setOnClickListener(arg_0 -> this.lambda$installApkWindow$1((Dialog)dialog, filePath, arg_0));
    }

    int getPersistentValueToInt(String key) {
        VPersistent persistent = this.getVPersistent();
        Map<String, String> buildAllConfig = persistent.buildAllConfig;
        String value = buildAllConfig.get(key);
        if (!TextUtils.isEmpty((CharSequence)value)) {
            try {
                return Integer.parseInt(value);
            }
            catch (Exception e) {
                return 0;
            }
        }
        return 0;
    }

    VPersistent getVPersistent() {
        StoragePersistenceServices storagePersistenceServices = StoragePersistenceServices.get();
        VPersistent persistent = storagePersistenceServices.getVPersistent();
        return persistent;
    }

    protected boolean isCheckLog() {
        return false;
    }

    protected void setStatusBar() {
        StatusBarUtil.setColor((Activity)this, this.getResources().getColor(R.color.color_6bc196), 1);
    }

    protected void setTitleName(@StringRes int res) {
        TextView title = (TextView)this.findViewById(R.id.toolbar_title);
        if (title != null) {
            title.setText(res);
        }
    }

    protected void setTitleName(String res) {
        TextView title = (TextView)this.findViewById(R.id.toolbar_title);
        if (title != null) {
            title.setText((CharSequence)res);
        }
    }

    protected ImageView getTitleLeftMenuIcon() {
        ImageView leftIv = (ImageView)this.findViewById(R.id.toolbar_left_menu);
        return leftIv;
    }

    protected void setTitleLeftMenuIcon(@DrawableRes int res) {
        ImageView leftIv = (ImageView)this.findViewById(R.id.toolbar_left_menu);
        leftIv.setImageResource(res);
    }

    public boolean isInstallAppByPackageName(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();
        List packageInfos = packageManager.getInstalledPackages(0);
        ArrayList<String> packageNames = new ArrayList<String>();
        if (packageInfos != null) {
            for (int i = 0; i < packageInfos.size(); ++i) {
                String packName = ((PackageInfo)packageInfos.get((int)i)).packageName;
                packageNames.add(packName);
            }
        }
        return packageNames.contains(packageName);
    }

    protected void onDestroy() {
        super.onDestroy();
    }

    public void finish() {
        InputMethodManager manager;
        View view = this.getCurrentFocus();
        if (view != null && (manager = (InputMethodManager)this.getSystemService("input_method")) != null) {
            manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        super.finish();
    }

    public void onClick(View v) {
    }

    public Handler getHandler() {
        return this.mHandler;
    }

    public static int getDeviceId(String pkg, int userId) {
        int hashCode = pkg.hashCode();
        return hashCode + userId;
    }

    public String getSavePath() {
        String path = Build.VERSION.SDK_INT > 29 ? this.getExternalFilesDir(null).getAbsolutePath() + "/" : Environment.getExternalStorageDirectory().getPath() + "/";
        return path;
    }

    private SoftVersions getSoftVersions() {
        return this.mSoftVersions;
    }

    public boolean isUpgrade() {
        String versionsNumber;
        SoftVersions softVersions = this.getSoftVersions();
        if (softVersions != null && !TextUtils.isEmpty((CharSequence)(versionsNumber = softVersions.getNumber()))) {
            int versionNumber = Integer.parseInt(versionsNumber);
            int versionCode = DeviceInfo.getInstance((Context)this).getVersionCode((Context)this);
            HVLog.d("isUpgrade versionsNumber:" + versionsNumber + "    本地版本号：" + versionCode);
            if (versionCode < versionNumber) {
                this.downloadVersion();
                return true;
            }
        }
        return false;
    }

    protected boolean isNovatioNecessaria() {
        SoftVersions softVersions = this.getSoftVersions();
        if (softVersions != null) {
            return softVersions.novatioNecessaria == 1;
        }
        return false;
    }

    private void downloadVersion() {
        SoftVersions softVersions = this.getSoftVersions();
        if (softVersions != null) {
            String updateUrl = softVersions.getUpdateUrl();
            DownloadManager downloadManager = DownloadManager.getInstance();
            downloadManager.add((Context)this, updateUrl, this.getDataDir().getAbsolutePath(), "app" + softVersions.getNumber() + ".apk", new DownloadListner(){

                @Override
                public void onFinished() {
                    HVLog.d(" onFinished ");
                }

                @Override
                public void onProgress(float progress) {
                    HVLog.d("progress:" + progress);
                }

                @Override
                public void onPause() {
                    HVLog.d("版本 升级停止 onPause:");
                }

                @Override
                public void onCancel() {
                    HVLog.d("版本 升级停止 onCancel:");
                }
            });
            downloadManager.downloadSingle(updateUrl);
        }
    }

    public boolean isNetwork() {
        try {
            URL url = new URL("https://www.baidu.com");
            InputStream stream = url.openStream();
            HVLog.d("isNetwork");
            return true;
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    protected void install(File file) {
        try {
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.addFlags(0x10000000);
            intent.addFlags(1);
            if (Build.VERSION.SDK_INT >= 24) {
                String authority = this.getPackageName() + ".provider";
                Uri uri = FileProvider.getUriForFile((Context)this, (String)this.getPackageName().concat(".provider"), (File)file);
                intent.setDataAndType(uri, "application/vnd.android.package-archive");
            } else {
                Uri uri = Uri.fromFile((File)file);
                intent.setDataAndType(uri, "application/vnd.android.package-archive");
            }
            this.startActivity(intent);
        }
        catch (Exception e) {
            HVLog.printException(e);
        }
    }

    private /* synthetic */ void lambda$installApkWindow$1(Dialog dialog, String filePath, View v2) {
        dialog.dismiss();
        File apkFile = new File(filePath);
        this.install(apkFile);
    }

    private /* synthetic */ void lambda$installApkWindow$0(Dialog dialog, View v2) {
        dialog.dismiss();
        this.finish();
    }
}

