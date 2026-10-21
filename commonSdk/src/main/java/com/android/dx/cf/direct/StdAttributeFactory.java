/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.cf.direct;

import com.android.dx.cf.attrib.AttAnnotationDefault;
import com.android.dx.cf.attrib.AttBootstrapMethods;
import com.android.dx.cf.attrib.AttCode;
import com.android.dx.cf.attrib.AttConstantValue;
import com.android.dx.cf.attrib.AttDeprecated;
import com.android.dx.cf.attrib.AttEnclosingMethod;
import com.android.dx.cf.attrib.AttExceptions;
import com.android.dx.cf.attrib.AttInnerClasses;
import com.android.dx.cf.attrib.AttLineNumberTable;
import com.android.dx.cf.attrib.AttLocalVariableTable;
import com.android.dx.cf.attrib.AttLocalVariableTypeTable;
import com.android.dx.cf.attrib.AttRuntimeInvisibleAnnotations;
import com.android.dx.cf.attrib.AttRuntimeInvisibleParameterAnnotations;
import com.android.dx.cf.attrib.AttRuntimeVisibleAnnotations;
import com.android.dx.cf.attrib.AttRuntimeVisibleParameterAnnotations;
import com.android.dx.cf.attrib.AttSignature;
import com.android.dx.cf.attrib.AttSourceDebugExtension;
import com.android.dx.cf.attrib.AttSourceFile;
import com.android.dx.cf.attrib.AttSynthetic;
import com.android.dx.cf.attrib.InnerClassList;
import com.android.dx.cf.code.BootstrapMethodArgumentsList;
import com.android.dx.cf.code.BootstrapMethodsList;
import com.android.dx.cf.code.ByteCatchList;
import com.android.dx.cf.code.BytecodeArray;
import com.android.dx.cf.code.LineNumberList;
import com.android.dx.cf.code.LocalVariableList;
import com.android.dx.cf.direct.AnnotationParser;
import com.android.dx.cf.direct.AttributeFactory;
import com.android.dx.cf.direct.AttributeListParser;
import com.android.dx.cf.direct.CodeObserver;
import com.android.dx.cf.direct.DirectClassFile;
import com.android.dx.cf.iface.Attribute;
import com.android.dx.cf.iface.ParseException;
import com.android.dx.cf.iface.ParseObserver;
import com.android.dx.cf.iface.StdAttributeList;
import com.android.dx.rop.annotation.AnnotationVisibility;
import com.android.dx.rop.annotation.Annotations;
import com.android.dx.rop.annotation.AnnotationsList;
import com.android.dx.rop.code.AccessFlags;
import com.android.dx.rop.cst.Constant;
import com.android.dx.rop.cst.ConstantPool;
import com.android.dx.rop.cst.CstMethodHandle;
import com.android.dx.rop.cst.CstNat;
import com.android.dx.rop.cst.CstString;
import com.android.dx.rop.cst.CstType;
import com.android.dx.rop.cst.TypedConstant;
import com.android.dx.rop.type.TypeList;
import com.android.dx.util.ByteArray;
import com.android.dx.util.Hex;
import java.io.IOException;

