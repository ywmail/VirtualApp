/*
 * Decompiled with CFR 0.152.
 */
package mirror.android.content.pm;

import android.content.pm.PackageParser;
import com.lody.virtual.StringFog;
import com.swift.sandhook.annotation.MethodReflectParams;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefConstructor;

public class SigningInfo {
    public static Class<?> TYPE = RefClass.load(SigningInfo.class, "android.content.pm.SigningInfo");
    @MethodParams(value={PackageParser.SigningDetails.class})
    public static RefConstructor<Object> ctor;
    @MethodReflectParams(value={"android.content.pm.SigningDetails"})
    public static RefConstructor<Object> ctor2;
}

