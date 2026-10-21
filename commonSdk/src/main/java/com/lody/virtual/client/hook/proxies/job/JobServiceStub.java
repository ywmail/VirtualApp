/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.annotation.TargetApi
 *  android.app.job.JobInfo
 *  android.app.job.JobWorkItem
 *  android.os.Build$VERSION
 */
package com.lody.virtual.client.hook.proxies.job;

import android.annotation.TargetApi;
import android.app.job.JobInfo;
import android.app.job.JobWorkItem;
import android.os.Build;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.MethodProxy;
import com.lody.virtual.client.hook.utils.MethodParameterUtils;
import com.lody.virtual.client.ipc.VJobScheduler;
import com.lody.virtual.helper.compat.BuildCompat;
import com.lody.virtual.helper.compat.JobWorkItemCompat;
import com.lody.virtual.os.VUserHandle;
import java.lang.reflect.Method;
import java.util.List;
import mirror.android.app.job.IJobScheduler;
import mirror.android.content.pm.ParceledListSlice;

@TargetApi(value=21)
public class JobServiceStub
extends BinderInvocationProxy {
    public JobServiceStub() {
        super(IJobScheduler.Stub.asInterface, "jobscheduler");
    }

    @Override
    protected void onBindMethods() {
        super.onBindMethods();
        this.addMethodProxy(new schedule());
        this.addMethodProxy(new getAllPendingJobs());
        this.addMethodProxy(new cancelAll());
        this.addMethodProxy(new cancel());
        if (Build.VERSION.SDK_INT >= 24) {
            this.addMethodProxy(new getPendingJob());
        }
        if (Build.VERSION.SDK_INT >= 26) {
            this.addMethodProxy(new enqueue());
        }
    }

    @TargetApi(value=26)
    private class enqueue
    extends MethodProxy {
        private enqueue() {
        }

        @Override
        public String getMethodName() {
            return "enqueue";
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            int jobInfoIndex = 0;
            int jobWorkIntemIndex = 1;
            if (args.length > 2) {
                jobInfoIndex = 1;
                jobWorkIntemIndex = 2;
            }
            JobInfo jobInfo = (JobInfo)args[jobInfoIndex];
            JobWorkItem workItem = JobWorkItemCompat.redirect(VUserHandle.myUserId(), (JobWorkItem)args[jobWorkIntemIndex], enqueue.getAppPkg());
            return VJobScheduler.get().enqueue(jobInfo, workItem);
        }
    }

    private class getPendingJob
    extends MethodProxy {
        private getPendingJob() {
        }

        @Override
        public String getMethodName() {
            return "getPendingJob";
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            int jobId = (Integer)args[0];
            return VJobScheduler.get().getPendingJob(jobId);
        }
    }

    private class cancel
    extends MethodProxy {
        private cancel() {
        }

        @Override
        public String getMethodName() {
            return "cancel";
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            int jobId = (Integer)args[0];
            VJobScheduler.get().cancel(jobId);
            return 0;
        }
    }

    private class cancelAll
    extends MethodProxy {
        private cancelAll() {
        }

        @Override
        public String getMethodName() {
            return "cancelAll";
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            VJobScheduler.get().cancelAll();
            return 0;
        }
    }

    private class getAllPendingJobs
    extends MethodProxy {
        private getAllPendingJobs() {
        }

        @Override
        public String getMethodName() {
            return "getAllPendingJobs";
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            List<JobInfo> res = VJobScheduler.get().getAllPendingJobs();
            if (res == null) {
                return null;
            }
            if (BuildCompat.isQ()) {
                return ParceledListSlice.ctorQ.newInstance(res);
            }
            return res;
        }
    }

    private class schedule
    extends MethodProxy {
        private schedule() {
        }

        @Override
        public String getMethodName() {
            return "schedule";
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            JobInfo jobInfo = MethodParameterUtils.getFirstParam(args, JobInfo.class);
            return VJobScheduler.get().schedule(jobInfo);
        }
    }
}

