/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.content.Context
 *  android.text.TextUtils
 *  android.view.View
 *  android.view.ViewGroup
 *  org.jdeferred.Promise
 */
package com.carlos.science.client.core;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import com.carlos.common.utils.ResponseProgram;
import com.carlos.libcommon.StringFog;
import com.carlos.science.stebcore.StepImpl;
import com.kook.common.utils.HVLog;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.jdeferred.Promise;

public class FindView {
    public static int[] getLocationOnScreen(View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        return location;
    }

    public static Promise<List<View>, Throwable, Void> responseViews(Activity activity, StepImpl stepImpl, String idResName) {
        return ResponseProgram.defer().when(() -> {
            List<View> views = null;
            try {
                View view = activity.getWindow().getDecorView();
                int idRes = FindView.getViewIdByName((Context)activity, idResName);
                while (views == null || views.size() <= 0) {
                    boolean findResult;
                    if (stepImpl.finishStep) {
                        HVLog.i("当前任务 stepImpl 已经结束");
                        return null;
                    }
                    views = FindView.getAllChildViews(view, idRes);
                    boolean bl = findResult = views == null || views.size() <= 1;
                    if (findResult) {
                        views = FindView.getViewsByWindow((Context)activity, idRes);
                    }
                    boolean bl2 = findResult = views.size() <= 0;
                    if (!findResult) break;
                    Thread.sleep(200L);
                }
                return views;
            }
            catch (Exception e) {
                HVLog.printException(e);
                return null;
            }
        });
    }

    public static List<View> getViewsByWindow(Context context, int idRes) {
        Field wmGlobalField = null;
        int targetViewId = idRes;

        try {
            wmGlobalField = context.getSystemService("window").getClass().getDeclaredField("mGlobal");
        } catch (Exception var14) {
            wmGlobalField = null;
            HVLog.printException(var14);
        }

        List<View> viewList = new ArrayList();
        View viewById;
        if (wmGlobalField != null) {
            try {
                wmGlobalField.setAccessible(true);
                Object wmGlobal = wmGlobalField.get(context.getSystemService("window"));
                Field viewsField = wmGlobal.getClass().getDeclaredField("mViews");
                viewsField.setAccessible(true);
                ArrayList<View> views = (ArrayList)viewsField.get(wmGlobal);
                Iterator var18 = views.iterator();

                while(var18.hasNext()) {
                    View view = (View)var18.next();
                    viewById = view.findViewById(targetViewId);
                    if (viewById != null) {
                        viewList.add(viewById);
                    }
                }

                return viewList;
            } catch (Exception var12) {
                var12.printStackTrace();
                HVLog.printException(var12);
                return null;
            }
        } else {
            try {
                Field wmLocalField = context.getSystemService("window").getClass().getSuperclass().getDeclaredField("mWindowManager");
                wmLocalField.setAccessible(true);
                Object wmLocal = wmLocalField.get(context.getSystemService("window"));
                Field viewsField = wmLocal.getClass().getDeclaredField("mViews");
                viewsField.setAccessible(true);
                List<View> views = Arrays.asList((View[])viewsField.get(wmLocal));
                Iterator var9 = views.iterator();

                while(var9.hasNext()) {
                    viewById = (View)var9.next();
                    viewById = viewById.findViewById(targetViewId);
                    if (viewById != null) {
                        viewList.add(viewById);
                    }
                }

                return viewList;
            } catch (Exception var13) {
                HVLog.printException(var13);
                return null;
            }
        }
    }

    public static List<View> getAllChildViews(View view, int targetViewId) {
        ArrayList<View> allchildren = new ArrayList<View>();
        if (view instanceof ViewGroup) {
            ViewGroup vp = (ViewGroup)view;
            for (int i = 0; i < vp.getChildCount(); ++i) {
                View viewchild = vp.getChildAt(i);
                if (viewchild.getId() == targetViewId) {
                    allchildren.add(viewchild);
                }
                allchildren.addAll(FindView.getAllChildViews(viewchild, targetViewId));
            }
        }
        return allchildren;
    }

    public static List<View> getAllChildViews(View view, String viewIdName) {
        int targetViewId = FindView.getViewIdByName(view.getContext(), viewIdName);
        List<View> allChildViews = FindView.getAllChildViews(view, targetViewId);
        return allChildViews;
    }

