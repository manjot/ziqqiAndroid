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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ziqqi.OnItemClickListener;
import com.ziqqi.R;
import com.ziqqi.model.homecategorymodel.Payload;
import com.ziqqi.utils.FontCache;

import java.util.List;

public class TopCategoriesAdapter extends RecyclerView.Adapter<TopCategoriesAdapter.TopCategoriesViewHolder> {
    Context context;
    List<Payload> homeCategoryList;
    OnItemClickListener listener;
    Typeface medium;
    CircularProgressDrawable drawable;

    public TopCategoriesAdapter(Context context, List<Payload> homeCategoryList, OnItemClickListener listener) {
        this.context = context;
        this.homeCategoryList = homeCategoryList;
        this.listener = listener;
        drawable = new CircularProgressDrawable(context);
        drawable.start();
        medium = FontCache.get(context.getResources().getString(R.string.medium), context);
    }

    @NonNull
    @Override
    public TopCategoriesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View inflater = LayoutInflater.from(context).inflate(R.layout.item_top_categories, viewGroup, false);
        return new TopCategoriesAdapter.TopCategoriesViewHolder(inflater);
    }

    @Override
    public void onBindViewHolder(@NonNull TopCategoriesViewHolder holder, int i) {
        holder.tvTitle.setText(homeCategoryList.get(i).getName());
        Glide.with(context).load(homeCategoryList.get(i).getCategoryImage()).apply(RequestOptions.placeholderOf(drawable)).into(holder.ivImage);
    }

    @Override
    public int getItemCount() {
        return homeCategoryList.size();
    }

    public class TopCategoriesViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImage;
        TextView tvTitle;
        RelativeLayout rlCategory;

        public TopCategoriesViewHolder(@NonNull View itemView) {
            super(itemView);

            ivImage = itemView.findViewById(R.id.iv_image);
            tvTitle = itemView.findViewById(R.id.tv_title);
            rlCategory = itemView.findViewById(R.id.rl_category);
            tvTitle.setTypeface(medium);
            rlCategory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(homeCategoryList.get(getAdapterPosition()).getId());
                }
            });
        }
    }
}
