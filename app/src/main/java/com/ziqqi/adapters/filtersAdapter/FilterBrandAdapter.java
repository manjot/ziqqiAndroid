package com.ziqqi.adapters.filtersAdapter;

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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ziqqi.FilterItemListener;
import com.ziqqi.OnItemClickListener;
import com.ziqqi.R;
import com.ziqqi.activities.ProductDetailActivity;
import com.ziqqi.model.filtermodel.CategoryFilter;
import com.ziqqi.model.filtermodel.FilterValue;
import com.ziqqi.model.filtermodel.FilterValue_;
import com.ziqqi.model.filterproductmodel.FeatureValue;
import com.ziqqi.model.homecategorymodel.BestsellerProduct;
import com.ziqqi.utils.Constants;
import com.ziqqi.utils.FontCache;

import java.util.List;

public class FilterBrandAdapter extends RecyclerView.Adapter<FilterBrandAdapter.FiltersCategoriesViewHolder>  {
    Context context;
    List<FilterValue_> featureValues;
    int position;
    FilterItemListener listener;

    public FilterBrandAdapter(Context context, int position, List<FilterValue_> featureValues, FilterItemListener listener) {
        this.context = context;
        this.featureValues = featureValues;
        this.position = position;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FilterBrandAdapter.FiltersCategoriesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View inflater = LayoutInflater.from(context).inflate(R.layout.item_filter, viewGroup, false);
        return new FilterBrandAdapter.FiltersCategoriesViewHolder(inflater);
    }

    @Override
    public void onBindViewHolder(@NonNull FilterBrandAdapter.FiltersCategoriesViewHolder holder, final int i) {
        holder.cb_filter.setText(featureValues.get(i).getName());
    }

    @Override
    public int getItemCount() {
        Log.e("BestSellerProduct", " " + featureValues.size());
        return featureValues.size();
    }

    public class FiltersCategoriesViewHolder extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener {
        CheckBox cb_filter;

        public FiltersCategoriesViewHolder(@NonNull View itemView) {
            super(itemView);

            cb_filter = itemView.findViewById(R.id.cb_filter);
            cb_filter.setOnCheckedChangeListener(this);
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            listener.onFilterBrandClick(getAdapterPosition());
        }
    }
}