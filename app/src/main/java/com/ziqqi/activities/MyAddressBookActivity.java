package com.ziqqi.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.ziqqi.OnItemClickListener;
import com.ziqqi.R;
import com.ziqqi.adapters.MyAddressAdapter;
import com.ziqqi.adapters.SearchCategoryAdapter;
import com.ziqqi.databinding.ActivityMyAddressBookBinding;
import com.ziqqi.model.myaddressmodel.Payload;
import com.ziqqi.model.myaddressmodel.ShippingAddressModel;
import com.ziqqi.model.searchcategorymodel.SearchCategory;
import com.ziqqi.utils.SpacesItemDecoration;
import com.ziqqi.viewmodel.MyAddressViewModel;
import com.ziqqi.viewmodel.MyOrderViewModel;

import java.util.ArrayList;
import java.util.List;

public class MyAddressBookActivity extends AppCompatActivity {

    MyAddressViewModel myAddressViewModel;
    ActivityMyAddressBookBinding binding;
    LinearLayoutManager manager;
    List<Payload> payloadList = new ArrayList<>();
    List<Payload> addressDataList = new ArrayList<>();
    MyAddressAdapter adapter;
    SpacesItemDecoration spacesItemDecoration;
    OnItemClickListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_address_book);
        myAddressViewModel = ViewModelProviders.of(this).get(MyAddressViewModel.class);
        binding.executePendingBindings();
        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button);
        setUpAdapter();
        getAddress("");
    }

    private void getAddress(String authToken) {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.rvAddress.setVisibility(View.GONE);
        binding.llNoData.setVisibility(View.GONE);
        myAddressViewModel.fetchShippingAddress(authToken);
        myAddressViewModel.getShippingAddressResponse().observe(this, new Observer<ShippingAddressModel>() {
            @Override
            public void onChanged(@Nullable ShippingAddressModel shippingAddressModel) {
                if (!shippingAddressModel.getError()) {
                    binding.progressBar.setVisibility(View.GONE);
                    binding.rvAddress.setVisibility(View.VISIBLE);
                    binding.llNoData.setVisibility(View.GONE);
                    if (payloadList != null) {
                        addressDataList.clear();
                        payloadList = shippingAddressModel.getPayload();
                        addressDataList.addAll(payloadList);
                    }
                } else {
                    binding.progressBar.setVisibility(View.GONE);
                    binding.rvAddress.setVisibility(View.GONE);
                    binding.llNoData.setVisibility(View.VISIBLE);

                }
            }
        });
    }

    private void setUpAdapter() {
        manager = new LinearLayoutManager(MyAddressBookActivity.this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.rvAddress.setLayoutManager(manager);
        adapter = new MyAddressAdapter(MyAddressBookActivity.this, addressDataList, listener);
        binding.rvAddress.setAdapter(adapter);
        spacesItemDecoration = new SpacesItemDecoration(MyAddressBookActivity.this, R.dimen.dp_4);
        binding.rvAddress.addItemDecoration(spacesItemDecoration);
    }
}
