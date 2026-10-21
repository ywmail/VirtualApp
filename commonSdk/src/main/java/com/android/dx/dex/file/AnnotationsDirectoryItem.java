/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.dex.file;

import com.android.dx.dex.file.AnnotationSetItem;
import com.android.dx.dex.file.DexFile;
import com.android.dx.dex.file.FieldAnnotationStruct;
import com.android.dx.dex.file.ItemType;
import com.android.dx.dex.file.MethodAnnotationStruct;
import com.android.dx.dex.file.MixedItemSection;
import com.android.dx.dex.file.OffsettedItem;
import com.android.dx.dex.file.ParameterAnnotationStruct;
import com.android.dx.dex.file.Section;
import com.android.dx.rop.annotation.Annotations;
import com.android.dx.rop.annotation.AnnotationsList;
import com.android.dx.rop.cst.CstFieldRef;
import com.android.dx.rop.cst.CstMethodRef;
import com.android.dx.util.AnnotatedOutput;
import com.android.dx.util.Hex;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;

public final class AnnotationsDirectoryItem
extends OffsettedItem {
    private static final int ALIGNMENT = 4;
    private static final int HEADER_SIZE = 16;
    private static final int ELEMENT_SIZE = 8;
    private AnnotationSetItem classAnnotations = null;
    private ArrayList<FieldAnnotationStruct> fieldAnnotations = null;
    private ArrayList<MethodAnnotationStruct> methodAnnotations = null;
    private ArrayList<ParameterAnnotationStruct> parameterAnnotations = null;

    public AnnotationsDirectoryItem() {
        super(4, -1);
    }

    @Override
    public ItemType itemType() {
        return ItemType.TYPE_ANNOTATIONS_DIRECTORY_ITEM;
    }

    public boolean isEmpty() {
        return this.classAnnotations == null && this.fieldAnnotations == null && this.methodAnnotations == null && this.parameterAnnotations == null;
    }

    public boolean isInternable() {
        return this.classAnnotations != null && this.fieldAnnotations == null && this.methodAnnotations == null && this.parameterAnnotations == null;
    }

    public int hashCode() {
        if (this.classAnnotations == null) {
            return 0;
        }
        return this.classAnnotations.hashCode();
    }

    @Override
    public int compareTo0(OffsettedItem other) {
        if (!this.isInternable()) {
            throw new UnsupportedOperationException("uninternable instance");
        }
        AnnotationsDirectoryItem otherDirectory = (AnnotationsDirectoryItem)other;
        return this.classAnnotations.compareTo(otherDirectory.classAnnotations);
    }

    public void setClassAnnotations(Annotations annotations, DexFile dexFile) {
        if (annotations == null) {
            throw new NullPointerException("annotations == null");
        }
        if (this.classAnnotations != null) {
            throw new UnsupportedOperationException("class annotations already set");
        }
        this.classAnnotations = new AnnotationSetItem(annotations, dexFile);
    }

    public void addFieldAnnotations(CstFieldRef field, Annotations annotations, DexFile dexFile) {
        if (this.fieldAnnotations == null) {
            this.fieldAnnotations = new ArrayList();
        }
        this.fieldAnnotations.add(new FieldAnnotationStruct(field, new AnnotationSetItem(annotations, dexFile)));
    }

    public void addMethodAnnotations(CstMethodRef method, Annotations annotations, DexFile dexFile) {
        if (this.methodAnnotations == null) {
            this.methodAnnotations = new ArrayList();
        }
        this.methodAnnotations.add(new MethodAnnotationStruct(method, new AnnotationSetItem(annotations, dexFile)));
    }

    public void addParameterAnnotations(CstMethodRef method, AnnotationsList list, DexFile dexFile) {
        if (this.parameterAnnotations == null) {
            this.parameterAnnotations = new ArrayList();
        }
        this.parameterAnnotations.add(new ParameterAnnotationStruct(method, list, dexFile));
    }

    public Annotations getMethodAnnotations(CstMethodRef method) {
        if (this.methodAnnotations == null) {
            return null;
        }
        for (MethodAnnotationStruct item : this.methodAnnotations) {
            if (!item.getMethod().equals(method)) continue;
            return item.getAnnotations();
        }
        return null;
    }

    public AnnotationsList getParameterAnnotations(CstMethodRef method) {
        if (this.parameterAnnotations == null) {
            return null;
        }
        for (ParameterAnnotationStruct item : this.parameterAnnotations) {
            if (!item.getMethod().equals(method)) continue;
            return item.getAnnotationsList();
        }
        return null;
    }

    @Override
    public void addContents(DexFile file) {
        MixedItemSection wordData = file.getWordData();
        if (this.classAnnotations != null) {
            this.classAnnotations = wordData.intern(this.classAnnotations);
        }
        if (this.fieldAnnotations != null) {
            for (FieldAnnotationStruct fieldAnnotationStruct : this.fieldAnnotations) {
                fieldAnnotationStruct.addContents(file);
            }
        }
        if (this.methodAnnotations != null) {
            for (MethodAnnotationStruct methodAnnotationStruct : this.methodAnnotations) {
                methodAnnotationStruct.addContents(file);
            }
        }
        if (this.parameterAnnotations != null) {
            for (ParameterAnnotationStruct parameterAnnotationStruct : this.parameterAnnotations) {
                parameterAnnotationStruct.addContents(file);
            }
        }
    }

    @Override
    public String toHuman() {
        throw new RuntimeException("unsupported");
    }

    @Override
    protected void place0(Section addedTo, int offset) {
        int elementCount = AnnotationsDirectoryItem.listSize(this.fieldAnnotations) + AnnotationsDirectoryItem.listSize(this.methodAnnotations) + AnnotationsDirectoryItem.listSize(this.parameterAnnotations);
        this.setWriteSize(16 + elementCount * 8);
    }

    @Override
    protected void writeTo0(DexFile file, AnnotatedOutput out) {
        boolean annotates = out.annotates();
        int classOff = OffsettedItem.getAbsoluteOffsetOr0(this.classAnnotations);
        int fieldsSize = AnnotationsDirectoryItem.listSize(this.fieldAnnotations);
        int methodsSize = AnnotationsDirectoryItem.listSize(this.methodAnnotations);
        int parametersSize = AnnotationsDirectoryItem.listSize(this.parameterAnnotations);
        if (annotates) {
            out.annotate(0, this.offsetString() + " annotations directory");
            out.annotate(4, "  class_annotations_off: " + Hex.u4(classOff));
            out.annotate(4, "  fields_size:           " + Hex.u4(fieldsSize));
            out.annotate(4, "  methods_size:          " + Hex.u4(methodsSize));
            out.annotate(4, "  parameters_size:       " + Hex.u4(parametersSize));
        }
        out.writeInt(classOff);
        out.writeInt(fieldsSize);
        out.writeInt(methodsSize);
        out.writeInt(parametersSize);
        if (fieldsSize != 0) {
            Collections.sort(this.fieldAnnotations);
            if (annotates) {
                out.annotate(0, "  fields:");
            }
            for (FieldAnnotationStruct fieldAnnotationStruct : this.fieldAnnotations) {
                fieldAnnotationStruct.writeTo(file, out);
            }
        }
        if (methodsSize != 0) {
            Collections.sort(this.methodAnnotations);
            if (annotates) {
                out.annotate(0, "  methods:");
            }
            for (MethodAnnotationStruct methodAnnotationStruct : this.methodAnnotations) {
                methodAnnotationStruct.writeTo(file, out);
            }
        }
        if (parametersSize != 0) {
            Collections.sort(this.parameterAnnotations);
            if (annotates) {
                out.annotate(0, "  parameters:");
            }
            for (ParameterAnnotationStruct parameterAnnotationStruct : this.parameterAnnotations) {
                parameterAnnotationStruct.writeTo(file, out);
            }
        }
    }

    private static int listSize(ArrayList<?> list) {
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    void debugPrint(PrintWriter out) {
        if (this.classAnnotations != null) {
            out.println("  class annotations: " + this.classAnnotations);
        }
        if (this.fieldAnnotations != null) {
            out.println("  field annotations:");
            for (FieldAnnotationStruct fieldAnnotationStruct : this.fieldAnnotations) {
                out.println("    " + fieldAnnotationStruct.toHuman());
            }
        }
        if (this.methodAnnotations != null) {
            out.println("  method annotations:");
            for (MethodAnnotationStruct methodAnnotationStruct : this.methodAnnotations) {
                out.println("    " + methodAnnotationStruct.toHuman());
            }
        }
        if (this.parameterAnnotations != null) {
            out.println("  parameter annotations:");
            for (ParameterAnnotationStruct parameterAnnotationStruct : this.parameterAnnotations) {
                out.println("    " + parameterAnnotationStruct.toHuman());
            }
        }
    }
}

