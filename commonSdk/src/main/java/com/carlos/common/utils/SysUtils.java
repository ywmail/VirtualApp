//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.carlos.common.utils;

import com.carlos.libcommon.StringFog;
import java.io.FileInputStream;

public final class SysUtils {
    private static final String TAG = "SysUtils";

    public SysUtils() {
    }

    public static String getCurrentProcessName() {
        FileInputStream in = null;

        try {
            try {
                String fn = "/proc/self/cmdline";
                in = new FileInputStream(fn);
                byte[] buffer = new byte[256];

                int len;
                int b;
                for(len = 0; (b = in.read()) > 0 && len < buffer.length; buffer[len++] = (byte)b) {
                }

                if (len > 0) {
                    String s = new String(buffer, 0, len, "UTF-8");
                    return s;
                }
            } catch (Throwable var10) {
            }

            return null;
        } finally {
            ;
        }
    }
}
