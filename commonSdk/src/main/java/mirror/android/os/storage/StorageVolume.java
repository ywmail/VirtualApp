/*
 * Decompiled with CFR 0.152.
 */
package mirror.android.os.storage;

import com.lody.virtual.StringFog;
import java.io.File;
import mirror.RefClass;
import mirror.RefMethod;
import mirror.RefObject;

public class StorageVolume {
    public static Class<?> TYPE = RefClass.load(StorageVolume.class, "android.os.storage.StorageVolume");
    public static RefObject<File> mPath;
    public static RefObject<File> mInternalPath;
    public static RefObject<String> mDescription;
    public static RefMethod<String> getPath;
    public static RefMethod<File> getPathFile;
}

