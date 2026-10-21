/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.view.LayoutInflater
 *  android.view.View
 *  android.view.View$OnClickListener
 *  android.view.ViewGroup
 *  android.widget.ImageView
 *  androidx.annotation.NonNull
 *  androidx.annotation.RequiresApi
 *  androidx.core.content.ContextCompat
 *  androidx.recyclerview.widget.RecyclerView$Adapter
 *  androidx.recyclerview.widget.RecyclerView$ViewHolder
 *  com.bumptech.glide.Glide
 *  com.bumptech.glide.load.engine.DiskCacheStrategy
 *  com.bumptech.glide.request.RequestOptions
 *  com.kook.librelease.R$drawable
 *  com.kook.librelease.R$id
 *  com.kook.librelease.R$layout
 */
package com.carlos.common.imagepicker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.carlos.common.imagepicker.entity.Image;
import com.kook.librelease.R;
import java.util.List;

public class BottomPreviewAdapter extends RecyclerView.Adapter<BottomPreviewAdapter.CustomeHolder> {
    private Context context;
    private List<Image> imagesList;
    public OnItemClcikLitener onItemClcikLitener;
    public OnDataChangeFinishListener onDataChangeFinishListener;

    public void setOnItemClcikLitener(OnItemClcikLitener onItemClcikLitener) {
        this.onItemClcikLitener = onItemClcikLitener;
    }

    public void setOnDataChangeFinishListener(OnDataChangeFinishListener onDataChangeFinishListener) {
        this.onDataChangeFinishListener = onDataChangeFinishListener;
    }

    public BottomPreviewAdapter(Context context, List<Image> imagesList) {
        this.context = context;
        this.imagesList = imagesList;
    }

    @NonNull
    public CustomeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CustomeHolder(LayoutInflater.from((Context)this.context).inflate(R.layout.bootm_preview_item, parent, false));
    }

    @RequiresApi(api=16)
    public void onBindViewHolder(final @NonNull CustomeHolder holder, int position) {
        this.imagesList.get(position).setSelectPosition(position);
        Glide.with((Context)this.context).load(this.imagesList.get(holder.getAdapterPosition()).getPath()).apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).centerCrop().override(800, 800)).thumbnail(0.5f).into(holder.imageView);
        holder.imageView.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v) {
                for (Image image : BottomPreviewAdapter.this.imagesList) {
                    image.setChecked(false);
                }
                ((Image)BottomPreviewAdapter.this.imagesList.get(holder.getAdapterPosition())).setChecked(true);
                if (BottomPreviewAdapter.this.onItemClcikLitener != null) {
                    int a = holder.getAdapterPosition();
                    BottomPreviewAdapter.this.onItemClcikLitener.OnItemClcik(holder.getAdapterPosition(), (Image)BottomPreviewAdapter.this.imagesList.get(holder.getAdapterPosition()));
                }
            }
        });
        if (this.imagesList.get(position).isChecked()) {
            holder.imageView.setBackground(ContextCompat.getDrawable((Context)this.context, (int)R.drawable.border));
        } else {
            holder.imageView.setBackground(null);
        }
    }

    public int getItemCount() {
        return this.imagesList.size();
    }

    public void referesh(List<Image> newData) {
        this.imagesList = newData;
        this.notifyDataSetChanged();
    }

    class CustomeHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;

        public CustomeHolder(View itemView) {
            super(itemView);
            this.imageView = (ImageView)itemView.findViewById(R.id.bottom_imageview_item);
        }
    }

    public static interface OnDataChangeFinishListener {
        public void changeFinish();
    }

    public static interface OnItemClcikLitener {
        public void OnItemClcik(int var1, Image var2);
    }
}

