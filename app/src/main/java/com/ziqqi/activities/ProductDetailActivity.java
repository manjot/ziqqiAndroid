package com.ziqqi.activities;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;
import com.tbuonomo.viewpagerdotsindicator.SpringDotsIndicator;
import com.ziqqi.OnItemClickListener;
import com.ziqqi.OnSimilarItemClickListener;
import com.ziqqi.OnVariantItemClickListener;
import com.ziqqi.R;
import com.ziqqi.adapters.FeedbackVariantAdapter;
import com.ziqqi.adapters.ProductSliderAdapter;
import com.ziqqi.adapters.ReviewsAdapter;
import com.ziqqi.adapters.SimilarProductAdapter;
import com.ziqqi.adapters.VariantMainAdapter;
import com.ziqqi.databinding.ActivityProductDetailBinding;
import com.ziqqi.fragments.CheckoutDialogFragment;
import com.ziqqi.model.addtocart.AddToCart;
import com.ziqqi.model.addtowishlistmodel.AddToModel;
import com.ziqqi.model.loadvariantmodel.LoadVariantResponse;
import com.ziqqi.model.placeordermodel.PlaceOrderResponse;
import com.ziqqi.model.productdetailsmodel.ProductDetails;
import com.ziqqi.model.productdetailsmodel.Review;
import com.ziqqi.model.productvariantmodel.ProductVariantModel;
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
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class ProductDetailActivity extends AppCompatActivity {

    ActivityProductDetailBinding binding;
    ProductDetailsViewModel viewModel;
    LinearLayoutManager manager, feedbackManager, variantManager;
    ProductSliderAdapter productSliderAdapter;
    List<String> bannerPayLoad = new ArrayList<>();
    List<Review> feedbackPayLoad = new ArrayList<>();
    List<com.ziqqi.model.loadvariantmodel.Review> feedbackPayLoad2 = new ArrayList<>();
    List<com.ziqqi.model.productvariantmodel.Payload> productVariantList = new ArrayList<>();
    List<com.ziqqi.model.productvariantmodel.Payload> productVariantReturnedList = new ArrayList<>();
    Handler handler;
    Runnable update;
    int currentPage = 0;
    DotsIndicator indicator;
    List<com.ziqqi.model.similarproductsmodel.Payload> payloadsList = new ArrayList<>();
    List<com.ziqqi.model.similarproductsmodel.Payload> similarDataList = new ArrayList<>();
    List<Review> reviewDataList = new ArrayList<>();
    SimilarProductAdapter adapter;
    ReviewsAdapter reviewsAdapter;
    FeedbackVariantAdapter feedbackVariantAdapter;
    OnSimilarItemClickListener listener;
    OnItemClickListener listener2;
    OnVariantItemClickListener listener3;
    SpacesItemDecoration spacesItemDecoration;
    String product_id, variant_id;
    String strSharingUrl;
    LoginDialog loginDialog;
    int isWishlist = -1;
    String authToken;
    VariantMainAdapter variantMainAdapter;
    List<String> items;
    String strSendingVariant;

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
        } else {
            authToken = "";
        }

        if (getIntent().getExtras() != null) {
            product_id = getIntent().getStringExtra("product_id");

            if (getIntent().getStringExtra("variant_id") != null){
                variant_id = getIntent().getStringExtra("variant_id");
                items = Arrays.asList(variant_id.split("\\s*,\\s*"));
                strSendingVariant = items.get(0);
            }else{
                strSendingVariant = "";
            }

        }

        listener = new OnSimilarItemClickListener() {
            @Override
            public void onItemClick(String id, String type) {

            }

            @Override
            public void onItemClick(Payload payload, String type) {
                switch (type) {
                    case Constants.SHARE:
                        share(ProductDetailActivity.this, payload.getLinkhref());
                        break;
                    case Constants.WISH_LIST:
                        if (PreferenceManager.getBoolValue(Constants.LOGGED_IN)) {
                            addToWishlist(PreferenceManager.getStringValue(Constants.AUTH_TOKEN), Integer.parseInt(product_id), PreferenceManager.getStringValue(Constants.GUEST_ID));
                        } else {
                            addToWishlist("", Integer.parseInt(product_id), PreferenceManager.getStringValue(Constants.GUEST_ID));
                        }
                        break;
                    case Constants.CART:
                        if (PreferenceManager.getBoolValue(Constants.LOGGED_IN)) {
                            addToCart(product_id, PreferenceManager.getStringValue(Constants.AUTH_TOKEN), "1", PreferenceManager.getStringValue(Constants.GUEST_ID));
                        } else {
                            addToCart(product_id, "", "1", PreferenceManager.getStringValue(Constants.GUEST_ID));
                        }

                        break;
                }
            }
        };

        listener3 = new OnVariantItemClickListener() {
            @Override
            public void onFilterCategoryClick(String variantId) {
                loadVariant(authToken, product_id, PreferenceManager.getStringValue(Constants.GUEST_ID), variantId);
            }
        };

        setUpAdapter();
        getSimilar(Integer.parseInt(product_id));
        getDetails(Integer.parseInt(product_id), authToken, PreferenceManager.getStringValue(Constants.GUEST_ID), strSendingVariant);
        getProductVariants(product_id);

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
                        addToWishlist(PreferenceManager.getStringValue(Constants.AUTH_TOKEN), Integer.parseInt(product_id), PreferenceManager.getStringValue(Constants.GUEST_ID));
                    } else if (isWishlist == 1) {
                        removeWishlist(PreferenceManager.getStringValue(Constants.AUTH_TOKEN), Integer.parseInt(product_id), PreferenceManager.getStringValue(Constants.GUEST_ID));
                    }
                } else {
                    if (isWishlist == 0) {
                        addToWishlist("", Integer.parseInt(product_id), PreferenceManager.getStringValue(Constants.GUEST_ID));
                    } else if (isWishlist == 1) {
                        removeWishlist("", Integer.parseInt(product_id), PreferenceManager.getStringValue(Constants.GUEST_ID));
                    }
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
                    addToCart(product_id, PreferenceManager.getStringValue(Constants.AUTH_TOKEN), "1", PreferenceManager.getStringValue(Constants.GUEST_ID));
                } else {
                    addToCart(product_id, "", "1", PreferenceManager.getStringValue(Constants.GUEST_ID));
                }
            }
        });

        binding.tvBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PreferenceManager.getBoolValue(Constants.LOGGED_IN)) {
                    addToBuy(product_id, PreferenceManager.getStringValue(Constants.AUTH_TOKEN), "1", PreferenceManager.getStringValue(Constants.GUEST_ID));
                    CheckoutDialogFragment dialogFragment = new CheckoutDialogFragment();
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.rl_container, dialogFragment);
                    ft.commit();
                } else {
                    addToBuy(product_id, "", "1", PreferenceManager.getStringValue(Constants.GUEST_ID));
                }
            }
        });
    }


    private void getDetails(int id, String authToken, String guestId, String variantId) {
        if (ConnectivityHelper.isConnectedToNetwork(this)) {
            binding.progressBar.setVisibility(View.VISIBLE);
            viewModel.fetchData(id, authToken, guestId, variantId);
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

                            binding.tvPrice.setText("$ "+productDetails.getPayload().getMrpPrice());
                            binding.tvPrice.setPaintFlags(binding.tvPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                            binding.tvDiscountPrice.setText("$ "+productDetails.getPayload().getSalePrice());
                            binding.tvOverview.setText(resultOverview);
                            binding.tvSpecs.setText(resultSpecification);
                            bannerPayLoad.clear();
                            feedbackPayLoad.clear();
                            bannerPayLoad.addAll(productDetails.getPayload().getImage());
                            feedbackPayLoad.addAll(productDetails.getPayload().getReviews());
                            if (productDetails.getPayload().getReviews().size() == 0) {
                                binding.rvReviews.setVisibility(View.GONE);
                                binding.tvNoReviews.setVisibility(View.VISIBLE);
                            }
                            productSliderAdapter.notifyDataSetChanged();
                            reviewsAdapter.notifyDataSetChanged();
                        }
                    } else {
                        binding.progressBar.setVisibility(View.GONE);

                    }
                }
            });
        } else {
            Toast.makeText(ProductDetailActivity.this, "You're not connected!", Toast.LENGTH_SHORT).show();
        }

    }


    private void loadVariant(String authToken, String productId, String guestId, String attributeId) {
        if (ConnectivityHelper.isConnectedToNetwork(this)) {
            binding.progressBar.setVisibility(View.VISIBLE);
            viewModel.loadVariant(authToken, productId, guestId, attributeId);
            viewModel.loadVariantResponse().observe(this, new Observer<LoadVariantResponse>() {
                @Override
                public void onChanged(@Nullable LoadVariantResponse loadVariantResponse) {
                    if (!loadVariantResponse.getError()) {
                        binding.progressBar.setVisibility(View.GONE);
                        if (loadVariantResponse.getPayload() != null) {
                            binding.tvBrandName.setText(loadVariantResponse.getPayload().getBrandName());
                            binding.tvProductName.setText(loadVariantResponse.getPayload().getName());

                            String resultOverview = Html.fromHtml(loadVariantResponse.getPayload().getOverview()).toString();
                            String resultSpecification = Html.fromHtml(loadVariantResponse.getPayload().getSpecifications()).toString();

                            strSharingUrl = "www.ziqqi.com/" + loadVariantResponse.getPayload().getLinkhref();
                            isWishlist = loadVariantResponse.getPayload().getIsWishlist();

                            if (isWishlist == 1) {
                                binding.ivWishlist.setImageResource(R.drawable.ic_favorite_black);
                            } else if (isWishlist == 0) {
                                binding.ivWishlist.setImageResource(R.drawable.ic_wish);
                            }

                            binding.tvPrice.setText("$ "+loadVariantResponse.getPayload().getMrpPrice());
                            binding.tvPrice.setPaintFlags(binding.tvPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                            binding.tvDiscountPrice.setText("$ "+loadVariantResponse.getPayload().getSalePrice());
                            binding.tvOverview.setText(resultOverview);
                            binding.tvSpecs.setText(resultSpecification);
                            bannerPayLoad.clear();
                            feedbackPayLoad2.clear();
                            bannerPayLoad.addAll(loadVariantResponse.getPayload().getImage());
                            feedbackPayLoad2.addAll(loadVariantResponse.getPayload().getReviews());
                            if (loadVariantResponse.getPayload().getReviews().size() == 0) {
                                binding.rvReviews.setVisibility(View.GONE);
                                binding.tvNoReviews.setVisibility(View.VISIBLE);
                                setUpVariantReviewAdapter();
                            }
                            productSliderAdapter.notifyDataSetChanged();
                            reviewsAdapter.notifyDataSetChanged();
                        }
                    } else {
                        binding.progressBar.setVisibility(View.GONE);

                    }
                }
            });
        } else {
            Toast.makeText(ProductDetailActivity.this, "You're not connected!", Toast.LENGTH_SHORT).show();
        }

    }

    /*Similar*/
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
                    } else {
                        binding.llSimilar.setVisibility(View.GONE);
                    }
                } else {
                    binding.llSimilar.setVisibility(View.GONE);
                }
            }
        });
    }

    /*ProductVariants*/
    private void getProductVariants(String productId) {
        viewModel.getProductVariant(productId);
        viewModel.getProductVariantResponse().observe(this, new Observer<ProductVariantModel>() {
            @Override
            public void onChanged(@Nullable ProductVariantModel productVariantModel) {
                if (!productVariantModel.getError()) {
                    binding.rvMainVariant.setVisibility(View.VISIBLE);
                    if (productVariantModel.getPayload().size() != 0) {
                        productVariantReturnedList.clear();
                        productVariantList = productVariantModel.getPayload();
                        for (int i = 0; i < productVariantList.size(); i++) {
                            if (productVariantList.get(i).getAttributeValue().size() > 0) {
                                productVariantReturnedList.add(productVariantList.get(i));
                            }
                        }
                        variantMainAdapter.notifyDataSetChanged();
                    } else {
                        binding.rvMainVariant.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    /*Wihslist*/
    private void addToWishlist(String authToken, int id, String guest_id) {
        binding.progressBar.setVisibility(View.VISIBLE);
        viewModel.addProductWishlist(authToken, id, guest_id);
        viewModel.addWishlistResponse().observe(this, new Observer<AddToModel>() {
            @Override
            public void onChanged(@Nullable AddToModel addToModel) {
                if (!addToModel.getError()) {
                    binding.progressBar.setVisibility(View.GONE);
                    binding.ivWishlist.setImageResource(R.drawable.ic_favorite_black);
                    isWishlist = 1;
                    Toast.makeText(getApplicationContext(), addToModel.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), addToModel.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void removeWishlist(String authToken, int id, String guest_id) {
        binding.progressBar.setVisibility(View.VISIBLE);
        viewModel.removeWishlist(authToken, id, guest_id);
        viewModel.deleteWishlistResponse().observe(this, new Observer<DeleteWishlistModel>() {
            @Override
            public void onChanged(@Nullable DeleteWishlistModel deleteWishlistModel) {
                if (!deleteWishlistModel.getError()) {
                    binding.progressBar.setVisibility(View.GONE);
                    binding.ivWishlist.setImageResource(R.drawable.ic_wish);
                    isWishlist = 0;
                    Toast.makeText(getApplicationContext(), deleteWishlistModel.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), deleteWishlistModel.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /*Cart*/
    private void addToCart(String id, String authToken, String quantity, String guest_id) {
        binding.progressBar.setVisibility(View.VISIBLE);
        viewModel.addProductToCart(id, authToken, quantity, guest_id);
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

    /*Buy*/
    private void addToBuy(String id, String authToken, String quantity, String guest_id) {
        binding.progressBar.setVisibility(View.VISIBLE);
        viewModel.addProductToCart(id, authToken, quantity, guest_id);
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

        variantManager = new LinearLayoutManager(ProductDetailActivity.this);
        variantManager.setOrientation(LinearLayoutManager.VERTICAL);

        binding.rvSimilar.setLayoutManager(manager);
        binding.rvReviews.setLayoutManager(feedbackManager);
        binding.rvMainVariant.setLayoutManager(variantManager);

        adapter = new SimilarProductAdapter(ProductDetailActivity.this, similarDataList, listener);
        binding.rvSimilar.setAdapter(adapter);
        spacesItemDecoration = new SpacesItemDecoration(ProductDetailActivity.this, R.dimen.dp_4);
        binding.rvSimilar.addItemDecoration(spacesItemDecoration);

        reviewsAdapter = new ReviewsAdapter(ProductDetailActivity.this, feedbackPayLoad);
        binding.rvReviews.setAdapter(reviewsAdapter);
        spacesItemDecoration = new SpacesItemDecoration(ProductDetailActivity.this, R.dimen.dp_4);
        binding.rvReviews.addItemDecoration(spacesItemDecoration);

        variantMainAdapter = new VariantMainAdapter(ProductDetailActivity.this, productVariantReturnedList, listener3);
        binding.rvMainVariant.setAdapter(variantMainAdapter);
        spacesItemDecoration = new SpacesItemDecoration(ProductDetailActivity.this, R.dimen.dp_4);
        binding.rvMainVariant.addItemDecoration(spacesItemDecoration);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(binding.rvMainVariant.getContext(),
                variantManager.getOrientation());
        binding.rvMainVariant.addItemDecoration(dividerItemDecoration);
    }

    private void setUpVariantReviewAdapter(){
        feedbackVariantAdapter = new FeedbackVariantAdapter(ProductDetailActivity.this, feedbackPayLoad2);
        binding.rvReviews.setAdapter(feedbackVariantAdapter);
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
