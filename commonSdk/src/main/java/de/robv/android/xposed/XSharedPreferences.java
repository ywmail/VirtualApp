//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package de.robv.android.xposed;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;
import com.android.internal.util.XmlUtils;
import de.robv.android.xposed.services.FileResult;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.xmlpull.v1.XmlPullParserException;

public final class XSharedPreferences implements SharedPreferences {
    private static final String TAG = "XSharedPreferences";
    private final File mFile;
    private final String mFilename;
    private Map<String, Object> mMap;
    private boolean mLoaded;
    private long mLastModified;
    private long mFileSize;

    public XSharedPreferences(File prefFile) {
        this.mLoaded = false;
        this.mFile = prefFile;
        this.mFilename = this.mFile.getAbsolutePath();
        this.startLoadFromDisk();
    }

    public XSharedPreferences(String packageName) {
        this(packageName, packageName + "_preferences");
    }

    public XSharedPreferences(String packageName, String prefFileName) {
        this.mLoaded = false;
        this.mFile = new File(Environment.getDataDirectory(), "data/" + packageName + "/shared_prefs/" + prefFileName + ".xml");
        this.mFilename = this.mFile.getAbsolutePath();
        this.startLoadFromDisk();
    }

    @SuppressLint({"SetWorldReadable"})
    public boolean makeWorldReadable() {
        if (!SELinuxHelper.getAppDataFileService().hasDirectFileAccess()) {
            return false;
        } else {
            return !this.mFile.exists() ? false : this.mFile.setReadable(true, false);
        }
    }

    public File getFile() {
        return this.mFile;
    }

    private void startLoadFromDisk() {
        synchronized(this) {
            this.mLoaded = false;
        }

        (new Thread("XSharedPreferences-load") {
            public void run() {
                synchronized(XSharedPreferences.this) {
                    XSharedPreferences.this.loadFromDiskLocked();
                }
            }
        }).start();
    }

    private void loadFromDiskLocked() {
        if (!this.mLoaded) {
            Map map = null;
            FileResult result = null;

            try {
                result = SELinuxHelper.getAppDataFileService().getFileInputStream(this.mFilename, this.mFileSize, this.mLastModified);
                if (result.stream != null) {
                    map = XmlUtils.readMapXml(result.stream);
                    result.stream.close();
                } else {
                    map = this.mMap;
                }
            } catch (XmlPullParserException var22) {
                Log.w("XSharedPreferences", "getSharedPreferences", var22);
            } catch (FileNotFoundException var23) {
            } catch (IOException var24) {
                Log.w("XSharedPreferences", "getSharedPreferences", var24);
            } finally {
                if (result != null && result.stream != null) {
                    try {
                        result.stream.close();
                    } catch (RuntimeException var20) {
                        throw var20;
                    } catch (Exception var21) {
                    }
                }

            }

            this.mLoaded = true;
            if (map != null) {
                this.mMap = (Map)map;
                this.mLastModified = result.mtime;
                this.mFileSize = result.size;
            } else {
                this.mMap = new HashMap();
            }

            this.notifyAll();
        }
    }

    public synchronized void reload() {
        if (this.hasFileChanged()) {
            this.startLoadFromDisk();
        }

    }

    public synchronized boolean hasFileChanged() {
        try {
            FileResult result = SELinuxHelper.getAppDataFileService().statFile(this.mFilename);
            return this.mLastModified != result.mtime || this.mFileSize != result.size;
        } catch (FileNotFoundException var2) {
            return true;
        } catch (IOException var3) {
            Log.w("XSharedPreferences", "hasFileChanged", var3);
            return true;
        }
    }

    private void awaitLoadedLocked() {
        while(!this.mLoaded) {
            try {
                this.wait();
            } catch (InterruptedException var2) {
            }
        }

    }

    public Map<String, ?> getAll() {
        synchronized(this) {
            this.awaitLoadedLocked();
            return new HashMap(this.mMap);
        }
    }

    public String getString(String key, String defValue) {
        synchronized(this) {
            this.awaitLoadedLocked();
            String v = (String)this.mMap.get(key);
            return v != null ? v : defValue;
        }
    }

    public Set<String> getStringSet(String key, Set<String> defValues) {
        synchronized(this) {
            this.awaitLoadedLocked();
            Set<String> v = (Set)this.mMap.get(key);
            return v != null ? v : defValues;
        }
    }

    public int getInt(String key, int defValue) {
        synchronized(this) {
            this.awaitLoadedLocked();
            Integer v = (Integer)this.mMap.get(key);
            return v != null ? v : defValue;
        }
    }

    public long getLong(String key, long defValue) {
        synchronized(this) {
            this.awaitLoadedLocked();
            Long v = (Long)this.mMap.get(key);
            return v != null ? v : defValue;
        }
    }

    public float getFloat(String key, float defValue) {
        synchronized(this) {
            this.awaitLoadedLocked();
            Float v = (Float)this.mMap.get(key);
            return v != null ? v : defValue;
        }
    }

    public boolean getBoolean(String key, boolean defValue) {
        synchronized(this) {
            this.awaitLoadedLocked();
            Boolean v = (Boolean)this.mMap.get(key);
            return v != null ? v : defValue;
        }
    }

    public boolean contains(String key) {
        synchronized(this) {
            this.awaitLoadedLocked();
            return this.mMap.containsKey(key);
        }
    }

    /** @deprecated */
    @Deprecated
    public SharedPreferences.Editor edit() {
        throw new UnsupportedOperationException("read-only implementation");
    }

    /** @deprecated */
    @Deprecated
    public void registerOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        throw new UnsupportedOperationException("listeners are not supported in this implementation");
    }

    /** @deprecated */
    @Deprecated
    public void unregisterOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        throw new UnsupportedOperationException("listeners are not supported in this implementation");
    }
}
