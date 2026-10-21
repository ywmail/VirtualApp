/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.ContentResolver
 *  android.content.Context
 *  android.content.Intent
 *  android.content.res.AssetManager
 *  android.database.Cursor
 *  android.graphics.Bitmap
 *  android.graphics.Bitmap$CompressFormat
 *  android.net.Uri
 *  android.os.Environment
 *  android.os.Handler
 *  android.provider.MediaStore$Images$Media
 *  android.provider.MediaStore$Images$Thumbnails
 *  android.text.TextUtils
 *  android.util.Log
 */
package com.kook.common.util;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import com.kook.common.utils.HVLog;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class FileTools {
    public static boolean delete(String delFile) {
        File file = new File(delFile);
        if (!file.exists()) {
            return false;
        }
        if (file.isFile()) {
            return FileTools.deleteSingleFile(delFile);
        }
        return FileTools.deleteDirectory(delFile);
    }

    private static boolean deleteSingleFile(String filePath$Name) {
        File file = new File(filePath$Name);
        if (file.exists() && file.isFile()) {
            return file.delete();
        }
        return false;
    }

    private static boolean deleteDirectory(String filePath) {
        File[] files;
        File dirFile;
        if (!filePath.endsWith(File.separator)) {
            filePath = filePath + File.separator;
        }
        if (!(dirFile = new File(filePath)).exists() || !dirFile.isDirectory()) {
            return false;
        }
        boolean flag = true;
        for (File file : files = dirFile.listFiles()) {
            if (file.isFile() ? !(flag = FileTools.deleteSingleFile(file.getAbsolutePath())) : file.isDirectory() && !(flag = FileTools.deleteDirectory(file.getAbsolutePath()))) break;
        }
        if (!flag) {
            return false;
        }
        return dirFile.delete();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static boolean saveAsFileWriter(String filePath, String content) {
        Log.d((String)"kook", (String)("\u6587\u4ef6\u4fdd\u5b58\u5230:" + filePath));
        File file = new File(filePath);
        file.deleteOnExit();
        OutputStreamWriter fwriter = null;
        try {
            fwriter = new FileWriter(filePath, false);
            fwriter.write(content);
            boolean bl = true;
            return bl;
        }
        catch (IOException ex) {
            Log.d((String)"kook", (String)("" + ex.toString()));
        }
        finally {
            try {
                fwriter.flush();
                fwriter.close();
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return false;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static String saveBitmap(Bitmap bm, String imageFile) {
        File file;
        String filename = imageFile.substring(imageFile.lastIndexOf(File.separator) + 1);
        String filepath = imageFile.substring(0, imageFile.lastIndexOf(File.separator));
        File path = new File(filepath);
        if (!path.exists()) {
            path.mkdirs();
        }
        if ((file = new File(filepath, filename)).exists()) {
            file.delete();
        }
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.PNG, 90, (OutputStream)outputStream);
            outputStream.flush();
            outputStream.close();
            return file.getAbsolutePath();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        catch (Exception e) {
            return null;
        }
    }

    public static void saveBitmap(final Context context, Bitmap bm, boolean updateAlum) {
        String fileName = "save.png";
        final File file = new File(Environment.getExternalStoragePublicDirectory((String)""), fileName);
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.PNG, 90, (OutputStream)out);
            out.flush();
            out.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        if (updateAlum) {
            try {
                MediaStore.Images.Media.insertImage((ContentResolver)context.getContentResolver(), (String)file.getAbsolutePath(), (String)fileName, null);
                String[] projection = new String[]{"_id", "_data"};
                Cursor cursor = context.getContentResolver().query(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, projection, "_id = ?", new String[]{"123"}, null);
                while (cursor.moveToNext()) {
                }
                context.sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", Uri.parse((String)("file://" + file.getPath()))));
            }
            catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            new Handler().postDelayed(new Runnable(){

                @Override
                public void run() {
                    String where = "_data like \"" + file.getAbsolutePath() + "%\"";
                    int i = context.getContentResolver().delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, where, null);
                    if (i > 0) {
                        // empty if block
                    }
                }
            }, 15000L);
        }
    }

    public static String readFile(String filePath) {
        String content = "";
        try {
            FileInputStream instream = new FileInputStream(filePath);
            if (instream != null) {
                InputStreamReader inputreader = new InputStreamReader((InputStream)instream, "UTF-8");
                BufferedReader buffreader = new BufferedReader(inputreader);
                char[] chars = new char[1024];
                int len = 0;
                while ((len = inputreader.read(chars)) != -1) {
                    content = content + new String(chars, 0, len);
                }
                ((InputStream)instream).close();
            }
        }
        catch (FileNotFoundException fileNotFoundException) {
        }
        catch (IOException iOException) {
            // empty catch block
        }
        return content;
    }

    public static int copyDir(String fromFile, String toFile) {
        File root = new File(fromFile);
        if (!root.exists()) {
            return -1;
        }
        File[] currentFiles = root.listFiles();
        File targetDir = new File(toFile);
        if (!targetDir.exists()) {
            targetDir.mkdirs();
        }
        for (int i = 0; i < currentFiles.length; ++i) {
            if (currentFiles[i].isDirectory()) {
                FileTools.copyDir(currentFiles[i].getPath() + "/", toFile + currentFiles[i].getName() + "/");
                continue;
            }
            FileTools.copyFile(currentFiles[i].getPath(), toFile + currentFiles[i].getName());
        }
        return 0;
    }

    public static String getFileNameByPath(String path, boolean suffix) {
        int end;
        if (path == null) {
            return "";
        }
        int start = path.lastIndexOf("/");
        int n = end = suffix ? path.length() : path.lastIndexOf(".");
        if (start != -1 && end != -1) {
            return path.substring(start + 1, end);
        }
        return "";
    }

    public static String getFileDirByPath(String path) {
        if (path == null) {
            return "";
        }
        int start = 0;
        int end = path.lastIndexOf("/");
        if (start != -1 && end != -1) {
            return path.substring(start, end + 1);
        }
        return "";
    }

    public static void copyFolder(String oldPath, String newPath) {
        try {
            new File(newPath).mkdirs();
            File a = new File(oldPath);
            String[] file = a.list();
            File temp = null;
            for (int i = 0; i < file.length; ++i) {
                temp = oldPath.endsWith(File.separator) ? new File(oldPath + file[i]) : new File(oldPath + File.separator + file[i]);
                if (temp.isFile()) {
                    int len;
                    FileInputStream input = new FileInputStream(temp);
                    FileOutputStream output = new FileOutputStream(newPath + "/" + temp.getName().toString());
                    byte[] b = new byte[5120];
                    while ((len = input.read(b)) != -1) {
                        output.write(b, 0, len);
                    }
                    output.flush();
                    output.close();
                    input.close();
                }
                if (!temp.isDirectory()) continue;
                FileTools.copyFolder(oldPath + "/" + file[i], newPath + "/" + file[i]);
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    public static void copyFile(String oldPath, String newPath) {
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) {
                FileInputStream inStream = new FileInputStream(oldPath);
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                while ((byteread = ((InputStream)inStream).read(buffer)) != -1) {
                    System.out.println(bytesum += byteread);
                    fs.write(buffer, 0, byteread);
                }
                ((InputStream)inStream).close();
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    public static String FormetFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        fileSizeString = fileS < 1024L ? df.format((double)fileS) + "B" : (fileS < 0x100000L ? df.format((double)fileS / 1024.0) + "K" : (fileS < 0x40000000L ? df.format((double)fileS / 1048576.0) + "M" : df.format((double)fileS / 1.073741824E9) + "G"));
        return fileSizeString;
    }

    public static String getFileNameWithParams(String path, int type) {
        if (TextUtils.isEmpty((CharSequence)path) || type <= 0 || type > 4) {
            throw new RuntimeException(" \u4f20\u5165\u53c2\u6570\u5f02\u5e38 ");
        }
        int start = path.lastIndexOf("/");
        if (start != -1) {
            if (type == 1) {
                return path.substring(start + 1);
            }
            if (type == 2) {
                return path.substring(0, start + 1);
            }
            if (type == 3) {
                int index = path.lastIndexOf(".");
                return path.substring(index + 1);
            }
            if (type == 4) {
                String substring = path.substring(0, start);
                int indexOf = substring.lastIndexOf("/");
                if (indexOf != -1) {
                    return substring.substring(indexOf + 1);
                }
                return "";
            }
            return "";
        }
        return "";
    }

    public static void inputstreamtofile(InputStream ins, File file) {
        try {
            if (file.exists()) {
                file.deleteOnExit();
            } else {
                File parentFile = file.getParentFile();
                if (!parentFile.exists()) {
                    file.getParentFile().mkdirs();
                }
                file.createNewFile();
            }
            FileOutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                ((OutputStream)os).write(buffer, 0, bytesRead);
            }
            ((OutputStream)os).close();
            ins.close();
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void copyAssetsToSdCard(Context context, String assetFileName, String targetDir) {
        AssetManager assetManager = context.getAssets();
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            int length;
            inputStream = assetManager.open(assetFileName);
            File outputFile = new File(targetDir, assetFileName);
            HVLog.d("copyAssetsToSdCard " + targetDir);
            outputStream = new FileOutputStream(outputFile);
            byte[] buffer = new byte[1024];
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            outputStream.flush();
        }
        catch (IOException e) {
            e.printStackTrace();
            HVLog.printException(e);
        }
        finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static List<String> copyAssetsToDirectory(Context context, String assetPath, String destinationDirectory) {
        String[] files;
        AssetManager assetManager = context.getAssets();
        try {
            files = assetManager.list(assetPath);
        }
        catch (IOException e) {
            Log.e((String)"kooklog", (String)"Failed to get asset file list.", (Throwable)e);
            return null;
        }
        ArrayList<String> fileNames = new ArrayList<String>();
        for (String filename : files) {
            if (filename.indexOf(".apk") == -1 || filename.indexOf("plugin_") != -1) continue;
            fileNames.add(filename);
        }
        for (String filename : fileNames) {
            try {
                InputStream in = assetManager.open(filename);
                File outFile = new File(destinationDirectory, filename);
                FileOutputStream out = new FileOutputStream(outFile);
                FileTools.copyFile(in, out);
                in.close();
                out.flush();
                ((OutputStream)out).close();
            }
            catch (IOException e) {
                Log.e((String)"kooklog", (String)("Failed to copy asset file: " + filename), (Throwable)e);
            }
        }
        return fileNames;
    }

    private static void copyFile(InputStream in, OutputStream out) throws IOException {
        int read;
        byte[] buffer = new byte[1024];
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }
}

