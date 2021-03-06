package com.ziqqi.fragments;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ziqqi.OnItemClickListener;
import com.ziqqi.R;
import com.ziqqi.adapters.SearchAdapter;
import com.ziqqi.databinding.ActivitySearchBinding;
import com.ziqqi.model.searchmodel.Payload;
import com.ziqqi.model.searchmodel.SearchResponse;
import com.ziqqi.utils.FontCache;
import com.ziqqi.utils.SpacesItemDecoration;
import com.ziqqi.viewmodel.SearchViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {
    ActivitySearchBinding binding;
    SearchViewModel viewModel;
    LinearLayoutManager manager;
    SpacesItemDecoration spacesItemDecoration;
    List<Payload> payloadList = new ArrayList<>();
    List<Payload> searchDataList = new ArrayList<>();
    OnItemClickListener listener;
    SearchAdapter adapter;
    long delay = 1000; // 1 seconds after user stops typing
    long last_text_edit = 0;
    Handler handler;
    boolean noData = false;
    CountDownTimer timer;

    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
                inflater, R.layout.activity_search, container, false);
        binding.executePendingBindings();
        binding.setViewModel(viewModel);
        binding.setViewModel(viewModel);
        View view = binding.getRoot();
        binding.toolbar.setVisibility(View.GONE);

        binding.tvSearch.setTypeface(FontCache.get(getResources().getString(R.string.light), getContext()));
        viewModel = ViewModelProviders.of(this).get(SearchViewModel.class);


        setUpAdapter();

        binding.tvSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(final CharSequence charSequence, int i, int i1, int i2) {
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
                            binding.ivNdf.setVisibility(View.GONE);
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

        listener = new OnItemClickListener() {
            @Override
            public void onItemClick(String id) {

            }

            @Override
            public void onItemClick(String id, String type) {

            }
        };
        return view;
    }

    private void searchQuery(String query) {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.recyclerView.setVisibility(View.GONE);
        binding.ivNdf.setVisibility(View.GONE);
        viewModel.fetchData(query);
        viewModel.getSearchResponse().observe(this, new Observer<SearchResponse>() {
            @Override
            public void onChanged(@Nullable SearchResponse searchResponse) {
                if (!searchResponse.getError()) {
                    binding.progressBar.setVisibility(View.GONE);
                    binding.recyclerView.setVisibility(View.VISIBLE);
                    binding.ivNdf.setVisibility(View.GONE);
                    if (payloadList != null) {
                        searchDataList.clear();
                        payloadList = searchResponse.getPayload();
                        searchDataList.addAll(payloadList);
                    }
                } else {
                    binding.progressBar.setVisibility(View.GONE);
                    binding.recyclerView.setVisibility(View.GONE);
                    binding.ivNdf.setVisibility(View.VISIBLE);

                }
            }
        });
    }

    private void setUpAdapter() {
        manager = new GridLayoutManager(getContext(), 3);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.recyclerView.setLayoutManager(manager);
        adapter = new SearchAdapter(getContext(), searchDataList, listener);
        binding.recyclerView.setAdapter(adapter);
        spacesItemDecoration = new SpacesItemDecoration(getContext(), R.dimen.dp_4);
        binding.recyclerView.addItemDecoration(spacesItemDecoration);
    }

}
