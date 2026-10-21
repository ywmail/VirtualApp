/*
 * Decompiled with CFR 0.152.
 */
package com.lody.virtual.oem;

import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.helper.compat.BuildCompat;
import mirror.android.app.ActivityThread;
import mirror.huawei.android.app.HwApiCacheManagerEx;
import mirror.huawei.android.app.HwFrameworkFactory;

public class EmuiHelper {
    public static void disableCache() {
        if (BuildCompat.isEMUI()) {
            Object hwApiCacheManagerEx = HwFrameworkFactory.getHwApiCacheManagerEx();
            try {
                if (HwFrameworkFactory.TYPE != null && hwApiCacheManagerEx != null && HwApiCacheManagerEx.TYPE != null && HwApiCacheManagerEx.TYPE.isInstance(hwApiCacheManagerEx)) {
                    HwApiCacheManagerEx.disableCache(hwApiCacheManagerEx);
                    ActivityThread.USE_CACHE(false);
                    if (HwApiCacheManagerEx.mPkg != null) {
                        HwApiCacheManagerEx.mPkg.set(HwApiCacheManagerEx.getDefault.call(new Object[0]), VirtualCore.getPM());
                    }
                }
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
    }
}

