package com.ziqqi.activities;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
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

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarFinalValueListener;
import com.ziqqi.FilterItemListener;
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
import com.ziqqi.model.filtermodel.FilterValue__;
import com.ziqqi.model.filtermodel.FilterValue___;
import com.ziqqi.model.filtermodel.VariantFilter;
import com.ziqqi.model.filterproductmodel.Payload;
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

    List<FilterValue__> filterVariantInsideList = new ArrayList<>();
    List<FilterValue___> filterFeatureInsideList = new ArrayList<>();
    LinearLayoutManager manager;
    LinearLayoutManager brandManager;
    LinearLayoutManager variantManager;
    LinearLayoutManager featureManager;
    FilterCategoriesAdapter adapter;
    FilterBrandAdapter brandAdapter;
    FilterMainVariantAdapter filterMainVariantAdapter;
    FilterMainFeatureAdapter filterMainFeatureAdapter;
    FilterItemListener listener;
    String catId;
    boolean isCategoryExpanded = false, isBrandExpanded = false;

    ArrayList<String> categoryList = new ArrayList<String>();
    ArrayList<String> brandList = new ArrayList<String>();
    ArrayList<String> variantList = new ArrayList<String>();
    ArrayList<String> featureList = new ArrayList<String>();
    StringBuilder stringBuilderForFilter;
    String selectedCategoryFilters;
    String selectedBrandFilters;
    String selectedVariantFilters;
    String selectedFeatureFilters;
    String minRange;
    String maxRange;

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

        listener = new FilterItemListener() {
            @Override
            public void onFilterCategoryClick(int position, boolean isChecked) {
                if (isChecked){
                    categoryList.add(filterCategoryList.get(position).getId());
                }else{
                    for (int i=0;i<categoryList.size();i++){
                        if (categoryList.get(i).equals(filterCategoryList.get(position).getId())){
                            categoryList.remove(i);
                        }
                    }
                }
            }

            @Override
            public void onFilterBrandClick(int position, boolean isChecked) {
                if (isChecked){
                    brandList.add(filterBrandList.get(position).getId());
                }else{
                    for (int i=0;i<brandList.size();i++){
                        if (brandList.get(i).equals(filterBrandList.get(position).getId())){
                            brandList.remove(i);
                        }
                    }
                }
            }

            @Override
            public void onFilterVariantClick(int position, boolean isChecked) {
                if (isChecked){
                    variantList.add(filterVariantInsideList.get(position).getId());
                }else{
                    for (int i=0;i<variantList.size();i++){
                        if (variantList.get(i).equals(filterVariantInsideList.get(position).getId())){
                            variantList.remove(i);
                        }
                    }
                }
            }

            @Override
            public void onFilterFeatureClick(int position, boolean isChecked) {
                if (isChecked){
                    featureList.add(filterFeatureInsideList.get(position).getId());
                }else{
                    for (int i=0;i<featureList.size();i++){
                        if (featureList.get(i).equals(filterFeatureInsideList.get(position).getId())){
                            featureList.remove(i);
                        }
                    }
                }
            }
        };

        setUpCategoryAdapter();
        setUpBrandAdapter();
        setUpVariantsAdapter();
        setUpFeatureAdapter();

        getFiterCategories(catId);

        stringBuilderForFilter = new StringBuilder();

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

        binding.btApply.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {

                /*for(int  i =0;i<list.size();i++)
                {
                    String prefix = "";
                    for (String str : list)
                    {
                        stringBuilderForFilter.append(prefix);
                        prefix = ",";
                        stringBuilderForFilter.append(str);
                    }
                }


                selectedFilters = ""+stringBuilderForFilter;*/

                selectedCategoryFilters = String.join(",", categoryList);
                selectedBrandFilters = String.join(",", brandList);
                selectedVariantFilters = String.join(",", variantList);
                selectedFeatureFilters = String.join(",", featureList);
                Log.i("Ids", selectedCategoryFilters);

                Intent i = new Intent();
                i.putExtra("CAT_FILTERS", selectedCategoryFilters);
                i.putExtra("BRAND_FILTERS", selectedBrandFilters);
                i.putExtra("VARIANT_FILTERS", selectedVariantFilters);
                i.putExtra("FEATURE_FILTERS", selectedFeatureFilters);
                i.putExtra("MIN", minRange);
                i.putExtra("MAX", maxRange);
                setResult(100, i);
                finish();
            }
        });

        binding.btCancel.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {

                selectedCategoryFilters = "";
                selectedBrandFilters = "";
                selectedVariantFilters = "";
                selectedFeatureFilters = "";
                maxRange = "";
                minRange = "";
                Log.i("Ids", selectedCategoryFilters);

                Intent i = new Intent();
                i.putExtra("CAT_FILTERS", selectedCategoryFilters);
                i.putExtra("BRAND_FILTERS", selectedBrandFilters);
                i.putExtra("VARIANT_FILTERS", selectedVariantFilters);
                i.putExtra("FEATURE_FILTERS", selectedFeatureFilters);
                i.putExtra("MIN", minRange);
                i.putExtra("MAX", maxRange);
                setResult(101, i);
                finish();
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
                            for(int i = 0; i < filterCategoriesResponse.getPayload().getVariantFilter().size(); i++){
                                filterVariantInsideList.addAll(filterCategoriesResponse.getPayload().getVariantFilter().get(i).getFilterValue());
                            }


                        }
                        if (filterFeatureList != null) {
                            filterFeatureReturnedList.clear();
                            filterFeatureList = filterCategoriesResponse.getPayload().getFeatureFilter();
                            filterFeatureReturnedList.addAll(filterFeatureList);

                            for(int i = 0; i < filterCategoriesResponse.getPayload().getFeatureFilter().size(); i++){
                                filterFeatureInsideList.addAll(filterCategoriesResponse.getPayload().getFeatureFilter().get(i).getFilterValue());
                            }
                        }

                        binding.rangeSeekbar1.setMinValue(Float.parseFloat(filterCategoriesResponse.getMinPrice()));
                        binding.rangeSeekbar1.setMaxValue(Float.parseFloat(filterCategoriesResponse.getMaxPrice()));

                        minRange = filterCategoriesResponse.getMinPrice();
                        maxRange = filterCategoriesResponse.getMaxPrice();

                        binding.rangeSeekbar1.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
                            @Override
                            public void valueChanged(Number minValue, Number maxValue) {
                                binding.textMin1.setText("$ "+ minValue);
                                binding.textMax1.setText("$ "+ maxValue);

                                minRange = String.valueOf(minValue);
                                maxRange = String.valueOf(maxValue);
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

    @Override
    public void onBackPressed() {
        selectedCategoryFilters = "";
        selectedBrandFilters = "";
        selectedVariantFilters = "";
        selectedFeatureFilters = "";
        maxRange = "";
        minRange = "";
        Log.i("Ids", selectedCategoryFilters);

        Intent i = new Intent();
        i.putExtra("CAT_FILTERS", selectedCategoryFilters);
        i.putExtra("BRAND_FILTERS", selectedBrandFilters);
        i.putExtra("VARIANT_FILTERS", selectedVariantFilters);
        i.putExtra("FEATURE_FILTERS", selectedFeatureFilters);
        i.putExtra("MIN", minRange);
        i.putExtra("MAX", maxRange);
        setResult(101, i);
        finish();
    }
}
