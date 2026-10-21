/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Build$VERSION
 *  dalvik.system.DexFile
 */
package com.lody.virtual.helper;

import android.os.Build;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.env.VirtualRuntime;
import com.lody.virtual.helper.utils.FileUtils;
import dalvik.system.DexFile;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DexOptimizer {
    public static void dex2oat(String dexFilePath, String oatFilePath) throws IOException {
        block6: {
            if (Build.VERSION.SDK_INT < 30) {
                File oatFile = new File(oatFilePath);
                FileUtils.ensureDirCreate(oatFile.getParentFile());
                ArrayList<String> commandAndParams = new ArrayList<String>();
                commandAndParams.add("dex2oat");
                commandAndParams.add("--dex-file=" + dexFilePath);
                commandAndParams.add("--oat-file=" + oatFilePath);
                commandAndParams.add("--instruction-set=" + VirtualRuntime.getCurrentInstructionSet());
                if (Build.VERSION.SDK_INT > 25) {
                    commandAndParams.add("--compiler-filter=quicken");
                } else {
                    commandAndParams.add("--compiler-filter=interpret-only");
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
                    break block6;
                }
                catch (InterruptedException e) {
                    throw new IOException("dex2oat is interrupted, msg: " + e.getMessage(), e);
                }
            }
            DexFile.loadDex((String)dexFilePath, (String)oatFilePath, (int)0).close();
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

