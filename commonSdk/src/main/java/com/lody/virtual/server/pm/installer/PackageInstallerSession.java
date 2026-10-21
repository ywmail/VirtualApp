/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.annotation.TargetApi
 *  android.content.Context
 *  android.content.IntentSender
 *  android.content.pm.Checksum
 *  android.net.Uri
 *  android.os.Bundle
 *  android.os.Handler
 *  android.os.Handler$Callback
 *  android.os.Looper
 *  android.os.Message
 *  android.os.ParcelFileDescriptor
 *  android.os.RemoteException
 *  android.system.ErrnoException
 *  android.system.Os
 *  android.system.OsConstants
 *  android.text.TextUtils
 */
package com.lody.virtual.server.pm.installer;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.Checksum;
import android.content.pm.DataLoaderParamsParcel;
import android.content.pm.IPackageInstallObserver2;
import android.content.pm.IPackageInstallerSession;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.system.ErrnoException;
import android.system.Os;
import android.system.OsConstants;
import android.text.TextUtils;
import com.lody.virtual.StringFog;
import com.lody.virtual.helper.utils.FileUtils;
import com.lody.virtual.helper.utils.VLog;
import com.lody.virtual.remote.VAppInstallerParams;
import com.lody.virtual.remote.VAppInstallerResult;
import com.lody.virtual.server.pm.VAppManagerService;
import com.lody.virtual.server.pm.installer.FileBridge;
import com.lody.virtual.server.pm.installer.SessionInfo;
import com.lody.virtual.server.pm.installer.SessionParams;
import com.lody.virtual.server.pm.installer.VPackageInstallerService;
import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@TargetApi(value=21)
public class PackageInstallerSession
extends IPackageInstallerSession.Stub {
    public static final int INSTALL_FAILED_INTERNAL_ERROR = -110;
    public static final int INSTALL_FAILED_ABORTED = -115;
    public static final int INSTALL_SUCCEEDED = 1;
    public static final int INSTALL_FAILED_INVALID_APK = -2;
    private static final String TAG = "PackageInstaller";
    private static final String REMOVE_SPLIT_MARKER_EXTENSION = ".removed";
    private static final int MSG_COMMIT = 0;
    private final VPackageInstallerService.InternalCallback mCallback;
    private final Context mContext;
    private final Handler mHandler;
    final int sessionId;
    final int userId;
    final int installerUid;
    final SessionParams params;
    final String installerPackageName;
    private boolean mPermissionsAccepted;
    final File stageDir;
    private final AtomicInteger mActiveCount = new AtomicInteger();
    private final Object mLock = new Object();
    private float mClientProgress = 0.0f;
    private float mInternalProgress = 0.0f;
    private float mProgress = 0.0f;
    private float mReportedProgress = -1.0f;
    private boolean mPrepared = false;
    private boolean mSealed = false;
    private boolean mDestroyed = false;
    private int mFinalStatus;
    private String mFinalMessage;
    private IPackageInstallObserver2 mRemoteObserver;
    private ArrayList<FileBridge> mBridges = new ArrayList();
    private File mResolvedStageDir;
    private String mPackageName;
    private File mResolvedBaseFile;
    private final List<File> mResolvedStagedFiles = new ArrayList<File>();
    private final Handler.Callback mHandlerCallback = new Handler.Callback(){

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        public boolean handleMessage(Message msg) {
            Object object = PackageInstallerSession.this.mLock;
            synchronized (object) {
                if (msg.obj != null) {
                    PackageInstallerSession.this.mRemoteObserver = (IPackageInstallObserver2)msg.obj;
                }
                try {
                    PackageInstallerSession.this.commitLocked();
                }
                catch (PackageManagerException e) {
                    String completeMsg = PackageInstallerSession.getCompleteMessage(e);
                    VLog.e("PackageInstaller", "Commit of session " + PackageInstallerSession.this.sessionId + " failed: " + completeMsg);
                    PackageInstallerSession.this.destroyInternal();
                    PackageInstallerSession.this.dispatchSessionFinished(e.error, completeMsg, null);
                }
                return true;
            }
        }
    };

    public PackageInstallerSession(VPackageInstallerService.InternalCallback callback, Context context, Looper looper, String installerPackageName, int sessionId, int userId, int installerUid, SessionParams params, File stageDir) {
        this.mCallback = callback;
        this.mContext = context;
        this.mHandler = new Handler(looper, this.mHandlerCallback);
        this.installerPackageName = installerPackageName;
        this.sessionId = sessionId;
        this.userId = userId;
        this.installerUid = installerUid;
        this.mPackageName = params.appPackageName;
        this.params = params;
        this.stageDir = stageDir;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public SessionInfo generateInfo() {
        SessionInfo info = new SessionInfo();
        Object object = this.mLock;
        synchronized (object) {
            info.sessionId = this.sessionId;
            info.installerPackageName = this.installerPackageName;
            info.resolvedBaseCodePath = this.mResolvedBaseFile != null ? this.mResolvedBaseFile.getAbsolutePath() : null;
            info.progress = this.mProgress;
            info.sealed = this.mSealed;
            info.active = this.mActiveCount.get() > 0;
            info.mode = this.params.mode;
            info.sizeBytes = this.params.sizeBytes;
            info.appPackageName = this.params.appPackageName;
            info.appIcon = this.params.appIcon;
            info.appLabel = this.params.appLabel;
        }
        return info;
    }

    private void commitLocked() throws PackageManagerException {
        if (this.mDestroyed) {
            throw new PackageManagerException(-110, "Session destroyed");
        }
        if (!this.mSealed) {
            throw new PackageManagerException(-110, "Session not sealed");
        }
        try {
            this.resolveStageDir();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        this.validateInstallLocked();
        this.mInternalProgress = 0.5f;
        this.computeProgressLocked(true);
        boolean success = false;
        for (File file : this.stageDir.listFiles()) {
            VLog.e(TAG, "found apk in stage dir: " + file.getPath());
            VAppInstallerResult res = VAppManagerService.get().installPackage(Uri.fromFile((File)file), new VAppInstallerParams());
            if (res.status != 0) continue;
            success = true;
        }
        this.destroyInternal();
        int returnCode = success ? 1 : -115;
        this.dispatchSessionFinished(returnCode, null, null);
    }

    private void validateInstallLocked() throws PackageManagerException {
        this.mResolvedBaseFile = null;
        this.mResolvedStagedFiles.clear();
        File[] addedFiles = this.mResolvedStageDir.listFiles();
        if (addedFiles == null || addedFiles.length == 0) {
            throw new PackageManagerException(-2, "No packages staged");
        }
        int i = 0;
        for (File addedFile : addedFiles) {
            if (addedFile.isDirectory()) continue;
            String targetName = "base_" + i + ".apk";
            File targetFile = new File(this.mResolvedStageDir, targetName);
            if (!addedFile.equals(targetFile)) {
                addedFile.renameTo(targetFile);
            }
            this.mResolvedBaseFile = targetFile;
            this.mResolvedStagedFiles.add(targetFile);
            ++i;
        }
        if (this.mResolvedBaseFile == null) {
            throw new PackageManagerException(-2, "Full install must include a base package");
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void setClientProgress(float progress) throws RemoteException {
        Object object = this.mLock;
        synchronized (object) {
            boolean forcePublish = this.mClientProgress == 0.0f;
            this.mClientProgress = progress;
            this.computeProgressLocked(forcePublish);
        }
    }

    private static float constrain(float amount, float low, float high) {
        return amount < low ? low : (amount > high ? high : amount);
    }

    private void computeProgressLocked(boolean forcePublish) {
        this.mProgress = PackageInstallerSession.constrain(this.mClientProgress * 0.8f, 0.0f, 0.8f) + PackageInstallerSession.constrain(this.mInternalProgress * 0.2f, 0.0f, 0.2f);
        if (forcePublish || (double)Math.abs(this.mProgress - this.mReportedProgress) >= 0.01) {
            this.mReportedProgress = this.mProgress;
            this.mCallback.onSessionProgressChanged(this, this.mProgress);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void addClientProgress(float progress) throws RemoteException {
        Object object = this.mLock;
        synchronized (object) {
            this.setClientProgress(this.mClientProgress + progress);
        }
    }

    @Override
    public String[] getNames() throws RemoteException {
        this.assertPreparedAndNotSealed("getNames");
        try {
            return this.resolveStageDir().list();
        }
        catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private File resolveStageDir() throws IOException {
        Object object = this.mLock;
        synchronized (object) {
            if (this.mResolvedStageDir == null && this.stageDir != null) {
                this.mResolvedStageDir = this.stageDir;
                if (!this.stageDir.exists()) {
                    this.stageDir.mkdirs();
                }
            }
            return this.mResolvedStageDir;
        }
    }

    @Override
    public ParcelFileDescriptor openWrite(String name, long offsetBytes, long lengthBytes) throws RemoteException {
        try {
            return this.openWriteInternal(name, offsetBytes, lengthBytes);
        }
        catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void assertPreparedAndNotSealed(String cookie) {
        Object object = this.mLock;
        synchronized (object) {
            if (!this.mPrepared) {
                throw new IllegalStateException(cookie + " before prepared");
            }
            if (this.mSealed) {
                throw new SecurityException(cookie + " not allowed after commit");
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private ParcelFileDescriptor openWriteInternal(String name, long offsetBytes, long lengthBytes) throws IOException {
        FileBridge bridge;
        Object object = this.mLock;
        synchronized (object) {
            this.assertPreparedAndNotSealed("openWrite");
            bridge = new FileBridge();
            this.mBridges.add(bridge);
        }
        try {
            File target = new File(this.resolveStageDir(), name);
            FileDescriptor targetFd = Os.open((String)target.getAbsolutePath(), (int)(OsConstants.O_CREAT | OsConstants.O_WRONLY), (int)420);
            if (lengthBytes > 0L) {
                Os.posix_fallocate((FileDescriptor)targetFd, (long)0L, (long)lengthBytes);
            }
            if (offsetBytes > 0L) {
                Os.lseek((FileDescriptor)targetFd, (long)offsetBytes, (int)OsConstants.SEEK_SET);
            }
            bridge.setTargetFile(targetFd);
            bridge.start();
            return ParcelFileDescriptor.dup((FileDescriptor)bridge.getClientSocket());
        }
        catch (ErrnoException e) {
            throw new IOException(e);
        }
    }

    @Override
    public ParcelFileDescriptor openRead(String name) throws RemoteException {
        try {
            return this.openReadInternal(name);
        }
        catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private ParcelFileDescriptor openReadInternal(String name) throws IOException {
        this.assertPreparedAndNotSealed("openRead");
        try {
            if (!FileUtils.isValidExtFilename(name)) {
                throw new IllegalArgumentException("Invalid name: " + name);
            }
            File target = new File(this.resolveStageDir(), name);
            FileDescriptor targetFd = Os.open((String)target.getAbsolutePath(), (int)OsConstants.O_RDONLY, (int)0);
            return ParcelFileDescriptor.dup((FileDescriptor)targetFd);
        }
        catch (ErrnoException e) {
            throw new IOException(e);
        }
    }

    @Override
    public void removeSplit(String splitName) throws RemoteException {
        if (TextUtils.isEmpty((CharSequence)this.params.appPackageName)) {
            throw new IllegalStateException("Must specify package name to remove a split");
        }
        try {
            this.createRemoveSplitMarker(splitName);
        }
        catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private void createRemoveSplitMarker(String splitName) throws IOException {
        try {
            String markerName = splitName + REMOVE_SPLIT_MARKER_EXTENSION;
            if (!FileUtils.isValidExtFilename(markerName)) {
                throw new IllegalArgumentException("Invalid marker: " + markerName);
            }
            File target = new File(this.resolveStageDir(), markerName);
            target.createNewFile();
            Os.chmod((String)target.getAbsolutePath(), (int)0);
        }
        catch (ErrnoException e) {
            throw new IOException(e);
        }
    }

    @Override
    public void close() throws RemoteException {
        if (this.mActiveCount.decrementAndGet() == 0) {
            this.mCallback.onSessionActiveChanged(this, false);
        }
    }

    @TargetApi(value=26)
    public void commit(IntentSender statusReceiver, boolean forTransfer) throws RemoteException {
        this.commit(statusReceiver);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void commit(IntentSender statusReceiver) throws RemoteException {
        boolean wasSealed;
        Object object = this.mLock;
        synchronized (object) {
            wasSealed = this.mSealed;
            if (!this.mSealed) {
                for (FileBridge bridge : this.mBridges) {
                    if (bridge.isClosed()) continue;
                    throw new SecurityException("Files still open");
                }
                this.mSealed = true;
            }
            this.mClientProgress = 1.0f;
            this.computeProgressLocked(true);
        }
        if (!wasSealed) {
            this.mCallback.onSessionSealedBlocking(this);
        }
        this.mActiveCount.incrementAndGet();
        VPackageInstallerService.PackageInstallObserverAdapter adapter = new VPackageInstallerService.PackageInstallObserverAdapter(this.mContext, statusReceiver, this.sessionId, this.userId);
        this.mHandler.obtainMessage(0, (Object)adapter.getBinder()).sendToTarget();
    }

    @Override
    public void abandon() throws RemoteException {
        this.destroyInternal();
        this.dispatchSessionFinished(-115, "Session was abandoned", null);
    }

    @Override
    public boolean isMultiPackage() throws RemoteException {
        return false;
    }

    @Override
    public DataLoaderParamsParcel getDataLoaderParams() throws RemoteException {
        return null;
    }

    @Override
    public void setChecksums(String name, Checksum[] checksums, byte[] signature) {
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void destroyInternal() {
        Object object = this.mLock;
        synchronized (object) {
            this.mSealed = true;
            this.mDestroyed = true;
            for (FileBridge bridge : this.mBridges) {
                bridge.forceClose();
            }
        }
        if (this.stageDir != null) {
            FileUtils.deleteDir(this.stageDir.getAbsolutePath());
        }
    }

    private void dispatchSessionFinished(int returnCode, String msg, Bundle extras) {
        this.mFinalStatus = returnCode;
        this.mFinalMessage = msg;
        if (this.mRemoteObserver != null) {
            try {
                this.mRemoteObserver.onPackageInstalled(this.mPackageName, returnCode, msg, extras);
            }
            catch (RemoteException remoteException) {
                // empty catch block
            }
        }
        boolean success = returnCode == 1;
        this.mCallback.onSessionFinished(this, success);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    void setPermissionsResult(boolean accepted) {
        if (!this.mSealed) {
            throw new SecurityException("Must be sealed to accept permissions");
        }
        if (accepted) {
            Object object = this.mLock;
            synchronized (object) {
                this.mPermissionsAccepted = true;
            }
            this.mHandler.obtainMessage(0).sendToTarget();
        } else {
            this.destroyInternal();
            this.dispatchSessionFinished(-115, "User rejected permissions", null);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void open() throws IOException {
        if (this.mActiveCount.getAndIncrement() == 0) {
            this.mCallback.onSessionActiveChanged(this, true);
        }
        Object object = this.mLock;
        synchronized (object) {
            if (!this.mPrepared) {
                if (this.stageDir == null) {
                    throw new IllegalArgumentException("Exactly one of stageDir or stageCid stage must be set");
                }
                this.mPrepared = true;
                this.mCallback.onSessionPrepared(this);
            }
        }
    }

    public static String getCompleteMessage(Throwable t) {
        StringBuilder builder = new StringBuilder();
        builder.append(t.getMessage());
        while ((t = t.getCause()) != null) {
            builder.append(": ").append(t.getMessage());
        }
        return builder.toString();
    }

    private class PackageManagerException
    extends Exception {
        public final int error;

        PackageManagerException(int error, String detailMessage) {
            super(detailMessage);
            this.error = error;
        }
    }
}

