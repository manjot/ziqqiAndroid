package com.ziqqi.fragments;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ziqqi.OnItemClickListener;
import com.ziqqi.R;
import com.ziqqi.adapters.BestSellerAdapter;
import com.ziqqi.adapters.SearchAdapter;
import com.ziqqi.databinding.FragmentDealsBinding;
import com.ziqqi.model.searchmodel.Payload;
import com.ziqqi.model.searchmodel.SearchResponse;
import com.ziqqi.utils.SpacesItemDecoration;
import com.ziqqi.viewmodel.DealsViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class DealsFragment extends Fragment {
    RecyclerView rvDeals;
    LinearLayoutManager manager;
    FragmentDealsBinding binding;
    DealsViewModel viewModel;
    List<Payload> payloadList = new ArrayList<>();
    List<Payload> searchDataList = new ArrayList<>();
    OnItemClickListener listener;
    SearchAdapter adapter;
    SpacesItemDecoration spacesItemDecoration;
    Toolbar toolbar;
    TextView tvTitle;
    ImageView ivTitle;

    public DealsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_deals, container, false);
        viewModel = ViewModelProviders.of(this).get(DealsViewModel.class);

        toolbar=  getActivity().findViewById(R.id.toolbar);
        tvTitle=  toolbar.findViewById(R.id.tv_toolbar_title_text);
        ivTitle=  toolbar.findViewById(R.id.tv_toolbar_title);
        ivTitle.setVisibility(View.VISIBLE);
        tvTitle.setVisibility(View.GONE);

        binding.executePendingBindings();
        binding.setViewModel(viewModel);
        binding.setViewModel(viewModel);
        View view = binding.getRoot();
        rvDeals = binding.rvDeals;
//        setUpAdapter();
//        searchQuery();
        return view;
    }

//    private void searchQuery() {
//        binding.progressBar.setVisibility(View.VISIBLE);
//        binding.rvDeals.setVisibility(View.GONE);
//        binding.ivNdf.setVisibility(View.GONE);
//        viewModel.fetchData("flip");
//        viewModel.getSearchResponse().observe(this, new Observer<SearchResponse>() {
//            @Override
//            public void onChanged(@Nullable SearchResponse searchResponse) {
//                if (!searchResponse.getError()) {
//                    binding.progressBar.setVisibility(View.GONE);
//                    binding.ivNdf.setVisibility(View.GONE);
//                    if (payloadList != null) {
//                        searchDataList.clear();
//                        payloadList = searchResponse.getPayload();
//                        searchDataList.addAll(payloadList);
//                        binding.rvDeals.setVisibility(View.VISIBLE);
//                        binding.progressBar.setVisibility(View.GONE);
//                        // adapter.notifyDataSetChanged();
//                    }
//                } else {
//                    binding.progressBar.setVisibility(View.GONE);
//                    binding.ivNdf.setVisibility(View.VISIBLE);
//
//                }
//            }
//        });
//    }
//
//    private void setUpAdapter() {
//        manager = new GridLayoutManager(getContext(), 3);
//        manager.setOrientation(LinearLayoutManager.VERTICAL);
//        binding.rvDeals.setLayoutManager(manager);
//        adapter = new SearchAdapter(getContext(), searchDataList, listener);
//        binding.rvDeals.setAdapter(adapter);
//        spacesItemDecoration = new SpacesItemDecoration(getContext(), R.dimen.dp_4);
//        binding.rvDeals.addItemDecoration(spacesItemDecoration);
//    }

}
