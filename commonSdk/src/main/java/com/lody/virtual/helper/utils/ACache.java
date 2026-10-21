/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.graphics.Bitmap
 *  android.graphics.Bitmap$CompressFormat
 *  android.graphics.Bitmap$Config
 *  android.graphics.BitmapFactory
 *  android.graphics.Canvas
 *  android.graphics.drawable.BitmapDrawable
 *  android.graphics.drawable.Drawable
 *  android.os.Process
 *  org.json.JSONArray
 *  org.json.JSONObject
 */
package com.lody.virtual.helper.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Process;
import com.lody.virtual.StringFog;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import org.json.JSONArray;
import org.json.JSONObject;

public class ACache {
    public static final int TIME_HOUR = 3600;
    public static final int TIME_DAY = 86400;
    private static final int MAX_SIZE = 50000000;
    private static final int MAX_COUNT = Integer.MAX_VALUE;
    private static Map<String, ACache> mInstanceMap = new HashMap<String, ACache>();
    private ACacheManager mCache;

    public static ACache get(Context ctx) {
        return ACache.get(ctx, "ACache");
    }

    public static ACache get(Context ctx, String cacheName) {
        File f = new File(ctx.getCacheDir(), cacheName);
        return ACache.get(f, 50000000L, Integer.MAX_VALUE);
    }

    public static ACache get(File cacheDir) {
        return ACache.get(cacheDir, 50000000L, Integer.MAX_VALUE);
    }

    public static ACache get(Context ctx, long max_zise, int max_count) {
        File f = new File(ctx.getCacheDir(), "ACache");
        return ACache.get(f, max_zise, max_count);
    }

    public static ACache get(File cacheDir, long max_zise, int max_count) {
        ACache manager = mInstanceMap.get(cacheDir.getAbsoluteFile() + ACache.myPid());
        if (manager == null) {
            manager = new ACache(cacheDir, max_zise, max_count);
            mInstanceMap.put(cacheDir.getAbsolutePath() + ACache.myPid(), manager);
        }
        return manager;
    }

    private static String myPid() {
        return "_" + Process.myPid();
    }

