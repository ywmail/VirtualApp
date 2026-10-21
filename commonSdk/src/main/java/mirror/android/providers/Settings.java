package mirror.android.providers;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.os.IInterface;
import com.lody.virtual.StringFog;
import mirror.RefClass;
import mirror.RefMethod;
import mirror.RefObject;
import mirror.RefStaticObject;

import java.lang.reflect.Field;

public class Settings {
    public static Class<?> TYPE = RefClass.load(Settings.class, android.provider.Settings.class);

    public Settings() {
    }

    public static class Config {
        public static Class<?> TYPE = RefClass.load(Config.class, "android.provider.Settings$Config");
        private static RefMethod<Object> getString;

        static {
            try {
                Field field = TYPE.getDeclaredField("getString");
                getString = new RefMethod<>(TYPE, field);
            } catch (Throwable e) {
                getString = null;
            }
        }

        public Config() { }

        public static Object getString(ContentResolver contentResolver, String str) {
            RefMethod<Object> refMethod = getString;
            return refMethod == null ? null : refMethod.call(new Object[]{contentResolver, str}, new Object[0]);
        }
    }

    public static class System {
        public static Class<?> TYPE = RefClass.load(System.class, android.provider.Settings.System.class);
        public static RefStaticObject<Object> sNameValueCache;

        static {
            try {
                Field field = TYPE.getDeclaredField("sNameValueCache");
                sNameValueCache = new RefStaticObject<>(TYPE, field);
            } catch (Throwable e) {
                sNameValueCache = null;
            }
        }

        public System() {}
    }

    public static class Secure {
        public static Class<?> TYPE = RefClass.load(Secure.class, android.provider.Settings.Secure.class);
        public static RefStaticObject<Object> sNameValueCache;

        static {
            try {
                Field field = TYPE.getDeclaredField("sNameValueCache");
                sNameValueCache = new RefStaticObject<>(TYPE, field);
            } catch (Throwable e) {
                sNameValueCache = null;
            }
        }

        public Secure() {}
    }

    public static class ContentProviderHolder {
        public static Class<?> TYPE = RefClass.load(ContentProviderHolder.class, "android.provider.Settings$ContentProviderHolder");
        public static RefObject<IInterface> mContentProvider;

        static {
            try {
                Field field = TYPE.getDeclaredField("mContentProvider");
                mContentProvider = new RefObject<>(TYPE, field);
            } catch (Throwable e) {
                mContentProvider = null;
            }
        }

        public ContentProviderHolder() {}
    }

    public static class NameValueCacheOreo {
        public static Class<?> TYPE = RefClass.load(NameValueCacheOreo.class, "android.provider.Settings$NameValueCache");
        public static RefObject<Object> mProviderHolder;

        static {
            try {
                Field field = TYPE.getDeclaredField("mProviderHolder");
                mProviderHolder = new RefObject<>(TYPE, field);
            } catch (Throwable e) {
                mProviderHolder = null;
            }
        }

        public NameValueCacheOreo() {}
    }

    public static class NameValueCache {
        public static Class<?> TYPE = RefClass.load(NameValueCache.class, "android.provider.Settings$NameValueCache");
        public static RefObject<Object> mContentProvider;

        static {
            try {
                Field field = TYPE.getDeclaredField("mContentProvider");
                mContentProvider = new RefObject<>(TYPE, field);
            } catch (Throwable e) {
                mContentProvider = null;
            }
        }

        public NameValueCache() {}
    }

    @TargetApi(17)
    public static class Global {
        public static Class<?> TYPE = RefClass.load(Global.class, android.provider.Settings.Global.class);
        public static RefStaticObject<Object> sNameValueCache;

        static {
            try {
                Field field = TYPE.getDeclaredField("sNameValueCache");
                sNameValueCache = new RefStaticObject<>(TYPE, field);
            } catch (Throwable e) {
                sNameValueCache = null;
            }
        }

        public Global() {}
    }
}