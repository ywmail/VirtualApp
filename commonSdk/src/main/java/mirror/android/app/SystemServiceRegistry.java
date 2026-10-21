/*
 * Decompiled with CFR 0.152.
 */
package mirror.android.app;

import com.lody.virtual.StringFog;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefStaticMethod;
import mirror.android.app.ContextImpl;

public class SystemServiceRegistry {
    public static Class<?> TYPE = RefClass.load(SystemServiceRegistry.class, "android.app.SystemServiceRegistry");
    @MethodParams(value={ContextImpl.class, String.class})
    public static RefStaticMethod<Object> getSystemService;
}

