package com.ziqqi.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ziqqi.OnItemClickListener;
import com.ziqqi.OnVariantItemClickListener;
import com.ziqqi.R;
import com.ziqqi.model.productvariantmodel.AttributeValue;
import com.ziqqi.model.productvariantmodel.Payload;
import com.ziqqi.utils.Constants;
import com.ziqqi.utils.FontCache;
import com.ziqqi.utils.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class VariantMainAdapter extends RecyclerView.Adapter<VariantMainAdapter.BestSellerViewMainHolder> {
    Context context;
    List<Payload> payloadList;
    OnVariantItemClickListener listener;
    VariantAdapter variantAdapter;
    List<AttributeValue> attributeValues = new ArrayList<>();

    public VariantMainAdapter(Context context, List<Payload> payloadList, OnVariantItemClickListener listener) {
        this.context = context;
        this.payloadList = payloadList;
        this.listener = listener;

    }

    @NonNull
    @Override
    public BestSellerViewMainHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View inflater = LayoutInflater.from(context).inflate(R.layout.item_main_variant, viewGroup, false);
        return new BestSellerViewMainHolder(inflater);
    }

    @Override
    public void onBindViewHolder(@NonNull BestSellerViewMainHolder holder, int i) {

        attributeValues = payloadList.get(i).getAttributeValue();

        if (payloadList.get(i).getAttributeValue().size() ==0){
            holder.main_layout.setVisibility(View.GONE);
            holder.main_layout.setLayoutParams(new LinearLayout.LayoutParams(0,0));
//            attributeValues.remove(i);

        }else{
            holder.tv_variant_name.setText(payloadList.get(i).getAttributeName());
            variantAdapter = new VariantAdapter(context, i, attributeValues, listener);
            holder.rv_variants.setAdapter(variantAdapter);
        }
    }

    @Override
    public int getItemCount() {
        return payloadList.size();
    }

    public class BestSellerViewMainHolder extends RecyclerView.ViewHolder {
        RecyclerView rv_variants;
        LinearLayoutManager manager;
        LinearLayout main_layout;
        TextView tv_variant_name;

        public BestSellerViewMainHolder(@NonNull View itemView) {
            super(itemView);
            rv_variants = itemView.findViewById(R.id.rv_variants);
            tv_variant_name = itemView.findViewById(R.id.tv_variant_name);
            main_layout = itemView.findViewById(R.id.main_layout);
            manager = new LinearLayoutManager(context);
            manager.setOrientation(LinearLayoutManager.HORIZONTAL);
            rv_variants.setLayoutManager(manager);
        }
    }
}
