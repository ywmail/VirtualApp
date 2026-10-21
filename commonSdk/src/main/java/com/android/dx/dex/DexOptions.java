/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.dex;

import com.android.dex.DexFormat;
import java.io.PrintStream;

public final class DexOptions {
    public static final boolean ALIGN_64BIT_REGS_SUPPORT = true;
    public boolean ALIGN_64BIT_REGS_IN_OUTPUT_FINISHER = true;
    public int minSdkVersion = 13;
    public boolean forceJumbo = false;
    public boolean allowAllInterfaceMethodInvokes = false;
    public final PrintStream err;

    public DexOptions() {
        this.err = System.err;
    }

    public DexOptions(PrintStream stream) {
        this.err = stream;
    }

    public String getMagic() {
        return DexFormat.apiToMagic(this.minSdkVersion);
    }

    public boolean apiIsSupported(int apiLevel) {
        return this.minSdkVersion >= apiLevel;
    }
}

