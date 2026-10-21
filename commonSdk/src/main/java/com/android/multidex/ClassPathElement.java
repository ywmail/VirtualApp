/*
 * Decompiled with CFR 0.152.
 */
package com.android.multidex;

import java.io.IOException;
import java.io.InputStream;

interface ClassPathElement {
    public static final char SEPARATOR_CHAR = '/';

    public InputStream open(String var1) throws IOException;

    public void close() throws IOException;

    public Iterable<String> list();
}

