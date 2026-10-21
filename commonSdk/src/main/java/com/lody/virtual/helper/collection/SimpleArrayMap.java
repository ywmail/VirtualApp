/*
 * Decompiled with CFR 0.152.
 */
package com.lody.virtual.helper.collection;

import com.lody.virtual.StringFog;
import com.lody.virtual.helper.collection.ArrayMap;
import com.lody.virtual.helper.collection.ContainerHelpers;
import java.util.Map;

public class SimpleArrayMap<K, V> {
    private static final boolean DEBUG = false;
    private static final String TAG = "ArrayMap";
    private static final int BASE_SIZE = 4;
    private static final int CACHE_SIZE = 10;
    static Object[] mBaseCache;
    static int mBaseCacheSize;
    static Object[] mTwiceBaseCache;
    static int mTwiceBaseCacheSize;
    int[] mHashes;
    Object[] mArray;
    int mSize;

    public SimpleArrayMap() {
        this.mHashes = ContainerHelpers.EMPTY_INTS;
        this.mArray = ContainerHelpers.EMPTY_OBJECTS;
        this.mSize = 0;
    }

    public SimpleArrayMap(int capacity) {
        if (capacity == 0) {
            this.mHashes = ContainerHelpers.EMPTY_INTS;
            this.mArray = ContainerHelpers.EMPTY_OBJECTS;
        } else {
            this.allocArrays(capacity);
        }
        this.mSize = 0;
    }

