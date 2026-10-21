/*
 * Decompiled with CFR 0.152.
 */
package com.kook.network.vo;

public class HttpResult<T> {
    private boolean ok;
    private String errorcode;
    private String errormsg;
    private T data;

    public boolean isOk() {
        return this.ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public String getErrormsg() {
        return this.errormsg;
    }

    public void setErrormsg(String errormsg) {
        this.errormsg = errormsg;
    }

    public String getErrorcode() {
        return this.errorcode;
    }

    public void setErrorcode(String errorcode) {
        this.errorcode = errorcode;
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }
}

