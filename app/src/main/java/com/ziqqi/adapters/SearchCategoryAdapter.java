package com.ziqqi.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.ziqqi.OnItemClickListener;
import com.ziqqi.R;
import com.ziqqi.activities.SearchResultActivity;
import com.ziqqi.utils.FontCache;

import java.util.List;

public class SearchCategoryAdapter extends RecyclerView.Adapter<SearchCategoryAdapter.SearchCategoryViewHolder> {
    Context context;
    List<com.ziqqi.model.searchcategorymodel.Payload> payloadList;
    OnItemClickListener listener;
    Typeface regular, medium, light, bold;


    public SearchCategoryAdapter(Context context, List<com.ziqqi.model.searchcategorymodel.Payload> payloadList, OnItemClickListener listener) {
        this.context = context;
        this.payloadList = payloadList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SearchCategoryAdapter.SearchCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View inflater = LayoutInflater.from(context).inflate(R.layout.item_search_category, viewGroup, false);
        return new SearchCategoryAdapter.SearchCategoryViewHolder(inflater);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchCategoryAdapter.SearchCategoryViewHolder holder, final int i) {
        holder.tvName.setText(payloadList.get(i).getCategoryName());
        holder.ll_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SearchResultActivity.class);
                intent.putExtra("category_id" , payloadList.get(i).getCategoryId());
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return payloadList.size();
    }

    public class SearchCategoryViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        LinearLayout ll_card;

        public SearchCategoryViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tv_name);
            ll_card = itemView.findViewById(R.id.ll_card);

        }
    }
}
