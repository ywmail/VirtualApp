/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.merge;

import com.android.dex.ClassDef;
import com.android.dex.Dex;
import com.android.dex.DexException;
import com.android.dx.merge.IndexMap;
import java.util.Comparator;

final class SortableType {
    public static final Comparator<SortableType> NULLS_LAST_ORDER = new Comparator<SortableType>(){

        @Override
        public int compare(SortableType a, SortableType b) {
            if (a == b) {
                return 0;
            }
            if (b == null) {
                return -1;
            }
            if (a == null) {
                return 1;
            }
            if (a.depth != b.depth) {
                return a.depth - b.depth;
            }
            return a.getTypeIndex() - b.getTypeIndex();
        }
    };
    private final Dex dex;
    private final IndexMap indexMap;
    private final ClassDef classDef;
    private int depth = -1;

    public SortableType(Dex dex, IndexMap indexMap, ClassDef classDef) {
        this.dex = dex;
        this.indexMap = indexMap;
        this.classDef = classDef;
    }

    public Dex getDex() {
        return this.dex;
    }

    public IndexMap getIndexMap() {
        return this.indexMap;
    }

    public ClassDef getClassDef() {
        return this.classDef;
    }

    public int getTypeIndex() {
        return this.classDef.getTypeIndex();
    }

    public boolean tryAssignDepth(SortableType[] types) {
        int max;
        if (this.classDef.getSupertypeIndex() == -1) {
            max = 0;
        } else {
            if (this.classDef.getSupertypeIndex() == this.classDef.getTypeIndex()) {
                throw new DexException("Class with type index " + this.classDef.getTypeIndex() + " extends itself");
            }
            SortableType sortableSupertype = types[this.classDef.getSupertypeIndex()];
            if (sortableSupertype == null) {
                max = 1;
            } else {
                if (sortableSupertype.depth == -1) {
                    return false;
                }
                max = sortableSupertype.depth;
            }
        }
        for (short interfaceIndex : this.classDef.getInterfaces()) {
            SortableType implemented = types[interfaceIndex];
            if (implemented == null) {
                max = Math.max(max, 1);
                continue;
            }
            if (implemented.depth == -1) {
                return false;
            }
            max = Math.max(max, implemented.depth);
        }
        this.depth = max + 1;
        return true;
    }

    public boolean isDepthAssigned() {
        return this.depth != -1;
    }
}

