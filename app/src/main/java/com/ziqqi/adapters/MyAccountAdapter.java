package com.ziqqi.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.ziqqi.R;
import com.ziqqi.model.helpcenterbyidmodel.HelpCenterByIdResponse;
import com.ziqqi.model.helpcenterbyidmodel.Payload;
import com.ziqqi.utils.FontCache;

import java.util.HashMap;
import java.util.List;

public class MyAccountAdapter extends RecyclerView.Adapter<MyAccountAdapter.MyAccountViewHolder> {

    Context context;
    List<Payload> payloadList;
    Typeface regular, medium, light, bold;

    public MyAccountAdapter(Context context, List<Payload> payloadList) {
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
    public MyAccountViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View inflater = LayoutInflater.from(context).inflate(R.layout.item_my_account, viewGroup, false);
        return new MyAccountViewHolder(inflater);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAccountViewHolder myAccountViewHolder, int i) {
        myAccountViewHolder.tv_header.setText(payloadList.get(i).getHeader());
        String resultDesc = Html.fromHtml(payloadList.get(i).getDescription()).toString();
        myAccountViewHolder.tv_desc.setText(resultDesc);

    }

    @Override
    public int getItemCount() {
        return payloadList.size();
    }

    public class MyAccountViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        RelativeLayout rlAboutTnc;
        LinearLayout llAboutTnc;
        ImageView ivArrow;
        TextView tv_header, tv_desc;

        public MyAccountViewHolder(@NonNull View itemView) {
            super(itemView);
            rlAboutTnc = itemView.findViewById(R.id.rl_about_tnc);
            llAboutTnc = itemView.findViewById(R.id.ll_about_tnc);
            ivArrow = itemView.findViewById(R.id.iv_arrow);
            tv_header = itemView.findViewById(R.id.tv_header);
            tv_desc = itemView.findViewById(R.id.tv_desc);

            rlAboutTnc.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.rl_about_tnc:
                    if (llAboutTnc.getVisibility() == View.GONE) {
                        rlAboutTnc.setBackground(context.getResources().getDrawable(R.drawable.white_stroke_light_primary_rounded));
                        ivArrow.setRotation(180);
                        llAboutTnc.setVisibility(View.VISIBLE);
                    } else {
                        rlAboutTnc.setBackground(context.getResources().getDrawable(R.drawable.white_stroke_grey_rounded));
                        llAboutTnc.setVisibility(View.GONE);
                        ivArrow.setRotation(0);
                    }
            }

        }
    }
}
