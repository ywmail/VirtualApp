/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.graphics.Matrix
 *  android.graphics.RectF
 *  android.graphics.drawable.Drawable
 *  android.net.Uri
 *  android.util.AttributeSet
 *  android.view.GestureDetector$OnDoubleTapListener
 *  android.view.View$OnClickListener
 *  android.view.View$OnLongClickListener
 *  android.widget.ImageView
 *  android.widget.ImageView$ScaleType
 *  androidx.appcompat.widget.AppCompatImageView
 */
package com.carlos.common.imagepicker.photoview;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.widget.AppCompatImageView;
import com.carlos.common.imagepicker.photoview.OnMatrixChangedListener;
import com.carlos.common.imagepicker.photoview.OnOutsidePhotoTapListener;
import com.carlos.common.imagepicker.photoview.OnPhotoTapListener;
import com.carlos.common.imagepicker.photoview.OnScaleChangedListener;
import com.carlos.common.imagepicker.photoview.OnSingleFlingListener;
import com.carlos.common.imagepicker.photoview.OnViewDragListener;
import com.carlos.common.imagepicker.photoview.OnViewTapListener;
import com.carlos.common.imagepicker.photoview.PhotoViewAttacher;

public class PhotoView
extends AppCompatImageView {
    private PhotoViewAttacher attacher;
    private ImageView.ScaleType pendingScaleType;

    public PhotoView(Context context) {
        this(context, null);
    }

    public PhotoView(Context context, AttributeSet attr2) {
        this(context, attr2, 0);
    }

    public PhotoView(Context context, AttributeSet attr2, int defStyle) {
        super(context, attr2, defStyle);
        this.init();
    }

    private void init() {
        this.attacher = new PhotoViewAttacher((ImageView)this);
        super.setScaleType(ImageView.ScaleType.MATRIX);
        if (this.pendingScaleType != null) {
            this.setScaleType(this.pendingScaleType);
            this.pendingScaleType = null;
        }
    }

    public PhotoViewAttacher getAttacher() {
        return this.attacher;
    }

    public ImageView.ScaleType getScaleType() {
        return this.attacher.getScaleType();
    }

    public Matrix getImageMatrix() {
        return this.attacher.getImageMatrix();
    }

    public void setOnLongClickListener(View.OnLongClickListener l) {
        this.attacher.setOnLongClickListener(l);
    }

    public void setOnClickListener(View.OnClickListener l) {
        this.attacher.setOnClickListener(l);
    }

    public void setScaleType(ImageView.ScaleType scaleType) {
        if (this.attacher == null) {
            this.pendingScaleType = scaleType;
        } else {
            this.attacher.setScaleType(scaleType);
        }
    }

    public void setImageDrawable(Drawable drawable2) {
        super.setImageDrawable(drawable2);
        if (this.attacher != null) {
            this.attacher.update();
        }
    }

    public void setImageResource(int resId) {
        super.setImageResource(resId);
        if (this.attacher != null) {
            this.attacher.update();
        }
    }

    public void setImageURI(Uri uri) {
        super.setImageURI(uri);
        if (this.attacher != null) {
            this.attacher.update();
        }
    }

    protected boolean setFrame(int l, int t, int r, int b) {
        boolean changed = super.setFrame(l, t, r, b);
        if (changed) {
            this.attacher.update();
        }
        return changed;
    }

    public void setRotationTo(float rotationDegree) {
        this.attacher.setRotationTo(rotationDegree);
    }

    public void setRotationBy(float rotationDegree) {
        this.attacher.setRotationBy(rotationDegree);
    }

    public boolean isZoomable() {
        return this.attacher.isZoomable();
    }

    public void setZoomable(boolean zoomable) {
        this.attacher.setZoomable(zoomable);
    }

    public RectF getDisplayRect() {
        return this.attacher.getDisplayRect();
    }

    public void getDisplayMatrix(Matrix matrix) {
        this.attacher.getDisplayMatrix(matrix);
    }

    public boolean setDisplayMatrix(Matrix finalRectangle) {
        return this.attacher.setDisplayMatrix(finalRectangle);
    }

    public void getSuppMatrix(Matrix matrix) {
        this.attacher.getSuppMatrix(matrix);
    }

    public boolean setSuppMatrix(Matrix matrix) {
        return this.attacher.setDisplayMatrix(matrix);
    }

    public float getMinimumScale() {
        return this.attacher.getMinimumScale();
    }

    public float getMediumScale() {
        return this.attacher.getMediumScale();
    }

    public float getMaximumScale() {
        return this.attacher.getMaximumScale();
    }

    public float getScale() {
        return this.attacher.getScale();
    }

    public void setAllowParentInterceptOnEdge(boolean allow) {
        this.attacher.setAllowParentInterceptOnEdge(allow);
    }

    public void setMinimumScale(float minimumScale) {
        this.attacher.setMinimumScale(minimumScale);
    }

    public void setMediumScale(float mediumScale) {
        this.attacher.setMediumScale(mediumScale);
    }

    public void setMaximumScale(float maximumScale) {
        this.attacher.setMaximumScale(maximumScale);
    }

    public void setScaleLevels(float minimumScale, float mediumScale, float maximumScale) {
        this.attacher.setScaleLevels(minimumScale, mediumScale, maximumScale);
    }

    public void setOnMatrixChangeListener(OnMatrixChangedListener listener) {
        this.attacher.setOnMatrixChangeListener(listener);
    }

    public void setOnPhotoTapListener(OnPhotoTapListener listener) {
        this.attacher.setOnPhotoTapListener(listener);
    }

    public void setOnOutsidePhotoTapListener(OnOutsidePhotoTapListener listener) {
        this.attacher.setOnOutsidePhotoTapListener(listener);
    }

    public void setOnViewTapListener(OnViewTapListener listener) {
        this.attacher.setOnViewTapListener(listener);
    }

    public void setOnViewDragListener(OnViewDragListener listener) {
        this.attacher.setOnViewDragListener(listener);
    }

    public void setScale(float scale) {
        this.attacher.setScale(scale);
    }

    public void setScale(float scale, boolean animate) {
        this.attacher.setScale(scale, animate);
    }

    public void setScale(float scale, float focalX, float focalY, boolean animate) {
        this.attacher.setScale(scale, focalX, focalY, animate);
    }

    public void setZoomTransitionDuration(int milliseconds) {
        this.attacher.setZoomTransitionDuration(milliseconds);
    }

    public void setOnDoubleTapListener(GestureDetector.OnDoubleTapListener onDoubleTapListener) {
        this.attacher.setOnDoubleTapListener(onDoubleTapListener);
    }

    public void setOnScaleChangeListener(OnScaleChangedListener onScaleChangedListener) {
        this.attacher.setOnScaleChangeListener(onScaleChangedListener);
    }

    public void setOnSingleFlingListener(OnSingleFlingListener onSingleFlingListener) {
        this.attacher.setOnSingleFlingListener(onSingleFlingListener);
    }
}

