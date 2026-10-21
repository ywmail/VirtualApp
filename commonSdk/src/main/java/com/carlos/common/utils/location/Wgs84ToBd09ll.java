/*
 * Decompiled with CFR 0.152.
 */
package com.carlos.common.utils.location;

import com.kook.librelease.StringFog;

public class Wgs84ToBd09ll {
    static double x_PI = 52.35987755982988;
    static double PI = Math.PI;
    static double a = 6378245.0;
    static double ee = 0.006693421622965943;

    private String bd09togcj02(double bd_lon, double bd_lat) {
        double x = bd_lon - 0.0065;
        double y = bd_lat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 2.0E-5 * Math.sin(y * x_PI);
        double theta = Math.atan2(y, x) - 3.0E-6 * Math.cos(x * x_PI);
        double gg_lng = z * Math.cos(theta);
        double gg_lat = z * Math.sin(theta);
        return gg_lng + "," + gg_lat;
    }

    private String gcj02tobd09(double lng, double lat) {
        double z = Math.sqrt(lng * lng + lat * lat) + 2.0E-5 * Math.sin(lat * x_PI);
        double theta = Math.atan2(lat, lng) + 3.0E-6 * Math.cos(lng * x_PI);
        double bd_lng = z * Math.cos(theta) + 0.0065;
        double bd_lat = z * Math.sin(theta) + 0.006;
        return bd_lng + "," + bd_lat;
    }

    private String wgs84togcj02(double lng, double lat) {
        double dlat = this.transformlat(lng - 105.0, lat - 35.0);
        double dlng = this.transformlng(lng - 105.0, lat - 35.0);
        double radlat = lat / 180.0 * PI;
        double magic = Math.sin(radlat);
        magic = 1.0 - ee * magic * magic;
        double sqrtmagic = Math.sqrt(magic);
        dlat = dlat * 180.0 / (a * (1.0 - ee) / (magic * sqrtmagic) * PI);
        dlng = dlng * 180.0 / (a / sqrtmagic * Math.cos(radlat) * PI);
        double mglat = lat + dlat;
        double mglng = lng + dlng;
        return mglng + "," + mglat;
    }

    private String gcj02towgs84(double lng, double lat) {
        double dlat = this.transformlat(lng - 105.0, lat - 35.0);
        double dlng = this.transformlng(lng - 105.0, lat - 35.0);
        double radlat = lat / 180.0 * PI;
        double magic = Math.sin(radlat);
        magic = 1.0 - ee * magic * magic;
        double sqrtmagic = Math.sqrt(magic);
        dlat = dlat * 180.0 / (a * (1.0 - ee) / (magic * sqrtmagic) * PI);
        dlng = dlng * 180.0 / (a / sqrtmagic * Math.cos(radlat) * PI);
        double mglat = lat + dlat;
        double mglng = lng + dlng;
        return mglat + "," + mglng;
    }

    public String wgs84tobd09(double lng, double lat) {
        double dlat = this.transformlat(lng - 105.0, lat - 35.0);
        double dlng = this.transformlng(lng - 105.0, lat - 35.0);
        double radlat = lat / 180.0 * PI;
        double magic = Math.sin(radlat);
        magic = 1.0 - ee * magic * magic;
        double sqrtmagic = Math.sqrt(magic);
        dlat = dlat * 180.0 / (a * (1.0 - ee) / (magic * sqrtmagic) * PI);
        dlng = dlng * 180.0 / (a / sqrtmagic * Math.cos(radlat) * PI);
        double mglat = lat + dlat;
        double mglng = lng + dlng;
        double z = Math.sqrt(mglng * mglng + mglat * mglat) + 2.0E-5 * Math.sin(mglat * x_PI);
        double theta = Math.atan2(mglat, mglng) + 3.0E-6 * Math.cos(mglng * x_PI);
        double bd_lng = z * Math.cos(theta) + 0.0065;
        double bd_lat = z * Math.sin(theta) + 0.006;
        return bd_lng + "," + bd_lat;
    }

    private double transformlat(double lng, double lat) {
        double ret = -100.0 + 2.0 * lng + 3.0 * lat + 0.2 * lat * lat + 0.1 * lng * lat + 0.2 * Math.sqrt(Math.abs(lng));
        ret += (20.0 * Math.sin(6.0 * lng * PI) + 20.0 * Math.sin(2.0 * lng * PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(lat * PI) + 40.0 * Math.sin(lat / 3.0 * PI)) * 2.0 / 3.0;
        return ret += (160.0 * Math.sin(lat / 12.0 * PI) + 320.0 * Math.sin(lat * PI / 30.0)) * 2.0 / 3.0;
    }

    private double transformlng(double lng, double lat) {
        double ret = 300.0 + lng + 2.0 * lat + 0.1 * lng * lng + 0.1 * lng * lat + 0.1 * Math.sqrt(Math.abs(lng));
        ret += (20.0 * Math.sin(6.0 * lng * PI) + 20.0 * Math.sin(2.0 * lng * PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(lng * PI) + 40.0 * Math.sin(lng / 3.0 * PI)) * 2.0 / 3.0;
        return ret += (150.0 * Math.sin(lng / 12.0 * PI) + 300.0 * Math.sin(lng / 30.0 * PI)) * 2.0 / 3.0;
    }
}

