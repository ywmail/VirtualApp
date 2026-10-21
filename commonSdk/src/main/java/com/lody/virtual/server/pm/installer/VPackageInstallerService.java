/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.annotation.TargetApi
 *  android.content.Context
 *  android.content.Intent
 *  android.content.IntentSender
 *  android.content.IntentSender$SendIntentException
 *  android.graphics.Bitmap
 *  android.os.Bundle
 *  android.os.Handler
 *  android.os.HandlerThread
 *  android.os.IInterface
 *  android.os.Looper
 *  android.os.Message
 *  android.os.Parcelable
 *  android.os.RemoteCallbackList
 *  android.os.RemoteException
 *  android.text.TextUtils
 *  android.util.SparseArray
 */
package com.lody.virtual.server.pm.installer;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.IPackageInstallerCallback;
import android.content.pm.IPackageInstallerSession;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IInterface;
import android.os.Looper;
import android.os.Message;
import android.os.Parcelable;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.SparseArray;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.helper.compat.ObjectsCompat;
import com.lody.virtual.helper.utils.Singleton;
import com.lody.virtual.os.VBinder;
import com.lody.virtual.os.VEnvironment;
import com.lody.virtual.os.VUserHandle;
import com.lody.virtual.remote.VParceledListSlice;
import com.lody.virtual.server.IPackageInstaller;
import com.lody.virtual.server.pm.VAppManagerService;
import com.lody.virtual.server.pm.installer.PackageHelper;
import com.lody.virtual.server.pm.installer.PackageInstallObserver;
import com.lody.virtual.server.pm.installer.PackageInstallerSession;
import com.lody.virtual.server.pm.installer.SessionInfo;
import com.lody.virtual.server.pm.installer.SessionParams;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Random;

