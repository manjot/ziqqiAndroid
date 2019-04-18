package com.ziqqi.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ziqqi.OnWishlistItemClick;
import com.ziqqi.R;
import com.ziqqi.adapters.WishlistApdater;
import com.ziqqi.databinding.FragmentWishlistBinding;
import com.ziqqi.model.addtocart.AddToCart;
import com.ziqqi.model.searchcategorymodel.SearchCategory;
import com.ziqqi.model.viewwishlistmodel.Payload;
import com.ziqqi.model.viewwishlistmodel.ViewWishlist;
import com.ziqqi.utils.ConnectivityHelper;
import com.ziqqi.utils.Constants;
import com.ziqqi.utils.LoginDialog;
import com.ziqqi.utils.PreferenceManager;
import com.ziqqi.utils.SpacesItemDecoration;
import com.ziqqi.utils.Utils;
import com.ziqqi.viewmodel.WishlistViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.facebook.FacebookSdk.getApplicationContext;

public class WishlistFragment extends Fragment {

    FragmentWishlistBinding binding;
    WishlistViewModel viewModel;
    RecyclerView rvWishlist;
    List<Payload> payloadList = new ArrayList<>();
    List<Payload> wishlistDataList = new ArrayList<>();
    OnWishlistItemClick listener;
    WishlistApdater adapter;
    SpacesItemDecoration spacesItemDecoration;
    LinearLayoutManager manager;
    Toolbar toolbar;
    TextView tvTitle;
    ImageView ivTitle;
    com.ziqqi.addToCartListener addToCartListener;
    LoginDialog loginDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_wishlist, container, false);
        viewModel = ViewModelProviders.of(this).get(WishlistViewModel.class);

        Locale locale = new Locale(PreferenceManager.getStringValue(Constants.LANG));
        Configuration config = getApplicationContext().getResources().getConfiguration();
        config.locale = locale;
        getApplicationContext().getResources().updateConfiguration(config, getApplicationContext().getResources().getDisplayMetrics());

        toolbar=  getActivity().findViewById(R.id.toolbar);
        tvTitle=  toolbar.findViewById(R.id.tv_toolbar_title_text);
        ivTitle=  toolbar.findViewById(R.id.tv_toolbar_title);
        ivTitle.setVisibility(View.GONE);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(getString(R.string.wishlist));
        View view = binding.getRoot();
        rvWishlist = binding.rvWishlist;
        loginDialog = new LoginDialog();

        setUpAdapter();
        if (PreferenceManager.getBoolValue(Constants.LOGGED_IN)){
            fetchWishlist(PreferenceManager.getStringValue(Constants.AUTH_TOKEN));
        }

        listener = new OnWishlistItemClick() {
            @Override
            public void onItemClick(String id, String type) {

            }

            @Override
            public void onItemClick(Payload payload, String type) {
                switch (type){
                    case Constants.CART:
                        if (PreferenceManager.getBoolValue(Constants.LOGGED_IN)) {
                            viewModel.addToCart(payload.getId(), PreferenceManager.getStringValue(Constants.AUTH_TOKEN), "1");
                            viewModel.addToCartResponse().observe(getViewLifecycleOwner(), new Observer<AddToCart>() {
                                @Override
                                public void onChanged(@Nullable AddToCart addToCart) {
                                    if (!addToCart.getError()) {
                                        addToCartListener.addToCart();
                                        Utils.showalertResponse(getActivity(), addToCart.getMessage());
                                    } else {
                                        Utils.showalertResponse(getActivity(), addToCart.getMessage());
                                    }
                                }
                            });
                        } else {
                            loginDialog.showDialog(getActivity());
                        }

                        break;
                }
            }
        };


        return view;
    }

    private void fetchWishlist(String authToken) {
        if (ConnectivityHelper.isConnectedToNetwork(getContext())){
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
        }else{
            Toast.makeText(getApplicationContext(),"You're not connected!", Toast.LENGTH_SHORT).show();
        }

    }

    private void setUpAdapter() {
        manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.rvWishlist.setLayoutManager(manager);
        adapter = new WishlistApdater(getContext(), wishlistDataList, listener);
        binding.rvWishlist.setAdapter(adapter);
//        spacesItemDecoration = new SpacesItemDecoration(getContext(), R.dimen.dp_4);
//        binding.rvWishlist.addItemDecoration(spacesItemDecoration);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        addToCartListener = (com.ziqqi.addToCartListener) context;
    }
}
