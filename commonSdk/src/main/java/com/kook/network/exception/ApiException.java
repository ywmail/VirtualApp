/*
 * Decompiled with CFR 0.152.
 */
package com.kook.network.exception;

public class ApiException
extends RuntimeException {
    private String errorCode;
    private Object data;

    public ApiException(String detailMessage) {
        super(detailMessage);
    }

    public ApiException(String detailMessage, String errorCode) {
        super(detailMessage);
        this.errorCode = errorCode;
    }

    public ApiException(String detailMessage, String errorCode, Object data) {
        super(detailMessage);
        this.errorCode = errorCode;
        this.data = data;
    }

    public String getErrorCode() {
        return this.errorCode;
    }

    public Object getData() {
        return this.data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}

