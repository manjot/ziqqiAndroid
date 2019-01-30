package com.ziqqi.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.ziqqi.OnItemClickListener;
import com.ziqqi.R;
import com.ziqqi.adapters.SearchAdapter;
import com.ziqqi.databinding.ActivitySearchBinding;
import com.ziqqi.model.searchmodel.Payload;
import com.ziqqi.model.searchmodel.SearchResponse;
import com.ziqqi.utils.FontCache;
import com.ziqqi.utils.SpacesItemDecoration;
import com.ziqqi.utils.Utils;
import com.ziqqi.viewmodel.SearchViewModel;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    ActivitySearchBinding binding;
    SearchViewModel viewModel;
    LinearLayoutManager manager;
    SpacesItemDecoration spacesItemDecoration;
    List<Payload> payloadList = new ArrayList<>();
    OnItemClickListener listener;
    SearchAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search);
        viewModel = ViewModelProviders.of(this).get(SearchViewModel.class);

        binding.executePendingBindings();
        binding.setViewModel(viewModel);

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0.0f);

        binding.tvSearch.setTypeface(FontCache.get(getResources().getString(R.string.light), this));


        setUpAdapter();

        binding.tvSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(final CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() == 0) {
                    payloadList.clear();
                    adapter.notifyDataSetChanged();
                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            searchQuery(charSequence.toString());
                        }
                    }, 1000);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        listener = new OnItemClickListener() {
            @Override
            public void onItemClick(String id) {

            }
        };
    }

    private void searchQuery(String query) {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.recyclerView.setVisibility(View.GONE);
        viewModel.fetchData(query);
        viewModel.getSearchResponse().observe(this, new Observer<SearchResponse>() {
            @Override
            public void onChanged(@Nullable SearchResponse searchResponse) {
                if (!searchResponse.getError()) {
                    binding.progressBar.setVisibility(View.GONE);
                    binding.recyclerView.setVisibility(View.VISIBLE);
                    if (payloadList != null) {
                        payloadList.clear();
                        payloadList.addAll(searchResponse.getPayload());
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    Utils.ShowToast(SearchActivity.this, searchResponse.getMessage());
                }
            }
        });
    }

    private void setUpAdapter() {
        manager = new GridLayoutManager(this, 3);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.recyclerView.setLayoutManager(manager);
        adapter = new SearchAdapter(this, payloadList, listener);
        binding.recyclerView.setAdapter(adapter);
        spacesItemDecoration = new SpacesItemDecoration(this, R.dimen.dp_4);
        binding.recyclerView.addItemDecoration(spacesItemDecoration);
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
