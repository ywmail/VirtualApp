/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Build$VERSION
 */
package com.swift.sandhook.utils;

import android.os.Build;
import com.swift.sandhook.SandHook;
import com.swift.sandhook.SandHookConfig;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ArtDexOptimizer {
    public static void dexoatAndDisableInline(String dexFilePath, String oatFilePath) throws IOException {
        File oatFile = new File(oatFilePath);
        if (!oatFile.exists()) {
            oatFile.getParentFile().mkdirs();
        }
        ArrayList<String> commandAndParams = new ArrayList<String>();
        commandAndParams.add("dex2oat");
        if (SandHookConfig.SDK_INT >= 24) {
            commandAndParams.add("--runtime-arg");
            commandAndParams.add("-classpath");
            commandAndParams.add("--runtime-arg");
            commandAndParams.add("&");
        }
        commandAndParams.add("--dex-file=" + dexFilePath);
        commandAndParams.add("--oat-file=" + oatFilePath);
        commandAndParams.add("--instruction-set=" + (SandHook.is64Bit() ? "arm64" : "arm"));
        commandAndParams.add("--compiler-filter=everything");
        if (SandHookConfig.SDK_INT >= 22 && SandHookConfig.SDK_INT < 29) {
            commandAndParams.add("--compile-pic");
        }
        if (SandHookConfig.SDK_INT > 25) {
            commandAndParams.add("--inline-max-code-units=0");
        } else if (Build.VERSION.SDK_INT >= 23) {
            commandAndParams.add("--inline-depth-limit=0");
        }
        ProcessBuilder pb = new ProcessBuilder(commandAndParams);
        pb.redirectErrorStream(true);
        Process dex2oatProcess = pb.start();
        StreamConsumer.consumeInputStream(dex2oatProcess.getInputStream());
        StreamConsumer.consumeInputStream(dex2oatProcess.getErrorStream());
        try {
            int ret = dex2oatProcess.waitFor();
            if (ret != 0) {
                throw new IOException("dex2oat works unsuccessfully, exit code: " + ret);
            }
        }
        catch (InterruptedException e) {
            throw new IOException("dex2oat is interrupted, msg: " + e.getMessage(), e);
        }
    }

    private static class StreamConsumer {
        static final Executor STREAM_CONSUMER = Executors.newSingleThreadExecutor();

        private StreamConsumer() {
        }

        static void consumeInputStream(final InputStream is) {
            STREAM_CONSUMER.execute(new Runnable(){

                @Override
                public void run() {
                    if (is == null) {
                        return;
                    }
                    byte[] buffer = new byte[256];
                    try {
                        while (is.read(buffer) > 0) {
                        }
                    }
                    catch (IOException iOException) {
                    }
                    finally {
                        try {
                            is.close();
                        }
                        catch (Exception exception) {}
                    }
                }
            });
        }
    }
}

