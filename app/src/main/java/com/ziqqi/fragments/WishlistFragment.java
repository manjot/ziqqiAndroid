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
import com.ziqqi.adapters.SearchAdapter;
import com.ziqqi.adapters.WishlistApdater;
import com.ziqqi.databinding.FragmentWishlistBinding;
import com.ziqqi.model.searchmodel.SearchResponse;
import com.ziqqi.model.viewwishlistmodel.Payload;
import com.ziqqi.model.viewwishlistmodel.ViewWishlist;
import com.ziqqi.utils.SpacesItemDecoration;
import com.ziqqi.viewmodel.WishlistViewModel;

import java.util.ArrayList;
import java.util.List;

public class WishlistFragment extends Fragment {

    FragmentWishlistBinding binding;
    WishlistViewModel viewModel;
    RecyclerView rvWishlist;
    List<Payload> payloadList = new ArrayList<>();
    List<Payload> wishlistDataList = new ArrayList<>();
    OnItemClickListener listener;
    WishlistApdater adapter;
    SpacesItemDecoration spacesItemDecoration;
    LinearLayoutManager manager;
    Toolbar toolbar;
    TextView tvTitle;
    ImageView ivTitle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_wishlist, container, false);
        viewModel = ViewModelProviders.of(this).get(WishlistViewModel.class);
        toolbar=  getActivity().findViewById(R.id.toolbar);
        tvTitle=  toolbar.findViewById(R.id.tv_toolbar_title_text);
        ivTitle=  toolbar.findViewById(R.id.tv_toolbar_title);
        ivTitle.setVisibility(View.GONE);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText("WISHLIST");
        View view = binding.getRoot();
        rvWishlist = binding.rvWishlist;
        setUpAdapter();
        fetchWishlist("5c2f405346f56");
        return view;
    }

    private void fetchWishlist(String authToken) {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.rvWishlist.setVisibility(View.GONE);
        binding.llNoData.setVisibility(View.GONE);
        viewModel.fetchWishlist(authToken);
        viewModel.getWishlistResponse().observe(this, new Observer<ViewWishlist>() {
            @Override
            public void onChanged(@Nullable ViewWishlist viewWishlist) {
                if (!viewWishlist.getError()) {
                    binding.progressBar.setVisibility(View.GONE);
                    binding.llNoData.setVisibility(View.GONE);
                    if (payloadList != null) {
                        wishlistDataList.clear();
                        payloadList = viewWishlist.getPayload();
                        wishlistDataList.addAll(payloadList);
                        binding.rvWishlist.setVisibility(View.VISIBLE);
                        binding.progressBar.setVisibility(View.GONE);
                        // adapter.notifyDataSetChanged();
                    }
                } else {
                    binding.progressBar.setVisibility(View.GONE);
                    binding.llNoData.setVisibility(View.VISIBLE);

                }
            }
        });
    }

    private void setUpAdapter() {
        manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.rvWishlist.setLayoutManager(manager);
        adapter = new WishlistApdater(getContext(), wishlistDataList, listener);
        binding.rvWishlist.setAdapter(adapter);
        spacesItemDecoration = new SpacesItemDecoration(getContext(), R.dimen.dp_4);
        binding.rvWishlist.addItemDecoration(spacesItemDecoration);
    }
}
