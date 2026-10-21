/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.pm.ActivityInfo
 *  android.content.pm.ApplicationInfo
 *  android.content.pm.PackageInfo
 *  android.content.pm.ProviderInfo
 *  android.content.pm.ServiceInfo
 *  android.util.DisplayMetrics
 */
package mirror.android.content.pm;

import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageParser;
import android.content.pm.ProviderInfo;
import android.content.pm.ServiceInfo;
import android.util.DisplayMetrics;
import com.lody.virtual.StringFog;
import java.io.File;
import mirror.MethodParams;
import mirror.MethodReflectParams;
import mirror.RefClass;
import mirror.RefConstructor;
import mirror.RefMethod;
import mirror.RefStaticMethod;

public class PackageParserJellyBean {
    public static Class<?> TYPE = RefClass.load(PackageParserJellyBean.class, "android.content.pm.PackageParser");
    @MethodReflectParams(value={"android.content.pm.PackageParser$Package", "int"})
    public static RefMethod<Void> collectCertificates;
    @MethodParams(value={String.class})
    public static RefConstructor<PackageParser> ctor;
    @MethodReflectParams(value={"android.content.pm.PackageParser$Activity", "int", "boolean", "int", "int"})
    public static RefStaticMethod<ActivityInfo> generateActivityInfo;
    @MethodReflectParams(value={"android.content.pm.PackageParser$Package", "int", "boolean", "int"})
    public static RefStaticMethod<ApplicationInfo> generateApplicationInfo;
    @MethodReflectParams(value={"android.content.pm.PackageParser$Package", "[I", "int", "long", "long", "java.util.HashSet"})
    public static RefStaticMethod<PackageInfo> generatePackageInfo;
    @MethodReflectParams(value={"android.content.pm.PackageParser$Provider", "int", "boolean", "int", "int"})
    public static RefStaticMethod<ProviderInfo> generateProviderInfo;
    @MethodReflectParams(value={"android.content.pm.PackageParser$Service", "int", "boolean", "int", "int"})
    public static RefStaticMethod<ServiceInfo> generateServiceInfo;
    @MethodParams(value={File.class, String.class, DisplayMetrics.class, int.class})
    public static RefMethod<PackageParser.Package> parsePackage;
}

