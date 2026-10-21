/*
 * Decompiled with CFR 0.152.
 */
package com.lody.virtual.helper.collection;

import com.lody.virtual.helper.collection.MapCollections;
import com.lody.virtual.helper.collection.SimpleArrayMap;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class ArrayMap<K, V>
extends SimpleArrayMap<K, V>
implements Map<K, V> {
    MapCollections<K, V> mCollections;

    public ArrayMap() {
    }

    public ArrayMap(int capacity) {
        super(capacity);
    }

    public ArrayMap(SimpleArrayMap map) {
        super(map);
    }

    private MapCollections<K, V> getCollection() {
        if (this.mCollections == null) {
            this.mCollections = new MapCollections<K, V>(){

                @Override
                protected int colGetSize() {
                    return ArrayMap.this.mSize;
                }

                @Override
                protected Object colGetEntry(int index, int offset) {
                    return ArrayMap.this.mArray[(index << 1) + offset];
                }

                @Override
                protected int colIndexOfKey(Object key) {
                    return ArrayMap.this.indexOfKey(key);
                }

                @Override
                protected int colIndexOfValue(Object value) {
                    return ArrayMap.this.indexOfValue(value);
                }

                @Override
                protected Map<K, V> colGetMap() {
                    return ArrayMap.this;
                }

                @Override
                protected void colPut(K key, V value) {
                    ArrayMap.this.put(key, value);
                }

                @Override
                protected V colSetValue(int index, V value) {
                    return ArrayMap.this.setValueAt(index, value);
                }

                @Override
                protected void colRemoveAt(int index) {
                    ArrayMap.this.removeAt(index);
                }

                @Override
                protected void colClear() {
                    ArrayMap.this.clear();
                }
            };
        }
        return this.mCollections;
    }

    public boolean containsAll(Collection<?> collection) {
        return MapCollections.containsAllHelper(this, collection);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> map) {
        this.ensureCapacity(this.mSize + map.size());
        Iterator var2 = map.entrySet().iterator();

        while(var2.hasNext()) {
            Map.Entry<? extends K, ? extends V> entry = (Map.Entry)var2.next();
            this.put(entry.getKey(), entry.getValue());
        }

    }

    public boolean removeAll(Collection<?> collection) {
        return MapCollections.removeAllHelper(this, collection);
    }

    public boolean retainAll(Collection<?> collection) {
        return MapCollections.retainAllHelper(this, collection);
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        return this.getCollection().getEntrySet();
    }

    @Override
    public Set<K> keySet() {
        return this.getCollection().getKeySet();
    }

    @Override
    public Collection<V> values() {
        return this.getCollection().getValues();
    }
}

