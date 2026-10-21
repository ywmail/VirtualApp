/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.storage.StorageVolume
 */
package com.lody.virtual.client.hook.proxies.mount;

import android.os.Build;
import android.os.storage.StorageVolume;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.hook.base.MethodProxy;
import com.lody.virtual.client.hook.utils.MethodParameterUtils;
import com.lody.virtual.helper.compat.BuildCompat;
import com.lody.virtual.helper.utils.VLog;
import java.io.File;
import java.lang.reflect.Method;

class MethodProxies {
    public static File file = null;

    MethodProxies() {
    }

    static class Mkdirs
    extends MethodProxy {
        Mkdirs() {
        }

        @Override
        public String getMethodName() {
            return "mkdirs";
        }

        @Override
        public boolean beforeCall(Object who, Method method, Object ... args) {
            MethodParameterUtils.replaceFirstAppPkg(args);
            return super.beforeCall(who, method, args);
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            if (Build.VERSION.SDK_INT < 19) {
                return super.call(who, method, args);
            }
            String path = args.length == 1 ? (String)args[0] : (String)args[1];
            File file = new File(path);
            if (!file.exists() && !file.mkdirs()) {
                return -1;
            }
            return 0;
        }
    }

    static class GetVolumeList
    extends MethodProxy {
        GetVolumeList() {
        }

        @Override
        public String getMethodName() {
            return "getVolumeList";
        }

        @Override
        public boolean beforeCall(Object who, Method method, Object ... args) {
            if (args == null || args.length == 0) {
                return super.beforeCall(who, method, args);
            }
            if (args[0] instanceof Integer) {
                args[0] = BuildCompat.isT() ? Integer.valueOf(GetVolumeList.getRealUserId()) : Integer.valueOf(GetVolumeList.getRealUid());
            }
            MethodParameterUtils.replaceFirstAppPkg(args);
            return super.beforeCall(who, method, args);
        }

        private boolean checkPackageSdcard(String appPkg) {
            return appPkg.equals("com.nexon.hit2") || appPkg.equals("com.nexon.er");
        }

        @Override
        public Object afterCall(Object who, Method method, Object[] args, Object result) throws Throwable {
            if (this.checkPackageSdcard(GetVolumeList.getAppPkg()) && "samsung".equals(Build.BRAND) && Build.VERSION.SDK_INT == 29) {
                VLog.e("HV-", "由于mount services MethodProxies com.nexon.hit2 游戏在android 10 上重定向出现问题,这里将重定向的问题修复掉  " + GetVolumeList.getAppPkg());
                StorageVolume[] storageVolumes = (StorageVolume[])result;
                if (file == null) {
                    // empty if block
                }
                if (file == null) {
                    new NullPointerException("file is not nullpointer");
                }
                if (!file.exists()) {
                    file.mkdirs();
                }
                for (StorageVolume storageVolume : storageVolumes) {
                    File directory;
                    if (mirror.android.os.storage.StorageVolume.mPath != null) {
                        directory = mirror.android.os.storage.StorageVolume.mPath.get(storageVolume);
                        mirror.android.os.storage.StorageVolume.mPath.set(storageVolume, file);
                    }
                    if (mirror.android.os.storage.StorageVolume.mInternalPath == null) continue;
                    directory = mirror.android.os.storage.StorageVolume.mInternalPath.get(storageVolume);
                    mirror.android.os.storage.StorageVolume.mInternalPath.set(storageVolume, file);
                }
            }
            return result;
        }
    }
}

