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
        try {
            try {
                USE_CACHE = new RefStaticObject<>(TYPE, TYPE.getDeclaredField("USE_CACHE"));
            } catch (NoSuchFieldException ignore) {
                // Android 14+ 移除了 USE_CACHE 字段
            }
            currentActivityThread = new RefStaticMethod<>(TYPE, TYPE.getDeclaredField("currentActivityThread"));
            getProcessName = initRefMethod("getProcessName");
            currentPackageName = new RefStaticMethod<>(TYPE, TYPE.getDeclaredField("currentPackageName"));
            currentApplication = new RefStaticMethod<>(TYPE, TYPE.getDeclaredField("currentApplication"));
            getHandler = initRefMethod("getHandler");

            // installProvider签名可能存在差异，优先反射6参
            try {
                Method m = TYPE.getDeclaredMethod("installProvider", Context.class, Object.class, ProviderInfo.class,
                        boolean.class, boolean.class, boolean.class);
                installProvider = new RefMethod<>(TYPE, asField("installProvider"));
            } catch (Throwable e) {
                try {
                    Method m = TYPE.getDeclaredMethod("installProvider", Context.class, Object.class, ProviderInfo.class,
                            boolean.class, boolean.class);
                    installProvider = new RefMethod<>(TYPE, asField("installProvider"));
                } catch (Throwable ignore) {}
            }

            mBoundApplication = new RefObject<>(TYPE, TYPE.getDeclaredField("mBoundApplication"));
            mH = new RefObject<>(TYPE, TYPE.getDeclaredField("mH"));
            mInitialApplication = new RefObject<>(TYPE, TYPE.getDeclaredField("mInitialApplication"));
            mInstrumentation = new RefObject<>(TYPE, TYPE.getDeclaredField("mInstrumentation"));
            mPackages = new RefObject<>(TYPE, TYPE.getDeclaredField("mPackages"));
            mActivities = new RefObject<>(TYPE, TYPE.getDeclaredField("mActivities"));
            mProviderMap = new RefObject<>(TYPE, TYPE.getDeclaredField("mProviderMap"));

            Field fPerformNewIntents = ActivityThread.class.getDeclaredField("performNewIntents");
            performNewIntents = new RefMethod<>(TYPE, fPerformNewIntents);

            sPackageManager = new RefStaticObject<>(TYPE, TYPE.getDeclaredField("sPackageManager"));
            sPermissionManager = new RefStaticObject<>(TYPE, TYPE.getDeclaredField("sPermissionManager"));

            Field fSendActivityResult = ActivityThread.class.getDeclaredField("sendActivityResult");
            sendActivityResult = new RefMethod<>(TYPE, fSendActivityResult);

            getApplicationThread = initRefMethod("getApplicationThread");
            getPackageManager = new RefStaticMethod<>(TYPE, TYPE.getDeclaredField("getPackageManager"));
            getLaunchingActivity = initRefMethod("getLaunchingActivity");

            // getPackageInfoNoCheck参数在不同系统有变化，请根据实际字段和注解情况初始化
            getPackageInfoNoCheck = initRefMethod("getPackageInfoNoCheck");

            getPermissionManager = new RefStaticMethod<>(TYPE, TYPE.getDeclaredField("getPermissionManager"));

        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    // 支持无注解的 RefMethod 自动初始化
    private static <T> RefMethod<T> initRefMethod(String name) {
        try {
            Field field = ActivityThread.class.getDeclaredField(name);
            return new RefMethod<>(TYPE, field);
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }
    // 用于补充无直接Field时的临时Field构造
    private static Field asField(String name) throws NoSuchFieldException {
        Field f = ActivityThread.class.getDeclaredField(name);
        return f;
    }

    public static Object installProvider(Object mainThread, Context context, ProviderInfo providerInfo, Object holder) throws Throwable {
        if (Build.VERSION.SDK_INT <= 15 && installProvider != null) {
            return installProvider.callWithException(mainThread, context, holder, providerInfo, false, true);
        }
        if (installProvider != null)
            return installProvider.callWithException(mainThread, context, holder, providerInfo, false, true, true);
        return null;
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