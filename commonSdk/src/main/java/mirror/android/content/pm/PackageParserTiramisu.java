/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.pm.ActivityInfo
 *  android.content.pm.ApplicationInfo
 *  android.content.pm.PackageInfo
 *  android.content.pm.ProviderInfo
 *  android.content.pm.ServiceInfo
 */
package mirror.android.content.pm;

import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageParser;
import android.content.pm.ProviderInfo;
import android.content.pm.ServiceInfo;
import com.lody.virtual.StringFog;
import java.io.File;
import mirror.MethodParams;
import mirror.MethodReflectParams;
import mirror.RefClass;
import mirror.RefConstructor;
import mirror.RefMethod;
import mirror.RefStaticMethod;

public class PackageParserTiramisu {
    public static Class<?> TYPE = RefClass.load(PackageParserTiramisu.class, "android.content.pm.PackageParser");
    public static RefConstructor<PackageParser> ctor;
    @MethodReflectParams(value={"android.content.pm.PackageParser$Activity", "int", "android.content.pm.pkg.FrameworkPackageUserState", "int"})
    public static RefStaticMethod<ActivityInfo> generateActivityInfo;
    @MethodReflectParams(value={"android.content.pm.PackageParser$Package", "int", "android.content.pm.pkg.FrameworkPackageUserState"})
    public static RefStaticMethod<ApplicationInfo> generateApplicationInfo;
    @MethodReflectParams(value={"android.content.pm.PackageParser$Package", "[I", "int", "long", "long", "java.util.Set", "android.content.pm.pkg.FrameworkPackageUserState"})
    public static RefStaticMethod<PackageInfo> generatePackageInfo;
    @MethodReflectParams(value={"android.content.pm.PackageParser$Provider", "int", "android.content.pm.pkg.FrameworkPackageUserState", "int"})
    public static RefStaticMethod<ProviderInfo> generateProviderInfo;
    @MethodReflectParams(value={"android.content.pm.PackageParser$Service", "int", "android.content.pm.pkg.FrameworkPackageUserState", "int"})
    public static RefStaticMethod<ServiceInfo> generateServiceInfo;
    @MethodParams(value={File.class, int.class})
    public static RefMethod<PackageParser.Package> parsePackage;
}

