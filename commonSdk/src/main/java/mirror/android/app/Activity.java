/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.content.Intent
 *  android.content.pm.ActivityInfo
 *  android.os.IBinder
 */
package mirror.android.app;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.IBinder;
import com.lody.virtual.StringFog;
import mirror.RefBoolean;
import mirror.RefClass;
import mirror.RefInt;
import mirror.RefObject;

public class Activity {
    public static Class<?> TYPE = RefClass.load(Activity.class, "android.app.Activity");
    public static RefObject<ActivityInfo> mActivityInfo;
    public static RefBoolean mFinished;
    public static RefObject<android.app.Activity> mParent;
    public static RefInt mResultCode;
    public static RefObject<Intent> mResultData;
    public static RefObject<IBinder> mToken;
    public static RefObject<String> mEmbeddedID;
}

