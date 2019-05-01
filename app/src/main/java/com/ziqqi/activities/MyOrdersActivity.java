package com.ziqqi.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.ziqqi.model.getbillingaddressmodel.BillingAddressModel;
import com.ziqqi.model.myordersmodel.MyOrdersResponse;
import com.ziqqi.model.myordersmodel.Payload;
import com.ziqqi.utils.ConnectivityHelper;
import com.ziqqi.utils.Constants;
import com.ziqqi.utils.PreferenceManager;
import com.ziqqi.utils.SpacesItemDecoration;
import com.ziqqi.viewmodel.MyOrderViewModel;
import com.ziqqi.viewmodel.SignUpViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MyOrdersActivity extends AppCompatActivity {

    MyOrderViewModel myOrderViewModel;
    ActivityMyOrdersBinding binding;
    LinearLayoutManager manager;
    List<Payload> payloadList = new ArrayList<>();
    List<Payload> addressDataList = new ArrayList<>();
    MyOrdersAdapter adapter;
    SpacesItemDecoration spacesItemDecoration;
    OnItemClickListener listener;
    ArrayList<String> names = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_orders);
        myOrderViewModel = ViewModelProviders.of(this).get(MyOrderViewModel.class);

        Locale locale = new Locale(PreferenceManager.getStringValue(Constants.LANG));
        Configuration config = getBaseContext().getResources().getConfiguration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        binding.executePendingBindings();
        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button);

        setUpAdapter();
        if (PreferenceManager.getBoolValue(Constants.LOGGED_IN)){
            getOrders(PreferenceManager.getStringValue(Constants.AUTH_TOKEN));
        }

        binding.tvSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //after the change calling the method and passing the search input
                filter(editable.toString());
            }
        });
    }

    private void getOrders(String authToken) {
        if (ConnectivityHelper.isConnectedToNetwork(this)){
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

                            for (int i = 0;  i <= myOrdersResponse.getPayload().size()-1; i++){
                                names.add(payloadList.get(i).getProductName());
                            }
                        }
                    } else {
                        binding.progressBar.setVisibility(View.GONE);
                        binding.rvMyOrders.setVisibility(View.GONE);
                        binding.llNoData.setVisibility(View.VISIBLE);
                    }
                }
            });
        }else{
            Toast.makeText(MyOrdersActivity.this,"You're not connected!", Toast.LENGTH_SHORT).show();
        }

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

    private void filter(String text) {
        //new array list that will hold the filtered data
        ArrayList<String> filterdNames = new ArrayList<>();

        //looping through existing elements
        for (String s : names) {
            //if the existing elements contains the search input
            if (s.toLowerCase().contains(text.toLowerCase())) {
                //adding the element to filtered list
                filterdNames.add(s);
            }
        }

        //calling a method of the adapter class and passing the filtered list
        adapter.filterList(filterdNames);
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
