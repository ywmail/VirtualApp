//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.kook.deviceinfo.impClasses;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Build;
import android.os.StatFs;
import android.os.SystemClock;
import android.os.Build.VERSION;
import android.provider.Settings;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Size;
import android.view.Display;
import android.view.WindowManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.pm.PackageInfoCompat;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.kook.common.utils.HVLog;
import com.kook.deviceinfo.constant.SystemFileConStant;
import com.kook.deviceinfo.models.ThermalModel;
import com.kook.librelease.R.string;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BuildInfo {
    private final Context context;
    CameraManager cameraManager;
    ArrayList<ThermalModel> thermalList = new ArrayList();

    public BuildInfo(Context context) {
        this.context = context;
        if (VERSION.SDK_INT >= 21) {
            this.cameraManager = (CameraManager)context.getSystemService(Context.CAMERA_SERVICE);
        }

    }

    public String getNetworkName() {
        StringBuilder name = new StringBuilder();
        TelephonyManager telephonyManager = (TelephonyManager)this.context.getSystemService(Context.TELEPHONY_SERVICE);
        if (VERSION.SDK_INT > 22) {
            SubscriptionManager manager = (SubscriptionManager)this.context.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE);
            if (ActivityCompat.checkSelfPermission(this.context, "android.permission.READ_PHONE_STATE") == 0) {
                List<SubscriptionInfo> info = manager.getActiveSubscriptionInfoList();
                if (info != null) {
                    Iterator var5 = info.iterator();

                    while(var5.hasNext()) {
                        SubscriptionInfo subscriptionInfo = (SubscriptionInfo)var5.next();
                        name.append(this.context.getResources().getString(string.netop)).append(" : ").append(subscriptionInfo.getCarrierName()).append("\n");
                    }
                } else {
                    name.append(this.context.getResources().getString(string.netop)).append(" : ").append(this.context.getResources().getString(string.nosim)).append("\n");
                }
            }
        } else if (telephonyManager.getNetworkOperatorName() != null) {
            name.append(this.context.getResources().getString(string.netop)).append(" : ").append(telephonyManager.getNetworkOperatorName()).append("\n");
        }

        return name.toString();
    }

    public String getPhoneType() {
        TelephonyManager manager = (TelephonyManager)this.context.getSystemService(Context.TELEPHONY_SERVICE);
        String devicetype = "";
        switch (manager.getPhoneType()) {
            case 0:
                devicetype = "NONE";
                break;
            case 1:
                devicetype = "GSM";
                break;
            case 2:
                devicetype = "CDMA";
                break;
            case 3:
                devicetype = "SIP";
        }

        return devicetype;
    }

    public String getCpuGovernor() {
        String line;
        try {
            File file = new File(SystemFileConStant.SOC_GOVERNOR);
            BufferedReader reader = new BufferedReader(new FileReader(file));
            line = reader.readLine();
            reader.close();
        } catch (IOException var4) {
            line = this.context.getResources().getString(string.unknown);
            var4.printStackTrace();
        }

        return line;
    }

    public String getCpuDriver() {
        String line;
        try {
            File file = new File(SystemFileConStant.SOC_DRIVER);
            BufferedReader reader = new BufferedReader(new FileReader(file));
            line = reader.readLine();
            reader.close();
        } catch (IOException var4) {
            line = this.context.getResources().getString(string.unknown);
            var4.printStackTrace();
        }

        return line;
    }

    public String getRunningCpuString() {
        StringBuilder cpuBuilder = new StringBuilder();

        for(int i = 0; i < this.getCoresCount(); ++i) {
            cpuBuilder.append("   Core ").append(i).append("       ").append(this.getRunningCpu(i, true)).append("\n\n");
        }

        String totalRunning = cpuBuilder.toString();
        return totalRunning.substring(0, totalRunning.length() - 2);
    }

    public String getUsage() {
        int c = 0;

        for(int i = 0; i < this.getCoresCount(); ++i) {
            c += (int)(this.getCpuUsage(i) * 100.0F);
        }

        return c / this.getCoresCount() + "%";
    }

    public float getCpuUsage(int i) {
        int freq = 0;
        int maxFreq = 0;

        try {
            Process process1 = Runtime.getRuntime().exec("cat /sys/devices/system/cpu/cpu" + i + "/cpufreq/scaling_cur_freq");
            BufferedReader reader1 = new BufferedReader(new InputStreamReader(process1.getInputStream()));
            Process process2 = Runtime.getRuntime().exec("cat /sys/devices/system/cpu/cpu" + i + "/cpufreq/cpuinfo_max_freq");
            BufferedReader reader2 = new BufferedReader(new InputStreamReader(process2.getInputStream()));
            String line1;
            if ((line1 = reader1.readLine()) != null) {
                freq = Integer.parseInt(line1) / 1000;
            }

            String line2;
            if ((line2 = reader2.readLine()) != null) {
                maxFreq = Integer.parseInt(line2) / 1000;
            }

            process1.destroy();
            reader1.close();
            process2.destroy();
            reader2.close();
        } catch (IOException var10) {
            var10.getMessage();
        }

        return maxFreq == 0 ? 0.5F : (float)freq / (float)maxFreq;
    }

    public String getCpuFrequency() {
        int max = Integer.MIN_VALUE;
        int min = Integer.MAX_VALUE;

        for(int i = 0; i < this.getCoresCount(); ++i) {
            try {
                Process process1 = Runtime.getRuntime().exec("cat /sys/devices/system/cpu/cpu" + i + "/cpufreq/cpuinfo_min_freq");
                BufferedReader reader1 = new BufferedReader(new InputStreamReader(process1.getInputStream()));
                Process process2 = Runtime.getRuntime().exec("cat /sys/devices/system/cpu/cpu" + i + "/cpufreq/cpuinfo_max_freq");
                BufferedReader reader2 = new BufferedReader(new InputStreamReader(process2.getInputStream()));
                String line1;
                if ((line1 = reader1.readLine()) != null) {
                    int minFreq = Integer.parseInt(line1) / 1000;
                    if (min >= minFreq) {
                        min = minFreq;
                    }
                }

                String line2;
                if ((line2 = reader2.readLine()) != null) {
                    int maxFreq = Integer.parseInt(line2) / 1000;
                    if (max <= maxFreq) {
                        max = maxFreq;
                    }
                }

                process1.destroy();
                reader1.close();
                process2.destroy();
                reader2.close();
            } catch (IOException var12) {
                var12.printStackTrace();
            }
        }

        if (min != Integer.MAX_VALUE && max != Integer.MIN_VALUE) {
            return min + " Mhz - " + max + " Mhz";
        } else {
            return this.context.getResources().getString(string.unknown);
        }
    }

    @SuppressLint({"DefaultLocale"})
    public String getCpuArchitecture() {
        StringBuilder builder = new StringBuilder();
        int cores = this.getCoresCount();
        Process process;
        BufferedReader reader;
        if (cores <= 4) {
            try {
                process = Runtime.getRuntime().exec("cat /sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq");
                reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                float f = 0.0F;
                String line;
                if ((line = reader.readLine()) != null) {
                    f = Float.parseFloat(line) / 1000000.0F;
                }

                builder.append(cores).append(" x ").append(String.format("%.2f", f)).append(" GHz\n");
            } catch (IOException var11) {
                var11.getMessage();
            }
        } else {
            float[] arr = new float[cores];

            float freq;
            for(int i = 0; i < cores; ++i) {
                try {
                    process = Runtime.getRuntime().exec("cat /sys/devices/system/cpu/cpu" + i + "/cpufreq/cpuinfo_max_freq");
                    reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    String line;
                    if ((line = reader.readLine()) != null) {
                        freq = Float.parseFloat(line) / 1000000.0F;
                        arr[i] = freq;
                    }
                } catch (IOException var10) {
                    var10.getMessage();
                }
            }

            int count = 0;
            freq = 0.0F;

            for(int j = 0; j < arr.length; ++j) {
                if (j == 0) {
                    freq = arr[0];
                    ++count;
                } else if (j >= 1) {
                    if (freq == arr[j]) {
                        ++count;
                    } else {
                        builder.append(count).append(" x ").append(String.format("%.2f", freq)).append(" GHz\n");
                        freq = arr[j];
                        count = 1;
                    }
                }

                if (j == arr.length - 1) {
                    builder.append(count).append(" x ").append(String.format("%.2f", freq)).append(" GHz\n");
                }
            }
        }

        return builder.toString().length() > 0 ? builder.toString().substring(0, builder.toString().length() - 1) : this.context.getResources().getString(string.unknown);
    }

    @SuppressLint({"DefaultLocale"})
    public String getRunningCpu(int i, boolean bool) {
        String frequency = this.context.getResources().getString(string.unknown);

        try {
            Process process = Runtime.getRuntime().exec("cat /sys/devices/system/cpu/cpu" + i + "/cpufreq/scaling_cur_freq");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            if ((line = reader.readLine()) != null) {
                if (bool) {
                    float freq = Float.parseFloat(line) / 1000.0F;
                    frequency = String.format("%.2f", freq) + " MHz";
                } else {
                    int f = Integer.parseInt(line) / 1000;
                    frequency = String.format("%d", f) + " MHz";
                }
            }

            process.destroy();
            reader.close();
        } catch (IOException var9) {
            var9.printStackTrace();
        }

        return frequency;
    }

    public int getCoresCount() {
        int processorCount = 0;

        try {
            Process process = Runtime.getRuntime().exec("cat " + SystemFileConStant.SOC_PRESENT);
            process.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            if ((line = reader.readLine()) != null) {
                processorCount = Integer.parseInt(line.substring(line.indexOf("-") + 1)) + 1;
            }

            process.destroy();
            reader.close();
        } catch (InterruptedException | NumberFormatException | IOException var5) {
            processorCount = Runtime.getRuntime().availableProcessors();
            var5.printStackTrace();
        }

        return processorCount;
    }

    public long getTotalStorageInfo(String path) {
        long t = 10L;

        try {
            StatFs statFs = new StatFs(path);
            t = statFs.getTotalBytes();
        } catch (Exception var5) {
            System.out.println(var5.getMessage() + " " + var5.getCause());
        }

        return t;
    }

    public long getUsedStorageInfo(String path) {
        long u = 10L;

        try {
            StatFs statFs = new StatFs(path);
            u = statFs.getTotalBytes() - statFs.getAvailableBytes();
        } catch (Exception var5) {
            System.out.println(var5.getMessage() + " " + var5.getCause());
        }

        return u;
    }

    public Map<String, String> getZRamInfo() {
        Map<String, String> map = new HashMap();
        File file = new File("proc/meminfo");

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));

            String line;
            while((line = reader.readLine()) != null) {
                String[] str = line.split(":");
                map.put(str[0].trim().toLowerCase(), str[1].trim().replace("kB", "").toLowerCase());
            }

            reader.close();
        } catch (IOException var6) {
            var6.printStackTrace();
        }

        return map;
    }

    public String getBatteryLevel() {
        int pct = 0;
        IntentFilter intentFilter = new IntentFilter("android.intent.action.BATTERY_CHANGED");
        Intent batterystat = this.context.registerReceiver((BroadcastReceiver)null, intentFilter);
        if (batterystat != null) {
            int level = batterystat.getIntExtra("level", -1);
            int scale = batterystat.getIntExtra("scale", -1);
            pct = level * 100 / scale;
        }

        return pct + "%";
    }

    public String getCurrentLevel() {
        int current = 0;
        if (VERSION.SDK_INT >= 21) {
            BatteryManager batteryManager = (BatteryManager)this.context.getSystemService(Context.BATTERY_SERVICE);
            current = batteryManager.getIntProperty(2);
        }

        return -(current / 1000) + " mA";
    }

    public String getBatteryStatus() {
        String batterystatus = null;
        IntentFilter intentFilter = new IntentFilter("android.intent.action.BATTERY_CHANGED");
        Intent batterystat = this.context.registerReceiver((BroadcastReceiver)null, intentFilter);
        int status = 0;
        if (batterystat != null) {
            status = batterystat.getIntExtra("status", -1);
        }

        switch (status) {
            case 1:
                batterystatus = this.context.getResources().getString(string.unknown);
                break;
            case 2:
                batterystatus = this.context.getResources().getString(string.charging);
                break;
            case 3:
                batterystatus = this.context.getResources().getString(string.discharge);
                break;
            case 4:
                batterystatus = this.context.getResources().getString(string.notcharge);
                break;
            case 5:
                batterystatus = this.context.getResources().getString(string.full);
        }

        return batterystatus;
    }

    public String getBatteryHealth() {
        String health = null;
        IntentFilter intentFilter = new IntentFilter("android.intent.action.BATTERY_CHANGED");
        Intent batterystat = this.context.registerReceiver((BroadcastReceiver)null, intentFilter);
        int status = 0;
        if (batterystat != null) {
            status = batterystat.getIntExtra("health", -1);
        }

        switch (status) {
            case 1:
                health = this.context.getResources().getString(string.unknown);
                break;
            case 2:
                health = this.context.getResources().getString(string.good);
                break;
            case 3:
                health = this.context.getResources().getString(string.overheat);
                break;
            case 4:
                health = this.context.getResources().getString(string.dead);
                break;
            case 5:
                health = this.context.getResources().getString(string.overvoltage);
                break;
            case 6:
                health = this.context.getResources().getString(string.unspec_fail);
                break;
            case 7:
                health = this.context.getResources().getString(string.cold);
        }

        return health;
    }

    public String getPowerSource() {
        IntentFilter intentFilter = new IntentFilter("android.intent.action.BATTERY_CHANGED");
        Intent batterystat = this.context.registerReceiver((BroadcastReceiver)null, intentFilter);
        int status = 0;
        if (batterystat != null) {
            status = batterystat.getIntExtra("plugged", -1);
        }

        String source;
        switch (status) {
            case 1:
                source = this.context.getResources().getString(string.ac);
                break;
            case 2:
                source = this.context.getResources().getString(string.usbport);
                break;
            case 3:
            default:
                source = this.context.getResources().getString(string.battery);
                break;
            case 4:
                source = this.context.getResources().getString(string.wireless);
        }

        return source;
    }

    public String getBatteryTechnology() {
        IntentFilter intentFilter = new IntentFilter("android.intent.action.BATTERY_CHANGED");
        Intent batterystat = this.context.registerReceiver((BroadcastReceiver)null, intentFilter);
        String tech = null;
        if (batterystat != null) {
            tech = batterystat.getStringExtra("technology");
        }

        return tech;
    }

    @SuppressLint({"PrivateApi"})
    public String getBatteryCapacity() {
        double capacity = 0.0;

        try {
            Object power = Class.forName("com.android.internal.os.PowerProfile").getConstructor(Context.class).newInstance(this.context);
            capacity = (Double)Class.forName("com.android.internal.os.PowerProfile").getMethod("getBatteryCapacity").invoke(power);
        } catch (InstantiationException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException | NullPointerException | IllegalAccessException var5) {
            var5.printStackTrace();
        }

        return (int)capacity + " mAh";
    }

    public String getVoltage() {
        IntentFilter intentFilter = new IntentFilter("android.intent.action.BATTERY_CHANGED");
        Intent batterystat = this.context.registerReceiver((BroadcastReceiver)null, intentFilter);
        int volt = 0;
        if (batterystat != null) {
            volt = batterystat.getIntExtra("voltage", 0);
        }

        return volt + " mV";
    }

    @SuppressLint({"DefaultLocale"})
    public String getScreenSize() {
        Point point = new Point();
        DisplayMetrics displayMetrics = new DisplayMetrics();

        try {
            Display.class.getMethod("getRealSize", Point.class).invoke(((Activity)this.context).getWindowManager().getDefaultDisplay(), point);
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException var9) {
            var9.printStackTrace();
        }

        ((Activity)this.context).getWindowManager().getDefaultDisplay().getRealMetrics(displayMetrics);
        double x = Math.pow((double)((float)point.x / displayMetrics.xdpi), 2.0);
        double y = Math.pow((double)((float)point.y / displayMetrics.ydpi), 2.0);
        double inches = (double)((float)Math.round(Math.sqrt(x + y) * 10.0) / 10.0F);
        return String.format("%.2f", inches);
    }

    public String getBrightnessMode() {
        String mode = null;
        try {
            switch (Settings.System.getInt((ContentResolver)this.context.getContentResolver(), (String)"screen_brightness_mode")) {
                case 0: {
                    mode = "Manual";
                    break;
                }
                case 1: {
                    mode = "Automatic";
                }
            }
        }
        catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return mode;
    }

    public String getOrientation() {
        String orien = "";
        switch (this.context.getResources().getConfiguration().orientation) {
            case 0:
                orien = this.context.getResources().getString(string.undefined);
                break;
            case 1:
                orien = this.context.getResources().getString(string.portrait);
                break;
            case 2:
                orien = this.context.getResources().getString(string.landscape);
                break;
            case 3:
                orien = this.context.getResources().getString(string.square);
        }

        return orien;
    }

    public String getDensityDpi() {
        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity)this.context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        float density = metrics.density;
        String dpi;
        if (density >= 0.75F && density < 1.0F) {
            dpi = " (LDPI)";
        } else if (density >= 1.0F && density < 1.5F) {
            dpi = " (MDPI)";
        } else if (density >= 1.5F && density <= 2.0F) {
            dpi = " (HDPI)";
        } else if (density > 2.0F && density <= 3.0F) {
            dpi = " (XHDPI)";
        } else if (density >= 3.0F && density < 4.0F) {
            dpi = " (XXHDPI)";
        } else {
            dpi = " (XXXHDPI)";
        }

        return dpi;
    }

    public static int getScreenWidth(Context context) {
        WindowManager windowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    public static int getScreenHeight(Context context) {
        WindowManager windowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    public int getSensorsCount() {
        SensorManager sm = (SensorManager)this.context.getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> sensors = sm.getSensorList(-1);
        return sensors.size();
    }

    public String getRootInfo() {
        return !this.checkRootFiles() && !this.checkTags() ? "No" : "Yes";
    }

    public boolean checkRootFiles() {
        boolean root = false;
        String[] paths = SystemFileConStant.ROOT_FILE;
        String[] var3 = paths;
        int var4 = paths.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            String path = var3[var5];
            root = (new File(path)).exists();
            if (root) {
                break;
            }
        }

        return root;
    }

    public boolean checkTags() {
        String tag = Build.TAGS;
        return tag != null && tag.trim().contains("test-keys");
    }

    @SuppressLint({"PrivateApi"})
    public String getTrebleInfo() {
        String treble = null;

        try {
            Class<?> s = Class.forName("android.os.SystemProperties");
            Method m = s.getMethod("get", String.class);
            treble = (String)m.invoke((Object)null, "ro.treble.enabled");
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | ClassNotFoundException var4) {
            var4.printStackTrace();
        }

        if (VERSION.SDK_INT >= 26) {
            if (treble != null) {
                return treble.equalsIgnoreCase("true") ? this.context.getResources().getString(string.supported) : this.context.getResources().getString(string.not_supported);
            } else {
                return this.context.getResources().getString(string.not_supported);
            }
        } else {
            return this.context.getResources().getString(string.not_supported);
        }
    }

    @SuppressLint({"PrivateApi"})
    public String getSeamlessUpdatesInfo() {
        String updates = null;

        try {
            Class<?> s = Class.forName("android.os.SystemProperties");
            Method m = s.getMethod("get", String.class);
            if (m.invoke((Object)null, "ro.virtual_ab.enabled") == "true" && m.invoke((Object)null, "ro.virtual_ab.retrofit") == "false") {
                updates = "true";
            }

            if (m.invoke((Object)null, "ro.build.ab_update") == "true") {
                updates = "true";
            } else {
                updates = "false";
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | ClassNotFoundException var4) {
            var4.printStackTrace();
        }

        if (VERSION.SDK_INT >= 24) {
            if (updates != null) {
                return updates.equalsIgnoreCase("true") ? this.context.getResources().getString(string.supported) : this.context.getResources().getString(string.not_supported);
            } else {
                return this.context.getResources().getString(string.not_supported);
            }
        } else {
            return this.context.getResources().getString(string.not_supported);
        }
    }

    @SuppressLint({"PrivateApi"})
    public String getSeLinuxInfo() {
        String seLinux = "";

        try {
            Class<?> s = Class.forName("android.os.SystemProperties");
            Method m = s.getMethod("get", String.class);
            seLinux = (String)m.invoke((Object)null, "ro.build.selinux");
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | ClassNotFoundException var4) {
            var4.printStackTrace();
        }

        if (seLinux != null && seLinux.isEmpty()) {
            seLinux = this.context.getResources().getString(string.unable);
        }

        return seLinux;
    }

    @SuppressLint({"DefaultLocale"})
    public String getUpTime() {
        long time = SystemClock.elapsedRealtime();
        long sec = TimeUnit.MILLISECONDS.toSeconds(time) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(time));
        long min = TimeUnit.MILLISECONDS.toMinutes(time) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(time));
        long hour = TimeUnit.MILLISECONDS.toHours(time);
        String uptime = String.format(Locale.getDefault(), "%02d:%02d:%02d", hour, min, sec);
        return uptime;
    }

    public String getCodeName(int level) {
        String[] api_level = new String[]{"Android 4.1 Jelly Bean", "Android 4.2 Jelly Bean", "Android 4.3 Jelly Bean", "Android 4.4 KitKat", "Android 4.4W KitKat", "Android 5.0 Lollipop", "Android 5.1 Lollipop", "Android 6.0 Marshmallow", "Android 7.0 Nougat", "Android 7.1 Nougat", "Android 8.0 Oreo", "Android 8.1.0 Oreo", "Android 9 Pie", "Android 10", "Android 11", "Android 12", "Android 12L Sv2", " Android 13 Tiramisu"};
        return (String)Array.get(api_level, level - 16);
    }

    public String getReleaseDate(int level) {
        Configuration configuration = this.context.getResources().getConfiguration();
        Locale locale = configuration.locale;
        String[] date;
        if (locale != null && locale.getLanguage().equals("zh")) {
            date = new String[]{"2012年7月9日", "2012年11月13日", "2013年7月24日", "2013年10月31日", "2014年6月25日", "2014年11月12日", "2015年3月9日", "2015年10月5日", "2016年8月22日", "2016年10月4日", "2017年8月21日", "2017年12月5日", "2018年8月6日", "2019年9月3日", "2020年9月8日", "2021年10月4日", "2022年3月7日", "2022年10月2日", "2023年4月24日"};
        } else {
            date = new String[]{"July 9, 2012", "November 13, 2012", "July 24, 2013", "October 31, 2013", "June 25, 2014", "November 12, 2014", "March 9, 2015", "October 5, 2015", "August 22, 2016", "October 4, 2016", "August 21, 2017", "December 5, 2017", "August 6, 2018", "September 3, 2019", "September 8, 2020", "October 4, 2021", "March 7, 2022", "October 2, 2022", "April 24, 2023"};
        }

        return (String)Array.get(date, level - 16);
    }

    public String advertId() {
        String adId = this.context.getResources().getString(string.unable);

        try {
            AdvertisingIdClient.Info info = AdvertisingIdClient.getAdvertisingIdInfo(this.context);
            adId = info.getId();
        } catch (GooglePlayServicesNotAvailableException | GooglePlayServicesRepairableException | IOException var4) {
            var4.printStackTrace();
        }

        return adId;
    }

    public String getMacAddress() {
        try {
            List<NetworkInterface> networkInterfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            Iterator var2 = networkInterfaces.iterator();

            while(var2.hasNext()) {
                NetworkInterface ni = (NetworkInterface)var2.next();
                if (ni.getName().equalsIgnoreCase("wlan0")) {
                    byte[] b = ni.getHardwareAddress();
                    StringBuilder builder = new StringBuilder();
                    Log.e("kookmac", "  getMacAddress b " + b);
                    if (b != null) {
                        byte[] var6 = b;
                        int var7 = b.length;

                        for(int var8 = 0; var8 < var7; ++var8) {
                            byte add = var6[var8];
                            builder.append(String.format("%02X:", add));
                        }

                        return builder.deleteCharAt(builder.length() - 1).toString();
                    }
                }
            }
        } catch (SocketException var10) {
            var10.printStackTrace();
        }

        return "02:00:00:00:00";
    }

    public String getPlayServices() {
        String playServices = "";
        if (GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this.context) == 0) {
            try {
                String version = String.valueOf(PackageInfoCompat.getLongVersionCode(this.context.getPackageManager().getPackageInfo("com.google.android.gms", 0)));
                playServices = version.substring(0, 2).concat(".").concat(version.substring(2, 4)).concat(".").concat(version.substring(4, 6));
            } catch (PackageManager.NameNotFoundException var4) {
                var4.printStackTrace();
            }
        } else {
            playServices = "Play Services Not Available";
        }

        return playServices;
    }

    public String afCamera(int facing) {
        StringBuilder afm = new StringBuilder();
        String afModes = null;
        if (VERSION.SDK_INT >= 21) {
            CameraCharacteristics characteristics = null;

            try {
                characteristics = this.cameraManager.getCameraCharacteristics(String.valueOf(facing));
            } catch (Exception var10) {
                var10.printStackTrace();
            }

            if (characteristics != null) {
                String[] af = Arrays.toString((int[])characteristics.get(CameraCharacteristics.CONTROL_AF_AVAILABLE_MODES)).replace("[", "").replace("]", "").split(",");
                String[] var6 = af;
                int var7 = af.length;

                for(int var8 = 0; var8 < var7; ++var8) {
                    String mode = var6[var8];
                    if (mode.trim().contains("0")) {
                        afm.append("Off, ");
                    }

                    if (mode.trim().contains("1")) {
                        afm.append("Auto, ");
                    }

                    if (mode.trim().contains("2")) {
                        afm.append("Macro, ");
                    }

                    if (mode.trim().contains("3")) {
                        afm.append("Continuous Video, ");
                    }

                    if (mode.trim().contains("4")) {
                        afm.append("Continuous Picture, ");
                    }

                    if (mode.trim().contains("5")) {
                        afm.append("EDof, ");
                    }
                }
            }

            if (afm.toString().length() > 0) {
                afModes = afm.toString().substring(0, afm.toString().length() - 2);
            } else {
                afModes = "Not Available";
            }
        }

        return afModes;
    }

    public String abCamera(int facing) {
        StringBuilder abm = new StringBuilder();
        String abModes = null;
        if (VERSION.SDK_INT >= 21) {
            CameraCharacteristics characteristics = null;

            try {
                characteristics = this.cameraManager.getCameraCharacteristics(String.valueOf(facing));
            } catch (Exception var10) {
                var10.printStackTrace();
            }

            if (characteristics != null) {
                String[] ab = Arrays.toString((int[])characteristics.get(CameraCharacteristics.CONTROL_AE_AVAILABLE_ANTIBANDING_MODES)).replace("[", "").replace("]", "").split(",");
                String[] var6 = ab;
                int var7 = ab.length;

                for(int var8 = 0; var8 < var7; ++var8) {
                    String mode = var6[var8];
                    if (mode.trim().contains("0")) {
                        abm.append("Off, ");
                    }

                    if (mode.trim().contains("1")) {
                        abm.append("50Hz, ");
                    }

                    if (mode.trim().contains("2")) {
                        abm.append("60Hz, ");
                    }

                    if (mode.trim().contains("3")) {
                        abm.append("Auto, ");
                    }
                }
            }

            if (abm.toString().length() > 0) {
                abModes = abm.toString().substring(0, abm.toString().length() - 2);
            } else {
                abModes = "Not Available";
            }
        }

        return abModes;
    }

    public String effCamera(int facing) {
        StringBuilder effm = new StringBuilder();
        String effects = null;
        if (VERSION.SDK_INT >= 21) {
            CameraCharacteristics characteristics = null;

            try {
                characteristics = this.cameraManager.getCameraCharacteristics(String.valueOf(facing));
            } catch (Exception var10) {
                var10.printStackTrace();
            }

            if (characteristics != null) {
                String[] eff = Arrays.toString((int[])characteristics.get(CameraCharacteristics.CONTROL_AVAILABLE_EFFECTS)).replace("[", "").replace("]", "").split(",");
                String[] var6 = eff;
                int var7 = eff.length;

                for(int var8 = 0; var8 < var7; ++var8) {
                    String mode = var6[var8];
                    if (mode.trim().contains("0")) {
                        effm.append("Off, ");
                    }

                    if (mode.trim().contains("1")) {
                        effm.append("Mono, ");
                    }

                    if (mode.trim().contains("2")) {
                        effm.append("Negative, ");
                    }

                    if (mode.trim().contains("3")) {
                        effm.append("Solarize, ");
                    }

                    if (mode.trim().contains("4")) {
                        effm.append("Sepia, ");
                    }

                    if (mode.trim().contains("5")) {
                        effm.append("Posterize, ");
                    }

                    if (mode.trim().contains("6")) {
                        effm.append("Whiteboard, ");
                    }

                    if (mode.trim().contains("7")) {
                        effm.append("Blackboard, ");
                    }

                    if (mode.trim().contains("8")) {
                        effm.append("Aqua, ");
                    }
                }
            }

            if (effm.toString().length() > 0) {
                effects = effm.toString().substring(0, effm.toString().length() - 2);
            } else {
                effects = "Not Available";
            }
        }

        return effects;
    }

    public String sceneCamera(int facing) {
        String sceneModes = null;
        StringBuilder scenem = new StringBuilder();
        if (VERSION.SDK_INT >= 21) {
            CameraCharacteristics characteristics = null;

            try {
                characteristics = this.cameraManager.getCameraCharacteristics(String.valueOf(facing));
            } catch (Exception var10) {
                var10.printStackTrace();
            }

            if (characteristics != null) {
                String[] scenes = Arrays.toString((int[])characteristics.get(CameraCharacteristics.CONTROL_AVAILABLE_SCENE_MODES)).replace("[", "").replace("]", "").split(",");
                String[] var6 = scenes;
                int var7 = scenes.length;

                for(int var8 = 0; var8 < var7; ++var8) {
                    String mode = var6[var8];
                    if (mode.trim().contains("0")) {
                        scenem.append("Disabled, ");
                    }

                    if (mode.trim().contains("1")) {
                        scenem.append("Face Priority, ");
                    }

                    if (mode.trim().contains("2")) {
                        scenem.append("Action, ");
                    }

                    if (mode.trim().contains("3")) {
                        scenem.append("Portrait, ");
                    }

                    if (mode.trim().contains("4")) {
                        scenem.append("Landscape, ");
                    }

                    if (mode.trim().contains("5")) {
                        scenem.append("Night, ");
                    }

                    if (mode.trim().contains("6")) {
                        scenem.append("Night Portrait, ");
                    }

                    if (mode.trim().contains("7")) {
                        scenem.append("Theatre, ");
                    }

                    if (mode.trim().contains("8")) {
                        scenem.append("Beach, ");
                    }

                    if (mode.trim().contains("9")) {
                        scenem.append("Snow, ");
                    }

                    if (mode.trim().contains("10")) {
                        scenem.append("Sunset, ");
                    }

                    if (mode.trim().contains("11")) {
                        scenem.append("Steady Photo, ");
                    }

                    if (mode.trim().contains("12")) {
                        scenem.append("FireWorks, ");
                    }

                    if (mode.trim().contains("13")) {
                        scenem.append("Sports, ");
                    }

                    if (mode.trim().contains("14")) {
                        scenem.append("Party, ");
                    }

                    if (mode.trim().contains("15")) {
                        scenem.append("CandleLight, ");
                    }

                    if (mode.trim().contains("16")) {
                        scenem.append("Barcode, ");
                    }
                }
            }

            if (scenem.toString().length() > 0) {
                sceneModes = scenem.toString().substring(0, scenem.toString().length() - 2);
            } else {
                sceneModes = "Not Available";
            }
        }

        return sceneModes;
    }

    public String awbCamera(int facing) {
        StringBuilder awbm = new StringBuilder();
        String awbModes = null;
        if (VERSION.SDK_INT >= 21) {
            CameraCharacteristics characteristics = null;

            try {
                characteristics = this.cameraManager.getCameraCharacteristics(String.valueOf(facing));
            } catch (Exception var10) {
                var10.printStackTrace();
            }

            if (characteristics != null) {
                String[] awb = Arrays.toString((int[])characteristics.get(CameraCharacteristics.CONTROL_AWB_AVAILABLE_MODES)).replace("[", "").replace("]", "").split(",");
                String[] var6 = awb;
                int var7 = awb.length;

                for(int var8 = 0; var8 < var7; ++var8) {
                    String mode = var6[var8];
                    if (mode.trim().contains("0")) {
                        awbm.append("Off, ");
                    }

                    if (mode.trim().contains("1")) {
                        awbm.append("Auto, ");
                    }

                    if (mode.trim().contains("2")) {
                        awbm.append("Incandescent, ");
                    }

                    if (mode.trim().contains("3")) {
                        awbm.append("Fluorescent, ");
                    }

                    if (mode.trim().contains("4")) {
                        awbm.append("Warm Fluorescent, ");
                    }

                    if (mode.trim().contains("5")) {
                        awbm.append("Daylight, ");
                    }

                    if (mode.trim().contains("6")) {
                        awbm.append("Cloudy Daylight, ");
                    }

                    if (mode.trim().contains("7")) {
                        awbm.append("Twilight, ");
                    }

                    if (mode.trim().contains("8")) {
                        awbm.append("Shade, ");
                    }
                }
            }

            if (awbm.toString().length() > 0) {
                awbModes = awbm.toString().substring(0, awbm.toString().length() - 2);
            } else {
                awbModes = "Not Available";
            }
        }

        return awbModes;
    }

    public String hotPixelCamera(int facing) {
        StringBuilder hpm = new StringBuilder();
        String hotPixelModes = null;
        if (VERSION.SDK_INT >= 21) {
            CameraCharacteristics characteristics = null;

            try {
                characteristics = this.cameraManager.getCameraCharacteristics(String.valueOf(facing));
            } catch (Exception var10) {
                var10.printStackTrace();
            }

            if (characteristics != null) {
                String[] hp = Arrays.toString((int[])characteristics.get(CameraCharacteristics.HOT_PIXEL_AVAILABLE_HOT_PIXEL_MODES)).replace("[", "").replace("]", "").split(",");
                String[] var6 = hp;
                int var7 = hp.length;

                for(int var8 = 0; var8 < var7; ++var8) {
                    String mode = var6[var8];
                    if (mode.trim().contains("0")) {
                        hpm.append("Off, ");
                    }

                    if (mode.trim().contains("1")) {
                        hpm.append("Fast, ");
                    }

                    if (mode.trim().contains("2")) {
                        hpm.append("High Quality, ");
                    }
                }
            }

            if (hpm.toString().length() > 0) {
                hotPixelModes = hpm.toString().substring(0, hpm.toString().length() - 2);
            } else {
                hotPixelModes = "Not Available";
            }
        }

        return hotPixelModes;
    }

    public String edgeModesCamera(int facing) {
        StringBuilder edgem = new StringBuilder();
        String edgeModes = null;
        if (VERSION.SDK_INT >= 21) {
            CameraCharacteristics characteristics = null;

            try {
                characteristics = this.cameraManager.getCameraCharacteristics(String.valueOf(facing));
            } catch (Exception var10) {
                var10.printStackTrace();
            }

            if (characteristics != null) {
                String[] ed = Arrays.toString((int[])characteristics.get(CameraCharacteristics.EDGE_AVAILABLE_EDGE_MODES)).replace("[", "").replace("]", "").split(",");
                String[] var6 = ed;
                int var7 = ed.length;

                for(int var8 = 0; var8 < var7; ++var8) {
                    String mode = var6[var8];
                    if (mode.trim().contains("0")) {
                        edgem.append("Off, ");
                    }

                    if (mode.trim().contains("1")) {
                        edgem.append("Fast, ");
                    }

                    if (mode.trim().contains("2")) {
                        edgem.append("High Quality, ");
                    }

                    if (mode.trim().contains("3")) {
                        edgem.append("Zero Shutter Lag, ");
                    }
                }
            }

            if (edgem.toString().length() > 0) {
                edgeModes = edgem.toString().substring(0, edgem.toString().length() - 2);
            } else {
                edgeModes = "Not Available";
            }
        }

        return edgeModes;
    }

    public String videoModesCamera(int facing) {
        StringBuilder vsm = new StringBuilder();
        String videoModes = null;
        if (VERSION.SDK_INT >= 21) {
            CameraCharacteristics characteristics = null;

            try {
                characteristics = this.cameraManager.getCameraCharacteristics(String.valueOf(facing));
            } catch (Exception var10) {
                var10.printStackTrace();
            }

            if (characteristics != null) {
                String[] vs = Arrays.toString((int[])characteristics.get(CameraCharacteristics.CONTROL_AVAILABLE_VIDEO_STABILIZATION_MODES)).replace("[", "").replace("]", "").split(",");
                String[] var6 = vs;
                int var7 = vs.length;

                for(int var8 = 0; var8 < var7; ++var8) {
                    String mode = var6[var8];
                    if (mode.trim().contains("0")) {
                        vsm.append("Off, ");
                    }

                    if (mode.trim().contains("1")) {
                        vsm.append("On, ");
                    }
                }
            }

            if (vsm.toString().length() > 0) {
                videoModes = vsm.toString().substring(0, vsm.toString().length() - 2);
            } else {
                videoModes = "Not Available";
            }
        }

        return videoModes;
    }

    public String camCapCamera(int facing) {
        StringBuilder camcapm = new StringBuilder();
        String capabilities = null;
        if (VERSION.SDK_INT >= 21) {
            CameraCharacteristics characteristics = null;

            try {
                characteristics = this.cameraManager.getCameraCharacteristics(String.valueOf(facing));
            } catch (Exception var10) {
                var10.printStackTrace();
            }

            if (characteristics != null) {
                String[] camcap = Arrays.toString((int[])characteristics.get(CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES)).replace("[", "").replace("]", "").split(",");
                String[] var6 = camcap;
                int var7 = camcap.length;

                for(int var8 = 0; var8 < var7; ++var8) {
                    String mode = var6[var8];
                    if (mode.trim().contains("0")) {
                        camcapm.append("Backward Compatible, ");
                    }

                    if (mode.trim().contains("1")) {
                        camcapm.append("Manual Sensor, ");
                    }

                    if (mode.trim().contains("2")) {
                        camcapm.append("Manual Post Processing, ");
                    }

                    if (mode.trim().contains("3")) {
                        camcapm.append("RAW, ");
                    }

                    if (mode.trim().contains("4")) {
                        camcapm.append("Private Reprocessing, ");
                    }

                    if (mode.trim().contains("5")) {
                        camcapm.append("Read Sensor Settings, ");
                    }

                    if (mode.trim().contains("6")) {
                        camcapm.append("Burst Capture, ");
                    }

                    if (mode.trim().contains("7")) {
                        camcapm.append("YUV Reprocessing, ");
                    }

                    if (mode.trim().contains("8")) {
                        camcapm.append("Depth Output, ");
                    }

                    if (mode.trim().contains("9")) {
                        camcapm.append("High Speed Video, ");
                    }

                    if (mode.trim().contains("10")) {
                        camcapm.append("Motion Tracking, ");
                    }

                    if (mode.trim().contains("11")) {
                        camcapm.append("Logical Multi Camera, ");
                    }

                    if (mode.trim().contains("12")) {
                        camcapm.append("Monochrome, ");
                    }

                    if (mode.trim().contains("13")) {
                        camcapm.append("Secure Image Data, ");
                    }
                }
            }

            if (camcapm.toString().length() > 0) {
                capabilities = camcapm.toString().substring(0, camcapm.toString().length() - 2);
            } else {
                capabilities = "Not Available";
            }
        }

        return capabilities;
    }

    public String testModesCamera(int facing) {
        StringBuilder tpm = new StringBuilder();
        String testPattern = null;
        if (VERSION.SDK_INT >= 21) {
            CameraCharacteristics characteristics = null;

            try {
                characteristics = this.cameraManager.getCameraCharacteristics(String.valueOf(facing));
            } catch (Exception var10) {
                var10.printStackTrace();
            }

            if (characteristics != null) {
                String[] tp = Arrays.toString((int[])characteristics.get(CameraCharacteristics.SENSOR_AVAILABLE_TEST_PATTERN_MODES)).replace("[", "").replace("]", "").split(",");
                String[] var6 = tp;
                int var7 = tp.length;

                for(int var8 = 0; var8 < var7; ++var8) {
                    String mode = var6[var8];
                    if (mode.trim().contains("0")) {
                        tpm.append("Off, ");
                    }

                    if (mode.trim().contains("1")) {
                        tpm.append("Solid Color, ");
                    }

                    if (mode.trim().contains("2")) {
                        tpm.append("Color Bars, ");
                    }

                    if (mode.trim().contains("3")) {
                        tpm.append("Color Bars Fade to Gray, ");
                    }

                    if (mode.trim().contains("4")) {
                        tpm.append("PN9, ");
                    }

                    if (mode.trim().contains("256")) {
                        tpm.append("Custom1, ");
                    }
                }
            }

            if (tpm.toString().length() > 0) {
                testPattern = tpm.toString().substring(0, tpm.toString().length() - 2);
            } else {
                testPattern = "Not Available";
            }
        }

        return testPattern;
    }

    public String aeCamera(int facing) {
        StringBuilder aem = new StringBuilder();
        String aeModes = null;
        if (VERSION.SDK_INT >= 21) {
            CameraCharacteristics characteristics = null;

            try {
                characteristics = this.cameraManager.getCameraCharacteristics(String.valueOf(facing));
            } catch (Exception var10) {
                var10.printStackTrace();
            }

            if (characteristics != null) {
                String[] ae = Arrays.toString((int[])characteristics.get(CameraCharacteristics.CONTROL_AE_AVAILABLE_MODES)).replace("[", "").replace("]", "").split(",");
                String[] var6 = ae;
                int var7 = ae.length;

                for(int var8 = 0; var8 < var7; ++var8) {
                    String mode = var6[var8];
                    if (mode.trim().contains("0")) {
                        aem.append("Off, ");
                    }

                    if (mode.trim().contains("1")) {
                        aem.append("On, ");
                    }

                    if (mode.trim().contains("2")) {
                        aem.append("Auto Flash, ");
                    }

                    if (mode.trim().contains("3")) {
                        aem.append("Always Flash, ");
                    }

                    if (mode.trim().contains("4")) {
                        aem.append("Auto Flash Red-Eye, ");
                    }

                    if (mode.trim().contains("5")) {
                        aem.append("External Flash, ");
                    }
                }
            }

            if (aem.toString().length() > 0) {
                aeModes = aem.toString().substring(0, aem.toString().length() - 2);
            } else {
                aeModes = "Not Available";
            }
        }

        return aeModes;
    }

    public String fdCamera(int facing) {
        StringBuilder fdm = new StringBuilder();
        String faceDetectModes = null;
        if (VERSION.SDK_INT >= 21) {
            CameraCharacteristics characteristics = null;

            try {
                characteristics = this.cameraManager.getCameraCharacteristics(String.valueOf(facing));
            } catch (Exception var10) {
                var10.printStackTrace();
            }

            if (characteristics != null) {
                String[] fd = Arrays.toString((int[])characteristics.get(CameraCharacteristics.STATISTICS_INFO_AVAILABLE_FACE_DETECT_MODES)).replace("[", "").replace("]", "").split(",");
                String[] var6 = fd;
                int var7 = fd.length;

                for(int var8 = 0; var8 < var7; ++var8) {
                    String mode = var6[var8];
                    if (mode.trim().contains("0")) {
                        fdm.append("Off, ");
                    }

                    if (mode.trim().contains("1")) {
                        fdm.append("Simple, ");
                    }

                    if (mode.trim().contains("2")) {
                        fdm.append("Full, ");
                    }
                }
            }

            if (fdm.toString().length() > 0) {
                faceDetectModes = fdm.toString().substring(0, fdm.toString().length() - 2);
            } else {
                faceDetectModes = "Not Available";
            }
        }

        return faceDetectModes;
    }

    public String amCamera(int facing) {
        StringBuilder am = new StringBuilder();
        String aberrationMode = null;
        if (VERSION.SDK_INT >= 21) {
            CameraCharacteristics characteristics = null;

            try {
                characteristics = this.cameraManager.getCameraCharacteristics(String.valueOf(facing));
            } catch (Exception var10) {
                var10.printStackTrace();
            }

            if (characteristics != null) {
                String[] aberrationModes = Arrays.toString((int[])characteristics.get(CameraCharacteristics.COLOR_CORRECTION_AVAILABLE_ABERRATION_MODES)).replace("[", "").replace("]", "").split(",");
                String[] var6 = aberrationModes;
                int var7 = aberrationModes.length;

                for(int var8 = 0; var8 < var7; ++var8) {
                    String mode = var6[var8];
                    if (mode.trim().contains("0")) {
                        am.append("Off, ");
                    }

                    if (mode.trim().contains("1")) {
                        am.append("Fast, ");
                    }

                    if (mode.trim().contains("2")) {
                        am.append("High Quality, ");
                    }
                }
            }

            if (am.toString().length() > 0) {
                aberrationMode = am.toString().substring(0, am.toString().length() - 2);
            } else {
                aberrationMode = "Not Available";
            }
        }

        return aberrationMode;
    }

    public String osCamera(int facing) {
        StringBuilder osm = new StringBuilder();
        String opticalStable = null;
        if (VERSION.SDK_INT >= 21) {
            CameraCharacteristics characteristics = null;

            try {
                characteristics = this.cameraManager.getCameraCharacteristics(String.valueOf(facing));
            } catch (Exception var10) {
                var10.printStackTrace();
            }

            if (characteristics != null) {
                String[] os = Arrays.toString((int[])characteristics.get(CameraCharacteristics.LENS_INFO_AVAILABLE_OPTICAL_STABILIZATION)).replace("[", "").replace("]", "").split(",");
                String[] var6 = os;
                int var7 = os.length;

                for(int var8 = 0; var8 < var7; ++var8) {
                    String mode = var6[var8];
                    if (mode.trim().contains("0")) {
                        osm.append("Off, ");
                    }

                    if (mode.trim().contains("1")) {
                        osm.append("On, ");
                    }
                }
            }

            if (osm.toString().length() > 0) {
                opticalStable = osm.toString().substring(0, osm.toString().length() - 2);
            } else {
                opticalStable = "Not Available";
            }
        }

        return opticalStable;
    }

    @SuppressLint({"DefaultLocale"})
    public String resolutionsCamera(int facing) {
        StringBuilder srm = new StringBuilder();
        String resolutions = null;
        if (VERSION.SDK_INT >= 21) {
            try {
                CameraCharacteristics characteristics = this.cameraManager.getCameraCharacteristics(String.valueOf(facing));
                StreamConfigurationMap streamConfigurationMap = (StreamConfigurationMap)characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                Size[] sizes = streamConfigurationMap.getOutputSizes(32);
                Size[] var7;
                int var8;
                int var9;
                Size size;
                if (sizes != null) {
                    var7 = sizes;
                    var8 = sizes.length;

                    for(var9 = 0; var9 < var8; ++var9) {
                        size = var7[var9];
                        srm.append(String.format("%.2f", (double)(size.getWidth() * size.getHeight()) / 1000000.0)).append(" MP - ").append(size.getWidth()).append(" x ").append(size.getHeight()).append("\n");
                    }
                }

                sizes = streamConfigurationMap.getOutputSizes(256);
                if (sizes != null) {
                    var7 = sizes;
                    var8 = sizes.length;

                    for(var9 = 0; var9 < var8; ++var9) {
                        size = var7[var9];
                        srm.append(String.format("%.2f", (double)(size.getWidth() * size.getHeight()) / 1000000.0)).append(" MP - ").append(size.getWidth()).append(" x ").append(size.getHeight()).append("\n");
                    }
                }

                if (srm.length() > 0) {
                    resolutions = srm.toString().substring(0, srm.toString().length() - 1);
                }
            } catch (Exception var11) {
                var11.printStackTrace();
            }
        }

        return resolutions;
    }

    public String getIp4Address() {
        String a = this.context.getResources().getString(string.connect_to_net);

        try {
            List<NetworkInterface> networkInterfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            Iterator var3 = networkInterfaces.iterator();

            while(var3.hasNext()) {
                NetworkInterface intF = (NetworkInterface)var3.next();
                List<InterfaceAddress> interfaceAddresses = intF.getInterfaceAddresses();
                Iterator var6 = interfaceAddresses.iterator();

                while(var6.hasNext()) {
                    InterfaceAddress interfaceAddress = (InterfaceAddress)var6.next();
                    if (!interfaceAddress.getAddress().isLoopbackAddress() && interfaceAddress.getAddress() instanceof Inet4Address) {
                        if (interfaceAddress.getNetworkPrefixLength() % 8 == 0) {
                            a = interfaceAddress.getAddress().getHostAddress();
                        } else if (a.equalsIgnoreCase(this.context.getResources().getString(string.connect_to_net))) {
                            a = interfaceAddress.getAddress().getHostAddress();
                        }
                    }
                }
            }
        } catch (SocketException var8) {
            var8.printStackTrace();
        }

        return a;
    }

    public String getIp6Address() {
        String a = this.context.getResources().getString(string.connect_to_net);

        try {
            List<NetworkInterface> networkInterfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            Iterator var3 = networkInterfaces.iterator();

            while(var3.hasNext()) {
                NetworkInterface intF = (NetworkInterface)var3.next();
                List<InterfaceAddress> interfaceAddresses = intF.getInterfaceAddresses();
                Iterator var6 = interfaceAddresses.iterator();

                while(var6.hasNext()) {
                    InterfaceAddress interfaceAddress = (InterfaceAddress)var6.next();
                    if (!interfaceAddress.getAddress().isLoopbackAddress() && interfaceAddress.getAddress() instanceof Inet6Address) {
                        a = interfaceAddress.getAddress().getHostAddress();
                    }
                }
            }
        } catch (SocketException var8) {
            var8.printStackTrace();
        }

        if (a.contains("%")) {
            int index = a.indexOf("%");
            a = a.substring(0, index);
        }

        return a;
    }

    public String subnet() {
        int x = 0;

        try {
            List<NetworkInterface> networkInterfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            Iterator var3 = networkInterfaces.iterator();

            label68:
            while(true) {
                while(true) {
                    if (!var3.hasNext()) {
                        break label68;
                    }

                    NetworkInterface intF = (NetworkInterface)var3.next();
                    List<InterfaceAddress> interfaceAddresses = intF.getInterfaceAddresses();
                    Iterator var6 = interfaceAddresses.iterator();

                    while(var6.hasNext()) {
                        InterfaceAddress interfaceAddress = (InterfaceAddress)var6.next();
                        if (!interfaceAddress.getAddress().isLoopbackAddress() && interfaceAddress.getAddress() instanceof Inet4Address) {
                            if (interfaceAddress.getNetworkPrefixLength() % 8 == 0) {
                                x = interfaceAddress.getNetworkPrefixLength();
                                break;
                            }

                            x = interfaceAddress.getNetworkPrefixLength();
                        }
                    }
                }
            }
        } catch (SocketException var8) {
            var8.printStackTrace();
        }

        StringBuilder stringBuilder = new StringBuilder();

        for(int i = 0; i < 4; ++i) {
            int bitValue = 0;
            if (x > 0) {
                int v;
                if (x >= 8) {
                    v = 0;
                } else {
                    v = x % 8;
                }

                if (v == 0) {
                    stringBuilder.append("255.");
                } else {
                    for(int j = 7; v > 0; --v) {
                        bitValue = (int)((double)bitValue + Math.pow(2.0, (double)j));
                        --j;
                    }

                    stringBuilder.append(bitValue).append(".");
                }

                x -= 8;
            } else {
                stringBuilder.append("0.");
            }
        }

        return stringBuilder.toString().substring(0, stringBuilder.toString().length() - 1);
    }

    public String getNetworkG(int type) {
        switch (type) {
            case 1:
            case 2:
            case 4:
            case 7:
            case 11:
                return "2G";
            case 3:
            case 5:
            case 6:
            case 8:
            case 9:
            case 10:
            case 12:
            case 14:
            case 15:
                return "3G";
            case 13:
                return "4G LTE";
            case 16:
            case 17:
            case 18:
            case 19:
            default:
                return "Unknown";
            case 20:
                return "5G";
        }
    }

    public String getAxis(int num) {
        String axis = null;
        switch (num) {
            case 0:
                axis = "AXIS_X";
                break;
            case 1:
                axis = "AXIS_Y";
                break;
            case 2:
                axis = "AXIS_PRESSURE";
                break;
            case 3:
                axis = "AXIS_SIZE";
                break;
            case 4:
                axis = "AXIS_TOUCH_MAJOR";
                break;
            case 5:
                axis = "AXIS_TOUCH_MINOR";
                break;
            case 6:
                axis = "AXIS_TOOL_MAJOR";
                break;
            case 7:
                axis = "AXIS_TOOL_MINOR";
                break;
            case 8:
                axis = "AXIS_ORIENTATION";
                break;
            case 9:
                axis = "AXIS_VSCROLL";
                break;
            case 10:
                axis = "AXIS_HSCROLL";
                break;
            case 11:
                axis = "AXIS_Z";
                break;
            case 12:
                axis = "AXIS_RX";
                break;
            case 13:
                axis = "AXIS_RY";
                break;
            case 14:
                axis = "AXIS_RZ";
                break;
            case 15:
                axis = "AXIS_HAT_X";
                break;
            case 16:
                axis = "AXIS_HAT_Y";
                break;
            case 17:
                axis = "AXIS_LTRIGGER";
                break;
            case 18:
                axis = "AXIS_RTRIGGER";
                break;
            case 19:
                axis = "AXIS_THROTTLE";
                break;
            case 20:
                axis = "AXIS_RUDDER";
                break;
            case 21:
                axis = "AXIS_WHEEL";
                break;
            case 22:
                axis = "AXIS_GAS";
                break;
            case 23:
                axis = "AXIS_BRAKE";
                break;
            case 24:
                axis = "AXIS_DISTANCE";
                break;
            case 25:
                axis = "AXIS_TILT";
                break;
            case 26:
                axis = "AXIS_SCROLL";
                break;
            case 27:
                axis = "AXIS_RELATIVE_X";
                break;
            case 28:
                axis = "AXIS_RELATIVE_Y";
            case 29:
            case 30:
            case 31:
            default:
                break;
            case 32:
                axis = "AXIS_GENERIC_1";
                break;
            case 33:
                axis = "AXIS_GENERIC_2";
                break;
            case 34:
                axis = "AXIS_GENERIC_3";
                break;
            case 35:
                axis = "AXIS_GENERIC_4";
                break;
            case 36:
                axis = "AXIS_GENERIC_5";
                break;
            case 37:
                axis = "AXIS_GENERIC_6";
                break;
            case 38:
                axis = "AXIS_GENERIC_7";
                break;
            case 39:
                axis = "AXIS_GENERIC_8";
                break;
            case 40:
                axis = "AXIS_GENERIC_9";
                break;
            case 41:
                axis = "AXIS_GENERIC_10";
                break;
            case 42:
                axis = "AXIS_GENERIC_11";
                break;
            case 43:
                axis = "AXIS_GENERIC_12";
                break;
            case 44:
                axis = "AXIS_GENERIC_13";
                break;
            case 45:
                axis = "AXIS_GENERIC_14";
                break;
            case 46:
                axis = "AXIS_GENERIC_15";
                break;
            case 47:
                axis = "AXIS_GENERIC_16";
        }

        return axis;
    }

    public String getWifiChannel(int frequency) {
        byte i;
        switch (frequency) {
            case 2412:
                i = 1;
                break;
            case 2417:
                i = 2;
                break;
            case 2422:
                i = 3;
                break;
            case 2427:
                i = 4;
                break;
            case 2432:
                i = 5;
                break;
            case 2437:
                i = 6;
                break;
            case 2442:
                i = 7;
                break;
            case 2447:
                i = 8;
                break;
            case 2452:
                i = 9;
                break;
            case 2457:
                i = 10;
                break;
            case 2462:
                i = 11;
                break;
            case 2467:
                i = 12;
                break;
            case 2472:
                i = 13;
                break;
            case 2484:
                i = 14;
                break;
            default:
                return this.context.getResources().getString(string.unable);
        }

        return String.valueOf(i);
    }

    public ArrayList<ThermalModel> getThermalList() {
        return this.thermalList;
    }

    @SuppressLint({"PrivateApi"})
    public String getSimState() {
        TelephonyManager telephonyManager = (TelephonyManager)this.context.getSystemService("phone");
        String state = "Unknown";
        switch (telephonyManager.getSimState()) {
            case 0:
                state = "Unknown";
                break;
            case 1:
                state = "Absent";
                break;
            case 2:
                state = "PIN Required";
                break;
            case 3:
                state = "PUK Required";
                break;
            case 4:
                state = "Locked";
                break;
            case 5:
                state = "Ready";
                break;
            case 6:
                state = "Not Ready";
                break;
            case 7:
                state = "Permanently Disabled";
                break;
            case 8:
                state = "Card IO Error";
                break;
            case 9:
                state = "Card Restricted";
        }

        return state;
    }

    public Map<String, String> getAndroidProperty() {
        BufferedReader bufferedReader = null;
        String pattern = "\\[(.*?)\\]: \\[(.*?)\\]";
        Pattern compiledPattern = Pattern.compile(pattern);
        Map<String, String> property = new HashMap();

        try {
            Process process = Runtime.getRuntime().exec("getprop");
            bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            int index = 1;

            String line;
            while((line = bufferedReader.readLine()) != null) {
                ++index;
                System.out.println(line);
                Matcher matcher = compiledPattern.matcher(line);
                if (matcher.find()) {
                    String key = matcher.group(1);
                    String value = matcher.group(2);
                    property.put(key, value);
                } else {
                    HVLog.d("ro属性 在" + line + "没有发现匹配");
                }
            }

            HVLog.d("一共有" + index + "个RO属性");
            return property;
        } catch (IOException var18) {
            throw new RuntimeException(var18);
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException var17) {
                var17.printStackTrace();
            }

        }
    }

    public Map<String, String> getJavaProperties() {
        Properties properties = System.getProperties();
        Set<String> propertyKeys = properties.stringPropertyNames();
        Map<String, String> property = new HashMap();
        Iterator var4 = propertyKeys.iterator();

        while(var4.hasNext()) {
            String key = (String)var4.next();
            String value = properties.getProperty(key);
            property.put(key, value);
        }

        return property;
    }

    public Map<String, String> settingsProperty(Uri uri) {
        Map<String, String> property = new HashMap();
        ContentResolver resolver = this.context.getContentResolver();
        Cursor cursor = resolver.query(uri, (String[])null, (String)null, (String[])null, (String)null);
        if (cursor != null) {
            while(true) {
                if (!cursor.moveToNext()) {
                    cursor.close();
                    break;
                }

                String key = cursor.getString(cursor.getColumnIndex("name"));
                String value = cursor.getString(cursor.getColumnIndex("value"));
                property.put(key, value);
            }
        }

        return property;
    }
}
