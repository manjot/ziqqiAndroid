package com.ziqqi.repository;

import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.ziqqi.model.addtocart.AddToCart;
import com.ziqqi.model.addtowishlistmodel.AddToModel;
import com.ziqqi.model.bannerimagemodel.BannerImageModel;
import com.ziqqi.model.feedbackmastermodel.FeedbackMaster;
import com.ziqqi.model.helpcentermodel.HelpCenterModel;
import com.ziqqi.model.homecategorymodel.HomeCategoriesResponse;
import com.ziqqi.model.myaddressmodel.ShippingAddressModel;
import com.ziqqi.model.productcategorymodel.ProductCategory;
import com.ziqqi.model.productdetailsmodel.ProductDetails;
import com.ziqqi.model.searchcategorymodel.SearchCategory;
import com.ziqqi.model.searchmodel.SearchResponse;
import com.ziqqi.model.similarproductsmodel.SimilarProduct;
import com.ziqqi.model.viewwishlistmodel.ViewWishlist;
import com.ziqqi.retrofit.ApiClient;
import com.ziqqi.retrofit.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Repository {
    ApiInterface apiInterface;

    public Repository() {

    }


    public MutableLiveData<BannerImageModel> getHomeBanners() {
        final MutableLiveData<BannerImageModel> bannerImageModel = new MutableLiveData<>();
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<BannerImageModel> call = apiInterface.getHomeBanners();
        call.enqueue(new Callback<BannerImageModel>() {
            @Override
            public void onResponse(Call<BannerImageModel> call, Response<BannerImageModel> response) {
                if (response.body() != null) {
                    bannerImageModel.setValue(response.body());
                    Log.e("LanguageData ", response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<BannerImageModel> call, Throwable t) {

            }
        });

        return bannerImageModel;
    }

    public MutableLiveData<HomeCategoriesResponse> getHomeCategories() {
        final MutableLiveData<HomeCategoriesResponse> homeCategoriesResponse = new MutableLiveData<>();
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<HomeCategoriesResponse> call = apiInterface.getHomeCategories();
        call.enqueue(new Callback<HomeCategoriesResponse>() {
            @Override
            public void onResponse(Call<HomeCategoriesResponse> call, Response<HomeCategoriesResponse> response) {
                if (response.body() != null) {
                    homeCategoriesResponse.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<HomeCategoriesResponse> call, Throwable t) {

            }
        });
        return homeCategoriesResponse;
    }

    public MutableLiveData<HomeCategoriesResponse> getSubCategories(String categoryId) {
        final MutableLiveData<HomeCategoriesResponse> subCategoriesMutableLiveData = new MutableLiveData<>();
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<HomeCategoriesResponse> call = apiInterface.getSubCategories(categoryId);
        call.enqueue(new Callback<HomeCategoriesResponse>() {
            @Override
            public void onResponse(Call<HomeCategoriesResponse> call, Response<HomeCategoriesResponse> response) {
                subCategoriesMutableLiveData.postValue(response.body());
            }

            @Override
            public void onFailure(Call<HomeCategoriesResponse> call, Throwable t) {

            }
        });
        return subCategoriesMutableLiveData;
    }

    public MutableLiveData<SearchResponse> getSearch(String categoryId, String page) {
        final MutableLiveData<SearchResponse> searchResponse = new MutableLiveData<>();
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<SearchResponse> call = apiInterface.getSearch(categoryId, page );
        call.enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                searchResponse.setValue(response.body());
            }

            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
        return searchResponse;
    }

    public MutableLiveData<ProductCategory> getCategoryProducts(String id, String page) {
        final MutableLiveData<ProductCategory> searchResponse = new MutableLiveData<>();
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ProductCategory> call = apiInterface.getCategoryProduct(id, page);
        call.enqueue(new Callback<ProductCategory>() {
            @Override
            public void onResponse(Call<ProductCategory> call, Response<ProductCategory> response) {
                searchResponse.setValue(response.body());
            }

            @Override
            public void onFailure(Call<ProductCategory> call, Throwable t) {
                t.printStackTrace();
            }
        });
        return searchResponse;
    }

    public MutableLiveData<ProductDetails> getProductDetails(int id) {
        final MutableLiveData<ProductDetails> productDetailsResponse = new MutableLiveData<>();
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ProductDetails> call = apiInterface.productDetails(id);
        call.enqueue(new Callback<ProductDetails>() {
            @Override
            public void onResponse(Call<ProductDetails> call, Response<ProductDetails> response) {
                productDetailsResponse.setValue(response.body());
            }

            @Override
            public void onFailure(Call<ProductDetails> call, Throwable t) {
                t.printStackTrace();
            }
        });
        return productDetailsResponse;
    }

    public MutableLiveData<SimilarProduct> getSimilarProduct(int id) {
        final MutableLiveData<SimilarProduct> similarProductsResponse = new MutableLiveData<>();
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<SimilarProduct> call = apiInterface.similarProducts(id);
        call.enqueue(new Callback<SimilarProduct>() {
            @Override
            public void onResponse(Call<SimilarProduct> call, Response<SimilarProduct> response) {
                similarProductsResponse.setValue(response.body());
            }

            @Override
            public void onFailure(Call<SimilarProduct> call, Throwable t) {
                t.printStackTrace();
            }
        });
        return similarProductsResponse;
    }

    public MutableLiveData<AddToModel> addToWishlist(String authToken, String id) {
        final MutableLiveData<AddToModel> addwishlist = new MutableLiveData<>();
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<AddToModel> call = apiInterface.addToWishlist(authToken, id);
        call.enqueue(new Callback<AddToModel>() {
            @Override
            public void onResponse(Call<AddToModel> call, Response<AddToModel> response) {
                addwishlist.setValue(response.body());
            }

            @Override
            public void onFailure(Call<AddToModel> call, Throwable t) {
                t.printStackTrace();
            }
        });
        return addwishlist;
    }

    public MutableLiveData<ViewWishlist> fetchWishlist(String authToken) {
        final MutableLiveData<ViewWishlist> viewWishlist = new MutableLiveData<>();
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ViewWishlist> call = apiInterface.viewWishlist(authToken);
        call.enqueue(new Callback<ViewWishlist>() {
            @Override
            public void onResponse(Call<ViewWishlist> call, Response<ViewWishlist> response) {
                viewWishlist.setValue(response.body());
            }

            @Override
            public void onFailure(Call<ViewWishlist> call, Throwable t) {
                t.printStackTrace();
            }
        });
        return viewWishlist;
    }

    public MutableLiveData<SearchCategory> getSearchCategory(String searchName) {
        final MutableLiveData<SearchCategory> getCategories = new MutableLiveData<>();
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<SearchCategory> call = apiInterface.getSearchCategory(searchName);
        call.enqueue(new Callback<SearchCategory>() {
            @Override
            public void onResponse(Call<SearchCategory> call, Response<SearchCategory> response) {
                getCategories.setValue(response.body());
            }

            @Override
            public void onFailure(Call<SearchCategory> call, Throwable t) {
                t.printStackTrace();
            }
        });
        return getCategories;
    }

    public MutableLiveData<HelpCenterModel> getHelpCenter() {
        final MutableLiveData<HelpCenterModel> getHelps = new MutableLiveData<>();
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<HelpCenterModel> call = apiInterface.getHelps();
        call.enqueue(new Callback<HelpCenterModel>() {
            @Override
            public void onResponse(Call<HelpCenterModel> call, Response<HelpCenterModel> response) {
                getHelps.setValue(response.body());
            }

            @Override
            public void onFailure(Call<HelpCenterModel> call, Throwable t) {
                t.printStackTrace();
            }
        });
        return getHelps;
    }

    public MutableLiveData<FeedbackMaster> getFeedbackMaster() {
        final MutableLiveData<FeedbackMaster> getFeedbacks = new MutableLiveData<>();
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<FeedbackMaster> call = apiInterface.getFeedbackMaster();
        call.enqueue(new Callback<FeedbackMaster>() {
            @Override
            public void onResponse(Call<FeedbackMaster> call, Response<FeedbackMaster> response) {
                getFeedbacks.setValue(response.body());
            }

            @Override
            public void onFailure(Call<FeedbackMaster> call, Throwable t) {
                t.printStackTrace();
            }
        });
        return getFeedbacks;
    }

    public MutableLiveData<ShippingAddressModel> getShippingAddress(String authToken) {
        final MutableLiveData<ShippingAddressModel> getShipAddress = new MutableLiveData<>();
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ShippingAddressModel> call = apiInterface.shippingAddress(authToken);
        call.enqueue(new Callback<ShippingAddressModel>() {
            @Override
            public void onResponse(Call<ShippingAddressModel> call, Response<ShippingAddressModel> response) {
                getShipAddress.setValue(response.body());
            }

            @Override
            public void onFailure(Call<ShippingAddressModel> call, Throwable t) {
                t.printStackTrace();
            }
        });
        return getShipAddress;
    }

    public MutableLiveData<AddToCart> addToCart(String productId, String customerId, String productVariantId, String quantity) {
        final MutableLiveData<AddToCart> addToCart = new MutableLiveData<>();
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<AddToCart> call = apiInterface.addToCart(productId, customerId, productVariantId, quantity);
        call.enqueue(new Callback<AddToCart>() {
            @Override
            public void onResponse(Call<AddToCart> call, Response<AddToCart> response) {
                addToCart.setValue(response.body());
            }

            @Override
            public void onFailure(Call<AddToCart> call, Throwable t) {
                t.printStackTrace();
            }
        });
        return addToCart;
    }
}
