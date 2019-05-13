package com.ziqqi.retrofit;

import com.ziqqi.model.filtermodel.FilterResponse;
import com.ziqqi.model.filterproductmodel.FilterCategoriesResponse;
import com.ziqqi.model.uploadphotomodel.UploadPhoto;
import com.ziqqi.model.verifyotpmodel.VerifyOtpResponse;
import com.ziqqi.model.addbillingaddressmodel.AddBillingAddressModel;
import com.ziqqi.model.addshippingaddressmodel.AddShippingAddressModel;
import com.ziqqi.model.addtocart.AddToCart;
import com.ziqqi.model.addtowishlistmodel.AddToModel;
import com.ziqqi.model.applycouponmodel.ApplyCouponModel;
import com.ziqqi.model.bannerimagemodel.BannerImageModel;
import com.ziqqi.model.changequantitymodel.ChangeQuantityResponse;
import com.ziqqi.model.citymodel.CityResponse;
import com.ziqqi.model.countrymodel.CountryResponse;
import com.ziqqi.model.dealsmodel.DealsResponse;
import com.ziqqi.model.deletecartmodel.DeleteCartResponse;
import com.ziqqi.model.feedbackmastermodel.FeedbackMaster;
import com.ziqqi.model.forgotpasswordmodel.ForgotPasswordResponse;
import com.ziqqi.model.getbillingaddressmodel.BillingAddressModel;
import com.ziqqi.model.helpcenterbyidmodel.HelpCenterByIdResponse;
import com.ziqqi.model.helpcentermodel.HelpCenterModel;
import com.ziqqi.model.homecategorymodel.HomeCategoriesResponse;
import com.ziqqi.model.languagemodel.LanguageModel;
import com.ziqqi.model.loginResponse.LoginResponse;
import com.ziqqi.model.myaddressmodel.ShippingAddressModel;
import com.ziqqi.model.myordersmodel.MyOrdersResponse;
import com.ziqqi.model.ordertrackingmodel.OrderTrackingResponse;
import com.ziqqi.model.placeordermodel.PlaceOrderResponse;
import com.ziqqi.model.productcategorymodel.ProductCategory;
import com.ziqqi.model.productdetailsmodel.ProductDetails;
import com.ziqqi.model.removewislistmodel.DeleteWishlistModel;
import com.ziqqi.model.resendotpmodel;
import com.ziqqi.model.searchcategorymodel.SearchCategory;
import com.ziqqi.model.searchmodel.SearchResponse;
import com.ziqqi.model.signup.SignUpResponse;
import com.ziqqi.model.similarproductsmodel.SimilarProduct;
import com.ziqqi.model.viewcartmodel.ViewCartResponse;
import com.ziqqi.model.viewwishlistmodel.ViewWishlist;

