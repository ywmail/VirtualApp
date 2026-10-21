/*
 * Decompiled with CFR 0.152.
 */
package de.robv.android.xposed;

import de.robv.android.xposed.IXposedMod;

public interface IXposedHookCmdInit
extends IXposedMod {
    public void initCmdApp(StartupParam var1) throws Throwable;

    public static final class StartupParam {
        public String modulePath;
        public String startClassName;

        StartupParam() {
        }
    }
}

