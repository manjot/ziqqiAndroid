package com.ziqqi.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.widget.CircularProgressDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ziqqi.OnItemClickListener;
import com.ziqqi.R;
import com.ziqqi.model.homecategorymodel.Payload;
import com.ziqqi.utils.FontCache;

import java.util.List;

public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.SubCategoryViewHolder> {
    List<Payload> subCategoriesList;
    Context context;
    OnItemClickListener listener;
    Typeface medium;
    CircularProgressDrawable drawable;

    public SubCategoryAdapter(Context context, List<Payload> subCategoriesList, OnItemClickListener listener) {
        this.subCategoriesList = subCategoriesList;
        this.context = context;
        this.listener = listener;
        medium = FontCache.get(context.getResources().getString(R.string.medium), context);
        drawable = new CircularProgressDrawable(context);

    }

    @NonNull
    @Override
    public SubCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View inflater = LayoutInflater.from(context).inflate(R.layout.item_grid_sub_categories, viewGroup, false);
        return new SubCategoryViewHolder(inflater);
    }

    @Override
    public void onBindViewHolder(@NonNull SubCategoryViewHolder holder, int i) {
        holder.tvTitle.setText(subCategoriesList.get(i).getName());
        Glide.with(context).load(subCategoriesList.get(i).getCategoryImage()).apply(RequestOptions.placeholderOf(drawable)).into(holder.ivImage);
    }

    @Override
    public int getItemCount() {
        return subCategoriesList.size();
    }

    public class SubCategoryViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        ImageView ivImage;

        public SubCategoryViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tv_sub_category_title);
            ivImage = itemView.findViewById(R.id.iv_sub_category_image);

            tvTitle.setTypeface(medium);
        }
    }

    @Override
    public int getItemViewType(int position) {
        int position1 = position + 1;
        if (position1 == subCategoriesList.size()) {
            if (position1 % 3 == 1) {
                return 3;
            } else if (position1 % 3 == 2) {
                return 2;
            } else {
                return 1;
            }
        } else {
            return 1;
        }
    }
}
