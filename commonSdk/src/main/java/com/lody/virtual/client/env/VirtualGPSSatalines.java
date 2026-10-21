/*
 * Decompiled with CFR 0.152.
 */
package com.lody.virtual.client.env;

import com.lody.virtual.client.env.GPSStateline;
import java.util.ArrayList;

public class VirtualGPSSatalines {
    private static final int SVID_SHIFT_WIDTH = 7;
    private static final int CONSTELLATION_TYPE_SHIFT_WIDTH = 3;
    private static final int CONSTELLATION_UNKNOWN = 0;
    private static final int CONSTELLATION_GPS = 1;
    private static final int CONSTELLATION_SBAS = 2;
    private static final int CONSTELLATION_GLONASS = 3;
    private static final int CONSTELLATION_QZSS = 4;
    private static final int CONSTELLATION_BEIDOU = 5;
    private static final int CONSTELLATION_GALILEO = 6;
    private static final int CONSTELLATION_TYPE_MASK = 15;
    private static final int GLONASS_SVID_OFFSET = 64;
    private static final int BEIDOU_SVID_OFFSET = 200;
    private static final int SBAS_SVID_OFFSET = -87;
    private static final int GNSS_SV_FLAGS_NONE = 0;
    private static final int GNSS_SV_FLAGS_HAS_EPHEMERIS_DATA = 1;
    private static final int GNSS_SV_FLAGS_HAS_ALMANAC_DATA = 2;
    private static final int GNSS_SV_FLAGS_USED_IN_FIX = 4;
    private static VirtualGPSSatalines INSTANCE = new VirtualGPSSatalines();
    private int mAlmanacMask;
    private float[] mAzimuths;
    private float[] mElevations;
    private int mEphemerisMask;
    private float[] mSnrs;
    private int mUsedInFixMask;
    private int[] pnrs;
    private int[] prnWithFlags;
    private int svCount;
    private float[] carrierFreqs;

    public int getAlmanacMask() {
        return this.mAlmanacMask;
    }

    public float[] getAzimuths() {
        return this.mAzimuths;
    }

    public float[] getElevations() {
        return this.mElevations;
    }

    public int getEphemerisMask() {
        return this.mEphemerisMask;
    }

    public int[] getPrns() {
        return this.pnrs;
    }

    public float[] getSnrs() {
        return this.mSnrs;
    }

    public int getUsedInFixMask() {
        return this.mUsedInFixMask;
    }

    public static VirtualGPSSatalines get() {
        return INSTANCE;
    }

    public float[] getCarrierFreqs() {
        return this.carrierFreqs;
    }

