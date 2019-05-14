package com.ziqqi.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.ziqqi.OnItemClickListener;
import com.ziqqi.R;
import com.ziqqi.activities.ProductDetailActivity;
import com.ziqqi.model.myordersmodel.Payload;

import java.util.ArrayList;
import java.util.List;

public class MyOrdersAdapter extends RecyclerView.Adapter<MyOrdersAdapter.MyOrdedersViewHolder> implements Filterable {

    Context context;
    List<Payload> payloadList;
    List<Payload> filteredPayload;
    OnItemClickListener listener;
    ArrayList<String> names = new ArrayList<>();

    public MyOrdersAdapter(Context context, List<Payload> payloadList, OnItemClickListener listener) {
        this.context = context;
        this.payloadList = payloadList;
        filteredPayload = payloadList;
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
        //names.add(payloadList.get(i).getProductName());
        holder.tvBrandName.setText(filteredPayload.get(i).getBrandName());
        holder.tvName.setText(filteredPayload.get(i).getProductName());
        holder.tvDiscountPrice.setText("$ " + filteredPayload.get(i).getPrice());
        holder.tvStatus.setText(filteredPayload.get(i).getStatus());
        Glide.with(context).load(filteredPayload.get(i).getProductImage()).apply(RequestOptions.placeholderOf(R.drawable.place_holder)).into(holder.ivImage);

        holder.ll_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductDetailActivity.class);
                intent.putExtra("product_id", payloadList.get(i).getProductId());
                intent.putExtra("variant_id", payloadList.get(i).getVariantId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return filteredPayload.size();
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

    public void filterList(List<Payload> filterdNames) {
        payloadList.clear();
        payloadList = filterdNames;
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                Log.e("CharSeq : ", charString);
                if (charString.isEmpty()) {
                    filteredPayload = payloadList;
                } else {
                    List<Payload> filteredList = new ArrayList<>();
                    for (Payload row : payloadList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getProductName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    filteredPayload = filteredList;
                    Log.e("CharSeq Row : ", new Gson().toJson(filteredPayload));
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredPayload;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredPayload = (ArrayList<Payload>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
