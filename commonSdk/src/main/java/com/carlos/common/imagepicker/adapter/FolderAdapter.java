/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.annotation.SuppressLint
 *  android.content.Context
 *  android.view.LayoutInflater
 *  android.view.View
 *  android.view.View$OnClickListener
 *  android.view.ViewGroup
 *  android.widget.ImageView
 *  android.widget.TextView
 *  androidx.annotation.NonNull
 *  androidx.recyclerview.widget.RecyclerView$Adapter
 *  androidx.recyclerview.widget.RecyclerView$ViewHolder
 *  com.bumptech.glide.Glide
 *  com.bumptech.glide.load.engine.DiskCacheStrategy
 *  com.bumptech.glide.request.RequestOptions
 *  com.kook.librelease.R$id
 *  com.kook.librelease.R$layout
 */
package com.carlos.common.imagepicker.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.carlos.common.imagepicker.entity.Folder;
import com.carlos.common.imagepicker.entity.Image;
import com.kook.librelease.R;
import com.kook.librelease.StringFog;
import java.io.File;
import java.util.ArrayList;

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<Folder> mFolders;
    private LayoutInflater mInflater;
    private int mSelectItem;
    private OnFolderSelectListener mListener;

    public FolderAdapter(Context context, ArrayList<Folder> folders) {
        this.mContext = context;
        this.mFolders = folders;
        this.mInflater = LayoutInflater.from((Context)context);
    }

    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = this.mInflater.inflate(R.layout.adapter_folder, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint(value={"SetTextI18n"})
    public void onBindViewHolder(final @NonNull ViewHolder holder, int position) {
        final Folder folder = this.mFolders.get(position);
        ArrayList<Image> images = folder.getImages();
        holder.tvFolderName.setText((CharSequence)folder.getName());
        holder.ivSelect.setVisibility(this.mSelectItem == position ? View.VISIBLE : View.GONE);
        if (images != null && !images.isEmpty()) {
            holder.tvFolderSize.setText((CharSequence)(images.size() + "张"));
            Glide.with((Context)this.mContext).load(new File(images.get(0).getPath())).apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE)).into(holder.ivImage);
        } else {
            holder.tvFolderSize.setText((CharSequence)"0张");
            holder.ivImage.setImageBitmap(null);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v) {
                FolderAdapter.this.mSelectItem = holder.getAdapterPosition();
                FolderAdapter.this.notifyDataSetChanged();
                if (FolderAdapter.this.mListener != null) {
                    FolderAdapter.this.mListener.OnFolderSelect(folder);
                }
            }
        });
    }

    public int getItemCount() {
        return this.mFolders == null ? 0 : this.mFolders.size();
    }

    public void setOnFolderSelectListener(OnFolderSelectListener listener) {
        this.mListener = listener;
    }

    public static interface OnFolderSelectListener {
        public void OnFolderSelect(Folder var1);
    }

    static class ViewHolder
    extends RecyclerView.ViewHolder {
        ImageView ivImage;
        ImageView ivSelect;
        TextView tvFolderName;
        TextView tvFolderSize;

        ViewHolder(View itemView) {
            super(itemView);
            this.ivImage = (ImageView)itemView.findViewById(R.id.iv_image);
            this.ivSelect = (ImageView)itemView.findViewById(R.id.iv_select);
            this.tvFolderName = (TextView)itemView.findViewById(R.id.tv_folder_name);
            this.tvFolderSize = (TextView)itemView.findViewById(R.id.tv_folder_size);
        }
    }
}

