package com.ziqqi.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.ziqqi.OnItemClickListener;
import com.ziqqi.R;
import com.ziqqi.adapters.BestSellerMainAdapter;
import com.ziqqi.adapters.SubCategoryAdapter;
import com.ziqqi.databinding.ActivityHomeCategoryBinding;
import com.ziqqi.model.homecategorymodel.HomeCategoriesResponse;
import com.ziqqi.model.homecategorymodel.Payload;
import com.ziqqi.utils.SpacesItemDecoration;
import com.ziqqi.utils.Utils;
import com.ziqqi.viewmodel.SubCategoryViewModel;

import java.util.ArrayList;
import java.util.List;

public class SubCategoryActivity extends AppCompatActivity {
    LinearLayoutManager managerBestSeller;
    GridLayoutManager manager;
    ActivityHomeCategoryBinding binding;
    SubCategoryViewModel viewModel;
    String categoryId;
    SubCategoryAdapter adapter;
    SpacesItemDecoration spacesItemDecoration;
    BestSellerMainAdapter bestSellerMainAdapter;

    List<Payload> payloadList = new ArrayList<>();
    List<Payload> bestSellerPayloadList = new ArrayList<>();
    OnItemClickListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home_category);
        viewModel = ViewModelProviders.of(this).get(SubCategoryViewModel.class);

        binding.executePendingBindings();
        binding.setViewModel(viewModel);

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button);

        listener = new OnItemClickListener() {
            @Override
            public void onItemClick(String id) {

            }

            @Override
            public void onItemClick(String id, String type) {
                startActivity(new Intent(SubCategoryActivity.this, ViewAllProductsActivity.class).putExtra("categoryId", id));
            }
        };

        setUpAdapter();

        if (getIntent().getExtras() != null) {
            categoryId = getIntent().getExtras().getString("categoryId");
        }

        setUpListUpdate();
    }

    private void setUpAdapter() {
        manager = new GridLayoutManager(this, 3);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int i) {
                if (adapter.getItemViewType(i) == 1) {
                    return 1;
                } else if (adapter.getItemViewType(i) == 2) {
                    return 2;
                } else {
                    return 3;
                }
            }
        });
        binding.rvMainRecyclerView.setLayoutManager(manager);
        adapter = new SubCategoryAdapter(this, payloadList, listener);
        binding.rvMainRecyclerView.setAdapter(adapter);

        spacesItemDecoration = new SpacesItemDecoration(this, R.dimen.dp_4);
        binding.rvMainRecyclerView.addItemDecoration(spacesItemDecoration);

        managerBestSeller = new LinearLayoutManager(this);
        managerBestSeller.setOrientation(LinearLayoutManager.VERTICAL);
        binding.rvBestSeller.setLayoutManager(managerBestSeller);
        bestSellerMainAdapter = new BestSellerMainAdapter(this, bestSellerPayloadList, listener);
        binding.rvBestSeller.setAdapter(bestSellerMainAdapter);
    }

    public void setUpListUpdate() {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.mainLayout.setVisibility(View.GONE);
        viewModel.fetchData(categoryId);
        viewModel.getSubCategoriesResponse().observe(this, new Observer<HomeCategoriesResponse>() {
            @Override
            public void onChanged(@Nullable HomeCategoriesResponse subCategories) {
                binding.progressBar.setVisibility(View.GONE);
                binding.mainLayout.setVisibility(View.VISIBLE);
                if (!subCategories.getError()) {
                    payloadList.addAll(subCategories.getPayload());
                    for (int i = 0; i < payloadList.size(); i++) {
                        if (!payloadList.get(i).getBestsellerProduct().isEmpty()) {
                            bestSellerPayloadList.add(payloadList.get(i));
                        }
                    }
                    adapter.notifyDataSetChanged();
                    bestSellerMainAdapter.notifyDataSetChanged();
                } else {
                    Utils.ShowToast(SubCategoryActivity.this, subCategories.getMessage());
                }
            }
        });
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
