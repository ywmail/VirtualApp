/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.ClipData
 *  android.content.ClipData$Item
 *  android.content.ComponentName
 *  android.content.Intent
 *  android.content.pm.ActivityInfo
 *  android.content.pm.ApplicationInfo
 *  android.content.pm.ComponentInfo
 *  android.content.pm.ProviderInfo
 *  android.net.Uri
 *  android.os.Build$VERSION
 *  android.os.Bundle
 *  android.os.IBinder
 *  android.os.Parcelable
 *  android.text.TextUtils
 */
package com.lody.virtual.helper.utils;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.ComponentInfo;
import android.content.pm.ProviderInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.text.TextUtils;
import com.lody.virtual.GmsSupport;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.NativeEngine;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.env.SpecialComponentList;
import com.lody.virtual.client.ipc.VPackageManager;
import com.lody.virtual.client.stub.ContentProviderProxy;
import com.lody.virtual.client.stub.ShadowPendingActivity;
import com.lody.virtual.client.stub.ShadowPendingReceiver;
import com.lody.virtual.client.stub.ShadowPendingService;
import com.lody.virtual.helper.compat.ObjectsCompat;
import com.lody.virtual.helper.utils.VLog;
import com.lody.virtual.os.VUserHandle;
import com.lody.virtual.remote.BroadcastIntentData;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

public class ComponentUtils {
    public static String getTaskAffinity(ActivityInfo info) {
        if (info.launchMode == 3) {
            return "-SingleInstance-" + info.packageName + "/" + info.name;
        }
        if (info.taskAffinity == null && info.applicationInfo.taskAffinity == null) {
            return info.packageName;
        }
        if (info.taskAffinity != null) {
            return info.taskAffinity;
        }
        return info.applicationInfo.taskAffinity;
    }

    public static boolean intentFilterEquals(Intent a, Intent b) {
        if (a != null && b != null) {
            String pkgB;
            if (!ObjectsCompat.equals(a.getAction(), b.getAction())) {
                return false;
            }
            if (!ObjectsCompat.equals(a.getData(), b.getData())) {
                return false;
            }
            if (!ObjectsCompat.equals(a.getType(), b.getType())) {
                return false;
            }
            String pkgA = a.getPackage();
            if (pkgA == null && a.getComponent() != null) {
                pkgA = a.getComponent().getPackageName();
            }
            if ((pkgB = b.getPackage()) == null && b.getComponent() != null) {
                pkgB = b.getComponent().getPackageName();
            }
            if (!ObjectsCompat.equals(pkgA, pkgB)) {
                return false;
            }
            if (!ObjectsCompat.equals(a.getComponent(), b.getComponent())) {
                return false;
            }
            if (!ObjectsCompat.equals(a.getCategories(), b.getCategories())) {
                return false;
            }
        }
        return true;
    }

    public static String getProcessName(ComponentInfo componentInfo) {
        String processName = componentInfo.processName;
        if (processName == null) {
            componentInfo.processName = processName = componentInfo.packageName;
        }
        return processName;
    }

    public static boolean isSameComponent(ComponentInfo first, ComponentInfo second) {
        if (first != null && second != null) {
            String pkg1 = first.packageName + "";
            String pkg2 = second.packageName + "";
            String name1 = first.name + "";
            String name2 = second.name + "";
            return pkg1.equals(pkg2) && name1.equals(name2);
        }
        return false;
    }

    public static ComponentName toComponentName(ComponentInfo componentInfo) {
        return new ComponentName(componentInfo.packageName, componentInfo.name);
    }

    public static boolean isSystemApp(ApplicationInfo applicationInfo) {
        if (applicationInfo == null) {
            return false;
        }
        if (GmsSupport.isGoogleAppOrService(applicationInfo.packageName)) {
            return false;
        }
        if (SpecialComponentList.isSpecSystemPackage(applicationInfo.packageName)) {
            return true;
        }
        if (applicationInfo.uid >= 10000) {
            return (applicationInfo.flags & 1) != 0 || (applicationInfo.flags & 0x80) != 0;
        }
        return true;
    }

    public static String getComponentAction(ActivityInfo info) {
        return ComponentUtils.getComponentAction(info.packageName, info.name);
    }

    public static String getComponentAction(ComponentName component) {
        return ComponentUtils.getComponentAction(component.getPackageName(), component.getClassName());
    }

    public static String getComponentAction(String packageName, String name) {
        return String.format("_VA_%s_%s_%s", VirtualCore.get().getHostPkg(), packageName, name);
    }


