/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.command.dump;

import com.android.dex.util.FileUtils;
import com.android.dx.cf.iface.ParseException;
import com.android.dx.command.dump.Args;
import com.android.dx.command.dump.BlockDumper;
import com.android.dx.command.dump.ClassDumper;
import com.android.dx.command.dump.DotDumper;
import com.android.dx.command.dump.SsaDumper;
import com.android.dx.util.HexParser;
import java.io.UnsupportedEncodingException;

public class Main {
    private final Args parsedArgs = new Args();

    private Main() {
    }

    public static void main(String[] args) {
        new Main().run(args);
    }

    private void run(String[] args) {
        String arg;
        int at;
        for (at = 0; at < args.length && !(arg = args[at]).equals("--") && arg.startsWith("--"); ++at) {
            if (arg.equals("--bytes")) {
                this.parsedArgs.rawBytes = true;
                continue;
            }
            if (arg.equals("--basic-blocks")) {
                this.parsedArgs.basicBlocks = true;
                continue;
            }
            if (arg.equals("--rop-blocks")) {
                this.parsedArgs.ropBlocks = true;
                continue;
            }
            if (arg.equals("--optimize")) {
                this.parsedArgs.optimize = true;
                continue;
            }
            if (arg.equals("--ssa-blocks")) {
                this.parsedArgs.ssaBlocks = true;
                continue;
            }
            if (arg.startsWith("--ssa-step=")) {
                this.parsedArgs.ssaStep = arg.substring(arg.indexOf(61) + 1);
                continue;
            }
            if (arg.equals("--debug")) {
                this.parsedArgs.debug = true;
                continue;
            }
            if (arg.equals("--dot")) {
                this.parsedArgs.dotDump = true;
                continue;
            }
            if (arg.equals("--strict")) {
                this.parsedArgs.strictParse = true;
                continue;
            }
            if (arg.startsWith("--width=")) {
                arg = arg.substring(arg.indexOf(61) + 1);
                this.parsedArgs.width = Integer.parseInt(arg);
                continue;
            }
            if (arg.startsWith("--method=")) {
                this.parsedArgs.method = arg = arg.substring(arg.indexOf(61) + 1);
                continue;
            }
            System.err.println("unknown option: " + arg);
            throw new RuntimeException("usage");
        }
        if (at == args.length) {
            System.err.println("no input files specified");
            throw new RuntimeException("usage");
        }
        while (at < args.length) {
            try {
                String name = args[at];
                System.out.println("reading " + name + "...");
                byte[] bytes = FileUtils.readFile(name);
                if (!name.endsWith(".class")) {
                    String src;
                    try {
                        src = new String(bytes, "utf-8");
                    }
                    catch (UnsupportedEncodingException ex) {
                        throw new RuntimeException("shouldn't happen", ex);
                    }
                    bytes = HexParser.parse(src);
                }
                this.processOne(name, bytes);
            }
            catch (ParseException ex) {
                System.err.println("\ntrouble parsing:");
                if (this.parsedArgs.debug) {
                    ex.printStackTrace();
                }
                ex.printContext(System.err);
            }
            ++at;
        }
    }

    private void processOne(String name, byte[] bytes) {
        if (this.parsedArgs.dotDump) {
            DotDumper.dump(bytes, name, this.parsedArgs);
        } else if (this.parsedArgs.basicBlocks) {
            BlockDumper.dump(bytes, System.out, name, false, this.parsedArgs);
        } else if (this.parsedArgs.ropBlocks) {
            BlockDumper.dump(bytes, System.out, name, true, this.parsedArgs);
        } else if (this.parsedArgs.ssaBlocks) {
            this.parsedArgs.optimize = false;
            SsaDumper.dump(bytes, System.out, name, this.parsedArgs);
        } else {
            ClassDumper.dump(bytes, System.out, name, this.parsedArgs);
        }
    }
}

