/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 */
package mirror.android.content.pm;

import android.os.Parcelable;
import com.lody.virtual.StringFog;
import java.util.List;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefConstructor;
import mirror.RefMethod;
import mirror.RefStaticObject;

public class ParceledListSlice {
    public static RefStaticObject<Parcelable.Creator> CREATOR;
    public static Class<?> TYPE;
    public static RefMethod<Boolean> append;
    public static RefConstructor<Parcelable> ctor;
    @MethodParams(value={List.class})
    public static RefConstructor<Parcelable> ctorQ;
    public static RefMethod<Boolean> isLastSlice;
    public static RefMethod<Parcelable> populateList;
    public static RefMethod<Void> setLastSlice;
    public static RefMethod<List<?>> getList;

    static {
        TYPE = RefClass.load(ParceledListSlice.class, "android.content.pm.ParceledListSlice");
    }
}