    public static Intent proxyBroadcastIntent(Intent intent, int userId) {
        if (intent.getAction() != null && VirtualCore.getConfig().isUnProtectAction(intent.getAction())) {
            return intent;
        } else {
            Intent newIntent = new Intent();
            newIntent.setDataAndType(intent.getData(), intent.getType());
            Set<String> categories = intent.getCategories();
            String targetPackage;
            if (categories != null) {
                Iterator var4 = categories.iterator();

                while(var4.hasNext()) {
                    targetPackage = (String)var4.next();
                    newIntent.addCategory(targetPackage);
                }
            }

            ComponentName component = intent.getComponent();
            targetPackage = intent.getPackage();
            if (component != null) {
                String componentAction = getComponentAction(component);
                newIntent.setAction(componentAction);
                if (targetPackage == null) {
                    targetPackage = component.getPackageName();
                }
            } else {
                newIntent.setAction(SpecialComponentList.protectAction(intent.getAction()));
            }

            BroadcastIntentData data = new BroadcastIntentData(userId, intent, targetPackage);
            data.saveIntent(newIntent);
            return newIntent;
        }
    }

    public static void parcelActivityIntentSender(Intent fillIn, IBinder resultTo, Bundle options) {
        Bundle extras = fillIn.getExtras();
        if (extras != null) {
            fillIn.getExtras().clear();
            fillIn.putExtra("_VA_|_fill_in_", extras);
        }
        if (options != null) {
            fillIn.putExtra("_VA_|_options_", options);
        }
        mirror.android.content.Intent.putExtra.call(fillIn, "_VA_|_caller_activity_", resultTo);
    }

    public static IntentSenderInfo parseIntentSenderInfo(Intent proxyIntent, boolean isActivity) {
        Intent selector = proxyIntent.getSelector();
        if (selector == null) {
            return null;
        } else {
            IntentSenderInfo info = new IntentSenderInfo();
            info.userId = selector.getIntExtra("_VA_|_user_id_", -1);
            info.targetPkg = selector.getStringExtra("_VA_|_target_pkg_");
            info.originalType = selector.getStringExtra("_VA_|_original_type_");
            info.type = selector.getStringExtra("_VA_|_type_");
            info.fillIn = proxyIntent.getExtras();
            Intent realIntent = (Intent)selector.getParcelableExtra("_VA_|_intent_");
            info.base = realIntent.getExtras();
            Intent intent = proxyIntent.cloneFilter();
            intent.setComponent(realIntent.getComponent());
            intent.setPackage(realIntent.getPackage());
            intent.setSelector(realIntent.getSelector());
            intent.setFlags(realIntent.getFlags());
            if (TextUtils.equals(info.type, proxyIntent.getType())) {
                intent.setDataAndType(realIntent.getData(), info.originalType);
            }

            if (Build.VERSION.SDK_INT > 15 && realIntent.getClipData() == null && proxyIntent.getClipData() != null) {
                intent.setClipData(proxyIntent.getClipData());
                if ((proxyIntent.getFlags() & 268435456) != 0) {
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }
            }

            if (isActivity) {
                info.callerActivity = (IBinder)mirror.android.content.Intent.getIBinderExtra.call(proxyIntent, new Object[]{"_VA_|_caller_activity_"});
                info.options = proxyIntent.getBundleExtra("_VA_|_options_");
                info.fillIn = proxyIntent.getBundleExtra("_VA_|_fill_in_");
            }

            intent.putExtra("_VA_|_fill_in_", info.fillIn);
            intent.putExtra("_VA_|_base_", info.base);
            info.intent = intent;
            return info;
        }
    }

