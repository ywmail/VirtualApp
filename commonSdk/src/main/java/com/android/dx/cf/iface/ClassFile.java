/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.cf.iface;

import com.android.dx.cf.code.BootstrapMethodsList;
import com.android.dx.cf.iface.AttributeList;
import com.android.dx.cf.iface.FieldList;
import com.android.dx.cf.iface.HasAttribute;
import com.android.dx.cf.iface.MethodList;
import com.android.dx.rop.cst.ConstantPool;
import com.android.dx.rop.cst.CstString;
import com.android.dx.rop.cst.CstType;
import com.android.dx.rop.type.TypeList;

public interface ClassFile
extends HasAttribute {
    public int getMagic();

    public int getMinorVersion();

    public int getMajorVersion();

    public int getAccessFlags();

    public CstType getThisClass();

    public CstType getSuperclass();

    public ConstantPool getConstantPool();

    public TypeList getInterfaces();

    public FieldList getFields();

    public MethodList getMethods();

    @Override
    public AttributeList getAttributes();

    public BootstrapMethodsList getBootstrapMethods();

    public CstString getSourceFile();
}

