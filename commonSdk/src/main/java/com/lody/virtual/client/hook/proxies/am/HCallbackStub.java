//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.lody.virtual.client.hook.proxies.am;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;

import com.carlos.common.utils.LogUtil;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.VClient;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.hook.proxies.app.ActivityClientControllerStub;
import com.lody.virtual.client.interfaces.IInjector;
import com.lody.virtual.client.ipc.VActivityManager;
import com.lody.virtual.helper.AvoidRecursive;
import com.lody.virtual.helper.compat.BuildCompat;
import com.lody.virtual.helper.utils.ComponentUtils;
import com.lody.virtual.helper.utils.VLog;
import com.lody.virtual.remote.InstalledAppInfo;
import com.lody.virtual.remote.ShadowActivityInfo;
import com.orhanobut.logger.Logger;

import java.util.List;
import mirror.android.app.ActivityClient;
import mirror.android.app.ActivityManagerNative;
import mirror.android.app.ActivityThread;
import mirror.android.app.ClientTransactionHandler;
import mirror.android.app.IActivityManager;
import mirror.android.app.ActivityClient.ActivityClientControllerSingleton;
import mirror.android.app.ActivityThread.ActivityClientRecord;
import mirror.android.app.ActivityThread.H;
import mirror.android.app.servertransaction.ClientTransaction;
import mirror.android.app.servertransaction.ClientTransactionItem;
import mirror.android.app.servertransaction.LaunchActivityItem;
import mirror.android.app.servertransaction.TopResumedActivityChangeItem;

public class HCallbackStub implements Handler.Callback, IInjector {
    private static final int LAUNCH_ACTIVITY;
    private static final int EXECUTE_TRANSACTION;
    private static final int SCHEDULE_CRASH;
    private static final String TAG;
    private static final HCallbackStub sCallback;
    private final AvoidRecursive mAvoidRecurisve = new AvoidRecursive();
    private Handler.Callback otherCallback;

    private HCallbackStub() {
    }

    public static HCallbackStub getDefault() {
        return sCallback;
    }

    private static Handler getH() {
        return (Handler)ActivityThread.mH.get(VirtualCore.mainThread());
    }

    private static Handler.Callback getHCallback() {
        try {
            Handler handler = getH();
            return (Handler.Callback)mirror.android.os.Handler.mCallback.get(handler);
        } catch (Throwable var1) {
            var1.printStackTrace();
            return null;
        }
    }

    public boolean handleMessage(Message msg) {
        if (this.mAvoidRecurisve.beginCall()) {
            try {
                boolean var7;
                if (LAUNCH_ACTIVITY == msg.what) {
                    if (!this.handleLaunchActivity(msg, msg.obj)) {
                        var7 = true;
                        return var7;
                    }
                } else if (BuildCompat.isPie() && EXECUTE_TRANSACTION == msg.what) {
                    if (!this.handleExecuteTransaction(msg)) {
                        var7 = true;
                        return var7;
                    }
                } else if (SCHEDULE_CRASH == msg.what) {
                    String crashReason = (String)msg.obj;
                    (new RemoteException(crashReason)).printStackTrace();
                    boolean var3 = false;
                    return var3;
                }

                if (this.otherCallback == null) {
                    return false;
                } else {
                    var7 = this.otherCallback.handleMessage(msg);
                    return var7;
                }
            } finally {
                this.mAvoidRecurisve.finishCall();
            }
        } else {
            return false;
        }
    }

    private boolean handleExecuteTransaction(Message msg) {
        Object transaction = msg.obj;
        IBinder token = this.getTokenByClientTransaction(transaction);
        Object r = ClientTransactionHandler.getActivityClient.call(VirtualCore.mainThread(), new Object[]{token});
        List<Object> activityCallbacks = (List)ClientTransaction.mActivityCallbacks.get(transaction);
        if (activityCallbacks != null && !activityCallbacks.isEmpty()) {
            Object item = activityCallbacks.get(0);
            if (item == null) {
                return true;
            } else if (r == null) {
                return item.getClass() != LaunchActivityItem.TYPE ? true : this.handleLaunchActivity(msg, item);
            } else {
                if (BuildCompat.isQ() && TopResumedActivityChangeItem.TYPE != null && item.getClass() == TopResumedActivityChangeItem.TYPE) {
                    try {
                        if (TopResumedActivityChangeItem.mOnTop.get(item) == ActivityClientRecord.isTopResumedActivity.get(r)) {
                            Log.e(TAG, "Activity top position already set to onTop=" + TopResumedActivityChangeItem.mOnTop.get(item));
                            return false;
                        }
                    } catch (Throwable var8) {
                        return false;
                    }
                }

                return true;
            }
        } else {
            return true;
        }
    }

