/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.IInterface
 */
package mirror.com.android.internal.policy;

import android.os.IInterface;
import com.lody.virtual.StringFog;
import mirror.RefClass;
import mirror.RefStaticObject;

public class PhoneWindow {
    public static Class<?> TYPE = RefClass.load(PhoneWindow.class, "com.android.internal.policy.impl.PhoneWindow$WindowManagerHolder");
    public static RefStaticObject<IInterface> sWindowManager;

    static {
        if (TYPE == null) {
            TYPE = RefClass.load(PhoneWindow.class, "com.android.internal.policy.PhoneWindow$WindowManagerHolder");
        }
    }
}

