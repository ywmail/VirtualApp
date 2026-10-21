/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.BroadcastReceiver
 *  android.content.BroadcastReceiver$PendingResult
 *  android.content.ComponentName
 *  android.content.Context
 *  android.content.Intent
 *  android.content.IntentFilter
 *  android.content.pm.ActivityInfo
 *  android.content.pm.ApplicationInfo
 *  android.content.pm.ComponentInfo
 *  android.os.Handler
 *  android.os.HandlerThread
 *  android.os.IBinder
 *  android.os.Looper
 *  android.os.Message
 *  androidx.annotation.NonNull
 *  androidx.annotation.RequiresApi
 */
package com.lody.virtual.receiver;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.ComponentInfo;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.VClient;
import com.lody.virtual.client.env.SpecialComponentList;
import com.lody.virtual.client.ipc.VPackageManager;
import com.lody.virtual.helper.compat.BuildCompat;
import com.lody.virtual.helper.utils.ComponentUtils;
import com.lody.virtual.helper.utils.VLog;
import com.lody.virtual.remote.BroadcastIntentData;
import com.lody.virtual.remote.ReceiverInfo;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mirror.android.content.BroadcastReceiver;

public class StaticReceiverSystem {
    private static final int BROADCAST_TIME_OUT = 8500;
    private static final String TAG = "StaticReceiverSystem";
    private static StaticReceiverSystem mSystem = new StaticReceiverSystem();
    private ApplicationInfo mApplicationInfo;
    private Map<IBinder, BroadcastRecord> mBroadcastRecords = new HashMap<IBinder, BroadcastRecord>();
    private Context mContext;
    private StaticScheduler mScheduler;
    private TimeoutHandler mTimeoutHandler;
    private int mUserId;

    public static StaticReceiverSystem get() {
        return mSystem;
    }

