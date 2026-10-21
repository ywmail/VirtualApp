/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.xmlpull.v1.XmlSerializer
 */
package com.lody.virtual.helper.utils;

import com.lody.virtual.StringFog;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import org.xmlpull.v1.XmlSerializer;

public class FastXmlSerializer
implements XmlSerializer {
    private static final String[] ESCAPE_TABLE = new String[]{null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "&quot;", null, null, null, "&amp;", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "&lt;", null, "&gt;", null};
    private static final int BUFFER_LEN = 8192;
    private static String sSpace = "                                                              ";
    private final char[] mText = new char[8192];
    private int mPos;
    private Writer mWriter;
    private OutputStream mOutputStream;
    private CharsetEncoder mCharset;
    private ByteBuffer mBytes = ByteBuffer.allocate(8192);
    private boolean mIndent = false;
    private boolean mInTag;
    private int mNesting = 0;
    private boolean mLineStart = true;

    private void append(char c) throws IOException {
        int pos = this.mPos;
        if (pos >= 8191) {
            this.flush();
            pos = this.mPos;
        }
        this.mText[pos] = c;
        this.mPos = pos + 1;
    }

    private void append(String str, int i, int length) throws IOException {
        if (length > 8192) {
            int end = i + length;
            while (i < end) {
                int next = i + 8192;
                this.append(str, i, next < end ? 8192 : end - i);
                i = next;
            }
            return;
        }
        int pos = this.mPos;
        if (pos + length > 8192) {
            this.flush();
            pos = this.mPos;
        }
        str.getChars(i, i + length, this.mText, pos);
        this.mPos = pos + length;
    }

    private void append(char[] buf, int i, int length) throws IOException {
        if (length > 8192) {
            int end = i + length;
            while (i < end) {
                int next = i + 8192;
                this.append(buf, i, next < end ? 8192 : end - i);
                i = next;
            }
            return;
        }
        int pos = this.mPos;
        if (pos + length > 8192) {
            this.flush();
            pos = this.mPos;
        }
        System.arraycopy(buf, i, this.mText, pos, length);
        this.mPos = pos + length;
    }

    private void append(String str) throws IOException {
        this.append(str, 0, str.length());
    }

    private void appendIndent(int indent) throws IOException {
        if ((indent *= 4) > sSpace.length()) {
            indent = sSpace.length();
        }
        this.append(sSpace, 0, indent);
    }

    private void escapeAndAppendString(String string2) throws IOException {
        int pos;
        int N = string2.length();
        char NE = (char)ESCAPE_TABLE.length;
        String[] escapes = ESCAPE_TABLE;
        int lastPos = 0;
        for (pos = 0; pos < N; ++pos) {
            String escape;
            char c = string2.charAt(pos);
            if (c >= NE || (escape = escapes[c]) == null) continue;
            if (lastPos < pos) {
                this.append(string2, lastPos, pos - lastPos);
            }
            lastPos = pos + 1;
            this.append(escape);
        }
        if (lastPos < pos) {
            this.append(string2, lastPos, pos - lastPos);
        }
    }

    private void escapeAndAppendString(char[] buf, int start, int len) throws IOException {
        int pos;
        char NE = (char)ESCAPE_TABLE.length;
        String[] escapes = ESCAPE_TABLE;
        int end = start + len;
        int lastPos = start;
        for (pos = start; pos < end; ++pos) {
            String escape;
            char c = buf[pos];
            if (c >= NE || (escape = escapes[c]) == null) continue;
            if (lastPos < pos) {
                this.append(buf, lastPos, pos - lastPos);
            }
            lastPos = pos + 1;
            this.append(escape);
        }
        if (lastPos < pos) {
            this.append(buf, lastPos, pos - lastPos);
        }
    }

    public XmlSerializer attribute(String namespace, String name, String value) throws IOException, IllegalArgumentException, IllegalStateException {
        this.append(' ');
        if (namespace != null) {
            this.append(namespace);
            this.append(':');
        }
        this.append(name);
        this.append("=\"");
        this.escapeAndAppendString(value);
        this.append('\"');
        this.mLineStart = false;
        return this;
    }

    public void cdsect(String text) throws IOException, IllegalArgumentException, IllegalStateException {
        throw new UnsupportedOperationException();
    }

    public void comment(String text) throws IOException, IllegalArgumentException, IllegalStateException {
        throw new UnsupportedOperationException();
    }

    public void docdecl(String text) throws IOException, IllegalArgumentException, IllegalStateException {
        throw new UnsupportedOperationException();
    }

    public void endDocument() throws IOException, IllegalArgumentException, IllegalStateException {
        this.flush();
    }

    public XmlSerializer endTag(String namespace, String name) throws IOException, IllegalArgumentException, IllegalStateException {
        --this.mNesting;
        if (this.mInTag) {
            this.append(" />\n");
        } else {
            if (this.mIndent && this.mLineStart) {
                this.appendIndent(this.mNesting);
            }
            this.append("</");
            if (namespace != null) {
                this.append(namespace);
                this.append(':');
            }
            this.append(name);
            this.append(">\n");
        }
        this.mLineStart = true;
        this.mInTag = false;
        return this;
    }

    public void entityRef(String text) throws IOException, IllegalArgumentException, IllegalStateException {
        throw new UnsupportedOperationException();
    }

    private void flushBytes() throws IOException {
        int position = this.mBytes.position();
        if (position > 0) {
            this.mBytes.flip();
            this.mOutputStream.write(this.mBytes.array(), 0, position);
            this.mBytes.clear();
        }
    }

    public void flush() throws IOException {
        if (this.mPos > 0) {
            if (this.mOutputStream != null) {
                CharBuffer charBuffer = CharBuffer.wrap(this.mText, 0, this.mPos);
                CoderResult result = this.mCharset.encode(charBuffer, this.mBytes, true);
                while (true) {
                    if (result.isError()) {
                        throw new IOException(result.toString());
                    }
                    if (!result.isOverflow()) break;
                    this.flushBytes();
                    result = this.mCharset.encode(charBuffer, this.mBytes, true);
                }
                this.flushBytes();
                this.mOutputStream.flush();
            } else {
                this.mWriter.write(this.mText, 0, this.mPos);
                this.mWriter.flush();
            }
            this.mPos = 0;
        }
    }

    public int getDepth() {
        throw new UnsupportedOperationException();
    }

    public boolean getFeature(String name) {
        throw new UnsupportedOperationException();
    }

    public String getName() {
        throw new UnsupportedOperationException();
    }

    public String getNamespace() {
        throw new UnsupportedOperationException();
    }

    public String getPrefix(String namespace, boolean generatePrefix) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    public Object getProperty(String name) {
        throw new UnsupportedOperationException();
    }

    public void ignorableWhitespace(String text) throws IOException, IllegalArgumentException, IllegalStateException {
        throw new UnsupportedOperationException();
    }

    public void processingInstruction(String text) throws IOException, IllegalArgumentException, IllegalStateException {
        throw new UnsupportedOperationException();
    }

    public void setFeature(String name, boolean state) throws IllegalArgumentException, IllegalStateException {
        if (name.equals("http://xmlpull.org/v1/doc/features.html#indent-output")) {
            this.mIndent = true;
            return;
        }
        throw new UnsupportedOperationException();
    }

    public void setOutput(OutputStream os, String encoding) throws IOException, IllegalArgumentException, IllegalStateException {
        if (os == null) {
            throw new IllegalArgumentException();
        }
        try {
            this.mCharset = Charset.forName(encoding).newEncoder();
        }
        catch (IllegalCharsetNameException e) {
            throw (UnsupportedEncodingException)new UnsupportedEncodingException(encoding).initCause(e);
        }
        catch (UnsupportedCharsetException e) {
            throw (UnsupportedEncodingException)new UnsupportedEncodingException(encoding).initCause(e);
        }
        this.mOutputStream = os;
    }

    public void setOutput(Writer writer) throws IOException, IllegalArgumentException, IllegalStateException {
        this.mWriter = writer;
    }

    public void setPrefix(String prefix, String namespace) throws IOException, IllegalArgumentException, IllegalStateException {
        throw new UnsupportedOperationException();
    }

    public void setProperty(String name, Object value) throws IllegalArgumentException, IllegalStateException {
        throw new UnsupportedOperationException();
    }

    public void startDocument(String encoding, Boolean standalone) throws IOException, IllegalArgumentException, IllegalStateException {
        this.append("<?xml version='1.0' encoding='utf-8' standalone='" + (standalone != false ? "yes" : "no") + "' ?>\n");
        this.mLineStart = true;
    }

    public XmlSerializer startTag(String namespace, String name) throws IOException, IllegalArgumentException, IllegalStateException {
        if (this.mInTag) {
            this.append(">\n");
        }
        if (this.mIndent) {
            this.appendIndent(this.mNesting);
        }
        ++this.mNesting;
        this.append('<');
        if (namespace != null) {
            this.append(namespace);
            this.append(':');
        }
        this.append(name);
        this.mInTag = true;
        this.mLineStart = false;
        return this;
    }

    public XmlSerializer text(char[] buf, int start, int len) throws IOException, IllegalArgumentException, IllegalStateException {
        if (this.mInTag) {
            this.append(">");
            this.mInTag = false;
        }
        this.escapeAndAppendString(buf, start, len);
        if (this.mIndent) {
            this.mLineStart = buf[start + len - 1] == '\n';
        }
        return this;
    }

    public XmlSerializer text(String text) throws IOException, IllegalArgumentException, IllegalStateException {
        if (this.mInTag) {
            this.append(">");
            this.mInTag = false;
        }
        this.escapeAndAppendString(text);
        if (this.mIndent) {
            this.mLineStart = text.length() > 0 && text.charAt(text.length() - 1) == '\n';
        }
        return this;
    }
}

