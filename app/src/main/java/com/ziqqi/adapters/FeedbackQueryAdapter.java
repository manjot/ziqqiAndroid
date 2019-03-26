package com.ziqqi.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ziqqi.OnItemClickListener;
import com.ziqqi.R;
import com.ziqqi.model.feedbackmastermodel.Payload;
import com.ziqqi.utils.FontCache;

import java.util.ArrayList;
import java.util.List;

public class FeedbackQueryAdapter extends RecyclerView.Adapter<FeedbackQueryAdapter.FeedbackViewModel> {

    Context context;
    List<com.ziqqi.model.feedbackmastermodel.Payload> payloadList;

    public FeedbackQueryAdapter(Context context, List<Payload> payloadList) {
        this.context = context;
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

    public class FeedbackViewModel extends RecyclerView.ViewHolder {
        TextView tv_query;
        AppCompatRatingBar ratingBar;

        public FeedbackViewModel(@NonNull View itemView) {
            super(itemView);

            tv_query = itemView.findViewById(R.id.tv_query);
            ratingBar = itemView.findViewById(R.id.rb_star);
        }
    }
}
