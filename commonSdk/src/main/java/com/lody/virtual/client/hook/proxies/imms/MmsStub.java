/*
 * Decompiled with CFR 0.152.
 */
package com.lody.virtual.client.hook.proxies.imms;

import com.lody.virtual.StringFog;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.ReplaceCallingPkgMethodProxy;
import com.lody.virtual.client.hook.base.ReplaceSpecPkgMethodProxy;
import mirror.com.android.internal.telephony.IMms;

public class MmsStub
extends BinderInvocationProxy {
    public MmsStub() {
        super(IMms.Stub.asInterface, "imms");
    }

    @Override
    protected void onBindMethods() {
        this.addMethodProxy(new ReplaceSpecPkgMethodProxy("sendMessage", 1));
        this.addMethodProxy(new ReplaceSpecPkgMethodProxy("downloadMessage", 1));
        this.addMethodProxy(new ReplaceCallingPkgMethodProxy("importTextMessage"));
        this.addMethodProxy(new ReplaceCallingPkgMethodProxy("importMultimediaMessage"));
        this.addMethodProxy(new ReplaceCallingPkgMethodProxy("deleteStoredMessage"));
        this.addMethodProxy(new ReplaceCallingPkgMethodProxy("deleteStoredConversation"));
        this.addMethodProxy(new ReplaceCallingPkgMethodProxy("updateStoredMessageStatus"));
        this.addMethodProxy(new ReplaceCallingPkgMethodProxy("archiveStoredConversation"));
        this.addMethodProxy(new ReplaceCallingPkgMethodProxy("addTextMessageDraft"));
        this.addMethodProxy(new ReplaceCallingPkgMethodProxy("addMultimediaMessageDraft"));
        this.addMethodProxy(new ReplaceSpecPkgMethodProxy("sendStoredMessage", 1));
        this.addMethodProxy(new ReplaceCallingPkgMethodProxy("setAutoPersisting"));
    }
}

