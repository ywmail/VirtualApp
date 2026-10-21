/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.AlarmManager
 *  android.os.Build$VERSION
 *  android.os.IInterface
 *  android.os.WorkSource
 */
package com.lody.virtual.client.hook.proxies.alarm;

import android.app.AlarmManager;
import android.os.Build;
import android.os.IInterface;
import android.os.WorkSource;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.BinderInvocationStub;
import com.lody.virtual.client.hook.base.MethodProxy;
import com.lody.virtual.client.hook.base.ReplaceCallingPkgAndLastUserIdMethodProxy;
import com.lody.virtual.client.hook.base.ReplaceFirstPkgMethodProxy;
import com.lody.virtual.helper.utils.ArrayUtils;
import java.lang.reflect.Method;
import mirror.android.app.IAlarmManager;

public class AlarmManagerStub
extends BinderInvocationProxy {
    public AlarmManagerStub() {
        super(IAlarmManager.Stub.asInterface, "alarm");
    }

    @Override
    public void inject() throws Throwable {
        super.inject();
        AlarmManager alarmManager = (AlarmManager)VirtualCore.get().getContext().getSystemService("alarm");
        if (mirror.android.app.AlarmManager.mService != null) {
            try {
                mirror.android.app.AlarmManager.mService.set(alarmManager, (IInterface)((BinderInvocationStub)this.getInvocationStub()).getProxyInterface());
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onBindMethods() {
        super.onBindMethods();
        this.addMethodProxy(new Set());
        this.addMethodProxy(new SetTime());
        this.addMethodProxy(new SetTimeZone());
        this.addMethodProxy(new ReplaceFirstPkgMethodProxy("canScheduleExactAlarms"));
        this.addMethodProxy(new ReplaceCallingPkgAndLastUserIdMethodProxy("hasScheduleExactAlarm"));
    }

    private static class GetNextAlarmClock
    extends MethodProxy {
        private GetNextAlarmClock() {
        }

        @Override
        public String getMethodName() {
            return "getNextAlarmClock";
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            GetNextAlarmClock.replaceLastUserId(args);
            return super.call(who, method, args);
        }
    }

    private static class Set
    extends MethodProxy {
        private Set() {
        }

        @Override
        public String getMethodName() {
            return "set";
        }

        @Override
        public boolean beforeCall(Object who, Method method, Object ... args) {
            int index;
            if (Build.VERSION.SDK_INT >= 24 && args[0] instanceof String) {
                args[0] = Set.getHostPkg();
            }
            if ((index = ArrayUtils.indexOfFirst(args, WorkSource.class)) >= 0) {
                args[index] = null;
            }
            return true;
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            try {
                return super.call(who, method, args);
            }
            catch (Throwable e) {
                e.printStackTrace();
                return 0;
            }
        }
    }

    private static class SetTime
    extends MethodProxy {
        private SetTime() {
        }

        @Override
        public String getMethodName() {
            return "setTime";
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            if (Build.VERSION.SDK_INT >= 21) {
                return false;
            }
            return null;
        }
    }

    private static class SetTimeZone
    extends MethodProxy {
        private SetTimeZone() {
        }

        @Override
        public String getMethodName() {
            return "setTimeZone";
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            return null;
        }
    }
}

