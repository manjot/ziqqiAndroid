package com.ziqqi.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ziqqi.OnAllItemClickListener;
import com.ziqqi.OnItemClickListener;
import com.ziqqi.R;
import com.ziqqi.activities.ProductDetailActivity;
import com.ziqqi.model.productcategorymodel.Payload;
import com.ziqqi.utils.Constants;
import com.ziqqi.utils.FontCache;

import java.util.List;

public class ViewAllProductAdapter extends RecyclerView.Adapter<ViewAllProductAdapter.ViewAllViewHolder> {
    Context context;
    List<Payload> payloadList;
    OnAllItemClickListener listener;
    Typeface regular, medium, light, bold;

    public ViewAllProductAdapter(Context context, List<Payload> payloadList, OnAllItemClickListener listener) {
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
    public ViewAllViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View inflater = LayoutInflater.from(context).inflate(R.layout.item_best_seller, viewGroup, false);
        return new ViewAllViewHolder(inflater);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewAllViewHolder holder, int i) {
        holder.tvBrandName.setText(payloadList.get(i).getBrandName());
        holder.tvName.setText(payloadList.get(i).getName());
        holder.tvDesc.setText(Html.fromHtml(payloadList.get(i).getSku()));
        holder.tvMarketPrice.setText("$ " + payloadList.get(i).getMrpPrice());
        holder.tvDiscountPrice.setText("$ " + payloadList.get(i).getSalePrice());
        Glide.with(context).load(payloadList.get(i).getImage()).apply(RequestOptions.placeholderOf(R.drawable.place_holder)).into(holder.ivImage);
    }

    @Override
    public int getItemCount() {
        return payloadList.size();
    }

    public class ViewAllViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvName, tvBrandName, tvDesc, tvMarketPrice, tvDiscountPrice;
        ImageView ivImage, ivCart, ivShare, ivWishList;
        LinearLayout ll_card;

        public ViewAllViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tv_name);
            tvDesc = itemView.findViewById(R.id.tv_desc);
            ivImage = itemView.findViewById(R.id.iv_image);
            tvBrandName = itemView.findViewById(R.id.tv_brand_name);
            tvMarketPrice = itemView.findViewById(R.id.tv_market_price);
            tvDiscountPrice = itemView.findViewById(R.id.tv_discount_price);
            ivWishList = itemView.findViewById(R.id.iv_wishlist);
            ivShare = itemView.findViewById(R.id.iv_share);
            ivCart = itemView.findViewById(R.id.iv_cart);
            ll_card = itemView.findViewById(R.id.ll_card);

            tvBrandName.setTypeface(regular);
            tvName.setTypeface(medium);
            tvDesc.setTypeface(light);
            tvMarketPrice.setTypeface(regular);
            tvDiscountPrice.setTypeface(bold);

            ivWishList.setOnClickListener(this);
            ivShare.setOnClickListener(this);
            ivCart.setOnClickListener(this);
            ll_card.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.ll_card:
                    Intent intent = new Intent(context, ProductDetailActivity.class);
                    intent.putExtra("product_id", payloadList.get(getAdapterPosition()).getProductId());
                    context.startActivity(intent);
                    break;
                case R.id.iv_share:
                    listener.onItemClick(payloadList.get(getAdapterPosition()), Constants.SHARE);
                    break;
                case R.id.iv_cart:
                    listener.onItemClick(payloadList.get(getAdapterPosition()), Constants.CART);
                    break;
                case R.id.iv_wishlist:
                    listener.onItemClick(payloadList.get(getAdapterPosition()), Constants.WISH_LIST);
                    break;
            }
        }
    }

    public void setPayloadList(List<Payload> payloadList) {
        this.payloadList = payloadList;
        notifyDataSetChanged();
    }

}
