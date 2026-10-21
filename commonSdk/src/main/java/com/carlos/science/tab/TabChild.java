/*
 * Decompiled with CFR 0.152.
 */
package com.carlos.science.tab;

import com.carlos.science.tab.TabContainer;

public class TabChild {
    private String packageName;
    private String title;
    private TabContainer tabContainer;

    public TabChild(String packageName, String title, TabContainer tabContainer) {
        this.title = title;
        this.tabContainer = tabContainer;
        this.packageName = packageName;
        tabContainer.setTabChild(this);
    }

    public TabChild(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

    public TabContainer getTabContainer() {
        return this.tabContainer;
    }

    public String getPackageName() {
        return this.packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
}

