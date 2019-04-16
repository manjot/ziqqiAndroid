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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ziqqi.OnItemClickListener;
import com.ziqqi.R;
import com.ziqqi.activities.ProductDetailActivity;
import com.ziqqi.model.myordersmodel.Payload;

import java.util.List;

public class MyOrdersAdapter extends RecyclerView.Adapter<MyOrdersAdapter.MyOrdedersViewHolder> {

    Context context;
    List<Payload> payloadList;
    OnItemClickListener listener;

    public MyOrdersAdapter(Context context, List<Payload> payloadList, OnItemClickListener listener) {
        this.context = context;
        this.payloadList = payloadList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyOrdersAdapter.MyOrdedersViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View inflater = LayoutInflater.from(context).inflate(R.layout.item_my_order, viewGroup, false);
        return new MyOrdersAdapter.MyOrdedersViewHolder(inflater);
    }

    @Override
    public void onBindViewHolder(@NonNull MyOrdersAdapter.MyOrdedersViewHolder holder, final int i) {
        holder.tvBrandName.setText(payloadList.get(i).getBrandName());
        holder.tvName.setText(payloadList.get(i).getProductName());
        holder.tvDiscountPrice.setText("$ " + payloadList.get(i).getPrice());
        holder.tvStatus.setText(payloadList.get(i).getStatus());
        Glide.with(context).load(payloadList.get(i).getProductImage()).apply(RequestOptions.placeholderOf(R.drawable.place_holder)).into(holder.ivImage);

        holder.ll_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductDetailActivity.class );
                intent.putExtra("product_id", payloadList.get(i).getProductId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return payloadList.size();
    }

    public class MyOrdedersViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvBrandName, tvDiscountPrice, tvStatus;
        ImageView ivImage;
        LinearLayout ll_card;

        public MyOrdedersViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tv_name);
            ivImage = itemView.findViewById(R.id.iv_product_image);
            tvBrandName = itemView.findViewById(R.id.tv_brand_name);
            tvDiscountPrice = itemView.findViewById(R.id.tv_discount_price);
            tvStatus = itemView.findViewById(R.id.tv_status);
            ll_card = itemView.findViewById(R.id.ll_card);
        }
    }
}
