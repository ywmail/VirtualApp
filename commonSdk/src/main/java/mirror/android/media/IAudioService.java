/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.IBinder
 *  android.os.IInterface
 */
package mirror.android.media;

import android.os.IBinder;
import android.os.IInterface;
import com.lody.virtual.StringFog;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefStaticMethod;

public class IAudioService {
    public static Class<?> TYPE = RefClass.load(IAudioService.class, "android.media.IAudioService");

    public static class Stub {
        public static Class<?> TYPE = RefClass.load(Stub.class, "android.media.IAudioService$Stub");
        @MethodParams(value={IBinder.class})
        public static RefStaticMethod<IInterface> asInterface;
    }
}

