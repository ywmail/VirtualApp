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

public class ParceledListSliceJBMR2 {
    public static RefStaticObject<Parcelable.Creator> CREATOR;
    public static Class<?> TYPE;
    @MethodParams(value={List.class})
    public static RefConstructor<Parcelable> ctor;
    public static RefMethod<List> getList;

    static {
        TYPE = RefClass.load(ParceledListSliceJBMR2.class, "android.content.pm.ParceledListSlice");
    }
}

