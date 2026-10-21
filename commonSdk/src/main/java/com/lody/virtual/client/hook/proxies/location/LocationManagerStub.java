/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.location.LocationManager
 *  android.os.Binder
 *  android.os.Build$VERSION
 *  android.os.IBinder
 *  android.os.IInterface
 *  android.text.TextUtils
 */
package com.lody.virtual.client.hook.proxies.location;

import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.IInterface;
import android.text.TextUtils;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.hook.annotations.Inject;
import com.lody.virtual.client.hook.base.BinderInvocationStub;
import com.lody.virtual.client.hook.base.MethodInvocationProxy;
import com.lody.virtual.client.hook.base.ReplaceCallingPkgMethodProxy;
import com.lody.virtual.client.hook.base.ReplaceFirstPkgMethodProxy;
import com.lody.virtual.client.hook.base.ReplaceLastPkgMethodProxy;
import com.lody.virtual.client.hook.base.ReplaceLastUserIdMethodProxy;
import com.lody.virtual.client.hook.base.ReplaceSequencePkgMethodProxy;
import com.lody.virtual.client.hook.base.ResultStaticMethodProxy;
import com.lody.virtual.client.hook.base.StaticMethodProxy;
import com.lody.virtual.client.hook.proxies.location.MethodProxies;
import com.lody.virtual.helper.compat.BuildCompat;
import com.lody.virtual.helper.utils.ArrayUtils;
import com.lody.virtual.helper.utils.Reflect;
import com.lody.virtual.helper.utils.ReflectException;
import java.lang.reflect.Method;
import mirror.android.location.GeocoderParams;
import mirror.android.location.GpsStatus;
import mirror.android.location.ILocationManager;
import mirror.android.location.LocationManager;
import mirror.android.os.ServiceManager;

