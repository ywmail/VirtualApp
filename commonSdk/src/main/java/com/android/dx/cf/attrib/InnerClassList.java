/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.cf.attrib;

import com.android.dx.rop.cst.CstString;
import com.android.dx.rop.cst.CstType;
import com.android.dx.util.FixedSizeList;

public final class InnerClassList
extends FixedSizeList {
    public InnerClassList(int count) {
        super(count);
    }

    public Item get(int n) {
        return (Item)this.get0(n);
    }

    public void set(int n, CstType innerClass, CstType outerClass, CstString innerName, int accessFlags) {
        this.set0(n, new Item(innerClass, outerClass, innerName, accessFlags));
    }

    public static class Item {
        private final CstType innerClass;
        private final CstType outerClass;
        private final CstString innerName;
        private final int accessFlags;

        public Item(CstType innerClass, CstType outerClass, CstString innerName, int accessFlags) {
            if (innerClass == null) {
                throw new NullPointerException("innerClass == null");
            }
            this.innerClass = innerClass;
            this.outerClass = outerClass;
            this.innerName = innerName;
            this.accessFlags = accessFlags;
        }

        public CstType getInnerClass() {
            return this.innerClass;
        }

        public CstType getOuterClass() {
            return this.outerClass;
        }

        public CstString getInnerName() {
            return this.innerName;
        }

        public int getAccessFlags() {
            return this.accessFlags;
        }
    }
}

