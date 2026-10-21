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
 */
package com.lody.virtual.client.receiver;


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
import com.lody.virtual.StringFog;
import com.lody.virtual.client.VClient;
import com.lody.virtual.client.env.SpecialComponentList;
import com.lody.virtual.client.ipc.VPackageManager;
import com.lody.virtual.helper.utils.ComponentUtils;
import com.lody.virtual.helper.utils.VLog;
import com.lody.virtual.remote.BroadcastIntentData;
import com.lody.virtual.remote.ReceiverInfo;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mirror.android.content.BroadcastReceiver;

public class StaticReceiverSystem {
    private static final String TAG = "StaticReceiverSystem";
    private static final StaticReceiverSystem mSystem = new StaticReceiverSystem();
    private static final int BROADCAST_TIME_OUT = 8500;
    private Context mContext;
    private ApplicationInfo mApplicationInfo;
    private int mUserId;
    private StaticScheduler mScheduler;
    private TimeoutHandler mTimeoutHandler;
    private final Map<IBinder, BroadcastRecord> mBroadcastRecords = new HashMap<IBinder, BroadcastRecord>();

    public void attach(String processName, Context context, ApplicationInfo appInfo, int userId) {
        if (this.mApplicationInfo != null) {
            throw new IllegalStateException("attached");
        }
        this.mContext = context;
        this.mApplicationInfo = appInfo;
        this.mUserId = userId;
        HandlerThread broadcastThread = new HandlerThread("BroadcastThread");
        HandlerThread anrThread = new HandlerThread("BroadcastAnrThread");
        broadcastThread.start();
        anrThread.start();
        this.mScheduler = new StaticScheduler(broadcastThread.getLooper());
        this.mTimeoutHandler = new TimeoutHandler(anrThread.getLooper());
        List<ReceiverInfo> receiverList = VPackageManager.get().getReceiverInfos(appInfo.packageName, processName, userId);
        for (ReceiverInfo receiverInfo : receiverList) {
            String componentAction = ComponentUtils.getComponentAction(receiverInfo.info);
            IntentFilter componentFilter = new IntentFilter(componentAction);
            componentFilter.addCategory("__VA__|_static_receiver_");
            this.mContext.registerReceiver((android.content.BroadcastReceiver)new StaticReceiver(receiverInfo.info), componentFilter, null, (Handler)this.mScheduler);
            for (IntentFilter filter : receiverInfo.filters) {
                SpecialComponentList.protectIntentFilter(filter);
                filter.addCategory("__VA__|_static_receiver_");
                this.mContext.registerReceiver((android.content.BroadcastReceiver)new StaticReceiver(receiverInfo.info), filter, null, (Handler)this.mScheduler);
            }
        }
    }

    public static StaticReceiverSystem get() {
        return mSystem;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public boolean broadcastFinish(IBinder token) {
        BroadcastRecord record;
        Map<IBinder, BroadcastRecord> map = this.mBroadcastRecords;
        synchronized (map) {
            record = this.mBroadcastRecords.remove(token);
        }
        if (record == null) {
            return false;
        }
        this.mTimeoutHandler.removeMessages(0, token);
        record.pendingResult.finish();
        return true;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private boolean handleStaticBroadcast(BroadcastIntentData data, ActivityInfo info, android.content.BroadcastReceiver.PendingResult result) {
        if (data.targetPackage != null && !data.targetPackage.equals(info.packageName)) {
            return false;
        }
        if (data.userId != -1 && data.userId != this.mUserId) {
            return false;
        }
        ComponentName componentName = ComponentUtils.toComponentName((ComponentInfo)info);
        BroadcastRecord record = new BroadcastRecord(info, result);
        IBinder token = BroadcastReceiver.PendingResult.mToken.get(result);
        Map<IBinder, BroadcastRecord> map = this.mBroadcastRecords;
        synchronized (map) {
            this.mBroadcastRecords.put(token, record);
        }
        Message msg = new Message();
        msg.obj = token;
        this.mTimeoutHandler.sendMessageDelayed(msg, 8500L);
        VClient.get().scheduleReceiver(info.processName, componentName, data.intent, result);
        return true;
    }

    private class StaticReceiver extends android.content.BroadcastReceiver {
        private ActivityInfo info;

        public StaticReceiver(ActivityInfo info) {
            this.info = info;
        }

        public void onReceive(Context context, Intent intent) {
            android.content.BroadcastReceiver.PendingResult result;
            if ((intent.getFlags() & 0x40000000) != 0 || this.isInitialStickyBroadcast() || VClient.get() == null || VClient.get().getCurrentApplication() == null) {
                return;
            }
            intent.setExtrasClassLoader(VClient.get().getCurrentApplication().getClassLoader());
            BroadcastIntentData data = new BroadcastIntentData(intent);
            if (data.intent == null) {
                data.intent = intent;
                data.targetPackage = intent.getPackage();
                intent.setPackage(null);
            }
            if ((result = this.goAsync()) != null && !StaticReceiverSystem.this.handleStaticBroadcast(data, this.info, result)) {
                result.finish();
            }
        }
    }

    private final class TimeoutHandler extends Handler {
        TimeoutHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message msg) {
            IBinder token = (IBinder)msg.obj;
            BroadcastRecord r = (BroadcastRecord)StaticReceiverSystem.this.mBroadcastRecords.remove(token);
            if (r != null) {
                VLog.w("StaticReceiverSystem", "Broadcast timeout, cancel to dispatch it.", new Object[0]);
                r.pendingResult.finish();
            }
        }
    }

    private static final class BroadcastRecord {
        ActivityInfo receiverInfo;
        android.content.BroadcastReceiver.PendingResult pendingResult;

        BroadcastRecord(ActivityInfo receiverInfo, android.content.BroadcastReceiver.PendingResult pendingResult) {
            this.receiverInfo = receiverInfo;
            this.pendingResult = pendingResult;
        }
    }

    private static final class StaticScheduler
    extends Handler {
        StaticScheduler(Looper looper) {
            super(looper);
        }
    }
}

