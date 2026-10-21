/*
 * Decompiled with CFR 0.152.
 */
package com.carlos.common.imagepicker.entity;

import com.carlos.common.imagepicker.entity.Image;
import com.carlos.common.imagepicker.utils.StringUtils;
import com.kook.librelease.StringFog;
import java.util.ArrayList;

public class Folder {
    private boolean useCamera;
    private String name;
    private ArrayList<Image> images;

    public Folder(String name) {
        this.name = name;
    }

    public Folder(String name, ArrayList<Image> images) {
        this.name = name;
        this.images = images;
    }

    public boolean isUseCamera() {
        return this.useCamera;
    }

    public void setUseCamera(boolean useCamera) {
        this.useCamera = useCamera;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Image> getImages() {
        return this.images;
    }

    public void setImages(ArrayList<Image> images) {
        this.images = images;
    }

    public void addImage(Image image) {
        if (image != null && StringUtils.isNotEmptyString(image.getPath())) {
            if (this.images == null) {
                this.images = new ArrayList();
            }
            this.images.add(image);
        }
    }

    public String toString() {
        return "Folder{name='" + this.name + '\'' + ", images=" + this.images + '}';
    }
}

