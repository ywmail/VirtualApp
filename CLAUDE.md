# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 项目概述

VirtualApp14 是一个 Android 应用虚拟化（沙箱/多开）框架，基于 Lody 的原版 VirtualApp 二次开发，com.ywmail.virtualapp 包名。它允许在同一设备上安装并运行同一应用的多个副本，并提供 Xposed 框架兼容（通过 SandHook）。README 中提到的"过韩国游戏盾"等能力属于本分支的外挂适配层，不在通用构建路径中。

> **源码来源**：本仓库基于 [ServenScorpion/VirtualApp](https://github.com/ServenScorpion/VirtualApp)（上游用预编译 `app/sdk/lib-release.aar`），并从 [phong12a5/OpenVirtualApp](https://github.com/phong12a5/OpenVirtualApp) 引入 `commonSdk/` 源码模块替代 AAR，使全部代码可从源码构建。`commonSdk` 包含 `com.carlos.*`、`com.kook.*`、`com.lody.virtual.*`、`com.swift.sandhook.*`、`com.android.dx.*`、`com.bytedance.shadowhook.*` 等包，共约 1614 个 Java 源文件。注意：`commonSdk` 不包含 xdja 适配层（`com.xdja.*`），phong12a5 版本已裁剪该部分；本仓库 `lib/` 源码模块仍保留 xdja 代码但不参与构建。

## 模块结构

工程是一个多模块 Gradle Android 项目（`settings.gradle` 仅 `include ':app'` 和 `include ':commonSdk'`）：

- **`app/`**：宿主应用入口（`com.carlos.*`），包含 Splash、HomeActivity、ListAppActivity、AppSettingActivity、XposedManagerActivity 等 UI，以及 `PackageReceiver`、`AppRepository`。依赖 `:commonSdk`（不再依赖 `app/sdk/lib-release.aar`，该 AAR 保留在仓库里但未启用）。
- **`commonSdk/`**：虚拟化引擎 + 宿主公共代码的源码模块（namespace `com.kook.librelease`，CMake 构建 native）。包含原 `lib/` + `hooklib/` + `xposedcompat/` 三个模块的全部功能（`com.lody.virtual.*`、`com.swift.sandhook.*`、`com.android.dx.*`、`com.bytedance.shadowhook.*`），以及原 AAR 里预编译的 `com.carlos.common.*`、`com.carlos.libcommon.*`、`com.kook.*` 等包。包含 jniLibs 预编译 .so（`libcommon.so`、`libcontroller.so`、`libhyxd_64.so`、`libshadowhook.so`）和 cpp 源码（sandhook native）。引用 [phong12a5/OpenVirtualApp](https://github.com/phong12a5/OpenVirtualApp)。
- **`lib/`、`hooklib/`、`xposedcompat/`、`annotation/`**：上游遗留的源码模块，**不参与构建**（`settings.gradle` 已注释掉）。保留作参考。`lib/` 含 xdja 适配层源码（`com.xdja.*`），但 xdja 适配层已从构建中裁剪。

## 构建系统与命令

### 工具链版本

- **AGP**：`com.android.tools.build:gradle:9.4.1`（[build.gradle](build.gradle)）
- **Gradle**：9.6.0（[gradle/wrapper/gradle-wrapper.properties](gradle/wrapper/gradle-wrapper.properties)，AGP 9.4 最低要求）
- **JDK**：Java 25（Amazon Corretto 25.0.4），路径 `D:/greenProgram/Scoop/apps/corretto25-jdk/current`
- **NDK**：`27.0.12077973`（[AppConfig.gradle](AppConfig.gradle) 显式指定，避免 AGP 9.4 默认下载 28.2.13676358）
- **SDK Build-Tools**：36.0.0（AGP 9.4 要求，通过 `sdkmanager --proxy=http --proxy_host=127.0.0.1 --proxy_port=7890 "build-tools;36.0.0"` 安装）
- **SDK Platform**：33 和 36（commonSdk 用 compileSdk 33，app 用 compileSdk 36；33 通过 `sdkmanager --proxy=http --proxy_host=127.0.0.1 --proxy_port=7890 "platforms;android-33"` 安装）
- compileSdk：app 36 / commonSdk 33；minSdk 21 / targetSdk 26 / Java 17 兼容
- Gradle Wrapper 已入库（`gradlew`、`gradlew.bat`、`gradle/wrapper/gradle-wrapper.jar`、`gradle/wrapper/gradle-wrapper.properties`）
- **commonSdk 模块用 compileSdk 33 + Java 1.8**（[commonSdk/build.gradle](commonSdk/build.gradle)），因为它的 `dalvik.system.VMRuntime` stub 跟 compileSdk 36 的 android.jar 冲突。[AppConfig.gradle](AppConfig.gradle) 的 `subprojects { afterEvaluate { ... } }` 用 `if (project.name == 'commonSdk') { return }` 跳过 commonSdk，让它用自己的配置。

### 关键环境配置（Windows 上必读）

这台机器（Windows 11）有两个坑，构建前必须配置环境变量，否则必定失败：

1. **JDK 17/25 的 AF_UNIX pipe 损坏**：JDK 的 `sun.nio.ch.PipeImpl$LoopbackConnector` 在这台机器上用 Unix domain socket 失败（`connect0` 返回 `EINVAL`），根因是 `java.io.tmpdir` 默认 `C:\Users\Y00798~1\AppData\Local\Temp` 路径过长 + 含 8.3 短文件名，超出 Windows AF_UNIX socket 路径长度限制。Gradle daemon 启动时调用 `Selector.open()` 必然失败，报 `Unable to establish loopback connection`。
   **解决**：强制 `TMP`、`TEMP`、`java.io.tmpdir` 指向短路径（如 `D:\temp`）。

2. **公司代理做 TLS 中间人**：Gradle 下载 distribution 时 PKIX 校验失败。这台机器用本地代理 `http://127.0.0.1:7890`（Clash/V2Ray 类），上游是公司代理 `proxyjp.huawei.com:8080`，后者重签了所有 HTTPS 证书。

**完整构建命令**（PowerShell 或 Git Bash，在工程根目录执行）：

```bash
# 环境变量（每次新 shell 都要设置，或写入用户环境变量持久化）
export JAVA_HOME="D:/greenProgram/Scoop/apps/corretto25-jdk/current"
export PATH="$JAVA_HOME/bin:$PATH"
export TMP="D:\temp"
export TEMP="D:\temp"
export GRADLE_OPTS="-Djava.io.tmpdir=D:/temp -Dhttp.proxyHost=127.0.0.1 -Dhttp.proxyPort=7890 -Dhttps.proxyHost=127.0.0.1 -Dhttps.proxyPort=7890"

# 首次构建（约 14 分钟）—— 用本地解压的 Gradle 9.6.0 跑，绕过 wrapper 下载
"C:/Users/y00798287/.gradle/wrapper/dists/gradle-9.6.0-bin/manual/gradle-9.6.0/bin/gradle" :app:assembleDebug --no-daemon --console=plain

# 后续构建（Wrapper 已缓存 Gradle 9.6.0 后）
./gradlew :app:assembleDebug --no-daemon --console=plain

# Release 构建
./gradlew :app:assembleRelease --no-daemon --console=plain

# 清理
./gradlew clean --no-daemon --console=plain
```

### Gradle distribution 首次下载

如果 `~/.gradle/wrapper/dists/gradle-9.6.0-bin/` 下没有缓存，wrapper 启动时会下载 distribution，但因 PKIX 问题失败。用以下方式手动下载并解压：

```bash
# 1. 用 curl 走代理下载（约 134MB）
curl -fSL --ssl-no-revoke -x http://127.0.0.1:7890 \
  -o "$TEMP/gradle-9.6.0-bin.zip" \
  https://services.gradle.org/distributions/gradle-9.6.0-bin.zip

# 2. 解压到 wrapper 缓存目录
mkdir -p "C:/Users/y00798287/.gradle/wrapper/dists/gradle-9.6.0-bin/manual"
unzip -q "$TEMP/gradle-9.6.0-bin.zip" \
  -d "C:/Users/y00798287/.gradle/wrapper/dists/gradle-9.6.0-bin/manual/"

# 3. 验证
"C:/Users/y00798287/.gradle/wrapper/dists/gradle-9.6.0-bin/manual/gradle-9.6.0/bin/gradle" --version
```

### SDK 组件下载

AGP 9.4 要求 SDK Build-Tools 36.0.0，默认还会下载 NDK 28.2.13676358。两个都可能因代理问题卡住。`AppConfig.gradle` 已显式指定 `ndkVersion "27.0.12077973"` 跳过 NDK 下载。Build-Tools 36 需手动安装：

```bash
SDKMAN="D:/greenProgram/android/sdk/cmdline-tools/latest/bin/sdkmanager.bat"
echo y | "$SDKMAN" --sdk_root="D:/greenProgram/android/sdk" \
  --proxy=http --proxy_host=127.0.0.1 --proxy_port=7890 --no_https \
  "build-tools;36.0.0"
```

### 签名密钥

- `security/scorpion.jks`：release/debug 共用，口令在 [VAConfig.gradle](VAConfig.gradle) 明文写死（`scorpion123/scorpion`）。
- 密钥已于 2026-10-21 轮换（新密钥 2048 位 RSA + SHA256withRSA，PKCS12 格式，有效期 100 年）。旧 APK 因签名指纹变化需先卸载才能安装新版本。
- `security/*.jks`、`security/*.keystore`、`security/*.jks.old.bak` 均已在 `.gitignore` 中。历史上旧密钥曾入库，如需公开仓库应进一步用 `git filter-repo` 清理历史。
- `security/scorpion.jks.old.bak` 是轮换前的旧密钥备份，仅供回滚使用，不应删除或提交。

### 构建配置文件

- 顶层 [build.gradle](build.gradle) 通过 `apply from:` 引入 [VAConfig.gradle](VAConfig.gradle)（全局版本号、包名、签名信息）和 [AppConfig.gradle](AppConfig.gradle)（所有子模块的通用 Android 配置）。
- [gradle.properties](gradle.properties) 启用 `android.useAndroidX`、`android.enableJetifier`、`android.injected.testOnly=false`，`org.gradle.jvmargs=-Xmx4g -XX:+UseParallelGC -XX:+HeapDumpOnOutOfMemoryError -Dfile.encoding=UTF-8`，`org.gradle.daemon` 与 `org.gradle.parallel`。
- [local.properties](local.properties) 需配置 `sdk.dir`（Windows 上 Android Studio 会自动生成，被 `.gitignore` 排除）。
- [annotation/build.gradle](annotation/build.gradle) 是纯 Java 库（`java-library` 插件），用 `java {}` 块指定 Java 17 兼容（Gradle 9.x 移除了顶层 `sourceCompatibility`/`targetCompatibility` 属性）。
- ABI：`VAConfig.gradle` 中 `VA_MAIN_PACKAGE_32BIT` 为 `false`，默认只构建 `arm64-v8a`。`lib/` 模块的 `lib/build.gradle` 在 `transformNativeLibsWithStripDebugSymbolFor` 任务后会把 `*_64.so` 复制成 32 位 `armeabi-v7a` 版本（实现 32/64 位兼容），这一步对 SO 命名约定有硬性要求（`libv++.so` / `libv++_64.so`），改 native 模块名时需要同步修改。
- release 构建启用 `minifyEnabled`、`shrinkResources`，proguard 文件位于 `security/proguard-rules.pro`（被 `.gitignore` 排除）和 `app/proguard-rules.pro`。
- 引入了 StringFog（`com.github.megatronking.stringfog:gradle-plugin` + `xor` 实现）做字符串混淆；`security/` 下的字典文件 `dic.txt` 和 `proguard-rules.pro` 也被 gitignore。

### 验证 APK

```bash
# 验证签名（应显示 CN=VirtualApp, OU=Dev, O=VirtualApp, L=Local, ST=Local, C=CN）
"D:/greenProgram/android/sdk/build-tools/36.0.0/apksigner.bat" verify --print-certs \
  app/build/outputs/apk/debug/app-debug.apk

# 安装到设备（签名变更后必须先 uninstall）
adb -s <device> uninstall com.ywmail.virtualapp
adb -s <device> install app/build/outputs/apk/debug/app-debug.apk

# 启动（HomeActivity 未 exported，需用 monkey 启动 launcher intent）
adb -s <device> shell monkey -p com.ywmail.virtualapp -c android.intent.category.LAUNCHER 1
```

### 已知运行时问题（非构建问题）

应用启动时崩溃在 `VirtualCore.initialize()`（[lib/src/main/java/com/lody/virtual/client/core/VirtualCore.java:453](lib/src/main/java/com/lody/virtual/client/core/VirtualCore.java:453)），报 `NullPointerException: VirtualCore$ProcessType.ordinal() on null`。这是项目代码在 Android API 36 模拟器上的兼容性 bug，跟构建链无关，需要单独排查。

## 核心架构

VirtualApp 的整体架构是经典的「宿主进程 + 服务进程（`:x`）+ 虚拟应用进程（`:p0`–`:p99`）」三段式。理解以下几条主线就掌握了大部分代码：

### 1. 进程拓扑

在 `lib/src/main/AndroidManifest.xml` 中可以看到：
- **engine 进程**（`@string/engine_process_name` = `:x`）：承载 `KeepAliveService`、`HiddenForeNotification`、所有 `Shadow*` stub 组件（`ShadowActivity`、`ShadowService`、`ShadowContentProvider`、`ShadowJobService` 等）、`BinderProvider`。这是「服务端」。
- **`:p0`–`:p99`**：100 个 stub Activity/Dialog 的独立进程槽位，用于承载虚拟应用的实际 Activity（`ShadowActivity$P0..$P99`、`ShadowDialogActivity$P0..$P49` 等）。`StubManifest.STUB_COUNT = 100`。
- **宿主进程**：运行 `com.carlos.*` 的 UI。
- 虚拟应用通过把目标 Activity 重定向到某个空闲的 `ShadowActivity$PN` 来"借壳"启动。

### 2. BinderProvider —— 服务端的入口

`com.lody.virtual.server.BinderProvider`（ContentProvider，authority = `${AUTHORITY_PREFIX}.virtual.service.BinderProvider`）是服务端暴露 Binder 的唯一入口。`onCreate` 时初始化所有 VService：`VAppManagerService`、`VActivityManagerService`、`VPackageManagerService`、`VAccountManagerService`、`VContentService`、`VDeviceManagerService`、`VJobSchedulerService`、`VirtualLocationService`、`VNotificationManagerService`、`VUserManagerService`、`VirtualStorageService` 等，以及 xdja 的 `controllerService`、`VAppPermissionManagerService`、`VWaterMarkService` 等。客户端通过 `ServiceManagerNative` 调用 `BinderProvider` 拿到这些 IBinder。

### 3. 客户端 Hook 框架 —— `InvocationStubManager`

`com.lody.virtual.client.core.InvocationStubManager` 是整个 hook 系统的注册中枢。它在 `VirtualCore` 启动时被调用，实例化约 60 个 `MethodInvocationProxy` 子类（每个对应一个系统 Service：`ActivityManagerStub`、`PackageManagerStub`、`WindowManagerStub`、`NotificationManagerStub`、`TelephonyStub`...，全部位于 `lib/.../client/hook/proxies/<service>/`）。

关键设计（见 `lib/.../client/hook/base/`）：
- `MethodInvocationStub<T>`：用 `java.lang.reflect.Proxy` 包装系统 Service 的 IBinder 接口，构造一个 `HookInvocationHandler`。
- `MethodInvocationProxy<T>`：持有 `MethodInvocationStub`，在 `onBindMethods()` 中扫描自身 `@Inject(MethodProxies.class)` 注解指定的内部类，把所有非 abstract、被 `MethodProxy` 继承的内部类自动注册为 hook。可以用 `@SkipInject` 跳过，用 `@LogInvocation` 开启调用日志。
- `MethodProxy`：每个 hook 方法的封装，`call(who, method, args)` 决定如何改写调用。有一族预置实现：`ReplaceCallingPkgMethodProxy`、`ReplaceLastPkgMethodProxy`、`ReplaceUidMethodProxy`、`StaticMethodProxy`、`ResultStaticMethodProxy` 等。
- `BinderInvocationStub` / `BinderInvocationProxy`：用于直接替换 `ServiceManager.sCache` 中的 IBinder，而不是走 Proxy。

新增一个系统 Service 的 hook 时，惯用做法是在 `hook/proxies/<service>/` 下建 `<Service>Stub`（继承 `MethodInvocationProxy`，加 `@Inject(MethodProxies.class)` 注解）和 `MethodProxies`（含若干内部类继承 `MethodProxy`），然后在 `InvocationStubManager` 的 `applyHooks()`（或类似方法）里 `addInjector(new XxxStub())`。

`mirror/` 包下是用反射访问 hidden API 的镜像类（如 `mirror.android.app.ActivityManagerNative`、`mirror.android.os.ServiceManager`、`mirror.android.app.IActivityManager`），按 Android 版本分支。

### 4. VirtualCore —— 客户端入口

`com.lody.virtual.client.core.VirtualCore` 是单例（`gCore`），暴露 `startup`、`installPackage`、`launchApp`、`uninstallPackage`、`getInstalledApps` 等 API（见 `com.lody.virtual.open.MultiAppHelper` 作为外部使用门面）。它在应用进程启动时：
1. `VClient` 主导应用初始化（`com.lody.virtual.client.VClient`），绑定 `BinderProvider` 拿到 `IServiceFetcher`。
2. 创建 `InvocationStubManager` 并 inject 所有系统 Service hook。
3. 通过 `AppInstrumentation`（继承 `Instrumentation`）接管 Activity 生命周期，把目标 Activity 替换成 `ShadowActivity$PN`，再在 stub 内恢复真正的 Activity。
4. `StubManifest` 提供所有 stub 的类名、authority、`isStubActivity()` / `isHostPackageName()` 等判定工具。

### 5. VAppManagerService / VPackageManagerService —— 虚拟应用管理

`com.lody.virtual.server.pm.VAppManagerService`（实现 `IAppManager.aidl`）负责虚拟应用的安装/卸载/启动。它调用 `PackageParserEx`（`server/pm/parser/`）解析 APK，写入 `VEnvironment` 指定的私有目录，维护 `PackageCacheManager`。`VPackageManagerService` 实现 `IPackageManager.aidl`，对外提供查询。`VUserManagerService` 支持多用户（虚拟用户）。`PackageInstallerSession` 提供会话式安装。

### 6. VActivityManagerService —— 虚拟 Activity/Service 调度

`com.lody.virtual.server.am.VActivityManagerService`（实现 `IActivityManager.aidl`）维护 `ActivityStack`、`ActivityRecord`、`TaskRecord`、`ProcessRecord`、`ServiceRecord`、`ActiveServices`。`BroadcastSystem` 负责虚拟广播分发。`UidSystem` 维护虚拟 uid。客户端通过 `com.lody.virtual.client.ipc.VActivityManager`（同名门面）访问。

### 7. Native 层

`lib/src/main/jni/` 用 NDK Build 构建 `libv++.so` / `libv++_64.so`：
- `Foundation/`：IO 重定向（`IORelocator`、`IORelocator64`、`MapsRedirector`、`SandboxFs`）、syscall 拦截（`BinarySyscallFinder`）、`fake_dlfcn`（自定义 dlopen/dlsym）、`VMHook`。
- `Substrate/`：inline hook（`SubstrateHook`、`And64InlineHook`、`hde64` 反汇编）。
- `transparentED/`：透明加密（CTR 模式 + SM4 + SpookyV2），加密虚拟应用文件。
- `safekey/`：xdja 安全密钥相关 JNI。
- `Jni/VAJni.cpp` + `Jni/Helper.cpp`：Java 层入口（`com.lody.virtual.client.NativeEngine` 通过 `System.loadLibrary("v++")` 加载）。

`hooklib/src/main/cpp/` 用 CMake 构建 `libsandhook.so`：ART 方法替换（`art_method.cpp`）、JIT 支持（`art_jit.h`）、trampoline（`trampoline/`）、inst（`inst/`）。

### 8. xdja 适配层

`lib/.../com/xdja/` 下的代码（`zs`、`activitycounter`、`call`、`mms`、`tun`、`tunvpn`、`safeclient`、`ckms`、`lxf`、`monitor`、`utils`）是 xdja 安全沙箱适配层，被 `VClient`/`VirtualCore`/`BinderProvider` 深度引用。`com.xdja.zs.VAppPermissionManager`、`controllerManager`、`VWaterMarkService`、`VSafekeyManagerService`、`VServiceKeepAliveService` 在服务端注册并在客户端被 hook 调用。这一层与原版 VirtualApp 差异最大，但属于可选的 OEM 适配，通用功能不强依赖它。

### 9. Xposed 兼容

`app/.../sandxposed/SandHookConfigProvider` 与 `xposedcompat/` 模块提供 Xposed 模块加载能力。`app/.../home/XposedManager/` 提供 UI 列出已安装的 Xposed 模块。`xposedcompat/genhookstubs.py` 是构建期代码生成脚本，不要手改它生成的 `MethodHookerStubs*.java`。

## 工作约定

- 修改 hook 时优先看 `lib/.../client/hook/proxies/<service>/MethodProxies.java`，里面是该 service 所有方法级 hook 的内部类集合。新增 hook 必须是 `MethodProxy` 的非 abstract 子类，否则不会被 `@Inject` 扫描到。
- 修改 stub 数量或名称时，必须同步改 `lib/src/main/AndroidManifest.xml`（声明 `ShadowActivity$PN` 等）以及 `StubManifest.STUB_COUNT`，否则会启动失败。
- 添加新的 VService 时，要在 `BinderProvider.init()` 注册，并在 `aidl/com/lody/virtual/server/interfaces/` 下定义对应 AIDL，在 `client/ipc/` 下加同名门面类。
- Java 兼容版本固定 17（`AppConfig.gradle` 的 `compileOptions` 与 `annotation/build.gradle` 的 `java {}` 块），可使用 Java 17 及以下 API，不要引入 Java 18+ API。注意：**构建用 JDK 25 跑 Gradle daemon**（JDK 17 在这台 Windows 11 上的 AF_UNIX pipe 有 bug，见上文"关键环境配置"），但 `sourceCompatibility`/`targetCompatibility` 仍是 17。
- `proguard-rules.pro` 在 `security/` 下，被 gitignore，不要把核心 keep 规则写到这里；写在 `app/proguard-rules.pro` 或 `app/virtualapp-proguard-rules.pro`。
- clone 后的首次构建步骤：
  1. 配置 `local.properties`（`sdk.dir=D:\greenProgram\android\sdk`）
  2. 设置环境变量（`JAVA_HOME` 指向 Corretto 25、`TMP`/`TEMP` 指向 `D:\temp`、`GRADLE_OPTS` 含代理和 tmpdir），见上文"关键环境配置"
  3. 手动下载 Gradle 9.6.0 distribution 解压到 `~/.gradle/wrapper/dists/gradle-9.6.0-bin/manual/`（见上文"Gradle distribution 首次下载"），或用 Android Studio 内置 Gradle（在 Android Studio 里配置 Gradle JDK 为 Corretto 25，VM options 加 `-Djava.io.tmpdir=D:/temp`）
  4. 用 `sdkmanager --proxy=http --proxy_host=127.0.0.1 --proxy_port=7890 "build-tools;36.0.0"` 安装 Build-Tools 36（见上文"SDK 组件下载"）
  5. 用 `sdkmanager --proxy=http --proxy_host=127.0.0.1 --proxy_port=7890 "platforms;android-33"` 安装 SDK Platform 33（commonSdk 用 compileSdk 33）
  6. `./gradlew :app:assembleDebug --no-daemon --console=plain` 构建

### 已知运行时问题（Android 14+ / API 36）

应用在 Android 14+（API 34+，包括 API 36 模拟器）上启动时，`VirtualCore.startup()` 内部抛 `NoSuchFieldException`，被 `App.attachBaseContext()` 的 try-catch 吞掉，应用不崩溃但核心 hook 机制未完整初始化：

- **根因**：`commonSdk/src/main/java/mirror/android/app/ActivityThread.java` 的静态初始化块用 `getDeclaredField("USE_CACHE")`、`getDeclaredField("currentActivityThread")` 等访问 `android.app.ActivityThread` 的内部字段，但 Android 14+ 移除了这些字段，导致整个静态块失败，后续 mirror 字段（`getPackageManager`、`sPackageManager` 等）未初始化。
- **影响**：`HostPackageManager.init()` 失败 → `VirtualCore.startup()` 未完整执行 → VirtualApp 的包管理 hook、Activity 重定向等核心功能可能不工作（虚拟应用安装/启动可能失效）。
- **已修复**：`USE_CACHE` 字段初始化已加 try-catch（[commonSdk/src/main/java/mirror/android/app/ActivityThread.java:67-72](commonSdk/src/main/java/mirror/android/app/ActivityThread.java#L67)），但 `currentActivityThread`、可能还有其它字段仍会失败。
- **未修复**：需要给 `ActivityThread.java` 静态块的所有字段初始化逐一加 try-catch，并针对 Android 14+ 提供替代实现（如 `currentActivityThread` 用 `ActivityThread.currentActivityThread()` 方法而非字段）。这是 mirror 框架对 `ActivityThread` 内部字段重度依赖的系统性问题，修复工作量大。
- **验证方法**：`adb logcat | grep "HostPackageManager\|VirtualCore.startup"` 看是否有 `System.err` 异常栈；正常情况下应无异常。
- **上游状态**：phong12a5/OpenVirtualApp 原始代码也有此问题（只有一个 commit，无 Android 14+ 适配）。
