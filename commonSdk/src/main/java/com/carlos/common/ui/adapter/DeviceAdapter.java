//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.carlos.common.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.carlos.common.ui.activity.abs.ui.BaseAdapterPlus;
import com.carlos.common.ui.adapter.bean.DeviceData;
import com.kook.librelease.R.drawable;
import com.kook.librelease.R.id;
import com.kook.librelease.R.layout;
import com.kook.librelease.R.string;

public class DeviceAdapter extends BaseAdapterPlus<DeviceData> {
    public DeviceAdapter(Context context) {
        super(context);
    }

    protected View createView(int position, ViewGroup parent) {
        View view = this.inflate(layout.item_location_app, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        return view;
    }

    protected void attach(View view, DeviceData item, int position) {
        ViewHolder viewHolder = (ViewHolder)view.getTag();
        if (item.icon == null) {
            viewHolder.icon.setImageResource(drawable.ic_about);
        } else {
            viewHolder.icon.setVisibility(View.VISIBLE);
            viewHolder.icon.setImageDrawable(item.icon);
        }

        viewHolder.label.setText(item.name);
        if (item.isMocking()) {
            viewHolder.location.setText(string.mock_device);
        } else {
            viewHolder.location.setText(string.mock_none);
        }

    }

    static class ViewHolder extends BaseAdapterPlus.BaseViewHolder {
        final ImageView icon;
        final TextView label;
        final TextView location;

        public ViewHolder(View view) {
            super(view);
            this.icon = (ImageView)this.$(id.item_app_icon);
            this.label = (TextView)this.$(id.item_app_name);
            this.location = (TextView)this.$(id.item_location);
        }
    }
}
