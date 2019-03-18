package com.ziqqi.fragments;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.ziqqi.OnDealsItemClickListener;
import com.ziqqi.OnItemClickListener;
import com.ziqqi.R;
import com.ziqqi.activities.ViewAllDealsActivity;
import com.ziqqi.adapters.BestSellerAdapter;
import com.ziqqi.adapters.DealsAdapter;
import com.ziqqi.adapters.SearchAdapter;
import com.ziqqi.addToCartListener;
import com.ziqqi.databinding.FragmentDealsBinding;
import com.ziqqi.model.addtocart.AddToCart;
import com.ziqqi.model.addtowishlistmodel.AddToModel;
import com.ziqqi.model.dealsmodel.DealsResponse;
import com.ziqqi.model.dealsmodel.Payload;
import com.ziqqi.model.searchmodel.SearchResponse;
import com.ziqqi.utils.Constants;
import com.ziqqi.utils.LoginDialog;
import com.ziqqi.utils.PreferenceManager;
import com.ziqqi.utils.SpacesItemDecoration;
import com.ziqqi.utils.Utils;
import com.ziqqi.viewmodel.DealsViewModel;

import java.util.ArrayList;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class DealsFragment extends Fragment implements View.OnClickListener {
    RecyclerView rvDeals;
    LinearLayoutManager manager;
    FragmentDealsBinding binding;
    DealsViewModel viewModel;
    List<Payload> payloadList = new ArrayList<>();
    List<Payload> searchDataList = new ArrayList<>();
    OnDealsItemClickListener listener;
    DealsAdapter adapter;
    SpacesItemDecoration spacesItemDecoration;
    Toolbar toolbar;
    TextView tvTitle;
    ImageView ivTitle;
    int pageCount = 1;
    LoginDialog loginDialog;
    addToCartListener addToCartListener;

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
        binding.tvViewAllDeals.setOnClickListener(this);
        loginDialog = new LoginDialog();

        binding.executePendingBindings();
        binding.setViewModel(viewModel);
        binding.setViewModel(viewModel);
        View view = binding.getRoot();
        rvDeals = binding.rvDeals;
        getDeals();

        listener = new OnDealsItemClickListener() {
            @Override
            public void onItemClick(String id, String type) {

            }

            @Override
            public void onItemClick(Payload payload, String type) {
                switch (type) {
                    case Constants.SHARE:
                        Utils.share(getActivity(), payload.getId());
                        break;
                    case Constants.WISH_LIST:
                        if (PreferenceManager.getBoolValue(Constants.LOGGED_IN)){
                            addToWishList(PreferenceManager.getStringValue(Constants.AUTH_TOKEN), payload.getId());
                        }else{
                            loginDialog.showDialog(getActivity());
                        }
                        break;
                    case Constants.CART:
                        if (PreferenceManager.getBoolValue(Constants.LOGGED_IN)){
                            viewModel.addToCart(payload.getId(), PreferenceManager.getStringValue(Constants.AUTH_TOKEN), "1");
                            viewModel.addToCartResponse().observe(getViewLifecycleOwner(), new Observer<AddToCart>() {
                                @Override
                                public void onChanged(@Nullable AddToCart addToCart) {
                                    Toast.makeText(getApplicationContext(),  addToCart.getMessage(), Toast.LENGTH_SHORT).show();
                                    addToCartListener.addToCart();
                                }
                            });
                        }else{
                            loginDialog.showDialog(getActivity());
                        }

                        break;
                }
            }
        };

        setUpAdapter();

        return view;
    }

    private void getDeals() {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.rvDeals.setVisibility(View.GONE);
        binding.ivNdf.setVisibility(View.GONE);
        viewModel.fetchData(1);
        viewModel.getDealsResponse().observe(this, new Observer<DealsResponse>() {
            @Override
            public void onChanged(@Nullable DealsResponse searchResponse) {
                if (!searchResponse.getError()) {
                    binding.progressBar.setVisibility(View.GONE);
                    binding.ivNdf.setVisibility(View.GONE);
                    if (payloadList != null) {
                        searchDataList.clear();
                        payloadList = searchResponse.getPayload();
                        searchDataList.addAll(payloadList);
                        binding.rvDeals.setVisibility(View.VISIBLE);
                        binding.progressBar.setVisibility(View.GONE);
                        // adapter.notifyDataSetChanged();
                    }
                } else {
                    binding.progressBar.setVisibility(View.GONE);
                    binding.ivNdf.setVisibility(View.VISIBLE);

                }
            }
        });
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


    private void setUpAdapter() {
        manager = new GridLayoutManager(getContext(), 3);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.rvDeals.setLayoutManager(manager);
        adapter = new DealsAdapter(getContext(), searchDataList, listener);
        binding.rvDeals.setAdapter(adapter);
        spacesItemDecoration = new SpacesItemDecoration(getContext(), R.dimen.dp_4);
        binding.rvDeals.addItemDecoration(spacesItemDecoration);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_view_all_deals:
                Intent intent = new Intent(getContext(), ViewAllDealsActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        addToCartListener = (com.ziqqi.addToCartListener) context;
    }
}
