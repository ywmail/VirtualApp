/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Handler$Callback
 */
package mirror.android.os;


import com.lody.virtual.StringFog;
import mirror.RefClass;
import mirror.RefObject;

public class Handler {
    public static Class<?> TYPE = RefClass.load(android.os.Handler.class, "android.os.Handler");
    public static RefObject<android.os.Handler.Callback> mCallback;

    static {
        try {
            mCallback = new RefObject<>(TYPE, TYPE.getDeclaredField("mCallback"));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

}

