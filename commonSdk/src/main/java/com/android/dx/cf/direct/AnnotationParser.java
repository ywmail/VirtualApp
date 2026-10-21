/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.cf.direct;

import com.android.dx.cf.direct.DirectClassFile;
import com.android.dx.cf.iface.ParseException;
import com.android.dx.cf.iface.ParseObserver;
import com.android.dx.rop.annotation.Annotation;
import com.android.dx.rop.annotation.AnnotationVisibility;
import com.android.dx.rop.annotation.Annotations;
import com.android.dx.rop.annotation.AnnotationsList;
import com.android.dx.rop.annotation.NameValuePair;
import com.android.dx.rop.cst.Constant;
import com.android.dx.rop.cst.ConstantPool;
import com.android.dx.rop.cst.CstAnnotation;
import com.android.dx.rop.cst.CstArray;
import com.android.dx.rop.cst.CstBoolean;
import com.android.dx.rop.cst.CstByte;
import com.android.dx.rop.cst.CstChar;
import com.android.dx.rop.cst.CstDouble;
import com.android.dx.rop.cst.CstEnumRef;
import com.android.dx.rop.cst.CstFloat;
import com.android.dx.rop.cst.CstInteger;
import com.android.dx.rop.cst.CstLiteralBits;
import com.android.dx.rop.cst.CstLong;
import com.android.dx.rop.cst.CstNat;
import com.android.dx.rop.cst.CstShort;
import com.android.dx.rop.cst.CstString;
import com.android.dx.rop.cst.CstType;
import com.android.dx.rop.type.Type;
import com.android.dx.util.ByteArray;
import com.android.dx.util.Hex;
import java.io.IOException;

public final class AnnotationParser {
    private final DirectClassFile cf;
    private final ConstantPool pool;
    private final ByteArray bytes;
    private final ParseObserver observer;
    private final ByteArray.MyDataInputStream input;
    private int parseCursor;

    public AnnotationParser(DirectClassFile cf, int offset, int length, ParseObserver observer) {
        if (cf == null) {
            throw new NullPointerException("cf == null");
        }
        this.cf = cf;
        this.pool = cf.getConstantPool();
        this.observer = observer;
        this.bytes = cf.getBytes().slice(offset, offset + length);
        this.input = this.bytes.makeDataInputStream();
        this.parseCursor = 0;
    }

    public Constant parseValueAttribute() {
        Constant result;
        try {
            result = this.parseValue();
            if (this.input.available() != 0) {
                throw new ParseException("extra data in attribute");
            }
        }
        catch (IOException ex) {
            throw new RuntimeException("shouldn't happen", ex);
        }
        return result;
    }

    public AnnotationsList parseParameterAttribute(AnnotationVisibility visibility) {
        AnnotationsList result;
        try {
            result = this.parseAnnotationsList(visibility);
            if (this.input.available() != 0) {
                throw new ParseException("extra data in attribute");
            }
        }
        catch (IOException ex) {
            throw new RuntimeException("shouldn't happen", ex);
        }
        return result;
    }

    public Annotations parseAnnotationAttribute(AnnotationVisibility visibility) {
        Annotations result;
        try {
            result = this.parseAnnotations(visibility);
            if (this.input.available() != 0) {
                throw new ParseException("extra data in attribute");
            }
        }
        catch (IOException ex) {
            throw new RuntimeException("shouldn't happen", ex);
        }
        return result;
    }

    private AnnotationsList parseAnnotationsList(AnnotationVisibility visibility) throws IOException {
        int count = this.input.readUnsignedByte();
        if (this.observer != null) {
            this.parsed(1, "num_parameters: " + Hex.u1(count));
        }
        AnnotationsList outerList = new AnnotationsList(count);
        for (int i = 0; i < count; ++i) {
            if (this.observer != null) {
                this.parsed(0, "parameter_annotations[" + i + "]:");
                this.changeIndent(1);
            }
            Annotations annotations = this.parseAnnotations(visibility);
            outerList.set(i, annotations);
            if (this.observer == null) continue;
            this.observer.changeIndent(-1);
        }
        outerList.setImmutable();
        return outerList;
    }

    private Annotations parseAnnotations(AnnotationVisibility visibility) throws IOException {
        int count = this.input.readUnsignedShort();
        if (this.observer != null) {
            this.parsed(2, "num_annotations: " + Hex.u2(count));
        }
        Annotations annotations = new Annotations();
        for (int i = 0; i < count; ++i) {
            if (this.observer != null) {
                this.parsed(0, "annotations[" + i + "]:");
                this.changeIndent(1);
            }
            Annotation annotation = this.parseAnnotation(visibility);
            annotations.add(annotation);
            if (this.observer == null) continue;
            this.observer.changeIndent(-1);
        }
        annotations.setImmutable();
        return annotations;
    }

