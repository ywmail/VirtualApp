/*
 * Decompiled with CFR 0.152.
 */
package mirror.android.compat;

import com.lody.virtual.StringFog;
import mirror.MethodReflectParams;
import mirror.RefClass;
import mirror.RefMethod;
import mirror.RefStaticObject;

public class Compatibility {
    public static Class<?> TYPE = RefClass.load(Compatibility.class, "android.compat.Compatibility");
    public static RefStaticObject<Object> DEFAULT_CALLBACKS;
    public static RefStaticObject<Object> sCallbacks;
    @MethodReflectParams(value={"android.compat.Compatibility$BehaviorChangeDelegate"})
    public static RefMethod<Void> setBehaviorChangeDelegate;
}

