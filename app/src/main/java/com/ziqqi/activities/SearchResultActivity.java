package com.ziqqi.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.ziqqi.OnDealsItemClickListener;
import com.ziqqi.OnItemClickListener;
import com.ziqqi.OnSearchedItemClickListener;
import com.ziqqi.R;
import com.ziqqi.adapters.SearchAdapter;
import com.ziqqi.adapters.SearchCategoryAdapter;
import com.ziqqi.addToCartListener;
import com.ziqqi.databinding.ActivitySearchResultBinding;
import com.ziqqi.model.addtocart.AddToCart;
import com.ziqqi.model.addtowishlistmodel.AddToModel;
import com.ziqqi.model.searchcategorymodel.SearchCategory;
import com.ziqqi.model.searchmodel.Payload;
import com.ziqqi.model.searchmodel.SearchResponse;
import com.ziqqi.utils.Constants;
import com.ziqqi.utils.LoginDialog;
import com.ziqqi.utils.PreferenceManager;
import com.ziqqi.utils.SpacesItemDecoration;
import com.ziqqi.utils.Utils;
import com.ziqqi.viewmodel.SearchResultViewModel;
import com.ziqqi.viewmodel.SearchViewModel;

import java.util.ArrayList;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

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
        binding.executePendingBindings();
        binding.setViewModel(viewModel);

        if (getIntent().getExtras() != null){
            catId = getIntent().getStringExtra("category_id");
            title = getIntent().getStringExtra("title");
        }

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button);
        getSupportActionBar().setElevation(0.0f);
        binding.tvToolbarTitle.setText(title);

        searchQuery(catId, String.valueOf(pageCount));

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
                        if (PreferenceManager.getBoolValue(Constants.LOGGED_IN)){
                            addToWishList(PreferenceManager.getStringValue(Constants.AUTH_TOKEN), payload.getId());
                        }else{
                            loginDialog.showDialog(SearchResultActivity.this);
                        }
                        break;
                    case Constants.CART:
                        if (PreferenceManager.getBoolValue(Constants.LOGGED_IN)){
                            addToCart(payload.getId());
                        }else{
                            loginDialog.showDialog(SearchResultActivity.this);
                        }

                        break;
                }
            }
        };

        setUpAdapter();


        binding.recyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                pageCount = 1;
                searchQuery(catId, String.valueOf(pageCount));
            }

            @Override
            public void onLoadMore() {
                pageCount++;
                Log.e("PageCOunt ", " " + pageCount);
                searchQuery(catId, String.valueOf(pageCount));
            }
        });
        binding.recyclerView.setPullRefreshEnabled(false);

    }

    private void addToWishList(String authToken, String id) {
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
    }

    private void addToCart(String id) {
        binding.progressBar.setVisibility(View.VISIBLE);
        viewModel.addToCart(id, PreferenceManager.getStringValue(Constants.AUTH_TOKEN), "1");
        viewModel.addToCartResponse().observe(this, new Observer<AddToCart>() {
            @Override
            public void onChanged(@Nullable AddToCart addToCart) {
                Toast.makeText(getApplicationContext(),  addToCart.getMessage(), Toast.LENGTH_SHORT).show();
                addToCartListener.addToCart();
            }
        });
    }


    private void searchQuery(String categoryId, String page) {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.recyclerView.setVisibility(View.GONE);
        binding.llNoData.setVisibility(View.GONE);
        viewModel.fetchData(categoryId, page);
        viewModel.getSearchResponse().observe(this, new Observer<SearchResponse>() {
            @Override
            public void onChanged(@Nullable SearchResponse searchResponse) {
                if (!searchResponse.getError()) {
                    binding.progressBar.setVisibility(View.GONE);
                    binding.recyclerView.setVisibility(View.VISIBLE);
                    binding.llNoData.setVisibility(View.GONE);
                    if (payloadList != null) {
                        searchDataList.clear();
                        payloadList = searchResponse.getPayload();
                        searchDataList.addAll(payloadList);
                    }
                } else {
                    binding.progressBar.setVisibility(View.GONE);
                    binding.recyclerView.setVisibility(View.GONE);
                    binding.llNoData.setVisibility(View.VISIBLE);

                }
            }
        });
    }

    private void setUpAdapter() {
        manager = new GridLayoutManager(SearchResultActivity.this,3);
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
