# 全局通用规则
-dontshrink
-dontwarn
-ignorewarnings
-keepattributes *Annotation*,InnerClasses,Signature,EnclosingMethod
-dontskipnonpubliclibraryclassmembers
-printconfiguration

# 通用组件 keep
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.ContentProvider

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
-keepclassmembers class * extends android.os.Binder {
    public <methods>;
}
-keepclassmembers class * implements java.io.Serializable {*;}
-keepclasseswithmembernames class * {
    native <methods>;
}

# android 系统类
-keep class android.** { *; }

# support.annotation.Keep
-keep,allowobfuscation @interface android.support.annotation.Keep
-keep @android.support.annotation.Keep class * { *; }
-keepclassmembers class * {
    @android.support.annotation.Keep *;
}

# Trend LazyInject Component 注解
-keep,allowobfuscation @interface com.trend.lazyinject.annotation.Component
-keep,allowobfuscation @com.trend.lazyinject.annotation.Component class * { *; }
-keepclassmembers class * {
    @com.trend.lazyinject.annotation.Component *;
}

# 第三方库
-keep class c.t.m.g.** { *; }
-keep class com.tencent.** { *; }
-keep class jonathanfinerty.once.** { public *; }

# andhook
-keep class andhook.lib.AndHook$Dalvik
-keepclassmembers class andhook.lib.AndHook$Dalvik { native <methods>; }
-keep class andhook.lib.AndHook
-keepclassmembers class andhook.lib.AndHook { native <methods>; }
-keep class andhook.lib.YunOSHelper
-keepclassmembers class andhook.lib.YunOSHelper { public *; }
-keep class andhook.lib.xposed.XC_MethodHook
-keepclassmembers class andhook.lib.xposed.XC_MethodHook { *; }
-keep class andhook.lib.xposed.XC_MethodHook$*
-keepclassmembers class andhook.lib.xposed.XC_MethodHook$* { *; }
-keep class * extends andhook.lib.xposed.XC_MethodHook
-keepclassmembers class * extends andhook.lib.xposed.XC_MethodHook {
   public *;
   protected *;
}

# easyxp / vposed
-keep class com.easy.android.easyxp.*
-keepclassmembers class com.easy.android.easyxp.* { *; }
-keep class android.app.AndroidAppHelper
-keepclassmembers class android.app.AndroidAppHelper { public *; }
-keep class io.vposed.VPosed
-keepclassmembers class io.vposed.VPosed { public *; }

# sun.misc.Unsafe — 项目自带 stub 类，运行时用系统的 Unsafe，
# R8 混淆方法名会导致 NoSuchMethodError（如 objectFieldOffset 被改成 b）
-keep class sun.misc.Unsafe { *; }
-keepclassmembers class sun.misc.Unsafe { *; }

# 禁用 R8 优化阶段 — R8 优化会移除 VirtualCore.startup() 里的字段赋值逻辑，
# 导致 processType 为 null，initialize() 的 switch 抛 ordinal() on null
-dontoptimize

# Keep VirtualCore 及内部类 — R8 混淆破坏 processType 字段赋值与 enum switch 语义
-keep class com.lody.virtual.client.core.** { *; }
-keepclassmembers class com.lody.virtual.client.core.** { *; }

# Keep hiddenapibypass — Helper 内的 MethodHandle/MethodHandleImpl 是布局 stub 类，
# 字段顺序必须与系统 java.lang.invoke.MethodHandleImpl 一致，R8 重排字段会导致
# Unsafe.objectFieldOffset 算出错误偏移，引发 ClassCastException
-keep class com.lody.virtual.client.hiddenapibypass.** { *; }
-keepclassmembers class com.lody.virtual.client.hiddenapibypass.** { *; }

