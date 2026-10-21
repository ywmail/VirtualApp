/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.Environment
 *  android.text.TextUtils
 *  androidx.annotation.RequiresApi
 *  com.alibaba.fastjson.JSON
 *  com.alibaba.fastjson.JSONObject
 *  io.reactivex.functions.Consumer
 *  okhttp3.ResponseBody
 */
package com.carlos.common.network;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import androidx.annotation.RequiresApi;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.carlos.common.device.DeviceInfo;
import com.carlos.common.network.StringFog;
import com.carlos.common.persistent.StoragePersistenceServices;
import com.carlos.common.persistent.VPersistent;
import com.kook.common.utils.HVLog;
import com.kook.network.Constant;
import com.kook.network.api.FileUploadService;
import com.kook.network.api.HttpManager;
import com.kook.network.exception.ErrorAction;
import com.kook.network.file.FileDownloadService;
import com.kook.network.file.FileUploadObserver;
import com.kook.network.file.IDownloadListener;
import com.kook.network.secret.MD5Utils;
import io.reactivex.functions.Consumer;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import okhttp3.ResponseBody;

public class VNetworkManagerService {
    public static String SERVICE_NAME = "network_sync";
    public static final int oneHourMillis = 360000;
    public static final int oneDayMillis = 86400000;
    public Context mContext;
    private static VNetworkManagerService sInstance = new VNetworkManagerService();
    long devicelogTime = 0L;

    private VNetworkManagerService() {
    }

    public static VNetworkManagerService get() {
        return sInstance;
    }

    public void systemReady(Context context) {
        this.mContext = context;
    }

    @RequiresApi(api=26)
    public void devicesLog() {
        StoragePersistenceServices storagePersistenceServices = StoragePersistenceServices.get();
        if (storagePersistenceServices == null) {
            return;
        }
        VPersistent persistent = storagePersistenceServices.getVPersistent();
        boolean isRequestConfigUrl = this.configRequestCan(storagePersistenceServices, persistent);
        String hostUrl = isRequestConfigUrl ? Constant.API_URL.ENV_PROD : persistent.getBuildConfig("url_host");
        String env_config = persistent.getBuildConfig(VPersistent.PRODUCT_ENV_KEY);
        if ("sit".equals(env_config)) {
            hostUrl = Constant.API_URL.ENV_DEV;
        }
        HVLog.d("查看 devicesLog hostUrl:" + hostUrl + "   env_config:" + env_config + "    isRequestConfigUrl:" + isRequestConfigUrl);
        DeviceInfo deviceInfo = DeviceInfo.getInstance(this.mContext);
        String deviceNo = deviceInfo.getDevicesNo();
        String channelNo = deviceInfo.getChannelNo();
        String softId = deviceInfo.getSoftId(this.mContext);
        String applicationName = deviceInfo.getApplicationName();
        String packgeName = this.mContext.getPackageName();
        int versionCode = deviceInfo.getVersionCode();
        long currentTimestamp = deviceInfo.getCurrentTimestamp();
        if (System.currentTimeMillis() - this.devicelogTime < 5000L) {
            HVLog.d("devicesLog 访问过于频繁");
            this.devicelogTime = System.currentTimeMillis();
            return;
        }
        HttpManager.getInstance(this.mContext, hostUrl).syncLogOrConfigAction(deviceNo, channelNo, softId, applicationName, packgeName, versionCode, isRequestConfigUrl, String.valueOf(currentTimestamp)).subscribe(messageEntity -> {
            if (messageEntity.getCode() == 4004) {
                HVLog.d(" 表示服务端没有该设备需要添加该设备 4004 ");
            } else if (messageEntity.getCode() == 4015) {
                String md5;
                JSONObject jsonObject;
                HVLog.d(" 请求 4015 ");
                String config = messageEntity.getData();
                HVLog.e("应用需要升级的数据信息:" + config);
                this.persistentConfig(storagePersistenceServices, messageEntity.getData(), persistent);
                String downloadUrl = persistent.getBuildConfig("download_app_url");
                if (downloadUrl != null && !downloadUrl.endsWith("/")) {
                    downloadUrl = downloadUrl + "/";
                }
                String fileName = (jsonObject = JSON.parseObject((String)messageEntity.getData())).containsKey((Object)"fileName") ? jsonObject.getString("fileName") : null;
                String string2 = md5 = jsonObject.containsKey((Object)"fileMd5") ? jsonObject.getString("fileMd5") : null;
                if (!TextUtils.isEmpty((CharSequence)fileName) && !TextUtils.isEmpty((CharSequence)md5)) {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("devicesNo", deviceNo);
                    headers.put("channelNo", channelNo);
                    headers.put("packgeName", packgeName);
                    String localApk = this.mContext.getFilesDir().getAbsolutePath() + "/Download/" + fileName;
                    File apkFile = new File(localApk);
                    String fileMD5Sync = MD5Utils.fileMD5Sync(apkFile);
                    if (apkFile.exists() && md5.equals(fileMD5Sync)) {
                        HVLog.d(fileName + " 已经存在,不需要download        apkFile:" + apkFile.exists() + "   localApk:" + localApk);
                    } else {
                        apkFile.deleteOnExit();
                        this.downloadFile(downloadUrl + fileName, headers, fileName, md5);
                    }
                }
            } else if (messageEntity.getCode() == 4016) {
                HVLog.d(" 请求 4016 " + messageEntity.getData());
                this.persistentConfig(storagePersistenceServices, messageEntity.getData(), persistent);
            } else if (messageEntity.getCode() == 4017) {
                HVLog.d("请求 4017 ");
            }
            if (isRequestConfigUrl) {
                storagePersistenceServices.updatePersistent(persistent);
            }
        }, (Consumer)new ErrorAction());
        HVLog.d("devicesLog");
    }

