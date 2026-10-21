/*
 * Decompiled with CFR 0.152.
 */
package com.lody.virtual.helper.collection;

import com.lody.virtual.StringFog;
import java.util.Arrays;

public class IntArray {
    private static final int[] EMPTY_ARRAY = new int[0];
    private int[] mData;
    private int mSize;

    private IntArray() {
    }

    public IntArray(int capacity) {
        this.mData = new int[capacity];
    }

    public static IntArray of(int ... values) {
        IntArray array2 = new IntArray();
        array2.mData = Arrays.copyOf(values, values.length);
        array2.mSize = values.length;
        return array2;
    }

    public void clear() {
        this.mSize = 0;
    }

    public void optimize() {
        if (this.mSize > this.mData.length) {
            this.mData = Arrays.copyOf(this.mData, this.mSize);
        }
    }

    public int[] getAll() {
        return this.mSize > 0 ? Arrays.copyOf(this.mData, this.mSize) : EMPTY_ARRAY;
    }

    public int get(int index) {
        return this.mData[index];
    }

    public int[] getRange(int start, int end) {
        return Arrays.copyOfRange(this.mData, start, end);
    }

    public void set(int index, int value) {
        if (index >= this.mSize) {
            throw new IndexOutOfBoundsException("Index " + index + " is greater than the list size " + this.mSize);
        }
        this.mData[index] = value;
    }

    private void ensureCapacity() {
        if (this.mSize <= this.mData.length) {
            return;
        }
        int newCap = this.mData.length;
        while (this.mSize > newCap) {
            newCap = newCap * 3 / 2 + 1;
        }
        this.mData = Arrays.copyOf(this.mData, newCap);
    }

    public int size() {
        return this.mSize;
    }

    public void addAll(int[] items) {
        int target = this.mSize;
        this.mSize += items.length;
        this.ensureCapacity();
        System.arraycopy(items, 0, this.mData, target, items.length);
    }

    public void add(int item) {
        ++this.mSize;
        this.ensureCapacity();
        this.mData[this.mSize - 1] = item;
    }

    public void remove(int index) {
        this.remove(index, 1);
    }

    public void remove(int index, int count) {
        System.arraycopy(this.mData, index + count, this.mData, index, this.mSize - index - count);
        this.mSize -= count;
    }

    public boolean contains(int item) {
        for (int i = 0; i < this.mSize; ++i) {
            if (this.mData[i] != item) continue;
            return true;
        }
        return false;
    }
}

