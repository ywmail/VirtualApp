/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.annotation.TargetApi
 *  android.app.job.JobWorkItem
 *  android.content.Intent
 */
package mirror.android.app.job;

import android.annotation.TargetApi;
import android.content.Intent;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefConstructor;
import mirror.RefInt;
import mirror.RefMethod;
import mirror.RefObject;

@TargetApi(value=26)
public class JobWorkItem {
    public static Class<?> TYPE = RefClass.load(JobWorkItem.class, android.app.job.JobWorkItem.class);
    @MethodParams(value={Intent.class})
    public static RefConstructor<Object> ctor;
    public static RefMethod<Intent> getIntent;
    public static RefInt mWorkId;
    public static RefObject<Object> mGrants;
    public static RefInt mDeliveryCount;
}

