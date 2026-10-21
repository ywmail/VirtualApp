//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.lody.virtual.server.settings;

import android.content.ContentResolver;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.Parcel;
import android.os.Build.VERSION;
import android.provider.Settings.Global;
import android.provider.Settings.Secure;
import android.text.TextUtils;
import android.util.SparseArray;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.os.VEnvironment;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import mirror.android.providers.Settings.Config;

public class VSettingsProvider {
    private static final int MSG_QUITE = 2;
    private static final int MSG_SAVE_SETTINGS_TO_FILE = 1;
    private static final int SETTINGS_FILE_VERIFY_FLAG = 1;
    public static final int TABLE_INDEX_CONFIG = 3;
    public static final int TABLE_INDEX_GLOBAL = 2;
    public static final int TABLE_INDEX_SECURE = 1;
    public static final int TABLE_INDEX_SYSTEM = 0;
    private static final long TIME_TO_QUITE = 60000L;
    public static final List<String> sConfigTableCanLookup = new ArrayList();
    private static VSettingsProvider sVSettingsProvider = new VSettingsProvider();
    private final ContentResolver mContentResolver = VirtualCore.get().getContext().getContentResolver();
    private HandlerThread mHandleThread;
    private Handler mHandlerSettingsSync;
    private final SparseArray<HashMap<String, String>> mSystem = new SparseArray();
    private final SparseArray<HashMap<String, String>> sSecure = new SparseArray();
    private final SparseArray<HashMap<String, String>> mGlobal = new SparseArray();
    private final SparseArray<HashMap<String, String>> mConfig = new SparseArray();
    private final SparseArray[] mTables;

    private VSettingsProvider() {
        this.mTables = new SparseArray[]{this.mSystem, this.sSecure, this.mGlobal, this.mConfig};
    }

    public void saveSettingsToFile(int userId) {
        Parcel target = Parcel.obtain();

        try {
            target.writeInt(1);
            writeMapToParcel((HashMap)this.mSystem.get(userId), target);
            writeMapToParcel((HashMap)this.sSecure.get(userId), target);
            writeMapToParcel((HashMap)this.mGlobal.get(userId), target);
            writeMapToParcel((HashMap)this.mConfig.get(userId), target);
            File settingsFile = this.getSystemSettingsFile(userId);
            if (!settingsFile.exists()) {
                settingsFile.createNewFile();
            }
        } catch (Exception var4) {
        }

        target.recycle();
    }

