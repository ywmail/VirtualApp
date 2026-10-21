/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.net.Uri
 *  android.widget.Toast
 */
package io.busniess.va.delegate;

import android.content.Context;
import android.net.Uri;
import android.widget.Toast;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.ipc.VActivityManager;
import com.lody.virtual.helper.utils.VLog;
import com.lody.virtual.remote.VAppInstallerParams;
import com.lody.virtual.remote.VAppInstallerResult;
import java.io.File;

public class MyAppRequestListener
implements VirtualCore.AppRequestListener {
    private final Context mContext;

    public MyAppRequestListener(Context context) {
        this.mContext = context;
    }

    @Override
    public void onRequestInstall(String path) {
        MyAppRequestListener.info("Start installing: " + path);
        VAppInstallerParams params = new VAppInstallerParams();
        VAppInstallerResult res = VirtualCore.get().installPackage(Uri.fromFile((File)new File(path)), params);
        if (res.status == 0) {
            MyAppRequestListener.info("Install " + res.packageName + " success.");
            boolean success = VActivityManager.get().launchApp(0, res.packageName);
            MyAppRequestListener.info("launch app " + (success ? "success." : "fail."));
        } else {
            MyAppRequestListener.info("Install " + res.packageName + " fail, error code: " + res.status);
        }
    }

    private static void info(String msg) {
        VLog.e("AppInstaller", msg);
    }

    @Override
    public void onRequestUninstall(String pkg) {
        Toast.makeText((Context)this.mContext, (CharSequence)("Intercept uninstall request: " + pkg), (int)0).show();
    }
}

