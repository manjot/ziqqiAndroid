package com.ziqqi.retrofit;

import com.ziqqi.model.VerifyOtpResponse;
import com.ziqqi.model.addtocart.AddToCart;
import com.ziqqi.model.addtowishlistmodel.AddToModel;
import com.ziqqi.model.bannerimagemodel.BannerImageModel;
import com.ziqqi.model.feedbackmastermodel.FeedbackMaster;
import com.ziqqi.model.helpcentermodel.HelpCenterModel;
import com.ziqqi.model.homecategorymodel.HomeCategoriesResponse;
import com.ziqqi.model.languagemodel.LanguageModel;
import com.ziqqi.model.loginResponse.LoginResponse;
import com.ziqqi.model.myaddressmodel.ShippingAddressModel;
import com.ziqqi.model.productcategorymodel.ProductCategory;
import com.ziqqi.model.productdetailsmodel.ProductDetails;
import com.ziqqi.model.searchcategorymodel.SearchCategory;
import com.ziqqi.model.searchmodel.SearchResponse;
import com.ziqqi.model.signup.SignUpResponse;
import com.ziqqi.model.similarproductsmodel.SimilarProduct;
import com.ziqqi.model.viewwishlistmodel.ViewWishlist;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiInterface {
    @GET("language_master")
    Call<LanguageModel> getLanguageData();

    @GET("home_banners")
    Call<BannerImageModel> getHomeBanners();

    @GET("getHelpCenters")
    Call<HelpCenterModel> getHelps();

    @GET("feedback_master")
    Call<FeedbackMaster> getFeedbackMaster();

    @FormUrlEncoded
    @POST("socialSignup")
    Call<LoginResponse> getLogin(@FieldMap HashMap<String, Object> socialLoginRequest);

    @FormUrlEncoded
    @POST("login")
    Call<LoginResponse> login(@FieldMap HashMap<String, Object> loginRequest);


    @FormUrlEncoded
    @POST("signup")
    Call<SignUpResponse> getSignUpData(@FieldMap HashMap<String, String> socialLoginRequest);

    @POST("home_categories")
    Call<HomeCategoriesResponse> getHomeCategories();

    @FormUrlEncoded
    @POST("home_categories")
    Call<HomeCategoriesResponse> getSubCategories(@Field("category_id") String categoryId);

    @FormUrlEncoded
    @POST("homesearch")
    Call<SearchResponse> getSearch(@Field("category_id") String categoryId, @Field("page") String page);

    @FormUrlEncoded
    @POST("forgot_password")
    Call<String> getPassword(@Field("email") String email);

    @FormUrlEncoded
    @POST("verifyotp")
    Call<VerifyOtpResponse> verifyOtp(@Field("customer_id") int id, @Field("otp") int otp);

    @FormUrlEncoded
    @POST("getcategoryProduct")
    Call<ProductCategory> getCategoryProduct(@Field("category_id") String id, @Field("page") String page);

    @FormUrlEncoded
    @POST("productDetails")
    Call<ProductDetails> productDetails(@Field("product_id") int id);


    @FormUrlEncoded
    @POST("similar_products")
    Call<SimilarProduct> similarProducts(@Field("product_id") int id);

    @FormUrlEncoded
    @POST("addTowishlist")
    Call<AddToModel> addToWishlist(@Field("auth_token") String auth_token, @Field("product_id") String product_id);

    @FormUrlEncoded
    @POST("veiwWishlistProduct")
    Call<ViewWishlist> viewWishlist(@Field("auth_token") String auth_token);

    @FormUrlEncoded
    @POST("categorysearch")
    Call<SearchCategory> getSearchCategory(@Field("searchname") String searchname);

    @FormUrlEncoded
    @POST("getShippingAddress")
    Call<ShippingAddressModel> shippingAddress(@Field("auth_token") String auth_token);

    @POST("addTocart")
    Call<AddToCart> addToCart(@Field("product_id") String productId, @Field("customer_id") String customerId, @Field("product_variant_id") String productVariantId, @Field("quantity") String quantity);
}
