/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.command.findusages;

import com.android.dex.ClassData;
import com.android.dex.ClassDef;
import com.android.dex.Dex;
import com.android.dex.FieldId;
import com.android.dex.MethodId;
import com.android.dx.io.CodeReader;
import com.android.dx.io.OpcodeInfo;
import com.android.dx.io.instructions.DecodedInstruction;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public final class FindUsages {
    private final Dex dex;
    private final Set<Integer> methodIds;
    private final Set<Integer> fieldIds;
    private final CodeReader codeReader = new CodeReader();
    private final PrintWriter out;
    private ClassDef currentClass;
    private ClassData.Method currentMethod;

    public FindUsages(final Dex dex, String declaredBy, String memberName, final PrintWriter out) {
        this.dex = dex;
        this.out = out;
        HashSet<Integer> typeStringIndexes = new HashSet<Integer>();
        HashSet<Integer> memberNameIndexes = new HashSet<Integer>();
        Pattern declaredByPattern = Pattern.compile(declaredBy);
        Pattern memberNamePattern = Pattern.compile(memberName);
        List<String> strings = dex.strings();
        for (int i = 0; i < strings.size(); ++i) {
            String string = strings.get(i);
            if (declaredByPattern.matcher(string).matches()) {
                typeStringIndexes.add(i);
            }
            if (!memberNamePattern.matcher(string).matches()) continue;
            memberNameIndexes.add(i);
        }
        if (typeStringIndexes.isEmpty() || memberNameIndexes.isEmpty()) {
            this.fieldIds = null;
            this.methodIds = null;
            return;
        }
        this.methodIds = new HashSet<Integer>();
        this.fieldIds = new HashSet<Integer>();
        Iterator iterator = typeStringIndexes.iterator();
        while (iterator.hasNext()) {
            int typeStringIndex = (Integer)iterator.next();
            int typeIndex = Collections.binarySearch(dex.typeIds(), typeStringIndex);
            if (typeIndex < 0) continue;
            this.methodIds.addAll(this.getMethodIds(dex, memberNameIndexes, typeIndex));
            this.fieldIds.addAll(this.getFieldIds(dex, memberNameIndexes, typeIndex));
        }
        this.codeReader.setFieldVisitor(new CodeReader.Visitor(){

            @Override
            public void visit(DecodedInstruction[] all, DecodedInstruction one) {
                int fieldId = one.getIndex();
                if (FindUsages.this.fieldIds.contains(fieldId)) {
                    out.println(FindUsages.this.location() + ": field reference " + dex.fieldIds().get(fieldId) + " (" + OpcodeInfo.getName(one.getOpcode()) + ")");
                }
            }
        });
        this.codeReader.setMethodVisitor(new CodeReader.Visitor(){

            @Override
            public void visit(DecodedInstruction[] all, DecodedInstruction one) {
                int methodId = one.getIndex();
                if (FindUsages.this.methodIds.contains(methodId)) {
                    out.println(FindUsages.this.location() + ": method reference " + dex.methodIds().get(methodId) + " (" + OpcodeInfo.getName(one.getOpcode()) + ")");
                }
            }
        });
    }

    private String location() {
        String className = this.dex.typeNames().get(this.currentClass.getTypeIndex());
        if (this.currentMethod != null) {
            MethodId methodId = this.dex.methodIds().get(this.currentMethod.getMethodIndex());
            return className + "." + this.dex.strings().get(methodId.getNameIndex());
        }
        return className;
    }

    public void findUsages() {
        if (this.fieldIds == null || this.methodIds == null) {
            return;
        }
        Iterator<ClassDef> iterator = this.dex.classDefs().iterator();
        while (iterator.hasNext()) {
            ClassDef classDef;
            this.currentClass = classDef = iterator.next();
            this.currentMethod = null;
            if (classDef.getClassDataOffset() == 0) continue;
            ClassData classData = this.dex.readClassData(classDef);
            for (ClassData.Field field : classData.allFields()) {
                int fieldIndex = field.getFieldIndex();
                if (!this.fieldIds.contains(fieldIndex)) continue;
                this.out.println(this.location() + " field declared " + this.dex.fieldIds().get(fieldIndex));
            }
            Object[] objectArray = classData.allMethods();
            int n = objectArray.length;
            for (int i = 0; i < n; ++i) {
                Object method;
                this.currentMethod = (ClassData.Method) (method = objectArray[i]);
                int methodIndex = ((ClassData.Method)method).getMethodIndex();
                if (this.methodIds.contains(methodIndex)) {
                    this.out.println(this.location() + " method declared " + this.dex.methodIds().get(methodIndex));
                }
                if (((ClassData.Method)method).getCodeOffset() == 0) continue;
                this.codeReader.visitAll(this.dex.readCode((ClassData.Method)method).getInstructions());
            }
        }
        this.currentClass = null;
        this.currentMethod = null;
    }

    private Set<Integer> getFieldIds(Dex dex, Set<Integer> memberNameIndexes, int declaringType) {
        HashSet<Integer> fields = new HashSet<Integer>();
        int fieldIndex = 0;
        for (FieldId fieldId : dex.fieldIds()) {
            if (memberNameIndexes.contains(fieldId.getNameIndex()) && declaringType == fieldId.getDeclaringClassIndex()) {
                fields.add(fieldIndex);
            }
            ++fieldIndex;
        }
        return fields;
    }

    private Set<Integer> getMethodIds(Dex dex, Set<Integer> memberNameIndexes, int declaringType) {
        Set<Integer> subtypes = this.findAssignableTypes(dex, declaringType);
        HashSet<Integer> methods = new HashSet<Integer>();
        int methodIndex = 0;
        for (MethodId method : dex.methodIds()) {
            if (memberNameIndexes.contains(method.getNameIndex()) && subtypes.contains(method.getDeclaringClassIndex())) {
                methods.add(methodIndex);
            }
            ++methodIndex;
        }
        return methods;
    }

    private Set<Integer> findAssignableTypes(Dex dex, int typeIndex) {
        HashSet<Integer> assignableTypes = new HashSet<Integer>();
        assignableTypes.add(typeIndex);
        block0: for (ClassDef classDef : dex.classDefs()) {
            if (assignableTypes.contains(classDef.getSupertypeIndex())) {
                assignableTypes.add(classDef.getTypeIndex());
                continue;
            }
            for (short implemented : classDef.getInterfaces()) {
                if (!assignableTypes.contains(implemented)) continue;
                assignableTypes.add(classDef.getTypeIndex());
                continue block0;
            }
        }
        return assignableTypes;
    }
}