    @RequiresApi(api=26)
    public void checkDevicesUpload(String filePath) {
        File kookCustom = new File("/system/kook");
        if (kookCustom.exists()) {
            HVLog.d("kook 定制rom");
            return;
        }
        StoragePersistenceServices storagePersistenceServices = StoragePersistenceServices.get();
        if (storagePersistenceServices == null) {
            return;
        }
        VPersistent persistent = storagePersistenceServices.getVPersistent();
        String urlHost = persistent.getBuildConfig("url_host");
        if (TextUtils.isEmpty((CharSequence)urlHost)) {
            this.devicesLog();
            return;
        }
        String hostUrl = persistent.getBuildConfig("url_host");
        HVLog.d("查看checkDevicesUpload hostUrl:" + hostUrl);
        DeviceInfo deviceInfo = DeviceInfo.getInstance(this.mContext);
        String model = deviceInfo.model;
        String manufacturer = deviceInfo.manufacturer;
        String product = deviceInfo.product;
        String channelNo = deviceInfo.getChannelNo();
        String devicesNo = deviceInfo.getDevicesNo();
        String cardNumber = "";
        String uploadVersion = Constant.UPLOAD_VERSION_V2_0;
        String uploadNote = "";
        String leaveme = "";
        long currentTimestamp = deviceInfo.getCurrentTimestamp();
        HttpManager.getInstance(this.mContext, hostUrl).syncCheckDevices(model, manufacturer, product, channelNo, devicesNo, cardNumber, uploadVersion, leaveme, String.valueOf(currentTimestamp)).subscribe(messageEntity -> {
            HVLog.d("请求成功:" + messageEntity);
            if (messageEntity.getCode() == 4004) {
                HVLog.d("upload file:" + messageEntity + " " + persistent.getBuildConfig("upload_devices_url"));
                this.uploadDevices(persistent, filePath);
            }
        }, (Consumer)new ErrorAction());
    }

    private void persistentConfig(StoragePersistenceServices storagePersistenceServices, String data, VPersistent persistent) {
        if (!TextUtils.isEmpty(data)) {
            JSONObject jsonObject = JSON.parseObject(data);
            Set<Map.Entry<String, Object>> entries = jsonObject.entrySet();
            Iterator var6 = entries.iterator();

            while(var6.hasNext()) {
                Map.Entry<String, Object> entry = (Map.Entry)var6.next();
                String key = (String)entry.getKey();
                Object value = entry.getValue();
                persistent.setBuildConfig(key, String.valueOf(value));
            }
        }

        try {
            storagePersistenceServices.updatePersistent(persistent);
        } catch (Exception var10) {
            HVLog.printException(var10);
        }

    }

