package com.ziqqi.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.ziqqi.R;
import com.ziqqi.adapters.CartAdapter;
import com.ziqqi.adapters.MyAccountAdapter;
import com.ziqqi.databinding.ActivityMyAccountBinding;
import com.ziqqi.model.helpcenterbyidmodel.HelpCenterByIdResponse;
import com.ziqqi.model.helpcenterbyidmodel.Payload;
import com.ziqqi.model.helpcentermodel.HelpCenterModel;
import com.ziqqi.model.viewcartmodel.ViewCartResponse;
import com.ziqqi.utils.ConnectivityHelper;
import com.ziqqi.viewmodel.MyAccountViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyAccountActivity extends AppCompatActivity {
    ActivityMyAccountBinding binding;
    MyAccountViewModel viewModel;
    MyAccountAdapter adapter;
    LinearLayoutManager manager;
    List<Payload> payloadList = new ArrayList<>();
    List<Payload> helpsDataList = new ArrayList<>();
    String id = " ", title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_account);
        viewModel = ViewModelProviders.of(this).get(MyAccountViewModel.class);

        binding.executePendingBindings();
        binding.setViewModel(viewModel);

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button);

        setUpAdapter();
        if (getIntent().getExtras() != null){
            id = getIntent().getStringExtra("id");
            title = getIntent().getStringExtra("title");
        }
        binding.tvToolbarTitle.setText(title);
        fetchHelps(Integer.parseInt(id));
    }

    private void fetchHelps(int helpId) {
        if (ConnectivityHelper.isConnectedToNetwork(this)){
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.rvHelp.setVisibility(View.GONE);
            viewModel.fetchData(helpId);
            viewModel.getHelpByIdResponse().observe(this, new Observer<HelpCenterByIdResponse>() {
                @Override
                public void onChanged(@Nullable HelpCenterByIdResponse helpCenter) {
                    if (!helpCenter.getError()) {
                        binding.progressBar.setVisibility(View.GONE);
                        if (payloadList != null) {
                            helpsDataList.clear();
                            payloadList = helpCenter.getPayload();
                            helpsDataList.addAll(payloadList);
                            binding.rvHelp.setVisibility(View.VISIBLE);
                            binding.progressBar.setVisibility(View.GONE);
                            // adapter.notifyDataSetChanged();
                        }
                    } else {
                        binding.progressBar.setVisibility(View.GONE);
                    }
                }
            });
        }else{
            Toast.makeText(MyAccountActivity.this,"You're not connected!", Toast.LENGTH_SHORT).show();
        }
    }

    private void setUpAdapter() {
        manager = new LinearLayoutManager(MyAccountActivity.this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.rvHelp.setLayoutManager(manager);
        adapter = new MyAccountAdapter(MyAccountActivity.this, helpsDataList);
        binding.rvHelp.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
