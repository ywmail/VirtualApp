//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.lody.virtual.server.am;

import com.lody.virtual.helper.collection.ArrayMap;
import com.lody.virtual.helper.collection.SparseArray;

class ProcessMap<E> {
    private final ArrayMap<String, SparseArray<E>> mMap = new ArrayMap();

    ProcessMap() {
    }

    public E get(String name, int uid) {
        SparseArray<E> uids = (SparseArray)this.mMap.get(name);
        return uids == null ? null : uids.get(uid);
    }

    public E put(String name, int uid, E value) {
        SparseArray<E> uids = (SparseArray)this.mMap.get(name);
        if (uids == null) {
            uids = new SparseArray(2);
            this.mMap.put(name, uids);
        }

        uids.put(uid, value);
        return value;
    }

    public E remove(String name, int uid) {
        SparseArray<E> uids = (SparseArray)this.mMap.get(name);
        if (uids != null) {
            E old = uids.removeReturnOld(uid);
            if (uids.size() == 0) {
                this.mMap.remove(name);
            }

            return old;
        } else {
            return null;
        }
    }

    public ArrayMap<String, SparseArray<E>> getMap() {
        return this.mMap;
    }
}
