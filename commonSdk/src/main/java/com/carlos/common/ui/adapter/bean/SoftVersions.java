/*
 * Decompiled with CFR 0.152.
 */
package com.carlos.common.ui.adapter.bean;

import com.kook.librelease.StringFog;

public class SoftVersions {
    public String id;
    public Long createDate;
    public Integer delFlag;
    public String remarks;
    public String notice;
    public Integer novatioNecessaria;
    public String number;
    public String softId;
    public String updateUrl;
    public String softName;

    public String getId() {
        return this.id;
    }

    public void setId(String id2) {
        this.id = id2;
    }

    public Long getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(Long createDate) {
        this.createDate = createDate;
    }

    public Integer getDelFlag() {
        return this.delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    public String getRemarks() {
        return this.remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getNotice() {
        return this.notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public Integer getNovatioNecessaria() {
        return this.novatioNecessaria;
    }

    public void setNovatioNecessaria(Integer novatioNecessaria) {
        this.novatioNecessaria = novatioNecessaria;
    }

    public String getNumber() {
        return this.number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getSoftId() {
        return this.softId;
    }

    public void setSoftId(String softId) {
        this.softId = softId;
    }

    public String getUpdateUrl() {
        return this.updateUrl;
    }

    public void setUpdateUrl(String updateUrl) {
        this.updateUrl = updateUrl;
    }

    public String getSoftName() {
        return this.softName;
    }

    public void setSoftName(String softName) {
        this.softName = softName;
    }

    public String toString() {
        return "SoftVersions{id='" + this.id + '\'' + ", createDate=" + this.createDate + ", delFlag=" + this.delFlag + ", remarks='" + this.remarks + '\'' + ", notice='" + this.notice + '\'' + ", novatioNecessaria=" + this.novatioNecessaria + ", number='" + this.number + '\'' + ", softId='" + this.softId + '\'' + ", updateUrl='" + this.updateUrl + '\'' + ", softName='" + this.softName + '\'' + '}';
    }
}

