/*
 * Decompiled with CFR 0.152.
 */
package mirror.com.android.internal.content;

import com.lody.virtual.StringFog;
import java.io.File;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefStaticMethod;

public class NativeLibraryHelper {
    public static Class<?> TYPE = RefClass.load(NativeLibraryHelper.class, "com.android.internal.content.NativeLibraryHelper");
    @MethodParams(value={Handle.class, File.class, String.class})
    public static RefStaticMethod<Integer> copyNativeBinaries;
    @MethodParams(value={Handle.class, String[].class})
    public static RefStaticMethod<Integer> findSupportedAbi;

    public static class Handle {
        public static Class<?> TYPE = RefClass.load(Handle.class, "com.android.internal.content.NativeLibraryHelper$Handle");
        @MethodParams(value={File.class})
        public static RefStaticMethod<Object> create;
    }
}

