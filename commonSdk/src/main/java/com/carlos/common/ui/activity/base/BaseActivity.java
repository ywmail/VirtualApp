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
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import com.carlos.common.ui.utils.StatusBarUtil;
import com.kook.common.utils.HVLog;
import com.kook.librelease.R;
import com.kook.librelease.StringFog;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class BaseActivity
extends AppCompatActivity
implements View.OnClickListener {
    String TAG = BaseActivity.class.getSimpleName();
    public static final int DOWNLOAD_FAIL = 0;
    public static final int DOWNLOAD_PROGRESS = 1;
    public static final int DOWNLOAD_SUCCESS = 2;
    Handler mHandler = new Handler();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = this.getIntent();
        this.setStatusBar();
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
}