    public SimpleArrayMap(SimpleArrayMap map) {
        this();
        if (map != null) {
            this.putAll(map);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private static void freeArrays(int[] hashes, Object[] array2, int size) {
        if (hashes.length == 8) {
            Class<ArrayMap> clazz = ArrayMap.class;
            synchronized (ArrayMap.class) {
                if (mTwiceBaseCacheSize >= 10) return;
                array2[0] = mTwiceBaseCache;
                array2[1] = hashes;
                for (int i = (size << 1) - 1; i >= 2; --i) {
                    array2[i] = null;
                }
                mTwiceBaseCache = array2;
                ++mTwiceBaseCacheSize;
                // ** MonitorExit[var3_3] (shouldn't be in output)
                return;
            }
        }
        if (hashes.length != 4) return;
        Class<ArrayMap> clazz = ArrayMap.class;
        synchronized (ArrayMap.class) {
            if (mBaseCacheSize >= 10) return;
            array2[0] = mBaseCache;
            array2[1] = hashes;
            for (int i = (size << 1) - 1; i >= 2; --i) {
                array2[i] = null;
            }
            mBaseCache = array2;
            ++mBaseCacheSize;
            // ** MonitorExit[var3_4] (shouldn't be in output)
            return;
        }
    }

    int indexOf(Object key, int hash) {
        int end;
        int N = this.mSize;
        if (N == 0) {
            return -1;
        }
        int index = ContainerHelpers.binarySearch(this.mHashes, N, hash);
        if (index < 0) {
            return index;
        }
        if (key.equals(this.mArray[index << 1])) {
            return index;
        }
        for (end = index + 1; end < N && this.mHashes[end] == hash; ++end) {
            if (!key.equals(this.mArray[end << 1])) continue;
            return end;
        }
        for (int i = index - 1; i >= 0 && this.mHashes[i] == hash; --i) {
            if (!key.equals(this.mArray[i << 1])) continue;
            return i;
        }
        return ~end;
    }

    int indexOfNull() {
        int end;
        int N = this.mSize;
        if (N == 0) {
            return -1;
        }
        int index = ContainerHelpers.binarySearch(this.mHashes, N, 0);
        if (index < 0) {
            return index;
        }
        if (null == this.mArray[index << 1]) {
            return index;
        }
        for (end = index + 1; end < N && this.mHashes[end] == 0; ++end) {
            if (null != this.mArray[end << 1]) continue;
            return end;
        }
        for (int i = index - 1; i >= 0 && this.mHashes[i] == 0; --i) {
            if (null != this.mArray[i << 1]) continue;
            return i;
        }
        return ~end;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    private void allocArrays(int size) {
        if (size == 8) {
            Class<ArrayMap> clazz = ArrayMap.class;
            // MONITORENTER : com.lody.virtual.helper.collection.ArrayMap.class
            if (mTwiceBaseCache != null) {
                Object[] array2 = mTwiceBaseCache;
                this.mArray = array2;
                mTwiceBaseCache = (Object[])array2[0];
                this.mHashes = (int[])array2[1];
                array2[1] = null;
                array2[0] = null;
                --mTwiceBaseCacheSize;
                // MONITOREXIT : clazz
                return;
            }
            // MONITOREXIT : clazz
        } else if (size == 4) {
            Class<ArrayMap> clazz = ArrayMap.class;
            // MONITORENTER : com.lody.virtual.helper.collection.ArrayMap.class
            if (mBaseCache != null) {
                Object[] array3 = mBaseCache;
                this.mArray = array3;
                mBaseCache = (Object[])array3[0];
                this.mHashes = (int[])array3[1];
                array3[1] = null;
                array3[0] = null;
                --mBaseCacheSize;
                // MONITOREXIT : clazz
                return;
            }
            // MONITOREXIT : clazz
        }
        this.mHashes = new int[size];
        this.mArray = new Object[size << 1];
    }

    public void clear() {
        if (this.mSize != 0) {
            SimpleArrayMap.freeArrays(this.mHashes, this.mArray, this.mSize);
            this.mHashes = ContainerHelpers.EMPTY_INTS;
            this.mArray = ContainerHelpers.EMPTY_OBJECTS;
            this.mSize = 0;
        }
    }

    public void ensureCapacity(int minimumCapacity) {
        if (this.mHashes.length < minimumCapacity) {
            int[] ohashes = this.mHashes;
            Object[] oarray = this.mArray;
            this.allocArrays(minimumCapacity);
            if (this.mSize > 0) {
                System.arraycopy(ohashes, 0, this.mHashes, 0, this.mSize);
                System.arraycopy(oarray, 0, this.mArray, 0, this.mSize << 1);
            }
            SimpleArrayMap.freeArrays(ohashes, oarray, this.mSize);
        }
    }

    public boolean containsKey(Object key) {
        return this.indexOfKey(key) >= 0;
    }

    public int indexOfKey(Object key) {
        return key == null ? this.indexOfNull() : this.indexOf(key, key.hashCode());
    }

    int indexOfValue(Object value) {
        int N = this.mSize * 2;
        Object[] array2 = this.mArray;
        if (value == null) {
            for (int i = 1; i < N; i += 2) {
                if (array2[i] != null) continue;
                return i >> 1;
            }
        } else {
            for (int i = 1; i < N; i += 2) {
                if (!value.equals(array2[i])) continue;
                return i >> 1;
            }
        }
        return -1;
    }

    public boolean containsValue(Object value) {
        return this.indexOfValue(value) >= 0;
    }

    public V get(Object key) {
        int index = this.indexOfKey(key);
        return (V)(index >= 0 ? this.mArray[(index << 1) + 1] : null);
    }

    public K keyAt(int index) {
        return (K)this.mArray[index << 1];
    }

    public V valueAt(int index) {
        return (V)this.mArray[(index << 1) + 1];
    }

    public V setValueAt(int index, V value) {
        index = (index << 1) + 1;
        Object old = this.mArray[index];
        this.mArray[index] = value;
        return (V)old;
    }

    public boolean isEmpty() {
        return this.mSize <= 0;
    }

    public V put(K key, V value) {
        int index;
        int hash;
        if (key == null) {
            hash = 0;
            index = this.indexOfNull();
        } else {
            hash = key.hashCode();
            index = this.indexOf(key, hash);
        }
        if (index >= 0) {
            index = (index << 1) + 1;
            Object old = this.mArray[index];
            this.mArray[index] = value;
            return (V)old;
        }
        index ^= 0xFFFFFFFF;
        if (this.mSize >= this.mHashes.length) {
            int n = this.mSize >= 8 ? this.mSize + (this.mSize >> 1) : (this.mSize >= 4 ? 8 : 4);
            int[] ohashes = this.mHashes;
            Object[] oarray = this.mArray;
            this.allocArrays(n);
            if (this.mHashes.length > 0) {
                System.arraycopy(ohashes, 0, this.mHashes, 0, ohashes.length);
                System.arraycopy(oarray, 0, this.mArray, 0, oarray.length);
            }
            SimpleArrayMap.freeArrays(ohashes, oarray, this.mSize);
        }
        if (index < this.mSize) {
            System.arraycopy(this.mHashes, index, this.mHashes, index + 1, this.mSize - index);
            System.arraycopy(this.mArray, index << 1, this.mArray, index + 1 << 1, this.mSize - index << 1);
        }
        this.mHashes[index] = hash;
        this.mArray[index << 1] = key;
        this.mArray[(index << 1) + 1] = value;
        ++this.mSize;
        return null;
    }

    public void putAll(SimpleArrayMap<? extends K, ? extends V> array2) {
        int N = array2.mSize;
        this.ensureCapacity(this.mSize + N);
        if (this.mSize == 0) {
            if (N > 0) {
                System.arraycopy(array2.mHashes, 0, this.mHashes, 0, N);
                System.arraycopy(array2.mArray, 0, this.mArray, 0, N << 1);
                this.mSize = N;
            }
        } else {
            for (int i = 0; i < N; ++i) {
                this.put(array2.keyAt(i), array2.valueAt(i));
            }
        }
    }

    public V remove(Object key) {
        int index = this.indexOfKey(key);
        if (index >= 0) {
            return this.removeAt(index);
        }
        return null;
    }

    public V removeAt(int index) {
        Object old = this.mArray[(index << 1) + 1];
        if (this.mSize <= 1) {
            SimpleArrayMap.freeArrays(this.mHashes, this.mArray, this.mSize);
            this.mHashes = ContainerHelpers.EMPTY_INTS;
            this.mArray = ContainerHelpers.EMPTY_OBJECTS;
            this.mSize = 0;
        } else if (this.mHashes.length > 8 && this.mSize < this.mHashes.length / 3) {
            int n = this.mSize > 8 ? this.mSize + (this.mSize >> 1) : 8;
            int[] ohashes = this.mHashes;
            Object[] oarray = this.mArray;
            this.allocArrays(n);
            --this.mSize;
            if (index > 0) {
                System.arraycopy(ohashes, 0, this.mHashes, 0, index);
                System.arraycopy(oarray, 0, this.mArray, 0, index << 1);
            }
            if (index < this.mSize) {
                System.arraycopy(ohashes, index + 1, this.mHashes, index, this.mSize - index);
                System.arraycopy(oarray, index + 1 << 1, this.mArray, index << 1, this.mSize - index << 1);
            }
        } else {
            --this.mSize;
            if (index < this.mSize) {
                System.arraycopy(this.mHashes, index + 1, this.mHashes, index, this.mSize - index);
                System.arraycopy(this.mArray, index + 1 << 1, this.mArray, index << 1, this.mSize - index << 1);
            }
            this.mArray[this.mSize << 1] = null;
            this.mArray[(this.mSize << 1) + 1] = null;
        }
        return (V)old;
    }

    public int size() {
        return this.mSize;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object instanceof Map) {
            Map map = (Map)object;
            if (this.size() != map.size()) {
                return false;
            }
            try {
                for (int i = 0; i < this.mSize; ++i) {
                    K key = this.keyAt(i);
                    V mine = this.valueAt(i);
                    Object theirs = map.get(key);
                    if (!(mine == null ? theirs != null || !map.containsKey(key) : !mine.equals(theirs))) continue;
                    return false;
                }
            }
            catch (NullPointerException ignored) {
                return false;
            }
            catch (ClassCastException ignored) {
                return false;
            }
            return true;
        }
        return false;
    }

    public int hashCode() {
        int[] hashes = this.mHashes;
        Object[] array2 = this.mArray;
        int result = 0;
        int i = 0;
        int v = 1;
        int s = this.mSize;
        while (i < s) {
            Object value = array2[v];
            result += hashes[i] ^ (value == null ? 0 : value.hashCode());
            ++i;
            v += 2;
        }
        return result;
    }

    public String toString() {
        if (this.isEmpty()) {
            return "{}";
        }
        StringBuilder buffer = new StringBuilder(this.mSize * 28);
        buffer.append('{');
        for (int i = 0; i < this.mSize; ++i) {
            K key;
            if (i > 0) {
                buffer.append(", ");
            }
            if ((key = this.keyAt(i)) != this) {
                buffer.append(key);
            } else {
                buffer.append("(this Map)");
            }
            buffer.append('=');
            V value = this.valueAt(i);
            if (value != this) {
                buffer.append(value);
                continue;
            }
            buffer.append("(this Map)");
        }
        buffer.append('}');
        return buffer.toString();
    }
}

