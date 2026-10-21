/*
 * Decompiled with CFR 0.152.
 */
package com.bytedance.shadowhook;

public final class ShadowHook {
    private static final int ERRNO_OK = 0;
    private static final int ERRNO_PENDING = 1;
    private static final int ERRNO_UNINIT = 2;
    private static final int ERRNO_LOAD_LIBRARY_EXCEPTION = 100;
    private static final int ERRNO_INIT_EXCEPTION = 101;
    private static boolean inited = false;
    private static int initErrno = 2;
    private static long initCostMs = -1L;
    private static final String libName = "shadowhook";
    private static final ILibLoader defaultLibLoader = null;
    private static final int defaultMode = Mode.SHARED.getValue();
    private static final boolean defaultDebuggable = false;
    private static final boolean defaultRecordable = false;
    private static final int recordItemAll = 1023;
    private static final int recordItemTimestamp = 1;
    private static final int recordItemCallerLibName = 2;
    private static final int recordItemOp = 4;
    private static final int recordItemLibName = 8;
    private static final int recordItemSymName = 16;
    private static final int recordItemSymAddr = 32;
    private static final int recordItemNewAddr = 64;
    private static final int recordItemBackupLen = 128;
    private static final int recordItemErrno = 256;
    private static final int recordItemStub = 512;

    public static String getVersion() {
        return ShadowHook.nativeGetVersion();
    }

    public static int init() {
        return ShadowHook.init(null);
    }

    public static synchronized int init(Config config) {
        if (inited) {
            return initErrno;
        }
        inited = true;
        long start = System.currentTimeMillis();
        if (config == null) {
            config = new ConfigBuilder().build();
        }
        if (!ShadowHook.loadLibrary(config)) {
            initErrno = 100;
            initCostMs = System.currentTimeMillis() - start;
            return initErrno;
        }
        try {
            initErrno = ShadowHook.nativeInit(config.getMode(), config.getDebuggable());
        }
        catch (Throwable ignored) {
            initErrno = 101;
        }
        if (config.getRecordable()) {
            try {
                ShadowHook.nativeSetRecordable(config.getRecordable());
            }
            catch (Throwable ignored) {
                initErrno = 101;
            }
        }
        initCostMs = System.currentTimeMillis() - start;
        return initErrno;
    }

    public static int getInitErrno() {
        return initErrno;
    }

    public static long getInitCostMs() {
        return initCostMs;
    }

    public static Mode getMode() {
        if (ShadowHook.isInitedOk()) {
            return Mode.SHARED.getValue() == ShadowHook.nativeGetMode() ? Mode.SHARED : Mode.UNIQUE;
        }
        return Mode.SHARED;
    }

    public static boolean getDebuggable() {
        if (ShadowHook.isInitedOk()) {
            return ShadowHook.nativeGetDebuggable();
        }
        return false;
    }

    public static void setDebuggable(boolean debuggable) {
        if (ShadowHook.isInitedOk()) {
            ShadowHook.nativeSetDebuggable(debuggable);
        }
    }

    public static boolean getRecordable() {
        if (ShadowHook.isInitedOk()) {
            return ShadowHook.nativeGetRecordable();
        }
        return false;
    }

    public static void setRecordable(boolean recordable) {
        if (ShadowHook.isInitedOk()) {
            ShadowHook.nativeSetRecordable(recordable);
        }
    }

    public static String toErrmsg(int errno) {
        if (errno == 0) {
            return "OK";
        }
        if (errno == 1) {
            return "Pending task";
        }
        if (errno == 2) {
            return "Not initialized";
        }
        if (errno == 100) {
            return "Load libshadowhook.so failed";
        }
        if (errno == 101) {
            return "Init exception";
        }
        if (ShadowHook.isInitedOk()) {
            return ShadowHook.nativeToErrmsg(errno);
        }
        return "Unknown";
    }

    public static String getRecords(RecordItem ... recordItems) {
        if (ShadowHook.isInitedOk()) {
            int itemFlags = 0;
            block12: for (RecordItem recordItem : recordItems) {
                switch (recordItem) {
                    case TIMESTAMP: {
                        itemFlags |= 1;
                        continue block12;
                    }
                    case CALLER_LIB_NAME: {
                        itemFlags |= 2;
                        continue block12;
                    }
                    case OP: {
                        itemFlags |= 4;
                        continue block12;
                    }
                    case LIB_NAME: {
                        itemFlags |= 8;
                        continue block12;
                    }
                    case SYM_NAME: {
                        itemFlags |= 0x10;
                        continue block12;
                    }
                    case SYM_ADDR: {
                        itemFlags |= 0x20;
                        continue block12;
                    }
                    case NEW_ADDR: {
                        itemFlags |= 0x40;
                        continue block12;
                    }
                    case BACKUP_LEN: {
                        itemFlags |= 0x80;
                        continue block12;
                    }
                    case ERRNO: {
                        itemFlags |= 0x100;
                        continue block12;
                    }
                    case STUB: {
                        itemFlags |= 0x200;
                        continue block12;
                    }
                }
            }
            if (itemFlags == 0) {
                itemFlags = 1023;
            }
            return ShadowHook.nativeGetRecords(itemFlags);
        }
        return null;
    }

