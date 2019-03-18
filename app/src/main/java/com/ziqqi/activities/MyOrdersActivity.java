package com.ziqqi.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.ziqqi.OnItemClickListener;
import com.ziqqi.R;
import com.ziqqi.adapters.MyAddressAdapter;
import com.ziqqi.adapters.MyOrdersAdapter;
import com.ziqqi.databinding.ActivityMyOrdersBinding;
import com.ziqqi.model.addbillingaddressmodel.AddBillingAddressModel;
import com.ziqqi.model.myordersmodel.MyOrdersResponse;
import com.ziqqi.model.myordersmodel.Payload;
import com.ziqqi.utils.Constants;
import com.ziqqi.utils.PreferenceManager;
import com.ziqqi.utils.SpacesItemDecoration;
import com.ziqqi.viewmodel.MyOrderViewModel;
import com.ziqqi.viewmodel.SignUpViewModel;

import java.util.ArrayList;
import java.util.List;

public class MyOrdersActivity extends AppCompatActivity {

    MyOrderViewModel myOrderViewModel;
    ActivityMyOrdersBinding binding;
    LinearLayoutManager manager;
    List<Payload> payloadList = new ArrayList<>();
    List<Payload> addressDataList = new ArrayList<>();
    MyOrdersAdapter adapter;
    SpacesItemDecoration spacesItemDecoration;
    OnItemClickListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_orders);
        myOrderViewModel = ViewModelProviders.of(this).get(MyOrderViewModel.class);
        binding.executePendingBindings();
        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button);

        setUpAdapter();
        if (PreferenceManager.getBoolValue(Constants.LOGGED_IN)){
            getOrders(PreferenceManager.getStringValue(Constants.AUTH_TOKEN));
        }
    }

    private void getOrders(String authToken) {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.rvMyOrders.setVisibility(View.GONE);
        binding.llNoData.setVisibility(View.GONE);
        myOrderViewModel.fetchData(authToken);
        myOrderViewModel.getMyOrdersResponse().observe(this, new Observer<MyOrdersResponse>() {
            @Override
            public void onChanged(@Nullable MyOrdersResponse myOrdersResponse) {
                if (!myOrdersResponse.getError()) {
                    binding.progressBar.setVisibility(View.GONE);
                    binding.rvMyOrders.setVisibility(View.VISIBLE);
                    binding.llNoData.setVisibility(View.GONE);
                    if (payloadList != null) {
                        addressDataList.clear();
                        payloadList = myOrdersResponse.getPayload();
                        addressDataList.addAll(payloadList);
                    }
                } else {
                    binding.progressBar.setVisibility(View.GONE);
                    binding.rvMyOrders.setVisibility(View.GONE);
                    binding.llNoData.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void setUpAdapter() {
        manager = new LinearLayoutManager(MyOrdersActivity.this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.rvMyOrders.setLayoutManager(manager);
        adapter = new MyOrdersAdapter(MyOrdersActivity.this, addressDataList, listener);
        binding.rvMyOrders.setAdapter(adapter);
        spacesItemDecoration = new SpacesItemDecoration(MyOrdersActivity.this, R.dimen.dp_4);
        binding.rvMyOrders.addItemDecoration(spacesItemDecoration);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
