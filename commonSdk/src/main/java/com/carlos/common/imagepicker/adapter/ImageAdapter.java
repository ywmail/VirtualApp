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
 *  android.widget.Toast
 *  androidx.annotation.NonNull
 *  androidx.annotation.RequiresApi
 *  androidx.recyclerview.widget.RecyclerView$Adapter
 *  androidx.recyclerview.widget.RecyclerView$ViewHolder
 *  com.bumptech.glide.GenericTransitionOptions
 *  com.bumptech.glide.Glide
 *  com.bumptech.glide.TransitionOptions
 *  com.bumptech.glide.load.engine.DiskCacheStrategy
 *  com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
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
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.bumptech.glide.TransitionOptions;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.carlos.common.imagepicker.entity.Image;
import com.kook.librelease.R;
import com.kook.librelease.StringFog;
import java.util.ArrayList;

public class ImageAdapter
extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private ArrayList<Image> mImages;
    private LayoutInflater mInflater;
    private View.OnClickListener onCameraClickListener = null;
    private static final int ITEM_TYPE_CAMERA = 100;
    private static final int ITEM_TYPE_PHOTO = 101;
    private boolean showCamera;
    private ArrayList<Image> mSelectImages = new ArrayList();
    private OnImageSelectListener mSelectListener;
    private OnItemClickListener mItemClickListener;
    private int mMaxCount;
    private boolean isSingle;

    public int getItemViewType(int position) {
        if (this.showCamera && position == 0) {
            return 100;
        }
        return 101;
    }

    public ImageAdapter(Context context, int maxCount, boolean isSingle) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from((Context)this.mContext);
        this.mMaxCount = maxCount;
        this.isSingle = isSingle;
    }

    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 100) {
            CameraHolder cameraHolder = new CameraHolder(this.mInflater.inflate(R.layout.adapter_camera_item, parent, false));
            cameraHolder.itemView.setOnClickListener(new View.OnClickListener(){

                public void onClick(View v) {
                    if (ImageAdapter.this.onCameraClickListener != null) {
                        ImageAdapter.this.onCameraClickListener.onClick(v);
                    }
                }
            });
            return cameraHolder;
        }
        return new ImageHolder(this.mInflater.inflate(R.layout.adapter_images_item, parent, false));
    }

    @RequiresApi(api=16)
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (this.getItemViewType(position) == 101) {
            Image image;
            final ImageHolder imageHolder = (ImageHolder)holder;
            if (this.showCamera) {
                image = this.mImages.get(position - 1);
                image.setPosition(position - 1);
            } else {
                image = this.mImages.get(position);
                image.setPosition(position);
            }
            Glide.with((Context)this.mContext).load(image.getPath()).transition(new GenericTransitionOptions().transition(17432578)).transition((TransitionOptions)new DrawableTransitionOptions().crossFade(150)).apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).centerCrop().placeholder(R.drawable.ic_image).error(R.drawable.ic_img_load_fail)).thumbnail(0.5f).into(imageHolder.ivImage);
            this.setItemSelect(imageHolder, this.mSelectImages.contains(image));
            imageHolder.ivSelectIcon.setOnClickListener(new View.OnClickListener(){

                public void onClick(View v) {
                    if (ImageAdapter.this.mSelectImages.contains(image)) {
                        ImageAdapter.this.unSelectImage(image);
                        ImageAdapter.this.setItemSelect(imageHolder, false);
                    } else if (ImageAdapter.this.isSingle) {
                        ImageAdapter.this.clearImageSelect();
                        ImageAdapter.this.selectImage(image);
                        ImageAdapter.this.setItemSelect(imageHolder, true);
                    } else if (ImageAdapter.this.mMaxCount <= 0 || ImageAdapter.this.mSelectImages.size() < ImageAdapter.this.mMaxCount) {
                        ImageAdapter.this.selectImage(image);
                        ImageAdapter.this.setItemSelect(imageHolder, true);
                    } else if (ImageAdapter.this.mSelectImages.size() == ImageAdapter.this.mMaxCount) {
                        Toast.makeText((Context)ImageAdapter.this.mContext, (CharSequence)("最多只能选" + ImageAdapter.this.mMaxCount + "张"), (int)0).show();
                    }
                }
            });
            holder.itemView.setOnClickListener(new View.OnClickListener(){

                public void onClick(View v) {
                    if (ImageAdapter.this.mItemClickListener != null) {
                        if (ImageAdapter.this.showCamera) {
                            ImageAdapter.this.mItemClickListener.OnItemClick(image, imageHolder.itemView, imageHolder.getAdapterPosition() - 1);
                        } else {
                            ImageAdapter.this.mItemClickListener.OnItemClick(image, imageHolder.itemView, imageHolder.getAdapterPosition());
                        }
                    }
                }
            });
        }
    }

    public void setOnCameraClickListener(View.OnClickListener onCameraClickListener) {
        this.onCameraClickListener = onCameraClickListener;
    }

    private void selectImage(Image image) {
        this.mSelectImages.add(image);
        if (this.mSelectListener != null) {
            this.mSelectListener.OnImageSelect(image, true, this.mSelectImages.size());
        }
    }

    private void unSelectImage(Image image) {
        this.mSelectImages.remove(image);
        if (this.mSelectListener != null) {
            this.mSelectListener.OnImageSelect(image, false, this.mSelectImages.size());
        }
    }

    public int getItemCount() {
        if (this.showCamera) {
            return this.mImages == null ? 0 : this.mImages.size() + 1;
        }
        return this.mImages == null ? 0 : this.mImages.size();
    }

    public ArrayList<Image> getData() {
        return this.mImages;
    }

    public void refresh(ArrayList<Image> data, boolean showCamera) {
        this.showCamera = showCamera;
        this.mImages = data;
        this.notifyDataSetChanged();
    }

    private void setItemSelect(ImageHolder holder, boolean isSelect) {
        if (isSelect) {
            holder.ivSelectIcon.setImageResource(R.drawable.ic_image_select);
            holder.ivMasking.setAlpha(0.5f);
        } else {
            holder.ivSelectIcon.setImageResource(R.drawable.ic_image_un_select);
            holder.ivMasking.setAlpha(0.2f);
        }
    }

    private void clearImageSelect() {
        this.mSelectImages.clear();
        this.notifyDataSetChanged();
    }

    public void setSelectedImages(ArrayList<String> selected) {
        this.mSelectImages.clear();
        if (this.mImages != null && selected != null) {
            block0: for (String path : selected) {
                if (this.isFull()) {
                    return;
                }
                for (Image image : this.mImages) {
                    if (!path.equals(image.getPath())) continue;
                    if (this.mSelectImages.contains(image)) continue block0;
                    this.mSelectImages.add(image);
                    continue block0;
                }
            }
            this.notifyDataSetChanged();
        }
    }

    private boolean isFull() {
        return this.isSingle && this.mSelectImages.size() == 1 || this.mMaxCount > 0 && this.mSelectImages.size() == this.mMaxCount;
    }

    public ArrayList<Image> getSelectImages() {
        return this.mSelectImages;
    }

    public void setOnImageSelectListener(OnImageSelectListener listener) {
        this.mSelectListener = listener;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    public static interface OnItemClickListener {
        public void OnItemClick(Image var1, View var2, int var3);
    }

    public static interface OnImageSelectListener {
        public void OnImageSelect(Image var1, boolean var2, int var3);
    }

    class CameraHolder
    extends RecyclerView.ViewHolder {
        ImageView ivCamera;

        CameraHolder(View itemView) {
            super(itemView);
            this.ivCamera = (ImageView)itemView.findViewById(R.id.iv_camera);
        }
    }

    class ImageHolder
    extends RecyclerView.ViewHolder {
        ImageView ivImage;
        ImageView ivSelectIcon;
        ImageView ivMasking;

        ImageHolder(View itemView) {
            super(itemView);
            this.ivImage = (ImageView)itemView.findViewById(R.id.iv_image);
            this.ivSelectIcon = (ImageView)itemView.findViewById(R.id.iv_select);
            this.ivMasking = (ImageView)itemView.findViewById(R.id.iv_masking);
        }
    }
}

