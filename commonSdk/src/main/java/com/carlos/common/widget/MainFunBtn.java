/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.util.AttributeSet
 *  android.widget.RelativeLayout
 *  android.widget.TextView
 *  com.kook.librelease.R$id
 */
package com.carlos.common.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.kook.librelease.R;

public class MainFunBtn
extends RelativeLayout {
    TextView tv_top;
    TextView tv_buttom;

    public MainFunBtn(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected void onFinishInflate() {
        super.onFinishInflate();
        this.tv_top = (TextView)this.findViewById(R.id.tv_top);
        this.tv_buttom = (TextView)this.findViewById(R.id.tv_buttom);
    }

    public void setTopText(String text) {
        if (this.tv_top != null) {
            this.tv_top.setText((CharSequence)text);
        }
    }

    public void setButtomText(String text) {
        if (this.tv_buttom != null) {
            this.tv_buttom.setText((CharSequence)text);
        }
    }
}

