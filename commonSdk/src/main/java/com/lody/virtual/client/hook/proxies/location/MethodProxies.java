/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Build$VERSION
 *  android.os.WorkSource
 */
package com.lody.virtual.client.hook.proxies.location;

import android.location.LocationRequest;
import android.os.Build;
import android.os.WorkSource;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.hook.annotations.SkipInject;
import com.lody.virtual.client.hook.base.MethodProxy;
import com.lody.virtual.client.hook.base.ReplaceFirstPkgMethodProxy;
import com.lody.virtual.client.hook.base.ReplaceLastPkgMethodProxy;
import com.lody.virtual.client.hook.base.ReplaceSequencePkgMethodProxy;
import com.lody.virtual.client.ipc.VLocationManager;
import com.lody.virtual.helper.compat.BuildCompat;
import com.lody.virtual.helper.utils.Reflect;
import com.lody.virtual.helper.utils.VLog;
import com.lody.virtual.remote.vloc.VLocation;
import java.lang.reflect.Method;
import java.util.Arrays;
import mirror.android.location.LocationRequestL;

class MethodProxies {
    MethodProxies() {
    }

    private static void fixLocationRequest(LocationRequest locationRequest) {
        if (locationRequest == null) {
            return;
        }
        if (LocationRequestL.mHideFromAppOps != null) {
            LocationRequestL.mHideFromAppOps.set(locationRequest, false);
        }
        if (!BuildCompat.isS() || LocationRequestL.mWorkSource == null) {
            if (LocationRequestL.mWorkSource != null) {
                LocationRequestL.mWorkSource.set(locationRequest, null);
                return;
            }
            return;
        }
        WorkSource workSource = (WorkSource)LocationRequestL.mWorkSource.get(locationRequest);
        if (workSource != null) {
            workSource.clear();
        }
    }

    private static class RegisterLocationListener
    extends ReplaceSequencePkgMethodProxy {
        public RegisterLocationListener() {
            super("registerLocationListener", 2);
        }

        @Override
        public Object call(Object obj, Method method, Object ... args) throws Throwable {
            VLog.d("VA-", "   registerLocationListener  ", new Object[0]);
            if (RegisterLocationListener.isFakeLocationEnable()) {
                VLocationManager.get().requestLocationUpdates(args);
                return 0;
            }
            if ("passive".equals(args[0])) {
                args[0] = "gps";
            }
            LocationRequest locationRequest = (LocationRequest)args[1];
            MethodProxies.fixLocationRequest(locationRequest);
            return super.call(obj, method, args);
        }
    }

    static class locationCallbackFinished
    extends MethodProxy {
        locationCallbackFinished() {
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            return super.call(who, method, args);
        }

        @Override
        public String getMethodName() {
            return "locationCallbackFinished";
        }
    }

    static class getProviderProperties
    extends MethodProxy {
        getProviderProperties() {
        }

        @Override
        public String getMethodName() {
            return "getProviderProperties";
        }

        @Override
        public Object afterCall(Object who, Method method, Object[] args, Object result) throws Throwable {
            if (getProviderProperties.isFakeLocationEnable()) {
                try {
                    Reflect.on(result).set("mRequiresNetwork", false);
                    Reflect.on(result).set("mRequiresCell", false);
                }
                catch (Throwable e) {
                    e.printStackTrace();
                }
                return result;
            }
            return super.afterCall(who, method, args, result);
        }
    }

    static class sendExtraCommand
    extends MethodProxy {
        sendExtraCommand() {
        }

        @Override
        public String getMethodName() {
            return "sendExtraCommand";
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            if (sendExtraCommand.isFakeLocationEnable()) {
                return true;
            }
            return super.call(who, method, args);
        }
    }

    @SkipInject
    static class RegisterGnssStatusCallback
    extends AddGpsStatusListener {
        public RegisterGnssStatusCallback() {
            super("registerGnssStatusCallback");
        }
    }

    @SkipInject
    static class UnregisterGnssStatusCallback
    extends RemoveGpsStatusListener {
        public UnregisterGnssStatusCallback() {
            super("unregisterGnssStatusCallback");
        }
    }

    @SkipInject
    static class RemoveUpdatesPI
    extends RemoveUpdates {
        public RemoveUpdatesPI() {
            super("removeUpdatesPI");
        }
    }

