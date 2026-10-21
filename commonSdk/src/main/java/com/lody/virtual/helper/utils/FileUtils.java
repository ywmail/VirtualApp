/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.Build$VERSION
 *  android.os.Parcel
 *  android.system.Os
 *  android.text.TextUtils
 */
package com.lody.virtual.helper.utils;

import android.content.Context;
import android.os.Build;
import android.os.Parcel;
import android.system.Os;
import android.text.TextUtils;
import com.lody.virtual.StringFog;
import com.lody.virtual.helper.utils.ArrayUtils;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.spi.AbstractInterruptibleChannel;

public class FileUtils {
    public static String getFilenameExt(String filename) {
        int dotPos = filename.lastIndexOf(46);
        if (dotPos == -1) {
            return "";
        }
        return filename.substring(dotPos + 1);
    }

    public static File changeExt(File f, String targetExt) {
        String outPath = f.getAbsolutePath();
        if (!FileUtils.getFilenameExt(outPath).equals(targetExt)) {
            int dotPos = outPath.lastIndexOf(".");
            outPath = dotPos > 0 ? outPath.substring(0, dotPos + 1) + targetExt : outPath + "." + targetExt;
            return new File(outPath);
        }
        return f;
    }

    public static String readToString(String fileName) throws IOException {
        int i;
        FileInputStream is = new FileInputStream(fileName);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        while ((i = ((InputStream)is).read()) != -1) {
            baos.write(i);
        }
        return baos.toString();
    }

