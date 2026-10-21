//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.carlos.common.imagepicker.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.carlos.common.imagepicker.entity.Image;
import com.carlos.libcommon.StringFog;
import com.kook.librelease.R.drawable;
import com.kook.librelease.R.id;
import com.kook.librelease.R.layout;
import java.io.File;
import java.util.List;

public class PreviewImageAdapter extends RecyclerView.Adapter<PreviewImageAdapter.ImageHolder> {
    private Context mContext;
    private List<Image> mImgList;
    public OnItemClcikLitener onItemClcikLitener;

    public void setOnItemClcikLitener(OnItemClcikLitener onItemClcikLitener) {
        this.onItemClcikLitener = onItemClcikLitener;
    }

    public PreviewImageAdapter(Context mContext, List<Image> mImgList) {
        this.mContext = mContext;
        this.mImgList = mImgList;
    }

    public List<Image> getData() {
        return this.mImgList;
    }

    @NonNull
    public ImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final ImageHolder imageHolder = new ImageHolder(LayoutInflater.from(this.mContext).inflate(layout.preview_item, parent, false));
        imageHolder.itemView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (PreviewImageAdapter.this.onItemClcikLitener != null) {
                    PreviewImageAdapter.this.onItemClcikLitener.OnItemClcik(PreviewImageAdapter.this, imageHolder.itemView, imageHolder.getLayoutPosition());
                }

            }
        });
        return imageHolder;
    }

    public void onBindViewHolder(@NonNull ImageHolder holder, int position) {
        String path = ((Image)this.mImgList.get(position)).getPath();
        Uri uri;
        if (path.startsWith("http")) {
            uri = Uri.parse(path);
        } else {
            uri = Uri.fromFile(new File(path));
        }

        Glide.with(this.mContext).setDefaultRequestOptions((new RequestOptions()).dontTransform().placeholder(drawable.ic_image).error(drawable.ic_img_load_fail).override(800, 1200)).load(uri).into(holder.imageView);
    }

    public int getItemCount() {
        return this.mImgList.size();
    }

    class ImageHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;

        ImageHolder(View itemView) {
            super(itemView);
            this.imageView = (ImageView)itemView.findViewById(id.iv_itemimg);
        }
    }

    public interface OnItemClcikLitener {
        void OnItemClcik(PreviewImageAdapter var1, View var2, int var3);
    }
}
