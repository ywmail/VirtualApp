/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.dex.file;

import com.android.dex.EncodedValueCodec;
import com.android.dx.dex.file.DexFile;
import com.android.dx.dex.file.StringIdsSection;
import com.android.dx.dex.file.TypeIdsSection;
import com.android.dx.rop.annotation.Annotation;
import com.android.dx.rop.annotation.NameValuePair;
import com.android.dx.rop.cst.Constant;
import com.android.dx.rop.cst.CstAnnotation;
import com.android.dx.rop.cst.CstArray;
import com.android.dx.rop.cst.CstBoolean;
import com.android.dx.rop.cst.CstByte;
import com.android.dx.rop.cst.CstChar;
import com.android.dx.rop.cst.CstDouble;
import com.android.dx.rop.cst.CstEnumRef;
import com.android.dx.rop.cst.CstFieldRef;
import com.android.dx.rop.cst.CstFloat;
import com.android.dx.rop.cst.CstInteger;
import com.android.dx.rop.cst.CstKnownNull;
import com.android.dx.rop.cst.CstLiteralBits;
import com.android.dx.rop.cst.CstLong;
import com.android.dx.rop.cst.CstMethodHandle;
import com.android.dx.rop.cst.CstMethodRef;
import com.android.dx.rop.cst.CstProtoRef;
import com.android.dx.rop.cst.CstShort;
import com.android.dx.rop.cst.CstString;
import com.android.dx.rop.cst.CstType;
import com.android.dx.util.AnnotatedOutput;
import com.android.dx.util.Hex;
import java.util.Collection;

public final class ValueEncoder {
    private static final int VALUE_BYTE = 0;
    private static final int VALUE_SHORT = 2;
    private static final int VALUE_CHAR = 3;
    private static final int VALUE_INT = 4;
    private static final int VALUE_LONG = 6;
    private static final int VALUE_FLOAT = 16;
    private static final int VALUE_DOUBLE = 17;
    private static final int VALUE_METHOD_TYPE = 21;
    private static final int VALUE_METHOD_HANDLE = 22;
    private static final int VALUE_STRING = 23;
    private static final int VALUE_TYPE = 24;
    private static final int VALUE_FIELD = 25;
    private static final int VALUE_METHOD = 26;
    private static final int VALUE_ENUM = 27;
    private static final int VALUE_ARRAY = 28;
    private static final int VALUE_ANNOTATION = 29;
    private static final int VALUE_NULL = 30;
    private static final int VALUE_BOOLEAN = 31;
    private final DexFile file;
    private final AnnotatedOutput out;

    public ValueEncoder(DexFile file, AnnotatedOutput out) {
        if (file == null) {
            throw new NullPointerException("file == null");
        }
        if (out == null) {
            throw new NullPointerException("out == null");
        }
        this.file = file;
        this.out = out;
    }

    public void writeConstant(Constant cst) {
        int type = ValueEncoder.constantToValueType(cst);
        switch (type) {
            case 0: 
            case 2: 
            case 4: 
            case 6: {
                long value = ((CstLiteralBits)cst).getLongBits();
                EncodedValueCodec.writeSignedIntegralValue(this.out, type, value);
                break;
            }
            case 3: {
                long value = ((CstLiteralBits)cst).getLongBits();
                EncodedValueCodec.writeUnsignedIntegralValue(this.out, type, value);
                break;
            }
            case 16: {
                long value = ((CstFloat)cst).getLongBits() << 32;
                EncodedValueCodec.writeRightZeroExtendedValue(this.out, type, value);
                break;
            }
            case 17: {
                long value = ((CstDouble)cst).getLongBits();
                EncodedValueCodec.writeRightZeroExtendedValue(this.out, type, value);
                break;
            }
            case 21: {
                int index = this.file.getProtoIds().indexOf(((CstProtoRef)cst).getPrototype());
                EncodedValueCodec.writeUnsignedIntegralValue(this.out, type, index);
                break;
            }
            case 22: {
                int index = this.file.getMethodHandles().indexOf((CstMethodHandle)cst);
                EncodedValueCodec.writeUnsignedIntegralValue(this.out, type, index);
                break;
            }
            case 23: {
                int index = this.file.getStringIds().indexOf((CstString)cst);
                EncodedValueCodec.writeUnsignedIntegralValue(this.out, type, index);
                break;
            }
            case 24: {
                int index = this.file.getTypeIds().indexOf((CstType)cst);
                EncodedValueCodec.writeUnsignedIntegralValue(this.out, type, index);
                break;
            }
            case 25: {
                int index = this.file.getFieldIds().indexOf((CstFieldRef)cst);
                EncodedValueCodec.writeUnsignedIntegralValue(this.out, type, index);
                break;
            }
            case 26: {
                int index = this.file.getMethodIds().indexOf((CstMethodRef)cst);
                EncodedValueCodec.writeUnsignedIntegralValue(this.out, type, index);
                break;
            }
            case 27: {
                CstFieldRef fieldRef = ((CstEnumRef)cst).getFieldRef();
                int index = this.file.getFieldIds().indexOf(fieldRef);
                EncodedValueCodec.writeUnsignedIntegralValue(this.out, type, index);
                break;
            }
            case 28: {
                this.out.writeByte(type);
                this.writeArray((CstArray)cst, false);
                break;
            }
            case 29: {
                this.out.writeByte(type);
                this.writeAnnotation(((CstAnnotation)cst).getAnnotation(), false);
                break;
            }
            case 30: {
                this.out.writeByte(type);
                break;
            }
            case 31: {
                int value = ((CstBoolean)cst).getIntBits();
                this.out.writeByte(type | value << 5);
                break;
            }
            default: {
                throw new RuntimeException("Shouldn't happen");
            }
        }
    }

