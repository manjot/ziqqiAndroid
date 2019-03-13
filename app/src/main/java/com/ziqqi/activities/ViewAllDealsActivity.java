package com.ziqqi.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.ziqqi.OnItemClickListener;
import com.ziqqi.R;
import com.ziqqi.adapters.DealsAdapter;
import com.ziqqi.databinding.ActivityViewAllDealsBinding;
import com.ziqqi.databinding.FragmentDealsBinding;
import com.ziqqi.model.dealsmodel.DealsResponse;
import com.ziqqi.model.dealsmodel.Payload;
import com.ziqqi.utils.SpacesItemDecoration;
import com.ziqqi.viewmodel.DealsViewModel;
import com.ziqqi.viewmodel.ViewAllViewModel;

import java.util.ArrayList;
import java.util.List;

public class ViewAllDealsActivity extends AppCompatActivity {

    XRecyclerView rvDeals;
    LinearLayoutManager manager;
    ActivityViewAllDealsBinding binding;
    DealsViewModel viewModel;
    List<Payload> payloadList = new ArrayList<>();
    List<Payload> searchDataList = new ArrayList<>();
    OnItemClickListener listener;
    DealsAdapter adapter;
    SpacesItemDecoration spacesItemDecoration;
    int pageCount = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_view_all_deals);
        viewModel = ViewModelProviders.of(this).get(DealsViewModel.class);

        binding.executePendingBindings();
        binding.setViewModel(viewModel);
        binding.setViewModel(viewModel);
        rvDeals = binding.rvDeals;

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button);


        setUpAdapter();
        getDeals();

        rvDeals.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                pageCount = 1;
                getDeals();
            }

            @Override
            public void onLoadMore() {
                pageCount++;
                Log.e("PageCOunt ", " " + pageCount);
                getDeals();
            }
        });
        binding.rvDeals.setPullRefreshEnabled(false);
    }

    private void getDeals() {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.rvDeals.setVisibility(View.GONE);
        viewModel.fetchData(1);
        viewModel.getDealsResponse().observe(this, new Observer<DealsResponse>() {
            @Override
            public void onChanged(@Nullable DealsResponse searchResponse) {
                if (!searchResponse.getError()) {
                    binding.progressBar.setVisibility(View.GONE);
                    if (payloadList != null) {
                        searchDataList.clear();
                        payloadList = searchResponse.getPayload();
                        searchDataList.addAll(payloadList);
                        binding.rvDeals.setVisibility(View.VISIBLE);
                        binding.progressBar.setVisibility(View.GONE);
                        // adapter.notifyDataSetChanged();
                    }
                } else {
                    binding.progressBar.setVisibility(View.GONE);

                }
            }
        });
    }

    private void setUpAdapter() {
        manager = new GridLayoutManager(ViewAllDealsActivity.this, 3);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.rvDeals.setLayoutManager(manager);
        adapter = new DealsAdapter(ViewAllDealsActivity.this, searchDataList, listener);
        binding.rvDeals.setAdapter(adapter);
        spacesItemDecoration = new SpacesItemDecoration(ViewAllDealsActivity.this, R.dimen.dp_4);
        binding.rvDeals.addItemDecoration(spacesItemDecoration);
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

