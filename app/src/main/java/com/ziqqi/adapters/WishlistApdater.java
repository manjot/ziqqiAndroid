package com.ziqqi.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ziqqi.OnWishlistItemClick;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ziqqi.R;
import com.ziqqi.activities.ProductDetailActivity;
import com.ziqqi.utils.Constants;

import java.util.List;

public class WishlistApdater extends RecyclerView.Adapter<WishlistApdater.WishListViewHolder> {

    Context context;
    List<com.ziqqi.model.viewwishlistmodel.Payload> viewWishlistList;
    OnWishlistItemClick listener;


    public WishlistApdater(Context context, List<com.ziqqi.model.viewwishlistmodel.Payload> viewWishlistList,OnWishlistItemClick listener) {
        this.context = context;
        this.viewWishlistList = viewWishlistList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public WishlistApdater.WishListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View inflater = LayoutInflater.from(context).inflate(R.layout.item_wishlist, viewGroup, false);
        return new WishlistApdater.WishListViewHolder(inflater);
    }

    @Override
    public void onBindViewHolder(@NonNull WishlistApdater.WishListViewHolder holder, final int i) {
        holder.tvBrandName.setText(viewWishlistList.get(i).getBrandName());
        holder.tvName.setText(viewWishlistList.get(i).getName());
        holder.tvDiscountPrice.setText("$ " + viewWishlistList.get(i).getSalePrice());
        Glide.with(context).load(viewWishlistList.get(i).getImage()).apply(RequestOptions.placeholderOf(R.drawable.place_holder)).into(holder.ivImage);

        holder.ll_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductDetailActivity.class );
                intent.putExtra("product_id", viewWishlistList.get(i).getProductId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return viewWishlistList.size();
    }

    public class WishListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {
        TextView tvName, tvBrandName, tvDiscountPrice;
        ImageView ivImage, iv_cart;
        LinearLayout ll_card;

        public WishListViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tv_name);
            ivImage = itemView.findViewById(R.id.iv_product_image);
            iv_cart = itemView.findViewById(R.id.iv_cart);
            tvBrandName = itemView.findViewById(R.id.tv_brand_name);
            tvDiscountPrice = itemView.findViewById(R.id.tv_discount_price);
            ll_card = itemView.findViewById(R.id.ll_card);

            iv_cart.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.iv_cart:
                    listener.onItemClick(viewWishlistList.get(getAdapterPosition()), Constants.CART);
                    break;
            }
        }
    }
}
