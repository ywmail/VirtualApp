/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.Bitmap
 *  android.os.RemoteException
 *  android.util.Log
 */
package com.lody.virtual.os;

import android.graphics.Bitmap;
import android.os.RemoteException;
import android.util.Log;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.ipc.LocalProxyUtils;
import com.lody.virtual.client.ipc.ServiceManagerNative;
import com.lody.virtual.helper.utils.IInterfaceUtils;
import com.lody.virtual.os.VUserHandle;
import com.lody.virtual.os.VUserInfo;
import com.lody.virtual.server.interfaces.IUserManager;
import java.util.List;

public class VUserManager {
    private static String TAG = "VUserManager";
    private IUserManager mService;
    private static final VUserManager sInstance = new VUserManager();

    private Object getRemoteInterface() {
        return IUserManager.Stub.asInterface(ServiceManagerNative.getService("user"));
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public IUserManager getService() {
        if (IInterfaceUtils.isAlive(this.mService) && !VirtualCore.get().isExtHelperProcess()) return this.mService;
        Class<VUserManager> clazz = VUserManager.class;
        synchronized (VUserManager.class) {
            Object remote = this.getRemoteInterface();
            this.mService = LocalProxyUtils.genProxy(IUserManager.class, remote);
            // ** MonitorExit[var1_1] (shouldn't be in output)
            return this.mService;
        }
    }

    public static synchronized VUserManager get() {
        return sInstance;
    }

    public static boolean supportsMultipleUsers() {
        return VUserManager.getMaxSupportedUsers() > 1;
    }

    public int getUserHandle() {
        return VUserHandle.myUserId();
    }

    public String getUserName() {
        try {
            return this.getService().getUserInfo((int)this.getUserHandle()).name;
        }
        catch (RemoteException re) {
            Log.w((String)TAG, (String)"Could not get user name", (Throwable)re);
            return "";
        }
    }

    public boolean isUserAGoat() {
        return false;
    }

    public VUserInfo getUserInfo(int handle) {
        try {
            return this.getService().getUserInfo(handle);
        }
        catch (RemoteException re) {
            Log.w((String)TAG, (String)"Could not get user info", (Throwable)re);
            return null;
        }
    }

    public long getSerialNumberForUser(VUserHandle user) {
        return this.getUserSerialNumber(user.getIdentifier());
    }

    public VUserHandle getUserForSerialNumber(long serialNumber) {
        int ident = this.getUserHandle((int)serialNumber);
        return ident >= 0 ? new VUserHandle(ident) : null;
    }

    public VUserInfo createUser(String name, int flags) {
        try {
            return this.getService().createUser(name, flags);
        }
        catch (RemoteException re) {
            Log.w((String)TAG, (String)"Could not create a user", (Throwable)re);
            return null;
        }
    }

    public int getUserCount() {
        List<VUserInfo> users = this.getUsers();
        return users != null ? users.size() : 1;
    }

    public List<VUserInfo> getUsers() {
        try {
            return this.getService().getUsers(false);
        }
        catch (RemoteException re) {
            re.printStackTrace();
            Log.w((String)TAG, (String)"Could not get user list", (Throwable)re);
            return null;
        }
    }

    public List<VUserInfo> getUsers(boolean excludeDying) {
        try {
            return this.getService().getUsers(excludeDying);
        }
        catch (RemoteException re) {
            Log.w((String)TAG, (String)"Could not get user list", (Throwable)re);
            return null;
        }
    }

    public boolean removeUser(int handle) {
        try {
            return this.getService().removeUser(handle);
        }
        catch (RemoteException re) {
            Log.w((String)TAG, (String)"Could not remove user ", (Throwable)re);
            return false;
        }
    }

    public void setUserName(int handle, String name) {
        try {
            this.getService().setUserName(handle, name);
        }
        catch (RemoteException re) {
            Log.w((String)TAG, (String)"Could not set the user name ", (Throwable)re);
        }
    }

    public void setUserIcon(int handle, Bitmap icon) {
        try {
            this.getService().setUserIcon(handle, icon);
        }
        catch (RemoteException re) {
            Log.w((String)TAG, (String)"Could not set the user icon ", (Throwable)re);
        }
    }

    public Bitmap getUserIcon(int handle) {
        try {
            return this.getService().getUserIcon(handle);
        }
        catch (RemoteException re) {
            Log.w((String)TAG, (String)"Could not get the user icon ", (Throwable)re);
            return null;
        }
    }

    public void setGuestEnabled(boolean enable) {
        try {
            this.getService().setGuestEnabled(enable);
        }
        catch (RemoteException re) {
            Log.w((String)TAG, (String)("Could not change guest account availability to " + enable));
        }
    }

    public boolean isGuestEnabled() {
        try {
            return this.getService().isGuestEnabled();
        }
        catch (RemoteException re) {
            Log.w((String)TAG, (String)"Could not retrieve guest enabled state");
            return false;
        }
    }

    public void wipeUser(int handle) {
        try {
            this.getService().wipeUser(handle);
        }
        catch (RemoteException re) {
            Log.w((String)TAG, (String)("Could not wipe user " + handle));
        }
    }

    public static int getMaxSupportedUsers() {
        return Integer.MAX_VALUE;
    }

    public int getUserSerialNumber(int handle) {
        try {
            return this.getService().getUserSerialNumber(handle);
        }
        catch (RemoteException re) {
            Log.w((String)TAG, (String)("Could not get serial number for user " + handle));
            return -1;
        }
    }

    public int getUserHandle(int userSerialNumber) {
        try {
            return this.getService().getUserHandle(userSerialNumber);
        }
        catch (RemoteException re) {
            Log.w((String)TAG, (String)("Could not get VUserHandle for user " + userSerialNumber));
            return -1;
        }
    }
}

