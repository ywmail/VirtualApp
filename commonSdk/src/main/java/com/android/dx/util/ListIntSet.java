/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.util;

import com.android.dx.util.BitIntSet;
import com.android.dx.util.Bits;
import com.android.dx.util.IntIterator;
import com.android.dx.util.IntList;
import com.android.dx.util.IntSet;
import java.util.NoSuchElementException;

public class ListIntSet
implements IntSet {
    final IntList ints = new IntList();

    public ListIntSet() {
        this.ints.sort();
    }

    @Override
    public void add(int value) {
        int index = this.ints.binarysearch(value);
        if (index < 0) {
            this.ints.insert(-(index + 1), value);
        }
    }

    @Override
    public void remove(int value) {
        int index = this.ints.indexOf(value);
        if (index >= 0) {
            this.ints.removeIndex(index);
        }
    }

    @Override
    public boolean has(int value) {
        return this.ints.indexOf(value) >= 0;
    }

    @Override
    public void merge(IntSet other) {
        if (other instanceof ListIntSet) {
            ListIntSet o = (ListIntSet)other;
            int szThis = this.ints.size();
            int szOther = o.ints.size();
            int i = 0;
            int j = 0;
            while (j < szOther && i < szThis) {
                while (j < szOther && o.ints.get(j) < this.ints.get(i)) {
                    this.add(o.ints.get(j++));
                }
                if (j == szOther) break;
                while (i < szThis && o.ints.get(j) >= this.ints.get(i)) {
                    ++i;
                }
            }
            while (j < szOther) {
                this.add(o.ints.get(j++));
            }
            this.ints.sort();
        } else if (other instanceof BitIntSet) {
            BitIntSet o = (BitIntSet)other;
            int i = 0;
            while (i >= 0) {
                this.ints.add(i);
                i = Bits.findFirst(o.bits, i + 1);
            }
            this.ints.sort();
        } else {
            IntIterator iter = other.iterator();
            while (iter.hasNext()) {
                this.add(iter.next());
            }
        }
    }

    @Override
    public int elements() {
        return this.ints.size();
    }

    @Override
    public IntIterator iterator() {
        return new IntIterator(){
            private int idx = 0;

            @Override
            public boolean hasNext() {
                return this.idx < ListIntSet.this.ints.size();
            }

            @Override
            public int next() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }
                return ListIntSet.this.ints.get(this.idx++);
            }
        };
    }

    public String toString() {
        return this.ints.toString();
    }
}

