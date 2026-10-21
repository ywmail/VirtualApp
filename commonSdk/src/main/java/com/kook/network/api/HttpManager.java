/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  io.reactivex.Observable
 *  io.reactivex.ObservableTransformer
 *  io.reactivex.android.schedulers.AndroidSchedulers
 *  io.reactivex.schedulers.Schedulers
 */
package com.kook.network.api;

import android.content.Context;
import com.kook.common.utils.HVLog;
import com.kook.network.StringFog;
import com.kook.network.api.ApiService;
import com.kook.network.creator.RequestCreator;
import com.kook.network.secret.CipherUtil;
import com.kook.network.vo.MessageEntity;
import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class HttpManager {
    private static ApiService mApiService;
    DateTimeFormatter formatter;
    ZonedDateTime zonedDateTime;
    private final String decryptPrefix = "Enc[";
    private final String decryptSuffix = "]";

    public static HttpManager getInstance(Context context, String baseUrl) {
        mApiService = (ApiService)RequestCreator.getRetrofitClient(context, baseUrl).create(ApiService.class);
        return InstanceHolder.INSTANCE;
    }

    public String getDate(long time) {
        if (this.formatter == null || this.zonedDateTime == null) {
            ZoneId beijingZoneId = ZoneId.of("Asia/Shanghai");
            this.zonedDateTime = ZonedDateTime.now(beijingZoneId);
            this.formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        }
        String formattedDateTime = this.zonedDateTime.format(this.formatter);
        return formattedDateTime;
    }

    public static <T> ObservableTransformer<T, T> io_main() {
        return tObservable -> tObservable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    private String comboEncryptPrefix(String encrtptContent) {
        return this.decryptPrefix + encrtptContent + this.decryptSuffix;
    }

    public Observable<MessageEntity> syncLogOrConfigAction(String devicesNo, String channelNo, String applicationId, String applicationName, String packgeName, int versionCode, boolean isRequestConfigUrl, String content) {
        String encrypt = this.comboEncryptPrefix(CipherUtil.encrypt("Hhj2024.08.06-07", content));
        int requestConfig = isRequestConfigUrl ? 1 : 0;
        HVLog.d("devicesNo:" + devicesNo + "   channelNo:" + channelNo + "    applicationId:" + applicationId + "    applicationName:" + applicationName + "     packgeName:" + packgeName + "    versionCode:" + versionCode + "    content:" + content);
        return mApiService.syncDevicesLogAction(devicesNo, channelNo, applicationId, applicationName, packgeName, versionCode, requestConfig, encrypt).compose(HttpManager.io_main());
    }

    public Observable<MessageEntity> syncAddDevices(String model, String manufacturer, String product, String channelNo, String devicesNo, String cardNumber, String uploadVersion, String uploadNote, String leaveme, String content) {
        String encrypt = this.comboEncryptPrefix(CipherUtil.encrypt("Hhj2024.08.06-07", content));
        HVLog.d("model:" + model + "   manufacturer:" + manufacturer + "    product:" + product + "    channelNo:" + channelNo + "     devicesNo:" + devicesNo + "    cardNumber:" + cardNumber + "    leaveme:" + leaveme + "    content:" + content);
        return mApiService.syncAddDevices(model, manufacturer, product, channelNo, devicesNo, cardNumber, uploadVersion, uploadNote, leaveme, encrypt).compose(HttpManager.io_main());
    }

    public Observable<MessageEntity> syncCheckDevices(String model, String manufacturer, String product, String channelNo, String devicesNo, String cardNumber, String uploadVersion, String leaveme, String content) {
        String encrypt = this.comboEncryptPrefix(CipherUtil.encrypt("Hhj2024.08.06-07", content));
        HVLog.d("model:" + model + "   manufacturer:" + manufacturer + "    product:" + product + "    channelNo:" + channelNo + "     devicesNo:" + devicesNo + "    cardNumber:" + cardNumber + "   uploadVersion" + uploadVersion + "    leaveme:" + leaveme + "    content:" + content);
        return mApiService.syncCheckDevices(model, manufacturer, product, channelNo, devicesNo, cardNumber, uploadVersion, leaveme, encrypt).compose(HttpManager.io_main());
    }

    public Observable<MessageEntity> syncRandomDevices(String channelNo, String devicesNo, String packageName, String content) {
        String encrypt = this.comboEncryptPrefix(CipherUtil.encrypt("Hhj2024.08.06-07", content));
        HVLog.d("    channelNo:" + channelNo + "     devicesNo:" + devicesNo + "    packageName:" + packageName + "    content:" + content);
        return mApiService.syncRandomDevices(channelNo, devicesNo, packageName, encrypt).compose(HttpManager.io_main());
    }

    public static class InstanceHolder {
        private static final HttpManager INSTANCE = new HttpManager();
    }

    public static enum REQUEST_METHOD {
        RANDOM_DEVICES,
        CHECK_DEVICES,
        ADD_DEVICES,
        DEVICES_LOG_ACTION;

    }
}

