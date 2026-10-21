/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.command.grep;

import com.android.dex.ClassData;
import com.android.dex.ClassDef;
import com.android.dex.Dex;
import com.android.dex.EncodedValueReader;
import com.android.dex.MethodId;
import com.android.dx.io.CodeReader;
import com.android.dx.io.instructions.DecodedInstruction;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Pattern;

public final class Grep {
    private final Dex dex;
    private final CodeReader codeReader = new CodeReader();
    private final Set<Integer> stringIds;
    private final PrintWriter out;
    private int count = 0;
    private ClassDef currentClass;
    private ClassData.Method currentMethod;

    public Grep(Dex dex, Pattern pattern, PrintWriter out) {
        this.dex = dex;
        this.out = out;
        this.stringIds = this.getStringIds(dex, pattern);
        this.codeReader.setStringVisitor(new CodeReader.Visitor(){

            @Override
            public void visit(DecodedInstruction[] all, DecodedInstruction one) {
                Grep.this.encounterString(one.getIndex());
            }
        });
    }

    private void readArray(EncodedValueReader reader) {
        int size = reader.readArray();
        block4: for (int i = 0; i < size; ++i) {
            switch (reader.peek()) {
                case 23: {
                    this.encounterString(reader.readString());
                    continue block4;
                }
                case 28: {
                    this.readArray(reader);
                }
            }
        }
    }

    private void encounterString(int index) {
        if (this.stringIds.contains(index)) {
            this.out.println(this.location() + " " + this.dex.strings().get(index));
            ++this.count;
        }
    }

    private String location() {
        String className = this.dex.typeNames().get(this.currentClass.getTypeIndex());
        if (this.currentMethod != null) {
            MethodId methodId = this.dex.methodIds().get(this.currentMethod.getMethodIndex());
            return className + "." + this.dex.strings().get(methodId.getNameIndex());
        }
        return className;
    }

    public int grep() {
        Iterator<ClassDef> iterator = this.dex.classDefs().iterator();
        while (iterator.hasNext()) {
            ClassDef classDef;
            this.currentClass = classDef = iterator.next();
            this.currentMethod = null;
            if (classDef.getClassDataOffset() == 0) continue;
            ClassData classData = this.dex.readClassData(classDef);
            int staticValuesOffset = classDef.getStaticValuesOffset();
            if (staticValuesOffset != 0) {
                this.readArray(new EncodedValueReader(this.dex.open(staticValuesOffset)));
            }
            ClassData.Method[] methodArray = classData.allMethods();
            int n = methodArray.length;
            for (int i = 0; i < n; ++i) {
                ClassData.Method method;
                this.currentMethod = method = methodArray[i];
                if (method.getCodeOffset() == 0) continue;
                this.codeReader.visitAll(this.dex.readCode(method).getInstructions());
            }
        }
        this.currentClass = null;
        this.currentMethod = null;
        return this.count;
    }

    private Set<Integer> getStringIds(Dex dex, Pattern pattern) {
        HashSet<Integer> stringIds = new HashSet<Integer>();
        int stringIndex = 0;
        for (String s : dex.strings()) {
            if (pattern.matcher(s).find()) {
                stringIds.add(stringIndex);
            }
            ++stringIndex;
        }
        return stringIds;
    }
}

