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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ziqqi.OnItemClickListener;
import com.ziqqi.R;
import com.ziqqi.activities.ProductDetailActivity;
import com.ziqqi.model.homecategorymodel.BestsellerProduct;
import com.ziqqi.model.productvariantmodel.AttributeValue;
import com.ziqqi.utils.Constants;
import com.ziqqi.utils.FontCache;

import java.util.List;

public class VariantAdapter extends RecyclerView.Adapter<VariantAdapter.BestSellerMobileViewHolder> {

    Context context;
    List<AttributeValue> attributeValues;
    int position;
    OnItemClickListener listener;

    public VariantAdapter(Context context, int position, List<AttributeValue> attributeValues, OnItemClickListener listener) {
        this.context = context;
        this.attributeValues = attributeValues;
        this.position = position;
        this.listener = listener;
    }

    @NonNull
    @Override
    public BestSellerMobileViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View inflater = LayoutInflater.from(context).inflate(R.layout.item_variant, viewGroup, false);
        return new VariantAdapter.BestSellerMobileViewHolder(inflater);
    }

    @Override
    public void onBindViewHolder(@NonNull BestSellerMobileViewHolder holder, final int i) {

        if (attributeValues.size() == 0){
            holder.itemView.setLayoutParams(new LinearLayout.LayoutParams(0,0));
            holder.main_layout.setVisibility(View.GONE);

        }else{
            holder.tvValueName.setText(attributeValues.get(i).getValueName());
        }
    }

    @Override
    public int getItemCount() {
        Log.e("BestSellerProduct", " " + attributeValues.size());
        return attributeValues.size();
    }

    public class BestSellerMobileViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvValueName;
        FrameLayout main_layout;

        public BestSellerMobileViewHolder(@NonNull View itemView) {
            super(itemView);

            tvValueName = itemView.findViewById(R.id.tv_variant_value);
            main_layout = itemView.findViewById(R.id.main_layout);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.ll_card:
                    /*Intent intent = new Intent(context, ProductDetailActivity.class);
                    intent.putExtra("product_id", attributeValues.get(getAdapterPosition()).getAttributesValueId());
                    intent.putExtra("variant_id", attributeValues.get(getAdapterPosition()).getVariantId());
                    context.startActivity(intent);*/
                    break;
            }

        }
    }
}