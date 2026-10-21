/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx;

import com.android.dx.TypeId;
import com.android.dx.rop.type.StdTypeList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

final class TypeList {
    final TypeId<?>[] types;
    final StdTypeList ropTypes;

    TypeList(TypeId<?>[] types) {
        this.types = (TypeId[])types.clone();
        this.ropTypes = new StdTypeList(types.length);
        for (int i = 0; i < types.length; ++i) {
            this.ropTypes.set(i, types[i].ropType);
        }
    }

    public List<TypeId<?>> asList() {
        return Collections.unmodifiableList(Arrays.asList(this.types));
    }

    public boolean equals(Object o) {
        return o instanceof TypeList && Arrays.equals(((TypeList)o).types, this.types);
    }

    public int hashCode() {
        return Arrays.hashCode(this.types);
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < this.types.length; ++i) {
            if (i > 0) {
                result.append(", ");
            }
            result.append(this.types[i]);
        }
        return result.toString();
    }
}