    private ACache(File cacheDir, long max_size, int max_count) {
        if (!cacheDir.exists() && !cacheDir.mkdirs()) {
            throw new RuntimeException("can't make dirs in " + cacheDir.getAbsolutePath());
        }
        this.mCache = new ACacheManager(cacheDir, max_size, max_count);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void put(String key, String value) {
        File file = this.mCache.newFile(key);
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new FileWriter(file), 1024);
            out.write(value);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (out != null) {
                try {
                    out.flush();
                    out.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            this.mCache.put(file);
        }
    }

    public void put(String key, String value, int saveTime) {
        this.put(key, Utils.newStringWithDateInfo(saveTime, value));
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public String getAsString(String key) {
        File file = this.mCache.get(key);
        if (!file.exists()) {
            return null;
        }
        boolean removeFile = false;
        BufferedReader in = null;
        try {
            String currentLine;
            in = new BufferedReader(new FileReader(file));
            String readString = "";
            while ((currentLine = in.readLine()) != null) {
                readString = readString + currentLine;
            }
            if (!Utils.isDue(readString)) {
                String string2 = Utils.clearDateInfo(readString);
                return string2;
            }
            removeFile = true;
            String string3 = null;
            return string3;
        }
        catch (IOException e) {
            e.printStackTrace();
            String string4 = null;
            return string4;
        }
        finally {
            if (in != null) {
                try {
                    in.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (removeFile) {
                this.remove(key);
            }
        }
    }

    public void put(String key, JSONObject value) {
        this.put(key, value.toString());
    }

    public void put(String key, JSONObject value, int saveTime) {
        this.put(key, value.toString(), saveTime);
    }

    public JSONObject getAsJSONObject(String key) {
        String JSONString = this.getAsString(key);
        try {
            JSONObject obj = new JSONObject(JSONString);
            return obj;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void put(String key, JSONArray value) {
        this.put(key, value.toString());
    }

    public void put(String key, JSONArray value, int saveTime) {
        this.put(key, value.toString(), saveTime);
    }

    public JSONArray getAsJSONArray(String key) {
        String JSONString = this.getAsString(key);
        try {
            JSONArray obj = new JSONArray(JSONString);
            return obj;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void put(String key, byte[] value) {
        File file = this.mCache.newFile(key);
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            out.write(value);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (out != null) {
                try {
                    out.flush();
                    out.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            this.mCache.put(file);
        }
    }

    public OutputStream put(String key) throws FileNotFoundException {
        return new xFileOutputStream(this.mCache.newFile(key));
    }

    public InputStream get(String key) throws FileNotFoundException {
        File file = this.mCache.get(key);
        if (!file.exists()) {
            return null;
        }
        return new FileInputStream(file);
    }

    public void put(String key, byte[] value, int saveTime) {
        this.put(key, Utils.newByteArrayWithDateInfo(saveTime, value));
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public byte[] getAsBinary(String key) {
        RandomAccessFile RAFile = null;
        boolean removeFile = false;
        try {
            File file = this.mCache.get(key);
            if (!file.exists()) {
                byte[] byArray = null;
                return byArray;
            }
            RAFile = new RandomAccessFile(file, "r");
            byte[] byteArray = new byte[(int)RAFile.length()];
            RAFile.read(byteArray);
            if (!Utils.isDue(byteArray)) {
                byte[] e = Utils.clearDateInfo(byteArray);
                return e;
            }
            removeFile = true;
            byte[] e = null;
            return e;
        }
        catch (Exception e) {
            e.printStackTrace();
            byte[] byArray = null;
            return byArray;
        }
        finally {
            if (RAFile != null) {
                try {
                    RAFile.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (removeFile) {
                this.remove(key);
            }
        }
    }

    public void put(String key, Serializable value) {
        this.put(key, value, -1);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void put(String key, Serializable value, int saveTime) {
        ByteArrayOutputStream baos = null;
        ObjectOutputStream oos = null;
        try {
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(value);
            byte[] data = baos.toByteArray();
            if (saveTime != -1) {
                this.put(key, data, saveTime);
            } else {
                this.put(key, data);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                oos.close();
            }
            catch (IOException iOException) {}
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public Object getAsObject(String key) {
        byte[] data = this.getAsBinary(key);
        if (data != null) {
            ByteArrayInputStream bais = null;
            ObjectInputStream ois = null;
            try {
                Object reObject;
                bais = new ByteArrayInputStream(data);
                ois = new ObjectInputStream(bais);
                Object object = reObject = ois.readObject();
                return object;
            }
            catch (Exception e) {
                e.printStackTrace();
                Object var6_8 = null;
                return var6_8;
            }
            finally {
                try {
                    if (bais != null) {
                        bais.close();
                    }
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    if (ois != null) {
                        ois.close();
                    }
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public void put(String key, Bitmap value) {
        this.put(key, Utils.Bitmap2Bytes(value));
    }

    public void put(String key, Bitmap value, int saveTime) {
        this.put(key, Utils.Bitmap2Bytes(value), saveTime);
    }

    public Bitmap getAsBitmap(String key) {
        if (this.getAsBinary(key) == null) {
            return null;
        }
        return Utils.Bytes2Bimap(this.getAsBinary(key));
    }

    public void put(String key, Drawable value) {
        this.put(key, Utils.drawable2Bitmap(value));
    }

    public void put(String key, Drawable value, int saveTime) {
        this.put(key, Utils.drawable2Bitmap(value), saveTime);
    }

    public Drawable getAsDrawable(String key) {
        if (this.getAsBinary(key) == null) {
            return null;
        }
        return Utils.bitmap2Drawable(Utils.Bytes2Bimap(this.getAsBinary(key)));
    }

    public File file(String key) {
        File f = this.mCache.newFile(key);
        if (f.exists()) {
            return f;
        }
        return null;
    }

    public boolean remove(String key) {
        return this.mCache.remove(key);
    }

    public void clear() {
        this.mCache.clear();
    }

    private static class Utils {
        private static final char mSeparator = ' ';

        private Utils() {
        }

        private static boolean isDue(String str) {
            return Utils.isDue(str.getBytes());
        }

        private static boolean isDue(byte[] data) {
            String[] strs = Utils.getDateInfoFromDate(data);
            if (strs != null && strs.length == 2) {
                String saveTimeStr = strs[0];
                while (saveTimeStr.startsWith("0")) {
                    saveTimeStr = saveTimeStr.substring(1, saveTimeStr.length());
                }
                long saveTime = Long.valueOf(saveTimeStr);
                long deleteAfter = Long.valueOf(strs[1]);
                if (System.currentTimeMillis() > saveTime + deleteAfter * 1000L) {
                    return true;
                }
            }
            return false;
        }

        private static String newStringWithDateInfo(int second, String strInfo) {
            return Utils.createDateInfo(second) + strInfo;
        }

        private static byte[] newByteArrayWithDateInfo(int second, byte[] data2) {
            byte[] data1 = Utils.createDateInfo(second).getBytes();
            byte[] retdata = new byte[data1.length + data2.length];
            System.arraycopy(data1, 0, retdata, 0, data1.length);
            System.arraycopy(data2, 0, retdata, data1.length, data2.length);
            return retdata;
        }

        private static String clearDateInfo(String strInfo) {
            if (strInfo != null && Utils.hasDateInfo(strInfo.getBytes())) {
                strInfo = strInfo.substring(strInfo.indexOf(32) + 1, strInfo.length());
            }
            return strInfo;
        }

        private static byte[] clearDateInfo(byte[] data) {
            if (Utils.hasDateInfo(data)) {
                return Utils.copyOfRange(data, Utils.indexOf(data, ' ') + 1, data.length);
            }
            return data;
        }

        private static boolean hasDateInfo(byte[] data) {
            return data != null && data.length > 15 && data[13] == 45 && Utils.indexOf(data, ' ') > 14;
        }

        private static String[] getDateInfoFromDate(byte[] data) {
            if (Utils.hasDateInfo(data)) {
                String saveDate = new String(Utils.copyOfRange(data, 0, 13));
                String deleteAfter = new String(Utils.copyOfRange(data, 14, Utils.indexOf(data, ' ')));
                return new String[]{saveDate, deleteAfter};
            }
            return null;
        }

        private static int indexOf(byte[] data, char c) {
            for (int i = 0; i < data.length; ++i) {
                if (data[i] != c) continue;
                return i;
            }
            return -1;
        }

        private static byte[] copyOfRange(byte[] original, int from, int to) {
            int newLength = to - from;
            if (newLength < 0) {
                throw new IllegalArgumentException(from + " > " + to);
            }
            byte[] copy = new byte[newLength];
            System.arraycopy(original, from, copy, 0, Math.min(original.length - from, newLength));
            return copy;
        }

        private static String createDateInfo(int second) {
            String currentTime = System.currentTimeMillis() + "";
            while (currentTime.length() < 13) {
                currentTime = "0" + currentTime;
            }
            return currentTime + "-" + second + ' ';
        }

        private static byte[] Bitmap2Bytes(Bitmap bm) {
            if (bm == null) {
                return null;
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.PNG, 100, (OutputStream)baos);
            return baos.toByteArray();
        }

        private static Bitmap Bytes2Bimap(byte[] b) {
            if (b.length == 0) {
                return null;
            }
            return BitmapFactory.decodeByteArray((byte[])b, (int)0, (int)b.length);
        }

        private static Bitmap drawable2Bitmap(Drawable drawable2) {
            if (drawable2 == null) {
                return null;
            }
            int w = drawable2.getIntrinsicWidth();
            int h = drawable2.getIntrinsicHeight();
            Bitmap.Config config = drawable2.getOpacity() != -1 ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;
            Bitmap bitmap = Bitmap.createBitmap((int)w, (int)h, (Bitmap.Config)config);
            Canvas canvas = new Canvas(bitmap);
            drawable2.setBounds(0, 0, w, h);
            drawable2.draw(canvas);
            return bitmap;
        }

        private static Drawable bitmap2Drawable(Bitmap bm) {
            if (bm == null) {
                return null;
            }
            BitmapDrawable bd = new BitmapDrawable(bm);
            bd.setTargetDensity(bm.getDensity());
            return new BitmapDrawable(bm);
        }
    }

    public class ACacheManager {
        private final AtomicLong cacheSize;
        private final AtomicInteger cacheCount;
        private final long sizeLimit;
        private final int countLimit;
        private final Map<File, Long> lastUsageDates = Collections.synchronizedMap(new HashMap());
        protected File cacheDir;

        private ACacheManager(File cacheDir, long sizeLimit, int countLimit) {
            this.cacheDir = cacheDir;
            this.sizeLimit = sizeLimit;
            this.countLimit = countLimit;
            this.cacheSize = new AtomicLong();
            this.cacheCount = new AtomicInteger();
            this.calculateCacheSizeAndCacheCount();
        }

        private void calculateCacheSizeAndCacheCount() {
            new Thread(new Runnable(){

                @Override
                public void run() {
                    int size = 0;
                    int count = 0;
                    File[] cachedFiles = ACacheManager.this.cacheDir.listFiles();
                    if (cachedFiles != null) {
                        for (File cachedFile : cachedFiles) {
                            size = (int)((long)size + ACacheManager.this.calculateSize(cachedFile));
                            ++count;
                            ACacheManager.this.lastUsageDates.put(cachedFile, cachedFile.lastModified());
                        }
                        ACacheManager.this.cacheSize.set(size);
                        ACacheManager.this.cacheCount.set(count);
                    }
                }
            }).start();
        }

        private void put(File file) {
            int curCacheCount = this.cacheCount.get();
            while (curCacheCount + 1 > this.countLimit) {
                long freedSize = this.removeNext();
                this.cacheSize.addAndGet(-freedSize);
                curCacheCount = this.cacheCount.addAndGet(-1);
            }
            this.cacheCount.addAndGet(1);
            long valueSize = this.calculateSize(file);
            long curCacheSize = this.cacheSize.get();
            while (curCacheSize + valueSize > this.sizeLimit) {
                long freedSize = this.removeNext();
                curCacheSize = this.cacheSize.addAndGet(-freedSize);
            }
            this.cacheSize.addAndGet(valueSize);
            Long currentTime = System.currentTimeMillis();
            file.setLastModified(currentTime);
            this.lastUsageDates.put(file, currentTime);
        }

        private File get(String key) {
            File file = this.newFile(key);
            Long currentTime = System.currentTimeMillis();
            file.setLastModified(currentTime);
            this.lastUsageDates.put(file, currentTime);
            return file;
        }

        private File newFile(String key) {
            return new File(this.cacheDir, key.hashCode() + "");
        }

        private boolean remove(String key) {
            File image = this.get(key);
            return image.delete();
        }

        private void clear() {
            this.lastUsageDates.clear();
            this.cacheSize.set(0L);
            File[] files = this.cacheDir.listFiles();
            if (files != null) {
                for (File f : files) {
                    f.delete();
                }
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        private long removeNext() {
            if (this.lastUsageDates.isEmpty()) {
                return 0L;
            }
            Long oldestUsage = null;
            File mostLongUsedFile = null;
            Set<Map.Entry<File, Long>> entries = this.lastUsageDates.entrySet();
            Map<File, Long> map = this.lastUsageDates;
            synchronized (map) {
                for (Map.Entry<File, Long> entry : entries) {
                    if (mostLongUsedFile == null) {
                        mostLongUsedFile = entry.getKey();
                        oldestUsage = entry.getValue();
                        continue;
                    }
                    Long lastValueUsage = entry.getValue();
                    if (lastValueUsage >= oldestUsage) continue;
                    oldestUsage = lastValueUsage;
                    mostLongUsedFile = entry.getKey();
                }
            }
            long fileSize = this.calculateSize(mostLongUsedFile);
            if (mostLongUsedFile.delete()) {
                this.lastUsageDates.remove(mostLongUsedFile);
            }
            return fileSize;
        }

        private long calculateSize(File file) {
            return file.length();
        }
    }

    class xFileOutputStream
    extends FileOutputStream {
        File file;

        public xFileOutputStream(File file) throws FileNotFoundException {
            super(file);
            this.file = file;
        }

        @Override
        public void close() throws IOException {
            super.close();
            ACache.this.mCache.put(this.file);
        }
    }
}

