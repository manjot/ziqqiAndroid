package com.ziqqi.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.ziqqi.R;
import com.ziqqi.adapters.FeedbackQueryAdapter;
import com.ziqqi.adapters.SearchCategoryAdapter;
import com.ziqqi.databinding.ActivityFeedbackBinding;
import com.ziqqi.databinding.ActivityProductDetailBinding;
import com.ziqqi.model.feedbackmastermodel.FeedbackMaster;
import com.ziqqi.model.feedbackmastermodel.Payload;
import com.ziqqi.model.searchcategorymodel.SearchCategory;
import com.ziqqi.utils.SpacesItemDecoration;
import com.ziqqi.viewmodel.FeedbackViewModel;
import com.ziqqi.viewmodel.ProductDetailsViewModel;
import com.ziqqi.viewmodel.SearchViewModel;

import java.util.ArrayList;
import java.util.List;

public class FeedbackActivity extends AppCompatActivity {

    ActivityFeedbackBinding binding;
    FeedbackViewModel viewModel;
    List<Payload> payloadList = new ArrayList<>();
    List<Payload> queryList = new ArrayList<>();
    LinearLayoutManager manager;
    FeedbackQueryAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_feedback);
        viewModel = ViewModelProviders.of(this).get(FeedbackViewModel.class);
        binding.executePendingBindings();
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button);
        getSupportActionBar().setElevation(0.0f);

        setUpAdapter();
        getQueries();
    }

    private void getQueries() {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.rvQueries.setVisibility(View.GONE);
        viewModel.fetchData();
        viewModel.getFeedbackMasterResponse().observe(this, new Observer<FeedbackMaster>() {
            @Override
            public void onChanged(@Nullable FeedbackMaster feedbackMaster) {
                if (!feedbackMaster.getError()) {
                    binding.progressBar.setVisibility(View.GONE);
                    binding.rvQueries.setVisibility(View.VISIBLE);
                    if (payloadList != null) {
                        queryList.clear();
                        payloadList = feedbackMaster.getPayload();
                        queryList.addAll(payloadList);
                        binding.btSubmit.setVisibility(View.VISIBLE);
                    }
                } else {
                    binding.progressBar.setVisibility(View.GONE);
                    binding.rvQueries.setVisibility(View.GONE);

                }
            }
        });
    }

    private void setUpAdapter() {
        manager = new LinearLayoutManager(FeedbackActivity.this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.rvQueries.setLayoutManager(manager);
        adapter = new FeedbackQueryAdapter(FeedbackActivity.this, queryList);
        binding.rvQueries.setAdapter(adapter);
    }
}
