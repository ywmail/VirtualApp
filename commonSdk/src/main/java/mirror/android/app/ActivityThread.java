package mirror.android.app;

import android.app.Activity;
import android.app.Application;
import android.app.Instrumentation;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageManager;
import android.content.pm.ProviderInfo;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.IInterface;
import com.lody.virtual.StringFog;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import mirror.MethodParams;
import mirror.MethodReflectParams;
import mirror.RefClass;
import mirror.RefConstructor;
import mirror.RefMethod;
import mirror.RefObject;
import mirror.RefStaticInt;
import mirror.RefStaticMethod;
import mirror.RefStaticObject;

public class ActivityThread {
    public static Class<?> TYPE = RefClass.load(ActivityThread.class, "android.app.ActivityThread");

    public static RefStaticObject<Boolean> USE_CACHE;
    public static RefStaticMethod<Object> currentActivityThread;
    public static RefMethod<String> getProcessName;
    public static RefStaticMethod<String> currentPackageName;
    public static RefStaticMethod<Application> currentApplication;
    public static RefMethod<Handler> getHandler;
    public static RefMethod<Object> installProvider;
    public static RefObject<Object> mBoundApplication;
    public static RefObject<Handler> mH;
    public static RefObject<Application> mInitialApplication;
    public static RefObject<Instrumentation> mInstrumentation;
    public static RefObject<Map<String, WeakReference<?>>> mPackages;
    public static RefObject<Map<IBinder, Object>> mActivities;
    public static RefObject<Map> mProviderMap;

    @MethodParams({IBinder.class, List.class})
    public static RefMethod<Void> performNewIntents;

    public static RefStaticObject<IInterface> sPackageManager;
    public static RefStaticObject<IInterface> sPermissionManager;

    @MethodParams({IBinder.class, String.class, int.class, int.class, Intent.class})
    public static RefMethod<Void> sendActivityResult;

    public static RefMethod<IBinder> getApplicationThread;
    public static RefStaticMethod<IPackageManager> getPackageManager;
    public static RefMethod<Object> getLaunchingActivity;
    public static RefMethod<Object> getPackageInfoNoCheck;
    public static RefStaticMethod<IInterface> getPermissionManager;

    static {
        // RefClass.load 已在 TYPE 赋值时自动初始化所有 RefStaticObject/RefStaticMethod/RefObject/RefMethod 字段。
        // installProvider 使用方法已改为直接反射（处理多签名），不依赖 installProvider 字段。
    }

    public static Object installProvider(Object mainThread, Context context, ProviderInfo providerInfo, Object holder) throws Throwable {
        // 直接反射调用，不依赖 RefMethod 字段（避免多签名匹配问题）
        Class<?>[] sig6 = {Context.class, Object.class, ProviderInfo.class, boolean.class, boolean.class, boolean.class};
        Class<?>[] sig5 = {Context.class, Object.class, ProviderInfo.class, boolean.class, boolean.class};
        try {
            Method m;
            Object[] args;
            if (Build.VERSION.SDK_INT <= 15) {
                m = TYPE.getDeclaredMethod("installProvider", sig5);
                args = new Object[]{context, holder, providerInfo, false, true};
            } else {
                m = TYPE.getDeclaredMethod("installProvider", sig6);
                args = new Object[]{context, holder, providerInfo, false, true, true};
            }
            m.setAccessible(true);
            return m.invoke(mainThread, args);
        } catch (NoSuchMethodException e) {
            // fallback：尝试另一种签名
            try {
                Method m;
                Object[] args;
                if (Build.VERSION.SDK_INT <= 15) {
                    m = TYPE.getDeclaredMethod("installProvider", sig6);
                    args = new Object[]{context, holder, providerInfo, false, true, true};
                } else {
                    m = TYPE.getDeclaredMethod("installProvider", sig5);
                    args = new Object[]{context, holder, providerInfo, false, true};
                }
                m.setAccessible(true);
                return m.invoke(mainThread, args);
            } catch (Throwable ignore) {
                return null;
            }
        } catch (Throwable t) {
            if (t instanceof java.lang.reflect.InvocationTargetException && t.getCause() != null) {
                throw t.getCause();
            }
            return null;
        }
    }

