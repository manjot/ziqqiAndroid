package com.ziqqi.utils;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.DimenRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ziqqi.fragments.HomeFragment;

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {

    private int mItemOffset;
    private Fragment fragment;

    public SpacesItemDecoration(int itemOffset) {
        mItemOffset = itemOffset;
    }

    public SpacesItemDecoration(@NonNull Context context, @DimenRes int itemOffsetId) {
        this(context.getResources().getDimensionPixelSize(itemOffsetId));
    }

    public SpacesItemDecoration(@NonNull Context context, @DimenRes int itemOffsetId, Fragment fragment) {
        this(context.getResources().getDimensionPixelSize(itemOffsetId));
        this.fragment = fragment;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (fragment instanceof HomeFragment) {
            int position = parent.getChildViewHolder(view).getAdapterPosition();
            int itemCount = state.getItemCount();
            for (int i = 0; i < itemCount; i++) {
                if (position == 0)
                    outRect.set(0, 0, mItemOffset, mItemOffset);
                if (position == 1)
                    outRect.set(0, 0, 0, mItemOffset);
                if (position == 2)
                    outRect.set(0, 0, mItemOffset, 0);
            }
        } else {
            outRect.set(mItemOffset, mItemOffset, mItemOffset, mItemOffset);
        }
    }
}