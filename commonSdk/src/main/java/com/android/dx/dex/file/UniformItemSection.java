/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.dex.file;

import com.android.dx.dex.file.DexFile;
import com.android.dx.dex.file.IndexedItem;
import com.android.dx.dex.file.Item;
import com.android.dx.dex.file.Section;
import com.android.dx.rop.cst.Constant;
import com.android.dx.util.AnnotatedOutput;
import java.util.Collection;

public abstract class UniformItemSection
extends Section {
    public UniformItemSection(String name, DexFile file, int alignment) {
        super(name, file, alignment);
    }

    @Override
    public final int writeSize() {
        Collection<? extends Item> items = this.items();
        int sz = items.size();
        if (sz == 0) {
            return 0;
        }
        return sz * items.iterator().next().writeSize();
    }

    public abstract IndexedItem get(Constant var1);

    @Override
    protected final void prepare0() {
        DexFile file = this.getFile();
        this.orderItems();
        for (Item item : this.items()) {
            item.addContents(file);
        }
    }

    @Override
    protected final void writeTo0(AnnotatedOutput out) {
        DexFile file = this.getFile();
        int alignment = this.getAlignment();
        for (Item item : this.items()) {
            item.writeTo(file, out);
            out.alignTo(alignment);
        }
    }

    @Override
    public final int getAbsoluteItemOffset(Item item) {
        IndexedItem ii = (IndexedItem)item;
        int relativeOffset = ii.getIndex() * ii.writeSize();
        return this.getAbsoluteOffset(relativeOffset);
    }

    protected abstract void orderItems();
}

