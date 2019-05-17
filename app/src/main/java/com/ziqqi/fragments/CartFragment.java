package com.ziqqi.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ziqqi.OnCartItemlistener;
import com.ziqqi.OnItemClickListener;
import com.ziqqi.R;
import com.ziqqi.activities.BillingInfoActivity;
import com.ziqqi.activities.MainActivity;
import com.ziqqi.activities.ProductDetailActivity;
import com.ziqqi.activities.ProfileActivity;
import com.ziqqi.adapters.CartAdapter;
import com.ziqqi.adapters.WishlistApdater;
import com.ziqqi.addToCartListener;
import com.ziqqi.databinding.FragmentCartBinding;
import com.ziqqi.fetchCartListener;
import com.ziqqi.model.addtocart.AddToCart;
import com.ziqqi.model.changequantitymodel.ChangeQuantityResponse;
import com.ziqqi.model.deletecartmodel.DeleteCartResponse;
import com.ziqqi.model.viewcartmodel.Payload;
import com.ziqqi.model.viewcartmodel.ViewCartResponse;
import com.ziqqi.model.viewwishlistmodel.ViewWishlist;
import com.ziqqi.utils.ConnectivityHelper;
import com.ziqqi.utils.Constants;
import com.ziqqi.utils.LoginDialog;
import com.ziqqi.utils.PreferenceManager;
import com.ziqqi.utils.SpacesItemDecoration;
import com.ziqqi.utils.Utils;
import com.ziqqi.viewmodel.CartViewModel;
import com.ziqqi.viewmodel.ProfileViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.facebook.FacebookSdk.getApplicationContext;

public class CartFragment extends Fragment {

    FragmentCartBinding binding;
    CartViewModel viewModel;
    Toolbar toolbar;
    TextView tvTitle;
    ImageView ivTitle;
    List<Payload> payloadList = new ArrayList<>();
    List<Payload> cartDataList = new ArrayList<>();
    OnCartItemlistener listener;
    CartAdapter adapter;
    SpacesItemDecoration spacesItemDecoration;
    LinearLayoutManager manager;
    fetchCartListener fcListener;
    com.ziqqi.addToCartListener addToCartListener;
    LoginDialog loginDialog;

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

        Locale locale = new Locale(PreferenceManager.getStringValue(Constants.LANG));
        Configuration config = getApplicationContext().getResources().getConfiguration();
        config.locale = locale;
        getApplicationContext().getResources().updateConfiguration(config, getApplicationContext().getResources().getDisplayMetrics());

        toolbar = getActivity().findViewById(R.id.toolbar);
        tvTitle = toolbar.findViewById(R.id.tv_toolbar_title_text);
        ivTitle = toolbar.findViewById(R.id.tv_toolbar_title);
        ivTitle.setVisibility(View.GONE);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(getString(R.string.cart));

        binding.executePendingBindings();
        binding.setViewModel(viewModel);
        binding.setViewModel(viewModel);
        View view = binding.getRoot();
        listener = new OnCartItemlistener() {
            @Override
            public void onCartItemClick(String id, String type) {

            }

            @Override
            public void onCartItemClick(Payload payload, String type) {
                switch (type) {
                    case Constants.REMOVE_CART:
                        if (PreferenceManager.getBoolValue(Constants.LOGGED_IN)){
                            deleteCart(PreferenceManager.getStringValue(Constants.AUTH_TOKEN),PreferenceManager.getStringValue(Constants.GUEST_ID), payload.getId());
                        }else{
                            deleteCart("",PreferenceManager.getStringValue(Constants.GUEST_ID), payload.getId());
                        }

                        break;
                    case Constants.ADD_ITEM:
                        if (PreferenceManager.getBoolValue(Constants.LOGGED_IN)){
                            updateQuantity(PreferenceManager.getStringValue(Constants.AUTH_TOKEN),PreferenceManager.getStringValue(Constants.GUEST_ID), payload.getId(), "1");
                        }else{
                            updateQuantity("",PreferenceManager.getStringValue(Constants.GUEST_ID), payload.getId(), "1");
                        }

                        break;
                    case Constants.MINUS_ITEM:
                        if (PreferenceManager.getBoolValue(Constants.LOGGED_IN)){
                            updateQuantity(PreferenceManager.getStringValue(Constants.AUTH_TOKEN),PreferenceManager.getStringValue(Constants.GUEST_ID), payload.getId(), "0");
                        }else{
                            updateQuantity("",PreferenceManager.getStringValue(Constants.GUEST_ID), payload.getId(), "0");
                        }

                        break;
                }
            }
        };
        setUpAdapter();

        if (PreferenceManager.getBoolValue(Constants.LOGGED_IN)) {
            fetchCart(PreferenceManager.getStringValue(Constants.AUTH_TOKEN) , PreferenceManager.getStringValue(Constants.GUEST_ID));
        }else{
            fetchCart("", PreferenceManager.getStringValue(Constants.GUEST_ID));
        }

