/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.annotation.TargetApi
 *  android.app.job.JobInfo
 *  android.app.job.JobWorkItem
 *  android.os.RemoteException
 */
package com.lody.virtual.client.ipc;

import android.annotation.TargetApi;
import android.app.job.JobInfo;
import android.app.job.JobWorkItem;
import android.os.RemoteException;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.VClient;
import com.lody.virtual.client.env.VirtualRuntime;
import com.lody.virtual.client.ipc.LocalProxyUtils;
import com.lody.virtual.client.ipc.ServiceManagerNative;
import com.lody.virtual.helper.compat.BuildCompat;
import com.lody.virtual.helper.utils.IInterfaceUtils;
import com.lody.virtual.remote.VJobWorkItem;
import com.lody.virtual.server.interfaces.IJobService;
import java.util.List;

public class VJobScheduler {
    private static final VJobScheduler sInstance = new VJobScheduler();
    private IJobService mService;

    public static VJobScheduler get() {
        return sInstance;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public IJobService getService() {
        if (this.mService == null || !IInterfaceUtils.isAlive(this.mService)) {
            VJobScheduler vJobScheduler = this;
            synchronized (vJobScheduler) {
                Object binder = this.getRemoteInterface();
                this.mService = LocalProxyUtils.genProxy(IJobService.class, binder);
            }
        }
        return this.mService;
    }

    private Object getRemoteInterface() {
        return IJobService.Stub.asInterface(ServiceManagerNative.getService("job"));
    }

    public int schedule(JobInfo job) {
        try {
            return this.getService().schedule(VClient.get().getVUid(), job);
        }
        catch (RemoteException e) {
            return (Integer)VirtualRuntime.crash(e);
        }
    }

    public List<JobInfo> getAllPendingJobs() {
        try {
            return this.getService().getAllPendingJobs(VClient.get().getVUid());
        }
        catch (RemoteException e) {
            return (List)VirtualRuntime.crash(e);
        }
    }

    public void cancelAll() {
        try {
            this.getService().cancelAll(VClient.get().getVUid());
        }
        catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void cancel(int jobId) {
        try {
            this.getService().cancel(VClient.get().getVUid(), jobId);
        }
        catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public JobInfo getPendingJob(int jobId) {
        try {
            return this.getService().getPendingJob(VClient.get().getVUid(), jobId);
        }
        catch (RemoteException e) {
            return (JobInfo)VirtualRuntime.crash(e);
        }
    }

    @TargetApi(value=26)
    public int enqueue(JobInfo job, JobWorkItem workItem) {
        if (workItem == null) {
            return -1;
        }
        if (BuildCompat.isOreo()) {
            try {
                return this.getService().enqueue(VClient.get().getVUid(), job, new VJobWorkItem(workItem));
            }
            catch (RemoteException e) {
                return (Integer)VirtualRuntime.crash(e);
            }
        }
        return -1;
    }
}

