/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.TypedArray
 *  android.graphics.RectF
 *  android.util.AttributeSet
 *  android.view.LayoutInflater
 *  android.view.View
 *  android.view.ViewGroup
 *  android.widget.FrameLayout
 *  androidx.annotation.NonNull
 *  com.kook.librelease.R$id
 *  com.kook.librelease.R$layout
 *  com.kook.librelease.R$styleable
 */
package com.carlos.common.imagepicker.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import com.carlos.common.imagepicker.callback.CropBoundsChangeListener;
import com.carlos.common.imagepicker.callback.OverlayViewChangeListener;
import com.carlos.common.imagepicker.view.GestureCropImageView;
import com.carlos.common.imagepicker.view.OverlayView;
import com.kook.librelease.R;

public class UCropView
extends FrameLayout {
    private GestureCropImageView mGestureCropImageView;
    private final OverlayView mViewOverlay;

    public UCropView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UCropView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from((Context)context).inflate(R.layout.ucrop_view, (ViewGroup)this, true);
        this.mGestureCropImageView = (GestureCropImageView)this.findViewById(R.id.image_view_crop);
        this.mViewOverlay = (OverlayView)this.findViewById(R.id.view_overlay);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ucrop_UCropView);
        this.mViewOverlay.processStyledAttributes(a);
        this.mGestureCropImageView.processStyledAttributes(a);
        a.recycle();
        this.setListenersToViews();
    }

    private void setListenersToViews() {
        this.mGestureCropImageView.setCropBoundsChangeListener(new CropBoundsChangeListener(){

            @Override
            public void onCropAspectRatioChanged(float cropRatio) {
                UCropView.this.mViewOverlay.setTargetAspectRatio(cropRatio);
            }
        });
        this.mViewOverlay.setOverlayViewChangeListener(new OverlayViewChangeListener(){

            @Override
            public void onCropRectUpdated(RectF cropRect) {
                UCropView.this.mGestureCropImageView.setCropRect(cropRect);
            }
        });
    }

    public boolean shouldDelayChildPressedState() {
        return false;
    }

    @NonNull
    public GestureCropImageView getCropImageView() {
        return this.mGestureCropImageView;
    }

    @NonNull
    public OverlayView getOverlayView() {
        return this.mViewOverlay;
    }

    public void resetCropImageView() {
        this.removeView((View)this.mGestureCropImageView);
        this.mGestureCropImageView = new GestureCropImageView(this.getContext());
        this.setListenersToViews();
        this.mGestureCropImageView.setCropRect(this.getOverlayView().getCropViewRect());
        this.addView((View)this.mGestureCropImageView, 0);
    }
}