    private VirtualGPSSatalines() {
        int i;
        ArrayList<GPSStateline> statelines = new ArrayList<GPSStateline>();
        statelines.add(new GPSStateline(5, 1.0, 5.0, 112.0, false, true, true, 0.0));
        statelines.add(new GPSStateline(13, 13.5, 23.0, 53.0, true, true, true, 0.0));
        statelines.add(new GPSStateline(14, 19.1, 6.0, 247.0, true, true, true, 0.0));
        statelines.add(new GPSStateline(15, 31.0, 58.0, 45.0, true, true, true, 0.0));
        statelines.add(new GPSStateline(18, 0.0, 52.0, 309.0, false, true, true, 0.0));
        statelines.add(new GPSStateline(20, 30.1, 54.0, 105.0, true, true, true, 0.0));
        statelines.add(new GPSStateline(21, 33.2, 56.0, 251.0, true, true, true, 0.0));
        statelines.add(new GPSStateline(22, 0.0, 14.0, 299.0, false, true, true, 0.0));
        statelines.add(new GPSStateline(24, 25.9, 57.0, 157.0, true, true, true, 0.0));
        statelines.add(new GPSStateline(27, 18.0, 3.0, 309.0, true, true, true, 0.0));
        statelines.add(new GPSStateline(28, 18.2, 3.0, 42.0, true, true, true, 0.0));
        statelines.add(new GPSStateline(41, 28.8, 0.0, 0.0, false, false, false, 0.0));
        statelines.add(new GPSStateline(50, 29.2, 0.0, 0.0, false, true, true, 0.0));
        statelines.add(new GPSStateline(67, 14.4, 2.0, 92.0, false, false, false, 0.0));
        statelines.add(new GPSStateline(68, 21.2, 45.0, 60.0, false, false, false, 0.0));
        statelines.add(new GPSStateline(69, 17.5, 50.0, 330.0, false, true, true, 0.0));
        statelines.add(new GPSStateline(70, 22.4, 7.0, 291.0, false, false, false, 0.0));
        statelines.add(new GPSStateline(77, 23.8, 10.0, 23.0, true, true, true, 0.0));
        statelines.add(new GPSStateline(78, 18.0, 47.0, 70.0, true, true, true, 0.0));
        statelines.add(new GPSStateline(79, 22.8, 41.0, 142.0, true, true, true, 0.0));
        statelines.add(new GPSStateline(83, 0.2, 9.0, 212.0, false, false, false, 0.0));
        statelines.add(new GPSStateline(84, 16.7, 30.0, 264.0, true, true, true, 0.0));
        statelines.add(new GPSStateline(85, 12.1, 20.0, 317.0, true, true, true, 0.0));
        this.svCount = statelines.size();
        this.pnrs = new int[statelines.size()];
        for (i = 0; i < statelines.size(); ++i) {
            this.pnrs[i] = ((GPSStateline)statelines.get(i)).getPnr();
        }
        this.mSnrs = new float[statelines.size()];
        for (i = 0; i < statelines.size(); ++i) {
            this.mSnrs[i] = (float)((GPSStateline)statelines.get(i)).getSnr();
        }
        this.mElevations = new float[statelines.size()];
        for (i = 0; i < statelines.size(); ++i) {
            this.mElevations[i] = (float)((GPSStateline)statelines.get(i)).getElevation();
        }
        this.mAzimuths = new float[statelines.size()];
        for (i = 0; i < statelines.size(); ++i) {
            this.mAzimuths[i] = (float)((GPSStateline)statelines.get(i)).getAzimuth();
        }
        this.carrierFreqs = new float[statelines.size()];
        for (i = 0; i < statelines.size(); ++i) {
            this.carrierFreqs[i] = (float)((GPSStateline)statelines.get(i)).getCarrierFrequencyHz();
        }
        this.prnWithFlags = new int[statelines.size()];
        for (i = 0; i < statelines.size(); ++i) {
            GPSStateline gpsStateline = (GPSStateline)statelines.get(i);
            int constellationType = 3;
            this.prnWithFlags[i] = gpsStateline.getPnr() << 71 | 0x18;
        }
        this.mEphemerisMask = 0;
        for (i = 0; i < statelines.size(); ++i) {
            if (!((GPSStateline)statelines.get(i)).isHasEphemeris()) continue;
            this.mEphemerisMask |= 1 << ((GPSStateline)statelines.get(i)).getPnr() - 1;
            int n = i;
            this.prnWithFlags[n] = this.prnWithFlags[n] | 1;
        }
        this.mAlmanacMask = 0;
        for (i = 0; i < statelines.size(); ++i) {
            if (!((GPSStateline)statelines.get(i)).isHasAlmanac()) continue;
            this.mAlmanacMask |= 1 << ((GPSStateline)statelines.get(i)).getPnr() - 1;
            int n = i;
            this.prnWithFlags[n] = this.prnWithFlags[n] | 2;
        }
        this.mUsedInFixMask = 0;
        for (i = 0; statelines.size() > i; ++i) {
            if (!((GPSStateline)statelines.get(i)).isUseInFix()) continue;
            this.mUsedInFixMask |= 1 << ((GPSStateline)statelines.get(i)).getPnr() - 1;
            int n = i;
            this.prnWithFlags[n] = this.prnWithFlags[n] | 4;
        }
    }

    public int getSvCount() {
        return this.svCount;
    }

    public int[] getPrnWithFlags() {
        return this.prnWithFlags;
    }
}

