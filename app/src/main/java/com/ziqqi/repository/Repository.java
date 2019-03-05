package com.ziqqi.repository;

import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.ziqqi.model.bannerimagemodel.BannerImageModel;
import com.ziqqi.model.homecategorymodel.HomeCategoriesResponse;
import com.ziqqi.model.productcategorymodel.ProductCategory;
import com.ziqqi.model.productdetailsmodel.ProductDetails;
import com.ziqqi.model.searchmodel.SearchResponse;
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

    public MutableLiveData<SearchResponse> getSearch(String productName) {
        final MutableLiveData<SearchResponse> searchResponse = new MutableLiveData<>();
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<SearchResponse> call = apiInterface.getSearch(productName);
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

}
