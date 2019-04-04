package com.ziqqi.adapters;

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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ziqqi.OnCartItemlistener;
import com.ziqqi.OnItemClickListener;
import com.ziqqi.R;
import com.ziqqi.activities.ProductDetailActivity;
import com.ziqqi.model.homecategorymodel.BestsellerProduct;
import com.ziqqi.model.viewwishlistmodel.ViewWishlist;
import com.ziqqi.utils.Constants;
import com.ziqqi.utils.FontCache;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    Context context;
    List<com.ziqqi.model.viewcartmodel.Payload> viewCartList;
    OnCartItemlistener listener;

    public CartAdapter(Context context, List<com.ziqqi.model.viewcartmodel.Payload> viewCartList,OnCartItemlistener listener) {
        this.context = context;
        this.viewCartList = viewCartList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartAdapter.CartViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View inflater = LayoutInflater.from(context).inflate(R.layout.item_cart, viewGroup, false);
        return new CartAdapter.CartViewHolder(inflater);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.CartViewHolder holder, final int i) {
        holder.tvBrandName.setText(viewCartList.get(i).getBrandName());
        holder.tvName.setText(viewCartList.get(i).getName());
        holder.tvDiscountPrice.setText("$ " + viewCartList.get(i).getSalePrice());
        Glide.with(context).load(viewCartList.get(i).getImage()).apply(RequestOptions.placeholderOf(R.drawable.place_holder)).into(holder.ivImage);

    }

    @Override
    public int getItemCount() {
        return viewCartList.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tvName, tvBrandName, tvDiscountPrice;
        ImageView ivImage, ivRemoveCart;
        LinearLayout ll_card;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tv_name);
            ivImage = itemView.findViewById(R.id.iv_product_image);
            tvBrandName = itemView.findViewById(R.id.tv_brand_name);
            tvDiscountPrice = itemView.findViewById(R.id.tv_discount_price);
            ll_card = itemView.findViewById(R.id.ll_card);
            ivRemoveCart = itemView.findViewById(R.id.iv_remove_cart);

            ivRemoveCart.setOnClickListener(this);
            ll_card.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            switch (view.getId()) {
                case R.id.ll_card:
                    Intent intent = new Intent(context, ProductDetailActivity.class);
                    intent.putExtra("product_id", viewCartList.get(getAdapterPosition()).getId());
                    context.startActivity(intent);
                    break;
                case R.id.iv_remove_cart:
                    listener.onCartItemClick(viewCartList.get(getAdapterPosition()), Constants.REMOVE_CART);
                    break;
            }
        }
    }
}
