package com.ziqqi.activities;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
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
import android.widget.Toast;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.ziqqi.OnDealsItemClickListener;
import com.ziqqi.OnItemClickListener;
import com.ziqqi.R;
import com.ziqqi.adapters.DealsAdapter;
import com.ziqqi.databinding.ActivityViewAllDealsBinding;
import com.ziqqi.databinding.FragmentDealsBinding;
import com.ziqqi.model.addshippingaddressmodel.AddShippingAddressModel;
import com.ziqqi.model.addtocart.AddToCart;
import com.ziqqi.model.addtowishlistmodel.AddToModel;
import com.ziqqi.model.dealsmodel.DealsResponse;
import com.ziqqi.model.dealsmodel.Payload;
import com.ziqqi.utils.ConnectivityHelper;
import com.ziqqi.utils.Constants;
import com.ziqqi.utils.LoginDialog;
import com.ziqqi.utils.PreferenceManager;
import com.ziqqi.utils.SpacesItemDecoration;
import com.ziqqi.utils.Utils;
import com.ziqqi.viewmodel.DealsViewModel;
import com.ziqqi.viewmodel.ViewAllViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ViewAllDealsActivity extends AppCompatActivity {

    XRecyclerView rvDeals;
    LinearLayoutManager manager;
    ActivityViewAllDealsBinding binding;
    DealsViewModel viewModel;
    List<Payload> payloadList = new ArrayList<>();
    List<Payload> searchDataList = new ArrayList<>();
    OnDealsItemClickListener listener;
    DealsAdapter adapter;
    SpacesItemDecoration spacesItemDecoration;
    int pageCount = 1;
    LoginDialog loginDialog;
    com.ziqqi.addToCartListener addToCartListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_view_all_deals);
        viewModel = ViewModelProviders.of(this).get(DealsViewModel.class);

        Locale locale = new Locale(PreferenceManager.getStringValue(Constants.LANG));
        Configuration config = getBaseContext().getResources().getConfiguration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        binding.executePendingBindings();
        binding.setViewModel(viewModel);
        binding.setViewModel(viewModel);
        rvDeals = binding.rvDeals;

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button);
        loginDialog = new LoginDialog();
        checkConnection();

        listener = new OnDealsItemClickListener() {
            @Override
            public void onItemClick(String id, String type) {

            }

            @Override
            public void onItemClick(Payload payload, String type) {
                switch (type) {
                    case Constants.SHARE:
                        Utils.share(ViewAllDealsActivity.this, payload.getLinkhref());
                        break;
                    case Constants.WISH_LIST:
                        if (PreferenceManager.getBoolValue(Constants.LOGGED_IN)) {
                            addToWishList(PreferenceManager.getStringValue(Constants.AUTH_TOKEN), payload.getId(), PreferenceManager.getStringValue(Constants.GUEST_ID));
                        } else {
                            addToWishList("", payload.getId(), PreferenceManager.getStringValue(Constants.GUEST_ID));
                        }
                        break;
                    case Constants.CART:
                        if (PreferenceManager.getBoolValue(Constants.LOGGED_IN)) {
                            addToCart(payload.getId(), PreferenceManager.getStringValue(Constants.AUTH_TOKEN), PreferenceManager.getStringValue(Constants.GUEST_ID));
                        } else {
                            addToCart(payload.getId(), "", PreferenceManager.getStringValue(Constants.GUEST_ID));
                        }

                        break;
                }
            }
        };

        setUpAdapter();

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
        binding.rlFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(ViewAllDealsActivity.this, FilterActivity.class), 100);
            }
        });
    }

    private void getDeals() {
        if (ConnectivityHelper.isConnectedToNetwork(this)) {
            viewModel.fetchData(pageCount);
            viewModel.getDealsResponse().observe(this, new Observer<DealsResponse>() {
                @Override
                public void onChanged(@Nullable DealsResponse searchResponse) {
                    if (!searchResponse.getError()) {
                        binding.progressBar.setVisibility(View.GONE);
                        if (payloadList != null) {
                            searchDataList.clear();
                            payloadList = searchResponse.getPayload();

                            if (pageCount == 1) {
                                searchDataList.addAll(payloadList);
                                // adapter.setPayloadList(viewAllList);
                            } else {
                                searchDataList.addAll(payloadList);
                                // adapter.setPayloadList(viewAllList);
                                rvDeals.loadMoreComplete();
                            }

                            // adapter.notifyDataSetChanged();
                        }
                    } else {
                        Utils.ShowToast(ViewAllDealsActivity.this, searchResponse.getMessage());

                    }
                }
            });
        } else {
            Toast.makeText(ViewAllDealsActivity.this, "You're not connected!", Toast.LENGTH_SHORT).show();
        }

    }

    private void checkConnection() {
        if (ConnectivityHelper.isConnectedToNetwork(this)) {
            binding.progressBar.setVisibility(View.VISIBLE);
            getDeals();
        } else {
            Snackbar snackbar = Snackbar
                    .make(binding.rlMain, "No Internet Connection", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Try Again", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            checkConnection();
                        }
                    });

            snackbar.show();
        }
    }

    private void addToWishList(String authToken, String id, String guest_id) {
        if (ConnectivityHelper.isConnectedToNetwork(this)) {
            binding.progressBar.setVisibility(View.VISIBLE);
            viewModel.addProductWishlist(authToken, id, guest_id);
            viewModel.addWishlistResponse().observe(this, new Observer<AddToModel>() {
                @Override
                public void onChanged(@Nullable AddToModel addToModel) {
                    if (!addToModel.getError()) {
                        binding.progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), addToModel.getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), addToModel.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(ViewAllDealsActivity.this, "You're not connected!", Toast.LENGTH_SHORT).show();
        }

    }

    private void addToCart(String id, String authToken, String guest_id) {
        if (ConnectivityHelper.isConnectedToNetwork(this)) {
            binding.progressBar.setVisibility(View.VISIBLE);
            viewModel.addToCart(id, authToken, "1", guest_id);
            viewModel.addToCartResponse().observe(this, new Observer<AddToCart>() {
                @Override
                public void onChanged(@Nullable AddToCart addToCart) {
                    if (!addToCart.getError()) {
                        Toast.makeText(getApplicationContext(), addToCart.getMessage(), Toast.LENGTH_SHORT).show();
//                        addToCartListener.addToCart();
                    }

                }
            });
        } else {
            Toast.makeText(ViewAllDealsActivity.this, "You're not connected!", Toast.LENGTH_SHORT).show();
        }

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       if(requestCode==100){
           Log.e("ASDF : ", "ASDF");
       }
    }
}

