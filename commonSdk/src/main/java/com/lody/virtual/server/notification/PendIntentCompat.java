//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.lody.virtual.server.notification;

import android.app.PendingIntent;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.TextView;
import com.lody.virtual.StringFog;
import com.lody.virtual.helper.utils.Reflect;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

class PendIntentCompat {
    private RemoteViews mRemoteViews;
    private Map<Integer, PendingIntent> clickIntents;

    PendIntentCompat(RemoteViews mRemoteViews) {
        this.mRemoteViews = mRemoteViews;
    }

    public int findPendIntents() {
        if (this.clickIntents == null) {
            this.clickIntents = this.getClickIntents(this.mRemoteViews);
        }

        return this.clickIntents.size();
    }

    public void setPendIntent(RemoteViews remoteViews, View remoteview, View oldRemoteView) {
        if (this.findPendIntents() > 0) {
            Iterator<Map.Entry<Integer, PendingIntent>> set = this.clickIntents.entrySet().iterator();
            List<RectInfo> list = new ArrayList();
            int index = 0;

            while(set.hasNext()) {
                Map.Entry<Integer, PendingIntent> e = (Map.Entry)set.next();
                View view = oldRemoteView.findViewById((Integer)e.getKey());
                if (view != null) {
                    Rect rect = this.getRect(view);
                    list.add(new RectInfo(rect, (PendingIntent)e.getValue(), index));
                    ++index;
                }
            }

            if (remoteview instanceof ViewGroup) {
                this.setIntentByViewGroup(remoteViews, (ViewGroup)remoteview, list);
            }
        }

    }

    private Rect getRect(View view) {
        Rect rect = new Rect();
        rect.top = view.getTop();
        rect.left = view.getLeft();
        rect.right = view.getRight();
        rect.bottom = view.getBottom();
        ViewParent viewParent = view.getParent();
        if (viewParent != null && viewParent instanceof ViewGroup) {
            Rect prect = this.getRect((ViewGroup)viewParent);
            rect.top += prect.top;
            rect.left += prect.left;
            rect.right += prect.left;
            rect.bottom += prect.top;
        }

        return rect;
    }

    private void setIntentByViewGroup(RemoteViews remoteViews, ViewGroup viewGroup, List<RectInfo> list) {
        int count = viewGroup.getChildCount();
        Rect p = new Rect();
        viewGroup.getHitRect(p);

        for(int i = 0; i < count; ++i) {
            View v = viewGroup.getChildAt(i);
            if (v instanceof ViewGroup) {
                this.setIntentByViewGroup(remoteViews, (ViewGroup)v, list);
            } else if (v instanceof TextView || v instanceof ImageView) {
                Rect rect = this.getRect(v);
                RectInfo next = this.findIntent(rect, list);
                if (next != null) {
                    remoteViews.setOnClickPendingIntent(v.getId(), next.mPendingIntent);
                }
            }
        }

    }

    private RectInfo findIntent(Rect rect, List<RectInfo> list) {
        int maxArea = 0;
        RectInfo next = null;
        Iterator var5 = list.iterator();

        while(var5.hasNext()) {
            RectInfo rectInfo = (RectInfo)var5.next();
            int size = this.getOverlapArea(rect, rectInfo.rect);
            if (size > maxArea) {
                if (size == 0) {
                    Log.w("PendingIntentCompat", "find two:" + rectInfo.rect);
                }

                maxArea = size;
                next = rectInfo;
            }
        }

        return next;
    }

    private int getOverlapArea(Rect rect1, Rect rect2) {
        Rect rect = new Rect();
        rect.left = Math.max(rect1.left, rect2.left);
        rect.top = Math.max(rect1.top, rect2.top);
        rect.right = Math.min(rect1.right, rect2.right);
        rect.bottom = Math.min(rect1.bottom, rect2.bottom);
        return rect.left < rect.right && rect.top < rect.bottom ? (rect.right - rect.left) * (rect.bottom - rect.top) : 0;
    }

    private Map<Integer, PendingIntent> getClickIntents(RemoteViews remoteViews) {
        Map<Integer, PendingIntent> map = new HashMap();
        if (remoteViews == null) {
            return map;
        } else {
            Object mActionsObj = null;

            try {
                mActionsObj = Reflect.on(remoteViews).get("mActions");
            } catch (Exception var11) {
                var11.printStackTrace();
            }

            if (mActionsObj == null) {
                return map;
            } else {
                if (mActionsObj instanceof Collection) {
                    Collection mActions = (Collection)mActionsObj;
                    Iterator var5 = mActions.iterator();

                    while(var5.hasNext()) {
                        Object one = var5.next();
                        if (one != null) {
                            String action;
                            try {
                                action = (String)Reflect.on(one).call("getActionName").get();
                            } catch (Exception var10) {
                                action = one.getClass().getSimpleName();
                            }

                            if ("SetOnClickPendingIntent".equalsIgnoreCase(action)) {
                                int id = (Integer)Reflect.on(one).get("viewId");
                                PendingIntent intent = (PendingIntent)Reflect.on(one).get("pendingIntent");
                                map.put(id, intent);
                            }
                        }
                    }
                }

                return map;
            }
        }
    }

    class RectInfo {
        Rect rect;
        PendingIntent mPendingIntent;
        int index;

        public RectInfo(Rect rect, PendingIntent pendingIntent, int index) {
            this.rect = rect;
            this.mPendingIntent = pendingIntent;
            this.index = index;
        }

        public String toString() {
            return "RectInfo{rect=" + this.rect + '}';
        }
    }
}