    private static int constantToValueType(Constant cst) {
        if (cst instanceof CstByte) {
            return 0;
        }
        if (cst instanceof CstShort) {
            return 2;
        }
        if (cst instanceof CstChar) {
            return 3;
        }
        if (cst instanceof CstInteger) {
            return 4;
        }
        if (cst instanceof CstLong) {
            return 6;
        }
        if (cst instanceof CstFloat) {
            return 16;
        }
        if (cst instanceof CstDouble) {
            return 17;
        }
        if (cst instanceof CstProtoRef) {
            return 21;
        }
        if (cst instanceof CstMethodHandle) {
            return 22;
        }
        if (cst instanceof CstString) {
            return 23;
        }
        if (cst instanceof CstType) {
            return 24;
        }
        if (cst instanceof CstFieldRef) {
            return 25;
        }
        if (cst instanceof CstMethodRef) {
            return 26;
        }
        if (cst instanceof CstEnumRef) {
            return 27;
        }
        if (cst instanceof CstArray) {
            return 28;
        }
        if (cst instanceof CstAnnotation) {
            return 29;
        }
        if (cst instanceof CstKnownNull) {
            return 30;
        }
        if (cst instanceof CstBoolean) {
            return 31;
        }
        throw new RuntimeException("Shouldn't happen");
    }

    public void writeArray(CstArray array, boolean topLevel) {
        boolean annotates = topLevel && this.out.annotates();
        CstArray.List list = array.getList();
        int size = list.size();
        if (annotates) {
            this.out.annotate("  size: " + Hex.u4(size));
        }
        this.out.writeUleb128(size);
        for (int i = 0; i < size; ++i) {
            Constant cst = list.get(i);
            if (annotates) {
                this.out.annotate("  [" + Integer.toHexString(i) + "] " + ValueEncoder.constantToHuman(cst));
            }
            this.writeConstant(cst);
        }
        if (annotates) {
            this.out.endAnnotation();
        }
    }

    public void writeAnnotation(Annotation annotation, boolean topLevel) {
        boolean annotates = topLevel && this.out.annotates();
        StringIdsSection stringIds = this.file.getStringIds();
        TypeIdsSection typeIds = this.file.getTypeIds();
        CstType type = annotation.getType();
        int typeIdx = typeIds.indexOf(type);
        if (annotates) {
            this.out.annotate("  type_idx: " + Hex.u4(typeIdx) + " // " + type.toHuman());
        }
        this.out.writeUleb128(typeIds.indexOf(annotation.getType()));
        Collection<NameValuePair> pairs = annotation.getNameValuePairs();
        int size = pairs.size();
        if (annotates) {
            this.out.annotate("  size: " + Hex.u4(size));
        }
        this.out.writeUleb128(size);
        int at = 0;
        for (NameValuePair pair : pairs) {
            CstString name = pair.getName();
            int nameIdx = stringIds.indexOf(name);
            Constant value = pair.getValue();
            if (annotates) {
                this.out.annotate(0, "  elements[" + at + "]:");
                ++at;
                this.out.annotate("    name_idx: " + Hex.u4(nameIdx) + " // " + name.toHuman());
            }
            this.out.writeUleb128(nameIdx);
            if (annotates) {
                this.out.annotate("    value: " + ValueEncoder.constantToHuman(value));
            }
            this.writeConstant(value);
        }
        if (annotates) {
            this.out.endAnnotation();
        }
    }

    public static String constantToHuman(Constant cst) {
        int type = ValueEncoder.constantToValueType(cst);
        if (type == 30) {
            return "null";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(cst.typeName());
        sb.append(' ');
        sb.append(cst.toHuman());
        return sb.toString();
    }

    public static void addContents(DexFile file, Annotation annotation) {
        TypeIdsSection typeIds = file.getTypeIds();
        StringIdsSection stringIds = file.getStringIds();
        typeIds.intern(annotation.getType());
        for (NameValuePair pair : annotation.getNameValuePairs()) {
            stringIds.intern(pair.getName());
            ValueEncoder.addContents(file, pair.getValue());
        }
    }

    public static void addContents(DexFile file, Constant cst) {
        if (cst instanceof CstAnnotation) {
            ValueEncoder.addContents(file, ((CstAnnotation)cst).getAnnotation());
        } else if (cst instanceof CstArray) {
            CstArray.List list = ((CstArray)cst).getList();
            int size = list.size();
            for (int i = 0; i < size; ++i) {
                ValueEncoder.addContents(file, list.get(i));
            }
        } else {
            file.internIfAppropriate(cst);
        }
    }
}

