/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Bundle
 *  android.view.LayoutInflater
 *  android.view.View
 *  android.view.View$OnClickListener
 *  android.view.ViewGroup
 *  android.widget.TextView
 *  androidx.annotation.Nullable
 *  com.kook.librelease.R$id
 *  com.kook.librelease.R$layout
 */
package com.carlos.common.widget;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.Nullable;
import com.carlos.common.widget.BaseDialogFragment;
import com.kook.librelease.R;

public class AgreementsDialog
extends BaseDialogFragment {
    public static Builder newBuilder() {
        Builder builder = new Builder();
        return builder;
    }

    public static AgreementsDialog newInstance(Builder builder) {
        AgreementsDialog dialog = new AgreementsDialog();
        return dialog;
    }

    @Override
    protected View setView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_agreement, container, false);
        TextView tvContent = (TextView)view.findViewById(R.id.tv_content);
        TextView tvConfirm = (TextView)view.findViewById(R.id.tv_confirm);
        TextView tvCancle = (TextView)view.findViewById(R.id.tv_cancle);
        tvConfirm.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v) {
                if (AgreementsDialog.this.mDialogResultListener != null) {
                    AgreementsDialog.this.mDialogResultListener.result("");
                }
            }
        });
        tvCancle.setOnClickListener(v -> {
            if (this.mDialogDismissListener != null) {
                this.mDialogDismissListener.dismiss(this);
            }
        });
        return view;
    }

    public static class Builder
    extends BaseDialogFragment.Builder<Builder, AgreementsDialog> {
        @Override
        public AgreementsDialog build() {
            return AgreementsDialog.newInstance(this);
        }
    }
}

