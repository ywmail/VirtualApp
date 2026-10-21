/*
 * Decompiled with CFR 0.152.
 */
package com.lody.virtual.helper.collection;

import com.lody.virtual.StringFog;
import com.lody.virtual.helper.collection.ContainerHelpers;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

abstract class MapCollections<K, V> {
    EntrySet mEntrySet;
    KeySet mKeySet;
    ValuesCollection mValues;

    MapCollections() {
    }

    public static <K, V> boolean containsAllHelper(Map<K, V> map, Collection<?> collection) {
        Iterator<?> it = collection.iterator();
        while (it.hasNext()) {
            if (map.containsKey(it.next())) continue;
            return false;
        }
        return true;
    }

    public static <K, V> boolean removeAllHelper(Map<K, V> map, Collection<?> collection) {
        int oldSize = map.size();
        Iterator<?> it = collection.iterator();
        while (it.hasNext()) {
            map.remove(it.next());
        }
        return oldSize != map.size();
    }

    public static <K, V> boolean retainAllHelper(Map<K, V> map, Collection<?> collection) {
        int oldSize = map.size();
        Iterator<K> it = map.keySet().iterator();
        while (it.hasNext()) {
            if (collection.contains(it.next())) continue;
            it.remove();
        }
        return oldSize != map.size();
    }

    public static <T> boolean equalsSetHelper(Set<T> set, Object object) {
        if (set == object) {
            return true;
        }
        if (object instanceof Set) {
            Set s = (Set)object;
            try {
                return set.size() == s.size() && set.containsAll(s);
            }
            catch (NullPointerException ignored) {
                return false;
            }
            catch (ClassCastException ignored) {
                return false;
            }
        }
        return false;
    }

    public Object[] toArrayHelper(int offset) {
        int N = this.colGetSize();
        Object[] result = new Object[N];
        for (int i = 0; i < N; ++i) {
            result[i] = this.colGetEntry(i, offset);
        }
        return result;
    }

    public <T> T[] toArrayHelper(T[] array, int offset) {
        int N = this.colGetSize();
        if (array.length < N) {
            T[] newArray = (T[]) Array.newInstance(array.getClass().getComponentType(), N);
            array = newArray;
        }

        for(int i = 0; i < N; ++i) {
            array[i] = (T) this.colGetEntry(i, offset);
        }

        if (array.length > N) {
            array[N] = null;
        }

        return array;
    }

    public Set<Map.Entry<K, V>> getEntrySet() {
        if (this.mEntrySet == null) {
            this.mEntrySet = new EntrySet();
        }
        return this.mEntrySet;
    }

    public Set<K> getKeySet() {
        if (this.mKeySet == null) {
            this.mKeySet = new KeySet();
        }
        return this.mKeySet;
    }

    public Collection<V> getValues() {
        if (this.mValues == null) {
            this.mValues = new ValuesCollection();
        }
        return this.mValues;
    }

    protected abstract int colGetSize();

    protected abstract Object colGetEntry(int var1, int var2);

    protected abstract int colIndexOfKey(Object var1);

    protected abstract int colIndexOfValue(Object var1);

    protected abstract Map<K, V> colGetMap();

    protected abstract void colPut(K var1, V var2);

    protected abstract V colSetValue(int var1, V var2);

    protected abstract void colRemoveAt(int var1);

    protected abstract void colClear();

