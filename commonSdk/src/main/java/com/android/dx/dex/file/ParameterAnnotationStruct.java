/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.dex.file;

import com.android.dx.dex.file.AnnotationSetItem;
import com.android.dx.dex.file.AnnotationSetRefItem;
import com.android.dx.dex.file.DexFile;
import com.android.dx.dex.file.ItemType;
import com.android.dx.dex.file.MethodIdsSection;
import com.android.dx.dex.file.MixedItemSection;
import com.android.dx.dex.file.UniformListItem;
import com.android.dx.rop.annotation.Annotations;
import com.android.dx.rop.annotation.AnnotationsList;
import com.android.dx.rop.cst.CstMethodRef;
import com.android.dx.util.AnnotatedOutput;
import com.android.dx.util.Hex;
import com.android.dx.util.ToHuman;
import java.util.ArrayList;

public final class ParameterAnnotationStruct
implements ToHuman,
Comparable<ParameterAnnotationStruct> {
    private final CstMethodRef method;
    private final AnnotationsList annotationsList;
    private final UniformListItem<AnnotationSetRefItem> annotationsItem;

    public ParameterAnnotationStruct(CstMethodRef method, AnnotationsList annotationsList, DexFile dexFile) {
        if (method == null) {
            throw new NullPointerException("method == null");
        }
        if (annotationsList == null) {
            throw new NullPointerException("annotationsList == null");
        }
        this.method = method;
        this.annotationsList = annotationsList;
        int size = annotationsList.size();
        ArrayList<AnnotationSetRefItem> arrayList = new ArrayList<AnnotationSetRefItem>(size);
        for (int i = 0; i < size; ++i) {
            Annotations annotations = annotationsList.get(i);
            AnnotationSetItem item = new AnnotationSetItem(annotations, dexFile);
            arrayList.add(new AnnotationSetRefItem(item));
        }
        this.annotationsItem = new UniformListItem(ItemType.TYPE_ANNOTATION_SET_REF_LIST, arrayList);
    }

    public int hashCode() {
        return this.method.hashCode();
    }

    public boolean equals(Object other) {
        if (!(other instanceof ParameterAnnotationStruct)) {
            return false;
        }
        return this.method.equals(((ParameterAnnotationStruct)other).method);
    }

    @Override
    public int compareTo(ParameterAnnotationStruct other) {
        return this.method.compareTo(other.method);
    }

    public void addContents(DexFile file) {
        MethodIdsSection methodIds = file.getMethodIds();
        MixedItemSection wordData = file.getWordData();
        methodIds.intern(this.method);
        wordData.add(this.annotationsItem);
    }

    public void writeTo(DexFile file, AnnotatedOutput out) {
        int methodIdx = file.getMethodIds().indexOf(this.method);
        int annotationsOff = this.annotationsItem.getAbsoluteOffset();
        if (out.annotates()) {
            out.annotate(0, "    " + this.method.toHuman());
            out.annotate(4, "      method_idx:      " + Hex.u4(methodIdx));
            out.annotate(4, "      annotations_off: " + Hex.u4(annotationsOff));
        }
        out.writeInt(methodIdx);
        out.writeInt(annotationsOff);
    }

    @Override
    public String toHuman() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.method.toHuman());
        sb.append(": ");
        boolean first = true;
        for (AnnotationSetRefItem item : this.annotationsItem.getItems()) {
            if (first) {
                first = false;
            } else {
                sb.append(", ");
            }
            sb.append(item.toHuman());
        }
        return sb.toString();
    }

    public CstMethodRef getMethod() {
        return this.method;
    }

    public AnnotationsList getAnnotationsList() {
        return this.annotationsList;
    }
}

