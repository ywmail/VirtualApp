/*
 * Decompiled with CFR 0.152.
 */
package com.android.multidex;

import com.android.dx.cf.direct.DirectClassFile;
import com.android.dx.cf.iface.FieldList;
import com.android.dx.cf.iface.MethodList;
import com.android.dx.rop.cst.Constant;
import com.android.dx.rop.cst.CstBaseMethodRef;
import com.android.dx.rop.cst.CstFieldRef;
import com.android.dx.rop.cst.CstType;
import com.android.dx.rop.type.Prototype;
import com.android.dx.rop.type.StdTypeList;
import com.android.dx.rop.type.TypeList;
import com.android.multidex.MainDexListBuilder;
import com.android.multidex.Path;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ClassReferenceListBuilder {
    private static final String CLASS_EXTENSION = ".class";
    private final Path path;
    private final Set<String> classNames = new HashSet<String>();

    public ClassReferenceListBuilder(Path path) {
        this.path = path;
    }

    @Deprecated
    public static void main(String[] args) {
        MainDexListBuilder.main(args);
    }

    public void addRoots(ZipFile jarOfRoots) throws IOException {
        String name;
        ZipEntry entry;
        Enumeration<? extends ZipEntry> entries = jarOfRoots.entries();
        while (entries.hasMoreElements()) {
            entry = entries.nextElement();
            name = entry.getName();
            if (!name.endsWith(CLASS_EXTENSION)) continue;
            this.classNames.add(name.substring(0, name.length() - CLASS_EXTENSION.length()));
        }
        entries = jarOfRoots.entries();
        while (entries.hasMoreElements()) {
            DirectClassFile classFile;
            entry = entries.nextElement();
            name = entry.getName();
            if (!name.endsWith(CLASS_EXTENSION)) continue;
            try {
                classFile = this.path.getClass(name);
            }
            catch (FileNotFoundException e) {
                throw new IOException("Class " + name + " is missing form original class path " + this.path, e);
            }
            this.addDependencies(classFile);
        }
    }

    Set<String> getClassNames() {
        return this.classNames;
    }

    private void addDependencies(DirectClassFile classFile) {
        for (Constant constant : classFile.getConstantPool().getEntries()) {
            if (constant instanceof CstType) {
                this.checkDescriptor(((CstType)constant).getClassType().getDescriptor());
                continue;
            }
            if (constant instanceof CstFieldRef) {
                this.checkDescriptor(((CstFieldRef)constant).getType().getDescriptor());
                continue;
            }
            if (!(constant instanceof CstBaseMethodRef)) continue;
            this.checkPrototype(((CstBaseMethodRef)constant).getPrototype());
        }
        FieldList fields = classFile.getFields();
        int nbField = fields.size();
        for (int i = 0; i < nbField; ++i) {
            this.checkDescriptor(fields.get(i).getDescriptor().getString());
        }
        MethodList methods = classFile.getMethods();
        int nbMethods = methods.size();
        for (int i = 0; i < nbMethods; ++i) {
            this.checkPrototype(Prototype.intern(methods.get(i).getDescriptor().getString()));
        }
    }

    private void checkPrototype(Prototype proto) {
        this.checkDescriptor(proto.getReturnType().getDescriptor());
        StdTypeList args = proto.getParameterTypes();
        for (int i = 0; i < args.size(); ++i) {
            this.checkDescriptor(args.get(i).getDescriptor());
        }
    }

    private void checkDescriptor(String typeDescriptor) {
        if (typeDescriptor.endsWith(";")) {
            int lastBrace = typeDescriptor.lastIndexOf(91);
            if (lastBrace < 0) {
                this.addClassWithHierachy(typeDescriptor.substring(1, typeDescriptor.length() - 1));
            } else {
                assert (typeDescriptor.length() > lastBrace + 3 && typeDescriptor.charAt(lastBrace + 1) == 'L');
                this.addClassWithHierachy(typeDescriptor.substring(lastBrace + 2, typeDescriptor.length() - 1));
            }
        }
    }

    private void addClassWithHierachy(String classBinaryName) {
        if (this.classNames.contains(classBinaryName)) {
            return;
        }
        try {
            DirectClassFile classFile = this.path.getClass(classBinaryName + CLASS_EXTENSION);
            this.classNames.add(classBinaryName);
            CstType superClass = classFile.getSuperclass();
            if (superClass != null) {
                this.addClassWithHierachy(superClass.getClassType().getClassName());
            }
            TypeList interfaceList = classFile.getInterfaces();
            int interfaceNumber = interfaceList.size();
            for (int i = 0; i < interfaceNumber; ++i) {
                this.addClassWithHierachy(interfaceList.getType(i).getClassName());
            }
        }
        catch (FileNotFoundException fileNotFoundException) {
            // empty catch block
        }
    }
}

