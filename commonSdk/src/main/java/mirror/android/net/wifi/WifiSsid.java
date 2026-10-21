/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.annotation.TargetApi
 */
package mirror.android.net.wifi;

import android.annotation.TargetApi;
import com.lody.virtual.StringFog;
import mirror.RefClass;
import mirror.RefStaticMethod;

@TargetApi(value=19)
public class WifiSsid {
    public static final Class<?> TYPE = RefClass.load(WifiSsid.class, "android.net.wifi.WifiSsid");
    public static RefStaticMethod<Object> createFromAsciiEncoded;
    public static RefStaticMethod<Object> createFromByteArray;
    public static RefStaticMethod<Object> createFromHex;
}

