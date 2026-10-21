/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.text.TextUtils
 *  android.util.ArrayMap
 *  com.alibaba.fastjson.JSON
 *  io.reactivex.functions.Consumer
 *  okhttp3.MediaType
 *  okhttp3.MultipartBody$Part
 *  okhttp3.RequestBody
 *  okhttp3.ResponseBody
 *  okio.Buffer
 *  okio.BufferedSource
 */
package com.kook.network.api;

import android.text.TextUtils;
import android.util.ArrayMap;
import com.alibaba.fastjson.JSON;
import com.kook.common.utils.HVLog;
import com.kook.network.StringFog;
import com.kook.network.api.ApiService;
import com.kook.network.api.HttpManager;
import com.kook.network.creator.FileUploadCreator;
import com.kook.network.exception.ApiException;
import com.kook.network.exception.ErrorAction;
import com.kook.network.file.FileUploadObserver;
import com.kook.network.file.UploadFileRequestBody;
import com.kook.network.secret.MD5Utils;
import com.kook.network.vo.MessageEntity;
import io.reactivex.functions.Consumer;
import java.io.File;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

public class FileUploadService {
    private static ApiService mApiService;
    private static final FileUploadService INSTANCE;

    public static FileUploadService getInstance(String url) {
        mApiService = (ApiService)FileUploadCreator.getRetrofitClient(url).create(ApiService.class);
        return INSTANCE;
    }

    public void uploadDevices(File file, String deviceNo, String uploadVersion, FileUploadObserver<ResponseBody> fileUploadObserver) {
        HashMap<String, RequestBody> params = new HashMap<String, RequestBody>();
        params.put("X-MD5", RequestBody.create((MediaType)MediaType.parse((String)"multipart/form-data"), (String)MD5Utils.fileMD5Sync(file)));
        params.put("deviceNo", RequestBody.create((MediaType)MediaType.parse((String)"multipart/form-data"), (String)deviceNo));
        params.put("uploadVersion", RequestBody.create((MediaType)MediaType.parse((String)"multipart/form-data"), (String)uploadVersion));
        RequestBody requestFile = RequestBody.create((MediaType)MediaType.parse((String)"multipart/form-data"), (File)file);
        MultipartBody.Part body = MultipartBody.Part.createFormData((String)"file", (String)file.getName(), (RequestBody)requestFile);
        mApiService.uploadDevices(body, params).filter(responseBody -> this.handleResponse((ResponseBody)responseBody)).compose(HttpManager.io_main()).doOnError((Consumer)new ErrorAction()).subscribe(fileUploadObserver);
    }

    public void uploadAvatar2(File file, String usercode, FileUploadObserver<ResponseBody> fileUploadObserver) {
        ArrayMap uploadInfo = new ArrayMap();
        UploadFileRequestBody uploadFileRequestBody = new UploadFileRequestBody(file, fileUploadObserver);
        uploadInfo.put("image\"; filename=\"" + file.getName() + "", uploadFileRequestBody);
        if (!TextUtils.isEmpty((CharSequence)usercode)) {
            uploadInfo.put("usercode", RequestBody.create((MediaType)MediaType.parse((String)"text/plain"), (String)usercode.trim()));
        }
        mApiService.uploadAvatar2((Map<String, RequestBody>)uploadInfo).filter(responseBody -> this.handleResponse((ResponseBody)responseBody)).compose(HttpManager.io_main()).doOnError((Consumer)new ErrorAction()).subscribe(fileUploadObserver);
    }

    private boolean handleResponse(ResponseBody responseBody) {
        BufferedSource source = responseBody.source();
        try {
            source.request(Long.MAX_VALUE);
            Buffer buffer = source.buffer();
            Charset charset = Charset.forName("UTF-8");
            MediaType contentType = responseBody.contentType();
            if (contentType != null) {
                charset = contentType.charset(charset);
            }
            String body = buffer.clone().readString(charset);
            HVLog.d("查看返回回来的  body:" + body);
            MessageEntity messageEntity = (MessageEntity)JSON.parseObject((String)body, MessageEntity.class);
            if (messageEntity.getCode() != 4000) {
                throw new ApiException(messageEntity.getMsg(), String.valueOf(messageEntity.getCode()), messageEntity.getCode());
            }
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    static {
        INSTANCE = new FileUploadService();
    }
}

