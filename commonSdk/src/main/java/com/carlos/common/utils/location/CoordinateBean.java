/*
 * Decompiled with CFR 0.152.
 */
package com.carlos.common.utils.location;

import com.kook.librelease.StringFog;

public class CoordinateBean {
    private double longitude;
    private double latitude;
    private boolean isChina;

    public CoordinateBean() {
    }

    public CoordinateBean(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public boolean isChina() {
        return this.isChina;
    }

    public void setChina(boolean china) {
        this.isChina = china;
    }

    public String toString() {
        return "latitude : " + this.latitude + " ; longitude : " + this.longitude;
    }
}

