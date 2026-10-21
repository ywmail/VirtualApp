/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Intent
 */
package mirror.android.app;

import android.content.Intent;
import com.lody.virtual.StringFog;
import mirror.MethodParams;
import mirror.RefBoolean;
import mirror.RefClass;
import mirror.RefConstructor;
import mirror.RefInt;
import mirror.RefObject;

public class ServiceStartArgs {
    public static Class<?> TYPE = RefClass.load(ServiceStartArgs.class, "android.app.ServiceStartArgs");
    @MethodParams(value={boolean.class, int.class, int.class, Intent.class})
    public static RefConstructor<Object> ctor;
    public static RefBoolean taskRemoved;
    public static RefInt startId;
    public static RefInt flags;
    public static RefObject<Intent> args;
}

