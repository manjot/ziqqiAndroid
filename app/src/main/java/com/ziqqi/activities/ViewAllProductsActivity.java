package com.ziqqi.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.ziqqi.OnAllItemClickListener;
import com.ziqqi.OnItemClickListener;
import com.ziqqi.OnSearchedItemClickListener;
import com.ziqqi.R;
import com.ziqqi.adapters.ViewAllProductAdapter;
import com.ziqqi.databinding.ActivityViewAllProductsBinding;
import com.ziqqi.model.addtocart.AddToCart;
import com.ziqqi.model.addtowishlistmodel.AddToModel;
import com.ziqqi.model.productcategorymodel.Payload;
import com.ziqqi.model.productcategorymodel.ProductCategory;
import com.ziqqi.utils.ConnectivityHelper;
import com.ziqqi.utils.Constants;
import com.ziqqi.utils.LoginDialog;
import com.ziqqi.utils.PreferenceManager;
import com.ziqqi.utils.SpacesItemDecoration;
import com.ziqqi.utils.Utils;
import com.ziqqi.viewmodel.ViewAllViewModel;

import java.util.ArrayList;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

public class ViewAllProductsActivity extends AppCompatActivity {
    ActivityViewAllProductsBinding binding;
    ViewAllViewModel viewModel;
    LinearLayoutManager manager;
    SpacesItemDecoration spacesItemDecoration;
    ViewAllProductAdapter adapter;
    OnAllItemClickListener listener;
    List<Payload> payloadList, viewAllList;
    String categoryId;
    int pageCount = 1;
    LoginDialog loginDialog;
    com.ziqqi.addToCartListener addToCartListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_view_all_products);
        viewModel = ViewModelProviders.of(this).get(ViewAllViewModel.class);

        binding.executePendingBindings();
        binding.setViewModel(viewModel);

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button);

        loginDialog = new LoginDialog();
        payloadList = new ArrayList<>();
        viewAllList = new ArrayList<>();

        if (getIntent().getExtras() != null) {
            categoryId = getIntent().getExtras().getString("categoryId");
        }

        listener = new OnAllItemClickListener() {
            @Override
            public void onItemClick(String id, String type) {

            }

            @Override
            public void onItemClick(Payload payload, String type) {
                switch (type) {
                    case Constants.SHARE:
                        Utils.share(ViewAllProductsActivity.this, payload.getId());
                        break;
                    case Constants.WISH_LIST:
                        if (PreferenceManager.getBoolValue(Constants.LOGGED_IN)){
                            addToWishList(PreferenceManager.getStringValue(Constants.AUTH_TOKEN), payload.getId());
                        }else{
                            loginDialog.showDialog(ViewAllProductsActivity.this);
                        }
                        break;
                    case Constants.CART:
                        if (PreferenceManager.getBoolValue(Constants.LOGGED_IN)){
                            addToCart(payload.getId());
                        }else{
                            loginDialog.showDialog(ViewAllProductsActivity.this);
                        }

                        break;
                }
            }
        };

        setUpAdapter();

        checkConnection();

        binding.recyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                pageCount = 1;
                getData();
            }

            @Override
            public void onLoadMore() {
                pageCount++;
                Log.e("PageCOunt ", " " + pageCount);
                getData();
            }
        });
        binding.recyclerView.setPullRefreshEnabled(false);
    }

    private void addToWishList(String authToken, String id) {
        if (ConnectivityHelper.isConnectedToNetwork(this)){
            binding.progressBar.setVisibility(View.VISIBLE);
            viewModel.addProductWishlist(authToken, id);
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
        }else{
            Toast.makeText(ViewAllProductsActivity.this,"You're not connected!", Toast.LENGTH_SHORT).show();
        }

    }

    private void addToCart(String id) {
        if (ConnectivityHelper.isConnectedToNetwork(this)){
            binding.progressBar.setVisibility(View.VISIBLE);
            viewModel.addToCart(id, PreferenceManager.getStringValue(Constants.AUTH_TOKEN), "1");
            viewModel.addToCartResponse().observe(this, new Observer<AddToCart>() {
                @Override
                public void onChanged(@Nullable AddToCart addToCart) {
                    if (!addToCart.getError()){
                        Toast.makeText(getApplicationContext(),  addToCart.getMessage(), Toast.LENGTH_SHORT).show();
                        addToCartListener.addToCart();
                    }
                }
            });
        }else{
            Toast.makeText(ViewAllProductsActivity.this,"You're not connected!", Toast.LENGTH_SHORT).show();
        }

    }


    private void setUpAdapter() {
        manager = new GridLayoutManager(this, 3);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.recyclerView.setLayoutManager(manager);
        adapter = new ViewAllProductAdapter(this, viewAllList, listener);
        binding.recyclerView.setAdapter(adapter);
        spacesItemDecoration = new SpacesItemDecoration(this, R.dimen.dp_4);
        binding.recyclerView.addItemDecoration(spacesItemDecoration);
    }

    private void getData() {
        Log.e("PageCount ", " " + pageCount);
        viewModel.fetchData(categoryId, String.valueOf(pageCount));
        viewModel.getCategoryProduct().observe(this, new Observer<ProductCategory>() {
            @Override
            public void onChanged(@Nullable ProductCategory productCategory) {
                binding.progressBar.setVisibility(View.GONE);
                if (!productCategory.getError()) {
                    if (payloadList != null) {
                        // viewAllList.clear();
                        payloadList = productCategory.getPayload();

                        if (pageCount == 1) {
                            viewAllList.addAll(payloadList);
                            // adapter.setPayloadList(viewAllList);
                        } else {
                            viewAllList.addAll(payloadList);
                            // adapter.setPayloadList(viewAllList);
                            binding.recyclerView.loadMoreComplete();
                        }

                        // adapter.notifyDataSetChanged();
                    }
                } else {
                    Utils.ShowToast(ViewAllProductsActivity.this, productCategory.getMessage());
                }
            }
        });
    }

    private void checkConnection() {
        if (ConnectivityHelper.isConnectedToNetwork(this)) {
            binding.progressBar.setVisibility(View.VISIBLE);
            getData();
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