    public Map BroadcastRecords(StaticReceiverSystem staticReceiverSystem) {
        return staticReceiverSystem.mBroadcastRecords;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public boolean handleStaticBroadcast(StaticReceiverSystem staticReceiverSystem, BroadcastIntentData broadcastIntentData, ActivityInfo activityInfo, android.content.BroadcastReceiver.PendingResult pendingResult) {
        if (broadcastIntentData.targetPackage == null || broadcastIntentData.targetPackage.equals(activityInfo.packageName)) {
            if (broadcastIntentData.userId == -1 || broadcastIntentData.userId == this.mUserId) {
                ComponentName componentName = ComponentUtils.toComponentName((ComponentInfo)activityInfo);
                BroadcastRecord broadcastRecord = new BroadcastRecord(pendingResult, activityInfo);
                IBinder iBinder = BroadcastReceiver.PendingResult.mToken.get(pendingResult);
                Map<IBinder, BroadcastRecord> map = this.mBroadcastRecords;
                synchronized (map) {
                    this.mBroadcastRecords.put(iBinder, broadcastRecord);
                }
                Message message = new Message();
                message.obj = iBinder;
                this.mTimeoutHandler.sendMessageDelayed(message, 8500L);
                VClient.get().scheduleReceiver(activityInfo.processName, componentName, broadcastIntentData.intent, pendingResult);
                return true;
            }
            return false;
        }
        return false;
    }

    @RequiresApi(api=26)
    public void attach(String processName, Context context, ApplicationInfo appInfo, int userId) {
        StaticReceiverSystem staticReceiverSystem = this;
        if (staticReceiverSystem.mApplicationInfo != null) {
            throw new IllegalStateException("attached");
        }
        staticReceiverSystem.mContext = context;
        staticReceiverSystem.mApplicationInfo = appInfo;
        staticReceiverSystem.mUserId = userId;
        HandlerThread broadcastThread = new HandlerThread("BroadcastThread");
        HandlerThread anrThread = new HandlerThread("BroadcastAnrThread");
        broadcastThread.start();
        anrThread.start();
        staticReceiverSystem.mScheduler = new StaticScheduler(broadcastThread.getLooper());
        staticReceiverSystem.mTimeoutHandler = new TimeoutHandler(staticReceiverSystem, anrThread.getLooper());
        List<ReceiverInfo> receiverList = VPackageManager.get().getReceiverInfos(appInfo.packageName, processName, userId);
        for (ReceiverInfo receiverInfo : receiverList) {
            String str;
            String componentAction = ComponentUtils.getComponentAction(receiverInfo.info);
            IntentFilter componentFilter = new IntentFilter(componentAction);
            componentFilter.addCategory("__VA__|_static_receiver_");
            if (BuildCompat.isUpsideDownCake()) {
                str = "__VA__|_static_receiver_";
                staticReceiverSystem.mContext.registerReceiver((android.content.BroadcastReceiver)new StaticReceiver(staticReceiverSystem, receiverInfo.info), componentFilter, null, (Handler)staticReceiverSystem.mScheduler, 2);
            } else {
                str = "__VA__|_static_receiver_";
                staticReceiverSystem.mContext.registerReceiver((android.content.BroadcastReceiver)new StaticReceiver(staticReceiverSystem, receiverInfo.info), componentFilter, null, (Handler)staticReceiverSystem.mScheduler);
            }
            for (IntentFilter filter : receiverInfo.filters) {
                Object obj;
                SpecialComponentList.protectIntentFilter(filter);
                String str2 = str;
                filter.addCategory(str2);
                if (BuildCompat.isUpsideDownCake()) {
                    staticReceiverSystem.mContext.registerReceiver((android.content.BroadcastReceiver)new StaticReceiver(staticReceiverSystem, receiverInfo.info), filter, null, (Handler)staticReceiverSystem.mScheduler, 2);
                    obj = null;
                } else {
                    Context context2 = staticReceiverSystem.mContext;
                    StaticReceiver staticReceiver = new StaticReceiver(staticReceiverSystem, receiverInfo.info);
                    StaticScheduler staticScheduler = staticReceiverSystem.mScheduler;
                    obj = null;
                    context2.registerReceiver((android.content.BroadcastReceiver)staticReceiver, filter, null, (Handler)staticScheduler);
                }
                str = str2;
                staticReceiverSystem = this;
            }
            staticReceiverSystem = this;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public boolean broadcastFinish(IBinder iBinder) {
        BroadcastRecord remove;
        Map<IBinder, BroadcastRecord> map = this.mBroadcastRecords;
        synchronized (map) {
            remove = this.mBroadcastRecords.remove(iBinder);
        }
        if (remove == null) {
            return false;
        }
        this.mTimeoutHandler.removeMessages(0, iBinder);
        remove.pendingResult.finish();
        return true;
    }

    static {
        mSystem = new StaticReceiverSystem();
    }

    final class TimeoutHandler
    extends Handler {
        final StaticReceiverSystem staticReceiverSystem;

        public TimeoutHandler(StaticReceiverSystem s, Looper looper) {
            super(looper);
            this.staticReceiverSystem = s;
        }

        public void handleMessage(@NonNull Message msg) {
            BroadcastRecord broadcastRecord = (BroadcastRecord)StaticReceiverSystem.get().BroadcastRecords(this.staticReceiverSystem).remove((IBinder)msg.obj);
            if (broadcastRecord != null) {
                VLog.w("StaticReceiverSystem", "Broadcast timeout, cancel to dispatch it.", new Object[0]);
                broadcastRecord.pendingResult.finish();
            }
        }
    }

    final class StaticScheduler
    extends Handler {
        public StaticScheduler(Looper looper) {
            super(looper);
        }
    }

    class StaticReceiver extends android.content.BroadcastReceiver {
        private ActivityInfo info;
        final StaticReceiverSystem staticReceiverSystem;

        public StaticReceiver(StaticReceiverSystem staticReceiverSystem, ActivityInfo info) {
            this.info = info;
            this.staticReceiverSystem = staticReceiverSystem;
        }

        public void onReceive(Context context, Intent intent) {
            android.content.BroadcastReceiver.PendingResult result;
            if ((intent.getFlags() & 0x40000000) != 0 || this.isInitialStickyBroadcast() || VClient.get() == null || VClient.get().getCurrentApplication() == null) {
                return;
            }
            if (intent.getAction() != null && intent.getAction().startsWith("_VA_protected_")) {
                return;
            }
            intent.setExtrasClassLoader(VClient.get().getCurrentApplication().getClassLoader());
            BroadcastIntentData data = new BroadcastIntentData(intent);
            if (data.intent == null) {
                data.intent = intent;
                data.targetPackage = intent.getPackage();
                intent.setPackage(null);
            }
            if ((result = this.goAsync()) != null && !StaticReceiverSystem.get().handleStaticBroadcast(this.staticReceiverSystem, data, this.info, result)) {
                result.finish();
            }
        }
    }

    final class BroadcastRecord {
        android.content.BroadcastReceiver.PendingResult pendingResult;
        ActivityInfo receiverInfo;

        public BroadcastRecord(android.content.BroadcastReceiver.PendingResult pendingResult, ActivityInfo receiverInfo) {
            this.pendingResult = pendingResult;
            this.receiverInfo = receiverInfo;
        }
    }
}

