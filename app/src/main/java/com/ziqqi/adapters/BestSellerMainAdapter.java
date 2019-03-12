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
import com.ziqqi.model.homecategorymodel.Payload;
import com.ziqqi.utils.Constants;
import com.ziqqi.utils.FontCache;
import com.ziqqi.utils.SpacesItemDecoration;

import java.util.List;

public class BestSellerMainAdapter extends RecyclerView.Adapter<BestSellerMainAdapter.BestSellerViewMainHolder> {
    Context context;
    List<Payload> payloadList;
    OnItemClickListener listener;
    BestSellerAdapter bestSellerAdapter;
    SpacesItemDecoration spacesItemDecoration;
    Typeface medium, regular;

    public BestSellerMainAdapter(Context context, List<Payload> payloadList, OnItemClickListener listener) {
        this.context = context;
        this.payloadList = payloadList;
        this.listener = listener;
        spacesItemDecoration = new SpacesItemDecoration(context, R.dimen.dp_4);
        medium = FontCache.get(context.getResources().getString(R.string.medium), context);
        regular = FontCache.get(context.getResources().getString(R.string.regular), context);

    }

    @NonNull
    @Override
    public BestSellerViewMainHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View inflater = LayoutInflater.from(context).inflate(R.layout.best_seller_recycler_view, viewGroup, false);
        return new BestSellerViewMainHolder(inflater);
    }

    @Override
    public void onBindViewHolder(@NonNull BestSellerViewMainHolder holder, int i) {
        if (!payloadList.get(i).getBestsellerProduct().isEmpty()) {
            holder.tvBestSellerTitle.setText(payloadList.get(i).getName());
            Log.e("IDDDDDD", payloadList.get(i).getId());
            bestSellerAdapter = new BestSellerAdapter(context, i, payloadList.get(i).getBestsellerProduct(), listener);
            holder.recyclerView.setAdapter(bestSellerAdapter);
        }
    }

    @Override
    public int getItemCount() {
        return payloadList.size();
    }

    public class BestSellerViewMainHolder extends RecyclerView.ViewHolder {
        RecyclerView recyclerView;
        LinearLayoutManager manager;
        TextView tvBestSellerTitle, tvViewAll, tvBestSellers;

        public BestSellerViewMainHolder(@NonNull View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.recycler_view);
            tvBestSellerTitle = itemView.findViewById(R.id.tv_best_seller_title);
            tvBestSellers = itemView.findViewById(R.id.tv_best_sellers);
            tvViewAll = itemView.findViewById(R.id.tv_view_all_mobiles);
            manager = new LinearLayoutManager(context);
            manager.setOrientation(LinearLayoutManager.HORIZONTAL);
            recyclerView.setLayoutManager(manager);
            recyclerView.addItemDecoration(spacesItemDecoration);

            tvBestSellerTitle.setTypeface(medium);
            tvBestSellers.setTypeface(regular);
            tvViewAll.setTypeface(regular);
            tvViewAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(payloadList.get(getAdapterPosition()).getId(), Constants.VIEW_ALL);
                }
            });
        }
    }
}
