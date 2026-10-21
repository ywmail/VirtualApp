/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.annotation.TargetApi
 *  android.app.Activity
 *  android.app.AlertDialog$Builder
 *  android.app.Application
 *  android.app.Dialog
 *  android.content.Context
 *  android.content.Intent
 *  android.content.pm.ApplicationInfo
 *  android.content.pm.PackageInfo
 *  android.content.pm.PackageManager$NameNotFoundException
 *  android.net.Uri
 *  android.os.Build$VERSION
 *  android.os.Bundle
 *  android.os.Environment
 *  android.provider.Settings
 *  android.view.LayoutInflater
 *  android.view.View
 *  android.view.View$OnClickListener
 *  android.widget.TextView
 *  android.widget.Toast
 *  androidx.appcompat.app.AlertDialog$Builder
 *  com.google.android.material.bottomsheet.BottomSheetDialog
 *  com.kook.librelease.R$id
 *  com.kook.librelease.R$layout
 *  com.kook.librelease.R$string
 *  com.kook.librelease.R$style
 */
package com.carlos.common.ui.activity.base;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.carlos.common.clouddisk.listview.FileItem;
import com.carlos.common.ui.activity.base.PermissionRequestActivity;
import com.carlos.common.ui.activity.base.VActivity;
import com.carlos.common.utils.InstallTools;
import com.carlos.common.utils.SPTools;
import com.carlos.common.widget.BottomSheetLayout;
import com.carlos.common.widget.toast.Toasty;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.kook.common.utils.HVLog;
import com.kook.librelease.R;
import com.kook.librelease.StringFog;
import com.lody.virtual.client.core.AppCallback;
import com.lody.virtual.client.core.AppLauncherCallback;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.ipc.VActivityManager;
import com.lody.virtual.client.ipc.VPackageManager;
import com.lody.virtual.client.stub.RequestExternalStorageManagerActivity;
import com.lody.virtual.helper.compat.BuildCompat;
import com.lody.virtual.helper.compat.PermissionCompat;
import com.lody.virtual.helper.utils.FileUtils;
import com.lody.virtual.oem.OemPermissionHelper;
import com.lody.virtual.remote.InstalledAppInfo;
import com.lody.virtual.server.extension.VExtPackageAccessor;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class VerifyActivity
extends VActivity
implements AppLauncherCallback {
    private static String META_DATA_KEY;
    BottomSheetDialog bottomSheetDialog;
    BottomSheetLayout bottomSheetLayout;
    private static final String PKG_NAME_ARGUMENT;
    private static final String KEY_PKGNAME;
    private static final String APP_NAME;
    private static final String KEY_USER;
    protected final int ACTION_REQUEST_CODE_LAUNCH = 1;
    protected ViewOnclick mViewOnclick = new ViewOnclick();
    protected Dialog mDialog;
    AlertDialog.Builder mBuilder;
    String[] whitelist = new String[]{".doc", ".docx", ".zip", ".rar", ".apk", ".ipa", ".txt", ".exe", ".7z", ".e", ".z", ".ct", ".ke", ".cetrainer", ".db", ".tar", ".pdf", ".w3x", ".epub", ".mobi", ".azw", ".azw3", ".osk", ".osz", ".xpa", ".cpk", ".lua", ".jar", ".dmg", ".ppt", ".pptx", ".xls", ".xlsx", ".mp3", ".ipa", ".iso", ".img", ".gho", ".ttf", ".ttc", ".txf", ".dwg", ".bat", ".dll"};
    private List<String> history = new ArrayList<String>();
    private List<FileItem> currentFile = new ArrayList<FileItem>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VirtualCore.get().setAppCallback(new AppCallback(){

            @Override
            public void beforeStartApplication(String packageName, String processName, Context context) {
            }

            @Override
            public void beforeApplicationCreate(String packageName, String processName, Application application) {
            }

            @Override
            public void afterApplicationCreate(String packageName, String processName, Application application) {
            }

            @Override
            public void beforeActivityOnCreate(Activity activity) {
            }

            @Override
            public void afterActivityOnCreate(Activity activity) {
            }

            @Override
            public void beforeActivityOnStart(Activity activity) {
            }

            @Override
            public void afterActivityOnStart(Activity activity) {
            }

            @Override
            public void beforeActivityOnResume(Activity activity) {
            }

            @Override
            public void afterActivityOnResume(Activity activity) {
            }

            @Override
            public void beforeActivityOnStop(Activity activity) {
            }

            @Override
            public void afterActivityOnStop(Activity activity) {
            }

            @Override
            public void beforeActivityOnDestroy(Activity activity) {
            }

            @Override
            public void afterActivityOnDestroy(Activity activity) {
            }
        });
    }

    public void tipsDialog(String content, View.OnClickListener ... onclick) {
        if (this.mDialog == null) {
            this.mBuilder = new AlertDialog.Builder((Context)this, R.style.VACustomTheme);
            View view1 = this.getLayoutInflater().inflate(R.layout.dialog_tips, null);
            this.mBuilder.setView(view1);
            if (!this.isFinishing()) {
                this.mDialog = this.mBuilder.show();
            }
            if (this.mDialog == null) {
                return;
            }
            this.mDialog.setCanceledOnTouchOutside(false);
            TextView textView = (TextView)view1.findViewById(R.id.tips_content);
            textView.setText((CharSequence)content);
            this.mDialog.setCancelable(false);
            if (onclick != null && onclick.length == 2) {
                view1.findViewById(R.id.double_btn_layout).setVisibility(View.VISIBLE);
                view1.findViewById(R.id.btn_cancel).setOnClickListener(onclick[0]);
                view1.findViewById(R.id.btn_ok).setOnClickListener(onclick[0]);
            } else if (onclick != null && onclick.length == 1) {
                view1.findViewById(R.id.single_btn_layout).setVisibility(View.VISIBLE);
                view1.findViewById(R.id.single_btn).setOnClickListener(onclick[0]);
            }
        } else {
            this.mDialog.show();
        }
    }

    private String getMetaDataFromApp(Context context, String meta) {
        String value = "";
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128);
            value = appInfo.metaData.getString(meta);
        }
        catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return value;
    }

    protected void checkExtProcessAndlunch(int userId, String packageName, String appName) {
        if (this.bottomSheetLayout == null) {
            this.bottomSheetLayout = (BottomSheetLayout)LayoutInflater.from((Context)this.getContext()).inflate(R.layout.layout_bottom_sheet, null);
        }
        if (this.bottomSheetDialog == null) {
            this.bottomSheetDialog = new BottomSheetDialog((Context)this, R.style.BottomSheetDialog);
            this.bottomSheetDialog.setContentView((View)this.bottomSheetLayout);
        }
        this.bottomSheetLayout.beginShow(packageName);
        try {
            this.bottomSheetDialog.show();
        } catch (Exception e) {

        }
        this.launchMirrorApp(userId, packageName, appName);
    }

    protected void launchMirrorApp(int userId, String packageName, String appName) {
        block18: {
            if (VirtualCore.get().isRunInExtProcess(packageName)) {
                if (!VirtualCore.get().isExtPackageInstalled()) {
                    Toast.makeText((Context)this, (CharSequence)"Please install Extension Package.", (int)0).show();
                    return;
                }
                if (!VExtPackageAccessor.hasExtPackageBootPermission()) {
                    Toast.makeText((Context)this, (int)R.string.permission_boot_content, (int)0).show();
                    return;
                }
            }
            try {
                if (userId == -1 || packageName == null) break block18;
                boolean runAppNow = true;
                if (Build.VERSION.SDK_INT >= 23) {
                    String[] permissions;
                    InstalledAppInfo info = VirtualCore.get().getInstalledAppInfo(packageName, userId);
                    ApplicationInfo applicationInfo = info.getApplicationInfo(userId);
                    boolean isExt = VirtualCore.get().isRunInExtProcess(info.packageName);
                    int runHostTargetSdkVersion = VirtualCore.get().getHostApplicationInfo().targetSdkVersion;
                    if (isExt) {
                        try {
                            runHostTargetSdkVersion = this.getPackageManager().getApplicationInfo((String)VirtualCore.getConfig().getExtPackageName(), (int)0).targetSdkVersion;
                        }
                        catch (Exception exception) {
                            // empty catch block
                        }
                        if (this.checkExtPackageBootPermission()) {
                            return;
                        }
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        if (BuildCompat.isR() && runHostTargetSdkVersion >= 30 && info.getApplicationInfo((int)0).targetSdkVersion < 30 && (isExt && !VExtPackageAccessor.isExternalStorageManager() || !isExt && !Environment.isExternalStorageManager())) {
                            new AlertDialog.Builder(this.getContext()).setTitle(R.string.permission_boot_notice).setMessage(R.string.request_external_storage_manager_notice).setCancelable(false).setNegativeButton((CharSequence)"GO", (dialog, which) -> RequestExternalStorageManagerActivity.request(VirtualCore.get().getContext(), isExt)).show();
                            return;
                        }
                    }
                    if (PermissionCompat.isCheckPermissionRequired(applicationInfo) && !PermissionCompat.checkPermissions(permissions = VPackageManager.get().getDangerousPermissions(info.packageName), isExt)) {
                        runAppNow = false;
                        PermissionRequestActivity.requestPermission(this.getActivity(), permissions, appName, userId, packageName, 6);
                    }
                }
                HVLog.d(" runAppNow :" + runAppNow);
                if (runAppNow) {
                    this.channelLimit = this.getPersistentValueToInt("channelLimit");
                    this.channelStatus = this.getPersistentValueToInt("channelStatus");
                    int channelLimitLocal = SPTools.getInt((Context)this, "channelLimit", 0);
                    /*
                    long currentTimeMillisLimit = 0L;
                    if (channelLimitLocal == 0) {
                        SPTools.putLong((Context)this, "currentTimeMillisLimit", System.currentTimeMillis());
                    } else {
                        currentTimeMillisLimit = SPTools.getLong((Context)this, "currentTimeMillisLimit");
                    }
                    HVLog.d("channelLimitLocal:" + channelLimitLocal + "    channelLimit:" + this.channelLimit);
                    HVLog.d("channelStatus:" + this.channelStatus + "    ");
                    if (this.channelLimit <= channelLimitLocal) {
                        Toasty.warning(this.getContext(), "功能受限、请在设置中联系软件作者qixie0306@gmail.com").show();
                        this.finish();
                        return;
                    }
                    if (this.channelStatus == 0) {
                        Toasty.warning(this.getContext(), "功能受限、请在设置中联系软件作者qixie0306@gmail.com").show();
                        this.finish();
                        return;
                    }
                     */
                    if (!this.checkUpgrade()) {
                        VActivityManager.get().launchApp(userId, packageName);
                    }
                    SPTools.putInt((Context)this, "channelLimit", channelLimitLocal + 1);
                    this.finish();
                }
            }
            catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    private boolean checkExtPackageBootPermission() {
        if (VirtualCore.get().isExtPackageInstalled()) {
            if (!VExtPackageAccessor.hasExtPackageBootPermission()) {
                this.showPermissionDialog();
                return true;
            }
            if (BuildCompat.isQ() && !Settings.canDrawOverlays((Context)this.getActivity())) {
                this.showOverlayPermissionDialog();
                return true;
            }
        }
        return false;
    }

    private void showOverlayPermissionDialog() {
        new AlertDialog.Builder((Context)this).setTitle((CharSequence)"提示").setMessage((CharSequence)"您必须向允许的启动活动界面后台授予覆盖权限.").setCancelable(false).setNegativeButton((CharSequence)"GO", (dialog, which) -> {
            Intent intent = new Intent("android.settings.action.MANAGE_OVERLAY_PERMISSION");
            intent.setData(Uri.parse((String)("package:" + this.getPackageName())));
            this.startActivityForResult(intent, 0);
        }).show();
    }

    public void showPermissionDialog() {
        Intent intent = OemPermissionHelper.getPermissionActivityIntent((Context)this);
        new AlertDialog.Builder((Context)this).setTitle(R.string.permission_boot_notice).setMessage(R.string.permission_boot_content).setCancelable(false).setNegativeButton((CharSequence)"GO", (dialog, which) -> {
            if (intent != null) {
                try {
                    this.startActivity(intent);
                }
                catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }).show();
    }

    @Override
    public boolean checkVerify() {
        return true;
    }

    @Override
    public String currentActivity() {
        return this.getLocalClassName();
    }

    public static String getSHA1(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 64);
            byte[] cert = info.signatures[0].toByteArray();
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] publicKey = md.digest(cert);
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < publicKey.length; ++i) {
                String appendString = Integer.toHexString(0xFF & publicKey[i]).toUpperCase(Locale.US);
                if (appendString.length() == 1) {
                    hexString.append("0");
                }
                hexString.append(appendString);
                hexString.append(":");
            }
            String result = hexString.toString();
            return result.substring(0, result.length() - 1);
        }
        catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    @TargetApi(value=23)
    private boolean checkAndRequestPermission() {
        ArrayList<String> lackedPermission = new ArrayList<String>();
        if (this.checkSelfPermission("android.permission.READ_PHONE_STATE") != PackageManager.PERMISSION_GRANTED) {
            lackedPermission.add("android.permission.READ_PHONE_STATE");
        }
        if (this.checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED) {
            lackedPermission.add("android.permission.WRITE_EXTERNAL_STORAGE");
        }
        if (this.checkSelfPermission("android.permission.ACCESS_FINE_LOCATION") != PackageManager.PERMISSION_GRANTED) {
            lackedPermission.add("android.permission.ACCESS_FINE_LOCATION");
        }
        if (lackedPermission.size() == 0) {
            HVLog.d("权限都已经有了，那么直接调用 return");
            return true;
        }
        String[] requestPermissions = new String[lackedPermission.size()];
        lackedPermission.toArray(requestPermissions);
        this.requestPermissions(requestPermissions, 1024);
        return false;
    }

    public List<FileItem> getCurrentFile() {
        return this.currentFile;
    }

    public String getCloudDiskDirectory(String directoryName) {
        for (FileItem fileItem : this.currentFile) {
            if (!directoryName.equals(fileItem.getFilename())) continue;
            return fileItem.getId();
        }
        return null;
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    protected void onPause() {
        super.onPause();
    }

    static {
        PKG_NAME_ARGUMENT = "MODEL_ARGUMENT";
        KEY_PKGNAME = "KEY_PKGNAME";
        APP_NAME = "APP_NAME";
        KEY_USER = "KEY_USER";
        META_DATA_KEY = "ScorpionSDK";
    }

    class ViewOnclick
    implements View.OnClickListener {
        ViewOnclick() {
        }

        public void onClick(View v) {
            if (v.getId() == R.id.single_btn) {
                VerifyActivity.this.mDialog.dismiss();
                VerifyActivity.this.finish();
            } else if (v.getId() == R.id.btn_cancel) {
                VerifyActivity.this.mDialog.dismiss();
            } else if (v.getId() == R.id.btn_ok) {
                VerifyActivity.this.mDialog.dismiss();
                try {
                    String assetFileName = "plugin_release.apk";
                    HVLog.d("安装插件" + assetFileName);
                    InputStream inputStream = null;
                    File dir = VerifyActivity.this.getCacheDir();
                    try {
                        inputStream = VerifyActivity.this.getAssets().open(assetFileName);
                        File apkFile = new File(dir, "plugin_release.apk");
                        FileUtils.writeToFile(inputStream, apkFile);
                        InstallTools.install((Context)VerifyActivity.this, apkFile);
                    }
                    catch (IOException e) {
                        HVLog.printException(e);
                    }
                }
                catch (Exception exception) {
                    // empty catch block
                }
            }
        }
    }
}

