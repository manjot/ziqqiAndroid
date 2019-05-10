package com.ziqqi.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.ziqqi.OnAllItemClickListener;
import com.ziqqi.OnItemClickListener;
import com.ziqqi.OnSearchedItemClickListener;
import com.ziqqi.R;
import com.ziqqi.adapters.ViewAllProductAdapter;
import com.ziqqi.databinding.ActivityViewAllProductsBinding;
import com.ziqqi.fragments.SortSheetFragment;
import com.ziqqi.model.addtocart.AddToCart;
import com.ziqqi.model.addtowishlistmodel.AddToModel;
import com.ziqqi.model.productcategorymodel.Payload;
import com.ziqqi.model.productcategorymodel.ProductCategory;
import com.ziqqi.utils.ConnectivityHelper;
import com.ziqqi.utils.Constants;
import com.ziqqi.utils.LoginDialog;
import com.ziqqi.utils.PreferenceManager;
import com.ziqqi.utils.SpacesItemDecoration;
import com.ziqqi.utils.Utils;
import com.ziqqi.viewmodel.ViewAllViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static com.facebook.FacebookSdk.getApplicationContext;

public class ViewAllProductsActivity extends AppCompatActivity {
    ActivityViewAllProductsBinding binding;
    ViewAllViewModel viewModel;
    LinearLayoutManager manager;
    SpacesItemDecoration spacesItemDecoration;
    ViewAllProductAdapter adapter;
    OnAllItemClickListener listener;
    List<Payload> payloadList, viewAllList;
    String categoryId;
    int pageCount = 1;
    LoginDialog loginDialog;
    com.ziqqi.addToCartListener addToCartListener;
    BottomSheetBehavior sheetBehavior;
    RelativeLayout layoutBottomSheet;
    RadioButton rb_popular, rb_price_low_to_high, rb_price_high_to_low, rb_name_asc, rb_name_desc;
    BottomSheetDialog mBottomSheetDialog;
    String sortBy = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_view_all_products);
        viewModel = ViewModelProviders.of(this).get(ViewAllViewModel.class);

        Locale locale = new Locale(PreferenceManager.getStringValue(Constants.LANG));
        Configuration config = getBaseContext().getResources().getConfiguration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        binding.executePendingBindings();
        binding.setViewModel(viewModel);

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button);

        layoutBottomSheet = findViewById(R.id.bottom_sheet);
        loginDialog = new LoginDialog();
        payloadList = new ArrayList<>();
        viewAllList = new ArrayList<>();

        sheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);
        sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {
//                        btnBottomSheet.setText("Close Sheet");
                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
//                        btnBottomSheet.setText("Expand Sheet");
                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        mBottomSheetDialog = new BottomSheetDialog(ViewAllProductsActivity.this);
        View sheetView = getLayoutInflater().inflate(R.layout.product_sort_bottom_sheet, null);
        mBottomSheetDialog.setContentView(sheetView);


        rb_popular = sheetView.findViewById(R.id.rb_popular);
        rb_price_low_to_high = sheetView.findViewById(R.id.rb_price_low_to_high);
        rb_price_high_to_low = sheetView.findViewById(R.id.rb_price_high_to_low);
        rb_name_asc = sheetView.findViewById(R.id.rb_name_asc);
        rb_name_desc = sheetView.findViewById(R.id.rb_name_desc);

        if (getIntent().getExtras() != null) {
            categoryId = getIntent().getExtras().getString("categoryId");
        }

        listener = new OnAllItemClickListener() {
            @Override
            public void onItemClick(String id, String type) {
                startActivity(new Intent(ViewAllProductsActivity.this, ProductDetailActivity.class).putExtra("product_id", id));
            }

            @Override
            public void onItemClick(Payload payload, String type) {
                switch (type) {
                    case Constants.SHARE:
                        Utils.share(ViewAllProductsActivity.this, payload.getProductId());
                        break;
                    case Constants.WISH_LIST:
                        if (PreferenceManager.getBoolValue(Constants.LOGGED_IN)){
                            addToWishList(PreferenceManager.getStringValue(Constants.AUTH_TOKEN), payload.getProductId(), PreferenceManager.getStringValue(Constants.GUEST_ID));
                        }else{
                            addToWishList("", payload.getProductId(), PreferenceManager.getStringValue(Constants.GUEST_ID));
                        }
                        break;
                    case Constants.CART:
                        if (PreferenceManager.getBoolValue(Constants.LOGGED_IN)){
                            addToCart(payload.getProductId(), PreferenceManager.getStringValue(Constants.AUTH_TOKEN), PreferenceManager.getStringValue(Constants.GUEST_ID));
                        }else{
                            addToCart(payload.getProductId(), "", PreferenceManager.getStringValue(Constants.GUEST_ID));
                        }

                        break;
                }
            }
        };

        setUpAdapter();

        getData(sortBy);

        binding.recyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                pageCount = 1;
                getData(sortBy);
            }

            @Override
            public void onLoadMore() {
                pageCount++;
                Log.e("PageCOunt ", " " + pageCount);
                getData(sortBy);
            }
        });
        binding.recyclerView.setPullRefreshEnabled(false);

