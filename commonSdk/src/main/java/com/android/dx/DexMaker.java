/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx;

import com.android.dx.AppDataDirGuesser;
import com.android.dx.Code;
import com.android.dx.Constants;
import com.android.dx.FieldId;
import com.android.dx.MethodId;
import com.android.dx.TypeId;
import com.android.dx.TypeList;
import com.android.dx.dex.DexOptions;
import com.android.dx.dex.code.DalvCode;
import com.android.dx.dex.code.RopTranslator;
import com.android.dx.dex.file.ClassDefItem;
import com.android.dx.dex.file.DexFile;
import com.android.dx.dex.file.EncodedField;
import com.android.dx.dex.file.EncodedMember;
import com.android.dx.dex.file.EncodedMethod;
import com.android.dx.rop.code.LocalVariableInfo;
import com.android.dx.rop.code.RopMethod;
import com.android.dx.rop.cst.CstString;
import com.android.dx.rop.cst.CstType;
import com.android.dx.rop.type.StdTypeList;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;

public final class DexMaker {
    private final Map<TypeId<?>, TypeDeclaration> types = new LinkedHashMap();
    private static boolean didWarnBlacklistedMethods;
    private static boolean didWarnNonBaseDexClassLoader;
    private ClassLoader sharedClassLoader;
    private DexFile outputDex;
    private boolean markAsTrusted;

    TypeDeclaration getTypeDeclaration(TypeId<?> type) {
        TypeDeclaration result = this.types.get(type);
        if (result == null) {
            result = new TypeDeclaration(type);
            this.types.put(type, result);
        }
        return result;
    }

    public void declare(TypeId<?> type, String sourceFile, int flags, TypeId<?> supertype, TypeId<?> ... interfaces) {
        TypeDeclaration declaration = this.getTypeDeclaration(type);
        int supportedFlags = 5137;
        if ((flags & ~supportedFlags) != 0) {
            throw new IllegalArgumentException("Unexpected flag: " + Integer.toHexString(flags));
        }
        if (declaration.declared) {
            throw new IllegalStateException("already declared: " + type);
        }
        declaration.declared = true;
        declaration.flags = flags;
        declaration.supertype = supertype;
        declaration.sourceFile = sourceFile;
        declaration.interfaces = new TypeList(interfaces);
    }

    public Code declare(MethodId<?, ?> method, int flags) {
        TypeDeclaration typeDeclaration = this.getTypeDeclaration(method.declaringType);
        if (typeDeclaration.methods.containsKey(method)) {
            throw new IllegalStateException("already declared: " + method);
        }
        int supportedFlags = 4223;
        if ((flags & ~supportedFlags) != 0) {
            throw new IllegalArgumentException("Unexpected flag: " + Integer.toHexString(flags));
        }
        if ((flags & 0x20) != 0) {
            flags = flags & 0xFFFFFFDF | 0x20000;
        }
        if (method.isConstructor() || method.isStaticInitializer()) {
            flags |= 0x10000;
        }
        MethodDeclaration methodDeclaration = new MethodDeclaration(method, flags);
        typeDeclaration.methods.put(method, methodDeclaration);
        return methodDeclaration.code;
    }

    public void declare(FieldId<?, ?> fieldId, int flags, Object staticValue) {
        TypeDeclaration typeDeclaration = this.getTypeDeclaration(fieldId.declaringType);
        if (typeDeclaration.fields.containsKey(fieldId)) {
            throw new IllegalStateException("already declared: " + fieldId);
        }
        int supportedFlags = 4319;
        if ((flags & ~supportedFlags) != 0) {
            throw new IllegalArgumentException("Unexpected flag: " + Integer.toHexString(flags));
        }
        if ((flags & 8) == 0 && staticValue != null) {
            throw new IllegalArgumentException("staticValue is non-null, but field is not static");
        }
        FieldDeclaration fieldDeclaration = new FieldDeclaration(fieldId, flags, staticValue);
        typeDeclaration.fields.put(fieldId, fieldDeclaration);
    }

