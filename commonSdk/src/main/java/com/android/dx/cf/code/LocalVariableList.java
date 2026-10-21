/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.cf.code;

import com.android.dx.rop.code.LocalItem;
import com.android.dx.rop.cst.CstString;
import com.android.dx.rop.type.Type;
import com.android.dx.util.FixedSizeList;

public final class LocalVariableList
extends FixedSizeList {
    public static final LocalVariableList EMPTY = new LocalVariableList(0);

    public static LocalVariableList concat(LocalVariableList list1, LocalVariableList list2) {
        int i;
        if (list1 == EMPTY) {
            return list2;
        }
        int sz1 = list1.size();
        int sz2 = list2.size();
        LocalVariableList result = new LocalVariableList(sz1 + sz2);
        for (i = 0; i < sz1; ++i) {
            result.set(i, list1.get(i));
        }
        for (i = 0; i < sz2; ++i) {
            result.set(sz1 + i, list2.get(i));
        }
        result.setImmutable();
        return result;
    }

    public static LocalVariableList mergeDescriptorsAndSignatures(LocalVariableList descriptorList, LocalVariableList signatureList) {
        int descriptorSize = descriptorList.size();
        LocalVariableList result = new LocalVariableList(descriptorSize);
        for (int i = 0; i < descriptorSize; ++i) {
            Item item = descriptorList.get(i);
            Item signatureItem = signatureList.itemToLocal(item);
            if (signatureItem != null) {
                CstString signature = signatureItem.getSignature();
                item = item.withSignature(signature);
            }
            result.set(i, item);
        }
        result.setImmutable();
        return result;
    }

    public LocalVariableList(int count) {
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

    public void set(int n, int startPc, int length, CstString name, CstString descriptor, CstString signature, int index) {
        this.set0(n, new Item(startPc, length, name, descriptor, signature, index));
    }

    public Item itemToLocal(Item item) {
        int sz = this.size();
        for (int i = 0; i < sz; ++i) {
            Item one = (Item)this.get0(i);
            if (one == null || !one.matchesAllButType(item)) continue;
            return one;
        }
        return null;
    }

    public Item pcAndIndexToLocal(int pc, int index) {
        int sz = this.size();
        for (int i = 0; i < sz; ++i) {
            Item one = (Item)this.get0(i);
            if (one == null || !one.matchesPcAndIndex(pc, index)) continue;
            return one;
        }
        return null;
    }

    public static class Item {
        private final int startPc;
        private final int length;
        private final CstString name;
        private final CstString descriptor;
        private final CstString signature;
        private final int index;

        public Item(int startPc, int length, CstString name, CstString descriptor, CstString signature, int index) {
            if (startPc < 0) {
                throw new IllegalArgumentException("startPc < 0");
            }
            if (length < 0) {
                throw new IllegalArgumentException("length < 0");
            }
            if (name == null) {
                throw new NullPointerException("name == null");
            }
            if (descriptor == null && signature == null) {
                throw new NullPointerException("(descriptor == null) && (signature == null)");
            }
            if (index < 0) {
                throw new IllegalArgumentException("index < 0");
            }
            this.startPc = startPc;
            this.length = length;
            this.name = name;
            this.descriptor = descriptor;
            this.signature = signature;
            this.index = index;
        }

        public int getStartPc() {
            return this.startPc;
        }

        public int getLength() {
            return this.length;
        }

        public CstString getDescriptor() {
            return this.descriptor;
        }

        public LocalItem getLocalItem() {
            return LocalItem.make(this.name, this.signature);
        }

        private CstString getSignature() {
            return this.signature;
        }

        public int getIndex() {
            return this.index;
        }

        public Type getType() {
            return Type.intern(this.descriptor.getString());
        }

        public Item withSignature(CstString newSignature) {
            return new Item(this.startPc, this.length, this.name, this.descriptor, newSignature, this.index);
        }

        public boolean matchesPcAndIndex(int pc, int index) {
            return index == this.index && pc >= this.startPc && pc < this.startPc + this.length;
        }

        public boolean matchesAllButType(Item other) {
            return this.startPc == other.startPc && this.length == other.length && this.index == other.index && this.name.equals(other.name);
        }
    }
}