    public static void handleNewIntent(Object obj, List list) {
        try {
            Object currentAT = currentActivityThread.call();
            if (currentAT != null) {
                Method declaredMethod = currentAT.getClass().getDeclaredMethod(
                        "handleNewIntent", obj.getClass(), List.class);
                if (declaredMethod != null) {
                    declaredMethod.setAccessible(true);
                    declaredMethod.invoke(currentAT, obj, list);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void USE_CACHE(boolean useCache) {
        if (USE_CACHE != null) {
            USE_CACHE.set(useCache);
        }
    }

    public static class H {
        public static Class<?> TYPE = RefClass.load(H.class, "android.app.ActivityThread$H");
        public static RefStaticInt LAUNCH_ACTIVITY;
        public static RefStaticInt EXECUTE_TRANSACTION;
        public static RefStaticInt SCHEDULE_CRASH;

        static {
            try {
                LAUNCH_ACTIVITY = new RefStaticInt(TYPE, TYPE.getDeclaredField("LAUNCH_ACTIVITY"));
                EXECUTE_TRANSACTION = new RefStaticInt(TYPE, TYPE.getDeclaredField("EXECUTE_TRANSACTION"));
                SCHEDULE_CRASH = new RefStaticInt(TYPE, TYPE.getDeclaredField("SCHEDULE_CRASH"));
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    public static class AppBindData {
        public static Class<?> TYPE = RefClass.load(AppBindData.class, "android.app.ActivityThread$AppBindData");
        public static RefObject<ApplicationInfo> appInfo;
        public static RefObject<Object> info;
        public static RefObject<String> processName;
        public static RefObject<ComponentName> instrumentationName;
        public static RefObject<List<ProviderInfo>> providers;

        static {
            try {
                appInfo = new RefObject<>(TYPE, TYPE.getDeclaredField("appInfo"));
                info = new RefObject<>(TYPE, TYPE.getDeclaredField("info"));
                processName = new RefObject<>(TYPE, TYPE.getDeclaredField("processName"));
                instrumentationName = new RefObject<>(TYPE, TYPE.getDeclaredField("instrumentationName"));
                providers = new RefObject<>(TYPE, TYPE.getDeclaredField("providers"));
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    public static class ProviderKeyJBMR1 {
        public static Class<?> TYPE = RefClass.load(ProviderKeyJBMR1.class, "android.app.ActivityThread$ProviderKey");
        @MethodParams({String.class, int.class})
        public static RefConstructor<?> ctor;

        static {
            try {
                Field field = ProviderKeyJBMR1.class.getDeclaredField("ctor");
                ctor = new RefConstructor<>(TYPE, field);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    public static class ProviderClientRecordJB {
        public static Class<?> TYPE = RefClass.load(ProviderClientRecordJB.class, "android.app.ActivityThread$ProviderClientRecord");
        public static RefObject<Object> mHolder;
        public static RefObject<IInterface> mProvider;

        static {
            try {
                mHolder = new RefObject<>(TYPE, TYPE.getDeclaredField("mHolder"));
                mProvider = new RefObject<>(TYPE, TYPE.getDeclaredField("mProvider"));
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    public static class ProviderClientRecord {
        public static Class<?> TYPE = RefClass.load(ProviderClientRecord.class, "android.app.ActivityThread$ProviderClientRecord");
        @MethodReflectParams({"android.app.ActivityThread", "java.lang.String", "android.content.IContentProvider", "android.content.ContentProvider"})
        public static RefConstructor<?> ctor;
        public static RefObject<String> mName;
        public static RefObject<IInterface> mProvider;

        static {
            try {
                Field field = ProviderClientRecord.class.getDeclaredField("ctor");
                ctor = new RefConstructor<>(TYPE, field);
                mName = new RefObject<>(TYPE, TYPE.getDeclaredField("mName"));
                mProvider = new RefObject<>(TYPE, TYPE.getDeclaredField("mProvider"));
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    public static class ActivityClientRecord {
        public static Class<?> TYPE = RefClass.load(ActivityClientRecord.class, "android.app.ActivityThread$ActivityClientRecord");
        public static RefObject<Activity> activity;
        public static RefObject<ActivityInfo> activityInfo;
        public static RefObject<Intent> intent;
        public static RefObject<IBinder> token;
        public static RefObject<Boolean> isTopResumedActivity;
        public static RefObject<Object> packageInfo;
        public static RefObject<Object> compatInfo;

        static {
            try {
                activity = new RefObject<>(TYPE, TYPE.getDeclaredField("activity"));
                activityInfo = new RefObject<>(TYPE, TYPE.getDeclaredField("activityInfo"));
                intent = new RefObject<>(TYPE, TYPE.getDeclaredField("intent"));
                token = new RefObject<>(TYPE, TYPE.getDeclaredField("token"));
                isTopResumedActivity = new RefObject<>(TYPE, TYPE.getDeclaredField("isTopResumedActivity"));
                packageInfo = new RefObject<>(TYPE, TYPE.getDeclaredField("packageInfo"));
                compatInfo = new RefObject<>(TYPE, TYPE.getDeclaredField("compatInfo"));
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }
}