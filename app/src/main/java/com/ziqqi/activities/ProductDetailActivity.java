package com.ziqqi.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ziqqi.R;
import com.ziqqi.adapters.BookAdapter;
import com.ziqqi.adapters.OverViewAdapter;
import com.ziqqi.databinding.ActivityProductDetailBinding;
import com.ziqqi.model.productdetailsmodel.Payload;
import com.ziqqi.model.productdetailsmodel.ProductDetails;
import com.ziqqi.model.searchmodel.SearchResponse;
import com.ziqqi.viewmodel.ProductDetailsViewModel;
import com.ziqqi.viewmodel.SearchViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ProductDetailActivity extends AppCompatActivity {

    ActivityProductDetailBinding binding;
    ProductDetailsViewModel viewModel;
    LinearLayoutManager manager;
    List<Payload> payloadList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_product_detail);
        viewModel = ViewModelProviders.of(this).get(ProductDetailsViewModel.class);

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button);
        getSupportActionBar().setElevation(0.0f);
        getDetails(1539);

    }

    private void getDetails(int id) {
        binding.progressBar.setVisibility(View.VISIBLE);
        viewModel.fetchData(id);
        viewModel.getProductDetailsResponse().observe(this, new Observer<ProductDetails>() {
            @Override
            public void onChanged(@Nullable ProductDetails productDetails) {
                if (!productDetails.getError()) {
                    binding.progressBar.setVisibility(View.GONE);
                    if (payloadList != null) {
                        binding.tvBrandName.setText(payloadList.get(0).getBrandName());
                        binding.tvProductName.setText(payloadList.get(0).getName());
                        // adapter.notifyDataSetChanged();
                    }
                } else {
                    binding.progressBar.setVisibility(View.GONE);

                }
            }
        });
    }

}
