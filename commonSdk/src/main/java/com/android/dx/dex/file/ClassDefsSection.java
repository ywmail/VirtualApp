/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.dex.file;

import com.android.dx.dex.file.ClassDefItem;
import com.android.dx.dex.file.DexFile;
import com.android.dx.dex.file.IndexedItem;
import com.android.dx.dex.file.Item;
import com.android.dx.dex.file.UniformItemSection;
import com.android.dx.rop.cst.Constant;
import com.android.dx.rop.cst.CstType;
import com.android.dx.rop.type.Type;
import com.android.dx.rop.type.TypeList;
import com.android.dx.util.AnnotatedOutput;
import com.android.dx.util.Hex;
import java.util.ArrayList;
import java.util.Collection;
import java.util.TreeMap;

public final class ClassDefsSection
extends UniformItemSection {
    private final TreeMap<Type, ClassDefItem> classDefs = new TreeMap();
    private ArrayList<ClassDefItem> orderedDefs = null;

    public ClassDefsSection(DexFile file) {
        super("class_defs", file, 4);
    }

    @Override
    public Collection<? extends Item> items() {
        if (this.orderedDefs != null) {
            return this.orderedDefs;
        }
        return this.classDefs.values();
    }

    @Override
    public IndexedItem get(Constant cst) {
        if (cst == null) {
            throw new NullPointerException("cst == null");
        }
        this.throwIfNotPrepared();
        Type type = ((CstType)cst).getClassType();
        IndexedItem result = this.classDefs.get(type);
        if (result == null) {
            throw new IllegalArgumentException("not found");
        }
        return result;
    }

    public void writeHeaderPart(AnnotatedOutput out) {
        int offset;
        this.throwIfNotPrepared();
        int sz = this.classDefs.size();
        int n = offset = sz == 0 ? 0 : this.getFileOffset();
        if (out.annotates()) {
            out.annotate(4, "class_defs_size: " + Hex.u4(sz));
            out.annotate(4, "class_defs_off:  " + Hex.u4(offset));
        }
        out.writeInt(sz);
        out.writeInt(offset);
    }

    public void add(ClassDefItem clazz) {
        Type type;
        try {
            type = clazz.getThisClass().getClassType();
        }
        catch (NullPointerException ex) {
            throw new NullPointerException("clazz == null");
        }
        this.throwIfPrepared();
        if (this.classDefs.get(type) != null) {
            throw new IllegalArgumentException("already added: " + type);
        }
        this.classDefs.put(type, clazz);
    }

    @Override
    protected void orderItems() {
        int sz = this.classDefs.size();
        int idx = 0;
        this.orderedDefs = new ArrayList(sz);
        for (Type type : this.classDefs.keySet()) {
            idx = this.orderItems0(type, idx, sz - idx);
        }
    }

    private int orderItems0(Type type, int idx, int maxDepth) {
        ClassDefItem c = this.classDefs.get(type);
        if (c == null || c.hasIndex()) {
            return idx;
        }
        if (maxDepth < 0) {
            throw new RuntimeException("class circularity with " + type);
        }
        --maxDepth;
        CstType superclassCst = c.getSuperclass();
        if (superclassCst != null) {
            Type superclass = superclassCst.getClassType();
            idx = this.orderItems0(superclass, idx, maxDepth);
        }
        TypeList interfaces = c.getInterfaces();
        int sz = interfaces.size();
        for (int i = 0; i < sz; ++i) {
            idx = this.orderItems0(interfaces.getType(i), idx, maxDepth);
        }
        c.setIndex(idx);
        this.orderedDefs.add(c);
        return idx + 1;
    }
}

