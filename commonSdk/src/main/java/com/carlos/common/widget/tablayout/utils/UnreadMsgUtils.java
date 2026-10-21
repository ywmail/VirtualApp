/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.util.DisplayMetrics
 *  android.view.ViewGroup$LayoutParams
 *  android.widget.RelativeLayout$LayoutParams
 */
package com.carlos.common.widget.tablayout.utils;

import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import com.carlos.common.widget.tablayout.widget.MsgView;
import com.kook.librelease.StringFog;

public class UnreadMsgUtils {
    public static void show(MsgView msgView, int num) {
        if (msgView == null) {
            return;
        }
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams)msgView.getLayoutParams();
        DisplayMetrics dm = msgView.getResources().getDisplayMetrics();
        msgView.setVisibility(0);
        if (num <= 0) {
            msgView.setStrokeWidth(0);
            msgView.setText("");
            lp.width = (int)(5.0f * dm.density);
            lp.height = (int)(5.0f * dm.density);
            msgView.setLayoutParams((ViewGroup.LayoutParams)lp);
        } else {
            lp.height = (int)(18.0f * dm.density);
            if (num > 0 && num < 10) {
                lp.width = (int)(18.0f * dm.density);
                msgView.setText(num + "");
            } else if (num > 9 && num < 100) {
                lp.width = -2;
                msgView.setPadding((int)(6.0f * dm.density), 0, (int)(6.0f * dm.density), 0);
                msgView.setText(num + "");
            } else {
                lp.width = -2;
                msgView.setPadding((int)(6.0f * dm.density), 0, (int)(6.0f * dm.density), 0);
                msgView.setText("99+");
            }
            msgView.setLayoutParams((ViewGroup.LayoutParams)lp);
        }
    }

    public static void setSize(MsgView rtv, int size) {
        if (rtv == null) {
            return;
        }
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams)rtv.getLayoutParams();
        lp.width = size;
        lp.height = size;
        rtv.setLayoutParams((ViewGroup.LayoutParams)lp);
    }
}