import java.util.HashMap;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

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
    Call<String> getPassword(@Field("email") String email, @Field("otp_method") String otp_method);

    @FormUrlEncoded
    @POST("forgot_password")
    Call<String> changedPassword(@Field("email") String email, @Field("otp") String otp, @Field("new_password") String new_password);

    @FormUrlEncoded
    @POST("verifyotp")
    Call<VerifyOtpResponse> verifyOtp(@Field("customer_id") String id, @Field("otp") String otp);

    @FormUrlEncoded
    @POST("getcategoryProduct")
    Call<ProductCategory> getCategoryProduct(@Field("category_id") String id,
                                             @Field("brand_id") String brand_id,
                                             @Field("min_price") String min_price,
                                             @Field("max_price") String max_price,
                                             @Field("attribute_id") String attribute_id,
                                             @Field("feature_id") String feature_id,
                                             @Field("page") String page,
                                             @Field("sort") String sort );

    @FormUrlEncoded
    @POST("productDetails")
    Call<ProductDetails> productDetails(@Field("product_id") int id, @Field("auth_token") String auth_token);


    @FormUrlEncoded
    @POST("similar_products")
    Call<SimilarProduct> similarProducts(@Field("product_id") int id);

    @FormUrlEncoded
    @POST("addTowishlist")
    Call<AddToModel> addToWishlist(@Field("auth_token") String auth_token, @Field("product_id") String product_id, @Field("guest_id") String guest_id);

    @FormUrlEncoded
    @POST("deleteWishlistProduct")
    Call<DeleteWishlistModel> removeWishlist(@Field("auth_token") String auth_token, @Field("product_id") String product_id, @Field("guest_id") String guest_id);

    @FormUrlEncoded
    @POST("veiwWishlistProduct")
    Call<ViewWishlist> viewWishlist(@Field("auth_token") String auth_token, @Field("guest_id") String guest_id);

    @FormUrlEncoded
    @POST("categorysearch")
    Call<SearchCategory> getSearchCategory(@Field("searchname") String searchname);

    @FormUrlEncoded
    @POST("getShippingAddress")
    Call<ShippingAddressModel> shippingAddress(@Field("auth_token") String auth_token);

    @FormUrlEncoded
    @POST("viewCartProduct")
    Call<ViewCartResponse> fetchCart(@Field("auth_token") String auth_token, @Field("guest_id") String guest_id);

    @FormUrlEncoded
    @POST("deleteCartProduct")
    Call<DeleteCartResponse> deleteCart(@Field("auth_token") String auth_token, @Field("guest_id") String guest_id, @Field("product_id") String product_id);

    @FormUrlEncoded
    @POST("addTocart")
    Call<AddToCart> addToCart(@Field("product_id") String productId, @Field("auth_token") String auth_token, @Field("quantity") String quantity, @Field("guest_id") String guest_id);

    @FormUrlEncoded
    @POST("deals")
    Call<DealsResponse> getDeals(@Field("page") int page);

    @FormUrlEncoded
    @POST("getHelpCenterById")
    Call<HelpCenterByIdResponse> getHelpById(@Field("help_id") int help_id);

    @FormUrlEncoded
    @POST("addBillingAddress")
    Call<AddBillingAddressModel> addBillingAddress(@Field("auth_token") String auth_token,
                                                   @Field("first_name") String first_name,
                                                   @Field("last_name") String last_name,
                                                   @Field("mobile") String mobile,
                                                   @Field("country") String country,
                                                   @Field("city") String city,
                                                   @Field("location") String location,
                                                   @Field("address") String address);
    @FormUrlEncoded
    @POST("addShippingAddress")
    Call<AddShippingAddressModel> addShippingAddress(@Field("auth_token") String auth_token,
                                                     @Field("name") String name,
                                                     @Field("mobile") String mobile,
                                                     @Field("country") String country,
                                                     @Field("city") String city,
                                                     @Field("location") String location,
                                                     @Field("address") String address);

    @FormUrlEncoded
    @POST("placeOrder")
    Call<PlaceOrderResponse> placeOrder(@Field("auth_token") String auth_token,
                                        @Field("guest_id") String guest_id,
                                        @Field("payment_method") String payment_method,
                                        @Field("order_status") String order_status,
                                        @Field("payment_status") String payment_status,
                                        @Field("transaction_id") String transaction_id,
                                        @Field("wallet_mobile_no") String wallet_mobile_no,
                                        @Field("billing_fname") String billing_fname,
                                        @Field("billing_lname") String billing_lname,
                                        @Field("billing_mobile") String billing_mobile,
                                        @Field("pickup_name") String pickup_name,
                                        @Field("pickup_mobile") String pickup_mobile,
                                        @Field("pickup_city") String pickup_city,
                                        @Field("pickup_country") String pickup_country,
                                        @Field("pickup_location") String pickup_location,
                                        @Field("pickup_address") String pickup_address,
                                        @Field("payment_currency") String payment_currency);

    @FormUrlEncoded
    @POST("getMyOrders")
    Call<MyOrdersResponse> getOrder(@Field("auth_token") String auth_token);

    @GET("country_master")
    Call<CountryResponse> getCountry();

    @FormUrlEncoded
    @POST("getCity")
    Call<CityResponse> getCity(@Field("country_id") String country_id);

    @FormUrlEncoded
    @POST("getBillingAddress")
    Call<BillingAddressModel> getBillingAddress(@Field("auth_token") String auth_token);

    @FormUrlEncoded
    @POST("orderTracking")
    Call<OrderTrackingResponse> getOrderTracking(@Field("auth_token") String auth_token);

    @FormUrlEncoded
    @POST("change_cart_quantity")
    Call<ChangeQuantityResponse> changeCartQuantity(@Field("auth_token") String auth_token, @Field("guest_id") String guest_id, @Field("product_id") String product_id, @Field("type") String type);

    @FormUrlEncoded
    @POST("forgot_password")
    Call<ForgotPasswordResponse> forgotPassword(@Field("email") String email, @Field("otp") String otp, @Field("new_password") String new_password);

    @FormUrlEncoded
    @POST("resendotp")
    Call<resendotpmodel> resendOtp(@Field("customer_id") String customer_id);

    @FormUrlEncoded
    @POST("applyCoupon")
    Call<ApplyCouponModel> applyCoupon(@Field("auth_token") String auth_token, @Field("coupon_code") String coupon_code, @Field("guest_id") String guest_id);

    @FormUrlEncoded
    @POST("categoryFilter")
    Call<FilterCategoriesResponse> getCategoryFilter(@Field("menu_id") String menu_id);


    @Multipart
    @POST("userFileUpload")
    Call<UploadPhoto> addPhoto(@Part("auth_token") RequestBody auth_token, @Part MultipartBody.Part filename);

    @FormUrlEncoded
    @POST("masterFilter")
    Call<FilterResponse> getMasterFilter(@Field("category_id") String category_id);
}
