package com.ziqqi.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ziqqi.OnItemClickListener;
import com.ziqqi.R;
import com.ziqqi.activities.SearchResultActivity;
import com.ziqqi.model.myaddressmodel.Payload;

import java.util.List;

public class MyAddressAdapter extends RecyclerView.Adapter<MyAddressAdapter.MyAddressViewHolder> {

    Context context;
    List<Payload> payloadList;
    OnItemClickListener listener;


    public MyAddressAdapter(Context context, List<Payload> payloadList, OnItemClickListener listener) {
        this.context = context;
        this.payloadList = payloadList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyAddressAdapter.MyAddressViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View inflater = LayoutInflater.from(context).inflate(R.layout.item_address, viewGroup, false);
        return new MyAddressAdapter.MyAddressViewHolder(inflater);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAddressAdapter.MyAddressViewHolder holder, final int i) {
        holder.tv_user_name.setText(payloadList.get(i).getFirstName());
        holder.tv_address.setText(context.getString(R.string.address_details) +"\n"+ payloadList.get(i).getAddress1() +"\n"+ payloadList.get(i).getAddress2()+"\n"+payloadList.get(i).getAddressDetails() +"\n"+payloadList.get(i).getCity() );
        holder.tv_mobile.setText(context.getString(R.string.mobile_number) +"\n"+ payloadList.get(i).getMobile());
    }

    @Override
    public int getItemCount() {
        return payloadList.size();
    }

    public class MyAddressViewHolder extends RecyclerView.ViewHolder {
        TextView tv_user_name, tv_address, tv_mobile;
        Button bt_edit;

        public MyAddressViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_user_name = itemView.findViewById(R.id.tv_user_name);
            tv_address = itemView.findViewById(R.id.tv_address);
            tv_mobile = itemView.findViewById(R.id.tv_mobile);
            bt_edit = itemView.findViewById(R.id.bt_edit);
            bt_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }
}
