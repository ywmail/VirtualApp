/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.view.ViewGroup
 *  androidx.recyclerview.widget.RecyclerView
 *  androidx.recyclerview.widget.RecyclerView$Adapter
 *  androidx.recyclerview.widget.RecyclerView$AdapterDataObserver
 *  androidx.recyclerview.widget.RecyclerView$ViewHolder
 */
package com.carlos.common.ui.activity.abs.nestedadapter;

import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAdapterWrapper
extends RecyclerView.Adapter {
    protected final RecyclerView.Adapter wrapped;

    public RecyclerViewAdapterWrapper(RecyclerView.Adapter wrapped) {
        this.wrapped = wrapped;
        this.wrapped.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver(){

            public void onChanged() {
                RecyclerViewAdapterWrapper.this.notifyDataSetChanged();
            }

            public void onItemRangeChanged(int positionStart, int itemCount) {
                RecyclerViewAdapterWrapper.this.notifyItemRangeChanged(positionStart, itemCount);
            }

            public void onItemRangeInserted(int positionStart, int itemCount) {
                RecyclerViewAdapterWrapper.this.notifyItemRangeInserted(positionStart, itemCount);
            }

            public void onItemRangeRemoved(int positionStart, int itemCount) {
                RecyclerViewAdapterWrapper.this.notifyItemRangeRemoved(positionStart, itemCount);
            }

            public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
                RecyclerViewAdapterWrapper.this.notifyItemMoved(fromPosition, toPosition);
            }
        });
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return this.wrapped.onCreateViewHolder(parent, viewType);
    }

    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        this.wrapped.onBindViewHolder(holder, position);
    }

    public int getItemCount() {
        return this.wrapped.getItemCount();
    }

    public int getItemViewType(int position) {
        return this.wrapped.getItemViewType(position);
    }

    public void setHasStableIds(boolean hasStableIds) {
        this.wrapped.setHasStableIds(hasStableIds);
    }

    public long getItemId(int position) {
        return this.wrapped.getItemId(position);
    }

    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        this.wrapped.onViewRecycled(holder);
    }

    public boolean onFailedToRecycleView(RecyclerView.ViewHolder holder) {
        return this.wrapped.onFailedToRecycleView(holder);
    }

    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        this.wrapped.onViewAttachedToWindow(holder);
    }

    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        this.wrapped.onViewDetachedFromWindow(holder);
    }

    public void registerAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        this.wrapped.registerAdapterDataObserver(observer);
    }

    public void unregisterAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        this.wrapped.unregisterAdapterDataObserver(observer);
    }

    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        this.wrapped.onAttachedToRecyclerView(recyclerView);
    }

    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        this.wrapped.onDetachedFromRecyclerView(recyclerView);
    }

    public RecyclerView.Adapter getWrappedAdapter() {
        return this.wrapped;
    }
}

