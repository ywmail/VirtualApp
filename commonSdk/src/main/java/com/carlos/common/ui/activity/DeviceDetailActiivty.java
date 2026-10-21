/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.annotation.SuppressLint
 *  android.app.Activity
 *  android.content.ContentResolver
 *  android.content.Context
 *  android.content.Intent
 *  android.net.wifi.WifiManager
 *  android.os.Build
 *  android.os.Bundle
 *  android.provider.Settings$Secure
 *  android.telephony.TelephonyManager
 *  android.text.TextUtils
 *  android.view.Menu
 *  android.view.MenuItem
 *  android.widget.EditText
 *  android.widget.Toast
 *  androidx.annotation.Nullable
 *  androidx.appcompat.app.AlertDialog$Builder
 *  androidx.appcompat.widget.Toolbar
 *  com.kook.librelease.R$id
 *  com.kook.librelease.R$layout
 *  com.kook.librelease.R$menu
 *  com.kook.librelease.R$string
 */
package com.carlos.common.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import com.carlos.common.ui.activity.base.VActivity;
import com.carlos.common.ui.adapter.bean.DeviceData;
import com.carlos.common.widget.MainFunBtn;
import com.kook.common.utils.HVLog;
import com.kook.librelease.R;
import com.kook.librelease.StringFog;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.ipc.VDeviceManager;
import com.lody.virtual.remote.VDeviceConfig;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class DeviceDetailActiivty
extends VActivity {
    private static final String TAG = "DeviceData";
    private int mDeviceID;
    private String mPackageName;
    private String mTitle;
    private int mUserId;
    private int mPosition;
    private VDeviceConfig mDeviceConfig;
    private TelephonyManager mTelephonyManager;
    private WifiManager mWifiManager;
    private EditText edt_androidId;
    private EditText edt_imei;
    private EditText edt_imsi;
    private EditText edt_mac;
    private EditText edt_brand;
    private EditText edt_model;
    private EditText edt_name;
    private EditText edt_device;
    private EditText edt_board;
    private EditText edt_display;
    private EditText edt_id;
    private EditText edt_serial;
    private EditText edt_manufacturer;
    private EditText edt_fingerprint;
    private EditText edt_bluetooth_name;
    MainFunBtn randomData;

    public static void open(Activity context, DeviceData data, int position) {
        Intent intent = new Intent((Context)context, DeviceDetailActiivty.class);
        intent.putExtra("title", data.name);
        intent.putExtra("pkg", data.packageName);
        intent.putExtra("user", data.userId);
        intent.putExtra("pos", position);
        context.startActivityForResult(intent, 1001);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setResult(0);
        this.setContentView(R.layout.activity_mock_device);
        Toolbar toolbar = (Toolbar)this.bind(R.id.top_toolbar);
        this.setSupportActionBar(toolbar);
        this.enableBackHome();
        this.edt_androidId = (EditText)this.findViewById(R.id.edt_androidId);
        this.edt_imei = (EditText)this.findViewById(R.id.edt_imei);
        this.edt_imsi = (EditText)this.findViewById(R.id.edt_imsi);
        this.edt_mac = (EditText)this.findViewById(R.id.edt_mac);
        this.edt_brand = (EditText)this.findViewById(R.id.edt_brand);
        this.edt_model = (EditText)this.findViewById(R.id.edt_model);
        this.edt_name = (EditText)this.findViewById(R.id.edt_name);
        this.edt_device = (EditText)this.findViewById(R.id.edt_device);
        this.edt_board = (EditText)this.findViewById(R.id.edt_board);
        this.edt_display = (EditText)this.findViewById(R.id.edt_display);
        this.edt_id = (EditText)this.findViewById(R.id.edt_id);
        this.edt_serial = (EditText)this.findViewById(R.id.edt_serial);
        this.edt_manufacturer = (EditText)this.findViewById(R.id.edt_manufacturer);
        this.edt_fingerprint = (EditText)this.findViewById(R.id.edt_fingerprint);
        this.edt_bluetooth_name = (EditText)this.findViewById(R.id.edt_bluetooth_name);
        this.mWifiManager = (WifiManager)this.getApplicationContext().getSystemService("wifi");
        this.mTelephonyManager = (TelephonyManager)this.getSystemService("phone");
        if (TextUtils.isEmpty((CharSequence)this.mTitle)) {
            this.mPackageName = this.getIntent().getStringExtra("pkg");
            this.mUserId = this.getIntent().getIntExtra("user", 0);
            this.mTitle = this.getIntent().getStringExtra("title");
        }
        this.mDeviceID = DeviceDetailActiivty.getDeviceId(this.mPackageName, this.mUserId);
        HVLog.d("mDeviceID：" + this.mDeviceID + "   mUserId:" + this.mUserId + "    mPackageName:" + this.mPackageName);
        this.setTitle(this.mTitle);
        this.setTitleColor(-1);
        this.mDeviceConfig = VDeviceManager.get().getDeviceConfig(this.mDeviceID);
        this.updateConfig();
        this.randomData = (MainFunBtn)this.findViewById(R.id.main_fun_btn);
        this.randomData.setTopText("一键新机");
        this.randomData.setOnClickListener(view -> {
            this.mDeviceConfig = VDeviceConfig.random();
            this.updateConfig();
        });
    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        this.mPackageName = intent.getStringExtra("pkg");
        this.mUserId = intent.getIntExtra("user", 0);
        this.mTitle = intent.getStringExtra("title");
        this.mPosition = intent.getIntExtra("pos", -1);
    }

    public boolean onCreateOptionsMenu(Menu menu2) {
        this.getMenuInflater().inflate(R.menu.menu_device, menu2);
        return true;
    }

    private void killApp() {
        if (TextUtils.isEmpty((CharSequence)this.mPackageName)) {
            VirtualCore.get().killAllApps();
        } else {
            VirtualCore.get().killApp(this.mPackageName, this.mUserId);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_save) {
            this.mDeviceConfig.enable = true;
            this.fillConfig();
            this.updateConfig();
            VDeviceManager.get().updateDeviceConfig(this.mDeviceID, this.mDeviceConfig);
            Intent intent = new Intent();
            intent.putExtra("pkg", this.mPackageName);
            intent.putExtra("user", this.mUserId);
            intent.putExtra("pos", this.mPosition);
            intent.putExtra("result", "save");
            this.setResult(-1, intent);
            if (TextUtils.isEmpty((CharSequence)this.mPackageName)) {
                VirtualCore.get().killAllApps();
            } else {
                VirtualCore.get().killApp(this.mPackageName, this.mUserId);
            }
            this.killApp();
            Toast.makeText((Context)this, (CharSequence)"保存成功", (int)0).show();
        } else if (item.getItemId() == R.id.action_reset) {
            new AlertDialog.Builder((Context)this).setMessage(R.string.dlg_reset_device).setPositiveButton(17039370, (dialog, which) -> {
                this.mDeviceConfig.enable = false;
                this.mDeviceConfig.clear();
                VDeviceManager.get().updateDeviceConfig(this.mDeviceID, this.mDeviceConfig);
                Intent intent = new Intent();
                intent.putExtra("pkg", this.mPackageName);
                intent.putExtra("user", this.mUserId);
                intent.putExtra("pos", this.mPosition);
                intent.putExtra("result", "reset");
                this.setResult(-1, intent);
                this.killApp();
                this.updateConfig();
            }).setNegativeButton(0x1040000, (dialog, which) -> dialog.dismiss()).setCancelable(false).show();
        }
        return super.onOptionsItemSelected(item);
    }

    private String getValue(EditText text) {
        return text.getText().toString().trim();
    }

    private void setValue(EditText text, String value, String defValue) {
        if (TextUtils.isEmpty((CharSequence)value)) {
            text.setText((CharSequence)defValue);
            return;
        }
        text.setText((CharSequence)value);
    }

    private void fillConfig() {
        this.mDeviceConfig.setProp("BRAND", this.getValue(this.edt_brand));
        this.mDeviceConfig.setProp("MODEL", this.getValue(this.edt_model));
        this.mDeviceConfig.setProp("PRODUCT", this.getValue(this.edt_name));
        this.mDeviceConfig.setProp("DEVICE", this.getValue(this.edt_device));
        this.mDeviceConfig.setProp("BOARD", this.getValue(this.edt_board));
        this.mDeviceConfig.setProp("DISPLAY", this.getValue(this.edt_display));
        this.mDeviceConfig.setProp("ID", this.getValue(this.edt_id));
        this.mDeviceConfig.setProp("MANUFACTURER", this.getValue(this.edt_manufacturer));
        this.mDeviceConfig.setProp("BluetoothName", this.getValue(this.edt_bluetooth_name));
        this.mDeviceConfig.setProp("FINGERPRINT", this.getValue(this.edt_fingerprint));
        this.mDeviceConfig.serial = this.getValue(this.edt_serial);
        this.mDeviceConfig.deviceId = this.getValue(this.edt_imei);
        this.mDeviceConfig.iccId = this.getValue(this.edt_imsi);
        this.mDeviceConfig.wifiMac = this.getValue(this.edt_mac);
        this.mDeviceConfig.androidId = this.getValue(this.edt_androidId);
    }

    @SuppressLint(value={"HardwareIds", "MissingPermission"})
    private void updateConfig() {
        this.setValue(this.edt_brand, this.mDeviceConfig.getProp("BRAND"), Build.BRAND);
        this.setValue(this.edt_model, this.mDeviceConfig.getProp("MODEL"), Build.MODEL);
        this.setValue(this.edt_name, this.mDeviceConfig.getProp("PRODUCT"), Build.PRODUCT);
        this.setValue(this.edt_device, this.mDeviceConfig.getProp("DEVICE"), Build.DEVICE);
        this.setValue(this.edt_board, this.mDeviceConfig.getProp("BOARD"), Build.BOARD);
        this.setValue(this.edt_display, this.mDeviceConfig.getProp("DISPLAY"), Build.DISPLAY);
        this.setValue(this.edt_id, this.mDeviceConfig.getProp("ID"), Build.ID);
        this.setValue(this.edt_manufacturer, this.mDeviceConfig.getProp("MANUFACTURER"), Build.MANUFACTURER);
        this.setValue(this.edt_fingerprint, this.mDeviceConfig.getProp("FINGERPRINT"), Build.FINGERPRINT + this.mUserId);
        String bluetoothName = Settings.Secure.getString((ContentResolver)this.getContentResolver(), (String)"bluetooth_name");
        this.setValue(this.edt_bluetooth_name, this.mDeviceConfig.getProp("BluetoothName"), bluetoothName);
        this.setValue(this.edt_serial, this.mDeviceConfig.serial, Build.SERIAL);
        try {
            this.setValue(this.edt_imei, this.mDeviceConfig.deviceId, this.mTelephonyManager.getDeviceId());
        }
        catch (Throwable e) {
            this.setValue(this.edt_imei, this.mDeviceConfig.deviceId, "");
        }
        try {
            this.setValue(this.edt_imsi, this.mDeviceConfig.iccId, this.mTelephonyManager.getSimSerialNumber());
        }
        catch (Throwable e) {
            this.setValue(this.edt_imsi, this.mDeviceConfig.iccId, "");
        }
        this.setValue(this.edt_mac, this.mDeviceConfig.wifiMac, this.getDefaultWifiMac());
        this.setValue(this.edt_androidId, this.mDeviceConfig.androidId, Settings.Secure.getString((ContentResolver)this.getContentResolver(), (String)"android_id"));
    }

    @SuppressLint(value={"HardwareIds"})
    private String getDefaultWifiMac() {
        String[] files = new String[]{"/sys/class/net/wlan0/address", "/sys/class/net/eth0/address", "/sys/class/net/wifi/address"};
        String mac = this.mWifiManager.getConnectionInfo().getMacAddress();
        if (TextUtils.isEmpty((CharSequence)mac)) {
            for (String file : files) {
                try {
                    mac = this.readFileAsString(file);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                if (!TextUtils.isEmpty((CharSequence)mac)) break;
            }
        }
        return mac;
    }

    private String readFileAsString(String filePath) throws IOException {
        StringBuilder sb = new StringBuilder(1000);
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        char[] buf = new char[1024];
        int numRead = 0;
        while ((numRead = reader.read(buf)) != -1) {
            String readData = String.valueOf(buf, 0, numRead);
            sb.append(readData);
        }
        reader.close();
        return sb.toString().trim();
    }
}

