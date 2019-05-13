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
import android.widget.TextView;

import com.ziqqi.OnItemClickListener;
import com.ziqqi.R;
import com.ziqqi.model.productvariantmodel.Payload;
import com.ziqqi.utils.Constants;
import com.ziqqi.utils.FontCache;
import com.ziqqi.utils.SpacesItemDecoration;

import java.util.List;

public class VariantMainAdapter extends RecyclerView.Adapter<VariantMainAdapter.BestSellerViewMainHolder> {
    Context context;
    List<Payload> payloadList;
    OnItemClickListener listener;
    BestSellerAdapter bestSellerAdapter;

    public VariantMainAdapter(Context context, List<Payload> payloadList, OnItemClickListener listener) {
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
        if (!payloadList.isEmpty()) {
            holder.tv_variant_name.setText(payloadList.get(i).getAttributeName());
//            bestSellerAdapter = new BestSellerAdapter(context, i, payloadList.get(i).getBestsellerProduct(), listener);
//            holder.recyclerView.setAdapter(bestSellerAdapter);
        }
    }

    @Override
    public int getItemCount() {
        return payloadList.size();
    }

    public class BestSellerViewMainHolder extends RecyclerView.ViewHolder {
        RecyclerView rv_variants;
        LinearLayoutManager manager;
        TextView tv_variant_name;

        public BestSellerViewMainHolder(@NonNull View itemView) {
            super(itemView);
            rv_variants = itemView.findViewById(R.id.rv_variants);
            tv_variant_name = itemView.findViewById(R.id.tv_variant_name);
            manager = new LinearLayoutManager(context);
            manager.setOrientation(LinearLayoutManager.HORIZONTAL);
            rv_variants.setLayoutManager(manager);
        }
    }
}