    public static String getArch() {
        if (ShadowHook.isInitedOk()) {
            return ShadowHook.nativeGetArch();
        }
        return "unknown";
    }

    private static boolean loadLibrary(Config config) {
        try {
            if (config == null || config.getLibLoader() == null) {
                System.loadLibrary(libName);
            } else {
                config.getLibLoader().loadLibrary(libName);
            }
            return true;
        }
        catch (Throwable ignored) {
            return false;
        }
    }

    private static boolean loadLibrary() {
        return ShadowHook.loadLibrary(null);
    }

    private static boolean isInitedOk() {
        if (inited) {
            return initErrno == 0;
        }
        if (!ShadowHook.loadLibrary()) {
            return false;
        }
        try {
            int errno = ShadowHook.nativeGetInitErrno();
            if (errno != 2) {
                initErrno = errno;
                inited = true;
            }
            return errno == 0;
        }
        catch (Throwable ignored) {
            return false;
        }
    }

    private static native String nativeGetVersion();

    private static native int nativeInit(int var0, boolean var1);

    private static native int nativeGetInitErrno();

    private static native int nativeGetMode();

    private static native boolean nativeGetDebuggable();

    private static native void nativeSetDebuggable(boolean var0);

    private static native boolean nativeGetRecordable();

    private static native void nativeSetRecordable(boolean var0);

    private static native String nativeToErrmsg(int var0);

    private static native String nativeGetRecords(int var0);

    private static native String nativeGetArch();

    static /* synthetic */ ILibLoader access$000() {
        return defaultLibLoader;
    }

    static /* synthetic */ int access$100() {
        return defaultMode;
    }

    public static interface ILibLoader {
        public void loadLibrary(String var1);
    }

    public static class Config {
        private ILibLoader libLoader;
        private int mode;
        private boolean debuggable;
        private boolean recordable;

        public void setLibLoader(ILibLoader libLoader) {
            this.libLoader = libLoader;
        }

        public ILibLoader getLibLoader() {
            return this.libLoader;
        }

        public void setMode(int mode) {
            this.mode = mode;
        }

        public int getMode() {
            return this.mode;
        }

        public void setDebuggable(boolean debuggable) {
            this.debuggable = debuggable;
        }

        public boolean getDebuggable() {
            return this.debuggable;
        }

        public void setRecordable(boolean recordable) {
            this.recordable = recordable;
        }

        public boolean getRecordable() {
            return this.recordable;
        }
    }

    public static class ConfigBuilder {
        private ILibLoader libLoader = ShadowHook.access$000();
        private int mode = ShadowHook.access$100();
        private boolean debuggable = false;
        private boolean recordable = false;

        public ConfigBuilder setLibLoader(ILibLoader libLoader) {
            this.libLoader = libLoader;
            return this;
        }

        public ConfigBuilder setMode(Mode mode) {
            this.mode = mode.getValue();
            return this;
        }

        public ConfigBuilder setDebuggable(boolean debuggable) {
            this.debuggable = debuggable;
            return this;
        }

        public ConfigBuilder setRecordable(boolean recordable) {
            this.recordable = recordable;
            return this;
        }

        public Config build() {
            Config config = new Config();
            config.setLibLoader(this.libLoader);
            config.setMode(this.mode);
            config.setDebuggable(this.debuggable);
            config.setRecordable(this.recordable);
            return config;
        }
    }

    public static enum Mode {
        SHARED(0),
        UNIQUE(1);

        private final int value;

        private Mode(int value) {
            this.value = value;
        }

        int getValue() {
            return this.value;
        }
    }

    public static enum RecordItem {
        TIMESTAMP,
        CALLER_LIB_NAME,
        OP,
        LIB_NAME,
        SYM_NAME,
        SYM_ADDR,
        NEW_ADDR,
        BACKUP_LEN,
        ERRNO,
        STUB;

    }
}

