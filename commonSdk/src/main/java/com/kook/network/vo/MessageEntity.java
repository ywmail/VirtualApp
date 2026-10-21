/*
 * Decompiled with CFR 0.152.
 */
package com.kook.network.vo;

import com.kook.network.StringFog;

public class MessageEntity {
    private int code;
    private int codeAction;
    private String msg;
    private String data;
    private long totalTime;

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getData() {
        return this.data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public long getTotalTime() {
        return this.totalTime;
    }

    public void setTotalTime(long totalTime) {
        this.totalTime = totalTime;
    }

    public int getCodeAction() {
        return this.codeAction;
    }

    public void setCodeAction(int codeAction) {
        this.codeAction = codeAction;
    }

    public String toString() {
        return "MessageEntity{code=" + this.code + ", codeAction=" + this.codeAction + ", msg='" + this.msg + '\'' + ", data='" + this.data + '\'' + ", totalTime=" + this.totalTime + '}';
    }
}

