/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.command.dump;

import com.android.dx.cf.code.ConcreteMethod;
import com.android.dx.cf.iface.Member;
import com.android.dx.cf.iface.ParseObserver;
import com.android.dx.command.dump.Args;
import com.android.dx.dex.DexOptions;
import com.android.dx.util.ByteArray;
import com.android.dx.util.Hex;
import com.android.dx.util.IndentingWriter;
import com.android.dx.util.TwoColumnOutput;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringWriter;

public abstract class BaseDumper
implements ParseObserver {
    private final byte[] bytes;
    private final boolean rawBytes;
    private final PrintStream out;
    private final int width;
    private final String filePath;
    private final boolean strictParse;
    private final int hexCols;
    private int indent;
    private String separator;
    private int readBytes;
    protected Args args;
    protected final DexOptions dexOptions;

    public BaseDumper(byte[] bytes, PrintStream out, String filePath, Args args) {
        this.bytes = bytes;
        this.rawBytes = args.rawBytes;
        this.out = out;
        this.width = args.width <= 0 ? 79 : args.width;
        this.filePath = filePath;
        this.strictParse = args.strictParse;
        this.indent = 0;
        this.separator = this.rawBytes ? "|" : "";
        this.readBytes = 0;
        this.args = args;
        this.dexOptions = new DexOptions();
        int hexCols = (this.width - 5) / 15 + 1 & 0xFFFFFFFE;
        if (hexCols < 6) {
            hexCols = 6;
        } else if (hexCols > 10) {
            hexCols = 10;
        }
        this.hexCols = hexCols;
    }

    static int computeParamWidth(ConcreteMethod meth, boolean isStatic) {
        return meth.getEffectiveDescriptor().getParameterTypes().getWordCount();
    }

    @Override
    public void changeIndent(int indentDelta) {
        this.indent += indentDelta;
        this.separator = this.rawBytes ? "|" : "";
        for (int i = 0; i < this.indent; ++i) {
            this.separator = this.separator + "  ";
        }
    }

    @Override
    public void parsed(ByteArray bytes, int offset, int len, String human) {
        offset = bytes.underlyingOffset(offset);
        boolean rawBytes = this.getRawBytes();
        String hex = rawBytes ? this.hexDump(offset, len) : "";
        this.print(this.twoColumns(hex, human));
        this.readBytes += len;
    }

    @Override
    public void startParsingMember(ByteArray bytes, int offset, String name, String descriptor) {
    }

    @Override
    public void endParsingMember(ByteArray bytes, int offset, String name, String descriptor, Member member) {
    }

    protected final int getReadBytes() {
        return this.readBytes;
    }

    protected final byte[] getBytes() {
        return this.bytes;
    }

    protected final String getFilePath() {
        return this.filePath;
    }

    protected final boolean getStrictParse() {
        return this.strictParse;
    }

    protected final void print(String s) {
        this.out.print(s);
    }

    protected final void println(String s) {
        this.out.println(s);
    }

    protected final boolean getRawBytes() {
        return this.rawBytes;
    }

    protected final int getWidth1() {
        if (this.rawBytes) {
            return 5 + this.hexCols * 2 + this.hexCols / 2;
        }
        return 0;
    }

    protected final int getWidth2() {
        int w1 = this.rawBytes ? this.getWidth1() + 1 : 0;
        return this.width - w1 - this.indent * 2;
    }

    protected final String hexDump(int offset, int len) {
        return Hex.dump(this.bytes, offset, len, offset, this.hexCols, 4);
    }

    protected final String twoColumns(String s1, String s2) {
        int w1 = this.getWidth1();
        int w2 = this.getWidth2();
        try {
            if (w1 == 0) {
                int len2 = s2.length();
                StringWriter sw = new StringWriter(len2 * 2);
                IndentingWriter iw = new IndentingWriter(sw, w2, this.separator);
                iw.write(s2);
                if (len2 == 0 || s2.charAt(len2 - 1) != '\n') {
                    iw.write(10);
                }
                iw.flush();
                return sw.toString();
            }
            return TwoColumnOutput.toString(s1, w1, this.separator, s2, w2);
        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}

