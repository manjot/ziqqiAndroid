package com.ziqqi.activities;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;
import com.tbuonomo.viewpagerdotsindicator.SpringDotsIndicator;
import com.ziqqi.OnSimilarItemClickListener;
import com.ziqqi.R;
import com.ziqqi.adapters.ProductSliderAdapter;
import com.ziqqi.adapters.ReviewsAdapter;
import com.ziqqi.adapters.SimilarProductAdapter;
import com.ziqqi.databinding.ActivityProductDetailBinding;
import com.ziqqi.fragments.CheckoutDialogFragment;
import com.ziqqi.model.addtocart.AddToCart;
import com.ziqqi.model.addtowishlistmodel.AddToModel;
import com.ziqqi.model.placeordermodel.PlaceOrderResponse;
import com.ziqqi.model.productdetailsmodel.ProductDetails;
import com.ziqqi.model.productdetailsmodel.Review;
import com.ziqqi.model.removewislistmodel.DeleteWishlistModel;
import com.ziqqi.model.similarproductsmodel.Payload;
import com.ziqqi.model.similarproductsmodel.SimilarProduct;
import com.ziqqi.utils.ConnectivityHelper;
import com.ziqqi.utils.Constants;
import com.ziqqi.utils.LoginDialog;
import com.ziqqi.utils.PreferenceManager;
import com.ziqqi.utils.SpacesItemDecoration;
import com.ziqqi.utils.Utils;
import com.ziqqi.viewmodel.ProductDetailsViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
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
    DotsIndicator indicator;
    List<com.ziqqi.model.similarproductsmodel.Payload> payloadsList = new ArrayList<>();
    List<com.ziqqi.model.similarproductsmodel.Payload> similarDataList = new ArrayList<>();
    List<Review> reviewDataList = new ArrayList<>();
    SimilarProductAdapter adapter;
    ReviewsAdapter reviewsAdapter;
    OnSimilarItemClickListener listener;
    SpacesItemDecoration spacesItemDecoration;
    String product_id;
    String strSharingUrl;
    LoginDialog loginDialog;
    int isWishlist = -1;
    String authToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_product_detail);
        viewModel = ViewModelProviders.of(this).get(ProductDetailsViewModel.class);

        Locale locale = new Locale(PreferenceManager.getStringValue(Constants.LANG));
        Configuration config = getBaseContext().getResources().getConfiguration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

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

        if (PreferenceManager.getBoolValue(Constants.LOGGED_IN)) {
            authToken = PreferenceManager.getStringValue(Constants.AUTH_TOKEN);
        }else {
            authToken = " ";
        }

        if (getIntent().getExtras() != null) {
            product_id = getIntent().getStringExtra("product_id");
        }

        listener = new OnSimilarItemClickListener() {
            @Override
            public void onItemClick(String id, String type) {

            }

            @Override
            public void onItemClick(Payload payload, String type) {
                switch (type) {
                    case Constants.SHARE:
                        share(ProductDetailActivity.this, payload.getId());
                        break;
                    case Constants.WISH_LIST:
                        if (PreferenceManager.getBoolValue(Constants.LOGGED_IN)) {
                            addToWishlist(PreferenceManager.getStringValue(Constants.AUTH_TOKEN), Integer.parseInt(product_id));
                        } else {
                            loginDialog.showDialog(ProductDetailActivity.this);
                        }
                        break;
                    case Constants.CART:
                        if (PreferenceManager.getBoolValue(Constants.LOGGED_IN)) {
                            addToCart(product_id, PreferenceManager.getStringValue(Constants.AUTH_TOKEN), "1");
                        } else {
                            loginDialog.showDialog(ProductDetailActivity.this);
                        }

                        break;
                }
            }
        };

        setUpAdapter();
        getSimilar(Integer.parseInt(product_id));
        getDetails(Integer.parseInt(product_id), authToken);

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


        binding.tablayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    binding.tvOverview.setVisibility(View.VISIBLE);
                    binding.tvSpecs.setVisibility(View.GONE);
                    binding.rvReviews.setVisibility(View.GONE);
                    binding.tvNoReviews.setVisibility(View.GONE);
                } else if (tab.getPosition() == 1) {
                    binding.tvSpecs.setVisibility(View.VISIBLE);
                    binding.tvOverview.setVisibility(View.GONE);
                    binding.rvReviews.setVisibility(View.GONE);
                    binding.tvNoReviews.setVisibility(View.GONE);
                } else if (tab.getPosition() == 2) {
                    binding.rvReviews.setVisibility(View.VISIBLE);
                    binding.tvOverview.setVisibility(View.GONE);
                    binding.tvSpecs.setVisibility(View.GONE);
                    binding.tvNoReviews.setVisibility(View.VISIBLE);
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
                if (PreferenceManager.getBoolValue(Constants.LOGGED_IN)) {
                    if (isWishlist == 0) {
                        addToWishlist(PreferenceManager.getStringValue(Constants.AUTH_TOKEN), Integer.parseInt(product_id));
                    } else if (isWishlist == 1) {
                        removeWishlist(PreferenceManager.getStringValue(Constants.AUTH_TOKEN), Integer.parseInt(product_id));
                    }
                } else {
                    loginDialog.showDialog(ProductDetailActivity.this);
                }
            }
        });

        binding.ivShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share(ProductDetailActivity.this, strSharingUrl);
            }
        });

        binding.tvAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PreferenceManager.getBoolValue(Constants.LOGGED_IN)) {
                    addToCart(product_id, PreferenceManager.getStringValue(Constants.AUTH_TOKEN), "1");
                } else {
                    loginDialog.showDialog(ProductDetailActivity.this);
                }
            }
        });

        binding.tvBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PreferenceManager.getBoolValue(Constants.LOGGED_IN)) {
                    addToBuy(product_id, PreferenceManager.getStringValue(Constants.AUTH_TOKEN), "1");
                    CheckoutDialogFragment dialogFragment = new CheckoutDialogFragment();
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.rl_container, dialogFragment);
                    ft.commit();
                } else {
                    loginDialog.showDialog(ProductDetailActivity.this);
                }
            }
        });
    }


    private void getDetails(int id, String authToken) {
        if (ConnectivityHelper.isConnectedToNetwork(this)){
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

                            if (isWishlist == 1) {
                                binding.ivWishlist.setImageResource(R.drawable.ic_favorite_black);
                            } else if (isWishlist == 0) {
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
        }else{
            Toast.makeText(ProductDetailActivity.this,"You're not connected!", Toast.LENGTH_SHORT).show();
        }

    }

    private void getSimilar(int id) {
        binding.rvSimilar.setVisibility(View.GONE);
        viewModel.fetchSimilarProducts(id);
        viewModel.getSimilarProductsResponse().observe(this, new Observer<SimilarProduct>() {
            @Override
            public void onChanged(@Nullable final SimilarProduct similarProduct) {
                if (!similarProduct.getError()) {
                    binding.progressBar.setVisibility(View.GONE);
                    if (similarProduct.getPayload().size() != 0) {
                        similarDataList.clear();
                        payloadsList = similarProduct.getPayload();
                        similarDataList.addAll(payloadsList);
                        binding.rvSimilar.setVisibility(View.VISIBLE);
                        binding.progressBar.setVisibility(View.GONE);

                        binding.tvViewAllMobiles.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(ProductDetailActivity.this, ViewAllProductsActivity.class).putExtra("categoryId", similarProduct.getCat_id()));
                            }
                        });
                    }else {
                        binding.llSimilar.setVisibility(View.GONE);
                    }
                } else {
                    binding.llSimilar.setVisibility(View.GONE);
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
                    finishAffinity();
                } else {
                    Toast.makeText(getApplicationContext(), addToCart.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addToBuy(String id, String authToken, String quantity) {
        binding.progressBar.setVisibility(View.VISIBLE);
        viewModel.addProductToCart(id, authToken, quantity);
        viewModel.addCartResponse().observe(this, new Observer<AddToCart>() {
            @Override
            public void onChanged(@Nullable AddToCart addToCart) {
                if (!addToCart.getError()) {
                    binding.progressBar.setVisibility(View.GONE);
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

        reviewsAdapter = new ReviewsAdapter(ProductDetailActivity.this, feedbackPayLoad);
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

    public void share(Context context, String strSharingUrl) {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Ziqqi");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "www.ziqqi.com/" + strSharingUrl);
        startActivityForResult(Intent.createChooser(sharingIntent, context.getResources().getString(R.string.share_using)), 1710);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {

        }
    }
}
