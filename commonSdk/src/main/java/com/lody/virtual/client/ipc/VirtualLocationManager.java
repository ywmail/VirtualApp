/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.RemoteException
 */
package com.lody.virtual.client.ipc;

import android.os.RemoteException;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.VClient;
import com.lody.virtual.client.env.VirtualRuntime;
import com.lody.virtual.client.hook.base.MethodProxy;
import com.lody.virtual.client.ipc.LocalProxyUtils;
import com.lody.virtual.client.ipc.ServiceManagerNative;
import com.lody.virtual.helper.utils.IInterfaceUtils;
import com.lody.virtual.os.VUserHandle;
import com.lody.virtual.remote.vloc.VCell;
import com.lody.virtual.remote.vloc.VLocation;
import com.lody.virtual.server.interfaces.IVirtualLocationManager;
import java.util.List;

public class VirtualLocationManager {
    public static final String GLOBAL_PACKAGE = "virtual.package.global";
    public static final int GLOBAL_USERID = 888888;
    public static final int MODE_CLOSE = 0;
    public static final int MODE_USE_GLOBAL = 1;
    public static final int MODE_USE_SELF = 2;
    private static final VirtualLocationManager sInstance = new VirtualLocationManager();
    private IVirtualLocationManager mService;

    public static VirtualLocationManager get() {
        return sInstance;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public IVirtualLocationManager getService() {
        if (this.mService == null || !IInterfaceUtils.isAlive(this.mService)) {
            VirtualLocationManager virtualLocationManager = this;
            synchronized (virtualLocationManager) {
                Object binder = this.getRemoteInterface();
                this.mService = LocalProxyUtils.genProxy(IVirtualLocationManager.class, binder);
            }
        }
        return this.mService;
    }

    private Object getRemoteInterface() {
        return IVirtualLocationManager.Stub.asInterface(ServiceManagerNative.getService("virtual-loc"));
    }

    public int getMode(int userId, String pkg) {
        try {
            return this.getService().getMode(userId, pkg);
        }
        catch (RemoteException e) {
            return (Integer)VirtualRuntime.crash(e);
        }
    }

    public int getMode() {
        return this.getMode(MethodProxy.getAppUserId(), MethodProxy.getAppPkg());
    }

    public boolean isUseVirtualLocation(int userId, String pkg) {
        return this.getMode(userId, pkg) != 0;
    }

    public void setMode(int userId, String pkg, int mode) {
        try {
            this.getService().setMode(userId, pkg, mode);
        }
        catch (RemoteException e) {
            VirtualRuntime.crash(e);
        }
    }

    public void setCell(int userId, String pkg, VCell cell) {
        try {
            this.getService().setCell(userId, pkg, cell);
        }
        catch (RemoteException e) {
            VirtualRuntime.crash(e);
        }
    }

    public void setAllCell(int userId, String pkg, List<VCell> cell) {
        try {
            this.getService().setAllCell(userId, pkg, cell);
        }
        catch (RemoteException e) {
            VirtualRuntime.crash(e);
        }
    }

    public void setNeighboringCell(int userId, String pkg, List<VCell> cell) {
        try {
            this.getService().setNeighboringCell(userId, pkg, cell);
        }
        catch (RemoteException e) {
            VirtualRuntime.crash(e);
        }
    }

    public VCell getCell(int userId, String pkg) {
        try {
            return this.getService().getCell(userId, pkg);
        }
        catch (RemoteException e) {
            return (VCell)VirtualRuntime.crash(e);
        }
    }

    public List<VCell> getAllCell(int userId, String pkg) {
        try {
            return this.getService().getAllCell(userId, pkg);
        }
        catch (RemoteException e) {
            return (List)VirtualRuntime.crash(e);
        }
    }

    public List<VCell> getNeighboringCell(int userId, String pkg) {
        try {
            return this.getService().getNeighboringCell(userId, pkg);
        }
        catch (RemoteException e) {
            return (List)VirtualRuntime.crash(e);
        }
    }

    public void setGlobalCell(VCell cell) {
        try {
            this.getService().setGlobalCell(cell);
        }
        catch (RemoteException e) {
            VirtualRuntime.crash(e);
        }
    }

    public void setGlobalAllCell(List<VCell> cell) {
        try {
            this.getService().setGlobalAllCell(cell);
        }
        catch (RemoteException e) {
            VirtualRuntime.crash(e);
        }
    }

    public void setGlobalNeighboringCell(List<VCell> cell) {
        try {
            this.getService().setGlobalNeighboringCell(cell);
        }
        catch (RemoteException e) {
            VirtualRuntime.crash(e);
        }
    }

    public void setLocation(int userId, String pkg, VLocation loc) {
        try {
            this.getService().setLocation(userId, pkg, loc);
        }
        catch (RemoteException e) {
            VirtualRuntime.crash(e);
        }
    }

    public VLocation getLocation(int userId, String pkg) {
        try {
            return this.getService().getLocation(userId, pkg);
        }
        catch (RemoteException e) {
            return (VLocation)VirtualRuntime.crash(e);
        }
    }

    public VLocation getLocation() {
        return this.getLocation(MethodProxy.getAppUserId(), MethodProxy.getAppPkg());
    }

    public void setGlobalLocation(VLocation loc) {
        try {
            this.getService().setGlobalLocation(loc);
        }
        catch (RemoteException e) {
            VirtualRuntime.crash(e);
        }
    }

    public VLocation getGlobalLocation() {
        try {
            return this.getService().getGlobalLocation();
        }
        catch (RemoteException e) {
            return (VLocation)VirtualRuntime.crash(e);
        }
    }

    public void setGlobalMode(int i) {
        try {
            this.getService().setMode(888888, GLOBAL_PACKAGE, i);
        }
        catch (RemoteException e) {
            VirtualRuntime.crash(e);
        }
    }

    public int getGlobalMode() {
        try {
            return this.getService().getMode(888888, GLOBAL_PACKAGE);
        }
        catch (RemoteException e) {
            return (Integer)VirtualRuntime.crash(e);
        }
    }

    public int getCurAppMode() {
        return this.getMode(VUserHandle.myUserId(), VClient.get().getCurrentPackage());
    }

    public VLocation getCurAppLocation() {
        return this.getLocation(VUserHandle.myUserId(), VClient.get().getCurrentPackage());
    }
}

