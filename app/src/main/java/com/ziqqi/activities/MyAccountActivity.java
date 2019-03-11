package com.ziqqi.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.MenuItem;

import com.ziqqi.R;
import com.ziqqi.adapters.MyAccountAdapter;
import com.ziqqi.databinding.ActivityMyAccountBinding;
import com.ziqqi.viewmodel.MyAccountViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyAccountActivity extends AppCompatActivity {
    ActivityMyAccountBinding binding;
    MyAccountViewModel viewModel;
    List<String> headers;
    List<String> parentHeaders;
    HashMap<String, List<String>> children;
    MyAccountAdapter adapter;
    LinearLayoutManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_account);
        viewModel = ViewModelProviders.of(this).get(MyAccountViewModel.class);

        binding.executePendingBindings();
        binding.setViewModel(viewModel);

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.recyclerView.setLayoutManager(manager);

        prepareListData();

        adapter = new MyAccountAdapter(this, parentHeaders, headers, children);
        binding.recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void prepareListData() {
        headers = new ArrayList<>();
        children = new HashMap<>();
        parentHeaders = new ArrayList<>();

        parentHeaders.add(getResources().getString(R.string.aotnc));
        parentHeaders.add(getResources().getString(R.string.order_of_rights));
        parentHeaders.add(getResources().getString(R.string.guideliness_for));
        parentHeaders.add(getResources().getString(R.string.website_purchase));

        headers.add("View Information about ZIQQI.COM");
        headers.add("PART A: INFORMATION ABOUT ZIQQI.COM");
        headers.add("PART B: WEBSITE TERMS");
        headers.add("YOUR USE OF THIS WEBSITE");
        headers.add("USE OF THIS WEBSITE INTERNALLY");

        List<String> childList1 = new ArrayList<String>();
        childList1.add(getResources().getString(R.string.dummy_data));

        List<String> childList2 = new ArrayList<String>();
        childList2.add(getResources().getString(R.string.dummy_data));

        List<String> childList3 = new ArrayList<String>();
        childList3.add(getResources().getString(R.string.dummy_data));

        List<String> childList4 = new ArrayList<String>();
        childList4.add(getResources().getString(R.string.dummy_data));

        List<String> childList5 = new ArrayList<String>();
        childList5.add(getResources().getString(R.string.dummy_data));

        children.put(headers.get(0), childList1);
        children.put(headers.get(1), childList2);
        children.put(headers.get(2), childList3);
        children.put(headers.get(3), childList4);
        children.put(headers.get(4), childList5);
    }
}
