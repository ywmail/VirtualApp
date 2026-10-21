/*
 * Decompiled with CFR 0.152.
 */
package com.carlos.common.utils.location;

import com.carlos.common.utils.location.CoordinateBean;

public class PositionConvertUtil {
    private static final double A = 6378245.0;
    private static final double PI = Math.PI;
    private static final double EE = 0.006693421622965943;
    private static final double LON_BOUNDARY_MIN = 72.004;
    private static final double LAT_BOUNDARY_MIN = 0.8293;
    private static final double LON_BOUNDARY_MAX = 137.8347;
    private static final double LAT_BOUNDARY_MAX = 55.8271;
    private static final double X_PI = 52.35987755982988;

    public static CoordinateBean wgs84ToGcj02(double lat, double lon) {
        CoordinateBean info = new CoordinateBean();
        if (PositionConvertUtil.outOfChina(lat, lon)) {
            info.setChina(false);
            info.setLatitude(lat);
            info.setLongitude(lon);
        } else {
            double dLat = PositionConvertUtil.transformLat(lon - 105.0, lat - 35.0);
            double dLon = PositionConvertUtil.transformLon(lon - 105.0, lat - 35.0);
            double radLat = lat / 180.0 * Math.PI;
            double magic = Math.sin(radLat);
            magic = 1.0 - 0.006693421622965943 * magic * magic;
            double sqrtMagic = Math.sqrt(magic);
            dLat = dLat * 180.0 / (6335552.717000426 / (magic * sqrtMagic) * Math.PI);
            dLon = dLon * 180.0 / (6378245.0 / sqrtMagic * Math.cos(radLat) * Math.PI);
            double mgLat = lat + dLat;
            double mgLon = lon + dLon;
            info.setChina(true);
            info.setLatitude(mgLat);
            info.setLongitude(mgLon);
        }
        return info;
    }

    public static CoordinateBean gcj02ToWgs84(double lat, double lon) {
        CoordinateBean info = new CoordinateBean();
        CoordinateBean gps = PositionConvertUtil.transform(lat, lon);
        double lontitude = lon * 2.0 - gps.getLongitude();
        double latitude = lat * 2.0 - gps.getLatitude();
        info.setChina(gps.isChina());
        info.setLatitude(latitude);
        info.setLongitude(lontitude);
        return info;
    }

    public CoordinateBean bd09tToGcj02(double bdLat, double bdLon) {
        double x = bdLon - 0.0065;
        double y = bdLat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 2.0E-5 * Math.sin(y * 52.35987755982988);
        double theta = Math.atan2(y, x) - 3.0E-6 * Math.cos(x * 52.35987755982988);
        double ggLng = z * Math.cos(theta);
        double ggLat = z * Math.sin(theta);
        return new CoordinateBean(ggLng, ggLat);
    }

    public CoordinateBean gcj02ToBd09(double lat, double lon) {
        double z = Math.sqrt(lon * lon + lat * lat) + 2.0E-5 * Math.sin(lat * 52.35987755982988);
        double theta = Math.atan2(lat, lon) + 3.0E-6 * Math.cos(lon * 52.35987755982988);
        double bdLng = z * Math.cos(theta) + 0.0065;
        double bdLat = z * Math.sin(theta) + 0.006;
        return new CoordinateBean(bdLng, bdLat);
    }

    public CoordinateBean baidu09ToWgs84(double lat, double lon) {
        CoordinateBean info = this.bd09tToGcj02(lat, lon);
        info = PositionConvertUtil.gcj02ToWgs84(info.getLatitude(), info.getLongitude());
        return info;
    }

    private static boolean outOfChina(double lat, double lon) {
        if (lon < 72.004 || lon > 137.8347) {
            return true;
        }
        return lat < 0.8293 || lat > 55.8271;
    }

    private static double transformLon(double x, double y) {
        double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * Math.PI) + 20.0 * Math.sin(2.0 * x * Math.PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(x * Math.PI) + 40.0 * Math.sin(x / 3.0 * Math.PI)) * 2.0 / 3.0;
        return ret += (150.0 * Math.sin(x / 12.0 * Math.PI) + 300.0 * Math.sin(x / 30.0 * Math.PI)) * 2.0 / 3.0;
    }

    private static double transformLat(double lng, double lat) {
        double ret = -100.0 + 2.0 * lng + 3.0 * lat + 0.2 * lat * lat + 0.1 * lng * lat + 0.2 * Math.sqrt(Math.abs(lng));
        ret += (20.0 * Math.sin(6.0 * lng * Math.PI) + 20.0 * Math.sin(2.0 * lng * Math.PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(lat * Math.PI) + 40.0 * Math.sin(lat / 3.0 * Math.PI)) * 2.0 / 3.0;
        return ret += (160.0 * Math.sin(lat / 12.0 * Math.PI) + 320.0 * Math.sin(lat * Math.PI / 30.0)) * 2.0 / 3.0;
    }

    private static CoordinateBean transform(double lat, double lon) {
        CoordinateBean info = new CoordinateBean();
        if (PositionConvertUtil.outOfChina(lat, lon)) {
            info.setChina(false);
            info.setLatitude(lat);
            info.setLongitude(lon);
            return info;
        }
        double dLat = PositionConvertUtil.transformLat(lon - 105.0, lat - 35.0);
        double dLon = PositionConvertUtil.transformLon(lon - 105.0, lat - 35.0);
        double radLat = lat / 180.0 * Math.PI;
        double magic = Math.sin(radLat);
        magic = 1.0 - 0.006693421622965943 * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dLat = dLat * 180.0 / (6335552.717000426 / (magic * sqrtMagic) * Math.PI);
        dLon = dLon * 180.0 / (6378245.0 / sqrtMagic * Math.cos(radLat) * Math.PI);
        double mgLat = lat + dLat;
        double mgLon = lon + dLon;
        info.setChina(true);
        info.setLatitude(mgLat);
        info.setLongitude(mgLon);
        return info;
    }
}

