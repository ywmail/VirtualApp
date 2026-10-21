/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.pm.ApplicationInfo
 *  android.os.Build$VERSION
 */
package com.lody.virtual.helper.compat;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.stub.RequestPermissionsActivity;
import com.lody.virtual.server.IRequestPermissionsResult;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PermissionCompat {
    public static Set<String> DANGEROUS_PERMISSION = new HashSet<String>(){
        {
            this.add("android.permission.READ_CALENDAR");
            this.add("android.permission.WRITE_CALENDAR");
            this.add("android.permission.CAMERA");
            this.add("android.permission.READ_CONTACTS");
            this.add("android.permission.WRITE_CONTACTS");
            this.add("android.permission.GET_ACCOUNTS");
            this.add("android.permission.ACCESS_FINE_LOCATION");
            this.add("android.permission.ACCESS_COARSE_LOCATION");
            this.add("android.permission.READ_PHONE_STATE");
            this.add("android.permission.CALL_PHONE");
            if (Build.VERSION.SDK_INT >= 16) {
                this.add("android.permission.READ_CALL_LOG");
                this.add("android.permission.WRITE_CALL_LOG");
            }
            this.add("com.android.voicemail.permission.ADD_VOICEMAIL");
            this.add("android.permission.USE_SIP");
            this.add("android.permission.PROCESS_OUTGOING_CALLS");
            this.add("android.permission.SEND_SMS");
            this.add("android.permission.RECEIVE_SMS");
            this.add("android.permission.READ_SMS");
            this.add("android.permission.RECEIVE_WAP_PUSH");
            this.add("android.permission.RECEIVE_MMS");
            this.add("android.permission.RECORD_AUDIO");
            this.add("android.permission.WRITE_EXTERNAL_STORAGE");
            if (Build.VERSION.SDK_INT >= 16) {
                this.add("android.permission.READ_EXTERNAL_STORAGE");
            }
            if (Build.VERSION.SDK_INT >= 20) {
                this.add("android.permission.BODY_SENSORS");
            }
        }
    };

    public static String[] findDangerousPermissions(List<String> permissions) {
        if (permissions == null) {
            return null;
        }
        ArrayList<String> list = new ArrayList<String>();
        for (String per : permissions) {
            if (!DANGEROUS_PERMISSION.contains(per)) continue;
            list.add(per);
        }
        return list.toArray(new String[0]);
    }

    public static String[] findDangrousPermissions(String[] permissions) {
        if (permissions == null) {
            return null;
        }
        ArrayList<String> list = new ArrayList<String>();
        for (String permission2 : permissions) {
            if (!DANGEROUS_PERMISSION.contains(permission2)) continue;
            list.add(permission2);
        }
        return list.toArray(new String[0]);
    }

    public static boolean isCheckPermissionRequired(ApplicationInfo info) {
        if (Build.VERSION.SDK_INT < 23 || VirtualCore.get().getTargetSdkVersion() < 23) {
            return false;
        }
        return info.targetSdkVersion < 23;
    }

    public static boolean checkPermissions(String[] permissions, boolean isExt) {
        if (permissions == null) {
            return true;
        }
        for (String per : permissions) {
            if (VirtualCore.get().checkSelfPermission(per, isExt)) continue;
            return false;
        }
        return true;
    }

    public static boolean isRequestGranted(int[] grantResults) {
        boolean allGranted = true;
        for (int grantResult : grantResults) {
            if (grantResult != -1) continue;
            allGranted = false;
            break;
        }
        return allGranted;
    }

    public static void startRequestPermissions(Context context, boolean isExt, String[] permissions, final CallBack callBack) {
        RequestPermissionsActivity.request(context, isExt, permissions, new IRequestPermissionsResult.Stub(){

            @Override
            public boolean onResult(int requestCode, String[] permissions, int[] grantResults) {
                if (callBack != null) {
                    return callBack.onResult(requestCode, permissions, grantResults);
                }
                return false;
            }
        });
    }

    public static interface CallBack {
        public boolean onResult(int var1, String[] var2, int[] var3);
    }
}

