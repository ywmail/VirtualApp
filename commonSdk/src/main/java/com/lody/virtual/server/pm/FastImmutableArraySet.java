/*
 * Decompiled with CFR 0.152.
 */
package com.lody.virtual.server.pm;

import java.util.AbstractSet;
import java.util.Iterator;

public final class FastImmutableArraySet<T>
extends AbstractSet<T> {
    FastIterator<T> mIterator;
    T[] mContents;

    public FastImmutableArraySet(T[] contents) {
        this.mContents = contents;
    }

    @Override
    public Iterator<T> iterator() {
        FastIterator<T> it = this.mIterator;
        if (it == null) {
            this.mIterator = it = new FastIterator<T>(this.mContents);
        } else {
            it.mIndex = 0;
        }
        return it;
    }

    @Override
    public int size() {
        return this.mContents.length;
    }

    private static final class FastIterator<T>
    implements Iterator<T> {
        private final T[] mContents;
        int mIndex;

        public FastIterator(T[] contents) {
            this.mContents = contents;
        }

        @Override
        public boolean hasNext() {
            return this.mIndex != this.mContents.length;
        }

        @Override
        public T next() {
            return this.mContents[this.mIndex++];
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}

