/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.pm.Signature
 *  android.util.ArraySet
 */
package mirror.android.content.pm;

import android.content.pm.PackageParser;
import android.content.pm.Signature;
import android.util.ArraySet;
import com.lody.virtual.StringFog;
import com.lody.virtual.helper.utils.Reflect;
import java.lang.reflect.Constructor;
import mirror.MethodReflectParams;
import mirror.RefClass;
import mirror.RefConstructor;
import mirror.android.content.pm.SigningInfo;

public class SigningInfoT {
    public static Class<?> TYPE = RefClass.load(SigningInfo.class, "android.content.pm.SigningInfo");
    @MethodReflectParams(value={"android.content.pm.SigningDetails"})
    public static RefConstructor<Object> ctor;

    public static Object createSigningInfo(PackageParser.SigningDetails signingDetails) {
        try {
            Object detail = Class.forName("android.content.pm.SigningDetails").getDeclaredConstructor(new Signature[0].getClass(), Integer.TYPE, new ArraySet().getClass(), new Signature[0].getClass()).newInstance(Reflect.on(signingDetails).field("signatures").get(), Reflect.on(signingDetails).field("signatureSchemeVersion").get(), Reflect.on(signingDetails).field("publicKeys").get(), Reflect.on(signingDetails).field("pastSigningCertificates").get());
            for (Constructor<?> constructorSigInfo : Class.forName("android.content.pm.SigningInfo").getDeclaredConstructors()) {
                if (!constructorSigInfo.toString().contains("SigningDetails")) continue;
                return constructorSigInfo.newInstance(detail);
            }
            return null;
        }
        catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }
}

