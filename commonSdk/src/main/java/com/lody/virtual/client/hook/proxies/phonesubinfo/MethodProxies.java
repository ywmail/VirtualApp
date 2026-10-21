/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.text.TextUtils
 */
package com.lody.virtual.client.hook.proxies.phonesubinfo;

import android.text.TextUtils;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.hook.base.ReplaceLastPkgMethodProxy;
import com.lody.virtual.remote.VDeviceConfig;
import java.lang.reflect.Method;

class MethodProxies {
    MethodProxies() {
    }

    static class GetIccSerialNumberForSubscriber
    extends GetIccSerialNumber {
        GetIccSerialNumberForSubscriber() {
        }

        @Override
        public String getMethodName() {
            return "getIccSerialNumberForSubscriber";
        }
    }

    static class GetIccSerialNumber
    extends ReplaceLastPkgMethodProxy {
        public GetIccSerialNumber() {
            super("getIccSerialNumber");
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            String iccId;
            VDeviceConfig config = GetIccSerialNumber.getDeviceConfig();
            if (config.enable && !TextUtils.isEmpty((CharSequence)(iccId = GetIccSerialNumber.getDeviceConfig().iccId))) {
                return iccId;
            }
            try {
                return super.call(who, method, args);
            }
            catch (Throwable th) {
                return null;
            }
        }
    }

    static class GetImeiForSubscriber
    extends GetDeviceId {
        GetImeiForSubscriber() {
        }

        @Override
        public String getMethodName() {
            return "getImeiForSubscriber";
        }
    }

    static class GetDeviceIdForSubscriber
    extends GetDeviceId {
        GetDeviceIdForSubscriber() {
        }

        @Override
        public String getMethodName() {
            return "getDeviceIdForSubscriber";
        }
    }

    static class GetDeviceIdForPhone
    extends GetDeviceId {
        GetDeviceIdForPhone() {
        }

        @Override
        public String getMethodName() {
            return "getDeviceIdForPhone";
        }
    }

    static class GetDeviceId
    extends ReplaceLastPkgMethodProxy {
        public GetDeviceId() {
            super("getDeviceId");
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            String deviceId;
            VDeviceConfig config = GetDeviceId.getDeviceConfig();
            if (config.enable && !TextUtils.isEmpty((CharSequence)(deviceId = config.deviceId))) {
                return deviceId;
            }
            try {
                return super.call(who, method, args);
            }
            catch (Throwable th) {
                return null;
            }
        }
    }
}