@Inject(value=MethodProxies.class)
public class LocationManagerStub
extends MethodInvocationProxy<BinderInvocationStub> {
    public LocationManagerStub() {
        super(new BinderInvocationStub(LocationManagerStub.getInterface()));
    }

    private static IInterface getInterface() {
        IBinder base = ServiceManager.getService.call("location");
        if (base instanceof Binder) {
            try {
                return (IInterface)Reflect.on(base).get("mILocationManager");
            }
            catch (ReflectException e) {
                e.printStackTrace();
            }
        }
        return ILocationManager.Stub.asInterface.call(base);
    }

    @Override
    public void inject() {
        android.location.LocationManager locationManager = (android.location.LocationManager)this.getContext().getSystemService("location");
        IInterface base = LocationManager.mService.get(locationManager);
        if (base instanceof Binder) {
            Reflect.on(base).set("mILocationManager", ((BinderInvocationStub)this.getInvocationStub()).getProxyInterface());
        }
        LocationManager.mService.set(locationManager, (IInterface)((BinderInvocationStub)this.getInvocationStub()).getProxyInterface());
        ((BinderInvocationStub)this.getInvocationStub()).replaceService("location");
    }

    @Override
    public boolean isEnvBad() {
        return false;
    }

    @Override
    protected void onBindMethods() {
        super.onBindMethods();
        if (Build.VERSION.SDK_INT >= 23) {
            this.addMethodProxy(new ReplaceLastPkgMethodProxy("addTestProvider"));
            this.addMethodProxy(new ReplaceLastPkgMethodProxy("removeTestProvider"));
            this.addMethodProxy(new ReplaceLastPkgMethodProxy("setTestProviderLocation"));
            this.addMethodProxy(new ReplaceLastPkgMethodProxy("clearTestProviderLocation"));
            this.addMethodProxy(new ReplaceLastPkgMethodProxy("setTestProviderEnabled"));
            this.addMethodProxy(new ReplaceLastPkgMethodProxy("clearTestProviderEnabled"));
            this.addMethodProxy(new ReplaceLastPkgMethodProxy("setTestProviderStatus"));
            this.addMethodProxy(new ReplaceLastPkgMethodProxy("clearTestProviderStatus"));
        }
        if (Build.VERSION.SDK_INT >= 21) {
            this.addMethodProxy(new FakeReplaceLastPkgMethodProxy("addGpsMeasurementListener", true));
            this.addMethodProxy(new FakeReplaceLastPkgMethodProxy("addGpsNavigationMessageListener", true));
            this.addMethodProxy(new FakeReplaceLastPkgMethodProxy("removeGpsMeasurementListener", 0));
            this.addMethodProxy(new FakeReplaceLastPkgMethodProxy("removeGpsNavigationMessageListener", 0));
        }
        if (Build.VERSION.SDK_INT >= 17) {
            this.addMethodProxy(new FakeReplaceLastPkgMethodProxy("requestGeofence", 0));
            this.addMethodProxy(new FakeReplaceLastPkgMethodProxy("removeGeofence", 0));
        }
        if (Build.VERSION.SDK_INT <= 16) {
            this.addMethodProxy(new MethodProxies.GetLastKnownLocation());
            this.addMethodProxy(new FakeReplaceLastPkgMethodProxy("addProximityAlert", 0));
        }
        if (Build.VERSION.SDK_INT <= 16) {
            this.addMethodProxy(new MethodProxies.RequestLocationUpdatesPI());
            this.addMethodProxy(new MethodProxies.RemoveUpdatesPI());
        }
        if (Build.VERSION.SDK_INT >= 16) {
            this.addMethodProxy(new MethodProxies.RequestLocationUpdates());
            this.addMethodProxy(new MethodProxies.RemoveUpdates());
        }
        this.addMethodProxy(new MethodProxies.IsProviderEnabled());
        this.addMethodProxy(new MethodProxies.GetBestProvider());
        if (Build.VERSION.SDK_INT >= 17) {
            this.addMethodProxy(new MethodProxies.GetLastLocation());
            this.addMethodProxy(new MethodProxies.AddGpsStatusListener());
            this.addMethodProxy(new MethodProxies.RemoveGpsStatusListener());
            this.addMethodProxy(new FakeReplaceLastPkgMethodProxy("addNmeaListener", 0));
            this.addMethodProxy(new FakeReplaceLastPkgMethodProxy("removeNmeaListener", 0));
        }
        if (Build.VERSION.SDK_INT >= 24) {
            if (BuildCompat.isS()) {
                this.addMethodProxy(new ReplaceFirstPkgMethodProxy("registerGnssStatusCallback"));
            } else {
                this.addMethodProxy(new MethodProxies.RegisterGnssStatusCallback());
            }
            this.addMethodProxy(new MethodProxies.UnregisterGnssStatusCallback());
        }
        this.addMethodProxy(new ReplaceLastUserIdMethodProxy("isProviderEnabledForUser"));
        this.addMethodProxy(new ReplaceLastUserIdMethodProxy("isLocationEnabledForUser"));
        if (BuildCompat.isQ()) {
            this.addMethodProxy(new StaticMethodProxy("setLocationControllerExtraPackageEnabled"){

                @Override
                public Object call(Object who, Method method, Object ... args) throws Throwable {
                    return null;
                }
            });
            this.addMethodProxy(new StaticMethodProxy("setExtraLocationControllerPackageEnabled"){

                @Override
                public Object call(Object who, Method method, Object ... args) throws Throwable {
                    return null;
                }
            });
            this.addMethodProxy(new StaticMethodProxy("setExtraLocationControllerPackage"){

                @Override
                public Object call(Object who, Method method, Object ... args) throws Throwable {
                    return null;
                }
            });
        }
        if (BuildCompat.isR()) {
            this.addMethodProxy(new ResultStaticMethodProxy("setLocationEnabledForUser", null));
        }
        if (BuildCompat.isS()) {
            this.addMethodProxy(new ReplaceSequencePkgMethodProxy("registerLocationPendingIntent", 2));
            this.addMethodProxy(new ReplaceSequencePkgMethodProxy("registerLocationPendingIntent", 2));
            this.addMethodProxy(new ReplaceCallingPkgMethodProxy("registerGnssNmeaCallback"));
            this.addMethodProxy(new GetFromLocation("getFromLocationName"));
            this.addMethodProxy(new GetFromLocation("getFromLocation"));
        }
    }

    private static class GetFromLocation
    extends StaticMethodProxy {
        public GetFromLocation(String str) {
            super(str);
        }

        @Override
        public Object call(Object obj, Method method, Object ... args) throws Throwable {
            int index = ArrayUtils.indexOf(args, GpsStatus.TYPE, 0);
            if (index >= 0) {
                int mUid;
                String hostPkg = VirtualCore.get().getHostPkg();
                String mPackageName = GeocoderParams.mPackageName(args[index]);
                if (mPackageName != null && !TextUtils.equals((CharSequence)hostPkg, (CharSequence)mPackageName)) {
                    GeocoderParams.mPackageName(args[index], hostPkg);
                }
                if ((mUid = GeocoderParams.mUid(args[index])) > 0 && mUid != VirtualCore.get().myUid()) {
                    GeocoderParams.mUid(args[index], VirtualCore.get().myUid());
                }
            }
            return super.call(obj, method, args);
        }
    }

    private static class FakeReplaceLastPkgMethodProxy
    extends ReplaceLastPkgMethodProxy {
        private Object mDefValue;

        private FakeReplaceLastPkgMethodProxy(String name, Object def) {
            super(name);
            this.mDefValue = def;
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            if (FakeReplaceLastPkgMethodProxy.isFakeLocationEnable()) {
                return this.mDefValue;
            }
            return super.call(who, method, args);
        }
    }
}

