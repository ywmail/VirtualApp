/*
 * Decompiled with CFR 0.152.
 */
package com.carlos.common.email;

import java.io.Serializable;

public class LeaveMessage
implements Serializable {
    private String sendAccount;
    private String sendPassword;
    private String title = "";
    private String content = "";
    private String receiveAccount;

    public String getSendAccount() {
        return this.sendAccount;
    }

    public void setSendAccount(String sendAccount) {
        this.sendAccount = sendAccount;
    }

    public String getSendPassword() {
        return this.sendPassword;
    }

    public void setSendPassword(String sendPassword) {
        this.sendPassword = sendPassword;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getReceiveAccount() {
        return this.receiveAccount;
    }

    public void setReceiveAccount(String receiveAccount) {
        this.receiveAccount = receiveAccount;
    }
}

