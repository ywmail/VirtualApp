/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.command.dexer;

import com.android.dex.Dex;
import com.android.dex.DexException;
import com.android.dex.util.FileUtils;
import com.android.dx.cf.code.SimException;
import com.android.dx.cf.direct.ClassPathOpener;
import com.android.dx.cf.direct.DirectClassFile;
import com.android.dx.cf.direct.StdAttributeFactory;
import com.android.dx.cf.iface.ParseException;
import com.android.dx.command.UsageException;
import com.android.dx.command.dexer.DxContext;
import com.android.dx.dex.DexOptions;
import com.android.dx.dex.cf.CfOptions;
import com.android.dx.dex.cf.CfTranslator;
import com.android.dx.dex.file.ClassDefItem;
import com.android.dx.dex.file.DexFile;
import com.android.dx.dex.file.EncodedMethod;
import com.android.dx.merge.CollisionPolicy;
import com.android.dx.merge.DexMerger;
import com.android.dx.rop.annotation.Annotation;
import com.android.dx.rop.annotation.Annotations;
import com.android.dx.rop.annotation.AnnotationsList;
import com.android.dx.rop.code.RegisterSpec;
import com.android.dx.rop.cst.CstNat;
import com.android.dx.rop.cst.CstString;
import com.android.dx.rop.cst.CstType;
import com.android.dx.rop.type.Prototype;
import com.android.dx.rop.type.Type;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

public class Main {
    private static final String DEX_EXTENSION = ".dex";
    private static final String DEX_PREFIX = "classes";
    private static final String IN_RE_CORE_CLASSES = "Ill-advised or mistaken usage of a core class (java.* or javax.*)\nwhen not building a core library.\n\nThis is often due to inadvertently including a core library file\nin your application's project, when using an IDE (such as\nEclipse). If you are sure you're not intentionally defining a\ncore class, then this is the most likely explanation of what's\ngoing on.\n\nHowever, you might actually be trying to define a class in a core\nnamespace, the source of which you may have taken, for example,\nfrom a non-Android virtual machine project. This will most\nassuredly not work. At a minimum, it jeopardizes the\ncompatibility of your app with future versions of the platform.\nIt is also often of questionable legality.\n\nIf you really intend to build a core library -- which is only\nappropriate as part of creating a full virtual machine\ndistribution, as opposed to compiling an application -- then use\nthe \"--core-library\" option to suppress this error message.\n\nIf you go ahead and use \"--core-library\" but are in fact\nbuilding an application, then be forewarned that your application\nwill still fail to build or run, at some point. Please be\nprepared for angry customers who find, for example, that your\napplication ceases to function once they upgrade their operating\nsystem. You will be to blame for this problem.\n\nIf you are legitimately using some code that happens to be in a\ncore package, then the easiest safe alternative you have is to\nrepackage that code. That is, move the classes in question into\nyour own package namespace. This means that they will never be in\nconflict with core system classes. JarJar is a tool that may help\nyou in this endeavor. If you find that you cannot do this, then\nthat is an indication that the path you are on will ultimately\nlead to pain, suffering, grief, and lamentation.\n";
    private static final String MANIFEST_NAME = "META-INF/MANIFEST.MF";
    private static final Attributes.Name CREATED_BY = new Attributes.Name("Created-By");
    private static final String[] JAVAX_CORE = new String[]{"accessibility", "crypto", "imageio", "management", "naming", "net", "print", "rmi", "security", "sip", "sound", "sql", "swing", "transaction", "xml"};
    private static final int MAX_METHOD_ADDED_DURING_DEX_CREATION = 2;
    private static final int MAX_FIELD_ADDED_DURING_DEX_CREATION = 9;
    private AtomicInteger errors = new AtomicInteger(0);
    private Arguments args;
    private DexFile outputDex;
    private TreeMap<String, byte[]> outputResources;
    private final List<byte[]> libraryDexBuffers = new ArrayList<byte[]>();
    private ExecutorService classTranslatorPool;
    private ExecutorService classDefItemConsumer;
    private List<Future<Boolean>> addToDexFutures = new ArrayList<Future<Boolean>>();
    private ExecutorService dexOutPool;
    private List<Future<byte[]>> dexOutputFutures = new ArrayList<Future<byte[]>>();
    private Object dexRotationLock = new Object();
    private int maxMethodIdsInProcess = 0;
    private int maxFieldIdsInProcess = 0;
    private volatile boolean anyFilesProcessed;
    private long minimumFileAge = 0L;
    private Set<String> classesInMainDex = null;
    private List<byte[]> dexOutputArrays = new ArrayList<byte[]>();
    private OutputStreamWriter humanOutWriter = null;
    private final DxContext context;

    public Main(DxContext context) {
        this.context = context;
    }

    public static void main(String[] argArray) throws IOException {
        DxContext context = new DxContext();
        Arguments arguments = new Arguments(context);
        arguments.parse(argArray);
        int result = new Main(context).runDx(arguments);
        if (result != 0) {
            System.exit(result);
        }
    }

    public static void clearInternTables() {
        Prototype.clearInternTable();
        RegisterSpec.clearInternTable();
        CstType.clearInternTable();
        Type.clearInternTable();
    }

