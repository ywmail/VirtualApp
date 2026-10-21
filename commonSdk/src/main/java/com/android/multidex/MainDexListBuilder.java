/*
 * Decompiled with CFR 0.152.
 */
package com.android.multidex;

import com.android.dx.cf.attrib.AttRuntimeVisibleAnnotations;
import com.android.dx.cf.direct.DirectClassFile;
import com.android.dx.cf.iface.Attribute;
import com.android.dx.cf.iface.FieldList;
import com.android.dx.cf.iface.HasAttribute;
import com.android.dx.cf.iface.MethodList;
import com.android.multidex.ClassPathElement;
import com.android.multidex.ClassReferenceListBuilder;
import com.android.multidex.Path;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipFile;

public class MainDexListBuilder {
    private static final String CLASS_EXTENSION = ".class";
    private static final int STATUS_ERROR = 1;
    private static final String EOL = System.getProperty("line.separator");
    private static final String USAGE_MESSAGE = "Usage:" + EOL + EOL + "Short version: Don't use this." + EOL + EOL + "Slightly longer version: This tool is used by mainDexClasses script to build" + EOL + "the main dex list." + EOL;
    private static final String DISABLE_ANNOTATION_RESOLUTION_WORKAROUND = "--disable-annotation-resolution-workaround";
    private Set<String> filesToKeep = new HashSet<String>();

    public static void main(String[] args) {
        int argIndex;
        boolean keepAnnotated = true;
        for (argIndex = 0; argIndex < args.length - 2; ++argIndex) {
            if (args[argIndex].equals(DISABLE_ANNOTATION_RESOLUTION_WORKAROUND)) {
                keepAnnotated = false;
                continue;
            }
            System.err.println("Invalid option " + args[argIndex]);
            MainDexListBuilder.printUsage();
            System.exit(1);
        }
        if (args.length - argIndex != 2) {
            MainDexListBuilder.printUsage();
            System.exit(1);
        }
        try {
            MainDexListBuilder builder = new MainDexListBuilder(keepAnnotated, args[argIndex], args[argIndex + 1]);
            Set<String> toKeep = builder.getMainDexList();
            MainDexListBuilder.printList(toKeep);
        }
        catch (IOException e) {
            System.err.println("A fatal error occured: " + e.getMessage());
            System.exit(1);
            return;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public MainDexListBuilder(boolean keepAnnotated, String rootJar, String pathString) throws IOException {
        ZipFile jarOfRoots = null;
        Path path = null;
        try {
            try {
                jarOfRoots = new ZipFile(rootJar);
            }
            catch (IOException e) {
                throw new IOException("\"" + rootJar + "\" can not be read as a zip archive. (" + e.getMessage() + ")", e);
            }
            path = new Path(pathString);
            ClassReferenceListBuilder mainListBuilder = new ClassReferenceListBuilder(path);
            mainListBuilder.addRoots(jarOfRoots);
            for (String className : mainListBuilder.getClassNames()) {
                this.filesToKeep.add(className + CLASS_EXTENSION);
            }
            if (keepAnnotated) {
                this.keepAnnotated(path);
            }
        }
        finally {
            try {
                jarOfRoots.close();
            }
            catch (IOException iOException) {}
            if (path != null) {
                for (ClassPathElement element : path.elements) {
                    try {
                        element.close();
                    }
                    catch (IOException iOException) {}
                }
            }
        }
    }

    public Set<String> getMainDexList() {
        return this.filesToKeep;
    }

    private static void printUsage() {
        System.err.print(USAGE_MESSAGE);
    }

    private static void printList(Set<String> fileNames) {
        for (String fileName : fileNames) {
            System.out.println(fileName);
        }
    }

    private void keepAnnotated(Path path) throws FileNotFoundException {
        for (ClassPathElement element : path.getElements()) {
            block1: for (String name : element.list()) {
                if (!name.endsWith(CLASS_EXTENSION)) continue;
                DirectClassFile clazz = path.getClass(name);
                if (this.hasRuntimeVisibleAnnotation(clazz)) {
                    this.filesToKeep.add(name);
                    continue;
                }
                MethodList methods = clazz.getMethods();
                for (int i = 0; i < methods.size(); ++i) {
                    if (!this.hasRuntimeVisibleAnnotation(methods.get(i))) continue;
                    this.filesToKeep.add(name);
                    continue block1;
                }
                FieldList fields = clazz.getFields();
                for (int i = 0; i < fields.size(); ++i) {
                    if (!this.hasRuntimeVisibleAnnotation(fields.get(i))) continue;
                    this.filesToKeep.add(name);
                    continue block1;
                }
            }
        }
    }

    private boolean hasRuntimeVisibleAnnotation(HasAttribute element) {
        Attribute att = element.getAttributes().findFirst("RuntimeVisibleAnnotations");
        return att != null && ((AttRuntimeVisibleAnnotations)att).getAnnotations().size() > 0;
    }
}

