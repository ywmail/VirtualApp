/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.command.dump;

import com.android.dx.cf.direct.DirectClassFile;
import com.android.dx.cf.direct.StdAttributeFactory;
import com.android.dx.command.dump.Args;
import com.android.dx.command.dump.BaseDumper;
import com.android.dx.util.ByteArray;
import java.io.PrintStream;

public final class ClassDumper
extends BaseDumper {
    public static void dump(byte[] bytes, PrintStream out, String filePath, Args args) {
        ClassDumper cd = new ClassDumper(bytes, out, filePath, args);
        cd.dump();
    }

    private ClassDumper(byte[] bytes, PrintStream out, String filePath, Args args) {
        super(bytes, out, filePath, args);
    }

    public void dump() {
        byte[] bytes = this.getBytes();
        ByteArray ba = new ByteArray(bytes);
        DirectClassFile cf = new DirectClassFile(ba, this.getFilePath(), this.getStrictParse());
        cf.setAttributeFactory(StdAttributeFactory.THE_ONE);
        cf.setObserver(this);
        cf.getMagic();
        int readBytes = this.getReadBytes();
        if (readBytes != bytes.length) {
            this.parsed(ba, readBytes, bytes.length - readBytes, "<extra data at end of file>");
        }
    }
}

