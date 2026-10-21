/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.dex.file;

import com.android.dx.dex.file.DexFile;
import com.android.dx.dex.file.IndexedItem;
import com.android.dx.dex.file.Item;
import com.android.dx.dex.file.MemberIdsSection;
import com.android.dx.dex.file.MethodIdItem;
import com.android.dx.rop.cst.Constant;
import com.android.dx.rop.cst.CstBaseMethodRef;
import com.android.dx.util.AnnotatedOutput;
import com.android.dx.util.Hex;
import java.util.Collection;
import java.util.TreeMap;

public final class MethodIdsSection
extends MemberIdsSection {
    private final TreeMap<CstBaseMethodRef, MethodIdItem> methodIds = new TreeMap();

    public MethodIdsSection(DexFile file) {
        super("method_ids", file);
    }

    @Override
    public Collection<? extends Item> items() {
        return this.methodIds.values();
    }

    @Override
    public IndexedItem get(Constant cst) {
        if (cst == null) {
            throw new NullPointerException("cst == null");
        }
        this.throwIfNotPrepared();
        IndexedItem result = this.methodIds.get((CstBaseMethodRef)cst);
        if (result == null) {
            throw new IllegalArgumentException("not found");
        }
        return result;
    }

    public void writeHeaderPart(AnnotatedOutput out) {
        int offset;
        this.throwIfNotPrepared();
        int sz = this.methodIds.size();
        int n = offset = sz == 0 ? 0 : this.getFileOffset();
        if (out.annotates()) {
            out.annotate(4, "method_ids_size: " + Hex.u4(sz));
            out.annotate(4, "method_ids_off:  " + Hex.u4(offset));
        }
        out.writeInt(sz);
        out.writeInt(offset);
    }

    public synchronized MethodIdItem intern(CstBaseMethodRef method) {
        if (method == null) {
            throw new NullPointerException("method == null");
        }
        this.throwIfPrepared();
        MethodIdItem result = this.methodIds.get(method);
        if (result == null) {
            result = new MethodIdItem(method);
            this.methodIds.put(method, result);
        }
        return result;
    }

    public int indexOf(CstBaseMethodRef ref) {
        if (ref == null) {
            throw new NullPointerException("ref == null");
        }
        this.throwIfNotPrepared();
        MethodIdItem item = this.methodIds.get(ref);
        if (item == null) {
            throw new IllegalArgumentException("not found");
        }
        return item.getIndex();
    }
}