    public static long fileSize(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return 0L;
        }
        return file.length();
    }

    public static void chmod(String path, int mode) {
        if (Build.VERSION.SDK_INT >= 21) {
            try {
                Os.chmod((String)path, (int)mode);
                return;
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        File file = new File(path);
        String cmd = "chmod ";
        if (file.isDirectory()) {
            cmd = cmd + " -R ";
        }
        String cmode = String.format("%o", mode);
        try {
            Runtime.getRuntime().exec(cmd + cmode + " " + path).waitFor();
        }
        catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void createSymlink(String oldPath, String newPath) throws Exception {
        Os.symlink((String)oldPath, (String)newPath);
    }

    public static boolean ensureDirCreate(File dir) {
        return dir.exists() || dir.mkdirs();
    }

    public static boolean ensureDirCreate(File ... dirs) {
        boolean created = true;
        for (File dir : dirs) {
            if (FileUtils.ensureDirCreate(dir)) continue;
            created = false;
        }
        return created;
    }

    public static boolean isSymlink(File file) throws IOException {
        File canon;
        if (file == null) {
            throw new NullPointerException("File must not be null");
        }
        if (file.getParent() == null) {
            canon = file;
        } else {
            File canonDir = file.getParentFile().getCanonicalFile();
            canon = new File(canonDir, file.getName());
        }
        return !canon.getCanonicalFile().equals(canon.getAbsoluteFile());
    }

    public static void writeParcelToFile(Parcel p, File file) throws IOException {
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(p.marshall());
        fos.close();
    }

    public static byte[] toByteArray(InputStream inStream) throws IOException {
        int rc;
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        byte[] buff = new byte[100];
        while ((rc = inStream.read(buff, 0, 100)) > 0) {
            swapStream.write(buff, 0, rc);
        }
        return swapStream.toByteArray();
    }

    public static void copyTo(InputStream is, OutputStream os) throws IOException {
        int rc;
        BufferedOutputStream bos = new BufferedOutputStream(os);
        byte[] buff = new byte[4096];
        while ((rc = is.read(buff, 0, buff.length)) > 0) {
            bos.write(buff, 0, rc);
        }
        bos.flush();
    }

    public static void deleteDir(File dir) {
        boolean isDir = dir.isDirectory();
        if (isDir) {
            String[] children;
            boolean link = false;
            try {
                link = FileUtils.isSymlink(dir);
            }
            catch (Exception exception) {
                // empty catch block
            }
            if (!link && (children = dir.list()) != null) {
                for (String file : children) {
                    FileUtils.deleteDir(new File(dir, file));
                }
            }
        }
        dir.delete();
    }

    public static void deleteDir(String dir) {
        FileUtils.deleteDir(new File(dir));
    }

    public static void writeToFile(InputStream dataIns, File target) throws IOException {
        int BUFFER = 1024;
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(target));
        try {
            int count;
            byte[] data = new byte[1024];
            while ((count = dataIns.read(data, 0, 1024)) != -1) {
                bos.write(data, 0, count);
            }
            bos.close();
        }
        catch (IOException e) {
            throw e;
        }
        finally {
            FileUtils.closeQuietly(bos);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void writeToFile(byte[] data, File target) throws IOException {
        FileOutputStream fo = null;
        ReadableByteChannel src = null;
        AbstractInterruptibleChannel out = null;
        try {
            src = Channels.newChannel(new ByteArrayInputStream(data));
            fo = new FileOutputStream(target);
            out = fo.getChannel();
            ((FileChannel)out).transferFrom(src, 0L, data.length);
        }
        finally {
            if (fo != null) {
                fo.close();
            }
            if (src != null) {
                src.close();
            }
            if (out != null) {
                out.close();
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static void copyFile(File source, File target) throws IOException {
        if (source.getCanonicalPath().equals(target.getCanonicalPath())) {
            return;
        }
        FileInputStream inputStream = null;
        FileOutputStream outputStream = null;
        try {
            inputStream = new FileInputStream(source);
            outputStream = new FileOutputStream(target);
            FileChannel iChannel = inputStream.getChannel();
            FileChannel oChannel = outputStream.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            while (true) {
                buffer.clear();
                int r = iChannel.read(buffer);
                if (r != -1) {
                    buffer.limit(buffer.position());
                    buffer.position(0);
                    oChannel.write(buffer);
                    continue;
                }
                break;
            }
        }
        catch (Throwable throwable) {
            FileUtils.closeQuietly(inputStream);
            FileUtils.closeQuietly(outputStream);
            throw throwable;
        }
        FileUtils.closeQuietly(inputStream);
        FileUtils.closeQuietly(outputStream);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void copyFileFromAssets(Context context, String source, File target) throws IOException {
        InputStream inputStream = null;
        FileOutputStream outputStream = null;
        try {
            inputStream = context.getResources().getAssets().open(source);
            outputStream = new FileOutputStream(target);
            byte[] buffer = new byte[1024];
            int count = 0;
            while ((count = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, count);
            }
        }
        catch (Throwable throwable) {
            FileUtils.closeQuietly(inputStream);
            FileUtils.closeQuietly(outputStream);
            throw throwable;
        }
        FileUtils.closeQuietly(inputStream);
        FileUtils.closeQuietly(outputStream);
    }

    public static void linkDir(String sourcePath, String destPath) {
        File[] files;
        File source = new File(sourcePath);
        File dest = new File(destPath);
        if (!dest.exists()) {
            dest.mkdirs();
        }
        if (!ArrayUtils.isEmpty(files = source.listFiles())) {
            for (File file : files) {
                try {
                    Runtime.getRuntime().exec(String.format("ln -s %s %s", file.getAbsoluteFile(), new File(dest, file.getName()).getAbsolutePath())).waitFor();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
    }

    public static int peekInt(byte[] bytes, int value, ByteOrder endian) {
        int v0;
        if (endian == ByteOrder.BIG_ENDIAN) {
            v0 = value + 1;
            int v2 = v0 + 1;
            v0 = (bytes[v0] & 0xFF) << 16 | (bytes[value] & 0xFF) << 24 | (bytes[v2] & 0xFF) << 8 | bytes[v2 + 1] & 0xFF;
        } else {
            v0 = value + 1;
            int v2 = v0 + 1;
            v0 = (bytes[v0] & 0xFF) << 8 | bytes[value] & 0xFF | (bytes[v2] & 0xFF) << 16 | (bytes[v2 + 1] & 0xFF) << 24;
        }
        return v0;
    }

    private static boolean isValidExtFilenameChar(char c) {
        switch (c) {
            case '\u0000': 
            case '/': {
                return false;
            }
        }
        return true;
    }

    public static boolean isValidExtFilename(String name) {
        return name != null && name.equals(FileUtils.buildValidExtFilename(name));
    }

    public static String buildValidExtFilename(String name) {
        if (TextUtils.isEmpty((CharSequence)name) || ".".equals(name) || "..".equals(name)) {
            return "(invalid)";
        }
        StringBuilder res = new StringBuilder(name.length());
        for (int i = 0; i < name.length(); ++i) {
            char c = name.charAt(i);
            if (FileUtils.isValidExtFilenameChar(c)) {
                res.append(c);
                continue;
            }
            res.append('_');
        }
        return res.toString();
    }

    public static boolean isExist(String path) {
        return new File(path).exists();
    }

    public static boolean canRead(String path) {
        return new File(path).canRead();
    }

    public static String getPathFileName(String file) {
        String fName = file.trim();
        if (fName.indexOf("/") > -1) {
            return fName.substring(fName.lastIndexOf("/") + 1);
        }
        return file;
    }

    public static interface FileMode {
        public static final int MODE_ISUID = 2048;
        public static final int MODE_ISGID = 1024;
        public static final int MODE_ISVTX = 512;
        public static final int MODE_IRUSR = 256;
        public static final int MODE_IWUSR = 128;
        public static final int MODE_IXUSR = 64;
        public static final int MODE_IRGRP = 32;
        public static final int MODE_IWGRP = 16;
        public static final int MODE_IXGRP = 8;
        public static final int MODE_IROTH = 4;
        public static final int MODE_IWOTH = 2;
        public static final int MODE_IXOTH = 1;
        public static final int MODE_755 = 493;
    }
}

