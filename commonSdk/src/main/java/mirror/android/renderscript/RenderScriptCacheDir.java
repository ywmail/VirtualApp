/*
 * Decompiled with CFR 0.152.
 */
package mirror.android.renderscript;

import com.lody.virtual.StringFog;
import java.io.File;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefStaticMethod;

public class RenderScriptCacheDir {
    public static Class<?> TYPE = RefClass.load(RenderScriptCacheDir.class, "android.renderscript.RenderScriptCacheDir");
    @MethodParams(value={File.class})
    public static RefStaticMethod<Void> setupDiskCache;
}

