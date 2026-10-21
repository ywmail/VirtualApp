/*
 * Decompiled with CFR 0.152.
 */
package com.kook.deviceinfo.constant;

public class BuildInfoConstant {

    public static class SystemMediaDrm {
        public static String TYPE = SystemMediaDrm.class.getSimpleName();
    }

    public static class SystemAvailableFeatures {
        public static String TYPE = SystemAvailableFeatures.class.getSimpleName();
    }

    public static class SettingsSecureProperty {
        public static String TYPE = SettingsSecureProperty.class.getSimpleName();
    }

    public static class SettingsSystemProperty {
        public static String TYPE = SettingsSystemProperty.class.getSimpleName();
    }

    public static class SettingsGlobalProperty {
        public static String TYPE = SettingsGlobalProperty.class.getSimpleName();
    }

    public static class JavaProperty {
        public static String TYPE = JavaProperty.class.getSimpleName();
    }

    public static class SystemInputDevice {
        public static String TYPE = SystemInputDevice.class.getSimpleName();
    }

    public static class SystemMediaCodecList {
        public static String TYPE = SystemMediaCodecList.class.getSimpleName();
    }

    public static class SystemProperty {
        public static String TYPE = SystemProperty.class.getSimpleName();
    }

    public static class GLExtensions {
        public static String TYPE = GLExtensions.class.getSimpleName();
        public static String ORIENTATION = "";
    }

    public static class Codecs {
        public static String TYPE = Camera.class.getSimpleName();
    }

    public static class InputDevices {
        public static String TYPE = Camera.class.getSimpleName();
    }

    public static class Thermal {
        public static String TYPE = Camera.class.getSimpleName();
    }

    public static class Camera {
        public static String TYPE = Camera.class.getSimpleName();
        public static String CAMERA_IDS = "camera_ids";
    }

    public static class StorageSpace {
        public static String TYPE = StorageSpace.class.getSimpleName();
        public static String MEMORY_INFO_TOTALMEM = "memory_totalMem";
        public static String MEMORY_INFO_USEMEM = "memory_usedMem";
        public static String MEMORY_INFO_AVAILMEM = "memory_availMem";
        public static String SYSTEM_INFO_TOTALMEM = "system_totalMem";
        public static String SYSTEM_INFO_USEMEM = "system_usedMem";
        public static String SYSTEM_INFO_AVAILMEM = "system_availMem";
        public static String INTERNAL_INFO_TOTALMEM = "internal_totalMem";
        public static String INTERNAL_INFO_USEMEM = "internal_usedMem";
        public static String INTERNAL_INFO_AVAILMEM = "internal_availMem";
        public static String EXTERNAL_INFO_TOTALMEM = "external_totalMem";
        public static String EXTERNAL_INFO_USEMEM = "external_usedMem";
        public static String EXTERNAL_INFO_AVAILMEM = "external_availMem";
    }

    public static class Display {
        public static String TYPE = Display.class.getSimpleName();
        public static String RESOLUTION = "display_resolution";
        public static String DENSITY = "display_density";
        public static String FONTSCALE = "display_fontscale";
        public static String SIZE = "display_size";
        public static String REFRESHRATE = "display_refreshrate";
        public static String HDR = "display_hdr";
        public static String HDR_CAP = "display_hdr_cap";
        public static String BRIGHT_LEVEL = "display_bright_level";
        public static String BRIGHT_MODE = "display_bright_mode";
        public static String SCREEN_TIMEOUT = "display_screen_timeout";
        public static String ORIENTATION = "display_orientation";
    }

    public static class Battery {
        public static String TYPE = Battery.class.getSimpleName();
        public static String HEALTH = "battery_health";
        public static String STATUS = "battery_status";
        public static String LEVEL = "battery_level";
        public static String VOLTAGE = "battery_voltage";
        public static String POWER_SOURCE = "battery_powerSource";
        public static String TECHNOLOGY = "battery_technology";
        public static String TEMPERATURE = "battery_temperature";
        public static String CAPACITY = "battery_capacity";
    }

    public static class SocCpu {
        public static String TYPE = SocCpu.class.getSimpleName();
        public static String PROC_CPUINFO_FILE = "proc_cpuinfo";
        public static String CPU_PRESENT_FILE = "cpu_present";
        public static String CPU_GOVERNOR_FILE = "cpu_governor";
        public static String CPU_DRIVER_FILE = "cpu_driver";
        public static String CPU_MACHINE_FILE = "cpu_machine";
        public static String CPU_FAMILY_FILE = "cpu_family";
        public static String CPU_VENDOR_FILE = "cpu_vendor";
    }

    public static class InputInfo {
        public static String TYPE = InputInfo.class.getSimpleName();
    }

    public static class SensorInfo {
        public static String TYPE = SensorInfo.class.getSimpleName();
    }

    public static class UserAgent {
        public static String TYPE = UserAgent.class.getSimpleName();
    }

    public static class GeneralDataInfo {
        public static String TYPE = GeneralDataInfo.class.getSimpleName();
        public static String LANGUAGE = "DisplayLanguage";
        public static String OS_ARCH = "os_arch";
        public static String SUPPORTED_ABIS = "supported_adis";
        public static String BT_INFO = "bt_info";
    }

    public static class SystemAppInfo {
        public static String TYPE = SystemFileInfo.class.getSimpleName();
    }

    public static class SystemFileInfo {
        public static String TYPE = SystemFileInfo.class.getSimpleName();
    }

    public static class FingerprintInfo {
        public static String TYPE = FingerprintInfo.class.getSimpleName();
    }

    public static class Graphics {
        public static String TYPE = Graphics.class.getSimpleName();
        public static String GL_ES_VERSION = "GlEsVersion";
    }

    public static class Telephony {
        public static String TYPE = Telephony.class.getSimpleName();
        public static String TELEPHONY_TYPE = "TELEPHONY";
    }

    public static class Build {
        public static String TYPE = Build.class.getSimpleName();
        public static String BUILD_MODEL = "MODEL";
        public static String BUILD_MANUFACTURER = "MANUFACTURER";
        public static String BUILD_BRAND = "BRAND";
        public static String BUILD_DEVICE = "DEVICE";
        public static String BUILD_BOARD = "BOARD";
        public static String BUILD_HARDWARE = "HARDWARE";
        public static String BUILD_FINGERPRINT = "FINGERPRINT";
        public static String BUILD_BOOTLOADER = "BOOTLOADER";
        public static String BUILD_DISPLAY = "DISPLAY";
        public static String BUILD_SECURITY_PATCH = "SECURITY_PATCH";
        public static String BUILD_TAGS = "TAGS";
    }
}

