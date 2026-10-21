/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.ContentValues
 *  android.database.Cursor
 *  android.net.Uri
 *  android.os.Bundle
 *  android.os.IInterface
 */
package com.lody.virtual.client.hook.providers;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.IInterface;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.hook.base.MethodBox;
import com.lody.virtual.client.hook.providers.BadgeProviderHook;
import com.lody.virtual.client.hook.providers.DownloadProviderHook;
import com.lody.virtual.client.hook.providers.ExternalProviderHook;
import com.lody.virtual.client.hook.providers.InternalProviderHook;
import com.lody.virtual.client.hook.providers.SettingsProviderHook;
import com.lody.virtual.client.hook.secondary.ProxyBinder;
import com.lody.virtual.client.hook.utils.MethodParameterUtils;
import com.lody.virtual.helper.compat.BuildCompat;
import com.lody.virtual.helper.utils.VLog;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import mirror.android.content.IContentProvider;

public class ProviderHook
implements InvocationHandler {
    private static final Map<String, HookFetcher> PROVIDER_MAP;
    public static final String QUERY_ARG_SQL_SELECTION;
    public static final String QUERY_ARG_SQL_SELECTION_ARGS;
    public static final String QUERY_ARG_SQL_SORT_ORDER;
    protected final IInterface mBase;
    protected IInterface mProxy;
    protected ProxyBinder mProxyBinder;

    public ProviderHook(IInterface base) {
        this.mBase = base;
        this.mProxy = (IInterface)Proxy.newProxyInstance(IContentProvider.TYPE.getClassLoader(), new Class[]{IContentProvider.TYPE}, this);
        this.mProxyBinder = new ProxyBinder(this.mBase.asBinder(), this.mProxy);
    }

    public IInterface getProxyInterface() {
        return this.mProxy;
    }

    private static HookFetcher fetchHook(String authority) {
        VLog.d("HV-", "fetchHook authority:" + authority, new Object[0]);
        HookFetcher fetcher = PROVIDER_MAP.get(authority);
        if (fetcher == null) {
            fetcher = new HookFetcher(){

                @Override
                public ProviderHook fetch(boolean external, IInterface provider) {
                    if (external) {
                        return new ExternalProviderHook(provider);
                    }
                    return new InternalProviderHook(provider);
                }
            };
        }
        return fetcher;
    }

    public static IInterface createProxy(boolean external, String authority, IInterface provider) {
        ProviderHook hook;
        IInterface proxyProvider;
        if (provider instanceof Proxy && Proxy.getInvocationHandler(provider) instanceof ProviderHook) {
            return provider;
        }
        HookFetcher fetcher = ProviderHook.fetchHook(authority);
        if (fetcher != null && (proxyProvider = (hook = fetcher.fetch(external, provider)).getProxyInterface()) != null) {
            provider = proxyProvider;
        }
        return provider;
    }

    public Bundle call(MethodBox methodBox, String method, String arg, Bundle extras) throws InvocationTargetException {
        Object[] args = methodBox.args;
        int start = this.getCallIndex(methodBox);
        args[start] = method;
        args[start + 1] = arg;
        args[start + 2] = extras;
        return (Bundle)methodBox.call();
    }

    public int getCallIndex(MethodBox methodBox) {
        return methodBox.args.length - 3;
    }

    public Uri insert(MethodBox methodBox, Uri url, ContentValues initialValues) throws InvocationTargetException {
        Object[] args = methodBox.args;
        int start = MethodParameterUtils.getIndex(args, Uri.class);
        args[start] = url;
        args[start + 1] = initialValues;
        return (Uri)methodBox.call();
    }

    public Cursor query(MethodBox methodBox, Uri url, String[] projection, String selection, String[] selectionArgs, String sortOrder, Bundle originQueryArgs) throws InvocationTargetException {
        Object[] args = methodBox.args;
        int start = MethodParameterUtils.getIndex(args, Uri.class);
        args[start] = url;
        args[start + 1] = projection;
        if (BuildCompat.isOreo()) {
            if (originQueryArgs != null) {
                originQueryArgs.putString(QUERY_ARG_SQL_SELECTION, selection);
                originQueryArgs.putStringArray(QUERY_ARG_SQL_SELECTION_ARGS, selectionArgs);
                originQueryArgs.putString(QUERY_ARG_SQL_SORT_ORDER, sortOrder);
            }
        } else {
            args[start + 2] = selection;
            args[start + 3] = selectionArgs;
            args[start + 4] = sortOrder;
        }
        return (Cursor)methodBox.call();
    }

    @Override
    public Object invoke(Object proxy, Method method, Object ... args) throws Throwable {
        try {
            this.processArgs(method, args);
        }
        catch (Throwable e) {
            e.printStackTrace();
        }
        MethodBox methodBox = new MethodBox(method, this.mBase, args);
        try {
            String name = method.getName();
            if ("call".equals(name)) {
                int start = this.getCallIndex(methodBox);
                String methodName = (String)args[start];
                String arg = (String)args[start + 1];
                Bundle extras = (Bundle)args[start + 2];
                return this.call(methodBox, methodName, arg, extras);
            }
            if ("insert".equals(name)) {
                int start = MethodParameterUtils.getIndex(args, Uri.class);
                Uri url = (Uri)args[start];
                ContentValues initialValues = (ContentValues)args[start + 1];
                return this.insert(methodBox, url, initialValues);
            }
            if ("query".equals(name)) {
                int start = MethodParameterUtils.getIndex(args, Uri.class);
                Uri url = (Uri)args[start];
                String[] projection = (String[])args[start + 1];
                String selection = null;
                String[] selectionArgs = null;
                String sortOrder = null;
                Bundle queryArgs = null;
                if (BuildCompat.isOreo()) {
                    queryArgs = (Bundle)args[start + 2];
                    if (queryArgs != null) {
                        selection = queryArgs.getString(QUERY_ARG_SQL_SELECTION);
                        selectionArgs = queryArgs.getStringArray(QUERY_ARG_SQL_SELECTION_ARGS);
                        sortOrder = queryArgs.getString(QUERY_ARG_SQL_SORT_ORDER);
                    }
                } else {
                    selection = (String)args[start + 2];
                    selectionArgs = (String[])args[start + 3];
                    sortOrder = (String)args[start + 4];
                }
                return this.query(methodBox, url, projection, selection, selectionArgs, sortOrder, queryArgs);
            }
            if ("asBinder".equals(name)) {
                return this.mProxyBinder;
            }
            return methodBox.call();
        }
        catch (Throwable e) {
            VLog.w("ProviderHook", "call: %s (%s) with error", method.getName(), Arrays.toString(args));
            if (e instanceof InvocationTargetException) {
                throw e.getCause();
            }
            throw e;
        }
    }

    protected void processArgs(Method method, Object ... args) {
    }

    static {
        QUERY_ARG_SQL_SELECTION = "android:query-arg-sql-selection";
        QUERY_ARG_SQL_SELECTION_ARGS = "android:query-arg-sql-selection-args";
        QUERY_ARG_SQL_SORT_ORDER = "android:query-arg-sql-sort-order";
        PROVIDER_MAP = new HashMap<String, HookFetcher>();
        PROVIDER_MAP.put("settings", new HookFetcher(){

            @Override
            public ProviderHook fetch(boolean external, IInterface provider) {
                return new SettingsProviderHook(provider);
            }
        });
        PROVIDER_MAP.put("downloads", new HookFetcher(){

            @Override
            public ProviderHook fetch(boolean external, IInterface provider) {
                return new DownloadProviderHook(provider);
            }
        });
        PROVIDER_MAP.put("com.android.badge", new HookFetcher(){

            @Override
            public ProviderHook fetch(boolean external, IInterface provider) {
                return new BadgeProviderHook(provider);
            }
        });
        PROVIDER_MAP.put("com.huawei.android.launcher.settings", new HookFetcher(){

            @Override
            public ProviderHook fetch(boolean external, IInterface provider) {
                return new BadgeProviderHook(provider);
            }
        });
    }

    public static interface HookFetcher {
        public ProviderHook fetch(boolean var1, IInterface var2);
    }
}

