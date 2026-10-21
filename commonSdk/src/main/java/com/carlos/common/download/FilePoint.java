/*
 * Decompiled with CFR 0.152.
 */
package com.carlos.common.download;

public class FilePoint {
    private String fileName;
    private String url;
    private String filePath;

    public FilePoint(String url) {
        this.url = url;
    }

    public FilePoint(String filePath, String url) {
        this.filePath = filePath;
        this.url = url;
    }

    public FilePoint(String url, String filePath, String fileName) {
        this.url = url;
        this.filePath = filePath;
        this.fileName = fileName;
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFilePath() {
        return this.filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}

