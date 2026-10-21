/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.util.SparseBooleanArray
 */
package com.lody.virtual.helper;

import android.util.SparseBooleanArray;

public class MultiAvoidRecursive {
    private SparseBooleanArray mCallings;

    public MultiAvoidRecursive(int initialCapacity) {
        this.mCallings = new SparseBooleanArray(initialCapacity);
    }

    public MultiAvoidRecursive() {
        this(7);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public boolean beginCall(int id2) {
        SparseBooleanArray sparseBooleanArray = this.mCallings;
        synchronized (sparseBooleanArray) {
            if (this.mCallings.get(id2)) {
                return false;
            }
            this.mCallings.put(id2, true);
            return true;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void finishCall(int id2) {
        SparseBooleanArray sparseBooleanArray = this.mCallings;
        synchronized (sparseBooleanArray) {
            this.mCallings.put(id2, false);
        }
    }
}

