/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.app.AlertDialog
 *  android.app.AlertDialog$Builder
 *  android.app.Dialog
 *  android.content.Context
 *  android.view.View
 *  android.widget.EditText
 *  android.widget.TextView
 *  androidx.appcompat.widget.AppCompatEditText
 *  com.kook.librelease.R$id
 *  com.kook.librelease.R$layout
 *  com.kook.librelease.R$style
 */
package com.carlos.common.widget;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.widget.AppCompatEditText;
import com.carlos.common.widget.TextProgressBar;
import com.kook.librelease.R;

public class MirrorDialog {
    Dialog mDialog = null;
    AlertDialog.Builder mBuilder;
    static MirrorDialog mMirrorDialog;

    private MirrorDialog() {
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static MirrorDialog getInstance() {
        if (mMirrorDialog != null) return mMirrorDialog;
        Class<MirrorDialog> clazz = MirrorDialog.class;
        synchronized (MirrorDialog.class) {
            if (mMirrorDialog != null) return mMirrorDialog;
            mMirrorDialog = new MirrorDialog();
            // ** MonitorExit[var0] (shouldn't be in output)
            return mMirrorDialog;
        }
    }

    public void showBakupAndRecovery(Activity activity, String content, BakupAndRecoveryClickListener onClickListener) {
        this.mBuilder = new AlertDialog.Builder((Context)activity, R.style.VACustomTheme);
        View view1 = activity.getLayoutInflater().inflate(R.layout.dialog_mirror_backup, null);
        this.mBuilder.setView(view1);
        if (!activity.isFinishing()) {
            this.mDialog = this.mBuilder.show();
        }
        if (this.mDialog == null) {
            return;
        }
        this.mDialog.setCanceledOnTouchOutside(false);
        TextView textView = (TextView)view1.findViewById(R.id.tips_content);
        textView.setText((CharSequence)content);
        this.mDialog.setCancelable(false);
        view1.findViewById(R.id.double_btn_layout).setVisibility(0);
        TextProgressBar textProgressBar = (TextProgressBar)view1.findViewById(R.id.progress_bar);
        textProgressBar.setVisibility(4);
        view1.findViewById(R.id.btn_cancel).setOnClickListener(view -> this.mDialog.dismiss());
        view1.findViewById(R.id.btn_ok).setOnClickListener(view -> onClickListener.onClick(view, this.mDialog, textProgressBar));
    }

    public void tipsSingleDialog(Activity activity, String content, SingleDialogClickListener onclick) {
        this.mBuilder = new AlertDialog.Builder((Context)activity, R.style.VACustomTheme);
        View view1 = activity.getLayoutInflater().inflate(R.layout.dialog_tips, null);
        this.mBuilder.setView(view1);
        if (!activity.isFinishing()) {
            this.mDialog = this.mBuilder.show();
        }
        if (this.mDialog == null) {
            return;
        }
        this.mDialog.setCanceledOnTouchOutside(false);
        TextView textView = (TextView)view1.findViewById(R.id.tips_content);
        textView.setText((CharSequence)content);
        this.mDialog.setCancelable(false);
        view1.findViewById(R.id.single_btn_layout).setVisibility(0);
        view1.findViewById(R.id.single_btn).setOnClickListener(view -> onclick.onClick(view, this.mDialog));
    }

    public void singleRecoveryInputDialog(Activity activity, String content, SingleInputDialogClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder((Context)activity, R.style.VACustomTheme);
        View view1 = activity.getLayoutInflater().inflate(R.layout.dialog_single_input, null);
        builder.setView(view1);
        AlertDialog dialog = builder.show();
        dialog.setCanceledOnTouchOutside(false);
        TextView textView = (TextView)view1.findViewById(R.id.tips_content);
        textView.setText((CharSequence)content);
        AppCompatEditText editText1 = (AppCompatEditText)view1.findViewById(R.id.edit_code);
        TextProgressBar textProgressBar = (TextProgressBar)view1.findViewById(R.id.progress_bar);
        textProgressBar.setVisibility(4);
        dialog.setCancelable(false);
        view1.findViewById(R.id.btn_cancel).setOnClickListener(arg_0 -> MirrorDialog.lambda$singleRecoveryInputDialog$3((Dialog)dialog, arg_0));
        view1.findViewById(R.id.btn_ok).setOnClickListener(arg_0 -> MirrorDialog.lambda$singleRecoveryInputDialog$4(listener, editText1, (Dialog)dialog, textProgressBar, arg_0));
    }

    private static /* synthetic */ void lambda$singleRecoveryInputDialog$4(SingleInputDialogClickListener listener, AppCompatEditText editText1, Dialog dialog, TextProgressBar textProgressBar, View v2) {
        listener.onClick(v2, (EditText)editText1, dialog, textProgressBar);
    }

    private static /* synthetic */ void lambda$singleRecoveryInputDialog$3(Dialog dialog, View v2) {
        dialog.dismiss();
    }

    public static interface SingleInputDialogClickListener {
        public void onClick(View var1, EditText var2, Dialog var3, TextProgressBar var4);
    }

    public static interface SingleDialogClickListener {
        public void onClick(View var1, Dialog var2);
    }

    public static interface BakupAndRecoveryClickListener {
        public void onClick(View var1, Dialog var2, TextProgressBar var3);
    }
}

