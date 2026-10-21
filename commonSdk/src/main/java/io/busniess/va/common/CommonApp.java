/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Application
 *  android.content.BroadcastReceiver
 *  android.content.ComponentName
 *  android.content.Context
 *  android.content.Intent
 *  android.content.IntentFilter
 *  android.view.LayoutInflater
 *  android.view.View
 *  android.widget.Toast
 *  androidx.annotation.RequiresApi
 *  androidx.appcompat.app.AppCompatDelegate
 *  com.kook.librelease.R$layout
 *  jonathanfinerty.once.Once
 */
package io.busniess.va.common;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatDelegate;
import com.kook.librelease.R;
import com.lody.virtual.client.core.SettingConfig;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.helper.Keep;
import com.lody.virtual.helper.compat.BuildCompat;
import com.lody.virtual.helper.utils.VLog;
import io.busniess.va.delegate.MyAppRequestListener;
import io.busniess.va.delegate.MyTaskDescDelegate;
import jonathanfinerty.once.Once;

@Keep
public class CommonApp {
    private SettingConfig mConfig = new SettingConfig(){

        @Override
        public String getMainPackageName() {
            return "com.carlos.multiapp";
        }

        @Override
        public String getExtPackageName() {
            return "com.carlos.multiapp.ext";
        }

        @Override
        public boolean isEnableIORedirect() {
            return true;
        }

        @Override
        public Intent onHandleLauncherIntent(Intent originIntent) {
            Intent intent = new Intent();
            ComponentName component = new ComponentName(this.getMainPackageName(), "io.busniess.va.home.BackHomeActivity");
            intent.setComponent(component);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            return intent;
        }

        @Override
        public boolean isUseRealDataDir(String packageName) {
            return false;
        }

        @Override
        public boolean isOutsidePackage(String packageName) {
            return false;
        }

        @Override
        public boolean isAllowCreateShortcut() {
            return false;
        }

        @Override
        public boolean isHostIntent(Intent intent) {
            return intent.getData() != null && "market".equals(intent.getData().getScheme());
        }

        @Override
        public boolean isUseRealApkPath(String packageName) {
            return packageName.equals("com.seeyon.cmp");
        }

        @Override
        public boolean isEnableVirtualSdcardAndroidData() {
            return BuildCompat.isQ();
        }

        @Override
        public boolean resumeInstrumentationInMakeApplication(String packageName) {
            if (packageName.equals("com.ss.android.ugc.aweme.lite")) {
                return true;
            }
            return super.resumeInstrumentationInMakeApplication(packageName);
        }

        @Override
        public boolean isUnProtectAction(String action) {
            if (action.startsWith("VA_BroadcastTest_")) {
                return true;
            }
            return super.isUnProtectAction(action);
        }
    };
    private BroadcastReceiver mGmsInitializeReceiver = new BroadcastReceiver(){

        public void onReceive(Context context, Intent intent) {
            View view = LayoutInflater.from((Context)context).inflate(R.layout.content_gms_init, null);
            Toast toast = new Toast(context);
            toast.setGravity(81, 0, 0);
            toast.setDuration(0);
            toast.setView(view);
            try {
                toast.show();
            }
            catch (Throwable e) {
                e.printStackTrace();
            }
        }
    };

    public void attachBaseContext(Application application, Context base) {
        VLog.OPEN_LOG = true;
        try {
            VirtualCore.get().startup(application, base, this.mConfig);
        }
        catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void onCreate(final Application application) {
        final VirtualCore virtualCore = VirtualCore.get();
        virtualCore.initialize(new VirtualCore.VirtualInitializer(){

            @Override
            public void onMainProcess() {
                AppCompatDelegate.setCompatVectorFromResourcesEnabled((boolean)true);
                Once.initialise((Context)application);
                application.registerReceiver(CommonApp.this.mGmsInitializeReceiver, new IntentFilter("android.intent.action.GMS_INITIALIZED"));
            }

            @Override
            @RequiresApi(api=17)
            public void onVirtualProcess() {
                virtualCore.setTaskDescriptionDelegate(new MyTaskDescDelegate());
                virtualCore.setAppRequestListener(new MyAppRequestListener((Context)application));
            }

            @Override
            public void onServerProcess() {
            }
        });
    }
}

