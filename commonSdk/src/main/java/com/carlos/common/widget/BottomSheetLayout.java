/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.annotation.SuppressLint
 *  android.content.Context
 *  android.content.pm.ApplicationInfo
 *  android.content.pm.PackageManager
 *  android.graphics.drawable.Drawable
 *  android.os.Handler
 *  android.util.AttributeSet
 *  android.view.LayoutInflater
 *  android.widget.ImageView
 *  android.widget.LinearLayout
 *  android.widget.TextView
 *  androidx.annotation.Nullable
 *  com.google.android.material.bottomsheet.BottomSheetDialog
 *  com.kook.librelease.R$id
 */
package com.carlos.common.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import com.carlos.common.widget.EatBeansView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.kook.librelease.R;
import com.kook.librelease.StringFog;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.remote.InstalledAppInfo;
import java.util.Locale;

public class BottomSheetLayout
extends LinearLayout {
    Handler mHandler = new Handler();
    public static String PKG_KEY = "packageName";
    public static String PKG_USERID = "appuserid";
    BottomSheetDialog bottomSheetDialog;
    private EatBeansView loadingView;

    public BottomSheetLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LayoutInflater getLayoutInflater() {
        return LayoutInflater.from((Context)this.getContext());
    }

    public void beginShow(String pkg) {
        InstalledAppInfo installedAppInfo = VirtualCore.get().getInstalledAppInfo(pkg, 0);
        ApplicationInfo applicationInfo = installedAppInfo.getApplicationInfo(installedAppInfo.getInstalledUsers()[0]);
        ImageView iconView = (ImageView)this.findViewById(R.id.app_icon);
        PackageManager pm = this.getContext().getPackageManager();
        CharSequence sequence = applicationInfo.loadLabel(pm);
        String appName = null;
        if (sequence != null) {
            appName = sequence.toString();
        }
        Drawable icon = applicationInfo.loadIcon(pm);
        iconView.setImageDrawable(icon);
        TextView nameView = (TextView)this.findViewById(R.id.app_name);
        nameView.setText((CharSequence)String.format(Locale.ENGLISH, "Opening %s...", appName));
    }

    @SuppressLint(value={"WrongViewCast"})
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.loadingView = (EatBeansView)this.findViewById(R.id.loading_anim);
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        if (visibility != 0) {
            this.loadingView.stopAnim();
        } else {
            this.loadingView.startAnim();
        }
    }
}

