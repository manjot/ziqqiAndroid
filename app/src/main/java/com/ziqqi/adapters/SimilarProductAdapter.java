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
import com.ziqqi.activities.ProductDetailActivity;
import com.ziqqi.model.similarproductsmodel.Payload;
import com.ziqqi.utils.FontCache;

import java.util.List;

public class SimilarProductAdapter extends RecyclerView.Adapter<SimilarProductAdapter.SimilarViewHolder> {

    Context context;
    List<Payload> payloadList;
    OnItemClickListener listener;
    Typeface regular, medium, light, bold;

    public SimilarProductAdapter(Context context, List<Payload> payloadList, OnItemClickListener listener) {
        this.context = context;
        this.payloadList = payloadList;
        this.listener = listener;
        Resources resources = context.getResources();
        regular = FontCache.get(resources.getString(R.string.regular), context);
        medium = FontCache.get(resources.getString(R.string.medium), context);
        light = FontCache.get(resources.getString(R.string.light), context);
        bold = FontCache.get(resources.getString(R.string.bold), context);
    }

    @NonNull
    @Override
    public SimilarProductAdapter.SimilarViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View inflater = LayoutInflater.from(context).inflate(R.layout.item_best_seller, viewGroup, false);
        return new SimilarProductAdapter.SimilarViewHolder(inflater);
    }

    @Override
    public void onBindViewHolder(@NonNull SimilarProductAdapter.SimilarViewHolder holder,final int i) {
        holder.tvBrandName.setText(payloadList.get(i).getBrandName());
        holder.tvName.setText(payloadList.get(i).getName());
        holder.tvDesc.setText(payloadList.get(i).getSku());
        holder.tvMarketPrice.setText("$ " + payloadList.get(i).getMrpPrice());
        holder.tvDiscountPrice.setText("$ " + payloadList.get(i).getSalePrice());
        if (payloadList.get(i).getImage().size() > 0){
            Glide.with(context).load(payloadList.get(i).getImage().get(0)).into(holder.ivImage);
        }

        holder.ll_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductDetailActivity.class );
                intent.putExtra("product_id", payloadList.get(i).getId());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return payloadList.size();
    }

    public class SimilarViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvBrandName, tvDesc, tvMarketPrice, tvDiscountPrice;
        ImageView ivImage;
        LinearLayout ll_card;

        public SimilarViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tv_name);
            tvDesc = itemView.findViewById(R.id.tv_desc);
            ivImage = itemView.findViewById(R.id.iv_image);
            tvBrandName = itemView.findViewById(R.id.tv_brand_name);
            tvMarketPrice = itemView.findViewById(R.id.tv_market_price);
            tvDiscountPrice = itemView.findViewById(R.id.tv_discount_price);
            ll_card = itemView.findViewById(R.id.ll_card);

            tvBrandName.setTypeface(regular);
            tvName.setTypeface(medium);
            tvDesc.setTypeface(light);
            tvMarketPrice.setTypeface(regular);
            tvDiscountPrice.setTypeface(bold);
        }
    }
}
