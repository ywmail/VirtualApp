/*
 * Decompiled with CFR 0.152.
 */
package com.kook.deviceinfo.constant;

import java.util.ArrayList;
import java.util.List;

public class SystemFileConStant {
    public static List<String> SOC_FILE_LIST = new ArrayList<String>();
    public static String SOC_VENDOR = "/sys/devices/soc0/vendor";
    public static String SOC_MACHINE = "/sys/devices/soc0/machine";
    public static String SOC_FAMILY = "/sys/devices/soc0/family";
    public static String SOC_GOVERNOR = "/sys/devices/system/cpu/cpu0/cpufreq/scaling_governor";
    public static String SOC_DRIVER = "/sys/devices/system/cpu/cpu0/cpufreq/scaling_driver";
    public static String SOC_PRESENT = "/sys/devices/system/cpu/present";
    public static String[] ROOT_FILE = new String[]{"/system/app/Superuser.apk", "/sbin/su", "/system/bin/su", "/system/xbin/su", "/data/local/xbin/su", "/data/local/bin/su", "/system/sd/xbin/su", "/system/bin/failsafe/su", "/data/local/su", "/su/bin/su"};
    public static String SOC_CPU_INFO = "/proc/cpuinfo";
    public static String SOC_CPU_ID = "/sys/devices/soc0/soc_id";
    public static String SOC_CPU_CORE = "/sys/devices/system/cpu/online";
    public static String SOC_CPU_CORE1 = "/sys/devices/system/cpu/cpu1/online";
    public static String SOC_CPU_CURFREQQ = "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_cur_freq";
    public static String SELINUX_ENABLE = "/sys/fs/selinux/enforce";

    static {
        SOC_FILE_LIST.add(SOC_CPU_INFO);
        SOC_FILE_LIST.add(SOC_VENDOR);
        SOC_FILE_LIST.add(SOC_MACHINE);
        SOC_FILE_LIST.add(SOC_FAMILY);
        SOC_FILE_LIST.add(SOC_GOVERNOR);
        SOC_FILE_LIST.add(SOC_DRIVER);
        SOC_FILE_LIST.add(SOC_PRESENT);
        SOC_FILE_LIST.add(SOC_CPU_ID);
        SOC_FILE_LIST.add(SOC_CPU_CORE);
        SOC_FILE_LIST.add(SOC_CPU_CORE1);
        SOC_FILE_LIST.add(SOC_CPU_CURFREQQ);
        SOC_FILE_LIST.add(SELINUX_ENABLE);
        SOC_FILE_LIST.add("/proc/sys/kernel/random/uuid");
        SOC_FILE_LIST.add("/proc/sys/kernel/random/boot_id");
        SOC_FILE_LIST.add("/sys/block/mmcblk0/device/cid");
        SOC_FILE_LIST.add("/sys/devices/soc0/serial_number");
        SOC_FILE_LIST.add("/proc/misc");
        SOC_FILE_LIST.add("/proc/version");
        SOC_FILE_LIST.add("/sys/block/mmcblk0/device/serial");
        SOC_FILE_LIST.add(" /proc/bus/input/devices");
    }
}

