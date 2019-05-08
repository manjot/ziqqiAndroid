package com.ziqqi.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarFinalValueListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.ziqqi.OnItemClickListener;
import com.ziqqi.R;
import com.ziqqi.adapters.FeedbackQueryAdapter;
import com.ziqqi.adapters.FilterMainAdapter;
import com.ziqqi.databinding.ActivityFilterBinding;
import com.ziqqi.model.feedbackmastermodel.FeedbackMaster;
import com.ziqqi.model.filterproductmodel.FilterCategoriesResponse;
import com.ziqqi.model.filterproductmodel.Payload;
import com.ziqqi.utils.ConnectivityHelper;
import com.ziqqi.viewmodel.BillingInfoViewModel;
import com.ziqqi.viewmodel.FilterProductViewModel;

import java.util.ArrayList;
import java.util.List;

public class FilterActivity extends AppCompatActivity {

    ActivityFilterBinding binding;
    FilterProductViewModel viewModel;
    List<Payload> payloadList = new ArrayList<>();
    List<Payload> dataList = new ArrayList<>();
    LinearLayoutManager manager;
    FilterMainAdapter adapter;
    OnItemClickListener listener;
    String catId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_filter);
        viewModel = ViewModelProviders.of(this).get(FilterProductViewModel.class);
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button);

        if (getIntent().getExtras() != null){
            catId = getIntent().getStringExtra("catId");
        }

        setUpAdapter();
        getFiterCategories(catId);

        binding.rangeSeekbar1.setMinValue(30f);
        binding.rangeSeekbar1.setMaxValue(60f);

        binding.rangeSeekbar1.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                binding.textMin1.setText("$ "+ minValue);
                binding.textMax1.setText("$ "+ maxValue);
            }
        });

        binding.rangeSeekbar1.setOnRangeSeekbarFinalValueListener(new OnRangeSeekbarFinalValueListener() {
            @Override
            public void finalValue(Number minValue, Number maxValue) {
                Log.d("CRS=>", String.valueOf(minValue) + " : " + String.valueOf(maxValue));
            }
        });
    }

    private void getFiterCategories(String catId) {
        if (ConnectivityHelper.isConnectedToNetwork(this)){
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.mainLayout.setVisibility(View.GONE);
            viewModel.fetchData(catId);
            viewModel.getFilterResponse().observe(this, new Observer<FilterCategoriesResponse>() {
                @Override
                public void onChanged(@Nullable FilterCategoriesResponse filterCategoriesResponse) {
                    if (!filterCategoriesResponse.getError()) {
                        binding.progressBar.setVisibility(View.GONE);
                        binding.mainLayout.setVisibility(View.VISIBLE);
                        if (payloadList != null) {
                            dataList.clear();
                            payloadList = filterCategoriesResponse.getPayload();
                            dataList.addAll(payloadList);
                        }
                    } else {
                        binding.progressBar.setVisibility(View.GONE);
                        binding.mainLayout.setVisibility(View.GONE);
                        Toast.makeText(FilterActivity.this, filterCategoriesResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else{
            Toast.makeText(FilterActivity.this,"You're not connected!", Toast.LENGTH_SHORT).show();
        }

    }

    private void setUpAdapter() {
        manager = new LinearLayoutManager(FilterActivity.this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.rvFilterCategory.setLayoutManager(manager);
        adapter = new FilterMainAdapter(FilterActivity.this, dataList, listener);
        binding.rvFilterCategory.setAdapter(adapter);
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
