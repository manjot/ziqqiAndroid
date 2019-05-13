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
import android.widget.Toast;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarFinalValueListener;
import com.ziqqi.OnItemClickListener;
import com.ziqqi.R;
import com.ziqqi.adapters.filtersAdapter.FilterBrandAdapter;
import com.ziqqi.adapters.filtersAdapter.FilterCategoriesAdapter;
import com.ziqqi.adapters.filtersAdapter.FilterMainFeatureAdapter;
import com.ziqqi.adapters.filtersAdapter.FilterMainVariantAdapter;
import com.ziqqi.databinding.ActivityFilterBinding;
import com.ziqqi.model.filtermodel.FeatureFilter;
import com.ziqqi.model.filtermodel.FilterResponse;
import com.ziqqi.model.filtermodel.FilterValue;
import com.ziqqi.model.filtermodel.FilterValue_;
import com.ziqqi.model.filtermodel.VariantFilter;
import com.ziqqi.model.homecategorymodel.BestsellerProduct;
import com.ziqqi.utils.ConnectivityHelper;
import com.ziqqi.viewmodel.FilterProductViewModel;

import java.util.ArrayList;
import java.util.List;

public class FilterActivity extends AppCompatActivity {

    ActivityFilterBinding binding;
    FilterProductViewModel viewModel;
    List<FilterValue> filterCategoryList = new ArrayList<>();
    List<FilterValue> filterCategoryReturnedList = new ArrayList<>();
    List<FilterValue_> filterBrandList = new ArrayList<>();
    List<FilterValue_> filterBrandReturnedList = new ArrayList<>();
    List<VariantFilter> filterVariantList = new ArrayList<>();
    List<VariantFilter> filterVariantReturnedList = new ArrayList<>();
    List<FeatureFilter> filterFeatureList = new ArrayList<>();
    List<FeatureFilter> filterFeatureReturnedList = new ArrayList<>();
    LinearLayoutManager manager;
    LinearLayoutManager brandManager;
    LinearLayoutManager variantManager;
    LinearLayoutManager featureManager;
    FilterCategoriesAdapter adapter;
    FilterBrandAdapter brandAdapter;
    FilterMainVariantAdapter filterMainVariantAdapter;
    FilterMainFeatureAdapter filterMainFeatureAdapter;
    OnItemClickListener listener;
    String catId;
    boolean isCategoryExpanded = false, isBrandExpanded = false;

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

        listener = new OnItemClickListener() {
            @Override
            public void onItemClick(String id, String type) {

            }

            @Override
            public void onItemClick(BestsellerProduct bestsellerProduct, String type) {

            }
        };

        setUpCategoryAdapter();
        setUpBrandAdapter();
        setUpVariantsAdapter();
        setUpFeatureAdapter();

        getFiterCategories(catId);



        binding.ivExpandCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isCategoryExpanded){
                    isCategoryExpanded = true;
                    binding.rvCategory.setVisibility(View.VISIBLE);
                }else {
                    binding.rvCategory.setVisibility(View.GONE);
                    isCategoryExpanded = false;
                }
            }
        });

        binding.ivExpandBrand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isBrandExpanded){
                    isBrandExpanded = true;
                    binding.rvBrand.setVisibility(View.VISIBLE);
                }else {
                    binding.rvBrand.setVisibility(View.GONE);
                    isBrandExpanded = false;
                }
            }
        });
    }

    private void getFiterCategories(String catId) {
        if (ConnectivityHelper.isConnectedToNetwork(this)){
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.mainLayout.setVisibility(View.GONE);
            viewModel.fetchData(catId);
            viewModel.getFilterResponse().observe(this, new Observer<FilterResponse>() {
                @Override
                public void onChanged(@Nullable FilterResponse filterCategoriesResponse) {
                    if (!filterCategoriesResponse.getError()) {
                        binding.progressBar.setVisibility(View.GONE);
                        binding.mainLayout.setVisibility(View.VISIBLE);
                        if (filterCategoryList != null) {
                            filterCategoryReturnedList.clear();
                            filterCategoryList = filterCategoriesResponse.getPayload().getCategoryFilter().getFilterValue();
                            filterCategoryReturnedList.addAll(filterCategoryList);
                        }
                        if (filterBrandList != null) {
                            filterBrandReturnedList.clear();
                            filterBrandList = filterCategoriesResponse.getPayload().getBrandFilter().getFilterValue();
                            filterBrandReturnedList.addAll(filterBrandList);
                        }
                        if (filterVariantList != null) {
                            filterVariantReturnedList.clear();
                            filterVariantList = filterCategoriesResponse.getPayload().getVariantFilter();
                            filterVariantReturnedList.addAll(filterVariantList);
                        }
                        if (filterFeatureList != null) {
                            filterFeatureReturnedList.clear();
                            filterFeatureList = filterCategoriesResponse.getPayload().getFeatureFilter();
                            filterFeatureReturnedList.addAll(filterFeatureList);
                        }

                        binding.rangeSeekbar1.setMinValue(Float.parseFloat(filterCategoriesResponse.getMinPrice()));
                        binding.rangeSeekbar1.setMaxValue(Float.parseFloat(filterCategoriesResponse.getMaxPrice()));

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

    private void setUpCategoryAdapter() {
        manager = new LinearLayoutManager(FilterActivity.this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.rvCategory.setLayoutManager(manager);
        adapter = new FilterCategoriesAdapter(FilterActivity.this, 0, filterCategoryReturnedList, listener);
        binding.rvCategory.setAdapter(adapter);
    }

    private void setUpBrandAdapter() {
        brandManager = new LinearLayoutManager(FilterActivity.this);
        brandManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.rvBrand.setLayoutManager(brandManager);
        brandAdapter = new FilterBrandAdapter(FilterActivity.this, 0, filterBrandReturnedList, listener);
        binding.rvBrand.setAdapter(brandAdapter);
    }

    private void setUpVariantsAdapter() {
        variantManager = new LinearLayoutManager(FilterActivity.this);
        variantManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.rvFilterVariant.setLayoutManager(variantManager);
        filterMainVariantAdapter = new FilterMainVariantAdapter(FilterActivity.this, filterVariantReturnedList, listener);
        binding.rvFilterVariant.setAdapter(filterMainVariantAdapter);
    }

    private void setUpFeatureAdapter() {
        featureManager = new LinearLayoutManager(FilterActivity.this);
        featureManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.rvFilterFeature.setLayoutManager(featureManager);
        filterMainFeatureAdapter = new FilterMainFeatureAdapter(FilterActivity.this, filterFeatureReturnedList, listener);
        binding.rvFilterFeature.setAdapter(filterMainFeatureAdapter);
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
