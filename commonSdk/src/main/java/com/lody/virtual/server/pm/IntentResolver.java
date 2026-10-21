/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Intent
 *  android.content.IntentFilter
 *  android.content.pm.ResolveInfo
 *  android.net.Uri
 *  android.os.Build$VERSION
 */
package com.lody.virtual.server.pm;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import com.lody.virtual.StringFog;
import com.lody.virtual.helper.utils.VLog;
import com.lody.virtual.server.pm.FastImmutableArraySet;
import com.lody.virtual.server.pm.parser.VPackage;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public abstract class IntentResolver<F extends VPackage.IntentInfo, R> {
    private static final String TAG = "IntentResolver";
    private static final Comparator sResolvePrioritySorter = new Comparator(){

        public int compare(Object o1, Object o2) {
            int q2;
            int q1;
            if (o1 instanceof IntentFilter) {
                q1 = ((IntentFilter)o1).getPriority();
                q2 = ((IntentFilter)o2).getPriority();
            } else if (o1 instanceof ResolveInfo) {
                ResolveInfo r1 = (ResolveInfo)o1;
                ResolveInfo r2 = (ResolveInfo)o2;
                q1 = r1.filter == null ? 0 : r1.filter.getPriority();
                q2 = r2.filter == null ? 0 : r2.filter.getPriority();
            } else {
                return 0;
            }
            return q1 > q2 ? -1 : (q1 < q2 ? 1 : 0);
        }
    };
    private HashSet<F> mFilters = new HashSet();
    private HashMap<String, F[]> mTypeToFilter = new HashMap();
    private HashMap<String, F[]> mBaseTypeToFilter = new HashMap();
    private HashMap<String, F[]> mWildTypeToFilter = new HashMap();
    private HashMap<String, F[]> mSchemeToFilter = new HashMap();
    private HashMap<String, F[]> mActionToFilter = new HashMap();
    private HashMap<String, F[]> mTypedActionToFilter = new HashMap();

    private static FastImmutableArraySet<String> getFastIntentCategories(Intent intent) {
        Set categories = intent.getCategories();
        if (categories == null) {
            return null;
        }
        return new FastImmutableArraySet<String>((String[]) categories.toArray(new String[categories.size()]));
    }

    public void addFilter(F f) {
        this.mFilters.add(f);
        int numS = this.register_intent_filter(f, ((VPackage.IntentInfo)f).filter.schemesIterator(), this.mSchemeToFilter, "      Scheme: ");
        int numT = this.register_mime_types(f, "      Type: ");
        if (numS == 0 && numT == 0) {
            this.register_intent_filter(f, ((VPackage.IntentInfo)f).filter.actionsIterator(), this.mActionToFilter, "      Action: ");
        }
        if (numT != 0) {
            this.register_intent_filter(f, ((VPackage.IntentInfo)f).filter.actionsIterator(), this.mTypedActionToFilter, "      TypedAction: ");
        }
    }

    private boolean filterEquals(IntentFilter f1, IntentFilter f2) {
        int i;
        int s2;
        int s1 = f1.countActions();
        if (s1 != (s2 = f2.countActions())) {
            return false;
        }
        for (i = 0; i < s1; ++i) {
            if (f2.hasAction(f1.getAction(i))) continue;
            return false;
        }
        s1 = f1.countCategories();
        if (s1 != (s2 = f2.countCategories())) {
            return false;
        }
        for (i = 0; i < s1; ++i) {
            if (f2.hasCategory(f1.getCategory(i))) continue;
            return false;
        }
        s1 = f1.countDataTypes();
        if (s1 != (s2 = f2.countDataTypes())) {
            return false;
        }
        s1 = f1.countDataSchemes();
        if (s1 != (s2 = f2.countDataSchemes())) {
            return false;
        }
        for (i = 0; i < s1; ++i) {
            if (f2.hasDataScheme(f1.getDataScheme(i))) continue;
            return false;
        }
        s1 = f1.countDataAuthorities();
        if (s1 != (s2 = f2.countDataAuthorities())) {
            return false;
        }
        s1 = f1.countDataPaths();
        if (s1 != (s2 = f2.countDataPaths())) {
            return false;
        }
        return Build.VERSION.SDK_INT < 19 || (s1 = f1.countDataSchemeSpecificParts()) == (s2 = f2.countDataSchemeSpecificParts());
    }

    private ArrayList<F> collectFilters(F[] array2, IntentFilter matching) {
        ArrayList<F> res = null;
        if (array2 != null) {
            F cur;
            for (int i = 0; i < array2.length && (cur = array2[i]) != null; ++i) {
                if (!this.filterEquals(((VPackage.IntentInfo)cur).filter, matching)) continue;
                if (res == null) {
                    res = new ArrayList<F>();
                }
                res.add(cur);
            }
        }
        return res;
    }

    public ArrayList<F> findFilters(IntentFilter matching) {
        if (matching.countDataSchemes() == 1) {
            return this.collectFilters((F[]) this.mSchemeToFilter.get(matching.getDataScheme(0)), matching);
        }
        if (matching.countDataTypes() != 0 && matching.countActions() == 1) {
            return this.collectFilters((F[]) this.mTypedActionToFilter.get(matching.getAction(0)), matching);
        }
        if (matching.countDataTypes() == 0 && matching.countDataSchemes() == 0 && matching.countActions() == 1) {
            return this.collectFilters((F[]) this.mActionToFilter.get(matching.getAction(0)), matching);
        }
        ArrayList<VPackage.IntentInfo> res = null;
        for (VPackage.IntentInfo cur : this.mFilters) {
            if (!this.filterEquals(cur.filter, matching)) continue;
            if (res == null) {
                res = new ArrayList<VPackage.IntentInfo>();
            }
            res.add(cur);
        }
        return (ArrayList<F>) res;
    }

    public void removeFilter(F f) {
        this.removeFilterInternal(f);
        this.mFilters.remove(f);
    }

    void removeFilterInternal(F f) {
        int numS = this.unregister_intent_filter(f, ((VPackage.IntentInfo)f).filter.schemesIterator(), this.mSchemeToFilter, "      Scheme: ");
        int numT = this.unregister_mime_types(f, "      Type: ");
        if (numS == 0 && numT == 0) {
            this.unregister_intent_filter(f, ((VPackage.IntentInfo)f).filter.actionsIterator(), this.mActionToFilter, "      Action: ");
        }
        if (numT != 0) {
            this.unregister_intent_filter(f, ((VPackage.IntentInfo)f).filter.actionsIterator(), this.mTypedActionToFilter, "      TypedAction: ");
        }
    }

    public Iterator<F> filterIterator() {
        return new IteratorWrapper(this.mFilters.iterator());
    }

    public Set<F> filterSet() {
        return Collections.unmodifiableSet(this.mFilters);
    }

    public List<R> queryIntentFromList(Intent intent, String resolvedType, boolean defaultOnly, ArrayList<F[]> listCut, int userId) {
        ArrayList resultList = new ArrayList();
        FastImmutableArraySet<String> categories = IntentResolver.getFastIntentCategories(intent);
        String scheme = intent.getScheme();
        int N = listCut.size();
        for (int i = 0; i < N; ++i) {
            this.buildResolveList(intent, categories, defaultOnly, resolvedType, scheme, (F[]) listCut.get(i), resultList, userId);
        }
        this.sortResults(resultList);
        return resultList;
    }

    public List<R> queryIntent(Intent intent, String resolvedType, boolean defaultOnly, int userId) {
        int slashpos;
        String scheme = intent.getScheme();
        ArrayList finalList = new ArrayList();
        VPackage.IntentInfo[] firstTypeCut = null;
        VPackage.IntentInfo[] secondTypeCut = null;
        VPackage.IntentInfo[] thirdTypeCut = null;
        VPackage.IntentInfo[] schemeCut = null;
        if (resolvedType != null && (slashpos = resolvedType.indexOf(47)) > 0) {
            String baseType = resolvedType.substring(0, slashpos);
            if (!baseType.equals("*")) {
                if (resolvedType.length() != slashpos + 2 || resolvedType.charAt(slashpos + 1) != '*') {
                    firstTypeCut = (VPackage.IntentInfo[])this.mTypeToFilter.get(resolvedType);
                    secondTypeCut = (VPackage.IntentInfo[])this.mWildTypeToFilter.get(baseType);
                } else {
                    firstTypeCut = (VPackage.IntentInfo[])this.mBaseTypeToFilter.get(baseType);
                    secondTypeCut = (VPackage.IntentInfo[])this.mWildTypeToFilter.get(baseType);
                }
                thirdTypeCut = (VPackage.IntentInfo[])this.mWildTypeToFilter.get("*");
            } else if (intent.getAction() != null) {
                firstTypeCut = (VPackage.IntentInfo[])this.mTypedActionToFilter.get(intent.getAction());
            }
        }
        if (scheme != null) {
            schemeCut = (VPackage.IntentInfo[])this.mSchemeToFilter.get(scheme);
        }
        if (resolvedType == null && scheme == null && intent.getAction() != null) {
            firstTypeCut = (VPackage.IntentInfo[])this.mActionToFilter.get(intent.getAction());
        }
        FastImmutableArraySet<String> categories = IntentResolver.getFastIntentCategories(intent);
        if (firstTypeCut != null) {
            this.buildResolveList(intent, categories, defaultOnly, resolvedType, scheme, (F[]) firstTypeCut, finalList, userId);
        }
        if (secondTypeCut != null) {
            this.buildResolveList(intent, categories, defaultOnly, resolvedType, scheme, (F[]) secondTypeCut, finalList, userId);
        }
        if (thirdTypeCut != null) {
            this.buildResolveList(intent, categories, defaultOnly, resolvedType, scheme, (F[]) thirdTypeCut, finalList, userId);
        }
        if (schemeCut != null) {
            this.buildResolveList(intent, categories, defaultOnly, resolvedType, scheme, (F[]) schemeCut, finalList, userId);
        }
        this.sortResults(finalList);
        return finalList;
    }

    protected boolean allowFilterResult(F filter, List<R> dest) {
        return true;
    }

    protected boolean isFilterStopped(F filter) {
        return false;
    }

    protected abstract boolean isPackageForFilter(String var1, F var2);

    protected abstract F[] newArray(int var1);

    protected R newResult(F filter, int match, int userId) {
        return (R)filter;
    }

    protected void sortResults(List<R> results) {
        Collections.sort(results, sResolvePrioritySorter);
    }

    protected void dumpFilter(PrintWriter out, String prefix, F filter) {
        out.print(prefix);
        out.println(filter);
    }

    protected Object filterToLabel(F filter) {
        return "IntentFilter";
    }

    protected void dumpFilterLabel(PrintWriter out, String prefix, Object label, int count) {
        out.print(prefix);
        out.print(label);
        out.print(": ");
        out.println(count);
    }


    private void addFilter(HashMap<String, F[]> map, String name, F filter) {
        F[] array = (F[]) map.get(name);
        if (array == null) {
            array = this.newArray(2);
            map.put(name, array);
            array[0] = filter;
        } else {
            int N = array.length;

            int i;
            for(i = N; i > 0 && array[i - 1] == null; --i) {
            }

            if (i < N) {
                array[i] = filter;
            } else {
                F[] newa = this.newArray(N * 3 / 2);
                System.arraycopy(array, 0, newa, 0, N);
                newa[N] = filter;
                map.put(name, newa);
            }
        }

    }

    private int register_mime_types(F filter, String prefix) {
        Iterator i = ((VPackage.IntentInfo)filter).filter.typesIterator();
        if (i == null) {
            return 0;
        }
        int num = 0;
        while (i.hasNext()) {
            String name = (String)i.next();
            ++num;
            String baseName = name;
            int slashpos = name.indexOf(47);
            if (slashpos > 0) {
                baseName = name.substring(0, slashpos).intern();
            } else {
                name = name + "/*";
            }
            this.addFilter(this.mTypeToFilter, name, filter);
            if (slashpos > 0) {
                this.addFilter(this.mBaseTypeToFilter, baseName, filter);
                continue;
            }
            this.addFilter(this.mWildTypeToFilter, baseName, filter);
        }
        return num;
    }

    private int unregister_mime_types(F filter, String prefix) {
        Iterator i = ((VPackage.IntentInfo)filter).filter.typesIterator();
        if (i == null) {
            return 0;
        }
        int num = 0;
        while (i.hasNext()) {
            String name = (String)i.next();
            ++num;
            String baseName = name;
            int slashpos = name.indexOf(47);
            if (slashpos > 0) {
                baseName = name.substring(0, slashpos).intern();
            } else {
                name = name + "/*";
            }
            this.remove_all_objects(this.mTypeToFilter, name, filter);
            if (slashpos > 0) {
                this.remove_all_objects(this.mBaseTypeToFilter, baseName, filter);
                continue;
            }
            this.remove_all_objects(this.mWildTypeToFilter, baseName, filter);
        }
        return num;
    }

    private int register_intent_filter(F filter, Iterator<String> i, HashMap<String, F[]> dest, String prefix) {
        if (i == null) {
            return 0;
        }
        int num = 0;
        while (i.hasNext()) {
            String name = i.next();
            ++num;
            this.addFilter(dest, name, filter);
        }
        return num;
    }

    private int unregister_intent_filter(F filter, Iterator<String> i, HashMap<String, F[]> dest, String prefix) {
        if (i == null) {
            return 0;
        }
        int num = 0;
        while (i.hasNext()) {
            String name = i.next();
            ++num;
            this.remove_all_objects(dest, name, filter);
        }
        return num;
    }

    private void remove_all_objects(HashMap<String, F[]> map, String name, Object object) {
        VPackage.IntentInfo[] array2 = (VPackage.IntentInfo[])map.get(name);
        if (array2 != null) {
            int LAST;
            for (LAST = array2.length - 1; LAST >= 0 && array2[LAST] == null; --LAST) {
            }
            for (int idx = LAST; idx >= 0; --idx) {
                if (array2[idx] != object) continue;
                int remain = LAST - idx;
                if (remain > 0) {
                    System.arraycopy(array2, idx + 1, array2, idx, remain);
                }
                array2[LAST] = null;
                --LAST;
            }
            if (LAST < 0) {
                map.remove(name);
            } else if (LAST < array2.length / 2) {
                VPackage.IntentInfo[] newa = this.newArray(LAST + 2);
                System.arraycopy(array2, 0, newa, 0, LAST + 1);
                map.put(name, (F[]) newa);
            }
        }
    }

    private void buildResolveList(Intent intent, FastImmutableArraySet<String> categories, boolean defaultOnly, String resolvedType, String scheme, F[] src, List<R> dest, int userId) {
        F filter;
        String action = intent.getAction();
        Uri data = intent.getData();
        String packageName = intent.getPackage();
        int N = src != null ? src.length : 0;
        boolean hasNonDefaults = false;
        for (int i = 0; i < N && (filter = src[i]) != null; ++i) {
            int match;
            if (packageName != null && !this.isPackageForFilter(packageName, filter) || !this.allowFilterResult(filter, dest) || (match = ((VPackage.IntentInfo)filter).filter.match(action, resolvedType, scheme, data, categories, TAG)) < 0) continue;
            if (!defaultOnly || ((VPackage.IntentInfo)filter).filter.hasCategory("android.intent.category.DEFAULT")) {
                R oneResult = this.newResult(filter, match, userId);
                if (oneResult == null) continue;
                dest.add(oneResult);
                continue;
            }
            hasNonDefaults = true;
        }
        if (hasNonDefaults) {
            if (dest.size() == 0) {
                VLog.w(TAG, "resolveIntent failed: found match, but none with CATEGORY_DEFAULT", new Object[0]);
            } else if (dest.size() > 1) {
                VLog.w(TAG, "resolveIntent: multiple matches, only some with CATEGORY_DEFAULT", new Object[0]);
            }
        }
    }

    private class IteratorWrapper
    implements Iterator<F> {
        private Iterator<F> mI;
        private F mCur;

        IteratorWrapper(Iterator<F> it) {
            this.mI = it;
        }

        @Override
        public boolean hasNext() {
            return this.mI.hasNext();
        }

        @Override
        public F next() {
            this.mCur = (F) this.mI.next();
            return this.mCur;
        }

        @Override
        public void remove() {
            if (this.mCur != null) {
                IntentResolver.this.removeFilterInternal(this.mCur);
            }
            this.mI.remove();
        }
    }
}

