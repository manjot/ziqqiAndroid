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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ziqqi.OnCartItemlistener;
import com.ziqqi.R;
import com.ziqqi.activities.BillingInfoActivity;
import com.ziqqi.adapters.OrderTrackingAdapter;
import com.ziqqi.databinding.LayoutOrderTrackBinding;
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

    private void setUpAdapter() {
        manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.rvOrderTracking.setLayoutManager(manager);
        adapter = new OrderTrackingAdapter(getContext(), orderDataList);
        binding.rvOrderTracking.setAdapter(adapter);
//        spacesItemDecoration = new SpacesItemDecoration(getContext(), R.dimen.dp_4);
//        binding.rvCart.addItemDecoration(spacesItemDecoration);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
