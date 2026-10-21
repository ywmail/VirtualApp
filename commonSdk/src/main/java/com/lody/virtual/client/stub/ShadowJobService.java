/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.annotation.TargetApi
 *  android.app.Service
 *  android.app.job.JobParameters
 *  android.content.Context
 *  android.content.Intent
 *  android.os.IBinder
 */
package com.lody.virtual.client.stub;

import android.annotation.TargetApi;
import android.app.Service;
import android.app.job.IJobService;
import android.app.job.JobParameters;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.stub.ShadowJobWorkService;
import com.lody.virtual.helper.utils.VLog;

@TargetApi(value=21)
public class ShadowJobService
extends Service {
    private final IJobService mService = new IJobService.Stub(){

        @Override
        public void startJob(JobParameters jobParams) {
            ShadowJobWorkService.startJob((Context)ShadowJobService.this, jobParams);
        }

        @Override
        public void stopJob(JobParameters jobParams) {
            ShadowJobWorkService.stopJob((Context)ShadowJobService.this, jobParams);
        }
    };

    public void onCreate() {
        super.onCreate();
        VLog.e("ShadowJobService", "-> onCreate");
    }

    public IBinder onBind(Intent intent) {
        VLog.e("ShadowJobService", "-> onBind: " + intent);
        return this.mService.asBinder();
    }
}

