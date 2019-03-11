package com.ziqqi.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.ziqqi.OnItemClickListener;
import com.ziqqi.R;
import com.ziqqi.adapters.SearchAdapter;
import com.ziqqi.adapters.SearchCategoryAdapter;
import com.ziqqi.databinding.ActivitySearchResultBinding;
import com.ziqqi.model.searchcategorymodel.SearchCategory;
import com.ziqqi.model.searchmodel.Payload;
import com.ziqqi.model.searchmodel.SearchResponse;
import com.ziqqi.utils.SpacesItemDecoration;
import com.ziqqi.viewmodel.SearchResultViewModel;
import com.ziqqi.viewmodel.SearchViewModel;

import java.util.ArrayList;
import java.util.List;

public class SearchResultActivity extends AppCompatActivity {

    ActivitySearchResultBinding binding;
    SearchResultViewModel viewModel;
    LinearLayoutManager manager;
    SpacesItemDecoration spacesItemDecoration;
    List<Payload> payloadList = new ArrayList<>();
    List<Payload> searchDataList = new ArrayList<>();
    OnItemClickListener listener;
    SearchAdapter adapter;
    String catId;
    int pageCount = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search_result);
        viewModel = ViewModelProviders.of(this).get(SearchResultViewModel.class);
        binding.executePendingBindings();
        binding.setViewModel(viewModel);

        if (getIntent().getExtras() != null){
            catId = getIntent().getStringExtra("category_id");
        }

        setUpAdapter();
        searchQuery(catId, String.valueOf(pageCount));

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

        listener = new OnItemClickListener() {
            @Override
            public void onItemClick(String id) {

            }

            @Override
            public void onItemClick(String id, String type) {

            }
        };
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
}
