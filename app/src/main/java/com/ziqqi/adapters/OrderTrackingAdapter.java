package com.ziqqi.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ziqqi.OnItemClickListener;
import com.ziqqi.OrderTrackingItemClickListener;
import com.ziqqi.R;
import com.ziqqi.model.ordertrackingmodel.Payload;

import java.util.List;

public class OrderTrackingAdapter extends RecyclerView.Adapter<OrderTrackingAdapter.MyOrdedersViewHolder> {

    Context context;
    List<Payload> payloadList;
    OrderTrackingItemClickListener listener;

    public OrderTrackingAdapter(Context context, List<Payload> payloadList, OrderTrackingItemClickListener listener) {
        this.context = context;
        this.payloadList = payloadList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public OrderTrackingAdapter.MyOrdedersViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View inflater = LayoutInflater.from(context).inflate(R.layout.item_order_track, viewGroup, false);
        return new OrderTrackingAdapter.MyOrdedersViewHolder(inflater);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderTrackingAdapter.MyOrdedersViewHolder holder, int i) {
        holder.tvBrandName.setText(payloadList.get(i).getBrandName());
        holder.tvName.setText(payloadList.get(i).getProductName());
        holder.tvDiscountPrice.setText("$ "+payloadList.get(i).getPrice());
        holder.tvOrderNo.setText("Order "+payloadList.get(i).getOrdersId());
        holder.tvRcpName.setText("Recipient : "+payloadList.get(i).getCustomer_name());
        holder.tvTime.setText("Placed on "+payloadList.get(i).getOrderDatetime());
        holder.tvPayMethod.setText("Payment method : "+payloadList.get(i).getPaymentGateway());
        Glide.with(context).load(payloadList.get(i).getProductImage()).apply(RequestOptions.placeholderOf(R.drawable.place_holder)).into(holder.ivImage);

        if (payloadList.get(i).getIs_cancel_date() ==0){
            holder.tv_cancel_item.setVisibility(View.GONE);
        }else{
            holder.tv_cancel_item.setVisibility(View.VISIBLE);
        }

        if (payloadList.get(i).getStatus().equalsIgnoreCase("Payment Pending")){
            holder.cb_pending.setChecked(true);
            holder.tv_delivered.setVisibility(View.GONE);
        }else if (payloadList.get(i).getStatus().equalsIgnoreCase("Order Confirmed")){
            holder.cb_pending.setChecked(true);
            holder.cb_confirmed.setChecked(true);
            holder.tv_delivered.setVisibility(View.GONE);
        }else if (payloadList.get(i).getStatus().equalsIgnoreCase("Out for Delivery")){
            holder.cb_pending.setChecked(true);
            holder.cb_confirmed.setChecked(true);
            holder.cb_hargeisa.setChecked(true);
            holder.tv_delivered.setVisibility(View.GONE);
        }else if (payloadList.get(i).getStatus().equalsIgnoreCase("Order Shipped")){
            holder.cb_pending.setChecked(true);
            holder.cb_confirmed.setChecked(true);
            holder.cb_hargeisa.setChecked(true);
            holder.cb_out.setChecked(true);
            holder.tv_delivered.setVisibility(View.GONE);
        }else if (payloadList.get(i).getStatus().equalsIgnoreCase("Delivery Completed")){
            holder.status.setVisibility(View.GONE);
            holder.tv_delivered.setVisibility(View.VISIBLE);
        }else if (payloadList.get(i).getStatus().equalsIgnoreCase("Order Item Cancelled")){
            holder.status.setVisibility(View.GONE);
            holder.tv_delivered.setVisibility(View.VISIBLE);
            holder.tv_delivered.setText("Order Cancelled");
            holder.tv_cancel_item.setVisibility(View.GONE);
        }




    }

    @Override
    public int getItemCount() {
        return payloadList.size();
    }

    public class MyOrdedersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvName, tvBrandName, tvDiscountPrice, tvOrderNo, tvRcpName, tvTime, tvPayMethod, tv_delivered, tv_cancel_item;
        CheckBox cb_pending, cb_confirmed, cb_hargeisa, cb_out, cb_completed;
        ImageView ivImage;
        LinearLayout ll_card;
        CardView status;

        public MyOrdedersViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tv_name);
            ivImage = itemView.findViewById(R.id.iv_product_image);
            tvBrandName = itemView.findViewById(R.id.tv_brand_name);
            tvDiscountPrice = itemView.findViewById(R.id.tv_discount_price);
            tvOrderNo = itemView.findViewById(R.id.tv_order_no);
            tvRcpName = itemView.findViewById(R.id.tv_rcp_name);
            tvTime = itemView.findViewById(R.id.tv_time);
            tvPayMethod = itemView.findViewById(R.id.tv_pay_method);
            ll_card = itemView.findViewById(R.id.ll_card);
            cb_pending = itemView.findViewById(R.id.cb_pending);
            cb_confirmed = itemView.findViewById(R.id.cb_confirmed);
            cb_hargeisa = itemView.findViewById(R.id.cb_hargeisa);
            cb_out = itemView.findViewById(R.id.cb_out);
            cb_completed = itemView.findViewById(R.id.cb_completed);
            status = itemView.findViewById(R.id.status);
            tv_delivered = itemView.findViewById(R.id.tv_delivered);
            tv_cancel_item = itemView.findViewById(R.id.tv_cancel_item);

            tv_cancel_item.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.tv_cancel_item:
                    listener.onItemClick(payloadList.get(getAdapterPosition()).getProductId(), payloadList.get(getAdapterPosition()).getOrdersId());
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
