/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.command.grep;

import com.android.dex.Dex;
import com.android.dx.command.grep.Grep;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Pattern;

public final class Main {
    public static void main(String[] args) throws IOException {
        String dexFile = args[0];
        Dex dex = new Dex(new File(dexFile));
        String pattern = args[1];
        int count = new Grep(dex, Pattern.compile(pattern), new PrintWriter(System.out)).grep();
        System.exit(count > 0 ? 0 : 1);
    }
}

