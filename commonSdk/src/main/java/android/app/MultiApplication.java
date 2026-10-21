/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Application
 *  android.content.Context
 *  android.content.pm.PackageInfo
 *  android.content.pm.PackageManager$NameNotFoundException
 *  android.content.res.AssetManager
 *  android.content.res.Resources
 *  android.content.res.Resources$Theme
 *  dalvik.system.DexClassLoader
 */
package android.app;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import com.carlos.libcommon.StringFog;
import com.lody.virtual.client.core.AppCallback;
import dalvik.system.DexClassLoader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

public abstract class MultiApplication
extends Application {
    private DexClassLoader pluginDexClassLoader;
    private Resources pluginResources;
    private PackageInfo pluginPackageArchiveInfo;
    private AssetManager assetManager;
    private Resources newResource;
    private Resources.Theme mTheme;
    private static MultiApplication multiApplication;

    public DexClassLoader getPluginDexClassLoader() {
        return this.pluginDexClassLoader;
    }

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        multiApplication = this;
    }

    public void onCreate() {
        super.onCreate();
        String sha1 = MultiApplication.getSHA1((Context)this);
    }

    public static String getSHA1(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 64);
            byte[] cert = info.signatures[0].toByteArray();
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] publicKey = md.digest(cert);
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < publicKey.length; ++i) {
                String appendString = Integer.toHexString(0xFF & publicKey[i]).toUpperCase(Locale.US);
                if (appendString.length() == 1) {
                    hexString.append("0");
                }
                hexString.append(appendString);
                hexString.append(":");
            }
            String result = hexString.toString();
            return result.substring(0, result.length() - 1);
        }
        catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public abstract AppCallback getAppCallback();

    public void loadApk(Context context, String dexPath) {
        if (dexPath == null) {
            dexPath = "/storage/emulated/0/component-debug.apk";
        }
        this.pluginDexClassLoader = new DexClassLoader(dexPath, context.getDir("dex", 0).getAbsolutePath(), null, context.getClassLoader());
        this.pluginPackageArchiveInfo = context.getPackageManager().getPackageArchiveInfo(dexPath, 1);
        try {
            this.assetManager = (AssetManager)AssetManager.class.newInstance();
            Method addAssetPath = AssetManager.class.getMethod("addAssetPath", String.class);
            addAssetPath.invoke(this.assetManager, dexPath);
        }
        catch (InstantiationException e) {
            e.printStackTrace();
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        this.newResource = new Resources(this.assetManager, context.getResources().getDisplayMetrics(), context.getResources().getConfiguration());
        this.mTheme = this.newResource.newTheme();
        this.mTheme.setTo(context.getTheme());
    }

    public AssetManager getAssets() {
        return this.assetManager == null ? super.getAssets() : this.assetManager;
    }

    public Resources getResources() {
        if (this.newResource != null) {
            // empty if block
        }
        return this.newResource == null ? super.getResources() : this.newResource;
    }

    public Resources.Theme getTheme() {
        return this.mTheme == null ? super.getTheme() : this.mTheme;
    }

    public static MultiApplication getMultiApplication() {
        return multiApplication;
    }
}

