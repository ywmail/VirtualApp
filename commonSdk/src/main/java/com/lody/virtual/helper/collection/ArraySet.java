/*
 * Decompiled with CFR 0.152.
 */
package com.lody.virtual.helper.collection;

import com.lody.virtual.StringFog;
import com.lody.virtual.helper.collection.ContainerHelpers;
import com.lody.virtual.helper.collection.MapCollections;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public final class ArraySet<E>
implements Collection<E>,
Set<E> {
    private static final boolean DEBUG = false;
    private static final String TAG = "ArraySet";
    private static final int BASE_SIZE = 4;
    private static final int CACHE_SIZE = 10;
    static Object[] mBaseCache;
    static int mBaseCacheSize;
    static Object[] mTwiceBaseCache;
    static int mTwiceBaseCacheSize;
    int[] mHashes;
    Object[] mArray;
    int mSize;
    MapCollections<E, E> mCollections;

    public ArraySet() {
        this.mHashes = ContainerHelpers.EMPTY_INTS;
        this.mArray = ContainerHelpers.EMPTY_OBJECTS;
        this.mSize = 0;
    }

    public ArraySet(int capacity) {
        if (capacity == 0) {
            this.mHashes = ContainerHelpers.EMPTY_INTS;
            this.mArray = ContainerHelpers.EMPTY_OBJECTS;
        } else {
            this.allocArrays(capacity);
        }
        this.mSize = 0;
    }

    public ArraySet(ArraySet<E> set) {
        this();
        if (set != null) {
            this.addAll(set);
        }
    }

    public ArraySet(Collection<E> set) {
        this();
        if (set != null) {
            this.addAll(set);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private static void freeArrays(int[] hashes, Object[] array2, int size) {
        if (hashes.length == 8) {
            Class<ArraySet> clazz = ArraySet.class;
            synchronized (ArraySet.class) {
                if (mTwiceBaseCacheSize >= 10) return;
                array2[0] = mTwiceBaseCache;
                array2[1] = hashes;
                for (int i = size - 1; i >= 2; --i) {
                    array2[i] = null;
                }
                mTwiceBaseCache = array2;
                ++mTwiceBaseCacheSize;
                // ** MonitorExit[var3_3] (shouldn't be in output)
                return;
            }
        }
        if (hashes.length != 4) return;
        Class<ArraySet> clazz = ArraySet.class;
        synchronized (ArraySet.class) {
            if (mBaseCacheSize >= 10) return;
            array2[0] = mBaseCache;
            array2[1] = hashes;
            for (int i = size - 1; i >= 2; --i) {
                array2[i] = null;
            }
            mBaseCache = array2;
            ++mBaseCacheSize;
            // ** MonitorExit[var3_4] (shouldn't be in output)
            return;
        }
    }

    private int indexOf(Object key, int hash) {
        int end;
        int N = this.mSize;
        if (N == 0) {
            return -1;
        }
        int index = ContainerHelpers.binarySearch(this.mHashes, N, hash);
        if (index < 0) {
            return index;
        }
        if (key.equals(this.mArray[index])) {
            return index;
        }
        for (end = index + 1; end < N && this.mHashes[end] == hash; ++end) {
            if (!key.equals(this.mArray[end])) continue;
            return end;
        }
        for (int i = index - 1; i >= 0 && this.mHashes[i] == hash; --i) {
            if (!key.equals(this.mArray[i])) continue;
            return i;
        }
        return ~end;
    }

    private int indexOfNull() {
        int end;
        int N = this.mSize;
        if (N == 0) {
            return -1;
        }
        int index = ContainerHelpers.binarySearch(this.mHashes, N, 0);
        if (index < 0) {
            return index;
        }
        if (null == this.mArray[index]) {
            return index;
        }
        for (end = index + 1; end < N && this.mHashes[end] == 0; ++end) {
            if (null != this.mArray[end]) continue;
            return end;
        }
        for (int i = index - 1; i >= 0 && this.mHashes[i] == 0; --i) {
            if (null != this.mArray[i]) continue;
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
            Class<ArraySet> clazz = ArraySet.class;
            // MONITORENTER : com.lody.virtual.helper.collection.ArraySet.class
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
            Class<ArraySet> clazz = ArraySet.class;
            // MONITORENTER : com.lody.virtual.helper.collection.ArraySet.class
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
        this.mArray = new Object[size];
    }

    @Override
    public void clear() {
        if (this.mSize != 0) {
            ArraySet.freeArrays(this.mHashes, this.mArray, this.mSize);
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
                System.arraycopy(oarray, 0, this.mArray, 0, this.mSize);
            }
            ArraySet.freeArrays(ohashes, oarray, this.mSize);
        }
    }

    @Override
    public boolean contains(Object key) {
        return this.indexOf(key) >= 0;
    }

    public int indexOf(Object key) {
        return key == null ? this.indexOfNull() : this.indexOf(key, key.hashCode());
    }

    public E valueAt(int index) {
        return (E)this.mArray[index];
    }

    @Override
    public boolean isEmpty() {
        return this.mSize <= 0;
    }

    @Override
    public boolean add(E value) {
        int index;
        int hash;
        if (value == null) {
            hash = 0;
            index = this.indexOfNull();
        } else {
            hash = value.hashCode();
            index = this.indexOf(value, hash);
        }
        if (index >= 0) {
            return false;
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
            ArraySet.freeArrays(ohashes, oarray, this.mSize);
        }
        if (index < this.mSize) {
            System.arraycopy(this.mHashes, index, this.mHashes, index + 1, this.mSize - index);
            System.arraycopy(this.mArray, index, this.mArray, index + 1, this.mSize - index);
        }
        this.mHashes[index] = hash;
        this.mArray[index] = value;
        ++this.mSize;
        return true;
    }

    public void addAll(ArraySet<? extends E> array2) {
        int N = array2.mSize;
        this.ensureCapacity(this.mSize + N);
        if (this.mSize == 0) {
            if (N > 0) {
                System.arraycopy(array2.mHashes, 0, this.mHashes, 0, N);
                System.arraycopy(array2.mArray, 0, this.mArray, 0, N);
                this.mSize = N;
            }
        } else {
            for (int i = 0; i < N; ++i) {
                this.add(array2.valueAt(i));
            }
        }
    }

    @Override
    public boolean remove(Object object) {
        int index = this.indexOf(object);
        if (index >= 0) {
            this.removeAt(index);
            return true;
        }
        return false;
    }

    public E removeAt(int index) {
        Object old = this.mArray[index];
        if (this.mSize <= 1) {
            ArraySet.freeArrays(this.mHashes, this.mArray, this.mSize);
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
                System.arraycopy(oarray, 0, this.mArray, 0, index);
            }
            if (index < this.mSize) {
                System.arraycopy(ohashes, index + 1, this.mHashes, index, this.mSize - index);
                System.arraycopy(oarray, index + 1, this.mArray, index, this.mSize - index);
            }
        } else {
            --this.mSize;
            if (index < this.mSize) {
                System.arraycopy(this.mHashes, index + 1, this.mHashes, index, this.mSize - index);
                System.arraycopy(this.mArray, index + 1, this.mArray, index, this.mSize - index);
            }
            this.mArray[this.mSize] = null;
        }
        return (E)old;
    }


    public boolean removeAll(ArraySet<? extends E> array2) {
        int N = array2.mSize;
        int originalSize = this.mSize;
        for (int i = 0; i < N; ++i) {
            this.remove(array2.valueAt(i));
        }
        return originalSize != this.mSize;
    }

    @Override
    public int size() {
        return this.mSize;
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[this.mSize];
        System.arraycopy(this.mArray, 0, result, 0, this.mSize);
        return result;
    }

    @Override
    public <T> T[] toArray(T[] array) {
        if (array.length < this.mSize) {
            T[] newArray = (T[]) Array.newInstance(array.getClass().getComponentType(), this.mSize);
            array = newArray;
        }

        System.arraycopy(this.mArray, 0, array, 0, this.mSize);
        if (array.length > this.mSize) {
            array[this.mSize] = null;
        }

        return array;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object instanceof Set) {
            Set set = (Set)object;
            if (this.size() != set.size()) {
                return false;
            }
            try {
                for (int i = 0; i < this.mSize; ++i) {
                    E mine = this.valueAt(i);
                    if (set.contains(mine)) continue;
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

    @Override
    public int hashCode() {
        int[] hashes = this.mHashes;
        int result = 0;
        int s = this.mSize;
        for (int i = 0; i < s; ++i) {
            result += hashes[i];
        }
        return result;
    }

    public String toString() {
        if (this.isEmpty()) {
            return "{}";
        }
        StringBuilder buffer = new StringBuilder(this.mSize * 14);
        buffer.append('{');
        for (int i = 0; i < this.mSize; ++i) {
            E value;
            if (i > 0) {
                buffer.append(", ");
            }
            if ((value = this.valueAt(i)) != this) {
                buffer.append(value);
                continue;
            }
            buffer.append("(this Set)");
        }
        buffer.append('}');
        return buffer.toString();
    }

    private MapCollections<E, E> getCollection() {
        if (this.mCollections == null) {
            this.mCollections = new MapCollections<E, E>(){

                @Override
                protected int colGetSize() {
                    return ArraySet.this.mSize;
                }

                @Override
                protected Object colGetEntry(int index, int offset) {
                    return ArraySet.this.mArray[index];
                }

                @Override
                protected int colIndexOfKey(Object key) {
                    return ArraySet.this.indexOf(key);
                }

                @Override
                protected int colIndexOfValue(Object value) {
                    return ArraySet.this.indexOf(value);
                }

                @Override
                protected Map<E, E> colGetMap() {
                    throw new UnsupportedOperationException("not a map");
                }

                @Override
                protected void colPut(E key, E value) {
                    ArraySet.this.add(key);
                }

                @Override
                protected E colSetValue(int index, E value) {
                    throw new UnsupportedOperationException("not a map");
                }

                @Override
                protected void colRemoveAt(int index) {
                    ArraySet.this.removeAt(index);
                }

                @Override
                protected void colClear() {
                    ArraySet.this.clear();
                }
            };
        }
        return this.mCollections;
    }

    @Override
    public Iterator<E> iterator() {
        return this.getCollection().getKeySet().iterator();
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        Iterator<?> it = collection.iterator();
        while (it.hasNext()) {
            if (this.contains(it.next())) continue;
            return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> collection) {
        this.ensureCapacity(this.mSize + collection.size());
        boolean added = false;
        for (E value : collection) {
            added |= this.add(value);
        }
        return added;
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        boolean removed = false;
        for (Object value : collection) {
            removed |= this.remove(value);
        }
        return removed;
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        boolean removed = false;
        for (int i = this.mSize - 1; i >= 0; --i) {
            if (collection.contains(this.mArray[i])) continue;
            this.removeAt(i);
            removed = true;
        }
        return removed;
    }
}

