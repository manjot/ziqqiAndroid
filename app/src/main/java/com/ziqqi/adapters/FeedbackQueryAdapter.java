package com.ziqqi.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ziqqi.OnFeedbackItemListener;
import com.ziqqi.OnItemClickListener;
import com.ziqqi.R;
import com.ziqqi.model.feedbackmastermodel.Payload;
import com.ziqqi.utils.FontCache;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FeedbackQueryAdapter extends RecyclerView.Adapter<FeedbackQueryAdapter.FeedbackViewModel> {

    Context context;
    List<com.ziqqi.model.feedbackmastermodel.Payload> payloadList;
    OnFeedbackItemListener listener;

    public FeedbackQueryAdapter(Context context, List<Payload> payloadList, OnFeedbackItemListener listener) {
        this.context = context;
        this.listener = listener;
        this.payloadList = payloadList;
    }

    @NonNull
    @Override
    public FeedbackQueryAdapter.FeedbackViewModel onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View inflater = LayoutInflater.from(context).inflate(R.layout.item_feedback_question, viewGroup, false);
        return new FeedbackQueryAdapter.FeedbackViewModel(inflater);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedbackQueryAdapter.FeedbackViewModel holder, final int i) {
        holder.tv_query.setText(payloadList.get(i).getFeedback());

    }

    @Override
    public int getItemCount() {
        return payloadList.size();
    }

    public class FeedbackViewModel extends RecyclerView.ViewHolder implements RatingBar.OnRatingBarChangeListener {
        TextView tv_query;
        AppCompatRatingBar ratingBar;

        public FeedbackViewModel(@NonNull View itemView) {
            super(itemView);

            tv_query = itemView.findViewById(R.id.tv_query);
            ratingBar = itemView.findViewById(R.id.rb_star);

            ratingBar.setOnRatingBarChangeListener(this);
        }

        @Override
        public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
            if(rating<1.0f){
                ratingBar.setRating(1.0f);
            }
            listener.onFeedbackItemClick(getAdapterPosition(), (int) ratingBar.getRating());
        }
    }
}
