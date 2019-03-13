package com.ziqqi.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.tbuonomo.viewpagerdotsindicator.SpringDotsIndicator;
import com.ziqqi.OnItemClickListener;
import com.ziqqi.R;
import com.ziqqi.adapters.ProductSliderAdapter;
import com.ziqqi.adapters.ReviewsAdapter;
import com.ziqqi.adapters.SimilarProductAdapter;
import com.ziqqi.databinding.ActivityProductDetailBinding;
import com.ziqqi.fragments.ProfileFragment;
import com.ziqqi.model.addtocart.AddToCart;
import com.ziqqi.model.addtowishlistmodel.AddToModel;
import com.ziqqi.model.productdetailsmodel.ProductDetails;
import com.ziqqi.model.productdetailsmodel.Review;
import com.ziqqi.model.removewislistmodel.DeleteWishlistModel;
import com.ziqqi.model.similarproductsmodel.SimilarProduct;
import com.ziqqi.utils.Constants;
import com.ziqqi.utils.LoginDialog;
import com.ziqqi.utils.PreferenceManager;
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
    List<Review> reviewDataList = new ArrayList<>();
    SimilarProductAdapter adapter;
    ReviewsAdapter reviewsAdapter;
    OnItemClickListener listener;
    SpacesItemDecoration spacesItemDecoration;
    String product_id;
    String strSharingUrl;
    LoginDialog loginDialog;
    int isWishlist = -1;
    String authToken = " ";

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

        binding.rvReviews.setHasFixedSize(true);
        binding.rvReviews.setNestedScrollingEnabled(false);

        if (getIntent().getExtras() != null) {
            product_id = getIntent().getStringExtra("product_id");
        }

        loginDialog = new LoginDialog();
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
        if (PreferenceManager.getBoolValue(Constants.LOGGED_IN)){
            authToken = PreferenceManager.getStringValue(Constants.AUTH_TOKEN);
        }

        getDetails(Integer.parseInt(product_id), authToken);
        setUpAdapter();
        getSimilar(Integer.parseInt(product_id));

        binding.tablayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    binding.tvOverview.setVisibility(View.VISIBLE);
                    binding.tvNoReviews.setVisibility(View.VISIBLE);
                    binding.tvSpecs.setVisibility(View.GONE);
                    binding.rvReviews.setVisibility(View.GONE);
                } else if (tab.getPosition() == 1) {
                    binding.tvSpecs.setVisibility(View.VISIBLE);
                    binding.tvOverview.setVisibility(View.GONE);
                    binding.tvNoReviews.setVisibility(View.VISIBLE);
                    binding.rvReviews.setVisibility(View.GONE);
                } else if (tab.getPosition() == 2) {
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

        binding.ivWishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PreferenceManager.getBoolValue(Constants.LOGGED_IN)){
                    if (isWishlist == 0){
                        addToWishlist(PreferenceManager.getStringValue(Constants.AUTH_TOKEN), Integer.parseInt(product_id));
                    }else if (isWishlist == 1){
                        removeWishlist(PreferenceManager.getStringValue(Constants.AUTH_TOKEN), Integer.parseInt(product_id));
                    }
                }else{
                    loginDialog.showDialog(ProductDetailActivity.this);
                }
            }
        });

        binding.ivShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.share(ProductDetailActivity.this, strSharingUrl);
            }
        });

        binding.tvAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PreferenceManager.getBoolValue(Constants.LOGGED_IN)){
                    addToCart(product_id, PreferenceManager.getStringValue(Constants.AUTH_TOKEN), "1");
                }else{
                    loginDialog.showDialog(ProductDetailActivity.this);
                }
            }
        });

        binding.tvBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PreferenceManager.getBoolValue(Constants.LOGGED_IN)){
                    Toast.makeText(getApplicationContext(), "OK", Toast.LENGTH_LONG).show();
                }else{
                    loginDialog.showDialog(ProductDetailActivity.this);
                }
            }
        });
    }


    private void getDetails(int id, String authToken) {
        binding.progressBar.setVisibility(View.VISIBLE);
        viewModel.fetchData(id, authToken);
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
                        strSharingUrl = "www.ziqqi.com/" + productDetails.getPayload().getLinkhref();
                        isWishlist = productDetails.getPayload().getIs_wishlist();

                        if (isWishlist == 1){
                            binding.ivWishlist.setImageResource(R.drawable.ic_favorite_black);
                        }else if (isWishlist == 0){
                            binding.ivWishlist.setImageResource(R.drawable.ic_wish);
                        }

                        binding.tvOverview.setText(resultOverview);
                        binding.tvSpecs.setText(resultSpecification);
                        bannerPayLoad.addAll(productDetails.getPayload().getImage());
                        feedbackPayLoad.addAll(productDetails.getPayload().getReviews());
                        if (productDetails.getPayload().getReviews().size() == 0) {
                            binding.rvReviews.setVisibility(View.GONE);
                            binding.tvNoReviews.setVisibility(View.VISIBLE);
                        }
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

    /*Wihslist*/
    private void addToWishlist(String authToken, int id) {
        binding.progressBar.setVisibility(View.VISIBLE);
        viewModel.addProductWishlist(authToken, id);
        viewModel.addWishlistResponse().observe(this, new Observer<AddToModel>() {
            @Override
            public void onChanged(@Nullable AddToModel addToModel) {
                if (!addToModel.getError()) {
                    binding.progressBar.setVisibility(View.GONE);
                    binding.ivWishlist.setImageResource(R.drawable.ic_favorite_black);
                    Toast.makeText(getApplicationContext(), addToModel.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), addToModel.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void removeWishlist(String authToken, int id) {
        binding.progressBar.setVisibility(View.VISIBLE);
        viewModel.removeWishlist(authToken, id);
        viewModel.deleteWishlistResponse().observe(this, new Observer<DeleteWishlistModel>() {
            @Override
            public void onChanged(@Nullable DeleteWishlistModel deleteWishlistModel) {
                if (!deleteWishlistModel.getError()) {
                    binding.progressBar.setVisibility(View.GONE);
                    binding.ivWishlist.setImageResource(R.drawable.ic_wish);
                    Toast.makeText(getApplicationContext(), deleteWishlistModel.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), deleteWishlistModel.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /*Cart*/
    private void addToCart(String id, String authToken, String quantity) {
        binding.progressBar.setVisibility(View.VISIBLE);
        viewModel.addProductToCart(id, authToken, quantity);
        viewModel.addCartResponse().observe(this, new Observer<AddToCart>() {
            @Override
            public void onChanged(@Nullable AddToCart addToCart) {
                if (!addToCart.getError()) {
                    binding.progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), addToCart.getMessage(), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ProductDetailActivity.this, MainActivity.class).putExtra("type", "cart"));
                } else {
                    Toast.makeText(getApplicationContext(), addToCart.getMessage(), Toast.LENGTH_SHORT).show();
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

        reviewsAdapter = new ReviewsAdapter(ProductDetailActivity.this, feedbackPayLoad, listener);
        binding.rvReviews.setAdapter(reviewsAdapter);
        spacesItemDecoration = new SpacesItemDecoration(ProductDetailActivity.this, R.dimen.dp_4);
        binding.rvReviews.addItemDecoration(spacesItemDecoration);
    }

    @Override
    public void onStop() {
        super.onStop();
        handler.removeCallbacks(update);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
