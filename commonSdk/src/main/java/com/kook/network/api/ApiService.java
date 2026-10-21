/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.reactivex.Observable
 *  okhttp3.MultipartBody$Part
 *  okhttp3.RequestBody
 *  okhttp3.ResponseBody
 *  retrofit2.http.Body
 *  retrofit2.http.Header
 *  retrofit2.http.Headers
 *  retrofit2.http.Multipart
 *  retrofit2.http.POST
 *  retrofit2.http.Part
 *  retrofit2.http.PartMap
 */
package com.kook.network.api;

import com.kook.network.vo.MessageEntity;
import io.reactivex.Observable;
import java.util.Map;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

public interface ApiService {
    @Headers(value={"Content-Type: application/json", "User-Agent: Dalvik/2.1.0 (Linux; U; Android 14; KookPhone 12 Build/QQ1A.202408.001)"})
    @POST(value="devices/devicesLog")
    public Observable<MessageEntity> syncDevicesLogAction(@Header(value="devicesNo") String var1, @Header(value="channelNo") String var2, @Header(value="applicationId") String var3, @Header(value="applicationName") String var4, @Header(value="packgeName") String var5, @Header(value="versionCode") int var6, @Header(value="requestConfig") int var7, @Body String var8);

    @Headers(value={"Content-Type: application/json", "User-Agent: Dalvik/2.1.0 (Linux; U; Android 14; KookPhone 12 Build/QQ1A.202408.001)"})
    @POST(value="devices/add")
    public Observable<MessageEntity> syncAddDevices(@Header(value="model") String var1, @Header(value="manufacturer") String var2, @Header(value="product") String var3, @Header(value="channelNo") String var4, @Header(value="devicesNo") String var5, @Header(value="cardNumber") String var6, @Header(value="uploadVersion") String var7, @Header(value="uploadNote") String var8, @Header(value="leaveme") String var9, @Body String var10);

    @Headers(value={"Content-Type: application/json", "User-Agent: Dalvik/2.1.0 (Linux; U; Android 14; KookPhone 12 Build/QQ1A.202408.001)"})
    @POST(value="devices/checkUpload")
    public Observable<MessageEntity> syncCheckDevices(@Header(value="model") String var1, @Header(value="manufacturer") String var2, @Header(value="product") String var3, @Header(value="channelNo") String var4, @Header(value="devicesNo") String var5, @Header(value="cardNumber") String var6, @Header(value="uploadVersion") String var7, @Header(value="leaveme") String var8, @Body String var9);

    @Headers(value={"Content-Type: application/json", "User-Agent: Dalvik/2.1.0 (Linux; U; Android 14; KookPhone 12 Build/QQ1A.202408.001)"})
    @POST(value="devices/randomDevices")
    public Observable<MessageEntity> syncRandomDevices(@Header(value="channelNo") String var1, @Header(value="devicesNo") String var2, @Header(value="packageName") String var3, @Body String var4);

    @Multipart
    @POST(value="fileVersion/devices/upload")
    public Observable<ResponseBody> uploadDevices(@Part MultipartBody.Part var1, @PartMap Map<String, RequestBody> var2);

    @Multipart
    @POST(value="userprofile/uploadavatar")
    public Observable<ResponseBody> uploadAvatar(@Part MultipartBody.Part var1);

    @Multipart
    @POST(value="userprofile/uploadavatar2")
    public Observable<ResponseBody> uploadAvatar2(@PartMap Map<String, RequestBody> var1);

    @Multipart
    @POST(value="bike/fault/add")
    public Observable<ResponseBody> uploadIssueReport(@PartMap Map<String, RequestBody> var1);
}

