/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.content.Context
 *  android.os.Build$VERSION
 *  android.os.Bundle
 *  android.view.View
 *  android.widget.Button
 *  android.widget.ProgressBar
 *  android.widget.TextView
 *  android.widget.Toast
 *  androidx.appcompat.app.AppCompatActivity
 *  androidx.core.app.ActivityCompat
 *  androidx.core.content.ContextCompat
 *  com.kook.librelease.R$layout
 */
package com.carlos.common.download;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.carlos.common.download.DownloadListner;
import com.carlos.common.download.DownloadManager;
import com.kook.librelease.R;
import com.kook.librelease.StringFog;

public class DownloadActivity
extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 1;
    TextView tv_file_name1;
    TextView tv_progress1;
    TextView tv_file_name2;
    TextView tv_progress2;
    Button btn_download1;
    Button btn_download2;
    Button btn_download_all;
    ProgressBar pb_progress1;
    ProgressBar pb_progress2;
    DownloadManager mDownloadManager;
    String wechatUrl = "http://dldir1.qq.com/weixin/android/weixin703android1400.apk";
    String qqUrl = "https://qd.myapp.com/myapp/qqteam/AndroidQQ/mobileqq_android.apk";
    Button btn_cancel2;
    Button btn_cancel1;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.download_demo);
        this.initDownloads();
    }

    private void initDownloads() {
        this.mDownloadManager = DownloadManager.getInstance();
        this.mDownloadManager.add((Context)this, this.wechatUrl, new DownloadListner(){

            @Override
            public void onFinished() {
                Toast.makeText((Context)DownloadActivity.this, (CharSequence)"下载完成!", (int)0).show();
            }

            @Override
            public void onProgress(float progress) {
                DownloadActivity.this.pb_progress1.setProgress((int)(progress * 100.0f));
                DownloadActivity.this.tv_progress1.setText((CharSequence)(String.format("%.2f", Float.valueOf(progress * 100.0f)) + "%"));
            }

            @Override
            public void onPause() {
                Toast.makeText((Context)DownloadActivity.this, (CharSequence)"暂停了!", (int)0).show();
            }

            @Override
            public void onCancel() {
                DownloadActivity.this.tv_progress1.setText((CharSequence)"0%");
                DownloadActivity.this.pb_progress1.setProgress(0);
                DownloadActivity.this.btn_download1.setText((CharSequence)"下载");
                Toast.makeText((Context)DownloadActivity.this, (CharSequence)"下载已取消!", (int)0).show();
            }
        });
        this.mDownloadManager.add((Context)this, this.qqUrl, new DownloadListner(){

            @Override
            public void onFinished() {
                Toast.makeText((Context)DownloadActivity.this, (CharSequence)"下载完成!", (int)0).show();
            }

            @Override
            public void onProgress(float progress) {
                DownloadActivity.this.pb_progress2.setProgress((int)(progress * 100.0f));
                DownloadActivity.this.tv_progress2.setText((CharSequence)(String.format("%.2f", Float.valueOf(progress * 100.0f)) + "%"));
            }

            @Override
            public void onPause() {
                Toast.makeText((Context)DownloadActivity.this, (CharSequence)"暂停了!", (int)0).show();
            }

            @Override
            public void onCancel() {
                DownloadActivity.this.tv_progress2.setText((CharSequence)"0%");
                DownloadActivity.this.pb_progress2.setProgress(0);
                DownloadActivity.this.btn_download2.setText((CharSequence)"下载");
                Toast.makeText((Context)DownloadActivity.this, (CharSequence)"下载已取消!", (int)0).show();
            }
        });
    }

    public void downloadOrPause(View view) {
        if (view == this.btn_download1) {
            if (!this.mDownloadManager.isDownloading(this.wechatUrl)) {
                this.mDownloadManager.download(this.wechatUrl);
                this.btn_download1.setText((CharSequence)"暂停");
            } else {
                this.btn_download1.setText((CharSequence)"下载");
                this.mDownloadManager.pause(this.wechatUrl);
            }
        } else if (view == this.btn_download2) {
            if (!this.mDownloadManager.isDownloading(this.qqUrl)) {
                this.mDownloadManager.download(this.qqUrl);
                this.btn_download2.setText((CharSequence)"暂停");
            } else {
                this.btn_download2.setText((CharSequence)"下载");
                this.mDownloadManager.pause(this.qqUrl);
            }
        }
    }

    public void downloadOrPauseAll(View view) {
        if (!this.mDownloadManager.isDownloading(this.wechatUrl, this.qqUrl)) {
            this.btn_download1.setText((CharSequence)"暂停");
            this.btn_download2.setText((CharSequence)"暂停");
            this.btn_download_all.setText((CharSequence)"全部暂停");
            this.mDownloadManager.download(this.wechatUrl, this.qqUrl);
        } else {
            this.mDownloadManager.pause(this.wechatUrl, this.qqUrl);
            this.btn_download1.setText((CharSequence)"下载");
            this.btn_download2.setText((CharSequence)"下载");
            this.btn_download_all.setText((CharSequence)"全部下载");
        }
    }

    public void cancel(View view) {
        if (view == this.btn_cancel1) {
            this.mDownloadManager.cancel(this.wechatUrl);
        } else if (view == this.btn_cancel2) {
            this.mDownloadManager.cancel(this.qqUrl);
        }
    }

    public void cancelAll(View view) {
        this.mDownloadManager.cancel(this.wechatUrl, this.qqUrl);
        this.btn_download1.setText((CharSequence)"下载");
        this.btn_download2.setText((CharSequence)"下载");
        this.btn_download_all.setText((CharSequence)"全部下载");
    }

    protected void onStart() {
        super.onStart();
        if (Build.VERSION.SDK_INT < 23) {
            return;
        }
        String permission2 = "android.permission.WRITE_EXTERNAL_STORAGE";
        if (!this.checkPermission(permission2)) {
            if (this.shouldShowRationale(permission2)) {
                this.showMessage("需要权限跑demo哦...");
            }
            ActivityCompat.requestPermissions((Activity)this, (String[])new String[]{permission2}, (int)1);
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        this.cancelAll(null);
    }

    private void showMessage(String msg) {
        Toast.makeText((Context)this, (CharSequence)msg, (int)0).show();
    }

    protected boolean checkPermission(String permission2) {
        return ContextCompat.checkSelfPermission((Context)this, (String)permission2) == 0;
    }

    protected boolean shouldShowRationale(String permission2) {
        return ActivityCompat.shouldShowRequestPermissionRationale((Activity)this, (String)permission2);
    }
}

