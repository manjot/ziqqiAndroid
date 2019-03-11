package com.ziqqi.fragments;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ziqqi.OnItemClickListener;
import com.ziqqi.R;
import com.ziqqi.adapters.BestSellerMainAdapter;
import com.ziqqi.adapters.SubCategoryAdapter;
import com.ziqqi.databinding.ActivityHomeCategoryBinding;
import com.ziqqi.model.homecategorymodel.HomeCategoriesResponse;
import com.ziqqi.model.homecategorymodel.Payload;
import com.ziqqi.utils.SpacesItemDecoration;
import com.ziqqi.utils.Utils;
import com.ziqqi.viewmodel.SubCategoryViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SubCategoryFragment extends Fragment {
    LinearLayoutManager managerBestSeller;
    GridLayoutManager manager;
    ActivityHomeCategoryBinding binding;
    SubCategoryViewModel viewModel;
    String categoryId;
    SubCategoryAdapter adapter;
    SpacesItemDecoration spacesItemDecoration;
    BestSellerMainAdapter bestSellerMainAdapter;

    List<Payload> payloadList = new ArrayList<>();
    List<Payload> subCategoryList = new ArrayList<>();
    List<Payload> bestSellerPayloadList = new ArrayList<>();
    OnItemClickListener listener;

    Toolbar toolbar;
    TextView tvTitle;
    ImageView ivTitle;


    public SubCategoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
                inflater, R.layout.activity_home_category, container, false);
        viewModel = ViewModelProviders.of(this).get(SubCategoryViewModel.class);

        toolbar=  getActivity().findViewById(R.id.toolbar);
        tvTitle=  toolbar.findViewById(R.id.tv_toolbar_title_text);
        ivTitle=  toolbar.findViewById(R.id.tv_toolbar_title);
        ivTitle.setVisibility(View.VISIBLE);
        tvTitle.setVisibility(View.GONE);
        binding.executePendingBindings();
        binding.setViewModel(viewModel);
        View view = binding.getRoot();

        binding.toolbar.setVisibility(View.GONE);

        Bundle bundle = getArguments();

        if (bundle != null) {
            categoryId = bundle.getString("categoryId");
        }

        setUpAdapter();
        setUpListUpdate();

        return view;
    }

    private void setUpAdapter() {
        manager = new GridLayoutManager(getContext(), 3);
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
        adapter = new SubCategoryAdapter(getContext(), subCategoryList, listener);
        binding.rvMainRecyclerView.setAdapter(adapter);

        spacesItemDecoration = new SpacesItemDecoration(getContext(), R.dimen.dp_4);
        binding.rvMainRecyclerView.addItemDecoration(spacesItemDecoration);

        managerBestSeller = new LinearLayoutManager(getContext());
        managerBestSeller.setOrientation(LinearLayoutManager.VERTICAL);
        binding.rvBestSeller.setLayoutManager(managerBestSeller);
        bestSellerMainAdapter = new BestSellerMainAdapter(getContext(), bestSellerPayloadList, listener);
        binding.rvBestSeller.setAdapter(bestSellerMainAdapter);
    }

    public void setUpListUpdate() {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.mainLayout.setVisibility(View.GONE);
        Log.e("CategoryId", categoryId);
        viewModel.fetchData(categoryId);
        viewModel.getSubCategoriesResponse().observe(this, new Observer<HomeCategoriesResponse>() {
            @Override
            public void onChanged(@Nullable HomeCategoriesResponse subCategories) {
                binding.progressBar.setVisibility(View.GONE);
                binding.mainLayout.setVisibility(View.VISIBLE);
                if (!subCategories.getError()) {
                    if (payloadList != null) {
                        bestSellerPayloadList.clear();
                        subCategoryList.clear();
                        payloadList = subCategories.getPayload();
                    }
                    subCategoryList.addAll(payloadList);
                    for (int i = 0; i < payloadList.size(); i++) {
                        if (!payloadList.get(i).getBestsellerProduct().isEmpty()) {
                            bestSellerPayloadList.add(payloadList.get(i));
                        }
                    }
                    adapter.notifyDataSetChanged();
                    bestSellerMainAdapter.notifyDataSetChanged();
                } else {
                    Utils.ShowToast(getContext(), subCategories.getMessage());
                }
            }
        });
    }
}
