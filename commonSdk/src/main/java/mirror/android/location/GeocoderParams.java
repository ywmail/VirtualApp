/*
 * Decompiled with CFR 0.152.
 */
package mirror.android.location;

import com.lody.virtual.StringFog;
import mirror.RefClass;
import mirror.RefObject;

public class GeocoderParams {
    public static Class<?> TYPE = RefClass.load(GeocoderParams.class, "android.location.GeocoderParams");
    public static RefObject<String> mPackageName;
    public static RefObject<Integer> mUid;

    public static void mPackageName(Object obj, String str) {
        RefObject<String> field = mPackageName;
        if (field != null) {
            field.set(obj, str);
        }
    }

    public static void mUid(Object obj, int i2) {
        RefObject<Integer> field = mUid;
        if (field != null) {
            field.set(obj, i2);
        }
    }

    public static String mPackageName(Object obj) {
        RefObject<String> field = mPackageName;
        if (field != null) {
            return field.get(obj);
        }
        return null;
    }

    public static int mUid(Object obj) {
        RefObject<Integer> field = mUid;
        if (field != null) {
            return field.get(obj);
        }
        return 0;
    }
}