    public static View findView(Activity activity, Object idRes) {
        View viewByIdRes = null;
        if (!(idRes instanceof Integer || idRes instanceof String || idRes instanceof Class)) {
            HVLog.e("这里不要加入其他类型的数据进来，不能把这里做的太复杂了");
        }
        if (idRes instanceof Integer) {
            viewByIdRes = activity.findViewById(((Integer)idRes).intValue());
        } else if (idRes instanceof String) {
            viewByIdRes = FindView.findViewByName(activity, (String)idRes);
            if (viewByIdRes == null) {
                viewByIdRes = FindView.getViewByWindow((Context)activity, (String)idRes);
            }
        } else if (idRes instanceof Class) {
            List<View> viewListByClass = FindView.findViewListByClass(activity, (Class)idRes);
            if (viewListByClass.size() == 1) {
                viewByIdRes = viewListByClass.get(0);
            } else {
                throw new NullPointerException("查找的view 数量不一    size:" + viewListByClass.size());
            }
        }
        return viewByIdRes;
    }

    private static View findViewByName(Activity activity, String resName) {
        try {
            int targetViewId = FindView.getViewIdByName((Context)activity, resName);
            View decorView = activity.getWindow().getDecorView();
            View view = decorView.findViewById(targetViewId);
            if (view == null) {
                // empty if block
            }
            return view;
        }
        catch (Exception e) {
            HVLog.printException(e);
            return null;
        }
    }

    public static int getViewIdByName(Context context, String viewIdName) {
        if (TextUtils.isEmpty((CharSequence)viewIdName)) {
            HVLog.e("resName is null");
            return -1;
        }
        int targetViewId = context.getResources().getIdentifier(viewIdName, "id", context.getPackageName());
        return targetViewId;
    }

    private static View getViewByWindow(Context context, String resName) {
        if (TextUtils.isEmpty(resName)) {
            HVLog.e("getViewByWindow resName is null");
            return null;
        } else {
            Field wmGlobalField = null;
            int targetViewId = context.getResources().getIdentifier(resName, "id", context.getPackageName());

            try {
                wmGlobalField = context.getSystemService("window").getClass().getDeclaredField("mGlobal");
            } catch (Exception var14) {
                wmGlobalField = null;
                HVLog.printException(var14);
            }

            if (wmGlobalField != null) {
                try {
                    wmGlobalField.setAccessible(true);
                    Object wmGlobal = wmGlobalField.get(context.getSystemService("window"));
                    Field viewsField = wmGlobal.getClass().getDeclaredField("mViews");
                    viewsField.setAccessible(true);
                    ArrayList<View> views = (ArrayList)viewsField.get(wmGlobal);
                    Iterator var18 = views.iterator();

                    View viewById;
                    do {
                        if (!var18.hasNext()) {
                            return null;
                        }

                        View view = (View)var18.next();
                        viewById = view.findViewById(targetViewId);
                    } while(viewById == null);

                    return viewById;
                } catch (Exception var12) {
                    var12.printStackTrace();
                    HVLog.printException(var12);
                    return null;
                }
            } else {
                try {
                    Field wmLocalField = context.getSystemService("window").getClass().getSuperclass().getDeclaredField("mWindowManager");
                    wmLocalField.setAccessible(true);
                    Object wmLocal = wmLocalField.get(context.getSystemService("window"));
                    Field viewsField = wmLocal.getClass().getDeclaredField("mViews");
                    viewsField.setAccessible(true);
                    List<View> viewList = Arrays.asList((View[])viewsField.get(wmLocal));
                    ArrayList<View> views = new ArrayList();
                    Iterator var9 = views.iterator();

                    View viewById;
                    do {
                        if (!var9.hasNext()) {
                            return null;
                        }

                        View view = (View)var9.next();
                        viewById = view.findViewById(targetViewId);
                    } while(viewById == null);

                    return viewById;
                } catch (Exception var13) {
                    HVLog.printException(var13);
                    return null;
                }
            }
        }
    }

    protected static List<View> findViewListByClass(Activity activity, Class classz) {
        try {
            View decorView = activity.getWindow().getDecorView();
            List<View> allChildViews = FindView.getAllChildViews(decorView, classz);
            if (allChildViews == null || allChildViews.size() == 0) {
                HVLog.e("findViewListByClass classz:" + classz + "    " + activity.getLocalClassName() + "     view is null");
            }
            return allChildViews;
        }
        catch (Exception e) {
            HVLog.printException(e);
            return null;
        }
    }

    protected static List<View> getAllChildViews(View view, Class classzc) {
        ArrayList<View> allchildren = new ArrayList<View>();
        if (view instanceof ViewGroup) {
            ViewGroup vp = (ViewGroup)view;
            for (int i = 0; i < vp.getChildCount(); ++i) {
                View viewchild = vp.getChildAt(i);
                try {
                    if (classzc.asSubclass(viewchild.getClass()) == null) {
                        allchildren.add(viewchild);
                    }
                }
                catch (ClassCastException e) {
                    HVLog.printException(e);
                }
                allchildren.addAll(FindView.getAllChildViews(viewchild, classzc));
            }
        }
        return allchildren;
    }
}

