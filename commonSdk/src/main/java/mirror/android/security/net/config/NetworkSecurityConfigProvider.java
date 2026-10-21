/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 */
package mirror.android.security.net.config;

import android.content.Context;
import com.lody.virtual.StringFog;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefStaticMethod;

public class NetworkSecurityConfigProvider {
    public static Class<?> TYPE = RefClass.load(NetworkSecurityConfigProvider.class, "android.security.net.config.NetworkSecurityConfigProvider");
    @MethodParams(value={Context.class})
    public static RefStaticMethod<Void> install;
}

