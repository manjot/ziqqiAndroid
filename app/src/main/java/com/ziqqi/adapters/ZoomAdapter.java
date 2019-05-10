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
import android.widget.Toast;

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

public class ZoomAdapter extends RecyclerView.Adapter<ZoomAdapter.CartViewHolder> {

    Context context;
    List<String> payloadList;
    OnCartItemlistener listener;

    public ZoomAdapter(Context context, List<String> payloadList,OnCartItemlistener listener) {
        this.context = context;
        this.payloadList = payloadList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ZoomAdapter.CartViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View inflater = LayoutInflater.from(context).inflate(R.layout.item_product_zoom, viewGroup, false);
        return new ZoomAdapter.CartViewHolder(inflater);
    }

    @Override
    public void onBindViewHolder(@NonNull ZoomAdapter.CartViewHolder holder, final int i) {
        Glide.with(context).load(payloadList.get(i)).apply(RequestOptions.placeholderOf(R.drawable.place_holder)).into(holder.ivImage);

    }

    @Override
    public int getItemCount() {
        return payloadList.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder{
        ImageView ivImage;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.image);
        }
    }
}