//        sheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);

        binding.rlFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(ViewAllProductsActivity.this, FilterActivity.class).putExtra("catId", categoryId), 100);
            }
        });

        binding.rlSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*SortSheetFragment bottomSheetFragment = new SortSheetFragment();
                bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());*/
                mBottomSheetDialog.show();
            }
        });

        rb_popular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetDialog.dismiss();
                sortBy = "1";
                getData(sortBy);

            }
        });

        rb_price_low_to_high.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetDialog.dismiss();
                sortBy = "2";
                getData(sortBy);
            }
        });

        rb_price_high_to_low.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetDialog.dismiss();
                sortBy = "3";
                getData(sortBy);
            }
        });

        rb_name_asc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetDialog.dismiss();
                sortBy = "4";
                getData(sortBy);
            }
        });

        rb_name_desc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetDialog.dismiss();
                sortBy = "5";
                getData(sortBy);
            }
        });
    }

    private void addToWishList(String authToken, String id, String guest_id) {
        if (ConnectivityHelper.isConnectedToNetwork(this)){
            binding.progressBar.setVisibility(View.VISIBLE);
            viewModel.addProductWishlist(authToken, id, guest_id);
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
        }else{
            Toast.makeText(ViewAllProductsActivity.this,"You're not connected!", Toast.LENGTH_SHORT).show();
        }

    }

    private void addToCart(String id, String authToken, String guest_id) {
        if (ConnectivityHelper.isConnectedToNetwork(this)){
            binding.progressBar.setVisibility(View.VISIBLE);
            viewModel.addToCart(id, authToken, "1" , guest_id);
            viewModel.addToCartResponse().observe(this, new Observer<AddToCart>() {
                @Override
                public void onChanged(@Nullable AddToCart addToCart) {
                    binding.progressBar.setVisibility(View.GONE);
                    if (!addToCart.getError()){
                        Toast.makeText(getApplicationContext(),  addToCart.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else{
            Toast.makeText(ViewAllProductsActivity.this,"You're not connected!", Toast.LENGTH_SHORT).show();
        }

    }


    private void setUpAdapter() {
        manager = new GridLayoutManager(this, 3);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.recyclerView.setLayoutManager(manager);
        adapter = new ViewAllProductAdapter(this, viewAllList, listener);
        binding.recyclerView.setAdapter(adapter);
        spacesItemDecoration = new SpacesItemDecoration(this, R.dimen.dp_4);
        binding.recyclerView.addItemDecoration(spacesItemDecoration);
    }

    private void getData(String sortBy) {
        if (ConnectivityHelper.isConnectedToNetwork(this)) {
//            binding.progressBar.setVisibility(View.VISIBLE);
            Log.e("PageCount ", " " + pageCount);
            viewModel.fetchData(categoryId, String.valueOf(pageCount), sortBy);
            viewModel.getCategoryProduct().observe(this, new Observer<ProductCategory>() {
                @Override
                public void onChanged(@Nullable ProductCategory productCategory) {
                    binding.progressBar.setVisibility(View.GONE);
                    if (!productCategory.getError()) {
                        if (payloadList != null) {
                            viewAllList.clear();
                            adapter.notifyDataSetChanged();
                            payloadList = productCategory.getPayload();

                            if (pageCount == 1) {
                                viewAllList.addAll(payloadList);
                                // adapter.setPayloadList(viewAllList);
                            } else {
                                viewAllList.addAll(payloadList);
                                // adapter.setPayloadList(viewAllList);
                                binding.recyclerView.loadMoreComplete();
                            }

                            // adapter.notifyDataSetChanged();
                        }
                    } else {
                        Utils.ShowToast(ViewAllProductsActivity.this, productCategory.getMessage());
                    }
                }
            });
        } else {
            Snackbar snackbar = Snackbar
                    .make(binding.rlMain, "No Internet Connection", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Try Again", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            getData("");
                        }
                    });

            snackbar.show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