    private void loadSettingsFromFile(int userId) {
        HashMap system = new HashMap();
        HashMap secure = new HashMap();
        HashMap global = new HashMap();
        HashMap config = new HashMap();
        this.mSystem.put(userId, system);
        this.sSecure.put(userId, secure);
        this.mGlobal.put(userId, global);
        this.mConfig.put(userId, config);
        File settingsFile = this.getSystemSettingsFile(userId);
        if (settingsFile.exists()) {
            int fileLen = (int)settingsFile.length();
            byte[] settingsContent = new byte[fileLen];
            FileInputStream fis = null;

            try {
                try {
                    fis = new FileInputStream(settingsFile);
                    int readCount = fis.read(settingsContent);

                    try {
                        fis.close();
                    } catch (Exception var20) {
                    }

                    if (fileLen != readCount) {
                        settingsFile.delete();
                        return;
                    }

                    Parcel target = Parcel.obtain();
                    target.unmarshall(settingsContent, 0, fileLen);
                    target.setDataPosition(0);
                    int flag = target.readInt();
                    if (flag != 1) {
                        target.recycle();
                        return;
                    }

                    int size = target.readInt();

                    int size2;
                    for(size2 = 0; size2 < size; ++size2) {
                        system.put(target.readString(), target.readString());
                    }

                    size2 = target.readInt();

                    int size3;
                    for(size3 = 0; size3 < size2; ++size3) {
                        secure.put(target.readString(), target.readString());
                    }

                    size3 = target.readInt();

                    int size4;
                    for(size4 = 0; size4 < size3; ++size4) {
                        global.put(target.readString(), target.readString());
                    }

                    size4 = target.readInt();

                    for(int i4 = 0; i4 < size4; ++i4) {
                        config.put(target.readString(), target.readString());
                    }

                    target.recycle();
                } catch (Exception var21) {
                    var21.printStackTrace();
                    settingsFile.delete();
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (Exception var19) {
                        }
                    }
                }
            } catch (Throwable var22) {
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (Exception var18) {
                    }
                }

                throw var22;
            }
        }

    }

    public static VSettingsProvider getInstance() {
        return sVSettingsProvider;
    }

    public final String getSettingsProvider(int userId, int tableIndex, String arg) {
        try {
            if (tableIndex < this.mTables.length && userId >= 0) {
                SparseArray sparseArray = this.mTables[tableIndex];
                HashMap hashMap = (HashMap)sparseArray.get(userId);
                if (hashMap == null) {
                    Class var6 = VSettingsProvider.class;
                    synchronized(VSettingsProvider.class) {
                        this.loadSettingsFromFile(userId);
                    }

                    hashMap = (HashMap)sparseArray.get(userId);
                }

                String value = (String)hashMap.get(arg);
                if (value != null) {
                    return value;
                } else if (tableIndex == 1) {
                    return Secure.getString(this.mContentResolver, arg);
                } else if (tableIndex == 2) {
                    return VERSION.SDK_INT >= 17 ? Global.getString(this.mContentResolver, arg) : value;
                } else if (tableIndex == 3 && VERSION.SDK_INT >= 29) {
                    return sConfigTableCanLookup.contains(arg.split("/")[0]) ? (String)Config.getString(this.mContentResolver, arg) : value;
                } else {
                    return value;
                }
            } else {
                return null;
            }
        } catch (Exception var9) {
            var9.printStackTrace();
            return null;
        }
    }

    public final void setSettingsProvider(int userId, int tableIndex, String arg, String value) {
        try {
            if (tableIndex < this.mTables.length && userId >= 0) {
                SparseArray table = this.mTables[tableIndex];
                Class var7 = VSettingsProvider.class;
                synchronized(VSettingsProvider.class) {
                    if (table.get(userId) == null) {
                        this.loadSettingsFromFile(userId);
                    }

                    HashMap hashMap = (HashMap)table.get(userId);
                    boolean hasUpdate;
                    if (!TextUtils.equals((CharSequence)hashMap.get(arg), value)) {
                        hashMap.put(arg, value);
                        hasUpdate = true;
                    } else {
                        hasUpdate = false;
                    }

                    if (hasUpdate) {
                        if (this.mHandleThread == null) {
                            this.mHandleThread = new HandlerThread("SETTINGS_SAVE_TO_FILE");
                            this.mHandleThread.start();
                            this.mHandlerSettingsSync = new HandlerSM(this.mHandleThread.getLooper());
                        }

                        this.mHandlerSettingsSync.removeMessages(1);
                        this.mHandlerSettingsSync.sendMessageDelayed(this.mHandlerSettingsSync.obtainMessage(1, userId, 0), 5000L);
                    }
                }
            }
        } catch (Exception var11) {
            var11.printStackTrace();
        }

    }

    private File getSystemSettingsFile(int userId) {
        return VEnvironment.getSystemSettingsFile(userId);
    }

    private static void writeMapToParcel(HashMap<String, String> hashMap, Parcel parcel) {
        if (hashMap != null && hashMap.size() > 0) {
            parcel.writeInt(hashMap.size());
            Iterator var2 = hashMap.entrySet().iterator();

            while(var2.hasNext()) {
                Map.Entry<String, String> entry = (Map.Entry)var2.next();
                parcel.writeString((String)entry.getKey());
                parcel.writeString((String)entry.getValue());
            }

        } else {
            parcel.writeInt(0);
        }
    }

    static {
        sConfigTableCanLookup.add("textclassifier");
        sConfigTableCanLookup.add("runtime");
    }

    class HandlerSM extends Handler {
        HandlerSM(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message message) {
            Class var2;
            if (message.what == 1) {
                var2 = VSettingsProvider.class;
                synchronized(VSettingsProvider.class) {
                    VSettingsProvider.this.saveSettingsToFile(message.arg1);
                    if (!VSettingsProvider.this.mHandlerSettingsSync.hasMessages(1)) {
                        VSettingsProvider.this.mHandlerSettingsSync.sendEmptyMessageDelayed(2, 60000L);
                    }
                }
            } else if (message.what == 2) {
                var2 = VSettingsProvider.class;
                synchronized(VSettingsProvider.class) {
                    if (!this.hasMessages(1)) {
                        this.removeMessages(2);
                        if (VSettingsProvider.this.mHandleThread != null) {
                            VSettingsProvider.this.mHandleThread.quit();
                        }

                        VSettingsProvider.this.mHandleThread = null;
                        VSettingsProvider.this.mHandlerSettingsSync = null;
                    }
                }
            }

        }
    }
}
