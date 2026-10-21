/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.util.Log
 *  dalvik.system.DexClassLoader
 *  dalvik.system.DexFile
 */
package de.robv.android.xposed;

import android.util.Log;
import dalvik.system.DexClassLoader;
import dalvik.system.DexFile;
import de.robv.android.xposed.IXposedHookInitPackageResources;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.IXposedMod;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public final class XposedInit {
    private static final String TAG = "SandXposed";
    private static final String INSTANT_RUN_CLASS = "com.android.tools.fd.runtime.BootstrapApplication";
    private static boolean disableResources = true;
    private static volatile AtomicBoolean bootstrapHooked = new AtomicBoolean(false);
    private static volatile AtomicBoolean modulesLoaded = new AtomicBoolean(false);

    private XposedInit() {
    }

    static void hookResources() throws Throwable {
    }

    private static boolean needsToCloseFilesForFork() {
        return false;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void loadModule(String modulePath, String moduleOdexDir, String moduleSoPath, ClassLoader topClassLoader) {
        InputStream is;
        DexFile dexFile;
        if (!new File(modulePath).exists()) {
            Log.e((String)TAG, (String)"  File does not exist");
            return;
        }
        try {
            dexFile = new DexFile(modulePath);
        }
        catch (IOException e) {
            Log.e((String)TAG, (String)"  Cannot load module", (Throwable)e);
            return;
        }
        if (dexFile.loadClass(INSTANT_RUN_CLASS, topClassLoader) != null) {
            Log.e((String)TAG, (String)"  Cannot load module, please disable \"Instant Run\" in Android Studio.");
            XposedHelpers.closeSilently(dexFile);
            return;
        }
        if (dexFile.loadClass(XposedBridge.class.getName(), topClassLoader) != null) {
            Log.e((String)TAG, (String)"  Cannot load module:");
            Log.e((String)TAG, (String)"  The Xposed API classes are compiled into the module's APK.");
            Log.e((String)TAG, (String)"  This may cause strange issues and must be fixed by the module developer.");
            Log.e((String)TAG, (String)"  For details, see: http://api.xposed.info/using.html");
            XposedHelpers.closeSilently(dexFile);
            return;
        }
        XposedHelpers.closeSilently(dexFile);
        ZipFile zipFile = null;
        try {
            zipFile = new ZipFile(modulePath);
            ZipEntry zipEntry = zipFile.getEntry("assets/xposed_init");
            if (zipEntry == null) {
                Log.e((String)TAG, (String)"  assets/xposed_init not found in the APK");
                XposedHelpers.closeSilently(zipFile);
                return;
            }
            is = zipFile.getInputStream(zipEntry);
        }
        catch (IOException e) {
            Log.e((String)TAG, (String)"  Cannot read assets/xposed_init in the APK", (Throwable)e);
            XposedHelpers.closeSilently(zipFile);
            return;
        }
        DexClassLoader mcl = new DexClassLoader(modulePath, moduleOdexDir, moduleSoPath, topClassLoader);
        BufferedReader moduleClassesReader = new BufferedReader(new InputStreamReader(is));
        try {
            String moduleClassName;
            while ((moduleClassName = moduleClassesReader.readLine()) != null) {
                if ((moduleClassName = moduleClassName.trim()).isEmpty() || moduleClassName.startsWith("#")) continue;
                try {
                    Log.i((String)TAG, (String)("  Loading class " + moduleClassName));
                    Class<?> moduleClass = mcl.loadClass(moduleClassName);
                    if (!IXposedMod.class.isAssignableFrom(moduleClass)) {
                        Log.e((String)TAG, (String)"    This class doesn't implement any sub-interface of IXposedMod, skipping it");
                        continue;
                    }
                    if (disableResources && IXposedHookInitPackageResources.class.isAssignableFrom(moduleClass)) {
                        Log.e((String)TAG, (String)"    This class requires resource-related hooks (which are disabled), skipping it.");
                        continue;
                    }
                    Object moduleInstance = moduleClass.newInstance();
                    if (moduleInstance instanceof IXposedHookZygoteInit) {
                        IXposedHookZygoteInit.StartupParam param = new IXposedHookZygoteInit.StartupParam();
                        param.modulePath = modulePath;
                        param.startsSystemServer = false;
                        ((IXposedHookZygoteInit)moduleInstance).initZygote(param);
                    }
                    if (moduleInstance instanceof IXposedHookLoadPackage) {
                        XposedBridge.hookLoadPackage(new IXposedHookLoadPackage.Wrapper((IXposedHookLoadPackage)moduleInstance));
                    }
                    if (!(moduleInstance instanceof IXposedHookInitPackageResources)) continue;
                    throw new UnsupportedOperationException("can not hook resource!");
                }
                catch (Throwable t) {
                    Log.e((String)TAG, (String)("    Failed to load class " + moduleClassName), (Throwable)t);
                }
            }
        }
        catch (IOException e) {
            Log.e((String)TAG, (String)("  Failed to load module from " + modulePath), (Throwable)e);
        }
        finally {
            XposedHelpers.closeSilently(is);
            XposedHelpers.closeSilently(zipFile);
        }
    }
}

