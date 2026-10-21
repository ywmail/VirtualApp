/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.dex.code;

import com.android.dx.rop.cst.CstType;
import com.android.dx.util.FixedSizeList;
import com.android.dx.util.Hex;

public final class CatchHandlerList
extends FixedSizeList
implements Comparable<CatchHandlerList> {
    public static final CatchHandlerList EMPTY = new CatchHandlerList(0);

    public CatchHandlerList(int size) {
        super(size);
    }

    public Entry get(int n) {
        return (Entry)this.get0(n);
    }

    @Override
    public String toHuman() {
        return this.toHuman("", "");
    }

    public String toHuman(String prefix, String header) {
        StringBuilder sb = new StringBuilder(100);
        int size = this.size();
        sb.append(prefix);
        sb.append(header);
        sb.append("catch ");
        for (int i = 0; i < size; ++i) {
            Entry entry = this.get(i);
            if (i != 0) {
                sb.append(",\n");
                sb.append(prefix);
                sb.append("  ");
            }
            if (i == size - 1 && this.catchesAll()) {
                sb.append("<any>");
            } else {
                sb.append(entry.getExceptionType().toHuman());
            }
            sb.append(" -> ");
            sb.append(Hex.u2or4(entry.getHandler()));
        }
        return sb.toString();
    }

    public boolean catchesAll() {
        int size = this.size();
        if (size == 0) {
            return false;
        }
        Entry last = this.get(size - 1);
        return last.getExceptionType().equals(CstType.OBJECT);
    }

    public void set(int n, CstType exceptionType, int handler) {
        this.set0(n, new Entry(exceptionType, handler));
    }

    public void set(int n, Entry entry) {
        this.set0(n, entry);
    }

    @Override
    public int compareTo(CatchHandlerList other) {
        if (this == other) {
            return 0;
        }
        int thisSize = this.size();
        int otherSize = other.size();
        int checkSize = Math.min(thisSize, otherSize);
        for (int i = 0; i < checkSize; ++i) {
            Entry otherEntry;
            Entry thisEntry = this.get(i);
            int compare = thisEntry.compareTo(otherEntry = other.get(i));
            if (compare == 0) continue;
            return compare;
        }
        if (thisSize < otherSize) {
            return -1;
        }
        if (thisSize > otherSize) {
            return 1;
        }
        return 0;
    }

    public static class Entry
    implements Comparable<Entry> {
        private final CstType exceptionType;
        private final int handler;

        public Entry(CstType exceptionType, int handler) {
            if (handler < 0) {
                throw new IllegalArgumentException("handler < 0");
            }
            if (exceptionType == null) {
                throw new NullPointerException("exceptionType == null");
            }
            this.handler = handler;
            this.exceptionType = exceptionType;
        }

        public int hashCode() {
            return this.handler * 31 + this.exceptionType.hashCode();
        }

        public boolean equals(Object other) {
            if (other instanceof Entry) {
                return this.compareTo((Entry)other) == 0;
            }
            return false;
        }

        @Override
        public int compareTo(Entry other) {
            if (this.handler < other.handler) {
                return -1;
            }
            if (this.handler > other.handler) {
                return 1;
            }
            return this.exceptionType.compareTo(other.exceptionType);
        }

        public CstType getExceptionType() {
            return this.exceptionType;
        }

        public int getHandler() {
            return this.handler;
        }
    }
}

