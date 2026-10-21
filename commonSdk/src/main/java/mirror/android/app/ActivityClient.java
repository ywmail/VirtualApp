/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.IInterface
 */
package mirror.android.app;

import android.os.IInterface;
import com.lody.virtual.StringFog;
import mirror.RefClass;
import mirror.RefObject;
import mirror.RefStaticMethod;
import mirror.RefStaticObject;

public class ActivityClient {
    public static Class<?> TYPE = RefClass.load(ActivityClient.class, "android.app.ActivityClient");
    public static RefStaticMethod<IInterface> getActivityClientController;
    public static RefStaticObject<Object> INTERFACE_SINGLETON;

    public static class ActivityClientControllerSingleton {
        public static Class<?> TYPE = RefClass.load(ActivityClientControllerSingleton.class, "android.app.ActivityClient$ActivityClientControllerSingleton");
        public static RefObject<IInterface> mKnownInstance;
    }
}

