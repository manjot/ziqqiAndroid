package com.ziqqi.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ziqqi.OnItemClickListener;
import com.ziqqi.R;
import com.ziqqi.activities.BillingInfoActivity;
import com.ziqqi.adapters.CartAdapter;
import com.ziqqi.adapters.WishlistApdater;
import com.ziqqi.databinding.FragmentCartBinding;
import com.ziqqi.model.viewcartmodel.Payload;
import com.ziqqi.model.viewcartmodel.ViewCartResponse;
import com.ziqqi.model.viewwishlistmodel.ViewWishlist;
import com.ziqqi.utils.Constants;
import com.ziqqi.utils.PreferenceManager;
import com.ziqqi.utils.SpacesItemDecoration;
import com.ziqqi.viewmodel.CartViewModel;
import com.ziqqi.viewmodel.ProfileViewModel;

import java.util.ArrayList;
import java.util.List;

public class CartFragment extends Fragment {

    FragmentCartBinding binding;
    CartViewModel viewModel;
    Toolbar toolbar;
    TextView tvTitle;
    ImageView ivTitle;
    List<Payload> payloadList = new ArrayList<>();
    List<Payload> cartDataList = new ArrayList<>();
    OnItemClickListener listener;
    CartAdapter adapter;
    SpacesItemDecoration spacesItemDecoration;
    LinearLayoutManager manager;

    public CartFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewModel = ViewModelProviders.of(this).get(CartViewModel.class);
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_cart, container, false);

        toolbar=  getActivity().findViewById(R.id.toolbar);
        tvTitle=  toolbar.findViewById(R.id.tv_toolbar_title_text);
        ivTitle=  toolbar.findViewById(R.id.tv_toolbar_title);
        ivTitle.setVisibility(View.GONE);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText("CART");

        binding.executePendingBindings();
        binding.setViewModel(viewModel);
        binding.setViewModel(viewModel);
        View view = binding.getRoot();
        setUpAdapter();
        if (PreferenceManager.getBoolValue(Constants.LOGGED_IN)){
            fetchCart(PreferenceManager.getStringValue(Constants.AUTH_TOKEN));
        }

        return view;
    }

    private void fetchCart(String authToken) {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.rvCart.setVisibility(View.GONE);
        binding.llNoData.setVisibility(View.GONE);
        viewModel.fetchData(authToken);
        viewModel.getCartResponse().observe(this, new Observer<ViewCartResponse>() {
            @Override
            public void onChanged(@Nullable ViewCartResponse viewCart) {
                if (!viewCart.getError()) {
                    binding.progressBar.setVisibility(View.GONE);
                    binding.llNoData.setVisibility(View.GONE);
                    if (payloadList != null) {
                        cartDataList.clear();
                        payloadList = viewCart.getPayload();
                        cartDataList.addAll(payloadList);
                        binding.rvCart.setVisibility(View.VISIBLE);
                        binding.progressBar.setVisibility(View.GONE);
                        binding.btSubmit.setVisibility(View.VISIBLE);
                        binding.llTotal.setVisibility(View.VISIBLE);
                        binding.tvTotalPrice.setText("$ " +viewCart.getTotal());
                        binding.btSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(getContext(), BillingInfoActivity.class));
                            }
                        });
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
        binding.rvCart.setLayoutManager(manager);
        adapter = new CartAdapter(getContext(), cartDataList, listener);
        binding.rvCart.setAdapter(adapter);
//        spacesItemDecoration = new SpacesItemDecoration(getContext(), R.dimen.dp_4);
//        binding.rvCart.addItemDecoration(spacesItemDecoration);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
