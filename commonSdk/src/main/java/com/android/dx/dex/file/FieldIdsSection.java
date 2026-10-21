/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.dex.file;

import com.android.dx.dex.file.DexFile;
import com.android.dx.dex.file.FieldIdItem;
import com.android.dx.dex.file.IndexedItem;
import com.android.dx.dex.file.Item;
import com.android.dx.dex.file.MemberIdsSection;
import com.android.dx.rop.cst.Constant;
import com.android.dx.rop.cst.CstFieldRef;
import com.android.dx.util.AnnotatedOutput;
import com.android.dx.util.Hex;
import java.util.Collection;
import java.util.TreeMap;

public final class FieldIdsSection
extends MemberIdsSection {
    private final TreeMap<CstFieldRef, FieldIdItem> fieldIds = new TreeMap();

    public FieldIdsSection(DexFile file) {
        super("field_ids", file);
    }

    @Override
    public Collection<? extends Item> items() {
        return this.fieldIds.values();
    }

    @Override
    public IndexedItem get(Constant cst) {
        if (cst == null) {
            throw new NullPointerException("cst == null");
        }
        this.throwIfNotPrepared();
        IndexedItem result = this.fieldIds.get((CstFieldRef)cst);
        if (result == null) {
            throw new IllegalArgumentException("not found");
        }
        return result;
    }

    public void writeHeaderPart(AnnotatedOutput out) {
        int offset;
        this.throwIfNotPrepared();
        int sz = this.fieldIds.size();
        int n = offset = sz == 0 ? 0 : this.getFileOffset();
        if (out.annotates()) {
            out.annotate(4, "field_ids_size:  " + Hex.u4(sz));
            out.annotate(4, "field_ids_off:   " + Hex.u4(offset));
        }
        out.writeInt(sz);
        out.writeInt(offset);
    }

    public synchronized FieldIdItem intern(CstFieldRef field) {
        if (field == null) {
            throw new NullPointerException("field == null");
        }
        this.throwIfPrepared();
        FieldIdItem result = this.fieldIds.get(field);
        if (result == null) {
            result = new FieldIdItem(field);
            this.fieldIds.put(field, result);
        }
        return result;
    }

    public int indexOf(CstFieldRef ref) {
        if (ref == null) {
            throw new NullPointerException("ref == null");
        }
        this.throwIfNotPrepared();
        FieldIdItem item = this.fieldIds.get(ref);
        if (item == null) {
            throw new IllegalArgumentException("not found");
        }
        return item.getIndex();
    }
}

