/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.content.Context
 *  android.content.Intent
 *  android.content.pm.ActivityInfo
 *  android.graphics.drawable.ColorDrawable
 *  android.graphics.drawable.Drawable
 *  android.os.Bundle
 *  android.os.Parcelable
 *  android.view.WindowManager$LayoutParams
 */
package com.lody.virtual.client.stub;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.WindowManager;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.stub.StubManifest;
import com.lody.virtual.client.stub.WindowPreviewActivity_Land;
import com.lody.virtual.helper.utils.VLog;
import com.lody.virtual.server.am.AttributeCache;
import mirror.android.graphics.drawable.LayerDrawable;
import mirror.com.android.internal.R_Hide;

public class WindowPreviewActivity
extends Activity {
    private long startTime;

    public static void previewActivity(int userId, ActivityInfo info) {
        Context context = VirtualCore.get().getContext();
        Intent windowBackgroundIntent = new Intent(context, WindowPreviewActivity.class);
        try {
            boolean isFixedOrientationLandscape = StubManifest.isFixedOrientationLandscape(info);
            VLog.d("VA-", "previewActivity isFixedOrientationLandscape:" + isFixedOrientationLandscape + ",info:" + info, new Object[0]);
            if (isFixedOrientationLandscape) {
                windowBackgroundIntent = new Intent(context, WindowPreviewActivity_Land.class);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        windowBackgroundIntent.putExtra("_VA_|user_id", userId);
        windowBackgroundIntent.putExtra("_VA_|activity_info", (Parcelable)info);
        windowBackgroundIntent.addFlags(0x10000000);
        windowBackgroundIntent.addFlags(65536);
        context.startActivity(windowBackgroundIntent);
    }

    protected void onCreate(Bundle savedInstanceState) {
        AttributeCache.Entry windowExt;
        this.startTime = System.currentTimeMillis();
        this.overridePendingTransition(0, 0);
        super.onCreate(savedInstanceState);
        Intent intent = this.getIntent();
        if (intent == null) {
            this.finish();
            return;
        }
        ActivityInfo info = (ActivityInfo)intent.getParcelableExtra("_VA_|activity_info");
        int userId = intent.getIntExtra("_VA_|user_id", -1);
        if (info == null || userId == -1) {
            this.finish();
            return;
        }
        int theme = info.theme;
        if (theme == 0) {
            theme = info.applicationInfo.theme;
        }
        if ((windowExt = AttributeCache.instance().get(info.packageName, theme, R_Hide.styleable.Window.get())) != null) {
            boolean fullscreen = windowExt.array.getBoolean(R_Hide.styleable.Window_windowFullscreen.get(), false);
            boolean translucent = windowExt.array.getBoolean(R_Hide.styleable.Window_windowIsTranslucent.get(), false);
            boolean disablePreview = windowExt.array.getBoolean(R_Hide.styleable.Window_windowDisablePreview.get(), false);
            if (disablePreview) {
                return;
            }
            if (fullscreen) {
                this.getWindow().addFlags(1024);
            }
            Drawable drawable2 = null;
            AttributeCache.Entry viewEnt = AttributeCache.instance().get(info.packageName, info.theme, R_Hide.styleable.View.get());
            if (viewEnt != null) {
                try {
                    drawable2 = viewEnt.array.getDrawable(R_Hide.styleable.View_background.get());
                }
                catch (Throwable throwable) {
                    // empty catch block
                }
            }
            if (drawable2 == null) {
                try {
                    drawable2 = windowExt.array.getDrawable(R_Hide.styleable.Window_windowBackground.get());
                }
                catch (Throwable throwable) {
                    // empty catch block
                }
            }
            if (drawable2 != null && !this.isDrawableBroken(drawable2)) {
                this.getWindow().setBackgroundDrawable(drawable2);
            } else {
                if (!translucent) {
                    this.getWindow().setBackgroundDrawable((Drawable)new ColorDrawable(-1));
                }
                WindowManager.LayoutParams lp = this.getWindow().getAttributes();
                lp.dimAmount = 0.4f;
                this.getWindow().setAttributes(lp);
                this.getWindow().addFlags(2);
            }
        }
    }

    private boolean isDrawableBroken(Drawable drawable2) {
        if (LayerDrawable.TYPE.isInstance(drawable2) && LayerDrawable.isProjected != null) {
            try {
                LayerDrawable.isProjected.callWithException(drawable2, new Object[0]);
                return false;
            }
            catch (Throwable throwable) {
                VLog.e("WindowPreviewActivity", "Bad preview background!", throwable);
                return true;
            }
        }
        return false;
    }

    public void onBackPressed() {
        long time = System.currentTimeMillis();
        if (time - this.startTime > 5000L) {
            this.finish();
        }
    }

    protected void onStop() {
        super.onStop();
        this.finish();
    }
}