    private Annotation parseAnnotation(AnnotationVisibility visibility) throws IOException {
        this.requireLength(4);
        int typeIndex = this.input.readUnsignedShort();
        int numElements = this.input.readUnsignedShort();
        CstString typeString = (CstString)this.pool.get(typeIndex);
        CstType type = new CstType(Type.intern(typeString.getString()));
        if (this.observer != null) {
            this.parsed(2, "type: " + type.toHuman());
            this.parsed(2, "num_elements: " + numElements);
        }
        Annotation annotation = new Annotation(type, visibility);
        for (int i = 0; i < numElements; ++i) {
            if (this.observer != null) {
                this.parsed(0, "elements[" + i + "]:");
                this.changeIndent(1);
            }
            NameValuePair element = this.parseElement();
            annotation.add(element);
            if (this.observer == null) continue;
            this.changeIndent(-1);
        }
        annotation.setImmutable();
        return annotation;
    }

    private NameValuePair parseElement() throws IOException {
        this.requireLength(5);
        int elementNameIndex = this.input.readUnsignedShort();
        CstString elementName = (CstString)this.pool.get(elementNameIndex);
        if (this.observer != null) {
            this.parsed(2, "element_name: " + elementName.toHuman());
            this.parsed(0, "value: ");
            this.changeIndent(1);
        }
        Constant value = this.parseValue();
        if (this.observer != null) {
            this.changeIndent(-1);
        }
        return new NameValuePair(elementName, value);
    }

    private Constant parseValue() throws IOException {
        int tag = this.input.readUnsignedByte();
        if (this.observer != null) {
            CstString humanTag = new CstString(Character.toString((char)tag));
            this.parsed(1, "tag: " + humanTag.toQuoted());
        }
        switch (tag) {
            case 66: {
                CstLiteralBits value = (CstInteger)this.parseConstant();
                return CstByte.make(((CstInteger)value).getValue());
            }
            case 67: {
                CstLiteralBits value = (CstInteger)this.parseConstant();
                int intValue = ((CstInteger)value).getValue();
                return CstChar.make(((CstInteger)value).getValue());
            }
            case 68: {
                CstLiteralBits value = (CstDouble)this.parseConstant();
                return value;
            }
            case 70: {
                CstLiteralBits value = (CstFloat)this.parseConstant();
                return value;
            }
            case 73: {
                CstLiteralBits value = (CstInteger)this.parseConstant();
                return value;
            }
            case 74: {
                CstLiteralBits value = (CstLong)this.parseConstant();
                return value;
            }
            case 83: {
                CstLiteralBits value = (CstInteger)this.parseConstant();
                return CstShort.make(((CstInteger)value).getValue());
            }
            case 90: {
                CstLiteralBits value = (CstInteger)this.parseConstant();
                return CstBoolean.make(((CstInteger)value).getValue());
            }
            case 99: {
                int classInfoIndex = this.input.readUnsignedShort();
                CstString value = (CstString)this.pool.get(classInfoIndex);
                Type type = Type.internReturnType(value.getString());
                if (this.observer != null) {
                    this.parsed(2, "class_info: " + type.toHuman());
                }
                return new CstType(type);
            }
            case 115: {
                return this.parseConstant();
            }
            case 101: {
                this.requireLength(4);
                int typeNameIndex = this.input.readUnsignedShort();
                int constNameIndex = this.input.readUnsignedShort();
                CstString typeName = (CstString)this.pool.get(typeNameIndex);
                CstString constName = (CstString)this.pool.get(constNameIndex);
                if (this.observer != null) {
                    this.parsed(2, "type_name: " + typeName.toHuman());
                    this.parsed(2, "const_name: " + constName.toHuman());
                }
                return new CstEnumRef(new CstNat(constName, typeName));
            }
            case 64: {
                Annotation annotation = this.parseAnnotation(AnnotationVisibility.EMBEDDED);
                return new CstAnnotation(annotation);
            }
            case 91: {
                this.requireLength(2);
                int numValues = this.input.readUnsignedShort();
                CstArray.List list = new CstArray.List(numValues);
                if (this.observer != null) {
                    this.parsed(2, "num_values: " + numValues);
                    this.changeIndent(1);
                }
                for (int i = 0; i < numValues; ++i) {
                    if (this.observer != null) {
                        this.changeIndent(-1);
                        this.parsed(0, "element_value[" + i + "]:");
                        this.changeIndent(1);
                    }
                    list.set(i, this.parseValue());
                }
                if (this.observer != null) {
                    this.changeIndent(-1);
                }
                list.setImmutable();
                return new CstArray(list);
            }
        }
        throw new ParseException("unknown annotation tag: " + Hex.u1(tag));
    }

    private Constant parseConstant() throws IOException {
        int constValueIndex = this.input.readUnsignedShort();
        Constant value = this.pool.get(constValueIndex);
        if (this.observer != null) {
            String human = value instanceof CstString ? ((CstString)value).toQuoted() : value.toHuman();
            this.parsed(2, "constant_value: " + human);
        }
        return value;
    }

    private void requireLength(int requiredLength) throws IOException {
        if (this.input.available() < requiredLength) {
            throw new ParseException("truncated annotation attribute");
        }
    }

    private void parsed(int length, String message) {
        this.observer.parsed(this.bytes, this.parseCursor, length, message);
        this.parseCursor += length;
    }

    private void changeIndent(int indent) {
        this.observer.changeIndent(indent);
    }
}

