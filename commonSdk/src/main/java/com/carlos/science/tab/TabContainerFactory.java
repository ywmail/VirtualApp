/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.view.LayoutInflater
 */
package com.carlos.science.tab;

import android.view.LayoutInflater;
import com.carlos.libcommon.StringFog;
import com.carlos.science.ApplicationPluginPkgName;
import com.carlos.science.FloatBallManager;
import com.carlos.science.server.module.hyxd.HYXDTab1;
import com.carlos.science.server.module.mrfz.MRFZTab;
import com.carlos.science.server.module.normal.NormalTab;
import com.carlos.science.tab.TabChild;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TabContainerFactory
extends ApplicationPluginPkgName {
    static TabContainerFactory tabContainerFactory = new TabContainerFactory();
    private static Map<String, List<TabChild>> tabChildMap = new HashMap<String, List<TabChild>>();
    private static List<String> controlerApplication = new ArrayList<String>();

    public void initTag(LayoutInflater layoutInflater, FloatBallManager floatBallManager) {
        ArrayList<TabChild> childList = new ArrayList<TabChild>();
        childList.add(new TabChild("com.tencent.mm", "设置", new NormalTab(layoutInflater, floatBallManager, 0)));
        tabChildMap.put("com.tencent.mm", childList);
        childList = new ArrayList();
        childList.add(new TabChild("com.hypergryph.arknights", "拓展功能", new MRFZTab(layoutInflater, floatBallManager, 3)));
        tabChildMap.put("com.hypergryph.arknights", childList);
        childList = new ArrayList();
        tabChildMap.put("com.taobao.taobao", childList);
        childList = new ArrayList();
        childList.add(new TabChild("com.netease.hyxd.ewan", "功能1", new HYXDTab1(layoutInflater, floatBallManager, 4)));
        tabChildMap.put("com.netease.hyxd.ewan", childList);
        childList = new ArrayList();
        childList.add(new TabChild("def", "基本功能", new NormalTab(layoutInflater, floatBallManager, 0)));
        tabChildMap.put("def", childList);
        Set<String> pkgList = tabChildMap.keySet();
        controlerApplication = new ArrayList<String>(pkgList);
    }

    public static TabContainerFactory getInstance() {
        return tabContainerFactory;
    }

    public List<TabChild> getTabChildListByPackageName(String packageName) {
        List<TabChild> childList = tabChildMap.containsKey(packageName) ? tabChildMap.get(packageName) : tabChildMap.get("def");
        return childList;
    }

    public List<String> getControlerApplication() {
        return controlerApplication;
    }
}

