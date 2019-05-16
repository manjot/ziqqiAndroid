package com.ziqqi.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ziqqi.OnCartItemlistener;
import com.ziqqi.OrderTrackingItemClickListener;
import com.ziqqi.R;
import com.ziqqi.activities.BillingInfoActivity;
import com.ziqqi.adapters.OrderTrackingAdapter;
import com.ziqqi.databinding.LayoutOrderTrackBinding;
import com.ziqqi.model.ordercancelmodel.OrderCancelResponse;
import com.ziqqi.model.ordertrackingmodel.OrderTrackingResponse;
import com.ziqqi.model.ordertrackingmodel.Payload;
import com.ziqqi.utils.ConnectivityHelper;
import com.ziqqi.utils.Constants;
import com.ziqqi.utils.PreferenceManager;
import com.ziqqi.utils.SpacesItemDecoration;
import com.ziqqi.viewmodel.CartViewModel;
import com.ziqqi.viewmodel.OrderTrackingViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static com.facebook.FacebookSdk.getApplicationContext;

public class OrderTracking extends Fragment {

    LayoutOrderTrackBinding binding;
    OrderTrackingViewModel viewModel;
    Toolbar toolbar;
    TextView tvTitle;
    ImageView ivTitle;
    List<Payload> payloadList = new ArrayList<>();
    List<Payload> orderDataList = new ArrayList<>();
    OrderTrackingAdapter adapter;
    SpacesItemDecoration spacesItemDecoration;
    LinearLayoutManager manager;
    OrderTrackingItemClickListener listener;
    TextView tv_wrong_item, tv_cheaper, tv_others;

    public OrderTracking() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewModel = ViewModelProviders.of(this).get(OrderTrackingViewModel.class);
        binding = DataBindingUtil.inflate(
                inflater, R.layout.layout_order_track, container, false);

        Locale locale = new Locale(PreferenceManager.getStringValue(Constants.LANG));
        Configuration config = getApplicationContext().getResources().getConfiguration();
        config.locale = locale;
        getApplicationContext().getResources().updateConfiguration(config, getApplicationContext().getResources().getDisplayMetrics());

        toolbar=  getActivity().findViewById(R.id.toolbar);
        tvTitle=  toolbar.findViewById(R.id.tv_toolbar_title_text);
        ivTitle=  toolbar.findViewById(R.id.tv_toolbar_title);
        ivTitle.setVisibility(View.GONE);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(getString(R.string.order_tracking));

        binding.executePendingBindings();
        binding.setViewModel(viewModel);
        binding.setViewModel(viewModel);
        View view = binding.getRoot();

        listener = new OrderTrackingItemClickListener() {
            @Override
            public void onItemClick(final String productId, final String orderId) {
                final AlertDialog dialogBuilder = new AlertDialog.Builder(Objects.requireNonNull(getActivity())).create();
                LayoutInflater inflater = getActivity().getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.dialog_cancel_order, null);

                tv_wrong_item = dialogView.findViewById(R.id.tv_wrong_item);
                tv_cheaper = dialogView.findViewById(R.id.tv_cheaper);
                tv_others = dialogView.findViewById(R.id.tv_others);


                tv_wrong_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogBuilder.dismiss();
                        cancelOrder(PreferenceManager.getStringValue(Constants.AUTH_TOKEN), productId, orderId, tv_wrong_item.getText().toString());
                    }
                });

                tv_cheaper.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogBuilder.dismiss();
                        cancelOrder(PreferenceManager.getStringValue(Constants.AUTH_TOKEN), productId, orderId, tv_cheaper.getText().toString());
                    }
                });

                tv_others.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogBuilder.dismiss();
                        cancelOrder(PreferenceManager.getStringValue(Constants.AUTH_TOKEN), productId, orderId, tv_others.getText().toString());
                    }
                });

                dialogBuilder.setView(dialogView);
                dialogBuilder.show();
            }
        };

        setUpAdapter();
        if (PreferenceManager.getBoolValue(Constants.LOGGED_IN)){
            fetchCart(PreferenceManager.getStringValue(Constants.AUTH_TOKEN));
        }

        return view;
    }

    private void fetchCart(String authToken) {
        if (ConnectivityHelper.isConnectedToNetwork(getContext())){
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.rvOrderTracking.setVisibility(View.GONE);
            binding.llNoData.setVisibility(View.GONE);
            viewModel.fetchData(authToken);
            viewModel.getOrderTrackingResponse().observe(this, new Observer<OrderTrackingResponse>() {
                @Override
                public void onChanged(@Nullable final OrderTrackingResponse orderTrackingResponse) {
                    if (!orderTrackingResponse.getError()) {
                        binding.progressBar.setVisibility(View.GONE);
                        binding.llNoData.setVisibility(View.GONE);
                        if (payloadList != null) {
                            orderDataList.clear();
                            payloadList = orderTrackingResponse.getPayload();
                            orderDataList.addAll(payloadList);
                            binding.rvOrderTracking.setVisibility(View.VISIBLE);
                            binding.progressBar.setVisibility(View.GONE);
                            // adapter.notifyDataSetChanged();
                        }
                    } else {
                        binding.progressBar.setVisibility(View.GONE);
                        binding.llNoData.setVisibility(View.VISIBLE);

                    }
                }
            });
        }else{
            Toast.makeText(getApplicationContext(),"You're not connected!", Toast.LENGTH_SHORT).show();
        }

    }


    private void cancelOrder(String authToken, String productId, String orderId, String reason) {
        if (ConnectivityHelper.isConnectedToNetwork(getContext())){
            binding.progressBar.setVisibility(View.VISIBLE);
            viewModel.cancelOrder(authToken, productId, orderId, reason);
            viewModel.cancelOrderResponse().observe(this, new Observer<OrderCancelResponse>() {
                @Override
                public void onChanged(@Nullable final OrderCancelResponse orderCancelResponse) {
                    if (!orderCancelResponse.getError()) {
                        binding.progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), orderCancelResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        fetchCart(PreferenceManager.getStringValue(Constants.AUTH_TOKEN));

                    } else {
                        binding.progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), orderCancelResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else{
            Toast.makeText(getApplicationContext(),"You're not connected!", Toast.LENGTH_SHORT).show();
        }

    }

    private void setUpAdapter() {
        manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.rvOrderTracking.setLayoutManager(manager);
        adapter = new OrderTrackingAdapter(getContext(), orderDataList, listener);
        binding.rvOrderTracking.setAdapter(adapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