        binding.btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PreferenceManager.getBoolValue(Constants.LOGGED_IN)){
                    startActivity(new Intent(getContext(), BillingInfoActivity.class));
                }else {
                    loginDialog = new LoginDialog();
                    loginDialog.showDialog(getActivity());
                }
            }
        });

        return view;
    }

    private void deleteCart(String authToken, String guest_id, String productId) {
        binding.progressBar.setVisibility(View.VISIBLE);
        viewModel.deleteCart(authToken, guest_id, productId);
        viewModel.deleteCartResponse().observe(this, new Observer<DeleteCartResponse>() {
            @Override
            public void onChanged(@Nullable final DeleteCartResponse viewCart) {
                if (!viewCart.getError()) {
                    binding.progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), viewCart.getMessage(), Toast.LENGTH_SHORT).show();
                    addToCartListener.addToCart();
                    if (PreferenceManager.getBoolValue(Constants.LOGGED_IN)){
                        fetchCart(PreferenceManager.getStringValue(Constants.AUTH_TOKEN), PreferenceManager.getStringValue(Constants.GUEST_ID));
                    }else{
                        fetchCart("", PreferenceManager.getStringValue(Constants.GUEST_ID));
                    }

                } else {
                    adapter.notifyDataSetChanged();
                    binding.progressBar.setVisibility(View.GONE);

                }
            }
        });
    }

    private void fetchCart(String authToken, String guest_id) {
        if (ConnectivityHelper.isConnectedToNetwork(getContext())) {
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.rvCart.setVisibility(View.GONE);
            binding.llNoData.setVisibility(View.GONE);
            viewModel.fetchData(authToken, guest_id);
            viewModel.getCartResponse().observe(this, new Observer<ViewCartResponse>() {
                @Override
                public void onChanged(@Nullable final ViewCartResponse viewCart) {
                    if (!viewCart.getError()) {
                        binding.progressBar.setVisibility(View.GONE);
                        binding.llNoData.setVisibility(View.GONE);
                        if (viewCart.getPayload().size() != 0) {
                            cartDataList.clear();
                            payloadList = viewCart.getPayload();
                            cartDataList.addAll(payloadList);
                            binding.rvCart.setVisibility(View.VISIBLE);
                            binding.progressBar.setVisibility(View.GONE);
                            binding.btSubmit.setVisibility(View.VISIBLE);
                            binding.llTotal.setVisibility(View.VISIBLE);
                            binding.tvTotalPrice.setText("$ " + viewCart.getTotal());
                            PreferenceManager.setStringValue(Constants.CART_TOTAL_AMOUNT, viewCart.getTotal() + "");
                            adapter.notifyDataSetChanged();
                        } else {
                            binding.btSubmit.setVisibility(View.GONE);
                            binding.llNoData.setVisibility(View.VISIBLE);
                            binding.llTotal.setVisibility(View.GONE);
                            binding.tvTotalPrice.setVisibility(View.GONE);
//                            adapter.notifyDataSetChanged();
                        }
                    } else {
                        binding.progressBar.setVisibility(View.GONE);
                        binding.llNoData.setVisibility(View.VISIBLE);
                        binding.llTotal.setVisibility(View.GONE);
                        binding.tvTotalPrice.setVisibility(View.GONE);
                        binding.btSubmit.setVisibility(View.GONE);

                    }
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "You're not connected!", Toast.LENGTH_SHORT).show();
        }

    }

    private void setTotal(String authToken, String guest_id) {
        if (ConnectivityHelper.isConnectedToNetwork(getContext())) {
            viewModel.fetchData(authToken, guest_id);
            viewModel.getCartResponse().observe(this, new Observer<ViewCartResponse>() {
                @Override
                public void onChanged(@Nullable final ViewCartResponse viewCart) {
                    if (!viewCart.getError()) {
                        binding.tvTotalPrice.setText("$ " + viewCart.getTotal());
                        PreferenceManager.setStringValue(Constants.CART_TOTAL_AMOUNT, viewCart.getTotal() + "");
                    }
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "You're not connected!", Toast.LENGTH_SHORT).show();
        }
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

    private void addToCart(String id, String authToken, String quantity, String guest_id) {
        binding.progressBar.setVisibility(View.VISIBLE);
        viewModel.addProductToCart(id, authToken, quantity, guest_id);
        viewModel.addCartResponse().observe(this, new Observer<AddToCart>() {
            @Override
            public void onChanged(@Nullable AddToCart addToCart) {
                binding.progressBar.setVisibility(View.GONE);
                if (!addToCart.getError()) {
                    Toast.makeText(getApplicationContext(), addToCart.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), addToCart.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateQuantity(String authToken,String guest_id, String productId, String type) {
        binding.progressBar.setVisibility(View.VISIBLE);
        viewModel.changeCart(authToken, guest_id, productId, type);
        viewModel.changeCartResponse().observe(this, new Observer<ChangeQuantityResponse>() {
            @Override
            public void onChanged(@Nullable ChangeQuantityResponse changeCart) {
                binding.progressBar.setVisibility(View.GONE);
                if (!changeCart.getError()) {
                    Toast.makeText(getApplicationContext(), changeCart.getMessage(), Toast.LENGTH_SHORT).show();
                    if (PreferenceManager.getBoolValue(Constants.LOGGED_IN)){
                        setTotal(PreferenceManager.getStringValue(Constants.AUTH_TOKEN), PreferenceManager.getStringValue(Constants.GUEST_ID));
                    }else{
                        setTotal("", PreferenceManager.getStringValue(Constants.GUEST_ID));
                    }

                } else {
                    Toast.makeText(getApplicationContext(), changeCart.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        fcListener = (com.ziqqi.fetchCartListener) context;
        addToCartListener = (com.ziqqi.addToCartListener) context;
    }
}
