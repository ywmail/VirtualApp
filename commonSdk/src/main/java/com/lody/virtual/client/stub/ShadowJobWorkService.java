/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.annotation.TargetApi
 *  android.app.Service
 *  android.app.job.JobParameters
 *  android.app.job.JobScheduler
 *  android.app.job.JobWorkItem
 *  android.content.ComponentName
 *  android.content.Context
 *  android.content.Intent
 *  android.content.ServiceConnection
 *  android.os.IBinder
 *  android.os.Parcelable
 *  android.os.RemoteException
 *  android.util.Log
 */
package com.lody.virtual.client.stub;

import android.annotation.TargetApi;
import android.app.Service;
import android.app.job.IJobCallback;
import android.app.job.IJobService;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobWorkItem;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Parcelable;
import android.os.RemoteException;
import android.util.Log;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.core.InvocationStubManager;
import com.lody.virtual.client.hook.proxies.am.ActivityManagerStub;
import com.lody.virtual.client.ipc.VActivityManager;
import com.lody.virtual.helper.collection.SparseArray;
import com.lody.virtual.helper.compat.JobWorkItemCompat;
import com.lody.virtual.helper.utils.VLog;
import com.lody.virtual.os.VUserHandle;
import com.lody.virtual.server.job.VJobSchedulerService;
import java.util.Map;

