/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.res.AssetManager
 */
package mirror.android.content.res;

import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefConstructor;
import mirror.RefMethod;

public class AssetManager {
    public static Class<?> TYPE = RefClass.load(AssetManager.class, android.content.res.AssetManager.class);
    public static RefConstructor<android.content.res.AssetManager> ctor;
    @MethodParams(value={String.class})
    public static RefMethod<Integer> addAssetPath;
}

