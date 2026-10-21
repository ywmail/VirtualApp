/*
 * Decompiled with CFR 0.152.
 */
package android.os;

import com.lody.virtual.StringFog;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.ResultStaticMethodProxy;
import mirror.android.os.IStatsManagerService;

public class StatsManagerServiceStub
extends BinderInvocationProxy {
    private static final String SERVER_NAME = "statsmanager";

    public StatsManagerServiceStub() {
        super(IStatsManagerService.Stub.asInterface, "statsmanager");
    }

    @Override
    protected void onBindMethods() {
        super.onBindMethods();
        this.addMethodProxy(new ResultStaticMethodProxy("setDataFetchOperation", null));
        this.addMethodProxy(new ResultStaticMethodProxy("removeDataFetchOperation", null));
        this.addMethodProxy(new ResultStaticMethodProxy("setActiveConfigsChangedOperation", new long[0]));
        this.addMethodProxy(new ResultStaticMethodProxy("removeActiveConfigsChangedOperation", null));
        this.addMethodProxy(new ResultStaticMethodProxy("setBroadcastSubscriber", null));
        this.addMethodProxy(new ResultStaticMethodProxy("unsetBroadcastSubscriber", null));
        this.addMethodProxy(new ResultStaticMethodProxy("getRegisteredExperimentIds", new long[0]));
        this.addMethodProxy(new ResultStaticMethodProxy("getMetadata", new byte[0]));
        this.addMethodProxy(new ResultStaticMethodProxy("getData", new byte[0]));
        this.addMethodProxy(new ResultStaticMethodProxy("addConfiguration", null));
        this.addMethodProxy(new ResultStaticMethodProxy("registerPullAtomCallback", null));
        this.addMethodProxy(new ResultStaticMethodProxy("unregisterPullAtomCallback", null));
    }
}

