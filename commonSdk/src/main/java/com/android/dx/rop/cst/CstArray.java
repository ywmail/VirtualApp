/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.rop.cst;

import com.android.dx.rop.cst.Constant;
import com.android.dx.util.FixedSizeList;

public class CstArray
extends Constant {
    private final List list;

    public CstArray(List list) {
        if (list == null) {
            throw new NullPointerException("list == null");
        }
        list.throwIfMutable();
        this.list = list;
    }

    public boolean equals(Object other) {
        if (!(other instanceof CstArray)) {
            return false;
        }
        return this.list.equals(((CstArray)other).list);
    }

    public int hashCode() {
        return this.list.hashCode();
    }

    @Override
    protected int compareTo0(Constant other) {
        return this.list.compareTo(((CstArray)other).list);
    }

    public String toString() {
        return this.list.toString("array{", ", ", "}");
    }

    @Override
    public String typeName() {
        return "array";
    }

    @Override
    public boolean isCategory2() {
        return false;
    }

    @Override
    public String toHuman() {
        return this.list.toHuman("{", ", ", "}");
    }

    public List getList() {
        return this.list;
    }

    public static final class List
    extends FixedSizeList
    implements Comparable<List> {
        public List(int size) {
            super(size);
        }

        @Override
        public int compareTo(List other) {
            int otherSize;
            int thisSize = this.size();
            int compareSize = thisSize < (otherSize = other.size()) ? thisSize : otherSize;
            for (int i = 0; i < compareSize; ++i) {
                Constant otherItem;
                Constant thisItem = (Constant)this.get0(i);
                int compare = thisItem.compareTo(otherItem = (Constant)other.get0(i));
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

        public Constant get(int n) {
            return (Constant)this.get0(n);
        }

        public void set(int n, Constant a) {
            this.set0(n, a);
        }
    }
}

