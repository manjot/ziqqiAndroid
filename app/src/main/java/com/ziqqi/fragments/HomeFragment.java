package com.ziqqi.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.tbuonomo.viewpagerdotsindicator.SpringDotsIndicator;
import com.ziqqi.OnItemClickListener;
import com.ziqqi.R;
import com.ziqqi.activities.MainActivity;
import com.ziqqi.activities.SearchActivity;
import com.ziqqi.activities.SubCategoryActivity;
import com.ziqqi.activities.ViewAllProductsActivity;
import com.ziqqi.adapters.BestSellerMainAdapter;
import com.ziqqi.adapters.ImageSliderAdapter;
import com.ziqqi.adapters.TopCategoriesAdapter;
import com.ziqqi.addToCartListener;
import com.ziqqi.databinding.FragmentHomeBinding;
import com.ziqqi.model.addtocart.AddToCart;
import com.ziqqi.model.addtowishlistmodel.AddToModel;
import com.ziqqi.model.bannerimagemodel.BannerImageModel;
import com.ziqqi.model.bannerimagemodel.Payload;
import com.ziqqi.model.homecategorymodel.BestsellerProduct;
import com.ziqqi.model.homecategorymodel.HomeCategoriesResponse;
import com.ziqqi.utils.Constants;
import com.ziqqi.utils.FontCache;
import com.ziqqi.utils.LoginDialog;
import com.ziqqi.utils.PreferenceManager;
import com.ziqqi.utils.SpacesItemDecoration;
import com.ziqqi.utils.Utils;
import com.ziqqi.viewmodel.HomeViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.facebook.FacebookSdk.getApplicationContext;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    RecyclerView rvTopCategoriesGridOne, rvTopCategoriesGridTwo, rvBestSellers;
    LinearLayoutManager topCategoriesGridOneManager, topCategoriesGridTwoManager, managerBestSeller;
    TopCategoriesAdapter topCategoriesGridAdapterOne, topCategoriesGridAdapterTwo;
    int currentPage = 0;
    List<Payload> bannerImageList = new ArrayList<>();
    List<Payload> bannerPayLoad = new ArrayList<>();
    List<com.ziqqi.model.homecategorymodel.Payload> homeCategoryListGrid1 = new ArrayList<>();
    List<com.ziqqi.model.homecategorymodel.Payload> homeCategoryListGrid2 = new ArrayList<>();
    List<com.ziqqi.model.homecategorymodel.Payload> payLoadData = new ArrayList<>();
    List<com.ziqqi.model.homecategorymodel.Payload> bestSellerPayloadList = new ArrayList<>();

    HomeViewModel viewModel;
    SpringDotsIndicator indicator;
    FragmentHomeBinding binding;
    ViewPager viewPager;
    ImageSliderAdapter imageSliderAdapter;
    BestSellerMainAdapter bestSellerMainAdapter;
    OnItemClickListener listener;
    boolean a = false;

    Handler handler;
    Runnable update;
    Toolbar toolbar;
    TextView tvTitle;
    ImageView ivTitle;

    addToCartListener addToCartListener;
    LoginDialog loginDialog;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);

        toolbar = getActivity().findViewById(R.id.toolbar);
        tvTitle = toolbar.findViewById(R.id.tv_toolbar_title_text);
        ivTitle = toolbar.findViewById(R.id.tv_toolbar_title);
        ivTitle.setVisibility(View.VISIBLE);
        tvTitle.setVisibility(View.GONE);
        binding.executePendingBindings();
        binding.setViewModel(viewModel);
        binding.setViewModel(viewModel);
        View view = binding.getRoot();
        loginDialog = new LoginDialog();

        rvTopCategoriesGridOne = binding.rvTopCategoriesGrid1;
        rvTopCategoriesGridTwo = binding.rvTopCategoriesGrid2;
        rvBestSellers = binding.rvBestSellers;

        viewPager = binding.viewPager;
        indicator = binding.circleIndicator;

        binding.tvSearch.setTypeface(FontCache.get(getResources().getString(R.string.light), getContext()));
        binding.tvTopCategories.setTypeface(FontCache.get(getResources().getString(R.string.bold), getContext()));
        binding.tvCopyRight.setTypeface(FontCache.get(getResources().getString(R.string.regular), getContext()));

        imageSliderAdapter = new ImageSliderAdapter(getContext(), bannerImageList);
        viewPager.setAdapter(imageSliderAdapter);
        indicator.setViewPager(viewPager);

        binding.tvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), SearchActivity.class));
            }
        });

        listener = new OnItemClickListener() {
            @Override
            public void onItemClick(String id, String type) {
                if (type.equals(Constants.VIEW_ALL)) {
                    startActivity(new Intent(getContext(), ViewAllProductsActivity.class).putExtra("categoryId", id));
                } else {
                    startActivity(new Intent(getContext(), SubCategoryActivity.class).putExtra("categoryId", id));
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
                            addToWishList(Constants.AUTH_TOKEN, bestsellerProduct.getId());
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

        topCategoriesGridOneManager = new GridLayoutManager(getActivity(), 2);
        topCategoriesGridOneManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvTopCategoriesGridOne.setLayoutManager(topCategoriesGridOneManager);
        topCategoriesGridAdapterOne = new TopCategoriesAdapter(getActivity(), homeCategoryListGrid1, listener);
        topCategoriesGridAdapterTwo = new TopCategoriesAdapter(getActivity(), homeCategoryListGrid2, listener);

        rvTopCategoriesGridOne.setAdapter(topCategoriesGridAdapterOne);

        topCategoriesGridTwoManager = new GridLayoutManager(getActivity(), 2);
        topCategoriesGridTwoManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvTopCategoriesGridTwo.setLayoutManager(topCategoriesGridTwoManager);
        rvTopCategoriesGridTwo.setAdapter(topCategoriesGridAdapterTwo);

        SpacesItemDecoration itemDecoration = new SpacesItemDecoration(getActivity(), R.dimen.dp_8, HomeFragment.this);
        rvTopCategoriesGridOne.addItemDecoration(itemDecoration);
        rvTopCategoriesGridTwo.addItemDecoration(itemDecoration);

        handler = new Handler();
        update = new Runnable() {
            public void run() {
                if (currentPage == bannerImageList.size()) {
                    currentPage = 0;
                }
                viewPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(update);
            }
        }, 2500, 2500);

        binding.cvLastGrid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), SubCategoryActivity.class)
                        .putExtra("categoryId", payLoadData.get(8).getId()));
            }
        });
        binding.mainLayout.setVisibility(View.GONE);
        binding.progressBar.setVisibility(View.VISIBLE);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                currentPage = i;
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        managerBestSeller = new LinearLayoutManager(getContext());
        managerBestSeller.setOrientation(LinearLayoutManager.VERTICAL);
        binding.rvBestSellers.setLayoutManager(managerBestSeller);
        bestSellerMainAdapter = new BestSellerMainAdapter(getActivity(), bestSellerPayloadList, listener);
        binding.rvBestSellers.setAdapter(bestSellerMainAdapter);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(HomeViewModel.class);

        viewModel.init();
        viewModel.getHomeBanners().observe(getViewLifecycleOwner(), new Observer<BannerImageModel>() {
            @Override
            public void onChanged(@Nullable BannerImageModel bannerImageModel) {
                if (!bannerImageModel.getError()) {
                    if (bannerPayLoad != null) {
                        bannerPayLoad.clear();
                        bannerPayLoad = bannerImageModel.getPayload();
                        bannerImageList.addAll(bannerPayLoad);
                    }
                    imageSliderAdapter.notifyDataSetChanged();
                } else {
                    Utils.ShowToast(getContext(), bannerImageModel.getMessage());
                }
            }
        });

        viewModel.initHomeCategories();
        viewModel.getHomeCategoryResponse().observe(getViewLifecycleOwner(), new Observer<HomeCategoriesResponse>() {
            @Override
            public void onChanged(@Nullable HomeCategoriesResponse homeCategoriesResponse) {
                binding.progressBar.setVisibility(View.GONE);
                binding.mainLayout.setVisibility(View.VISIBLE);
                if (!homeCategoriesResponse.getError()) {
                    if (payLoadData != null) {
                        homeCategoryListGrid1.clear();
                        homeCategoryListGrid2.clear();
                        bestSellerPayloadList.clear();
                        payLoadData = homeCategoriesResponse.getPayload();
                    }
                    for (int i = 0; i < payLoadData.size(); i++) {
                        if (i < 4) {
                            homeCategoryListGrid1.add(payLoadData.get(i));
                        }
                        if (i >= 4 && i < 8) {
                            homeCategoryListGrid2.add(payLoadData.get(i));
                        } else {
                            binding.tvTitle.setText(payLoadData.get(i).getName());
                            Glide.with(getActivity()).load(payLoadData.get(i).getCategoryImage()).apply(RequestOptions.placeholderOf(R.drawable.place_holder)).into(binding.ivImage);
                            binding.tvTitle.setTypeface(FontCache.get(getResources().getString(R.string.medium), getContext()));
                        }
                        if (!payLoadData.get(i).getBestsellerProduct().isEmpty()) {
                            bestSellerPayloadList.add(payLoadData.get(i));
                        }
                    }

                    topCategoriesGridAdapterOne.notifyDataSetChanged();
                    topCategoriesGridAdapterTwo.notifyDataSetChanged();
                    bestSellerMainAdapter.notifyDataSetChanged();
                } else {
                    Utils.ShowToast(getContext(), homeCategoriesResponse.getMessage());
                }
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        handler.removeCallbacks(update);
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        addToCartListener = (com.ziqqi.addToCartListener) context;
    }
}