    final class ValuesCollection
    implements Collection<V> {
        ValuesCollection() {
        }

        @Override
        public boolean add(V object) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean addAll(Collection<? extends V> collection) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void clear() {
            MapCollections.this.colClear();
        }

        @Override
        public boolean contains(Object object) {
            return MapCollections.this.colIndexOfValue(object) >= 0;
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
        public boolean isEmpty() {
            return MapCollections.this.colGetSize() == 0;
        }

        @Override
        public Iterator<V> iterator() {
            return new ArrayIterator(1);
        }

        @Override
        public boolean remove(Object object) {
            int index = MapCollections.this.colIndexOfValue(object);
            if (index >= 0) {
                MapCollections.this.colRemoveAt(index);
                return true;
            }
            return false;
        }

        @Override
        public boolean removeAll(Collection<?> collection) {
            int N = MapCollections.this.colGetSize();
            boolean changed = false;
            for (int i = 0; i < N; ++i) {
                Object cur = MapCollections.this.colGetEntry(i, 1);
                if (!collection.contains(cur)) continue;
                MapCollections.this.colRemoveAt(i);
                --i;
                --N;
                changed = true;
            }
            return changed;
        }

        @Override
        public boolean retainAll(Collection<?> collection) {
            int N = MapCollections.this.colGetSize();
            boolean changed = false;
            for (int i = 0; i < N; ++i) {
                Object cur = MapCollections.this.colGetEntry(i, 1);
                if (collection.contains(cur)) continue;
                MapCollections.this.colRemoveAt(i);
                --i;
                --N;
                changed = true;
            }
            return changed;
        }

        @Override
        public int size() {
            return MapCollections.this.colGetSize();
        }

        @Override
        public Object[] toArray() {
            return MapCollections.this.toArrayHelper(1);
        }

        @Override
        public <T> T[] toArray(T[] array2) {
            return MapCollections.this.toArrayHelper(array2, 1);
        }
    }

    final class KeySet
    implements Set<K> {
        KeySet() {
        }

        @Override
        public boolean add(K object) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean addAll(Collection<? extends K> collection) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void clear() {
            MapCollections.this.colClear();
        }

        @Override
        public boolean contains(Object object) {
            return MapCollections.this.colIndexOfKey(object) >= 0;
        }

        @Override
        public boolean containsAll(Collection<?> collection) {
            return MapCollections.containsAllHelper(MapCollections.this.colGetMap(), collection);
        }

        @Override
        public boolean isEmpty() {
            return MapCollections.this.colGetSize() == 0;
        }

        @Override
        public Iterator<K> iterator() {
            return new ArrayIterator(0);
        }

        @Override
        public boolean remove(Object object) {
            int index = MapCollections.this.colIndexOfKey(object);
            if (index >= 0) {
                MapCollections.this.colRemoveAt(index);
                return true;
            }
            return false;
        }

        @Override
        public boolean removeAll(Collection<?> collection) {
            return MapCollections.removeAllHelper(MapCollections.this.colGetMap(), collection);
        }

        @Override
        public boolean retainAll(Collection<?> collection) {
            return MapCollections.retainAllHelper(MapCollections.this.colGetMap(), collection);
        }

        @Override
        public int size() {
            return MapCollections.this.colGetSize();
        }

        @Override
        public Object[] toArray() {
            return MapCollections.this.toArrayHelper(0);
        }

        @Override
        public <T> T[] toArray(T[] array2) {
            return MapCollections.this.toArrayHelper(array2, 0);
        }

        @Override
        public boolean equals(Object object) {
            return MapCollections.equalsSetHelper(this, object);
        }

        @Override
        public int hashCode() {
            int result = 0;
            for (int i = MapCollections.this.colGetSize() - 1; i >= 0; --i) {
                Object obj = MapCollections.this.colGetEntry(i, 0);
                result += obj == null ? 0 : obj.hashCode();
            }
            return result;
        }
    }

    final class EntrySet
    implements Set<Map.Entry<K, V>> {
        EntrySet() {
        }

        @Override
        public boolean add(Map.Entry<K, V> object) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean addAll(Collection<? extends Map.Entry<K, V>> collection) {
            int oldSize = MapCollections.this.colGetSize();
            Iterator var3 = collection.iterator();

            while(var3.hasNext()) {
                Map.Entry<K, V> entry = (Map.Entry)var3.next();
                MapCollections.this.colPut(entry.getKey(), entry.getValue());
            }

            return oldSize != MapCollections.this.colGetSize();
        }

        @Override
        public void clear() {
            MapCollections.this.colClear();
        }

        @Override
        public boolean contains(Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            Map.Entry e = (Map.Entry)o;
            int index = MapCollections.this.colIndexOfKey(e.getKey());
            if (index < 0) {
                return false;
            }
            Object foundVal = MapCollections.this.colGetEntry(index, 1);
            return ContainerHelpers.equal(foundVal, e.getValue());
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
        public boolean isEmpty() {
            return MapCollections.this.colGetSize() == 0;
        }

        @Override
        public Iterator<Map.Entry<K, V>> iterator() {
            return new MapIterator();
        }

        @Override
        public boolean remove(Object object) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean removeAll(Collection<?> collection) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean retainAll(Collection<?> collection) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int size() {
            return MapCollections.this.colGetSize();
        }

        @Override
        public Object[] toArray() {
            throw new UnsupportedOperationException();
        }

        @Override
        public <T> T[] toArray(T[] array2) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean equals(Object object) {
            return MapCollections.equalsSetHelper(this, object);
        }

        @Override
        public int hashCode() {
            int result = 0;
            for (int i = MapCollections.this.colGetSize() - 1; i >= 0; --i) {
                Object key = MapCollections.this.colGetEntry(i, 0);
                Object value = MapCollections.this.colGetEntry(i, 1);
                result += (key == null ? 0 : key.hashCode()) ^ (value == null ? 0 : value.hashCode());
            }
            return result;
        }
    }

    final class MapIterator
    implements Iterator<Map.Entry<K, V>>,
    Map.Entry<K, V> {
        int mEnd;
        int mIndex;
        boolean mEntryValid = false;

        MapIterator() {
            this.mEnd = MapCollections.this.colGetSize() - 1;
            this.mIndex = -1;
        }

        @Override
        public boolean hasNext() {
            return this.mIndex < this.mEnd;
        }

        @Override
        public Map.Entry<K, V> next() {
            ++this.mIndex;
            this.mEntryValid = true;
            return this;
        }

        @Override
        public void remove() {
            if (!this.mEntryValid) {
                throw new IllegalStateException();
            }
            MapCollections.this.colRemoveAt(this.mIndex);
            --this.mIndex;
            --this.mEnd;
            this.mEntryValid = false;
        }

        @Override
        public K getKey() {
            if (!this.mEntryValid) {
                throw new IllegalStateException("This container does not support retaining Map.Entry objects");
            } else {
                return (K) MapCollections.this.colGetEntry(this.mIndex, 0);
            }
        }

        @Override
        public V getValue() {
            if (!this.mEntryValid) {
                throw new IllegalStateException("This container does not support retaining Map.Entry objects");
            } else {
                return (V) MapCollections.this.colGetEntry(this.mIndex, 1);
            }
        }

        @Override
        public V setValue(V object) {
            if (!this.mEntryValid) {
                throw new IllegalStateException("This container does not support retaining Map.Entry objects");
            }
            return MapCollections.this.colSetValue(this.mIndex, object);
        }

        @Override
        public final boolean equals(Object o) {
            if (!this.mEntryValid) {
                throw new IllegalStateException("This container does not support retaining Map.Entry objects");
            }
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            Map.Entry e = (Map.Entry)o;
            return ContainerHelpers.equal(e.getKey(), MapCollections.this.colGetEntry(this.mIndex, 0)) && ContainerHelpers.equal(e.getValue(), MapCollections.this.colGetEntry(this.mIndex, 1));
        }

        @Override
        public final int hashCode() {
            if (!this.mEntryValid) {
                throw new IllegalStateException("This container does not support retaining Map.Entry objects");
            }
            Object key = MapCollections.this.colGetEntry(this.mIndex, 0);
            Object value = MapCollections.this.colGetEntry(this.mIndex, 1);
            return (key == null ? 0 : key.hashCode()) ^ (value == null ? 0 : value.hashCode());
        }

        public final String toString() {
            return this.getKey() + "=" + this.getValue();
        }
    }

    final class ArrayIterator<T>
    implements Iterator<T> {
        final int mOffset;
        int mSize;
        int mIndex;
        boolean mCanRemove = false;

        ArrayIterator(int offset) {
            this.mOffset = offset;
            this.mSize = MapCollections.this.colGetSize();
        }

        @Override
        public boolean hasNext() {
            return this.mIndex < this.mSize;
        }

        @Override
        public T next() {
            Object res = MapCollections.this.colGetEntry(this.mIndex, this.mOffset);
            ++this.mIndex;
            this.mCanRemove = true;
            return (T)res;
        }

        @Override
        public void remove() {
            if (!this.mCanRemove) {
                throw new IllegalStateException();
            }
            --this.mIndex;
            --this.mSize;
            this.mCanRemove = false;
            MapCollections.this.colRemoveAt(this.mIndex);
        }
    }
}

