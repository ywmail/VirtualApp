/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.ClipboardManager
 *  android.content.Context
 *  android.content.Intent
 *  android.content.pm.PackageInfo
 *  android.content.pm.PackageManager
 *  android.content.pm.PackageManager$NameNotFoundException
 *  android.graphics.Point
 *  android.net.Uri
 *  android.os.Bundle
 *  android.view.View
 *  android.view.View$OnClickListener
 *  android.widget.FrameLayout$LayoutParams
 *  android.widget.ImageView
 *  android.widget.RelativeLayout
 *  android.widget.TextView
 *  androidx.annotation.Nullable
 *  com.kook.librelease.R$id
 *  com.kook.librelease.R$layout
 *  com.kook.librelease.R$string
 */
package com.carlos.common.ui.activity;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import com.carlos.common.device.DeviceInfo;
import com.carlos.common.ui.activity.base.BaseActivity;
import com.carlos.common.widget.toast.Toasty;
import com.kook.librelease.R;
import com.kook.librelease.StringFog;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SettingActivity
extends BaseActivity
implements View.OnClickListener {
    TextView toolbar_title;
    private ImageView imageViewBack;
    private TextView txtVersionCode;
    private TextView txtQQNumber;
    private RelativeLayout relaQQ;
    private TextView activationTv;
    private TextView devicesNo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_setting);
        this.initView();
        this.initData();
    }

    public void initView() {
        this.imageViewBack = (ImageView)this.findViewById(R.id.toolbar_left_menu);
        this.toolbar_title = (TextView)this.findViewById(R.id.toolbar_title);
        this.txtVersionCode = (TextView)this.findViewById(R.id.txtVersionCode);
        this.txtQQNumber = (TextView)this.findViewById(R.id.txtQQNumber);
        this.relaQQ = (RelativeLayout)this.findViewById(R.id.relaQQ);
        this.activationTv = (TextView)this.findViewById(R.id.activation_tv);
        this.devicesNo = (TextView)this.findViewById(R.id.devices_no);
        this.findViewById(R.id.txtAddFriend).setOnClickListener((View.OnClickListener)this);
        this.findViewById(R.id.relaFeedBack).setOnClickListener((View.OnClickListener)this);
        this.findViewById(R.id.relaUpdate).setOnClickListener((View.OnClickListener)this);
        this.findViewById(R.id.txtShare).setOnClickListener((View.OnClickListener)this);
        this.findViewById(R.id.relaQuestions).setOnClickListener((View.OnClickListener)this);
        this.imageViewBack.setOnClickListener((View.OnClickListener)this);
        this.relaQQ.setOnClickListener((View.OnClickListener)this);
    }

    public static void copy(String content, Context context) {
        ClipboardManager cmb = (ClipboardManager)context.getSystemService("clipboard");
        cmb.setText((CharSequence)content.trim());
        Toasty.success(context, "编码已复制", 1);
    }

    public void initData() {
        DeviceInfo instance = DeviceInfo.getInstance((Context)this);
        this.toolbar_title.setText((CharSequence)"设置");
        this.txtVersionCode.setText((CharSequence)("(当前版本" + instance.getVersionName((Context)this) + ")" + instance.getVersionCode()));
        this.txtQQNumber.setText((CharSequence)"请点击联系客服QQ 后续持续更新,请多多关注");
        this.activationTv.setText((CharSequence)"激活状态");
        this.activationTv.setOnClickListener(view -> {
            this.devicesNo.setText((CharSequence)instance.getDevicesNo());
            SettingActivity.copy(instance.getDevicesNo(), (Context)this);
        });
    }

    public static String timeMillisToFormat(long timestamp) {
        String time = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒").format(new Date(timestamp));
        return time;
    }

    private FrameLayout.LayoutParams getUnifiedBannerLayoutParams() {
        Point screenSize = new Point();
        this.getWindowManager().getDefaultDisplay().getSize(screenSize);
        return new FrameLayout.LayoutParams(screenSize.x, Math.round((float)screenSize.x / 6.4f));
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.toolbar_left_menu) {
            this.finish();
        } else if (view.getId() == R.id.txtAddFriend) {
            if (SettingActivity.isAppInstall(this.getPackageManager(), "com.tencent.mobileqq")) {
                String url = "mqqwpa://im/chat?chat_type=wpa&uin=82319214";
                this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse((String)url)));
            } else {
                Toasty.info((Context)this, "请安装QQ客户端").show();
            }
        } else if (view.getId() == R.id.relaQuestions) {
            Toasty.success((Context)this, "开发中ing").show();
        } else if (view.getId() == R.id.relaUpdate) {
            if (this.isUpgrade()) {
                if (this.mSoftVersions != null) {
                    String updateUrl = this.mSoftVersions.getUpdateUrl();
                    Uri uri = Uri.parse((String)updateUrl);
                    Intent intent = new Intent("android.intent.action.VIEW", uri);
                    this.startActivity(intent);
                }
            } else {
                Toasty.success((Context)this, this.getString(R.string.latest_version)).show();
            }
        }
    }

    protected void onResume() {
        super.onResume();
    }

    private void getDownload() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public static boolean isAppInstall(PackageManager pm, String packageName) {
        boolean mBoolean = false;
        try {
            mBoolean = SettingActivity.queryPackageInfo(pm, packageName) != null;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return mBoolean;
    }

    public static PackageInfo queryPackageInfo(PackageManager pm, String packageName) {
        PackageInfo mPackageInfo = null;
        if (pm != null && packageName.length() > 0) {
            try {
                mPackageInfo = pm.getPackageInfo(packageName.trim(), 0);
            }
            catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return mPackageInfo;
    }
}

