package com.ziqqi.activities;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ziqqi.OnItemClickListener;
import com.ziqqi.R;
import com.ziqqi.adapters.BestSellerMainAdapter;
import com.ziqqi.adapters.SubCategoryAdapter;
import com.ziqqi.databinding.ActivityHomeCategoryBinding;
import com.ziqqi.model.addtocart.AddToCart;
import com.ziqqi.model.addtowishlistmodel.AddToModel;
import com.ziqqi.model.homecategorymodel.BestsellerProduct;
import com.ziqqi.model.homecategorymodel.HomeCategoriesResponse;
import com.ziqqi.model.homecategorymodel.Payload;
import com.ziqqi.utils.Constants;
import com.ziqqi.utils.LoginDialog;
import com.ziqqi.utils.PreferenceManager;
import com.ziqqi.utils.SpacesItemDecoration;
import com.ziqqi.utils.Utils;
import com.ziqqi.viewmodel.SubCategoryViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.ziqqi.activities.MainActivity.eventName;

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
    LoginDialog loginDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home_category);
        viewModel = ViewModelProviders.of(this).get(SubCategoryViewModel.class);

        binding.executePendingBindings();
        binding.setViewModel(viewModel);

        Locale locale = new Locale(PreferenceManager.getStringValue(Constants.LANG));
        Configuration config = getBaseContext().getResources().getConfiguration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button);
        loginDialog = new LoginDialog();


        listener = new OnItemClickListener() {
            @Override
            public void onItemClick(String id, String type) {
                startActivity(new Intent(SubCategoryActivity.this, ViewAllProductsActivity.class).putExtra("categoryId", id));
            }

            @Override
            public void onItemClick(BestsellerProduct bestsellerProduct, String type) {
                switch (type) {
                    case Constants.SHARE:
                        share(SubCategoryActivity.this, bestsellerProduct.getLinkhref());
                        break;
                    case Constants.WISH_LIST:
                        if (PreferenceManager.getBoolValue(Constants.LOGGED_IN)) {
                            addToWishList(PreferenceManager.getStringValue(Constants.AUTH_TOKEN), bestsellerProduct.getProductId(), PreferenceManager.getStringValue(Constants.GUEST_ID));
                        } else {
                            addToWishList("", bestsellerProduct.getProductId(), PreferenceManager.getStringValue(Constants.GUEST_ID));
                        }
                        break;
                    case Constants.CART:
                        if (PreferenceManager.getBoolValue(Constants.LOGGED_IN)) {
                            viewModel.addToCart(bestsellerProduct.getProductId(), PreferenceManager.getStringValue(Constants.AUTH_TOKEN), "1", PreferenceManager.getStringValue(Constants.GUEST_ID));
                            viewModel.addToCartResponse().observe(SubCategoryActivity.this, new Observer<AddToCart>() {
                                @Override
                                public void onChanged(@Nullable AddToCart addToCart) {
                                    if (!addToCart.getError()) {
                                        Utils.showalertResponse(SubCategoryActivity.this, addToCart.getMessage());
                                    } else {
                                        Utils.showalertResponse(SubCategoryActivity.this, addToCart.getMessage());
                                    }
                                }
                            });
                        } else {
                            viewModel.addToCart(bestsellerProduct.getProductId(), "", "1", PreferenceManager.getStringValue(Constants.GUEST_ID));
                            viewModel.addToCartResponse().observe(SubCategoryActivity.this, new Observer<AddToCart>() {
                                @Override
                                public void onChanged(@Nullable AddToCart addToCart) {
                                    if (!addToCart.getError()) {
                                        Utils.showalertResponse(SubCategoryActivity.this, addToCart.getMessage());
                                    } else {
                                        Utils.showalertResponse(SubCategoryActivity.this, addToCart.getMessage());
                                    }
                                }
                            });
                        }

                        break;
                }
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
                    Glide.with(getApplicationContext()).load(subCategories.getCategory_banner()).apply(RequestOptions.placeholderOf(R.drawable.place_holder)).into(binding.ivBannerImage);                    for (int i = 0; i < payloadList.size(); i++) {
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

    public void share(Context context, String strSharingUrl) {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Ziqqi");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "www.ziqqi.com/" + strSharingUrl);
        startActivityForResult(Intent.createChooser(sharingIntent, context.getResources().getString(R.string.share_using)), 1710);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Activity.RESULT_OK) {

        }
    }

    private void addToWishList(String authToken, String id, String guest_id) {
        binding.progressBar.setVisibility(View.VISIBLE);
        viewModel.addProductWishlist(authToken, id, guest_id);
        viewModel.addWishlistResponse().observe(this, new Observer<AddToModel>() {
            @Override
            public void onChanged(@Nullable AddToModel addToModel) {
                if (!addToModel.getError()) {
                    binding.progressBar.setVisibility(View.GONE);
                    Utils.showalertResponse(SubCategoryActivity.this, addToModel.getMessage());
                } else {
                    Utils.showalertResponse(SubCategoryActivity.this, addToModel.getMessage());
                }
            }
        });
    }
}