    public byte[] generate() {
        if (this.outputDex == null) {
            DexOptions options = new DexOptions();
            options.minSdkVersion = 26;
            this.outputDex = new DexFile(options);
        }
        for (TypeDeclaration typeDeclaration : this.types.values()) {
            this.outputDex.add(typeDeclaration.toClassDefItem());
        }
        try {
            return this.outputDex.toDex(null, false);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String generateFileName() {
        int checksum = 1;
        Set<TypeId<?>> typesKeySet = this.types.keySet();
        Iterator<TypeId<?>> it = typesKeySet.iterator();
        int[] checksums = new int[typesKeySet.size()];
        int i = 0;
        while (it.hasNext()) {
            TypeId<?> typeId = it.next();
            TypeDeclaration decl = this.getTypeDeclaration(typeId);
            Set methodSet = decl.methods.keySet();
            if (decl.supertype == null) continue;
            int sum = 31 * decl.supertype.hashCode() + decl.interfaces.hashCode();
            checksums[i++] = 31 * sum + methodSet.hashCode();
        }
        Arrays.sort(checksums);
        for (int sum : checksums) {
            checksum *= 31;
            checksum += sum;
        }
        return "Generated_" + checksum + ".jar";
    }

    public void setSharedClassLoader(ClassLoader classLoader) {
        this.sharedClassLoader = classLoader;
    }

    public void markAsTrusted() {
        this.markAsTrusted = true;
    }

    /*
     * Exception decompiling
     */
    private ClassLoader generateClassLoader(File result, File dexCache, ClassLoader parent) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Started 3 blocks at once
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.getStartingBlocks(Op04StructuredStatement.java:412)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:487)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:850)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    public ClassLoader loadClassDirect(ClassLoader parent, File dexCache, String dexFileName) {
        File result = new File(dexCache, dexFileName);
        if (result.exists()) {
            return this.generateClassLoader(result, dexCache, parent);
        }
        return null;
    }

    public ClassLoader generateAndLoad(ClassLoader parent, File dexCache) throws IOException {
        return this.generateAndLoad(parent, dexCache, this.generateFileName());
    }

    public ClassLoader generateAndLoad(ClassLoader parent, File dexCache, String dexFileName) throws IOException {
        File parentDir;
        File result;
        if (dexCache == null) {
            String property = System.getProperty("dexmaker.dexcache");
            if (property != null) {
                dexCache = new File(property);
            } else {
                dexCache = new AppDataDirGuesser().guess();
                if (dexCache == null) {
                    throw new IllegalArgumentException("dexcache == null (and no default could be found; consider setting the 'dexmaker.dexcache' system property)");
                }
            }
        }
        if ((result = new File(dexCache, dexFileName)).exists()) {
            try {
                this.deleteOldDex(result);
            }
            catch (Throwable throwable) {
                // empty catch block
            }
        }
        if (!(parentDir = result.getParentFile()).exists()) {
            parentDir.mkdirs();
        }
        result.createNewFile();
        JarOutputStream jarOut = new JarOutputStream(new FileOutputStream(result));
        JarEntry entry = new JarEntry("classes.dex");
        byte[] dex = this.generate();
        entry.setSize(dex.length);
        jarOut.putNextEntry(entry);
        jarOut.write(dex);
        jarOut.closeEntry();
        jarOut.close();
        return this.generateClassLoader(result, dexCache, parent);
    }

    public void deleteOldDex(File dexFile) {
        dexFile.delete();
        String dexDir = dexFile.getParent();
        File oatDir = new File(dexDir, "/oat/");
        File oatDirArm = new File(oatDir, "/arm/");
        File oatDirArm64 = new File(oatDir, "/arm64/");
        if (!oatDir.exists()) {
            return;
        }
        String nameStart = dexFile.getName().replaceAll(".jar", "");
        this.doDeleteOatFiles(oatDir, nameStart);
        this.doDeleteOatFiles(oatDirArm, nameStart);
        this.doDeleteOatFiles(oatDirArm64, nameStart);
    }