@TargetApi(value=21)
public class ShadowJobWorkService
extends Service {
    private static final boolean debug = true;
    private static final String TAG = ShadowJobWorkService.class.getSimpleName();
    private final SparseArray<JobSession> mJobSessions = new SparseArray();
    private JobScheduler mScheduler;

    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String action = intent.getAction();
            if ("action.startJob".equals(action)) {
                JobParameters jobParams = (JobParameters)intent.getParcelableExtra("jobParams");
                this.startJob(jobParams);
            } else if ("action.stopJob".equals(action)) {
                JobParameters jobParams = (JobParameters)intent.getParcelableExtra("jobParams");
                this.stopJob(jobParams);
            }
        }
        return 2;
    }

    public static void startJob(Context context, JobParameters jobParams) {
        Intent intent = new Intent(context, ShadowJobWorkService.class);
        intent.setAction("action.startJob");
        intent.putExtra("jobParams", (Parcelable)jobParams);
        context.startService(intent);
    }

    public static void stopJob(Context context, JobParameters jobParams) {
        Intent intent = new Intent(context, ShadowJobWorkService.class);
        intent.setAction("action.stopJob");
        intent.putExtra("jobParams", (Parcelable)jobParams);
        context.startService(intent);
    }

    private void emptyCallback(IJobCallback callback, int jobId) {
        try {
            callback.acknowledgeStartMessage(jobId, false);
            callback.jobFinished(jobId, false);
        }
        catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void onCreate() {
        super.onCreate();
        InvocationStubManager.getInstance().checkEnv(ActivityManagerStub.class);
        this.mScheduler = (JobScheduler)this.getSystemService("jobscheduler");
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void onDestroy() {
        VLog.i(TAG, "ShadowJobService:onDestroy", new Object[0]);
        SparseArray<JobSession> sparseArray = this.mJobSessions;
        synchronized (sparseArray) {
            for (int i = this.mJobSessions.size() - 1; i >= 0; --i) {
                JobSession session = this.mJobSessions.valueAt(i);
                session.stopSessionLocked();
            }
            this.mJobSessions.clear();
        }
        super.onDestroy();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void startJob(JobParameters jobParams) {
        int jobId = jobParams.getJobId();
        IBinder binder = mirror.android.app.job.JobParameters.callback.get(jobParams);
        IJobCallback callback = IJobCallback.Stub.asInterface(binder);
        Map.Entry<VJobSchedulerService.JobId, VJobSchedulerService.JobConfig> entry = VJobSchedulerService.get().findJobByVirtualJobId(jobId);
        if (entry == null) {
            this.emptyCallback(callback, jobId);
            this.mScheduler.cancel(jobId);
        } else {
            JobSession session;
            VJobSchedulerService.JobId key = entry.getKey();
            VJobSchedulerService.JobConfig config = entry.getValue();
            SparseArray<JobSession> sparseArray = this.mJobSessions;
            synchronized (sparseArray) {
                session = this.mJobSessions.get(jobId);
            }
            if (session != null) {
                session.startJob(true);
            } else {
                boolean bound = false;
                SparseArray<JobSession> sparseArray2 = this.mJobSessions;
                synchronized (sparseArray2) {
                    mirror.android.app.job.JobParameters.jobId.set(jobParams, key.clientJobId);
                    session = new JobSession(jobId, callback, jobParams, key.packageName);
                    mirror.android.app.job.JobParameters.callback.set(jobParams, session.asBinder());
                    this.mJobSessions.put(jobId, session);
                    Intent service = new Intent();
                    service.setComponent(new ComponentName(key.packageName, config.serviceName));
                    try {
                        VLog.i(TAG, "ShadowJobService:binService:%s, jobId=%s", service.getComponent(), jobId);
                        bound = VActivityManager.get().bindService((Context)this, service, session, 5, VUserHandle.getUserId(key.vuid));
                    }
                    catch (Throwable e) {
                        VLog.e(TAG, e);
                    }
                }
                if (!bound) {
                    sparseArray2 = this.mJobSessions;
                    synchronized (sparseArray2) {
                        this.mJobSessions.remove(jobId);
                    }
                    this.emptyCallback(callback, jobId);
                    this.mScheduler.cancel(jobId);
                    VJobSchedulerService.get().cancel(-1, jobId);
                }
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void stopJob(JobParameters jobParams) {
        int jobId = jobParams.getJobId();
        SparseArray<JobSession> sparseArray = this.mJobSessions;
        synchronized (sparseArray) {
            JobSession session = this.mJobSessions.get(jobId);
            if (session != null) {
                VLog.i(TAG, "stopJob:%d", jobId);
                session.stopSessionLocked();
            }
        }
    }

    private final class JobSession
    extends IJobCallback.Stub
    implements ServiceConnection {
        private int jobId;
        private IJobCallback clientCallback;
        private JobParameters jobParams;
        private IJobService clientJobService;
        private boolean isWorking;
        private String packageName;
        private JobWorkItem lastWorkItem;

        JobSession(int jobId, IJobCallback clientCallback, JobParameters jobParams, String packageName) {
            this.jobId = jobId;
            this.clientCallback = clientCallback;
            this.jobParams = jobParams;
            this.packageName = packageName;
        }

        @Override
        public void acknowledgeStartMessage(int jobId, boolean ongoing) throws RemoteException {
            this.isWorking = true;
            VLog.i(TAG, "ShadowJobService:acknowledgeStartMessage:%d", this.jobId);
            this.clientCallback.acknowledgeStartMessage(this.jobId, ongoing);
        }

        @Override
        public void acknowledgeStopMessage(int jobId, boolean reschedule) throws RemoteException {
            this.isWorking = false;
            VLog.i(TAG, "ShadowJobService:acknowledgeStopMessage:%d", this.jobId);
            this.clientCallback.acknowledgeStopMessage(this.jobId, reschedule);
        }

        @Override
        public void jobFinished(int jobId, boolean reschedule) throws RemoteException {
            this.isWorking = false;
            VLog.i(TAG, "ShadowJobService:jobFinished:%d", this.jobId);
            this.clientCallback.jobFinished(this.jobId, reschedule);
        }

        @Override
        public boolean completeWork(int jobId, int workId) throws RemoteException {
            VLog.i(TAG, "ShadowJobService:completeWork:%d", this.jobId);
            return this.clientCallback.completeWork(this.jobId, workId);
        }

        @Override
        public JobWorkItem dequeueWork(int jobId) throws RemoteException {
            try {
                this.lastWorkItem = null;
                VLog.i(TAG, "ShadowJobService:dequeueWork:%d", this.jobId);
                JobWorkItem workItem = this.clientCallback.dequeueWork(this.jobId);
                if (workItem != null) {
                    this.lastWorkItem = JobWorkItemCompat.parse(workItem);
                    return this.lastWorkItem;
                }
                this.isWorking = false;
                this.stopSessionLocked();
            }
            catch (Exception e) {
                e.printStackTrace();
                VLog.i(TAG, "ShadowJobService:dequeueWork:" + e, new Object[0]);
            }
            return null;
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        public void startJob(boolean wait) {
            if (this.isWorking) {
                VLog.w(TAG, "ShadowJobService:startJob:%d,but is working", this.jobId);
                return;
            }
            VLog.i(TAG, "ShadowJobService:startJob:%d", this.jobId);
            if (this.clientJobService == null) {
                if (!wait) {
                    ShadowJobWorkService.this.emptyCallback(this.clientCallback, this.jobId);
                    SparseArray sparseArray = ShadowJobWorkService.this.mJobSessions;
                    synchronized (sparseArray) {
                        this.stopSessionLocked();
                    }
                }
                return;
            }
            try {
                this.clientJobService.startJob(this.jobParams);
            }
            catch (RemoteException e) {
                this.forceFinishJob();
                Log.e((String)TAG, (String)"ShadowJobService:startJob", (Throwable)e);
            }
        }

        public void onServiceConnected(ComponentName name, IBinder service) {
            VLog.i(TAG, "ShadowJobService:onServiceConnected:%s", name);
            this.clientJobService = IJobService.Stub.asInterface(service);
            this.startJob(false);
        }

        public void onServiceDisconnected(ComponentName name) {
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        void forceFinishJob() {
            try {
                this.clientCallback.jobFinished(this.jobId, false);
            }
            catch (RemoteException e) {
                e.printStackTrace();
            }
            finally {
                SparseArray sparseArray = ShadowJobWorkService.this.mJobSessions;
                synchronized (sparseArray) {
                    this.stopSessionLocked();
                }
            }
        }

        void stopSessionLocked() {
            VLog.i(TAG, "ShadowJobService:stopSession:%d", this.jobId);
            if (this.clientJobService != null) {
                try {
                    this.clientJobService.stopJob(this.jobParams);
                }
                catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            ShadowJobWorkService.this.mJobSessions.remove(this.jobId);
            VActivityManager.get().unbindService((Context)ShadowJobWorkService.this, this);
        }
    }
}

