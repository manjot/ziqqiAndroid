package com.ziqqi.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.ziqqi.OnSearchedItemClickListener;
import com.ziqqi.R;
import com.ziqqi.adapters.SearchAdapter;
import com.ziqqi.databinding.ActivitySearchResultBinding;
import com.ziqqi.model.addtocart.AddToCart;
import com.ziqqi.model.addtowishlistmodel.AddToModel;
import com.ziqqi.model.searchcategorymodel.SearchCategory;
import com.ziqqi.model.searchmodel.Payload;
import com.ziqqi.model.searchmodel.SearchResponse;
import com.ziqqi.utils.ConnectivityHelper;
import com.ziqqi.utils.Constants;
import com.ziqqi.utils.LoginDialog;
import com.ziqqi.utils.PreferenceManager;
import com.ziqqi.utils.SpacesItemDecoration;
import com.ziqqi.utils.Utils;
import com.ziqqi.viewmodel.SearchResultViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SearchResultActivity extends AppCompatActivity {

    ActivitySearchResultBinding binding;
    SearchResultViewModel viewModel;
    LinearLayoutManager manager;
    SpacesItemDecoration spacesItemDecoration;
    List<Payload> payloadList = new ArrayList<>();
    List<Payload> searchDataList = new ArrayList<>();
    OnSearchedItemClickListener listener;
    SearchAdapter adapter;
    String catId, title;
    int pageCount = 1;
    LoginDialog loginDialog;
    com.ziqqi.addToCartListener addToCartListener;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search_result);
        viewModel = ViewModelProviders.of(this).get(SearchResultViewModel.class);

        Locale locale = new Locale(PreferenceManager.getStringValue(Constants.LANG));
        Configuration config = getBaseContext().getResources().getConfiguration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        binding.executePendingBindings();
        binding.setViewModel(viewModel);

        if (getIntent().getExtras() != null) {
            catId = getIntent().getStringExtra("category_id");
            title = getIntent().getStringExtra("title");
        }

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button);
        getSupportActionBar().setElevation(0.0f);
        binding.tvToolbarTitle.setText(title);

        setUpAdapter();
        searchQuery();

        listener = new OnSearchedItemClickListener() {
            @Override
            public void onItemClick(String id, String type) {

            }

            @Override
            public void onItemClick(Payload payload, String type) {
                switch (type) {
                    case Constants.SHARE:
                        Utils.share(SearchResultActivity.this, payload.getId());
                        break;
                    case Constants.WISH_LIST:
                        if (PreferenceManager.getBoolValue(Constants.LOGGED_IN)) {
                            addToWishList(PreferenceManager.getStringValue(Constants.AUTH_TOKEN), payload.getId());
                        } else {
                            loginDialog.showDialog(SearchResultActivity.this);
                        }
                        break;
                    case Constants.CART:
                        if (PreferenceManager.getBoolValue(Constants.LOGGED_IN)) {
                            addToCart(payload.getId());
                        } else {
                            loginDialog.showDialog(SearchResultActivity.this);
                        }

                        break;
                }
            }
        };


        binding.recyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                pageCount = 1;
                searchQuery();
            }

            @Override
            public void onLoadMore() {
                pageCount++;
                Log.e("PageCOunt ", " " + pageCount);
                searchQuery();
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
                        Utils.showalertResponse(SearchResultActivity.this, addToModel.getMessage());
                    } else {
                        Utils.showalertResponse(SearchResultActivity.this, addToModel.getMessage());
                    }
                }
            });
        }else{
            Toast.makeText(SearchResultActivity.this,"You're not connected!", Toast.LENGTH_SHORT).show();
        }
    }

    private void addToCart(String id) {
        if (ConnectivityHelper.isConnectedToNetwork(this)){
            binding.progressBar.setVisibility(View.VISIBLE);
            viewModel.addToCart(id, PreferenceManager.getStringValue(Constants.AUTH_TOKEN), "1");
            viewModel.addToCartResponse().observe(this, new Observer<AddToCart>() {
                @Override
                public void onChanged(@Nullable AddToCart addToCart) {
                    if (!addToCart.getError()) {
                        Utils.showalertResponse(SearchResultActivity.this, addToCart.getMessage());
                        addToCartListener.addToCart();
                    } else {
                        Utils.showalertResponse(SearchResultActivity.this, addToCart.getMessage());
                    }
                }
            });
        }else{
            Toast.makeText(SearchResultActivity.this,"You're not connected!", Toast.LENGTH_SHORT).show();
        }
    }


    private void searchQuery() {
        if (ConnectivityHelper.isConnectedToNetwork(this)){
            viewModel.fetchData(catId, String.valueOf(pageCount));
            viewModel.getSearchResponse().observe(this, new Observer<SearchResponse>() {
                @Override
                public void onChanged(@Nullable SearchResponse searchResponse) {
                    if (!searchResponse.getError()) {
                        if (payloadList != null) {
                            //searchDataList.clear();
                            payloadList = searchResponse.getPayload();
                            if (pageCount == 1) {
                                searchDataList.addAll(payloadList);
                                // adapter.setPayloadList(viewAllList);
                            } else {
                                searchDataList.addAll(payloadList);
                                // adapter.setPayloadList(viewAllList);
                                binding.recyclerView.loadMoreComplete();
                            }
                        }
                    } else {
                        Utils.ShowToast(SearchResultActivity.this, searchResponse.getMessage());

                    }
                }
            });
        }else{
            Toast.makeText(SearchResultActivity.this,"You're not connected!", Toast.LENGTH_SHORT).show();
        }

    }

    private void setUpAdapter() {
        manager = new GridLayoutManager(SearchResultActivity.this, 3);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.recyclerView.setLayoutManager(manager);
        adapter = new SearchAdapter(SearchResultActivity.this, searchDataList, listener);
        binding.recyclerView.setAdapter(adapter);
        spacesItemDecoration = new SpacesItemDecoration(SearchResultActivity.this, R.dimen.dp_4);
        binding.recyclerView.addItemDecoration(spacesItemDecoration);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
