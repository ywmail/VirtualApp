/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.util;

import com.android.dx.util.IndentingWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;

public final class TwoColumnOutput {
    private final Writer out;
    private final int leftWidth;
    private final StringBuffer leftBuf;
    private final StringBuffer rightBuf;
    private final IndentingWriter leftColumn;
    private final IndentingWriter rightColumn;

    public static String toString(String s1, int width1, String spacer, String s2, int width2) {
        int len1 = s1.length();
        int len2 = s2.length();
        StringWriter sw = new StringWriter((len1 + len2) * 3);
        TwoColumnOutput twoOut = new TwoColumnOutput(sw, width1, width2, spacer);
        try {
            twoOut.getLeft().write(s1);
            twoOut.getRight().write(s2);
        }
        catch (IOException ex) {
            throw new RuntimeException("shouldn't happen", ex);
        }
        twoOut.flush();
        return sw.toString();
    }

    public TwoColumnOutput(Writer out, int leftWidth, int rightWidth, String spacer) {
        if (out == null) {
            throw new NullPointerException("out == null");
        }
        if (leftWidth < 1) {
            throw new IllegalArgumentException("leftWidth < 1");
        }
        if (rightWidth < 1) {
            throw new IllegalArgumentException("rightWidth < 1");
        }
        if (spacer == null) {
            throw new NullPointerException("spacer == null");
        }
        StringWriter leftWriter = new StringWriter(1000);
        StringWriter rightWriter = new StringWriter(1000);
        this.out = out;
        this.leftWidth = leftWidth;
        this.leftBuf = leftWriter.getBuffer();
        this.rightBuf = rightWriter.getBuffer();
        this.leftColumn = new IndentingWriter(leftWriter, leftWidth);
        this.rightColumn = new IndentingWriter(rightWriter, rightWidth, spacer);
    }

    public TwoColumnOutput(OutputStream out, int leftWidth, int rightWidth, String spacer) {
        this(new OutputStreamWriter(out), leftWidth, rightWidth, spacer);
    }

    public Writer getLeft() {
        return this.leftColumn;
    }

    public Writer getRight() {
        return this.rightColumn;
    }

    public void flush() {
        try {
            TwoColumnOutput.appendNewlineIfNecessary(this.leftBuf, this.leftColumn);
            TwoColumnOutput.appendNewlineIfNecessary(this.rightBuf, this.rightColumn);
            this.outputFullLines();
            this.flushLeft();
            this.flushRight();
        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void outputFullLines() throws IOException {
        int leftLen;
        while ((leftLen = this.leftBuf.indexOf("\n")) >= 0) {
            int rightLen = this.rightBuf.indexOf("\n");
            if (rightLen < 0) {
                return;
            }
            if (leftLen != 0) {
                this.out.write(this.leftBuf.substring(0, leftLen));
            }
            if (rightLen != 0) {
                TwoColumnOutput.writeSpaces(this.out, this.leftWidth - leftLen);
                this.out.write(this.rightBuf.substring(0, rightLen));
            }
            this.out.write(10);
            this.leftBuf.delete(0, leftLen + 1);
            this.rightBuf.delete(0, rightLen + 1);
        }
        return;
    }

    private void flushLeft() throws IOException {
        TwoColumnOutput.appendNewlineIfNecessary(this.leftBuf, this.leftColumn);
        while (this.leftBuf.length() != 0) {
            this.rightColumn.write(10);
            this.outputFullLines();
        }
    }

    private void flushRight() throws IOException {
        TwoColumnOutput.appendNewlineIfNecessary(this.rightBuf, this.rightColumn);
        while (this.rightBuf.length() != 0) {
            this.leftColumn.write(10);
            this.outputFullLines();
        }
    }

    private static void appendNewlineIfNecessary(StringBuffer buf, Writer out) throws IOException {
        int len = buf.length();
        if (len != 0 && buf.charAt(len - 1) != '\n') {
            out.write(10);
        }
    }

    private static void writeSpaces(Writer out, int amt) throws IOException {
        while (amt > 0) {
            out.write(32);
            --amt;
        }
    }
}

