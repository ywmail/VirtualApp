/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.cf.code;

import com.android.dx.cf.attrib.AttCode;
import com.android.dx.cf.attrib.AttLineNumberTable;
import com.android.dx.cf.attrib.AttLocalVariableTable;
import com.android.dx.cf.attrib.AttLocalVariableTypeTable;
import com.android.dx.cf.code.ByteCatchList;
import com.android.dx.cf.code.BytecodeArray;
import com.android.dx.cf.code.LineNumberList;
import com.android.dx.cf.code.LocalVariableList;
import com.android.dx.cf.iface.AttributeList;
import com.android.dx.cf.iface.ClassFile;
import com.android.dx.cf.iface.Method;
import com.android.dx.rop.code.SourcePosition;
import com.android.dx.rop.cst.CstNat;
import com.android.dx.rop.cst.CstString;
import com.android.dx.rop.cst.CstType;
import com.android.dx.rop.type.Prototype;

public final class ConcreteMethod
implements Method {
    private final Method method;
    private final ClassFile classFile;
    private final AttCode attCode;
    private final LineNumberList lineNumbers;
    private final LocalVariableList localVariables;

    public ConcreteMethod(Method method, ClassFile classFile, boolean keepLines, boolean keepLocals) {
        this.method = method;
        this.classFile = classFile;
        AttributeList attribs = method.getAttributes();
        this.attCode = (AttCode)attribs.findFirst("Code");
        AttributeList codeAttribs = this.attCode.getAttributes();
        LineNumberList lnl = LineNumberList.EMPTY;
        if (keepLines) {
            AttLineNumberTable lnt = (AttLineNumberTable)codeAttribs.findFirst("LineNumberTable");
            while (lnt != null) {
                lnl = LineNumberList.concat(lnl, lnt.getLineNumbers());
                lnt = (AttLineNumberTable)codeAttribs.findNext(lnt);
            }
        }
        this.lineNumbers = lnl;
        LocalVariableList lvl = LocalVariableList.EMPTY;
        if (keepLocals) {
            AttLocalVariableTable lvt = (AttLocalVariableTable)codeAttribs.findFirst("LocalVariableTable");
            while (lvt != null) {
                lvl = LocalVariableList.concat(lvl, lvt.getLocalVariables());
                lvt = (AttLocalVariableTable)codeAttribs.findNext(lvt);
            }
            LocalVariableList typeList = LocalVariableList.EMPTY;
            AttLocalVariableTypeTable lvtt = (AttLocalVariableTypeTable)codeAttribs.findFirst("LocalVariableTypeTable");
            while (lvtt != null) {
                typeList = LocalVariableList.concat(typeList, lvtt.getLocalVariables());
                lvtt = (AttLocalVariableTypeTable)codeAttribs.findNext(lvtt);
            }
            if (typeList.size() != 0) {
                lvl = LocalVariableList.mergeDescriptorsAndSignatures(lvl, typeList);
            }
        }
        this.localVariables = lvl;
    }

    public CstString getSourceFile() {
        return this.classFile.getSourceFile();
    }

    public final boolean isDefaultOrStaticInterfaceMethod() {
        return (this.classFile.getAccessFlags() & 0x200) != 0 && !this.getNat().isClassInit();
    }

    public final boolean isStaticMethod() {
        return (this.getAccessFlags() & 8) != 0;
    }

    @Override
    public CstNat getNat() {
        return this.method.getNat();
    }

    @Override
    public CstString getName() {
        return this.method.getName();
    }

    @Override
    public CstString getDescriptor() {
        return this.method.getDescriptor();
    }

    @Override
    public int getAccessFlags() {
        return this.method.getAccessFlags();
    }

    @Override
    public AttributeList getAttributes() {
        return this.method.getAttributes();
    }

    @Override
    public CstType getDefiningClass() {
        return this.method.getDefiningClass();
    }

    @Override
    public Prototype getEffectiveDescriptor() {
        return this.method.getEffectiveDescriptor();
    }

    public int getMaxStack() {
        return this.attCode.getMaxStack();
    }

    public int getMaxLocals() {
        return this.attCode.getMaxLocals();
    }

    public BytecodeArray getCode() {
        return this.attCode.getCode();
    }

    public ByteCatchList getCatches() {
        return this.attCode.getCatches();
    }

    public LineNumberList getLineNumbers() {
        return this.lineNumbers;
    }

    public LocalVariableList getLocalVariables() {
        return this.localVariables;
    }

    public SourcePosition makeSourcePosistion(int offset) {
        return new SourcePosition(this.getSourceFile(), offset, this.lineNumbers.pcToLine(offset));
    }
}