    private boolean configRequestCan(StoragePersistenceServices storagePersistenceServices, VPersistent persistent) {
        block8: {
            block7: {
                if (TextUtils.isEmpty((CharSequence)persistent.getBuildConfig("url_host"))) break block7;
                if (TextUtils.isEmpty((CharSequence)persistent.getBuildConfig("upload_app_url"))) break block7;
                if (TextUtils.isEmpty((CharSequence)persistent.getBuildConfig("download_app_url"))) break block7;
                if (TextUtils.isEmpty((CharSequence)persistent.getBuildConfig("upload_devices_url"))) break block7;
                if (!TextUtils.isEmpty((CharSequence)persistent.getBuildConfig("download_devices_url"))) break block8;
            }
            return true;
        }
        HVLog.d("requestCount:" + persistent.requestCount);
        int heartbeatCount = 0;
        String heartbeatCountStr = persistent.getBuildConfig("heartbeatCount");
        if (!TextUtils.isEmpty((CharSequence)heartbeatCountStr)) {
            heartbeatCount = Integer.parseInt(heartbeatCountStr);
        }
        HVLog.d("requestCount:" + persistent.requestCount + "    heartbeatCount:" + heartbeatCount);
        if (persistent.requestCount >= 20 || persistent.requestCount >= heartbeatCount) {
            HVLog.d("超过50次的心跳还是需要去请求服务器 " + persistent.requestCount);
            persistent.requestCount = 0;
            this.updatePersistent(storagePersistenceServices, persistent);
            return true;
        }
        ++persistent.requestCount;
        String requestTimeAppConfig = persistent.getBuildConfig("requestTime");
        HVLog.d("超过 requestTimeAppConfig 的时间还是需要去请求服务器 " + requestTimeAppConfig);
        if (TextUtils.isEmpty((CharSequence)requestTimeAppConfig)) {
            return true;
        }
        int requestConfig = Integer.parseInt(requestTimeAppConfig);
        int requestTime = 360000 * requestConfig;
        if (System.currentTimeMillis() - persistent.currentTimeMillis > (long)requestTime) {
            HVLog.d("超过心跳再次请求");
            persistent.currentTimeMillis = System.currentTimeMillis();
            this.updatePersistent(storagePersistenceServices, persistent);
            return true;
        }
        this.updatePersistent(storagePersistenceServices, persistent);
        return false;
    }

    public void updatePersistent(StoragePersistenceServices storagePersistenceServices, VPersistent persistent) {
        storagePersistenceServices.updatePersistent(persistent);
    }

    @RequiresApi(api=26)
    public void addDeviceByRemote(VPersistent persistent) {
        DeviceInfo deviceInfo = DeviceInfo.getInstance(this.mContext);
        String model = deviceInfo.model;
        String manufacturer = deviceInfo.manufacturer;
        String product = deviceInfo.product;
        String channelNo = deviceInfo.getChannelNo();
        String devicesNo = deviceInfo.getDevicesNo();
        String cardNumber = "";
        String uploadVersion = Constant.UPLOAD_VERSION_V2_0;
        String uploadNote = null;
        try {
            uploadNote = URLEncoder.encode("va发型的第一个版本", "UTF-8");
        }
        catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        String leaveme = "";
        long currentTimestamp = deviceInfo.getCurrentTimestamp();
        String urlHost = persistent.getBuildConfig("url_host");
        HttpManager.getInstance(this.mContext, urlHost).syncAddDevices(model, manufacturer, product, channelNo, devicesNo, cardNumber, uploadVersion, uploadNote, leaveme, String.valueOf(currentTimestamp)).subscribe(messageEntity -> HVLog.d("请求成功:" + messageEntity), (Consumer)new ErrorAction());
    }

    public void uploadDevices(final VPersistent persistent, String uploadFile) {
        String baseUrl = persistent.getBuildConfig("url_host");
        if (baseUrl != null && !baseUrl.endsWith("/")) {
            baseUrl = baseUrl + "/";
        }
        HVLog.d("需要上传Devices信息,这里的BaseUrl:" + baseUrl);
        DeviceInfo deviceInfo = DeviceInfo.getInstance(this.mContext);
        String deviceNo = deviceInfo.getDevicesNo();
        String uploadVersion = Constant.UPLOAD_VERSION_V2_0;
        String uploadNote = "";
        FileUploadService.getInstance(baseUrl).uploadDevices(new File(uploadFile), deviceNo, uploadVersion, new FileUploadObserver<ResponseBody>(){

            @Override
            public void onUpLoadSuccess() {
                HVLog.d("文件onUpLoadSuccess 上传成功");
                VNetworkManagerService.this.addDeviceByRemote(persistent);
            }

            @Override
            @RequiresApi(api=26)
            public void onUpLoadNext(ResponseBody responseBody) {
                HVLog.d("文件 onUpLoadNext 上传成功");
            }

            @Override
            public void onUpLoadFail(Throwable e) {
                HVLog.d("文件上传失败");
            }

            @Override
            public void onProgress(int progress) {
                HVLog.d("文件上传progress:" + progress);
            }
        });
    }

