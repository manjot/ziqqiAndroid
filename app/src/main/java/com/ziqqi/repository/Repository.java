package com.ziqqi.repository;

import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.ziqqi.model.addbillingaddressmodel.AddBillingAddressModel;
import com.ziqqi.model.addshippingaddressmodel.AddShippingAddressModel;
import com.ziqqi.model.addtocart.AddToCart;
import com.ziqqi.model.addtowishlistmodel.AddToModel;
import com.ziqqi.model.bannerimagemodel.BannerImageModel;
import com.ziqqi.model.citymodel.CityResponse;
import com.ziqqi.model.countrymodel.CountryResponse;
import com.ziqqi.model.dealsmodel.DealsResponse;
import com.ziqqi.model.feedbackmastermodel.FeedbackMaster;
import com.ziqqi.model.helpcenterbyidmodel.HelpCenterByIdResponse;
import com.ziqqi.model.helpcentermodel.HelpCenterModel;
import com.ziqqi.model.homecategorymodel.HomeCategoriesResponse;
import com.ziqqi.model.myaddressmodel.ShippingAddressModel;
import com.ziqqi.model.myordersmodel.MyOrdersResponse;
import com.ziqqi.model.placeordermodel.PlaceOrderResponse;
import com.ziqqi.model.productcategorymodel.ProductCategory;
import com.ziqqi.model.productdetailsmodel.ProductDetails;
import com.ziqqi.model.removewislistmodel.DeleteWishlistModel;
import com.ziqqi.model.searchcategorymodel.SearchCategory;
import com.ziqqi.model.searchmodel.SearchResponse;
import com.ziqqi.model.similarproductsmodel.SimilarProduct;
import com.ziqqi.model.viewcartmodel.ViewCartResponse;
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

    public MutableLiveData<ProductDetails> getProductDetails(int id, String authToken) {
        final MutableLiveData<ProductDetails> productDetailsResponse = new MutableLiveData<>();
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ProductDetails> call = apiInterface.productDetails(id, authToken);
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

    public MutableLiveData<DeleteWishlistModel> removeWishlist(String authToken, String id) {
        final MutableLiveData<DeleteWishlistModel> addwishlist = new MutableLiveData<>();
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<DeleteWishlistModel> call = apiInterface.removeWishlist(authToken, id);
        call.enqueue(new Callback<DeleteWishlistModel>() {
            @Override
            public void onResponse(Call<DeleteWishlistModel> call, Response<DeleteWishlistModel> response) {
                addwishlist.setValue(response.body());
            }

            @Override
            public void onFailure(Call<DeleteWishlistModel> call, Throwable t) {
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

    public MutableLiveData<AddToCart> addToCart(String productId, String authToken, String quantity) {
        final MutableLiveData<AddToCart> addToCart = new MutableLiveData<>();
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<AddToCart> call = apiInterface.addToCart(productId, authToken, quantity);
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

    public MutableLiveData<ViewCartResponse> viewCart(String authToken) {
        final MutableLiveData<ViewCartResponse> getCart = new MutableLiveData<>();
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ViewCartResponse> call = apiInterface.fetchCart(authToken);
        call.enqueue(new Callback<ViewCartResponse>() {
            @Override
            public void onResponse(Call<ViewCartResponse> call, Response<ViewCartResponse> response) {
                getCart.setValue(response.body());
            }

            @Override
            public void onFailure(Call<ViewCartResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
        return getCart;
    }

    public MutableLiveData<DealsResponse> getDeals(int page) {
        final MutableLiveData<DealsResponse> fetchDeals = new MutableLiveData<>();
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<DealsResponse> call = apiInterface.getDeals(page);
        call.enqueue(new Callback<DealsResponse>() {
            @Override
            public void onResponse(Call<DealsResponse> call, Response<DealsResponse> response) {
                fetchDeals.setValue(response.body());
            }

            @Override
            public void onFailure(Call<DealsResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
        return fetchDeals;
    }

    public MutableLiveData<HelpCenterByIdResponse> getHelpById(int helpId) {
        final MutableLiveData<HelpCenterByIdResponse> fetchHelps = new MutableLiveData<>();
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<HelpCenterByIdResponse> call = apiInterface.getHelpById(helpId);
        call.enqueue(new Callback<HelpCenterByIdResponse>() {
            @Override
            public void onResponse(Call<HelpCenterByIdResponse> call, Response<HelpCenterByIdResponse> response) {
                fetchHelps.setValue(response.body());
            }

            @Override
            public void onFailure(Call<HelpCenterByIdResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
        return fetchHelps;
    }

    public MutableLiveData<AddBillingAddressModel> addBillingAddress(String authToken, String firstName, String lastName, String mobile, String address, String county) {
        final MutableLiveData<AddBillingAddressModel> addBilling = new MutableLiveData<>();
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<AddBillingAddressModel> call = apiInterface.addBillingAddress(authToken, firstName, lastName, mobile, address, county);
        call.enqueue(new Callback<AddBillingAddressModel>() {
            @Override
            public void onResponse(Call<AddBillingAddressModel> call, Response<AddBillingAddressModel> response) {
                addBilling.setValue(response.body());
            }

            @Override
            public void onFailure(Call<AddBillingAddressModel> call, Throwable t) {
                t.printStackTrace();
            }
        });
        return addBilling;
    }

    public MutableLiveData<AddShippingAddressModel> addShippingAddress(String authToken, String name, String mobile, String country, String city, String location, String address) {
        final MutableLiveData<AddShippingAddressModel> addBilling = new MutableLiveData<>();
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<AddShippingAddressModel> call = apiInterface.addShippingAddress(authToken, name, mobile, country, city, location, address);
        call.enqueue(new Callback<AddShippingAddressModel>() {
            @Override
            public void onResponse(Call<AddShippingAddressModel> call, Response<AddShippingAddressModel> response) {
                addBilling.setValue(response.body());
            }

            @Override
            public void onFailure(Call<AddShippingAddressModel> call, Throwable t) {
                t.printStackTrace();
            }
        });
        return addBilling;
    }


    public MutableLiveData<PlaceOrderResponse> placeOrder(String authToken, String billingFname, String billingLname, String billingMobile, String pickupName, String pickupMobile, String pickupCountry, String pickup_city, String pickup_location, String pickup_address) {
        final MutableLiveData<PlaceOrderResponse> placingOrder = new MutableLiveData<>();
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<PlaceOrderResponse> call = apiInterface.placeOrder(authToken, billingFname, billingLname, billingMobile, pickupName, pickupMobile, pickupCountry, pickup_city, pickup_location,  pickup_address);
        call.enqueue(new Callback<PlaceOrderResponse>() {
            @Override
            public void onResponse(Call<PlaceOrderResponse> call, Response<PlaceOrderResponse> response) {
                placingOrder.setValue(response.body());
            }

            @Override
            public void onFailure(Call<PlaceOrderResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
        return placingOrder;
    }

    public MutableLiveData<MyOrdersResponse> getOrder(String authToken) {
        final MutableLiveData<MyOrdersResponse> placingOrder = new MutableLiveData<>();
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<MyOrdersResponse> call = apiInterface.getOrder(authToken);
        call.enqueue(new Callback<MyOrdersResponse>() {
            @Override
            public void onResponse(Call<MyOrdersResponse> call, Response<MyOrdersResponse> response) {
                placingOrder.setValue(response.body());
            }

            @Override
            public void onFailure(Call<MyOrdersResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
        return placingOrder;
    }

    public MutableLiveData<CountryResponse> getCountries() {
        final MutableLiveData<CountryResponse> countries = new MutableLiveData<>();
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<CountryResponse> call = apiInterface.getCountry();
        call.enqueue(new Callback<CountryResponse>() {
            @Override
            public void onResponse(Call<CountryResponse> call, Response<CountryResponse> response) {
                countries.setValue(response.body());
            }

            @Override
            public void onFailure(Call<CountryResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
        return countries;
    }

    public MutableLiveData<CityResponse> getCities(String country_id) {
        final MutableLiveData<CityResponse> countries = new MutableLiveData<>();
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<CityResponse> call = apiInterface.getCity(country_id);
        call.enqueue(new Callback<CityResponse>() {
            @Override
            public void onResponse(Call<CityResponse> call, Response<CityResponse> response) {
                countries.setValue(response.body());
            }

            @Override
            public void onFailure(Call<CityResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
        return countries;
    }
}
