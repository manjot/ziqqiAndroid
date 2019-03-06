package com.ziqqi.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ziqqi.R;
import com.ziqqi.activities.ProductDetailActivity;
import com.ziqqi.model.homecategorymodel.BestsellerProduct;
import com.ziqqi.utils.FontCache;

import java.util.List;

public class BestSellerAdapter extends RecyclerView.Adapter<BestSellerAdapter.BestSellerMobileViewHolder> {

    Context context;
    List<BestsellerProduct> bestsellerProductList;
    int position;
    Typeface regular, medium, light, bold;

    public BestSellerAdapter(Context context, int position, List<BestsellerProduct> bestsellerProductList) {
        this.context = context;
        this.bestsellerProductList = bestsellerProductList;
        this.position = position;
        Resources resources = context.getResources();
        regular = FontCache.get(resources.getString(R.string.regular), context);
        medium = FontCache.get(resources.getString(R.string.medium), context);
        light = FontCache.get(resources.getString(R.string.light), context);
        bold = FontCache.get(resources.getString(R.string.bold), context);
    }

    @NonNull
    @Override
    public BestSellerMobileViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View inflater = LayoutInflater.from(context).inflate(R.layout.item_best_seller, viewGroup, false);
        return new BestSellerAdapter.BestSellerMobileViewHolder(inflater);
    }

    @Override
    public void onBindViewHolder(@NonNull BestSellerMobileViewHolder holder, final int i) {
        holder.tvBrandName.setText(bestsellerProductList.get(i).getBrandName());
        holder.tvName.setText(bestsellerProductList.get(i).getName());
        holder.tvDesc.setText(Html.fromHtml(bestsellerProductList.get(i).getSku()));
        holder.tvMarketPrice.setText("$ " + bestsellerProductList.get(i).getMrpPrice());
        holder.tvDiscountPrice.setText("$ " + bestsellerProductList.get(i).getSalePrice());
        Glide.with(context).load(bestsellerProductList.get(i).getImage()).apply(RequestOptions.placeholderOf(R.drawable.place_holder)).into(holder.ivImage);

        holder.ll_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductDetailActivity.class );
                intent.putExtra("product_id", bestsellerProductList.get(i).getProductId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        Log.e("BestSellerProduct", " " + bestsellerProductList.size());
        return bestsellerProductList.size();
    }

    public class BestSellerMobileViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvBrandName, tvDesc, tvMarketPrice, tvDiscountPrice;
        ImageView ivImage;
        LinearLayout ll_card;

        public BestSellerMobileViewHolder(@NonNull View itemView) {
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