public class StdAttributeFactory
extends AttributeFactory {
    public static final StdAttributeFactory THE_ONE = new StdAttributeFactory();

    @Override
    protected Attribute parse0(DirectClassFile cf, int context, String name, int offset, int length, ParseObserver observer) {
        switch (context) {
            case 0: {
                if (name == "BootstrapMethods") {
                    return this.bootstrapMethods(cf, offset, length, observer);
                }
                if (name == "Deprecated") {
                    return this.deprecated(cf, offset, length, observer);
                }
                if (name == "EnclosingMethod") {
                    return this.enclosingMethod(cf, offset, length, observer);
                }
                if (name == "InnerClasses") {
                    return this.innerClasses(cf, offset, length, observer);
                }
                if (name == "RuntimeInvisibleAnnotations") {
                    return this.runtimeInvisibleAnnotations(cf, offset, length, observer);
                }
                if (name == "RuntimeVisibleAnnotations") {
                    return this.runtimeVisibleAnnotations(cf, offset, length, observer);
                }
                if (name == "Synthetic") {
                    return this.synthetic(cf, offset, length, observer);
                }
                if (name == "Signature") {
                    return this.signature(cf, offset, length, observer);
                }
                if (name == "SourceDebugExtension") {
                    return this.sourceDebugExtension(cf, offset, length, observer);
                }
                if (name != "SourceFile") break;
                return this.sourceFile(cf, offset, length, observer);
            }
            case 1: {
                if (name == "ConstantValue") {
                    return this.constantValue(cf, offset, length, observer);
                }
                if (name == "Deprecated") {
                    return this.deprecated(cf, offset, length, observer);
                }
                if (name == "RuntimeInvisibleAnnotations") {
                    return this.runtimeInvisibleAnnotations(cf, offset, length, observer);
                }
                if (name == "RuntimeVisibleAnnotations") {
                    return this.runtimeVisibleAnnotations(cf, offset, length, observer);
                }
                if (name == "Signature") {
                    return this.signature(cf, offset, length, observer);
                }
                if (name != "Synthetic") break;
                return this.synthetic(cf, offset, length, observer);
            }
            case 2: {
                if (name == "AnnotationDefault") {
                    return this.annotationDefault(cf, offset, length, observer);
                }
                if (name == "Code") {
                    return this.code(cf, offset, length, observer);
                }
                if (name == "Deprecated") {
                    return this.deprecated(cf, offset, length, observer);
                }
                if (name == "Exceptions") {
                    return this.exceptions(cf, offset, length, observer);
                }
                if (name == "RuntimeInvisibleAnnotations") {
                    return this.runtimeInvisibleAnnotations(cf, offset, length, observer);
                }
                if (name == "RuntimeVisibleAnnotations") {
                    return this.runtimeVisibleAnnotations(cf, offset, length, observer);
                }
                if (name == "RuntimeInvisibleParameterAnnotations") {
                    return this.runtimeInvisibleParameterAnnotations(cf, offset, length, observer);
                }
                if (name == "RuntimeVisibleParameterAnnotations") {
                    return this.runtimeVisibleParameterAnnotations(cf, offset, length, observer);
                }
                if (name == "Signature") {
                    return this.signature(cf, offset, length, observer);
                }
                if (name != "Synthetic") break;
                return this.synthetic(cf, offset, length, observer);
            }
            case 3: {
                if (name == "LineNumberTable") {
                    return this.lineNumberTable(cf, offset, length, observer);
                }
                if (name == "LocalVariableTable") {
                    return this.localVariableTable(cf, offset, length, observer);
                }
                if (name != "LocalVariableTypeTable") break;
                return this.localVariableTypeTable(cf, offset, length, observer);
            }
        }
        return super.parse0(cf, context, name, offset, length, observer);
    }

    private Attribute annotationDefault(DirectClassFile cf, int offset, int length, ParseObserver observer) {
        if (length < 2) {
            StdAttributeFactory.throwSeverelyTruncated();
        }
        AnnotationParser ap = new AnnotationParser(cf, offset, length, observer);
        Constant cst = ap.parseValueAttribute();
        return new AttAnnotationDefault(cst, length);
    }

    private Attribute bootstrapMethods(DirectClassFile cf, int offset, int length, ParseObserver observer) {
        if (length < 2) {
            return StdAttributeFactory.throwSeverelyTruncated();
        }
        ByteArray bytes = cf.getBytes();
        int numMethods = bytes.getUnsignedShort(offset);
        if (observer != null) {
            observer.parsed(bytes, offset, 2, "num_boostrap_methods: " + Hex.u2(numMethods));
        }
        BootstrapMethodsList methods = this.parseBootstrapMethods(bytes, cf.getConstantPool(), cf.getThisClass(), numMethods, offset += 2, length -= 2, observer);
        return new AttBootstrapMethods(methods);
    }

    private Attribute code(DirectClassFile cf, int offset, int length, ParseObserver observer) {
        int exceptionTableLength;
        ByteCatchList catches;
        if (length < 12) {
            return StdAttributeFactory.throwSeverelyTruncated();
        }
        ByteArray bytes = cf.getBytes();
        ConstantPool pool = cf.getConstantPool();
        int maxStack = bytes.getUnsignedShort(offset);
        int maxLocals = bytes.getUnsignedShort(offset + 2);
        int codeLength = bytes.getInt(offset + 4);
        int origOffset = offset;
        if (observer != null) {
            observer.parsed(bytes, offset, 2, "max_stack: " + Hex.u2(maxStack));
            observer.parsed(bytes, offset + 2, 2, "max_locals: " + Hex.u2(maxLocals));
            observer.parsed(bytes, offset + 4, 4, "code_length: " + Hex.u4(codeLength));
        }
        offset += 8;
        if ((length -= 8) < codeLength + 4) {
            return StdAttributeFactory.throwTruncated();
        }
        int codeOffset = offset;
        offset += codeLength;
        length -= codeLength;
        BytecodeArray code = new BytecodeArray(bytes.slice(codeOffset, codeOffset + codeLength), pool);
        if (observer != null) {
            code.forEach(new CodeObserver(code.getBytes(), observer));
        }
        ByteCatchList byteCatchList = catches = (exceptionTableLength = bytes.getUnsignedShort(offset)) == 0 ? ByteCatchList.EMPTY : new ByteCatchList(exceptionTableLength);
        if (observer != null) {
            observer.parsed(bytes, offset, 2, "exception_table_length: " + Hex.u2(exceptionTableLength));
        }
        offset += 2;
        if ((length -= 2) < exceptionTableLength * 8 + 2) {
            return StdAttributeFactory.throwTruncated();
        }
        for (int i = 0; i < exceptionTableLength; ++i) {
            if (observer != null) {
                observer.changeIndent(1);
            }
            int startPc = bytes.getUnsignedShort(offset);
            int endPc = bytes.getUnsignedShort(offset + 2);
            int handlerPc = bytes.getUnsignedShort(offset + 4);
            int catchTypeIdx = bytes.getUnsignedShort(offset + 6);
            CstType catchType = (CstType)pool.get0Ok(catchTypeIdx);
            catches.set(i, startPc, endPc, handlerPc, catchType);
            if (observer != null) {
                observer.parsed(bytes, offset, 8, Hex.u2(startPc) + ".." + Hex.u2(endPc) + " -> " + Hex.u2(handlerPc) + " " + (catchType == null ? "<any>" : catchType.toHuman()));
            }
            offset += 8;
            length -= 8;
            if (observer == null) continue;
            observer.changeIndent(-1);
        }
        catches.setImmutable();
        AttributeListParser parser = new AttributeListParser(cf, 3, offset, this);
        parser.setObserver(observer);
        StdAttributeList attributes = parser.getList();
        attributes.setImmutable();
        int attributeByteCount = parser.getEndOffset() - offset;
        if (attributeByteCount != length) {
            return StdAttributeFactory.throwBadLength(attributeByteCount + (offset - origOffset));
        }
        return new AttCode(maxStack, maxLocals, code, catches, attributes);
    }

    private Attribute constantValue(DirectClassFile cf, int offset, int length, ParseObserver observer) {
        if (length != 2) {
            return StdAttributeFactory.throwBadLength(2);
        }
        ByteArray bytes = cf.getBytes();
        ConstantPool pool = cf.getConstantPool();
        int idx = bytes.getUnsignedShort(offset);
        TypedConstant cst = (TypedConstant)pool.get(idx);
        AttConstantValue result = new AttConstantValue(cst);
        if (observer != null) {
            observer.parsed(bytes, offset, 2, "value: " + cst);
        }
        return result;
    }

    private Attribute deprecated(DirectClassFile cf, int offset, int length, ParseObserver observer) {
        if (length != 0) {
            return StdAttributeFactory.throwBadLength(0);
        }
        return new AttDeprecated();
    }

    private Attribute enclosingMethod(DirectClassFile cf, int offset, int length, ParseObserver observer) {
        if (length != 4) {
            StdAttributeFactory.throwBadLength(4);
        }
        ByteArray bytes = cf.getBytes();
        ConstantPool pool = cf.getConstantPool();
        int idx = bytes.getUnsignedShort(offset);
        CstType type = (CstType)pool.get(idx);
        idx = bytes.getUnsignedShort(offset + 2);
        CstNat method = (CstNat)pool.get0Ok(idx);
        AttEnclosingMethod result = new AttEnclosingMethod(type, method);
        if (observer != null) {
            observer.parsed(bytes, offset, 2, "class: " + type);
            observer.parsed(bytes, offset + 2, 2, "method: " + DirectClassFile.stringOrNone(method));
        }
        return result;
    }

    private Attribute exceptions(DirectClassFile cf, int offset, int length, ParseObserver observer) {
        if (length < 2) {
            return StdAttributeFactory.throwSeverelyTruncated();
        }
        ByteArray bytes = cf.getBytes();
        int count = bytes.getUnsignedShort(offset);
        if (observer != null) {
            observer.parsed(bytes, offset, 2, "number_of_exceptions: " + Hex.u2(count));
        }
        offset += 2;
        if ((length -= 2) != count * 2) {
            StdAttributeFactory.throwBadLength(count * 2 + 2);
        }
        TypeList list = cf.makeTypeList(offset, count);
        return new AttExceptions(list);
    }

    private Attribute innerClasses(DirectClassFile cf, int offset, int length, ParseObserver observer) {
        if (length < 2) {
            return StdAttributeFactory.throwSeverelyTruncated();
        }
        ByteArray bytes = cf.getBytes();
        ConstantPool pool = cf.getConstantPool();
        int count = bytes.getUnsignedShort(offset);
        if (observer != null) {
            observer.parsed(bytes, offset, 2, "number_of_classes: " + Hex.u2(count));
        }
        offset += 2;
        if ((length -= 2) != count * 8) {
            StdAttributeFactory.throwBadLength(count * 8 + 2);
        }
        InnerClassList list = new InnerClassList(count);
        for (int i = 0; i < count; ++i) {
            int innerClassIdx = bytes.getUnsignedShort(offset);
            int outerClassIdx = bytes.getUnsignedShort(offset + 2);
            int nameIdx = bytes.getUnsignedShort(offset + 4);
            int accessFlags = bytes.getUnsignedShort(offset + 6);
            CstType innerClass = (CstType)pool.get(innerClassIdx);
            CstType outerClass = (CstType)pool.get0Ok(outerClassIdx);
            CstString name = (CstString)pool.get0Ok(nameIdx);
            list.set(i, innerClass, outerClass, name, accessFlags);
            if (observer != null) {
                observer.parsed(bytes, offset, 2, "inner_class: " + DirectClassFile.stringOrNone(innerClass));
                observer.parsed(bytes, offset + 2, 2, "  outer_class: " + DirectClassFile.stringOrNone(outerClass));
                observer.parsed(bytes, offset + 4, 2, "  name: " + DirectClassFile.stringOrNone(name));
                observer.parsed(bytes, offset + 6, 2, "  access_flags: " + AccessFlags.innerClassString(accessFlags));
            }
            offset += 8;
        }
        list.setImmutable();
        return new AttInnerClasses(list);
    }

    private Attribute lineNumberTable(DirectClassFile cf, int offset, int length, ParseObserver observer) {
        if (length < 2) {
            return StdAttributeFactory.throwSeverelyTruncated();
        }
        ByteArray bytes = cf.getBytes();
        int count = bytes.getUnsignedShort(offset);
        if (observer != null) {
            observer.parsed(bytes, offset, 2, "line_number_table_length: " + Hex.u2(count));
        }
        offset += 2;
        if ((length -= 2) != count * 4) {
            StdAttributeFactory.throwBadLength(count * 4 + 2);
        }
        LineNumberList list = new LineNumberList(count);
        for (int i = 0; i < count; ++i) {
            int startPc = bytes.getUnsignedShort(offset);
            int lineNumber = bytes.getUnsignedShort(offset + 2);
            list.set(i, startPc, lineNumber);
            if (observer != null) {
                observer.parsed(bytes, offset, 4, Hex.u2(startPc) + " " + lineNumber);
            }
            offset += 4;
        }
        list.setImmutable();
        return new AttLineNumberTable(list);
    }

    private Attribute localVariableTable(DirectClassFile cf, int offset, int length, ParseObserver observer) {
        if (length < 2) {
            return StdAttributeFactory.throwSeverelyTruncated();
        }
        ByteArray bytes = cf.getBytes();
        int count = bytes.getUnsignedShort(offset);
        if (observer != null) {
            observer.parsed(bytes, offset, 2, "local_variable_table_length: " + Hex.u2(count));
        }
        LocalVariableList list = this.parseLocalVariables(bytes.slice(offset + 2, offset + length), cf.getConstantPool(), observer, count, false);
        return new AttLocalVariableTable(list);
    }

    private Attribute localVariableTypeTable(DirectClassFile cf, int offset, int length, ParseObserver observer) {
        if (length < 2) {
            return StdAttributeFactory.throwSeverelyTruncated();
        }
        ByteArray bytes = cf.getBytes();
        int count = bytes.getUnsignedShort(offset);
        if (observer != null) {
            observer.parsed(bytes, offset, 2, "local_variable_type_table_length: " + Hex.u2(count));
        }
        LocalVariableList list = this.parseLocalVariables(bytes.slice(offset + 2, offset + length), cf.getConstantPool(), observer, count, true);
        return new AttLocalVariableTypeTable(list);
    }

    private LocalVariableList parseLocalVariables(ByteArray bytes, ConstantPool pool, ParseObserver observer, int count, boolean typeTable) {
        if (bytes.size() != count * 10) {
            StdAttributeFactory.throwBadLength(count * 10 + 2);
        }
        ByteArray.MyDataInputStream in = bytes.makeDataInputStream();
        LocalVariableList list = new LocalVariableList(count);
        try {
            for (int i = 0; i < count; ++i) {
                int startPc = in.readUnsignedShort();
                int length = in.readUnsignedShort();
                int nameIdx = in.readUnsignedShort();
                int typeIdx = in.readUnsignedShort();
                int index = in.readUnsignedShort();
                CstString name = (CstString)pool.get(nameIdx);
                CstString type = (CstString)pool.get(typeIdx);
                CstString descriptor = null;
                CstString signature = null;
                if (typeTable) {
                    signature = type;
                } else {
                    descriptor = type;
                }
                list.set(i, startPc, length, name, descriptor, signature, index);
                if (observer == null) continue;
                observer.parsed(bytes, i * 10, 10, Hex.u2(startPc) + ".." + Hex.u2(startPc + length) + " " + Hex.u2(index) + " " + name.toHuman() + " " + type.toHuman());
            }
        }
        catch (IOException ex) {
            throw new RuntimeException("shouldn't happen", ex);
        }
        list.setImmutable();
        return list;
    }

    private Attribute runtimeInvisibleAnnotations(DirectClassFile cf, int offset, int length, ParseObserver observer) {
        if (length < 2) {
            StdAttributeFactory.throwSeverelyTruncated();
        }
        AnnotationParser ap = new AnnotationParser(cf, offset, length, observer);
        Annotations annotations = ap.parseAnnotationAttribute(AnnotationVisibility.BUILD);
        return new AttRuntimeInvisibleAnnotations(annotations, length);
    }

    private Attribute runtimeVisibleAnnotations(DirectClassFile cf, int offset, int length, ParseObserver observer) {
        if (length < 2) {
            StdAttributeFactory.throwSeverelyTruncated();
        }
        AnnotationParser ap = new AnnotationParser(cf, offset, length, observer);
        Annotations annotations = ap.parseAnnotationAttribute(AnnotationVisibility.RUNTIME);
        return new AttRuntimeVisibleAnnotations(annotations, length);
    }

    private Attribute runtimeInvisibleParameterAnnotations(DirectClassFile cf, int offset, int length, ParseObserver observer) {
        if (length < 2) {
            StdAttributeFactory.throwSeverelyTruncated();
        }
        AnnotationParser ap = new AnnotationParser(cf, offset, length, observer);
        AnnotationsList list = ap.parseParameterAttribute(AnnotationVisibility.BUILD);
        return new AttRuntimeInvisibleParameterAnnotations(list, length);
    }

    private Attribute runtimeVisibleParameterAnnotations(DirectClassFile cf, int offset, int length, ParseObserver observer) {
        if (length < 2) {
            StdAttributeFactory.throwSeverelyTruncated();
        }
        AnnotationParser ap = new AnnotationParser(cf, offset, length, observer);
        AnnotationsList list = ap.parseParameterAttribute(AnnotationVisibility.RUNTIME);
        return new AttRuntimeVisibleParameterAnnotations(list, length);
    }

    private Attribute signature(DirectClassFile cf, int offset, int length, ParseObserver observer) {
        if (length != 2) {
            StdAttributeFactory.throwBadLength(2);
        }
        ByteArray bytes = cf.getBytes();
        ConstantPool pool = cf.getConstantPool();
        int idx = bytes.getUnsignedShort(offset);
        CstString cst = (CstString)pool.get(idx);
        AttSignature result = new AttSignature(cst);
        if (observer != null) {
            observer.parsed(bytes, offset, 2, "signature: " + cst);
        }
        return result;
    }

    private Attribute sourceDebugExtension(DirectClassFile cf, int offset, int length, ParseObserver observer) {
        ByteArray bytes = cf.getBytes().slice(offset, offset + length);
        CstString smapString = new CstString(bytes);
        AttSourceDebugExtension result = new AttSourceDebugExtension(smapString);
        if (observer != null) {
            String decoded = smapString.getString();
            observer.parsed(bytes, offset, length, "sourceDebugExtension: " + decoded);
        }
        return result;
    }

    private Attribute sourceFile(DirectClassFile cf, int offset, int length, ParseObserver observer) {
        if (length != 2) {
            StdAttributeFactory.throwBadLength(2);
        }
        ByteArray bytes = cf.getBytes();
        ConstantPool pool = cf.getConstantPool();
        int idx = bytes.getUnsignedShort(offset);
        CstString cst = (CstString)pool.get(idx);
        AttSourceFile result = new AttSourceFile(cst);
        if (observer != null) {
            observer.parsed(bytes, offset, 2, "source: " + cst);
        }
        return result;
    }

    private Attribute synthetic(DirectClassFile cf, int offset, int length, ParseObserver observer) {
        if (length != 0) {
            return StdAttributeFactory.throwBadLength(0);
        }
        return new AttSynthetic();
    }

    private static Attribute throwSeverelyTruncated() {
        throw new ParseException("severely truncated attribute");
    }

    private static Attribute throwTruncated() {
        throw new ParseException("truncated attribute");
    }

    private static Attribute throwBadLength(int expected) {
        throw new ParseException("bad attribute length; expected length " + Hex.u4(expected));
    }

    private BootstrapMethodsList parseBootstrapMethods(ByteArray bytes, ConstantPool constantPool, CstType declaringClass, int numMethods, int offset, int length, ParseObserver observer) throws ParseException {
        BootstrapMethodsList methods = new BootstrapMethodsList(numMethods);
        for (int methodIndex = 0; methodIndex < numMethods; ++methodIndex) {
            if (length < 4) {
                StdAttributeFactory.throwTruncated();
            }
            int methodRef = bytes.getUnsignedShort(offset);
            int numArguments = bytes.getUnsignedShort(offset + 2);
            if (observer != null) {
                observer.parsed(bytes, offset, 2, "bootstrap_method_ref: " + Hex.u2(methodRef));
                observer.parsed(bytes, offset + 2, 2, "num_bootstrap_arguments: " + Hex.u2(numArguments));
            }
            offset += 4;
            if ((length -= 4) < numArguments * 2) {
                StdAttributeFactory.throwTruncated();
            }
            BootstrapMethodArgumentsList arguments = new BootstrapMethodArgumentsList(numArguments);
            int argIndex = 0;
            while (argIndex < numArguments) {
                int argumentRef = bytes.getUnsignedShort(offset);
                if (observer != null) {
                    observer.parsed(bytes, offset, 2, "bootstrap_arguments[" + argIndex + "]" + Hex.u2(argumentRef));
                }
                arguments.set(argIndex, constantPool.get(argumentRef));
                ++argIndex;
                offset += 2;
                length -= 2;
            }
            arguments.setImmutable();
            Constant cstMethodRef = constantPool.get(methodRef);
            methods.set(methodIndex, declaringClass, (CstMethodHandle)cstMethodRef, arguments);
        }
        methods.setImmutable();
        if (length != 0) {
            StdAttributeFactory.throwBadLength(length);
        }
        return methods;
    }
}

