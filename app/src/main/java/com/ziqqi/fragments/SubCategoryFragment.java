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
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ziqqi.OnItemClickListener;
import com.ziqqi.R;
import com.ziqqi.activities.SubCategoryActivity;
import com.ziqqi.activities.ViewAllProductsActivity;
import com.ziqqi.adapters.BestSellerMainAdapter;
import com.ziqqi.adapters.SubCategoryAdapter;
import com.ziqqi.addToCartListener;
import com.ziqqi.databinding.ActivityHomeCategoryBinding;
import com.ziqqi.model.addtocart.AddToCart;
import com.ziqqi.model.addtowishlistmodel.AddToModel;
import com.ziqqi.model.homecategorymodel.BestsellerProduct;
import com.ziqqi.model.homecategorymodel.HomeCategoriesResponse;
import com.ziqqi.model.homecategorymodel.Payload;
import com.ziqqi.utils.Constants;
import com.ziqqi.utils.LoginDialog;
import com.ziqqi.utils.PreferenceManager;
import com.ziqqi.utils.SpacesItemDecoration;
import com.ziqqi.utils.Utils;
import com.ziqqi.viewmodel.SubCategoryViewModel;

import java.util.ArrayList;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

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
    LoginDialog loginDialog;
    com.ziqqi.addToCartListener addToCartListener;


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
        loginDialog = new LoginDialog();

        binding.toolbar.setVisibility(View.GONE);

        Bundle bundle = getArguments();

        if (bundle != null) {
            categoryId = bundle.getString("categoryId");
        }


        listener = new OnItemClickListener() {
            @Override
            public void onItemClick(String id, String type) {
                if (type.equals(Constants.VIEW_ALL)) {
                    startActivity(new Intent(getContext(), ViewAllProductsActivity.class).putExtra("categoryId", id));
                }
            }

            @Override
            public void onItemClick(BestsellerProduct bestsellerProduct, String type) {
                switch (type) {
                    case Constants.SHARE:
                        Utils.share(getActivity(), bestsellerProduct.getId());
                        break;
                    case Constants.WISH_LIST:
                        if (PreferenceManager.getBoolValue(Constants.LOGGED_IN)){
                            addToWishList(PreferenceManager.getStringValue(Constants.AUTH_TOKEN), bestsellerProduct.getId());
                        }else{
                            loginDialog.showDialog(getActivity());
                        }
                        break;
                    case Constants.CART:
                        if (PreferenceManager.getBoolValue(Constants.LOGGED_IN)){
                            viewModel.addToCart(bestsellerProduct.getId(), PreferenceManager.getStringValue(Constants.AUTH_TOKEN), "1");
                            viewModel.addToCartResponse().observe(getViewLifecycleOwner(), new Observer<AddToCart>() {
                                @Override
                                public void onChanged(@Nullable AddToCart addToCart) {
                                    if (!addToCart.getError()){
                                        Toast.makeText(getApplicationContext(),  addToCart.getMessage(), Toast.LENGTH_SHORT).show();
                                        addToCartListener.addToCart();
                                    }

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
        setUpListUpdate();

        return view;
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
                        Glide.with(getApplicationContext()).load(subCategories.getCategory_banner()).apply(RequestOptions.placeholderOf(R.drawable.place_holder)).into(binding.ivBannerImage);
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        addToCartListener = (com.ziqqi.addToCartListener) context;
    }
}