    public static int run(Arguments arguments) throws IOException {
        return new Main(new DxContext()).runDx(arguments);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public int runDx(Arguments arguments) throws IOException {
        this.errors.set(0);
        this.libraryDexBuffers.clear();
        this.args = arguments;
        this.args.makeOptionsObjects();
        OutputStream humanOutRaw = null;
        if (this.args.humanOutName != null) {
            humanOutRaw = this.openOutput(this.args.humanOutName);
            this.humanOutWriter = new OutputStreamWriter(humanOutRaw);
        }
        try {
            if (this.args.multiDex) {
                int n = this.runMultiDex();
                return n;
            }
            int n = this.runMonoDex();
            return n;
        }
        finally {
            this.closeOutput(humanOutRaw);
        }
    }

    private int runMonoDex() throws IOException {
        File incrementalOutFile = null;
        if (this.args.incremental) {
            if (this.args.outName == null) {
                this.context.err.println("error: no incremental output name specified");
                return -1;
            }
            incrementalOutFile = new File(this.args.outName);
            if (incrementalOutFile.exists()) {
                this.minimumFileAge = incrementalOutFile.lastModified();
            }
        }
        if (!this.processAllFiles()) {
            return 1;
        }
        if (this.args.incremental && !this.anyFilesProcessed) {
            return 0;
        }
        byte[] outArray = null;
        if (!(this.outputDex.isEmpty() && this.args.humanOutName == null || (outArray = this.writeDex(this.outputDex)) != null)) {
            return 2;
        }
        if (this.args.incremental) {
            outArray = this.mergeIncremental(outArray, incrementalOutFile);
        }
        outArray = this.mergeLibraryDexBuffers(outArray);
        if (this.args.jarOutput) {
            this.outputDex = null;
            if (outArray != null) {
                this.outputResources.put("classes.dex", outArray);
            }
            if (!this.createJar(this.args.outName)) {
                return 3;
            }
        } else if (outArray != null && this.args.outName != null) {
            OutputStream out = this.openOutput(this.args.outName);
            out.write(outArray);
            this.closeOutput(out);
        }
        return 0;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private int runMultiDex() throws IOException {
        assert (!this.args.incremental);
        if (this.args.mainDexListFile != null) {
            this.classesInMainDex = new HashSet<String>();
            Main.readPathsFromFile(this.args.mainDexListFile, this.classesInMainDex);
        }
        this.dexOutPool = Executors.newFixedThreadPool(this.args.numThreads);
        if (!this.processAllFiles()) {
            return 1;
        }
        if (!this.libraryDexBuffers.isEmpty()) {
            throw new DexException("Library dex files are not supported in multi-dex mode");
        }
        if (this.outputDex != null) {
            this.dexOutputFutures.add(this.dexOutPool.submit(new DexWriter(this.outputDex)));
            this.outputDex = null;
        }
        try {
            this.dexOutPool.shutdown();
            if (!this.dexOutPool.awaitTermination(600L, TimeUnit.SECONDS)) {
                throw new RuntimeException("Timed out waiting for dex writer threads.");
            }
            for (Future<byte[]> f : this.dexOutputFutures) {
                this.dexOutputArrays.add(f.get());
            }
        }
        catch (InterruptedException ex) {
            this.dexOutPool.shutdownNow();
            throw new RuntimeException("A dex writer thread has been interrupted.");
        }
        catch (Exception e) {
            this.dexOutPool.shutdownNow();
            throw new RuntimeException("Unexpected exception in dex writer thread");
        }
        if (this.args.jarOutput) {
            for (int i = 0; i < this.dexOutputArrays.size(); ++i) {
                this.outputResources.put(Main.getDexFileName(i), this.dexOutputArrays.get(i));
            }
            if (!this.createJar(this.args.outName)) {
                return 3;
            }
        } else if (this.args.outName != null) {
            File outDir = new File(this.args.outName);
            assert (outDir.isDirectory());
            for (int i = 0; i < this.dexOutputArrays.size(); ++i) {
                FileOutputStream out = new FileOutputStream(new File(outDir, Main.getDexFileName(i)));
                try {
                    ((OutputStream)out).write(this.dexOutputArrays.get(i));
                    continue;
                }
                finally {
                    this.closeOutput(out);
                }
            }
        }
        return 0;
    }

    private static String getDexFileName(int i) {
        if (i == 0) {
            return "classes.dex";
        }
        return DEX_PREFIX + (i + 1) + DEX_EXTENSION;
    }

    private static void readPathsFromFile(String fileName, Collection<String> paths) throws IOException {
        BufferedReader bfr = null;

        try {
            FileReader fr = new FileReader(fileName);
            bfr = new BufferedReader(fr);

            String line;
            while(null != (line = bfr.readLine())) {
                paths.add(fixPath(line));
            }
        } finally {
            if (bfr != null) {
                bfr.close();
            }

        }

    }

    private byte[] mergeIncremental(byte[] update, File base) throws IOException {
        Dex dexA = null;
        Dex dexB = null;
        if (update != null) {
            dexA = new Dex(update);
        }
        if (base.exists()) {
            dexB = new Dex(base);
        }
        if (dexA == null && dexB == null) {
            return null;
        }
        Dex result = dexA == null ? dexB : (dexB == null ? dexA : new DexMerger(new Dex[]{dexA, dexB}, CollisionPolicy.KEEP_FIRST, this.context).merge());
        ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
        result.writeTo(bytesOut);
        return bytesOut.toByteArray();
    }

    private byte[] mergeLibraryDexBuffers(byte[] outArray) throws IOException {
        ArrayList<Dex> dexes = new ArrayList<Dex>();
        if (outArray != null) {
            dexes.add(new Dex(outArray));
        }
        for (byte[] libraryDex : this.libraryDexBuffers) {
            dexes.add(new Dex(libraryDex));
        }
        if (dexes.isEmpty()) {
            return null;
        }
        Dex merged = new DexMerger(dexes.toArray(new Dex[dexes.size()]), CollisionPolicy.FAIL, this.context).merge();
        return merged.getBytes();
    }

    /*
     * Exception decompiling
     */
    private boolean processAllFiles() {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         *
         * org.benf.cfr.reader.util.ConfusedCFRException: Started 2 blocks at once
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

    private void createDexFile() {
        this.outputDex = new DexFile(this.args.dexOptions);
        if (this.args.dumpWidth != 0) {
            this.outputDex.setDumpWidth(this.args.dumpWidth);
        }
    }

    private void rotateDexFile() {
        if (this.outputDex != null) {
            if (this.dexOutPool != null) {
                this.dexOutputFutures.add(this.dexOutPool.submit(new DexWriter(this.outputDex)));
            } else {
                this.dexOutputArrays.add(this.writeDex(this.outputDex));
            }
        }
        this.createDexFile();
    }

    private void processOne(String pathname, ClassPathOpener.FileNameFilter filter) {
        ClassPathOpener opener = new ClassPathOpener(pathname, true, filter, new FileBytesConsumer());
        if (opener.process()) {
            this.updateStatus(true);
        }
    }

    private void updateStatus(boolean res) {
        this.anyFilesProcessed |= res;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private boolean processFileBytes(String name, long lastModified, byte[] bytes) {
        boolean keepResources;
        boolean isClass = name.endsWith(".class");
        boolean isClassesDex = name.equals("classes.dex");
        boolean bl = keepResources = this.outputResources != null;
        if (!(isClass || isClassesDex || keepResources)) {
            if (this.args.verbose) {
                this.context.out.println("ignored resource " + name);
            }
            return false;
        }
        if (this.args.verbose) {
            this.context.out.println("processing " + name + "...");
        }
        String fixedName = Main.fixPath(name);
        if (isClass) {
            if (keepResources && this.args.keepClassesInJar) {
                TreeMap<String, byte[]> treeMap = this.outputResources;
                synchronized (treeMap) {
                    this.outputResources.put(fixedName, bytes);
                }
            }
            if (lastModified < this.minimumFileAge) {
                return true;
            }
            this.processClass(fixedName, bytes);
            return false;
        }
        if (isClassesDex) {
            List<byte[]> list = this.libraryDexBuffers;
            synchronized (list) {
                this.libraryDexBuffers.add(bytes);
            }
            return true;
        }
        TreeMap<String, byte[]> treeMap = this.outputResources;
        synchronized (treeMap) {
            this.outputResources.put(fixedName, bytes);
        }
        return true;
    }

    private boolean processClass(String name, byte[] bytes) {
        if (!this.args.coreLibrary) {
            this.checkClassName(name);
        }
        try {
            new DirectClassFileConsumer(name, bytes, null).call(new ClassParserTask(name, bytes).call());
        }
        catch (ParseException ex) {
            throw ex;
        }
        catch (Exception ex) {
            throw new RuntimeException("Exception parsing classes", ex);
        }
        return true;
    }

    private DirectClassFile parseClass(String name, byte[] bytes) {
        DirectClassFile cf = new DirectClassFile(bytes, name, this.args.cfOptions.strictNameCheck);
        cf.setAttributeFactory(StdAttributeFactory.THE_ONE);
        cf.getMagic();
        return cf;
    }

    private ClassDefItem translateClass(byte[] bytes, DirectClassFile cf) {
        try {
            return CfTranslator.translate(this.context, cf, bytes, this.args.cfOptions, this.args.dexOptions, this.outputDex);
        }
        catch (ParseException ex) {
            this.context.err.println("\ntrouble processing:");
            if (this.args.debug) {
                ex.printStackTrace(this.context.err);
            } else {
                ex.printContext(this.context.err);
            }
            this.errors.incrementAndGet();
            return null;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private boolean addClassToDex(ClassDefItem clazz) {
        DexFile dexFile = this.outputDex;
        synchronized (dexFile) {
            this.outputDex.add(clazz);
        }
        return true;
    }

    private void checkClassName(String name) {
        boolean bogus = false;
        if (name.startsWith("java/")) {
            bogus = true;
        } else if (name.startsWith("javax/")) {
            int slashAt = name.indexOf(47, 6);
            if (slashAt == -1) {
                bogus = true;
            } else {
                String pkg = name.substring(6, slashAt);
                boolean bl = bogus = Arrays.binarySearch(JAVAX_CORE, pkg) >= 0;
            }
        }
        if (!bogus) {
            return;
        }
        this.context.err.println("\ntrouble processing \"" + name + "\":\n\n" + IN_RE_CORE_CLASSES);
        this.errors.incrementAndGet();
        throw new StopProcessing();
    }

    private byte[] writeDex(DexFile outputDex) {
        byte[] outArray = null;
        try {
            try {
                if (this.args.methodToDump != null) {
                    outputDex.toDex(null, false);
                    this.dumpMethod(outputDex, this.args.methodToDump, this.humanOutWriter);
                } else {
                    outArray = outputDex.toDex(this.humanOutWriter, this.args.verboseDump);
                }
                if (this.args.statistics) {
                    this.context.out.println(outputDex.getStatistics().toHuman());
                }
            }
            finally {
                if (this.humanOutWriter != null) {
                    this.humanOutWriter.flush();
                }
            }
        }
        catch (Exception ex) {
            if (this.args.debug) {
                this.context.err.println("\ntrouble writing output:");
                ex.printStackTrace(this.context.err);
            } else {
                this.context.err.println("\ntrouble writing output: " + ex.getMessage());
            }
            return null;
        }
        return outArray;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private boolean createJar(String fileName) {
        try {
            Manifest manifest = this.makeManifest();
            OutputStream out = this.openOutput(fileName);
            JarOutputStream jarOut = new JarOutputStream(out, manifest);
            try {
                for (Map.Entry<String, byte[]> e : this.outputResources.entrySet()) {
                    String name = e.getKey();
                    byte[] contents = e.getValue();
                    JarEntry entry = new JarEntry(name);
                    int length = contents.length;
                    if (this.args.verbose) {
                        this.context.out.println("writing " + name + "; size " + length + "...");
                    }
                    entry.setSize(length);
                    jarOut.putNextEntry(entry);
                    jarOut.write(contents);
                    jarOut.closeEntry();
                }
            }
            finally {
                jarOut.finish();
                jarOut.flush();
                this.closeOutput(out);
            }
        }
        catch (Exception ex) {
            if (this.args.debug) {
                this.context.err.println("\ntrouble writing output:");
                ex.printStackTrace(this.context.err);
            } else {
                this.context.err.println("\ntrouble writing output: " + ex.getMessage());
            }
            return false;
        }
        return true;
    }

    private Manifest makeManifest() throws IOException {
        Attributes attribs;
        Manifest manifest;
        byte[] manifestBytes = this.outputResources.get(MANIFEST_NAME);
        if (manifestBytes == null) {
            manifest = new Manifest();
            attribs = manifest.getMainAttributes();
            attribs.put(Attributes.Name.MANIFEST_VERSION, "1.0");
        } else {
            manifest = new Manifest(new ByteArrayInputStream(manifestBytes));
            attribs = manifest.getMainAttributes();
            this.outputResources.remove(MANIFEST_NAME);
        }
        String createdBy = attribs.getValue(CREATED_BY);
        createdBy = createdBy == null ? "" : createdBy + " + ";
        createdBy = createdBy + "dx 1.16";
        attribs.put(CREATED_BY, createdBy);
        attribs.putValue("Dex-Location", "classes.dex");
        return manifest;
    }

    private OutputStream openOutput(String name) throws IOException {
        if (name.equals("-") || name.startsWith("-.")) {
            return this.context.out;
        }
        return new FileOutputStream(name);
    }

    private void closeOutput(OutputStream stream) throws IOException {
        if (stream == null) {
            return;
        }
        stream.flush();
        if (stream != this.context.out) {
            stream.close();
        }
    }

    private static String fixPath(String path) {
        int index;
        if (File.separatorChar == '\\') {
            path = path.replace('\\', '/');
        }
        if ((index = path.lastIndexOf("/./")) != -1) {
            return path.substring(index + 3);
        }
        if (path.startsWith("./")) {
            return path.substring(2);
        }
        return path;
    }

    private void dumpMethod(DexFile dex, String fqName, OutputStreamWriter out) {
        boolean wildcard = fqName.endsWith("*");
        int lastDot = fqName.lastIndexOf(46);
        if (lastDot <= 0 || lastDot == fqName.length() - 1) {
            this.context.err.println("bogus fully-qualified method name: " + fqName);
            return;
        }
        String className = fqName.substring(0, lastDot).replace('.', '/');
        String methodName = fqName.substring(lastDot + 1);
        ClassDefItem clazz = dex.getClassOrNull(className);
        if (clazz == null) {
            this.context.err.println("no such class: " + className);
            return;
        }
        if (wildcard) {
            methodName = methodName.substring(0, methodName.length() - 1);
        }
        ArrayList<EncodedMethod> allMeths = clazz.getMethods();
        TreeMap<CstNat, EncodedMethod> meths = new TreeMap<CstNat, EncodedMethod>();
        for (EncodedMethod meth : allMeths) {
            String methName = meth.getName().getString();
            if ((!wildcard || !methName.startsWith(methodName)) && (wildcard || !methName.equals(methodName))) continue;
            meths.put(meth.getRef().getNat(), meth);
        }
        if (meths.size() == 0) {
            this.context.err.println("no such method: " + fqName);
            return;
        }
        PrintWriter pw = new PrintWriter(out);
        for (EncodedMethod meth : meths.values()) {
            meth.debugPrint(pw, this.args.verboseDump);
            CstString sourceFile = clazz.getSourceFile();
            if (sourceFile != null) {
                pw.println("  source file: " + sourceFile.toQuoted());
            }
            Annotations methodAnnotations = clazz.getMethodAnnotations(meth.getRef());
            AnnotationsList parameterAnnotations = clazz.getParameterAnnotations(meth.getRef());
            if (methodAnnotations != null) {
                pw.println("  method annotations:");
                for (Annotation a : methodAnnotations.getAnnotations()) {
                    pw.println("    " + a);
                }
            }
            if (parameterAnnotations == null) continue;
            pw.println("  parameter annotations:");
            int sz = parameterAnnotations.size();
            for (int i = 0; i < sz; ++i) {
                pw.println("    parameter " + i);
                Annotations annotations = parameterAnnotations.get(i);
                for (Annotation a : annotations.getAnnotations()) {
                    pw.println("      " + a);
                }
            }
        }
        pw.flush();
    }

    private class DexWriter
    implements Callable<byte[]> {
        private final DexFile dexFile;

        private DexWriter(DexFile dexFile) {
            this.dexFile = dexFile;
        }

        @Override
        public byte[] call() throws IOException {
            return Main.this.writeDex(this.dexFile);
        }
    }

    private class ClassDefItemConsumer
    implements Callable<Boolean> {
        String name;
        Future<ClassDefItem> futureClazz;
        int maxMethodIdsInClass;
        int maxFieldIdsInClass;

        private ClassDefItemConsumer(String name, Future<ClassDefItem> futureClazz, int maxMethodIdsInClass, int maxFieldIdsInClass) {
            this.name = name;
            this.futureClazz = futureClazz;
            this.maxMethodIdsInClass = maxMethodIdsInClass;
            this.maxFieldIdsInClass = maxFieldIdsInClass;
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public Boolean call() throws Exception {
            try {
                ClassDefItem clazz = this.futureClazz.get();
                if (clazz != null) {
                    Main.this.addClassToDex(clazz);
                    Main.this.updateStatus(true);
                }
                Boolean bl = true;
                return bl;
            }
            catch (ExecutionException ex) {
                Throwable t = ex.getCause();
                throw t instanceof Exception ? (Exception)t : ex;
            }
            finally {
                if (((Main)Main.this).args.multiDex) {
                    Object object = Main.this.dexRotationLock;
                    synchronized (object) {
                        Main.this.maxMethodIdsInProcess -= this.maxMethodIdsInClass;
                        Main.this.maxFieldIdsInProcess -= this.maxFieldIdsInClass;
                        Main.this.dexRotationLock.notifyAll();
                    }
                }
            }
        }
    }

    private class ClassTranslatorTask
    implements Callable<ClassDefItem> {
        String name;
        byte[] bytes;
        DirectClassFile classFile;

        private ClassTranslatorTask(String name, byte[] bytes, DirectClassFile classFile) {
            this.name = name;
            this.bytes = bytes;
            this.classFile = classFile;
        }

        @Override
        public ClassDefItem call() {
            ClassDefItem clazz = Main.this.translateClass(this.bytes, this.classFile);
            return clazz;
        }
    }

    private class DirectClassFileConsumer
    implements Callable<Boolean> {
        String name;
        byte[] bytes;
        Future<DirectClassFile> dcff;

        private DirectClassFileConsumer(String name, byte[] bytes, Future<DirectClassFile> dcff) {
            this.name = name;
            this.bytes = bytes;
            this.dcff = dcff;
        }

        @Override
        public Boolean call() throws Exception {
            DirectClassFile cf = this.dcff.get();
            return this.call(cf);
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        private Boolean call(DirectClassFile cf) {
            int maxMethodIdsInClass = 0;
            int maxFieldIdsInClass = 0;
            if (((Main)Main.this).args.multiDex) {
                int constantPoolSize = cf.getConstantPool().size();
                maxMethodIdsInClass = constantPoolSize + cf.getMethods().size() + 2;
                maxFieldIdsInClass = constantPoolSize + cf.getFields().size() + 9;
                Object object = Main.this.dexRotationLock;
                synchronized (object) {
                    int numFieldIds;
                    int numMethodIds;
                    DexFile dexFile = Main.this.outputDex;
                    synchronized (dexFile) {
                        numMethodIds = Main.this.outputDex.getMethodIds().items().size();
                        numFieldIds = Main.this.outputDex.getFieldIds().items().size();
                    }
                    while (numMethodIds + maxMethodIdsInClass + Main.this.maxMethodIdsInProcess > ((Main)Main.this).args.maxNumberOfIdxPerDex || numFieldIds + maxFieldIdsInClass + Main.this.maxFieldIdsInProcess > ((Main)Main.this).args.maxNumberOfIdxPerDex) {
                        if (Main.this.maxMethodIdsInProcess > 0 || Main.this.maxFieldIdsInProcess > 0) {
                            try {
                                Main.this.dexRotationLock.wait();
                            }
                            catch (InterruptedException interruptedException) {}
                        } else {
                            if (Main.this.outputDex.getClassDefs().items().size() <= 0) break;
                            Main.this.rotateDexFile();
                        }
                        dexFile = Main.this.outputDex;
                        synchronized (dexFile) {
                            numMethodIds = Main.this.outputDex.getMethodIds().items().size();
                            numFieldIds = Main.this.outputDex.getFieldIds().items().size();
                        }
                    }
                    Main.this.maxMethodIdsInProcess += maxMethodIdsInClass;
                    Main.this.maxFieldIdsInProcess += maxFieldIdsInClass;
                }
            }
            Future<ClassDefItem> cdif = Main.this.classTranslatorPool.submit(new ClassTranslatorTask(this.name, this.bytes, cf));
            Future<Boolean> res = Main.this.classDefItemConsumer.submit(new ClassDefItemConsumer(this.name, cdif, maxMethodIdsInClass, maxFieldIdsInClass));
            Main.this.addToDexFutures.add(res);
            return true;
        }
    }

    private class ClassParserTask
    implements Callable<DirectClassFile> {
        String name;
        byte[] bytes;

        private ClassParserTask(String name, byte[] bytes) {
            this.name = name;
            this.bytes = bytes;
        }

        @Override
        public DirectClassFile call() throws Exception {
            DirectClassFile cf = Main.this.parseClass(this.name, this.bytes);
            return cf;
        }
    }

    private class FileBytesConsumer
    implements ClassPathOpener.Consumer {
        private FileBytesConsumer() {
        }

        @Override
        public boolean processFileBytes(String name, long lastModified, byte[] bytes) {
            return Main.this.processFileBytes(name, lastModified, bytes);
        }

        @Override
        public void onException(Exception ex) {
            if (ex instanceof StopProcessing) {
                throw (StopProcessing)ex;
            }
            if (ex instanceof SimException) {
                ((Main)Main.this).context.err.println("\nEXCEPTION FROM SIMULATION:");
                ((Main)Main.this).context.err.println(ex.getMessage() + "\n");
                ((Main)Main.this).context.err.println(((SimException)ex).getContext());
            } else if (ex instanceof ParseException) {
                ((Main)Main.this).context.err.println("\nPARSE ERROR:");
                ParseException parseException = (ParseException)ex;
                if (((Main)Main.this).args.debug) {
                    parseException.printStackTrace(((Main)Main.this).context.err);
                } else {
                    parseException.printContext(((Main)Main.this).context.err);
                }
            } else {
                ((Main)Main.this).context.err.println("\nUNEXPECTED TOP-LEVEL EXCEPTION:");
                ex.printStackTrace(((Main)Main.this).context.err);
            }
            Main.this.errors.incrementAndGet();
        }

        @Override
        public void onProcessArchiveStart(File file) {
            if (((Main)Main.this).args.verbose) {
                ((Main)Main.this).context.out.println("processing archive " + file + "...");
            }
        }
    }

    public static class Arguments {
        private static final String MINIMAL_MAIN_DEX_OPTION = "--minimal-main-dex";
        private static final String MAIN_DEX_LIST_OPTION = "--main-dex-list";
        private static final String MULTI_DEX_OPTION = "--multi-dex";
        private static final String NUM_THREADS_OPTION = "--num-threads";
        private static final String INCREMENTAL_OPTION = "--incremental";
        private static final String INPUT_LIST_OPTION = "--input-list";
        public final DxContext context;
        public boolean debug = false;
        public boolean warnings = true;
        public boolean verbose = false;
        public boolean verboseDump = false;
        public boolean coreLibrary = false;
        public String methodToDump = null;
        public int dumpWidth = 0;
        public String outName = null;
        public String humanOutName = null;
        public boolean strictNameCheck = true;
        public boolean emptyOk = false;
        public boolean jarOutput = false;
        public boolean keepClassesInJar = false;
        public int minSdkVersion = 13;
        public int positionInfo = 2;
        public boolean localInfo = true;
        public boolean incremental = false;
        public boolean forceJumbo = false;
        public boolean allowAllInterfaceMethodInvokes = false;
        public String[] fileNames;
        public boolean optimize = true;
        public String optimizeListFile = null;
        public String dontOptimizeListFile = null;
        public boolean statistics;
        public CfOptions cfOptions;
        public DexOptions dexOptions;
        public int numThreads = 1;
        public boolean multiDex = false;
        public String mainDexListFile = null;
        public boolean minimalMainDex = false;
        public int maxNumberOfIdxPerDex = 65536;
        private List<String> inputList = null;
        private boolean outputIsDirectory = false;
        private boolean outputIsDirectDex = false;

        public Arguments(DxContext context) {
            this.context = context;
        }

        public Arguments() {
            this(new DxContext());
        }

        private void parseFlags(ArgumentsParser parser) {
            while (parser.getNext()) {
                if (parser.isArg("--debug")) {
                    this.debug = true;
                    continue;
                }
                if (parser.isArg("--no-warning")) {
                    this.warnings = false;
                    continue;
                }
                if (parser.isArg("--verbose")) {
                    this.verbose = true;
                    continue;
                }
                if (parser.isArg("--verbose-dump")) {
                    this.verboseDump = true;
                    continue;
                }
                if (parser.isArg("--no-files")) {
                    this.emptyOk = true;
                    continue;
                }
                if (parser.isArg("--no-optimize")) {
                    this.optimize = false;
                    continue;
                }
                if (parser.isArg("--no-strict")) {
                    this.strictNameCheck = false;
                    continue;
                }
                if (parser.isArg("--core-library")) {
                    this.coreLibrary = true;
                    continue;
                }
                if (parser.isArg("--statistics")) {
                    this.statistics = true;
                    continue;
                }
                if (parser.isArg("--optimize-list=")) {
                    if (this.dontOptimizeListFile != null) {
                        this.context.err.println("--optimize-list and --no-optimize-list are incompatible.");
                        throw new UsageException();
                    }
                    this.optimize = true;
                    this.optimizeListFile = parser.getLastValue();
                    continue;
                }
                if (parser.isArg("--no-optimize-list=")) {
                    if (this.dontOptimizeListFile != null) {
                        this.context.err.println("--optimize-list and --no-optimize-list are incompatible.");
                        throw new UsageException();
                    }
                    this.optimize = true;
                    this.dontOptimizeListFile = parser.getLastValue();
                    continue;
                }
                if (parser.isArg("--keep-classes")) {
                    this.keepClassesInJar = true;
                    continue;
                }
                if (parser.isArg("--output=")) {
                    this.outName = parser.getLastValue();
                    if (new File(this.outName).isDirectory()) {
                        this.jarOutput = false;
                        this.outputIsDirectory = true;
                        continue;
                    }
                    if (FileUtils.hasArchiveSuffix(this.outName)) {
                        this.jarOutput = true;
                        continue;
                    }
                    if (this.outName.endsWith(Main.DEX_EXTENSION) || this.outName.equals("-")) {
                        this.jarOutput = false;
                        this.outputIsDirectDex = true;
                        continue;
                    }
                    this.context.err.println("unknown output extension: " + this.outName);
                    throw new UsageException();
                }
                if (parser.isArg("--dump-to=")) {
                    this.humanOutName = parser.getLastValue();
                    continue;
                }
                if (parser.isArg("--dump-width=")) {
                    this.dumpWidth = Integer.parseInt(parser.getLastValue());
                    continue;
                }
                if (parser.isArg("--dump-method=")) {
                    this.methodToDump = parser.getLastValue();
                    this.jarOutput = false;
                    continue;
                }
                if (parser.isArg("--positions=")) {
                    String pstr = parser.getLastValue().intern();
                    if (pstr == "none") {
                        this.positionInfo = 1;
                        continue;
                    }
                    if (pstr == "important") {
                        this.positionInfo = 3;
                        continue;
                    }
                    if (pstr == "lines") {
                        this.positionInfo = 2;
                        continue;
                    }
                    this.context.err.println("unknown positions option: " + pstr);
                    throw new UsageException();
                }
                if (parser.isArg("--no-locals")) {
                    this.localInfo = false;
                    continue;
                }
                if (parser.isArg("--num-threads=")) {
                    this.numThreads = Integer.parseInt(parser.getLastValue());
                    continue;
                }
                if (parser.isArg(INCREMENTAL_OPTION)) {
                    this.incremental = true;
                    continue;
                }
                if (parser.isArg("--force-jumbo")) {
                    this.forceJumbo = true;
                    continue;
                }
                if (parser.isArg(MULTI_DEX_OPTION)) {
                    this.multiDex = true;
                    continue;
                }
                if (parser.isArg("--main-dex-list=")) {
                    this.mainDexListFile = parser.getLastValue();
                    continue;
                }
                if (parser.isArg(MINIMAL_MAIN_DEX_OPTION)) {
                    this.minimalMainDex = true;
                    continue;
                }
                if (parser.isArg("--set-max-idx-number=")) {
                    this.maxNumberOfIdxPerDex = Integer.parseInt(parser.getLastValue());
                    continue;
                }
                if (parser.isArg("--input-list=")) {
                    File inputListFile = new File(parser.getLastValue());
                    try {
                        this.inputList = new ArrayList<String>();
                        Main.readPathsFromFile(inputListFile.getAbsolutePath(), this.inputList);
                        continue;
                    }
                    catch (IOException e) {
                        this.context.err.println("Unable to read input list file: " + inputListFile.getName());
                        throw new UsageException();
                    }
                }
                if (parser.isArg("--min-sdk-version=")) {
                    int value;
                    String arg = parser.getLastValue();
                    try {
                        value = Integer.parseInt(arg);
                    }
                    catch (NumberFormatException ex) {
                        value = -1;
                    }
                    if (value < 1) {
                        System.err.println("improper min-sdk-version option: " + arg);
                        throw new UsageException();
                    }
                    this.minSdkVersion = value;
                    continue;
                }
                if (parser.isArg("--allow-all-interface-method-invokes")) {
                    this.allowAllInterfaceMethodInvokes = true;
                    continue;
                }
                this.context.err.println("unknown option: " + parser.getCurrent());
                throw new UsageException();
            }
        }

        private void parse(String[] args) {
            ArgumentsParser parser = new ArgumentsParser(args);
            this.parseFlags(parser);
            this.fileNames = parser.getRemaining();
            if (this.inputList != null && !this.inputList.isEmpty()) {
                this.inputList.addAll(Arrays.asList(this.fileNames));
                this.fileNames = this.inputList.toArray(new String[this.inputList.size()]);
            }
            if (this.fileNames.length == 0) {
                if (!this.emptyOk) {
                    this.context.err.println("no input files specified");
                    throw new UsageException();
                }
            } else if (this.emptyOk) {
                this.context.out.println("ignoring input files");
            }
            if (this.humanOutName == null && this.methodToDump != null) {
                this.humanOutName = "-";
            }
            if (this.mainDexListFile != null && !this.multiDex) {
                this.context.err.println("--main-dex-list is only supported in combination with --multi-dex");
                throw new UsageException();
            }
            if (this.minimalMainDex && (this.mainDexListFile == null || !this.multiDex)) {
                this.context.err.println("--minimal-main-dex is only supported in combination with --multi-dex and --main-dex-list");
                throw new UsageException();
            }
            if (this.multiDex && this.incremental) {
                this.context.err.println("--incremental is not supported with --multi-dex");
                throw new UsageException();
            }
            if (this.multiDex && this.outputIsDirectDex) {
                this.context.err.println("Unsupported output \"" + this.outName + "\". " + MULTI_DEX_OPTION + " supports only archive or directory output");
                throw new UsageException();
            }
            if (this.outputIsDirectory && !this.multiDex) {
                this.outName = new File(this.outName, "classes.dex").getPath();
            }
            this.makeOptionsObjects();
        }

        public void parseFlags(String[] flags) {
            this.parseFlags(new ArgumentsParser(flags));
        }

        public void makeOptionsObjects() {
            this.cfOptions = new CfOptions();
            this.cfOptions.positionInfo = this.positionInfo;
            this.cfOptions.localInfo = this.localInfo;
            this.cfOptions.strictNameCheck = this.strictNameCheck;
            this.cfOptions.optimize = this.optimize;
            this.cfOptions.optimizeListFile = this.optimizeListFile;
            this.cfOptions.dontOptimizeListFile = this.dontOptimizeListFile;
            this.cfOptions.statistics = this.statistics;
            this.cfOptions.warn = this.warnings ? this.context.err : this.context.noop;
            this.dexOptions = new DexOptions(this.context.err);
            this.dexOptions.minSdkVersion = this.minSdkVersion;
            this.dexOptions.forceJumbo = this.forceJumbo;
            this.dexOptions.allowAllInterfaceMethodInvokes = this.allowAllInterfaceMethodInvokes;
        }

        private static class ArgumentsParser {
            private final String[] arguments;
            private int index;
            private String current;
            private String lastValue;

            public ArgumentsParser(String[] arguments) {
                this.arguments = arguments;
                this.index = 0;
            }

            public String getCurrent() {
                return this.current;
            }

            public String getLastValue() {
                return this.lastValue;
            }

            public boolean getNext() {
                if (this.index >= this.arguments.length) {
                    return false;
                }
                this.current = this.arguments[this.index];
                if (this.current.equals("--") || !this.current.startsWith("--")) {
                    return false;
                }
                ++this.index;
                return true;
            }

            private boolean getNextValue() {
                if (this.index >= this.arguments.length) {
                    return false;
                }
                this.current = this.arguments[this.index];
                ++this.index;
                return true;
            }

            public String[] getRemaining() {
                int n = this.arguments.length - this.index;
                String[] remaining = new String[n];
                if (n > 0) {
                    System.arraycopy(this.arguments, this.index, remaining, 0, n);
                }
                return remaining;
            }

            public boolean isArg(String prefix) {
                int n = prefix.length();
                if (n > 0 && prefix.charAt(n - 1) == '=') {
                    if (this.current.startsWith(prefix)) {
                        this.lastValue = this.current.substring(n);
                        return true;
                    }
                    if (this.current.equals(prefix = prefix.substring(0, n - 1))) {
                        if (this.getNextValue()) {
                            this.lastValue = this.current;
                            return true;
                        }
                        System.err.println("Missing value after parameter " + prefix);
                        throw new UsageException();
                    }
                    return false;
                }
                return this.current.equals(prefix);
            }
        }
    }

    private static class StopProcessing
    extends RuntimeException {
        private StopProcessing() {
        }
    }

    private class BestEffortMainDexListFilter
    implements ClassPathOpener.FileNameFilter {
        Map<String, List<String>> map = new HashMap<String, List<String>>();

        public BestEffortMainDexListFilter() {
            for (String pathOfClass : Main.this.classesInMainDex) {
                String normalized = Main.fixPath(pathOfClass);
                String simple = this.getSimpleName(normalized);
                List<String> fullPath = this.map.get(simple);
                if (fullPath == null) {
                    fullPath = new ArrayList<String>(1);
                    this.map.put(simple, fullPath);
                }
                fullPath.add(normalized);
            }
        }

        @Override
        public boolean accept(String path) {
            if (path.endsWith(".class")) {
                String normalized = Main.fixPath(path);
                String simple = this.getSimpleName(normalized);
                List<String> fullPaths = this.map.get(simple);
                if (fullPaths != null) {
                    for (String fullPath : fullPaths) {
                        if (!normalized.endsWith(fullPath)) continue;
                        return true;
                    }
                }
                return false;
            }
            return true;
        }

        private String getSimpleName(String path) {
            int index = path.lastIndexOf(47);
            if (index >= 0) {
                return path.substring(index + 1);
            }
            return path;
        }
    }

    private class MainDexListFilter
    implements ClassPathOpener.FileNameFilter {
        private MainDexListFilter() {
        }

        @Override
        public boolean accept(String fullPath) {
            if (fullPath.endsWith(".class")) {
                String path = Main.fixPath(fullPath);
                return Main.this.classesInMainDex.contains(path);
            }
            return true;
        }
    }

    private static class RemoveModuleInfoFilter
    implements ClassPathOpener.FileNameFilter {
        protected final ClassPathOpener.FileNameFilter delegate;

        public RemoveModuleInfoFilter(ClassPathOpener.FileNameFilter delegate) {
            this.delegate = delegate;
        }

        @Override
        public boolean accept(String path) {
            return this.delegate.accept(path) && !"module-info.class".equals(path);
        }
    }

    private static class NotFilter
    implements ClassPathOpener.FileNameFilter {
        private final ClassPathOpener.FileNameFilter filter;

        private NotFilter(ClassPathOpener.FileNameFilter filter) {
            this.filter = filter;
        }

        @Override
        public boolean accept(String path) {
            return !this.filter.accept(path);
        }
    }
}

