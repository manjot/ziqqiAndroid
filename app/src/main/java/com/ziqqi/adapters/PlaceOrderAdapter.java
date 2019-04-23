package com.ziqqi.adapters;

import android.support.v7.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ziqqi.OnCartItemlistener;
import com.ziqqi.OnItemClickListener;
import com.ziqqi.R;
import com.ziqqi.activities.ProductDetailActivity;
import com.ziqqi.model.homecategorymodel.BestsellerProduct;
import com.ziqqi.model.placeordermodel.Payload;
import com.ziqqi.model.viewwishlistmodel.ViewWishlist;
import com.ziqqi.utils.Constants;
import com.ziqqi.utils.FontCache;

import java.util.List;

public class PlaceOrderAdapter extends RecyclerView.Adapter<PlaceOrderAdapter.CartViewHolder> {

    Context context;
    List<Payload> viewCartList;
    OnCartItemlistener listener;

    public PlaceOrderAdapter(Context context, List<Payload> viewCartList, OnCartItemlistener listener) {
        this.context = context;
        this.viewCartList = viewCartList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PlaceOrderAdapter.CartViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View inflater = LayoutInflater.from(context).inflate(R.layout.item_cart, viewGroup, false);
        return new PlaceOrderAdapter.CartViewHolder(inflater);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceOrderAdapter.CartViewHolder holder, final int i) {
//        holder.tvBrandName.setText(viewCartList.get(i).get);
        holder.tvName.setText(viewCartList.get(i).getProductName());
        holder.tvDiscountPrice.setText("$ " + viewCartList.get(i).getDiscountAmount());
        holder.tvQuantity.setText(viewCartList.get(i).getQty());
        Glide.with(context).load(viewCartList.get(i).getId()).apply(RequestOptions.placeholderOf(R.drawable.place_holder)).into(holder.ivImage);

    }

    @Override
    public int getItemCount() {
        return viewCartList.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder{
        TextView tvName, tvBrandName, tvDiscountPrice, tvQuantity;
        ImageView ivImage, ivRemoveCart, ivIncrease, ivDecrease;
        LinearLayout ll_card;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tv_name);
            ivImage = itemView.findViewById(R.id.iv_product_image);
            tvBrandName = itemView.findViewById(R.id.tv_brand_name);
            tvDiscountPrice = itemView.findViewById(R.id.tv_discount_price);
            ll_card = itemView.findViewById(R.id.ll_card);
            ivRemoveCart = itemView.findViewById(R.id.iv_remove_cart);
            tvQuantity = itemView.findViewById(R.id.tv_quantity);
            ivIncrease = itemView.findViewById(R.id.iv_increase);
            ivDecrease = itemView.findViewById(R.id.iv_decrease);
        }
    }
}
