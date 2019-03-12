package com.ziqqi.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ziqqi.R;

import java.util.HashMap;
import java.util.List;

public class MyAccountAdapter extends RecyclerView.Adapter<MyAccountAdapter.MyAccountViewHolder> {
    Context context;
    List<String> parentHeaderList;
    List<String> childHeaderList;
    HashMap<String, List<String>> children;
    ExpandableAdapter adapter;

    public MyAccountAdapter(Context context, List<String> parentHeaderList, List<String> childHeaderList, HashMap<String, List<String>> children) {
        this.context = context;
        this.parentHeaderList = parentHeaderList;
        this.childHeaderList = childHeaderList;
        this.children = children;
    }

    @NonNull
    @Override
    public MyAccountViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View inflater = LayoutInflater.from(context).inflate(R.layout.item_my_account, viewGroup, false);
        return new MyAccountViewHolder(inflater);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAccountViewHolder myAccountViewHolder, int i) {
        myAccountViewHolder.tvAboutTnc.setText(parentHeaderList.get(i));
        adapter = new ExpandableAdapter(context, childHeaderList, children);
        myAccountViewHolder.elvAboutTnc.setAdapter(adapter);

    }

    @Override
    public int getItemCount() {
        return parentHeaderList.size();
    }

    public class MyAccountViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        RelativeLayout rlAboutTnc;
        ImageView ivArrow;
        LinearLayout llAboutTnc;
        ExpandableListView elvAboutTnc;
        TextView tvAboutTnc;

        public MyAccountViewHolder(@NonNull View itemView) {
            super(itemView);
            rlAboutTnc = itemView.findViewById(R.id.rl_about_tnc);
            ivArrow = itemView.findViewById(R.id.iv_arrow);
            llAboutTnc = itemView.findViewById(R.id.ll_about_tnc);
            elvAboutTnc = itemView.findViewById(R.id.elv_about_tnc);
            tvAboutTnc = itemView.findViewById(R.id.tv_about_tnc);
            rlAboutTnc.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
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
