/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.text.TextUtils
 *  okhttp3.MultipartBody
 *  okhttp3.MultipartBody$Builder
 *  okhttp3.OkHttpClient
 *  okhttp3.Request
 *  okhttp3.Request$Builder
 *  okhttp3.RequestBody
 *  okhttp3.Response
 *  org.jdeferred.Promise
 */
package com.carlos.common.clouddisk;

import android.content.Context;
import android.text.TextUtils;
import com.carlos.common.clouddisk.http.FileProgressRequestBody;
import com.carlos.common.clouddisk.http.HttpWorker;
import com.carlos.common.clouddisk.http.MyCookieJar;
import com.carlos.common.clouddisk.http.OkHttpUtil;
import com.carlos.common.clouddisk.listview.FileItem;
import com.carlos.common.ui.UIConstant;
import com.carlos.common.ui.utils.LanzouHelper;
import com.carlos.common.utils.ResponseProgram;
import com.kook.common.utils.HVLog;
import com.kook.librelease.StringFog;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.jdeferred.Promise;

public class ClouddiskLauncher {
    private static ClouddiskLauncher mClouddiskLauncher;
    private List<String> historyDir = new ArrayList<String>();
    private List<FileItem> currentFolder = new ArrayList<FileItem>();
    static SimpleDateFormat mSimpleDateFormat;

