/*
 * Decompiled with CFR 0.152.
 */
package com.lody.virtual.client.hook.proxies.libcore;

import com.lody.virtual.StringFog;
import com.lody.virtual.client.NativeEngine;
import com.lody.virtual.client.VClient;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.hook.base.MethodProxy;
import com.lody.virtual.helper.utils.Reflect;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import mirror.libcore.io.Os;

class MethodProxies {
    MethodProxies() {
    }

    static class Stat
    extends MethodProxy {
        private static Field st_uid;

        Stat() {
        }

        @Override
        public Object afterCall(Object who, Method method, Object[] args, Object result) throws Throwable {
            int uid = (Integer)st_uid.get(result);
            if (uid == VirtualCore.get().myUid()) {
                st_uid.set(result, Stat.getBaseVUid());
            }
            return result;
        }

        @Override
        public String getMethodName() {
            return "stat";
        }

        static {
            try {
                Method stat = Os.TYPE.getMethod("stat", String.class);
                Class<?> StructStat = stat.getReturnType();
                st_uid = StructStat.getDeclaredField("st_uid");
                st_uid.setAccessible(true);
            }
            catch (Throwable e) {
                throw new IllegalStateException(e);
            }
        }
    }

    static class GetsockoptUcred
    extends MethodProxy {
        GetsockoptUcred() {
        }

        @Override
        public String getMethodName() {
            return "getsockoptUcred";
        }

        @Override
        public Object afterCall(Object who, Method method, Object[] args, Object result) throws Throwable {
            Reflect ucred;
            int uid;
            if (result != null && (uid = ((Integer)(ucred = Reflect.on(result)).get("uid")).intValue()) == VirtualCore.get().myUid()) {
                ucred.set("uid", GetsockoptUcred.getBaseVUid());
            }
            return result;
        }
    }

    static class GetUid
    extends MethodProxy {
        GetUid() {
        }

        @Override
        public String getMethodName() {
            return "getuid";
        }

        @Override
        public Object afterCall(Object who, Method method, Object[] args, Object result) throws Throwable {
            int uid = (Integer)result;
            return NativeEngine.onGetUid(uid);
        }
    }

    static class Getpwnam
    extends MethodProxy {
        Getpwnam() {
        }

        @Override
        public String getMethodName() {
            return "getpwnam";
        }

        @Override
        public Object afterCall(Object who, Method method, Object[] args, Object result) throws Throwable {
            Reflect pwd;
            int uid;
            if (result != null && (uid = ((Integer)(pwd = Reflect.on(result)).get("pw_uid")).intValue()) == VirtualCore.get().myUid()) {
                pwd.set("pw_uid", VClient.get().getVUid());
            }
            return result;
        }
    }

    static class Fstat
    extends Stat {
        Fstat() {
        }

        @Override
        public String getMethodName() {
            return "fstat";
        }

        @Override
        public Object afterCall(Object who, Method method, Object[] args, Object result) throws Throwable {
            Reflect pwd;
            int uid;
            if (result != null && (uid = ((Integer)(pwd = Reflect.on(result)).get("st_uid")).intValue()) == VirtualCore.get().myUid()) {
                pwd.set("st_uid", VClient.get().getVUid());
            }
            return result;
        }
    }

    static class Lstat
    extends Stat {
        Lstat() {
        }

        @Override
        public String getMethodName() {
            return "lstat";
        }

        @Override
        public Object afterCall(Object who, Method method, Object[] args, Object result) throws Throwable {
            Reflect pwd;
            int uid;
            if (result != null && (uid = ((Integer)(pwd = Reflect.on(result)).get("st_uid")).intValue()) == VirtualCore.get().myUid()) {
                pwd.set("st_uid", VClient.get().getVUid());
            }
            return result;
        }
    }
}

