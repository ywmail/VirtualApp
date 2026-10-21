/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.command.annotool;

import com.android.dx.cf.attrib.BaseAnnotations;
import com.android.dx.cf.direct.ClassPathOpener;
import com.android.dx.cf.direct.DirectClassFile;
import com.android.dx.cf.direct.StdAttributeFactory;
import com.android.dx.cf.iface.Attribute;
import com.android.dx.cf.iface.AttributeList;
import com.android.dx.command.annotool.Main;
import com.android.dx.rop.annotation.Annotation;
import com.android.dx.util.ByteArray;
import java.io.File;
import java.lang.annotation.ElementType;
import java.util.HashSet;

class AnnotationLister {
    private static final String PACKAGE_INFO = "package-info";
    private final Main.Arguments args;
    HashSet<String> matchInnerClassesOf = new HashSet();
    HashSet<String> matchPackages = new HashSet();

    AnnotationLister(Main.Arguments args) {
        this.args = args;
    }

    void process() {
        for (String path : this.args.files) {
            ClassPathOpener opener = new ClassPathOpener(path, true, new ClassPathOpener.Consumer(){

                @Override
                public boolean processFileBytes(String name, long lastModified, byte[] bytes) {
                    if (!name.endsWith(".class")) {
                        return true;
                    }
                    ByteArray ba = new ByteArray(bytes);
                    DirectClassFile cf = new DirectClassFile(ba, name, true);
                    cf.setAttributeFactory(StdAttributeFactory.THE_ONE);
                    AttributeList attributes = cf.getAttributes();
                    String cfClassName = cf.getThisClass().getClassType().getClassName();
                    if (cfClassName.endsWith(AnnotationLister.PACKAGE_INFO)) {
                        BaseAnnotations ann;
                        Attribute att = attributes.findFirst("RuntimeInvisibleAnnotations");
                        while (att != null) {
                            ann = (BaseAnnotations)att;
                            AnnotationLister.this.visitPackageAnnotation(cf, ann);
                            att = attributes.findNext(att);
                        }
                        att = attributes.findFirst("RuntimeVisibleAnnotations");
                        while (att != null) {
                            ann = (BaseAnnotations)att;
                            AnnotationLister.this.visitPackageAnnotation(cf, ann);
                            att = attributes.findNext(att);
                        }
                    } else if (AnnotationLister.this.isMatchingInnerClass(cfClassName) || AnnotationLister.this.isMatchingPackage(cfClassName)) {
                        AnnotationLister.this.printMatch(cf);
                    } else {
                        BaseAnnotations ann;
                        Attribute att = attributes.findFirst("RuntimeInvisibleAnnotations");
                        while (att != null) {
                            ann = (BaseAnnotations)att;
                            AnnotationLister.this.visitClassAnnotation(cf, ann);
                            att = attributes.findNext(att);
                        }
                        att = attributes.findFirst("RuntimeVisibleAnnotations");
                        while (att != null) {
                            ann = (BaseAnnotations)att;
                            AnnotationLister.this.visitClassAnnotation(cf, ann);
                            att = attributes.findNext(att);
                        }
                    }
                    return true;
                }

                @Override
                public void onException(Exception ex) {
                    throw new RuntimeException(ex);
                }

                @Override
                public void onProcessArchiveStart(File file) {
                }
            });
            opener.process();
        }
    }

    private void visitClassAnnotation(DirectClassFile cf, BaseAnnotations ann) {
        if (!this.args.eTypes.contains((Object)ElementType.TYPE)) {
            return;
        }
        for (Annotation anAnn : ann.getAnnotations().getAnnotations()) {
            String annClassName = anAnn.getType().getClassType().getClassName();
            if (!this.args.aclass.equals(annClassName)) continue;
            this.printMatch(cf);
        }
    }

    private void visitPackageAnnotation(DirectClassFile cf, BaseAnnotations ann) {
        if (!this.args.eTypes.contains((Object)ElementType.PACKAGE)) {
            return;
        }
        String packageName = cf.getThisClass().getClassType().getClassName();
        int slashIndex = packageName.lastIndexOf(47);
        packageName = slashIndex == -1 ? "" : packageName.substring(0, slashIndex);
        for (Annotation anAnn : ann.getAnnotations().getAnnotations()) {
            String annClassName = anAnn.getType().getClassType().getClassName();
            if (!this.args.aclass.equals(annClassName)) continue;
            this.printMatchPackage(packageName);
        }
    }

    private void printMatchPackage(String packageName) {
        for (Main.PrintType pt : this.args.printTypes) {
            switch (pt) {
                case CLASS: 
                case INNERCLASS: 
                case METHOD: {
                    this.matchPackages.add(packageName);
                    break;
                }
                case PACKAGE: {
                    System.out.println(packageName.replace('/', '.'));
                }
            }
        }
    }

    private void printMatch(DirectClassFile cf) {
        for (Main.PrintType pt : this.args.printTypes) {
            switch (pt) {
                case CLASS: {
                    String classname = cf.getThisClass().getClassType().getClassName();
                    classname = classname.replace('/', '.');
                    System.out.println(classname);
                    break;
                }
                case INNERCLASS: {
                    this.matchInnerClassesOf.add(cf.getThisClass().getClassType().getClassName());
                    break;
                }
                case METHOD: {
                    break;
                }
            }
        }
    }

    private boolean isMatchingInnerClass(String s) {
        int i;
        while (0 < (i = s.lastIndexOf(36))) {
            if (!this.matchInnerClassesOf.contains(s = s.substring(0, i))) continue;
            return true;
        }
        return false;
    }

    private boolean isMatchingPackage(String s) {
        int slashIndex = s.lastIndexOf(47);
        String packageName = slashIndex == -1 ? "" : s.substring(0, slashIndex);
        return this.matchPackages.contains(packageName);
    }
}

