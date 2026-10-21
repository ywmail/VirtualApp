/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.app.Application
 *  android.os.Binder
 *  android.os.Handler
 *  android.os.RemoteException
 *  android.util.Log
 */
package com.carlos.science.client.hyxd;

import android.app.Activity;
import android.app.Application;
import android.os.Binder;
import android.os.Handler;
import android.os.RemoteException;
import android.util.Log;
import com.carlos.libcommon.StringFog;
import com.carlos.science.IVirtualController;
import com.carlos.science.client.ClientActivityLifecycle;
import com.carlos.science.client.hyxd.HYXDNative;
import com.kook.common.utils.HVLog;
import com.kook.controller.client.hyxd.IHYXDController;
import com.kook.controller.server.IServerController;
import com.lody.virtual.helper.utils.Singleton;
import java.io.File;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class HYXDControllerImpl
extends IHYXDController.Stub
implements ClientActivityLifecycle,
Runnable {
    String TAG = "HYXDControllerImpl";
    public static final int VIEW_ACTION_WD = 1;
    private Queue<Unpack> queue = new ConcurrentLinkedQueue<Unpack>();
    Thread threadProcess;
    public boolean isInit = false;
    Activity mCurrentActivity;
    private static final Singleton<HYXDControllerImpl> sService = new Singleton<HYXDControllerImpl>(){

        @Override
        protected HYXDControllerImpl create() {
            return new HYXDControllerImpl();
        }
    };

    public static HYXDControllerImpl get() {
        return sService.get();
    }

    @Override
    public void setIServerController(Application application, IServerController serverController, IVirtualController virtualControllerImpl) {
    }

    @Override
    public IVirtualController getVirtualControllerImpl() {
        return null;
    }

    @Override
    public IServerController getIServerController() {
        return null;
    }

    @Override
    public Activity getCurrentActivity() {
        return this.mCurrentActivity;
    }

    @Override
    public Handler getHandler() {
        return null;
    }

    @Override
    public void onActivityResumed(Activity activity) {
        this.mCurrentActivity = activity;
    }

    @Override
    public void onActivityPaused(Activity activity) {
    }

    @Override
    public void runOnUiThread(Runnable runnable) {
    }

    @Override
    public boolean memorySRWData(String searchValue, String writeValue, boolean permission2) throws RemoteException {
        if (!this.isInit) {
            File file = this.getCurrentActivity().getFilesDir().getParentFile();
            int callingPid = Binder.getCallingPid();
            boolean directory = file.isDirectory();
            Log.d((String)"VA-NATIVE-HYXD", (String)("path:" + file.getAbsolutePath() + "   directory：" + directory + "      pid:" + callingPid));
            HYXDNative.init(callingPid, file.getPath() + "/cacheResult");
            this.threadProcess = new Thread(this);
            this.threadProcess.start();
            this.isInit = true;
        }
        this.addUnpack(new Unpack(searchValue, writeValue, permission2));
        return true;
    }

    @Override
    public boolean memoryTest() throws RemoteException {
        if (!this.isInit) {
            File file = this.getCurrentActivity().getFilesDir().getParentFile();
            int callingPid = Binder.getCallingPid();
            boolean directory = file.isDirectory();
            Log.d((String)"VA-NATIVE-HYXD", (String)("path:" + file.getAbsolutePath() + "   directory：" + directory + "      pid:" + callingPid));
            HYXDNative.init(callingPid, file.getPath() + "/cacheResult");
            this.threadProcess = new Thread(this);
            this.threadProcess.start();
        }
        HYXDNative.memoryTest();
        return false;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void addUnpack(Unpack unpack) {
        Queue<Unpack> queue = this.queue;
        synchronized (queue) {
            this.queue.add(unpack);
            this.queue.notifyAll();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void run() {
        while (!Thread.interrupted()) {
            Queue<Unpack> queue = this.queue;
            synchronized (queue) {
                try {
                    if (null == this.queue || this.queue.size() == 0) {
                        this.queue.wait();
                    }
                    Unpack unpack = this.queue.poll();
                    HVLog.d("查看整包数据结果 " + unpack.toString());
                    HYXDNative.searchWrite(unpack.searchValue, unpack.writeValue, unpack.permission);
                }
                catch (Exception e) {
                    HVLog.printException(e);
                }
            }
        }
    }

    class Unpack {
        String searchValue;
        String writeValue;
        boolean permission;

        public Unpack(String searchValue, String writeValue, boolean permission2) {
            this.searchValue = searchValue;
            this.writeValue = writeValue;
            this.permission = permission2;
        }

        public String toString() {
            return "Unpack{searchValue='" + this.searchValue + '\'' + ", writeValue='" + this.writeValue + '\'' + ", permission=" + this.permission + '}';
        }
    }
}

