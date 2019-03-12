package com.ziqqi.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import com.ziqqi.adapters.SearchCategoryAdapter;
import com.ziqqi.databinding.ActivitySearchBinding;
import com.ziqqi.model.searchcategorymodel.SearchCategory;
import com.ziqqi.model.searchcategorymodel.Payload;
import com.ziqqi.model.searchmodel.SearchResponse;
import com.ziqqi.utils.FontCache;
import com.ziqqi.utils.SpacesItemDecoration;
import com.ziqqi.viewmodel.SearchViewModel;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    ActivitySearchBinding binding;
    SearchViewModel viewModel;
    LinearLayoutManager manager;
    SpacesItemDecoration spacesItemDecoration;
    List<Payload> payloadList = new ArrayList<>();
    List<Payload> searchDataList = new ArrayList<>();
    OnItemClickListener listener;
    SearchCategoryAdapter adapter;
    long delay = 1000; // 1 seconds after user stops typing
    long last_text_edit = 0;
    Handler handler;
    boolean noData = false;
    CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search);
        viewModel = ViewModelProviders.of(this).get(SearchViewModel.class);

        binding.executePendingBindings();
        binding.setViewModel(viewModel);
        handler = new Handler();

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button);
        getSupportActionBar().setElevation(0.0f);

        binding.tvSearch.setTypeface(FontCache.get(getResources().getString(R.string.light), this));


        setUpAdapter();

        binding.tvSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(final CharSequence charSequence, int i, int i1, int i2) {
               /* if (charSequence.toString().length() == 0) {
                    payloadList.clear();
                    adapter.notifyDataSetChanged();
                } else {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            searchQuery(charSequence.toString());
                        }
                    }, 1000);
                }*/

                if (timer != null) {
                    timer.cancel();
                }

                timer = new CountDownTimer(1000, 500) {

                    public void onTick(long millisUntilFinished) {

                    }

                    public void onFinish() {
                        if (charSequence.toString().length() > 0) {
                            searchQuery(charSequence.toString());
                        } else {
                            binding.llNoData.setVisibility(View.GONE);
                            binding.progressBar.setVisibility(View.GONE);
                            binding.recyclerView.setVisibility(View.GONE);
                        }

                    }

                }.start();

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    private void searchQuery(String searchname) {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.recyclerView.setVisibility(View.GONE);
        binding.llNoData.setVisibility(View.GONE);
        viewModel.fetchData(searchname);
        viewModel.getSearchCategoryResponse().observe(this, new Observer<SearchCategory>() {
            @Override
            public void onChanged(@Nullable SearchCategory searchCategory) {
                if (!searchCategory.getError()) {
                    binding.progressBar.setVisibility(View.GONE);
                    binding.recyclerView.setVisibility(View.VISIBLE);
                    binding.llNoData.setVisibility(View.GONE);
                    if (payloadList != null) {
                        searchDataList.clear();
                        payloadList = searchCategory.getPayload();
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
        manager = new LinearLayoutManager(SearchActivity.this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.recyclerView.setLayoutManager(manager);
        adapter = new SearchCategoryAdapter(SearchActivity.this, searchDataList, listener);
        binding.recyclerView.setAdapter(adapter);
        spacesItemDecoration = new SpacesItemDecoration(SearchActivity.this, R.dimen.dp_4);
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