    private IBinder getTokenByClientTransaction(Object transaction) {
        IBinder token = null;
        if (ClientTransaction.mActivityToken == null) {
            List items;
            Object mLifecycleStateRequest;
            if (ClientTransaction.getTransactionItems != null && (items = (List)ClientTransaction.getTransactionItems.call(transaction, new Object[0])) != null && !items.isEmpty()) {
                mLifecycleStateRequest = items.get(0);
                token = (IBinder)ClientTransactionItem.getActivityToken.call(mLifecycleStateRequest, new Object[0]);
            }

            if (token == null) {
                mLifecycleStateRequest = ClientTransaction.mLifecycleStateRequest.get(transaction);
                if (mLifecycleStateRequest == null) {
                    List<Object> activityCallbacks = (List)ClientTransaction.mActivityCallbacks.get(transaction);
                    return activityCallbacks != null && !activityCallbacks.isEmpty() ? (IBinder)ClientTransactionItem.getActivityToken.call(activityCallbacks.get(0), new Object[0]) : token;
                } else {
                    return (IBinder)ClientTransactionItem.getActivityToken.call(mLifecycleStateRequest, new Object[0]);
                }
            } else {
                return token;
            }
        } else {
            return (IBinder)ClientTransaction.mActivityToken.get(transaction);
        }
    }

    private boolean handleLaunchActivity(Message msg, Object r) {
        Intent stubIntent;
        if (BuildCompat.isPie()) {
            stubIntent = (Intent)LaunchActivityItem.mIntent.get(r);
        } else {
            stubIntent = (Intent)ActivityClientRecord.intent.get(r);
        }

        ShadowActivityInfo saveInstance = new ShadowActivityInfo(stubIntent);
        if (saveInstance.intent == null) {
            return true;
        } else {
            Intent intent = saveInstance.intent;
            IBinder token;
            if (BuildCompat.isPie()) {
                token = (IBinder)ClientTransaction.mActivityToken.get(msg.obj);
            } else {
                token = this.getTokenByClientTransaction(msg.obj);
            }

            ActivityInfo info = saveInstance.info;
            if (info == null) {
                return true;
            } else if (VClient.get().getClientConfig() == null) {
                InstalledAppInfo installedAppInfo = VirtualCore.get().getInstalledAppInfo(info.packageName, 0);
                if (installedAppInfo == null) {
                    return true;
                } else {
                    VActivityManager.get().processRestarted(info.packageName, info.processName, saveInstance.userId);
                    getH().sendMessageAtFrontOfQueue(Message.obtain(msg));
                    return false;
                }
            } else {
                VClient.get().bindApplication(info.packageName, info.processName);
                int taskId = (Integer)IActivityManager.getTaskForActivity.call(ActivityManagerNative.getDefault.call(new Object[0]), new Object[]{token, false});
                VActivityManager.get().onActivityCreate(saveInstance.virtualToken, token, taskId);
                ClassLoader appClassLoader = VClient.get().getClassLoader(info.applicationInfo);
                ComponentUtils.unpackFillIn(intent, appClassLoader);
                if (BuildCompat.isPie()) {
                    if (BuildCompat.isS() && ActivityThread.getLaunchingActivity != null) {
                        Object activityClientRecord = ActivityThread.getLaunchingActivity.call(VirtualCore.mainThread(), new Object[]{token});
                        if (activityClientRecord != null) {
                            Object compatInfo = ActivityClientRecord.compatInfo.get(activityClientRecord);
                            Object loadedApk = ActivityThread.getPackageInfoNoCheck.call(VirtualCore.mainThread(), new Object[]{info.applicationInfo, compatInfo});
                            ActivityClientRecord.intent.set(activityClientRecord, intent);
                            ActivityClientRecord.activityInfo.set(activityClientRecord, info);
                            ActivityClientRecord.packageInfo.set(activityClientRecord, loadedApk);
                        }
                    }

                    if (BuildCompat.isS() && LaunchActivityItem.mActivityClientController != null) {
                        IInterface activityClientController = (IInterface)LaunchActivityItem.mActivityClientController.get(r);
                        if (activityClientController != null) {
                            ActivityClientControllerSingleton.mKnownInstance.set(ActivityClient.INTERFACE_SINGLETON.get(), ActivityClientControllerStub.getProxyInterface());
                        }
                    }

                    LaunchActivityItem.mIntent.set(r, intent);
                    LaunchActivityItem.mInfo.set(r, info);
                } else {
                    ActivityClientRecord.intent.set(r, intent);
                    ActivityClientRecord.activityInfo.set(r, info);
                }

                return true;
            }
        }
    }

    public void inject() {
        this.otherCallback = getHCallback();
        mirror.android.os.Handler.mCallback.set(getH(), this);
    }

    public boolean isEnvBad() {
        Handler.Callback callback = getHCallback();
        boolean envBad = callback != this;
        if (callback != null && envBad) {
            VLog.d(TAG, "HCallback has bad, other callback = " + callback, new Object[0]);
        }

        return envBad;
    }

    static {
        SCHEDULE_CRASH = H.SCHEDULE_CRASH.get();
        LAUNCH_ACTIVITY = BuildCompat.isPie() ? -1 : H.LAUNCH_ACTIVITY.get();
        EXECUTE_TRANSACTION = BuildCompat.isPie() ? H.EXECUTE_TRANSACTION.get() : -1;
        TAG = HCallbackStub.class.getSimpleName();
        sCallback = new HCallbackStub();
    }
}
