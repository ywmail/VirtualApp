/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.dex.file;

import com.android.dex.DexIndexOverflowException;
import com.android.dx.dex.file.DexFile;
import com.android.dx.dex.file.IndexedItem;
import com.android.dx.dex.file.Item;
import com.android.dx.dex.file.TypeIdItem;
import com.android.dx.dex.file.UniformItemSection;
import com.android.dx.rop.cst.Constant;
import com.android.dx.rop.cst.CstType;
import com.android.dx.rop.type.Type;
import com.android.dx.util.AnnotatedOutput;
import com.android.dx.util.Hex;
import java.util.Collection;
import java.util.TreeMap;

public final class TypeIdsSection
extends UniformItemSection {
    private final TreeMap<Type, TypeIdItem> typeIds = new TreeMap();

    public TypeIdsSection(DexFile file) {
        super("type_ids", file, 4);
    }

    @Override
    public Collection<? extends Item> items() {
        return this.typeIds.values();
    }

    @Override
    public IndexedItem get(Constant cst) {
        if (cst == null) {
            throw new NullPointerException("cst == null");
        }
        this.throwIfNotPrepared();
        Type type = ((CstType)cst).getClassType();
        IndexedItem result = this.typeIds.get(type);
        if (result == null) {
            throw new IllegalArgumentException("not found: " + cst);
        }
        return result;
    }

    public void writeHeaderPart(AnnotatedOutput out) {
        int offset;
        this.throwIfNotPrepared();
        int sz = this.typeIds.size();
        int n = offset = sz == 0 ? 0 : this.getFileOffset();
        if (sz > 65536) {
            throw new DexIndexOverflowException(String.format("Too many type identifiers to fit in one dex file: %1$d; max is %2$d.%nYou may try using multi-dex. If multi-dex is enabled then the list of classes for the main dex list is too large.", this.items().size(), 65536));
        }
        if (out.annotates()) {
            out.annotate(4, "type_ids_size:   " + Hex.u4(sz));
            out.annotate(4, "type_ids_off:    " + Hex.u4(offset));
        }
        out.writeInt(sz);
        out.writeInt(offset);
    }

    public synchronized TypeIdItem intern(Type type) {
        if (type == null) {
            throw new NullPointerException("type == null");
        }
        this.throwIfPrepared();
        TypeIdItem result = this.typeIds.get(type);
        if (result == null) {
            result = new TypeIdItem(new CstType(type));
            this.typeIds.put(type, result);
        }
        return result;
    }

    public synchronized TypeIdItem intern(CstType type) {
        if (type == null) {
            throw new NullPointerException("type == null");
        }
        this.throwIfPrepared();
        Type typePerSe = type.getClassType();
        TypeIdItem result = this.typeIds.get(typePerSe);
        if (result == null) {
            result = new TypeIdItem(type);
            this.typeIds.put(typePerSe, result);
        }
        return result;
    }

    public int indexOf(Type type) {
        if (type == null) {
            throw new NullPointerException("type == null");
        }
        this.throwIfNotPrepared();
        TypeIdItem item = this.typeIds.get(type);
        if (item == null) {
            throw new IllegalArgumentException("not found: " + type);
        }
        return item.getIndex();
    }

    public int indexOf(CstType type) {
        if (type == null) {
            throw new NullPointerException("type == null");
        }
        return this.indexOf(type.getClassType());
    }

    @Override
    protected void orderItems() {
        int idx = 0;
        for (Item item : this.items()) {
            ((TypeIdItem)item).setIndex(idx);
            ++idx;
        }
    }
}

