/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.accounts.AuthenticatorDescription
 *  android.app.Activity
 *  android.content.Context
 *  android.content.Intent
 *  android.content.res.Resources
 *  android.content.res.Resources$NotFoundException
 *  android.graphics.drawable.Drawable
 *  android.os.Bundle
 *  android.view.LayoutInflater
 *  android.view.View
 *  android.view.ViewGroup
 *  android.widget.AdapterView
 *  android.widget.AdapterView$OnItemClickListener
 *  android.widget.ArrayAdapter
 *  android.widget.ImageView
 *  android.widget.ListAdapter
 *  android.widget.ListView
 *  android.widget.TextView
 *  com.kook.librelease.R$id
 *  com.kook.librelease.R$layout
 */
package com.lody.virtual.client.stub;

import android.accounts.AuthenticatorDescription;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.kook.librelease.R;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.ipc.VAccountManager;
import com.lody.virtual.helper.utils.VLog;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class ChooseAccountTypeActivity
extends Activity {
    public static final String KEY_USER_ID = "userId";
    private static final String TAG = "AccountChooser";
    private static final boolean DEBUG = false;
    private HashMap<String, AuthInfo> mTypeToAuthenticatorInfo = new HashMap();
    private ArrayList<AuthInfo> mAuthenticatorInfosToDisplay;
    private int mCallingUid;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mCallingUid = this.getIntent().getIntExtra(KEY_USER_ID, -1);
        if (this.mCallingUid == -1) {
            this.finish();
            return;
        }
        HashSet setOfAllowableAccountTypes = null;
        String[] validAccountTypes = this.getIntent().getStringArrayExtra("allowableAccountTypes");
        if (validAccountTypes != null) {
            setOfAllowableAccountTypes = new HashSet(validAccountTypes.length);
            Collections.addAll(setOfAllowableAccountTypes, validAccountTypes);
        }
        this.buildTypeToAuthDescriptionMap();
        this.mAuthenticatorInfosToDisplay = new ArrayList(this.mTypeToAuthenticatorInfo.size());
        for (Map.Entry<String, AuthInfo> entry : this.mTypeToAuthenticatorInfo.entrySet()) {
            String type = entry.getKey();
            AuthInfo info = entry.getValue();
            if (setOfAllowableAccountTypes != null && !setOfAllowableAccountTypes.contains(type)) continue;
            this.mAuthenticatorInfosToDisplay.add(info);
        }
        if (this.mAuthenticatorInfosToDisplay.isEmpty()) {
            Bundle bundle = new Bundle();
            bundle.putString("errorMessage", "no allowable account types");
            this.setResult(-1, new Intent().putExtras(bundle));
            this.finish();
            return;
        }
        if (this.mAuthenticatorInfosToDisplay.size() == 1) {
            this.setResultAndFinish(this.mAuthenticatorInfosToDisplay.get((int)0).desc.type);
            return;
        }
        this.setContentView(R.layout.choose_account_type);
        ListView list = (ListView)this.findViewById(16908298);
        list.setAdapter((ListAdapter)new AccountArrayAdapter((Context)this, 17367043, this.mAuthenticatorInfosToDisplay));
        list.setChoiceMode(0);
        list.setTextFilterEnabled(false);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            public void onItemClick(AdapterView<?> parent, View v, int position, long id2) {
                ChooseAccountTypeActivity.this.setResultAndFinish(((AuthInfo)((ChooseAccountTypeActivity)ChooseAccountTypeActivity.this).mAuthenticatorInfosToDisplay.get((int)position)).desc.type);
            }
        });
    }

    private void setResultAndFinish(String type) {
        Bundle bundle = new Bundle();
        bundle.putString("accountType", type);
        this.setResult(-1, new Intent().putExtras(bundle));
        this.finish();
    }

    private void buildTypeToAuthDescriptionMap() {
        for (AuthenticatorDescription desc : VAccountManager.get().getAuthenticatorTypes(this.mCallingUid)) {
            String name = null;
            Drawable icon = null;
            try {
                Resources res = VirtualCore.get().getResources(desc.packageName);
                icon = res.getDrawable(desc.iconId);
                CharSequence sequence = res.getText(desc.labelId);
                name = sequence.toString();
                name = sequence.toString();
            }
            catch (Resources.NotFoundException e) {
                VLog.w(TAG, "No icon resource for account type " + desc.type, new Object[0]);
            }
            AuthInfo authInfo = new AuthInfo(desc, name, icon);
            this.mTypeToAuthenticatorInfo.put(desc.type, authInfo);
        }
    }

    private static class AccountArrayAdapter
    extends ArrayAdapter<AuthInfo> {
        private LayoutInflater mLayoutInflater;
        private ArrayList<AuthInfo> mInfos;

        AccountArrayAdapter(Context context, int textViewResourceId, ArrayList<AuthInfo> infos) {
            super(context, textViewResourceId, infos);
            this.mInfos = infos;
            this.mLayoutInflater = (LayoutInflater)context.getSystemService("layout_inflater");
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = this.mLayoutInflater.inflate(R.layout.choose_account_row, null);
                holder = new ViewHolder();
                holder.text = (TextView)convertView.findViewById(R.id.account_row_text);
                holder.icon = (ImageView)convertView.findViewById(R.id.account_row_icon);
                convertView.setTag((Object)holder);
            } else {
                holder = (ViewHolder)convertView.getTag();
            }
            holder.text.setText((CharSequence)this.mInfos.get((int)position).name);
            holder.icon.setImageDrawable(this.mInfos.get((int)position).drawable);
            return convertView;
        }
    }

    private static class ViewHolder {
        ImageView icon;
        TextView text;

        private ViewHolder() {
        }
    }

    private static class AuthInfo {
        final AuthenticatorDescription desc;
        final String name;
        final Drawable drawable;

        AuthInfo(AuthenticatorDescription desc, String name, Drawable drawable2) {
            this.desc = desc;
            this.name = name;
            this.drawable = drawable2;
        }
    }
}

