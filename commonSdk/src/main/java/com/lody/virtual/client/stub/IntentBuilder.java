/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.ComponentName
 *  android.content.Intent
 *  android.content.pm.ServiceInfo
 *  android.os.IBinder
 *  android.os.Process
 */
package com.lody.virtual.client.stub;

import android.app.IServiceConnection;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ServiceInfo;
import android.os.IBinder;
import android.os.Process;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.stub.StubManifest;
import com.lody.virtual.remote.ServiceData;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

public class IntentBuilder {
    private static final AtomicInteger sServiceBindCounter = new AtomicInteger(0);

    public static Intent createStartProxyServiceIntent(int vpid, boolean isExt, ServiceInfo serviceInfo, Intent service, int userId) {
        Intent intent = new Intent();
        String serviceName = StubManifest.getStubServiceName(vpid);
        intent.setClassName(StubManifest.getStubPackageName(isExt), serviceName);
        ServiceData.ServiceStartData data = new ServiceData.ServiceStartData(new ComponentName(serviceInfo.packageName, serviceInfo.name), serviceInfo, service, userId);
        data.saveToIntent(intent);
        return intent;
    }

    public static Intent createStopProxyServiceIntent(int vpid, boolean isExt, ComponentName component, int userId, int startId, IBinder token) {
        Intent intent = new Intent();
        String serviceName = StubManifest.getStubServiceName(vpid);
        intent.setClassName(StubManifest.getStubPackageName(isExt), serviceName);
        ServiceData.ServiceStopData data = new ServiceData.ServiceStopData(userId, component, startId, token);
        data.saveToIntent(intent);
        return intent;
    }

    public static Intent createBindProxyServiceIntent(int vpid, boolean isExt, ServiceInfo serviceInfo, Intent service, int flags, int userId, IServiceConnection connection) {
        Intent intent = new Intent();
        String serviceName = StubManifest.getStubServiceName(vpid);
        intent.setClassName(StubManifest.getStubPackageName(isExt), serviceName);
        intent.setType(String.format(Locale.ENGLISH, "bind_service_%d_%d_%s|%s", Process.myPid(), sServiceBindCounter.getAndIncrement(), serviceInfo.packageName, serviceInfo.name));
        ServiceData.ServiceBindData data = new ServiceData.ServiceBindData(new ComponentName(serviceInfo.packageName, serviceInfo.name), serviceInfo, service, flags, userId, connection);
        data.saveToIntent(intent);
        return intent;
    }
}

