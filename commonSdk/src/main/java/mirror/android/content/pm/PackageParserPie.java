/*
 * Decompiled with CFR 0.152.
 */
package mirror.android.content.pm;

import com.lody.virtual.StringFog;
import java.io.File;
import mirror.MethodParams;
import mirror.MethodReflectParams;
import mirror.RefClass;
import mirror.RefStaticMethod;

public class PackageParserPie {
    public static Class<?> TYPE = RefClass.load(PackageParserPie.class, "android.content.pm.PackageParser");
    @MethodReflectParams(value={"android.content.pm.PackageParser$Package", "boolean"})
    public static RefStaticMethod<Void> collectCertificates;
    @MethodParams(value={File.class, int.class})
    public static RefStaticMethod<Object> parseClusterPackageLite;
    @MethodParams(value={File.class, int.class})
    public static RefStaticMethod<Object> parseMonolithicPackageLite;
    @MethodParams(value={File.class, int.class})
    public static RefStaticMethod<Object> parsePackageLite;
}

