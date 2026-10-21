/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.dex.file;

import com.android.dx.dex.file.DexFile;
import com.android.dx.dex.file.EncodedField;
import com.android.dx.dex.file.EncodedMember;
import com.android.dx.dex.file.EncodedMethod;
import com.android.dx.dex.file.ItemType;
import com.android.dx.dex.file.OffsettedItem;
import com.android.dx.dex.file.Section;
import com.android.dx.rop.cst.Constant;
import com.android.dx.rop.cst.CstArray;
import com.android.dx.rop.cst.CstLiteralBits;
import com.android.dx.rop.cst.CstType;
import com.android.dx.rop.cst.Zeroes;
import com.android.dx.util.AnnotatedOutput;
import com.android.dx.util.ByteArrayAnnotatedOutput;
import com.android.dx.util.Writers;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public final class ClassDataItem
extends OffsettedItem {
    private final CstType thisClass;
    private final ArrayList<EncodedField> staticFields;
    private final HashMap<EncodedField, Constant> staticValues;
    private final ArrayList<EncodedField> instanceFields;
    private final ArrayList<EncodedMethod> directMethods;
    private final ArrayList<EncodedMethod> virtualMethods;
    private CstArray staticValuesConstant;
    private byte[] encodedForm;

    public ClassDataItem(CstType thisClass) {
        super(1, -1);
        if (thisClass == null) {
            throw new NullPointerException("thisClass == null");
        }
        this.thisClass = thisClass;
        this.staticFields = new ArrayList(20);
        this.staticValues = new HashMap(40);
        this.instanceFields = new ArrayList(20);
        this.directMethods = new ArrayList(20);
        this.virtualMethods = new ArrayList(20);
        this.staticValuesConstant = null;
    }

    @Override
    public ItemType itemType() {
        return ItemType.TYPE_CLASS_DATA_ITEM;
    }

    @Override
    public String toHuman() {
        return this.toString();
    }

    public boolean isEmpty() {
        return this.staticFields.isEmpty() && this.instanceFields.isEmpty() && this.directMethods.isEmpty() && this.virtualMethods.isEmpty();
    }

    public void addStaticField(EncodedField field, Constant value) {
        if (field == null) {
            throw new NullPointerException("field == null");
        }
        if (this.staticValuesConstant != null) {
            throw new UnsupportedOperationException("static fields already sorted");
        }
        this.staticFields.add(field);
        this.staticValues.put(field, value);
    }

    public void addInstanceField(EncodedField field) {
        if (field == null) {
            throw new NullPointerException("field == null");
        }
        this.instanceFields.add(field);
    }

    public void addDirectMethod(EncodedMethod method) {
        if (method == null) {
            throw new NullPointerException("method == null");
        }
        this.directMethods.add(method);
    }

    public void addVirtualMethod(EncodedMethod method) {
        if (method == null) {
            throw new NullPointerException("method == null");
        }
        this.virtualMethods.add(method);
    }

    public ArrayList<EncodedMethod> getMethods() {
        int sz = this.directMethods.size() + this.virtualMethods.size();
        ArrayList<EncodedMethod> result = new ArrayList<EncodedMethod>(sz);
        result.addAll(this.directMethods);
        result.addAll(this.virtualMethods);
        return result;
    }

    public void debugPrint(Writer out, boolean verbose) {
        int i;
        PrintWriter pw = Writers.printWriterFor(out);
        int sz = this.staticFields.size();
        for (i = 0; i < sz; ++i) {
            pw.println("  sfields[" + i + "]: " + this.staticFields.get(i));
        }
        sz = this.instanceFields.size();
        for (i = 0; i < sz; ++i) {
            pw.println("  ifields[" + i + "]: " + this.instanceFields.get(i));
        }
        sz = this.directMethods.size();
        for (i = 0; i < sz; ++i) {
            pw.println("  dmeths[" + i + "]:");
            this.directMethods.get(i).debugPrint(pw, verbose);
        }
        sz = this.virtualMethods.size();
        for (i = 0; i < sz; ++i) {
            pw.println("  vmeths[" + i + "]:");
            this.virtualMethods.get(i).debugPrint(pw, verbose);
        }
    }

    @Override
    public void addContents(DexFile file) {
        if (!this.staticFields.isEmpty()) {
            this.getStaticValuesConstant();
            for (EncodedField field : this.staticFields) {
                field.addContents(file);
            }
        }
        if (!this.instanceFields.isEmpty()) {
            Collections.sort(this.instanceFields);
            for (EncodedField field : this.instanceFields) {
                field.addContents(file);
            }
        }
        if (!this.directMethods.isEmpty()) {
            Collections.sort(this.directMethods);
            for (EncodedMethod method : this.directMethods) {
                method.addContents(file);
            }
        }
        if (!this.virtualMethods.isEmpty()) {
            Collections.sort(this.virtualMethods);
            for (EncodedMethod method : this.virtualMethods) {
                method.addContents(file);
            }
        }
    }

    public CstArray getStaticValuesConstant() {
        if (this.staticValuesConstant == null && this.staticFields.size() != 0) {
            this.staticValuesConstant = this.makeStaticValuesConstant();
        }
        return this.staticValuesConstant;
    }

    private CstArray makeStaticValuesConstant() {
        EncodedField field;
        Constant cst;
        int size;
        Collections.sort(this.staticFields);
        for (size = this.staticFields.size(); size > 0 && !((cst = this.staticValues.get(field = this.staticFields.get(size - 1))) instanceof CstLiteralBits ? ((CstLiteralBits)cst).getLongBits() != 0L : cst != null); --size) {
        }
        if (size == 0) {
            return null;
        }
        CstArray.List list = new CstArray.List(size);
        for (int i = 0; i < size; ++i) {
            EncodedField field2 = this.staticFields.get(i);
            Constant cst2 = this.staticValues.get(field2);
            if (cst2 == null) {
                cst2 = Zeroes.zeroFor(field2.getRef().getType());
            }
            list.set(i, cst2);
        }
        list.setImmutable();
        return new CstArray(list);
    }

    @Override
    protected void place0(Section addedTo, int offset) {
        ByteArrayAnnotatedOutput out = new ByteArrayAnnotatedOutput();
        this.encodeOutput(addedTo.getFile(), out);
        this.encodedForm = out.toByteArray();
        this.setWriteSize(this.encodedForm.length);
    }

    private void encodeOutput(DexFile file, AnnotatedOutput out) {
        boolean annotates = out.annotates();
        if (annotates) {
            out.annotate(0, this.offsetString() + " class data for " + this.thisClass.toHuman());
        }
        ClassDataItem.encodeSize(file, out, "static_fields", this.staticFields.size());
        ClassDataItem.encodeSize(file, out, "instance_fields", this.instanceFields.size());
        ClassDataItem.encodeSize(file, out, "direct_methods", this.directMethods.size());
        ClassDataItem.encodeSize(file, out, "virtual_methods", this.virtualMethods.size());
        ClassDataItem.encodeList(file, out, "static_fields", this.staticFields);
        ClassDataItem.encodeList(file, out, "instance_fields", this.instanceFields);
        ClassDataItem.encodeList(file, out, "direct_methods", this.directMethods);
        ClassDataItem.encodeList(file, out, "virtual_methods", this.virtualMethods);
        if (annotates) {
            out.endAnnotation();
        }
    }

    private static void encodeSize(DexFile file, AnnotatedOutput out, String label, int size) {
        if (out.annotates()) {
            out.annotate(String.format("  %-21s %08x", label + "_size:", size));
        }
        out.writeUleb128(size);
    }

    private static void encodeList(DexFile file, AnnotatedOutput out, String label, ArrayList<? extends EncodedMember> list) {
        int size = list.size();
        int lastIndex = 0;
        if (size == 0) {
            return;
        }
        if (out.annotates()) {
            out.annotate(0, "  " + label + ":");
        }
        for (int i = 0; i < size; ++i) {
            lastIndex = list.get(i).encode(file, out, lastIndex, i);
        }
    }

    @Override
    public void writeTo0(DexFile file, AnnotatedOutput out) {
        boolean annotates = out.annotates();
        if (annotates) {
            this.encodeOutput(file, out);
        } else {
            out.write(this.encodedForm);
        }
    }
}

