/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.cf.code;

import com.android.dx.cf.code.BootstrapMethodArgumentsList;
import com.android.dx.rop.cst.CstMethodHandle;
import com.android.dx.rop.cst.CstType;
import com.android.dx.util.FixedSizeList;

public class BootstrapMethodsList
extends FixedSizeList {
    public static final BootstrapMethodsList EMPTY = new BootstrapMethodsList(0);

    public BootstrapMethodsList(int count) {
        super(count);
    }

    public Item get(int n) {
        return (Item)this.get0(n);
    }

    public void set(int n, Item item) {
        if (item == null) {
            throw new NullPointerException("item == null");
        }
        this.set0(n, item);
    }

    public void set(int n, CstType declaringClass, CstMethodHandle bootstrapMethodHandle, BootstrapMethodArgumentsList arguments) {
        this.set(n, new Item(declaringClass, bootstrapMethodHandle, arguments));
    }

    public static BootstrapMethodsList concat(BootstrapMethodsList list1, BootstrapMethodsList list2) {
        int i;
        if (list1 == EMPTY) {
            return list2;
        }
        if (list2 == EMPTY) {
            return list1;
        }
        int sz1 = list1.size();
        int sz2 = list2.size();
        BootstrapMethodsList result = new BootstrapMethodsList(sz1 + sz2);
        for (i = 0; i < sz1; ++i) {
            result.set(i, list1.get(i));
        }
        for (i = 0; i < sz2; ++i) {
            result.set(sz1 + i, list2.get(i));
        }
        return result;
    }

    public static class Item {
        private final BootstrapMethodArgumentsList bootstrapMethodArgumentsList;
        private final CstMethodHandle bootstrapMethodHandle;
        private final CstType declaringClass;

        public Item(CstType declaringClass, CstMethodHandle bootstrapMethodHandle, BootstrapMethodArgumentsList bootstrapMethodArguments) {
            if (declaringClass == null) {
                throw new NullPointerException("declaringClass == null");
            }
            if (bootstrapMethodHandle == null) {
                throw new NullPointerException("bootstrapMethodHandle == null");
            }
            if (bootstrapMethodArguments == null) {
                throw new NullPointerException("bootstrapMethodArguments == null");
            }
            this.bootstrapMethodHandle = bootstrapMethodHandle;
            this.bootstrapMethodArgumentsList = bootstrapMethodArguments;
            this.declaringClass = declaringClass;
        }

        public CstMethodHandle getBootstrapMethodHandle() {
            return this.bootstrapMethodHandle;
        }

        public BootstrapMethodArgumentsList getBootstrapMethodArguments() {
            return this.bootstrapMethodArgumentsList;
        }

        public CstType getDeclaringClass() {
            return this.declaringClass;
        }
    }
}

