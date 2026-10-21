/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.annotation.TargetApi
 *  android.app.job.JobInfo
 *  android.app.job.JobScheduler
 *  android.content.ComponentName
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 *  android.os.PersistableBundle
 *  android.text.TextUtils
 */
package com.lody.virtual.server.job;

import android.annotation.TargetApi;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.text.TextUtils;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.ipc.VJobScheduler;
import com.lody.virtual.client.stub.StubManifest;
import com.lody.virtual.helper.utils.Singleton;
import com.lody.virtual.os.VEnvironment;
import com.lody.virtual.remote.VJobWorkItem;
import com.lody.virtual.server.interfaces.IJobService;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import mirror.android.app.job.JobInfo;

@TargetApi(value=21)
public class VJobSchedulerService
extends IJobService.Stub {
    private static final String TAG = VJobScheduler.class.getSimpleName();
    private static final int JOB_FILE_VERSION = 1;
    private final Map<JobId, JobConfig> mJobStore = new HashMap<JobId, JobConfig>();
    private int mNextJobId = 1;
    private final JobScheduler mScheduler = (JobScheduler)VirtualCore.get().getContext().getSystemService("jobscheduler");
    private final ComponentName mJobProxyComponent = new ComponentName(VirtualCore.get().getHostPkg(), StubManifest.STUB_JOB);
    private static final Singleton<VJobSchedulerService> gDefault = new Singleton<VJobSchedulerService>(){

        @Override
        protected VJobSchedulerService create() {
            return new VJobSchedulerService();
        }
    };

    private VJobSchedulerService() {
        this.readJobs();
    }

    public static VJobSchedulerService get() {
        return gDefault.get();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public int schedule(int uid, android.app.job.JobInfo job) {
        JobConfig config;
        int id2 = job.getId();
        ComponentName service = job.getService();
        JobId jobId = new JobId(uid, service.getPackageName(), id2);
        Map<JobId, JobConfig> map = this.mJobStore;
        synchronized (map) {
            config = this.mJobStore.get(jobId);
            if (config == null) {
                int jid = this.mNextJobId++;
                config = new JobConfig(jid, service.getClassName(), job.getExtras());
                this.mJobStore.put(jobId, config);
            }
        }
        config.serviceName = service.getClassName();
        config.extras = job.getExtras();
        this.saveJobs();
        JobInfo.jobId.set(job, config.virtualJobId);
        JobInfo.service.set(job, this.mJobProxyComponent);
        return this.mScheduler.schedule(job);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void saveJobs() {
        File jobFile = VEnvironment.getJobConfigFile();
        Parcel p = Parcel.obtain();
        try {
            p.writeInt(1);
            p.writeInt(this.mJobStore.size());
            for (Map.Entry<JobId, JobConfig> entry : this.mJobStore.entrySet()) {
                entry.getKey().writeToParcel(p, 0);
                entry.getValue().writeToParcel(p, 0);
            }
            FileOutputStream fos = new FileOutputStream(jobFile);
            fos.write(p.marshall());
            fos.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            p.recycle();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void readJobs() {
        File jobFile = VEnvironment.getJobConfigFile();
        if (!jobFile.exists()) {
            return;
        }
        Parcel p = Parcel.obtain();
        try {
            FileInputStream fis = new FileInputStream(jobFile);
            byte[] bytes = new byte[(int)jobFile.length()];
            int len = fis.read(bytes);
            fis.close();
            if (len != bytes.length) {
                throw new IOException("Unable to read job config.");
            }
            p.unmarshall(bytes, 0, bytes.length);
            p.setDataPosition(0);
            int version = p.readInt();
            if (version != 1) {
                throw new IOException("Bad version of job file: " + version);
            }
            if (!this.mJobStore.isEmpty()) {
                this.mJobStore.clear();
            }
            int count = p.readInt();
            int max = 0;
            for (int i = 0; i < count; ++i) {
                JobId jobId = new JobId(p);
                JobConfig config = new JobConfig(p);
                this.mJobStore.put(jobId, config);
                max = Math.max(max, config.virtualJobId);
            }
            this.mNextJobId = max + 1;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            p.recycle();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void cancel(int uid, int jobId) {
        Map<JobId, JobConfig> map = this.mJobStore;
        synchronized (map) {
            boolean changed = false;
            Iterator<Map.Entry<JobId, JobConfig>> iterator = this.mJobStore.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<JobId, JobConfig> entry = iterator.next();
                JobId job = entry.getKey();
                JobConfig config = entry.getValue();
                if (uid != -1 && job.vuid != uid || job.clientJobId != jobId) continue;
                changed = true;
                this.mScheduler.cancel(config.virtualJobId);
                iterator.remove();
                break;
            }
            if (changed) {
                this.saveJobs();
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void cancelAll(int uid) {
        Map<JobId, JobConfig> map = this.mJobStore;
        synchronized (map) {
            boolean changed = false;
            Iterator<Map.Entry<JobId, JobConfig>> iterator = this.mJobStore.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<JobId, JobConfig> entry = iterator.next();
                JobId job = entry.getKey();
                if (job.vuid != uid) continue;
                JobConfig config = entry.getValue();
                this.mScheduler.cancel(config.virtualJobId);
                changed = true;
                iterator.remove();
                break;
            }
            if (changed) {
                this.saveJobs();
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public List<android.app.job.JobInfo> getAllPendingJobs(int uid) {
        List jobs = this.mScheduler.getAllPendingJobs();
        Map<JobId, JobConfig> map = this.mJobStore;
        synchronized (map) {
            ListIterator iterator = jobs.listIterator();
            while (iterator.hasNext()) {
                android.app.job.JobInfo job = (android.app.job.JobInfo)iterator.next();
                if (!StubManifest.STUB_JOB.equals(job.getService().getClassName())) {
                    iterator.remove();
                    continue;
                }
                Map.Entry<JobId, JobConfig> jobEntry = this.findJobByVirtualJobId(job.getId());
                if (jobEntry == null) {
                    iterator.remove();
                    continue;
                }
                JobId jobId = jobEntry.getKey();
                JobConfig config = jobEntry.getValue();
                if (jobId.vuid != uid) {
                    iterator.remove();
                    continue;
                }
                JobInfo.jobId.set(job, jobId.clientJobId);
                JobInfo.service.set(job, new ComponentName(jobId.packageName, config.serviceName));
            }
        }
        return jobs;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public Map.Entry<JobId, JobConfig> findJobByVirtualJobId(int virtualJobId) {
        Map<JobId, JobConfig> map = this.mJobStore;
        synchronized (map) {
            for (Map.Entry<JobId, JobConfig> entry : this.mJobStore.entrySet()) {
                if (entry.getValue().virtualJobId != virtualJobId) continue;
                return entry;
            }
            return null;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    @TargetApi(value=24)
    public android.app.job.JobInfo getPendingJob(int uid, int jobId) {
        android.app.job.JobInfo jobInfo = null;
        Map<JobId, JobConfig> map = this.mJobStore;
        synchronized (map) {
            for (Map.Entry<JobId, JobConfig> entry : this.mJobStore.entrySet()) {
                JobId job = entry.getKey();
                if (job.vuid != uid || job.clientJobId != jobId) continue;
                jobInfo = this.mScheduler.getPendingJob(job.clientJobId);
                break;
            }
        }
        return jobInfo;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    @TargetApi(value=26)
    public int enqueue(int uid, android.app.job.JobInfo job, VJobWorkItem workItem) {
        JobConfig config;
        if (workItem.get() == null) {
            return -1;
        }
        int id2 = job.getId();
        ComponentName service = job.getService();
        JobId jobId = new JobId(uid, service.getPackageName(), id2);
        Map<JobId, JobConfig> map = this.mJobStore;
        synchronized (map) {
            config = this.mJobStore.get(jobId);
            if (config == null) {
                int jid = this.mNextJobId++;
                config = new JobConfig(jid, service.getClassName(), job.getExtras());
                this.mJobStore.put(jobId, config);
            }
        }
        config.serviceName = service.getClassName();
        config.extras = job.getExtras();
        this.saveJobs();
        JobInfo.jobId.set(job, config.virtualJobId);
        JobInfo.service.set(job, this.mJobProxyComponent);
        return this.mScheduler.enqueue(job, workItem.get());
    }

    public static final class JobConfig
    implements Parcelable {
        public int virtualJobId;
        public String serviceName;
        public PersistableBundle extras;
        public static final Parcelable.Creator<JobConfig> CREATOR = new Parcelable.Creator<JobConfig>(){

            public JobConfig createFromParcel(Parcel source) {
                return new JobConfig(source);
            }

            public JobConfig[] newArray(int size) {
                return new JobConfig[size];
            }
        };

        JobConfig(int virtualJobId, String serviceName, PersistableBundle extra) {
            this.virtualJobId = virtualJobId;
            this.serviceName = serviceName;
            this.extras = extra;
        }

        JobConfig(Parcel in) {
            this.virtualJobId = in.readInt();
            this.serviceName = in.readString();
            this.extras = (PersistableBundle)in.readParcelable(PersistableBundle.class.getClassLoader());
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.virtualJobId);
            dest.writeString(this.serviceName);
            dest.writeParcelable((Parcelable)this.extras, flags);
        }
    }

    public static final class JobId
    implements Parcelable {
        public int vuid;
        public String packageName;
        public int clientJobId;
        public static final Parcelable.Creator<JobId> CREATOR = new Parcelable.Creator<JobId>(){

            public JobId createFromParcel(Parcel source) {
                return new JobId(source);
            }

            public JobId[] newArray(int size) {
                return new JobId[size];
            }
        };

        JobId(int vuid, String packageName, int id2) {
            this.vuid = vuid;
            this.packageName = packageName;
            this.clientJobId = id2;
        }

        JobId(Parcel in) {
            this.vuid = in.readInt();
            this.packageName = in.readString();
            this.clientJobId = in.readInt();
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || this.getClass() != o.getClass()) {
                return false;
            }
            JobId jobId = (JobId)o;
            return this.vuid == jobId.vuid && this.clientJobId == jobId.clientJobId && TextUtils.equals((CharSequence)this.packageName, (CharSequence)jobId.packageName);
        }

        public int hashCode() {
            int result = this.vuid;
            result = 31 * result + (this.packageName != null ? this.packageName.hashCode() : 0);
            result = 31 * result + this.clientJobId;
            return result;
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.vuid);
            dest.writeString(this.packageName);
            dest.writeInt(this.clientJobId);
        }
    }
}

