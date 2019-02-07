package com.ziqqi.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
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

public class SubCategoryAdapter extends RecyclerView.Adapter {
    List<Payload> subCategoriesList;
    Context context;
    OnItemClickListener listener;
    Typeface medium;

    public SubCategoryAdapter(Context context, List<Payload> subCategoriesList, OnItemClickListener listener) {
        this.subCategoriesList = subCategoriesList;
        this.context = context;
        this.listener = listener;
        medium = FontCache.get(context.getResources().getString(R.string.medium), context);
    }

    @NonNull
    @Override
    public SubCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View inflater;
        if (i == 3) {
            inflater = LayoutInflater.from(context).inflate(R.layout.last_grid_sub_category, viewGroup, false);
            return new SubCategoryViewHolder(inflater);
        } else {
            inflater = LayoutInflater.from(context).inflate(R.layout.item_grid_sub_categories, viewGroup, false);
            return new SubCategoryViewHolder(inflater);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {
        if (holder instanceof SubCategoryViewHolder) {
            ((SubCategoryViewHolder) holder).tvTitle.setText(subCategoriesList.get(i).getName());
            Glide.with(context).load(subCategoriesList.get(i).getCategoryImage()).apply(RequestOptions.placeholderOf(R.drawable.place_holder)).into(((SubCategoryViewHolder) holder).ivImage);
        } else {
            ((LastGridViewHolder) holder).tvTitle.setText(subCategoriesList.get(i).getName());
            Glide.with(context).load(subCategoriesList.get(i).getCategoryImage()).apply(RequestOptions.placeholderOf(R.drawable.place_holder)).into(((LastGridViewHolder) holder).ivImage);
        }
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

    public class LastGridViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        ImageView ivImage;

        public LastGridViewHolder(@NonNull View itemView) {
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
