/*
 * Decompiled with CFR 0.152.
 */
package mirror.android.ddm;

import com.lody.virtual.StringFog;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefStaticMethod;

public class DdmHandleAppName {
    public static Class Class = RefClass.load(DdmHandleAppName.class, "android.ddm.DdmHandleAppName");
    @MethodParams(value={String.class})
    public static RefStaticMethod<Void> setAppName;
}

