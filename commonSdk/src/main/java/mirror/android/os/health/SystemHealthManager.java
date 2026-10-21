/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.annotation.TargetApi
 *  android.os.IInterface
 *  android.os.health.SystemHealthManager
 */
package mirror.android.os.health;

import android.annotation.TargetApi;
import android.os.IInterface;
import mirror.RefClass;
import mirror.RefObject;

@TargetApi(value=24)
public class SystemHealthManager {
    public static Class<?> TYPE = RefClass.load(SystemHealthManager.class, android.os.health.SystemHealthManager.class);
    public static RefObject<IInterface> mBatteryStats;
}

