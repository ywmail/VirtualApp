/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.command.annotool;

import com.android.dx.command.annotool.AnnotationLister;
import java.lang.annotation.ElementType;
import java.util.AbstractCollection;
import java.util.EnumSet;
import java.util.Locale;

public class Main {
    private Main() {
    }

    public static void main(String[] argArray) {
        Arguments args = new Arguments();
        try {
            args.parse(argArray);
        }
        catch (InvalidArgumentException ex) {
            System.err.println(ex.getMessage());
            throw new RuntimeException("usage");
        }
        new AnnotationLister(args).process();
    }

    static class Arguments {
        String aclass;
        EnumSet<ElementType> eTypes = EnumSet.noneOf(ElementType.class);
        EnumSet<PrintType> printTypes = EnumSet.noneOf(PrintType.class);
        String[] files;

        Arguments() {
        }

        void parse(String[] argArray) throws InvalidArgumentException {
            for (int i = 0; i < argArray.length; ++i) {
                String argParam;
                String arg = argArray[i];
                if (arg.startsWith("--annotation=")) {
                    argParam = arg.substring(arg.indexOf(61) + 1);
                    if (this.aclass != null) {
                        throw new InvalidArgumentException("--annotation can only be specified once.");
                    }
                    this.aclass = argParam.replace('.', '/');
                    continue;
                }
                if (arg.startsWith("--element=")) {
                    argParam = arg.substring(arg.indexOf(61) + 1);
                    try {
                        for (String p : argParam.split(",")) {
                            this.eTypes.add(ElementType.valueOf(p.toUpperCase(Locale.ROOT)));
                        }
                        continue;
                    }
                    catch (IllegalArgumentException ex) {
                        throw new InvalidArgumentException("invalid --element");
                    }
                }
                if (arg.startsWith("--print=")) {
                    argParam = arg.substring(arg.indexOf(61) + 1);
                    try {
                        for (String p : argParam.split(",")) {
                            this.printTypes.add(PrintType.valueOf(p.toUpperCase(Locale.ROOT)));
                        }
                        continue;
                    }
                    catch (IllegalArgumentException ex) {
                        throw new InvalidArgumentException("invalid --print");
                    }
                }
                this.files = new String[argArray.length - i];
                System.arraycopy(argArray, i, this.files, 0, this.files.length);
                break;
            }
            if (this.aclass == null) {
                throw new InvalidArgumentException("--annotation must be specified");
            }
            if (this.printTypes.isEmpty()) {
                this.printTypes.add(PrintType.CLASS);
            }
            if (this.eTypes.isEmpty()) {
                this.eTypes.add(ElementType.TYPE);
            }
            Object set = this.eTypes.clone();
            ((AbstractCollection)set).remove((Object)ElementType.TYPE);
            ((AbstractCollection)set).remove((Object)ElementType.PACKAGE);
            if (!((AbstractCollection)set).isEmpty()) {
                throw new InvalidArgumentException("only --element parameters 'type' and 'package' supported");
            }
        }
    }

    static enum PrintType {
        CLASS,
        INNERCLASS,
        METHOD,
        PACKAGE;

    }

    private static class InvalidArgumentException
    extends Exception {
        InvalidArgumentException() {
        }

        InvalidArgumentException(String s) {
            super(s);
        }
    }
}

