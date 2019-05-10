package com.ziqqi.adapters.filtersAdapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ziqqi.OnItemClickListener;
import com.ziqqi.R;
import com.ziqqi.model.filtermodel.FeatureFilter;

import java.util.List;

public class FilterMainFeatureAdapter extends RecyclerView.Adapter<FilterMainFeatureAdapter.FilterViewModel> {
    Context context;
    List<FeatureFilter> payloadList;
    OnItemClickListener listener;
    FilterFeatureAdapter filterFeatureAdapter;
    boolean isExpanded = false;

    public FilterMainFeatureAdapter(Context context, List<FeatureFilter> payloadList, OnItemClickListener listener) {
        this.context = context;
        this.payloadList = payloadList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FilterMainFeatureAdapter.FilterViewModel onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View inflater = LayoutInflater.from(context).inflate(R.layout.item_filter_category, viewGroup, false);
        return new FilterMainFeatureAdapter.FilterViewModel(inflater);
    }

    @Override
    public void onBindViewHolder(@NonNull final FilterMainFeatureAdapter.FilterViewModel holder, int i) {

        holder.tv_filter_name.setText("By "+payloadList.get(i).getFilterName());
        if (!payloadList.get(i).getFilterValue().isEmpty()) {
            filterFeatureAdapter = new FilterFeatureAdapter(context, i, payloadList.get(i).getFilterValue(), listener);
            holder.recyclerView.setAdapter(filterFeatureAdapter);
        }

        holder.iv_expand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isExpanded){
                    holder.recyclerView.setVisibility(View.VISIBLE);
                    holder.iv_expand.setImageResource(R.drawable.ic_minus);
                    isExpanded = true;
                }else{
                    holder.recyclerView.setVisibility(View.GONE);
                    holder.iv_expand.setImageResource(R.drawable.ic_add_black);
                    isExpanded = false;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return payloadList.size();
    }

    public class FilterViewModel extends RecyclerView.ViewHolder {
        RecyclerView recyclerView;
        LinearLayoutManager manager;
        TextView tv_filter_name;
        ImageView iv_expand;

        public FilterViewModel(@NonNull View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.recycler_view);
            tv_filter_name = itemView.findViewById(R.id.tv_filter_name);
            iv_expand = itemView.findViewById(R.id.iv_expand);
            manager = new LinearLayoutManager(context);
            manager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(manager);
        }
    }
}