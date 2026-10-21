/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Binder
 *  android.os.IBinder
 *  android.os.Parcelable
 *  android.util.Log
 */
package mirror.android.content;

import android.os.Binder;
import android.os.IBinder;
import android.os.Parcelable;
import android.util.Log;
import com.lody.virtual.StringFog;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefMethod;
import mirror.RefObject;
import mirror.android.content.AttributionSourceState;

public class AttributionSource {
    private static final String TAG = "AttributionSource";
    public static Class<?> TYPE = RefClass.load(AttributionSource.class, "android.content.AttributionSource");
    public static Class<?> TYPE_COMP = RefClass.load(AttributionSource.class, "android.content.AttributionSource$ay");
    @MethodParams(value={Object.class})
    public static RefMethod<Boolean> equals;
    public static RefMethod<String> getAttributionTag;
    public static RefMethod<Object> getNext;
    public static RefMethod<String> getPackageName;
    public static RefMethod<IBinder> getToken;
    @MethodParams(value={Binder.class})
    public static RefMethod<Parcelable> withToken;
    public static RefObject<Object> mAttributionSourceState;

    public static boolean equals(Object obj, Object obj2) {
        RefMethod<Boolean> method = equals;
        if (method == null) {
            return false;
        }
        return method.call(method, obj2);
    }

    public static String getAttributionTag(Object obj) {
        RefMethod<String> method = getAttributionTag;
        if (method != null) {
            return method.call(obj, new Object[0]);
        }
        return null;
    }

    public static String getPackageName(Object obj) {
        RefMethod<String> method = getPackageName;
        if (method != null) {
            return method.call(obj, new Object[0]);
        }
        return null;
    }

    public static IBinder getToken(Object obj) {
        RefMethod<IBinder> method = getToken;
        if (method != null) {
            return method.call(obj, new Object[0]);
        }
        return null;
    }

    public static void mAttributionSourceState(Object obj, Object obj2) {
        RefObject<Object> objRef = mAttributionSourceState;
        if (objRef != null) {
            objRef.set(obj, obj2);
        }
    }

    public static Parcelable newInstance(Object obj) {
        Object mAttributionSourceState2;
        Parcelable withToken2;
        if (obj == null || (withToken2 = AttributionSource.withToken(obj, null)) == null || (mAttributionSourceState2 = AttributionSource.mAttributionSourceState(withToken2)) == null) {
            Log.w((String)TAG, (String)"newInstance reduce failed,return null");
            return null;
        }
        AttributionSourceState.token(mAttributionSourceState2, AttributionSource.getToken(obj));
        Object mAttributionSourceState3 = AttributionSource.mAttributionSourceState(obj);
        if (mAttributionSourceState3 != null) {
            AttributionSourceState.next(mAttributionSourceState2, AttributionSourceState.next(mAttributionSourceState3));
        }
        return withToken2;
    }

    public static Parcelable withToken(Object obj, Binder binder) {
        RefMethod<Parcelable> method = withToken;
        if (method == null) {
            return null;
        }
        return method.call(obj, binder);
    }

    public static Object mAttributionSourceState(Object obj) {
        RefObject<Object> objRef = mAttributionSourceState;
        if (objRef != null) {
            return objRef.get(obj);
        }
        return null;
    }

    static {
        if (TYPE_COMP != null) {
            // empty if block
        }
        Class<AttributionSource> cls = AttributionSource.class;
        TYPE = RefClass.load(cls, "android.content.AttributionSource");
        TYPE_COMP = RefClass.load(cls, "android.content.AttributionSource$");
    }
}

