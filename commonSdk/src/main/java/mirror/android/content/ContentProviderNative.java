/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.IBinder
 *  android.os.IInterface
 */
package mirror.android.content;

import android.os.IBinder;
import android.os.IInterface;
import com.lody.virtual.StringFog;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefStaticMethod;

public class ContentProviderNative {
    public static Class<?> TYPE = RefClass.load(ContentProviderNative.class, "android.content.ContentProviderNative");
    @MethodParams(value={IBinder.class})
    public static RefStaticMethod<IInterface> asInterface;
}

