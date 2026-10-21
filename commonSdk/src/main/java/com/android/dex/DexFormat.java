/*
 * Decompiled with CFR 0.152.
 */
package com.android.dex;

public final class DexFormat {
    public static final int API_CONST_METHOD_HANDLE = 28;
    public static final int API_METHOD_HANDLES = 26;
    public static final int API_DEFINE_INTERFACE_METHODS = 24;
    public static final int API_INVOKE_INTERFACE_METHODS = 24;
    public static final int API_INVOKE_STATIC_INTERFACE_METHODS = 21;
    public static final int API_NO_EXTENDED_OPCODES = 13;
    public static final int API_CURRENT = 28;
    public static final String VERSION_FOR_API_28 = "039";
    public static final String VERSION_FOR_API_26 = "038";
    public static final String VERSION_FOR_API_24 = "037";
    public static final String VERSION_FOR_API_13 = "035";
    public static final String VERSION_CURRENT = "039";
    public static final String DEX_IN_JAR_NAME = "classes.dex";
    public static final String MAGIC_PREFIX = "dex\n";
    public static final String MAGIC_SUFFIX = "\u0000";
    public static final int ENDIAN_TAG = 305419896;
    public static final int MAX_MEMBER_IDX = 65535;
    public static final int MAX_TYPE_IDX = 65535;

    private DexFormat() {
    }

    public static int magicToApi(byte[] magic) {
        if (magic.length != 8) {
            return -1;
        }
        if (magic[0] != 100 || magic[1] != 101 || magic[2] != 120 || magic[3] != 10 || magic[7] != 0) {
            return -1;
        }
        String version = "" + (char)magic[4] + (char)magic[5] + (char)magic[6];
        if (version.equals(VERSION_FOR_API_13)) {
            return 13;
        }
        if (version.equals(VERSION_FOR_API_24)) {
            return 24;
        }
        if (version.equals(VERSION_FOR_API_26)) {
            return 26;
        }
        if (version.equals("039")) {
            return 28;
        }
        if (version.equals("039")) {
            return 28;
        }
        return -1;
    }

    public static String apiToMagic(int targetApiLevel) {
        String version = targetApiLevel >= 28 ? "039" : (targetApiLevel >= 28 ? "039" : (targetApiLevel >= 26 ? VERSION_FOR_API_26 : (targetApiLevel >= 24 ? VERSION_FOR_API_24 : VERSION_FOR_API_13)));
        return MAGIC_PREFIX + version + MAGIC_SUFFIX;
    }

    public static boolean isSupportedDexMagic(byte[] magic) {
        int api = DexFormat.magicToApi(magic);
        return api > 0;
    }
}