    private ClouddiskLauncher() {
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static ClouddiskLauncher getInstance() {
        if (mClouddiskLauncher != null) return mClouddiskLauncher;
        Class<ClouddiskLauncher> clazz = ClouddiskLauncher.class;
        synchronized (ClouddiskLauncher.class) {
            if (mClouddiskLauncher != null) return mClouddiskLauncher;
            mClouddiskLauncher = new ClouddiskLauncher();
            // ** MonitorExit[var0] (shouldn't be in output)
            return mClouddiskLauncher;
        }
    }

    public static String getCurrentDate() {
        return mSimpleDateFormat.format(new Date());
    }

    public static String getCurrentDate(String date) {
        long time = Long.parseLong(date);
        return mSimpleDateFormat.format(new Date(time));
    }

    public Promise<Void, Throwable, Void> updaterCloud(String uploadFilePath, String folder_id, HttpWorker.UpLoadCallbackListener listener) {
        return ResponseProgram.defer().when(() -> {
            try {
                HttpWorker.getInstance().uploadFileSync(uploadFilePath, folder_id, listener);
            }
            catch (Exception e) {
                HVLog.printException(e);
            }
        });
    }

    public Promise<Void, Throwable, Void> updaterCloud(String uploadFilePath, String uploadFileName, String folder_id, final HttpWorker.UpLoadCallbackListener listener) {
        return ResponseProgram.defer().when(() -> {
            try {
                HttpWorker.getInstance().uploadFileSync(uploadFilePath, folder_id, listener);
                OkHttpClient client = OkHttpUtil.getmOkHttpClient2();
                File file = new File(uploadFilePath);
                FileProgressRequestBody fileBody = new FileProgressRequestBody("application/x-www-form-urlencoded;charset=utf-8", file, new FileProgressRequestBody.ProgressListener(){

                    @Override
                    public void transferred(double size) {
                        listener.Progress(size);
                    }
                });
                MultipartBody mBody = new MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart("size", String.valueOf(file.length())).addFormDataPart("task", "1").addFormDataPart("folder_id", folder_id).addFormDataPart("name", uploadFileName).addFormDataPart("upload_file", uploadFileName, (RequestBody)fileBody).build();
                Request request = new Request.Builder().url("https://pc.woozooo.com/fileup.php").post((RequestBody)mBody).build();
                Response response = client.newCall(request).execute();
                String string2 = response.body().string();
            }
            catch (Exception e) {
                HVLog.printException(e);
            }
        });
    }

    public Promise<LanzouHelper.Lanzou, Throwable, Void> downFileByCloud(String fileId) {
        return ResponseProgram.defer().when(() -> {
            try {
                String fileHrefSync = HttpWorker.getInstance().getFileHrefSync(fileId);
                LanzouHelper.Lanzou lanZouRealLink = LanzouHelper.getLanZouRealLink(fileHrefSync);
                return lanZouRealLink;
            }
            catch (Exception e) {
                HVLog.printException(e);
                return null;
            }
        });
    }

    public void launcherCloud(Context context) {
        if (this.historyDir.size() == 0) {
            MyCookieJar.resetCookies();
        }
        HttpWorker.Login("18117395833", "w329716228", new HttpWorker.loginCallbackListener(){

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onFinish() {
                HVLog.d("已经成功介入蓝奏云盘 " + ClouddiskLauncher.this.historyDir.size());
                ClouddiskLauncher.this.historyDir.clear();
                String[] dateDir = new String[]{UIConstant.CLOUD_DISK_BACKUP_RECOVERY_DIRECTORY, ClouddiskLauncher.getCurrentDate()};
                ClouddiskLauncher.this.openPageByDirectory(dateDir, UIConstant.CLOUD_DISK_ROOT_ID, 0).done(re -> HVLog.d("打开目录已经完成 1111 "));
            }
        });
    }

    private Promise<List<FileItem>, Throwable, Void> openPageByDirectory(String[] dirTrees, String folder_id, int treeIndex) {
        return ResponseProgram.defer().when(() -> {
            this.historyDir.add(folder_id);
            return HttpWorker.getInstance().getFolderInfoSync(folder_id);
        }).done(fileItemList -> {
            if (fileItemList != null) {
                if (treeIndex >= dirTrees.length) {
                    HVLog.d("最后目录情况:" + fileItemList + "    folder_id:" + folder_id);
                    for (FileItem fileItem : this.currentFolder) {
                        HVLog.d("最后目录 fileItem:" + fileItem.toString());
                    }
                    return;
                }
                this.currentFolder = fileItemList;
                HVLog.d("fileItemList:" + fileItemList);
                String directoryName = dirTrees[treeIndex];
                HVLog.d("directoryName:" + directoryName + "    treeIndex:" + treeIndex + "    dirTrees:" + dirTrees.length);
                String folderId = this.getCloudDiskFolderIdByDirectoryName((List<FileItem>)fileItemList, directoryName);
                if (TextUtils.isEmpty((CharSequence)folderId)) {
                    HVLog.d("创建目录 " + directoryName + "    是在" + folder_id + "下面创建的");
                    this.createPage(folder_id, directoryName).done(create -> {
                        HVLog.d("创建目录成功 " + treeIndex + "    " + dirTrees.length);
                        if (treeIndex < dirTrees.length) {
                            this.openPageByDirectory(dirTrees, folderId, treeIndex + 1);
                        }
                    });
                } else if (treeIndex < dirTrees.length) {
                    this.openPageByDirectory(dirTrees, folderId, treeIndex + 1);
                }
            }
        });
    }

    public void launcherCloud(Context context, final String currentDate, final CloudFileCallback cloudFileCallback) {
        if (this.historyDir.size() == 0) {
            MyCookieJar.resetCookies();
        }
        HttpWorker.Login("18117395833", "w329716228", new HttpWorker.loginCallbackListener(){

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onFinish() {
                HVLog.d("已经成功介入蓝奏云盘 " + ClouddiskLauncher.this.historyDir.size());
                ClouddiskLauncher.this.historyDir.clear();
                String[] dateDir = new String[]{UIConstant.CLOUD_DISK_BACKUP_RECOVERY_DIRECTORY, currentDate};
                ClouddiskLauncher.this.openPageByDirectoryFile(dateDir, UIConstant.CLOUD_DISK_ROOT_ID, 0, cloudFileCallback);
            }
        });
    }

    public void launcherCloudByAppcation(Context context, final CloudFileCallback cloudFileCallback) {
        if (this.historyDir.size() == 0) {
            MyCookieJar.resetCookies();
        }
        HttpWorker.Login("18117395833", "w329716228", new HttpWorker.loginCallbackListener(){

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onFinish() {
                HVLog.d("已经成功介入蓝奏云盘 " + ClouddiskLauncher.this.historyDir.size());
                ClouddiskLauncher.this.historyDir.clear();
                String[] dateDir = new String[]{UIConstant.CLOUD_DISK_BACKUP_APPLICATION_DIRECTORY};
                ClouddiskLauncher.this.openPageByDirectoryFile(dateDir, UIConstant.CLOUD_DISK_ROOT_ID, 0, cloudFileCallback);
            }
        });
    }

    public Promise<List<FileItem>, Throwable, Void> openPageByDirectoryFile(String[] dirTrees, String folder_id, int treeIndex, CloudFileCallback cloudFileCallback) {
        return ResponseProgram.defer().when(() -> {
            this.historyDir.add(folder_id);
            return HttpWorker.getInstance().getFolderInfoSync(folder_id);
        }).done(fileItemList -> {
            if (fileItemList != null) {
                if (treeIndex >= dirTrees.length) {
                    HVLog.d("最后目录情况:" + fileItemList + "    folder_id:" + folder_id);
                    ResponseProgram.defer().when(() -> {
                        try {
                            List<FileItem> fileInfoSync = HttpWorker.getInstance().getFileInfoSync(folder_id);
                            HVLog.d("文件数量： " + fileInfoSync.size());
                            cloudFileCallback.callback(fileInfoSync);
                        }
                        catch (Exception e) {
                            HVLog.printException(e);
                        }
                    });
                    return;
                }
                this.currentFolder = fileItemList;
                HVLog.d("fileItemList:" + fileItemList);
                String directoryName = dirTrees[treeIndex];
                HVLog.d("directoryName:" + directoryName + "    treeIndex:" + treeIndex + "    dirTrees:" + dirTrees.length);
                String folderId = this.getCloudDiskFolderIdByDirectoryName((List<FileItem>)fileItemList, directoryName);
                if (treeIndex < dirTrees.length) {
                    this.openPageByDirectoryFile(dirTrees, folderId, treeIndex + 1, cloudFileCallback);
                }
            }
        });
    }

    public List<FileItem> getCurrentFolder() {
        return this.currentFolder;
    }

    public String getCurrentDateFolderId() {
        String currentDate = ClouddiskLauncher.getCurrentDate();
        String folderId = this.getCloudDiskFolderIdByDirectoryName(this.currentFolder, currentDate);
        return folderId;
    }

    protected Promise<List<FileItem>, Throwable, Void> openPage(String folder_id) {
        HVLog.i("打开目录的 folder_id:" + folder_id);
        this.historyDir.add(folder_id);
        return ResponseProgram.defer().when(() -> {
            try {
                return HttpWorker.getInstance().getFolderInfoSync(folder_id);
            }
            catch (Exception e) {
                HVLog.printException(e);
                return null;
            }
        });
    }

    public Promise<Boolean, Throwable, Void> createPage(String uri, String folder_name) {
        this.historyDir.add(uri);
        return ResponseProgram.defer().when(() -> {
            try {
                return HttpWorker.getInstance().setNewFolderSync(uri, folder_name);
            }
            catch (Exception e) {
                HVLog.printException(e);
                return false;
            }
        });
    }

    public String getCloudDiskFolderIdByDirectoryName(List<FileItem> fileItemList, String directoryName) {
        for (FileItem fileItem : this.currentFolder) {
            HVLog.i("遍历当前目录:" + fileItem.toString());
            if (!directoryName.equals(fileItem.getFilename())) continue;
            return fileItem.getId();
        }
        return null;
    }

    static {
        mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    }

    public static interface CloudFileCallback {
        public void callback(List<FileItem> var1);
    }
}