    public static void unpackFillIn(Intent intent, ClassLoader classLoader) {
        intent.setExtrasClassLoader(classLoader);
        try {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                Bundle fillIn = extras.getBundle("_VA_|_fill_in_");
                Bundle base = extras.getBundle("_VA_|_base_");
                if (fillIn != null || base != null) {
                    if (fillIn != null) {
                        fillIn.setClassLoader(classLoader);
                    }
                    if (base != null) {
                        base.setClassLoader(classLoader);
                    }
                    if (fillIn != null && base != null) {
                        fillIn.putAll(base);
                    }
                    if (fillIn == null) {
                        fillIn = base;
                    }
                    intent.replaceExtras(fillIn);
                }
            }
        }
        catch (Throwable e) {
            e.printStackTrace();
        }
    }


    @SuppressLint("WrongConstant")
    public static Intent getProxyIntentSenderIntent(int userId, int type, String targetPkg, Intent intent) {
        if (type == 3) {
            VLog.printStackTrace("Unsupported IntentSender type: " + type);
            return null;
        } else {
            Intent proxyIntent = intent.cloneFilter();
            proxyIntent.setSourceBounds(intent.getSourceBounds());
            if (Build.VERSION.SDK_INT >= 16) {
                proxyIntent.setClipData(intent.getClipData());
            }

            proxyIntent.addFlags(intent.getFlags() & 3);
            if (Build.VERSION.SDK_INT >= 19) {
                proxyIntent.addFlags(intent.getFlags() & 64);
            }

            if (Build.VERSION.SDK_INT >= 21) {
                proxyIntent.addFlags(intent.getFlags() & 128);
            }

            String originalType = proxyIntent.getType();
            ComponentName component = proxyIntent.getComponent();
            String proxyIntentType = originalType == null ? targetPkg : originalType + ":" + targetPkg;
            if (component != null) {
                proxyIntentType = proxyIntentType + ":" + component.flattenToString();
            }

            proxyIntent.setDataAndType(proxyIntent.getData(), proxyIntentType);
            if (type == 2) {
                proxyIntent.setComponent(new ComponentName(VirtualCore.getConfig().getMainPackageName(), ShadowPendingActivity.class.getName()));
            } else if (type == 4) {
                proxyIntent.setComponent(new ComponentName(VirtualCore.getConfig().getMainPackageName(), ShadowPendingService.class.getName()));
            } else {
                proxyIntent.setComponent(new ComponentName(VirtualCore.getConfig().getMainPackageName(), ShadowPendingReceiver.class.getName()));
            }

            Intent selector = new Intent();
            selector.putExtra("_VA_|_user_id_", userId);
            selector.putExtra("_VA_|_intent_", intent);
            selector.putExtra("_VA_|_target_pkg_", targetPkg);
            selector.putExtra("_VA_|_original_type_", originalType);
            selector.putExtra("_VA_|_type_", proxyIntentType);
            proxyIntent.setPackage((String)null);
            proxyIntent.setSelector(selector);
            return proxyIntent;
        }
    }
    public static Intent processOutsideIntent(int userId, boolean isExt, Intent intent) {
        ArrayList<Uri> newList;
        ArrayList list;
        Parcelable output;
        Uri processedUri;
        ClipData.Item item;
        Uri uri;
        ClipData clipData;
        Uri data = intent.getData();
        if (data != null) {
            intent.setDataAndType(ComponentUtils.processOutsideUri(userId, isExt, data), intent.getType());
        }
        if (Build.VERSION.SDK_INT >= 16 && intent.getClipData() != null && (clipData = intent.getClipData()).getItemCount() >= 0 && (uri = (item = clipData.getItemAt(0)).getUri()) != null && (processedUri = ComponentUtils.processOutsideUri(userId, isExt, uri)) != uri) {
            ClipData processedClipData = new ClipData(clipData.getDescription(), new ClipData.Item(item.getText(), item.getHtmlText(), item.getIntent(), processedUri));
            for (int i = 1; i < clipData.getItemCount(); ++i) {
                ClipData.Item processedItem = clipData.getItemAt(i);
                uri = processedItem.getUri();
                if (uri != null) {
                    uri = ComponentUtils.processOutsideUri(userId, isExt, uri);
                }
                processedClipData.addItem(new ClipData.Item(processedItem.getText(), processedItem.getHtmlText(), processedItem.getIntent(), uri));
            }
            intent.setClipData(processedClipData);
        }
        if (intent.hasExtra("output")) {
            output = intent.getParcelableExtra("output");
            if (output instanceof Uri) {
                intent.putExtra("output", (Parcelable)ComponentUtils.processOutsideUri(userId, isExt, (Uri)output));
            } else if (output instanceof ArrayList) {
                list = (ArrayList)output;
                newList = new ArrayList<Uri>();
                for (Object o : list) {
                    if (!(o instanceof Uri)) break;
                    newList.add(ComponentUtils.processOutsideUri(userId, isExt, (Uri)o));
                }
                if (!newList.isEmpty()) {
                    intent.putExtra("output", newList);
                }
            }
        }
        if (intent.hasExtra("android.intent.extra.STREAM")) {
            output = intent.getParcelableExtra("android.intent.extra.STREAM");
            if (output instanceof Uri) {
                intent.putExtra("android.intent.extra.STREAM", (Parcelable)ComponentUtils.processOutsideUri(userId, isExt, (Uri)output));
            } else if (output instanceof ArrayList) {
                list = (ArrayList)output;
                newList = new ArrayList();
                for (Object o : list) {
                    if (!(o instanceof Uri)) break;
                    newList.add(ComponentUtils.processOutsideUri(userId, isExt, (Uri)o));
                }
                if (!newList.isEmpty()) {
                    intent.putExtra("android.intent.extra.STREAM", newList);
                }
            }
        }
        return intent;
    }

    public static Uri processOutsideUri(int userId, boolean isExt, Uri uri) {
        if (TextUtils.equals((CharSequence)uri.getScheme(), (CharSequence)"file")) {
            return Uri.fromFile((File)new File(NativeEngine.reverseRedirectedPath(uri.getPath())));
        }
        if (!TextUtils.equals((CharSequence)uri.getScheme(), (CharSequence)"content")) {
            return uri;
        }
        String authority = uri.getAuthority();
        if (authority == null) {
            return uri;
        }
        ProviderInfo info = VirtualCore.get().getHostPackageManager().resolveContentProvider(authority, 0L);
        if (info == null) {
            info = VPackageManager.get().resolveContentProvider(authority, 0, VUserHandle.myUserId());
        }
        if (info == null) {
            return uri;
        }
        uri = ContentProviderProxy.buildProxyUri(userId, isExt, authority, uri);
        return uri;
    }

    public static class IntentSenderInfo {
        public int userId;
        public Intent intent;
        public String targetPkg;
        public String originalType;
        public String type;
        public Bundle fillIn;
        public Bundle base;
        public Bundle options;
        public IBinder callerActivity;
    }
}

