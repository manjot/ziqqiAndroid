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
    List<String> stars = new ArrayList<>();

    public FeedbackQueryAdapter(Context context, List<Payload> payloadList) {
        this.context = context;
        this.payloadList = payloadList;
    }

    @NonNull
    @Override
    public FeedbackQueryAdapter.FeedbackViewModel onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View inflater = LayoutInflater.from(context).inflate(R.layout.item_feedback_question, viewGroup, false);
        for (int k=0; k>=payloadList.size(); k++){
            stars.add("0");
            Log.i("SEESTAR", stars.toString());
        }
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

            ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    if(rating==0){
                        Toast.makeText(context, String.valueOf(rating), Toast.LENGTH_SHORT).show();
                        stars.add(getAdapterPosition(), "0");
                        Log.i("SEESTAR", stars.toString());
                    }else if(rating==1){
                        Toast.makeText(context, String.valueOf(rating), Toast.LENGTH_SHORT).show();
                        stars.add(getAdapterPosition(), "1");
                        Log.i("SEESTAR", stars.toString());
                    }else if(rating==2){
                        Toast.makeText(context, String.valueOf(rating), Toast.LENGTH_SHORT).show();
                        stars.add(getAdapterPosition(), "2");
                        Log.i("SEESTAR", stars.toString());
                    }else if(rating==3){
                        Toast.makeText(context, String.valueOf(rating), Toast.LENGTH_SHORT).show();
                        stars.add(getAdapterPosition(), "3");
                        Log.i("SEESTAR", stars.toString());
                    }else if(rating==4){
                        Toast.makeText(context, String.valueOf(rating), Toast.LENGTH_SHORT).show();
                        stars.add(getAdapterPosition(), "4");
                        Log.i("SEESTAR", stars.toString());
                    }else if(rating==5){
                        Toast.makeText(context, String.valueOf(rating), Toast.LENGTH_SHORT).show();
                        stars.add(getAdapterPosition(), "5");
                        Log.i("SEESTAR", stars.toString());
                    }
                }
            });
        }
    }
}