    @SkipInject
    static class RequestLocationUpdatesPI
    extends RequestLocationUpdates {
        public RequestLocationUpdatesPI() {
            super("requestLocationUpdatesPI");
        }
    }

    @SkipInject
    static class RemoveGpsStatusListener
    extends ReplaceLastPkgMethodProxy {
        public RemoveGpsStatusListener() {
            super("removeGpsStatusListener");
        }

        public RemoveGpsStatusListener(String name) {
            super(name);
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            if (RemoveGpsStatusListener.isFakeLocationEnable()) {
                VLocationManager.get().removeGpsStatusListener(args);
                return 0;
            }
            return super.call(who, method, args);
        }
    }

    @SkipInject
    static class GetBestProvider
    extends MethodProxy {
        GetBestProvider() {
        }

        @Override
        public String getMethodName() {
            return "getBestProvider";
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            if (GetBestProvider.isFakeLocationEnable()) {
                return "gps";
            }
            return super.call(who, method, args);
        }
    }

    private static class getAllProviders
    extends MethodProxy {
        private getAllProviders() {
        }

        @Override
        public String getMethodName() {
            return "getAllProviders";
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            if (getAllProviders.isFakeLocationEnable()) {
                return Arrays.asList("gps", "network");
            }
            return super.call(who, method, args);
        }
    }

    @SkipInject
    static class IsProviderEnabled
    extends MethodProxy {
        IsProviderEnabled() {
        }

        @Override
        public String getMethodName() {
            return "isProviderEnabled";
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            if (IsProviderEnabled.isFakeLocationEnable() && args[0] instanceof String) {
                return VLocationManager.get().isProviderEnabled((String)args[0]);
            }
            return super.call(who, method, args);
        }
    }

    @SkipInject
    static class GetLastKnownLocation
    extends GetLastLocation {
        GetLastKnownLocation() {
        }

        @Override
        public String getMethodName() {
            return "getLastKnownLocation";
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            if (GetLastKnownLocation.isFakeLocationEnable()) {
                VLocation loc = VLocationManager.get().getLocation(GetLastKnownLocation.getAppPkg(), GetLastKnownLocation.getAppUserId());
                if (loc != null) {
                    return loc.toSysLocation();
                }
                return null;
            }
            return super.call(who, method, args);
        }
    }

    @SkipInject
    static class GetLastLocation
    extends ReplaceLastPkgMethodProxy {
        public GetLastLocation() {
            super("getLastLocation");
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            if (!(args[0] instanceof String)) {
                LocationRequest request = (LocationRequest)args[0];
                MethodProxies.fixLocationRequest(request);
            }
            if (GetLastLocation.isFakeLocationEnable()) {
                VLocation loc = VLocationManager.get().getLocation(GetLastLocation.getAppPkg(), GetLastLocation.getAppUserId());
                if (loc != null) {
                    return loc.toSysLocation();
                }
                return null;
            }
            return super.call(who, method, args);
        }
    }

    static class RemoveUpdates
    extends ReplaceLastPkgMethodProxy {
        public RemoveUpdates() {
            super("removeUpdates");
        }

        public RemoveUpdates(String name) {
            super(name);
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            if (RemoveUpdates.isFakeLocationEnable()) {
                VLocationManager.get().removeUpdates(args);
                return 0;
            }
            return super.call(who, method, args);
        }
    }

    @SkipInject
    static class RequestLocationUpdates
    extends ReplaceFirstPkgMethodProxy {
        public RequestLocationUpdates() {
            super("requestLocationUpdates");
        }

        public RequestLocationUpdates(String name) {
            super(name);
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            VLog.d("VA-", "   requestLocationUpdates  ", new Object[0]);
            if (RequestLocationUpdates.isFakeLocationEnable()) {
                VLocationManager.get().requestLocationUpdates(args);
                return 0;
            }
            if (Build.VERSION.SDK_INT > 16) {
                LocationRequest request = (LocationRequest)args[0];
                MethodProxies.fixLocationRequest(request);
            }
            return super.call(who, method, args);
        }
    }

    @SkipInject
    static class AddGpsStatusListener
    extends ReplaceLastPkgMethodProxy {
        public AddGpsStatusListener() {
            super("addGpsStatusListener");
        }

        public AddGpsStatusListener(String name) {
            super(name);
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            if (AddGpsStatusListener.isFakeLocationEnable()) {
                VLocationManager.get().addGpsStatusListener(args);
                return true;
            }
            return super.call(who, method, args);
        }
    }
}

