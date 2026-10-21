/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Intent
 *  android.content.pm.ActivityInfo
 *  android.os.IInterface
 */
package mirror.android.app.servertransaction;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.IInterface;
import com.lody.virtual.StringFog;
import mirror.RefClass;
import mirror.RefObject;

public class LaunchActivityItem {
    public static Class<?> TYPE = RefClass.load(LaunchActivityItem.class, "android.app.servertransaction.LaunchActivityItem");
    public static RefObject<ActivityInfo> mInfo;
    public static RefObject<Intent> mIntent;
    public static RefObject<IInterface> mActivityClientController;
}

