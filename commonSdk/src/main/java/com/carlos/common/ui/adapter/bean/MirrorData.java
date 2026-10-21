/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  androidx.annotation.DrawableRes
 *  androidx.annotation.LayoutRes
 *  androidx.annotation.StringRes
 *  com.kook.librelease.R$drawable
 *  com.kook.librelease.R$layout
 *  com.kook.librelease.R$string
 */
package com.carlos.common.ui.adapter.bean;

import androidx.annotation.DrawableRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.StringRes;
import com.kook.librelease.R;
import java.util.ArrayList;
import java.util.List;

public class MirrorData {
    public static List<MirrorData> mirrorDataList = new ArrayList<MirrorData>();
    int MENU_TYPE;
    @DrawableRes
    int icon;
    @StringRes
    int title;
    @StringRes
    int subTitle;
    @StringRes
    int actionBtn;
    @LayoutRes
    int itemLayoutRes;

    public MirrorData(@DrawableRes int icon, @StringRes int title, @StringRes int subTitle, @StringRes int actionBtn, @LayoutRes int layout2, int menuType) {
        this.icon = icon;
        this.title = title;
        this.subTitle = subTitle;
        this.actionBtn = actionBtn;
        this.MENU_TYPE = menuType;
        this.itemLayoutRes = layout2;
    }

    public int getIcon() {
        return this.icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public int getTitle() {
        return this.title;
    }

    public void setTitle(int title) {
        this.title = title;
    }

    public int getSubTitle() {
        return this.subTitle;
    }

    public void setSubTitle(int subTitle) {
        this.subTitle = subTitle;
    }

    public int getActionBtn() {
        return this.actionBtn;
    }

    public void setActionBtn(int actionBtn) {
        this.actionBtn = actionBtn;
    }

    public int getMenuType() {
        return this.MENU_TYPE;
    }

    public void setMenuType(int menuType) {
        this.MENU_TYPE = menuType;
    }

    public int getItemLayoutRes() {
        return this.itemLayoutRes;
    }

    public void setItemLayoutRes(int itemLayoutRes) {
        this.itemLayoutRes = itemLayoutRes;
    }

    static {
        mirrorDataList.add(new MirrorData(R.drawable.icon_loc, R.string.virtual_location, R.string.no_mock, R.string.activity_choose_location, R.layout.activity_mirror_item_menu, 1));
        mirrorDataList.add(new MirrorData(R.drawable.icon_wifi, R.string.menu_mock_wifi, R.string.no_mock, R.string.settings, R.layout.activity_mirror_item_menu, 0));
        mirrorDataList.add(new MirrorData(R.drawable.icon_phone, R.string.menu_mock_phone, R.string.menu_mock_wifi, R.string.fake_device_info, R.layout.activity_mirror_item_menu, 2));
        mirrorDataList.add(new MirrorData(R.drawable.icon_delete, R.string.application_manager, R.string.delete, R.string.clear, R.layout.activity_mirror_item_menu_btn2, 4));
        mirrorDataList.add(new MirrorData(R.drawable.icon_shortcut, R.string.create_shortcut, R.string.create_desktop_icon, R.string.create, R.layout.activity_mirror_item_menu, 3));
        mirrorDataList.add(new MirrorData(R.drawable.icon_bure, R.string.backup_recovery_title, R.string.recovery, R.string.backup, R.layout.activity_mirror_item_menu_btn2, 5));
    }
}

