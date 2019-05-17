package com.ziqqi.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.ziqqi.OnFeedbackItemListener;
import com.ziqqi.R;
import com.ziqqi.adapters.FeedbackQueryAdapter;
import com.ziqqi.adapters.SearchCategoryAdapter;
import com.ziqqi.databinding.ActivityFeedbackBinding;
import com.ziqqi.databinding.ActivityProductDetailBinding;
import com.ziqqi.model.addfeedbackmodel.AddFeedbackModel;
import com.ziqqi.model.addshippingaddressmodel.AddShippingAddressModel;
import com.ziqqi.model.feedbackmastermodel.FeedbackMaster;
import com.ziqqi.model.feedbackmastermodel.Payload;
import com.ziqqi.model.searchcategorymodel.SearchCategory;
import com.ziqqi.utils.ConnectivityHelper;
import com.ziqqi.utils.Constants;
import com.ziqqi.utils.LoginDialog;
import com.ziqqi.utils.PreferenceManager;
import com.ziqqi.utils.SpacesItemDecoration;
import com.ziqqi.viewmodel.FeedbackViewModel;
import com.ziqqi.viewmodel.ProductDetailsViewModel;
import com.ziqqi.viewmodel.SearchViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FeedbackActivity extends AppCompatActivity {

    ActivityFeedbackBinding binding;
    FeedbackViewModel viewModel;
    List<Payload> payloadList = new ArrayList<>();
    List<Payload> queryList = new ArrayList<>();
    LinearLayoutManager manager;
    FeedbackQueryAdapter adapter;
    OnFeedbackItemListener listener;
    List<Integer> stars = new ArrayList<>();
    String strStar;
    LoginDialog loginDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_feedback);
        viewModel = ViewModelProviders.of(this).get(FeedbackViewModel.class);

        Locale locale = new Locale(PreferenceManager.getStringValue(Constants.LANG));
        Configuration config = getBaseContext().getResources().getConfiguration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        binding.executePendingBindings();
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button);
        getSupportActionBar().setElevation(0.0f);

        listener = new OnFeedbackItemListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onFeedbackItemClick(int position, int star) {
                stars.add(position, star);
                strStar = android.text.TextUtils.join(",", stars);;
                Log.i("STAR", strStar);
            }
        };

        setUpAdapter();
        getQueries();

        binding.btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stars.size() < queryList.size()){
                    Toast.makeText(getApplicationContext(), "Please give rating to all queries!", Toast.LENGTH_SHORT).show();
                }else {
                    if (PreferenceManager.getBoolValue(Constants.LOGGED_IN)){
                        addFeedback(PreferenceManager.getStringValue(Constants.AUTH_TOKEN), strStar);
                    }else{
                        loginDialog = new LoginDialog();
                        loginDialog.showDialog(FeedbackActivity.this);
                    }

                }

            }
        });
    }

    private void addFeedback(String authToken, String ratting) {
        if (ConnectivityHelper.isConnectedToNetwork(this)){
            binding.progressBar.setVisibility(View.VISIBLE);
            viewModel.addFeedback(authToken, ratting);
            viewModel.addFeedbackResponse().observe(this, new Observer<AddFeedbackModel>() {
                @Override
                public void onChanged(@Nullable AddFeedbackModel addFeedbackModel) {
                    binding.progressBar.setVisibility(View.GONE);
                    if (!addFeedbackModel.getError()) {
                        Toast.makeText(FeedbackActivity.this,addFeedbackModel.getMessage(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(FeedbackActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();

                    } else {
                        Toast.makeText(FeedbackActivity.this,addFeedbackModel.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else{
            Toast.makeText(FeedbackActivity.this,"You're not connected!", Toast.LENGTH_SHORT).show();
        }

    }


    private void getQueries() {
        if (ConnectivityHelper.isConnectedToNetwork(this)){
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
        }else{
            Toast.makeText(FeedbackActivity.this,"You're not connected!", Toast.LENGTH_SHORT).show();
        }

    }

    private void setUpAdapter() {
        manager = new LinearLayoutManager(FeedbackActivity.this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.rvQueries.setLayoutManager(manager);
        adapter = new FeedbackQueryAdapter(FeedbackActivity.this, queryList, listener);
        binding.rvQueries.setAdapter(adapter);
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