@TargetApi(value=21)
public class VPackageInstallerService
extends IPackageInstaller.Stub {
    private static final String TAG = "PackageInstaller";
    private static final long MAX_ACTIVE_SESSIONS = 1024L;
    private static final Singleton<VPackageInstallerService> gDefault = new Singleton<VPackageInstallerService>(){

        @Override
        protected VPackageInstallerService create() {
            return new VPackageInstallerService();
        }
    };
    private final Random mRandom = new SecureRandom();
    private final SparseArray<PackageInstallerSession> mSessions = new SparseArray();
    private final Handler mInstallHandler;
    private final Callbacks mCallbacks;
    private final HandlerThread mInstallThread;
    private final InternalCallback mInternalCallback = new InternalCallback();
    private Context mContext = VirtualCore.get().getContext();

    private VPackageInstallerService() {
        this.mInstallThread = new HandlerThread("PackageInstaller");
        this.mInstallThread.start();
        this.mInstallHandler = new Handler(this.mInstallThread.getLooper());
        this.mCallbacks = new Callbacks(this.mInstallThread.getLooper());
    }

    public static VPackageInstallerService get() {
        return gDefault.get();
    }

    private static int getSessionCount(SparseArray<PackageInstallerSession> sessions, int installerUid) {
        int count = 0;
        int size = sessions.size();
        for (int i = 0; i < size; ++i) {
            PackageInstallerSession session = (PackageInstallerSession)sessions.valueAt(i);
            if (session.installerUid != installerUid) continue;
            ++count;
        }
        return count;
    }

    @Override
    public int createSession(SessionParams params, String installerPackageName, int userId) {
        try {
            return this.createSessionInternal(params, installerPackageName, userId, VBinder.getCallingUid());
        }
        catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private int createSessionInternal(SessionParams params, String installerPackageName, int userId, int callingUid) throws IOException {
        PackageInstallerSession session;
        int sessionId;
        SparseArray<PackageInstallerSession> sparseArray = this.mSessions;
        synchronized (sparseArray) {
            int activeCount = VPackageInstallerService.getSessionCount(this.mSessions, callingUid);
            if ((long)activeCount >= 1024L) {
                throw new IllegalStateException("Too many active sessions for UID " + callingUid);
            }
            sessionId = this.allocateSessionIdLocked();
            session = new PackageInstallerSession(this.mInternalCallback, this.mContext, this.mInstallHandler.getLooper(), installerPackageName, sessionId, userId, callingUid, params, VEnvironment.getPackageInstallerStageDir());
        }
        sparseArray = this.mSessions;
        synchronized (sparseArray) {
            this.mSessions.put(sessionId, session);
        }
        this.mCallbacks.notifySessionCreated(session.sessionId, session.userId);
        return sessionId;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void updateSessionAppIcon(int sessionId, Bitmap appIcon) {
        SparseArray<PackageInstallerSession> sparseArray = this.mSessions;
        synchronized (sparseArray) {
            PackageInstallerSession session = (PackageInstallerSession)this.mSessions.get(sessionId);
            if (session == null || !this.isCallingUidOwner(session)) {
                throw new SecurityException("Caller has no access to session " + sessionId);
            }
            session.params.appIcon = appIcon;
            session.params.appIconLastModified = -1L;
            this.mInternalCallback.onSessionBadgingChanged(session);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void updateSessionAppLabel(int sessionId, String appLabel) {
        SparseArray<PackageInstallerSession> sparseArray = this.mSessions;
        synchronized (sparseArray) {
            PackageInstallerSession session = (PackageInstallerSession)this.mSessions.get(sessionId);
            if (session == null || !this.isCallingUidOwner(session)) {
                throw new SecurityException("Caller has no access to session " + sessionId);
            }
            session.params.appLabel = appLabel;
            this.mInternalCallback.onSessionBadgingChanged(session);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void abandonSession(int sessionId) {
        SparseArray<PackageInstallerSession> sparseArray = this.mSessions;
        synchronized (sparseArray) {
            PackageInstallerSession session = (PackageInstallerSession)this.mSessions.get(sessionId);
            if (session == null || !this.isCallingUidOwner(session)) {
                throw new SecurityException("Caller has no access to session " + sessionId);
            }
            try {
                session.abandon();
            }
            catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public IPackageInstallerSession openSession(int sessionId) {
        try {
            return this.openSessionInternal(sessionId);
        }
        catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private IPackageInstallerSession openSessionInternal(int sessionId) throws IOException {
        SparseArray<PackageInstallerSession> sparseArray = this.mSessions;
        synchronized (sparseArray) {
            PackageInstallerSession session = (PackageInstallerSession)this.mSessions.get(sessionId);
            if (session == null || !this.isCallingUidOwner(session)) {
                throw new SecurityException("Caller has no access to session " + sessionId);
            }
            session.open();
            return session;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public SessionInfo getSessionInfo(int sessionId) {
        SparseArray<PackageInstallerSession> sparseArray = this.mSessions;
        synchronized (sparseArray) {
            PackageInstallerSession session = (PackageInstallerSession)this.mSessions.get(sessionId);
            return session != null ? session.generateInfo() : null;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public VParceledListSlice getAllSessions(int userId) {
        ArrayList<SessionInfo> result = new ArrayList<SessionInfo>();
        SparseArray<PackageInstallerSession> sparseArray = this.mSessions;
        synchronized (sparseArray) {
            for (int i = 0; i < this.mSessions.size(); ++i) {
                PackageInstallerSession session = (PackageInstallerSession)this.mSessions.valueAt(i);
                if (session.userId != userId) continue;
                result.add(session.generateInfo());
            }
        }
        return new VParceledListSlice(result);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public VParceledListSlice getMySessions(String installerPackageName, int userId) {
        ArrayList<SessionInfo> result = new ArrayList<SessionInfo>();
        SparseArray<PackageInstallerSession> sparseArray = this.mSessions;
        synchronized (sparseArray) {
            for (int i = 0; i < this.mSessions.size(); ++i) {
                PackageInstallerSession session = (PackageInstallerSession)this.mSessions.valueAt(i);
                if (!ObjectsCompat.equals(session.installerPackageName, installerPackageName) || session.userId != userId) continue;
                result.add(session.generateInfo());
            }
        }
        return new VParceledListSlice(result);
    }

    @Override
    public void registerCallback(IPackageInstallerCallback callback, int userId) {
        this.mCallbacks.register(callback, userId);
    }

    @Override
    public void unregisterCallback(IPackageInstallerCallback callback) {
        this.mCallbacks.unregister(callback);
    }

    @Override
    public void uninstall(String packageName, String callerPackageName, int flags, IntentSender statusReceiver, int userId) {
        boolean success = VAppManagerService.get().uninstallPackage(packageName);
        if (statusReceiver != null) {
            Intent fillIn = new Intent();
            fillIn.putExtra("android.content.pm.extra.PACKAGE_NAME", packageName);
            fillIn.putExtra("android.content.pm.extra.STATUS", success ? 0 : 1);
            fillIn.putExtra("android.content.pm.extra.STATUS_MESSAGE", PackageHelper.deleteStatusToString(success));
            fillIn.putExtra("android.content.pm.extra.LEGACY_STATUS", success ? 1 : -1);
            try {
                statusReceiver.sendIntent(this.mContext, 0, fillIn, null, null);
            }
            catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void setPermissionsResult(int sessionId, boolean accepted) {
        SparseArray<PackageInstallerSession> sparseArray = this.mSessions;
        synchronized (sparseArray) {
            PackageInstallerSession session = (PackageInstallerSession)this.mSessions.get(sessionId);
            if (session != null) {
                session.setPermissionsResult(accepted);
            }
        }
    }

    private boolean isCallingUidOwner(PackageInstallerSession session) {
        return true;
    }

    private int allocateSessionIdLocked() {
        int n = 0;
        do {
            int sessionId;
            if (this.mSessions.get(sessionId = this.mRandom.nextInt(0x7FFFFFFE) + 1) != null) continue;
            return sessionId;
        } while (n++ < 32);
        throw new IllegalStateException("Failed to allocate session ID");
    }

    class InternalCallback {
        InternalCallback() {
        }

        public void onSessionBadgingChanged(PackageInstallerSession session) {
            VPackageInstallerService.this.mCallbacks.notifySessionBadgingChanged(session.sessionId, session.userId);
        }

        public void onSessionActiveChanged(PackageInstallerSession session, boolean active) {
            VPackageInstallerService.this.mCallbacks.notifySessionActiveChanged(session.sessionId, session.userId, active);
        }

        public void onSessionProgressChanged(PackageInstallerSession session, float progress) {
            VPackageInstallerService.this.mCallbacks.notifySessionProgressChanged(session.sessionId, session.userId, progress);
        }

        public void onSessionFinished(final PackageInstallerSession session, boolean success) {
            VPackageInstallerService.this.mCallbacks.notifySessionFinished(session.sessionId, session.userId, success);
            VPackageInstallerService.this.mInstallHandler.post(new Runnable(){

                /*
                 * WARNING - Removed try catching itself - possible behaviour change.
                 */
                @Override
                public void run() {
                    SparseArray sparseArray = VPackageInstallerService.this.mSessions;
                    synchronized (sparseArray) {
                        VPackageInstallerService.this.mSessions.remove(session.sessionId);
                    }
                }
            });
        }

        public void onSessionPrepared(PackageInstallerSession session) {
        }

        public void onSessionSealedBlocking(PackageInstallerSession session) {
        }
    }

    static class PackageInstallObserverAdapter
    extends PackageInstallObserver {
        private final Context mContext;
        private final IntentSender mTarget;
        private final int mSessionId;
        private final int mUserId;

        PackageInstallObserverAdapter(Context context, IntentSender target, int sessionId, int userId) {
            this.mContext = context;
            this.mTarget = target;
            this.mSessionId = sessionId;
            this.mUserId = userId;
        }

        @Override
        public void onUserActionRequired(Intent intent) {
            Intent fillIn = new Intent();
            fillIn.putExtra("android.content.pm.extra.SESSION_ID", this.mSessionId);
            fillIn.putExtra("android.content.pm.extra.STATUS", -1);
            fillIn.putExtra("android.intent.extra.INTENT", (Parcelable)intent);
            try {
                this.mTarget.sendIntent(this.mContext, 0, fillIn, null, null);
            }
            catch (IntentSender.SendIntentException sendIntentException) {
                // empty catch block
            }
        }

        @Override
        public void onPackageInstalled(String basePackageName, int returnCode, String msg, Bundle extras) {
            String existing;
            Intent fillIn = new Intent();
            fillIn.putExtra("android.content.pm.extra.PACKAGE_NAME", basePackageName);
            fillIn.putExtra("android.content.pm.extra.SESSION_ID", this.mSessionId);
            fillIn.putExtra("android.content.pm.extra.STATUS", PackageHelper.installStatusToPublicStatus(returnCode));
            fillIn.putExtra("android.content.pm.extra.STATUS_MESSAGE", PackageHelper.installStatusToString(returnCode, msg));
            fillIn.putExtra("android.content.pm.extra.LEGACY_STATUS", returnCode);
            if (extras != null && !TextUtils.isEmpty((CharSequence)(existing = extras.getString("android.content.pm.extra.FAILURE_EXISTING_PACKAGE")))) {
                fillIn.putExtra("android.content.pm.extra.OTHER_PACKAGE_NAME", existing);
            }
            try {
                this.mTarget.sendIntent(this.mContext, 0, fillIn, null, null);
            }
            catch (IntentSender.SendIntentException sendIntentException) {
                // empty catch block
            }
        }
    }

    private static class Callbacks
    extends Handler {
        private static final int MSG_SESSION_CREATED = 1;
        private static final int MSG_SESSION_BADGING_CHANGED = 2;
        private static final int MSG_SESSION_ACTIVE_CHANGED = 3;
        private static final int MSG_SESSION_PROGRESS_CHANGED = 4;
        private static final int MSG_SESSION_FINISHED = 5;
        private final RemoteCallbackList<IPackageInstallerCallback> mCallbacks = new RemoteCallbackList();

        public Callbacks(Looper looper) {
            super(looper);
        }

        public void register(IPackageInstallerCallback callback, int userId) {
            this.mCallbacks.register((IPackageInstallerCallback) callback, (Object)new VUserHandle(userId));
        }

        public void unregister(IPackageInstallerCallback callback) {
            this.mCallbacks.unregister((IPackageInstallerCallback) callback);
        }

        public void handleMessage(Message msg) {
            int userId = msg.arg2;
            int n = this.mCallbacks.beginBroadcast();
            for (int i = 0; i < n; ++i) {
                IPackageInstallerCallback callback = (IPackageInstallerCallback)this.mCallbacks.getBroadcastItem(i);
                VUserHandle user = (VUserHandle)this.mCallbacks.getBroadcastCookie(i);
                if (userId != user.getIdentifier()) continue;
                try {
                    this.invokeCallback(callback, msg);
                    continue;
                }
                catch (RemoteException remoteException) {
                    // empty catch block
                }
            }
            this.mCallbacks.finishBroadcast();
        }

        private void invokeCallback(IPackageInstallerCallback callback, Message msg) throws RemoteException {
            int sessionId = msg.arg1;
            switch (msg.what) {
                case 1: {
                    callback.onSessionCreated(sessionId);
                    break;
                }
                case 2: {
                    callback.onSessionBadgingChanged(sessionId);
                    break;
                }
                case 3: {
                    callback.onSessionActiveChanged(sessionId, (Boolean)msg.obj);
                    break;
                }
                case 4: {
                    callback.onSessionProgressChanged(sessionId, ((Float)msg.obj).floatValue());
                    break;
                }
                case 5: {
                    callback.onSessionFinished(sessionId, (Boolean)msg.obj);
                }
            }
        }

        private void notifySessionCreated(int sessionId, int userId) {
            this.obtainMessage(1, sessionId, userId).sendToTarget();
        }

        private void notifySessionBadgingChanged(int sessionId, int userId) {
            this.obtainMessage(2, sessionId, userId).sendToTarget();
        }

        private void notifySessionActiveChanged(int sessionId, int userId, boolean active) {
            this.obtainMessage(3, sessionId, userId, active).sendToTarget();
        }

        private void notifySessionProgressChanged(int sessionId, int userId, float progress) {
            this.obtainMessage(4, sessionId, userId, Float.valueOf(progress)).sendToTarget();
        }

        public void notifySessionFinished(int sessionId, int userId, boolean success) {
            this.obtainMessage(5, sessionId, userId, success).sendToTarget();
        }
    }
}

