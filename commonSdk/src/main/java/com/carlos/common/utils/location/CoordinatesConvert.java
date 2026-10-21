/*
 * Decompiled with CFR 0.152.
 */
package com.carlos.common.utils.location;

import com.kook.librelease.StringFog;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class CoordinatesConvert {
    private static double[] MCBAND = new double[]{1.289059486E7, 8362377.87, 5591021.0, 3481989.83, 1678043.12, 0.0};
    private static double[] LLBAND = new double[]{75.0, 60.0, 45.0, 30.0, 15.0, 0.0};
    private static double[][] MC2LL = new double[][]{{1.410526172116255E-8, 8.98305509648872E-6, -1.9939833816331, 200.9824383106796, -187.2403703815547, 91.6087516669843, -23.38765649603339, 2.57121317296198, -0.03801003308653, 1.73379812E7}, {-7.435856389565537E-9, 8.983055097726239E-6, -0.78625201886289, 96.32687599759846, -1.85204757529826, -59.36935905485877, 47.40033549296737, -16.50741931063887, 2.28786674699375, 1.026014486E7}, {-3.030883460898826E-8, 8.98305509983578E-6, 0.30071316287616, 59.74293618442277, 7.357984074871, -25.38371002664745, 13.45380521110908, -3.29883767235584, 0.32710905363475, 6856817.37}, {-1.981981304930552E-8, 8.983055099779535E-6, 0.03278182852591, 40.31678527705744, 0.65659298677277, -4.44255534477492, 0.85341911805263, 0.12923347998204, -0.04625736007561, 4482777.06}, {3.09191371068437E-9, 8.983055096812155E-6, 6.995724062E-5, 23.10934304144901, -2.3663490511E-4, -0.6321817810242, -0.00663494467273, 0.03430082397953, -0.00466043876332, 2555164.4}, {2.890871144776878E-9, 8.983055095805407E-6, -3.068298E-8, 7.47137025468032, -3.53937994E-6, -0.02145144861037, -1.234426596E-5, 1.0322952773E-4, -3.23890364E-6, 826088.5}};
    private static double[][] LL2MC = new double[][]{{-0.0015702102444, 111320.7020616939, 1.704480524535203E15, -1.033898737604234E16, 2.611266785660388E16, -3.51496691766537E16, 2.659570071840392E16, -1.072501245418824E16, 1.800819912950474E15, 82.5}, {8.277824516172526E-4, 111320.7020463578, 6.477955746671607E8, -4.082003173641316E9, 1.077490566351142E10, -1.517187553151559E10, 1.205306533862167E10, -5.124939663577472E9, 9.133119359512032E8, 67.5}, {0.00337398766765, 111320.7020202162, 4481351.045890365, -2.339375119931662E7, 7.968221547186455E7, -1.159649932797253E8, 9.723671115602145E7, -4.366194633752821E7, 8477230.501135234, 52.5}, {0.00220636496208, 111320.7020209128, 51751.86112841131, 3796837.749470245, 992013.7397791013, -1221952.21711287, 1340652.697009075, -620943.6990984312, 144416.9293806241, 37.5}, {-3.441963504368392E-4, 111320.7020576856, 278.2353980772752, 2485758.690035394, 6070.750963243378, 54821.18345352118, 9540.606633304236, -2710.55326746645, 1405.483844121726, 22.5}, {-3.218135878613132E-4, 111320.7020701615, 0.00369383431289, 823725.6402795718, 0.46104986909093, 2351.343141331292, 1.58060784298199, 8.77738589078284, 0.37238884252424, 7.45}};

    public Map<String, Double> convertMC2LL(double x, double y) {
        double[] cF = null;
        x = Math.abs(x);
        y = Math.abs(y);
        for (int cE = 0; cE < MCBAND.length; ++cE) {
            if (!(y >= MCBAND[cE])) continue;
            cF = MC2LL[cE];
            break;
        }
        Map<String, Double> location = this.converter(x, y, cF);
        location.put("lng", location.get("x"));
        location.remove("x");
        location.put("lat", location.get("y"));
        location.remove("y");
        return location;
    }

    public String convertLL2MC(Double lng, Double lat) {
        int i;
        double[] cE = null;
        lng = this.getLoop(lng, -180, 180);
        lat = this.getRange(lat, -74, 74);
        for (i = 0; i < LLBAND.length; ++i) {
            if (!(lat >= LLBAND[i])) continue;
            cE = LL2MC[i];
            break;
        }
        if (cE != null) {
            for (i = LLBAND.length - 1; i >= 0; --i) {
                if (!(lat <= -LLBAND[i])) continue;
                cE = LL2MC[i];
                break;
            }
        }
        Map<String, Double> map = this.converter(lng, lat, cE);
        double x = map.get("x");
        double y = map.get("y");
        BigDecimal xTemp = new BigDecimal(x);
        BigDecimal yTemp = new BigDecimal(y);
        String tempX = xTemp.setScale(8, 4).toPlainString();
        String tempY = yTemp.setScale(10, 4).toPlainString();
        return "x=" + tempX + "&y=" + tempY;
    }

    private Map<String, Double> converter(double x, double y, double[] cE) {
        double xTemp = cE[0] + cE[1] * Math.abs(x);
        double cC = Math.abs(y) / cE[9];
        double yTemp = cE[2] + cE[3] * cC + cE[4] * cC * cC + cE[5] * cC * cC * cC + cE[6] * cC * cC * cC * cC + cE[7] * cC * cC * cC * cC * cC + cE[8] * cC * cC * cC * cC * cC * cC;
        int n = y < 0.0 ? -1 : 1;
        HashMap<String, Double> location = new HashMap<String, Double>(4);
        BigDecimal tempX = new BigDecimal(xTemp *= (double)(x < 0.0 ? -1 : 1));
        BigDecimal tempY = new BigDecimal(yTemp *= (double)n);
        xTemp = tempX.setScale(8, 4).doubleValue();
        yTemp = tempY.setScale(8, 4).doubleValue();
        location.put("x", xTemp);
        location.put("y", yTemp);
        return location;
    }

    private Double getLoop(Double lng, Integer min, Integer max) {
        while (lng > (double)max.intValue()) {
            lng = lng - (double)(max - min);
        }
        while (lng < (double)min.intValue()) {
            lng = lng + (double)(max - min);
        }
        return lng;
    }

    private Double getRange(Double lat, Integer min, Integer max) {
        if (min != null) {
            lat = Math.max(lat, (double)min.intValue());
        }
        if (max != null) {
            lat = Math.min(lat, (double)max.intValue());
        }
        return lat;
    }
}