    private void doDeleteOatFiles(File dir, String nameStart) {
        if (!dir.exists()) {
            return;
        }
        File[] oats = dir.listFiles();
        if (oats == null) {
            return;
        }
        for (File oatFile : oats) {
            if (!oatFile.isFile() || !oatFile.getName().startsWith(nameStart)) continue;
            oatFile.delete();
        }
    }

    DexFile getDexFile() {
        if (this.outputDex == null) {
            DexOptions options = new DexOptions();
            options.minSdkVersion = 13;
            this.outputDex = new DexFile(options);
        }
        return this.outputDex;
    }

    static class MethodDeclaration {
        final MethodId<?, ?> method;
        private final int flags;
        private final Code code;

        public MethodDeclaration(MethodId<?, ?> method, int flags) {
            this.method = method;
            this.flags = flags;
            this.code = new Code(this);
        }

        boolean isStatic() {
            return (this.flags & 8) != 0;
        }

        boolean isDirect() {
            return (this.flags & 0x1000A) != 0;
        }

        EncodedMethod toEncodedMethod(DexOptions dexOptions) {
            RopMethod ropMethod = new RopMethod(this.code.toBasicBlocks(), 0);
            LocalVariableInfo locals = null;
            DalvCode dalvCode = RopTranslator.translate(ropMethod, 1, locals, this.code.paramSize(), dexOptions);
            return new EncodedMethod(this.method.constant, this.flags, dalvCode, StdTypeList.EMPTY);
        }
    }

    static class FieldDeclaration {
        final FieldId<?, ?> fieldId;
        private final int accessFlags;
        private final Object staticValue;

        FieldDeclaration(FieldId<?, ?> fieldId, int accessFlags, Object staticValue) {
            if ((accessFlags & 8) == 0 && staticValue != null) {
                throw new IllegalArgumentException("instance fields may not have a value");
            }
            this.fieldId = fieldId;
            this.accessFlags = accessFlags;
            this.staticValue = staticValue;
        }

        EncodedField toEncodedField() {
            return new EncodedField(this.fieldId.constant, this.accessFlags);
        }

        public boolean isStatic() {
            return (this.accessFlags & 8) != 0;
        }
    }

    static class TypeDeclaration {
        private final TypeId<?> type;
        private boolean declared;
        private int flags;
        private TypeId<?> supertype;
        private String sourceFile;
        private TypeList interfaces;
        private ClassDefItem classDefItem;
        private final Map<FieldId, FieldDeclaration> fields = new LinkedHashMap<FieldId, FieldDeclaration>();
        private final Map<MethodId, MethodDeclaration> methods = new LinkedHashMap<MethodId, MethodDeclaration>();

        TypeDeclaration(TypeId<?> type) {
            this.type = type;
        }

        ClassDefItem toClassDefItem() {
            if (!this.declared) {
                throw new IllegalStateException("Undeclared type " + this.type + " declares members: " + this.fields.keySet() + " " + this.methods.keySet());
            }
            DexOptions dexOptions = new DexOptions();
            dexOptions.minSdkVersion = 13;
            CstType thisType = this.type.constant;
            if (this.classDefItem == null) {
                EncodedMember encoded;
                this.classDefItem = new ClassDefItem(thisType, this.flags, this.supertype.constant, this.interfaces.ropTypes, new CstString(this.sourceFile));
                for (MethodDeclaration method : this.methods.values()) {
                    encoded = method.toEncodedMethod(dexOptions);
                    if (method.isDirect()) {
                        this.classDefItem.addDirectMethod((EncodedMethod)encoded);
                        continue;
                    }
                    this.classDefItem.addVirtualMethod((EncodedMethod)encoded);
                }
                for (FieldDeclaration field : this.fields.values()) {
                    encoded = field.toEncodedField();
                    if (field.isStatic()) {
                        this.classDefItem.addStaticField((EncodedField)encoded, Constants.getConstant(field.staticValue));
                        continue;
                    }
                    this.classDefItem.addInstanceField((EncodedField)encoded);
                }
            }
            return this.classDefItem;
        }
    }
}

