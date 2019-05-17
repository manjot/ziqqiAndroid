package com.ziqqi.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ziqqi.OnItemClickListener;
import com.ziqqi.R;
import com.ziqqi.activities.ProductDetailActivity;
import com.ziqqi.model.loadvariantmodel.Review;
import com.ziqqi.model.productdetailsmodel.Payload;
import com.ziqqi.utils.FontCache;

import java.util.List;

public class FeedbackVariantAdapter extends RecyclerView.Adapter<FeedbackVariantAdapter.ReviewViewHolder> {

    Context context;
    List<Review> payloadList;
    Typeface regular, medium, light, bold;

    public FeedbackVariantAdapter(Context context, List<Review> payloadList) {
        this.context = context;
        this.payloadList = payloadList;
        Resources resources = context.getResources();
        regular = FontCache.get(resources.getString(R.string.regular), context);
        medium = FontCache.get(resources.getString(R.string.medium), context);
        light = FontCache.get(resources.getString(R.string.light), context);
        bold = FontCache.get(resources.getString(R.string.bold), context);
    }

    @NonNull
    @Override
    public FeedbackVariantAdapter.ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View inflater = LayoutInflater.from(context).inflate(R.layout.item_review, viewGroup, false);
        return new FeedbackVariantAdapter.ReviewViewHolder(inflater);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedbackVariantAdapter.ReviewViewHolder holder,final int i) {
        holder.tvName.setText(payloadList.get(i).getName());
        holder.tvTitle.setText(payloadList.get(i).getRateReview());
        holder.tvDate.setText(payloadList.get(i).getRateDatetime());

    }

    @Override
    public int getItemCount() {
        return payloadList.size();
    }


    public class ReviewViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvName, tvDate;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tv_name);
            tvTitle = itemView.findViewById(R.id.tv_review_title);
            tvDate = itemView.findViewById(R.id.tv_date);


        }
    }
}