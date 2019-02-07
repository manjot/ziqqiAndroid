package com.ziqqi.retrofit;

import com.ziqqi.model.VerifyOtpResponse;
import com.ziqqi.model.bannerimagemodel.BannerImageModel;
import com.ziqqi.model.homecategorymodel.HomeCategoriesResponse;
import com.ziqqi.model.languagemodel.LanguageModel;
import com.ziqqi.model.loginResponse.LoginResponse;
import com.ziqqi.model.productcategorymodel.ProductCategory;
import com.ziqqi.model.searchmodel.SearchResponse;
import com.ziqqi.model.signup.SignUpResponse;

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
    Call<SearchResponse> getSearch(@Field("productname") String productName);

    @FormUrlEncoded
    @POST("forgot_password")
    Call<String> getPassword(@Field("email") String email);

    @FormUrlEncoded
    @POST("verifyotp")
    Call<VerifyOtpResponse> verifyOtp(@Field("customer_id") int id, @Field("otp") int otp);

    @FormUrlEncoded
    @POST("getcategoryProduct")
    Call<ProductCategory> getCategoryProduct(@Field("category_id") String id, @Field("page") String page);

}
