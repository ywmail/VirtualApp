/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.dex.file;

import com.android.dex.util.ExceptionWithContext;
import com.android.dx.dex.file.DexFile;
import com.android.dx.dex.file.Item;
import com.android.dx.dex.file.ItemType;
import com.android.dx.dex.file.OffsettedItem;
import com.android.dx.dex.file.Section;
import com.android.dx.util.AnnotatedOutput;
import com.android.dx.util.Hex;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.TreeMap;

public final class MixedItemSection
extends Section {
    private static final Comparator<OffsettedItem> TYPE_SORTER = new Comparator<OffsettedItem>(){

        @Override
        public int compare(OffsettedItem item1, OffsettedItem item2) {
            ItemType type1 = item1.itemType();
            ItemType type2 = item2.itemType();
            return type1.compareTo(type2);
        }
    };
    private final ArrayList<OffsettedItem> items = new ArrayList(100);
    private final HashMap<OffsettedItem, OffsettedItem> interns = new HashMap(100);
    private final SortType sort;
    private int writeSize;

    public MixedItemSection(String name, DexFile file, int alignment, SortType sort) {
        super(name, file, alignment);
        this.sort = sort;
        this.writeSize = -1;
    }

    @Override
    public Collection<? extends Item> items() {
        return this.items;
    }

    @Override
    public int writeSize() {
        this.throwIfNotPrepared();
        return this.writeSize;
    }

    @Override
    public int getAbsoluteItemOffset(Item item) {
        OffsettedItem oi = (OffsettedItem)item;
        return oi.getAbsoluteOffset();
    }

    public int size() {
        return this.items.size();
    }

    public void writeHeaderPart(AnnotatedOutput out) {
        this.throwIfNotPrepared();
        if (this.writeSize == -1) {
            throw new RuntimeException("write size not yet set");
        }
        int sz = this.writeSize;
        int offset = sz == 0 ? 0 : this.getFileOffset();
        String name = this.getName();
        if (name == null) {
            name = "<unnamed>";
        }
        int spaceCount = 15 - name.length();
        char[] spaceArr = new char[spaceCount];
        Arrays.fill(spaceArr, ' ');
        String spaces = new String(spaceArr);
        if (out.annotates()) {
            out.annotate(4, name + "_size:" + spaces + Hex.u4(sz));
            out.annotate(4, name + "_off: " + spaces + Hex.u4(offset));
        }
        out.writeInt(sz);
        out.writeInt(offset);
    }

    public void add(OffsettedItem item) {
        this.throwIfPrepared();
        try {
            if (item.getAlignment() > this.getAlignment()) {
                throw new IllegalArgumentException("incompatible item alignment");
            }
        }
        catch (NullPointerException ex) {
            throw new NullPointerException("item == null");
        }
        this.items.add(item);
    }

    public synchronized <T extends OffsettedItem> T intern(T item) {
        this.throwIfPrepared();
        OffsettedItem result = this.interns.get(item);
        if (result != null) {
            return (T)result;
        }
        this.add(item);
        this.interns.put(item, item);
        return item;
    }

    public <T extends OffsettedItem> T get(T item) {
        this.throwIfNotPrepared();
        OffsettedItem result = this.interns.get(item);
        if (result != null) {
            return (T)result;
        }
        throw new NoSuchElementException(item.toString());
    }

    public void writeIndexAnnotation(AnnotatedOutput out, ItemType itemType, String intro) {
        String label;
        this.throwIfNotPrepared();
        TreeMap<String, OffsettedItem> index = new TreeMap<String, OffsettedItem>();
        for (OffsettedItem offsettedItem : this.items) {
            if (offsettedItem.itemType() != itemType) continue;
            label = offsettedItem.toHuman();
            index.put(label, offsettedItem);
        }
        if (index.size() == 0) {
            return;
        }
        out.annotate(0, intro);
        for (Map.Entry entry : index.entrySet()) {
            label = (String)entry.getKey();
            OffsettedItem item = (OffsettedItem)entry.getValue();
            out.annotate(0, item.offsetString() + ' ' + label + '\n');
        }
    }

    @Override
    protected void prepare0() {
        int sz;
        DexFile file = this.getFile();
        int i = 0;
        while (i < (sz = this.items.size())) {
            while (i < sz) {
                OffsettedItem one = this.items.get(i);
                one.addContents(file);
                ++i;
            }
        }
    }

    public void placeItems() {
        this.throwIfNotPrepared();
        switch (this.sort) {
            case INSTANCE: {
                Collections.sort(this.items);
                break;
            }
            case TYPE: {
                Collections.sort(this.items, TYPE_SORTER);
            }
        }
        int sz = this.items.size();
        int outAt = 0;
        for (int i = 0; i < sz; ++i) {
            OffsettedItem one = this.items.get(i);
            try {
                int placedAt = one.place(this, outAt);
                if (placedAt < outAt) {
                    throw new RuntimeException("bogus place() result for " + one);
                }
                outAt = placedAt + one.writeSize();
                continue;
            }
            catch (RuntimeException ex) {
                throw ExceptionWithContext.withContext(ex, "...while placing " + one);
            }
        }
        this.writeSize = outAt;
    }

    @Override
    protected void writeTo0(AnnotatedOutput out) {
        boolean annotates = out.annotates();
        boolean first = true;
        DexFile file = this.getFile();
        int at = 0;
        for (OffsettedItem one : this.items) {
            int alignMask;
            int writeAt;
            if (annotates) {
                if (first) {
                    first = false;
                } else {
                    out.annotate(0, "\n");
                }
            }
            if (at != (writeAt = at + (alignMask = one.getAlignment() - 1) & ~alignMask)) {
                out.writeZeroes(writeAt - at);
                at = writeAt;
            }
            one.writeTo(file, out);
            at += one.writeSize();
        }
        if (at != this.writeSize) {
            throw new RuntimeException("output size mismatch");
        }
    }

    static enum SortType {
        NONE,
        TYPE,
        INSTANCE;

    }
}

