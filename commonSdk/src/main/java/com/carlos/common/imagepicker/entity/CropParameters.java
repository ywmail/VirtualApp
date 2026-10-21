/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.Bitmap$CompressFormat
 */
package com.carlos.common.imagepicker.entity;

import android.graphics.Bitmap;
import com.carlos.common.imagepicker.entity.ExifInfo;

public class CropParameters {
    private int mMaxResultImageSizeX;
    private int mMaxResultImageSizeY;
    private Bitmap.CompressFormat mCompressFormat;
    private int mCompressQuality;
    private String mImageInputPath;
    private String mImageOutputPath;
    private ExifInfo mExifInfo;

    public CropParameters(int maxResultImageSizeX, int maxResultImageSizeY, Bitmap.CompressFormat compressFormat, int compressQuality, String imageInputPath, String imageOutputPath, ExifInfo exifInfo) {
        this.mMaxResultImageSizeX = maxResultImageSizeX;
        this.mMaxResultImageSizeY = maxResultImageSizeY;
        this.mCompressFormat = compressFormat;
        this.mCompressQuality = compressQuality;
        this.mImageInputPath = imageInputPath;
        this.mImageOutputPath = imageOutputPath;
        this.mExifInfo = exifInfo;
    }

    public int getMaxResultImageSizeX() {
        return this.mMaxResultImageSizeX;
    }

    public int getMaxResultImageSizeY() {
        return this.mMaxResultImageSizeY;
    }

    public Bitmap.CompressFormat getCompressFormat() {
        return this.mCompressFormat;
    }

    public int getCompressQuality() {
        return this.mCompressQuality;
    }

    public String getImageInputPath() {
        return this.mImageInputPath;
    }

    public String getImageOutputPath() {
        return this.mImageOutputPath;
    }

    public ExifInfo getExifInfo() {
        return this.mExifInfo;
    }
}