    @RequiresApi(api=26)
    public void randomDevices() {
        StoragePersistenceServices storagePersistenceServices = StoragePersistenceServices.get();
        if (storagePersistenceServices == null) {
            return;
        }
        VPersistent persistent = storagePersistenceServices.getVPersistent();
        String hostUrl = persistent.getBuildConfig("url_host");
        if (TextUtils.isEmpty((CharSequence)hostUrl)) {
            this.devicesLog();
            return;
        }
        HVLog.d("查看 randomDevices hostUrl:" + hostUrl);
        DeviceInfo deviceInfo = DeviceInfo.getInstance(this.mContext);
        String deviceNo = deviceInfo.getDevicesNo();
        String channelNo = deviceInfo.getChannelNo();
        String packgeName = this.mContext.getPackageName();
        long currentTimestamp = deviceInfo.getCurrentTimestamp();
        HttpManager.getInstance(this.mContext, hostUrl).syncRandomDevices(channelNo, deviceNo, packgeName, String.valueOf(currentTimestamp)).subscribe(messageEntity -> {
            HVLog.d("随机返回一个devices 信息返回数据:" + messageEntity);
            if (messageEntity != null && messageEntity.getCode() == 4020) {
                String md5;
                JSONObject jsonObject;
                String downloadUrl = persistent.getBuildConfig("download_devices_url");
                if (downloadUrl != null && !downloadUrl.endsWith("/")) {
                    downloadUrl = downloadUrl + "/";
                }
                String fileName = (jsonObject = JSON.parseObject((String)messageEntity.getData())).containsKey((Object)"fileName") ? jsonObject.getString("fileName") : null;
                String string2 = md5 = jsonObject.containsKey((Object)"md5") ? jsonObject.getString("md5") : null;
                if (!TextUtils.isEmpty((CharSequence)fileName) && !TextUtils.isEmpty((CharSequence)md5)) {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("devicesNo", deviceNo);
                    headers.put("channelNo", channelNo);
                    headers.put("packgeName", packgeName);
                    this.downloadFile(downloadUrl + fileName, headers, fileName, md5);
                }
            }
        }, (Consumer)new ErrorAction());
        HVLog.d("randomDevices");
    }

    public void downloadFile(String downloadUrl, Map<String, String> headers, String fileName, final String fileMd5) {
        HVLog.d("文件下载 downloadUrl：" + downloadUrl + "    fileName:" + fileName + "    fileMd5：" + fileMd5);
        if (Environment.getExternalStorageState().equals("mounted")) {
            String fileDirPath = this.mContext.getFilesDir().getAbsolutePath() + "/Download/";
            File fileDir = new File(fileDirPath);
            try {
                if (null == fileDir || !fileDir.exists()) {
                    fileDir.mkdir();
                }
                final String filePath = fileDirPath + fileName;
                FileDownloadService.getInstance(new IDownloadListener(){

                    @Override
                    public void onDownloadSuccess() {
                        String fileMD5Sync = MD5Utils.fileMD5Sync(filePath);
                        HVLog.d("fileMd5:" + fileMd5 + "    fileMD5Sync:" + fileMD5Sync);
                        if (fileMD5Sync.equals(fileMd5)) {
                            HVLog.d("文件下载成功");
                        } else {
                            HVLog.d("文件下载异常,MD5 校验失败");
                        }
                    }

                    @Override
                    public void onDownloadFail(Exception exception) {
                        HVLog.d("文件下载失败");
                        HVLog.printException(exception);
                    }

                    @Override
                    public void onProgress(int progress) {
                        HVLog.d("文件下载进度:" + progress);
                    }
                }).download(downloadUrl, headers, filePath);
            }
            catch (Exception e) {
                e.printStackTrace();
                HVLog.printException(e);
            }
        } else {
            HVLog.d("SD卡未挂载");
        }
    }
}

