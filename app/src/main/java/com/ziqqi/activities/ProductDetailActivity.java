package com.ziqqi.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TabHost;

import com.bumptech.glide.Glide;
import com.tbuonomo.viewpagerdotsindicator.SpringDotsIndicator;
import com.ziqqi.OnItemClickListener;
import com.ziqqi.R;
import com.ziqqi.adapters.ImageSliderAdapter;
import com.ziqqi.adapters.OverViewAdapter;
import com.ziqqi.adapters.ProductSliderAdapter;
import com.ziqqi.adapters.ReviewsAdapter;
import com.ziqqi.adapters.SearchAdapter;
import com.ziqqi.adapters.SimilarProductAdapter;
import com.ziqqi.databinding.ActivityProductDetailBinding;
import com.ziqqi.model.bannerimagemodel.BannerImageModel;
import com.ziqqi.model.productdetailsmodel.ProductDetails;
import com.ziqqi.model.productdetailsmodel.Review;
import com.ziqqi.model.searchmodel.Payload;
import com.ziqqi.model.searchmodel.SearchResponse;
import com.ziqqi.model.similarproductsmodel.SimilarProduct;
import com.ziqqi.utils.FontCache;
import com.ziqqi.utils.SpacesItemDecoration;
import com.ziqqi.utils.Utils;
import com.ziqqi.viewmodel.ProductDetailsViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ProductDetailActivity extends AppCompatActivity {

    ActivityProductDetailBinding binding;
    ProductDetailsViewModel viewModel;
    LinearLayoutManager manager, feedbackManager;
    ProductSliderAdapter productSliderAdapter;
    List<String> bannerPayLoad = new ArrayList<>();
    List<Review> feedbackPayLoad = new ArrayList<>();
    Handler handler;
    Runnable update;
    int currentPage = 0;
    SpringDotsIndicator indicator;
    List<com.ziqqi.model.similarproductsmodel.Payload> payloadsList = new ArrayList<>();
    List<com.ziqqi.model.similarproductsmodel.Payload> similarDataList = new ArrayList<>();
    List<com.ziqqi.model.productdetailsmodel.Payload> reviewDataList = new ArrayList<>();
    SimilarProductAdapter adapter;
    ReviewsAdapter reviewsAdapter;
    OnItemClickListener listener;
    SpacesItemDecoration spacesItemDecoration;
    String product_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_product_detail);
        viewModel = ViewModelProviders.of(this).get(ProductDetailsViewModel.class);
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button);
        getSupportActionBar().setElevation(0.0f);
        indicator = binding.circleIndicator;
        productSliderAdapter = new ProductSliderAdapter(this, bannerPayLoad);
        binding.sliderViewpager.setAdapter(productSliderAdapter);
        indicator.setViewPager(binding.sliderViewpager);
        binding.tvOverview.setVisibility(View.VISIBLE);

        if (getIntent().getExtras() != null){
            product_id = getIntent().getStringExtra("product_id");
        }

        handler = new Handler();
        update = new Runnable() {
            public void run() {
                if (currentPage == bannerPayLoad.size()) {
                    currentPage = 0;
                }
                binding.sliderViewpager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(update);
            }
        }, 2500, 2500);

        binding.sliderViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                currentPage = i;
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        getDetails(Integer.parseInt(product_id));
        setUpAdapter();
        getSimilar(Integer.parseInt(product_id));

        binding.tablayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    binding.tvOverview.setVisibility(View.VISIBLE);
                    binding.tvSpecs.setVisibility(View.GONE);
                    binding.rvReviews.setVisibility(View.GONE);
                } else if (tab.getPosition() == 1) {
                    binding.tvSpecs.setVisibility(View.VISIBLE);
                    binding.tvOverview.setVisibility(View.GONE);
                    binding.rvReviews.setVisibility(View.GONE);
                } else if (tab.getPosition() ==2){
                    binding.rvReviews.setVisibility(View.VISIBLE);
                    binding.tvOverview.setVisibility(View.GONE);
                    binding.tvSpecs.setVisibility(View.GONE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }



    private void getDetails(int id) {
        binding.progressBar.setVisibility(View.VISIBLE);
        viewModel.fetchData(id);
        viewModel.getProductDetailsResponse().observe(this, new Observer<ProductDetails>() {
            @Override
            public void onChanged(@Nullable ProductDetails productDetails) {
                if (!productDetails.getError()) {
                    binding.progressBar.setVisibility(View.GONE);
                    if (productDetails.getPayload() != null) {
                        binding.tvBrandName.setText(productDetails.getPayload().getBrandName());
                        binding.tvProductName.setText(productDetails.getPayload().getName());

                        String resultOverview = Html.fromHtml(productDetails.getPayload().getOverview()).toString();
                        String resultSpecification = Html.fromHtml(productDetails.getPayload().getSpecifications()).toString();

                        binding.tvOverview.setText(resultOverview);
                        binding.tvSpecs.setText(resultSpecification);
                        bannerPayLoad.addAll(productDetails.getPayload().getImage());
                        feedbackPayLoad.addAll(productDetails.getPayload().getReviews());
                        productSliderAdapter.notifyDataSetChanged();
                    }
                } else {
                    binding.progressBar.setVisibility(View.GONE);

                }
            }
        });
    }

    private void getSimilar(int id) {
        binding.rvSimilar.setVisibility(View.GONE);
        viewModel.fetchSimilarProducts(id);
        viewModel.getSimilarProductsResponse().observe(this, new Observer<SimilarProduct>() {
            @Override
            public void onChanged(@Nullable SimilarProduct similarProduct) {
                if (!similarProduct.getError()) {
                    binding.progressBar.setVisibility(View.GONE);
                    if (payloadsList != null) {
                        similarDataList.clear();
                        payloadsList = similarProduct.getPayload();
                        similarDataList.addAll(payloadsList);
                        binding.rvSimilar.setVisibility(View.VISIBLE);
                        binding.progressBar.setVisibility(View.GONE);
                    }
                } else {

                }
            }
        });
    }

    private void setUpAdapter() {
        manager = new LinearLayoutManager(ProductDetailActivity.this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);

        feedbackManager = new LinearLayoutManager(ProductDetailActivity.this);
        feedbackManager.setOrientation(LinearLayoutManager.VERTICAL);

        binding.rvSimilar.setLayoutManager(manager);
        binding.rvReviews.setLayoutManager(feedbackManager);

        adapter = new SimilarProductAdapter(ProductDetailActivity.this, similarDataList, listener);
        binding.rvSimilar.setAdapter(adapter);
        spacesItemDecoration = new SpacesItemDecoration(ProductDetailActivity.this, R.dimen.dp_4);
        binding.rvSimilar.addItemDecoration(spacesItemDecoration);

        reviewsAdapter = new ReviewsAdapter(ProductDetailActivity.this, reviewDataList, listener);
        binding.rvReviews.setAdapter(reviewsAdapter);
        spacesItemDecoration = new SpacesItemDecoration(ProductDetailActivity.this, R.dimen.dp_4);
        binding.rvReviews.addItemDecoration(spacesItemDecoration);
    }
    @Override
    public void onStop() {
        super.onStop();
        handler.removeCallbacks(update);
    }

}
