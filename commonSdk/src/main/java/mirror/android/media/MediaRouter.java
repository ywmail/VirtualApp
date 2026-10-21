/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.media.MediaRouter
 *  android.os.IInterface
 */
package mirror.android.media;

import android.os.IInterface;
import com.lody.virtual.StringFog;
import mirror.RefClass;
import mirror.RefObject;
import mirror.RefStaticObject;

public class MediaRouter {
    public static Class<?> TYPE = RefClass.load(MediaRouter.class, android.media.MediaRouter.class);
    public static RefStaticObject sStatic;

    public static class StaticKitkat {
        public static Class<?> TYPE = RefClass.load(StaticKitkat.class, "android.media.MediaRouter$Static");
        public static RefObject<IInterface> mMediaRouterService;
    }

    public static class Static {
        public static Class<?> TYPE = RefClass.load(Static.class, "android.media.MediaRouter$Static");
        public static RefObject<IInterface> mAudioService;
    }
}

